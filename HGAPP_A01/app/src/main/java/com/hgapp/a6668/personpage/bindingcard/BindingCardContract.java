package com.hgapp.a6668.personpage.bindingcard;

import com.hgapp.a6668.base.IMessageView;
import com.hgapp.a6668.base.IPresenter;
import com.hgapp.a6668.base.IProgressView;
import com.hgapp.a6668.base.IView;
import com.hgapp.a6668.data.GetBankCardListResult;

public interface BindingCardContract {
    public interface Presenter extends IPresenter
    {
        public void postGetBankCardList(String appRefer, String action_type);
        public void postBindingBankCard(String appRefer, String action_type, String bank_name, String bank_account, String bank_address, String pay_password, String pay_password2);
    }
    public interface View extends IView<BindingCardContract.Presenter>,IMessageView,IProgressView
    {
        public void postGetBankCardListResult(GetBankCardListResult getBankCardListResult);
        public void postBindingBankCardResult(Object withdrawResult);
    }
}
