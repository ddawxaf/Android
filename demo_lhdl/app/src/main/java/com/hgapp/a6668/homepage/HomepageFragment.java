package com.hgapp.a6668.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.hgapp.a6668.common.widgets.MarqueeTextView;
import com.hgapp.a6668.common.widgets.RoundCornerImageView;
import com.hgapp.a6668.data.AGCheckAcountResult;
import com.hgapp.a6668.data.BannerResult;
import com.hgapp.a6668.data.CPResult;
import com.hgapp.a6668.data.CheckAgLiveResult;
import com.hgapp.a6668.data.LoginResult;
import com.hgapp.a6668.data.MaintainResult;
import com.hgapp.a6668.data.NoticeResult;
import com.hgapp.a6668.data.OnlineServiceResult;
import com.hgapp.a6668.data.QipaiResult;
import com.hgapp.a6668.data.ValidResult;
import com.hgapp.a6668.homepage.aglist.AGListFragment;
import com.hgapp.a6668.homepage.aglist.playgame.XPlayGameActivity;
import com.hgapp.a6668.homepage.cplist.CPListFragment;
import com.hgapp.a6668.homepage.events.EventsFragment;
import com.hgapp.a6668.homepage.handicap.HandicapFragment;
import com.hgapp.a6668.homepage.noticelist.NoticeListFragment;
import com.hgapp.a6668.homepage.online.ContractFragment;
import com.hgapp.a6668.homepage.online.OnlineFragment;
import com.hgapp.a6668.launcher.MyHttpClient;
import com.hgapp.a6668.login.fastlogin.LoginFragment;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.hgapp.common.util.NetworkUtils;
import com.jude.rollviewpager.RollPagerView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 AG真人，老虎机，皇冠体育，彩票，优惠活动，联系我们，公告，代理加盟，新手教学（暂时先不做）

 AG真人，老虎机，皇冠体育，彩票，优惠活动，联系我们，公告，代理加盟，新手教学
 */
public class HomepageFragment extends HGBaseFragment implements HomePageContract.View{

    @BindView(R.id.tvHomePageLogin)
    TextView tvHomePageLogin;
    @BindView(R.id.tvHomePageUserMoney)
    TextView tvHomePageUserMoney;
    @BindView(R.id.rollpageview)
    RollPagerView rollpageview;
    @BindView(R.id.tv_homapage_bulletin)
    MarqueeTextView tvHomapageBulletin;
    @BindView(R.id.rv_homepage_game_hall)
    RecyclerView rvHomapageGameHall;


    private static List<HomePageIcon> homeGameList = new ArrayList<HomePageIcon>();

    HomePageContract.Presenter presenter;

    private RollPagerViewManager rollPagerViewManager;

    private NoticeResult noticeResultList;

