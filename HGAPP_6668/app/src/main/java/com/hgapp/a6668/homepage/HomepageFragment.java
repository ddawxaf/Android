package com.hgapp.a6668.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hgapp.a6668.HGApplication;
import com.hgapp.a6668.Injections;
import com.hgapp.a6668.R;
import com.hgapp.a6668.base.HGBaseFragment;
import com.hgapp.a6668.base.IPresenter;
import com.hgapp.a6668.common.adapters.AutoSizeRVAdapter;
import com.hgapp.a6668.common.event.LogoutEvent;
import com.hgapp.a6668.common.http.Client;
import com.hgapp.a6668.common.http.cphttp.CPClient;
import com.hgapp.a6668.common.util.ACache;
import com.hgapp.a6668.common.util.GameShipHelper;
import com.hgapp.a6668.common.util.HGConstant;
import com.hgapp.a6668.common.widgets.CustomPopWindow;
import com.hgapp.a6668.common.widgets.MarqueeTextView;
import com.hgapp.a6668.common.widgets.RoundCornerImageView;
import com.hgapp.a6668.data.AGCheckAcountResult;
import com.hgapp.a6668.data.AGGameLoginResult;
import com.hgapp.a6668.data.BannerResult;
import com.hgapp.a6668.data.CPResult;
import com.hgapp.a6668.data.CheckAgLiveResult;
import com.hgapp.a6668.data.DomainUrl;
import com.hgapp.a6668.data.LoginResult;
import com.hgapp.a6668.data.MaintainResult;
import com.hgapp.a6668.data.NoticeResult;
import com.hgapp.a6668.data.OnlineServiceResult;
import com.hgapp.a6668.data.QipaiResult;
import com.hgapp.a6668.data.ValidResult;
import com.hgapp.a6668.homepage.aglist.AGListFragment;
import com.hgapp.a6668.homepage.aglist.DZGameFragment;
import com.hgapp.a6668.homepage.aglist.playgame.XPlayGameActivity;
import com.hgapp.a6668.homepage.cplist.CPListFragment;
import com.hgapp.a6668.homepage.events.EventShowDialog;
import com.hgapp.a6668.homepage.events.EventsFragment;
import com.hgapp.a6668.homepage.handicap.HandicapFragment;
import com.hgapp.a6668.homepage.noticelist.NoticeListFragment;
import com.hgapp.a6668.homepage.online.ContractFragment;
import com.hgapp.a6668.homepage.online.OnlineFragment;
import com.hgapp.a6668.homepage.signtoday.SignTodayFragment;
import com.hgapp.a6668.login.fastlogin.LoginFragment;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.hgapp.common.util.NetworkUtils;
import com.lzj.gallery.library.views.BannerViewPager;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;

/**
 *
 AG真人，老虎机，皇冠体育，彩票，优惠活动，联系我们，公告，代理加盟，新手教学（暂时先不做）

 AG真人，老虎机，皇冠体育，彩票，优惠活动，联系我们，公告，代理加盟，新手教学
 */
public class HomepageFragment extends HGBaseFragment implements HomePageContract.View{

    @BindView(R.id.tvHomePageLine)
    TextView tvHomePageLine;
    @BindView(R.id.tvHomePageLogin)
    TextView tvHomePageLogin;
    @BindView(R.id.tvHomePageUserMoney)
    TextView tvHomePageUserMoney;
    /*@BindView(R.id.rollpageview)
    RollPagerView rollpageview;*/
    @BindView(R.id.banner_3d)
    BannerViewPager banner_3d;
    @BindView(R.id.tv_homapage_bulletin)
    MarqueeTextView tvHomapageBulletin;

    @BindView(R.id.homepageTab)
    TabLayout tabLayout;

    @BindView(R.id.rv_homepage_game_hall)
    RecyclerView recyclerView;
    @BindView(R.id.home_sign)
    ImageView homeSign;

    @BindView(R.id.homeUserName)
    TextView homeUserName;
    @BindView(R.id.homeGoLogin)
    LinearLayout homeGoLogin;
    @BindView(R.id.homeLoginAl)
    LinearLayout homeLoginAl;
    @BindView(R.id.homeMoney)
    TextView homeMoney;

    @BindView(R.id.homeDeposit)
    TextView homeDeposit;
    @BindView(R.id.homeDepositC)
    TextView homeDepositC;
    @BindView(R.id.homeDwith)
    TextView homeDwith;
    @BindView(R.id.homeBank)
    TextView homeBank;


    private static List<HomePageIcon> homeGameList = new ArrayList<HomePageIcon>();

    HomePageContract.Presenter presenter;

    private RollPagerViewManager rollPagerViewManager;

    private NoticeResult noticeResultList;
    private CustomPopWindow mCustomPopWindowIn;
    private String userName ="";
    private  String pro =  "";
    private String userMoney = "";
    private String userState = "19";
    //private CheckUpgradeResult checkUpgradeResult;

