package com.gmcp.gm.ui.home.cplist.bet;


import com.gmcp.gm.CFConstant;
import com.gmcp.gm.common.http.ResponseSubscriber;
import com.gmcp.gm.common.http.RxHelper;
import com.gmcp.gm.common.http.SubscriptionHelper;
import com.gmcp.gm.common.http.request.AppTextMessageResponse;
import com.gmcp.gm.common.utils.ACache;
import com.gmcp.gm.common.utils.GameLog;
import com.gmcp.gm.data.CPBetResult;

import java.util.Map;

import static com.gmcp.gm.common.utils.Utils.getContext;


public class CpBetApiPresenter implements CpBetApiContract.Presenter {
    private ICpBetApi api;
    private CpBetApiContract.View view;
    private SubscriptionHelper subscriptionHelper = new SubscriptionHelper();

    public CpBetApiPresenter(ICpBetApi api, CpBetApiContract.View  view){
        this.view = view;
        this.api = api;
        this.view.setPresenter(this);
    }


    @Override
    public void postCpBets(String game_code, String round, String totalNums, String totalMoney, String number, Map<String, String> fields, String x_session_token) {
        GameLog.log("投注的信息是 "+x_session_token);
        subscriptionHelper.add(RxHelper.addSugar(api.postCpBets("CreditBet","Credit",x_session_token, ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_TOKEN)))
                .subscribe(new ResponseSubscriber<AppTextMessageResponse<CPBetResult>>() {
                    @Override
                    public void success(AppTextMessageResponse<CPBetResult> response) {
                        if(response.isSuccess()){
                            view.postCpBetResult(response.getData());
                        }else{
                            view.showMessage(response.getDescribe());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        /*if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }*/
                    }
                }));
    }

    @Override
    public void postCpBetsHK(String game_code, String round, String totalNums, String totalMoney, String number,String betmoney,String typecode,String rtype, String x_session_token) {
        subscriptionHelper.add(RxHelper.addSugar(api.postCpBetsHK(game_code,round,totalNums,totalMoney,number,betmoney,typecode,rtype,x_session_token))
                .subscribe(new ResponseSubscriber<CPBetResult>() {
                    @Override
                    public void success(CPBetResult response) {
                        if(response.getCode().equals("200")){
                            view.postCpBetResult(response);
                        }else{
                            view.showMessage(response.getMsg());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        /*if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }*/
                    }
                }));
    }

    @Override
    public void postCpBetsHKMap(String game_code, String round, String totalNums, String totalMoney, String number, Map<String, String> fields, String x_session_token) {
        subscriptionHelper.add(RxHelper.addSugar(api.postCpBetsHKMap(game_code,round,totalNums,totalMoney,number,fields,x_session_token))
                .subscribe(new ResponseSubscriber<CPBetResult>() {
                    @Override
                    public void success(CPBetResult response) {
                        if(response.getCode().equals("200")){
                            view.postCpBetResult(response);
                        }else{
                            view.showMessage(response.getMsg());
                        }
                    }

                    @Override
                    public void fail(String msg) {
                        /*if(null != view)
                        {
                            view.setError(0,0);
                            view.showMessage(msg);
                        }*/
                    }
                }));
    }

    @Override
    public void postCpBetsLM(String game_code, String round, String totalNums, String totalMoney, String number,String betmoney,String typecode, String x_session_token) {
        subscriptionHelper.add(RxHelper.addSugar(api.postCpBetsLM(game_code,round,totalNums,totalMoney,number,betmoney,typecode,x_session_token))
                .subscribe(new ResponseSubscriber<CPBetResult>() {
                    @Override
                    public void success(CPBetResult response) {
                        if(response.getCode().equals("200")){
                            view.postCpBetResult(response);
                        }else{
                            view.showMessage(response.getMsg());
                        }
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

    }

}
