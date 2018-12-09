package com.hgapp.a6668.homepage.cplist.bet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgapp.a6668.CPInjections;
import com.hgapp.a6668.Injections;
import com.hgapp.a6668.R;
import com.hgapp.a6668.base.HGBaseDialogFragment;
import com.hgapp.a6668.common.adapters.AutoSizeRVAdapter;
import com.hgapp.a6668.common.util.CalcHelper;
import com.hgapp.a6668.data.CPBetResult;
import com.hgapp.a6668.homepage.cplist.events.CPOrderList;
import com.hgapp.a6668.homepage.cplist.events.CPOrderSuccessEvent;
import com.hgapp.a6668.homepage.handicap.BottombarViewManager;
import com.hgapp.a6668.homepage.handicap.leaguedetail.CalosEvent;
import com.hgapp.a6668.personpage.betrecord.BetRecordFragment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;

public class BetCPOrderDialog extends HGBaseDialogFragment implements CpBetApiContract.View{
    public static final String PARAM0 = "betResult";
    public static final String PARAM1 = "gold";
    public static final String PARAM2 = "game_code";
    public static final String PARAM3 = "round";
    public static final String PARAM4 = "x_session_token";
    @BindView(R.id.betOrderCp)
    RecyclerView betOrderCp;

    @BindView(R.id.betOrderLM)
    LinearLayout betOrderLM;
    @BindView(R.id.betOrderLMNumber)
    TextView betOrderLMNumber;
    @BindView(R.id.betOrderLMZH)
    TextView betOrderLMZH;
    @BindView(R.id.betOrderLMMoneyOne)
    TextView betOrderLMMoneyOne;
    @BindView(R.id.betOrderLMMoney)
    TextView betOrderLMMoney;

    @BindView(R.id.betOrderCpBottom)
    LinearLayout betOrderCpBottom;
    @BindView(R.id.betOrderCpNumber)
    TextView betOrderCpNumber;
    @BindView(R.id.betOrderCpMoney)
    TextView betOrderCpMoney;
    @BindView(R.id.betOrderCpSubmit)
    Button betOrderCpSubmit;
    @BindView(R.id.betOrderCpCancel)
    Button betOrderCpCancel;
    ArrayList<CPOrderList> betResult;
    CPBetParams cpBetParams;
    String userMoney;
    private String betGold="",betType;

    String game_code,  round, totalNums,totalMoney,number,typeCode, x_session_token;
    CpBetApiContract.Presenter presenter;

