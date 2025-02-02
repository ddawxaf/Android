package com.hgapp.a6668.homepage.sportslist;

import com.hgapp.a6668.base.IMessageView;
import com.hgapp.a6668.base.IPresenter;
import com.hgapp.a6668.base.IProgressView;
import com.hgapp.a6668.base.IView;
import com.hgapp.a6668.data.SportsListResult;

public interface SportsListContract {
    public interface Presenter extends IPresenter
    {
        public void postSportsList(String appRefer,String type, String more);
        public void postSportsListFU(String appRefer, String gtype,String showtype, String sorttype,String date);
        public void postSportsListFTs(String appRefer, String gtype,String showtype, String sorttype,String date);
        public void postSportsListFTr(String appRefer, String gtype,String showtype, String sorttype,String date);
        public void postSportsListBU(String appRefer, String gtype,String showtype, String sorttype,String date);
        public void postSportsListBKs(String appRefer, String gtype,String showtype, String sorttype,String date);
        public void postSportsListBKr(String appRefer, String gtype,String showtype, String sorttype,String date);
    }

    public interface View extends IView<SportsListContract.Presenter>,IMessageView,IProgressView {

        public void postSportsListResultResult(SportsListResult sportsListResult);
        public void postSportsListResultResultFU(SportsListResult sportsListResult);
        public void postSportsListResultResultFTs(SportsListResult sportsListResult);
        public void postSportsListResultResultFTr(SportsListResult sportsListResult);
        public void postSportsListResultResultBU(SportsListResult sportsListResult);
        public void postSportsListResultResultBKs(SportsListResult sportsListResult);
        public void postSportsListResultResultBKr(SportsListResult sportsListResult);
    }
}
