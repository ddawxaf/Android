package com.venen.tian.depositpage;

import com.venen.tian.data.DepositAliPayQCCodeResult;
import com.venen.tian.data.DepositBankCordListResult;
import com.venen.tian.data.DepositListResult;
import com.venen.tian.data.DepositThirdBankCardResult;
import com.venen.tian.data.DepositThirdQQPayResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface IDepositApi {
    //存款方式列表
    @POST("account/deposit_one_api.php")
    @FormUrlEncoded
    public Observable<DepositListResult> postDepositList(@Field("appRefer") String appRefer);


    //公司入款-银行卡列表  对应公司入款
    @POST("account/deposit_two_bank_company.php")
    @FormUrlEncoded
    public Observable<DepositBankCordListResult> postDepositBankCordList(@Field("appRefer") String appRefer);


    //公司入款-第三方银行卡  对应银行卡线上
    @POST("account/deposit_two_third_bank.php")
    @FormUrlEncoded
    public Observable<DepositThirdBankCardResult> postDepositThirdBankCard(@Field("appRefer") String appRefer);

    //公司入款-第三方微信
    @POST("account/deposit_two_third_wx.php")
    @FormUrlEncoded
    public Observable<DepositThirdQQPayResult> postDepositThirdWXPay(@Field("appRefer") String appRefer);

    //公司入款-第三方支付宝
    @POST("account/deposit_two_third_zfb.php")
    @FormUrlEncoded
    public Observable<DepositThirdQQPayResult> postDepositThirdAliPay(@Field("appRefer") String appRefer);

    //公司入款-第三方QQ
    @POST("account/deposit_two_third_qq.php")
    @FormUrlEncoded
    public Observable<DepositThirdQQPayResult> postDepositThirdQQPay(@Field("appRefer") String appRefer);

    //公司入款-支付宝二维码
    @POST("account/bank_type_ALISAOMA_api.php")
    @FormUrlEncoded
    public Observable<DepositAliPayQCCodeResult> postDepositAliPayQCCode(@Field("appRefer") String appRefer,@Field("bankid") String bankid);

    //公司入款-微信二维码
    @POST("account/bank_type_WESAOMA_api.php")
    @FormUrlEncoded
    public Observable<DepositAliPayQCCodeResult> postDepositWechatQCCode(@Field("appRefer") String appRefer,@Field("bankid") String bankid);

    //公司入款-云闪付
    @POST("account/bank_type_YLSMYSF_api.php")
    @FormUrlEncoded
    public Observable<DepositAliPayQCCodeResult> postDepositUQCCode(@Field("appRefer") String appRefer,@Field("bankid") String bankid);

}
