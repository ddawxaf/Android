<?php
session_start();
header("Expires: Mon, 26 Jul 1970 00:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");          
header("Cache-Control: no-cache, must-revalidate");      
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");

include "../include/address.mem.php";
//echo "<script>if(self == top) parent.location='".BROWSER_IP."'</script>\n";
require ("../include/config.inc.php");
require_once("../../../../common/sportCenterData.php");
require ("../include/define_function_list.inc.php");
require ("../include/curl_http.php");

//接收传递过来的参数：其中赔率和位置需要进行判断
$uid=(isset($_REQUEST['uid']) && $_REQUEST['uid'])? $_REQUEST['uid'] :$_SESSION['Oid'];
$langx=$_SESSION['langx'];
$gold=$_REQUEST['gold'];    // 交易金额
$active=$_REQUEST['active'];   //1 足球滚球、今日赛事, 11 足球早餐\r\n2 篮球滚球、今日赛事, 22 篮球早餐
$strong=$_REQUEST['strong'];
$line=$_REQUEST['line_type']; //不同玩法
$gid=$_REQUEST['gid'];
$type=$_REQUEST['type'];
$wtype=$_REQUEST['wtype'];
$rtype=$_REQUEST['rtype'];
$gnum=$_REQUEST['gnum'];
$ioradio_pd=$_REQUEST['ioradio_pd'];
$ioradio_f=$_REQUEST['ioradio_f'];
$odd_f_type=$_REQUEST['odd_f_type'];
$play_Source = 22 ; //'投注来源:0未知,1pc旧版,2pc新版,3苹果,4安卓,22 综合版',

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" ) {
	echo "<script>window.open('".BROWSER_IP."/tpl/logout_warn.html','_top')</script>";
	exit;
}

require ("../include/traditional.$langx.inc.php");

$token=$_REQUEST['token'];
if($token == $_SESSION['bet_token']){ // 防止重复订单
  echo resubmitAction() ;
  exit();
}else{
    $_SESSION['bet_token'] = $token ;
}
$Sign=$m_place='';
//下注时的赔率：应该根据盘口进行转换后，与数据库中的赔率进行比较。若不相同，返回下注。
$sql = "select Money from ".DBPREFIX.MEMBERTABLE." where Oid='$uid' and Status=0";
$result = mysqli_query($dbLink,$sql);
$memrow = mysqli_fetch_assoc($result);
$open=$_SESSION['OpenType'];
$pay_type =$_SESSION['Pay_Type'];
$memname=$_SESSION['UserName'];
$agents=trim($_SESSION['Agents']);
$world=$_SESSION['World'];
$corprator=$_SESSION['Corprator'];
$super=$_SESSION['Super'];
$admin=$_SESSION['Admin'];
$w_ratio=$_SESSION['ratio'];
$HMoney=$memrow['Money'];
if($HMoney < $gold || $gold<10 || $HMoney<=0){
	echo attention("$User_insufficient_balance",$uid,$langx);
	exit();
}
$w_current=$_SESSION['CurType'];
$memid= $_SESSION['userid'];
$test_flag=$_SESSION['test_flag'];

if($gid%2 == 1){
    $mysqlL = "select `MID` from `".DBPREFIX.SPORT_FLUSH_MATCH_TABLE."` where `M_Start`>now() and `MID`=$gid and Cancel!=1 and Open=1 and MB_Team!=''";
}else{
    $mysqlL = "select `MID` from `".DBPREFIX.SPORT_FLUSH_MATCH_TABLE."` where `M_Start`>now() and `MID`=$gid and Cancel!=1 and Open=1 and MB_Team!=''";
}
$resultL = mysqli_query($dbLink,$mysqlL);
$couL=mysqli_num_rows($resultL);
if($couL==0){
    echo attention("$Order_This_match_is_closed_Please_try_again", $uid, $langx);
    exit;
}

if($gid%2==0){
	$mysql = "select * from `".DATAHGPREFIX.SPORT_FLUSH_MATCH_TABLE."`  where `M_Start`>now() and `MID`=$gid and Open=1 and MB_Team!=''";//判断此赛程是否已经关闭：取出此场次信息
}elseif($gid%2==1){
	//$mysql = "select * from `".DATAHGPREFIX.SPORT_FLUSH_MATCH_TABLE."`  where `M_Start`>now() and `MID` = $gid-1 and Open=1 and MB_Team!=''";//判断此赛程是否已经关闭：取出此场次信息
    $mysql = "select * from `".DATAHGPREFIX.SPORT_FLUSH_MATCH_TABLE."`  where `M_Start`>now() and `MID` = $gid and Open=1 and MB_Team!=''";//判断此赛程是否已经关闭：取出此场次信息
}
$result = mysqli_query($dbCenterMasterDbLink,$mysql);
$cou=mysqli_num_rows($result);
$row = mysqli_fetch_assoc($result);

// -------------------------------------------------------更多玩法刷水 Start
$redisObj = new Ciredis();
if(isset($_REQUEST['dataSou']) && $_REQUEST['dataSou']=="interface") {
    $gtype = 'FT';
    $showtype = $row["Type"];
    $midLockSet = '';
    $midLockCheck = mysqli_query($dbLink, "select MID from " . DBPREFIX . "match_sports_more_midlock where `MID` = $gid");
    $cou = mysqli_num_rows($midLockCheck);
    if ($cou == 0) $midLockSet = mysqli_query($dbMasterLink, "INSERT INTO " . DBPREFIX . "match_sports_more_midlock(`MID`)VALUES({$gid})");
    if ($midLockSet || $cou == 1) {
        $valReflushTime = $redisObj->getSimpleOne($gid . "_reflush_time");
        if ($valReflushTime) { //存在赛事,更新数据库，redis
            if ($showtype == "RB") {
                $reflushTime = 5;
            } elseif ($showtype == "FU") {
                $reflushTime = 10;
            } else {
                $reflushTime = 20;
            }
            if (time() - $valReflushTime > $reflushTime) { //数据过期,重新抓取更新数据库,redis
                //echo "out date re get<br/>";
                $begin = mysqli_query($dbMasterLink, "start transaction");//开启事务$from
                $lockMid = mysqli_query($dbMasterLink, "select MID from " . DBPREFIX . "match_sports_more_midlock where `MID` = $gid for update");
                $valReflushTime1 = $redisObj->getSimpleOne($gid . "_reflush_time");
                if (time() - $valReflushTime1 > $reflushTime) {
                    if ($begin && $lockMid->num_rows == 1) {
                        $dataNew = getDataFromInterface($langx, $gtype, $showtype, $gid);
                        if ($dataNew['tmp_Obj'] && count($dataNew['tmp_Obj']) > 0 && $dataNew['gid_ary'] && count($dataNew['gid_ary']) > 0) {
                            $tmp_Obj = $dataNew['tmp_Obj'];
                            $gid_ary = $dataNew['gid_ary'];
                            $updateSt = $redisObj->getSET($gid . "_reflush_time", time());
                            if ($updateSt) {
                                $details = json_encode($tmp_Obj,JSON_UNESCAPED_UNICODE);
                                $details = str_replace('\'', '', $details);
                                $setGames = mysqli_query($dbMasterLink, "replace into " . DBPREFIX . "match_sports_more(`MID`,`details`)VALUES($gid,'$details')");
                                if ($setGames) {
                                    $comStatus = mysqli_query($dbMasterLink, "COMMIT");
                                    if ($comStatus) {
                                        $redisObj->getSET("gameMore_" . $gid, json_encode(array('status' => 1, 'tmp_Obj' => $tmp_Obj, 'gid_ary' => $gid_ary)));//写入redis
                                    }
                                    else{
                                        $redisObj->delete($gid . "_reflush_time");
                                        mysqli_query($dbMasterLink, "ROLLBACK");
                                        echo attention("更多玩法数据为空1", $uid, $langx);
                                        exit();
                                    }
                                }
                                else{
                                    $redisObj->delete($gid . "_reflush_time");
                                    mysqli_query($dbMasterLink, "ROLLBACK");
                                    echo attention("更多玩法数据为空11", $uid, $langx);
                                    exit();
                                }
                            }
                            else{
                                $redisObj->delete($gid . "_reflush_time");
                                mysqli_query($dbMasterLink, "ROLLBACK");
                                echo attention("更多玩法数据为空111", $uid, $langx);
                                exit();
                            }
                        }
                        else{
                            $redisObj->delete($gid . "_reflush_time");
                            mysqli_query($dbMasterLink, "ROLLBACK");
                            echo attention("更多玩法数据为空1111", $uid, $langx);
                            exit();
                        }
                    }
                }
            }
            //echo "in date <br/>";
            $games = $redisObj->getSimpleOne("gameMore_" . $gid);//在redis取出数据
//            echo $games;
//            exit();
        } else {//不存在赛事：接口抓取数据，存入数据库，redis
            //echo 'new data<br/>';
            $begin = mysqli_query($dbMasterLink, "start transaction");//开启事务$from
            $lockMid = mysqli_query($dbMasterLink, "select MID from " . DBPREFIX . "match_sports_more_midlock where `MID` = $gid for update");
            $valReflushTime2 = $redisObj->getSimpleOne($gid . "_reflush_time");
            if (!$valReflushTime2) {
                if ($begin && $lockMid->num_rows == 1) {
                    $dataNew = getDataFromInterface($langx, $gtype, $showtype, $gid);
                    if ($dataNew['tmp_Obj'] && count($dataNew['tmp_Obj']) > 0 && $dataNew['gid_ary'] && count($dataNew['gid_ary']) > 0) {
                        $tmp_Obj = $dataNew['tmp_Obj'];
                        $gid_ary = $dataNew['gid_ary'];
                        $rtStatus = $redisObj->setOne($gid . "_reflush_time", time());//写入刷新时间
                        if ($rtStatus) {
                            $details = json_encode($tmp_Obj,JSON_UNESCAPED_UNICODE);
                            $details = str_replace('\'', '', $details);
                            $exitResult = mysqli_query($dbLink, "select MID from " . DBPREFIX . "match_sports_more where MID=" . $gid);
                            $exitsNum = mysqli_fetch_assoc($exitResult);
                            if (isset($exitsNum['MID']) == $exitsNum['MID']) {//更新
                                $setGames = mysqli_query($dbMasterLink, "replace into " . DBPREFIX . "match_sports_more(`MID`,`details`)VALUES($gid,'$details')");
                            } else {//存入
                                $setGames = mysqli_query($dbMasterLink, "INSERT INTO " . DBPREFIX . "match_sports_more(`MID`,`details`)VALUES($gid,'$details')");
                            }
                            if ($setGames) {
                                $comStatus = mysqli_query($dbMasterLink, "COMMIT");
                                if ($comStatus) {
                                    $redisObj->setOne("gameMore_" . $gid, json_encode(array('status' => 1, 'tmp_Obj' => $tmp_Obj, 'gid_ary' => $gid_ary)));//写入redis
                                }
                                else{
                                    $redisObj->delete($gid . "_reflush_time");
                                    mysqli_query($dbMasterLink, "ROLLBACK");
                                    echo attention("更多玩法数据为空2", $uid, $langx);
                                    exit();
                                }
                            }
                            else{
                                $redisObj->delete($gid . "_reflush_time");
                                mysqli_query($dbMasterLink, "ROLLBACK");
                                echo attention("更多玩法数据为空22", $uid, $langx);
                                exit();
                            }
                        }
                        else{
                            mysqli_query($dbMasterLink, "ROLLBACK");
                            echo attention("更多玩法数据为空222", $uid, $langx);
                            exit();
                        }
                    }
                }
            } else {
                mysqli_query($dbMasterLink, "ROLLBACK");
                $games = $redisObj->getSimpleOne("gameMore_" . $gid);//在redis取出数据
//                echo $games;
//                exit();
            }
        }
    } else {
        echo attention("更多玩法数据为空3", $uid, $langx);
        exit();
    }
}
// -------------------------------------------------------更多玩法刷水 End

