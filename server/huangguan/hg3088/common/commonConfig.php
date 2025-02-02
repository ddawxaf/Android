<?php

//-------------------------------------------------开元棋牌 游戏数据 Start
// 开元棋牌游戏类型
$kyGameType = array(
    '220'=>'炸金花',
    '230'=>'极速炸金花',
    '380'=>'幸运五张',
    '390'=>'射龙门',
    '600'=>'21点',
    '610'=>'斗地主',
    '620'=>'德州扑克',
    '630'=>'十三水',
    '720'=>'二八杠',
    '730'=>'抢庄牌九',
    '740'=>'二人麻将',
    '830'=>'抢庄牛牛',
    '860'=>'三公',
    '870'=>'通比牛牛',
    '880'=>'欢乐红包',
    '890'=>'看牌抢庄牛牛',
    '900'=>'押庄龙虎',
    '910'=>'百家乐',
    '920'=>'森林舞会',
    '930'=>'百人牛牛',
    '510'=>'捕鱼大作战',
    '650'=>'血流成河',// 开元新加
    '1950'=>'万人炸金花', // 开元新加
    '1350'=>'幸运转盘',// 开元新加
    '1940'=>'金鲨银鲨',// 开元新加
    '1960'=>'奔驰宝马',// 开元新加
    '1980'=>'百人骰宝',// 开元新加
    '1810'=>'单挑牛牛',// 开元新加
    '1990'=>'炸金牛',// 开元新加
    '1850'=>'押宝抢庄牛牛',// 开元新加
    '1660'=>'血战到底',// 开元新加
    '1355'=>'搏一搏',// 开元新加
    '1970'=>'五星宏辉',// 开元新加
    '1860'=>'赌场扑克',// 开元新加
    '1370'=>'港式梭哈',// 开元新加
    '1690'=>'血战骰宝',// 开元新加
    '1890'=>'水果机',// 开元新加
    '1610'=>'幸运夺宝',// 开元新加
);

