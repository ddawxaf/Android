package com.hfcp.hf.ui.lottery;

import com.hfcp.hf.CFConstant;
import com.hfcp.hf.common.http.ResponseSubscriber;
import com.hfcp.hf.common.http.RxHelper;
import com.hfcp.hf.common.http.SubscriptionHelper;
import com.hfcp.hf.common.http.request.AppTextMessageResponse;
import com.hfcp.hf.common.http.request.AppTextMessageResponseList;
import com.hfcp.hf.common.utils.ACache;
import com.hfcp.hf.data.CPLotteryListResult;
import com.hfcp.hf.data.LotteryListResult;

import java.util.HashMap;
import java.util.Map;

import static com.hfcp.hf.common.utils.Utils.getContext;


/**
 * Created by Daniel on 2018/4/20.
 */
public class LotteryResultPresenter implements LotteryResultContract.Presenter {

    private ILotteryResultApi api;
    private LotteryResultContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public LotteryResultPresenter(ILotteryResultApi api, LotteryResultContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.api = api;
    }

    @Override
    public void getLotteryList(String terminal_id, String lottery_id, String token) {
        Map<String,String> params = new HashMap<>();
        params.put("terminal_id",CFConstant.PRODUCT_PLATFORM);
        params.put("packet","Game");
        params.put("action","GetWnNumberList");
        params.put("count","30");
        params.put("lottery_id",lottery_id);
        params.put("token", ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_TOKEN));
        subscriptionHelper.add(RxHelper.addSugar(api.getLotteryListResult(params))//CFConstant.PRODUCT_PLATFORM, "User", "Login", username, password, "1"
                .subscribe(new ResponseSubscriber<AppTextMessageResponseList<LotteryListResult>>() {
                    @Override
                    public void success(AppTextMessageResponseList<LotteryListResult> response) {
                        if (response.isSuccess()) {//目前返回的errno为0需要改成200 代表正确的
                            view.getLotteryListResult(response.getData());
                        } else {
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        if (null != view) {
                            view.showMessage(msg);
                        }
                    }
                }));
    }


    @Override
    public void postCPLotteryList(String dateStr,String dataId) {
        Map<String, String> params = new HashMap<>();
        params.put("terminal_id", CFConstant.PRODUCT_PLATFORM);
        params.put("packet", "Credit");
        params.put("action", "HistoryData");
        params.put("date", dateStr);
        params.put("lottery_id", dataId);
        params.put("token", ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_TOKEN));
        subscriptionHelper.add(RxHelper.addSugar(api.get(params))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<CPLotteryListResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<CPLotteryListResult> response) {
                        view.postCPLotteryListResult(response.getData());
                    }

                    @Override
                    public void fail(String msg) {
                        if(null != view)
                        {
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

        subscriptionHelper.unsubscribe();
    }


}

