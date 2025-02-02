package com.hfcp.hf.ui.home.bet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coolindicator.sdk.CoolIndicator;
import com.google.gson.Gson;
import com.hfcp.hf.CFConstant;
import com.hfcp.hf.Injections;
import com.hfcp.hf.R;
import com.hfcp.hf.common.adapters.LotteryAdapter;
import com.hfcp.hf.common.adapters.LotteryNumDetailsAdapter;
import com.hfcp.hf.common.base.BaseFragment;
import com.hfcp.hf.common.base.event.StartBrotherEvent;
import com.hfcp.hf.common.utils.ACache;
import com.hfcp.hf.common.utils.CPIWebSetting;
import com.hfcp.hf.common.utils.Check;
import com.hfcp.hf.common.utils.SingleClick;
import com.hfcp.hf.common.utils.TimeTools;
import com.hfcp.hf.common.utils.ToastUtils;
import com.hfcp.hf.common.widget.AnKeyboardUtils;
import com.hfcp.hf.common.widget.LotteryNumPop;
import com.hfcp.hf.common.widget.LotteryPlayMethodPop;
import com.hfcp.hf.common.widget.LotteryTipsPop;
import com.hfcp.hf.common.widget.LotteryTypePop;
import com.hfcp.hf.common.widget.PeriodsTipsPop;
import com.hfcp.hf.data.AllGamesResult;
import com.hfcp.hf.data.BetData;
import com.hfcp.hf.data.BetDataResult;
import com.hfcp.hf.data.BetGameSettingsForRefreshResult;
import com.hfcp.hf.data.GamesTipsResult;
import com.hfcp.hf.data.JSLoginParam;
import com.hfcp.hf.data.LogoutResult;
import com.hfcp.hf.data.UpBetData;
import com.hfcp.hf.ui.home.betGenerate.GenerateMoney;
import com.hfcp.hf.ui.home.betGenerate.GenerateNum;
import com.hfcp.hf.ui.home.betGenerate.JointBetNumber;
import com.hfcp.hf.ui.home.login.fastlogin.LoginFragment;
import com.hfcp.hf.ui.home.sidebar.BackHomeEvent;
import com.hfcp.hf.ui.home.sidebar.LotteryResultEvent;
import com.hfcp.hf.ui.home.sidebar.SideBarFragment;
import com.hfcp.hf.ui.main.MainEvent;
import com.hfcp.hf.ui.me.report.PersonFragment;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xw.repo.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;

public class BetFragment extends BaseFragment implements BetFragmentContract.View {

    private static final String TYPE = "type";
    private static final String LOTTERY_LIST = "LOTTERY_LIST";
    BetFragmentContract.Presenter presenter;

