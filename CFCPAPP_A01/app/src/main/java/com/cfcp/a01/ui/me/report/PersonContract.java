package com.cfcp.a01.ui.me.report;

import com.cfcp.a01.common.base.IMessageView;
import com.cfcp.a01.common.base.IPresenter;
import com.cfcp.a01.common.base.IView;
import com.cfcp.a01.data.LoginResult;
import com.cfcp.a01.data.PersonReportResult;

/**
 * Created by Daniel on 2018/12/20.
 */

public interface PersonContract {

    interface Presenter extends IPresenter {

        void getPersonReport(String begin_date, String end_date);
    }

    interface View extends IView<Presenter>, IMessageView {

        void getPersonReportResult(PersonReportResult personReportResult);
    }
}
