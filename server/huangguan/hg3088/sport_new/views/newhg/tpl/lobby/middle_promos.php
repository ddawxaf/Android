<?php
session_start();
include "../../../../app/member/include/config.inc.php";
$prokeys = isset($_REQUEST['prokey'])?$_REQUEST['prokey']:'' ;  // 活动图片key
$lists = returnPromosList('',1);
$promotionList = json_encode($lists);

$categoryList = returnPromosType();

?>
<style>

    /* 优惠 */
    .slots{background: url(<?php echo TPL_NAME;?>images/promos/promo_bg.png) center no-repeat;min-height: 600px;}
    .discount-nav{height:52px;line-height:52px;margin: 20px 0 15px;}
    .discount-nav-list{max-width:1200px;margin:0 auto;overflow:hidden}
    .discount-nav-list span{float:left}
    .discount-nav-list ul{float:left;overflow:hidden;}
    .discount-nav-list ul li{float:left;width: 120px;margin-right: 25px;}
    .discount-nav-list ul li a{border: 1px solid #DCDFE6;transition:.3s;position:relative;color: #838383;display: inline-block;width: 100%;height: 33px;line-height: 33px;text-align: center;background: #fff;border-radius: 20px; padding: 0 0 0 15px;}
    .discount-nav-list ul li.active a,.discount-nav-list ul li a:hover{background: #ff9a03;background-size: 100%;color: #fff;}
    .discount-nav-list ul li a:before{position:absolute;content:'';display:inline-block;width:26px;height:26px;background:url(<?php echo TPL_NAME;?>images/promos/prom_btn_ico.png) no-repeat;left:10px;top:3px;background-position:-23px -32px}
    .discount-nav-list ul li a.sport_icon:before{background-position-x:-153px;}
    .discount-nav-list ul li a.live_icon:before{background-position-x:-284px;}
    .discount-nav-list ul li a.lottery_icon:before{background-position-x:-416px;}
    .discount-nav-list ul li a.chess_icon:before{background-position-x:-550px;}
    .discount-nav-list ul li a.game_icon:before{background-position-x:-686px;}
    .discount-nav-list ul li a.autoget_icon:before{background-position-x:-822px;}
    .discount-nav-list ul li.active a:before,.discount-nav-list ul li a:hover:before{background-position-y: -2px;}
    .discount-nav-list span{color:#838383;margin-left:15px;}
    .news-content-center{max-width: 1200px;margin:0 auto;overflow-y: hidden;padding: 10px 0;}
    .news-content-center ul li{height: 390px;overflow:hidden;border-radius: 5px;width: 20%;float: left;margin: 10px;padding: 20px;transition: .3s;}
    .news-content-center ul li:hover {background: #fff;box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);transform: translateY(-2%);border-radius: 10px;}
    .news-content-center ul li:nth-child(4n+1) {margin-left: 10px;}
    .news-content-center ul li:last-child{border:none}
    .news-content-center ul li .imgbox{display:block;height: 313px;}
    .news-content-center ul li .imgbox img{width: 100%;height:100%;display:block;margin: 0 auto;}
    .news-content-center ul li .news-wrap{position:relative;text-align: center;}
    .news-content-center ul li .news-wrap .tit{font-weight:normal;margin:5px 0 0;color:#575656;}
    .news-content-center ul li .news-wrap .tit.sec_tit{font-weight:normal;font-size:12px;color: #a8a7a7;margin-top: 0;}
    .news-content-center ul li .news-wrap .more{background:#ff9a03;background-size:100%;display:inline-block;padding:2px 10px;color:#fff;border-radius:20px;margin-top:10px}
    .news-content-center ul li .hd_time{font-size:12px;color:#999;padding-left:26px}
</style>

<div class="slots">
    <div class="w_1200">
        <div class="discount-nav">
            <div class="discount-nav-list">
                <!-- <span>游戏类型：</span>-->
                <ul>
                    <li class="active"><a href="javascript:;" data-type="all">全部优惠</a></li>
                    <?php foreach ($categoryList as $key => $category){?>
                        <li><a href="javascript:;" class="<?php echo $category['tag'];?>_icon" data-id="<?php echo $category['id'];?>" data-type="<?php echo $category['tag'];?>"><?php echo $category['name'];?></a></li>
                    <?php }?>
                </ul>
            </div>
        </div>

        <div class="news-list-panel">
            <div class="news-content-center">
                <ul>

                </ul>
            </div>
        </div>

    </div>
</div>
<script type="text/javascript" src="js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
    $(function () {

        // 优惠列表
        var promosList = <?php echo $promotionList;?>; // 新的活动（后台上传的活动）

        function showPromosList() {
            var liststr = '';
            if(promosList.length > 0){
                for(var i=0;i<promosList.length;i++){
                    liststr += '     <li class="promos_'+  promosList[i].type +' promos_'+ promosList[i].typesec +'">' +
                        '                <a class="imgbox to_promos_details" href="javascript:;" data-flag="'+ promosList[i].flag +'" data-api="'+ promosList[i].ajaxurl +'" data-type="'+ promosList[i].type + '" data-keys="'+ promosList[i].contenturl +'" data-title="'+ promosList[i].title +','+ promosList[i].title1 +'">' +
                        '                    <img class="lazy" src="/images/loading.png" data-original="'+ promosList[i].imgurl +'" alt="">' +
                        '                </a>' +
                        '                <div class="news-wrap">' +
                        '                    <h1 class="tit">'+ promosList[i].title +'</h1>' +
                        '                    <h1 class="tit sec_tit">'+ promosList[i].title1 +'</h1>' +
                        // '                    <p class="date">'+ promosList[i].date +'</p>' +
                        '                    <a class="more to_promos_details promos_id_'+promosList[i].id+'" href="javascript:;" data-flag="'+ promosList[i].flag +'" data-api="'+ promosList[i].ajaxurl +'" data-type="'+ promosList[i].type + '" data-keys="'+ promosList[i].contenturl +'" data-title="'+ promosList[i].title +','+ promosList[i].title1 +'"> 查看详情 </a>' +
                        '                </div>' +
                        '            </li>'
                }
            }

            $('.news-content-center ul').html(liststr) ;
            $('img.lazy').lazyload(); // 懒加载
            goToPromosDetail();
        }

        // 标签切换
        function changePromoNav(){
            $('.discount-nav-list').on('click','a',function () {
                var categoryId = $(this).data('id') ;
                var type = $(this).data('type') ;
                $(this).parents('li').addClass('active').siblings('li').removeClass('active');
                if(type == 'all'){ // 全部
                    $('.news-content-center li').show() ;
                }else{
                    $('.news-content-center li').hide() ;
                    $('.news-content-center .promos_'+categoryId).show() ;
                }

            })
        }
        // 跳转到对应的优惠活动详情
        function goToPromosDetail(){
            var key = '<?php echo $prokeys;?>';
            if(key){
                $('.promos_id_'+key).click();
            }
        }

        changePromoNav() ;
        showPromosList() ;


    })
</script>