package com.gmcp.gm.ui.me.record.overbet;


import com.gmcp.gm.common.http.request.AppTextMessageResponse;
import com.gmcp.gm.data.TraceListResult;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Daniel on 2019/2/20.
 */

public interface ITraceListApi {


    //追号列表提交 (encoded = true)
    @GET("service")
    Observable<AppTextMessageResponse<TraceListResult>> getTraceList(
            @QueryMap Map<String, String> params
    );
}
