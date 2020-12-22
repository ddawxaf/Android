package com.hgapp.betnew.depositpage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.hgapp.betnew.Injections;
import com.hgapp.betnew.R;
import com.hgapp.betnew.base.HGBaseFragment;
import com.hgapp.betnew.base.IPresenter;
import com.hgapp.betnew.common.util.ACache;
import com.hgapp.betnew.common.util.GameShipHelper;
import com.hgapp.betnew.common.util.HGConstant;
import com.hgapp.betnew.common.widgets.NTitleBar;
import com.hgapp.betnew.data.DepositAliPayQCCodeResult;
import com.hgapp.betnew.data.DepositBankCordListResult;
import com.hgapp.betnew.data.DepositListResult;
import com.hgapp.betnew.data.DepositThirdBankCardResult;
import com.hgapp.betnew.data.DepositThirdQQPayResult;
import com.hgapp.betnew.data.LoginResult;
import com.hgapp.betnew.depositpage.aliqcpay.AliQCPayFragment;
import com.hgapp.betnew.depositpage.companypay.CompanyPayOneFragment;
import com.hgapp.betnew.depositpage.thirdbankcardpay.ThirdbankCardFragment;
import com.hgapp.betnew.depositpage.thirdmobilepay.OnlinePlayFragment;
import com.hgapp.betnew.depositpage.thirdmobilepay.ThirdMobilePayFragment;
import com.hgapp.betnew.depositpage.usdtpay.USDTPayFragment;
import com.hgapp.betnew.homepage.UserMoneyEvent;
import com.hgapp.betnew.personpage.realname.RealNameFragment;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.sample.demo_wechat.event.StartBrotherEvent;

public class DepositFragment extends HGBaseFragment implements DepositeContract.View{

    @BindView(R.id.backDeposite)
    NTitleBar backDeposite;
    /*@BindView(R.id.tvDepositUserMoney)
    TextView tvDepositUserMoney;*/
    @BindView(R.id.lvDeposit)
    ListView lvDeposit;
    private String userMoney;
    private int payId;
    private DepositeContract.Presenter presenter;

    public static DepositFragment newInstance() {
        DepositFragment fragment = new DepositFragment();
        Bundle args = new Bundle();
        Injections.inject(null,fragment);
        fragment.setArguments(args);
        return fragment;
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

        GameLog.log("存款获取的用户余额："+loginResult.getMoney());
        if(!Check.isEmpty(loginResult.getMoney())){
            userMoney = GameShipHelper.formatMoney(loginResult.getMoney());
            //tvDepositUserMoney.setText(userMoney);
        }
    }

    @Subscribe
    public void onEventMain(UserMoneyEvent userMoneyEvent){
        userMoney = userMoneyEvent.money;
        //tvDepositUserMoney.setText(userMoney);
    }

