<?php
session_start();
header("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-cache, must-revalidate");      
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");
include "include/address.mem.php";
require ("include/config.inc.php");
echo "<script>if(self == top) parent.location='".BROWSER_IP."'\n;</script>";
$uid=$_REQUEST['uid'];
$langx=$_SESSION['langx'];
$mtype=$_REQUEST['mtype'];
$showtype=$_REQUEST['showtype'];
require ("./include/traditional.$langx.inc.php");

if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" ) {
	echo "<script>window.open('".BROWSER_IP."/tpl/logout_warn.html','_top')</script>";
	exit;
}
if ($showtype=="future"){
	$ShowType="VB_future";
	$header="future";
}else{
	$ShowType="VB_browse";
	$header="";
}
?>
<html>
<head>
<title>歡迎光臨投注</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<script> 
	
try{	
	VB_lid_ary=top.VB_lid['VB_lid_ary'];
	VB_lid_type=top.VB_lid['VB_lid_type'];
	VB_lname_ary=top.VB_lid['VB_lname_ary'];
	VB_lid_ary_RE=top.VB_lid['VB_lid_ary_RE'];
	VB_lname_ary_RE=top.VB_lid['VB_lname_ary_RE'];
	VU_lid_ary=top.VU_lid['VU_lid_ary'];
	VU_lid_type=top.VU_lid['VU_lid_type'];
	VU_lname_ary=top.VU_lid['VU_lname_ary'];
 
}catch(E){
	initlid();
}
 
 
function initlid(){
	top.VB_lid = new Array();
	top.VU_lid = new Array();
	top.VB_lid['VB_lid_ary']= VB_lid_ary='ALL';
	top.VB_lid['VB_lid_type']= VB_lid_type='';
	top.VB_lid['VB_lname_ary']= VB_lname_ary='ALL';
	top.VB_lid['VB_lid_ary_RE']= VB_lid_ary_RE='ALL';
	top.VB_lid['VB_lname_ary_RE']= VB_lname_ary_RE='ALL';
	top.VU_lid['VU_lid_ary']= VU_lid_ary='ALL';
	top.VU_lid['VU_lid_type']= VU_lid_type='';
	top.VU_lid['VU_lname_ary']= VU_lname_ary='ALL';
}

</script>
<frameset rows="101,*" cols="*" frameborder="NO" border="0" framespacing="0"> 
  <frame name="header" scrolling="NO" noresize src="<?php echo BROWSER_IP?>/app/member/VB_header.php?uid=<?php echo $uid?>&showtype=<?php echo $header?>&langx=<?php echo $langx?>&mtype=<?php echo $mtype?>" >
  <frameset cols="240,1*" frameborder="NO" border="0" framespacing="0"> 
    <frame name="mem_order" noresize scrolling="AUTO" src="<?php echo BROWSER_IP?>/app/member/select.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&mtype=<?php echo $mtype?>">
    <frame name="body" src="<?php echo BROWSER_IP?>/app/member/<?php echo $ShowType?>/index.php?uid=<?php echo $uid?>&langx=<?php echo $langx?>&mtype=<?php echo $mtype?>">
  </frameset>
</frameset>
<noframes><body bgcolor="#FFFFFF">

</body></noframes>
</html>
