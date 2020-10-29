package com.hgapp.bet365.homepage.cplist.hall;

import com.hgapp.bet365.common.http.request.AppTextMessageResponse;
import com.hgapp.bet365.data.CPHallResult;
import com.hgapp.bet365.data.CPLeftInfoResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface ICPHallListApi {

    //创建AG账号
    @Headers({"Domain-Name: CpUrl"})
    @GET("{path}")
    Observable<AppTextMessageResponse<Object>> getLoginCP(@Path(value = "path", encoded = true) String path);


    //创建AG账号
    @Headers({"Domain-Name: CpUrl"})
    @GET
    Observable<CPHallResult> get(@Url String path);

    @Headers({"Domain-Name: CpUrl"})
    @POST("main/leftinfo")
    @FormUrlEncoded
    public Observable<CPLeftInfoResult> postLeftInfo(@Field("optype") String optype, @Field("x-session-token") String x_session_token);

}
