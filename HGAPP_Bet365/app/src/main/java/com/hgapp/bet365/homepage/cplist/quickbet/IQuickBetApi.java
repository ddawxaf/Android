package com.hgapp.bet365.homepage.cplist.quickbet;

import com.hgapp.bet365.common.http.request.AppTextMessageResponse;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Daniel on 2017/5/31.
 */

public interface IQuickBetApi {

    @POST("logout")
    public Observable<AppTextMessageResponse> logout();
}
