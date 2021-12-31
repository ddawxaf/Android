<?php
/**
 * 雷火电竞日结报表（每次执行的时间区间必须为1个天）
 *
 * Date: 2020/04/29
 */

define("CONFIG_DIR", dirname(dirname(dirname(__FILE__))));
require_once CONFIG_DIR . "/app/agents/include/config.inc.php";
require_once CONFIG_DIR."/app/agents/include/redis.php";
require_once ROOT_DIR . '/common/thunfire/api.php';

if (php_sapi_name() == "cli") {
    $today = date('Y-m-d');
    $yesterday = date('Y-m-d', strtotime('-1 day'));
    $startTime = isset($argv[1]) && $argv[1] ? $argv[1] : $yesterday;
    $endTime = isset($argv[2]) && $argv[2] ? $argv[2] : $today;
    $isRepeat = isset($argv[3]) && $argv[3] ? $argv[3] : 0;

    if($startTime > $yesterday)
        exit("【" . date('Y-m-d H:i:s') . "】开始时间>昨日【{$startTime} > {$yesterday}】\n");

    countDaiFireWinLoss();
}

function countDaiFireWinLoss()
{
    global $dbMasterLink, $dbLink, $startTime, $endTime, $isRepeat;

    // 1. 判断数据是否已统计
    if($isRepeat){
        echo "【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞重新统计数据\n";
    }else{
//        $sql = "SELECT `id`, `last_report_date`, `is_count` FROM " . DBPREFIX . "fire_report_log
//            WHERE `is_count` = 1
//            ORDER BY `last_report_date` DESC LIMIT 1";
//        $result  = mysqli_query($dbLink, $sql);
//        $row = mysqli_fetch_assoc($result);
//        $iCount = mysqli_num_rows($result);
//        if($iCount){
//            $lastCountDate = $row['last_report_date'];
//            if(date('Y-m-d', strtotime("{$lastCountDate} + 1 day")) != $startTime){
//                exit("【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计日期错误【{$startTime}~{$endTime}】，最近最后一次统计日期【{$lastCountDate}】\n");
//            }
//        }
    }
    echo "【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计日期美东时间：【{$startTime}~{$endTime}】\n";

    // 2.统计
    // 2.1.删除重复历史统计数据&日志报表数据
    mysqli_query($dbMasterLink, "DELETE FROM " . DBPREFIX . "fire_history_report WHERE `count_date` >= '{$startTime}' AND `count_date` < '{$endTime}'");
    mysqli_query($dbMasterLink, "DELETE FROM " . DBPREFIX . "fire_report_log WHERE `last_report_date` >= '{$startTime}' AND `last_report_date` < '{$endTime}'");

    // 2.2.事务处理
    $dbMasterLink->autocommit(false);
    $sql = "SELECT `userid`, `username`, `is_test`, `agents`, SUM(1) AS `total_times`, SUM(`amount`) AS `total_cellscore`, SUM(`amount`) AS `total_bet`, SUM(`reward`) AS `total_profit`,
            DATE_FORMAT(settlement_datetime,'%Y-%m-%d') AS `count_date`
            FROM " . DBPREFIX . "fire_projects
            WHERE `settlement_datetime` >= '{$startTime}' AND `settlement_datetime` < '{$endTime}' AND result_status != ''
            GROUP BY `userid`";
    /*$sql = "SELECT `userid`,`username`,`is_test`,`orderID`,`odds`,`malay_odds`,`euro_odds`,`member_odds`,`member_odds_style`,`game_type_id`,`game_type_name`,`game_market_name`,`market_option`,`map_num`,`bet_type_name`,`competition_name`,`event_id`,`event_name`,`event_datetime`,`date_created`,`settlement_datetime`,`modified_datetime`,`bet_selection`,`currency`,`amount`,`settlement_status`,`result_status`,`result`,`earnings`,`is_combo`,`ticket_type`
            DATE_FORMAT(settlement_datetime,'%Y-%m-%d') AS `count_date`
            FROM " . DBPREFIX . "fire_projects
            WHERE `settlement_datetime` >= '{$startTime}' AND `settlement_datetime` < '{$endTime}' AND result_status != ''
            GROUP BY `userid`";*/
    $result = mysqli_query($dbLink, $sql);
    $count = mysqli_num_rows($result);
    $now = date('Y-m-d H:i:s');
    $countDate = [];
    if($count == 0){
        $countDate[] = $startTime;
        echo "【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞暂无注单记录\n";
    }else{
        $countData = $countDate = [];
        // 投注总额、盈利总额 分2次统计
        while ($row = mysqli_fetch_assoc($result)){
            $countData[] = [
                'userid' => $row['userid'],
                'username' => $row['username'],
                'is_test' => $row['is_test'],
                'agents' => $row['agents'],
                'total_times' => $row['total_times'],
                'total_cellscore' => $row['total_cellscore'],
                'total_bet' => $row['total_bet'],
                'total_profit' => $row['total_profit'],
                'count_date' => $row['count_date'],
                'created_at' => $now,
            ];
            $countDate[] = $row['count_date'];
        }

        if ($count<1){
            exit("【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计报表生成失败，原因：记录数【{$count}】\n");
        }

        // 批量入库报表
        $keys = $values = '';
        foreach ($countData as $key => $value){
            $keys = '(' . implode(',', array_keys($value)) . ')';
            $values .= "('" . implode("','", array_values($value)) . "')" . ($key + 1 == $count ? '' : ',');
        }
        $sql = "INSERT INTO " . DBPREFIX . "fire_history_report {$keys} VALUES {$values}";
        //echo $sql;exit;
        if(!$inserted = mysqli_query($dbMasterLink, $sql)){
            $dbMasterLink->rollback();
            exit("【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计报表生成失败，原因：入库报表失败【{$inserted}】\n");
        }
    }

    $logData = [];
    $countDate = array_unique($countDate); // 去重
    $count = count($countDate);
    foreach ($countDate as $key => $date){
        $logData[] = [
            'last_report_date' => $date,
            'is_count' => 1,
            'created_at' => $now,
            'updated_at' => $now
        ];
    }
    // 批量入库日志表
    $keys = $values = '';
    foreach ($logData as $key => $value){
        $keys = '(' . implode(',', array_keys($value)) . ')';
        $values .= "('" . implode("','", array_values($value)) . "')" . ($key + 1 == $count ? '' : ',');
    }
    $sql = "INSERT INTO " . DBPREFIX . "fire_report_log {$keys} VALUES {$values} ON DUPLICATE KEY UPDATE `is_count` = 1";
    //echo $sql;die;
    if(!$inserted = mysqli_query($dbMasterLink, $sql)){
        $dbMasterLink->rollback();
        exit("【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计报表生成失败，原因：入库日志表失败【{$inserted}】\n");
    }
    $dbMasterLink->commit();
    exit("【" . date('Y-m-d H:i:s') . "】thunFire雷火电竞统计报表生成成功\n");
}