    private LinearLayoutManager manager;
    private String[] strTitleName = {"体育", "真人", "电竞", "棋牌", "彩票", "电游"};
    private int[] strTitleIcon = {R.mipmap.home_tab_ty,R.mipmap.home_tab_zr, R.mipmap.home_tab_dz, R.mipmap.home_tab_qp, R.mipmap.home_tab_cp, R.mipmap.home_tab_yy};
    /**
     * 需要定位的地方，从小到大排列，需要和tab对应起来，长度一样
     */
    private int[] strTitleMarkup = {0, 1, 5, 6, 10, 11};
    private boolean isScrolled = false;
    static {
        homeGameList.add(new HomePageIcon("体育投注",R.mipmap.home_hgty,0));

        homeGameList.add(new HomePageIcon("AG视讯",R.mipmap.home_ag,1));
        homeGameList.add(new HomePageIcon("OG视讯",R.mipmap.home_og,16));
        homeGameList.add(new HomePageIcon("BBIN视讯",R.mipmap.home_bbin,17));
        homeGameList.add(new HomePageIcon("DS视讯",R.mipmap.home_ds,18));
        homeGameList.add(new HomePageIcon("电子竞技",R.mipmap.home_avia,14));
        homeGameList.add(new HomePageIcon("VG棋牌",R.mipmap.home_vg,5));
        homeGameList.add(new HomePageIcon("乐游棋牌",R.mipmap.home_ly,13));
        homeGameList.add(new HomePageIcon("开元棋牌",R.mipmap.home_ky,4));
        homeGameList.add(new HomePageIcon("皇冠棋牌",R.mipmap.home_hg,3));
        homeGameList.add(new HomePageIcon("彩票游戏",R.mipmap.home_vrcp,2));

        homeGameList.add(new HomePageIcon("FG电子",R.mipmap.home_dz_fg,6));
        homeGameList.add(new HomePageIcon("AG电子",R.mipmap.home_dz_ag,6));
        homeGameList.add(new HomePageIcon("MG电子",R.mipmap.home_dz_mg,6));
        homeGameList.add(new HomePageIcon("MW电子",R.mipmap.home_dz_mw,6));
        homeGameList.add(new HomePageIcon("CQ9电子",R.mipmap.home_dz_cq9,6));


        /*homeGameList.add(new HomePageIcon("AG捕鱼",R.mipmap.home_agfishing,15));
        homeGameList.add(new HomePageIcon("幸运红包",R.mipmap.home_red,8));
        homeGameList.add(new HomePageIcon("代理加盟",R.mipmap.home_agent,7));
        homeGameList.add(new HomePageIcon("优惠活动",R.mipmap.home_pro,9));
        homeGameList.add(new HomePageIcon("联系我们",R.mipmap.home_contact,10));
        homeGameList.add(new HomePageIcon("新手教学",R.mipmap.home_new,11));
        homeGameList.add(new HomePageIcon("皇冠公告",R.mipmap.home_remind,12));*/
//        homeGameList.add(new HomePageIcon("电脑版",R.mipmap.home_pc));
//        homeGameList.add(new HomePageIcon("APP下载区",R.mipmap.home_download));
//        homeGameList.add(new HomePageIcon("线路导航",R.mipmap.home_wifi));

    }

    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        //注入控制器
        Injections.inject(null,(HomePageContract.View) fragment);
        return fragment;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_home;
    }


    private void initTab() {
        for (int i = 0; i < strTitleName.length; i++) {
            //插入tab标签
           // tabLayout.addTab(tabLayout.newTab().setText(strTitleName[i]));
            tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.home_tab_item));
        }

        for (int i = 0; i < strTitleName.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);//获得每一个tab
