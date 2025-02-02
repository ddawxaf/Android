﻿<?php
session_start();
include ("../include/address.mem.php");
require ("../include/config.inc.php");
require ("../include/define_function_list.inc.php");


checkAdminLogin(); // 同一账号不能同时登陆

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" || !isset($_SESSION['is_admin']) || $_SESSION['is_admin'] != ADMINLOGINFLAG ) {
    echo "<script>alert('您的登录信息已过期,请重新登录!');top.location.href='/';</script>";
    exit;
}
$langx=$_REQUEST["langx"];
$loginname=$_SESSION['UserName'];
$lv = isset($_POST['lv'])? $_POST['lv']:""; // 管理员层级
require ("../include/traditional.$langx.inc.php");
$uid=$_REQUEST["uid"];
$id=$_REQUEST['id'];
$confirmed=$_REQUEST['confirmed'];
$seconds=$_REQUEST["seconds"];
$date_start=$_REQUEST['date_start'];
if ($seconds==''){
    $seconds=180;
}
if ($date_start=='') {
    $date_start=date('Y-m-d');
}
$memname=$_REQUEST['memname'];
if ($memname!=''){
    $n_sql="and M_Name='".trim($memname)."'";
}else{
    $n_sql='';
}
$music=$_REQUEST['music'];

if ($music==''){
    $music='0';
}
if ($music==''){
    $mu='';
}else if ($music=='1'){
    $mu='msg.wav';
}else if ($music=='2'){
    $mu='chimes.wav';
}
$page=$_REQUEST['page'];
if ($page==''){
    $page=0;
}
$sort=$_REQUEST['sort'];
if ($sort==''){
    $sort='BetTime';
}
if ($sort=='color'){
    $sort='color';
}
if ($sort=='Cancel'){
    $cancel='and Cancel=1';
}else if ($sort=='Danger'){
    $cancel='and Danger=1';
}
if ($orderby==''){
    $orderby='desc';
}
$ball=$_REQUEST['ball'];
if ($ball==''){
    $match='';
}else{
    $match="Active='$ball' and";
}
$key=$_REQUEST['key'];
//取消的注单
if ($key=='cancel'){
    $id=$_REQUEST['id'];
    $beginFrom = mysqli_query($dbMasterLink,"start transaction");
    if($beginFrom){
        $rsql = "select userid,M_Name,Pay_Type,BetScore,M_Result,Cancel from ".DBPREFIX."web_report_data where id=$id and Pay_Type=1 for update";
        $rresult = mysqli_query($dbMasterLink,$rsql);
        $rrow = mysqli_fetch_assoc($rresult);
        if($rrow['Cancel']==0){
            $userid=$rrow['userid'];
            $username=$rrow['M_Name'];
            $betscore=$rrow['BetScore'];
            $m_result=$rrow['M_Result'];
            $resultMem = mysqli_query($dbMasterLink,"select ID,UserName,test_flag,Money from  ".DBPREFIX.MEMBERTABLE." where ID=$userid for update");
            if($resultMem){
                $rowMem = mysqli_fetch_assoc($resultMem);
                $descCancel='Score'.$confirmed*-1;
                if($m_result==''){//未结算
                    $moneyLog=$betscore;
                    $moneyDesLog=$$descCancel.'：退回用户投注金额';
                    $u_sql = "update ".DBPREFIX.MEMBERTABLE." set Money=Money+$betscore where ID='$userid' and Pay_Type=1";
                }else{//已结算
                    $moneyLog=$m_result*-1;
                    if($m_result==0){
                        $moneyDesLog=$$descCancel.'：和局,无资金变化';
                    }elseif($m_result>0){
                        $moneyDesLog=$$descCancel."：取消派彩,平台入款{$m_result}";
                    }elseif($m_result<0){
                        if($m_result==$betscore*-1){
                            $moneyDesLog=$$descCancel."：退回用户投注金额";
                        }else{
                            $moneyDesLog=$$descCancel."：取消派彩,平台入款{$m_result}";
                        }
                    }
                    $u_sql = "update ".DBPREFIX.MEMBERTABLE." set Money=Money-$m_result where ID='$userid' and Pay_Type=1";
                }
                $updateRes = mysqli_query($dbMasterLink,$u_sql);
                if($updateRes){
                    $sql="update ".DBPREFIX."web_report_data set VGOLD=0,M_Result=0,A_Result=0,B_Result=0,C_Result=0,D_Result=0,T_Result=0,Cancel=1,Confirmed='$confirmed',Danger=0,Checked=1,updateTime='".date('Y-m-d H:i:s',time())."' where id='$id'";
                    if(mysqli_query($dbMasterLink,$sql)){
                        $moneyDesLog="[滚球注单]".$moneyDesLog.",操作人:{$loginname}";
                        $moneyLogRes=addAccountRecords(array($userid,$username,$rowMem['test_flag'],$rowMem['Money'],$moneyLog,$rowMem['Money']+$moneyLog,2,6,$id,$moneyDesLog));
                        /* 插入系统日志 */
                        if($moneyLogRes){
                            mysqli_query($dbMasterLink,"COMMIT");
                            $loginfo = $loginname.' 对会员帐号 <font class="green">'.$username.'</font> 注单进行了 <font class="red">取消订单</font>操作,id 为 <font class="red">'.$id.'</font>';
                            innsertSystemLog($loginname,$lv,$loginfo);
                        }else{
                            mysqli_query($dbMasterLink,"ROLLBACK");
                            echo "<script>alert('用户资金账变添加失败！');</script>";
                        }
                    }else{
                        mysqli_query($dbMasterLink,"ROLLBACK");
                        echo "<script>alert('订单更新失败！');</script>";
                    }
                }else{
                    mysqli_query($dbMasterLink,"ROLLBACK");
                    echo "<script>alert('用户资金账户更新失败！');</script>";
                }
            }else{
                mysqli_query($dbMasterLink,"ROLLBACK");
                echo "<script>alert('用户资金锁定失败！');</script>";
            }
        }else{
            mysqli_query($dbMasterLink,"ROLLBACK");
            echo "<script>alert('订单已被取消,不能重复操作！');</script>";
        }
    }else{
        mysqli_query($dbMasterLink,"ROLLBACK");
        echo "<script>alert('事务开启失败！');</script>";
    }
    echo "<script languag='JavaScript'>self.location='re_list.php?uid=$uid&langx=$langx&&seconds=$seconds&music=$music&memname=$memname&date_start=$date_start&page=$page&sort=$sort&ball=$ball&type=$type'</script>";
}

