package com.hfcp.hf.ui.home.cplist.quickbet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.hfcp.hf.CPInjections;
import com.hfcp.hf.R;
import com.hfcp.hf.common.utils.Check;
import com.hfcp.hf.common.widget.bottomdialog.NBaseBottomDialog;
import com.hfcp.hf.data.CPQuickBetResult;
import com.hfcp.hf.ui.home.cplist.events.QuickBetMothedEvent;
import com.hfcp.hf.ui.home.cplist.quickbet.mothed.QuickBetMethodFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Daniel on 2017/7/19.
 */

public class QuickBetFragment extends NBaseBottomDialog implements QuickBetContract.View{
    private static final String TYPE1 = "type1";
    private static final String TYPE2 = "type2";
    @BindView(R.id.btnSaveQuickBet1)
    Button btnSaveQuickBet1;
    @BindView(R.id.btnSaveQuickBet2)
    Button btnSaveQuickBet2;
    private QuickBetContract.Presenter presenter;
    CPQuickBetResult cpQuickBetResult;
    QuickBetParam quickBetParam;
    public QuickBetFragment(){}

    public static QuickBetFragment newInstance(CPQuickBetResult cpQuickBetResult, QuickBetParam quickBetParam)
    {
        QuickBetFragment logoutFragment =  new QuickBetFragment();
        Bundle args = new Bundle();
        args.putParcelable(TYPE1, cpQuickBetResult);
        args.putParcelable(TYPE2, quickBetParam);
        logoutFragment.setArguments(args);
        CPInjections.inject((QuickBetContract.View) logoutFragment,null);
        return logoutFragment;
    }

    @OnClick({R.id.btnSaveQuickBet1, R.id.btnSaveQuickBet2,R.id.groupQuickbet,R.id.btnSaveQuickBet})
    public void onViewClicked(View view) {
        switch (view.getId())
        {
            case R.id.btnSaveQuickBet1:
                EventBus.getDefault().post(new QuickBetMothedEvent("1"));
                dismiss();
                break;
            case R.id.btnSaveQuickBet2:
                EventBus.getDefault().post(new QuickBetMothedEvent("2"));
                dismiss();
                break;
            case R.id.groupQuickbet:
                dismiss();
                break;
            case R.id.btnSaveQuickBet:
                if(Check.isEmpty(quickBetParam.code)){
                    showMessage("请选择注单数据！");
                }else{
                    QuickBetMethodFragment.newInstance(cpQuickBetResult,quickBetParam).show(getActivity().getSupportFragmentManager());
                }
                dismiss();
                break;
            default:break;
        }

    }
    @Override
    public int getLayoutRes() {
        return R.layout.layout_quick_bet;
    }

    @Override
    public void bindView(View v) {
        if (null != getArguments()) {
            cpQuickBetResult = getArguments().getParcelable(TYPE1);
            quickBetParam = getArguments().getParcelable(TYPE2);
            if(Check.isNull(cpQuickBetResult)||cpQuickBetResult.getData().size()==0){
                btnSaveQuickBet1.setVisibility(View.GONE);
                btnSaveQuickBet2.setVisibility(View.GONE);
            }else if(cpQuickBetResult.getData().size()==1){
                if(cpQuickBetResult.getData().get(0).getSort().equals("1")){
                    btnSaveQuickBet1.setVisibility(View.VISIBLE);
                    btnSaveQuickBet2.setVisibility(View.GONE);
                }else{
                    btnSaveQuickBet1.setVisibility(View.GONE);
                    btnSaveQuickBet2.setVisibility(View.VISIBLE);
                }
            }else if(cpQuickBetResult.getData().size()==2){
                btnSaveQuickBet1.setVisibility(View.VISIBLE);
                btnSaveQuickBet2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setPresenter(QuickBetContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onStop()
    {
        super.onStop();
        if(null != presenter)
        {
            presenter.destroy();
        }
    }
}
