package com.youjie.cfcpnew.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youjie.cfcpnew.BuildConfig;
import com.youjie.cfcpnew.R;
import com.youjie.cfcpnew.http.Constant;
import com.youjie.cfcpnew.model.SpareBean;
import com.youjie.cfcpnew.model.UrlBean;
import com.youjie.cfcpnew.rxbus.EventMsg;
import com.youjie.cfcpnew.rxbus.RxBus;
import com.youjie.cfcpnew.utils.ACache;
import com.youjie.cfcpnew.utils.AlertDialog;
import com.youjie.cfcpnew.utils.AppToast;
import com.youjie.cfcpnew.utils.FloatBall;
import com.youjie.cfcpnew.view.floatingball.FloatBallManager;
import com.youjie.cfcpnew.view.floatingball.menu.MenuItem;
import com.youjie.cfcpnew.view.floatingball.utils.BackGroudSeletor;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by Colin on 2017/12/18.
 * 聊天室页面
 */
public class WebFragmentChart extends Fragment {

    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.load_view)
    SpinKitView loadView;

    private String webViewUrl;
    private String rewebUrl;
    private boolean urlCount = false;
    private boolean urlNet = false;

    protected Activity mActivity;
    protected AgentWeb mAgentWeb;
    private WebView mWebView;
    private AlertDialog mDialog;
    private FloatBallManager mFloatballManagerChart;

    public static WebFragmentChart newInstance(String webViewUrl) {
        WebFragmentChart fragment = new WebFragmentChart();
        Bundle args = new Bundle();
        args.putString(Constant.WEBVIEW_URL, webViewUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFloatballManagerChart = FloatBall.initSinglePageFloatball(mActivity);
        addFloatMenuItem();
//        mFloatballManagerChart.show();
        if (getArguments() != null) {
            this.webViewUrl = getArguments().getString(Constant.WEBVIEW_URL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initView() {
        mAgentWeb = AgentWeb.with(mActivity)
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(mWebViewClient)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .interceptUnkownUrl()
                .setMainFrameErrorView(R.layout.error_page, R.id.iv_error)
                .createAgentWeb()
                .ready()
                .go(webViewUrl);

        mWebView = mAgentWeb.getWebCreator().getWebView();
        mWebView.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (!TextUtils.isEmpty(mWebView.getUrl())) {
                        return !mWebView.getUrl().equals(webViewUrl) && !urlCount && !urlNet && mAgentWeb.handleKeyEvent(keyCode, keyEvent);
                    }
                }
            }
            return false;
        });

        mWebView.setOnTouchListener((v, event) -> {
            mWebView.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mWebView.requestDisallowInterceptTouchEvent(true);
                    mWebView.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return false;
        });
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (loadView.getVisibility() != View.VISIBLE) {
                loadView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            urlCount = url.equals(rewebUrl);
            super.onPageFinished(view, url);
            loadView.setVisibility(View.GONE);
        }
    };

    private void getReloadURL() {
        AppToast.showShortText(mActivity, R.string.changeSparelineChart);
        OkGo.<String>post(ACache.get(getActivity()).getAsString(Constant.APP_URL) + BuildConfig.FLAVOR)
                .tag(this)
                .params(Constant.SPARE_LINE, Constant.SPARE_LINE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        SpareBean spareBean = new Gson().fromJson(response.body(), SpareBean.class);
                        rewebUrl = spareBean.spareLineChart;
                        mWebView.loadUrl(rewebUrl);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        //联网重载
        RxBus.getInstance().toObservable().map(o -> (EventMsg) o).subscribe(eventMsg -> {
            if (eventMsg != null) {
                if (eventMsg.getMsg().equals("重连")) {
                    //请求chartRoom在线投注线路
                    OkGo.<String>post(ACache.get(getActivity()).getAsString(Constant.APP_URL) + BuildConfig.FLAVOR)
                            .tag(this)
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Response<String> response) {
                                    super.onError(response);
                                }

                                @SuppressLint("CheckResult")
                                @Override
                                public void onSuccess(Response<String> response) {
                                    webViewUrl = new Gson().fromJson(response.body(), UrlBean.class).chartRoom;
                                    urlNet = true;
                                    mWebView.loadUrl(webViewUrl);
                                }
                            });
                }
            }
        });
    }

    private void addFloatMenuItem() {
        MenuItem personItem = new MenuItem(BackGroudSeletor.getdrawble("ic_switch", mActivity)) {
            @Override
            public void action() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder
                        .setNegativeButton(R.string.lineOne, (dialog, which) -> {
                            AppToast.showShortText(mActivity, R.string.changeMainline);
                            mDialog.dismiss();
                            mWebView.reload();
                        })
                        .setPositiveButton(R.string.lineTwo, (dialog, which) -> {
                            mDialog.dismiss();
                            getReloadURL();
                        });
                mDialog = builder.create();
                mDialog.show();
                mFloatballManagerChart.closeMenu();
            }
        };
        MenuItem walletItem = new MenuItem(BackGroudSeletor.getdrawble("ic_clear", mActivity)) {
            @Override
            public void action() {
                mAgentWeb.clearWebCache();
                AppToast.showShortText(mActivity, R.string.cleanCache);
                mFloatballManagerChart.closeMenu();
            }
        };
        MenuItem settingItem = new MenuItem(BackGroudSeletor.getdrawble("ic_back", mActivity)) {
            @Override
            public void action() {
                if (!urlCount) {
                    mWebView.goBack();
                }
                AppToast.showShortText(mActivity, R.string.back);
                mFloatballManagerChart.closeMenu();
            }
        };
        MenuItem RefreshItem = new MenuItem(BackGroudSeletor.getdrawble("ic_refresh", mActivity)) {
            @Override
            public void action() {
                mAgentWeb.getWebCreator().getWebView().reload();
                AppToast.showShortText(mActivity, R.string.refresh);
                mFloatballManagerChart.closeMenu();
            }
        };
        mFloatballManagerChart
                .addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)
                .addMenuItem(RefreshItem)
                .buildMenu();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mFloatballManagerChart != null) {
            if (isVisibleToUser) {
                mFloatballManagerChart.show();
                JAnalyticsInterface.onPageStart(mActivity, this.getClass().getCanonicalName());
            } else {
                mFloatballManagerChart.hide();
                JAnalyticsInterface.onPageEnd(mActivity, this.getClass().getCanonicalName());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }
}
