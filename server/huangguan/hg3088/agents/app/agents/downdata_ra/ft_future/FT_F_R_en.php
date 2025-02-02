<?php

if(php_sapi_name() == "cli") {
    define("CONFIG_DIR", dirname(dirname(dirname(__FILE__))));
    define("COMMON_DIR", dirname(dirname(dirname(dirname(dirname(dirname(__FILE__)))))));
    require(CONFIG_DIR."/include/config.inc.php");
    require_once(COMMON_DIR."/common/sportCenterData.php");
    require(CONFIG_DIR."/include/curl_http.php");
    require_once(CONFIG_DIR."/include/address.mem.php");
}else {
    require("../../include/config.inc.php");
    require_once("../../../../../common/sportCenterData.php");
    require("../../include/curl_http.php");
    require_once("../../include/address.mem.php");

    /*判断IP是否在白名单*/
    if(CHECK_IP_SWITCH) {
        if(!checkip()) {
            exit('登录失败!!\\n未被授权访问的IP!!');
        }
    }
}

$redisObj = new Ciredis();

$refurbishTimeData = refurbishTime();
$settime=$refurbishTimeData[0]['udp_ft_r'];

$allcount=0;
$langx="en-us";
$accoutArr=getFlushWaterAccount();

$curl = new Curl_HTTP_Client();
$curl->store_cookies("cookies.txt"); 
$curl->set_user_agent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
$dataArray= array() ; // 把需要的数据重新放在数组里面
foreach($accoutArr as $key=>$value){
//	for($page_no=0;$page_no<15;$page_no++){
//		$curl->set_referrer("".$value['Datasite']."/app/member/FT_future/index.php?rtype=r&uid=".$value['Uid']."&langx=".$langx."&mtype=3");
//		$html_data=$curl->fetch_url("".$value['Datasite']."/app/member/FT_future/body_var.php?rtype=r&uid=".$value['Uid']."&langx=".$langx."&g_date=ALL&mtype=3&page_no=".$page_no);
//		$matches = get_content_deal($html_data);
//		$cou=sizeof($matches);
    // 获取足球早盘的联赛ID
    $postdata = array(
        'p' => 'get_league_list_All',
        'ver' => date('Y-m-d-H').$value['Ver'],
        'langx' => $langx,
        'uid' => $value['Uid'],
        'gtype' => 'FT',
        'showtype' => 'fu',
        'FS' => 'N',
        'date' => 'all',
        'nocp' => 'N',
    );
    $xml_data=$curl->send_post_data($value['Datasite']."/transform.php?ver=".date('Y-m-d-H').$value['Ver'], $postdata);
    $aData = xmlToArray($xml_data);
    if ($aData['status']=='success'){
        if (count($aData['classifier']['region'])>0){
            $lid = getLids($aData)['lid'];
        }
        else{
            exit('success 没有足球早盘数据'.$langx);
        }
    }
    else{
        exit('error 没有足球早盘数据'.$langx);
    }

    // 获取早盘的赛事
    unset($postdata);
    $postdata = array(
        'p' => 'get_game_list',
        'ver' => date('Y-m-d-H').$value['Ver'],
        'langx' => $langx,
        'uid' => $value['Uid'],
        'gtype' => 'ft',
        'showtype' => 'early',
        'rtype' => 'r',
        'ltype' => '4',
        'lid' => $lid,
        'action' => 'clickCoupon',
        'sorttype' => 'T',
    );
    $xml_data=$curl->send_post_data($value['Datasite']."/transform.php?ver=".date('Y-m-d-H').$value['Ver'], $postdata);
    $aData = xmlToArray($xml_data);
    if(isset($aData['totalDataCount'])){
        $cou= $aData['totalDataCount'];
    }else{
        $cou=0;
    }

		if($cou>0){//可以抓到数据
            foreach ($aData['ec'] as $k => $v){
                $datainfo=$v['game'];

				if (!empty($datainfo)){

                    $LEAGUE=$datainfo['LEAGUE'];
                    $TEAM_H=$datainfo['TEAM_H'];
                    $TEAM_C=$datainfo['TEAM_C'];
                    $GID=$datainfo['GID'];

                    // 将从正网拉取的测试数据过滤掉
                    // stripos 查找字符串首次出现的位置（不区分大小写）
                    $pos_m = stripos($LEAGUE, 'test'); // 查找联赛名称是否含有 test
                    $pos_mb = stripos($TEAM_H, 'test'); // 检查主队名称是否含有 test
                    $pos_tg = stripos($TEAM_C, 'test'); // 检查客队名称是否含有 test
                    if ($pos_m !== false || $pos_mb !== false || $pos_tg !== false){
                        continue;
                    }

                    $dataArray[$GID]=(array($LEAGUE,$TEAM_H,$TEAM_C)); // 把数据放在二维数组里面
				    $allcount++;
				}else{
					continue;
				}
			}
		}else{
			break;
		} 
//	}
	if($allcount>0)	break;
}
$redisObj->setOne("FT_Future_Num",(int)$allcount);
if($allcount>0) { //可以抓到数据
    $ids = implode(',', array_keys($dataArray));
    //echo $ids;
    $e_sql .= "END,";
    $sql = "update `".DATAHGPREFIX.SPORT_FLUSH_MATCH_TABLE."` set ";
    $m_sql .="M_League_en = CASE MID " ; // 主队
    $t_sql .="MB_Team_en = CASE MID "; // 客队
    $l_sql .="TG_Team_en = CASE MID "; // 联赛
    foreach ($dataArray as $id => $ordinal) { // 主队名称
        $m_sql .= "WHEN $id THEN '$ordinal[0]' " ; // 拼接SQL语句
        $t_sql .= "WHEN $id THEN '$ordinal[1]' " ; // 拼接SQL语句
        $l_sql .= "WHEN $id THEN '$ordinal[2]' " ; // 拼接SQL语句
    }
    $sql .= $m_sql.$e_sql.$t_sql.$e_sql.$l_sql ;
    $sql .="END WHERE MID IN ($ids)"; // 实现一次性更新数据库操作
    mysqli_query($dbCenterMasterDbLink,$sql) or die ("操作失败!!");

}