    @BindView(R.id.betTitleBack)
    TextView betTitleBack;
    @BindView(R.id.betTitleName)
    TextView betTitleName;
    @BindView(R.id.betTitleArrows)
    ImageView betTitleArrows;
    @BindView(R.id.betTitleLay)
    LinearLayout betTitleLay;
    @BindView(R.id.betTitleSet)
    ImageView betTitleSet;
    @BindView(R.id.betTitleMenu)
    ImageView betTitleMenu;
    @BindView(R.id.betArea)
    TextView betArea;
    @BindView(R.id.betChat)
    TextView betChat;
    @BindView(R.id.betMethodName)
    TextView betMethodName;
    @BindView(R.id.betMethodDown)
    ImageView betMethodDown;
    @BindView(R.id.betMethodNameLay)
    LinearLayout betMethodNameLay;
    @BindView(R.id.betIssue)
    TextView betIssue;
    @BindView(R.id.betLastIssue)
    TextView betLastIssue;
    @BindView(R.id.betTime)
    TextView betTime;
    @BindView(R.id.betDaysProfit)
    TextView betDaysProfit;
    @BindView(R.id.betModel)
    TextView betModel;
    @BindView(R.id.betTimes)
    EditText betTimes;
    @BindView(R.id.betMinusTxt)
    TextView betMinusTxt;
    @BindView(R.id.betMinus)
    ImageView betMinus;
    @BindView(R.id.betPlus)
    ImageView betPlus;
    @BindView(R.id.betPlusTxt)
    TextView betPlusTxt;
    @BindView(R.id.betClear)
    ImageView betClear;
    @BindView(R.id.betMoney)
    TextView betMoney;
    @BindView(R.id.betSubmit)
    TextView betSubmit;
    @BindView(R.id.betSure)
    TextView betSure;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rv_bet_num)
    RecyclerView rvBetNum;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.rv_lottery)
    RecyclerView rvLottery;
    @BindView(R.id.kv_lottery)
    KeyboardView kvLottery;
    @BindView(R.id.et_lottery)
    EditText etLottery;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.ll_lottery_input)
    LinearLayout llLotteryInput;
    @BindView(R.id.rv_top)
    RecyclerView rvTop;
    @BindView(R.id.bs_bet_bar)
    BubbleSeekBar bsBetBar;
    @BindView(R.id.ll_bet)
    RelativeLayout llBet;
    @BindView(R.id.indicator)
    CoolIndicator indicator;
    @BindView(R.id.wv_service_online)
    WebView wvServiceOnline;
    @BindView(R.id.ll_chart)
    ConstraintLayout llChart;
    @BindView(R.id.flayout_xpay)
    FrameLayout flayoutXpay;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.rl_tips)
    RelativeLayout rlTips;

    private LotteryTypePop mLotteryTypePop;//彩种选择弹窗
    private LotteryNumPop mLotteryNumPop;//开奖号码弹窗
    private LotteryPlayMethodPop mLotteryPlayingMethodPop;//玩法设置弹窗
    private List<BetGameSettingsForRefreshResult.DataBean.WayGroupsBean> wayGroups;//玩法
    private List<UpBetData> mUpdateBet;//投注号码
    private LotteryAdapter lotteryAdapter;
    private String[] position;//记录玩法选择位置，并且判断是否和上次位置一样
    private AnKeyboardUtils keyboardUtils;
    private GenerateMoney generateMoney;
    private int mListSecSize;//重庆时时彩任选模式下单式玩法选择的位数
    private String mOptional;//重庆时时彩任选模式下单式玩法输入的数字
    private RotateAnimation showArrowAnim;
    private RotateAnimation dismissArrowAnim;
    private AllGamesResult.DataBean.LotteriesBean lotteriesBean;//当前选中的某个彩种数据
    private List<AllGamesResult.DataBean.LotteriesBean> lotteriesBeanList;
    private int lottery_id;
    private String nowIssue;//当前投注的期号
    private String newIssue;//最新的期号
    private Double moneyModel = 1.00;//投注模式
    private int multiple = 1;//倍
    private int number = 0;//注数
    private double onePrice;
    private PeriodsTipsPop periodsTipsPop;
    private CountDownTimer mCountDownTimer;
    private CountDownTimer gameTips;
    //赔率的进度条
    private int mProgressMin;
    private int mProgressMax;
    private int mProgress;
    private float percentRate;
    private float mRate;

    private String seat;//设置任选模式下的任选标记
    private StringBuilder extraPosition = new StringBuilder(); //设置任选模式下任选位数

    private CustomDialog betSuccessTips;//投注成功的提示

    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    private String chartUrl;
    private boolean isFirst = true;
    private boolean isFirstVie;
    private boolean tipsPop = true;

    public static BetFragment newInstance(AllGamesResult.DataBean.LotteriesBean lotteriesBean, ArrayList<AllGamesResult.DataBean.LotteriesBean> lotteriesBeanList) {
        BetFragment betFragment = new BetFragment();
        Injections.inject(betFragment, null);
        Bundle args = new Bundle();
        args.putParcelable(TYPE, lotteriesBean);
        args.putParcelableArrayList(LOTTERY_LIST, lotteriesBeanList);
        betFragment.setArguments(args);
        return betFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            lotteriesBean = getArguments().getParcelable(TYPE);
//            lotteriesBeanList = getArguments().getParcelableArrayList(LOTTERY_LIST);
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_bet;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        betTitleName.setText(lotteriesBean.getName());
        lottery_id = lotteriesBean.getLottery_id();
        //弹窗的动画效果
        buildShowArrowAnim();
        buildDismissArrowAnim();
        isFirstVie = true;
        Injections.inject(this, null);
        assert presenter != null;
        presenter.getAllGames();
        periodsTipsPop = new PeriodsTipsPop(_mActivity);
        betModel.setText("元");
        //倍数输入的监听
        betTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    multiple = 0;
                } else {
                    if (wayGroups != null) {
                        int maxMultiple = wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getChildren().get(Integer.valueOf(position[2])).getMax_multiple();
                        if (maxMultiple != 0 && Integer.valueOf(s.toString()) > maxMultiple) {
                            CharSequence charSequence = String.valueOf(maxMultiple).subSequence(0, String.valueOf(maxMultiple).length());
                            s.replace(0, s.toString().length(), charSequence);
                            ToastUtils.showShortToast("当前所选彩种玩法的最大倍投数为" + maxMultiple + "倍");
                        }
                        multiple = Integer.valueOf(s.toString());
                    }
                }
                generateMoney();
            }
        });

        indicator.setMax(100);
        CPIWebSetting.init(wvServiceOnline);
        webViewSetting(wvServiceOnline);
        wvServiceOnline.getSettings().setJavaScriptEnabled(true);
        wvServiceOnline.addJavascriptInterface(new ADInterface(), "AndroidWebView");
        chartUrl = ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_CHAT_ROOM);
    }

    //请求数据
    public void refresh(boolean isRefresh) {
        //当前彩种配置信息
        if (isRefresh) {
            WaitDialog.show((AppCompatActivity) _mActivity, "加载中...");
        }
        presenter.getGameSettingsForRefresh(lottery_id, isRefresh);
        gameTips();
    }

    /**
     * 弹窗动画效果
     */
    //弹窗开始动画
    private void buildShowArrowAnim() {
        if (showArrowAnim != null) return;
        showArrowAnim = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showArrowAnim.setDuration(450);
        showArrowAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        showArrowAnim.setFillAfter(true);
    }

    //弹窗结束动画
    private void buildDismissArrowAnim() {
        if (dismissArrowAnim != null) return;
        dismissArrowAnim = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dismissArrowAnim.setDuration(450);
        dismissArrowAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        dismissArrowAnim.setFillAfter(true);
    }

    //箭头开始动画
    private void showArrowAnim(View view) {
        if (view == null) return;
        view.clearAnimation();
        view.startAnimation(showArrowAnim);
    }

    //箭头结束动画
    private void dismissArrowAnim(View view) {
        if (view == null) return;
        view.clearAnimation();
        view.startAnimation(dismissArrowAnim);
    }

    /**
     * 监听弹窗消失
     */
    //标题弹窗消失
    private BasePopupWindow.OnDismissListener onDismissListener = new BasePopupWindow.OnDismissListener() {

        @Override
        public boolean onBeforeDismiss() {
            dismissArrowAnim(betTitleArrows);
            return super.onBeforeDismiss();
        }

        @Override
        public void onDismiss() {
            String title = lotteriesBeanList.get(mLotteryTypePop.getPosition()).getName();
            if (mLotteryTypePop.getPosition() != -1 && !betTitleName.getText().toString().equals(title)) {
                betTitleName.setText(title);
                lottery_id = lotteriesBeanList.get(mLotteryTypePop.getPosition()).getLottery_id();
                refresh(true);
            }
        }
    };

    //玩法设置弹窗消失
    private BasePopupWindow.OnDismissListener onDismissListenerMet = new BasePopupWindow.OnDismissListener() {

        @Override
        public boolean onBeforeDismiss() {
            dismissArrowAnim(betMethodDown);
            return super.onBeforeDismiss();
        }

        @Override
        public void onDismiss() {
            mLotteryPlayingMethodPop.refresh();
            if (mLotteryPlayingMethodPop.getConfirm()) {
                if (!Arrays.equals(position, mLotteryPlayingMethodPop.getPosition().split(","))) {
                    position = mLotteryPlayingMethodPop.getPosition().split(",");
                    lotteryAdapter.clearList();
                }
                String mBetMethodDetails = wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getChildren().get(Integer.valueOf(position[2])).getName_cn();
                betMethodName.setText(mBetMethodDetails);
                View bottomView = getLayoutInflater().inflate(R.layout.bottom_lottery, (ViewGroup) rvLottery.getParent(), false);
                GenerateNum.generateNum(lottery_id, lotteryAdapter, wayGroups, position, bottomView, rvLottery, llLotteryInput);
                mLotteryPlayingMethodPop.setConfirm(false);
                //输入号码时的监听
                keyboardUtils = new AnKeyboardUtils(_mActivity, kvLottery);
                keyboardUtils.bindEditTextEvent(etLottery);
                etLottery.getText().clear();
                etLottery.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void afterTextChanged(Editable s) {
                        mOptional = s.toString();
                        optional();
                    }
                });
                etLottery.setHint("说明：\n1.每一个号码之间的分割符支持如下符号：\n    回车[ r ] 空格[   ] 逗号[ , ] \n2.有疑问请咨询客服。");
                switch (lottery_id) {
                    case 9:
                    case 14:
                    case 44:
                    case 10:
                    case 19:
                    case 49:
                    case 52:
                        tvTips.setText("程序会自动过滤掉不合法的号码。您也可以点击“自动筛选号码”按钮对输入内容进行格式化。\n该模式下请您输入正确的号码并以分割符进行分割，否则无法筛选您所输入的号码。");
                        break;
                    default:
                        tvTips.setText("程序会自动过滤掉不合法的号码。您也可以点击“自动筛选号码”按钮对输入内容进行格式化。");
                        break;
                }
                rvTop.setLayoutManager(new GridLayoutManager(_mActivity, 5));
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void setGameSettingsForRefreshResult(BetGameSettingsForRefreshResult betGameSettingsForRefreshResult, boolean isRefresh) {
        WaitDialog.dismiss();
        if (betGameSettingsForRefreshResult.getErrno() == 0) {
            //历史开奖弹窗、期数、投注时间、最新一期
            setBetNumber(betGameSettingsForRefreshResult);
            //投注区域
            if (isRefresh) {
                //设置默认玩法选择
                wayGroups = betGameSettingsForRefreshResult.getData().getWayGroups();
                betMethodName.setText(wayGroups.get(0).getChildren().get(0).getChildren().get(0).getName_cn());
                position = new String[]{"0", "0", "0"};
                for (int i = 0; i < wayGroups.size(); i++) {
                    for (int j = 0; j < wayGroups.get(i).getChildren().size(); j++) {
                        for (int k = 0; k < wayGroups.get(i).getChildren().get(j).getChildren().size(); k++) {
                            if (wayGroups.get(i).getChildren().get(j).getChildren().get(k).getName_cn().equals("定位胆")) {
                                betMethodName.setText(wayGroups.get(i).getChildren().get(j).getChildren().get(k).getName_cn());
                                position = new String[]{i + "", j + "", k + ""};
                            }
                        }
                    }
                }
                //投注号码
                rvLottery.setVisibility(View.VISIBLE);
                llLotteryInput.setVisibility(View.GONE);
                rvLottery.setLayoutManager(new LinearLayoutManager(_mActivity));
                ((SimpleItemAnimator) Objects.requireNonNull(rvLottery.getItemAnimator())).setSupportsChangeAnimations(false);
                if (lotteryAdapter != null) {
                    lotteryAdapter.clearList();
                } else {
                    lotteryAdapter = new LotteryAdapter();
                    rvLottery.setAdapter(lotteryAdapter);
                }
                mLotteryPlayingMethodPop = new LotteryPlayMethodPop(_mActivity, wayGroups);
                mLotteryPlayingMethodPop.setOnDismissListener(onDismissListenerMet);
                mLotteryPlayingMethodPop.setDefault(position);
                GenerateNum.generateNum(lotteryAdapter, lottery_id);
                //赔率设定
                mProgressMin = Integer.valueOf(betGameSettingsForRefreshResult.getData().getOptionalPrizes().get(0).getPrize_group());
                mProgressMax = Integer.valueOf(betGameSettingsForRefreshResult.getData().getMaxPrizeGroup());
                bsBetBar.getConfigBuilder().min(mProgressMin).max(mProgressMax).build();
                bsBetBar.setProgress(bsBetBar.getMax());
                mProgress = bsBetBar.getProgress();
                betMinusTxt.setText("0.0%");
                betPlusTxt.setText(String.valueOf(mProgress));
                mRate = 100 * Float.valueOf(betGameSettingsForRefreshResult.getData().getOptionalPrizes().get(0).getRate()) / (mProgressMax - mProgressMin);
                bsBetBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                    @Override
                    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                        mProgress = progress;
                        percentRate = mRate * (mProgressMax - progress);
                        BigDecimal bg = new BigDecimal(percentRate);
                        double percent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        betMinusTxt.setText(percent + "%");
                        betPlusTxt.setText(String.valueOf(progress));
                    }

                    @Override
                    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                    }

                    @Override
                    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                    }
                });
            }
        } else {
            ToastUtils.showShortToast(betGameSettingsForRefreshResult.getError());
            if (betGameSettingsForRefreshResult.getErrno() == 3004) {
                ACache.get(getContext()).put(CFConstant.USERNAME_LOGIN_TOKEN, "");
            }
            finish();
            EventBus.getDefault().post(new MainEvent(0));
        }
    }

    @Override
    public void setAllGamesResult(AllGamesResult allGamesResult) {
        if (allGamesResult.getErrno() == 0) {
            lotteriesBeanList = allGamesResult.getData().getAvailableLottery();
            //彩种选择弹窗
            mLotteryTypePop = new LotteryTypePop(_mActivity, lotteriesBeanList, betTitleName.getText().toString());
            mLotteryTypePop.setOnDismissListener(onDismissListener);
        } else {
            ToastUtils.showShortToast(allGamesResult.getError());
        }
    }

    //投注结果
    @Override
    public void setBetResult(final BetDataResult betDataResult) {
        WaitDialog.dismiss();
        if (betDataResult.getErrno() == 0) {
            lotteryAdapter.clearList();
            etLottery.getText().clear();
            bsBetBar.setProgress(bsBetBar.getMax());
            betSuccessTips = CustomDialog.show((AppCompatActivity) _mActivity, R.layout.layout_bet_success_tips, new CustomDialog.OnBindView() {
                @Override
                public void onBind(CustomDialog dialog, View v) {
                    v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            betSuccessTips.doDismiss();
                        }
                    });
                }
            });
        } else {
            betSubmit.setClickable(true);
            betSubmit.setBackgroundResource(R.drawable.btn_bet_submit);
            betSuccessTips = CustomDialog.show((AppCompatActivity) _mActivity, R.layout.layout_bet_success_tips, new CustomDialog.OnBindView() {
                @Override
                public void onBind(CustomDialog dialog, View v) {
                    v.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            betSuccessTips.doDismiss();
                        }
                    });
                    TextView tvTips = v.findViewById(R.id.tv_tips);
                    ImageView ivTips = v.findViewById(R.id.iv_tips);
                    tvTips.setText(betDataResult.getError());
                    ivTips.setImageResource(R.mipmap.ic_bet_failure_tips);
                }
            });
        }
    }

    @Override
    public void setGamesTipsResult(GamesTipsResult gamesTipsResult) {
        if (gamesTipsResult.getErrno() == 0) {
            if (gamesTipsResult.getData().size() != 0){
                //是否中奖提示弹窗
                LotteryTipsPop mLotteryTipsPop = new LotteryTipsPop(_mActivity, gamesTipsResult.getData());
                Animation enterAnimation = createVerticalAnimation(1f, 0);
                Animation dismissAnimation = createVerticalAnimation(0, 1f);
                int gravity = Gravity.TOP;
                mLotteryTipsPop.setShowAnimation(enterAnimation).setDismissAnimation(dismissAnimation).setPopupGravity(gravity).showPopupWindow(rlTips);
            }
        }
    }

    @Override
    public void setPresenter(BetFragmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //先判断是否登录  如果没有登录 需要登录然后在显示这个界面
        String token = ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_TOKEN);
        if (Check.isEmpty(token)) {
            finish();
            EventBus.getDefault().post(new MainEvent(0));
            EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance()));
        } else {
            refresh(isFirstVie);
            isFirstVie = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        gameTips.cancel();
    }

    @Subscribe
    public void onEventMain(LotteryResultEvent lotteryResultEvent) {
        finish();
    }

    @Subscribe
    public void onEventMain(BackHomeEvent backHomeEvent) {
        finish();
    }

    @Subscribe
    public void onEventMain(LogoutResult logoutResult) {
        ACache.get(getContext()).put(CFConstant.USERNAME_LOGIN_TOKEN, "");
        EventBus.getDefault().post(new MainEvent(0));
        finish();
    }

    //非任选且不为单式时刷新投注注数及金额
    @Subscribe
    public void onEventMain(List<UpBetData> updateBet) {
        mUpdateBet = updateBet;
        generateMoney = new GenerateMoney(lottery_id, wayGroups, position, mUpdateBet);
        number = generateMoney.generateMoney();
        generateMoney();
    }

    //重庆时时彩任选且为单式时刷新投注注数及金额
    @Subscribe
    public void onEventMain(OptionalSizeEvent optionalSizeEvent) {
        mListSecSize = optionalSizeEvent.getSize();
        mUpdateBet.get(0).setListSec(optionalSizeEvent.getListSec());
        optional();
    }

    //重庆时时彩任选模式下单式的计算注数
    private void optional() {
        generateMoney = new GenerateMoney(lottery_id, wayGroups, position, mOptional, mListSecSize);
        number = generateMoney.generateMoney();
        generateMoney();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(false);
        tipsPop = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        gameTips.cancel();
        tipsPop = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        gameTips.cancel();
        EventBus.getDefault().unregister(this);
        try {
            if (!Check.isNull(wvServiceOnline)) {
                ViewParent parent = wvServiceOnline.getParent();
                if (!Check.isNull(parent)) {
                    ((ViewGroup) parent).removeAllViews();
                }
                wvServiceOnline.stopLoading();
                wvServiceOnline.clearHistory();
                wvServiceOnline.removeAllViews();
                wvServiceOnline.destroy();
                wvServiceOnline = null;
                System.gc();
            }
        } catch (Exception ignored) {
        }
    }

    @SingleClick
    @SuppressLint({"SetTextI18n"})
    @OnClick({R.id.betTitleBack, R.id.betTitleLay, R.id.betTitleSet, R.id.betDaysProfit, R.id.betTitleMenu, R.id.betArea, R.id.betChat, R.id.betMethodNameLay, R.id.betModel, R.id.betMinus, R.id.betPlus, R.id.betClear, R.id.betSubmit, R.id.betSure, R.id.tv_delete, R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.betTitleBack:
                finish();
                break;
            case R.id.betTitleLay:
                showArrowAnim(betTitleArrows);
                assert mLotteryTypePop != null;
                if (rlTitle != null) {
                    mLotteryTypePop.showPopupWindow(rlTitle);
                }
                break;
            case R.id.betTitleSet:
                Animation enterAnimation = createVerticalAnimation(-1f, 0);
                Animation dismissAnimation = createVerticalAnimation(0, -1f);
                int gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.pop_lottery_info)
                        .config(new QuickPopupConfig()
                                .clipChildren(true)
                                .withShowAnimation(enterAnimation)
                                .withDismissAnimation(dismissAnimation)
                                .gravity(gravity)
                                .withClick(R.id.tv_trend, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        EventBus.getDefault().post(new MainEvent(3));
                                    }
                                }, true)
                                .withClick(R.id.tv_issue, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        EventBus.getDefault().post(new MainEvent(3));
                                    }
                                }, true)
                                .withClick(R.id.tv_explain, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EventBus.getDefault().post(new StartBrotherEvent(ExplainWebFragment.newInstance(lottery_id), SupportFragment.SINGLETASK));
                                    }
                                }, true))
                        .show(betTitleSet);
                break;
            case R.id.betTitleMenu:
                SideBarFragment.newInstance().show(getFragmentManager());
                break;
            case R.id.betDaysProfit:
                EventBus.getDefault().post(new StartBrotherEvent(PersonFragment.newInstance("", "")));
                break;
            case R.id.betArea:
                tipsPop = true;
                refresh(false);
                llBet.setVisibility(View.VISIBLE);
                llChart.setVisibility(View.GONE);
                betArea.setTextColor(getResources().getColor(R.color.text_bet_clicked));
                betChat.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.betChat:
                tipsPop = false;
                if (Check.isEmpty(chartUrl)) {
                    chartUrl = "http://fh6630.com/room/test22.php";
                }
                if (chartUrl != null && isFirst) {
                    wvServiceOnline.loadUrl(chartUrl);
                    wvServiceOnline.loadUrl("javascript:callJS()");
                    isFirst = false;
                }
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                    mCountDownTimer = null;
                }
                gameTips.cancel();
                periodsTipsPop.dismiss();
                llBet.setVisibility(View.GONE);
                llChart.setVisibility(View.VISIBLE);
                betArea.setTextColor(getResources().getColor(R.color.black));
                betChat.setTextColor(getResources().getColor(R.color.text_bet_clicked));
                break;
            case R.id.betMethodNameLay:
                if (wayGroups != null) {
                    showArrowAnim(betMethodDown);
                    mLotteryPlayingMethodPop.showPopupWindow(rlInfo);
                }
                break;
            case R.id.betModel:
                enterAnimation = createVerticalAnimation(1f, 0);
                dismissAnimation = createVerticalAnimation(0, 1f);
                gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.pop_lottery_betmodel)
                        .config(new QuickPopupConfig()
                                .clipChildren(true)
                                .withShowAnimation(enterAnimation)
                                .withDismissAnimation(dismissAnimation)
                                .gravity(gravity)
                                .withClick(R.id.tv_yuan, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        moneyModel = 1.00;
                                        betModel.setText("元");
                                        generateMoney();
                                    }
                                }, true)
                                .withClick(R.id.tv_jiao, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        moneyModel = 0.10;
                                        betModel.setText("角");
                                        generateMoney();
                                    }
                                }, true))
                        .show(betModel);
                break;
            case R.id.betMinus:
                mProgress--;
                if (mProgress <= mProgressMin) {
                    mProgress = mProgressMin;
                }
                setRate(mProgress);
                break;
            case R.id.betPlus:
                mProgress++;
                if (mProgress >= mProgressMax) {
                    mProgress = mProgressMax;
                }
                setRate(mProgress);
                break;
            case R.id.betClear:
                if (lotteryAdapter != null) {
                    lotteryAdapter.clearList();
                }
                if (etLottery != null) {
                    etLottery.getText().clear();
                }
                break;
            case R.id.betSubmit:
                if (mUpdateBet != null && wayGroups != null && position != null) {
                    WaitDialog.show((AppCompatActivity) _mActivity, "提交中...");
                    betSubmit.setClickable(false);
                    betSubmit.setBackgroundResource(R.drawable.btn_bet_submit_no);
                    BetData betData = new BetData();
                    betData.setGameId(lottery_id);
                    betData.setTraceStopValue(1);
                    betData.setIsTrace(0);
                    Map<String, Integer> mapOrders = new HashMap<>();
                    mapOrders.put(nowIssue, 1);
                    betData.setOrders(JSON.toJSON(mapOrders));
                    List<BetData.BallsBean> list = new ArrayList<>();
                    BetData.BallsBean ballsBean = new BetData.BallsBean();
                    ballsBean.setJsId(0);
                    ballsBean.setMultiple(multiple);
                    ballsBean.setMoneyunit(moneyModel * 0.5);
                    ballsBean.setBall(JointBetNumber.jointNum(mUpdateBet, lottery_id, wayGroups, position));
                    ballsBean.setWayId(wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getChildren().get(Integer.valueOf(position[2])).getSeries_way_id());
                    ballsBean.setNum(number);
                    ballsBean.setViewBalls("");
                    ballsBean.setType(wayGroups.get(Integer.valueOf(position[0])).getName_en() + "." + wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getName_en() + "." + wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getChildren().get(Integer.valueOf(position[2])).getName_en());
                    ballsBean.setPrizeGroup(mProgress);
                    Map<String, String> mapExtra = new HashMap<>();
                    if (mUpdateBet.get(0).getListSec().size() != 0) {
                        setExtraParameter();
                        mapExtra.put("position", extraPosition.toString());
                        mapExtra.put("seat", seat);
                    }
                    ballsBean.setExtra(JSON.toJSON(mapExtra));
                    list.add(ballsBean);
                    betData.setBalls(list);
                    betData.setAmount(onePrice);
                    betData.setTraceWinStop(1);
                    String betJson = JSON.toJSONString(betData);
                    presenter.getBet(lottery_id, betJson);
                }
                break;
            case R.id.betSure:

                break;
            case R.id.tv_delete:
                if (!TextUtils.isEmpty(etLottery.getText().toString())) {
                    switch (lottery_id) {
                        case 9:
                        case 10:
                        case 14:
                        case 19:
                        case 44:
                        case 49:
                        case 52:
                            String lotteryNum = generateMoney.setPopup(_mActivity);
                            etLottery.setText(lotteryNum.substring(1, lotteryNum.length() - 1));
                            etLottery.setSelection(etLottery.getText().length());
                            break;
                        default:
                            etLottery.setText(generateMoney.setPopup(_mActivity).replace("[", "").replace("]", ""));
                            etLottery.setSelection(etLottery.getText().length());
                            break;
                    }
                } else {
                    ToastUtils.showShortToast("您还没有输入号码");
                }
                break;
            case R.id.tv_clear:
                etLottery.getText().clear();
                break;
            default:
                break;
        }
    }

    //设置重庆时时彩任选模式下的请求参数
    private void setExtraParameter() {
        if (lottery_id == 1 || lottery_id == 13 || lottery_id == 16 || lottery_id == 28 || lottery_id == 53) {
            extraPosition.setLength(0);
            if (wayGroups.get(Integer.valueOf(position[0])).getId() == 93) {
                extraPosition.append(mUpdateBet.get(0).getListSec().toString().replaceAll(" ", "").replaceAll(",", "").replace("[", "").replace("]", ""));
                //任选标识
                switch (wayGroups.get(Integer.valueOf(position[0])).getChildren().get(Integer.valueOf(position[1])).getId()) {
                    case 94:
                        seat = "2";
                        break;
                    case 95:
                        seat = "3";
                        break;
                    case 96:
                        seat = "4";
                        break;
                }
            }
        }
    }

    //设置开奖号码、当前期数、最新期数以及倒计时
    @SuppressLint("SetTextI18n")
    private void setBetNumber(BetGameSettingsForRefreshResult betGameSettingsForRefreshResult) {
        LinearLayoutManager betNum = new LinearLayoutManager(_mActivity);
        betNum.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (rvBetNum != null) {
            rvBetNum.setLayoutManager(betNum);
        }
        LotteryNumDetailsAdapter lotteryNumAdapter;
        List<String> numList = new ArrayList<>();
        if (TextUtils.isEmpty(betGameSettingsForRefreshResult.getData().getIssueHistory().getIssues().get(0).getWn_number())) {
            numList.add("开奖中...");
            lotteryNumAdapter = new LotteryNumDetailsAdapter(R.layout.item_lottery_details_txt, numList);
            new CountDownTimer(3 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    refresh(false);
                }
            }.start();
        } else {
            numList.addAll(Arrays.asList(betGameSettingsForRefreshResult.getData().getIssueHistory().getIssues().get(0).getWn_number().split(",")));
            if (numList.size() > 5) {
                numList.clear();
                numList.add("点击查看开奖结果");
                lotteryNumAdapter = new LotteryNumDetailsAdapter(R.layout.item_lottery_details_txt, numList);
            } else {
                lotteryNumAdapter = new LotteryNumDetailsAdapter(R.layout.item_lottery_num_details, numList);
            }
        }
        if (rvBetNum != null) {
            rvBetNum.setAdapter(lotteryNumAdapter);
        }
        mLotteryNumPop = new LotteryNumPop(_mActivity, betGameSettingsForRefreshResult.getData().getIssueHistory().getIssues());
        lotteryNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mLotteryNumPop.showPopupWindow(rlInfo);
            }
        });
        try {
            betIssue.setText("第" + betGameSettingsForRefreshResult.getData().getIssueHistory().getIssues().get(0).getIssue() + "期");
            betLastIssue.setText("第" + betGameSettingsForRefreshResult.getData().getIssueHistory().getCurrent_issue() + "期");
            nowIssue = betGameSettingsForRefreshResult.getData().getIssueHistory().getCurrent_issue();
            newIssue = betGameSettingsForRefreshResult.getData().getGameNumbers().get(1).getNumber();
            long millisFuture = (long) betGameSettingsForRefreshResult.getData().getCurrentNumberTime() * 1000 - (long) betGameSettingsForRefreshResult.getData().getCurrentTime() * 1000;
            if (millisFuture != 0) {
                initCountDownTimer(millisFuture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置赔率
    @SuppressLint("SetTextI18n")
    private void setRate(int progress) {
        percentRate = mRate * (mProgressMax - progress);
        BigDecimal bg = new BigDecimal(percentRate);
        double percent = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        bsBetBar.setProgress(mProgress);
        betMinusTxt.setText(percent + "%");
        betPlusTxt.setText(String.valueOf(progress));
    }

    //监听开奖倒计时
    private void initCountDownTimer(long millisInFuture) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (betTime != null) {
                    betTime.setText(TimeTools.getCountTimeByLong(millisUntilFinished));
                }
            }

            public void onFinish() {
                if (!periodsTipsPop.isShowing()) {
                    periodsTipsPop.setPeriods(newIssue);
                    if (tipsPop) {
                        periodsTipsPop.showPopupWindow();
                    }
                    refresh(false);
                }
            }
        }.start();
    }

    //查询是否中奖
    private void gameTips() {
        if (gameTips != null) {
            gameTips.cancel();
            gameTips = null;
        }
        gameTips = new CountDownTimer(24 * 60 * 60 * 1000, 3 * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                presenter.getGamesTips();
            }

            public void onFinish() {
            }
        }.start();
    }

    //计算注数以及投注金额
    @SuppressLint("SetTextI18n")
    private void generateMoney() {
        if (number * multiple * moneyModel == 0) {
            if (betSubmit != null) {
                betSubmit.setClickable(false);
                betSubmit.setBackgroundResource(R.drawable.btn_bet_submit_no);
                betMoney.setText(number + "注," + multiple + "倍,共" + "0元");
            }
        } else {
            if (betSubmit != null) {
                betSubmit.setClickable(true);
                betSubmit.setBackgroundResource(R.drawable.btn_bet_submit);
                BigDecimal bg = new BigDecimal(number * multiple * moneyModel).setScale(2, RoundingMode.DOWN);
                onePrice = bg.doubleValue();
                betMoney.setText(number + "注," + multiple + "倍,共" + onePrice + "元");
            }
        }
    }

    //返回键的监听
    @Override
    public boolean onBackPressedSupport() {
        if (keyboardUtils != null && keyboardUtils.isShow()) {
            keyboardUtils.hideKeyBoard();
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }

    //模式选择的弹窗动画
    private Animation createVerticalAnimation(float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                fromY,
                Animation.RELATIVE_TO_SELF,
                toY);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    public class ADInterface {

        @JavascriptInterface
        public void chatRoomUserLoginInfo(String foo) {
            //必须开启线程进行JS调用
            wvServiceOnline.post(new Runnable() {
                @Override
                public void run() {
                    JSLoginParam userInformJS = new JSLoginParam();
                    userInformJS.setUsername(ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_ACCOUNT));
                    userInformJS.setPassword(ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_PWD));
                    userInformJS.setApi_id("1");
                    userInformJS.setParent(ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_PARENT_ID));
                    userInformJS.setRoomid("0");
                    userInformJS.setHiddenheader("0");
                    userInformJS.setToken(ACache.get(getContext()).getAsString(CFConstant.USERNAME_LOGIN_TOKEN));
                    String userInformJson = new Gson().toJson(userInformJS);
                    if (!Check.isNull(wvServiceOnline)) {
                        wvServiceOnline.loadUrl("javascript:chatRoomUserLoginInfo('" + userInformJson + "')");
                    }
                }
            });
        }
    }

    private void webViewSetting(WebView webView) {

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                if (indicator != null) {
                    indicator.start();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (indicator != null) {
                    indicator.complete();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            IX5WebChromeClient.CustomViewCallback customViewCallback;

            @Override
            public void onHideCustomView() {
                if (null != customViewCallback) {
                    customViewCallback.onCustomViewHidden();
                }
                wvServiceOnline.setVisibility(View.VISIBLE);
                flayoutXpay.removeAllViews();
                flayoutXpay.setVisibility(View.GONE);
                super.onHideCustomView();
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                wvServiceOnline.setVisibility(View.GONE);
                this.customViewCallback = customViewCallback;
                flayoutXpay.removeAllViews();
                flayoutXpay.setVisibility(View.VISIBLE);
                flayoutXpay.addView(view);
                super.onShowCustomView(view, customViewCallback);
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                BetFragment.this.uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }
        });
    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "cf_better"), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                if (null != uploadFile) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFile.onReceiveValue(result);
                    uploadFile = null;
                }
                if (null != uploadFiles) {
                    Uri result = data == null ? null
                            : data.getData();
                    uploadFiles.onReceiveValue(new Uri[]{result});
                    uploadFiles = null;
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
        }
    }
}