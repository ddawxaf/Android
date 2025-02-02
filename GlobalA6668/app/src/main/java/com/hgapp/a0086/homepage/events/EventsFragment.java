package com.hgapp.a0086.homepage.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgapp.a0086.HGApplication;
import com.hgapp.a0086.Injections;
import com.hgapp.a0086.R;
import com.hgapp.a0086.base.HGBaseFragment;
import com.hgapp.a0086.common.util.ACache;
import com.hgapp.a0086.common.util.GameShipHelper;
import com.hgapp.a0086.common.util.HGConstant;
import com.hgapp.a0086.common.widgets.RoundRainbowTextView;
import com.hgapp.a0086.common.widgets.redpacket.RedPacketsLayout;
import com.hgapp.a0086.data.DepositAliPayQCCodeResult;
import com.hgapp.a0086.data.PersonBalanceResult;
import com.hgapp.a0086.homepage.UserMoneyEvent;
import com.hgapp.a0086.data.DownAppGiftResult;
import com.hgapp.a0086.data.LuckGiftResult;
import com.hgapp.a0086.homepage.events.anim.Swing;
import com.hgapp.a0086.data.ValidResult;
import com.hgapp.a0086.homepage.events.anim.ZoomOutRightExit;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public  class EventsFragment extends HGBaseFragment implements EventsContract.View {

    private static final String ARG_PARAM0 = "param0";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    @BindView(R.id.eventTitleUserMoney)
    TextView eventTitleUserMoney;
    @BindView(R.id.tvLastEventsFlowings)
    TextView tvLastEventsFlowings;
    @BindView(R.id.tvLastEventsNumber)
    TextView tvLastEventsNumber;
    @BindView(R.id.ivEventRefresh)
    ImageView ivEventRefresh;
    @BindView(R.id.packets_layout)
    RedPacketsLayout packets_layout;
    @BindView(R.id.downAppGold)
    RoundRainbowTextView downAppGold;
    @BindView(R.id.roundtv)
    RoundRainbowTextView roundtv;
    @BindView(R.id.ivClickOldestMember)
    ImageView ivClickOldestMember;
    @BindView(R.id.btnClickRed)
    Button btnClickRed;
    private String payId;
    private String getArgParam1;
    private int getArgParam2;
    Animation animation ;
    private EventsContract.Presenter presenter;
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private RedCustomDialog mRedPacketDialog;
    private boolean isShow = false;
    public static EventsFragment newInstance(DepositAliPayQCCodeResult dataBean, String getArgParam1, int getArgParam2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM0, dataBean);
        args.putString(ARG_PARAM1, getArgParam1);
        args.putInt(ARG_PARAM2, getArgParam2);
        fragment.setArguments(args);
        Injections.inject(null, fragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArgParam1 = getArguments().getString(ARG_PARAM1);
            getArgParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }


    @Override
    public int setLayoutId() {
        return R.layout.fragment_event;
    }


    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        downAppGold.setText(getString(R.string.games_event_mark1)+ACache.get(getContext()).getAsString(HGConstant.DOWNLOAD_APP_GIFT_GOLD)+getString(R.string.comm_roll_money_unit)+"】");
        roundtv.setText(getString(R.string.games_event_mark2)+ACache.get(getContext()).getAsString(HGConstant.DOWNLOAD_APP_GIFT_DEPOSIT)+getString(R.string.games_event_mark22));
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_clockwise);
        eventTitleUserMoney.setText(getArgParam1);
        presenter.postValidGift("","get_valid");
    }


    @Override
    public void showMessage(String message) {
        super.showMessage(message);
    }

    @Override
    public void setPresenter(EventsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void showRedDialog(String data){
        String alias = ACache.get(getContext()).getAsString(HGConstant.USERNAME_ALIAS);
        RedPacketEntity entity = new RedPacketEntity(alias, "http://xxx.xxx.com/20171205180511192.png", getString(R.string.games_event_mark11));
        showRedPacketDialog(entity,data);
    }

    public void showRedPacketDialog(RedPacketEntity entity, final String data) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(getContext(), R.layout.dialog_red_packet, null);
            mRedPacketViewHolder = new RedPacketViewHolder(getContext(), mRedPacketDialogView);
            mRedPacketDialog = new RedCustomDialog(getContext(), mRedPacketDialogView, R.style.red_custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }
        new Swing().start(mRedPacketDialogView);
        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                new ZoomOutRightExit().start(mRedPacketDialogView);
                mRedPacketDialogView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isShow){
                            showMessage(getString(R.string.games_event_mark10));
                        }
                        presenter.postPersonBalance("","");
                        mRedPacketDialog.dismiss();
                        new Swing().start(eventTitleUserMoney);
                    }
                },1000);
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                mRedPacketDialogView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRedPacketViewHolder.setData(data);
                    }
                },2000);
            }
        });
        mRedPacketDialog.show();
    }

    @OnClick({R.id.eventTitleBack,R.id.ivClickOldestMember, R.id.btnClickRed,R.id.ivEventRefresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eventTitleBack:
                pop();
                break;
            case R.id.ivClickOldestMember:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage(getString(R.string.comm_pls_register_real_acccount));
                    return;
                }
                presenter.postDownAppGift("");
                ivClickOldestMember.setClickable(false);
                ivClickOldestMember.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!Check.isNull(ivClickOldestMember)){
                            ivClickOldestMember.setClickable(true);
                        }
                    }
                },3000);
                break;
            case R.id.btnClickRed:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage(getString(R.string.comm_pls_register_real_acccount));
                    return;
                }
                presenter.postLuckGift("","extract_lucky_red_envelope");
                btnClickRed.setClickable(false);
                btnClickRed.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!Check.isNull(btnClickRed)){
                            btnClickRed.setClickable(true);
                        }
                    }
                },3000);
                break;
            case R.id.ivEventRefresh:
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage(getString(R.string.comm_pls_register_real_acccount));
                    return;
                }
                if(null !=ivEventRefresh){
                    ivEventRefresh.startAnimation(animation);
                }
                presenter.postValidGift("","get_valid");
                break;
        }
    }

    @Override
    public void postDownAppGiftResult(final DownAppGiftResult downAppGiftResult) {
        isShow = true;
        packets_layout.setVisibility(View.VISIBLE);
        packets_layout.post(new Runnable() {
            @Override
            public void run() {
                packets_layout.startRain();
            }
        });
        tvLastEventsFlowings.postDelayed(new Runnable() {
            @Override
            public void run() {
                showRedDialog(downAppGiftResult.getData_gold()+"");
            }
        },2000);
        packets_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                packets_layout.stopRain();
                packets_layout.setVisibility(View.GONE);
                GameLog.log("停止下雨了");
            }
        },2500);
    }

    @Override
    public void postLuckGiftResult(final LuckGiftResult luckGiftResult) {
        isShow = false;
        packets_layout.setVisibility(View.VISIBLE);
        packets_layout.post(new Runnable() {
            @Override
            public void run() {
                packets_layout.startRain();
            }
        });
        tvLastEventsFlowings.postDelayed(new Runnable() {
            @Override
            public void run() {
                showRedDialog(luckGiftResult.getData_gold()+"");
            }
        },2000);
        packets_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                packets_layout.stopRain();
                packets_layout.setVisibility(View.GONE);
                GameLog.log("停止下雨了");
            }
        },2500);
        presenter.postValidGift("","get_valid");
    }

    @Override
    public void postValidGiftResult(ValidResult validResult) {
        ivEventRefresh.clearAnimation();
        tvLastEventsFlowings.setText(getString(R.string.games_event_mark3)+validResult.getValid_money());
        tvLastEventsNumber.setText(getString(R.string.games_event_mark4)+validResult.getLast_times());
    }

    @Override
    public void postPersonBalanceResult(PersonBalanceResult personBalance) {
        eventTitleUserMoney.setText(GameShipHelper.formatMoney(personBalance.getBalance_hg()));
        EventBus.getDefault().post(new UserMoneyEvent(GameShipHelper.formatMoney(personBalance.getBalance_hg())));
        GameLog.log("红包的数量"+GameShipHelper.formatMoney(personBalance.getBalance_hg()));
    }
}
