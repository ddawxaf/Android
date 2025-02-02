<?php
/**
 * 信付通上分（银行、支付宝、微信、QQ）
 */
header ( 'Content-Type: text/html; charset=utf-8' );
foreach ( $_REQUEST as $rkey => $rval ) {
    $_REQUEST [$rkey] = trim ( addslashes( stripslashes ( strip_tags ( $rval ) ) ) );
}
@error_log(serialize($_REQUEST).PHP_EOL, 3, '/tmp/xftback_return_url.log');

include_once ("../class/config.inc.php");
include_once ("../model/Userlock.php");
include_once ("../model/Pay.php");
include_once ("./xft/utils.php");

$params['gmt_create']=$_REQUEST['gmt_create'];        // 交易创建时间
$params['order_no']= $ordernumber = $_REQUEST['order_no'];             //外部交易号  商户订单号
$params['gmt_payment']=$_REQUEST['gmt_payment'];       //交易付款时间
$params['seller_email']=$_REQUEST['seller_email'];     // 买家Email
$params['notify_time']=$_REQUEST['notify_time'];      // 通知时间
$params['quantity']=$_REQUEST['quantity'];            // 数量
$params['discount']=$_REQUEST['discount'];            // 预留 折扣
$params['body'] = $body = $_REQUEST['body'];              // 商品描述
$params['is_success']=$is_success=$_REQUEST['is_success'];         //通讯状态 T，表示成功；F，表示失败
$params['title'] = $_REQUEST['title'];                   // 商品名称
$params['gmt_logistics_modify'] =$_REQUEST['gmt_logistics_modify']; // 订单更改时间
$params['notify_id'] =$_REQUEST['notify_id'];           // 流水号
$params['notify_type']=$_REQUEST['notify_type'];      // 通知类型  WAIT_TRIGGER
$params['payment_type']=$_REQUEST['payment_type'];    // 支付类型

$params['ext_param2']=$_REQUEST['ext_param2'];        // 预留 扩展字段2
$params['price']=$_REQUEST['price'];                   // 单价 50.00
$params['total_fee'] = $orderAmount = $_REQUEST['total_fee'];          // 交易金额  50.00

$params['trade_status'] = $trade_status =  $_REQUEST['trade_status'];   // 交易状态
$params['trade_no'] = $_REQUEST['trade_no'];            // 交易流水号
$params['seller_actions']=$_REQUEST['seller_actions']; // 卖家  SEND_GOODS
$params['seller_id'] = $MemberID = $_REQUEST['seller_id'];           // 卖家id (商户号)  100000000004956
$params['is_total_fee_adjust']=$_REQUEST['is_total_fee_adjust'];   //总费用

//$params['buyer_email']=$_REQUEST['buyer_email'];     // 买家Email
//$params['buyer_id']=$_REQUEST['buyer_id'];            // 买家ID
//$params['ext_param1']=$_REQUEST['ext_param1'];        // 预留 扩展字段1  未接收到

$sign=$_REQUEST['sign'];         // 接收sign
$signType = $_REQUEST['signType'];      //signType不参与加密，所以要放在最后

$aData = explode('|',$body);        //qaz123|68|61045|ALIPAY  用户名|渠道id|userid|银行代码

$sql = "select ID from ".DBPREFIX.MEMBERTABLE." where ID='$aData[2]' and Status<2";
$result = mysqli_query($dbLink,$sql);
$row=mysqli_fetch_assoc($result);
$cou=mysqli_num_rows($result);
if($cou==0){
    echo "<script type='text/javascript'>alert('通知入款会员查找失败！');window.opener=null;window.open('', '_self');window.close();</script>";
    exit;
}

$sSql = 'SELECT * FROM `'.DBPREFIX.'gxfcy_pay` WHERE `id` ='.$aData[1].' AND `status` = 1 limit 1';
$oRes = mysqli_query($dbLink,$sSql);
$aRow = mysqli_fetch_assoc($oRes);
$iCou = mysqli_num_rows($oRes);
if($iCou==0){
    echo "<script type='text/javascript'>alert('渠道信息错误！');window.opener=null;window.open('', '_self');window.close();</script>";
    exit;
}

$apiKey = strval($aRow['business_pwd']);    //商户密匙
$str_to_sign = utils::Sign($params,$apiKey);

//@error_log('接收sign:'.$sign.'--生成str_to_sign:'.$str_to_sign.PHP_EOL, 3, '/tmp/xftback_return_url.log');

if ($sign == $str_to_sign) {
    if($is_success == 'T' && $trade_status =='TRADE_FINISHED') {
        $oUserLock = new Userlock_model($dbMasterLink);
        $userInfo=$oUserLock->lock($row['ID']); // 用户id
        $userInfoArr = json_decode($userInfo,true);
        if( is_array($userInfoArr) && count($userInfoArr)>0){
            $oPayin = new Pay_model($dbMasterLink);
            // 用户个人信息，回传参数，商户号，订单号，金额，第三方简称（xft 信付通）
            $oPayin->userpayin($userInfoArr, $aData, $MemberID, $ordernumber, $orderAmount, $aRow);
            $oUserLock->commit_lock();
            echo "SUCCESS";
            exit;
        }else{
            echo("上分失败,数据操作错误");
        }
    } else {
        echo("交易失败,数据操作错误");
        exit;
    }
} else {
   echo("Md5CheckFail'");//MD5校验失败
}
