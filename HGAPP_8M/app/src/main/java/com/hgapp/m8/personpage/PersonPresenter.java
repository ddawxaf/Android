package com.hgapp.m8.personpage;

import com.hgapp.m8.HGApplication;
import com.hgapp.m8.common.http.ResponseSubscriber;
import com.hgapp.m8.common.http.request.AppTextMessageResponse;
import com.hgapp.m8.common.http.request.AppTextMessageResponseList;
import com.hgapp.m8.common.util.ACache;
import com.hgapp.m8.common.util.HGConstant;
import com.hgapp.m8.common.util.RxHelper;
import com.hgapp.m8.common.util.SubscriptionHelper;
import com.hgapp.m8.data.CPResult;
import com.hgapp.m8.data.NoticeResult;
import com.hgapp.m8.data.PersonBalanceResult;
import com.hgapp.m8.data.PersonInformResult;
import com.hgapp.m8.data.QipaiResult;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.hgapp.common.util.Timber;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

public class PersonPresenter implements PersonContract.Presenter {

    private IPersonApi iPersonApi;
    private PersonContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public PersonPresenter(IPersonApi iPersonApi,PersonContract.View  view){
        this.view = view;
        this.iPersonApi = iPersonApi;
        this.view.setPresenter(this);
    }


    @Override
    public void postNoticeList(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postNotice(HGConstant.PRODUCT_PLATFORM,""))
                .subscribe(new ResponseSubscriber<NoticeResult>() {
                    @Override
                    public void success(NoticeResult response) {
                        if(response.getStatus()==200){
                            view.postNoticeListResult(response);
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
    public void getPersonInform(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postPersonInform(HGConstant.PRODUCT_PLATFORM))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<PersonInformResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<PersonInformResult> response) {
                        if(response.isSuccess())
                        {
                            view.postPersonInformResult(response.getData());
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
    public void getPersonBalance(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postPersonBalance(HGConstant.PRODUCT_PLATFORM,"b"))//loginGet() login(appRefer,username,pwd)
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
    public void postQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {

                        if(!Check.isNull(response)&&!Check.isNull(response.getData())){
                            view.postQipaiResult(response.getData());
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
    public void postHgQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postHgQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {

                        if(!Check.isNull(response)&&!Check.isNull(response.getData())){
                            view.postHgQipaiResult(response.getData());
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
    public void postCP() {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postCP(HGConstant.PRODUCT_PLATFORM,"login"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<CPResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<CPResult> response) {

                        if(!Check.isNull(response)&&response.isSuccess()){
                            view.postCPResult(response.getData());
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
    public void logOut() {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postLogOut(HGConstant.PRODUCT_PLATFORM))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<Object>>() {
                    @Override
                    public void success(AppTextMessageResponse<Object> response) {
                        if(response.isSuccess()){
                            view.postPersonLogoutResult(response.getDescribe());
                        }
                        else{
                            Timber.d("快速登陆失败:%s",response);
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view){
                            view.setError(0,0);
                            view.showMessage(msg);
                        }
                    }
                }));
        //RetrofitUrlManager.getInstance().putDomain("CpUrl", CPClient.baseUrl());
        String token = ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.APP_CP_X_SESSION_TOKEN);
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.getLogOutCP("login/out/?token="+token+"&x-session-token="+token))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<Object>>() {
                    @Override
                    public void success(AppTextMessageResponse<Object> response) {
                        GameLog.log("彩票退出日志 "+response);
                    }

                    @Override
                    public void fail(String msg) {
                        GameLog.log("日志"+msg);
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
