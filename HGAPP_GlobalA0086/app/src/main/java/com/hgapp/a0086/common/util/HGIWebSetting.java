package com.hgapp.a0086.common.util;

import android.os.Build;

import com.hgapp.common.util.Check;
import com.hgapp.common.util.DeviceUtils;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

/**
 * Created by ak on 2017/10/22.
 */


public class HGIWebSetting {
/**
     * 设置webSetting
     * @param webView
     */

    public static void init(WebView webView){
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        IX5WebViewExtension ix5 = webView.getX5WebViewExtension();
        if(!Check.isNull(ix5)){
            ix5.setScrollBarFadingEnabled(false);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(false);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setCursiveFontFamily("cursive");
        webSettings.setDisplayZoomControls(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setFantasyFontFamily("fantasy");
        webSettings.setFixedFontFamily("monospace");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLightTouchEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(true);
        webSettings.setNavDump(false);
        webSettings.setPluginsEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);//打开播放RTMP流
        webSettings.setSansSerifFontFamily("sans-serif");
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(false);
        webSettings.setSerifFontFamily("serif");
        webSettings.setStandardFontFamily("sans-serif");
        webSettings.setUseWebViewBackgroundForOverscrollBackground(false);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(true);
        //webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 5.1.1; zh-cn;) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/6.3 Mobile Safari/537.36");
        //webSettings.setUserAgentString(DeviceUtils.getUserAgent());
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }

        webSettings.setEnableSmoothTransition(false);

        webSettings.setGeolocationEnabled(true);

        webSettings.setSaveFormData(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
            webSettings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
}