//恢复取消的注单
if ($key=='resume'){
    $id=$_REQUEST['id'];
    $beginFrom = mysqli_query($dbMasterLink,"start transaction");
    if($beginFrom){
        $rsql = "select userid,M_Name,Pay_Type,BetScore,M_Result,Checked,Cancel from ".DBPREFIX."web_report_data where ID=$id and Pay_Type=1 for update";
        $rresult = mysqli_query($dbMasterLink,$rsql);
        if($rresult){
            $rrow = mysqli_fetch_array($rresult);
            $userid=$rrow['userid'];
            $username=$rrow['M_Name'];
            $betscore=$rrow['BetScore'];
            $m_result=$rrow['M_Result'];
            if($rrow['Checked']==1){
                $resultMem = mysqli_query($dbMasterLink,"select ID,UserName,test_flag,Money from  ".DBPREFIX.MEMBERTABLE." where ID=$userid for update");
                if($resultMem){
                    $cash=$betscore+$m_result;
                    if(mysqli_query($dbMasterLink,"update ".DBPREFIX.MEMBERTABLE." SET Money=Money-$cash where ID='$userid' and Pay_Type=1")){
                        $rowMem = mysqli_fetch_assoc($resultMem);
                        $moneyLogRes=addAccountRecords(array($userid,$username,$rowMem['test_flag'],$rowMem['Money'],$cash*-1,$rowMem['Money']-$cash,5,6,$id,"[滚球注单],操作人:{$loginname}"));
                        if($moneyLogRes){
                            $sql="update ".DBPREFIX."web_report_data set VGOLD='',M_Result='',A_Result='',B_Result='',C_Result='',D_Result='',T_Result='',Cancel=0,Confirmed=0,Danger=0,Checked=0,updateTime='".date('Y-m-d H:i:s',time())."' where id='$id'";
                            if(mysqli_query($dbMasterLink,$sql)){
                                mysqli_query($dbMasterLink,"COMMIT");
                                /* 插入系统日志 */
                                $loginfo = $loginname.' 对会员帐号 <font class="green">'.$username.'</font> 注单进行了 <font class="red">注单恢复</font>操作,id 为 <font class="red">'.$id.'</font>';
                                innsertSystemLog($loginname,$lv,$loginfo);
                            }else{
                                mysqli_query($dbMasterLink,"ROLLBACK");
                                echo "<script>alert('订单状态更新失败！');</script>";
                            }
                        }else{
                            mysqli_query($dbMasterLink,"ROLLBACK");
                            echo "<script>alert('用户资金账变添加失败！');</script>";
                        }
                    }else{
                        mysqli_query($dbMasterLink,"ROLLBACK");
                        echo "<script>alert('用户资金账户更新失败！');</script>";
                    }
                }else{
                    mysqli_query($dbMasterLink,"ROLLBACK");
                    echo "<script>alert('用户资金锁定失败！');</script>";
                }
            }else{
                $sql="update ".DBPREFIX."web_report_data set VGOLD='',M_Result='',A_Result='',B_Result='',C_Result='',D_Result='',T_Result='',Cancel=0,Confirmed=0,Danger=0,Checked=0,updateTime='".date('Y-m-d H:i:s',time())."' where id='$id'";
                if(mysqli_query($dbMasterLink,$sql)){
                    mysqli_query($dbMasterLink,"COMMIT");
                    /* 插入系统日志 */
                    $loginfo = $loginname.' 对会员帐号 <font class="green">'.$username.'</font> 注单进行了 <font class="red">'.$actionname.'</font>操作,id 为 <font class="red">'.$id.'</font>,gid 为 <font class="red">'.$gid.'</font> ,单号为 <font class="blue">'.$oder_number.'</font>' ;
                    innsertSystemLog($loginname,$lv,$loginfo);
                }else{
                    mysqli_query($dbMasterLink,"ROLLBACK");
                    echo "<script>alert('订单状态更新失败！');</script>";
                }
            }
        }else{
            mysqli_query($dbMasterLink,"ROLLBACK");
            echo "<script>alert('订单锁定失败！');</script>";
        }
    }else{
        mysqli_query($dbMasterLink,"ROLLBACK");
        echo "<script>alert('事务开启失败！');</script>";
    }
    echo "<script languag='JavaScript'>self.location='re_list.php?uid=$uid&langx=$langx&&seconds=$seconds&music=$music&memname=$memname&date_start=$date_start&page=$page&sort=$sort&ball=$ball&type=$type'</script>";
}