// 游戏房间类型
$kyRoomType = [
    '220' => [
        '2201' => '炸金花体验房',
        '2202' => '炸金花初级房',
        '2203' => '炸金花中级房',
        '2204' => '炸金花高级房'
    ],
    '230' => [
        '2301' => '极速炸金花体验房',
        '2302' => '极速炸金花初级房',
        '2303' => '极速炸金花中级房',
        '2304' => '极速炸金花高级房'
    ],
    '380' => [
        '3801' => '幸运五张体验房',
        '3802' => '幸运五张初级房',
        '3803' => '幸运五张中级房',
        '3804' => '幸运五张高级房'
    ],
    '390' => [
        '3901' => '神龙门经典房',
        '3902' => '射龙门暴击房',
    ],
    '600' => [
        '6001' => '21点体验房',
        '6002' => '21点初级房',
        '6003' => '21点中级房',
        '6004' => '21点高级房'
    ],
    '610' => [
        '6101' => '斗地主体验房',
        '6102' => '斗地主初级房',
        '6103' => '斗地主中级房',
        '6104' => '斗地主高级房'
    ],
    '620' => [
        '3600' => '德州扑克新手房',
        '3601' => '德州扑克初级房',
        '3602' => '德州扑克中级房',
        '3603' => '德州扑克高级房',
        '3700' => '德州扑克前注场财大气粗',
        '3701' => '德州扑克前注场腰缠万贯',
        '3702' => '德州扑克前注场挥金如土',
        '3703' => '德州扑克前注场富贵逼人'
    ],
    '630' => [
        '6301' => '十三水新手房',
        '6302' => '十三水初级房',
        '6303' => '十三水中级房',
        '6304' => '十三水高级房',
        '6305' => '十三水极速场新手房',
        '6306' => '十三水极速场初级房',
        '6307' => '十三水极速场中级房',
        '6308' => '十三水极速场高级房'
    ],
    '720' => [
        '7201' => '二八杠体验房',
        '7202' => '二八杠初级房',
        '7203' => '二八杠中级房',
        '7204' => '二八杠高级房',
        '7205' => '二八杠至尊房',
    ],
    '730' => [
        '7301' => '抢庄牌九体验房',
        '7302' => '抢庄牌九初级房',
        '7303' => '抢庄牌九中级房',
        '7304' => '抢庄牌九高级房',
        '7305' => '抢庄牌九至尊房',
    ],
    '740' => [
        '7400' => '二人麻将体验房',
        '7401' => '抢庄牌九新手房',
        '7402' => '抢庄牌九初级房',
        '7403' => '抢庄牌九中级房',
        '7404' => '抢庄牌九高级房',
    ],
    '830' => [
        '8301' => '抢庄牛牛体验房',
        '8302' => '抢庄牛牛初级房',
        '8303' => '抢庄牛牛中级房',
        '8304' => '抢庄牛牛高级房',
        '8305' => '抢庄牛牛至尊房',
        '8306' => '抢庄牛牛王者房'
    ],
    '860' => [
        '8601' => '三公体验房',
        '8602' => '三公初级房',
        '8603' => '三公中级房',
        '8604' => '三公高级房',
        '8605' => '三公至尊房',
    ],
    '870' => [
        '8701' => '通比牛牛体验房',
        '8702' => '通比牛牛初级房',
        '8703' => '通比牛牛中级房',
        '8704' => '通比牛牛高级房',
        '8705' => '通比牛牛至尊房',
    ],
    '880' => [
        '8801' => '欢乐红包体验房',
        '8802' => '欢乐红包初级房',
        '8803' => '欢乐红包中级房',
        '8804' => '欢乐红包高级房',
    ],
    '890' => [
        '8901' => '看三张抢庄牛牛体验房',
        '8902' => '看三张抢庄牛牛初级房',
        '8903' => '看三张抢庄牛牛中级房',
        '8904' => '看三张抢庄牛牛高级房',
        '8905' => '看三张抢庄牛牛至尊房',
        '8906' => '看三张抢庄牛牛王者房',
    ],
    '900' => [
        '9001' => '龙虎体验房',
        '9002' => '龙虎初级房',
        '9003' => '龙虎中级房',
        '9004' => '龙虎高级房',
    ],
    '910' => [
        '9101' => '百家乐体验房',
        '9102' => '百家乐初级房',
        '9103' => '百家乐中级房',
        '9104' => '百家乐高级房',
    ],
    '920' => [
        '9201' => '森林舞会体验房',
        '9202' => '森林舞会初级房',
        '9203' => '森林舞会中级房',
        '9204' => '森林舞会高级房',
    ],
    '930' => [
        '9301' => '百人牛牛体验房',
        '9302' => '百人牛牛初级房',
        '9303' => '百人牛牛中级房',
        '9304' => '百人牛牛高级房',
    ],
    '1950' => [
        '19501' => '万人炸金花体验房',
        '19502' => '万人炸金花初级房',
        '19503' => '万人炸金花中级房',
        '19504' => '万人炸金花高级房',
    ],
    '650' => [
        '6501' => '血流成河体验房',
        '6502' => '血流成河初级房',
        '6503' => '血流成河中级房',
        '6504' => '血流成河高级房',
    ],
    '1350' => [
        '13501' => '幸运转盘',
        '13502' => '幸运转盘',
        '13503' => '幸运转盘',
    ],
    '1940' => [
        '19401' => '金鲨银鲨体验房',
        '19402' => '金鲨银鲨初级房',
        '19403' => '金鲨银鲨中级房',
        '19404' => '金鲨银鲨高级房',
    ],
    '1960' => [
        '19601' => '奔驰宝马体验房',
        '19602' => '奔驰宝马初级房',
        '19603' => '奔驰宝马中级房',
        '19604' => '奔驰宝马高级房',
    ],
    '1980' => [
        '19801' => '百人骰宝体验房',
        '19802' => '百人骰宝初级房',
        '19803' => '百人骰宝中级房',
        '19804' => '百人骰宝高级房',
    ],
    '1810' => [
        '18101' => '单挑牛牛体验房',
        '18102' => '单挑牛牛初级房',
        '18103' => '单挑牛牛中级房',
        '18104' => '单挑牛牛高级房',
        '18105' => '单挑牛牛至尊房',
        '18106' => '单挑牛牛王者房',
    ],
    '1990' => [
        '19901' => '炸金牛体验房',
        '19902' => '炸金牛初级房',
        '19903' => '炸金牛中级房',
        '19904' => '炸金牛高级房',
        '19905' => '炸金牛至尊房',
        '19906' => '炸金牛王者房',
    ],
    '1850' => [
        '18501' => '押宝抢庄牛牛体验房',
        '18502' => '押宝抢庄牛牛初级房',
        '18503' => '押宝抢庄牛牛中级房',
        '18504' => '押宝抢庄牛牛高级房',
        '18505' => '押宝抢庄牛牛至尊房',
        '18506' => '押宝抢庄牛牛王者房',
    ],
    '510' => [
        '5101' => '人鱼港口',
        '5102' => '海王遗迹',
        '5103' => '伟大航道',
    ],
    '1660' => [
        '16601' => '血战到底体验房',
        '16602' => '血战到底初级房',
        '16603' => '血战到底中级房',
        '16604' => '血战到底高级房',
    ],
    '1355' => [
        '1355' => '搏一搏',
    ],
    '1970' => [
        '19701' => '五星宏辉体验房',
        '19702' => '五星宏辉初级房',
        '19703' => '五星宏辉中级房',
        '19704' => '五星宏辉高级房',
    ],
    '1860' => [
        '18601' => '赌场扑克体验房',
        '18602' => '赌场扑克初级房',
        '18603' => '赌场扑克中级房',
        '18604' => '赌场扑克高级房',
    ],
    '1370' => [
        '13701' => '港式梭哈体验房',
        '13702' => '港式梭哈初级房',
        '13703' => '港式梭哈中级房',
        '13704' => '港式梭哈高级房',
    ],
    '1690' => [
        '16901' => '血战骰宝体验房',
        '16902' => '血战骰宝初级房',
        '16903' => '血战骰宝中级房',
        '16904' => '血战骰宝高级房',
    ],
    '1890' => [
        '18901' => '水果机体验房',
        '18902' => '水果机初级房',
        '18903' => '水果机中级房',
        '18904' => '水果机高级房',
    ],
    '1610' => [
        '16101' => '幸运夺宝白银宝箱',
        '16102' => '幸运夺宝黄金宝箱',
        '16103' => '幸运夺宝铂金宝箱',
        '16104' => '幸运夺宝钻石宝箱',
    ],
];