//            tab.setCustomView(R.layout.home_tab_item);//给每一个tab设置view
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.homeTabItemName);
            textView.setText(strTitleName[i]);//设置tab上的文字
            tab.getCustomView().findViewById(R.id.homeTabItemIcon).setBackgroundResource(strTitleIcon[i]);
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.VISIBLE);
                tab.getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_normal_login);
            }else{
                tab.getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.GONE);
                tab.getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_person);
            }

        }
        //标签页可以滑动
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                GameLog.log("当前Tab的位置是："+pos);
                for (int i = 0; i < strTitleName.length; i++) {
//                  tab.setCustomView(R.layout.home_tab_item);//给每一个tab设置view
                    if (i == pos) {
                        // 设置第一个tab的TextView是被选择的样式
                        GameLog.log("设置当前Tab的位置是："+pos);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_normal_login);
                    }else{
                        GameLog.log("其他Tab的位置是："+i);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.GONE);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_person);
                    }

                }
                //ToastShow.show(MainActivity.this, "tab -pos=" + pos);
                if (!isScrolled) {
                    //滑动时不能点击,
                    //第一个参数是指定的位置，锚点
                    // 第二个参数表示 Item 移动到第一项后跟 RecyclerView 上边界或下边界之间的距离（默认是 0）
                    manager.scrollToPositionWithOffset(strTitleMarkup[pos], 0);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void init() {

        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new SimeAdapter(getContext(),R.layout.main_item,homeGameList));

//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                //Toast.makeText(MainActivity.this, "点击的item=" + position, Toast.LENGTH_SHORT).show();
////                switch (position) {
////                    case 0:
////
////                        ToastShow.show(MainActivity.this, "item---" + 0000);
////                        break;
////                    default:
////                        break;
////                }
//            }
//        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //重写该方法主要是判断recyclerview是否在滑动
                //0停止 ，12都是滑动
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }
                setMsg("isScrolled" + isScrolled + "--newState=" + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //这个主要是recyclerview滑动时让tab定位的方法
                int pos = 0;
                if (isScrolled) {
                    int top = manager.findFirstVisibleItemPosition();
                    int bottom = manager.findLastVisibleItemPosition();

                    if (top >= 12) {
                        //先判断滑到底部，tab定位到最后一个
                        pos = strTitleMarkup.length - 1;
                        GameLog.log("区间范围的时候"+pos);
                    } else if (bottom == homeGameList.size() - 1) {
                        //先判断滑到底部，tab定位到最后一个
                        pos = strTitleMarkup.length - 1;
                    } else if (top == strTitleMarkup[strTitleMarkup.length - 1]) {
                        //如果top等于指定的位置，对应到tab即可，
                        pos = strTitleMarkup[strTitleMarkup.length - 1];
                    } else {
                        //循环遍历，需要比较i+1的位置，所以循环长度要减1，
                        //  如果 i<top<i+1,  那么tab应该定位到i位置的字符，不管是向上还是向下滑动
                        for (int i = 0; i < strTitleMarkup.length - 1; i++) {
                            if (top == strTitleMarkup[i]) {
                                pos = i;
                                //GameLog.log("相等的时候："+pos);
                                break;
                            } else if (top > strTitleMarkup[i] && top < strTitleMarkup[i + 1]) {
                                pos = i;
                                //GameLog.log("区间范围的时候"+pos);
                                break;
                            }
                        }
                    }

                    if(pos>=5){
                        pos = 5;
                    }
                    //设置tab滑动到第pos个
                    tabLayout.setScrollPosition(pos, 0f, true);
                }
                GameLog.log("当前滑动的位置是："+pos);
                for (int i = 0; i < strTitleName.length; i++) {
//                  tab.setCustomView(R.layout.home_tab_item);//给每一个tab设置view
                    if (i == pos) {
                        // 设置第一个tab的TextView是被选择的样式
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setBackgroundResource(strTitleIcon[pos]);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_normal_login);
                    }else{
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setVisibility(View.GONE);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIcon).setBackgroundResource(strTitleIcon[i]);
                        tabLayout.getTabAt(i).getCustomView().findViewById(R.id.homeTabItemIconLay).setBackgroundResource(R.drawable.btn_person);
                    }

                }

            }
        });

    }

    public static void setMsg(String str) {
        GameLog.log("tab "+str);
    }

    private void initData() {

    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        initData();
        init();
        initTab();
        // EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
        tvHomePageLine.postDelayed(new Runnable() {
            @Override
            public void run() {
                String signSwitch  = ACache.get(getContext()).getAsString("signSwitch");
                if(!Check.isEmpty(signSwitch)&&"true".equals(signSwitch)){
                    homeSign.setVisibility(View.VISIBLE);
                }
                GameLog.log("签到活动说法："+signSwitch);
            }
        },5000);
        DomainUrl domainUrl = JSON.parseObject(ACache.get(getContext()).getAsString("homeLineChoice"), DomainUrl.class);
        if(!Check.isNull(domainUrl)&&domainUrl.getList().size()>0){
            int sizeq = domainUrl.getList().size();
            for(int k=0;k<sizeq;++k){
                if(domainUrl.getList().get(k).isChecked()){
                    tvHomePageLine.setText("线路"+domainUrl.getList().get(k).getPid());
                }
            }
        }

       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, OrientationHelper.VERTICAL,false);
        rvHomapageGameHall.setLayoutManager(gridLayoutManager);
        rvHomapageGameHall.setHasFixedSize(true);
        rvHomapageGameHall.setNestedScrollingEnabled(false);
        rvHomapageGameHall.setAdapter(new HomaPageGameAdapter(getContext(),R.layout.item_game_hall,homeGameList));
