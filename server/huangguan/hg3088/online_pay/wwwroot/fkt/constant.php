<?php


define("GATEWAY_URL", "http://pay.fktpay.vip/gateway/pay.html");//支付平台网关
define("MER_NO", "88393230");//这里填写商户号
define("MER_KEY", "098f6bcd4621d373cade4e832627b4f6");//这里填写签名时需要的私钥key
define("CHARSET", "UTF-8");//当前系统字符集编码
define("BACK_NOTIFY_URL", $_SERVER['HTTP_HOST']."/fkt_return_url.php");// 这里填写支付完成后，支付平台后台通知当前支付是否成功的URL
define("PAGE_NOTIFY_URL", $_SERVER['HTTP_HOST']."/fkt_page_url.php");// 这里填写支付完成后，页面跳转到商户页面的URL，同时告知支付是否成功


define("PAY_TYPE", "1");//支付方式，目前暂只支持网银支付，取值为1
define("REQ_REFERER", $_SERVER['HTTP_HOST']);//请指定当前系统的域名，用来防钓鱼，例如www.mer.com
define("DATE_TIME_FORMAT", "Y-m-d H:i:s");//默认时间格式化

define("REQ_CUSTOMER_ID", null);//手动设置请求消费者IP地址，主要是用于测试环境，生产环境请设置为null


