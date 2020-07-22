package com.hgapp.bet365.homepage;

import com.hgapp.bet365.common.http.ResponseSubscriber;
import com.hgapp.bet365.common.http.request.AppTextMessageResponse;
import com.hgapp.bet365.common.http.request.AppTextMessageResponseList;
import com.hgapp.bet365.common.util.ACache;
import com.hgapp.bet365.common.util.HGConstant;
import com.hgapp.bet365.common.util.RxHelper;
import com.hgapp.bet365.common.util.SubscriptionHelper;
import com.hgapp.bet365.data.AGGameLoginResult;
import com.hgapp.bet365.data.BannerResult;
import com.hgapp.bet365.data.CPResult;
import com.hgapp.bet365.data.GameNumResult;
import com.hgapp.bet365.data.MaintainResult;
import com.hgapp.bet365.data.NoticeResult;
import com.hgapp.bet365.data.QipaiResult;
import com.hgapp.bet365.data.ValidResult;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.Utils;


public class HomePagePresenter implements HomePageContract.Presenter {


    private IHomePageApi api;
    private HomePageContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public HomePagePresenter(IHomePageApi api, HomePageContract.View  view){
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void postGameNum(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(api.postGameNum(HGConstant.PRODUCT_PLATFORM))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<GameNumResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<GameNumResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postGameNumResult(response.getData());
                            }
                        }else{
                            ACache.get(Utils.getContext()).put(HGConstant.USERNAME_QIPAI_URL, "");
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postBanner(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBanner(HGConstant.PRODUCT_PLATFORM,"mobile"))
                .subscribe(new ResponseSubscriber<BannerResult>() {
                    @Override
                    public void success(BannerResult response) {
                        if(response.getStatus()==200){
                            view.postBannerResult(response);
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postNotice(String appRefer) {
        subscriptionHelper.add(RxHelper.addSugar(api.postNotice(HGConstant.PRODUCT_PLATFORM,"1"))
                .subscribe(new ResponseSubscriber<NoticeResult>() {
                    @Override
                    public void success(NoticeResult response) {
                        if(response.getStatus()==200){
                            view.postNoticeResult(response);
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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


    @Override
    public void postQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postQipaiResult(response.getData());
                            }
                        }else{
                            ACache.get(Utils.getContext()).put(HGConstant.USERNAME_QIPAI_URL, "");
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postHGQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postHGQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postHGQipaiResult(response.getData());
                            }
                        }else{
                            ACache.get(Utils.getContext()).put(HGConstant.USERNAME_HG_QIPAI_URL, "");
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postVGQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postVGQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postVGQipaiResult(response.getData());
                            }
                        }else{
                            ACache.get(Utils.getContext()).put(HGConstant.USERNAME_VG_QIPAI_URL, "");
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postLYQipai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postLYQiPai(HGConstant.PRODUCT_PLATFORM,"cm"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postLYQipaiResult(response.getData());
                            }
                        }else{
                            ACache.get(Utils.getContext()).put(HGConstant.USERNAME_LY_QIPAI_URL, "");
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postAviaQiPai(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postAviaQiPai(HGConstant.PRODUCT_PLATFORM,"getLaunchGameUrl"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess()){
                            if(!Check.isNull(response.getData())){
                                view.postAviaQiPaiResult(response.getData());
                            }
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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


    @Override
    public void postCP() {
        subscriptionHelper.add(RxHelper.addSugar(api.postCP(HGConstant.PRODUCT_PLATFORM,"login"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<CPResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<CPResult> response) {

                        if(!Check.isNull(response)&&response.isSuccess()){
                            view.postCPResult(response.getData());
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postValidGift(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postValidGift(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<ValidResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<ValidResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postValidGiftResult(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postValidGift2(String appRefer, String action) {
        subscriptionHelper.add(RxHelper.addSugar(api.postValidGift(HGConstant.PRODUCT_PLATFORM,action))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<ValidResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<ValidResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postValidGift2Result(response.getData().get(0));
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postMaintain() {
        subscriptionHelper.add(RxHelper.addSugar(api.postMaintain(HGConstant.PRODUCT_PLATFORM))
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<MaintainResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<MaintainResult> response) {
                        //view.postDownAppGiftResult("38");
                        if(response.isSuccess()){
                            view.postMaintainResult(response.getData());
                        }else{
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postBYGame(String appRefer, String gameid) {
        subscriptionHelper.add(RxHelper.addSugar(api.postLoginGame(HGConstant.PRODUCT_PLATFORM,gameid))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<AGGameLoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<AGGameLoginResult> response) {
                        if(response.isSuccess())
                        {
                            if(!Check.isNull(response.getData())){
                                view.postGoPlayGameResult(response.getData());
                            }else{
                                view.showMessage(response.getDescribe());
                            }
                        }
                        else
                        {
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postOGGame(String appRefer, String gameid) {
        subscriptionHelper.add(RxHelper.addSugar(api.postOGGame(HGConstant.PRODUCT_PLATFORM,"getLaunchGameUrl"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<AGGameLoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<AGGameLoginResult> response) {
                        if(response.isSuccess())
                        {
                            //view.postGoPlayGameResult(response.getData());
                            if(!Check.isNull(response.getData())){
                                view.postOGResult(response.getData());
                            }else{
                                view.showMessage(response.getDescribe());
                            }
                        }
                        else
                        {
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postBBINGame(String appRefer, String gameid) {
        subscriptionHelper.add(RxHelper.addSugar(api.postBBINGame(HGConstant.PRODUCT_PLATFORM,"getLaunchGameUrl"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<AGGameLoginResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<AGGameLoginResult> response) {
                        if(response.isSuccess())
                        {
                            //view.postGoPlayGameResult(response.getData());
                            if(!Check.isNull(response.getData())){
                                view.postOGResult(response.getData());
                            }else{
                                view.showMessage(response.getDescribe());
                            }
                        }
                        else
                        {
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void postThunFireGame(String appRefer, String gameid) {
        subscriptionHelper.add(RxHelper.addSugar(api.postThunFireGame(HGConstant.PRODUCT_PLATFORM,"getLaunchGameUrl"))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<QipaiResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<QipaiResult> response) {
                        if(response.isSuccess())
                        {
                            if(!Check.isNull(response.getData())){
                                view.postThunFireGameResult(response.getData());
                            }
                        }
                        else
                        {
                            view.showMessage(response.getDescribe());
                        }
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

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
