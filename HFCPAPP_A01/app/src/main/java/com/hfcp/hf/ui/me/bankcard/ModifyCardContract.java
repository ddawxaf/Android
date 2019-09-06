package com.hfcp.hf.ui.me.bankcard;

import com.hfcp.hf.common.base.IMessageView;
import com.hfcp.hf.common.base.IPresenter;
import com.hfcp.hf.common.base.IView;
import com.hfcp.hf.data.BankCardAddResult;
import com.hfcp.hf.data.BankListResult;

/**
 * Created by Daniel on 2018/12/20.
 */

public interface ModifyCardContract {

    interface Presenter extends IPresenter {

        void getBankList(String id);
        void getModifyCard(String id,String bank,String bank_id,String branch,String account_name,String account,String account_confirmation);
    }

    interface View extends IView<Presenter>, IMessageView {
        void getBankListResult(BankListResult bankListResult);
        void getModifyCardResult(BankCardAddResult bankCardAddResult);
    }
}
