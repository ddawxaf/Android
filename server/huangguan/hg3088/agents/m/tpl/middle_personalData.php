<?php
session_start();
header("Expires: Mon, 26 Jul 1970 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Content-type: text/html; charset=utf-8");

require ("../../app/agents/include/config.inc.php");

checkAdminLogin(); // 同一账号不能同时登陆
if( !isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" ) {
    echo "<script>alert('您的登录信息已过期,请重新登录!');top.location.href='/';</script>";
    exit;
}
$uid = $_SESSION['Oid'];
$Alias = $_SESSION['Alias'];
$navtype = isset($_REQUEST['type'])?$_REQUEST['type']:'';
$navtitle = isset($_REQUEST['navtitle'])?$_REQUEST['navtitle']:'';



?>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <META name="keywords" content="<?php echo COMPANY_NAME;?>,<?php echo COMPANY_NAME;?>登入,<?php echo COMPANY_NAME;?>平台">
    <meta name="HandheldFriendly" content="true"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="shortcut icon" href="/images/favicon_<?php echo TPL_FILE_NAME;?>.ico" type="image/x-icon"/>
    <link href="../css/main.css?v=<?php echo AUTOVER; ?>" rel="stylesheet" type="text/css" media="screen"/>

    <title> <?php echo COMPANY_NAME;?> </title>
    <style type="text/css">
        .setting_center{padding:0 3%}
        .setting_center li{justify-content: space-between;height:3rem;line-height:3rem;border-bottom:1px solid #f1f1f1;color: #000;font-size: 1.2rem;}
        .setting_center li span:last-child{color: #acacac;font-size: 1rem;}

    </style>
</head>
<body>
<div class="contentAll flex">
    <?php include 'middle_header.php'; ?>

    <div class="setting_center">
        <ul>
            <li class="flex"> <span > 用户账号 </span> <span > <?php echo $_SESSION['UserName'];?> </span> </li>
            <li class="flex"> <span > 真实姓名 </span> <span > <?php echo ($Alias?returnRealName($Alias):'***');?> </span> </li>
        </ul>

    </div>

    <?php include 'middle_footer.php'; ?>

</div>

<script type="text/javascript" src="/js/agents/jquery.js"></script>
<script type="text/javascript" src="/js/agents/layer/layer.js"></script>
<script type="text/javascript" src="/js/agents/common.js?v=<?php echo AUTOVER; ?>"></script>
<script type="text/javascript">
    settingHeight('.contentAll');

</script>
</body>
</html>