    public static BetCPOrderDialog newInstance(ArrayList<CPOrderList> cpOrderListArrayList, String gold, String game_code, String round, String x_session_token) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PARAM0, cpOrderListArrayList);
        bundle.putString(PARAM1, gold);
        bundle.putString(PARAM2, game_code);
        bundle.putString(PARAM3, round);
        bundle.putString(PARAM4, x_session_token);
        BetCPOrderDialog dialog = new BetCPOrderDialog();
        dialog.setArguments(bundle);
        CPInjections.inject(null,dialog);
        return dialog;
    }

    public static BetCPOrderDialog newInstances(ArrayList<CPOrderList> cpOrderListArrayList,CPBetParams cpBetParams) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PARAM0, cpOrderListArrayList);
        bundle.putParcelable(PARAM1, cpBetParams);
        BetCPOrderDialog dialog = new BetCPOrderDialog();
        dialog.setArguments(bundle);
        CPInjections.inject(null,dialog);
        return dialog;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_bet_order_cp;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        betResult =  getArguments().getParcelableArrayList(PARAM0);
        cpBetParams = getArguments().getParcelable(PARAM1);
        betGold = cpBetParams.getGold();
        betType = cpBetParams.getType();
        game_code = cpBetParams.getGame_code();
        round =  cpBetParams.getRound();
        x_session_token =  cpBetParams.getX_session_token();

        if("LM".equals(betType)){
            totalNums = cpBetParams.getTypeNumber();
            totalMoney = CalcHelper.multiply(betGold,totalNums)+"";
            typeCode = cpBetParams.getTypeCode();
            betOrderLM.setVisibility(View.VISIBLE);
            betOrderCp.setVisibility(View.GONE);
            betOrderCpBottom.setVisibility(View.GONE);
            int size = betResult.size();
            number ="";
            for(int i=0;i<size;++i){
                number += betResult.get(i).getGid()+",";
            }
            number = number.substring(0,number.length()-1);
            betOrderLMNumber.setText(cpBetParams.getTypeName()+"【"+number+"】");
            betOrderLMZH.setText("组合数："+cpBetParams.getTypeNumber());
            betOrderLMMoneyOne.setText("单注金额："+betGold);
            betOrderLMMoney.setText("总金额："+totalMoney);
        }else{
            totalNums = betResult.size()+"";
            totalMoney = CalcHelper.multiply(betGold,totalNums)+"";
            betOrderLM.setVisibility(View.GONE);
            betOrderCp.setVisibility(View.VISIBLE);
            betOrderCpBottom.setVisibility(View.VISIBLE);
            betOrderCpNumber.setText(totalNums);
            betOrderCpMoney.setText(totalMoney);
            LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
            betOrderCp.setLayoutManager(gridLayoutManager);
            betOrderCp.setHasFixedSize(true);
            betOrderCp.setNestedScrollingEnabled(false);
            betOrderCp.setAdapter(new ZHBetListAdapter(getContext(), R.layout.item_order_cp,betResult));
        }

       /* betGold =  getArguments().getString(PARAM1);
        game_code =  getArguments().getString(PARAM2);
        round =  getArguments().getString(PARAM3);
        x_session_token =  getArguments().getString(PARAM4);*/


    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
    }

    @Override
    public void postCpBetResult(CPBetResult betResult) {
        showMessage(betResult.getMsg());
        EventBus.getDefault().post(new CPOrderSuccessEvent());
        hide();
    }

    @Override
    public void setStart(int action) {

    }

    @Override
    public void setError(int action, int errcode) {

    }

    @Override
    public void setError(int action, String errString) {

    }

    @Override
    public void setComplete(int action) {

    }

    @Override
    public void setPresenter(CpBetApiContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public class ZHBetListAdapter extends AutoSizeRVAdapter<CPOrderList> {
        private Context context;

        public ZHBetListAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            this.context = context;
        }
        @Override
        protected void convert(ViewHolder holder, final CPOrderList rowsBean, int position) {
            holder.setText(R.id.itemZH1,  "【"+rowsBean.getgName()+"】@"+rowsBean.getRate()+" X "+betGold);
            /*TextView textView2 =  holder.getView(R.id.itemZH2);
            TextView textView3 =  holder.getView(R.id.itemZH3);
            textView2.setText(Html.fromHtml(rowsBean.s_mb_team+onMarkRed(rowsBean.sign)+rowsBean.s_tg_team));
            textView3.setText(Html.fromHtml(onMarkRed(rowsBean.s_m_place)+"@"+onMarkRed(rowsBean.w_m_rate)));
            holder.setText(R.id.itemZH4, "" );
            holder.setVisible(R.id.itemZHClear,false);*/
        }
    }

    //标记
    private String onMarkRed(String sign){
        return " <font color='#C9270B'>" + sign+"</font>";
    }

    @OnClick({R.id.betOrderCpCancel,R.id.betOrderCpSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.betOrderCpCancel:
                hide();
                break;
            case R.id.betOrderCpSubmit:
                int size = betResult.size();
                number="";

                if("LM".equals(betType)){
                    for(int i=0;i<size;++i){
                        number += betResult.get(i).getGid()+",";
                    }
                    number = number.substring(0,number.length()-1);
                    presenter.postCpBetsLM(game_code,  round, totalNums,totalMoney,number,betGold,typeCode, x_session_token);
                }else{
                    for(int i=0;i<size;++i){
                        number += "betBean["+betResult.get(i).getPosition()+"][ip_"+betResult.get(i).getGid()+"]: "+betGold+"\n";
                    }
                    presenter.postCpBets(game_code,  round, totalNums,totalMoney,number, x_session_token);
                }
                break;
        }
    }

}