    private String userName ="";
    private  String pro =  "";
    private String userMoney = "";
    private String userState = "9";
    //private CheckUpgradeResult checkUpgradeResult;
    static {
        homeGameList.add(new HomePageIcon("体育投注",R.mipmap.home_hgty));
        homeGameList.add(new HomePageIcon("真人视讯",R.mipmap.home_ag));
        homeGameList.add(new HomePageIcon("彩票游戏",R.mipmap.home_vrcp));
        homeGameList.add(new HomePageIcon("皇冠棋牌",R.mipmap.home_hg_qipai));
        homeGameList.add(new HomePageIcon("开元棋牌",R.mipmap.home_qipai));
        homeGameList.add(new HomePageIcon("VG棋牌",R.mipmap.home_vg));
        homeGameList.add(new HomePageIcon("电子游艺",R.mipmap.home_lhj));
//        homeGameList.add(new HomePageIcon("欧博真人",R.mipmap.home_obzr));
//        homeGameList.add(new HomePageIcon("沙巴体育",R.mipmap.home_sbty));
//        homeGameList.add(new HomePageIcon("BBIN",R.mipmap.home_bbin));
//        homeGameList.add(new HomePageIcon("开元棋牌",R.mipmap.home_kyqp));
//        homeGameList.add(new HomePageIcon("扑鱼王二代",R.mipmap.home_fish));
//        homeGameList.add(new HomePageIcon("甜心扑克王",R.mipmap.home_honey));
//        homeGameList.add(new HomePageIcon("抢红包",R.mipmap.home_red));
        homeGameList.add(new HomePageIcon("代理加盟",R.mipmap.home_agent));
        homeGameList.add(new HomePageIcon("幸运红包",R.mipmap.home_red));
        homeGameList.add(new HomePageIcon("优惠活动",R.mipmap.home_pro));
        homeGameList.add(new HomePageIcon("联系我们",R.mipmap.home_contact));
        homeGameList.add(new HomePageIcon("新手教学",R.mipmap.home_new));
        homeGameList.add(new HomePageIcon("皇冠公告",R.mipmap.home_remind));
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

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        // EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, OrientationHelper.VERTICAL,false);
        rvHomapageGameHall.setLayoutManager(gridLayoutManager);
        rvHomapageGameHall.setHasFixedSize(true);
        rvHomapageGameHall.setNestedScrollingEnabled(false);
        rvHomapageGameHall.setAdapter(new HomaPageGameAdapter(getContext(),R.layout.item_game_hall,homeGameList));
        BannerResult bannerResult = JSON.parseObject(ACache.get(getContext()).getAsString(HGConstant.USERNAME_HOME_BANNER), BannerResult.class);
        if(!Check.isNull(bannerResult)){
            rollPagerViewManager  = new RollPagerViewManager(rollpageview, bannerResult.getData());
            //rollPagerViewManager.testImagesLocal(null);
            rollPagerViewManager.testImagesNet(null,null);
        }
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
            GameLog.log("无网络连接，请求到的是本地缓存。。。。。");
        }else{
            //presenter.postOnlineService("");
            if(!Check.isNull(presenter)){
                presenter.postBanner("");
                presenter.postNotice("");
            }
        }

    }

    class HomaPageGameAdapter extends AutoSizeRVAdapter<HomePageIcon> {
        private Context context;
        public HomaPageGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, HomePageIcon data, final int position) {
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
                    onHomeGameItemClick(position);
                }
            });
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
                /*String cp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_URL);
                String cp_inform = ACache.get(getContext()).getAsString(HGConstant.USERNAME_CP_INFORM);
                if(Check.isEmpty(cp_url)||Check.isEmpty(cp_url)){
                    presenter.postCP();
                    showMessage("正在加载中，请稍后再试!");
                }else{
                    getActivity().startActivity(new Intent(getContext(),CPListFragment.class));
                }*/
                //ACache.get(getContext()).put(HGConstant.APP_CP_COOKIE_AVIABLE,"true");

                //EventBus.getDefault().post(new StartBrotherEvent(CPListFragment.newInstance(Arrays.asList(userName,userMoney,"live")), SupportFragment.SINGLETASK));
                String cp_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOTTERY_MAINTAIN);
                if("1".equals(cp_url)){
                    presenter.postMaintain();
                }else {
                    postCPGo();
                }
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
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                userState = "4";
                String hg_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_KY_MAINTAIN);
                if("1".equals(hg_url)){
                    presenter.postMaintain();
                }else {
                    postQiPaiGo();
                }
                break;
            case 5:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
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
                    EventBus.getDefault().post(new StartBrotherEvent(AGListFragment.newInstance(Arrays.asList(userName, userMoney, "game")), SupportFragment.SINGLETASK));
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
        }
    }

    @Override
    public void onVisible() {
        super.onVisible();
        // EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));

    }


    @OnClick({R.id.tvHomePageLogin})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tvHomePageLogin:
                //start(LoginFragment.newInstance());  启动一个新的Fragment 但是还是覆盖在以前的Fragemnet的基础上
                EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
                break;
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
        rollPagerViewManager  = new RollPagerViewManager(rollpageview, bannerResult.getData());
        //rollPagerViewManager.testImagesLocal(null);
        rollPagerViewManager.testImagesNet(null,null);
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
    public void postCPResult(CPResult cpResult) {
        //EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(userMoney, cpResult.getCpUrl())));
        ACache.get(getContext()).put(HGConstant.USERNAME_CP_URL,cpResult.getCpUrl());//+"?tip=app"
        CPClient.setClientDomain(cpResult.getCpUrl());
        HGApplication.instance().configCPClient();
        ACache.get(getContext()).put(HGConstant.USERNAME_CP_INFORM,cpResult.getUrlLogin());
        initWebView(cpResult.getUrlLogin());
    }

    @Override
    public void postValidGiftResult(ValidResult validResult) {
        GameLog.log("=============红包的地址是否正常=============");
        //EventBus.getDefault().post(new StartBrotherEvent(EventsFragment.newInstance(null,userMoney,1)));
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
        GameLog.log("=============维护日志=============");
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
           }
       }

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
        if(Check.isEmpty(qipai_url)){
            showMessage("正在加载中，请稍后再试!");
            presenter.postHGQipai("","");
        }/*else if(Check.isEmpty(ACache.get(getContext()).getAsString(HGConstant.USERNAME_GIFT_URL))){
            showMessage("正在加载中，请稍后再试!");
        }*/else {
            Intent intent = new Intent(getContext(),XPlayGameActivity.class);
            intent.putExtra("url",qipai_url);
            intent.putExtra("gameCnName","皇冠棋牌");
            intent.putExtra("hidetitlebar",false);
            getActivity().startActivity(intent);
        }

    }

    private void postVGQiPaiGo(){
        String qipai_url = ACache.get(getContext()).getAsString(HGConstant.USERNAME_VG_QIPAI_URL);
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
		ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, userMoney);
    }

    @Subscribe
    public void onEventMain(LoginResult loginResult) {
        GameLog.log("首页获取的用户余额："+loginResult.getMoney());
        userName = loginResult.getUserName();
        pro = "&Oid="+loginResult.getOid()+"&userid="+loginResult.getUserid()+"&UserName="+loginResult.getUserName()+"&Agents="+loginResult.getAgents();
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_BANNER, pro);
        if(!Check.isEmpty(loginResult.getMoney())){
            ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, loginResult.getMoney());
            tvHomePageUserMoney.setVisibility(View.VISIBLE);
            userMoney = GameShipHelper.formatMoney(loginResult.getMoney());
            tvHomePageUserMoney.setText(userMoney);
            tvHomePageLogin.setVisibility(View.GONE);
        }
        //presenter.postAGLiveCheckRegister("");
        presenter.postValidGift("","get_valid");
        presenter.postMaintain();
        presenter.postCP();
        presenter.postQipai("","");
        presenter.postHGQipai("","");
        presenter.postVGQipai("","");
        ACache.get(getContext()).put(HGConstant.APP_CP_COOKIE_AVIABLE,"false");
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
        tvHomePageUserMoney.setVisibility(View.GONE);
        userName = "";
        userMoney = "";
        userState = "9";
		ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_MONEY, userMoney);
        ACache.get(getContext()).put(HGConstant.APP_CP_COOKIE_AVIABLE,"false");
    }

}
