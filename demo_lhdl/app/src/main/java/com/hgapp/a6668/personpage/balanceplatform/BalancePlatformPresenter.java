package com.hgapp.a6668.personpage.balanceplatform;

import com.hgapp.a6668.common.http.ResponseSubscriber;
import com.hgapp.a6668.common.http.request.AppTextMessageResponse;
import com.hgapp.a6668.common.http.request.AppTextMessageResponseList;
import com.hgapp.a6668.common.util.HGConstant;
import com.hgapp.a6668.common.util.RxHelper;
import com.hgapp.a6668.common.util.SubscriptionHelper;
import com.hgapp.a6668.data.KYBalanceResult;
import com.hgapp.a6668.data.PersonBalanceResult;


public class BalancePlatformPresenter implements BalancePlatformContract.Presenter {


    private IBalancePlatformApi api;
    private BalancePlatformContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public BalancePlatformPresenter(IBalancePlatformApi api, BalancePlatformContract.View  view){
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void postBanalceTransferCP(String appRefer,  String action, String from,String to, String fund) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanalceTransferCP(HGConstant.PRODUCT_PLATFORM,action,from,to,fund))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<Object>>() {
                    @Override
                    public void success(AppTextMessageResponse<Object> response) {

                        view.showMessage(response.getDescribe());
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
    public void postPersonBalanceKY(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postPersonBalanceKY(HGConstant.PRODUCT_PLATFORM,"b"))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess())
                        {
                            view.postPersonBalanceKYResult(response.getData().get(0));
                        }
                        //view.showMessage(response.getDescribe());
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
    public void postPersonBalanceHG(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postPersonBalanceHG(HGConstant.PRODUCT_PLATFORM,"b"))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess())
                        {
                            view.postPersonBalanceHGResult(response.getData().get(0));
                        }
                        //view.showMessage(response.getDescribe());
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
    public void postPersonBalanceVG(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postPersonBalanceVG(HGConstant.PRODUCT_PLATFORM,"b"))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess())
                        {
                            view.postPersonBalanceVGResult(response.getData().get(0));
                        }
                        //view.showMessage(response.getDescribe());
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
    public void postBanalceTransfer(String appRefer, String f, String t,String b) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanalceTransfer(HGConstant.PRODUCT_PLATFORM,f,t,b))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<Object>>() {
                    @Override
                    public void success(AppTextMessageResponseList<Object> response) {

                        view.showMessage(response.getDescribe());
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
    public void postBanalceTransferKY(String appRefer, String f, String t, String b) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanalceTransferKY(HGConstant.PRODUCT_PLATFORM,f,t,b))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess()){
                            view.postPersonBalanceKYResult(response.getData().get(0));
                        }
                        view.showMessage(response.getDescribe());
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
    public void postBanalceTransferHG(String appRefer, String f, String t, String b) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanalceTransferHG(HGConstant.PRODUCT_PLATFORM,f,t,b))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess()){
                            view.postPersonBalanceHGResult(response.getData().get(0));
                        }
                        view.showMessage(response.getDescribe());
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
    public void postBanalceTransferVG(String appRefer, String f, String t, String b) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanalceTransferVG(HGConstant.PRODUCT_PLATFORM,f,t,b))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<KYBalanceResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<KYBalanceResult> response) {

                        if(response.isSuccess()){
                            view.postPersonBalanceVGResult(response.getData().get(0));
                        }
                        view.showMessage(response.getDescribe());
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
