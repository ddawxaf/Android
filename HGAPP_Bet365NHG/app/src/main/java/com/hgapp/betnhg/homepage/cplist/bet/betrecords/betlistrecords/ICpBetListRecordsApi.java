package com.hgapp.betnhg.homepage.cplist.bet.betrecords.betlistrecords;

import com.hgapp.betnhg.data.BetRecordsListItemResult;
import com.hgapp.betnhg.data.BetRecordsResult;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface ICpBetListRecordsApi {

    @GET
    Observable<BetRecordsListItemResult> getCpBetRecords(@Url String path);

    @POST
    Observable<BetRecordsResult> postCpBetRecords(@Url String path);

}