$mysql="update ".DBPREFIX."web_system_data set udp_ft_time=".$_REQUEST['FT'].",udp_time_ft=".$_REQUEST['FT1'].",udp_djft_time=".$_REQUEST['DJFT'].",udp_time_djft=".$_REQUEST['DJFT1'].",udp_bs_time=".$_REQUEST['BS'].",udp_time_bs=".$_REQUEST['BS1'].",udp_op_time=".$_REQUEST['OP'].",udp_time_op=".$_REQUEST['OP1'];
mysqli_query($dbMasterLink,$mysql);

$tsql = "select udp_ft_time,udp_time_ft,udp_djft_time,udp_time_djft,udp_bs_time,udp_time_bs,udp_op_time,udp_time_op from ".DBPREFIX."web_system_data";
$result = mysqli_query($dbLink,$tsql);
$trow = mysqli_fetch_assoc($result);

$mDate=date('Y-m-d');
$sql = "select ID,MID,Active,LineType,Pay_Type,M_Date,BetTime,$middle as Middle,$bettype as BetType,BetScore,M_Name,Gwin,M_Result,OpenType,OddsType,ShowType,Cancel,MB_ball,TG_ball,Confirmed,Danger from ".DBPREFIX."web_report_data where $match M_Date='$date_start' $n_sql and (LineType=9 or LineType=19 or LineType=10 or LineType=20 or LineType=21 or LineType=31) $cancel order by $sort desc";
//echo $sql;
$result = mysqli_query($dbLink,$sql);
$cou=mysqli_num_rows($result);
$page_size=50;
$page_count=ceil($cou/$page_size);
$offset=$page*$page_size;
$mysql=$sql."  limit $offset,$page_size;";
$result = mysqli_query($dbLink,$mysql);
?>
    <html>
    <head>
        <TITLE></TITLE>
        <meta http-equiv=Content-Type content="text/html; charset=utf-8">
        <link rel="stylesheet" href="/style/agents/control_main.css?v=<?php echo AUTOVER; ?>" type="text/css">
        <meta content="Microsoft FrontPage 4.0" name=GENERATOR>
        <style>
            input.za_text{ width: auto;}
        </style>

    </head>
    <body  onLoad="onLoad();auto_refresh()">
    <dl class="main-nav">
        <dt>滚球注单</dt>
        <dd> </dd>
    </dl>
    <div class="main-ui">
        <form name="myFORM" method="post" action="">
            <table class="m_tab">
                <tr>
                    <td width="75" align="center"><input name=button type=button class="za_button" onClick="location.reload()" value="手动更新"></td>
                    <td width="115">
                        <select class="za_select za_select_auto" onChange="document.myFORM.submit();" id="seconds" name="seconds">
                            <option value="10">10秒</option>
                            <option value="30">30秒</option>
                            <option value="60">60秒</option>
                            <option value="90">90秒</option>
                            <option value="120">120秒</option>
                            <option value="180">180秒</option>
                        </select>&nbsp;&nbsp;&nbsp;
                        <span id=ShowTime></span>
                    </td>
                    <td width="200"></td>
                    <td width="60" >
                        <select id="music" name="music" onChange="self.myFORM.submit()" class="za_select za_select_auto">
                            <option value="0" selected >无声</option>
                            <option value="1" >鼓声</option>
                            <option value="2" >铃声</option>
                        </select>
                    </td>
                    <td width="60" align="center">共<?php echo $cou?>条</td>
                    <td width="40" align="center">
                        <select name='page'id="page" onChange="self.myFORM.submit()">
                            <?php
                            if ($page_count==0){
                                $page_count=1;
                            }
                            for($i=0;$i<$page_count;$i++){
                                if ($i==$page){
                                    echo "<option selected value='$i'>".($i+1)."</option>";
                                }else{
                                    echo "<option value='$i'>".($i+1)."</option>";
                                }
                            }
                            ?>
                        </select>			</td>
                    <td width="40" >共<?php echo $page_count?> 页</td>
                </tr>
                <tr>
                    <td colspan="8">
                    足球审核时间
                        <input class=za_text maxLength=3 size=3 value="<?php echo $trow['udp_ft_time']?>" name=FT>&nbsp;<input class=za_text  maxLength=3 size=3 value="<?php echo $trow['udp_time_ft']?>" name=FT1>
                    &nbsp;&nbsp;
                    电竞足球审核时间
                        <input class=za_text maxLength=3 size=3 value="<?php echo $trow['udp_djft_time']?>" name=DJFT>&nbsp;<input class=za_text  maxLength=3 size=3 value="<?php echo $trow['udp_time_djft']?>" name=DJFT1>
                    &nbsp;&nbsp;
                    棒球审核时间
                        <input class=za_text maxLength=3 size=3 value="<?php echo $trow['udp_bs_time']?>" name=BS>&nbsp;<input class=za_text  maxLength=3 size=3 value="<?php echo $trow['udp_time_bs']?>" name=BS1>
                    &nbsp;&nbsp;
                    其他审核时间
                        <input class=za_text maxLength=3 size=3 value="<?php echo $trow['udp_op_time']?>" name=OP>&nbsp;<input class=za_text  maxLength=3 size=3 value="<?php echo $trow['udp_time_op']?>" name=OP1>
                    &nbsp;&nbsp;
                        <br>
                        <input class=za_button type=submit value="确定" name=ch_ok>
                    </td>
                </tr>

            </table>


            <table class="m_tab" style="margin-top: 10px;">
                <tr class="m_title">
                    <td width="85" align="center">
                        <select name="sort" id="sort" onChange="document.myFORM.submit();" class="za_select">
                            <option value="BetTime">投注时间</option>
                            <option value="Gwin">投注金额</option>
                            <option value="Cancel">取消注单</option>
                            <option value="Danger">危险注单</option>
                        </select>	      </td>
                    <td width="80" align="center"></td>
                    <td width="110" align="center">
                        <select name="ball" id="ball" onChange="self.myFORM.submit()" class="za_select">
                            <option value="">全部</option>
                            <option value="1">足球</option>
                            <option value="2">篮球</option>
                            <option value="3">棒球</option>
                            <option value="4">网球</option>
                            <option value="5">排球</option>
                            <option value="6">其它</option>
                        </select>		  </td>
                    <td colspan="4" align="center">会员帐号:
                        <input name="memname" type="text" id="memname" value="<?php echo $memname?>" size="10">&nbsp;&nbsp;&nbsp;注单日期: <input name="date_start" type="text" id="date_start" value="<?php echo $date_start?>" size="10">&nbsp;&nbsp;&nbsp;<input class=za_button type="submit" name="Submit" value="提交">		  </td>
                    <td colspan="2" align="center"></td>
                </tr>
                <tr class="m_title">
                    <td width="85"align="center">投注时间</td>
                    <td width="80" align="center">用户名称</td>
                    <td width="110" align="center">球赛种类</td>
                    <td width="300" align="center">內容</td>
                    <td width="70" align="center">投注金额</td>
                    <td width="70" align="center">可赢金额</td>
                    <td width="80" align="center">结果</td>
                    <td width="50" align="center">操作</td>
                    <td width="120" align="center">功能</td>
                </tr>
                <?php
                while ($row = mysqli_fetch_assoc($result)){
                    switch($row['Active']){
                        case 1:
                            $betinfo=$Mnu_Soccer;
                            break;
                        case 2:
                            $betinfo=$Mnu_BasketBall;
                            break;
                        case 3:
                            $betinfo=$Mnu_Base;
                            break;
                        case 4:
                            $betinfo=$Mnu_Tennis;
                            break;
                        case 5:
                            $betinfo=$Mnu_Voll;
                            break;
                        case 6:
                            $betinfo=$Mnu_Other;
                            break;
                        case 7:
                            $Title=$Mnu_Stock;
                            break;
                    }
                    switch ($row['OddsType']){
                        case 'H':
                            $Odds='<BR><font color =green>'.$Rep_HK.'</font>';
                            break;
                        case 'M':
                            $Odds='<BR><font color =green>'.$Rep_Malay.'</font>';
                            break;
                        case 'I':
                            $Odds='<BR><font color =green>'.$Rep_Indo.'</font>';
                            break;
                        case 'E':
                            $Odds='<BR><font color =green>'.$Rep_Euro.'</font>';
                            break;
                        case '':
                            $Odds='';
                            break;
                    }
                    $time=strtotime($row['BetTime']);
                    $times=date("Y-m-d",$time).'<br>'.date("H:i:s",$time);

                    if($row['Danger']==1 or $row['Cancel']==1){
                        $bettimes='<font color="#FFFFFF"><span style="background-color: #FF0000">'.$times.'</span></font>';
                    }else{
                        $bettimes=$times;
                    }
                    if($row['Cancel']==1){
                        $betscore='<S><font color=#cc0000>'.number_format($row['BetScore']).'</font></S>';
                    }else{
                        $betscore=number_format($row['BetScore']);
                    }

                    $time1=strtotime($row['BetTime']);
                    $time2=time();
                    if($row['Confirmed']==-18 and $row['Cancel']==1 and ($time2-$time1<60)){
                        echo '<bgsound src="'.$mu.'" loop="10">';
                    }
                    if ($row['ShowType']=='H' or $row['LineType']=='10' or $row['LineType']=='20'){
                        $matchball=$row['MB_ball'].':'.$row['TG_ball'];
                    }else{
                        $matchball=$row['TG_ball'].':'.$row['MB_ball'];
                    }
                    ?>
                    <tr class="m_rig" onmouseover=sbar(this) onmouseout=cbar(this)>
                        <td align="center"><?php echo $bettimes?><br><font color="#0000cc"><?php echo $row['color']?></font></td>
                        <td align="center"><?php echo $row['M_Name']?><br><font color="#cc0000"><?php echo $row['OpenType']?></font></td>
                        <td align="center"><?php echo $betinfo?><?php echo $row['BetType']?><?php echo $Odds?><br><font color="#0000cc"><?php echo show_voucher($row['LineType'],$row['ID'])?></td>
                        <td align="right">
                            <?php
                            if($row['Cancel']==1){
                                echo "<span style=float:left;color=#0000FF>".$matchball."</span>";
                            }
                            ?>
                            <?php
                            if ($row['Active']='$active'){
                                if ($row['LineType']==8){
                                    $midd=explode('<br>',$row['Middle']);
                                    $mid=explode(',',$row['MID']);
                                    $show=explode(',',$row['ShowType']);

                                    for($t=0;$t<(sizeof($midd)-1)/3;$t++){
                                        echo $midd[3*$t].'<br>';
                                        $mysql="select MB_Inball,TG_Inball,MB_Inball_HR,TG_Inball_HR from `".DBPREFIX.SPORT_FLUSH_MATCH_TABLE."` where MID=".$mid[$t];
                                        $result1 = mysqli_query($dbLink,$mysql);
                                        $row1 = mysqli_fetch_assoc($result1);

                                        if ($row1["MB_Inball"]=='-1'){
                                            $font_a3='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-2'){
                                            $font_a3='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-3'){
                                            $font_a3='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-4'){
                                            $font_a3='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-5'){
                                            $font_a3='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-6'){
                                            $font_a3='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-7'){
                                            $font_a3='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-8'){
                                            $font_a3='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-9'){
                                            $font_a3='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-10'){
                                            $font_a3='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-11'){
                                            $font_a3='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-12'){
                                            $font_a3='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-13'){
                                            $font_a3='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-14'){
                                            $font_a3='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-15'){
                                            $font_a3='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-16'){
                                            $font_a3='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-17'){
                                            $font_a3='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-18'){
                                            $font_a3='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                        }else if ($row1["MB_Inball"]=='-19'){
                                            $font_a3='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                        }else{
                                            $font_a3='<font color="#009900"><b>'.$row1["TG_Inball"].'</b> : <b>'.$row1["MB_Inball"].'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$row1["MB_Inball"].'</b> : <b>'.$row1["TG_Inball"].'</b></font>&nbsp;';
                                        }
                                        echo $midd[3*$t+1].'<br>';
                                        if ($show[$t]=='C' and $row['LineType']==8){
                                            echo $font_a3;
                                        }else{
                                            echo $font_a4;
                                        }
                                        echo $midd[3*$t+2].'<br>';
                                    }
                                }else{
                                    $midd=explode('<br>',$row['Middle']);
                                    for($t=0;$t<sizeof($midd)-1;$t++){
                                        echo $midd[$t].'<br>';
                                    }
                                    $mysql="select MB_Inball,TG_Inball,MB_Inball_HR,TG_Inball_HR from `".DBPREFIX.SPORT_FLUSH_MATCH_TABLE."` where MID=".$row['MID'];
                                    $result1 = mysqli_query($dbLink,$mysql);
                                    $row1 = mysqli_fetch_assoc($result1);

                                    if ($row1["MB_Inball"]=='-1'){
                                        if($row1["MB_Inball_HR"]=='-1' and $row1["MB_Inball"]=='-1'){
                                            $font_a1='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score1.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-2'){
                                        if($row1["MB_Inball_HR"]=='-2' and $row1["MB_Inball"]=='-2'){
                                            $font_a1='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score2.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-3'){
                                        if($row1["MB_Inball_HR"]=='-3' and $row1["MB_Inball"]=='-3'){
                                            $font_a1='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score3.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-4'){
                                        if($row1["MB_Inball_HR"]=='-4' and $row1["MB_Inball"]=='-4'){
                                            $font_a1='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score4.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-5'){
                                        if($row1["MB_Inball_HR"]=='-5' and $row1["MB_Inball"]=='-5'){
                                            $font_a1='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score5.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-6'){
                                        if($row1["MB_Inball_HR"]=='-6' and $row1["MB_Inball"]=='-6')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score6.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-7'){
                                        if($row1["MB_Inball_HR"]=='-7' and $row1["MB_Inball"]=='-7'){
                                            $font_a1='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score7.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-8'){
                                        if($row1["MB_Inball_HR"]=='-8' and $row1["MB_Inball"]=='-8')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score8.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-9'){
                                        if($row1["MB_Inball_HR"]=='-9' and $row1["MB_Inball"]=='-9')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score9.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-10'){
                                        if($row1["MB_Inball_HR"]=='-10' and $row1["MB_Inball"]=='-10')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score10.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-11'){
                                        if($row1["MB_Inball_HR"]=='-11' and $row1["MB_Inball"]=='-11'){
                                            $font_a1='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score11.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-12'){
                                        if($row1["MB_Inball_HR"]=='-12' and $row1["MB_Inball"]=='-12'){
                                            $font_a1='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score12.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-13'){
                                        if($row1["MB_Inball_HR"]=='-13' and $row1["MB_Inball"]=='-13'){
                                            $font_a1='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score13.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-14'){
                                        if($row1["MB_Inball_HR"]=='-14' and $row1["MB_Inball"]=='-14'){
                                            $font_a1='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score14.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-15'){
                                        if($row1["MB_Inball_HR"]=='-15' and $row1["MB_Inball"]=='-15'){
                                            $font_a1='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score15.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-16'){
                                        if($row1["MB_Inball_HR"]=='-16' and $row1["MB_Inball"]=='-16')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score16.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-17'){
                                        if($row1["MB_Inball_HR"]=='-17' and $row1["MB_Inball"]=='-17'){
                                            $font_a1='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score17.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-18'){
                                        if($row1["MB_Inball_HR"]=='-18' and $row1["MB_Inball"]=='-18')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score18.'</b></font>&nbsp;';
                                        }
                                    }else if ($row1["MB_Inball"]=='-19'){
                                        if($row1["MB_Inball_HR"]=='-19' and $row1["MB_Inball"]=='-19')
                                        {
                                            $font_a1='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                            $font_a2='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                        }else{
                                            $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp;';
                                            $font_a3='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                            $font_a4='<font color="#009900"><b>'.$Score19.'</b></font>&nbsp;';
                                        }
                                    }else{
                                        $font_a3='<font color="#009900"><b>'.$row1["TG_Inball"].'</b> : <b>'.$row1["MB_Inball"].'</b></font> &nbsp;';
                                        $font_a4='<font color="#009900"><b>'.$row1["MB_Inball"].'</b> : <b>'.$row1["TG_Inball"].'</b></font>&nbsp; ';
                                        $font_a1='<font color="#009900"><b>'.$row1["TG_Inball_HR"].'</b> : <b>'.$row1["MB_Inball_HR"].'</b></font>&nbsp; ';
                                        $font_a2='<font color="#009900"><b>'.$row1["MB_Inball_HR"].'</b> : <b>'.$row1["TG_Inball_HR"].'</b></font>&nbsp; ';
                                    }

                                    if ($row['LineType']==11 or $row['LineType']==12 or $row['LineType']==13 or $row['LineType']==14 or $row['LineType']==19 or $row['LineType']==20){
                                        if ($row['ShowType']=='C' and ($row['LineType']==12 or $row['LineType']==19)){
                                            echo $font_a1;
                                        }else{
                                            echo $font_a2;
                                        }
                                    }else{
                                        if ($row['ShowType']=='C' and ($row['LineType']==2 or $row['LineType']==9)){
                                            echo $font_a3;
                                        }else{
                                            echo $font_a4;
                                        }
                                    }
                                    echo $midd[sizeof($midd)-1];
                                }

                            }else{
                                echo $row['Middle'];
                            }
                            ?>
                        </td>
                        <td align="right"><?php echo $betscore?></td>
                        <td align="right"><?php echo $row['Gwin']?></td>
                        <td align="right">
                            <?php
                            if($row['Cancel']==0){
                                ?>
                                <?php echo number_format($row['M_Result'],1)?>
                                <?php
                            }else{
                                ?>
                                <font color=red>
                                    <?php
                                    switch($row['Confirmed']){
                                        case 0:
                                            echo $zt=$Score20;
                                            break;
                                        case -1:
                                            echo $zt=$Score21;
                                            break;
                                        case -2:
                                            echo $zt=$Score22;
                                            break;
                                        case -3:
                                            echo $zt=$Score23;
                                            break;
                                        case -4:
                                            echo $zt=$Score24;
                                            break;
                                        case -5:
                                            echo $zt=$Score25;
                                            break;
                                        case -6:
                                            echo $zt=$Score26;
                                            break;
                                        case -7:
                                            echo $zt=$Score27;
                                            break;
                                        case -8:
                                            echo $zt=$Score28;
                                            break;
                                        case -9:
                                            echo $zt=$Score29;
                                            break;
                                        case -10:
                                            echo $zt=$Score30;
                                            break;
                                        case -11:
                                            echo $zt=$Score31;
                                            break;
                                        case -12:
                                            echo $zt=$Score32;
                                            break;
                                        case -13:
                                            echo $zt=$Score33;
                                            break;
                                        case -14:
                                            echo $zt=$Score34;
                                            break;
                                        case -15:
                                            echo $zt=$Score35;
                                            break;
                                        case -16:
                                            echo $zt=$Score36;
                                            break;
                                        case -17:
                                            echo $zt=$Score37;
                                            break;
                                        case -18:
                                            echo $zt=$Score38;
                                            break;
                                        case -19:
                                            echo $zt=$Score39;
                                            break;
                                        case -20:
                                            echo $zt=$Score40;
                                            break;
                                        case -21:
                                            echo $zt=$Score41;
                                            break;
                                    }
                                    ?>
                                </font>
                                <?php
                            }
                            ?>
                        </td>
                        <td align="center">
                            <?php
                            if ($row['Cancel']==1){
                                echo '<a href="re_list.php?uid='.$uid.'&langx='.$langx.'&key=resume&id='.$row['ID'].'&gid='.$row['MID'].'&seconds='.$seconds.'&music='.$music.'&page='.$page.'&memname='.$memname.'&date_start='.$date_start.'&sort='.$sort.'&ball='.$ball.'&type='.$type.'"><font color=red><b>恢复</b></font></a>';
                            }else{
                                echo '<font color=blue><b>正常</b></font>';
                            }
                            ?>
                        </td>
                        <td width="121" align="left">
                            <SELECT onchange=javascript:CheckSTOP(this.options[this.selectedIndex].value) size=1 name=select1>
                                <option>注单处理</option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=resume&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score20?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-1&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score21?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-2&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score22?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-3&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score23?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-4&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score24?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-5&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score25?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-6&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score26?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-7&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score27?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-8&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score28?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-9&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score29?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-10&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score30?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-11&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score31?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-12&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score32?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-13&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score33?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-14&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score34?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-15&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score35?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-16&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score36?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-17&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score37?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-18&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score38?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-19&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score39?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-20&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score40?></option>
                                <option value="re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&key=cancel&id=<?php echo $row['ID']?>&gid=<?php echo $row['MID']?>&confirmed=-21&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&type=<?php echo $type?>"><?php echo $Score41?></option>
                            </SELECT></td>
                    </tr>
                    <?php
                }
                ?>
            </table>
        </form>
    </div>

    <script type="text/javascript" src="../../../js/agents/common.js?v=<?php echo AUTOVER; ?>"></script>
    <script language="JavaScript" type="text/JavaScript">
        function CheckSTOP(str){
            if(confirm("确实取消本注单吗?")){
                document.location=str;
            }
        }

        function onLoad(){
            var obj_seconds = document.getElementById('seconds');
            obj_seconds.value = '<?php echo $seconds?>';
            var obj_music = document.getElementById('music');
            obj_music.value = '<?php echo $music?>';
            var obj_page = document.getElementById('page');
            obj_page.value = '<?php echo $page?>';
            var obj_sort=document.getElementById('sort');
            obj_sort.value='<?php echo $sort?>';
            var obj_ball=document.getElementById('ball');
            obj_ball.value='<?php echo $ball?>';
            var obj_date_start = document.getElementById('date_start');
            obj_date_start.value = '<?php echo $date_start?>';
        }
        var second="<?php echo $seconds?>"
        function auto_refresh(){
            if (second==1){
                window.location.href='re_list.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&lv=<?php echo $lv?>&seconds=<?php echo $seconds?>&music=<?php echo $music?>&page=<?php echo $page?>&page=<?php echo $page?>&sort=<?php echo $sort?>&ball=<?php echo $ball?>&memname=<?php echo $memname?>&date_start=<?php echo $date_start?>'; //刷新页面
            }else{
                second-=1
                curmin=Math.floor(second)
                curtime=curmin+"秒"
                ShowTime.innerText=curtime
                setTimeout("auto_refresh()",1000)
            }
        }
    </SCRIPT>

    </body>
    </html>
<?php
$ip_addr = get_ip();
$loginfo='滚球注单投注明细';
$mysql="insert into ".DBPREFIX."web_mem_log_data(UserName,Logintime,ConText,Loginip,Url) values('$loginname',now(),'$loginfo','$ip_addr','".BROWSER_IP."')";
mysqli_query($dbMasterLink,$mysql);
?>
