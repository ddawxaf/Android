<?php
session_start();
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");          
header("Cache-Control: no-cache, must-revalidate");      
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");
include "../include/address.mem.php";

require ("../include/config.inc.php");
require_once("../../../../common/sportCenterData.php");
require ("../include/define_function_list.inc.php");

$uid=(isset($_REQUEST['uid']) && $_REQUEST['uid'])? $_REQUEST['uid'] :$_SESSION['Oid'];
$langx=$_SESSION['langx'];
$gid=$_REQUEST['gid'];
$rtype=$_REQUEST['rtype']; 
$wtype=$_REQUEST['wtype'];
$gametype=$_REQUEST['gametype']; 
$change=$_REQUEST['change'];
require ("../include/traditional.$langx.inc.php");

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" ) {
	echo "<script>top.location.href='/'</script>";
	exit;
}
$gameswitch = judgeBetSwitch('BK') ; // 篮球投注开关
if($gametype=='BK'){ // 限制篮球投注
    if($gameswitch){ // 停用 篮球
        echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
    }

}
$sql = "select Money from ".DBPREFIX.MEMBERTABLE." where Oid='$uid' and Status=0";
$result = mysqli_query($dbLink,$sql);
$row = mysqli_fetch_assoc($result);
$credit=$row['Money'];
$pay_type=$_SESSION['Pay_Type'];
$memname=$_SESSION['UserName'];
$open=$_SESSION['OpenType'];

$btset=singleset('FS');
$GMIN_SINGLE=$btset[0];
$GMAX_SINGLE=FS_FS_Scene;
$GSINGLE_CREDIT=FS_FS_Bet;

