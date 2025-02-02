package com.hg3366.a3366.personpage;

import com.hg3366.a3366.HGApplication;
import com.hg3366.a3366.common.http.ResponseSubscriber;
import com.hg3366.a3366.common.http.cphttp.CPClient;
import com.hg3366.a3366.common.http.request.AppTextMessageResponse;
import com.hg3366.a3366.common.http.request.AppTextMessageResponseList;
import com.hg3366.a3366.common.util.ACache;
import com.hg3366.a3366.common.util.HGConstant;
import com.hg3366.a3366.common.util.RxHelper;
import com.hg3366.a3366.common.util.SubscriptionHelper;
import com.hg3366.a3366.data.AGGameLoginResult;
import com.hg3366.a3366.data.CPResult;
import com.hg3366.a3366.data.NoticeResult;
import com.hg3366.a3366.data.PersonBalanceResult;
import com.hg3366.a3366.data.PersonInformResult;
import com.hg3366.a3366.data.QipaiResult;
import com.hg3366.common.util.Check;
import com.hg3366.common.util.GameLog;
import com.hg3366.common.util.Timber;

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
        /*RetrofitUrlManager.getInstance().putDomain("CpUrl", CPClient.baseUrl());
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
                }));*/

    }

    @Override
    public void postBYGame(String appRefer, String gameid) {
        subscriptionHelper.add(RxHelper.addSugar(iPersonApi.postBYGame(HGConstant.PRODUCT_PLATFORM,gameid))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<AGGameLoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<AGGameLoginResult> response) {
                        if(response.isSuccess())
                        {
                            view.postGoPlayGameResult(response.getData());
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
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
