package com.hg3366.a3366.homepage.handicap.leaguedetail;

import com.hg3366.a3366.base.IMessageView;
import com.hg3366.a3366.base.IPresenter;
import com.hg3366.a3366.base.IProgressView;
import com.hg3366.a3366.base.IView;
import com.hg3366.a3366.data.BetResult;
import com.hg3366.a3366.data.ComPassSearchListResult;
import com.hg3366.a3366.data.LeagueDetailSearchListResult;
import com.hg3366.a3366.data.PrepareBetResult;

public interface LeagueDetailSearchListContract {

    public interface Presenter extends IPresenter
    {
        public void postLeagueDetailSearchList(String appRefer, String type, String more, String gid);
        public void postComPassSearchList(String appRefer, String gtype, String sorttype, String mdate,String showtype, String M_league);
        public void postPrepareBetApi(String appRefer, String order_method, String gid, String type, String wtype, String rtype,String odd_f_type, String error_flag, String order_type);
        public void postBetApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype);

    }
    public interface View extends IView<LeagueDetailSearchListContract.Presenter>,IMessageView,IProgressView
    {
        public void postLeagueDetailSearchListResult(LeagueDetailSearchListResult leagueDetailSearchListResult);
        public void postComPassSearchListResult(ComPassSearchListResult leagueDetailSearchListResult);
        public void postLeagueDetailSearchListNoDataResult(String  noDataString);
        public void postPrepareBetApiResult(PrepareBetResult prepareBetResult);
        public void postBetApiResult(BetResult betResult);
    }

}
