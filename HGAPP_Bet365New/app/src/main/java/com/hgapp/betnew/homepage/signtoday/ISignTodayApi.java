package com.hgapp.betnew.homepage.signtoday;

import com.hgapp.betnew.common.http.request.AppTextMessageResponseList;
import com.hgapp.betnew.data.ReceiveSignTidayResults;
import com.hgapp.betnew.data.SignTodayResults;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ISignTodayApi {

    // 签到
    @POST("api/attendanceApi.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<SignTodayResults>> postSignTodayCheck(@Field("appRefer") String appRefer, @Field("actype") String actype);


    // 领取
    @POST("api/attendanceApi.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<ReceiveSignTidayResults>> postSignTodayReceive(@Field("appRefer") String appRefer, @Field("actype") String actype);


}
