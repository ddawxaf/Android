<?php
/**
 * 自定义皇冠棋牌API
 * Date: 2018/8/23
 */
include_once('../include/config.inc.php');
include_once "../include/hgqp/ApiHg.php";

if(!isset($_SESSION['Oid']) || $_SESSION['Oid'] == ''){
    original_phone_request_response('401.1', '您的登录信息已过期，请您重新登录！');
}
$uid = $_SESSION['Oid'];

// 1.检测当前登录会员&查询会员信息
$stmt = $dbLink->prepare("SELECT `ID`, `Money`, `layer`, `test_flag`, `UserName`, `Agents`, `World`, `Corprator`, `Super`, `Admin`, `Phone` FROM ".DBPREFIX.MEMBERTABLE." where `Oid` = ? and Status <= 1 LIMIT 1");
$stmt->bind_param('s', $uid);
$stmt->execute();
if(!$stmt->affected_rows) {
    original_phone_request_response('401.1', '您的登录信息已过期，请您重新登录！');
}
$aUser = $stmt->get_result()->fetch_assoc();

//判断终端类型
if($_REQUEST['appRefer'] == 13 || $_REQUEST['appRefer'] == 14) {
    $playSource = $_REQUEST['appRefer'];
}
else{
    if(strpos($_SERVER['HTTP_USER_AGENT'], 'iPhone')||strpos($_SERVER['HTTP_USER_AGENT'], 'iPad')){
        $playSource = 3;
    }else if(strpos($_SERVER['HTTP_USER_AGENT'], 'Android')){
        $playSource = 4;
    }else{
        $playSource = 5;
    }
}

if ($_SESSION['Agents']=='demoguest'){
//    exit(json_encode( ['err' => '-2', 'msg' => '请使用真实账号登入HG棋牌'] ) );
    $status = '401.2';
    $describe = '请使用真实账号登入HG棋牌';
    original_phone_request_response($status,$describe);
}

// 2.接收参数
$action = isset($_REQUEST['action']) && $_REQUEST['action'] ? trim($_REQUEST['action']) : '';
$exchangeFrom = isset($_REQUEST['f']) && $_REQUEST['f'] ? trim($_REQUEST['f']) : '';
$exchangeTo = isset($_REQUEST['t']) && $_REQUEST['t'] ? trim($_REQUEST['t']) : '';
$score = isset($_REQUEST['b']) && $_REQUEST['b'] ? trim($_REQUEST['b']) : 0;
if($exchangeFrom == 'hg' && $exchangeTo == 'ff')
    $action = 'hg2ff';
if($exchangeFrom == 'ff' && $exchangeTo == 'hg')
    $action = 'ff2hg';
$sUserlayer = $aUser['layer'];
// 转账时，校验操作额度分层
if ($action=='hg2ff' || $action == 'ff2hg'){
    // 检查当前会员是否设置不准操作额度分层
    // 检查分层是否开启 status 1 开启 0 关闭
    // layer 1 不返水 2 不优惠 3 不准操作资金 4 不准领取彩金 5 仅限可以投注体育，不能额度转换去其它馆
    if ($sUserlayer==3 || $sUserlayer==5){
        $layer = getUserLayerById($sUserlayer);
        if ($layer['status']==1) {
            $status = '401.66';
            $describe = '账号分层异常，请联系我们在线客服';
            original_phone_request_response($status,$describe);
        }
    }
}
$testFlag = isset($_REQUEST['flag']) && $_REQUEST['flag'] ? trim($_REQUEST['flag']) : '';