*/
        NoticeResult noticeResult = JSON.parseObject(ACache.get(getContext()).getAsString(HGConstant.USERNAME_HOME_NOTICE), NoticeResult.class);
        if(!Check.isNull(noticeResult)){
            List<String> stringList = new ArrayList<String>();
            int size =noticeResult.getData().size();
            for(int i=0;i<size;++i){
                stringList.add(noticeResult.getData().get(i).getNotice());
            }
            tvHomapageBulletin.setContentList(stringList);
        }
        if(!NetworkUtils.isConnected()){
            BannerResult bannerResult = JSON.parseObject(ACache.get(getContext()).getAsString(HGConstant.USERNAME_HOME_BANNER), BannerResult.class);
            initBanner(bannerResult);
            GameLog.log("无网络连接，请求到的是本地缓存。。。。。");
        }else{
            //presenter.postOnlineService("");
            if(!Check.isNull(presenter)){
                presenter.postBanner("");
                presenter.postNotice("");
            }
        }

    }

    private void initBanner(final BannerResult bannerResult){
        if (!Check.isNull(bannerResult)) {
            ArrayList<String> urlList = new ArrayList<String>();
            for(int k=0;k<bannerResult.getData().size();++k){
                urlList.add(bannerResult.getData().get(k).getImg_path());
            }
            banner_3d.initBanner(urlList, false)//开启3D画廊效果
                    .addPageMargin(0, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                    .addPoint(6)//添加指示器
                    .addStartTimer(5)//自动轮播5秒间隔
                    //.addPointBottom(7)
//                    .addRoundCorners(1)//圆角
                    .finishConfig()//这句必须加
                    .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                        @Override
                        public void onBannerClick(int position) {
                            //点击item
                            if(bannerResult.getData().get(position).getName().equals("promo")){
                                String userMoney = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_MONEY);
                                //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+"template/promo.php?tip=app"+ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_BANNER))));
                                EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+ ACache.get(getContext()).getAsString("login_must_tpl_name")+"promo.php?tip=app" + ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_BANNER))));
                            }
                            //showMessage("效果1点击"+position);
                        }
                    });

            //rollPagerViewManager = new RollPagerViewManager(rollpageview, bannerResult.getData());
            //rollPagerViewManager.testImagesLocal(null);
            //rollPagerViewManager.testImagesNet(null, null);
        }
    }


    class HomaPageGameAdapter extends AutoSizeRVAdapter<HomePageIcon> {
        private Context context;
        public HomaPageGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, final HomePageIcon data, final int position) {
            holder.setText(R.id.tv_item_game_name,data.getIconName());
            RoundCornerImageView roundCornerImageView =      (RoundCornerImageView) holder.getView(R.id.iv_item_game_icon);
            roundCornerImageView.onCornerAll(roundCornerImageView);
            holder.setBackgroundRes(R.id.iv_item_game_icon,data.getIconId());
            holder.setOnClickListener(R.id.ll_home_main_show, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!NetworkUtils.isConnected()){
                        showMessage("请检查您的网络！");
                        return;
                    }
                    onHomeGameItemClick(data.getId());
                }
            });
        }
    }


    class SimeAdapter extends AutoSizeRVAdapter<HomePageIcon> {
        private Context context;
        public SimeAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder helper, final HomePageIcon data, final int position) {
            helper.setBackgroundRes(R.id.id_main_item,data.getIconId());
        }
    }


    private void onHomeGameItemClick( int position){

        //showMessage("点击的位置是："+position);
        /*if(Check.isNull(checkUpgradeResult)){
            return;
        }*/
        if(position<7&&Check.isEmpty(userName)){
            //start(LoginFragment.newInstance());
            EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
            return;
        }
        switch (position){
            case 0:
                userState = "0";
                String sport_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_SPORT_MAINTAIN);
                if("1".equals(sport_url)){
                    presenter.postMaintain();
                }else{
                    EventBus.getDefault().post(new StartBrotherEvent(HandicapFragment.newInstance(userName,userMoney), SupportFragment.SINGLETASK));
                }
                break;
            case 1:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "1";
                String video_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_VIDEO_MAINTAIN);
                if("1".equals(video_url)){
                    presenter.postMaintain();
                }else {
                    EventBus.getDefault().post(new StartBrotherEvent(AGListFragment.newInstance(Arrays.asList(userName, userMoney, "live")), SupportFragment.SINGLETASK));
                }
                break;
            case 2:
                userState = "2";
                String cp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_URL);
                String cp_inform = ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_INFORM);
                String cp_token = ACache.get(getContext()).getAsString(HGConstant.APP_CP_COOKIE);
                if(Check.isEmpty(cp_url)||Check.isEmpty(cp_inform)||Check.isEmpty(cp_token)||Check.isNull(CPClient.getRetrofit())){
                    presenter.postCP();
                    showMessage("正在加载中，请稍后再试!");
                }else{
                    this.startActivity(new Intent(getContext(),CPListFragment.class));
                }

                //EventBus.getDefault().post(new StartBrotherEvent(CPListFragment.newInstance(Arrays.asList(userName,userMoney,"live")), SupportFragment.SINGLETASK));
                /*String cp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOTTERY_MAINTAIN);
                if("1".equals(cp_url)){
                    presenter.postMaintain();
                }else {
                    postCPGo();
                }*/
                 break;
            case 3:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "3";
                String qp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_HG_MAINTAIN);
                if("1".equals(qp_url)){
                    presenter.postMaintain();
                }else {
                    postHGQiPaiGo();
                }
                break;
            case 4:
                /*if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }*/
                userState = "4";
                String hg_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_KY_MAINTAIN);
                if("1".equals(hg_url)){
                    presenter.postMaintain();
                }else {
                    postQiPaiGo();
                }
                break;
            case 5:
                /*if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }*/
                userState = "6";
                String vg_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_VG_MAINTAIN);
                if("1".equals(vg_url)){
                    presenter.postMaintain();
                }else {
                    postVGQiPaiGo();
                }
                break;
            case 6:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "5";
                String game_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_GAME_MAINTAIN);
                if("1".equals(game_url)){
                    presenter.postMaintain();
                }else {
                    //EventBus.getDefault().post(new StartBrotherEvent(AGListFragment.newInstance(Arrays.asList(userName, userMoney, "game")), SupportFragment.SINGLETASK));
                    EventBus.getDefault().post(new StartBrotherEvent(DZGameFragment.newInstance( userMoney, "game"), SupportFragment.SINGLETASK));
                }
                break;
            case 9:
                //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney,checkUpgradeResult.getDiscount_activity())));
                EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+"template/promo.php?tip=app"+pro)));
                break;
            case 7:
                //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney,checkUpgradeResult.getNewcomer_guide())));
                EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+"agents_reg.php?tip=app")));
                break;
            case 8:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                postValidGiftGo();
                //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+"/template/help.php?tip=app")));
                break;
            case 10:
                //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney,checkUpgradeResult.getBusiness_agent())));
                EventBus.getDefault().post(new StartBrotherEvent(ContractFragment.newInstance(userMoney,
                        ACache.get(getContext()).getAsString(HGConstant.USERNAME_SERVICE_URL_QQ),
                        ACache.get(getContext()).getAsString(HGConstant.USERNAME_SERVICE_URL_WECHAT))));
                break;
            case 11:
                EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, Client.baseUrl()+"/template/help.php?tip=app")));
                //presenter.postNoticeList("");
                break;
            case 12:
                presenter.postNoticeList("");
                break;
            case 13:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                /*if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }*/
                userState = "7";
                String ly_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LY_MAINTAIN);
                if("1".equals(ly_url)){
                    presenter.postMaintain();
                }else {
                    postLYQiPaiGo();
                }
                break;
            case 14:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "8";
                String avia_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_AVIA_MAINTAIN);
                if("1".equals(avia_url)){
                    presenter.postMaintain();
                }else {
                    postAviaQiPaiGo();
                }
                break;
            case 15:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                //userState = "2";
                presenter.postBYGame("","6");
                break;
            case 16:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "9";
                presenter.postOGGame("","");
                break;
            case 17:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "10";
                presenter.postBBINGame("","");
                break;
        }

    }

    @Override
    public void onVisible() {
        super.onVisible();
        // EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));

    }


    @OnClick({R.id.tvHomePageLogin,R.id.tvHomePageLine,R.id.homeUserName,R.id.homeGoLogin,R.id.homeDeposit,R.id.homeDepositC,R.id.homeDwith,R.id.homeBank})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tvHomePageLogin:
            case R.id.homeGoLogin:
                //start(LoginFragment.newInstance());  启动一个新的Fragment 但是还是覆盖在以前的Fragemnet的基础上
                EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                break;
            case R.id.homeDeposit:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                break;
            case R.id.homeDepositC:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                break;
            case R.id.homeDwith:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                break;
            case R.id.homeBank:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                break;
            case R.id.tvHomePageLine:
                showPopMenuIn();
                break;
            case R.id.home_sign:
                if(Check.isEmpty(userName)){
                    //start(LoginFragment.newInstance());
                    EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                    return;
                }
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                SignTodayFragment.newInstance(userMoney,1).show(getFragmentManager());
                break;
        }

    }

    private void showPopMenuIn(){
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_line_choice,null);
        //处理popWindow 显示内容
        DomainUrl  domainUrl = JSON.parseObject(ACache.get(getContext()).getAsString("homeLineChoice"), DomainUrl.class);
        if(Check.isNull(domainUrl)){
            showMessage("已是最优线路！");
            return;
        }
        RecyclerView recyclerView = contentView.findViewById(R.id.popLineChoice);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1, OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new LineChoiceAdapter(getContext(),R.layout.pop_line_choice_item,domainUrl.getList()));
        mCustomPopWindowIn= new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .enableBackgroundDark(true)
                .create()
                .showAsDropDown(tvHomePageLine,0,0);
        //}
    }


    class LineChoiceAdapter extends AutoSizeRVAdapter<DomainUrl.ListBean> {
        private Context context;
        public LineChoiceAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, final DomainUrl.ListBean data, final int position) {
            holder.setText(R.id.popLineName,"线路"+data.getPid());
            if(data.isChecked()){
                holder.setBackgroundRes(R.id.popLineImg,R.mipmap.line_choice_cheack1);
            }else{
                holder.setBackgroundRes(R.id.popLineImg,R.mipmap.line_choice_cheack2);
            }
            holder.setOnClickListener(R.id.popLineName, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!NetworkUtils.isConnected()){
                        showMessage("请检查您的网络！");
                        return;
                    }
                    DomainUrl  domainUrl = JSON.parseObject(ACache.get(getContext()).getAsString("homeLineChoice"), DomainUrl.class);
                    int size = domainUrl.getList().size();
                    for(int k=0;k<size;++k){
                        if(domainUrl.getList().get(k).getUrl().equals(data.getUrl())){
                            domainUrl.getList().get(k).setChecked(true);
                        }else{
                            domainUrl.getList().get(k).setChecked(false);
                        }
                    }
                    ACache.get(getContext()).put("homeLineChoice", JSON.toJSONString(domainUrl));
                    ACache.get(getContext()).put("homeTYUrl", data.getUrl());
                    ACache.get(getContext()).put("homeCPUrl", data.getUrl());
                    //ACache.get(getContext()).put("app_demain_url", data.getUrl());
                    RetrofitUrlManager.getInstance().setGlobalDomain(data.getUrl());
                   /* Client.setClientDomain(data.getUrl());
                    HGApplication.instance().configClient();*/
                    /*CPClient.setClientDomain(data.getUrl().replace("m.","mc."));
                    HGApplication.instance().configCPClient();*/
                    tvHomePageLine.setText("线路"+data.getPid());
                    mCustomPopWindowIn.dissmiss();
                    //onHomeGameItemClick(data.getId());
                }
            });
        }
    }


    @Override
    public void postOnlineServiceResult(OnlineServiceResult onlineServiceResult) {

        GameLog.log("在线客服地址："+onlineServiceResult.getOnlineserver());
       // onStartOnlineService(onlineServiceResult.getOnlineserver());
        ACache.get(getContext()).put(HGConstant.USERNAME_SERVICE_URL,onlineServiceResult.getOnlineserver() );
    }

    @Override
    public void postBannerResult(BannerResult bannerResult) {
        GameLog.log("。。。。。Banner的数据返回。。。。。");
        ACache.get(getContext()).put(HGConstant.USERNAME_HOME_BANNER, JSON.toJSONString(bannerResult));
        initBanner(bannerResult);
    }

    @Override
    public void postNoticeResult(NoticeResult noticeResult) {
        GameLog.log("。。。。。公告的数据返回。。。。。");
        ACache.get(getContext()).put(HGConstant.USERNAME_HOME_NOTICE, JSON.toJSONString(noticeResult));
        List<String> stringList = new ArrayList<String>();
        int size =noticeResult.getData().size();
        for(int i=0;i<size;++i){
            stringList.add(noticeResult.getData().get(i).getNotice());
        }
        tvHomapageBulletin.setContentList(stringList);
    }

    @Override
    public void postNoticeListResult(NoticeResult noticeResult) {

        noticeResultList =noticeResult;
        EventBus.getDefault().post(new StartBrotherEvent(NoticeListFragment.newInstance(noticeResultList,"","")));
    }

    @Override
    public void postAGLiveCheckRegisterResult(CheckAgLiveResult checkAgLiveResult) {
        GameLog.log("AG视讯是否已经注册："+checkAgLiveResult.getIs_registered());
        if(!"1".equals(checkAgLiveResult.getIs_registered())){
            presenter.postAGGameRegisterAccount("","cga");
            GameLog.log("开始注册 AG视讯账号");
        }
    }


    @Override
    public void postAGGameRegisterAccountResult(AGCheckAcountResult agCheckAcountResult) {
        GameLog.log("AG创建账号："+agCheckAcountResult.toString());
    }

    private void initWebView(String url) {
        final WebView mWebView = new WebView(getContext());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                //rl_loading.setVisibility(View.VISIBLE); // 显示加载界面
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                GameLog.log("请求的URL地址 "+url);
                String title = view.getTitle();
                CookieSyncManager.createInstance(getContext());
                CookieManager cookieManager = CookieManager.getInstance();
                if (cookieManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.setAcceptThirdPartyCookies(mWebView, true);
                    }
                }
                String CookieStr = cookieManager.getCookie(url);
                if(!Check.isEmpty(CookieStr))
                ACache.get(getContext()).put(HGConstant.APP_CP_COOKIE,CookieStr);
                GameLog.log("cookie日志："+CookieStr);
                /*tv_topbar_title.setText(title);
                tv_topbar_title.setVisibility(View.VISIBLE);
                rl_loading.setVisibility(View.GONE); // 隐藏加载界面*/
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //rl_loading.setVisibility(View.GONE); // 隐藏加载界面
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
//        String cacheDirPath = getContext().getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
        String cacheDirPath = getContext().getCacheDir().getAbsolutePath() + HGConstant.APP_DB_DIRNAME;
        GameLog.log("cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);

    }

    @Override
    public void postQipaiResult(QipaiResult qipaiResult) {
        //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, qipaiResult.getUrl())));
        ACache.get(getContext()).put(HGConstant.USERNAME_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============ky棋牌的地址=============");
    }

    @Override
    public void postHGQipaiResult(QipaiResult qipaiResult) {
        //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, qipaiResult.getUrl())));
        ACache.get(getContext()).put(HGConstant.USERNAME_HG_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============皇冠棋牌的地址=============");
    }

    @Override
    public void postVGQipaiResult(QipaiResult qipaiResult) {
        //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, qipaiResult.getUrl())));
        ACache.get(getContext()).put(HGConstant.USERNAME_VG_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============VG棋牌的地址=============");
    }

    @Override
    public void postLYQipaiResult(QipaiResult qipaiResult) {
        ACache.get(getContext()).put(HGConstant.USERNAME_LY_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============LY棋牌的地址=============");
    }

    @Override
    public void postAviaQiPaiResult(QipaiResult qipaiResult) {
        ACache.get(getContext()).put(HGConstant.USERNAME_AVIA_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============泛亚棋牌的地址=============");
    }

    @Override
    public void postOGResult(AGGameLoginResult qipaiResult) {
        GameLog.log("OG的返回数据："+qipaiResult.getUrl());
        //EventBus.getDefault().post(new StartBrotherEvent(XPlayGameFragment.newInstance(dzTitileName,agGameLoginResult.getUrl(),"1"), SupportFragment.SINGLETASK));
        /*Intent intent = new Intent(getContext(),XPlayGameActivity.class);
        intent.putExtra("url",qipaiResult.getUrl());
        intent.putExtra("gameCnName","OG视讯");
        intent.putExtra("hidetitlebar",false);
        getActivity().startActivity(intent);*/
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(qipaiResult.getUrl()));
        startActivity(intent);
        //ACache.get(getContext()).put(HGConstant.USERNAME_OG_QIPAI_URL,qipaiResult.getUrl());
        GameLog.log("=============OG的地址=============");
    }

    @Override
    public void postCPResult(CPResult cpResult) {
        //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, cpResult.getCpUrl())));
        CPClient.setClientDomain(cpResult.getCpUrl());
        HGApplication.instance().configCPClient();
        ACache.get(getContext()).put("homeTYUrl", cpResult.getCpUrl().replace("mc.","m."));
        ACache.get(getContext()).put("homeCPUrl", cpResult.getCpUrl());
        ACache.get(getContext()).put(HGConstant.USERNAME_CP_URL,cpResult.getCpUrl());//+"?tip=app"
        ACache.get(getContext()).put(HGConstant.USERNAME_CP_INFORM,cpResult.getUrlLogin());
        initWebView(cpResult.getUrlLogin());
    }

    @Override
    public void postValidGiftResult(ValidResult validResult) {
        GameLog.log("=============红包的地址是否正常=============");
        //EventBus.getDefault().post(new StartBrotherEvent(SignTodayFragment.newInstance(null,userMoney,1)));
        ACache.get(getContext()).put(HGConstant.USERNAME_GIFT_URL,"true");
    }

    @Override
    public void postValidGift2Result(ValidResult validResult) {
        GameLog.log("=============红包的地址是否正常=============");
        EventBus.getDefault().post(new StartBrotherEvent(EventsFragment.newInstance(null,userMoney,1)));
        //ACache.get(getContext()).put(HGConstant.USERNAME_GIFT_URL,"true");
    }

    @Override
    public void postMaintainResult(List<MaintainResult> maintainResult) {
        GameLog.log("=============维护日志============="+maintainResult);
       for(MaintainResult maintainResult1:maintainResult){
           switch (maintainResult1.getType()){
               case "sport":
                   GameLog.log("sport "+maintainResult1.getState());
                   if(userState.equals("0")){
                       showMessage(maintainResult1.getContent());
                   }
                   ACache.get(getContext()).put(HGConstant.USERNAME_SPORT_MAINTAIN,maintainResult1.getState());
                   break;
               case "video":
                   if(userState.equals("1")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("video "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_VIDEO_MAINTAIN,maintainResult1.getState());
                   break;
               case "game":
                   if(userState.equals("5")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("game "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_GAME_MAINTAIN,maintainResult1.getState());
                   break;
               case "lottery":
                   if(userState.equals("2")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("lottery "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_LOTTERY_MAINTAIN,maintainResult1.getState());
                   break;
               case "ky":
                   if(userState.equals("4")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("ky "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_KY_MAINTAIN,maintainResult1.getState());
                   break;
               case "hgqp":
                   if(userState.equals("3")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("hg "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_HG_MAINTAIN,maintainResult1.getState());
                   break;
               case "vgqp":
                   if(userState.equals("6")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("vg "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_VG_MAINTAIN,maintainResult1.getState());
                   break;
               case "ly":
                   if(userState.equals("7")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("ly "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_LY_MAINTAIN,maintainResult1.getState());
                   break;
               case "avia":
                   if(userState.equals("8")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("avia "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_AVIA_MAINTAIN,maintainResult1.getState());
                   break;
               case "og":
                   if(userState.equals("9")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("og "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_AVIA_MAINTAIN+"og",maintainResult1.getState());
                   break;
               case "bbin":
                   if(userState.equals("10")){
                       showMessage(maintainResult1.getContent());
                   }
                   GameLog.log("bbin "+maintainResult1.getState());
                   ACache.get(getContext()).put(HGConstant.USERNAME_AVIA_MAINTAIN+"bbin",maintainResult1.getState());
                   break;
           }
       }

    }

    @Override
    public void postGoPlayGameResult(AGGameLoginResult agGameLoginResult) {
        GameLog.log("AG捕鱼的返回数据："+agGameLoginResult.getUrl());
        //EventBus.getDefault().post(new StartBrotherEvent(XPlayGameFragment.newInstance(dzTitileName,agGameLoginResult.getUrl(),"1"), SupportFragment.SINGLETASK));
        Intent intent = new Intent(getContext(),XPlayGameActivity.class);
        intent.putExtra("url",agGameLoginResult.getUrl());
        intent.putExtra("gameCnName","AG捕鱼");
        intent.putExtra("hidetitlebar",false);
        getActivity().startActivity(intent);
    }

    private void postValidGiftGo(){
        String gift_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL);
        if(Check.isEmpty(gift_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postValidGift2("","get_valid");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/else {
            EventBus.getDefault().post(new StartBrotherEvent(EventsFragment.newInstance(null,userMoney,1)));
        }
    }
    private void postQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_QIPAI_URL);
        if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
            qipai_url = ACache.get(getContext()).getAsString(HGConstant.KY_DEMO_URL);
        }
        if(Check.isEmpty(qipai_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postQipai("","");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/else {
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",qipai_url);
            intent.putExtra("gameCnName","开元棋牌");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }

    }

    private void postHGQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_HG_QIPAI_URL);
        if (Check.isEmpty(qipai_url)) {
            showMessage("正在加载中，请稍后再试!");
            presenter.postHGQipai("", "");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/ else {
            Intent intent = new Intent(getContext(), XPlayGameActivity.class);
            intent.putExtra("url", qipai_url);
            intent.putExtra("gameCnName", "皇冠棋牌");
            intent.putExtra("hidetitlebar", false);
            getActivity().startActivity(intent);
        }

    }

    private void postVGQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_VG_QIPAI_URL);
        if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
            qipai_url = ACache.get(getContext()).getAsString(HGConstant.VG_DEMO_URL);
            //qipai_url = qipai_url+"&flag=test";
        }
        if(Check.isEmpty(qipai_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postVGQipai("","");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/else {
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",qipai_url);
            intent.putExtra("gameCnName","VG棋牌");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }

    }

    private void postLYQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LY_QIPAI_URL);
        if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
            qipai_url = ACache.get(getContext()).getAsString(HGConstant.LY_DEMO_URL);
        }
        if(Check.isEmpty(qipai_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postLYQipai("","");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/else {
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",qipai_url);
            intent.putExtra("gameCnName","乐游棋牌");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }

    }

    private void postAviaQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_AVIA_QIPAI_URL);
        if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
            qipai_url = ACache.get(getContext()).getAsString(HGConstant.AV_DEMO_URL);
        }
        if(Check.isEmpty(qipai_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postAviaQiPai("","");
        }else {
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",qipai_url);
            intent.putExtra("gameCnName","电子竞技");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }

    }


    private void postCPGo(){
        String cp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_URL);
        if(Check.isEmpty(cp_url)){
            presenter.postCP();
        }else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.APP_CP_COOKIE))){
			presenter.postCP();
            showMessage("正在加载中，请稍后再试!");
        }else{
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",cp_url);
            intent.putExtra("gameCnName","彩票游戏");
            intent.putExtra("gameType","CP");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }
        /*Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_URL));
        intent.setData(content_url);
        startActivity(intent);*/
    }

    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

   /* @Subscribe
    public void onEventMain(CheckUpgradeResult checkUpgradeResult){

        this.checkUpgradeResult = checkUpgradeResult;
        ACache.get(getContext()).put(HGConstant.USERNAME_SERVICE_URL,checkUpgradeResult.getService_meiqia() );
        ACache.get(getContext()).put(HGConstant.USERNAME_SERVICE_URL_QQ,checkUpgradeResult.getService_qq() );
        ACache.get(getContext()).put(HGConstant.USERNAME_SERVICE_URL_WECHAT,checkUpgradeResult.getService_wechat() );
    }*/

    @Subscribe
    public void onEventMain(UserMoneyEvent userMoneyEvent){
        userMoney = userMoneyEvent.money;
        tvHomePageUserMoney.setText(userMoney);
        homeMoney.setText(userMoney);
		ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, userMoney);
    }

    @Subscribe
    public void onEventMain(LoginResult loginResult) {
        if(!Check.isEmpty(loginResult.getNoteMessage())){
            EventShowDialog.newInstance(loginResult.getNoteMessage(),"").show(getFragmentManager());
        }
        GameLog.log("首页获取的用户余额："+loginResult.getMoney());
        userName = loginResult.getUserName();
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_USERNAME, userName);
        pro = "&Oid="+loginResult.getOid()+"&userid="+loginResult.getUserid()+"&UserName="+loginResult.getUserName()+"&Agents="+loginResult.getAgents();
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_BANNER, pro);
        if(!Check.isEmpty(loginResult.getMoney())){
            ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, loginResult.getMoney());
            tvHomePageUserMoney.setVisibility(View.VISIBLE);
            userMoney = GameShipHelper.formatMoney(loginResult.getMoney());
            tvHomePageUserMoney.setText(userMoney);
            homeMoney.setText(userMoney);
            homeUserName.setText(userName);
            homeGoLogin.setVisibility(View.GONE);
            homeLoginAl.setVisibility(View.VISIBLE);
            tvHomePageLogin.setVisibility(View.GONE);
        }
        //presenter.postAGLiveCheckRegister("");
        presenter.postValidGift("","get_valid");
        presenter.postMaintain();
        presenter.postCP();
        presenter.postQipai("","");
        presenter.postHGQipai("","");
        presenter.postVGQipai("","");
        presenter.postLYQipai("","");
        presenter.postAviaQiPai("","");
        //presenter.postOGGame("","");

    }

    @Subscribe
    public void onEventMain(LogoutEvent logoutEvent) {

        GameLog.log("首页用户退出了");
//        if(Check.isEmpty(loginResult.getMoney())){
//            tvHomePageUserName.setText(loginResult.getMoney());
//
//        }
        pro ="";
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_BANNER, pro);
        tvHomePageLogin.setVisibility(View.VISIBLE);
        homeLoginAl.setVisibility(View.GONE);
        tvHomePageUserMoney.setVisibility(View.GONE);
        homeUserName.setText("欢迎您，亲爱的用户");
        homeGoLogin.setVisibility(View.VISIBLE);
        userName = "";
        userMoney = "";
        userState = "19";
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_USERNAME, userName);
		ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, userMoney);
    }

}