function get_content_deal($html_data){
	$a = array(
		"if(self == top)",
		"<script>",
		"</script>",
		"]=new Array()",
		"parent.GameFU=new Array();",
		"\n\n",
		"_.",
		"g([",
		"])"
	);
	$b = array(
		"",
		"",
		"",
		"",
		"",
		"",
		"parent.",
		"Array(",
		")"
	);
	$msg = str_replace($a,$b,$html_data);
	preg_match_all("/Array\((.+?)\);/is",$msg,$matches);
	return $matches[0];
}
?>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="/style/agents/control_down.css?v=<?php echo AUTOVER; ?>" rel="stylesheet" type="text/css">
</head>
<body>
<script> 

var limit="<?php echo $settime?>" 
if (document.images){ 
	var parselimit=limit
} 
function beginrefresh(){ 
if (!document.images) 
	return 
if (parselimit==1) 
	window.location.reload() 
else{ 
	parselimit-=1 
	curmin=Math.floor(parselimit) 
	if (curmin!=0) 
		curtime=curmin+"秒后自动获取!" 
	else 
		curtime=cursec+"秒后自动获取!" 
		timeinfo.innerText=curtime 
		setTimeout("beginrefresh()",1000) 
	} 
} 

window.onload=beginrefresh 

</script>
<table width="100" height="70" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="100" height="70" align="center">
    单式数据接收<br>
      <span id="timeinfo"></span><br>
      <input type=button name=button value="英文 <?php echo $allcount?>" onClick="window.location.reload()"></td>
  </tr>
</table>
</body>
</html>