    @Override
    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_deposit;
    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        //userMoney = GameShipHelper.formatMoney(ACache.get(getContext()).getAsString("userMoney"));
        userMoney = ACache.get(getContext()).getAsString("userMoney");
        backDeposite.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backDeposite.setMoreText(userMoney);
    }

    @Override
    public void postDepositListResult(DepositListResult message) {
        GameLog.log("存款方式列表 的数据大小"+message.getData().size());
        lvDeposit.setAdapter(new DepositFragment.DepositListAdapter(getContext(),R.layout.item_deposit,message.getData()));

    }

    @Override
    public void postDepositBankCordListResult(DepositBankCordListResult message) {
        GameLog.log("公司入款："+message.getData().size());
        EventBus.getDefault().post(new StartBrotherEvent(CompanyPayOneFragment.newInstance(message,userMoney), SupportFragment.SINGLETASK));

    }

    @Override
    public void postDepositAliPayQCCodeResult(DepositAliPayQCCodeResult message) {
        GameLog.log("支付宝二维码大小："+message.getData().size()+"金额："+userMoney);
        EventBus.getDefault().post(new StartBrotherEvent(AliQCPayFragment.newInstance(message,userMoney,payId), SupportFragment.SINGLETASK));
    }

    @Override
    public void postDepositUSDTPayCCodeResult(DepositAliPayQCCodeResult message) {

        GameLog.log("USDT："+message.getData().size()+"金额："+userMoney);
        EventBus.getDefault().post(new StartBrotherEvent(USDTPayFragment.newInstance(message.getData().get(0),userMoney), SupportFragment.SINGLETASK));

    }

    @Override
    public void postDepositThirdBankCardResult(DepositThirdBankCardResult message) {
        GameLog.log("第三方银行卡线上："+message.getData().size());
        EventBus.getDefault().post(new StartBrotherEvent(ThirdbankCardFragment.newInstance(message.getData().get(0),userMoney), SupportFragment.SINGLETASK));

    }

    @Override
    public void postDepositThirdWXPayResult(DepositThirdQQPayResult message) {
        GameLog.log("第三方微信支付大小："+message.getData().size());
        EventBus.getDefault().post(new StartBrotherEvent(ThirdMobilePayFragment.newInstance(message,userMoney,payId), SupportFragment.SINGLETASK));

    }

    @Override
    public void postDepositThirdAliPayResult(DepositThirdQQPayResult message) {
        GameLog.log("第三方支付宝支付大小："+message.getData().size());
        EventBus.getDefault().post(new StartBrotherEvent(ThirdMobilePayFragment.newInstance(message,userMoney,payId), SupportFragment.SINGLETASK));

    }

    @Override
    public void postDepositThirdQQPayResult(DepositThirdQQPayResult message) {

        GameLog.log("第三方QQ支付大小："+message.getData().size());

        EventBus.getDefault().post(new StartBrotherEvent(ThirdMobilePayFragment.newInstance(message,userMoney,payId), SupportFragment.SINGLETASK));
    }

    @Override
    public void setPresenter(DepositeContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void onVisible() {
        super.onVisible();
        presenter.postDepositList("");
    }


    public class DepositListAdapter extends com.hgapp.betnew.common.adapters.AutoSizeAdapter<DepositListResult.DataBean> {
        private Context context;

        public DepositListAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            this.context = context;
        }

        @Override
        protected void convert(ViewHolder holder, final DepositListResult.DataBean dataBean, final int position) {
            holder.setText(R.id.tvDepositItem, dataBean.getTitle());
            switch (dataBean.getId()){
                case 0:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_union);
                    break;
                case 1:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_union);
                    break;
                case 2:
                case 10:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_atm);
                    break;
                case 3:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_wechat);
                    break;
                case 4:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_ali);
                    break;
                case 5:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_qq);
                    break;
                case 6:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_ali_code);
                    break;
                case 7:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.deposit_wechat_code);
                    break;
                case 8:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.u_pay);
                    break;
                case 9:
                    holder.setBackgroundRes(R.id.ivDepositItem,R.mipmap.usdt);
                    break;

            }

            holder.setOnClickListener(R.id.llDepositItem, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //EventBus.getDefault().post(new DepositEvent(dataBean.getId()));
                    String alias = ACache.get(getContext()).getAsString(HGConstant.USERNAME_ALIAS);
                    if(Check.isEmpty(alias)){
                        EventBus.getDefault().post(new StartBrotherEvent(RealNameFragment.newInstance(userMoney,""), SupportFragment.SINGLETASK));
                        return;
                    }
                    onListenerDeposit(dataBean.getId(),dataBean.getBankid(),dataBean.getApi());
                }
            });

        }
    }

    private void onListenerDeposit(int id,String bankid,String api){
        payId = id;
        GameLog.log("当前支付的ID是： "+id);
        switch (id){
            case 0://快速充值
                //直接跳转到支付页面
                EventBus.getDefault().post(new StartBrotherEvent(OnlinePlayFragment.newInstance(api,"","","",""), SupportFragment.SINGLETASK));
                break;
            case 1://银行卡线上
                presenter.postDepositThirdBankCard("");
                break;
            case 2://公司入款
                presenter.postDepositBankCordList("");
                break;
            case 3://微信第三方
                presenter.postDepositThirdWXPay("");
                break;
            case 4://支付宝第三方
                presenter.postDepositThirdAliPay("");
                break;
            case 5://QQ第三方
                presenter.postDepositThirdQQPay("");
                break;
            case 6://支付宝扫码
                presenter.postDepositAliPayQCCode("",bankid);
                break;
            case 7://微信扫码
                presenter.postDepositWechatQCCode("",bankid);
                break;
            case 8://云闪付
                presenter.postDepositThirdUQCCode("",bankid);
                break;
            case 9://USDT
                presenter.postDepositThirdUSDTCCode("",bankid);
                break;
            case 10://显示公司入款，实际三方网银，走公司优惠
                presenter.postDepositThirdBankCardYouHui("",bankid);
                break;
        }
    }

}
