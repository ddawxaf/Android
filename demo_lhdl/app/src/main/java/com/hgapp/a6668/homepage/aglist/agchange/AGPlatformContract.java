package com.hgapp.a6668.homepage.aglist.agchange;

import com.hgapp.a6668.base.IMessageView;
import com.hgapp.a6668.base.IPresenter;
import com.hgapp.a6668.base.IProgressView;
import com.hgapp.a6668.base.IView;
import com.hgapp.a6668.data.BetRecordResult;
import com.hgapp.a6668.data.PersonBalanceResult;

public interface AGPlatformContract {
    public interface Presenter extends IPresenter
    {
        public void postBanalceTransfer(String appRefer, String f, String t, String b);

        public void postPersonBalance(String appRefer, String action);
    }
    public interface View extends IView<AGPlatformContract.Presenter>,IMessageView,IProgressView
    {
        public void postBanalceTransferSuccess();
        public void postBetRecordResult(BetRecordResult message);
        public void postPersonBalanceResult(PersonBalanceResult personBalance);
    }
}
