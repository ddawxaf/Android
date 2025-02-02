package com.qpweb.a01.ui.loginhome.fastlogin;

import com.qpweb.a01.base.IMessageView;
import com.qpweb.a01.base.IPresenter;
import com.qpweb.a01.base.IView;
import com.qpweb.a01.data.LoginResult;

/**
 * Created by Daniel on 2017/4/20.
 */

public interface LoginContract {

    public interface Presenter extends IPresenter {

        public void postLogin(String appRefer, String username, String password);
        public void postPhone(String appRefer, String phone, String code);
        public void postRegister(String appRefer, String phone, String code);
        public void postLoginPhone(String appRefer, String mem_phone,String mem_yzm, String reference, String code);
    }

    public interface View extends IView<Presenter>, IMessageView {

        public void postLoginResult(LoginResult loginResult);
        public void postPhoneResult(String errorMessage);
    }
}
