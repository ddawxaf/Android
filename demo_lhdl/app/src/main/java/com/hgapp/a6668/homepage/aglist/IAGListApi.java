package com.hgapp.a6668.homepage.aglist;

import com.hgapp.a6668.common.http.request.AppTextMessageResponse;
import com.hgapp.a6668.common.http.request.AppTextMessageResponseList;
import com.hgapp.a6668.data.AGGameLoginResult;
import com.hgapp.a6668.data.AGLiveResult;
import com.hgapp.a6668.data.CheckAgLiveResult;
import com.hgapp.a6668.data.PersonBalanceResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface IAGListApi {

    //获取余额
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);

    //检查AG真人 或者 电子列表
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postAGGameList(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);

    //检查AG视讯账号是否注册
    @POST("live.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<CheckAgLiveResult>> postCheckAgLiveAccount(@Field("appRefer") String appRefer);

    //检查AG电子账号是否注册
    @POST("games.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<CheckAgLiveResult>> postCheckAgGameAccount(@Field("appRefer") String appRefer);

    //AG登录/注册接口
    @POST("zrsx_login.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<AGGameLoginResult>> postLoginGame(@Field("appRefer") String appRefer, @Field("gameid") String gameid);

    //检查AG账号
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<CheckAgLiveResult>> postCheckAgAccount(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);


    //创建AG账号
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<CheckAgLiveResult>> postCreateAgAccount(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);




}
