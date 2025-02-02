package com.gmcp.gm.ui.me.bankcard;


import com.gmcp.gm.common.http.request.AppTextMessageResponse;
import com.gmcp.gm.data.TeamReportResult;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Daniel on 2019/2/20.
 */

public interface IModifyApi {

    //修改银行卡(encoded = true)
    @GET("service")
    Observable<AppTextMessageResponse<TeamReportResult>> getCardModify(
            @QueryMap Map<String, String> params
    );

    //验证银行卡(encoded = true)
    @GET("service")
    Observable<AppTextMessageResponse<TeamReportResult>> getCardVerify(
            @QueryMap Map<String, String> params
    );

    //删除银行卡(encoded = true)
    @GET("service")
    Observable<AppTextMessageResponse<TeamReportResult>> getCardDelete(
            @QueryMap Map<String, String> params
    );

}
