package com.hgapp.a0086.homepage.cplist.quickbet.mothed;


import com.hgapp.a0086.common.http.ResponseSubscriber;
import com.hgapp.a0086.common.util.RxHelper;
import com.hgapp.a0086.common.util.SubscriptionHelper;
import com.hgapp.a0086.data.CPQuickBetMothedResult;
import com.hgapp.a0086.data.CPQuickBetResult;

/**
 * Created by Daniel on 2017/5/31.
 */

public class QuickBetMethodPresenter implements QuickBetMethodContract.Presenter {
    private QuickBetMethodContract.View view;
    private IQuickBetMethodApi api;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public QuickBetMethodPresenter(IQuickBetMethodApi api, QuickBetMethodContract.View view)
    {
        this.view = view;
        view.setPresenter(this);
        this.api = api;
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
       /* subscriptionHelper.unsubscribe();
        view = null;
        api = null;*/
    }

    @Override
    public void postQuickBetMothed(String code, String gamecode, String code_number, String sort,String token) {
        subscriptionHelper.add(RxHelper.addSugar(api.postQuickBetMothed(code,gamecode,code_number,sort,token))//loginGet() login(appRefer,username,pwd)
                .subscribe(new ResponseSubscriber<CPQuickBetMothedResult>() {
                    @Override
                    public void success(CPQuickBetMothedResult response) {
                        view.postQuickBetMothedResult(response);
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
}
