package com.hfcp.hf.ui.home.playgame;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.hfcp.hf.CFConstant;
import com.hfcp.hf.R;
import com.hfcp.hf.common.http.PNThreadFactory;
import com.hfcp.hf.common.utils.ACache;
import com.hfcp.hf.common.utils.Check;
import com.hfcp.hf.common.utils.GameLog;
import com.hfcp.hf.common.utils.ToastUtils;
import com.hfcp.hf.common.utils.Utils;
import com.hfcp.hf.common.widget.NTitleBar;
import com.hfcp.hf.ui.home.CloseGameViewEvent;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by daniel on 2018/10/20.
 * 嵌套游戏 X5内核适配
 */

public class XPlayGameActivity extends Activity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    @BindView(R.id.pay_x5_game_title)
    NTitleBar payGameTitle;
    @BindView(R.id.flayout_xpay)
    FrameLayout flayoutXpay;
    @BindView(R.id.wv_pay_x5_game)
    WebView wvPayGame;

    private int gameFull = 0;
    // TODO: Rename and change types of parameters
    //接收webview的参数传参
    private String mParam1;
    private String mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.fragment_pay_x5_game);
        ButterKnife.bind(this);
        setEvents(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String gameCnName = getIntent().getStringExtra("gameCnName");
        boolean hidetitlebar = getIntent().getBooleanExtra("hidetitlebar",false);
        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE){
            GameLog.log("SDK_INT upper ORIENTATION_LANDSCAPE");
            payGameTitle.setVisibility(View.GONE);
        }
        else if(hidetitlebar)
        {
            payGameTitle.setVisibility(View.GONE);
        }
        else{
            payGameTitle.setVisibility(View.VISIBLE);
            GameLog.log("SDK_INT upper ......");
            /*if(gameType.equals("shaba")){
                payGameTitle.setVisibility(View.GONE);
            }else{
                payGameTitle.setVisibility(View.VISIBLE);
            }*/
            //payGameTitle.setTitle(gameCnName);
            payGameTitle.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void setEvents(@Nullable Bundle savedInstanceState) {

        PTIWebSetting.init(wvPayGame);
        String gameurl = getIntent().getStringExtra("url");
        String type = getIntent().getStringExtra("type");
        String gameType = getIntent().getStringExtra("gameType");
        String uuid = getIntent().getStringExtra("uuid");
        String gameCnName = getIntent().getStringExtra("gameCnName");
        boolean hidetitlebar = getIntent().getBooleanExtra("hidetitlebar",false);
        payGameTitle.setTitle(gameCnName);
        payGameTitle.setMoreImage(null);
        payGameTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameLog.log("设置全屏。。。。");
                if(gameFull==0){
                    gameFull = 1;
                    payGameTitle.setMoreText("正常");
                    payGameTitle.setVisibility(View.GONE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }else{
                    gameFull = 0;
                    payGameTitle.setMoreText("全屏");
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        });
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            GameLog.log("SDK_INT upper ORIENTATION_LANDSCAPE");
            payGameTitle.setVisibility(View.GONE);
        }
        else if(hidetitlebar)
        {
            payGameTitle.setVisibility(View.GONE);
        }
        else{
            payGameTitle.setVisibility(View.VISIBLE);
            GameLog.log("SDK_INT upper ......"+gameurl);
            /*if(gameType.equals("shaba")){
                payGameTitle.setVisibility(View.GONE);
            }else{
                payGameTitle.setVisibility(View.VISIBLE);
            }*/
           // payGameTitle.setTitle(gameCnName);
            payGameTitle.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        wvPayGame.addJavascriptInterface(new JsInterface(this),"AndroidWebView");
        //设置可以支持缩放
        wvPayGame.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        wvPayGame.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        wvPayGame.getSettings().setBuiltInZoomControls(true);

        wvPayGame.setWebChromeClient(new WebChromeClient(){

            IX5WebChromeClient.CustomViewCallback customViewCallback;

            @Override
            public void onHideCustomView() {
                if(!Check.isNull(customViewCallback)){
                    customViewCallback.onCustomViewHidden();
                }
                wvPayGame.setVisibility(View.VISIBLE);
                flayoutXpay.removeAllViews();
                flayoutXpay.setVisibility(View.GONE);
                super.onHideCustomView();
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                wvPayGame.setVisibility(View.GONE);
                this.customViewCallback = customViewCallback;
                flayoutXpay.removeAllViews();
                flayoutXpay.setVisibility(View.VISIBLE);
                flayoutXpay.addView(view);
                super.onShowCustomView(view, customViewCallback);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

        });
        wvPayGame.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {
               // super.onPageFinished(view, url);
                CookieSyncManager.createInstance(XPlayGameActivity.this);
                android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
                /*if (cookieManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.setAcceptThirdPartyCookies(wvPayGame, true);
                    }
                }*/
                /*view.loadUrl("javascript:window.AndroidWebView.showSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");*/
                String CookieStr = cookieManager.getCookie(url);
                GameLog.log(CookieStr);
                //ACache.get(XPlayGameActivity.this).put(HGConstant.APP_CP_COOKIE,CookieStr);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        if(!Check.isEmpty(gameType)){
            if("CP".equals(gameType)){//PT的用bobo的html格式加载
                //wvPayGame.loadUrl("file:///android_asset/androidjs.html");
                HashMap<String,String> headers=new HashMap<String,String>();
                headers.put("Connection","keep-alive");
                headers.put("Content-Type","text/html; charset=utf-8");
                String cookie = ACache.get(this).getAsString(CFConstant.APP_CP_COOKIE);
                GameLog.log("设置cookie "+cookie);
                headers.put("Set-Cookie",cookie);
                wvPayGame.loadUrl(getIntent().getStringExtra("url"),headers);
                return;
                //synCookieToWebviewopus(gameurl,"S="+uuid);
                //onOPUS();
            }else if("shaba".equals(gameType)){
                synCookieToWebviewshaba(gameurl,"st="+uuid);
            }
        }
        if("post".equals(type)){
            wvPayGame.postUrl(getIntent().getStringExtra("url"),null);
        }else{
            wvPayGame.loadUrl(getIntent().getStringExtra("url"));
        }
    }


    /**
     * 同步cookie到webview
     * @param httpUrl
     * @param cookie
     */
    private  void synCookieToWebviewshaba(String httpUrl, String  cookie)
    {
        if(Check.isNull(httpUrl) || Check.isNull(cookie))
        {
            GameLog.log("synCookieToWebview cookie can't is null ");
            return;
        }
        CookieSyncManager.createInstance(Utils.getContext());
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(httpUrl,cookie);
        String newCookie = cookieManager.getCookie(httpUrl);
        GameLog.log("finsh syn  cookie ---- :" + newCookie);
        //立即同步cookie到webview，这是个耗时的工作
        PNThreadFactory.createThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                }
                else
                {
                    CookieSyncManager.getInstance().sync();
                }
            }
        });

    }


    /**
     * 同步cookie到webview
     * @param httpUrl
     * @param cookie
     */
    private  void synCookieToWebviewopus(String httpUrl, String  cookie)
    {
        if(Check.isNull(httpUrl) || Check.isNull(cookie))
        {
            GameLog.log("synCookieToWebview cookie can't is null ");
            return;
        }
        CookieSyncManager.createInstance(Utils.getContext());
        final CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(httpUrl,cookie);
        cookieManager.setCookie(httpUrl,"SelectedLanguage=zh-CN");
        String newCookie = cookieManager.getCookie(httpUrl);
        GameLog.log("finsh syn  cookie ---- :" + newCookie);
        //立即同步cookie到webview，这是个耗时的工作
        PNThreadFactory.createThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                }
                else
                {
                    CookieSyncManager.getInstance().sync();
                }
            }
        });

    }


    public class JsInterface{
        private Context mContext;

        public void showSource(String html) {
            GameLog.log("HTML "+html);
        }
        public JsInterface(Context mContext){
            this.mContext = mContext;
        }

        @JavascriptInterface
        public void showInforFroms(String name){
            ToastUtils.showLongToast(name);
            GameLog.log("javascriptTOjava("+name+","+name+")");
        }

        @JavascriptInterface
        public void showInfoFromJs(String name) {
            //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            ToastUtils.showLongToast(name);
            GameLog.log("showInfoFromJs("+name+")");
        }

        @JavascriptInterface
        public void getLoginInform(final String param){
            GameLog.log(" javascript2java(javaMethod)");
            wvPayGame.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showLongToast(param);
                    /*String loginInform = ACache.get(XPlayGameActivity.this).getAsString(HGConstant.USERNAME_CP_INFORM);
                    String [] spll = loginInform.split(Pattern.quote("?"));
                    String [] spll2 = spll[1].split("&");
                    UserInform userInform = new UserInform();
                    userInform.agent = spll2[0].split("=")[1];
                    userInform.id = spll2[1].split("=")[1];;
                    userInform.ida = spll2[2].split("=")[1];;
                    userInform.name = spll2[3].split("=")[1];;
                    userInform.pwd = spll2[4].split("=")[1];;
                    userInform.key = spll2[5].split("=")[1];;
                    userInform.flag = spll2[6].split("=")[1];;
                    userInform.apptip = spll2[7].split("=")[1];;
                    String userInformJson = new Gson().toJson(userInform);
                    GameLog.log(loginInform);
                    GameLog.log(userInformJson);*/
                    //wvPayGame.loadUrl("javascript:show('"+userInformJson+"')");
                }
            });
        }

        @JavascriptInterface
        public void closeAndroid(){
            GameLog.log(" javascript2java(closeAndroid)");
            wvPayGame.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void showAndroid(){
            GameLog.log(" javascript2java(showAndroid)");
            wvPayGame.post(new Runnable() {
                @Override
                public void run() {

                    ToastUtils.showLongToast("展示android ");
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!Check.isNull(wvPayGame)&&wvPayGame.canGoBack()){
                wvPayGame.goBack();
            }else{
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(!Check.isNull(wvPayGame)){
                ViewParent parent = wvPayGame.getParent();
                if(!Check.isNull(parent)){
                    ((ViewGroup)parent).removeAllViews();
                }
                wvPayGame.stopLoading();
                wvPayGame.getSettings().setJavaScriptEnabled(false);
                wvPayGame.clearHistory();
                wvPayGame.clearView();
                wvPayGame.removeAllViews();
                wvPayGame.destroy();
                wvPayGame = null;
                System.gc();
                EventBus.getDefault().post(new CloseGameViewEvent());
                GameLog.log("XplayGameActivity:--------onDestroy()--------");
            }
        }catch (Exception value){
            GameLog.log("XplayGameActivity异常:"+value);
        }

    }
}
