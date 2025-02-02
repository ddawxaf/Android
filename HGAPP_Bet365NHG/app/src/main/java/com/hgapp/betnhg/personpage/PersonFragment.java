package com.hgapp.betnhg.personpage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgapp.betnhg.HGApplication;
import com.hgapp.betnhg.Injections;
import com.hgapp.betnhg.R;
import com.hgapp.betnhg.base.HGBaseFragment;
import com.hgapp.betnhg.base.IPresenter;
import com.hgapp.betnhg.common.event.LogoutEvent;
import com.hgapp.betnhg.common.http.Client;
import com.hgapp.betnhg.common.util.ACache;
import com.hgapp.betnhg.common.util.GameShipHelper;
import com.hgapp.betnhg.common.util.HGConstant;
import com.hgapp.betnhg.data.CPResult;
import com.hgapp.betnhg.data.LoginResult;
import com.hgapp.betnhg.data.NoticeResult;
import com.hgapp.betnhg.data.PersonBalanceResult;
import com.hgapp.betnhg.data.PersonInformResult;
import com.hgapp.betnhg.data.QipaiResult;
import com.hgapp.betnhg.homepage.HomePageIcon;
import com.hgapp.betnhg.homepage.UserMoneyEvent;
import com.hgapp.betnhg.homepage.handicap.ShowMainEvent;
import com.hgapp.betnhg.homepage.noticelist.NoticeListFragment;
import com.hgapp.betnhg.homepage.online.ContractFragment;
import com.hgapp.betnhg.homepage.online.OnlineFragment;
import com.hgapp.betnhg.personpage.accountcenter.AccountCenterFragment;
import com.hgapp.betnhg.personpage.balancetransfer.BalanceTransferFragment;
import com.hgapp.betnhg.personpage.betrecord.BetRecordFragment;
import com.hgapp.betnhg.personpage.bindingcard.BindingCardFragment;
import com.hgapp.betnhg.personpage.depositrecord.DepositRecordFragment;
import com.hgapp.betnhg.personpage.realname.RealNameFragment;
import com.hgapp.betnhg.withdrawPage.WithdrawFragment;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.hgapp.common.util.PackageUtil;
import com.hgapp.common.util.Timber;
import com.hgapp.common.util.Utils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;

public class PersonFragment extends HGBaseFragment implements PersonContract.View {