if($cou==0){
	echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
	exit();
}else{
	$detailsData=array();
	$moreMethod = array(15,46,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,144,51,52,53,61,62,65,69,115,165);
	if(isset($_REQUEST['dataSou']) && $_REQUEST['dataSou']=="interface"){
		array_push($moreMethod,$line);
	}
	if(isset($_REQUEST['id']) && $_REQUEST['id']>0){
		array_push($moreMethod,$line);
	}
	if(in_array($line,$moreMethod)){
		$moreRes = mysqli_query($dbMasterLink,"select details from ".DBPREFIX."match_sports_more where `MID`='$gid'");
		$rowMore = mysqli_fetch_assoc($moreRes);
		$couMore = mysqli_num_rows($moreRes);	
		$detailsArr = json_decode($rowMore['details'],true);
		$detailsData =$detailsArr[$gid];
		if($rtype=='HODD')	$rtype="HEOO";
		if($rtype=='HEVEN') $rtype="HEOE";
		if($rtype=='ODD')	$rtype="EOO";
		if($rtype=='EVEN')  $rtype="EOE";
		if($rtype=='ODD')	$rtype="EOO";
		if($rtype=='EVEN')  $rtype="EOE";
		if($rtype=='0~1')   $rtype="T01"; 	
		if($rtype=='2~3')  $rtype="T23"; 	
		if($rtype=='4~6')  $rtype="T46";  		
		if($detailsData['sw_'.$wtype]=="Y" && $detailsData["ior_".$rtype]>0){
			$ioradio_r_h = $detailsData["ior_".$rtype];
			if(!$ioradio_r_h){
				echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
				exit();
			}
		}
		if($rtype=='HEOO')	$rtype="HODD";
		if($rtype=='HEOE')  $rtype="HEVEN";
		if($rtype=='EOO')	$rtype="ODD";
		if($rtype=='EOE') 	$rtype="EVEN";
		if($rtype=='T01')   $rtype="0~1"; 	
		if($rtype=='T23')   $rtype="2~3"; 	
		if($rtype=='T46')   $rtype="4~6";

        // 半场单双赔率处理
        if(in_array($rtype,array('HODD','HEVEN'))){
            $ior_Rate_arr=get_other_ioratio(GAME_POSITION,returnOddEvenRate($detailsData["ior_HEOO"]),returnOddEvenRate($detailsData["ior_HEOE"]),100);
            $ior_Rate[0] =returnOddEvenRate($ior_Rate_arr[0],'plus');
            $ior_Rate[1] =returnOddEvenRate($ior_Rate_arr[1],'plus');
            if($rtype=='HODD'){ //单
                $ioradio_r_h=$ior_Rate[0];
            }else{ // 双
                $ioradio_r_h=$ior_Rate[1];
            }
        }

        //更多玩法注入效验
        if(gameFtVerify($line,$wtype,$rtype)){
            if( $line==1 || $line==11 ){
                $type = substr($rtype,-1,1);
            }
            if($line==2){
                $Sign = $detailsData['ratio'];
                $type = substr($rtype,-1,1);
            }
            if($line==12){
                $Sign = $detailsData['hratio'];
                if( $rtype == 'HRH' ){ $type='H'; }
                if( $rtype == 'HRC' ){ $type='C'; }
            }
            if($line==3){
                if($rtype=='OUC'){
                    $type='C';
                    $m_place = '大&nbsp'.$detailsData['ratio_o'];
                    $s_m_place = $w_m_place = $w_m_place_tw = $w_m_place_en =  $m_place;
                }
                if($rtype=='OUH'){
                    $type='H';
                    $m_place = '小&nbsp;'.$detailsData['ratio_u'];
                    $s_m_place = $w_m_place = $w_m_place_tw = $w_m_place_en =  $m_place;
                }
            }
            if($line==13){
                if($rtype=='HOUC'){
                    $type='C';
                    $m_place = '大&nbsp'.$detailsData['ratio_ho'];
                    $s_m_place = $w_m_place = $w_m_place_tw = $w_m_place_en =  $m_place;
                }
                if($rtype=='HOUH'){
                    $type='H';
                    $m_place = '小&nbsp;'.$detailsData['ratio_hu'];
                    $s_m_place = $w_m_place = $w_m_place_tw = $w_m_place_en =  $m_place;
                }
            }
        }else{
            echo attention("非法操作,请重新下注!",$uid,$langx);
            exit();
        }
    }
	//取出写入数据库的四种语言的客队名称
	$w_tg_team=$row['TG_Team'];
	$w_tg_team_tw=$row['TG_Team_tw'];
	$w_tg_team_en=$row['TG_Team_en'];
	
	//取出四种语言的主队名称，并去掉其中的“主”和“中”字样
	$w_mb_team=filiter_team(trim($row['MB_Team']));
	$w_mb_team_tw=filiter_team(trim($row['MB_Team_tw']));
	$w_mb_team_en=filiter_team(trim($row['MB_Team_en']));
	
	$w_mb_mid=$row['MB_MID'];
	$w_tg_mid=$row['TG_MID'];	
	
	//取出当前字库的主客队伍名称
	$s_mb_team=filiter_team($row[$mb_team]);
	$s_tg_team=filiter_team($row[$tg_team]);
	
	//联盟处理:生成写入数据库的联盟样式和显示的样式，二者有区别
	$s_sleague=$row[$m_league];
	
	//下注时间
	$m_date=$row["M_Date"];
	$showtype=$row["ShowTypeR"];
	$bettime=date('Y-m-d H:i:s');
	$m_start=strtotime($row['M_Start']);
	$datetime=time();
//    if ($m_start-$datetime<120){
//	    echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
//	    exit();
//	}
	//根据下注的类型进行处理：构建成新的数据格式，准备写入数据库
	switch ($line){
  	case 1:
	  	$bet_type='独赢';
		$bet_type_tw='獨贏';
		$bet_type_en="1x2";
		$caption=$Order_FT.$Order_1_x_2_betting_order;
		switch ($type){
		case "H":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$s_mb_team;
			if(!$ioradio_r_h){$ioradio_r_h=$row["MB_Win_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$mtype='MH';
			break;
		case "C":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$s_tg_team;
			if(!$ioradio_r_h){$ioradio_r_h=$row["TG_Win_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		    $mtype='MC';
			break;
		case "N":
			$w_m_place="和局";
			$w_m_place_tw="和局";
			$w_m_place_en="Flat";
			$s_m_place=$Draw;
			if(!$ioradio_r_h){$ioradio_r_h=$row["M_Flat_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		    $mtype='MN';
			break;
		}
		$turn_url="/app/member/FT_order/FT_order_m.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$Sign="VS";
		$grape="";
		$gwin=($w_m_rate-1)*$gold;
		$ptype='M';
	break;
	case 2:
	  	$bet_type='让球';
		$bet_type_tw="讓球";
		$bet_type_en="Handicap";	
		$caption=$Order_FT.$Order_Handicap_betting_order;
		$rate=get_other_ioratio($odd_f_type,$row["MB_LetB_Rate"],$row["TG_LetB_Rate"],100);
		switch ($type){
			case "H":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				if(!$ioradio_r_h){$ioradio_r_h=$rate[0];}
				$w_m_rate=change_rate($open,$ioradio_r_h);
				$turn_url="/app/member/FT_order/FT_order_r.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&strong=".$strong."&odd_f_type=".$odd_f_type;
				$mtype='RH';
				break;
			case "C":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				if(!$ioradio_r_h){$ioradio_r_h=$rate[1];}
				$w_m_rate=change_rate($open,$ioradio_r_h);
				$turn_url="/app/member/FT_order/FT_order_r.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&strong=".$strong."&odd_f_type=".$odd_f_type;
				$mtype='RC';
				break;
		}
		if(!$Sign){$Sign=$row['M_LetB'];}
		$grape=$Sign;
		if ($showtype=="H"){
			$l_team=$s_mb_team;
			$r_team=$s_tg_team;
			$w_l_team=$w_mb_team;
			$w_l_team_tw=$w_mb_team_tw;
			$w_l_team_en=$w_mb_team_en;
			$w_r_team=$w_tg_team;
			$w_r_team_tw=$w_tg_team_tw;
			$w_r_team_en=$w_tg_team_en;
		}else{
			$r_team=$s_mb_team;
			$l_team=$s_tg_team;
			$w_r_team=$w_mb_team;
			$w_r_team_tw=$w_mb_team_tw;
			$w_r_team_en=$w_mb_team_en;
			$w_l_team=$w_tg_team;
			$w_l_team_tw=$w_tg_team_tw;
			$w_l_team_en=$w_tg_team_en;
		}
		$s_mb_team=$l_team;
		$s_tg_team=$r_team;
		$w_mb_team=$w_l_team;
		$w_mb_team_tw=$w_l_team_tw;
		$w_mb_team_en=$w_l_team_en;
		$w_tg_team=$w_r_team;
		$w_tg_team_tw=$w_r_team_tw;
		$w_tg_team_en=$w_r_team_en;
		
		if ($odd_f_type=='H'){
		    $gwin=($w_m_rate)*$gold;
		}else if ($odd_f_type=='M' or $odd_f_type=='I'){
		    if ($w_m_rate<0){
				$gwin=$gold;
			}else{
				$gwin=($w_m_rate)*$gold;
			}
		}else if ($odd_f_type=='E'){
		    $gwin=($w_m_rate-1)*$gold;
		}
		$ptype='R';
	break;
	case 3:
		$bet_type='大小';
		$bet_type_tw="大小";
		$bet_type_en="Over/Under";
		$caption=$Order_FT.$Order_Over_Under_betting_order;
		$rate=get_other_ioratio($odd_f_type,$row["MB_Dime_Rate"],$row["TG_Dime_Rate"],100);
		switch ($type){
		case "C":
		    if(!$w_m_place){
                $w_m_place=$row["MB_Dime"];
                $w_m_place=str_replace('O','大&nbsp;',$w_m_place);
            }
            if($w_m_place_tw){
                $w_m_place_tw=$row["MB_Dime"];
                $w_m_place_tw=str_replace('O','大&nbsp;',$w_m_place_tw);
            }
            if($w_m_place_en){
                $w_m_place_en=$row["MB_Dime"];
                $w_m_place_en=str_replace('O','over&nbsp;',$w_m_place_en);
            }

            if(!$m_place){$m_place=$row["MB_Dime"];}

			if(!$s_m_place){ $s_m_place=$row["MB_Dime"]; }
			if ($langx=="zh-cn"){
	            $s_m_place=str_replace('O','大&nbsp;',$s_m_place);
		    }else if ($langx=="zh-cn"){
		        $s_m_place=str_replace('O','大&nbsp;',$s_m_place);
		    }else if ($langx=="en-us" or $langx=="th-tis"){
		        $s_m_place=str_replace('O','over&nbsp;',$s_m_place);
		    }
			if(!$ioradio_r_h){$ioradio_r_h=$rate[0];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$turn_url="/app/member/FT_order/FT_order_ou.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type;
			$mtype='OUH';			
			break;
		case "H":
		    if(!$w_m_place){
                $w_m_place=$row["TG_Dime"];
                $w_m_place=str_replace('U','小&nbsp;',$w_m_place);
            }
            if(!$w_m_place_tw){
                $w_m_place_tw=$row["TG_Dime"];
                $w_m_place_tw=str_replace('U','小&nbsp;',$w_m_place_tw);
            }
            if(!$w_m_place_en){
                $w_m_place_en=$row["TG_Dime"];
                $w_m_place_en=str_replace('U','under&nbsp;',$w_m_place_en);
            }
			if(!$m_place){$m_place=$row["TG_Dime"];}

            if(!$s_m_place){ $s_m_place=$row["TG_Dime"];}
			if ($langx=="zh-cn"){
	            $s_m_place=str_replace('U','小&nbsp;',$s_m_place);
		    }else if ($langx=="zh-cn"){
		        $s_m_place=str_replace('U','小&nbsp;',$s_m_place);
		    }else if ($langx=="en-us" or $langx=="th-tis"){
		        $s_m_place=str_replace('U','under&nbsp;',$s_m_place);
		    }
			if(!$ioradio_r_h){$ioradio_r_h=$rate[1];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$turn_url="/app/member/FT_order/FT_order_ou.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type;
			$mtype='OUC';		
			break;
		}
		$Sign="VS";
		$grape=$m_place;
		if ($odd_f_type=='H'){
		    $gwin=($w_m_rate)*$gold;
		}else if ($odd_f_type=='M' or $odd_f_type=='I'){
		    if ($w_m_rate<0){
				$gwin=$gold;
			}else{
				$gwin=($w_m_rate)*$gold;
			}
		}else if ($odd_f_type=='E'){
		    $gwin=($w_m_rate-1)*$gold;
		}
		$ptype='OU';		
	break;
	case 4:
		$turn_url="/app/member/FT_order/FT_order_pd.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$bet_type='波胆';
		$bet_type_tw="波膽";
		$bet_type_en="Correct Score";
		$caption=$Order_FT.$Order_Correct_Score_betting_order;
		if($rtype!='OVH'){
			$rtype=str_replace('C','TG',str_replace('H','MB',$rtype));
			if(!$ioradio_r_h){$ioradio_r_h=$row[$rtype];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		}else{
			$rtype="OVMB";
			if(!$ioradio_r_h){$ioradio_r_h=$row['UP5'];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		}
		if ($rtype=="OVMB"){		
			$w_m_place='其它比分';
			$w_m_place_tw='其它比分';
			$w_m_place_en='Other Score';
			$Sign="VS";
			$s_m_place='其它比分';
		}else{
			$M_Place="";
			$M_Sign=$rtype;
			$M_Sign=str_replace("MB","",$M_Sign);
			$M_Sign=str_replace("TG",":",$M_Sign);
			$Sign=$M_Sign."";
		}
		$grape="";
		$gwin=($w_m_rate-1)*$gold;		
		$ptype='PD';		
		$mtype=$rtype;
	break;
	case 5: // 全场单双
	case 15: // 半场单双
		$turn_url="/app/member/FT_order/FT_order_t.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$bet_type='单双';
		$bet_type_tw="單雙";
		$bet_type_en="Odd/Even";
		$caption=$Order_FT.$Order_Odd_Even_betting_order;
		switch ($rtype){
		case "ODD":
			$w_m_place='单';
			$w_m_place_tw='單';
			$w_m_place_en='odd';
			$s_m_place='('.$Order_Odd.')';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_Single_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "EVEN":
			$w_m_place='双';
			$w_m_place_tw='雙';
			$w_m_place_en='even';
			$s_m_place='('.$Order_Even.')';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_Double_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HODD":
			$bet_type='半场单双';
			$bet_type_tw="半场單雙";
			$bet_type_en="HOdd/HEven";
			$caption=$Order_FT."半场".$Order_Odd_Even_betting_order;
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='单';
			$w_m_place_tw='單';
			$w_m_place_en='odd';
			$s_m_place='('.$Order_Odd.')';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_Single_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HEVEN":
			$bet_type='半场单双';
			$bet_type_tw="半场單雙";
			$bet_type_en="HOdd/HEven";
			$caption=$Order_FT."半场".$Order_Odd_Even_betting_order;
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='双';
			$w_m_place_tw='雙';
			$w_m_place_en='even';
			$s_m_place='('.$Order_Even.')';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_Double_Rate"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		}
		
		$Sign="VS";
		$gwin=($w_m_rate-1)*$gold;
		$ptype='EO';	
		$mtype=$rtype;
	break;
	case 6:
	case 46:
		$turn_url="/app/member/FT_order/FT_order_t.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx;
		if(in_array($rtype,array("HT0","HT1","HT2","HTOV"))){
			$bet_type='半场- 总入球数';
			$bet_type_tw="半场-總入球数";
			$bet_type_en="Total-Half";
			$caption=$Order_FT.'Half'.$Order_Total_Goals_betting_order;
		}else{
			$bet_type='总入球';
			$bet_type_tw="總入球";
			$bet_type_en="Total";
			$caption=$Order_FT.$Order_Total_Goals_betting_order;
		}
		switch ($rtype){
		case "0~1":
			$w_m_place='0~1';
			$w_m_place_tw='0~1';
			$w_m_place_en='0~1';
			$s_m_place='(0~1)';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_0_1"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "2~3":
			$w_m_place='2~3';
			$w_m_place_tw='2~3';
			$w_m_place_en='2~3';
			$s_m_place='(2~3)';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_2_3"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "4~6":
			$w_m_place='4~6';
			$w_m_place_tw='4~6';
			$w_m_place_en='4~6';
			$s_m_place='(4~6)';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_4_6"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "OVER":
			$w_m_place='7up';
			$w_m_place_tw='7up';
			$w_m_place_en='7up';
			$s_m_place='(7up)';
			if(!$ioradio_r_h){$ioradio_r_h=$row["S_7UP"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HT0":
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='0';
			$w_m_place_tw='0';
			$w_m_place_en='0';
			$s_m_place='0';
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HT1":
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='1';
			$w_m_place_tw='1';
			$w_m_place_en='1';
			$s_m_place='1';
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HT2":
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='2';
			$w_m_place_tw='2';
			$w_m_place_en='2';
			$s_m_place='2';
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "HTOV":
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$w_m_place='3或以上';
			$w_m_place_tw='3或以上';
			$w_m_place_en='3或以上';
			$s_m_place='3或以上';
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		}
		
		$Sign="VS";
		$gwin=($w_m_rate-1)*$gold;
		$ptype='T';
		$mtype=$rtype;				
	break;
	case 7:
		$turn_url="/app/member/FT_order/FT_order_f.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$bet_type='半全场';
		$bet_type_tw="半全場";
		$bet_type_en="Half/Full Time";	
		$caption=$Order_FT.$Order_Half_Full_Time_betting_order;
		switch ($rtype){
		case "FHH":
			$w_m_place=$w_mb_team.'&nbsp;/&nbsp;'.$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw.'&nbsp;/&nbsp;'.$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en.'&nbsp;/&nbsp;'.$w_mb_team_en;		
			$s_m_place=$row[$mb_team].'&nbsp;/&nbsp;'.$row[$mb_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["MBMB"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FHN":
			$w_m_place=$w_mb_team.'&nbsp;/&nbsp;和局';
			$w_m_place_tw=$w_mb_team_tw.'&nbsp;/&nbsp;和局';
			$w_m_place_en=$w_mb_team_en.'&nbsp;/&nbsp;Flat';		
			$s_m_place=$row[$mb_team].'&nbsp;/&nbsp;'.$Draw;
			if(!$ioradio_r_h){$ioradio_r_h=$row["MBFT"];}			
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FHC":
			$w_m_place=$w_mb_team.'&nbsp;/&nbsp;'.$w_tg_team;
			$w_m_place_tw=$w_mb_team_tw.'&nbsp;/&nbsp;'.$w_tg_team_tw;
			$w_m_place_en=$w_mb_team_en.'&nbsp;/&nbsp;'.$w_tg_team_en;
			$s_m_place=$row[$mb_team].'&nbsp;/&nbsp;'.$row[$tg_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["MBTG"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FNH":
			$w_m_place='和局&nbsp;/&nbsp;'.$w_mb_team;
			$w_m_place_tw='和局&nbsp;/&nbsp;'.$w_mb_team_tw;
			$w_m_place_en='Flat&nbsp;/&nbsp;'.$w_mb_team_en;
			$s_m_place=$Draw.'&nbsp;/&nbsp;'.$row[$mb_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["FTMB"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FNN":
			$w_m_place='和局&nbsp;/&nbsp;和局';
			$w_m_place_tw='和局&nbsp;/&nbsp;和局';
			$w_m_place_en='Flat&nbsp;/&nbsp;Flat';
			$s_m_place=$Draw.'&nbsp;/&nbsp;'.$Draw;
			if(!$ioradio_r_h){$ioradio_r_h=$row["FTFT"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FNC":
			$w_m_place='和局&nbsp;/&nbsp;'.$w_tg_team;
			$w_m_place_tw='和局&nbsp;/&nbsp;'.$w_tg_team_tw;
			$w_m_place_en='Flat&nbsp;/&nbsp;'.$w_tg_team_en;
			$s_m_place=$Draw.'&nbsp;/&nbsp;'.$row[$tg_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["FTTG"];}			
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FCH":
			$w_m_place=$w_tg_team.'&nbsp;/&nbsp;'.$w_mb_team;
			$w_m_place_tw=$w_tg_team_tw.'&nbsp;/&nbsp;'.$w_mb_team_tw;
			$w_m_place_en=$w_tg_team_en.'&nbsp;/&nbsp;'.$w_mb_team_en;
			$s_m_place=$row[$tg_team].'&nbsp;/&nbsp;'.$row[$mb_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["TGMB"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FCN":
			$w_m_place=$w_tg_team.'&nbsp;/&nbsp;和局';
			$w_m_place_tw=$w_tg_team_tw.'&nbsp;/&nbsp;和局';
			$w_m_place_en=$w_tg_team_en.'&nbsp;/&nbsp;Flat';
			$s_m_place=$row[$tg_team].'&nbsp;/&nbsp;'.$Draw;
			if(!$ioradio_r_h){$ioradio_r_h=$row["TGFT"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		case "FCC":
			$w_m_place=$w_tg_team.'&nbsp;/&nbsp;'.$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw.'&nbsp;/&nbsp;'.$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en.'&nbsp;/&nbsp;'.$w_tg_team_en;
			$s_m_place=$row[$tg_team].'&nbsp;/&nbsp;'.$row[$tg_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["TGTG"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			break;
		}
		$Sign="VS";
		$gwin=($w_m_rate-1)*$gold;		
		$ptype='F';
		$mtype=$rtype;								
	break;	
	case 11:
		$turn_url="/app/member/FT_order/FT_order_hm.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$bet_type='半场独赢';
		$bet_type_tw="半場獨贏";
		$bet_type_en="1st Half 1x2";
		$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
		$caption=$Order_FT.$Order_1st_Half_1_x_2_betting_order;
		switch ($type){
		case "H":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$row[$mb_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["MB_Win_Rate_H"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$mtype='VMH';
			break;
		case "C":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$row[$tg_team];
			if(!$ioradio_r_h){$ioradio_r_h=$row["TG_Win_Rate_H"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$mtype='VMC';
			break;
		case "N":
			$w_m_place="和局";
			$w_m_place_tw="和局";
			$w_m_place_en="Flat";
			$s_m_place=$Draw;
			if(!$ioradio_r_h){$ioradio_r_h=$row["M_Flat_Rate_H"];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$mtype='VMN';
			break;
		}
		$Sign="VS";
		$grape="";
		$gwin=($w_m_rate-1)*$gold;
		$ptype='VM';
	break;
	case 12:
		$bet_type='半场让球';
		$bet_type_tw="半場讓球";
		$bet_type_en="1st Half Handicap";
		$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
		$caption=$Order_FT.$Order_1st_Half_Handicap_betting_order;
		$rate=get_other_ioratio($odd_f_type,$row["MB_LetB_Rate_H"],$row["TG_LetB_Rate_H"],100);
		switch ($type){
			case "H":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$row[$mb_team];
				if(!$ioradio_r_h){$ioradio_r_h=$rate[0];}
				$w_m_rate=change_rate($open,$ioradio_r_h);
				$turn_url="/app/member/FT_order/FT_order_hr.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&strong=".$strong."&odd_f_type=".$odd_f_type;
				$mtype='VRH';
				break;
			case "C":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$row[$tg_team];
				if(!$ioradio_r_h){$ioradio_r_h=$rate[1];}
				$w_m_rate=change_rate($open,$ioradio_r_h);
				$turn_url="/app/member/FT_order/FT_order_hr.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&strong=".$strong."&odd_f_type=".$odd_f_type;
				$mtype='VRC';
				break;
		}
        if(!$Sign){$Sign=$row['M_LetB_H'];}
		$grape=$Sign;
		if ($showtype=="H"){
			$l_team=$s_mb_team;
			$r_team=$s_tg_team;
			
			$w_l_team=$w_mb_team;
			$w_l_team_tw=$w_mb_team_tw;
			$w_l_team_en=$w_mb_team_en;
			$w_r_team=$w_tg_team;
			$w_r_team_tw=$w_tg_team_tw;
			$w_r_team_en=$w_tg_team_en;
		}else{
			$r_team=$s_mb_team;
			$l_team=$s_tg_team;
			$w_r_team=$w_mb_team;
			$w_r_team_tw=$w_mb_team_tw;
			$w_r_team_en=$w_mb_team_en;
			$w_l_team=$w_tg_team;
			$w_l_team_tw=$w_tg_team_tw;
			$w_l_team_en=$w_tg_team_en;
		}
		$s_mb_team=$l_team;
		$s_tg_team=$r_team;
		$w_mb_team=$w_l_team;
		$w_mb_team_tw=$w_l_team_tw;
		$w_mb_team_en=$w_l_team_en;
		$w_tg_team=$w_r_team;
		$w_tg_team_tw=$w_r_team_tw;
		$w_tg_team_en=$w_r_team_en;
		if ($odd_f_type=='H'){
		    $gwin=($w_m_rate)*$gold;
		}else if ($odd_f_type=='M' or $odd_f_type=='I'){
		    if ($w_m_rate<0){
				$gwin=$gold;
			}else{
				$gwin=($w_m_rate)*$gold;
			}
		}else if ($odd_f_type=='E'){
		    $gwin=($w_m_rate-1)*$gold;
		}
		$ptype='VR';
	break;
	case 13:
  		$bet_type='半场大小';
		$bet_type_tw="半場大小";
		$bet_type_en="1st Half Over/Under";
		$caption=$Order_FT.$Order_1st_Half_Over_Under_betting_order;
		$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
		$rate=get_other_ioratio($odd_f_type,$row["MB_Dime_Rate_H"],$row["TG_Dime_Rate_H"],100);
		switch ($type){
		case "C":
            if(!$w_m_place){
                $w_m_place=$row["MB_Dime_H"];
                $w_m_place=str_replace('O','大&nbsp;',$w_m_place);
            }
            if(!$w_m_place_tw){
                $w_m_place_tw=$row["MB_Dime_H"];
                $w_m_place_tw=str_replace('O','大&nbsp;',$w_m_place_tw);
            }
            if(!$w_m_place_en){
                $w_m_place_en=$row["MB_Dime_H"];
                $w_m_place_en=str_replace('O','over&nbsp;',$w_m_place_en);
            }

			if(!$m_place){$m_place=$row["MB_Dime_H"];}

			if(!$s_m_place){$s_m_place=$row["MB_Dime_H"];}
			if ($langx=="zh-cn"){
	            $s_m_place=str_replace('O','大&nbsp;',$s_m_place);
		    }else if ($langx=="zh-cn"){
		        $s_m_place=str_replace('O','大&nbsp;',$s_m_place);
		    }else if ($langx=="en-us" or $langx=="th-tis"){
		        $s_m_place=str_replace('O','over&nbsp;',$s_m_place);
			}
			if(!$ioradio_r_h){$ioradio_r_h=$rate[0];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
			$turn_url="/app/member/FT_order/FT_order_hou.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type;
			$mtype='VOUH';		
			break;
		case "H":
			if(!$w_m_place){
		        $w_m_place=$row["TG_Dime_H"];
			    $w_m_place=str_replace('U','小&nbsp;',$w_m_place);
            }
            if(!$w_m_place_tw){
			    $w_m_place_tw=$row["TG_Dime_H"];
			    $w_m_place_tw=str_replace('U','小&nbsp;',$w_m_place_tw);
            }
            if(!$w_m_place_en){
			    $w_m_place_en=$row["TG_Dime_H"];
			    $w_m_place_en=str_replace('U','under&nbsp;',$w_m_place_en);
            }

			if(!$m_place){$m_place=$row["TG_Dime_H"];}

			if(!$s_m_place){$s_m_place=$row["TG_Dime_H"];}
			if ($langx=="zh-cn"){
	            $s_m_place=str_replace('U','小&nbsp;',$s_m_place);
		    }else if ($langx=="zh-cn"){
		        $s_m_place=str_replace('U','小&nbsp;',$s_m_place);
		    }else if ($langx=="en-us" or $langx=="th-tis"){
		        $s_m_place=str_replace('U','under&nbsp;',$s_m_place);
			}
			if(!$ioradio_r_h){$ioradio_r_h=$rate[1];}
			$w_m_rate=change_rate($open,$ioradio_r_h);	
			$turn_url="/app/member/FT_order/FT_order_hou.php?gid=".$gid."&uid=".$uid."&type=".$type."&gnum=".$gnum."&odd_f_type=".$odd_f_type;
			$mtype='VOUC';
		    break;
		}
		$Sign="VS";
		$grape=$m_place;
		if ($odd_f_type=='H'){
		    $gwin=($w_m_rate)*$gold;
		}else if ($odd_f_type=='M' or $odd_f_type=='I'){
		    if ($w_m_rate<0){
				$gwin=$gold;
			}else{
				$gwin=($w_m_rate)*$gold;
			}
		}else if ($odd_f_type=='E'){
		    $gwin=($w_m_rate-1)*$gold;
		}
		$ptype='VOU';		
	break;
	case 14:
		$turn_url="/app/member/FT_order/FT_order_hpd.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx;
		$bet_type='半场波胆';
		$bet_type_tw="半場波膽";
		$bet_type_en="1st Half Correct Score";
		$caption=$Order_FT.$Order_1st_Half_Correct_Score_betting_order;
		$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
		if($rtype!='HOVH'){
			$rtype=str_replace('C','TG',str_replace('H','MB',$rtype));
			if(!$ioradio_r_h){$ioradio_r_h=$row[$rtype.H];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		}else{
			$rtype="OVMB";
			if(!$ioradio_r_h){$ioradio_r_h=$row['UP5H'];}
			$w_m_rate=change_rate($open,$ioradio_r_h);
		}
		if ($rtype=="OVMB"){		
			$s_m_place=$Order_Other_Score;
			$w_m_place='其它比分';
			$w_m_place_tw='其它比分';
			$w_m_place_en='Other Score';
			$Sign="VS";
			$s_m_place='其它比分';
		}else{
			$M_Place="";
			$M_Sign=$rtype;
			$M_Sign=str_replace("MB","",$M_Sign);
			$M_Sign=str_replace("TG",":",$M_Sign);
			$Sign=$M_Sign."";
		}
		$grape="";
		$gwin=($w_m_rate-1)*$gold;		
		$ptype='VPD';
		if(strpos($rtype,"MBMB")>-1){
			$mtype=substr($rtype,2);
		}else{
			$mtype=$rtype;		
		}	
	break;
	case 65:
    case 165:	
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='双方球队进球';
		$bet_type_tw="双方球队进球";
		$bet_type_en="Double In Correct Score";
		$caption=$Order_FT.$Order_Double_In_betting_order;
		if($rtype=="HTSY" || $rtype=="HTSN"){
			$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$bet_type="双方球队进球-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
			$bet_type_tw="双方球队进球-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
		}
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "TSY":
			$w_m_place='是';
			$w_m_place_tw='是';
			$w_m_place_en='YES';
			$s_m_place='是';
			break;
		case "TSN":
			$w_m_place='不是';
			$w_m_place_tw='不是';
			$w_m_place_en='NO';
			$s_m_place='不是';
			break;
		case "HTSY":
			$w_m_place='是';
			$w_m_place_tw='是';
			$w_m_place_en='YES';
			$s_m_place='是';
			break;
		case "HTSN":
			$w_m_place='不是';
			$w_m_place_tw='不是';
			$w_m_place_en='NO';
			$s_m_place='不是';
			break;
		}
		
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='TS';
		$mtype=$rtype;
		break;
	case 17:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		echo "<script language='javascript'>self.location='$turn_url';</script>";
		exit;
	  	$bet_type='最先/最后进球';
		$bet_type_tw="最先/最后进球";
		$bet_type_en="Order_FL_Ball_In";
		$caption=$Order_FT.'_'.$Order_FL_Ball_In_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "PGFH": 
				$w_m_place='最先进球_'.$w_mb_team;
				$w_m_place_tw='最先进球_'.$w_mb_team_tw;
				$w_m_place_en='first ball in '.$w_mb_team_en; 
				$s_m_place='最先进球_'.$s_mb_team;
				break; 
			case "PGFC": 
				$w_m_place='最先进球_'.$w_tg_team;
				$w_m_place_tw='最先进球_'.$w_tg_team_tw;
				$w_m_place_en='first ball in '.$w_tg_team_en; 
				$s_m_place='最先进球_'.$s_tg_team;
				break; 
			case "PGLH": 
				$w_m_place='最后进球_'.$w_mb_team;
				$w_m_place_tw='最后进球_'.$w_mb_team_tw;
				$w_m_place_en='last ball in_'.$w_mb_team_en; 
				$s_m_place='最后进球_'.$s_mb_team;
				break; 
			case "PGLC": 
				$w_m_place='最后进球_'.$w_tg_team;
				$w_m_place_tw='最后进球_'.$w_tg_team_tw;
				$w_m_place_en='last ball in_'.$w_tg_team_en; 
				$s_m_place='最后进球_'.$s_tg_team;
				break;
			case "PGFN": 
				$w_m_place='无进球';
				$w_m_place_tw='无进球';
				$w_m_place_en='no ball in'; 
				$s_m_place='无进球';
				break; 
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='SP';
		$mtype=$rtype;
		break;
	case 18:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		$bet_type='净胜球数';
		$bet_type_tw="净胜球数";
		$bet_type_en="Order_Net_Win_Ballnum";
		$caption=$Order_FT.'_'.$Order_Net_Win_Ballnum.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "WMH1":
				$w_m_place=$w_mb_team." - 净胜1球";
				$w_m_place_tw=$w_mb_team_tw." - 净胜1球";
				$w_m_place_en=$w_mb_team_en." - net win one ball"; 
				$s_m_place=$s_mb_team." - 净胜1球";
				break;	
			case "WM0": 
				$w_m_place=" 0 - 0 和局";
				$w_m_place_tw=" 0 - 0 和局";
				$w_m_place_en=" 0 - 0 Flat"; 
				$s_m_place=" 0 - 0 和局";
				break;	
			case "WMC1":
				$w_m_place=$w_tg_team." - 净胜1球";
				$w_m_place_tw=$w_tg_team_tw." - 净胜1球";
				$w_m_place_en=$w_tg_team_en." - net win one ball";  
				$s_m_place=$s_tg_team." - 净胜1球";
				break;
			case "WMH2": 
				$w_m_place=$w_mb_team." - 净胜2球";
				$w_m_place_tw=$w_mb_team_tw." - 净胜2球";
				$w_m_place_en=$w_mb_team_en." - net win 2 ball"; 
				$s_m_place=$s_mb_team." - 净胜2球";
				break;	
			case "WMN": 
				$w_m_place="任何进球和局";
				$w_m_place_tw="任何进球和局";
				$w_m_place_en="any ball in Flat"; 
				$s_m_place="任何进球和局";
				break;	
			case "WMC2": 
				$w_m_place=$w_tg_team." - 净胜2球";
				$w_m_place_tw=$w_tg_team_tw." - 净胜2球";
				$w_m_place_en=$w_tg_team_en." - net win 2 ball";  
				$s_m_place=$s_tg_team." - 净胜2球";
				break;	
			case "WMH3": 
				$w_m_place=$w_mb_team." - 净胜3球";
				$w_m_place_tw=$w_mb_team_tw." - 净胜3球";
				$w_m_place_en=$w_mb_team_en." - net win 3 ball"; 
				$s_m_place=$s_mb_team." - 净胜3球";
				break;
			case "WMC3": 
				$w_m_place=$w_tg_team." - 净胜3球";
				$w_m_place_tw=$w_tg_team_tw." - 净胜3球";
				$w_m_place_en=$w_tg_team_en." - net win 3 ball";  
				$s_m_place=$s_tg_team." - 净胜3球";
				break;	
			case "WMHOV": 
				$M_Place=$MB_Team." - 净胜4球或更多"; 
				$w_m_place=$w_mb_team." - 净胜4球或更多";
				$w_m_place_tw=$w_mb_team_tw." - 净胜4球或更多";
				$w_m_place_en=$w_mb_team_en." - net win 4 ball or more"; 
				$s_m_place=$s_mb_team." - 净胜4球或更多";
				break;	
			case "WMCOV": 
				$w_m_place=$w_tg_team." - 净胜4球或更多";
				$w_m_place_tw=$w_tg_team_tw." - 净胜4球或更多";
				$w_m_place_en=$w_tg_team_en." - net win 4 ball or more";  
				$s_m_place=$s_tg_team." - 净胜4球或更多";
				break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='WM';
		$mtype=$rtype;
		break;
	case 69:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		$bet_type='双重机会';
		$bet_type_tw="双重机会";
		$bet_type_en="Order_Chance_Double";
		$caption=$Order_FT.'_'.$Order_Chance_Double.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "DCHN": 
				$w_m_place=$w_mb_team." / "."和局"; 
				$w_m_place_tw=$w_mb_team_tw." / "."和局";
				$w_m_place_en=$w_mb_team_en." / Flat";  
				$s_m_place=$s_mb_team." / "."和局"; 
				break; 
			case "DCCN": 
				$w_m_place=$w_tg_team." / "."和局"; 
				$w_m_place_tw=$w_tg_team_tw." / "."和局"; 
				$w_m_place_en=$w_tg_team_en." / Flat";  
				$s_m_place=$s_tg_team." / "."和局"; 
				break; 
			case "DCHC": 
				$w_m_place=$w_mb_team." / ".$w_tg_team;
				$w_m_place_tw=$w_mb_team_tw." / ".$w_tg_team_tw;
				$w_m_place_en=$w_mb_team_en." / ".$w_tg_team_en; 
				$s_m_place=$s_mb_team." / ".$s_tg_team;
				break; 
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='DC';
		$mtype=$rtype;
		break;
	case 62:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		$bet_type='零失球';
		$bet_type_tw="零失球";
		$bet_type_en="Order_Clean_Sheets";
		$caption=$Order_FT.'_'.$Order_Clean_Sheets.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "CSH": 
				$w_m_place=$w_mb_team; 
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;  
				$s_m_place=$s_mb_team; 
				break; 
			case "CSC": 
				$w_m_place=$w_tg_team; 
				$w_m_place_tw=$w_tg_team_tw; 
				$w_m_place_en=$w_tg_team_en;  
				$s_m_place=$s_tg_team; 
				break; 
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='CS';
		$mtype=$rtype;
		break;
	case 61:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='零失球获胜';
		$bet_type_tw="零失球获胜";
		$bet_type_en="Order_Clean_Sheets_Win";
		$caption=$Order_FT.'_'.$Order_Clean_Sheets_Win.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "WNH": 
				$w_m_place=$w_mb_team; 
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;  
				$s_m_place=$s_mb_team; 
				break; 
			case "WNC": 
				$w_m_place=$w_tg_team; 
				$w_m_place_tw=$w_tg_team_tw; 
				$w_m_place_en=$w_tg_team_en;  
				$s_m_place=$s_tg_team; 
				break; 
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='WN';
		$mtype=$rtype;
		break;
	case 22:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='独赢 & 进球 大 / 小';
		$bet_type_tw="独赢 & 进球 大  / 小";
		$bet_type_en="Order_M_Ball_OU";
		$caption=$Order_FT.'_'.$Order_M_Ball_OU.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "MOUAHO":	//独赢 & 进球 大 / 小  A
				$M_Place=$MB_Team." & 大 1.5"; 
				$w_m_place=$w_mb_team.' & 大 1.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 大 1.5';
				$w_m_place_en=$w_mb_team_en.' & Over 1.5';  
				$s_m_place=$s_mb_team." ".$U_B1;
				break;	
			case "MOUANO":	 
				$w_m_place="和局"." & 大 1.5"; 
				$w_m_place_tw="和局"." & 大 1.5"; 
				$w_m_place_en="Flat".' & Over 1.5';  
				$s_m_place="和局"." ".$U_B1;
				break;	
			case "MOUACO":	 
				$w_m_place=$w_tg_team." & 大 1.5"; 
				$w_m_place_tw=$w_tg_team_tw." & 大 1.5"; 
				$w_m_place_en=$w_tg_team_en.' & Over 1.5';  
				$s_m_place=$s_tg_team." ".$U_B1; 
				break;		
			case "MOUAHU":	
				$M_Place=$MB_Team." & 小 1.5"; 
				$w_m_place=$w_mb_team.' & 小 1.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 小 1.5';
				$w_m_place_en=$w_mb_team_en.' & Under 1.5';  
				$s_m_place=$s_mb_team.$U_S1;
				break;	
			case "MOUANU":	 
				$w_m_place="和局"." & 小 1.5"; 
				$w_m_place_tw="和局"." & 小 1.5"; 
				$w_m_place_en="Flat".' & Under 1.5';  
				$s_m_place="和局"." ".$U_S1;
				break;	
			case "MOUACU":	 
				$w_m_place=$w_tg_team.' & 小 1.5'; 
				$w_m_place_tw=$w_tg_team_tw.' & 小 1.5';
				$w_m_place_en=$w_tg_team_en.' & Under 1.5';  
				$s_m_place=$s_tg_team." ".$U_S1;
				break;
			case "MOUBHO":	//独赢 & 进球 大 / 小  B
				$M_Place=$MB_Team." & 大2.5"; 
				$w_m_place=$w_mb_team.' & 大 2.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 大 2.5';
				$w_m_place_en=$w_mb_team_en.' & Over 2.5';  
				$s_m_place=$s_mb_team." ".$U_B2;
				break;	
			case "MOUBNO":	 
				$w_m_place="和局"." & 大 2.5"; 
				$w_m_place_tw="和局"." & 大 2.5"; 
				$w_m_place_en="Flat".' & Over 2.5';  
				$s_m_place="和局"." ".$U_B2;
				break;	
			case "MOUBCO":	 
				$w_m_place=$w_tg_team." & 大 2.5"; 
				$w_m_place_tw=$w_tg_team_tw." & 大 2.5"; 
				$w_m_place_en=$w_tg_team_en.' & Over 2.5';  
				$s_m_place=$s_tg_team." ".$U_B2; 
				break;		
			case "MOUBHU":	
				$M_Place=$MB_Team." & 小 2.5"; 
				$w_m_place=$w_mb_team.' & 小 2.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 小 2.5';
				$w_m_place_en=$w_mb_team_en.' & Under 2.5';  
				$s_m_place=$s_mb_team." ".$U_S2;
				break;	
			case "MOUBNU":	 
				$w_m_place="和局"." & 小 2.5"; 
				$w_m_place_tw="和局"." & 小 2.5"; 
				$w_m_place_en="Flat".' & Under 2.5';  
				$s_m_place="和局"." ".$U_S2;
				break;	
			case "MOUBCU":	 
				$w_m_place=$w_tg_team.' & 小 2.5'; 
				$w_m_place_tw=$w_tg_team_tw.' & 小 2.5';
				$w_m_place_en=$w_tg_team_en.' & Under 2.5';  
				$s_m_place=$s_tg_team." ".$U_S2;
				break;
			case "MOUCHO":	//独赢 & 进球 大 / 小  C
				$M_Place=$MB_Team." & 大3.5"; 
				$w_m_place=$w_mb_team.' & 大 3.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 大 3.5';
				$w_m_place_en=$w_mb_team_en.' & Over 3.5';  
				$s_m_place=$s_mb_team." ".$U_B3;
				break;	
			case "MOUCNO":	 
				$w_m_place="和局"." & 大 3.5"; 
				$w_m_place_tw="和局"." & 大 3.5"; 
				$w_m_place_en="Flat".' & Over 3.5';  
				$s_m_place="和局"." ".$U_B3;
				break;	
			case "MOUCCO":	 
				$w_m_place=$w_tg_team." & 大 3.5"; 
				$w_m_place_tw=$w_tg_team_tw." & 大 3.5"; 
				$w_m_place_en=$w_tg_team_en.' & Over 3.5';  
				$s_m_place=$s_tg_team." ".$U_B3; 
				break;		
			case "MOUCHU":	
				$M_Place=$MB_Team." & 小 3.5"; 
				$w_m_place=$w_mb_team.' & 小 3.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 小 3.5';
				$w_m_place_en=$w_mb_team_en.' & Under 3.5';  
				$s_m_place=$s_mb_team." ".$U_S3;
				break;	
			case "MOUCNU":	 
				$w_m_place="和局"." & 小 3.5"; 
				$w_m_place_tw="和局"." & 小 3.5"; 
				$w_m_place_en="Flat".' & Under 3.5';  
				$s_m_place="和局"." ".$U_S3;
				break;	
			case "MOUCCU":	 
				$w_m_place=$w_tg_team.' & 小 3.5'; 
				$w_m_place_tw=$w_tg_team_tw.' & 小 3.5';
				$w_m_place_en=$w_tg_team_en.' & Under 3.5';  
				$s_m_place=$s_tg_team." ".$U_S3;
				break; 
			case "MOUDHO":	//独赢 & 进球 大 / 小  D
				$w_m_place=$w_mb_team.' & 大 4.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 大 4.5';
				$w_m_place_en=$w_mb_team_en.' & Over 4.5';  
				$s_m_place=$s_mb_team." ".$U_B4;
				break;	
			case "MOUDNO":	 
				$w_m_place="和局"." & 大 4.5"; 
				$w_m_place_tw="和局"." & 大 4.5"; 
				$w_m_place_en="Flat".' & Over 4.5';  
				$s_m_place="和局"." ".$U_B4;
				break;	
			case "MOUDCO":	 
				$w_m_place=$w_tg_team." & 大 4.5"; 
				$w_m_place_tw=$w_tg_team_tw." & 大 4.5"; 
				$w_m_place_en=$w_tg_team_en.' & Over 4.5';  
				$s_m_place=$s_tg_team." ".$U_B4; 
				break;		
			case "MOUDHU":	
				$w_m_place=$w_mb_team.' & 小 4.5'; 
				$w_m_place_tw=$w_mb_team_tw.' & 小 4.5';
				$w_m_place_en=$w_mb_team_en.' & Under 4.5';  
				$s_m_place=$s_mb_team." ".$U_S4;
				break;	
			case "MOUDNU":	 
				$w_m_place="和局"." & 小 4.5"; 
				$w_m_place_tw="和局"." & 小 4.5"; 
				$w_m_place_en="Flat".' & Under 4.5';  
				$s_m_place="和局"." ".$U_S4;
				break;	
			case "MOUDCU":	 
				$w_m_place=$w_tg_team.' & 小 4.5'; 
				$w_m_place_tw=$w_tg_team_tw.' & 小 4.5';
				$w_m_place_en=$w_tg_team_en.' & Under 4.5';  
				$s_m_place=$s_tg_team." ".$U_S4;
				break; 
		}
		
		$abcdType = substr($rtype,3,1);
		$abcdTypeOU = substr($rtype,5,1);
		if($abcdType=="A") $grape=$abcdTypeOU."1.5";
		if($abcdType=="B") $grape=$abcdTypeOU."2.5";
		if($abcdType=="C") $grape=$abcdTypeOU."3.5";
		if($abcdType=="D") $grape=$abcdTypeOU."4.5";
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='MOU';
		$mtype=$rtype;
		break;	
	case 23:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='独赢 & 双方球队进球';
		$bet_type_tw="独赢 & 双方球队进球";
		$bet_type_en="Double In Correct Score";
		$caption=$Order_FT.$Order_M_Ball_Double_in;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "MTSHY":
			$w_m_place=$w_mb_team.' & 是';
			$w_m_place_tw=$w_mb_team_tw.' & 是';
			$w_m_place_en=$w_mb_team_en.' & YES';
			$s_m_place=$s_mb_team.' & 是';
			break;
		case "MTSNY":
			$w_m_place='和局 & 是';
			$w_m_place_tw='和局 & 是';
			$w_m_place_en='Flat & YES';
			$s_m_place='和局 & 是';
			break;
		case "MTSCY":
			$w_m_place=$w_tg_team.' & 是';
			$w_m_place_tw=$w_tg_team_tw.' & 是';
			$w_m_place_en=$w_tg_team_en.' & YES';
			$s_m_place=$s_tg_team.' & 是';
			break;
		case "MTSHN":
			$w_m_place=$w_mb_team.' & 不是';
			$w_m_place_tw=$w_mb_team_tw.' & 不是';
			$w_m_place_en=$w_mb_team_en.' & NO';
			$s_m_place=$s_mb_team.' & 不是';
			break;
		case "MTSNN":
			$w_m_place='和局  & 不是';
			$w_m_place_tw='和局  & 不是';
			$w_m_place_en='和局  & NO';
			$s_m_place='和局  & 不是';
			break;
		case "MTSCN":
			$w_m_place=$w_tg_team.' & 不是';
			$w_m_place_tw=$w_tg_team_tw.' & 不是';
			$w_m_place_en=$w_tg_team_en.' & NO';
			$s_m_place=$s_tg_team.' & 不是';
			break;
		}
		
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='MTS';
		$mtype=$rtype;
		break;
	case 24:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='进球 大 / 小 & 双方球队进球';
		$bet_type_tw="进球 大 / 小 & 双方球队进球";
		$bet_type_en="Order_Ball_OU_Double_in";
		$caption=$Order_FT.$Order_Ball_OU_Double_in;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "OUTAOY":	//进球 大 / 小 & 双方球队进球	 A
				$w_m_place='大 1.5  & 是';
				$w_m_place_tw='大 1.5  & 是';
				$w_m_place_en='Over 1.5 & YES';
				$s_m_place=$U_B1.' & 是';
				break;	
			case "OUTAON":	 
				$w_m_place='大 1.5  & 不是';
				$w_m_place_tw='大 1.5  & 不是';
				$w_m_place_en='Over 1.5 & No';
				$s_m_place=$U_B1.' & 不是';
				break;		
			case "OUTAUY":	
				$w_m_place='小 1.5  & 是';
				$w_m_place_tw='小 1.5  & 是';
				$w_m_place_en='Under 1.5 & YES';
				$s_m_place=$U_S1.' & 是';
				break;	
			case "OUTAUN":	 
				$w_m_place='小 1.5  & 不是';
				$w_m_place_tw='小 1.5  & 不是';
				$w_m_place_en='Under 1.5 & No';
				$s_m_place=$U_S1.' & 不是';
				break;
			case "OUTBOY":	//进球 大 / 小 & 双方球队进球	 B
				$w_m_place='大 2.5  & 是';
				$w_m_place_tw='大 2.5  & 是';
				$w_m_place_en='Over 2.5 & YES';
				$s_m_place=$U_B2.' & 是';
				break;	
			case "OUTBON":	 
				$w_m_place='大 2.5  & 不是';
				$w_m_place_tw='大 2.5  & 不是';
				$w_m_place_en='Over 2.5 & No';
				$s_m_place=$U_B2.' & 不是';
				break;		
			case "OUTBUY":	
				$w_m_place='小 2.5  & 是';
				$w_m_place_tw='小 2.5  & 是';
				$w_m_place_en='Under 2.5 & YES';
				$s_m_place=$U_S2.' & 是';
				break;	
			case "OUTBUN":	 
				$w_m_place='小 2.5  & 不是';
				$w_m_place_tw='小 2.5  & 不是';
				$w_m_place_en='Under 2.5 & No';
				$s_m_place=$U_S2.' & 不是';
				break;
			case "OUTCOY":	//进球 大 / 小 & 双方球队进球	 C
				$w_m_place='大 3.5  & 是';
				$w_m_place_tw='大 3.5  & 是';
				$w_m_place_en='Over 3.5 & YES';
				$s_m_place=$U_B3.' & 是';
				break;	
			case "OUTCON":	 
				$w_m_place='大 3.5  & 不是';
				$w_m_place_tw='大 3.5  & 不是';
				$w_m_place_en='Over 3.5 & No';
				$s_m_place=$U_B3.' & 不是';
				break;		
			case "OUTCUY":	
				$w_m_place='小 3.5  & 是';
				$w_m_place_tw='小 3.5  & 是';
				$w_m_place_en='Under 3.5 & YES';
				$s_m_place=$U_S3.' & 是';
				break;	
			case "OUTCUN":	 
				$w_m_place='小 3.5  & 不是';
				$w_m_place_tw='小3.5  & 不是';
				$w_m_place_en='Under 3.5 & No';
				$s_m_place=$U_S3.' & 不是';
				break;
			case "OUTDOY":	//进球 大 / 小 & 双方球队进球	 D
				$w_m_place='大 4.5  & 是';
				$w_m_place_tw='大 4.5  & 是';
				$w_m_place_en='Over 4.5 & YES';
				$s_m_place=$U_B4.' & 是';
				break;	
			case "OUTDON":	 
				$w_m_place='大 4.5  & 不是';
				$w_m_place_tw='大 4.5  & 不是';
				$w_m_place_en='Over 4.5 & No';
				$s_m_place=$U_B4.' & 不是';
				break;		
			case "OUTDUY":	
				$w_m_place='小 4.5  & 是';
				$w_m_place_tw='小 4.5  & 是';
				$w_m_place_en='Under 4.5 & YES';
				$s_m_place=$U_S4.' & 是';
				break;	
			case "OUTDUN":	 
				$w_m_place='小 4.5  & 不是';
				$w_m_place_tw='小 4.5  & 不是';
				$w_m_place_en='Under 4.5 & No';
				$s_m_place=$U_S4.' & 不是';
				break;
		}
		$abcdType = substr($rtype,3,1);
		$abcdTypeOU = substr($rtype,4,1);
		if($abcdType=="A") $grape=$abcdTypeOU."1.5";
		if($abcdType=="B") $grape=$abcdTypeOU."2.5";
		if($abcdType=="C") $grape=$abcdTypeOU."3.5";
		if($abcdType=="D") $grape=$abcdTypeOU."4.5";
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='OUT';
		$mtype=$rtype;
		break;
	case 25:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='独赢 & 最先进球';
		$bet_type_tw="独赢 & 最先进球";
		$bet_type_en="Order_M_Ball_First";
		$caption=$Order_FT.$Order_M_Ball_First;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "MPGHH":
			$w_m_place=$w_mb_team.' & '.$w_mb_team."(最先进球)";
			$w_m_place_tw=$w_mb_team_tw.' & '.$w_mb_team_tw."(最先进球)";
			$w_m_place_en=$w_mb_team_en.' & '.$w_mb_team_en."(first ball in)";
			$s_m_place=$s_mb_team.' & '.$s_mb_team.'(最先进球)';
			break;
		case "MPGNH":
			$w_m_place='和局 & '.$w_mb_team."(最先进球)";
			$w_m_place_tw='和局 & '.$w_mb_team_tw."(最先进球)";
			$w_m_place_en='Flat & '.$w_mb_team_en."(first ball in)";
			$s_m_place='和局 & '.$s_mb_team.'(最先进球)';
			break;
		case "MPGCH":
			$w_m_place=$w_tg_team.' & '.$w_mb_team."(最先进球)";
			$w_m_place_tw=$w_tg_team_tw.' & '.$w_mb_team_tw."(最先进球)";
			$w_m_place_en=$w_tg_team_en.' & '.$w_mb_team_en."(first ball in)";
			$s_m_place=$s_tg_team.' & '.$s_mb_team.'(最先进球)';
			break;
		case "MPGHC":
			$w_m_place=$w_mb_team.' & '.$w_tg_team."(最先进球)";
			$w_m_place_tw=$w_mb_team_tw.' & '.$w_tg_team_tw."(最先进球)";
			$w_m_place_en=$w_mb_team_en.' & '.$w_tg_team_en."(first ball in)";
			$s_m_place=$s_mb_team.' & '.$s_tg_team.'(最先进球)';
			break;
		case "MPGNC":
			$w_m_place='和局 & '.$w_tg_team."(最先进球)";
			$w_m_place_tw='和局 & '.$w_tg_team_tw."(最先进球)";
			$w_m_place_en='Flat & '.$w_tg_team_en."(first ball in)";
			$s_m_place='和局 & '.$s_tg_team.'(最先进球)';
			break;
		case "MPGCC":
			$w_m_place=$w_tg_team.' & '.$w_tg_team."(最先进球)";
			$w_m_place_tw=$w_tg_team_tw.' & '.$w_tg_team_tw."(最先进球)";
			$w_m_place_en=$w_tg_team_en.' & '.$w_tg_team_en."(first ball in)";
			$s_m_place=$s_tg_team.' & '.$s_tg_team.'(最先进球)';
			break;
		}
		
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='MPG';
		$mtype=$rtype;
		break;
	case 26:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='先进2球的一方';
		$bet_type_tw="先进2球的一方";
		$bet_type_en="Order_Ball_In_2";
		$caption=$Order_FT.$Order_Ball_In_2.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "F2GH":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$s_mb_team;
			break;
		case "F2GC":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$s_tg_team;
			break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='F2G';
		$mtype=$rtype;
		break;
	case 27:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='先进3球的一方';
		$bet_type_tw="先进3球的一方";
		$bet_type_en="Order_Ball_In_3";
		$caption=$Order_FT.$Order_Ball_In_3.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "F3GH":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$s_mb_team;
			break;
		case "F3GC":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$s_tg_team;
			break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='F3G';
		$mtype=$rtype;
		break;
	case 28:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='最多进球的半场';
		$bet_type_tw="最多进球的半场";
		$bet_type_en="Order_Most_Ball_In_Half";
		$caption=$Order_FT.$Order_Most_Ball_In_Half.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "HGH":
			$w_m_place="上半场";
			$w_m_place_tw="上半场";
			$w_m_place_en="first half (of a game)";
			$s_m_place="上半场";
			break;
		case "HGC":
			$w_m_place="下半场";
			$w_m_place_tw="下半场";
			$w_m_place_en="second half (of a game)";
			$s_m_place="下半场";
			break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='HG';
		$mtype=$rtype;
		break;
	case 29:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='最多进球的半场 - 独赢';
		$bet_type_tw="最多进球的半场 - 独赢";
		$bet_type_en="Order_Most_Ball_In_Half_M";
		$caption=$Order_FT.$Order_Most_Ball_In_Half_M.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "MGH":
			$w_m_place="上半场";
			$w_m_place_tw="上半场";
			$w_m_place_en="first half (of a game)";
			$s_m_place="上半场";
			break;
		case "MGC":
			$w_m_place="下半场";
			$w_m_place_tw="下半场";
			$w_m_place_en="second half (of a game)";
			$s_m_place="下半场";
			break;
		case "MGN":
			$w_m_place="和局";
			$w_m_place_tw="和局";
			$w_m_place_en="Flat (of a game)";
			$s_m_place="和局";
			break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='MG';
		$mtype=$rtype;
		break;
	case 30:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='双半场进球';
		$bet_type_tw="双半场进球";
		$bet_type_en="Order_Double_Half_Ball_In";
		$caption=$Order_FT.$Order_Double_Half_Ball_In.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "SBH":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$s_mb_team;
			break;
		case "SBC":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$s_tg_team;
			break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='SB';
		$mtype=$rtype;
		break;
	case 131:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='首个进球方式';
		$bet_type_tw="首个进球方式";
		$bet_type_en="Order_Frist_Ball_In_Way";
		$caption=$Order_FT.$Order_Frist_Ball_In_Way.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "FGS":
				$w_m_place="射门";
				$w_m_place_tw="射门";
				$w_m_place_en="shoot (of a game)";
				$s_m_place="射门";
				break;
			case "FGH":
				$w_m_place="头球";
				$w_m_place_tw="头球";
				$w_m_place_en="Header";
				$s_m_place="头球";
				break;
			case "FGN":
				$w_m_place="无进球";
				$w_m_place_tw="无进球";
				$w_m_place_en="Without a goal";
				$s_m_place="无进球";
				break;
			case "FGP":
				$w_m_place="点球";
				$w_m_place_tw="点球";
				$w_m_place_en="point sphere";
				$s_m_place="点球";
				break;
			case "FGF":
				$w_m_place="任意球";
				$w_m_place_tw="任意球";
				$w_m_place_en="Free ball";
				$s_m_place="任意球";
				break;
			case "FGO":
				$w_m_place="乌龙球";
				$w_m_place_tw="乌龙球";
				$w_m_place_en="own goal";
				$s_m_place="乌龙球";
				break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='FG';
		$mtype=$rtype;
		break;
	case 32:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='首个进球时间-3项';
		$bet_type_tw="首个进球时间-3项";
		$bet_type_en="Order_Frist_Ball_In_Time_3P";
		$caption=$Order_FT.$Order_Frist_Ball_In_Time_3P.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "T3G1":
			$w_m_place="第26分钟或之前";
			$w_m_place_tw="第26分钟或之前";
			$w_m_place_en="26min  or before";
			$s_m_place="第26分钟或之前";
			break;
		case "T3G2":
			$w_m_place="第27分钟或之后";
			$w_m_place_tw="第27分钟或之后";
			$w_m_place_en="27min  or after";
			$s_m_place="第27分钟或之后";
			break;
		case "T3GN":
			$w_m_place="无进球";
			$w_m_place_tw="无进球";
			$w_m_place_en="Without a goal";
			$s_m_place="无进球";
			break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='T3G';
		$mtype=$rtype;
		break;
	case 33:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='首个进球时间';
		$bet_type_tw="首个进球时间";
		$bet_type_en="Order_Frist_Ball_In_Time";
		$caption=$Order_FT.$Order_Frist_Ball_In_Time.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
		case "T1G1":
			$w_m_place="上半场开场 - 14:59分钟";
			$w_m_place_tw="上半场开场 - 14:59分钟";
			$w_m_place_en="first start  - 14:59";
			$s_m_place="上半场开场 - 14:59分钟";
			break;
		case "T1G2":
			$w_m_place="15:00分钟 - 29:59分钟";
			$w_m_place_tw="15:00分钟 - 29:59分钟";
			$w_m_place_en="15min  -  29:59";
			$s_m_place="15:00分钟 - 29:59分钟";
			break;
		case "T1G3":
			$w_m_place="30:00分钟 - 半场";
			$w_m_place_tw="30:00分钟 - 半场";
			$w_m_place_en="30min  -  half end";
			$s_m_place="30:00分钟 - 半场";
			break;
		case "T1G4":
			$w_m_place="下半场开场 - 59:59分钟";
			$w_m_place_tw="下半场开场 - 59:59分钟";
			$w_m_place_en="sec start - 59:59";
			$s_m_place="下半场开场 - 59:59分钟";
			break;
		case "T1G5":
			$w_m_place="60:00分钟 - 74:59分钟";
			$w_m_place_tw="60:00分钟 - 74:59分钟";
			$w_m_place_en="60min  -  74:59min";
			$s_m_place="60:00分钟 - 74:59分钟";
			break;
		case "T1G6":
			$w_m_place="75:00分钟 - 全场完场";
			$w_m_place_tw="75:00分钟 - 全场完场";
			$w_m_place_en="75min - all end";
			$s_m_place="75:00分钟 - 全场完场";
			break;
		case "T1GN":
			$w_m_place="无进球";
			$w_m_place_tw="无进球";
			$w_m_place_en="Without a goal";
			$s_m_place="无进球";
			break;

		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='T1G';
		$mtype=$rtype;
		break;
	case 34:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='双重机会 & 进球 大 / 小';
		$bet_type_tw="双重机会 & 进球 大 / 小";
		$bet_type_en="Order_Chance_Double_And_OU";
		$caption=$Order_FT.$Order_Chance_Double_And_OU;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "DUAHO":	//进球 大 / 小 & 双方球队进球	 A
				$w_m_place=$w_mb_team.' / 和局  &  大 1.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  大 1.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Over 1.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_B1;
				break;	
			case "DUACO":	 
				$w_m_place=$w_tg_team.' / 和局  大 1.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  大 1.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Over 1.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_B1;
				break;		
			case "DUASO":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  大 1.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  大 1.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Over 1.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_B1;
				break;	
			case "DUAHU":	 
				$w_m_place=$w_mb_team.' / 和局  &  小 1.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  小 1.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Under 1.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_S1;
				break;
			case "DUACU":	
				$w_m_place=$w_tg_team.' / 和局  小 1.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  小 1.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Under 1.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_S1;
				break;	
			case "DUASU":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  小 1.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  小 1.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Under 1.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_S1;
				break;
			case "DUBHO":	// B
				$w_m_place=$w_mb_team.' / 和局  &  大 2.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  大 2.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Over 2.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_B2;
				break;	
			case "DUBCO":	 
				$w_m_place=$w_tg_team.' / 和局  大 2.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  大 2.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Over 2.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_B2;
				break;		
			case "DUBSO":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  大 2.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  大 2.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Over 2.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_B2;
				break;	
			case "DUBHU":	 
				$w_m_place=$w_mb_team.' / 和局  &  小 2.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  小 2.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Under 2.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_S2;
				break;
			case "DUBCU":	
				$w_m_place=$w_tg_team.' / 和局  小 2.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  小 2.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Under 2.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_S2;
				break;	
			case "DUBSU":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  小 2.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  小 2.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Under 2.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_S2;
				break;
			case "DUCHO":	//C
				$w_m_place=$w_mb_team.' / 和局  &  大 3.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  大 3.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Over 3.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_B2;
				break;	
			case "DUCCO":	 
				$w_m_place=$w_tg_team.' / 和局  大 3.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  大 3.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Over 3.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_B3;
				break;		
			case "DUCSO":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  大 3.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  大 3.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Over 3.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_B3;
				break;	
			case "DUCHU":	 
				$w_m_place=$w_mb_team.' / 和局  &  小 3.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  小 3.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Under 3.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_S3;
				break;
			case "DUCCU":	
				$w_m_place=$w_tg_team.' / 和局  小 3.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  小 3.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Under 3.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_S3;
				break;	
			case "DUCSU":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  小 3.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  小 3.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Under 3.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_S3;
				break;
			case "DUDHO":	//D
				$w_m_place=$w_mb_team.' / 和局  &  大 4.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  大 4.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Over 4.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_B4;
				break;	
			case "DUDCO":	 
				$w_m_place=$w_tg_team.' / 和局  大 4.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  大 4.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Over 4.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_B4;
				break;		
			case "DUDSO":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  大 4.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  大 4.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Over 4.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_B2;
				break;	
			case "DUDHU":	 
				$w_m_place=$w_mb_team.' / 和局  &  小 4.5 ';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  小 4.5 ';
				$w_m_place_en=$w_mb_team_en.' / Flat  And Under 4.5 ';
				$s_m_place=$s_mb_team.' / 和局  &  '.$U_S4;
				break;
			case "DUDCU":	
				$w_m_place=$w_tg_team.' / 和局  小 4.5';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  小 4.5';
				$w_m_place_en=$w_tg_team_en.' / Flat  And  Under 4.5';
				$s_m_place=$s_tg_team.' /和局  &  '.$U_S4;
				break;	
			case "DUDSU":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  小 4.5';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  &  小 4.5';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	Under 4.5';
				$s_m_place=$s_mb_team." / ".$s_tg_team." & ".$U_S4;
				break;
		}
		$abcdType = substr($rtype,2,1);
		$abcdTypeOU = substr($rtype,4,1);
		if($abcdType=="A") $grape=$abcdTypeOU."1.5";
		if($abcdType=="B") $grape=$abcdTypeOU."2.5";
		if($abcdType=="C") $grape=$abcdTypeOU."3.5";
		if($abcdType=="D") $grape=$abcdTypeOU."4.5";
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='DU';
		$mtype=$rtype;
		break;
	case 35:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='双重机会 & 双方球队进球';
		$bet_type_tw="双重机会 & 双方球队进球";
		$bet_type_en="Order_Chance_Double_And_Double_In";
		$caption=$Order_FT.$Order_Chance_Double_And_Double_In;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "DSHY":	
				$w_m_place=$w_mb_team.' / 和局 &  是';
				$w_m_place_tw=$w_mb_team_tw.'/ 和局  &  是';
				$w_m_place_en=$w_mb_team_en.'  /Flat  And  Yes';
				$s_m_place=$s_mb_team.'/ 和局   &  是';
				break;	
			case "DSCY":	 
				$w_m_place=$w_tg_team.' / 和局  &  是';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  &  是';
				$w_m_place_en=$w_tg_team_en.'  /Flat  And Yes';
				$s_m_place=$s_tg_team.' / 和局  &  是';
				break;		
			case "DSSY":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  & 是';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  & 是';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.' And Yes';
				$s_m_place=$s_mb_team." / ".$s_tg_team."  &  是";
				break;	
			case "DSHN":	 
				$w_m_place=$w_mb_team.' / 和局  &  不是';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  &  不是';
				$w_m_place_en=$w_mb_team_en.'  / Flat 	And	No';
				$s_m_place=$s_mb_team.' / 和局  &  不是';
				break;
			case "DSCN":	
				$w_m_place=$w_tg_team.' / 和局  &  不是';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  &  不是';
				$w_m_place_en=$w_tg_team_en.'  / Flat And	No';
				$s_m_place=$s_tg_team.' / 和局  &  不是';
				break;	
			case "DSSN":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  不是';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.' &  不是';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	No';
				$s_m_place=$s_mb_team." / ".$s_tg_team." &  不是";
				break;
		}
		
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='DS';
		$mtype=$rtype;
		break;	
	case 36:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='双重机会 & 最先进球';
		$bet_type_tw="双重机会 & 最先进球";
		$bet_type_en="Order_Chance_Double_And_Ball_In_First";
		$caption=$Order_FT.$Order_Chance_Double_And_Ball_In_First;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "DGHH":	
				$w_m_place=$w_mb_team.' / 和局 &  '.$w_mb_team.'(最先进球)';
				$w_m_place_tw=$w_mb_team_tw.'/ 和局  & '.$w_mb_team_tw.' (最先进球)';
				$w_m_place_en=$w_mb_team_en.'  /Flat '.$w_mb_team_en.' (And  Ball_In_First)';
				$s_m_place=$s_mb_team.'/ 和局   &  '.$s_mb_team.'(最先进球)';
				break;	
			case "DGCH":	 
				$w_m_place=$w_tg_team.' / 和局  &  '.$w_mb_team.'(最先进球)';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  &  '.$w_mb_team_tw.' (最先进球)';
				$w_m_place_en=$w_tg_team_en.'  /Flat  '.$w_mb_team_en.'(And Ball_In_First)';
				$s_m_place=$s_tg_team.' / 和局  &  '.$s_mb_team.'(最先进球)';
				break;		
			case "DGSH":	
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  & '.$w_mb_team.'(最先进球)';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.'  & '.$w_mb_team_tw.'(最先进球)';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.' And '.$w_mb_team_en.'(Ball_In_First)';
				$s_m_place=$s_mb_team." / ".$s_tg_team."  &  ".$s_mb_team."(最先进球)";
				break;	
			case "DGHC":	 
				$w_m_place=$w_mb_team.' / 和局  &  '.$w_tg_team.'(最先进球)';
				$w_m_place_tw=$w_mb_team_tw.' / 和局  & '.$w_tg_team_tw.'(最先进球)';
				$w_m_place_en=$w_mb_team_en.'  / Flat  And	 '.$w_tg_team_en.' (Ball_In_First)';
				$s_m_place=$s_mb_team.' / 和局  &  '.$s_tg_team.'(最先进球)';
				break;
			case "DGCC":	
				$w_m_place=$w_tg_team.' / 和局  &  '.$w_tg_team.'(最先进球)';
				$w_m_place_tw=$w_tg_team_tw.' / 和局  &  '.$w_tg_team_tw.'(最先进球)';
				$w_m_place_en=$w_tg_team_en.'  / Flat And  '.$w_tg_team_en.'(Ball_In_First)';
				$s_m_place=$s_tg_team.' / 和局  &  '.$s_tg_team.'(最先进球)';
				break;	
			case "DGSC":	 
				$w_m_place=$w_mb_team.' / '.$w_tg_team.'  &  '.$w_tg_team.'(最先进球)';
				$w_m_place_tw=$w_mb_team_tw.' / '.$w_tg_team_tw.' &  '.$w_tg_team_tw.'(最先进球)';
				$w_m_place_en=$w_mb_team_en.' / '.$w_tg_team_en.'	And	'.$w_tg_team_en.'Ball_In_First';
				$s_m_place=$s_mb_team." / ".$s_tg_team." &  ".$s_tg_team."(最先进球)";
				break;
		}
		
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='DG';
		$mtype=$rtype;
		break;
	case 37:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='进球 大 / 小 & 进球 单 / 双';
		$bet_type_tw="进球 大 / 小 & 进球 单 / 双";
		$bet_type_en="Order_OU_And_OE";
		$caption=$Order_FT.$Order_OU_And_OE;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "OUEAOO":	
				$w_m_place='单  &  大 1.5 ';
				$w_m_place_tw='单 &  大 1.5 ';
				$w_m_place_en=' Odd  And Over 1.5 ';
				$s_m_place=' 单 &  '.$U_B1;
				break;	
			case "OUEAOE":	 
				$w_m_place=' 双  &  大 1.5';
				$w_m_place_tw=' 双  &   大 1.5';
				$w_m_place_en=' Even  And  Over 1.5';
				$s_m_place='双  & '.$U_B1;
				break;		
			case "OUEAUO":	 
				$w_m_place='单  &  小 1.5 ';
				$w_m_place_tw='单   &  小 1.5 ';
				$w_m_place_en='Odd  And Under 1.5 ';
				$s_m_place='单  &  '.$U_S1;
				break;
			case "OUEAUE":	
				$w_m_place=' 双  &  小 1.5';
				$w_m_place_tw='双  &  小 1.5';
				$w_m_place_en=' Even  And  Under 1.5';
				$s_m_place='双  &  '.$U_S1;
				break;	
			case "OUEBOO":	
				$w_m_place='单  &  大 2.5 ';
				$w_m_place_tw='单 &  大 2.5 ';
				$w_m_place_en=' Odd  And Over 2.5 ';
				$s_m_place=' 单 &  '.$U_B2;
				break;	
			case "OUEBOE":	 
				$w_m_place=' 双  &  大 2.5';
				$w_m_place_tw=' 双  &   大 2.5';
				$w_m_place_en=' Even  And  Over 2.5';
				$s_m_place='双  & '.$U_B2;
				break;		
			case "OUEBUO":	 
				$w_m_place='单  &  小 2.5 ';
				$w_m_place_tw='单   &  小 2.5 ';
				$w_m_place_en='Odd  And Under 2.5 ';
				$s_m_place='单  &  '.$U_S2;
				break;
			case "OUEBUE":	
				$w_m_place=' 双  &  小 2.5';
				$w_m_place_tw='双  &  小 2.5';
				$w_m_place_en=' Even  And  Under 2.5';
				$s_m_place='双  &  '.$U_S2;
				break;	
			case "OUECOO":	
				$w_m_place='单  &  大 3.5 ';
				$w_m_place_tw='单 &  大 3.5 ';
				$w_m_place_en=' Odd  And Over 3.5 ';
				$s_m_place=' 单 &  '.$U_B3;
				break;	
			case "OUECOE":	 
				$w_m_place=' 双  &  大 3.5';
				$w_m_place_tw=' 双  &   大 3.5';
				$w_m_place_en=' Even  And  Over 3.5';
				$s_m_place='双  & '.$U_B3;
				break;		
			case "OUECUO":	 
				$w_m_place='单  &  小 3.5 ';
				$w_m_place_tw='单   &  小 3.5 ';
				$w_m_place_en='Odd  And Under 3.5 ';
				$s_m_place='单  &  '.$U_S3;
				break;
			case "OUECUE":	
				$w_m_place=' 双  &  小 3.5';
				$w_m_place_tw='双  &  小 3.5';
				$w_m_place_en=' Even  And  Under 3.5';
				$s_m_place='双  &  '.$U_S3;
				break;	
			case "OUEDOO":	
				$w_m_place='单  &  大 4.5 ';
				$w_m_place_tw='单 &  大 4.5 ';
				$w_m_place_en=' Odd  And Over 4.5 ';
				$s_m_place=' 单 &  '.$U_B4;
				break;	
			case "OUEDOE":	 
				$w_m_place=' 双  &  大 4.5';
				$w_m_place_tw=' 双  &   大 4.5';
				$w_m_place_en=' Even  And  Over 4.5';
				$s_m_place='双  & '.$U_B4;
				break;		
			case "OUEDUO":	 
				$w_m_place='单  &  小 4.5 ';
				$w_m_place_tw='单   &  小 4.5 ';
				$w_m_place_en='Odd  And Under 4.5 ';
				$s_m_place='单  &  '.$U_S4;
				break;
			case "OUEDUE":	
				$w_m_place=' 双  &  小 4.5';
				$w_m_place_tw='双  &  小 4.5';
				$w_m_place_en=' Even  And  Under 4.5';
				$s_m_place='双  &  '.$U_S4;
				break;		
		}
		$abcdType = substr($rtype,3,1);
		$abcdTypeOU = substr($rtype,4,1);
		if($abcdType=="A") $grape=$abcdTypeOU."1.5";
		if($abcdType=="B") $grape=$abcdTypeOU."2.5";
		if($abcdType=="C") $grape=$abcdTypeOU."3.5";
		if($abcdType=="D") $grape=$abcdTypeOU."4.5";
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='OUE';
		$mtype=$rtype;
		break;	
	case 38:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='进球 大 / 小 & 最先进球';
		$bet_type_tw="进球 大 / 小 & 最先进球";
		$bet_type_en="Order_OU_And_Ball_In_First";
		$caption=$Order_FT.$Order_OU_And_Ball_In_First;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "OUPAOH":	//A
				$w_m_place=$w_mb_team.'(最先进球)'.' &  大 1.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.' &  大 1.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.' And Over 1.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.' & '.$U_B1;
				break;	
			case "OUPAOC":	 
				$w_m_place=$w_tg_team.'(最先进球)'.' &  大 1.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.' &   大 1.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.' Even  And  Over 1.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'& '.$U_B1;
				break;		
			case "OUPAUH":	 
				$w_m_place=$w_mb_team.'(最先进球)'.' &  小 1.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.'   &  小 1.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.'  And Under 1.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.'  &  '.$U_S1;
				break;
			case "OUPAUC":	
				$w_m_place=$w_tg_team.'(最先进球)'.' &  小 1.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.'  &  小 1.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.'  And  Under 1.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'  &  '.$U_S1;
				break;
			case "OUPBOH":	//B
				$w_m_place=$w_mb_team.'(最先进球)'.' &  大 2.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.' &  大 2.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.' And Over 2.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.' & '.$U_B2;
				break;	
			case "OUPBOC":	 
				$w_m_place=$w_tg_team.'(最先进球)'.' &  大 2.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.' &   大 2.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.' Even  And  Over 2.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'& '.$U_B2;
				break;		
			case "OUPBUH":	 
				$w_m_place=$w_mb_team.'(最先进球)'.' &  小 2.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.'   &  小 2.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.'  And Under 2.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.'  &  '.$U_S2;
				break;
			case "OUPBUC":	
				$w_m_place=$w_tg_team.'(最先进球)'.' &  小 2.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.'  &  小 2.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.'  And  Under 2.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'  &  '.$U_S2;
				break;
			case "OUPCOH":	//C
				$w_m_place=$w_mb_team.'(最先进球)'.' &  大 3.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.' &  大 3.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.' And Over 3.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.' & '.$U_B3;
				break;	
			case "OUPCOC":	 
				$w_m_place=$w_tg_team.'(最先进球)'.' &  大 3.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.' &   大 3.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.' Even  And  Over 3.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'& '.$U_B3;
				break;		
			case "OUPCUH":	 
				$w_m_place=$w_mb_team.'(最先进球)'.' &  小 3.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.'   &  小 3.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.'  And Under 3.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.'  &  '.$U_S3;
				break;
			case "OUPCUC":	
				$w_m_place=$w_tg_team.'(最先进球)'.' &  小 3.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.'  &  小 3.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.'  And  Under 3.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'  &  '.$U_S3;
				break;	
			case "OUPDOH":	//D
				$w_m_place=$w_mb_team.'(最先进球)'.' &  大 4.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.' &  大 4.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.' And Over 4.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.' & '.$U_B4;
				break;	
			case "OUPDOC":	 
				$w_m_place=$w_tg_team.'(最先进球)'.' &  大 4.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.' &   大 4.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.' Even  And  Over 4.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'& '.$U_B4;
				break;		
			case "OUPDUH":	 
				$w_m_place=$w_mb_team.'(最先进球)'.' &  小 4.5 ';
				$w_m_place_tw=$w_mb_team_tw.' (最先进球)'.'   &  小 4.5 ';
				$w_m_place_en=$w_mb_team_en.' (And  Ball_In_First)'.'  And Under 4.5 ';
				$s_m_place=$s_mb_team.'(最先进球)'.'  &  '.$U_S4;
				break;
			case "OUPDUC":	
				$w_m_place=$w_tg_team.'(最先进球)'.' &  小 4.5';
				$w_m_place_tw=$w_tg_team_tw.'(最先进球)'.'  &  小 4.5';
				$w_m_place_en=$w_tg_team_en.'(Ball_In_First)'.'  And  Under 4.5';
				$s_m_place=$s_tg_team.'(最先进球)'.'  &  '.$U_S4;
				break;			
		}
		$abcdType = substr($rtype,3,1);
		if($abcdType=="A") $grape=1.5;
		if($abcdType=="B") $grape=2.5;
		if($abcdType=="C") $grape=3.5;
		if($abcdType=="D") $grape=4.5;
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='OUP';
		$mtype=$rtype;
		break;
     case 39:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='三项让球投注';
		$bet_type_tw="三项让球投注";
		$bet_type_en="Order_Ball_R_3";
		$caption=$Order_FT.$Order_Ball_R_3.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "W3H":
				$w_m_place=$w_mb_team." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw=$w_mb_team_tw." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en=$w_mb_team_en." ".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place=$s_mb_team." ".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "W3C":
				$w_m_place=$w_tg_team." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw=$w_tg_team_tw." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en=$w_tg_team_en." ".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place=$s_tg_team." ".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "W3N":
				$w_m_place="让球和局"." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="让球和局"." ".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="Ball  R  Flat"." ".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="让球和局"." ".$detailsData['ratio_'.strtolower($rtype)];
				break;
		}
		$Sign="VS";
		if($w_m_rate>1){
	    	$gwin=($w_m_rate-1)*$gold;
	    }else{
	    	$gwin=($w_m_rate)*$gold;	
	    }
	    if(strlen($showtype)==0 || $showtype=''){
	    	$showtype=$detailsData['strong'];
	    }
		$ptype='W3';
		$mtype=$rtype;
		break;
	case 40:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='落后反超获胜';
		$bet_type_tw="落后反超获胜";
		$bet_type_en="Order_Fall_Catchup_And_Win";
		$caption=$Order_FT.$Order_Fall_Catchup_And_Win.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "BHH":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				break;
			case "BHC":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='BH';
		$mtype=$rtype;
		break;
	case 41:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='赢得任一半场';
		$bet_type_tw="赢得任一半场";
		$bet_type_en="Order_Win_Any_Half";
		$caption=$Order_FT.$Order_Win_Any_Half.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "WEH":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				break;
			case "WEC":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='WE';
		$mtype=$rtype;
		break;
	case 42:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='赢得所有半场';
		$bet_type_tw="赢得所有半场";
		$bet_type_en="Order_Win_All_Half";
		$caption=$Order_FT.$Order_Win_All_Half.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "WBH":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				break;
			case "WBC":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				break;
		}
		$Sign="VS";
		if($w_m_rate>1){
			$gwin=($w_m_rate-1)*$gold;
		}else{
			$gwin=($w_m_rate)*$gold;	
		}
		$ptype='WE';
		$mtype=$rtype;
		break;	
	case 43:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	  	$bet_type='开球球队';
		$bet_type_tw="开球球队";
		$bet_type_en="Order_Team_First_Ball";
		$caption=$Order_FT.$Order_Team_First_Ball.$Order_betting_order;
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "TKH":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				break;
			case "TKC":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				break;
		}
		$Sign="VS";
		$gwin=($w_m_rate)*$gold;
		$ptype='TK';
		$mtype=$rtype;
		break;	
	case 44:	
	case 144:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		$teamKey = substr($wtype,strlen($wtype)-1,1);
		if($teamKey == "H"){
			if($rtype=="OUHO"||$rtype=="OUHU"){
				$bet_type='球队进球数'.' '.$w_mb_team.' -大/小';
				$bet_type_tw="球队进球数".' '.$w_mb_team_tw.' -大/小';
				$bet_type_en="Order_Team_Ball_In".' '.$w_mb_team_en.' -大/小';;
				$caption=$Order_FT.$Order_Team_Ball_In.' '.$w_mb_team.' -大/小 '.$Order_betting_order;	
			}else{
				$bet_type='半场球队进球数'.' '.$w_mb_team.' -大/小';
				$bet_type_tw="半场球队进球数".' '.$w_mb_team_tw.' -大/小';
				$bet_type_en="Half_Order_Team_Ball_In".' '.$w_mb_team_en.' -大/小';;
				$caption=$Order_FT.'半场'.$Order_Team_Ball_In.' '.$w_mb_team.' -大/小 '.$Order_betting_order;
			}
			$ptype='OUH';
		}elseif($teamKey == "C"){
			if($rtype=="OUCO"||$rtype=="OUCU"){
				$bet_type='球队进球数'.' '.$w_tg_team.' -大/小';
				$bet_type_tw="球队进球数".' '.$w_tg_team_tw.' -大/小';
				$bet_type_en="Order_Team_Ball_In".' '.$w_tg_team_en.' -大/小';;
				$caption=$Order_FT.$Order_Team_Ball_In.' '.$w_tg_team.' -大/小 '.$Order_betting_order;	
			}else{
				$bet_type='半场球队进球数'.' '.$w_tg_team.' -大/小';
				$bet_type_tw="半场球队进球数".' '.$w_tg_team_tw.' -大/小';
				$bet_type_en="Half_Order_Team_Ball_In".' '.$w_tg_team_en.' -大/小';;
				$caption=$Order_FT.'半场'.$Order_Team_Ball_In.' '.$w_tg_team.' -大/小 '.$Order_betting_order;
			}
			$ptype='OUC';	
		}
		$w_m_rate=change_rate($open,$ioradio_r_h);
		switch ($rtype){
			case "OUHO":
				$w_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="OVER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "OUHU":
				$w_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="UNDER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "OUCO":
				$w_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="OVER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "OUCU":
				$w_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="UNDER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "HOUHO":
				$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
				$w_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="OVER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "HOUHU":
				$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
				$w_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="UNDER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "HOUCO":
				$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
				$w_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="OVER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="大&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;
			case "HOUCU":
				$btype="-&nbsp;<font color=red><b>[$Order_1st_Half]</b></font>";
				$w_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en="UNDER&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place="小&nbsp;".$detailsData['ratio_'.strtolower($rtype)];
				break;	
		}
		$grape = $detailsData['ratio_'.strtolower($rtype)];
		$Sign="VS";
		$gwin=$w_m_rate*$gold;
		$mtype=$rtype;
		break;
	case 51:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		if(substr($_REQUEST['wtype'],0,1)=="A"){ $gametype=$U_74.':'.$U_74A.'-'.$U_M; }
		if(substr($_REQUEST['wtype'],0,1)=="B"){ $gametype=$U_74.':'.$U_74B.'-'.$U_M; }
		if(substr($_REQUEST['wtype'],0,1)=="C"){ $gametype=$U_74.':'.$U_74C.'-'.$U_M; }
		if(substr($_REQUEST['wtype'],0,1)=="D"){ $gametype=$U_74.':'.$U_74D.'-'.$U_M; }
		if(substr($_REQUEST['wtype'],0,1)=="E"){ $gametype=$U_74.':'.$U_74E.'-'.$U_M; }
		if(substr($_REQUEST['wtype'],0,1)=="F"){ $gametype=$U_74.':'.$U_74F.'-'.$U_M; }
	  	$bet_type=$gametype;
		$bet_type_tw=$gametype;
		$bet_type_en="15min single win";
		$caption=$Order_FT." ".$gametype;
		if($detailsData["sw_".$wtype]=="Y" && $detailsData["ior_".$wtype."H"]>0 && $detailsData["ior_".$wtype."C"]>0 && $detailsData["ior_".$wtype."N"]>0){
			$ior_Rate_H = $detailsData["ior_".$wtype."H"]; 
			$ior_Rate_C = $detailsData["ior_".$wtype."C"]; 
			$ior_Rate_N = $detailsData["ior_".$wtype."N"]; 
		}
		switch ($type){
			case "H":
				$w_m_place=$w_mb_team;
				$w_m_place_tw=$w_mb_team_tw;
				$w_m_place_en=$w_mb_team_en;
				$s_m_place=$s_mb_team;
				$w_m_rate=change_rate($open,$ior_Rate_H);
				break;
			case "C":
				$w_m_place=$w_tg_team;
				$w_m_place_tw=$w_tg_team_tw;
				$w_m_place_en=$w_tg_team_en;
				$s_m_place=$s_tg_team;
				$w_m_rate=change_rate($open,$ior_Rate_C);
				break;
			case "N":
				$w_m_place="和局";
				$w_m_place_tw="和局";
				$w_m_place_en="Flat";
				$s_m_place=$Draw;
				$w_m_rate=change_rate($open,$ior_Rate_N);
				break;
		}
		$Sign="VS";
		$grape="";
		$gwin=($w_m_rate)*$gold;
		$ptype='15M';
		$mtype=$rtype;
		break;	
	case 52:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
		if(substr($_REQUEST['wtype'],0,1)=="A"){ $gametype=$U_74.':'.$U_74A.'-'.$U_R; }
		if(substr($_REQUEST['wtype'],0,1)=="B"){ $gametype=$U_74.':'.$U_74B.'-'.$U_R; }
		if(substr($_REQUEST['wtype'],0,1)=="C"){ $gametype=$U_74.':'.$U_74C.'-'.$U_R; }
		if(substr($_REQUEST['wtype'],0,1)=="D"){ $gametype=$U_74.':'.$U_74D.'-'.$U_R; }
		if(substr($_REQUEST['wtype'],0,1)=="E"){ $gametype=$U_74.':'.$U_74E.'-'.$U_R; }
		if(substr($_REQUEST['wtype'],0,1)=="F"){ $gametype=$U_74.':'.$U_74F.'-'.$U_R; }
	  	$bet_type = $gametype;
		$bet_type_tw= $gametype;
		$bet_type_en=$gametype;	
		$caption=$Order_FT.' '.$gametype;
		if($detailsData["sw_".$wtype]=="Y" && $detailsData["ior_".$wtype."H"]>0 && $detailsData["ior_".$wtype."C"]>0){
			$ior_Rate_H = $detailsData["ior_".$wtype."H"]; 
			$ior_Rate_C = $detailsData["ior_".$wtype."C"]; 
		}
		$rate = get_other_ioratio($odd_f_type,$ior_Rate_H,$ior_Rate_C,100);
		
		switch ($type){
		case "H":
			$w_m_place=$w_mb_team;
			$w_m_place_tw=$w_mb_team_tw;
			$w_m_place_en=$w_mb_team_en;
			$s_m_place=$s_mb_team;
			$s_m_place.=$strong=="H" ? " ".$detailsData['ratio_'.strtolower($wtype)]:'';
			$w_m_rate=change_rate($open,$rate[0]);
			$grape=$strong=="H" ? $detailsData['ratio_'.strtolower($wtype)]:'';
			break;
		case "C":
			$w_m_place=$w_tg_team;
			$w_m_place_tw=$w_tg_team_tw;
			$w_m_place_en=$w_tg_team_en;
			$s_m_place=$s_tg_team;
			$s_m_place.=$strong=="C" ? " ".$detailsData['ratio_'.strtolower($wtype)]:'';
			$w_m_rate=change_rate($open,$rate[1]);
			$grape=$strong=="C" ? $detailsData['ratio_'.strtolower($wtype)]:'';
			break;
		}
		$ptype='15R';
		$mtype=$rtype;
		break;		
	case 53:
		$turn_url="/app/member/FT_order/FT_order_single.php?gid=".$gid."&uid=".$uid."&rtype=".$rtype."&wtype=".$wtype."&odd_f_type=".$odd_f_type."&langx=".$langx."&flag=".$_REQUEST['flag']."&id=".$_REQUEST['id'];
	    if(substr($_REQUEST['wtype'],0,1)=="A"){ $gametype=$U_74.':'.$U_74A.'-'.$U_OU; }
		if(substr($_REQUEST['wtype'],0,1)=="B"){ $gametype=$U_74.':'.$U_74B.'-'.$U_OU; }
		if(substr($_REQUEST['wtype'],0,1)=="C"){ $gametype=$U_74.':'.$U_74C.'-'.$U_OU; }
		if(substr($_REQUEST['wtype'],0,1)=="D"){ $gametype=$U_74.':'.$U_74D.'-'.$U_OU; }
		if(substr($_REQUEST['wtype'],0,1)=="E"){ $gametype=$U_74.':'.$U_74E.'-'.$U_OU; }
		if(substr($_REQUEST['wtype'],0,1)=="F"){ $gametype=$U_74.':'.$U_74F.'-'.$U_OU; }
		$bet_type=$gametype;
		$bet_type_tw=$gametype;
		$bet_type_en="15min Over/Under";
		$caption=$Order_FT." ".$gametype;
		if($detailsData["sw_".$wtype]=="Y" && $detailsData["ior_".$wtype."O"]>0 && $detailsData["ior_".$wtype."U"]>0){
			$ior_Rate_O = $detailsData["ior_".$wtype."O"]; 
			$ior_Rate_U = $detailsData["ior_".$wtype."U"]; 
		}
		$rate=get_other_ioratio($odd_f_type,$ior_Rate_O,$ior_Rate_U,100);
		switch ($type){
			case "O":
				$w_m_place='大&nbsp'.$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw='大&nbsp'.$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en='Over&nbsp;'.$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place=$w_m_place;
				$w_m_rate=change_rate($open,$rate[0]);
				break;
			case "U":
				$w_m_place='小&nbsp;'.$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_tw='小&nbsp;'.$detailsData['ratio_'.strtolower($rtype)];
				$w_m_place_en='Under&nbsp;'.$detailsData['ratio_'.strtolower($rtype)];
				$s_m_place=$w_m_place;
				$w_m_rate=change_rate($open,$rate[1]);
				break;
		}
		$Sign="VS";
		if ($odd_f_type=='H'){
		    $gwin=($w_m_rate)*$gold;
		}else if ($odd_f_type=='M' or $odd_f_type=='I'){
		    if ($w_m_rate<0){
				$gwin=$gold;
			}else{
				$gwin=($w_m_rate)*$gold;
			}
		}else if ($odd_f_type=='E'){
		    $gwin=($w_m_rate-1)*$gold;
		}
		$ptype='15OU';	
		$mtype=$rtype;
		$grape=$detailsData['ratio_'.strtolower($rtype)];		
		break;	
	}
	
	if ($line==11 or $line==12 or $line==13 or $line==14 or $line==15 or $line==144 or $line==165){
		$bottom1_cn="-&nbsp;<font color=#666666>[上半]</font>&nbsp;";
		$bottom1_tw="-&nbsp;<font color=#666666>[上半]</font>&nbsp;";
		$bottom1_en="-&nbsp;<font color=#666666>[1st Half]</font>&nbsp;";
	}

	if(in_array($line,array(2,12)) && trim($grape) == "" ){
        echo attention("让球参数异常,请刷新赛事!",$uid,$langx);
        exit();
    }

    if(in_array($line,array(44,144)) &&  (trim($grape) == "" || trim($grape) <= 0)  ){
        echo attention("大小球数参数异常,请刷新赛事!",$uid,$langx);
        exit();
    }

	if( $w_m_rate!=change_rate($open,$ioradio_r_h) ){
	    $turn_url=$turn_url.'&error_flag=1';
	    echo "<script language='javascript'>self.location='$turn_url';</script>";
	    exit();
	}
		
	if(!isset($_REQUEST['autoOdd']) || $_REQUEST['autoOdd']!="Y"){
		if($w_m_rate!=$_REQUEST['ioradio_r_h']){
			$turn_url=$turn_url.'&error_flag=1';
			echo "<script language='javascript'>self.location='$turn_url';</script>";
			exit;
		}	
	}
	
	if(strlen($w_m_rate)==0){
		echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
		exit;
	}
	
	$oddstype=$odd_f_type;
	$s_m_place=filiter_team(trim($s_m_place));
	
	$w_mid="<br>[".$row['MB_MID']."]vs[".$row['TG_MID']."]<br>";
	$lines=$row['M_League'].$w_mid.$w_mb_team."&nbsp;&nbsp;<FONT COLOR=#0000BB><b>".$Sign."</b></FONT>&nbsp;&nbsp;".$w_tg_team."<br>";
	$lines=$lines."<FONT color=#cc0000>".$w_m_place."</FONT>&nbsp;".$bottom1_cn."@&nbsp;<FONT color=#cc0000><b>".$w_m_rate."</b></FONT>";	
	$lines_tw=$row['M_League_tw'].$w_mid.$w_mb_team_tw."&nbsp;&nbsp;<FONT COLOR=#0000BB><b>".$Sign."</b></FONT>&nbsp;&nbsp;".$w_tg_team_tw."<br>";
	$lines_tw=$lines_tw."<FONT color=#cc0000>".$w_m_place_tw."</FONT>&nbsp;".$bottom1_tw."@&nbsp;<FONT color=#cc0000><b>".$w_m_rate."</b></FONT>";
	$lines_en=$row['M_League_en'].$w_mid.$w_mb_team_en."&nbsp;&nbsp;<FONT COLOR=#0000BB><b>".$Sign."</b></FONT>&nbsp;&nbsp;".$w_tg_team_en."<br>";
	$lines_en=$lines_en."<FONT color=#cc0000>".$w_m_place_en."</FONT>&nbsp;".$bottom1_en."@&nbsp;<FONT color=#cc0000><b>".$w_m_rate."</b></FONT>";	
	
$ip_addr = get_ip();

$psql = "select A_Point,B_Point,C_Point,D_Point from ".DBPREFIX."web_agents_data where UserName='$agents'";
$result = mysqli_query($dbLink,$psql);
$prow = mysqli_fetch_assoc($result);
$a_point=$prow['A_Point']+0;
$b_point=$prow['B_Point']+0;
$c_point=$prow['C_Point']+0;
$d_point=$prow['D_Point']+0;

$showVoucher= show_voucher($wtype);

$begin = mysqli_query($dbMasterLink,"start transaction");
$lockResult = mysqli_query($dbMasterLink,"select Money from ".DBPREFIX.MEMBERTABLE." where ID = ".$memid." for update");
if($begin && $lockResult){
    $checkRow = mysqli_fetch_assoc($lockResult);
        $HMoney=$Money=$checkRow['Money'];
        $havemoney=$HMoney-$gold;
        if($havemoney < 0 || $gold<=0 || $HMoney<=0){
            mysqli_query($dbMasterLink,"ROLLBACK");
            echo attention("$User_insufficient_balance",$uid,$langx);
            exit();
        }
    $sql = "INSERT INTO ".DBPREFIX."web_report_data	(MID,Glost,playSource,testflag,Userid,Type,Active,orderNo,LineType,Mtype,M_Date,BetTime,BetScore,Middle,Middle_tw,Middle_en,BetType,BetType_tw,BetType_en,M_Place,M_Rate,M_Name,Gwin,OpenType,OddsType,ShowType,Agents,World,Corprator,Super,Admin,A_Point,B_Point,C_Point,D_Point,BetIP,Ptype,Gtype,CurType,Ratio,MB_MID,TG_MID,Pay_Type,MB_Ball,TG_Ball) values ('$gid',$Money,$play_Source,$test_flag,$memid,'$wtype','$active','$showVoucher','$line','$mtype','$m_date','$bettime','$gold','$lines','$lines_tw','$lines_en','$bet_type','$bet_type_tw','$bet_type_en','$grape','$w_m_rate','$memname','$gwin','$open','$oddstype','$showtype','$agents','$world','$corprator','$super','$admin','$a_point','$b_point','$c_point','$d_point','$ip_addr','$ptype','FT','$w_current','$w_ratio','$w_mb_mid','$w_tg_mid','$pay_type','$mb_ball','$tg_ball')";
    $insertBet=mysqli_query($dbMasterLink,$sql);
    if($insertBet){
        $lastId=mysqli_insert_id($dbMasterLink);
        $moneyLogRes=addAccountRecords(array($memid,$memname,$test_flag,$Money,$gold*-1,$havemoney,1,1,$lastId,"FT投注$mtype"));
        if($moneyLogRes){
            $sql1 = "update ".DBPREFIX.MEMBERTABLE." set Money=".$havemoney." , Online=1 , OnlineTime=now() where ID=".$memid;
            $updateMoney=mysqli_query($dbMasterLink,$sql1);
            if($updateMoney){
                mysqli_query($dbMasterLink,"COMMIT");
            }else{
                mysqli_query($dbMasterLink,"ROLLBACK");
                die("操作失败3");
            }
        }else{
            mysqli_query($dbMasterLink,"ROLLBACK");
            die("操作失败2");
        }
    }else{
        mysqli_query($dbMasterLink,"ROLLBACK");
        die("操作失败1");
    }
}else{
    mysqli_query($dbMasterLink,"ROLLBACK");
    die("操作失败0");
}

if ($active==11){
	$caption=str_replace($Order_FT,$Order_FT.$Order_Early_Market,$caption);
}

// echo attention("$Order_OK");exit;
?>
<html>
<head>
<meta http-equiv='Content-Type' content="text/html; charset=utf-8">
<script charset="utf-8" type="text/javascript" src="../../../js/jquery.js" ></script>
<script language=javascript>
    // console.log(this.location.href);  // 顶级窗口的地址  http://www.hg3088_jn.lcn/app/member/FT_order/FT_order_finish.php
    // console.log(top.location.href);  // 当前窗口的地址  http://www.hg3088_jn.lcn/
//window.setTimeout('sendsubmit()',500);
//function sendsubmit(){
//alert('<?php //echo $Order_Please_check_transaction_record?>//');
//
//}
    // 生成交易注单， 再次传参请求生成交易单，用于截图
    //$caption ,$Order_Bet_success,$showVoucher,$s_sleague,$btype  ,
    // $row["M_Date"] , $s_mb_team, $Sign, $s_tg_team,$s_m_place，
    // $w_m_rate, $Order_Bet_Amount , $gold,
</script>

<link rel="stylesheet" href="/style/member/mem_order<?php echo $css?>.css?v=<?php echo AUTOVER; ?>" type="text/css">
<!--<script>window.setTimeout("self.location='../select.php?uid=<?php/*=$uid*/?>'", 45000);</script>-->
</head>
<body id="OFIN" class="order_finish_<?php echo TPL_FILE_NAME;?>" onSelectStart="self.event.returnValue=false" oncontextmenu="self.event.returnValue=false;window.event.returnValue=false;">
  <div class="ord">
    <span><h1><?php echo $caption?></h1></span>
    <div id="info">
    <!--<p><?php/*=$Order_Login_Name*/?><?php/*=$memname*/?></p>-->
  	<!--<p class="mem-can-use"><?php/*=$Order_Credit_line*/?><?php/*=$havemoney*/?></p>-->
  	<div class="fin_title">
        <p class="fin_acc">成功提交注单！</p>
        <p class="p-underline"><?php echo $Order_Bet_success?>&nbsp;<?php echo $showVoucher;?></p>
        <p class="error">危险球 - 待确认</p>
    </div>
  	    <p class="team">
        <?php echo $s_sleague?>&nbsp;<?php echo $btype?>&nbsp;
        <?php echo date('m-d',strtotime($row["M_Date"]))?><BR>
        <?php echo $s_mb_team?>&nbsp;&nbsp;<font color=#cc0000><?php echo $Sign?></font>&nbsp;&nbsp;<?php echo $s_tg_team?><br><em><?php echo $s_m_place?></em>&nbsp;@&nbsp;<em><strong><?php echo $w_m_rate?></strong></em></p>
  	    <p class="deal-money"><?php echo $Order_Bet_Amount?><?php echo $gold?></p>
	<!--<p class="canwin-money"><?php/*=$Order_Estimated_Payout*/?><FONT id=pc color=#cc0000><?php/*=$gwin*/?></FONT></p>-->
    </div>
    <p class="foot">
      <input type="button" name="FINISH" value="<?php echo $Order_Quit?>" onClick="parent.close_bet();" class="no">
      <input type="button" name="PRINT" value="<?php echo $Order_Print?>" onClick="window.print()" class="yes">
    </p>
  </div>
</body>

<?php

    // 确定交易生成图片开关
    if(GENERATE_IMA_SWITCH) {
        // 需要参数

        $data = array(
            'caption' => $caption, //标题
            'Order_Bet_success' => $Order_Bet_success, //交易成功单号
            'showVoucher' => $showVoucher, //单号
            's_sleague' => $s_sleague,  //联盟处理:联盟样式和显示的样式
            'btype' => $btype,
            'M_Date' => date('m-d',strtotime($row["M_Date"])), //日期
            'Sign' => $Sign,
            's_mb_team' => $s_mb_team,   // 主队
            's_tg_team' => $s_tg_team,  // 客队
            's_m_place' => $s_m_place,  // 选择所属队
            'w_m_rate' => $w_m_rate,  // 赔率
            'Order_Bet_Amount' => $Order_Bet_Amount,  // 交易金额：
            'gold' => $gold, //20
            'Order_Quit' => $Order_Quit, //关闭
            'Order_Print' => $Order_Print, //列印
            'userid' => $memid,
            'playSource' => $play_Source,  //'投注来源:0未知,1pc旧版,2pc新版,3苹果,4安卓,22 综合版',
        );

        $redisObj = new Ciredis();
        $redisObj->setOne($showVoucher,serialize($data));
        $redisObj->pushMessage('general_order_image',$showVoucher);

        // 订单成功后，传参，生成图片
//         transferOrderToImage($data);
    }
?>

</html>   
<?php
}
?>
