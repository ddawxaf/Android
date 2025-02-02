package com.sands.corp.withdrawPage;

import com.sands.corp.common.http.request.AppTextMessageResponse;
import com.sands.corp.data.WithdrawResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface IWithdrawApi {

    //取款银行卡信息
    @POST("account/updatebank.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<WithdrawResult>> postWithdrawBankCard(@Field("appRefer") String appRefer);

    //取款提交
    @POST("account/take.php")
    @FormUrlEncoded
    public Observable<AppTextMessageResponse<Object>> postWithdrawSubmit(@Field("appRefer") String appRefer, @Field("Bank_Address") String Bank_Address, @Field("Bank_Account") String Bank_Account, @Field("Bank_Name") String Bank_Name, @Field("Money") String Money, @Field("Withdrawal_Passwd") String Withdrawal_Passwd, @Field("Alias") String Alias, @Field("Key") String Key);

}
