package com.gmcp.gm.ui.me.link;


import com.gmcp.gm.common.http.request.AppTextMessageResponse;
import com.gmcp.gm.data.RegisterLinkListResult;
import com.gmcp.gm.data.RegisterMeResult;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Daniel on 2019/2/20.
 */

public interface IRegisterLinkApi {


    //存款方式提交 (encoded = true)
    @GET("service")
    Observable<AppTextMessageResponse<RegisterMeResult>> getFundGroup(
            @QueryMap Map<String, String> params
    );
    @GET("service")
    Observable<AppTextMessageResponse<RegisterLinkListResult>> getFundList(
            @QueryMap Map<String, String> params
    );
}
