package com.hgapp.betnhg.homepage.events;

import com.hgapp.betnhg.common.http.ResponseSubscriber;
import com.hgapp.betnhg.common.http.request.AppTextMessageResponseList;
import com.hgapp.betnhg.common.util.HGConstant;
import com.hgapp.betnhg.common.util.RxHelper;
import com.hgapp.betnhg.common.util.SubscriptionHelper;
import com.hgapp.betnhg.data.PersonBalanceResult;
import com.hgapp.betnhg.data.DownAppGiftResult;
import com.hgapp.betnhg.data.LuckGiftResult;
import com.hgapp.betnhg.data.ValidResult;
import com.hgapp.common.util.Timber;

public class EventsPresenter implements EventsContract.Presenter {

    private IEventsApi api;
    private EventsContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public EventsPresenter(IEventsApi api, EventsContract.View view){
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }
    @Override
    public void postDownAppGift(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(api.postDownAppGift(HGConstant.PRODUCT_PLATFORM))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<DownAppGiftResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<DownAppGiftResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postDownAppGiftResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postLuckGift(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postLuckGift(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<LuckGiftResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<LuckGiftResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postLuckGiftResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postValidGift(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postValidGift(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<ValidResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<ValidResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postValidGiftResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postNewYearRed(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postNewYearRed(HGConstant.PRODUCT_PLATFORM,"receive_red_envelope"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<LuckGiftResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<LuckGiftResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postLuckGiftResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postPersonBalance(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postPersonBalance(HGConstant.PRODUCT_PLATFORM,"b"))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<PersonBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<PersonBalanceResult> response) {
                        if(response.isSuccess())
                        {
                            view.postPersonBalanceResult(response.getData().get(0));
                        }
                        else
                        {
                            view.showMessage(response.getDescribe());
                            Timber.d("快速登陆失败:%s",response);
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
                            view.setError(0,0);
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
