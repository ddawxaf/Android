<?php
/**
 * 代理退水设置
 * Date: 2019/12/14
 */
include ("../../agents/include/address.mem.php");
require ("../../agents/include/config.inc.php");


checkAdminLogin(); // 同一账号不能同时登陆

// 验证管理员session
if( (!isset($_SESSION['Oid']) || $_SESSION['Oid'] == "" || !isset($_SESSION['is_admin']) || $_SESSION['is_admin'] != ADMINLOGINFLAG) && $_SESSION['admin_level'] != 'D' ) {
    echo "<script>alert('您的登录信息已过期,请重新登录!');top.location.href='/';</script>";
    exit;
}

$loginname = $_SESSION['UserName'];
$now = date('Y-m-d H:i:s');
$action = isset($_REQUEST['action']) && $_REQUEST['action'] ? $_REQUEST['action'] : '';
$submit = isset($_REQUEST['submit']) && $_REQUEST['submit'] ? $_REQUEST['submit'] : '';
$id = isset($_REQUEST['id']) && $_REQUEST['id'] ? $_REQUEST['id'] : '';

$sAgentRebateSet = '';
if($submit == 1){
    $money = isset($_REQUEST['money']) && $_REQUEST['money'] ? $_REQUEST['money'] : '';
    $rate_hg = isset($_REQUEST['hg']) && $_REQUEST['hg'] ? $_REQUEST['hg'] : 0;
    $rate_cp = isset($_REQUEST['cp']) && $_REQUEST['cp'] ? $_REQUEST['cp'] : 0;
    $rate_ssc = isset($_REQUEST['ssc']) && $_REQUEST['ssc'] ? $_REQUEST['ssc'] : 0;
    $rate_project = isset($_REQUEST['project']) && $_REQUEST['project'] ? $_REQUEST['project'] : 0;
    $rate_trace = isset($_REQUEST['trace']) && $_REQUEST['trace'] ? $_REQUEST['trace'] : 0;
    $rate_ag = isset($_REQUEST['ag']) && $_REQUEST['ag'] ? $_REQUEST['ag'] : 0;
    $rate_og = isset($_REQUEST['og']) && $_REQUEST['og'] ? $_REQUEST['og'] : 0;
    $rate_avia = isset($_REQUEST['avia']) && $_REQUEST['avia'] ? $_REQUEST['avia'] : 0;
    $rate_fire = isset($_REQUEST['fire']) && $_REQUEST['fire'] ? $_REQUEST['fire'] : 0;
    $rate_ag_dianzi = isset($_REQUEST['ag_dianzi']) && $_REQUEST['ag_dianzi'] ? $_REQUEST['ag_dianzi'] : 0;
    $rate_mg = isset($_REQUEST['mg']) && $_REQUEST['mg'] ? $_REQUEST['mg'] : 0;
    $rate_cq = isset($_REQUEST['cq']) && $_REQUEST['cq'] ? $_REQUEST['cq'] : 0;
    $rate_mw = isset($_REQUEST['mw']) && $_REQUEST['mw'] ? $_REQUEST['mw'] : 0;
    $rate_fg = isset($_REQUEST['fg']) && $_REQUEST['fg'] ? $_REQUEST['fg'] : 0;
    $rate_ag_dayu = isset($_REQUEST['ag_dayu']) && $_REQUEST['ag_dayu'] ? $_REQUEST['ag_dayu'] : 0;
    $rate_ky = isset($_REQUEST['ky']) && $_REQUEST['ky'] ? $_REQUEST['ky'] : 0;
    $rate_vgqp = isset($_REQUEST['vgqp']) && $_REQUEST['vgqp'] ? $_REQUEST['vgqp'] : 0;
    $rate_hgqp = isset($_REQUEST['hgqp']) && $_REQUEST['hgqp'] ? $_REQUEST['hgqp'] : 0;
    $rate_lyqp = isset($_REQUEST['lyqp']) && $_REQUEST['lyqp'] ? $_REQUEST['lyqp'] : 0;
    $rate_klqp = isset($_REQUEST['klqp']) && $_REQUEST['klqp'] ? $_REQUEST['klqp'] : 0;
    $rate_bbin = isset($_REQUEST['bbin']) && $_REQUEST['bbin'] ? $_REQUEST['bbin'] : 0;

    $agentSet = [
        'hg' => $rate_hg,
        'cp' => $rate_cp,
        'ssc' => $rate_ssc,
        'project' => $rate_project,
        'trace' => $rate_trace,
        'ag' => $rate_ag,
        'og' => $rate_og,
        'avia' => $rate_avia,
        'fire' => $rate_fire,
        'ag_dianzi' => $rate_ag_dianzi,
        'mg' => $rate_mg,
        'cq' => $rate_cq,
        'mw' => $rate_mw,
        'fg' => $rate_fg,
        'ag_dayu' => $rate_ag_dayu,
        'ky' => $rate_ky,
        'vgqp' => $rate_vgqp,
        'hgqp' => $rate_hgqp,
        'lyqp' => $rate_lyqp,
        'klqp' => $rate_klqp,
        'bbin' => $rate_bbin,
    ];
    $sAgentRebateSet = json_encode($agentSet);
}

