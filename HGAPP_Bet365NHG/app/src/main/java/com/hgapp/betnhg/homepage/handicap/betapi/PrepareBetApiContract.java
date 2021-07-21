package com.hgapp.betnhg.homepage.handicap.betapi;

import com.hgapp.betnhg.base.IMessageView;
import com.hgapp.betnhg.base.IPresenter;
import com.hgapp.betnhg.base.IProgressView;
import com.hgapp.betnhg.base.IView;
import com.hgapp.betnhg.data.BetResult;
import com.hgapp.betnhg.data.GameAllPlayRBKResult;
import com.hgapp.betnhg.data.GameAllPlayBKResult;
import com.hgapp.betnhg.data.GameAllPlayFTResult;
import com.hgapp.betnhg.data.GameAllPlayRFTResult;
import com.hgapp.betnhg.data.PersonInformResult;
import com.hgapp.betnhg.data.PrepareBetResult;

public interface PrepareBetApiContract {
    public interface Presenter extends IPresenter{

        public void postGameAllBets(String appRefer, String gid, String gtype,String showtype);
        public void postGameAllBetsBK(String appRefer, String gid, String gtype,String showtype);
        public void postGameAllBetsRBK(String appRefer, String gid, String gtype,String showtype);
        public void postGameAllBetsFT(String appRefer, String gid, String gtype,String showtype);
        public void postGameAllBetsRFT(String appRefer, String gid, String gtype,String showtype,String isMaster);
        public void postPrepareBetApi(String appRefer, String order_method, String gid, String type, String wtype, String rtype,String odd_f_type, String error_flag, String order_type,String isMaster);
        public void postBetFTApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void postBetFTreApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void postBetFThreApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void postBetBKApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void postBetBKreApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void postBetChampionApi(String appRefer, String cate, String gid, String type, String active, String line_type, String odd_f_type, String gold, String ioradio_r_h, String rtype, String wtype, String autoOdd);
        public void getPersonInform(String appRefer);
    }

    public interface View extends IView<PrepareBetApiContract.Presenter>,IMessageView,IProgressView {
        public void postGameAllBetsBKResult(GameAllPlayBKResult gameAllPlayBKResult);
        public void postGameAllBetsRBKResult(GameAllPlayRBKResult gameAllPlayRBKResult);
        public void postGameAllBetsFTResult(GameAllPlayFTResult gameAllPlayFTResult);
        public void postGameAllBetsRFTResult(GameAllPlayRFTResult gameAllPlayRFTResult);
        public void postGameAllBetsFTFailResult(String message);
        public void postGameAllBetsResult(GameAllPlayRBKResult gameAllBetsResult);
        public void postPrepareBetApiResult(PrepareBetResult prepareBetResult);
        public void postBetApiResult(BetResult betResult);
        public void postBetApiFailResult(String message);
        public void postPersonInformResult(PersonInformResult personInformResult);
    }
}
