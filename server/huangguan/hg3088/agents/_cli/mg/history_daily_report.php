<?php
/**
 * MG电子日结报表（每次执行的时间区间必须为1个天）
 *
 * Date: 2019/7/1
 */

define("CONFIG_DIR", dirname(dirname(dirname(__FILE__))));
require_once CONFIG_DIR . "/app/agents/include/config.inc.php";
require_once CONFIG_DIR."/app/agents/include/redis.php";

if (php_sapi_name() == "cli") {
    $today = date('Y-m-d');
    $yesterday = date('Y-m-d', strtotime('-1 day'));
    $startTime = isset($argv[1]) && $argv[1] ? $argv[1] : $yesterday;
    $endTime = isset($argv[2]) && $argv[2] ? $argv[2] : $today;
    $isRepeat = isset($argv[3]) && $argv[3] ? $argv[3] : 0;

    if($startTime > $yesterday)
        exit("【" . date('Y-m-d H:i:s') . "】开始时间>昨日【{$startTime} > {$yesterday}】\n");

    countDaiMGWinLoss();
}

function countDaiMGWinLoss()
{
    global $dbMasterLink, $dbLink, $startTime, $endTime, $isRepeat;
    // 1. 判断数据是否已统计
    if($isRepeat){
        echo "【" . date('Y-m-d H:i:s') . "】MG电子重新统计数据\n";
    }else{
//        $sql = "SELECT `id`, `last_report_date`, `is_count` FROM " . DBPREFIX . "mg_report_log
//            WHERE `is_count` = 1
//            ORDER BY `last_report_date` DESC LIMIT 1";
//        $result  = mysqli_query($dbLink, $sql);
//        $row = mysqli_fetch_assoc($result);
//        $iCount = mysqli_num_rows($result);
//        if($iCount){
//            $lastCountDate = $row['last_report_date'];
//            if(date('Y-m-d', strtotime("{$lastCountDate} + 1 day")) != $startTime){
//                exit("【" . date('Y-m-d H:i:s') . "】MG电子统计日期错误【{$startTime}~{$endTime}】，最近最后一次统计日期【{$lastCountDate}】\n");
//            }
//        }
    }
    echo "【" . date('Y-m-d H:i:s') . "】MG电子统计日期美东时间：【{$startTime}~{$endTime}】\n";

    // 2.统计
    // 2.1.删除重复历史统计数据&日志报表数据
    mysqli_query($dbMasterLink, "DELETE FROM " . DBPREFIX . "mg_history_report WHERE `count_date` >= '{$startTime}' AND `count_date` < '{$endTime}'");
    mysqli_query($dbMasterLink, "DELETE FROM " . DBPREFIX . "mg_report_log WHERE `last_report_date` >= '{$startTime}' AND `last_report_date` < '{$endTime}'");

    // 2.2.事务处理
    $dbMasterLink->autocommit(false);
    $sql = "SELECT `userid`, `username`, `is_test`, `agents`, SUM(1) AS `total_times`, SUM(`amount`) AS `total_cellscore`, SUM(`amount`) AS `total_bet`, DATE_FORMAT(transaction_time,'%Y-%m-%d') AS `count_date` 
            FROM " . DBPREFIX . "mg_projects 
            WHERE `transaction_time` >= '{$startTime}' AND `transaction_time` < '{$endTime}' AND category='WAGER'
            GROUP BY `userid`, `count_date` ORDER BY `count_date` ASC";
    $result = mysqli_query($dbLink, $sql);
    $count = mysqli_num_rows($result);
    $now = date('Y-m-d H:i:s');
    $countDate = [];
    if($count == 0){
        $countDate[] = $startTime;
        echo "【" . date('Y-m-d H:i:s') . "】MG电子暂无注单记录\n";
    }else{
        $countData = $countDate = [];
        // 投注总额、盈利总额 分2次统计
        while ($row = mysqli_fetch_assoc($result)){
            $data[$row['userid'].'_'.$row['count_date']]['userid'] = $row['userid'];
            $data[$row['userid'].'_'.$row['count_date']]['username'] = $row['username'];
            $data[$row['userid'].'_'.$row['count_date']]['is_test'] = $row['is_test'];
            $data[$row['userid'].'_'.$row['count_date']]['agents'] = $row['agents'];
            $data[$row['userid'].'_'.$row['count_date']]['total_times'] = $row['total_times'];
            $data[$row['userid'].'_'.$row['count_date']]['total_cellscore'] = $row['total_cellscore'];
            $data[$row['userid'].'_'.$row['count_date']]['total_bet'] = $row['total_bet'];
            $data[$row['userid'].'_'.$row['count_date']]['total_profit'] = 0;
            $data[$row['userid'].'_'.$row['count_date']]['count_date'] = $row['count_date'];
            $data[$row['userid'].'_'.$row['count_date']]['created_at'] = $now;

            $countDate[] = $row['count_date'];
        }

        // 总盈利额
        $sql = "SELECT `userid`, `username`, `is_test`, `agents`, SUM(`amount`) AS `total_payout`, DATE_FORMAT(transaction_time,'%Y-%m-%d') AS `count_date` 
            FROM " . DBPREFIX . "mg_projects 
            WHERE `transaction_time` >= '{$startTime}' AND `transaction_time` < '{$endTime}' AND category='PAYOUT'
            GROUP BY `userid`, `count_date` ORDER BY `count_date` ASC";
        $result = mysqli_query($dbLink, $sql);
        $count_payout = mysqli_num_rows($result);
        if($count_payout == 0){
            echo "【" . date('Y-m-d H:i:s') . "】MG电子暂无派彩记录\n";
        }else{
            while ($row = mysqli_fetch_assoc($result)){
                if (!isset($data[$row['userid'].'_'.$row['count_date']])){
                    $data[$row['userid'].'_'.$row['count_date']]['userid'] = $row['userid'];
                    $data[$row['userid'].'_'.$row['count_date']]['username'] = $row['username'];
                    $data[$row['userid'].'_'.$row['count_date']]['is_test'] = $row['is_test'];
                    $data[$row['userid'].'_'.$row['count_date']]['agents'] = $row['agents'];
                    $data[$row['userid'].'_'.$row['count_date']]['total_times'] = 0;
                    $data[$row['userid'].'_'.$row['count_date']]['total_cellscore'] = 0;
                    $data[$row['userid'].'_'.$row['count_date']]['total_bet'] = 0;
                }
                $data[$row['userid'].'_'.$row['count_date']]['total_profit'] = ($data[$row['userid'].'_'.$row['count_date']]['total_bet'] - $row['total_payout']) * -1; // 会员输的金额总数
                $data[$row['userid'].'_'.$row['count_date']]['count_date'] = $row['count_date'];
                $data[$row['userid'].'_'.$row['count_date']]['created_at'] = $now;

            }
        }
        $countData = array_values($data);

        // 批量入库报表
        $keys = $values = '';
        foreach ($countData as $key => $value){
            $keys = '(' . implode(',', array_keys($value)) . ')';
            $values .= "('" . implode("','", array_values($value)) . "'),";
        }
        $values=rtrim($values,',');
        $sql = "INSERT INTO " . DBPREFIX . "mg_history_report {$keys} VALUES {$values}";
        if(!$inserted = mysqli_query($dbMasterLink, $sql)){
//            echo $sql;die;
            $dbMasterLink->rollback();
            exit("【" . date('Y-m-d H:i:s') . "】MG电子统计报表生成失败，原因：入库报表失败【{$inserted}】\n");
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
    $sql = "INSERT INTO " . DBPREFIX . "mg_report_log {$keys} VALUES {$values} ON DUPLICATE KEY UPDATE `is_count` = 1";
//    echo $sql;die;
    if(!$inserted = mysqli_query($dbMasterLink, $sql)){
        $dbMasterLink->rollback();
        exit("【" . date('Y-m-d H:i:s') . "】MG电子统计报表生成失败，原因：入库日志表失败【{$inserted}】\n");
    }
    $dbMasterLink->commit();
    exit("【" . date('Y-m-d H:i:s') . "】MG电子统计报表生成成功\n");
}
