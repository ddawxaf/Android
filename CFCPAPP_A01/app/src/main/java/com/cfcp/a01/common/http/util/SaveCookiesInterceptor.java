package com.cfcp.a01.common.http.util;



import com.cfcp.a01.CFApplication;
import com.cfcp.a01.common.utils.ACache;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.Response;

public class SaveCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            ArrayList<String> cookies = new ArrayList<>();
            String cookie = " ";
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
               // GameLog.log("Cookie "+header);
                cookie += header+" ";
                ACache.get(CFApplication.instance().getApplicationContext()).put("tySet-Cookie",header);
            }
//            GameLog.log("Save 最终的cookie是'"+cookie);
//            GameLog.log("Save 当前的可用转台'"+ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.APP_CP_COOKIE_AVIABLE));
        }

        return originalResponse;

    }
}
