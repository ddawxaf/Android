<?php
include 'merchantProperties.php';
/*
 * @Description API支付产品通用接口范例
 */

#	产品通用接口正式请求地址
#	产品通用接口测试请求地址
// $reqURL_onLine = "http://localhost/GateWay/ReceiveBank.aspx";
$reqURL_onLine = "http://zhifu.zrb111.com/GateWay/ReceiveBank.aspx";

# 业务类型
# 支付请求，固定值"Buy" .
$p0_Cmd = "Buy";

#	送货地址
# 为"1": 需要用户将送货地址留在API支付系统;为"0": 不需要，默认为 "0".
$p9_SAF = "0";

#签名函数生成签名串
function getReqHmacString($p2_Order,$p3_Amt,$p4_Cur,$p5_Pid,$p6_Pcat,$p7_Pdesc,$p8_Url,$pa_MP,$pd_FrpId,$pr_NeedResponse,$p1_MerId,$merchantKey)
{
    global $p0_Cmd;
    global $p9_SAF;
    //include 'merchantProperties.php';

    #进行签名处理，一定按照文档中标明的签名顺序进行
    $sbOld = "";
    #加入业务类型
    $sbOld = $sbOld.$p0_Cmd;
    #加入商户编号
    $sbOld = $sbOld.$p1_MerId;
    #加入商户订单号
    $sbOld = $sbOld.$p2_Order;
    #加入支付金额
    $sbOld = $sbOld.$p3_Amt;
    #加入交易币种
    $sbOld = $sbOld.$p4_Cur;
    #加入商品名称
    $sbOld = $sbOld.$p5_Pid;
    #加入商品分类
    $sbOld = $sbOld.$p6_Pcat;
    #加入商品描述
    $sbOld = $sbOld.$p7_Pdesc;
    #加入商户接收支付成功数据的地址
    $sbOld = $sbOld.$p8_Url;
    #加入送货地址标识
    $sbOld = $sbOld.$p9_SAF;
    #加入商户扩展信息
    $sbOld = $sbOld.$pa_MP;
    #加入支付通道编码
    $sbOld = $sbOld.$pd_FrpId;
    #加入是否需要应答机制
    $sbOld = $sbOld.$pr_NeedResponse;
    //logstr($p2_Order,$sbOld,HmacMd5($sbOld,$merchantKey));
    return HmacMd5($sbOld,$merchantKey);

}

function getCallbackHmacString($r0_Cmd,$r1_Code,$r2_TrxId,$r3_Amt,$r4_Cur,$r5_Pid,$r6_Order,$r7_Uid,$r8_MP,$r9_BType,$p1_MerId,$merchantKey)
{

    //include 'merchantProperties.php';

    #取得加密前的字符串
    $sbOld = "";
    #加入商家ID
    $sbOld = $sbOld.$p1_MerId;
    #加入消息类型
    $sbOld = $sbOld.$r0_Cmd;
    #加入业务返回码
    $sbOld = $sbOld.$r1_Code;
    #加入交易ID
    $sbOld = $sbOld.$r2_TrxId;
    #加入交易金额
    $sbOld = $sbOld.$r3_Amt;
    #加入货币单位
    $sbOld = $sbOld.$r4_Cur;
    #加入产品Id
    $sbOld = $sbOld.$r5_Pid;
    #加入订单ID
    $sbOld = $sbOld.$r6_Order;
    #加入用户ID
    $sbOld = $sbOld.$r7_Uid;
    #加入商家扩展信息
    $sbOld = $sbOld.$r8_MP;
    #加入交易结果返回类型
    $sbOld = $sbOld.$r9_BType;

    //logstr($r6_Order,$sbOld,HmacMd5($sbOld,$merchantKey));
    return HmacMd5($sbOld,$merchantKey);

}


#	取得返回串中的所有参数
function getCallBackValue(&$r0_Cmd,&$r1_Code,&$r2_TrxId,&$r3_Amt,&$r4_Cur,&$r5_Pid,&$r6_Order,&$r7_Uid,&$r8_MP,&$r9_BType,&$hmac)
{
    $r0_Cmd		= $_REQUEST['r0_Cmd'];
    $r1_Code	= $_REQUEST['r1_Code'];
    $r2_TrxId	= $_REQUEST['r2_TrxId'];
    $r3_Amt		= $_REQUEST['r3_Amt'];
    $r4_Cur		= $_REQUEST['r4_Cur'];
    $r5_Pid		= $_REQUEST['r5_Pid'];
    $r6_Order	= $_REQUEST['r6_Order'];
    $r7_Uid		= $_REQUEST['r7_Uid'];
    $r8_MP		= $_REQUEST['r8_MP'];
    $r9_BType	= $_REQUEST['r9_BType'];
    $hmac			= $_REQUEST['hmac'];

    return null;
}

function CheckHmac($r0_Cmd,$r1_Code,$r2_TrxId,$r3_Amt,$r4_Cur,$r5_Pid,$r6_Order,$r7_Uid,$r8_MP,$r9_BType,$hmac,$p1_MerId,$merchantKey)
{
    if($hmac==getCallbackHmacString($r0_Cmd,$r1_Code,$r2_TrxId,$r3_Amt,$r4_Cur,$r5_Pid,$r6_Order,$r7_Uid,$r8_MP,$r9_BType,$p1_MerId,$merchantKey))
        return true;
    else
        return false;
}


function HmacMd5($data,$key)
{
// RFC 2104 HMAC implementation for php.
// Creates an md5 HMAC.
// Eliminates the need to install mhash to compute a HMAC
// Hacked by Lance Rushing(NOTE: Hacked means written)

//需要配置环境支持iconv，否则中文参数不能正常处理
    $key = iconv("GB2312","UTF-8",$key);
    $data = iconv("GB2312","UTF-8",$data);

    $b = 64; // byte length for md5
    if (strlen($key) > $b) {
        $key = pack("H*",md5($key));
    }
    $key = str_pad($key, $b, chr(0x00));
    $ipad = str_pad('', $b, chr(0x36));
    $opad = str_pad('', $b, chr(0x5c));
    $k_ipad = $key ^ $ipad ;
    $k_opad = $key ^ $opad;

    return md5($k_opad . pack("H*",md5($k_ipad . $data)));
}

function logstr($orderid,$str,$hmac)
{
    include './merchantProperties.php';
    $james=fopen($logName,"a+");
    fwrite($james,"\r\n".date("Y-m-d H:i:s")."|orderid[".$orderid."]|str[".$str."]|hmac[".$hmac."]");
    fclose($james);
}

?> 