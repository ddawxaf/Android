<?php
session_start();

$key = isset($_REQUEST['key'])?$_REQUEST['key']:'';
$companyName = $_SESSION['COMPANY_NAME_SESSION'];
?>

<style>
    .agencyWrap .nav li span.icon_gywm{background-position: -26px 0;}
    .agencyWrap .nav li span.icon_lxwm{background-position: -26px -34px;}
    .agencyWrap .nav li span.icon_ckbz{background-position: -26px -68px;}
    .agencyWrap .nav li span.icon_qkbz{background-position: -26px -100px;}
    .agencyWrap .nav li span.icon_cjwt{background-position: -26px -136px;}
    .agencyWrap .nav li span.icon_gzsm{background-position: -26px -168px;}
    .agencyWrap .nav li span.icon_sytk{background-position: -26px -203px;}
    .agencyWrap .nav li span.icon_bczr{background-position: -26px -238px;}
    .agencyWrap .nav li span.icon_right{background-position: -26px -274px;}
    .agencyWrap .nav li .active span.icon_gywm{background-position: 0 0;}
    .agencyWrap .nav li .active span.icon_lxwm{background-position: 0px -34px;}
    .agencyWrap .nav li .active span.icon_ckbz{background-position: 0px -68px;}
    .agencyWrap .nav li .active span.icon_qkbz{background-position: 0px -100px;}
    .agencyWrap .nav li .active span.icon_cjwt{background-position: 0px -136px;}
    .agencyWrap .nav li .active span.icon_gzsm{background-position: 0px -168px;}
    .agencyWrap .nav li .active span.icon_sytk{background-position: 0px -203px;}
    .agencyWrap .nav li .active span.icon_bczr{background-position: 0px -238px;}
    .agencyWrap .nav li .active span.icon_right{background-position: 0px -274px;}
    .agencyWrap .nav li:hover span{background-position-x:0px;color: #fff; }
</style>


<div class="agencyWrap">
    <div class="nav">

        <ul class="about-nav">
            <li><a class="<?php echo ($key==0?'active':'')?>" href="javascript:;"> <span class="icon_gywm"></span> 关于我们 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==1?'active':'')?>" href="javascript:;"> <span class="icon_lxwm"></span> 联系我们 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==2?'active':'')?>" href="javascript:;"> <span class="icon_ckbz"></span> 存款帮助 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==3?'active':'')?>" href="javascript:;"> <span class="icon_qkbz"></span> 取款帮助 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==4?'active':'')?>" href="javascript:;"> <span class="icon_cjwt"></span> 常见问题 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==5?'active':'')?>" href="javascript:;"> <span class="icon_gzsm"></span> 规则说明 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==6?'active':'')?>" href="javascript:;"> <span class="icon_sytk"></span> 使用条款 <span class="icon_right"> </a></li>
            <li><a class="<?php echo ($key==7?'active':'')?>" href="javascript:;"> <span class="icon_bczr"></span> 博彩责任 <span class="icon_right"> </a></li>
        </ul>
        <span class="bottomLogo"></span>
    </div>
    <div class="articleWrap">

        <h1 class="bzzx_title">关于我们</h1>
        <div class="textWrap">
            <div class="textBox textItem ">

                <p>“<?php echo $companyName;?>”是获得英属维京群岛(British Virgin Islands)政府认证的合法互联网体育博彩公司。自2007年开始正式投入服务至今，一直秉持诚信可靠，服务周到的企业宗旨为广大体育彩迷服务，由全球体育博彩业界精英组成的金牌团队，以超专业的服务素质，最先进完备的网上博彩系统每天为成千上万的体育彩迷提供难忘的博彩体验，因此“澳门线上娱乐体育博彩”在网上娱乐博彩业界已奠下了不可动摇的地位，成为广大体育彩迷信赖的合法网上娱乐博彩网站。</p>

                <p>“<?php echo $companyName;?>”网罗世界各地各种体育项目的精彩赛事投注选择，除了世界闻名的热门赛事外，更提供多样化的体育博彩项目与赛事，定能满足彩迷们的多样化体育观赏兴趣，在观赏比赛的同时获得更大的刺激与乐趣。 “澳门线上娱乐体育博彩”现正通过不断努力拓展，迈向体育博彩业界的高峰，同时希望能与广大的彩迷们共同向前迈进, 享受体育博彩投注的成果与乐趣、诚信可靠。</p>

                <p>“<?php echo $companyName;?>”总部位于菲律宾，是获得英属维京群岛政府认证的合法互联网体育博彩公司，汇集了全球的博彩业界精英，“澳门线上娱乐体育博彩”致力打造符合体育博彩市场需求，并值得体育彩迷信赖的网上投注平台，由业界精英组成的金牌专业团队，凭借对体育博彩市场的丰富经验，不断对体育博彩市场进行深入透彻的研究，并进一步将研究结果实践在产品完善化上。网罗世界各地各种体育比赛项目并提供多样化玩法选择，务求能成为为每位体育彩迷量身订造，以满足彩迷们的体育观赏及投注喜好为目标的世界性体彩投注网站。</p>

                <p>“<?php echo $companyName;?>”以雄厚实力建构了强大的网络在线体育博彩投注平台，提供安全稳定的投注系统是本公司对每位体育彩迷的承诺，专业技术团队拥有高度的系统保障和支持能力，高效健全的支付平台更确保在线投注的公平、公正和安全。彩金支付机制的安全快速更是业界之冠，所有会员的彩金都能在30分钟内到账，对会员承诺的履行永远都被我们视为成功的关键，因此“澳门线上娱乐体育博彩”能在全球网络在线体育投注平台赢得可靠可信的美誉，服务周到。</p>

                <p>“<?php echo $companyName;?>”一向以提供高品质高效率的客户服务为傲，根据对市场及体彩迷的分析而拟定之严谨客服人员培训规范，创造出具有专业认真，热情服务特点的专业客服团队，通过掌握丰富专业知识的客服人员友善热情的态度，体彩迷每次必能享受到完善周到的服务，无论是开户、充值、取款或有关体彩投注的问题咨询，热情专业的客服团队都能给予体彩迷们难忘的服务体验。</p>

                <p>24小时在线的专业热情客服团队让体彩迷能够随心所欲进行投注，全天候24小时随时恭候办理开户、充值及取款等各种博彩投注业务。体彩迷们可通过各种方包括网页在线客服、免费客服热线、QQ客服、微信客服/微信公众号与我们热情专业的客服人员联系，不论是办理投注业务或是向我们提出宝贵意见，我们都是无任欢迎。光临“澳门线上娱乐体育博彩”的每一位体彩迷们必能感受到我们的专业与热情，同时更能尽情享受体育博彩所带来的无限乐趣。</p>

                <p>“<?php echo $companyName;?>”设立于菲律宾,客户服务部设立在香港。我们提供每周7天每天24小时的实时中文客户服务,高素质全天候的客户服务可以保证客户的任何询问将以最快和最有效的方式得到处理。玩家可用聊天、电子邮件或电话的方式随时联络到我们。不论您有什么咨询、抱怨和建议，客服将以最快的速度给您满意的答复。 公司原名:<?php echo $companyName;?>；英文代名：CROWM成立于1990年，于1994年取得柬蒲寨政府颁发的合法经营执照。2004年5月18日宣布在澳门正式入股美国拉斯维加斯< 澳门挂牌注册。成为打入亚洲互联网博彩行业计划。由澳门政府认证合法的亚洲球类网上博彩投注公司,现同拉斯韦加斯皇家哈拉斯娱乐公司（Harrah is Entertainment Inc）及MGM Mirage公司合作发展亚洲更多国家博彩行业公司,是一个较为值得广大彩民信赖的正式球类网上投注公司澳门线上娱乐是一家以提供全方位100%的球类竞技博彩信息服务为主的网络公司。澳门线上娱乐博彩有限公司在欧洲享有极高的知名度。拥有国际级的博彩信息专家同市场策略专家，90年代亚洲盘各大博彩公司而普遍知名！为全球多家著名的网上博彩公司提供博彩信息服务及担任顾问。由于拥有财力雄厚的幕后集团为后盾，我们备有先进的软硬件开发技术专员及全面规划的组织系统。多年来奠定了澳门线上娱乐良好国际企业形象，并享誉信息服务界。 我们的顾客遍布欧洲及亚洲一带，尤其在英国、荷兰、意大利、德国、日本、韩国、中国、菲律宾、泰国、柬埔寨、越南等地。</p>

                <h2>------亲切体贴 以客为尊的亚洲客服中心-------</h2>
                <p>澳门线上娱乐博彩有限公司为服务会员，特设置亚洲地区多间客户服务中心，以最方便、最迅速与最亲切的服务宗旨来为会员解答所 有关于博彩汇款与会员权益等相关问题，为实践以客为尊的服务原则，澳门线上娱乐亚洲客服中心欢迎您的来电.....　　 客服中心欢迎您的来电: (每日二十四小时本公司将有专人为您服务,全年无休!)</p>

                <h2>------付款迅速 信用卓越的专属付款机制------</h2>
                <p>我们为了能让客户享有最快速的付款服务，当会员提出取款需求时，会员申请提款后将于3-5分钟内款项汇入会员指定的账户。坚持快速便利的付款机制，是我们经营企业的基本原则也是建立会员对我们企业信用最大的保障。</p>

                <h2>------快捷早盘 明显呈现保证能立即上位------</h2>
                <p>本公司提供亚洲最快最准的早盘讯息，使亚洲各个地区的朋友都能得到这种超值服务，而且肯定在第一时间通知您，明显的呈现在屏幕上。（例如：英超/意甲/德甲/西甲/法甲/荷甲/比甲在本周星期六开赛，本周星期一至星期二我们已经具备了这些赛事的盘口信息）</p>

                <h2>------优势水位 傲视亚洲各大盘口的赔率------</h2>
                <p>本站作为提供优质服务的足球博彩信息网络，主要目的在于让球友享有准确实用的信息，借此可以准确预测赛果所以在另外一方面也让各位球友享有优势的水位，使各位足球玩家受惠无穷。</p>

                <h2>------快捷赔率 为庄家及玩家的唯一首选------</h2>
                <p>本公司凭借与多家如英国、荷兰、意大利、德国等等国际大型博彩公司保持密切关系并且获得他们大力的支持固获得第一手信息，特提供全球最快最准的英国、中国、菲律宾、泰国、柬埔寨、越南等博彩公司的实时赔率表，而且每90秒自动更新，基于优势的水位，已成为各大庄家参考的标准赔率。</p>

                <h2>------多元化赔率 多重选择您所要的投注玩法------</h2>

                <p>澳门线上娱乐提供您全球最多元化的赔率的参考，例如：单式、滚球、波胆、入球数、标准过关、让球过关、先开球竞猜角球数 黄牌数 进球竞猜 角球竞猜 半场滚球 半场波胆 混合过关等等各位玩家可以利用这广阔的空间，尽情选择您所要的博彩方法，您会发现其中的多元奥妙之处，肯定让您乐不思蜀。</p>

                <h2>------专业 权威 荣誉 双赢------</h2>
                <p>凭多年的实力创造了有目共睹的辉煌，我们优越的技术管理赢得广大代理商的较好口碑！20年时间的见证，使得广大彩民对我们的信赖及热爱，较好的数据分析使得广大代理商一致的好评，立即加入我们，共迈双赢目标新纪元！</p>


            </div>
            <div class="textBox textItem hide">

                <p>澳门线上娱乐的客服中心全年无休，提供1周7天、每天24小时的优质服务。</p>
                <p>如果您对本网站的使用有任何疑问，可以通过下列任一方式与客服人员联系，享受最实时的服务。</p>
                <p>点击"在线客服"链接，即可进入在线客服系统与客服人员联系。</p>
                <p>您亦可使用Email与客服人员取得联系：</p>
                <p>24小时服务热线：<span class="ess_service_phone"> </span> </p>
                <p>24小时投诉电话：<span class="phl_service_phone"> </span> </p>
                <p>邮箱：<span class="sz_service_email"> </span> </p>

            </div>
            <div class="textBox textItem hide">
                <p>尊敬的用户，您现在可以通过以下方式存款给澳门线上娱乐：</p>

                <h2>一、公司入款</h2>
                <p>公司入款是通过公司指定的银行卡号进行存款支付，公司入款可以用网银转帐，ATM存款，或者柜台汇款的方式支付，具体步骤如下：</p>
                <p>1.会员可以通过在线客服获取公司入款账号。</p>
                <p>2. 跨行转账请您使用跨行快汇，存款到帐时间约为三到五分钟。</p>
                <p>3. 会员通过公司入款满100元系统将自动增加1%存款优惠，单笔存款10万以上赠送2%存款优惠，当日无上限。（入款1000+优惠10=1010）</p>
                <p>注：请您存款时在金额后面加个尾数（例如：欲入10000元，请转10000.15元），方便快速到账，谢谢！</p>

                <h2>二、 线上支付</h2>
                <p>1. 会员登入后点选"线上付款"。</p>
                <p>2. 选择入款额度，选择"支付银行"，选择确认送出。</p>
                <p>3. 确认送出后，将请您确认您的支付订单无误，并建议您记录您的支付订单号后，确认送出，并耐心等待载入网络银行页面，传输中已将您账户数据加密，请耐心等待。</p>
                <p>4. 进入网络银行页面，请确实填写您银行账号信息，支付成功，额度将在5分钟内系统处理完成，立即加入您的澳门线上娱乐 会员账户。</p>
                <p>5. 进入网上银行页面，请确认一下您填写的银行账号信息是否正确，支付成功后，额度将在5分钟内系统自动处理完成，立即加入您的澳门线上娱乐 会员账户。</p>

                <h2>◎存款需知：</h2>
                <p>· <?php echo $companyName;?> 单笔最低存款为100元人民币， 线上支付单笔最高存款上限为50,000人民币，若存款超过50,000，请分几次存款；公司入款单笔最高存款上限为1,000,000人民币。</p>
                <p>· 未开通网银的会员，请您亲自至银行柜台办理。</p>
                <p>· 如有任何问题，请您联系24小时线上客服。</p>

            </div>
            <div class="textBox textItem hide">

                <h2>◎取款方法 </h2>
                <p>1. 会员登入后点选"在线取款"。</p>
                <p>2. 输入取款密码，确认提款人姓名与您银行账号持有人相符。</p>
                <p>3. 输入出款额度点击下一步进行提交，如有任何问题，请您联系澳门线上娱乐在线客服，我们将在第一时间为您解答问题。</p>
                <p>4. 确认提款银行账号正确。</p>
                <p>5. 选择公司金流方式出款：</p>
                <p>绑定工商银行、农业银行、建设银行、招商银行、中国银行、交通银行、中信银行、光大银行、华夏银行、民生银行、广发银行、平安银行、邮政储蓄。</p>
                <h2>◎取款需知 </h2>
                <p>1. 澳门线上娱乐最低取款为$100人民币，单笔最高取款上限为$1,000,000人民币。</p>
                <p>2. 会员可在24小时内享有第一次提款$100,000人民币内5分钟内到帐，申请提款额度超过$1,000,000人民币，10分钟内到帐。</p>
                <p>3. 全天24小时无限取款次数，免除所有手续费、畅想取款无极限.</p>
                <p>4. **请注意: 各游戏和局/未接受/取消注单，不纳入有效投注计算。运动博弈游戏项目，大赔率玩法计算有效投注金额，小赔率玩法计算输赢金额为有效投注。 **大赔率产品包括: 过关、波胆、总入球、半全场、让球、大小、单双、冠军赛。</p>
                <p>5. 如有任何问题，请联系24小时在线客服！</p>
                <p>**澳门线上娱乐相关优惠，请点击'优惠活动'</p>

            </div>
            <div class="textBox textItem hide">
                    <p>
                        Q1：如何加入澳门线上娱乐？ 
                        <br>A1：您可以直接点选 "立即开户"，确实填写资料后，可立即登记成为澳门线上娱乐会员。 
                        <br>Q2：我可以直接在网络上存款吗？ 
                        <br>A2：可以，澳门线上娱乐提供多种在线存款选择，详情请参照 "如何存款"。 
                        <br>Q3：我在那里可以找到游戏的规则？ 
                        <br>A3：在未登入前，您可以在游戏的最外层看到"游戏规则"选项，清楚告诉您游戏的玩法、规则及派彩方式。 在游戏窗口中，也有"规则"选项，让您在享受游戏乐趣的同时，可以弹跳窗口随时提醒您游戏规则。 
                        <br>Q4：我的注码的限制是多少？ 
                        <br>A4：从最低注单10元人民币以上即可投注。 
                        <br>Q5：如果忘记密码怎么办？ 
                        <br>A5：你可以联系24小时在线客服人员咨询协助取回你的账号密码。 
                        <br>Q6：当你注册时出现姓名已注册怎么办？ 
                        <br>A6：你可通过24小时在线客服人员协助处理。 
                        <br>怎么开户？ 
                       <br>1：在注册开户过程中您必需确认您是否满十八岁并注意您所在国家或地区相关法律规定。 
                        <br>2：登陆我们的官方网站 <span class="backup_web_url"></span> 点击(立即开户)按要求填写相关资料就可以自助注册会员账号及设置登陆密码,为保证会员的个人信息安全请保管好您的会员账号与密码！ 
                        <br>我们如何相信澳门线上娱乐的信用呢？ 
                        <br>澳门线上娱乐建立网上博彩至今已有近十几年的优久历史，稳定的系统、丰富的玩法造就了数万计的会员，在网络这个传播速度飞跃的世界里，澳门线上娱乐从来没有信誉危机。信用制也好，现金制也好，根本还在于投注平台的稳定、服务的优越、资金进出的快捷等因素。澳门线上娱乐多年的口碑、长期的广告以及稳定的系统就是信誉的保证，在整个欧亚洲地区的所有在线博彩业里也称得上龙头老大。多年来，大陆地区的玩家都有一个思维误区：庄家永远是占优势，庄家是万利的，庄家可以通过各种途径改变比赛结果而实现稳赚。其实：全世界的网上博彩、陆地博彩都不用通过欺骗来赚钱，博彩业赚钱的本质来源于玩家对信息的掌握度不够、投注手法不专业以及玩家最难克服的贪性等等。全世界的博彩公司每年都投入巨资在设备、广告、营销上，而利润仅仅来源于微薄的水差，庄家靠的还是日益增长的交易量。如果靠欺骗，以互联网的传播速度臭名远扬，交易量迅速减少，会员丧失，庄家倒闭的速度会很快。当然，对于您头次买彩的金额，我们建议不一定要很大，长期的信用还要靠实践累积。澳门线上娱乐的会员并不是一天就增多的，逐渐增长的存款额、交易额证明了一切为了为消除新会员对澳门线上娱乐的疑虑，在整个亚洲地区本公司是唯一一家互联网开户网址 <span class="backup_web_url"></span> 祝您玩得开心！ 
                        <br>最低开户金额？ 
                        <br>在“<?php echo $companyName;?>”开户注册会员账号是完全免费的,投注资金最低为人民币10元,最高是没有限制的。 
                        <br>账户如何充值？ 
                        <br>我们公司有两种充值方式： 
                        <br>1、人工充值。请联系客服索取银行资料通过网上银行、银行柜台或者ATM柜员机转帐的方式存入我们公司指定的银行账户并及时通知我们客服帮您在账户里加上额度。 
                        <br>2、登入后点击在线充值。您可以进入相关银行网站通过第三方支付平台转帐至我们公司指定的银行账号进行充值。充值后系统会自动把您存的款项转至您所开户的会员账户里面，您将在个人账户信息栏里看到您所存的款项和相应的彩金或信用额度，就可进行交易。 
                        <br>注意：加入本公司的每位会员必需提供个人的联系电话和个人的银行账号(作为您要提彩的专用账号)。 不明处请联系客服! 
                       <br> 单场投注额最低是？ 
                        <br>我们单场最低投注额为人民币10元。 
                        <br>最高投注额有限制吗？ 
                        <br>您好，全世界任何一家正规博彩和网络博彩公司对客户的账户投注都有单注和单场限额，没有限额的公司都基本属于没有任何风险控制的私人或骗子公司，今天的控制是为了明天能保证100%提款给您，这点您可以自行分析。我们的新开户会员是都有单（注）场限额的，因为我们公司现有两种账户供你选择：一、现金账号。二、信用额账户(现金账户的最高投注额为人民币50万元)(信用额度是根据您所开的额度来按比例计算的最高不可超过信用额度本金的30%)。 
                        <br>如何登入投注页面？ 
                        <br>请您点击网站任一页面“会员登录”正确输入您的账号、密码,进入客户协议页面,点击“我同意”登入投注页面。 
                        <br>一、现金账户，投注方式：如您的现金账户是3000元,下1000元AC米兰,赛事结束后如赢的话您的账户里就马上有4000元,如输的话就剩2000元,可继续投注其它赛事。 
                       <br> 二、信用额度，投注方式：如您的信用额度账户是50000元，那么您每天最多只能投注50000元内不能超出50000元，不管输赢，账户里面的额度准时在第二天12：00系统会自动反回到您的账户里面。 
                        <br>我账户里面的帐怎么结算？ 
                        <br>一、现金账户是完场后系统会自动结算。 
                        <br>二、信用额度是每个星期一结算。 
                        <br>申请提彩要怎么办呢？ 
                        <br>您第一次提彩请登陆我们的官方网站 <span class="backup_web_url"></span> 登入会员账号后 点击(在线提款)填写好您的详细提款数据提交即可，我们会在最短时间内把资金转到您指定的账户里！ 
                        <br>可以随时申请提彩吗？ 
                        <br>可随时申请提彩和消户，随时提款，实时到帐！ 
                        <br>公司的彩金是怎样赠送呢？ 
                        <br>现金额度的彩金赠送基本上是根据不同时段公司不同赠送活动而变化的,多存多送不明处请联系客服！ 
                        <br>信用账号开设方法？ 
                       <br> 获取账户方式 每个账户都必须要押金 
                        <br>押金：50000以下的所有账户，押金需要开多少押多少。 （如：开设账户20000元押金需要：20000元） 
                        <br>押金：50000及以上的所有账户，押金押多少所开账户是押金的一倍。 （如：开设账户100000元押金只需要50000元） 
                        <br>结算方式？ 
                        <br>1：一个星期结算一次。 （在每个星期的星期一结算） 
                       <br> 2：一个星期结算两个。 （在每个星期的星期一与星期四进行结算） 
                        <br>3：按照金额结算，当天按照赢利或亏损进行结算，具体咨询我们的客服人员或专线人员。 
                       <br> 投注账户限制？ 
                        <br>所有开设的账户单场与单注大小具体根据账户的开设大小而定，请开设好了账户与本公司客服人员联系，如未与本公司联系，本公司将把此账户的金额大小做默认设置。 
                        <br>如何提走彩金？ 
                        <br>如果有赠送彩金的会员账户,提彩时您账户所交易的有效金额必需达到您开户金额15倍方可连彩金一起提走,不明处请联系客服! 
                        <br>有多少种投注方式选择？ 
                        <br>我们提供单式、上半场、滚球、波胆、上半波胆、1x2&入球数、半全场、标准过关、让球过关及综合过关等等多种投注方式选择。 
                        <br>可投注体育种类有哪些？ 
                        <br>可投注赛事包括：足球欧洲五大联赛,英格兰超级联赛,德国,法国,西班牙,意大利甲组联赛、篮球NBA、排球、棒球、高尔夫球、拳击、网球等等。
                    </p>

            </div>

            <div class="textBox textItem hide">

                <p>
                    <br>●此规则将被定义为规则和条款,适用于本公司所有交易.此条款包括了最大派彩金额、交易范围和交易时间.阁下有责任确保您获知所有的规则条款.我们保留随时修改条款的权利,有关修改的内容我们会公布在网上.请您留意公布在网上的交易附加条款.如果有任何不一致的地方,将以附加条款为准. 
                    <br>●此条款是公平公正的,如果您对此有任何意见,您可以联系我们的客服部,我们的客服团队将热诚协助每位客户,并确保能及时友善的解决您的问题.对于任何错误或争议,我们的客服团队将全力提供服务. 
                    <br>●所有的条款建立在会员与公司之间.对产生的任何争议,希望通过该条款让双方都达到满意的程度. 
                    <br>所有在进行的交易都需要依照以下规则容和条款: 
                    <br>1.所有比赛中的最高和最低交易额将由公司决定,如有更改不需要提前通知. 
                    <br>2.会员申请账户时需提供正确的讯息,对于提供错误讯息的账户我们将不负责任何责任. 
                    <br>3.在本公司的交易,会员需要自行负责账户交易.会员交易后需要仔细检查交易单,一旦交易接受,将无法更改或取消.公司对会员本身原因造成的遗漏或重复的交易单不负责任何责任.会员可以在"交易状况"中查看详情确保所有需要的交易单已经被接受. 
                    <br>4.对于任何交易投诉,如果没有记录或存储在公司的数据库中,公司将不接受或认可任何会员提供的复印件或单据. 
                    <br>5.赛事结果圴在赛事结束后确认,除非在相关体育或赛事规则另有说明.赛果公布72小时后,若对任何赛果有争议,将不认可.赛果公布72小时内,除了任何体育纪律委员会所重新裁决之赛果;所有人为错误或相官网页失误等原因本公司才会将错误的赛果修正. 
                    <br>6.公司保留在任何时候关闭或冻结会员账号的权利. 
                    <br>7.如果此规则和条款有任何争议,请以公司的解释为准. 
                    <br>8.在滚球赛事中,公司保留权利取消赛果已预先知道的交易.如果由于"现场直播"延迟而引起的盘口不正确或赔率,此期间的交易单将视为无效. 
                   <br> 9.比赛时候,定时器和红卡的显示仅供会员参考,公司对提供讯息的准确性不负任何责任. 
                    <br>10.如比赛在法定时间提前进行,在比赛开始前的交易仍然有效.在比赛开始后的所有交易圴视为无效.(滚球交易另作别论) 
                    <br>11.所有赛事都需要在公司原定的法定时间内进行,如果被终止,较原定比赛时间延迟12个小时或以上、或被取消,则该场球赛的交易圴被视为无效."连串过关"将被视为有效,唯赔率概以「1」计算.公司对此所作出的处理是最终的处理结果,不管与其它官方或体育网站公布的结果是否一致. 
                    <br>12.如果对其他语言版本的球赛队名有争议,请以英文网站的名称为准. 
                   <br> 13. 
                    <br>a.公司的网站,服务器或网络中断 
                    <br>b.公司的服务器丢失讯息或讯息遭受破坏 
                    <br>c.不法份子攻击网站,服务器或网络供货商 
                    <br>d.进入网站时候由于网络供货商原因造成的网络缓慢 
                    <br>所有的交易将以公司后台记录的数据为准,公司不接受任何投诉或争议,除非客户提供有效的交易记录的截图或证据,否则公司的交易记录将被视为最后的证明. 

                </p>
            </div>

            <div class="textBox textItem hide">

                <p>
                    接受私隐政策
                    <br>进入及使用本网站 ("<span class="backup_web_url"></span>>") 你将会被默认为接受本网站的使用条款。以下简称为"我们的"、"我们"、"我们的公司"或相似的简称是指Firstright Developments Limited；简称为"你"、"你们" 或相似的简称是指进入本网站的人（无论是自然进入与否）。
                    <br>不涉提供购买或销售
                    <br>本网站的内容在任何情况下都不涉及提出销售或要求购买任何产品或服务，无论是本公司的与否。
                    <br>连接到其它网站
                    <br>连接到第三方的网站仅仅是为了给你提供方便。 若你使用这些连接，你将离开本网站，而不同的使用条款将会套用在那些网站上。 有些到其它网站的连接，即使那些网站由我们的公司经营，提供的内容或许并不适合所有人，当通过这个网站的连接进入此网站时，你必须阅读及接受这个网站的使用条款。 我们不会查看第三方网站或其连接的有关条款，不会控制以及并不负责这些网站的任何事情，或者他们的内容。 我们不会采用和接受任何第三方的网站的声明。
                    <br>内容声明
                    <br>我们尽力维持本网站的内容准确，完全，与及最新（自发表的日期计算），然而我们的公司并不保证内容准确、完整与及最新，并且不对因技术或数据输入出错而负责。此外发表的信息可能会因日期的改变而不再有效。我们的公司不负责更新这些信息。在使用这些信息之前进行核对是你的责任。 我们的公司保留权利去修改或更新本网站的信息，而不用作预先通知。 一些产品或服务或会因为受监管或其它限制而不能在所有市场上提供。
                    <br>本网站所有内容由澳门线上娱乐 拥有版权，并保留一切权利。 任何未经我们的公司书面批准下的使用，复制，贮存便属非法。 本网站所有图案、商标、设计、商业包装与及/或者其它知识产权均由澳门线上娱乐 拥有或我们的公司有合法权益；任何未经批准的应用均属违法。 我们的公司会追讨每一项可能的赔偿(民事或刑事)，起诉任何对本网站的知识产权的非法应用或者滥用。
                    <br>通过本网站取得产品和服务，代表你已经接受该前述产品和服务的使用条款。
                    <br>会员、代理及第三方营销者受约束于维持与我们的公司的关系的规则与条款。
                    <br>促销、推广项目与及任何类似的活动将会受到显著地于本网站发布的每次促销或者推广项目特定规则与条款所控制。
                    <br>所有信息只是作为提供信息的目的，并不会给在美国、中华人民共和国香港特别行政区、越南、菲律宾共和国或者有关信息是被禁止的或需要执照的法律管辖区域，出版或发布。
                    <br>使用者的责任
                    <br>用户进入及使用本网站的内容均由用户承担风险。 <?php echo $companyName;?> 在线与及任何参与创作、制作或者传播本网站的人士，对任何直接的、偶然的、造成后果的、间接的或惩罚性的损坏，或在你访问过程中出现的损坏、能否使用此网站或任何连接的其它网站、任何错误或内容中的遗漏所引起的无论什么样的损坏，都不会承担责任。
                    <br>如有任何问题，意见或关注事项，请联系"在线客服"。
                </p>

            </div>

            <div class="textBox textItem hide">

                <p>
                    <?php echo $companyName;?> 致力于忠诚与可信赖的博彩保证。我们公司不但努力遵从远端博彩管理当局的适用法规以及指引，而且努力成为对社会负责任的远端博彩运营公司。 　　
                    <br>远程博彩是全球数以百万玩家的合法娱乐体验。对大多数玩家来说，远端博彩是一项令人愉快的体验，不过我们也接受这样的现实，少部分沉迷在远端博彩的玩家可能会未达到法定年龄或者出现因博彩而影响了他们的生活或财产状况的问题。 　　
                    <br>作为一个对社会负责的公司意味着要关注我们的玩家，意味着要对可能对社会产生影响的问题采用主动的方法去解决。这正是为何澳门线上娱乐 会采用并完全承诺执行以下最严格的程序和强制。 　　
                    <br>执行政策： 　　
                    <br>对未成年人的访问限制 　　
                    <br><?php echo $companyName;?> 要求新客户申明他们已经达到他们所属的司法管辖地区规定的法定年龄且至少年满18岁。当我们怀疑客户可能虚假申报或可能是未成年人试图使用我们的服务时，我们会使用合理的方法进一步进行验证。 　　
                    <br>而且，为确保未成年人不能在我们的网站上进行娱乐，我们在新客户注册时就要求所有客户提供姓名、位址或地址和出生日期；由我们的客户申明的这些资料必须真实和准确。 　　
                    <br>我们同样确保所有广告、赞助以及推广活动并不会针对未成年人。 　　
                    <br><?php echo $companyName;?> 不会允许任何未满18岁的人士使用我们的服务。此政策完全遵从并满足监管和给我们发放运营牌照的远端博彩管理当局，First Cagayan Leisure and Resort Corporation (FCRLC) for the Cagayan Economic Zone Authority (CEZA), of Santa Ana, Cagayan，Philippines的规则和规定； 　　
                    <br>我们承诺将尽我们所能而同时也需要您的协助做到以下这些： 　　
                    <br>1:使用儿童保护软件在可能被未成年人使用的电脑上屏蔽远端博彩网站。 　　
                    <br>2:当您的电脑登录到远端博彩网站时不要让电脑处于无人在旁的状况。 　　
                    <br>3:不要告知未成年人您的信用卡或银行帐户的详细资料。 　　
                    <br>4:不要在澳门线上娱乐 登录页面上让“保存密码”选项生效。 　　
                    <br>5:在电脑上为未成年人建立独立的登录档桉，令他们登录时无法访问您的资料。 　　
                    <br>6:如果您知道有人未满18岁（或未满他们所属司法管辖地区法定年龄）但错误地注册成为我们的玩家，请立刻通知我们。 　　
                    <br>控制嗜赌强迫症 　　
                    <br>嗜赌强迫症对澳门线上娱乐 毫无好处。我们力求在我们网站上营造一个安全、愉快的环境。嗜赌强迫症博彩者对他们的家庭、朋友和他们的生活都是一个问题，并形成一个不稳定和不安全的环境。 　　
                    <br><?php echo $companyName;?> 承诺监察和减少嗜赌强迫症。我们有一个玩家行为追踪系统，而且我们一直努力设计和强制控制以限制失控的博彩方式出现。 　　
                    <br>当大部分人在他们自我控制的方法下进行博彩娱乐，仍有一些人可能会无法自控。我们不但努力识别嗜赌强迫症博彩者，而且也帮助他们对付他们的问题并阻止其他玩家成为嗜赌强迫症博彩者。 　　
                    <br>为了帮助您保持自控能力，我们希望您牢记以下这些： 　　
                    <br>1:博彩应该被看成是娱乐活动，对待它应该如同您为娱乐而付费，因此不应该超出您的承受能力进行付费娱乐。 　　
                    <br>2:博彩不应该被视为赚钱赢利的正途。 　　
                    <br>3:避免急于追回损失。 　　
                    <br>4:只在您能够承受损失的范围内进行博彩。 　　
                    <br>5:追踪监控您在博彩上所花费的时间和金钱。 　　
                    <br>6:如果您需要帮助，请使用存款限制和时间限制来控制您所花费的金钱。 　　
                    <br>7:如果您需要暂时停止博彩，请联络我们进行自我排除或暂停帐户。 　　
                    <br><?php echo $companyName;?> 遵从处理博彩社会影响的专门制度指引。为帮助玩家负责任地博彩，我们承诺会在这些制度下发展负责任的政策和实践。 　　
                    <br>另外，我们鼓励以下行为： 　　
                    <br>1:公司方面的投注限制： 　　
                    <br>2:通过设置投注限制政策，我们努力限制玩家在一定时间段内进行的投注，是在受控制的次数以内。 　　
                    <br>3:客户方面的投注限制： 　　
                    <br>4:设置投注限额能够简单地令您在一定时间段内，控制您帐户的可以进行投注的金额。请您在我们网站开始玩之前，联络我们设置您的投注限额政策。 　　
                    <br>5:自我限制工具： 　　
                    <br>为了您自我的保护，您可以要求一定时间的自我限制或暂停帐户。这意味着在您指定的冷静时段内，您将不能登录到我们的网站。当您的自我限制时段结束时，您将自动可以重新登录。 　　
                    <br>懂得何时退出 　　
                    <br>在线上博彩是一种娱乐形式，玩家很有必要明白，博彩是否已经令您的经济或感情生活构成了负担，我们会建议您问自己以下20个问题： 　　
                    <br>您是否曾经因为博彩而损失了工作或教育的时间？ 　　
                    <br>博彩是否曾让您的家庭生活不愉快？ 　　
                    <br>博彩是否影响了您的名誉？ 　　
                    <br>您是否曾在进行博彩之后自责？ 　　
                    <br>您是否曾经用博彩来取得金钱进行偿还债务或解决财务困难？ 　　
                    <br>博彩是否曾令您的进取心或效率下降？ 　　
                    <br>当您输钱以后，您是否感到您需要尽快重新再来并赢回您的损失？ 　　
                    <br>当您赢钱以后，您是否有强烈的冲动重新再来并赢得更多？ 　　
                    <br>您是否曾经输到一文不剩？ 　　
                    <br>您是否曾经借钱来赌博？ 　　
                    <br>您是否曾经变卖一些东西换取金钱来赌博？ 　　
                    <br>您是否不情愿地把“用来进行赌博的钱”支付日常开支？ 　　
                    <br>赌博是否让您不再关心您自己或您家庭的幸福？　
                    <br>您是否曾经超过您计划的时间进行赌博？ 　　
                    <br>您是否曾经用赌博来逃避忧虑或麻烦？ 　　
                    <br>您是否曾经犯下或考虑过犯下不合法的行为来取得金钱进行赌博？ 　　
                    <br>赌博是否曾令您失眠？ 　　
                    <br>在您内心是否有争执、失望或挫折感令您产生去赌博的冲动？ 　　
                    <br>您是否曾有冲动用几个小时的赌博来庆祝您幸运的事情？ 　　
                    <br>您是否曾经考虑过自毁或自杀作为您赌博的最终结果？ 　　
                    <br>大部分的嗜赌强迫症博彩者对其中至少7个问题回答“是”。如果您希望找到专业的建议，请寻求帮助。 　　
                    <br>面向全球提升我们的标准 　　
                    <br><?php echo $companyName;?> 力求在我们的目标区域内提升标准，因此我们竭尽所能地以负责、诚信、透明度、合法性以及认知度来进行自律。我们致力于面向全球提升诚信度，因为我们相信以最高标准进行运营对所有人都将有所裨益。 　　
                    <br>负责任和诚信 　　
                    <br>我们相信我们会和客户对我们的看法一样出色。因此，最高水准的诚信度是澳门线上娱乐 得以做到让我们的客户对我们有这样的看法的唯一标准。 　　
                    <br>交易处理 　　
                    <br><?php echo $companyName;?> 遵守所属司法管辖区域关于资金交易的法律，而且IBC还遵从被国际认可的交易实体所接受的资金交易标准。 　　
                    <br>付款 　　
                    <br><?php echo $companyName;?> 保证从不拖延付款给客户。 　　
                    <br>客户服务承诺 　　
                    <br>在澳门线上娱乐 ，让客户称心如意具有最高优先权。这正是为什么您可以通过不同的方法通过客户服务支援部门与我们联系，客户服务部门会在一天24小时、一周7天随时准备为您提供技术支援、争端解决以及回答您的问题。
                </p>

            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        var index = '<?php echo $key;?>';
        // 进到页面默认处理
        $('.textWrap .textBox:eq('+index+')').removeClass('hide').siblings().addClass('hide');

       // 标签切换
        $('.about-nav li a').on('click',function () {
           var ii = $(this).parents('li').index();
           var tx = $(this).text();
            // console.log(ii);
            $('.bzzx_title').text(tx);
           $(this).addClass('active').parents('li').siblings().find('a').removeClass('active');
           $('.textWrap .textBox:eq('+ii+')').removeClass('hide').siblings().addClass('hide');

        });
        
    })
</script>