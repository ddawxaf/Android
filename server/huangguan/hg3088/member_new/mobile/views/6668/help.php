<?php
session_start();
$uid = $_SESSION['Oid'];
$tip = isset($_REQUEST['tip'])?$_REQUEST['tip']:'' ; // 用于app 跳转到这个页面

?>

<html class="zh-cn"><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="HandheldFriendly" content="true"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--<link href="../../style/font-awesome.min.css" rel="stylesheet" type="text/css" media="screen"/>-->
    <link href="style/iphone.css?v=<?php echo $_SESSION['AUTOVER_SESSION']; ?>" rel="stylesheet" type="text/css" media="screen"/>

    <title class="web-title"></title>
<style type="text/css">
    .deposit .tab .item, .deposit .tab .expand{ margin-top: 0;border-top:none;}
    /*新手教学*/
     main.newbie{background: #ebebeb;padding: 4% 4% 0 4%;min-height: 100vh;}
    .newbie_box {height:auto;padding:10px;background:#FFF;margin:10px 0;}
    .newbie_box.triggered .newbie_in {max-height:none;padding:0 0 0 10px;margin:10px 16px 20px 4px;visibility:visible;overflow:initial;opacity:1;text-align: left;}
    .newbie_tit {height:32px; text-align: left;}
    .newbie h1 {display: inline-block; font-size: 20px; color: #2A8FBD; position: absolute; letter-spacing: 2px; margin: 2px 0 0 20px; font-weight: 600; }
    .nsb_action {display:block; position:relative; width:35px; height:100%; float:right; color:#a6a6a6; }
    .nsb_action a {display:block; width:100%; height:100%; color:inherit; margin-top: -5px;}
    .nsb_action a i {position:relative; left:50%; top:50%;
        transition:all 0.2s ease-in-out; -webkit-transition:all 0.2s ease-in-out; -ms-transition:all 0.2s ease-in-out;
        transform:translate(-50%,-50%) rotate(90deg); -webkit-transform:translate(-50%,-50%) rotate(90deg); -ms-transform:translate(-50%,-50%) rotate(90deg);
    }
    .nsb_action.triggered a i {transform:translate(-50%,-50%) rotate(-90deg); -webkit-transform:translate(-50%,-50%) rotate(-90deg); -ms-transform:translate(-50%,-50%) rotate(-90deg);}
    .newbie_in {max-height:0px; overflow:hidden;transition:500ms; -webkit-transition:500ms; -ms-transition:500ms;}
    .newbie_in h2 {color: #888; font-size: 14px; margin: 12px 0 4px 0; }
    .newbie_in h2 span{color: #fff; font-size: 13px; font-weight: 600; background: #2A8FBD; margin: 0 6px 0 0; padding: 5px 6px; border-radius: 15px; }
    .newbie_in h2 strong{color: #333; font-size: 15px; font-weight: 600; height: 40px; line-height: 40px; }
    .newbie_in p {color: #888; font-size: 14px; margin: -4px 0 6px 0; }
    .newbie_in .line{margin: 10px auto 0px auto; }
    .line_pro {background-color:rgba(201,150,0,0.5); height:1px; margin:0px auto 6px auto; }
    .word_pro_sl {font-size:12px; line-height:2em; color:#acacac; margin:10px 0px 14px 20px; }
    .word_pro_sl b{color:#00b3e0;}
    .fa-chevron-right:after {margin: 0;}
    .triggered .fa-chevron-right:after {transform: rotate(135deg);-webkit-transform: rotate(135deg);}
    /*新手教学*/
</style>
</head>
<body class="dedede">
<div id="container" >
    <!-- 头部 -->
    <div class="header <?php if($tip){echo 'hide-cont';}?>">

    </div>

    <!-- 中间内容 -->
    <div class="content-center deposit">
        <main class="main newbie">

            <!-- 如何充值 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>如何充值</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>点击下方充值</p>
                    <img src="images/newbie/dps_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>选择一个充值方式，并关注注意事项</p>
                    <img src="images/newbie/dps_02.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 防劫持教学(苹果手机) -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>防劫持教学(苹果手机)</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>打开「设置」应用程序。点击「无线局域网」进入 Wi-Fi 列表，并选择连接上可用的 Wi-Fi 网络。</p>
                    <img src="images/newbie/dns_IOS01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>连接上可用的 Wi-Fi 网络后，然后点击 Wi-Fi 网络名称右方的「显示信息」按钮(字母「i」)。</p>
                    <img src="images/newbie/dns_IOS02.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>在该 Wi-Fi 网络详情部分向下滚动，找到「DNS」选项部分。</p>
                    <img src="images/newbie/dns_IOS03.jpg?v=1" width="100%">
                    <h2><span>04</span><strong>第四步:</strong></h2>
                    <p>接下来在弹出的键盘上输入你需要更改后的 DNS 数值.本例子中我们使用 Google 提供的 8.8.8.8</p>
                    <img src="images/newbie/dns_IOS04.jpg?v=1" width="100%">
                    <h2><span>05</span><strong>第五步:</strong></h2>
                    <p>点击页面左上角的「Wi-Fi」按钮，即可退出 DNS 设置。就这样，DNS 的设置已成功更改，但可能还没有 立即生效。为了使得设备的 DNS 设置更改生效，你可能需要重新启动你的 iOS 设备，否则旧的 DNS 设置可 能还存储在缓存中。</p>
                </div>
            </div>

            <!-- 防劫持教学(安卓手机) -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>防劫持教学(安卓手机)</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>点击设定&amp;设置</p>
                    <img src="images/newbie/dns_Ad01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击Wi-Fi选项</p>
                    <img src="images/newbie/dns_Ad02.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>在wifi列表中，选择已经连接的wifi名称，长按之后在弹出来的提示选择“修改网络配置”</p>
                    <img src="images/newbie/dns_Ad03.jpg?v=1" width="100%">
                    <h2><span>04</span><strong>第四步:</strong></h2>
                    <p>选择“显示高级选项”--将IP设定--改成静止</p>
                    <img src="images/newbie/dns_Ad04.jpg?v=1" width="100%">
                    <h2><span>05</span><strong>第五步:</strong></h2>
                    <p>将DNS1改成8.8.8.8。然后将DNS2改成8.8.4.4保存即可</p>
                    <img src="images/newbie/dns_Ad05.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 支付宝转银行卡 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>支付宝转银行卡</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <img src="images/newbie/AlypTh_01.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 微信转帐银行卡 -->
           <!-- <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>微信转帐银行卡</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>点击下方充值</p>
                    <img src="images/newbie/dps_01.jpg?v=1" width="100%">
                    <img src="images/newbie/WchTh_02.jpg?v=1" width="100%">
                </div>
            </div>-->

            <!-- 银联支付使用教学 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>银联支付使用教学</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>银联钱包APP下载</p>
                    <p>IOS版本：App Store搜索银联钱包<br>Android版本：各大应用市场搜索银联钱包</p>
                    <img src="images/newbie/elpayth_01.png" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>注册并登入</p>
                    <img src="images/newbie/elpayth_02.png" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>绑定银行卡，选择卡管家 → 卡列表 → 添加银行卡 → 添加一张新卡</p>
                    <p>信用卡和储蓄卡皆可添加</p>
                    <img src="images/newbie/elpayth_03.png" width="100%">
                    <h2><span>04</span><strong>第四步:</strong></h2>
                    <p>开通银联支付</p>
                    <p>有些用户虽然已绑卡，但是并未开通银联支付，所以会导致银联二维码支付不成功</p>
                    <img src="images/newbie/elpayth_04.png" width="100%">
                </div>
            </div>

            <!-- 钱包转帐 -->
            <!--<div class="newbie_box">
                <div class="newbie_tit">
                    <h1>钱包转帐</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>选择转帐</p>
                    <img src="images/newbie/tsf_01.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>选择转出转入平台，选择完金额后点确认转帐</p>
                    <img src="images/newbie/tsf_02.jpg?v=1" width="100%">
                </div>
            </div>-->

            <!-- 如何抢红包 -->
           <!-- <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>如何抢红包</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于首页点击抢红包</p>
                    <img src="images/newbie/honb_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>进入抢红包画面后，符合资格即可游戏，下方也有活动说明可以观看</p>
                    <img src="images/newbie/honb_02.jpg?v=1" width="100%">
                </div>
            </div>-->

            <!-- 备用线路与测速 -->
      <!--      <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>备用线路与测速</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于首页选择线路导航</p>
                    <img src="images/newbie/tynav_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>选择一个最快速的线路前往</p>
                    <img src="images/newbie/tynav_02.jpg?v=1" width="100%">
                </div>
            </div>-->

            <!-- APP下载 -->
           <!-- <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>APP下载</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于首页点击"APP下载区"</p>
                    <img src="images/newbie/app_dw_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>进入下载区后，选择想下载的APP</p>
                    <img src="images/newbie/app_dw_02.jpg?v=1" width="100%">
                </div>
            </div>-->

            <!-- 避免使用无痕/私密浏览 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>避免使用无痕/私密浏览</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <p>使用无痕/私密浏览 会导致IOS苹果系统在游戏中跳出错误，所以您必须要关闭私密浏览</p>
                    <br>
                    <p>您可以在 "设置"-&gt; "Safari" -&gt; "私密浏览" 中更改此项设置</p>
                </div>
            </div>

            <!-- 绑定银行卡 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>绑定银行卡</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击银行卡</p>
                    <img src="images/newbie/bdcard_01.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>将资料填写完善後，点击确认绑定</p>
                    <img src="images/newbie/bdcard_02.jpg?v=1" width="100%">
                </div>
            </div>


            <!-- 如何提现 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>如何提现</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击提现</p>
                    <img src="images/newbie/wdrl_01.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>进入后，输入金额与提现密码(您在添运的密码)，完成后按提交</p>
                    <img src="images/newbie/wdrl_02.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 如何查流水 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>如何查流水</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击流水纪录</p>
                    <img src="images/newbie/cls_01.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 修改密码 -->
            <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>修改密码</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击帐户中心</p>
                    <img src="images/newbie/pswd_01.jpg?v=1" width="100%">
                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>点击修改密码，填写好新旧密码后按确认修改</p>
                    <img src="images/newbie/pswd_02.jpg?v=1" width="100%">
                </div>
            </div>

            <!-- 绑定六合幸运码 -->
           <!-- <div class="newbie_box">
                <div class="newbie_tit">
                    <h1>绑定六合幸运码</h1>
                    <div class="nsb_action">
                        <a href="#">
                            <i class="fa fa-chevron-right"></i>
                        </a>
                    </div>
                </div>
                <div class="newbie_in">
                    <div class="line"></div>
                    <h2><span>01</span><strong>第一步:</strong></h2>
                    <p>于下方点击我的</p>
                    <img src="images/newbie/user_01.jpg?v=1" width="100%">
                    <h2><span>02</span><strong>第二步:</strong></h2>
                    <p>点击帐户中心</p>
                    <img src="images/newbie/pswd_01.jpg?v=1" width="100%">

                    <h2><span>03</span><strong>第三步:</strong></h2>
                    <p>点击"六合幸运码"，填入想绑定之数字，或选自动生成号码，选玩号码后点"确认绑定"</p>
                    <img src="images/newbie/lotto.jpg?v=1" width="100%">
                </div>
            </div>-->

        </main>
        <!--<div class="tab">
            <div class="deposit-nav">
                <a href="javascript:;" data-action="1" class="item ">
                    <span>存款注意事项</span>
                </a>
                <div class="panel-body expand_1">
                    <p>1.皇冠现金网城最低存款为$100人民币，最高存款暂不限制。未开通网银的会员，请亲洽您的银行柜台办理。如有任何问题，请联系24小时在线客服，谢谢。</p>
                    <p>2.皇冠现金网的新老会员通过网银、ATM、手机银行转账或现金到柜台存入到公司指定银行账户进行游戏充值，即可享受充值金额1％的返利。单笔最高存款10万以上即可享受2%返利。无上限，次次存次次送。</p>
                </div>
                <a href="javascript:;" data-action="2" class="item">
                    <span >存款方式</span>
                </a>
                <div class="panel-body expand_2">
                    <p>1.线上支付(已开通网上银行客户) <br>网上银行在線支付、0手續費、开户5钟完成（支持以下银行）<br>工商银行.招商银行.建设银行.农业银行.中国银行.交通银行.华夏银行.兴业银行.民生银行. <br>广发银行.深圳银行.浦东银行.中信银行.光大银行.重庆银行.南京银行.勃海银行.东亚银行.</p>
                    <p>2.公司入款(ATM柜员机或银行柜台转账客户)您需要向在线客服索取我公司银行账户，并通过银行柜台或者ATM柜员机转账至我们公司指定的银行账号。为了保障您的资金安全，请您汇款/转账成功后，及时联系在线客服提供您的存款信息，由在线客服为您确认款项。</p>
                </div>
                <a href="javascript:;" data-action="3" class="item">
                    <span >存款提交说明</span>
                </a>
                <div class="panel-body expand_3">
                    <p>1.您需要向在线客服索取我公司银行账户，并通过银行柜台或者ATM柜员机转账至我们公司指定的银行账号。为了保障您的资金安全，请您汇款/转账成功后，及时联系在线客服提供您的存款信息，由在线客服为您确认款项。</p>
                </div>
                <a href="javascript:;" data-action="4" class="item">
                    <span >公司入款流程解说</span>
                </a>
                <div class="panel-body expand_4">
                    <p>利用转帐方式将款项从自己的帐号转到本公司指定的银行帐号。</p>
                    <div class="banner">
                        <div class="banner-title">1.选择使用银行</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 选择要使用银行，建议与转入帐号之银行相同，同行转帐可以立即到帐。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">2.选择欲转入银行帐号</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 本页选择可存入的银行帐号，银行帐号不定期更换请务必每次查看。</li>
                                <li>◆ 建议选择与您使用银行相同之银行帐号加快到帐速度。</li>
                                <li>◆ 核对并记下选择银行帐号进行转帐。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">3.输入欲转入金额</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 输入金额与实际汇款提交金额必须完全一致。</li>
                                <li>◆ 建议存入有尾数金额方便系统辨别缩短到帐速度。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">4.完成转帐</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 本页选择可存入的银行帐号，银行帐号不定期更换请务必每次查看。</li>
                                <li>◆ 建议选择与您使用银行相同之银行帐号加快到帐速度。</li>
                                <li>◆ 核对并记下选择银行帐号进行转帐。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">5.填写并提交申请</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 实际汇款与提交金额必须完全一致。</li>
                                <li>◆ 每笔转帐请提交一次。</li>
                                <li>◆ 使用ATM存款请务必填写所属分行加快到帐。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">6.等候系统加款</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 系统收到款项后进行比对，如金额、时间相符会将款项加到您游戏帐号中。</li>
                                <li>◆ 处理时间通常为5-30分钟。</li>
                                <li>◆ 跨行转帐时间可能超过30分。</li>
                            </ul>
                        </div>
                    </div>
                    <div class="banner">
                        <div class="banner-title">7.三十分钟内是否到帐?</div>
                        <div class="banner-body">
                            <ul>
                                <li>◆ 是：存款成功，请享受游戏乐趣。</li>
                                <li>◆ 建议选择与您使用银行相同之银行帐号加快到帐速度。</li>
                                <li>◆ 否：请联系线上客服人员，客服人员会与您核对您的存款资料，必要时需要您提供截图资料等相关证明。</li>
                            </ul>
                        </div>
                    </div>
                </div>

            </div>

        </div>-->

    </div>

    <!-- 底部footer -->
    <div id="footer" class="<?php if($tip){echo 'hide-cont';}?>">

    </div>
</div>
<script type="text/javascript" src="../../js/zepto.min.js"></script>
<script type="text/javascript" src="../../js/animate.js"></script>
<script type="text/javascript" src="../../js/zepto.animate.alias.js"></script>
 <script type="text/javascript" src="../../js/main.js?v=<?php echo $_SESSION['AUTOVER_SESSION']; ?>"></script>

<script type="text/javascript">
    var uid = '<?php echo $uid?>' ;
    var usermon = getCookieAction('member_money') ; // 获取信息cookie
    setLoginHeaderAction('存取帮助','','',usermon,uid) ;
    setFooterAction(uid) ; // 在 addServerUrl 前调用
   // addServerUrl() ;

   // function showDetails() {
   //     $('.deposit-nav').on('click','a', function() {
   //         var val = $(this).data('action') ;
   //         if($(this).hasClass('expand')) {
   //             $(this).removeClass('expand').addClass('item');
   //             $('.expand_'+val).hide() ;
   //         } else {
   //             $(this).addClass('expand').removeClass('item');
   //             $('.expand_'+val).show() ;
   //         }
   //     });
   //
   // }
   //  showDetails() ;

    function unfoldPost() {
        var actionButton = $(".newbie_tit");
        actionButton.on("click", function(e) {
            e.preventDefault();
            $(this).closest(".newbie_box").toggleClass("triggered");
            $(this).closest(".nsb_action").toggleClass("triggered");
            $(".no_count").fadeOut(350);//關閉優惠也關閉不计算名单
        });
    }
    unfoldPost();


</script>

</body>
</html>