// 牌面
$kyPoker = [
    'fang', 'cao', 'hong', 'tao', 'joker'
];
//-------------------------------------------------开元棋牌 游戏数据 End

//-------------------------------------------------皇冠棋牌 游戏数据 Start
// 开元棋牌(非凡)游戏类型
$ffGameType = array(
    '3012'=>'斗地主',
    '3013'=>'百人牛牛',
    '3015'=>'抢庄牛牛',
    '3016'=>'龙虎斗',
    '3014'=>'百人诈金花',
    '3017'=>'二八杠',
    '3018'=>'德州扑克',
    '3019'=>'通比牛牛',
    '3020'=>'炸金花',
    '3021'=>'跑得快',
    '3022'=>'三公',
    '3023'=>'百家乐'
);

//-------------------------------------------------皇冠棋牌 游戏数据 End

//-------------------------------------------------VG棋牌 游戏数据 Start
// VG棋牌游戏类型
$vgGameType = array(
    '1'=>'斗地主',
    '2'=>'二人麻将',
    '3'=>'抢庄牛牛',
    '4'=>'百人牛牛',
    '5'=>'龙王捕鱼',
    '6'=>'多财多福',
    '7'=>'竞咪楚汉德州',
    '8'=>'推筒子',
    '9'=>'加倍斗地主',
    '10'=>'保险楚汉德州',
    '11'=>'血战麻将',
    '12'=>'炸金花',
    '13'=>'必下德州',
    '14'=>'百人三公',
    '15'=>'十三水',
    '17'=>'3D捕鱼',
    '19'=>'开心摇摇乐',
    '20'=>'通比牛牛',
    '22'=>'百家乐',
    '23'=>'二八杠',
    '24'=>'广东推倒胡',
    '25'=>'二十一点',
    '26'=>'广东鸡平胡',
    '33'=>'经典抢庄牛牛',
    '39'=>'跑得快',
    '42'=>'血流成河',
    '44'=>'龙湖斗',
    '45'=>'牛牛大吃小',
    '47'=>'开心翻翻乐',
    '49'=>'新版斗地主',
    '52'=>'看四张抢庄牛牛',
    '994'=>'搏一搏',
    '995'=>'幸运转盘',
    '998'=>'竞咪楚汉福袋',
    '996'=>'开心翻翻乐jackpot',
    '999'=>'JACKPOT',
    '1000'=>'游戏大厅',
);

