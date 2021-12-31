
jQuery.browser = {};
$(function(){
    /*右侧点击数字加到input框*/
    var sum = 0;
    $(".betAmount li").each(function () {
        //$(this).on("click",function(){
        $(this).click(function(){
            sum += $(this).val();
            $("#Money").val(sum);
        });
    });

    /*清空input框*/
    $(".ord_delBTN").click(function(){
        sum = 0;
        $("#Money").val("").focus();
    });

    /*密码小键盘*/
    var fn = 1;
    $(".withdrawpassword2").focus(function(){
        $(".pwd_num").show();
    });

    $('.pwd_num .num').on('click',function(){
        var n = $(this).html();
        if(fn<6){
            $("#address"+fn).val(n);
            fn+=1;
            $("#address"+fn).focus();

            $('.pwd_num #delete,#btnReset').on('click',function(){
                keyClear();
            })

        }else if(fn==6){
            $("#address"+fn).val(n);
            $(".pwd_num").hide();

            $('.pwd_num #delete,#btnReset').on('click',function(){
                keyClear();
            })
        }

    })
    function keyClear(){
        fn = 1;
        $("#address"+fn).val();
        $(".pwd_num").hide();
    }

    /*关闭小键盘*/
    $('.pwd_num .close').on('click',function(){
        $(".pwd_num").hide();
    }) ;

    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }

}) ;

//取款密码输入完自动跳到下一个
onload = function(){
    var txts = withdrawal_passwd.getElementsByTagName("input");
    var delt = document.getElementById('delete'); // 键盘清空按钮
    var btnR = document.getElementById('btnReset'); // 重新填写按钮
    for(var i = 0; i<txts.length;i++){
        var t = txts[i];
        t.index = i;
        //t.setAttribute("readonly", true);
        //txts[0].removeAttribute("readonly");
        t.onkeyup=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];
            //console.log(this)
            this.value=this.value.replace(/^(.).*$/,'$1');
            if(e && e.keyCode==8){
                txtClear();
            }else{
                this.setAttribute("readonly", true);
                var next = this.index + 1;
                if(next > txts.length - 1) {
                    $(".pwd_num").hide();
                    return;
                }
                //txts[next].removeAttribute("readonly");
                txts[next].focus();
            }
        }
        delt.onclick=function(){
            txtClear();
        }
        btnR.onclick=function(){
            document.getElementById('Money').value = '' ; // 清空金额
            txtClear();
        }
        function txtClear(){ // 清空提款密码输入框
            t.value="";
            $("#withdrawal_passwd input").val("");
            $(".pwd_num").hide();
            $("#address1").focus();
        }
    }
    //txts[0].removeAttribute("readonly");
}

function VerifyData() {
    var $mominput = document.getElementById('Money') ;
    var memmoney = $('#hgmoney').data('val') ; // 用户当前余额
    if (document.getElementById('Bank_Name').value == "") {
        alert("请输入开户银行！")
        document.getElementById('Bank_Name').focus();
        return false;
    }
    if (document.getElementById('Bank_Account').value == "") {
        alert("请输入银行账号！");
        document.getElementById('Bank_Account').focus();
        return false;
    }
    if ($mominput.value == "") {
        alert("请输入提款金额！");
        $mominput.focus();
        return false;
    }
    if (document.getElementById('withdrawal_passwd').value == "") {
        alert("请输入取款密码！");
        document.getElementById('withdrawal_passwd').focus();
        return false;
    }

    if ($mominput.value !="") {
        if($mominput.value > memmoney ){
            alert("提款金额不能大于帐号金额！")
            //document.main.Money.focus();
            $mominput.focus();
            return false;
        }
    }
    if ($mominput.value !="") {
        if($mominput.value <100 ){
            alert("提款金额不能小于100元！")
            $mominput.focus();
            return false;
        }
    }
}
function VerifyBank() {

    if (document.getElementById('chg_bank').value == "") {
        alert("请选择开户银行！");
        return false;
    }
    if (document.getElementById('chg_bank_account').value == "") {
        alert("请输入银行账号！");
        return false;
    }
    if (document.getElementById('chg_bank_address').value == "") {
        alert("请输入银行地址！");
        return false;
    }

    return true;
}
function Verifywithdrawpassword() {
    if (document.getElementById('address1').value !== "" &&document.getElementById('address2').value !== ""&&
        document.getElementById('address3').value !== ""&&document.getElementById('address4').value !== ""&&
        document.getElementById('address5').value == ""&&document.getElementById('address6').value == "") {
        alert("尊敬的客户，为了您的资金安全，建议您前往投注区页面顶部更改密码的地方，将取款密码修改为六位");
    }

    return true;
}