    @BindView(R.id.tvPersonBack)
    TextView tvPersonBack;
    @BindView(R.id.rvMyList)
    RecyclerView rvMyList;
    @BindView(R.id.tvPersonUsername)
    TextView tvPersonUsername;
    @BindView(R.id.personAgent)
    TextView personAgent;
    @BindView(R.id.personRefresh)
    TextView personRefresh;
    @BindView(R.id.personMyRefresh)
    ImageView personMyRefresh;
    @BindView(R.id.tvPersonHg)
    TextView tvPersonHg;
    @BindView(R.id.personLogout)
    TextView personLogout;
    @BindView(R.id.personJoinDays)
    TextView personJoinDays;
    @BindView(R.id.personVersion)
    TextView personVersion;
    private String personMoney,personJoinDay;
    private NoticeResult noticeResultList;
    private PersonContract.Presenter presenter;
    private static List<HomePageIcon> myList = new ArrayList<HomePageIcon>();
    static {
        myList.add(new HomePageIcon("充值金额",R.mipmap.icon_my_deposit,0));
        myList.add(new HomePageIcon("额度转换",R.mipmap.icon_my_transfer,1));
        myList.add(new HomePageIcon("银行卡",R.mipmap.icon_my_bank_card,2));
        myList.add(new HomePageIcon("提款",R.mipmap.icon_my_withdraw,3));
        //myList.add(new HomePageIcon("平台余额",R.mipmap.icon_my_deal_c,4));
        myList.add(new HomePageIcon("站内信",R.mipmap.icon_my_message,5));
        myList.add(new HomePageIcon("账户中心",R.mipmap.icon_my_psersion,6));
        myList.add(new HomePageIcon("投注记录",R.mipmap.icon_my_deal_record,7));
        myList.add(new HomePageIcon("流水记录",R.mipmap.icon_my_running_record,8));
        myList.add(new HomePageIcon("新手教程",R.mipmap.icon_my_new,9));
        myList.add(new HomePageIcon("联系我们",R.mipmap.icon_my_contract,10));
        myList.add(new HomePageIcon("8M公告",R.mipmap.icon_my_gonggao,11));

    }

    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Injections.inject(null, fragment);
        return fragment;
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        rvMyList.setLayoutManager(gridLayoutManager);
        //rvMyList.addItemDecoration(new GridRvItemDecoration(getContext()));
        rvMyList.setAdapter(new RvMylistAdapter(getContext(),R.layout.item_person,myList));
        PackageInfo packageInfo =  PackageUtil.getAppPackageInfo(Utils.getContext());
        if(null == packageInfo)
        {
            Timber.e("检查更新失败，获取不到app版本号");
            throw new RuntimeException("检查更新失败，获取不到app版本号");
        }
        String localver = packageInfo.versionName;
        GameLog.log("当前APP的版本号是："+localver);
        personVersion.setText("V:"+localver);
    }

    class RvMylistAdapter extends com.hgapp.betnhg.common.adapters.AutoSizeRVAdapter<HomePageIcon>{
        private Context context;
        public RvMylistAdapter(Context context, int layoutId,List<HomePageIcon> datas){
            super(context, layoutId, datas);
            this.context =  context;
        }
        @Override
        protected void convert(ViewHolder holder,final HomePageIcon data,final int position) {
            holder.setText(R.id.tvItemMyName,data.getIconName());
            holder.setImageResource(R.id.ivItemMyImage,data.getIconId());
            holder.setOnClickListener(R.id.llItemMySelf, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameLog.log("用户的金额："+personMoney);
                    switch (data.getId()){
                        case 0://充值金额
                            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }
                            //EventBus.getDefault().post(new StartBrotherEvent(MainFragment.newInstance("person_to_deposit",""), SupportFragment.SINGLETASK));
                            EventBus.getDefault().post(new ShowMainEvent(0));
                            break;
                        case 1://额度转换
                           /* if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }*/
                            EventBus.getDefault().post(new StartBrotherEvent(BalanceTransferFragment.newInstance(personMoney), SupportFragment.SINGLETASK));
                            break;
                        case 2://银行卡
                            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }
                            EventBus.getDefault().post(new StartBrotherEvent(BindingCardFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                            break;
                        case 3://提款
                            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }
                            String alias = ACache.get(getContext()).getAsString(HGConstant.USERNAME_ALIAS);
                            if(Check.isEmpty(alias)){
                                EventBus.getDefault().post(new StartBrotherEvent(RealNameFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                                return;
                            }
                            String userStatus = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT+ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT)+HGConstant.USERNAME_BIND_CARD);
                            //ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT+loginResult.getUserName()+, loginResult.getBindCard_Flag());
                            GameLog.log("用户是否已经绑定过银行卡："+userStatus);
                            if("0".equals(userStatus)){
                                showMessage("请先绑定银行卡！");
                                EventBus.getDefault().post(new StartBrotherEvent(BindingCardFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                            }else{
                                EventBus.getDefault().post(new StartBrotherEvent(WithdrawFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                            }
                            break;
                        case 4:
                            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }
                            EventBus.getDefault().post(new StartBrotherEvent(BalanceTransferFragment.newInstance(personMoney), SupportFragment.SINGLETASK));
                            //EventBus.getDefault().post(new StartBrotherEvent(BalancePlatformFragment.newInstance(personMoney), SupportFragment.SINGLETASK));
                            break;
                        case 5://站内信
                            showMessage("敬请期待！");
                            //EventBus.getDefault().post(new StartBrotherEvent(BalancePlatformFragment.newInstance(personBalance), SupportFragment.SINGLETASK));
                            break;
                        case 6://账户中心
                            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                                showMessage("非常抱歉，请您注册真实会员！");
                                return;
                            }
                            EventBus.getDefault().post(new StartBrotherEvent(AccountCenterFragment.newInstance(personMoney,personJoinDay)));
                            break;
                        case 7://投注记录
                            EventBus.getDefault().post(new StartBrotherEvent(BetRecordFragment.newInstance("today",personMoney), SupportFragment.SINGLETASK));

                            break;
                        case 8://交易记录
                            EventBus.getDefault().post(new StartBrotherEvent(DepositRecordFragment.newInstance("T",personMoney), SupportFragment.SINGLETASK));

                            break;
                        case 9://新手教学
                            EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(personMoney, Client.baseUrl()+ ACache.get(getContext()).getAsString("login_must_tpl_name")+"help.php?tip=app")));
                            break;
                        case 10://联系我们
                            EventBus.getDefault().post(new StartBrotherEvent(ContractFragment.newInstance(personMoney,
                                ACache.get(getContext()).getAsString(HGConstant.USERNAME_SERVICE_URL_QQ),
                                ACache.get(getContext()).getAsString(HGConstant.USERNAME_SERVICE_URL_WECHAT))));
                            break;
                        /*case 10://代理加盟
                            EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(personMoney, Client.baseUrl()+ ACache.get(getContext()).getAsString("login_must_tpl_name")+"agents_reg.php?tip=app")));
                            break;*/
                        case 11://公告
                            if(Check.isNull(noticeResultList)) {
                                presenter.postNoticeList("");
                            }else{
                                EventBus.getDefault().post(new StartBrotherEvent(NoticeListFragment.newInstance(noticeResultList,"","")));
                            }
                            break;
                    }
                }
            });

        }


    }


    @Override
    public void postNoticeListResult(NoticeResult noticeResult) {

        noticeResultList =noticeResult;
        EventBus.getDefault().post(new StartBrotherEvent(NoticeListFragment.newInstance(noticeResultList,"","")));
    }


    @Override
    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }

    @Override
    public void postPersonInformResult(PersonInformResult personInformResult) {

        //tvPersonUsername.setText(personInformResult.getUsername());
        personMoney = GameShipHelper.formatMoney(personInformResult.getBalance_hg());
        personJoinDay = personInformResult.getJoinDays();
        personJoinDays.setText(personInformResult.getJoinDays()+"天");
        GameLog.log("成功获取用户个人信心");
        tvPersonHg.setText(personMoney);
        tvPersonBack.setText(personMoney);
        EventBus.getDefault().post(new UserMoneyEvent(personMoney));
    }

    @Override
    public void postPersonBalanceResult(PersonBalanceResult personBalance) {
        personMoney = GameShipHelper.formatMoney(personBalance.getBalance_hg());
        tvPersonHg.setText(personMoney);
        EventBus.getDefault().post(new UserMoneyEvent(personMoney));
        tvPersonBack.setText(personMoney);
        GameLog.log("成功获取用户余额信息");
    }

    @Override
    public void postQipaiResult(QipaiResult qipaiResult) {

    }

    @Override
    public void postHgQipaiResult(QipaiResult qipaiResult) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMain(LoginResult loginResult) {

        GameLog.log("我的获取的用户余额："+loginResult.getMoney());
        if(!Check.isEmpty(loginResult.getMoney())){
            personMoney = GameShipHelper.formatMoney(loginResult.getMoney());
            tvPersonBack.setText(personMoney);
            if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                tvPersonUsername.setText("试玩玩家");
            }else{
                tvPersonUsername.setText(loginResult.getUserName());
            }
            tvPersonHg.setText(personMoney);
        }
    }

    @Override
    public void postPersonLogoutResult(String message) {
        showMessage(message);
        //EventBus.getDefault().post(new StartBrotherEvent(MainFragment.newInstance("person_to_home",""), SupportFragment.SINGLETASK));
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_STATUS+ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT), "0");
        //ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT, "");
        ACache.get(getContext()).put(HGConstant.APP_CP_COOKIE,"");
        ACache.get(getContext()).put(HGConstant.USERNAME_ALIAS, "");
        ACache.get(getContext()).put(HGConstant.USERNAME_LOGOUT, "true");
        EventBus.getDefault().post(new LogoutEvent(message));
    }

    @Override
    public void postCPResult(CPResult cpResult) {

    }

    @Override
    public void setPresenter(PersonContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if(!Check.isNull(presenter)) {
            //presenter.getPersonBalance("", "");
            presenter.getPersonInform("");
        }
        /*String userStatus = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_STATUS+ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT));
        GameLog.log("用户的登录状态 [ 1登录成功 ] [ 0 未登录 ] ："+userStatus);
        if("1".equals(userStatus)){
            presenter.getPersonBalance("", "");
            presenter.getPersonInform("");
        }else{
            EventBus.getDefault().post(new StartBrotherEvent(LoginFragment.newInstance(), SupportFragment.SINGLETASK));
        }*/
    }
    @OnClick({R.id.personAgent,R.id.personRefresh,R.id.personMyRefresh,R.id.personLogout,R.id.personDeposit,R.id.personDwith,R.id.personDepositC,R.id.personAD})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personAgent:
                EventBus.getDefault().post(new StartBrotherEvent(OnlineFragment.newInstance(personMoney, Client.baseUrl()+ ACache.get(getContext()).getAsString("login_must_tpl_name")+"agents_reg.php?tip=app")));
                break;
            case R.id.personMyRefresh:
            case R.id.personRefresh:
                presenter.getPersonInform("");
                break;
            case R.id.personLogout:
                if(Check.isNull(presenter)){
                    presenter = Injections.inject(null, this);
                }
                presenter.logOut();
                break;
            case R.id.personDeposit://存款
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                //EventBus.getDefault().post(new StartBrotherEvent(MainFragment.newInstance("person_to_deposit",""), SupportFragment.SINGLETASK));
                EventBus.getDefault().post(new ShowMainEvent(0));
                break;
            case R.id.personDwith://取款
                if("true".equals(ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.USERNAME_LOGIN_DEMO))){
                    showMessage("非常抱歉，请您注册真实会员！");
                    return;
                }
                String alias = ACache.get(getContext()).getAsString(HGConstant.USERNAME_ALIAS);
                if(Check.isEmpty(alias)){
                    EventBus.getDefault().post(new StartBrotherEvent(RealNameFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                    return;
                }
                String userStatus = ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT+ACache.get(getContext()).getAsString(HGConstant.USERNAME_LOGIN_ACCOUNT)+HGConstant.USERNAME_BIND_CARD);
                //ACache.get(getContext()).put(HGConstant.USERNAME_LOGIN_ACCOUNT+loginResult.getUserName()+, loginResult.getBindCard_Flag());
                GameLog.log("用户是否已经绑定过银行卡："+userStatus);
                if("0".equals(userStatus)){
                    showMessage("请先绑定银行卡！");
                    EventBus.getDefault().post(new StartBrotherEvent(BindingCardFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                }else{
                    EventBus.getDefault().post(new StartBrotherEvent(WithdrawFragment.newInstance(personMoney,""), SupportFragment.SINGLETASK));
                }
                break;
            case R.id.personDepositC://转账
                EventBus.getDefault().post(new StartBrotherEvent(BalanceTransferFragment.newInstance(personMoney), SupportFragment.SINGLETASK));
                //EventBus.getDefault().post(new StartBrotherEvent(BalancePlatformFragment.newInstance(personMoney), SupportFragment.SINGLETASK));
                break;
            case R.id.personAD://活动
                EventBus.getDefault().post(new ShowMainEvent(1));
                break;
        }
    }
}
