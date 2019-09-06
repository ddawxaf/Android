package com.hfcp.hf.common.http;


import com.hfcp.hf.common.utils.Timber;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by daniel on 2017/8/12.
 */

public class AppInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Timber.d("AppInterceptor intercept");
        Request newRequest = chain.request().newBuilder().addHeader("Connection","close").build();
        return chain.proceed(newRequest);
    }
}
