<?php
session_start();
header ("Expires: Mon, 26 Jul 1997 05:00:00 GMT");
header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
header("Cache-Control: no-cache, must-revalidate");
header("Pragma: no-cache");
header("Content-type: text/html; charset=utf-8");
include "../include/address.mem.php";
require ("../include/config.inc.php");

$uid=$_REQUEST['uid'];
$langx=$_SESSION['langx'];

require ("../include/traditional.$langx.inc.php");

?>
<html><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>消息中心</title>
    <link href="../../../style/message/ui-dialog.css?v=<?php echo AUTOVER; ?>" type="text/css" rel="stylesheet">
    <link href="../../../style/message/message.css?v=<?php echo AUTOVER; ?>" type="text/css" rel="stylesheet">
</head>
<body>
<div class="account-content pg-promocode">
    <div class="a-t-b"><span class="a-t-t">消息中心</span></div>
    <div class="mcj_a">
        <div class="mcj_b">
            <ul class="mcj_c m-module">
                <li id="tab0" class="active" data-type="get" data-user-id="48156">
                    <div class="logo-msgbox-r"></div>
                    <div class="mcj_d">收件箱</div>
                    <div id="receive-newcounts">0</div>
                </li>
                <li id="tab1" class="" data-type="getS" data-user-id="48156">
                    <div class="logo-msgbox-s"></div>
                    <div class="mcj_d">发件箱</div>
                </li>
                <li id="tab2" class="">
                    <div class="logo-msgbox-n"></div>
                    <div class="mcj_d">发送新消息</div>
                </li>
            </ul>
        </div>
        <div class="mcj_e">
            <!-- 收件箱 -->
            <div class="mcj_f m-item active" id="inbox">
                <!-- 信息列表 -->
                <ul class="mcj_g">
                    <li class="">
                        <div class="clearfix"><div class="mcj_h"><input type="checkbox" name="Id" value="197"></div><div class="mcj_i"></div><div class="mcj_j"><a href="#" data-title="紧急通知" data-content="紧急通知！我司公司入款招商银行【许嘉伟】账户已停用，最新银行卡号请及时联系/微信/QQ客服获取，如款项误入到停用账户本公司将不予承担责任，敬请注意！" data-time="2018-01-02 11:07:52" data-id="197">紧急通知</a></div><div class="mcj_k">2018-01-02 11:07:52</div></div>
                    </li>
                    <li class="">
                        <div class="clearfix"><div class="mcj_h"><input type="checkbox" name="Id" value="196"></div><div class="mcj_i"></div><div class="mcj_j"><a href="#" data-title="紧急通知" data-content="紧急通知！我司公司入款招商银行【许嘉伟】账户已停用，最新银行卡号请及时联系/微信/QQ客服获取，如款项误入到停用账户本公司将不予承担责任，敬请注意！" data-time="2018-01-02 11:07:48" data-id="196">紧急通知</a></div><div class="mcj_k">2018-01-02 11:07:48</div></div>
                    </li>
                    <li class="">
                        <div class="clearfix"><div class="mcj_h"><input type="checkbox" name="Id" value="118"></div><div class="mcj_i"></div><div class="mcj_j"><a href="#" data-title="银行维护" data-content="建设银行将于12月1日23:00-12月2日04:00进行系统维护农业银行由于系统升级，我行个人掌上银行、网上银行于2017年12月1日22:00-24:00暂时停止服务，期间如要取款请更换银行哦。 " data-time="2017-12-02 00:00:46" data-id="118">银行维护</a></div><div class="mcj_k">2017-12-02 00:00:46</div></div>
                    </li>
                    <li class="">
                        <div class="clearfix"><div class="mcj_h"><input type="checkbox" name="Id" value="117"></div><div class="mcj_i"></div><div class="mcj_j"><a href="#" data-title="公司入款账户停用通知！" data-content="尊敬的玩家，我司公司入款招商银行【周碧莹】入款账户已停用，最新银行卡号请及时联系在线/微信/QQ客服获取，如款项误入到停用账户本公司将不予承担责任，敬请注意！" data-time="2017-12-01 16:09:14" data-id="117">公司入款账户停用通知！</a></div><div class="mcj_k">2017-12-01 16:09:14</div></div>
                    </li>
                    <li class="">
                        <div class="clearfix"><div class="mcj_h"><input type="checkbox" name="Id" value="104"></div><div class="mcj_i"></div><div class="mcj_j"><a href="#" data-title="公司入款账户停用通知！" data-content="尊敬的玩家，本公司建设&quot;周世武&quot;招商&quot;罗方中&quot;入款账户已停用，最新银行卡号请及时联系在线/微信/QQ客服获取，如款项误入到停用账户本公司将不予承担责任，敬请注意！" data-time="2017-11-24 19:29:21" data-id="104">公司入款账户停用通知！</a></div><div class="mcj_k">2017-11-24 19:29:21</div></div>
                    </li>
                </ul>
                <!-- 分页、信息删除 -->
                <div class="mcj_l" style="">
                    <div class="page page-inbox light-theme simple-pagination" style="width: 60%; display: block;">
                        <ul id="inboxPage">当前第&nbsp;&nbsp;<select id="chgPage"><option value="1" selected="">1</option><option value="2">2</option><option value="3">3</option></select>&nbsp;&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共<em>3</em>页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;共<em>12</em>条记录</ul>
                    </div>

                </div>
            </div>

            <!-- 发件箱 -->
            <div class="mcj_f m-item" id="outbox">
                <!-- 信息列表 -->
                <ul class="mcj_g">
                    <h3 class="message-no">正在读取您的消息哦~</h3>
                </ul>
                <!-- 分页、信息删除 -->
                <div class="mcj_l" style="">
                    <div class="page page-inbox light-theme simple-pagination" style="width: 60%; display: block;">
                        <ul id="outboxPage">

                        </ul>
                    </div>

                </div>

            </div>
            <!-- 发送新消息 -->
            <div class="m-item">
                <form class="mc-content">
                    <input type="hidden" name="action" id="action" value="create">
                    <input type="hidden" name="addpople" id="addpople" value="48156">
                    <div class="mcj_o">
                        <div class="mcj_p">发送至:</div>
                        <div class="mcj_q sl-2">
                            <select class="select" name="msgType" id="msgType">
                                <option value="1">财务问题</option>
                                <option value="2">技术问题</option>
                                <option value="3">业务咨询</option>
                                <option value="4">意见建议</option>
                                <option value="5">其他问题</option>
                            </select>
                        </div>
                        <div class="mc-qtips">存款未到账者请留下转账姓名，转账时间和转账金额，我们会尽快为您办理。</div>
                    </div>
                    <div class="mcj_title">
                        <div class="k">标题:</div>
                        <div class="v"><input type="text" name="msgTitle" id="msgTitle" autocomplete="off" maxlength="50" placeholder="最长50个字符"></div>
                    </div>
                    <div class="mcj_r">
                        <div class="mcj_u">内容:</div>
                        <div class="mcj_s"><textarea class="mcj_t" name="msgContent" id="msgContent" maxlength="1000" placeholder="请详细描述您要咨询的问题，我们的客服人员会及时的回复您的消息，谢谢！（限1000个中文字符）"></textarea></div>
                    </div>
                    <div class="mcj_v">
                        <div class="mcj_w"></div>
                        <button class="btn-gray-2 mcj_x" id="btn-send">
                            <span>发送</span>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="../../../js/jquery.js" type="text/javascript" charset="utf-8"></script>
<script src="../../../js/plugin/dialog-plus-min.js?v=<?php echo AUTOVER; ?>" type="text/javascript" charset="utf-8"></script>
<script src="../../../js/message.js?v=<?php echo AUTOVER; ?>" type="text/javascript" charset="utf-8"></script>
<script>
var $userId ='<?php echo $uid ?>' ;
</script>

</body></html>