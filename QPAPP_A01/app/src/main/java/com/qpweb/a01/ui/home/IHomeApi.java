package com.qpweb.a01.ui.home;


import com.qpweb.a01.data.BannerResult;
import com.qpweb.a01.data.LogoutResult;
import com.qpweb.a01.data.NoticeResult;
import com.qpweb.a01.data.RefreshMoneyResult;
import com.qpweb.a01.data.SignTodayResult;
import com.qpweb.a01.data.WinNewsResult;
import com.qpweb.a01.http.request.AppTextMessageResponse;
import com.qpweb.a01.http.request.AppTextMessageResponseList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Daniel on 2018/7/3.
 */

public interface IHomeApi {

    //banner图片
    @POST("api/site/banner.php")
    @FormUrlEncoded
    public Observable<BannerResult> postBanner(@Field("appRefer") String appRefer);

    //滚动公告notice(1:滚动公告；2：公告列表；3：站内信)
    @POST("api/site/notice.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<NoticeResult>> postNotice(@Field("appRefer") String appRefer);

    //赢取的消息
    @POST("member/winningnews.php")
    @FormUrlEncoded
    public Observable<WinNewsResult> postWinNews(@Field("appRefer") String appRefer);

    //赢取的消息
    @POST("api/lyqp/index.php")
    @FormUrlEncoded
    public Observable<WinNewsResult> postPayGame(@Field("appRefer") String appRefer, @Field("uid") String uid, @Field("gameId") String gameId);

    //刷新余额
    @POST("api/lyqp/ly_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<RefreshMoneyResult>> postRefreshMoney(@Field("appRefer") String appRefer, @Field("action") String action);

    //创建乐游账号
    @POST("api/lyqp/ly_api.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<RefreshMoneyResult>> postNeedLyId(@Field("appRefer") String appRefer, @Field("action") String action);


    //签到
    @POST("api/signin.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<SignTodayResult>> postSignToday(
            @Field("appRefer") String appRefer,
            @Field("action") String action);


    //退出
    @POST("api/logout.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponseList<LogoutResult>> postLogout(@Field("appRefer") String appRefer);


}
