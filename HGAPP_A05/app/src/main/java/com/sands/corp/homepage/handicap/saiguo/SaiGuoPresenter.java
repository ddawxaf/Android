package com.sands.corp.homepage.handicap.saiguo;

import com.sands.corp.common.http.ResponseSubscriber;
import com.sands.corp.common.util.HGConstant;
import com.sands.corp.common.util.RxHelper;
import com.sands.corp.common.util.SubscriptionHelper;
import com.sands.corp.data.SaiGuoResult;
import com.sands.common.util.Check;

public class SaiGuoPresenter implements SaiGuoContract.Presenter {
    private ISaiGuoApi api;
    private SaiGuoContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public SaiGuoPresenter(ISaiGuoApi api, SaiGuoContract.View view) {
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }

    @Override
    public void postSaiGuoList(String appRefer, String game_type, String list_data) {
        subscriptionHelper.add(RxHelper.addSugar(api.postSaiGuoList(HGConstant.PRODUCT_PLATFORM, game_type, list_data))
                .subscribe(new ResponseSubscriber<SaiGuoResult>() {
                    @Override
                    public void success(SaiGuoResult response) {
                        if (response.getStatus().equals("200")) {
                            if(!Check.isNull(response.getData())&&response.getData().size()>0){
                                view.postSaiGuoResult(response);
                            } else {
                                view.showMessage(response.getDescribe());
                            }
                        } else {
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if (null != view) {
                            view.setError(0, 0);
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
