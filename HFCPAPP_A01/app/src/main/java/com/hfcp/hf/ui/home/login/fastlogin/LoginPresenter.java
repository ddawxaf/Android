package com.hfcp.hf.ui.home.login.fastlogin;

import com.hfcp.hf.CFConstant;
import com.hfcp.hf.common.http.ResponseSubscriber;
import com.hfcp.hf.common.http.RxHelper;
import com.hfcp.hf.common.http.SubscriptionHelper;
import com.hfcp.hf.common.http.request.AppTextMessageResponse;
import com.hfcp.hf.data.LoginResult;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Daniel on 2018/4/20.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private ILoginApi api;
    private LoginContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public LoginPresenter(ILoginApi api, LoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.api = api;
    }

    @Override
    public void postLogin(String appRefer, String username, String password) {
       /* subscriptionHelper.add(RxHelper.addSugar(api.postLogin(CFConstant.PRODUCT_PLATFORM, "User", "Login", username, password, "1"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<LoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<LoginResult> response) {
                        if (response.isSuccess()) {
                            view.postLoginResult(response.getData());
                        } else {
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if (null != view) {
                            view.showMessage(msg);
                        }
                    }
                }));*/
        Map<String,String> params = new HashMap<>();
        params.put("terminal_id",CFConstant.PRODUCT_PLATFORM);
        params.put("packet","User");
        params.put("action","Login");
        params.put("username",username);
        params.put("password",password);
        subscriptionHelper.add(RxHelper.addSugar(api.getLogin(params))//CFConstant.PRODUCT_PLATFORM, "User", "Login", username, password, "1"
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<LoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<LoginResult> response) {
                        if (response.isSuccess()) {//目前返回的errno为0需要改成200 代表正确的
                            //ACache.get(Utils.getContext()).put(CFConstant.USERNAME_LOGIN_DEMO, "false");
                            view.postLoginResult(response.getData());
                        } else {
                            view.showMessage(response.getDescribe());
                        }
                        //view.postLoginResult(response.getData());
                    }

                    @Override
                    public void fail(String msg) {
                        if (null != view) {
                            view.showMessage(msg);
                        }
                    }
                }));
    }

    @Override
    public void postDemo(String appRefer, String username, String password) {
        Map<String,String> params = new HashMap<>();
        params.put("terminal_id",CFConstant.PRODUCT_PLATFORM);
        params.put("packet","User");
        params.put("action","Guest");
        params.put("way","guestRegSignin");
        subscriptionHelper.add(RxHelper.addSugar(api.getLogin(params))//CFConstant.PRODUCT_PLATFORM, "User", "Login", username, password, "1"
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<LoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<LoginResult> response) {
                        if (response.isSuccess()) {//目前返回的errno为0需要改成200 代表正确的
                            //ACache.get(Utils.getContext()).put(CFConstant.USERNAME_LOGIN_DEMO, "true");
                            view.postLoginResult(response.getData());
                        } else {
                            view.showMessage(response.getDescribe());
                        }
                        //view.postLoginResult(response.getData());
                    }

                    @Override
                    public void fail(String msg) {
                        if (null != view) {
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

        subscriptionHelper.unsubscribe();
        view = null;
        api = null;
    }


}

