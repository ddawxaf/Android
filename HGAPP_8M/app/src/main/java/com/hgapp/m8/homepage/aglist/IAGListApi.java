package com.hgapp.m8.homepage.aglist;

import com.hgapp.m8.common.http.request.AppTextMessageResponse;
import com.hgapp.m8.common.http.request.AppTextMessageResponseList;
import com.hgapp.m8.data.AGGameLoginResult;
import com.hgapp.m8.data.AGLiveResult;
import com.hgapp.m8.data.CheckAgLiveResult;
import com.hgapp.m8.data.PersonBalanceResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface IAGListApi {

    //获取余额
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);

    //获取MG余额
    @POST("mg/mg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postMGPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);

    //获取CQ余额
    @POST("cq9/cq9_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postCQPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);

    //获取MW余额
    @POST("mw/mw_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postMWPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);


    //检查CQ电子列表
    @POST("cq9/cq9_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postCQGameList(@Field("appRefer") String appRefer,  @Field("action") String action);

    //检查MW电子列表
    @POST("mw/mw_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postMWGameList(@Field("appRefer") String appRefer,  @Field("action") String action);



    //检查AG真人 或者 电子列表
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postAGGameList(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);


    //检查MG电子列表
    @POST("mg/mg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postMGGameList(@Field("appRefer") String appRefer,  @Field("action") String action);


    //进入CQ游戏
    @POST("cq9/cq9_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<AGGameLoginResult>> postCQLoginGame(@Field("appRefer") String appRefer, @Field("game_id") String game_id, @Field("action") String action);

    //进入MW游戏
    @POST("mw/mw_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGGameLoginResult>> postMWLoginGame(@Field("appRefer") String appRefer, @Field("gameId") String gameId, @Field("action") String action);


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

    //进入MG游戏
    @POST("mg/mg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<AGGameLoginResult>> postMGLoginGame(@Field("appRefer") String appRefer, @Field("game_id") String game_id, @Field("action") String action);


    //检查AG账号
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<CheckAgLiveResult>> postCheckAgAccount(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);


    //创建AG账号
    @POST("ag_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<CheckAgLiveResult>> postCreateAgAccount(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("action") String action);

    //获取FG余额
    @POST("fg/fg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<PersonBalanceResult>> postFGPersonBalance(@Field("appRefer") String appRefer, @Field("action") String action);

    //检查FG电子列表
    @POST("fg/fg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGLiveResult>> postFGGameList(@Field("appRefer") String appRefer,  @Field("action") String action);

    //进入FG游戏
    @POST("fg/fg_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<AGGameLoginResult>> postFGLoginGame(@Field("appRefer") String appRefer, @Field("game_id") String game_id, @Field("action") String action);

}
