<?php
session_start();
/**
 * 足球综合过关下注接口
 * order/FT_order_p_finish_api.php  足球综合过关下注接口
 * active   1 足球今日赛事, 11 足球早餐
 * teamcount
 * gold  金额
 * wagerDatas
 * randomNum 随机数
 */
//error_reporting(E_ALL);
//ini_set('display_errors','On');
include('../include/address.mem.php');
include_once('../include/config.inc.php');
require_once("../../../common/sportCenterData.php");
//require ("../include/define_function_list.inc.php");
include_once("../../../common/sportapi/define_function_list.inc.php");

$langx=$_SESSION['Language'];
require ("../include/traditional.$langx.inc.php");

include_once "../../../common/order/FT_order_p_finish_api.php";