if ($change==1){
	$bet_title=$nobettitle;
}
$mysql = "select M_Start,$mb_team as MB_Team,$m_league as M_League,$m_item as M_Item, mcount as Num,mshow as Ftype,M_Rate from ".DBPREFIX.SPORT_FLUSH_FS_MATCH_TABLE." where `M_Start`>now() and `MID`='$gid' and Gid='$rtype' and $mb_team!=''";
$result = mysqli_query($dbLink,$mysql);
$row=mysqli_fetch_assoc($result);
$cou=mysqli_num_rows($result);
if($cou==0){
	echo attention("$Order_This_match_is_closed_Please_try_again",$uid,$langx);
	exit;
}else{

	$M_League=$row['M_League'];
	$MB_Team=$row['MB_Team'];
	$num=$row['Num'];
	$ftype=$row['Ftype'];
	$M_Item=$row['M_Item'];
	$M_Rate=change_rate($open,$row['M_Rate']);
	
	$havesql="select sum(BetScore) as BetScore from ".DBPREFIX."web_report_data where m_name='$memname' and MID='$gid' and linetype=1 and Mtype='$mtype' and Active=7";
	$result = mysqli_query($dbLink,$havesql);
	$haverow = mysqli_fetch_assoc($result);
	$have_bet=$haverow['BetScore']+0;
	
    $sql = "select CS,OU from ".DBPREFIX."match_league where  $m_league='$M_League'";
    $result = mysqli_query($dbLink,$sql);

    $league = mysqli_fetch_assoc($result);
    $bettop=$league['CS'];
	
	if ($M_Rate==0){
        echo attention("$Order_Odd_changed_please_bet_again",$uid,$langx);

	    exit;
	}
	if ($bettop<$GSINGLE_CREDIT){
		$bettop_money=$GSINGLE_CREDIT;
	}else{
		$bettop_money=$GSINGLE_CREDIT;
	}
?>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link href="/style/member/mem_order_ft.css?v=<?php echo AUTOVER; ?>" rel="stylesheet" type="text/css">
</head>
<script language="JavaScript" src="../../../js/jquery.js"></script>
  <script language="JavaScript" src="../../../js/ft_fs_order.js?v=<?php echo AUTOVER; ?>"></script>
<script type="text/javascript" class="language_choose" src="../../../js/zh-cn.js?v=<?php echo AUTOVER; ?>"></script><script type="text/javascript" src="../../../js/betcommon.js?v=<?php echo AUTOVER; ?>"></script>
<body id="OFS" class="bodyset bodyset_<?php echo TPL_FILE_NAME;?>" onSelectStart="self.event.returnValue=false" oncontextmenu="self.event.returnValue=false;window.event.returnValue=false;">
<form name="LAYOUTFORM" action="/app/member/FT_order/FT_order_nfs_finish.php" method="post" onSubmit="return false">
  <div class="ord">
   	<div class="title"><h1><span class="info">i</span><?php echo $MB_Team ?></h1><div class="tiTimer" onClick="orderReload();"><span id="ODtimer">10</span><input type="hidden" id="checkOrder" onClick="onclickReloadTime()" checked value="10"></div></div>
    <div class="main">
      <div class="leag"><?php echo $M_League?> - <?php echo $MB_Team?></div>
      <p class="team"><em><?php echo $M_Item?></em> @ <strong class="light" id="ioradio_id"><?php echo $M_Rate?></strong></p>

      <p class="error" style="display: none;"></p>
      <div class="betdata">
          <p class="amount"><input name="gold" type="text" class="txt" id="gold" onKeyPress="return CheckKey(event)" onKeyUp="return chaCountWinGold()" size="8" maxlength="10" placeholder="投注额"><span class="clean_bet_money" id="betClear"></span></p>
          <p class="mayWin"><span class="bet_txt">可赢金额：</span><font id="pc">0</font></p>

           <div class="betAmount"> </div> <p class="minBet">限额：<span class="bet_txt">最低<?php echo $GMIN_SINGLE?> / 最高<?php echo $GSINGLE_CREDIT?></span></p>
    </div>
    </div>
      <div id="gWager" style="display: none;position: absolute;"></div>
      <div id="gbutton" style="display: block;position: absolute;"></div>
      <div class="betBox">

          <input type="button" name="Submit" value="确定交易" onClick="chaCountWinGold();return SubChk();" class="yes">
          <input type="button" name="btnCancel" value="取消" onClick="parent.close_bet();" class="no">
          <p class="auto"><input type="checkbox" id="autoOdd" name="autoOdd" onClick="onclickReloadAutoOdd()" checked value="Y"><span class="auto_info" title="在方格里打勾表示，如果投注单里的任何选项在确认投注时赔率变佳，系统会无提示的继续进行下注。">自动接受较佳赔率</span></p>
      </div>
    </div>
      <div id="gfoot" style="display: block;position: absolute;"></div>



<input type="hidden" name="uid" value="<?php echo $uid?>">
<input type="hidden" name="active" value="7">
<input type="hidden" name="line_type" value="16">
<input type="hidden" name="gid" value="<?php echo $gid?>">
<input type="hidden" name="tid" value="122566">
<input type="hidden" id="ioradio_fs" name="ioradio_fs" value="<?php echo $M_Rate?>">
<input type="hidden" name="gmax_single" value="<?php echo $bettop_money?>">
<input type="hidden" name="gmin_single" value="<?php echo $GMIN_SINGLE?>">
<input type="hidden" name="singlecredit" value="<?php echo $GMAX_SINGLE?>">
<input type="hidden" name="singleorder" value="<?php echo $GSINGLE_CREDIT?>">
<input type="hidden" name="restsinglecredit" value="<?php echo $have_bet?>">
<input type="hidden" name="wagerstotal" value="<?php echo $GMAX_SINGLE?>">
    <input type="hidden" name="restcredit" value="<?php echo  $credit?>"> <input type="hidden" name="token" value="<?php echo getRandomString(32)?>">
<input type="hidden" name="pay_type" value="<?php echo $pay_type?>">
<input type="hidden" name="gametype" value="<?php echo $gametype?>">
<input type="hidden" name="rtype" value="<?php echo $rtype?>">
<input type="hidden" name="wtype" value="<?php echo $wtype?>">
<input type="hidden" id="maxgold" name="maxgold" value="<?php echo $GSINGLE_CREDIT?>">
</form>
</body>
<SCRIPT LANGUAGE="JavaScript">document.all.gold.focus();</script>
</html>
<?php
}
?>