$vgRoomType = [
    '1' => '入门馆',
    '2' => '初级馆',
    '3' => '中级馆',
    '4' => '高级馆',
    '5' => '顶级馆',
    '6' => '至尊馆',
    '101' => '入门馆',
    '201' => '初级馆',
    '301' => '中级馆',
    '401' => '高级馆',
    '501' => '顶级馆',
    '601' => '至尊馆',
];
//-------------------------------------------------VG棋牌 游戏数据 End


// 乐游棋牌游戏类型
// 开元棋牌游戏类型
$lyGameType = array(
    '220'=>'炸金花',
    '230'=>'极速炸金花',
    '380'=>'幸运五张', // 乐游棋牌
    '390'=>'射龙门', // 乐游棋牌
    '600'=>'21点',
    '610'=>'斗地主',
    '620'=>'德州扑克',
    '630'=>'十三水',
    '720'=>'二八杠',
    '730'=>'抢庄牌九',
    '740'=>'二人麻将',
    '830'=>'抢庄牛牛',
    '860'=>'三公',
    '870'=>'通比牛牛',
    '880'=>'欢乐红包', // 乐游棋牌
    '890'=>'看牌抢庄牛牛', // 乐游棋牌
    '900'=>'龙虎斗',
    '910'=>'百家乐',
    '920'=>'森林舞会',
    '930'=>'百人牛牛',
    '950'=>'红黑大战',// 乐游棋牌
    '510'=>'捕鱼大作战',// 乐游棋牌
    '8120'=>'血战到底',// 乐游棋牌
    '8150'=>'看四张抢庄牛牛',// 乐游棋牌
    '8180'=>'宝石消消乐',// 乐游棋牌
    '8160'=>'癞子牛牛',// 乐游棋牌新加
    '8210'=>'搏一搏',// 乐游棋牌新加
    '8130'=>'跑得快',// 乐游棋牌新加
    '8190'=>'万人推筒子',// 乐游棋牌新加
    '8200'=>'抢庄选三张',// 乐游棋牌新加
    '8220'=>'抢红包',// 乐游棋牌新加
    '8230'=>'百万红包',// 乐游棋牌新加
    '940'=>'金鲨银鲨',// 乐游棋牌新加
    '8260'=>'头号玩家',// 乐游棋牌新加
    '8270'=>'欢乐炸金花',// 乐游棋牌新加
    '8280'=>'好友包房',// 乐游棋牌新加
    '8281'=>'好友包房抢庄牛牛',// 乐游棋牌新加
    '8250'=>'豪车漂移',// 乐游棋牌新加
    '8110'=>'血流成河',// 乐游棋牌新加
    '8310'=>'财神到',// 乐游棋牌新加
    '8140'=>'水果机',// 乐游棋牌新加
);

