package com.sands.corp.homepage.signtoday;


import com.sands.corp.common.http.ResponseSubscriber;
import com.sands.corp.common.http.request.AppTextMessageResponseList;
import com.sands.corp.common.util.HGConstant;
import com.sands.corp.common.util.RxHelper;
import com.sands.corp.common.util.SubscriptionHelper;
import com.sands.corp.data.ReceiveSignTidayResults;
import com.sands.corp.data.SignTodayResults;

public class SignTodayPresenter implements SignTodayContract.Presenter {

    private ISignTodayApi api;
    private SignTodayContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public SignTodayPresenter(ISignTodayApi api, SignTodayContract.View view){
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void postSignTodayCheck(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postSignTodayCheck(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<SignTodayResults>>() {
                    @Override
                    public void success(AppTextMessageResponseList<SignTodayResults> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postSignTodayCheckResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postSignTodaySign(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postSignTodayCheck(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<SignTodayResults>>() {
                    @Override
                    public void success(AppTextMessageResponseList<SignTodayResults> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            postSignTodayCheck("","checked");
                        }
                        view.showMessage(response.getDescribe());
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postSignTodayReceive(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postSignTodayReceive(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<ReceiveSignTidayResults>>() {
                    @Override
                    public void success(AppTextMessageResponseList<ReceiveSignTidayResults> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postSignTodayReceiveResult(response.getData().get(0));
                        }
                        view.showMessage(response.getDescribe());
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