// 3.检测登录皇冠会员
$ffExist = 0;
$now = date('Y-m-d H:i:s');
if($action){
    $result = mysqli_query($dbLink, "SELECT `id` FROM `" . DBPREFIX . "ff_member_data` WHERE `userid` = '{$aUser['ID']}' LIMIT 1");
    $ffExist = mysqli_num_rows($result);
    if(!$ffExist){ // 未创建账号前请求余额接口
        if($action == 'b'){
            $data = [
                'ff_balance' => '0.00',
                'hg_balance' => formatMoney($aUser['Money'])
            ];
            original_phone_request_response('200', '获取用户余额成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
        }else if($action == 'hg2ff' || $action == 'ff2hg'){ // 未创建账号前请求额度转换接口
            // 先请求注册接口（非凡三方未处理）
            $params = [
                'method' => $testFlag == 'test' ? 'getTryGameUrl' : 'loginUrl',
                'sitemid' => $aUser['UserName'],
                'agent' => $aUser['Agents'],
            ];
            $aResult = ffApi($params);
            if($aResult['code'] == 1) { // 成功请求登录
                $data = [
                    'userid' => $aUser['ID'],
                    'username' => $aUser['UserName'],
                    'channel' => $ffConfig['channel'],
                    'agents' => $aUser['Agents'],
                    'world' => $aUser['World'],
                    'corporator' => $aUser['Corprator'],
                    'super' => $aUser['Super'],
                    'admin' => $aUser['Admin'],
                    'register_time' => $now,
                    'last_launch_time' => $now,
                    'launch_times' => 1,
                    'is_test' => $aUser['test_flag']
                ];
                $sInsData = '';
                foreach ($data as $key => $value){
                    $sInsData .= "`$key` = '{$value}'" . ($key == 'is_test' ? '' : ',');
                }
                $sql = "INSERT INTO `" . DBPREFIX . "ff_member_data` SET $sInsData";
                if (!mysqli_query($dbMasterLink, $sql)) {
                    original_phone_request_response('401.2', '皇冠棋牌账号异常，请您稍后重试！');
                }
            }else{
                original_phone_request_response('401.3', '皇冠棋牌账号异常，请您稍后重试！');
            }
        }
    }
}

switch ($action){
    case "cm":
        $params = [
            'method' => $testFlag == 'test' ? 'getTryGameUrl' : 'loginUrl',
            'sitemid' => $aUser['UserName'],
            'agent' => $aUser['Agents'],
        ];
        $aResult = ffApi($params);
        if($aResult['code'] == 1) { // 成功请求登录
            if($ffExist == 0){ // 若无平台皇冠棋牌账号，则注册会员入库
                $data = [
                    'userid' => $aUser['ID'],
                    'username' => $aUser['UserName'],
                    'channel' => $ffConfig['channel'],
                    'agents' => $aUser['Agents'],
                    'world' => $aUser['World'],
                    'corporator' => $aUser['Corprator'],
                    'super' => $aUser['Super'],
                    'admin' => $aUser['Admin'],
                    'register_time' => $now,
                    'last_launch_time' => $now,
                    'launch_times' => 1,
                    'is_test' => $aUser['test_flag']
                ];
                $sInsData = '';
                foreach ($data as $key => $value){
                    $sInsData .= "`$key` = '{$value}'" . ($key == 'is_test' ? '' : ',');
                }
                $sql = "INSERT INTO `" . DBPREFIX . "ff_member_data` SET $sInsData";
                if (!mysqli_query($dbMasterLink, $sql)) {
                    original_phone_request_response('401.4', '皇冠棋牌账号异常，请您稍后重试！',$aData);
                }
            }else{ // 若有平台皇冠账号，则更新登录信息
                mysqli_query($dbMasterLink, "update " . DBPREFIX . "ff_member_data set launch_times = launch_times + 1, last_launch_time = '$now'  WHERE userid = '{$aUser['ID']}'");
            }
            original_phone_request_response('200', '恭喜！创建皇冠棋牌账号成功！', $aResult['data']);
        } else {
            original_phone_request_response('401.5', '皇冠棋牌账号异常，请您稍后重试！');
        }
        break;
    case 'b':
        $aResult = checkBalance($aUser['UserName']);
        $data = [];
        if($aResult['code'] == 1){
            $data = [
                'ff_balance' => sprintf('%.2f', $aResult['data']['money'] / 100), // 返回余额分模式
                'hg_balance' => formatMoney($aUser['Money'])
            ];
            original_phone_request_response('200', '获取用户余额成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
        }else{
            original_phone_request_response('401.6', '皇冠棋牌余额获取失败，请稍后重试！');
        }
        break;
    case 'hg2ff':
        // 1.参数校验
        if(!preg_match("/^[1-9][0-9]*$/", $score))
            original_phone_request_response('401.7', '转账金额格式错误，请重新输入!');

        if ($score > 10000000)
            original_phone_request_response('401.8', '单次上分不能超过一千万，请重新输入!');

        // 2.事务处理
        $dbMasterLink->autocommit(false);

        // 2.1.事务内查询用户余额，后续用于更新用户余额
        $result = mysqli_query($dbMasterLink, 'SELECT `ID`, `Money` FROM '.DBPREFIX.MEMBERTABLE.' WHERE `ID` = ' . $aUser['ID'] . ' FOR UPDATE');
        $aForUpdate = mysqli_fetch_assoc($result);
        $beforeBalance = $aForUpdate['Money']; // 转换之前余额
        if(intval($beforeBalance) < intval($score)) { // 余额不足
            original_phone_request_response('401.9', '中心钱包不足!');
        }
        $afterBalance = bcsub($beforeBalance, $score, 4); // 转换之后余额

        // 更新会员余额
        if(!$updated = mysqli_query($dbMasterLink, 'UPDATE '.DBPREFIX.MEMBERTABLE.' SET `Money` = ' . $afterBalance . ' WHERE `ID` = ' . $aUser['ID'])) {
            $dbMasterLink->rollback();
            original_phone_request_response('401.10', '额度转换失败，请您稍后重试!');
        }
        // 皇冠棋牌-orderId
        $time_str = date('YmdHis'); // 美东时间
        $orderId = $ffConfig['agent'] . $time_str . $aUser['UserName'];

        // 2.2.入库额度转换表
        $data = [
            'userid' => $aUser['ID'],
            'Checked' => 1,
            'Gold' => $score,
            'moneyf' => $beforeBalance,
            'currency_after' => $afterBalance,
            'AddDate' => $now,
            'Type' => 'Q',
            'From' => 'hg',
            'To' => 'ff',
            'UserName' => $aUser['UserName'],
            'Agents' => $aUser['Agents'],
            'World' => $aUser['World'],
            'Corprator' => $aUser['Corprator'],
            'Super' => $aUser['Super'],
            'Admin' => $aUser['Admin'],
            'CurType' => 'RMB',
            'Date' => $now,
            'Name' => $aUser['Alias'],
            'Waterno' => '',
            'Phone' => $aUser['Phone'],
            'Notes' => '即时入账',
            'Order_Code' => $orderId,
            'reason' => 'FF TO TY',
            'AuditDate' => $now,
            'test_flag' => $aUser['test_flag']
        ];

        $sInsData = '';
        foreach ($data as $key => $value){
            $sInsData .= "`$key` = '{$value}'" . ($key == 'test_flag' ? '' : ',');
        }
        $sql = "INSERT INTO `" . DBPREFIX . "web_sys800_data` SET $sInsData";
        if (!$inserted = mysqli_query($dbMasterLink, $sql)) {
            $dbMasterLink->rollback();
            original_phone_request_response('401.11', '额度转换失败，请您稍后重试!');
        }
        $insertId = mysqli_insert_id($dbMasterLink); // 后续入账变

        // 2.3.入库账变
        $data = [
            $aUser['ID'],
            $aUser['UserName'],
            $aUser['test_flag'],
            $beforeBalance,
            -$score,
            $afterBalance,
            28, // type：28皇冠棋牌
            $playSource, // 类型
            $insertId,
            '皇冠棋牌额度转换'
        ];
        if(!$inserted = addAccountRecords($data)){
            $dbMasterLink->rollback();
            original_phone_request_response('401.12', '额度转换失败，请您稍后重试!');
        }

        // 2.4.调用三方上分接口
        $params = [
            'method' => 'depositMoney',
            'sitemid' => $aUser['UserName'],
            'amount' => $score * 100, // 元模式转分模式
            'order_id' => $orderId
        ];
        $aResult = ffApi($params);
        if($aResult){
            if($aResult['code'] == 1){
                $dbMasterLink->commit();
                $data = [
                    'ff_balance' => sprintf('%.2f', $aResult['data']['money'] / 100), // 分模式转元模式
                    'hg_balance' => formatMoney($afterBalance)
                ];
                original_phone_request_response('200', '额度转换成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
            }else{
                $dbMasterLink->rollback();
                original_phone_request_response('401.13', '额度转换失败，请您稍后重试!');
            }
        }else{ // 超时or错误返回NULL，则查询订单状态
            $aResult = checkOrder($aUser['UserName'], $orderId);
            if($aResult){
                if($aResult['code'] == 1) {
                    $dbMasterLink->commit();
                    $data = [
                        'ff_balance' => sprintf('%.2f', $aResult['data']['money'] / 100), // 分模式转元模式
                        'hg_balance' => formatMoney($afterBalance)
                    ];
                    original_phone_request_response('200', '额度转换成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
                }else{
                    $dbMasterLink->rollback();
                    original_phone_request_response('401.14', '额度转换失败，请您稍后重试!');
                }
            }else{ // 超时or错误返回NULL
                $dbMasterLink->rollback();
                original_phone_request_response('401.15', '额度转换失败，请您稍后重试!');
            }
        }
        break;
    case 'ff2hg':
        // 1.参数校验
        if(!preg_match("/^[1-9][0-9]*$/", $score))
            original_phone_request_response('401.16', '转账金额格式错误，请重新输入!');

        if ($score > 10000000)
            original_phone_request_response('401.17', '单次下分不能超过一千万，请重新输入!');

        // 2.查询皇冠棋牌可下分余额
        $ffBalance = '0.00';
        $aResult = checkBalance($aUser['UserName']);
        if(isset($aResult['code']) && $aResult['code'] == 1){
            $ffBalance = formatMoney($aResult['data']['money'] / 100);
        }else{
            original_phone_request_response('401.18', '皇冠棋牌余额获取失败，请稍后重试!');
        }
        if(intval($ffBalance) < intval($score))
            original_phone_request_response('401.19', '皇冠棋牌余额不足!');

        // 3.事务处理
        $dbMasterLink->autocommit(false);

        // 3.1.事务内查询用户余额，后续用于更新用户余额
        $result = mysqli_query($dbMasterLink, 'SELECT `ID`, `Money` FROM '.DBPREFIX.MEMBERTABLE.' WHERE `ID` = ' . $aUser['ID'] . ' FOR UPDATE');
        $aForUpdate = mysqli_fetch_assoc($result);
        $beforeBalance = $aForUpdate['Money']; // 下分转换之前余额
        $afterBalance = bcadd($beforeBalance, $score, 4); // 下分转换之后余额

        // 更新会员余额
        if(!$updated = mysqli_query($dbMasterLink, 'UPDATE '.DBPREFIX.MEMBERTABLE.' SET `Money` = ' . $afterBalance . ' WHERE `ID` = ' . $aUser['ID'])) {
            $dbMasterLink->rollback();
            original_phone_request_response('401.20', '额度转换失败，请您稍后重试!');
        }
        // 皇冠棋牌-orderId
        $time_str = date('YmdHis'); // 美东时间
        $orderId = $ffConfig['agent'] . $time_str . $aUser['UserName'];

        // 3.2.入库额度转换表
        $data = [
            'userid' => $aUser['ID'],
            'Checked' => 1,
            'Gold' => $score,
            'moneyf' => $beforeBalance,
            'currency_after' => $afterBalance,
            'AddDate' => $now,
            'Type' => 'Q',
            'From' => 'ff',
            'To' => 'hg',
            'UserName' => $aUser['UserName'],
            'Agents' => $aUser['Agents'],
            'World' => $aUser['World'],
            'Corprator' => $aUser['Corprator'],
            'Super' => $aUser['Super'],
            'Admin' => $aUser['Admin'],
            'CurType' => 'RMB',
            'Date' => $now,
            'Name' => $aUser['Alias'],
            'Waterno' => '',
            'Phone' => $aUser['Phone'],
            'Notes' => '即时入账',
            'Order_Code' => $orderId,
            'reason' => 'TY TO FF',
            'AuditDate' => $now,
            'test_flag' => $aUser['test_flag']
        ];
        $sInsData = '';
        foreach ($data as $key => $value){
            $sInsData .= "`$key` = '{$value}'" . ($key == 'test_flag' ? '' : ',');
        }
        $sql = "INSERT INTO `" . DBPREFIX . "web_sys800_data` SET $sInsData";
        if (!$inserted = mysqli_query($dbMasterLink, $sql)) {
            $dbMasterLink->rollback();
            original_phone_request_response('401.21', '额度转换失败，请您稍后重试!');
        }
        $insertId = mysqli_insert_id($dbMasterLink); // 后续入账变

        // 3.3.入库账变
        $data = [
            $aUser['ID'],
            $aUser['UserName'],
            $aUser['test_flag'],
            $beforeBalance,
            $score,
            $afterBalance,
            29, // type：29皇冠棋牌
            $playSource, // 类型
            $insertId,
            '皇冠棋牌额度转换'
        ];
        if(!$inserted = addAccountRecords($data)){
            $dbMasterLink->rollback();
            original_phone_request_response('401.22', '额度转换失败，请您稍后重试!');
        }

        // 3.4.调用三方下分接口
        $params = [
            'method' => 'debitMoney',
            'sitemid' => $aUser['UserName'],
            'amount' => $score * 100, // 元模式转分模式
            'order_id' => $orderId
        ];
        $aResult = ffApi($params);
        if($aResult){
            if($aResult['code'] == 1){
                $dbMasterLink->commit();
                $data = [
                    'ff_balance' => sprintf('%.2f', $aResult['data']['money'] / 100), // 分模式转元模式
                    'hg_balance' => formatMoney($afterBalance)
                ];
                original_phone_request_response('200', '额度转换成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
            }else{
                $dbMasterLink->rollback();
                original_phone_request_response('401.23', '额度转换失败，请您稍后重试!');
            }
        }else{ // 超时or错误返回NULL，则查询订单状态
            $aResult = checkOrder($aUser['UserName'] ,$orderId);
            if($aResult){
                if($aResult['code'] == 1) {
                    $dbMasterLink->commit();
                    $data = [
                        'ff_balance' => sprintf('%.2f', $aResult['data']['money'] / 100), // 分模式转元模式
                        'hg_balance' => formatMoney($afterBalance)
                    ];
                    original_phone_request_response('200', '额度转换成功！', $playSource == 13 || $playSource == 14 ? [$data] : $data);
                }else{
                    $dbMasterLink->rollback();
                    original_phone_request_response('401.24', '额度转换失败，请您稍后重试!');
                }
            }else{ // 超时or错误返回NULL
                $dbMasterLink->rollback();
                original_phone_request_response('401.25', '额度转换失败，请您稍后重试!');
            }
        }
        break;
    default:
        original_phone_request_response('401.26', '抱歉，您的请求不予处理!');
        break;
}

/**
 * 查询可下分余额
 * @param $username
 * @return array|mixed
 */
function checkBalance($username)
{
    $params = [
        'method' => 'getUserMoney',
        'sitemid' => $username
    ];
    $aResult = ffApi($params);
    return $aResult;
}

/**
 * 查询额度转换订单
 * @param $username
 * @param $orderId
 * @return array|mixed
 */
function checkOrder($username, $orderId)
{
    $params = [
        'method' => 'getTransRecord',
        'sitemid' => $username,
        'orderid' => $orderId,
    ];
    $aResult = ffApi($params);
    return $aResult;
}

/**
 * 调用接口
 * @param $params
 * @return array|mixed
 */
function ffApi($params){
    global $ffConfig;
    $off = new \mobile\ApiHg($ffConfig);
    try {
        ob_start();
        $res = $off->main($params);
        return json_decode($res, true);
    } catch (Exception $e) {
        ob_end_clean();
        return array(
            'm' => 'ffProxy',
            's' => $params['method'],
            'd' => array(
                'code' => -1,
                'message' => $e->getMessage()
            )
        );
    } finally {
        ob_end_flush();
        closelog();
    }
}