// 游戏房间类型
$lyRoomType = [
    '220' => [
        '2201' => '炸金花新手房',
        '2202' => '炸金花初级房',
        '2203' => '炸金花中级房',
        '2204' => '炸金花高级房'
    ],
    '230' => [
        '2301' => '极速炸金花新手房',
        '2302' => '极速炸金花初级房',
        '2303' => '极速炸金花中级房',
        '2304' => '极速炸金花高级房'
    ],
    '380' => [
        '3801' => '幸运五张新手房',
        '3802' => '幸运五张初级房',
        '3803' => '幸运五张中级房',
        '3804' => '幸运五张高级房'
    ],
    '390' => [
        '3901' => '神龙门经典房',
        '3902' => '射龙门暴击房',
    ],
    '600' => [
        '6001' => '21点新手房',
        '6002' => '21点初级房',
        '6003' => '21点中级房',
        '6004' => '21点高级房'
    ],
    '610' => [
        '6101' => '斗地主新手房',
        '6102' => '斗地主初级房',
        '6103' => '斗地主中级房',
        '6104' => '斗地主高级房'
    ],
    '620' => [
        '3600' => '德州扑克新手房',
        '3601' => '德州扑克初级房',
        '3602' => '德州扑克中级房',
        '3603' => '德州扑克高级房',
        '3700' => '德州扑克前注场财大气粗',
        '3701' => '德州扑克前注场腰缠万贯',
        '3702' => '德州扑克前注场挥金如土',
        '3703' => '德州扑克前注场富贵逼人'
    ],
    '630' => [
        '6301' => '十三水常规场新手房',
        '6302' => '十三水常规场初级房',
        '6303' => '十三水常规场中级房',
        '6304' => '十三水常规场高级房',
        '6305' => '十三水极速场新手房',
        '6306' => '十三水极速场初级房',
        '6307' => '十三水极速场中级房',
        '6308' => '十三水极速场高级房'
    ],
    '720' => [
        '7201' => '二八杠新手房',
        '7202' => '二八杠初级房',
        '7203' => '二八杠中级房',
        '7204' => '二八杠高级房',
        '7205' => '二八杠至尊房',
    ],
    '730' => [
        '7301' => '抢庄牌九新手房',
        '7302' => '抢庄牌九初级房',
        '7303' => '抢庄牌九中级房',
        '7304' => '抢庄牌九高级房',
        '7305' => '抢庄牌九至尊房',
    ],
    '740' => [
        '7400' => '二人麻将体验房',
        '7401' => '抢庄牌九新手房',
        '7402' => '抢庄牌九初级房',
        '7403' => '抢庄牌九中级房',
        '7404' => '抢庄牌九高级房',
    ],
    '830' => [
        '8301' => '抢庄牛牛新手房',
        '8302' => '抢庄牛牛初级房',
        '8303' => '抢庄牛牛中级房',
        '8304' => '抢庄牛牛高级房',
        '8305' => '抢庄牛牛至尊房',
        '8306' => '抢庄牛牛王者房',
        '8307' => '抢庄牛牛咪牌场新手房',
        '8308' => '抢庄牛牛咪牌场初级房',
        '8309' => '抢庄牛牛咪牌场中级房',
        '8310' => '抢庄牛牛咪牌场高级房',
        '8311' => '抢庄牛牛咪牌场至尊房',
        '8312' => '抢庄牛牛咪牌场王者房',
    ],
    '860' => [
        '8601' => '三公新手房',
        '8602' => '三公初级房',
        '8603' => '三公中级房',
        '8604' => '三公高级房',
        '8605' => '三公至尊房',
    ],
    '870' => [
        '8701' => '通比牛牛新手房',
        '8702' => '通比牛牛初级房',
        '8703' => '通比牛牛中级房',
        '8704' => '通比牛牛高级房',
        '8705' => '通比牛牛至尊房',
        '8706' => '通比牛牛王者房',
    ],
    '880' => [
        '8801' => '欢乐红包新手房',
        '8802' => '欢乐红包初级房',
        '8803' => '欢乐红包中级房',
        '8804' => '欢乐红包高级房',
    ],
    '890' => [
        '8901' => '看三张抢庄牛牛新手房',
        '8902' => '看三张抢庄牛牛初级房',
        '8903' => '看三张抢庄牛牛中级房',
        '8904' => '看三张抢庄牛牛高级房',
        '8905' => '看三张抢庄牛牛至尊房',
        '8906' => '看三张抢庄牛牛王者房',
    ],
    '900' => [
        '9001' => '龙虎新手房',
        '9002' => '龙虎初级房',
        '9003' => '龙虎中级房',
        '9004' => '龙虎高级房',
    ],
    '910' => [
        '9101' => '百家乐新手房',
        '9102' => '百家乐初级房',
        '9103' => '百家乐中级房',
        '9104' => '百家乐高级房',
    ],
    '920' => [
        '9201' => '森林舞会新手房',
        '9202' => '森林舞会初级房',
        '9203' => '森林舞会中级房',
        '9204' => '森林舞会高级房',
    ],
    '930' => [
        '9301' => '百人牛牛新手房',
        '9302' => '百人牛牛初级房',
        '9303' => '百人牛牛中级房',
        '9304' => '百人牛牛高级房',
    ],
    '950' => [
        '9501' => '红黑大战新手房',
        '9502' => '红黑大战初级房',
        '9503' => '红黑大战中级房',
        '9504' => '红黑大战高级房',
    ],
    '510' => [
        '5101' => '捕鱼大作战渔村港口',
        '5102' => '捕鱼大作战傲来渔场',
        '5103' => '捕鱼大作战东海龙宫',
    ],
    '8120' => [
        '81201' => '血战到底新手房',
        '81202' => '血战到底初级房',
        '81203' => '血战到底中级房',
        '81204' => '血战到底高级房',
    ],
    '8150' => [
        '81501' => '看四张抢庄牛牛新手房',
        '81502' => '看四张抢庄牛牛初级房',
        '81503' => '看四张抢庄牛牛中级房',
        '81504' => '看四张抢庄牛牛高级房',
        '81505' => '看四张抢庄牛牛至尊房',
        '81506' => '看四张抢庄牛牛王者房',
    ],
    '8180' => [
        '81801' => '宝石消消乐',
    ],
    '8160' => [
        '81601' => '癞子牛牛新手房',
        '81602' => '癞子牛牛初级房',
        '81603' => '癞子牛牛中级房',
        '81604' => '癞子牛牛高级房',
        '81605' => '癞子牛牛至尊房',
        '81606' => '癞子牛牛王者房',
    ],
    '8210' => [
        '82101' => '搏一搏',
    ],
    '8130' => [
        '81301' => '跑得快新手房',
        '81302' => '跑得快初级房',
        '81303' => '跑得快中级房',
        '81304' => '跑得快高级房',
        '81305' => '跑得快至尊房',
        '81306' => '跑得快王者房',
    ],
    '8190' => [
        '81901' => '万人推筒子新手房',
        '81902' => '万人推筒子初级房',
        '81903' => '万人推筒子中级房',
        '81904' => '万人推筒子高级房',
    ],
    '8200' => [
        '82001' => '抢庄选三张，三公场新手房',
        '82002' => '抢庄选三张，三公场初级房',
        '82003' => '抢庄选三张，三公场中级房',
        '82004' => '抢庄选三张，三公场高级房',
        '82005' => '抢庄选三张，三公场至尊房',
        '82006' => '抢庄选三张，三公场王者房',
        '82007' => '抢庄选三张，金花场新手房',
        '82008' => '抢庄选三张，金花场初级房',
        '82009' => '抢庄选三张，金花场中级房',
        '82010' => '抢庄选三张，金花场高级房',
        '82011' => '抢庄选三张，金花场至尊房',
        '82012' => '抢庄选三张，金花场王者房',
    ],
    '8220' => [
        '82201' => '抢红包 20 元房',
        '82202' => '抢红包 100 元房',
        '82203' => '抢红包 500 元房',
        '82204' => '抢红包 3000 元房',
        '82205' => '抢红包 15000 元房',
        '82206' => '抢红包 60000 元房',
    ],
    '8230' => [
        '82301' => '百万红包',
    ],
    '940' => [
        '9401' => '金鲨银鲨新手房',
        '9402' => '金鲨银鲨初级房',
        '9403' => '金鲨银鲨中级房',
        '9404' => '金鲨银鲨高级房',
    ],
    '8260' => [
        '82601' => '头号玩家新手房',
        '82602' => '头号玩家初级房',
        '82603' => '头号玩家中级房',
        '82604' => '头号玩家高级房',
        '82605' => '头号玩家至尊房',
        '82606' => '头号玩家王者房',
    ],
    '8270' => [
        '82701' => '欢乐炸金新手房',
        '82702' => '欢乐炸金初级房',
        '82703' => '欢乐炸金中级房',
        '82704' => '欢乐炸金高级房',
        '82705' => '欢乐炸金至尊房',
        '82706' => '欢乐炸金王者房',
    ],
    '8280' => [
        '82801' => '好友包房抢庄牛牛',
    ],
    '8281' => [
        '82811' => '好友包房抢庄牛牛',
    ],
    '8250' => [
        '82501' => '豪车漂移新手房',
        '82502' => '豪车漂移初级房',
        '82503' => '豪车漂移中级房',
        '82504' => '豪车漂移高级房',
    ],
    '8110' => [
        '81101' => '血流成河新手房',
        '81102' => '血流成河初级房',
        '81103' => '血流成河中级房',
        '81104' => '血流成河高级房',
        '81105' => '血流成河至尊房',
        '81106' => '血流成河王者房',
    ],
    '8310' => [
        '83101' => '财神到新手房',
        '83102' => '财神到初级房',
        '83103' => '财神到中级房',
        '83104' => '财神到高级房',
        '83105' => '财神到至尊房',
        '83106' => '财神到王者房',
    ],
    '8140' => [
        '81401' => '水果机新手房',
        '81402' => '水果机初级房',
        '81403' => '水果机中级房',
        '81404' => '水果机高级房',
    ],
];

//-------------------------------------------------乐游棋牌 游戏数据 End

?>