function agjb(uid,ctr){
    $.jBox('get:tran.php?uid='+uid+'&ctr='+ctr, {
        title: "额度转换",
        width: 400,height: "auto",border: 0,showIcon: false,buttons: {}
    });
}

// 更换银行 uid 用户id
function tranSetbank(uid) {
    var loadfalg = false ; // 防止重复提交
    var html = '<div class="msg-div">' +
        '<div class="fm" style="color:#c52000;">为了您的银行帐号安全，我们不建议您经常更换！</div>' +
        '<div class="fm"><label><span style="color:#c52000; vertical-align:middle;";>*</span>开户银行：</label><select id="chg_bank" style="width:210px" name="chg_bank"><span style="color:#c52000">*</span>' +
        '<option value="1" selected="selected" >***选择银行***</option>' +
        '<option value="中国工商银行">中国工商银行</option>' +
        '<option value="中国建设银行">中国建设银行</option>' +
        '<option value="中国农业银行">中国农业银行</option>' +
        '<option value="中国银行">中国银行</option>' +
        '<option value="交通银行">交通银行</option>' +
        '<option value="招商银行">招商银行</option>' +
        '<option value="中国民生银行">中国民生银行</option>' +
        '<option value="邮政储蓄银行">邮政储蓄银行</option>' +
        '<option value="中信银行">中信银行</option>' +
        '<option value="光大银行">光大银行</option>' +
        '<option value="浦发银行">浦发银行</option>' +
        '<option value="兴业银行">兴业银行</option>' +
        '<option value="华夏银行">华夏银行</option>' +
        '<option value="广发银行">广发银行</option>' +
        '<option value="平安银行">平安银行</option>' +
        '<option value="上海银行">上海银行</option>' +
        '<option value="江苏银行">江苏银行</option>' +
        '<option value="安徽省农村信用社联合社">安徽省农村信用社联合社</option>' +
        '<option value="鞍山市商业银行">鞍山市商业银行</option>' +
        '<option value="包商银行股份有限公司">包商银行股份有限公司</option>' +
        '<option value="北京农村商业银行">北京农村商业银行</option>' +
        '<option value="北京顺义银座村镇银行">北京顺义银座村镇银行</option>' +
        '<option value="北京银行">北京银行</option>' +
        '<option value="渤海银行">渤海银行</option>' +
        '<option value="沧州银行">沧州银行</option>' +
        '<option value="长安银行">长安银行</option>' +
        '<option value="长沙银行">长沙银行</option>' +
        '<option value="常熟农村商业银行">常熟农村商业银行</option>' +
        '<option value="成都银行">成都银行</option>' +
        '<option value="承德银行">承德银行</option>' +
        '<option value="重庆农村商业银行">重庆农村商业银行</option>' +
        '<option value="重庆黔江银座村镇银行">重庆黔江银座村镇银行</option>' +
        '<option value="重庆银行股份有限公司">重庆银行股份有限公司</option>' +
        '<option value="重庆渝北银座村镇银行">重庆渝北银座村镇银行</option>' +
        '<option value="大连银行">大连银行</option>' +
        '<option value="德阳银行">德阳银行</option>' +
        '<option value="德州银行">德州银行</option>' +
        '<option value="东莞农村商业银行">东莞农村商业银行</option>' +
        '<option value="东莞银行">东莞银行</option>' +
        '<option value="东亚银行（中国）有限公司">东亚银行（中国）有限公司</option>' +
        '<option value="东营莱商村镇银行股份有限公司">东营莱商村镇银行股份有限公司</option>' +
        '<option value="东营银行">东营银行</option>' +
        '<option value="鄂尔多斯银行">鄂尔多斯银行</option>' +
        '<option value="福建海峡银行">福建海峡银行</option>' +
        '<option value="福建省农村信用社">福建省农村信用社</option>' +
        '<option value="阜新银行结算中心">阜新银行结算中心</option>' +
        '<option value="富滇银行">富滇银行</option>' +
        '<option value="赣州银行">赣州银行</option>' +
        '<option value="广东华兴银行">广东华兴银行</option>' +
        '<option value="广东南粤银行股份有限公司">广东南粤银行股份有限公司</option>' +
        '<option value="广东省农村信用社联合社">广东省农村信用社联合社</option>' +
        '<option value="广发银行股份有限公司">广发银行股份有限公司</option>' +
        '<option value="广西北部湾银行">广西北部湾银行</option>' +
        '<option value="广西农村信用社">广西农村信用社（合作银行）</option>' +
        '<option value="广州农村商业银行">广州农村商业银行</option>' +
        '<option value="广州银行">广州银行</option>' +
        '<option value="贵阳银行">贵阳银行</option>' +
        '<option value="桂林银行股份有限公司">桂林银行股份有限公司</option>' +
        '<option value="哈尔滨银行结算中心">哈尔滨银行结算中心</option>' +
        '<option value="海口联合农村商业银行">海口联合农村商业银行</option>' +
        '<option value="海南省农村信用社">海南省农村信用社</option>' +
        '<option value="邯郸市商业银行">邯郸市商业银行</option>' +
        '<option value="韩亚银行">韩亚银行</option>' +
        '<option value="汉口银行">汉口银行</option>' +
        '<option value="杭州银行">杭州银行</option>' +
        '<option value="河北银行股份有限公司">河北银行股份有限公司</option>' +
        '<option value="恒丰银行">恒丰银行</option>' +
        '<option value="衡水银行">衡水银行</option>' +
        '<option value="湖北农信">湖北农信</option>' +
        '<option value="湖北银行">湖北银行</option>' +
        '<option value="湖州银行">湖州银行</option>' +
        '<option value="葫芦岛银行">葫芦岛银行</option>' +
        '<option value="华夏银行">华夏银行</option>' +
        '<option value="黄河农村商业银行">黄河农村商业银行</option>' +
        '<option value="徽商银行">徽商银行</option>' +
        '<option value="交通银行">交通银行</option>' +
        '<option value="吉林农村信用社">吉林农村信用社</option>' +
        '<option value="吉林银行">吉林银行</option>' +
        '<option value="济宁银行">济宁银行</option>' +
        '<option value="嘉兴银行清算中心">嘉兴银行清算中心</option>' +
        '<option value="江苏长江商行">江苏长江商行</option>' +
        '<option value="江苏省农村信用社联合社">江苏省农村信用社联合社</option>' +
        '<option value="江苏银行股份有限公司">江苏银行股份有限公司</option>' +
        '<option value="江西赣州银座村镇银行">江西赣州银座村镇银行</option>' +
        '<option value="江阴农商银行">江阴农商银行</option>' +
        '<option value="锦州银行">锦州银行</option>' +
        '<option value="晋城银行">晋城银行</option>' +
        '<option value="晋商银行网上银行">晋商银行网上银行</option>' +
        '<option value="九江银行股份有限公司">九江银行股份有限公司</option>' +
        '<option value="昆仑银行">昆仑银行</option>' +
        '<option value="昆山农村商业银行">昆山农村商业银行</option>' +
        '<option value="莱商银行">莱商银行</option>' +
        '<option value="兰州银行股份有限公司">兰州银行股份有限公司</option>' +
        '<option value="廊坊银行">廊坊银行</option>' +
        '<option value="临商银行">临商银行</option>' +
        '<option value="柳州银行">柳州银行</option>' +
        '<option value="龙江银行">龙江银行</option>' +
        '<option value="洛阳银行">洛阳银行</option>' +
        '<option value="漯河市商业银行">漯河市商业银行</option>' +
        '<option value="绵阳市商业银行">绵阳市商业银行</option>' +
        '<option value="南昌银行">南昌银行</option>' +
        '<option value="南充市商业银行">南充市商业银行</option>' +
        '<option value="南京银行">南京银行</option>' +
        '<option value="内蒙古银行">内蒙古银行</option>' +
        '<option value="宁波通商银行股份有限公司">宁波通商银行股份有限公司</option>' +
        '<option value="宁波银行">宁波银行</option>' +
        '<option value="宁夏银行">宁夏银行</option>' +
        '<option value="攀枝花市商业银行">攀枝花市商业银行</option>' +
        '<option value="平安银行（原深圳发展银行）">平安银行（原深圳发展银行）</option>' +
        '<option value="平顶山银行">平顶山银行</option>' +
        '<option value="齐鲁银行">齐鲁银行</option>' +
        '<option value="齐商银行">齐商银行</option>' +
        '<option value="企业银行">企业银行</option>' +
        '<option value="青岛银行">青岛银行</option>' +
        '<option value="青海银行">青海银行</option>' +
        '<option value="泉州银行">泉州银行</option>' +
        '<option value="日照银行">日照银行</option>' +
        '<option value="山东省农联社">山东省农联社</option>' +
        '<option value="上海农商银行">上海农商银行</option>' +
        '<option value="上海浦东发展银行">上海浦东发展银行</option>' +
        '<option value="上海银行">上海银行</option>' +
        '<option value="上饶银行">上饶银行</option>' +
        '<option value="绍兴银行">绍兴银行</option>' +
        '<option value="深圳福田银座村镇银行">深圳福田银座村镇银行</option>' +
        '<option value="深圳农商行">深圳农商行</option>' +
        '<option value="深圳前海微众银行">深圳前海微众银行</option>' +
        '<option value="盛京银行">盛京银行</option>' +
        '<option value="顺德农村商业银行">顺德农村商业银行</option>' +
        '<option value="四川省联社">四川省联社</option>' +
        '<option value="苏州银行">苏州银行</option>' +
        '<option value="厦门国际银行">厦门国际银行</option>' +
        '<option value="厦门银行">厦门银行</option>' +
        '<option value="台州银行">台州银行</option>' +
        '<option value="太仓农商行">太仓农商行</option>' +
        '<option value="泰安市商业银行">泰安市商业银行</option>' +
        '<option value="天津滨海农村商业银行股份有限公司">天津滨海农村商业银行股份有限公司</option>' +
        '<option value="天津农商银行">天津农商银行</option>' +
        '<option value="天津银行">天津银行</option>' +
        '<option value="威海市商业银行">威海市商业银行</option>' +
        '<option value="潍坊银行">潍坊银行</option>' +
        '<option value="温州银行">温州银行</option>' +
        '<option value="乌鲁木齐市商业银行">乌鲁木齐市商业银行</option>' +
        '<option value="吴江农村商业银行">吴江农村商业银行</option>' +
        '<option value="武汉农村商业银行">武汉农村商业银行</option>' +
        '<option value="西安银行">西安银行</option>' +
        '<option value="新韩银行中国">新韩银行中国</option>' +
        '<option value="兴业银行">兴业银行</option>' +
        '<option value="邢台银行">邢台银行</option>' +
        '<option value="烟台银行">烟台银行</option>' +
        '<option value="鄞州银行">鄞州银行</option>' +
        '<option value="营口银行">营口银行</option>' +
        '<option value="友利银行">友利银行</option>' +
        '<option value="云南省农村信用社">云南省农村信用社</option>' +
        '<option value="枣庄银行">枣庄银行</option>' +
        '<option value="张家港农村商业银行">张家港农村商业银行</option>' +
        '<option value="张家口银行股份有限公司">张家口银行股份有限公司</option>' +
        '<option value="浙江稠州商业银行">浙江稠州商业银行</option>' +
        '<option value="浙江景宁银座村镇银行">浙江景宁银座村镇银行</option>' +
        '<option value="浙江民泰商业银行">浙江民泰商业银行</option>' +
        '<option value="浙江三门银座村镇银行">浙江三门银座村镇银行</option>' +
        '<option value="浙江省农村信用社">浙江省农村信用社</option>' +
        '<option value="浙江泰隆商业银行">浙江泰隆商业银行</option>' +
        '<option value="浙商银行">浙商银行</option>' +
        '<option value="郑州银行">郑州银行</option>' +
        '<option value="中原银行">中原银行</option>' +
        '<option value="珠海华润银行清算中心">珠海华润银行清算中心</option>' +
        '<option value="自贡市商业银行清算中心">自贡市商业银行清算中心</option>' +
        '<option value="贵州省农村信用社">贵州省农村信用社</option>' +
        ' </select></div>' +
        '<div class="fm"><label><span style="color:#c52000; vertical-align:middle;";>*</span>银行账户：</label>' +
        '<input class="mn-ipt" type="text" id="chg_bank_account" name="chg_bank_account" style="width:210px"></div>' +
        '<div class="fm"><label><span style="color:#c52000; vertical-align:middle;">*</span>银行地址：</label>' +
        '<input class="mn-ipt" type="text" id="chg_bank_address" name="chg_bank_address" style="width:210px"></div>' +
        //        '<div class="fm">' +
        //        '<input class="mn-ipt" type="button" id="sumit" onclick="VerifyBank()" name="sumit" value="提交更换"/></div>' +
        //        '<div class="fm">' +
        //        '<input class="" type="button" id="chg_bank" name="chg_bank" value="取消更换"/></div>' +
        '</div>';
    if(loadfalg){
        return false ;
    }
    var submit = function (v, h, f) {
        if (v == true) {
            if (VerifyBank()==false){
                return false;
            }
            var dat = {};
            dat.uid = uid;
            dat.bank_name = $("#chg_bank").val(); // 银行名称
            dat.bank_address = $("#chg_bank_address").val(); // 银行地址
            dat.bank_account = $("#chg_bank_account").val(); // 银行账号
            loadfalg = true ;
            $.ajax({
                type: 'POST',
                url: '/app/member/money/updatebank.php',
                data: dat,
                dataType: 'json',

                success: function (ret) {
                    if (ret.code ==1) { // 更换成功
                        loadfalg = false ;
                        // 之前已经有帐号
                        $("#spn_bank").html(ret.resdata.Bank_Name);  // 银行名称
                        $('#spn_bank_account').html(ret.resdata.Bank_Account) ; // 银行帐号

                        $('#Bank_Address').val(ret.resdata.Bank_Address) ; // 银行地址
                        // 未绑定过银行帐号
                        $("#Bank_Name").val(ret.resdata.Bank_Name); // 银行名称
                        $("#Bank_Account").val(ret.resdata.Bank_Account);  // 银行帐号

                        jBox.tip("更换成功", 'success');

                    } else {
                        loadfalg = false ;
                        jBox.tip("更换失败", 'success');
                    }


                },
                error: function (res) {
                    loadfalg = false ;
                    jBox.tip("数据更新失败，请稍后再试!",'success');

                }
            });

        }

    };

    jBox.confirm(html, "更换银行帐号", submit, {
        id: 'creditsChangeBank',
        showScrolling: false,
        buttons: {'提交更换': true, '取消更换': false}
    });
}
function  mainSubmit(){
    if(false == VerifyData()) {
        return false;
    }
    Verifywithdrawpassword();
    main.submit();
    document.getElementById('mainSubmit').onclick=null;
    document.getElementById('mainSubmit').style.cursor="not-allowed";
    document.getElementById('mainSubmit').style.backgroundColor="gray";
    document.getElementById("next").innerHTML = "提交中...";
}
