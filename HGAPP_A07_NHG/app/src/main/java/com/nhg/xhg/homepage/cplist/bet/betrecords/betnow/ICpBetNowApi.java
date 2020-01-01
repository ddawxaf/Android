package com.nhg.xhg.homepage.cplist.bet.betrecords.betnow;

import com.nhg.xhg.data.CPBetNowResult;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface ICpBetNowApi {

    @GET
    Observable<CPBetNowResult> getCpBetRecords(@Url String path);

    @POST
    Observable<CPBetNowResult> postCpBetRecords(@Url String path);

}