$agentRebateSet = [];
if ($action == 'edit') {
    if ($submit == 1) { // 编辑页面提交
        $sql = "UPDATE " . DBPREFIX . "agent_rebate_set SET money='" . $money . "' ,rebate='" . $sAgentRebateSet. "',updated_at='" . $now . "' WHERE `id`='" . $id . "'";
        $result = mysqli_query($dbMasterLink, $sql);
        if($result){
            insertLog("更新【{$money}】为：【{$sAgentRebateSet}】成功");
            echo "<script> alert('更新成功！');location.href='water_rate_list.php';</script>";
        }else{
            insertLog("更新【{$money}】为：【{$sAgentRebateSet}】失败");
            echo "<script> alert('更新失败！');history.back();</script>";
        }
    } else { // 编辑页面
        $sWhere = '1';
        if($id){
            $sWhere .= ' AND `id`="' . $id . '"';
        }
        $sql = 'SELECT `money`, `rebate` FROM ' . DBPREFIX . 'agent_rebate_set WHERE ' . $sWhere;
        $result = mysqli_query($dbLink, $sql);
        $row = mysqli_fetch_assoc($result);
        $money = $row['money'];
        $agentRebateSet = json_decode($row['rebate'], true);
    }
}else if($action == 'add'){
    if ($submit == 1) { // 添加页面提交
        $sql = "INSERT INTO " . DBPREFIX . "agent_rebate_set(`money`,`rebate`,`created_at`,`updated_at`) VALUES ('" . $money . "', '" . $sAgentRebateSet . "','" . $now . "', '" . $now . "')";
        $insertId = mysqli_query($dbMasterLink, $sql);
        if($insertId){
            insertLog("添加【{$money}】为：【{$sAgentRebateSet}】成功");
            echo "<script> alert('添加成功！');location.href='water_rate_list.php';</script>";
        }else{
            insertLog("添加【{$money}】为：【{$sAgentRebateSet}】失败");
            echo "<script> alert('添加失败！');history.back();</script>";
        }
    }
}

function insertLog($info){
    global $dbMasterLink, $loginname;
    $ipAddress = get_ip();
    $info = "代理退水设置" . $info;
    $mysql = "insert into ".DBPREFIX."web_mem_log_data(UserName,Logintime,ConText,Loginip,Url,Level) values('$loginname',now(),'$info','$ipAddress','".BROWSER_IP."','管理员')";
    mysqli_query($dbMasterLink, $mysql);
}

?>

<html>
<head>
    <title>代理退水设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 ">
    <link rel="stylesheet" href="../../../style/agents/control_main.css?v=<?php echo AUTOVER; ?>" type="text/css">
    <style type="text/css">
        #myFORM td { padding: 3.5px 0 0  8px;}
        input.za_text {width: 300px;}
    </style>
</head>
<body  >
<dl class="main-nav">
    <dt>代理退水设置</dt>
    <dd></dd>
</dl>
<div class="main-ui width_1000">
    <FORM NAME="myFORM" action="" method="post">
        <input type="hidden" name="action" value="<?php echo $action?>">
        <input type="hidden" name="submit" value="1">
        <table class="m_tab_ed">
            <tr>
                <td colspan="2" ><h4><?php echo ($money ? '输赢金额：【>=' . $money . '】' : '添加')?>退水设置</h4></td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed" width="140">投注总额：</td>
                <td align="left"><input type="text" name="money" value="<?php echo $money; ?>" class="za_text">大于等于</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">皇冠体育：</td>
                <td align="left"><input type="text" name="hg" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['hg'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">体育彩票：</td>
                <td align="left"><input type="text" name="cp" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['cp'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">国民彩票信用盘：</td>
                <td align="left"><input type="text" name="ssc" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['ssc'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">国民彩票官方盘：</td>
                <td align="left"><input type="text" name="project" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['project'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">国民彩票追号：</td>
                <td align="left"><input type="text" name="trace" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['trace'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">AG视讯：</td>
                <td align="left"><input type="text" name="ag" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['ag'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">OG视讯：</td>
                <td align="left"><input type="text" name="og" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['og'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">BBIN视讯：</td>
                <td align="left"><input type="text" name="bbin" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['bbin'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">泛亚电竞：</td>
                <td align="left"><input type="text" name="avia" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['avia'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">雷火电竞：</td>
                <td align="left"><input type="text" name="fire" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['fire'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">AG电子：</td>
                <td align="left"><input type="text" name="ag_dianzi" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['ag_dianzi'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">MG电子：</td>
                <td align="left"><input type="text" name="mg" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['mg'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">CQ9电子：</td>
                <td align="left"><input type="text" name="cq" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['cq'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">MW电子：</td>
                <td align="left"><input type="text" name="mw" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['mw'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">FG电子：</td>
                <td align="left"><input type="text" name="fg" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['fg'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">AG捕鱼王：</td>
                <td align="left"><input type="text" name="ag_dayu" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['ag_dayu'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">开元棋牌：</td>
                <td align="left"><input type="text" name="ky" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['ky'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">VG棋牌：</td>
                <td align="left"><input type="text" name="vgqp" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['vgqp'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
          <!--  <tr class="m_bc_ed">
                <td class="m_co_ed">皇冠棋牌：</td>
                <td align="left"><input type="text" name="hgqp" value="<?php /*echo (!empty($agentRebateSet) ? $agentRebateSet['hgqp'] : '') */?>" class="za_text">%，建议0%</td>
            </tr>-->
            <tr class="m_bc_ed">
                <td class="m_co_ed">乐游棋牌：</td>
                <td align="left"><input type="text" name="lyqp" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['lyqp'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed">
                <td class="m_co_ed">快乐棋牌：</td>
                <td align="left"><input type="text" name="klqp" value="<?php echo (!empty($agentRebateSet) ? $agentRebateSet['klqp'] : '') ?>" class="za_text">%，建议0%</td>
            </tr>
            <tr class="m_bc_ed" align="center">
                <td colspan="2">
                    <input type=SUBMIT name="OK" value="确认" class="za_button">
                    &nbsp; &nbsp; &nbsp;
                    <input type=BUTTON name="FormsButton2" value="取消" id="FormsButton2" onClick="window.location.replace('water_rate_list.php');" class="za_button">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
