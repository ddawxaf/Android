package com.hgapp.a6668;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hgapp.a6668.common.http.Client;
import com.hgapp.a6668.common.http.cphttp.CPClient;
import com.hgapp.a6668.depositpage.DepositPresenter;
import com.hgapp.a6668.depositpage.DepositeContract;
import com.hgapp.a6668.depositpage.IDepositApi;
import com.hgapp.a6668.depositpage.aliqcpay.AliQCPayContract;
import com.hgapp.a6668.depositpage.aliqcpay.AliQCPayPresenter;
import com.hgapp.a6668.depositpage.aliqcpay.IAliQCPayApi;
import com.hgapp.a6668.depositpage.companypay.CompanyPayContract;
import com.hgapp.a6668.depositpage.companypay.CompanyPayPresenter;
import com.hgapp.a6668.depositpage.companypay.ICompanyPayApi;
import com.hgapp.a6668.homepage.HomePageContract;
import com.hgapp.a6668.homepage.HomePagePresenter;
import com.hgapp.a6668.homepage.IHomePageApi;
import com.hgapp.a6668.homepage.aglist.AGListContract;
import com.hgapp.a6668.homepage.aglist.AGListPresenter;
import com.hgapp.a6668.homepage.aglist.IAGListApi;
import com.hgapp.a6668.homepage.aglist.agchange.AGPlatformContract;
import com.hgapp.a6668.homepage.aglist.agchange.AGPlatformPresenter;
import com.hgapp.a6668.homepage.aglist.agchange.IAgPlatformApi;
import com.hgapp.a6668.homepage.cplist.CPListContract;
import com.hgapp.a6668.homepage.cplist.CPListPresenter;
import com.hgapp.a6668.homepage.cplist.ICPListApi;
import com.hgapp.a6668.homepage.cplist.hall.CPHallListContract;
import com.hgapp.a6668.homepage.cplist.hall.CPHallListPresenter;
import com.hgapp.a6668.homepage.cplist.hall.ICPHallListApi;
import com.hgapp.a6668.homepage.cplist.order.CPOrderContract;
import com.hgapp.a6668.homepage.cplist.order.CPOrderPresenter;
import com.hgapp.a6668.homepage.cplist.order.ICPOrderApi;
import com.hgapp.a6668.homepage.events.EventsContract;
import com.hgapp.a6668.homepage.events.EventsPresenter;
import com.hgapp.a6668.homepage.events.IEventsApi;
import com.hgapp.a6668.homepage.handicap.betapi.IPrepareBetApi;
import com.hgapp.a6668.homepage.handicap.betapi.PrepareBetApiContract;
import com.hgapp.a6668.homepage.handicap.betapi.PrepareBetApiPresenter;
import com.hgapp.a6668.homepage.handicap.betapi.zhbetapi.IPrepareZHBetApi;
import com.hgapp.a6668.homepage.handicap.betapi.zhbetapi.PrepareZHBetApiContract;
import com.hgapp.a6668.homepage.handicap.betapi.zhbetapi.PrepareZHBetApiPresenter;
import com.hgapp.a6668.homepage.handicap.leaguedetail.ILeagueDetailSearchListApi;
import com.hgapp.a6668.homepage.handicap.leaguedetail.LeagueDetailSearchListContract;
import com.hgapp.a6668.homepage.handicap.leaguedetail.LeagueDetailSearchListPresenter;
import com.hgapp.a6668.homepage.handicap.leaguedetail.zhbet.IPrepareBetZHApi;
import com.hgapp.a6668.homepage.handicap.leaguedetail.zhbet.PrepareBetZHApiContract;
import com.hgapp.a6668.homepage.handicap.leaguedetail.zhbet.PrepareBetZHApiPresenter;
import com.hgapp.a6668.homepage.handicap.leaguelist.ILeagueSearchListApi;
import com.hgapp.a6668.homepage.handicap.leaguelist.LeagueSearchListContract;
import com.hgapp.a6668.homepage.handicap.leaguelist.LeagueSearchListPresenter;
import com.hgapp.a6668.homepage.handicap.leaguelist.championlist.ChampionDetailListContract;
import com.hgapp.a6668.homepage.handicap.leaguelist.championlist.ChampionDetailListPresenter;
import com.hgapp.a6668.homepage.handicap.leaguelist.championlist.IChampionDetailListApi;
import com.hgapp.a6668.homepage.handicap.saiguo.ISaiGuoApi;
import com.hgapp.a6668.homepage.handicap.saiguo.SaiGuoContract;
import com.hgapp.a6668.homepage.handicap.saiguo.SaiGuoPresenter;
import com.hgapp.a6668.homepage.sportslist.ISportsListApi;
import com.hgapp.a6668.homepage.sportslist.SportsListContract;
import com.hgapp.a6668.homepage.sportslist.SportsListPresenter;
import com.hgapp.a6668.homepage.sportslist.bet.BetContract;
import com.hgapp.a6668.homepage.sportslist.bet.BetPresenter;
import com.hgapp.a6668.homepage.sportslist.bet.IBetApi;
import com.hgapp.a6668.login.fastlogin.ILoginApi;
import com.hgapp.a6668.login.fastlogin.LoginContract;
import com.hgapp.a6668.login.fastlogin.LoginPresenter;
import com.hgapp.a6668.login.fastregister.IRegisterApi;
import com.hgapp.a6668.login.fastregister.RegisterContract;
import com.hgapp.a6668.login.fastregister.RegisterPresenter;
import com.hgapp.a6668.login.forgetpwd.ForgetPwdContract;
import com.hgapp.a6668.login.forgetpwd.ForgetPwdPresenter;
import com.hgapp.a6668.login.forgetpwd.IForgetPwdApi;
import com.hgapp.a6668.personpage.IPersonApi;
import com.hgapp.a6668.personpage.PersonContract;
import com.hgapp.a6668.personpage.PersonPresenter;
import com.hgapp.a6668.personpage.balanceplatform.BalancePlatformContract;
import com.hgapp.a6668.personpage.balanceplatform.BalancePlatformPresenter;
import com.hgapp.a6668.personpage.balanceplatform.IBalancePlatformApi;
import com.hgapp.a6668.personpage.balancetransfer.BalanceTransferContract;
import com.hgapp.a6668.personpage.balancetransfer.BalanceTransferPresenter;
import com.hgapp.a6668.personpage.balancetransfer.IBalanceTransferApi;
import com.hgapp.a6668.personpage.betrecord.BetRecordContract;
import com.hgapp.a6668.personpage.betrecord.BetRecordPresenter;
import com.hgapp.a6668.personpage.betrecord.IBetRecordApi;
import com.hgapp.a6668.personpage.bindingcard.BindingCardContract;
import com.hgapp.a6668.personpage.bindingcard.BindingCardPresenter;
import com.hgapp.a6668.personpage.bindingcard.IBindingCardApi;
import com.hgapp.a6668.personpage.depositrecord.DepositRecordContract;
import com.hgapp.a6668.personpage.depositrecord.DepositRecordPresenter;
import com.hgapp.a6668.personpage.depositrecord.IDepositRecordApi;
import com.hgapp.a6668.personpage.flowingrecord.FlowingRecordContract;
import com.hgapp.a6668.personpage.flowingrecord.FlowingRecordPresenter;
import com.hgapp.a6668.personpage.flowingrecord.IFlowingRecordApi;
import com.hgapp.a6668.personpage.managepwd.IManagePwdApi;
import com.hgapp.a6668.personpage.managepwd.ManagePwdContract;
import com.hgapp.a6668.personpage.managepwd.ManagePwdPresenter;
import com.hgapp.a6668.personpage.realname.IRealNameApi;
import com.hgapp.a6668.personpage.realname.RealNameContract;
import com.hgapp.a6668.personpage.realname.RealNamePresenter;
import com.hgapp.a6668.upgrade.CheckUpdateContract;
import com.hgapp.a6668.upgrade.CheckUpdatePresenter;
import com.hgapp.a6668.upgrade.ICheckVerUpdateApi;
import com.hgapp.a6668.withdrawPage.IWithdrawApi;
import com.hgapp.a6668.withdrawPage.WithDrawPresenter;
import com.hgapp.a6668.withdrawPage.WithdrawContract;

public class Injections {
    private Injections(){}

    /**
     * 向快速登陆Presenter注入登陆视图和登陆接口
     * @param view
     * @return {@linkplain LoginContract.Presenter}
     */
    public static LoginContract.Presenter inject(@NonNull LoginContract.View view, @Nullable ILoginApi loginApi)
    {
        if(null == loginApi)
        {
            loginApi = Client.getRetrofit().create(ILoginApi.class);
        }

        LoginContract.Presenter presenter = new LoginPresenter(loginApi,view);
        return presenter;
    }

    public static RegisterContract.Presenter inject(@NonNull RegisterContract.View view, @Nullable IRegisterApi registerApi)
    {
        if(null == registerApi)
        {
            registerApi = Client.getRetrofit().create(IRegisterApi.class);
        }

        return new RegisterPresenter(registerApi,view);
    }

    public static RealNameContract.Presenter inject(@NonNull RealNameContract.View view, @Nullable IRealNameApi iRealNameApi)
    {
        if(null == iRealNameApi)
        {
            iRealNameApi = Client.getRetrofit().create(IRealNameApi.class);
        }

        return new RealNamePresenter(iRealNameApi,view);
    }

    public static ForgetPwdContract.Presenter inject(@NonNull ForgetPwdContract.View view, @Nullable IForgetPwdApi api)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IForgetPwdApi.class);
        }

        return new ForgetPwdPresenter(api,view);
    }

    public static HomePageContract.Presenter inject(@Nullable IHomePageApi api, @NonNull HomePageContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IHomePageApi.class);
        }

        return new HomePagePresenter(api,view);
    }

    public static LeagueSearchListContract.Presenter inject(@Nullable ILeagueSearchListApi api, @NonNull LeagueSearchListContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(ILeagueSearchListApi.class);
        }

        return new LeagueSearchListPresenter(api,view);
    }

    public static LeagueDetailSearchListContract.Presenter inject(@Nullable ILeagueDetailSearchListApi api, @NonNull LeagueDetailSearchListContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(ILeagueDetailSearchListApi.class);
        }

        return new LeagueDetailSearchListPresenter(api,view);
    }

    public static ChampionDetailListContract.Presenter inject(@Nullable IChampionDetailListApi api, @NonNull ChampionDetailListContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IChampionDetailListApi.class);
        }

        return new ChampionDetailListPresenter(api,view);
    }

    public static AGListContract.Presenter inject(@Nullable IAGListApi api, @NonNull AGListContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IAGListApi.class);
        }

        return new AGListPresenter(api,view);
    }

    public static SportsListContract.Presenter inject(@Nullable ISportsListApi api, @NonNull SportsListContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(ISportsListApi.class);
        }

        return new SportsListPresenter(api,view);
    }


    public static BetContract.Presenter inject(@Nullable IBetApi api, @NonNull BetContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IBetApi.class);
        }

        return new BetPresenter(api,view);
    }

    public static AGPlatformContract.Presenter inject(@Nullable IAgPlatformApi api, @NonNull AGPlatformContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IAgPlatformApi.class);
        }

        return new AGPlatformPresenter(api,view);
    }

    public static PrepareBetApiContract.Presenter inject(@Nullable IPrepareBetApi api, @NonNull PrepareBetApiContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IPrepareBetApi.class);
        }

        return new PrepareBetApiPresenter(api,view);
    }

    public static PrepareBetZHApiContract.Presenter inject(@Nullable IPrepareBetZHApi api, @NonNull PrepareBetZHApiContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IPrepareBetZHApi.class);
        }

        return new PrepareBetZHApiPresenter(api,view);
    }

    public static PrepareZHBetApiContract.Presenter inject(@Nullable IPrepareZHBetApi api, @NonNull PrepareZHBetApiContract.View view)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(IPrepareZHBetApi.class);
        }

        return new PrepareZHBetApiPresenter(api,view);
    }

    public static PersonContract.Presenter inject(@Nullable IPersonApi api ,@NonNull PersonContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IPersonApi.class);
        }
        return new PersonPresenter(api,view);
    }


    public static ManagePwdContract.Presenter inject(@Nullable IManagePwdApi api , @NonNull ManagePwdContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IManagePwdApi.class);
        }
        return new ManagePwdPresenter(api,view);
    }

    public static DepositRecordContract.Presenter inject(@Nullable IDepositRecordApi api , @NonNull DepositRecordContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IDepositRecordApi.class);
        }
        return new DepositRecordPresenter(api,view);
    }

    public static DepositeContract.Presenter inject(@Nullable IDepositApi api , @NonNull DepositeContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IDepositApi.class);
        }
        return new DepositPresenter(api,view);
    }

    public static CompanyPayContract.Presenter inject(@Nullable ICompanyPayApi api , @NonNull CompanyPayContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(ICompanyPayApi.class);
        }
        return new CompanyPayPresenter(api,view);
    }

    public static AliQCPayContract.Presenter inject(@Nullable IAliQCPayApi api , @NonNull AliQCPayContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IAliQCPayApi.class);
        }
        return new AliQCPayPresenter(api,view);
    }

    public static EventsContract.Presenter inject(@Nullable IEventsApi api , @NonNull EventsContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IEventsApi.class);
        }
        return new EventsPresenter(api,view);
    }

    public static BindingCardContract.Presenter inject(@Nullable IBindingCardApi api , @NonNull BindingCardContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IBindingCardApi.class);
        }
        return new BindingCardPresenter(api,view);
    }

    public static WithdrawContract.Presenter inject(@Nullable IWithdrawApi api , @NonNull WithdrawContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IWithdrawApi.class);
        }
        return new WithDrawPresenter(api,view);
    }

    public static BetRecordContract.Presenter inject(@Nullable IBetRecordApi api , @NonNull BetRecordContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IBetRecordApi.class);
        }
        return new BetRecordPresenter(api,view);
    }

    public static SaiGuoContract.Presenter inject(@Nullable ISaiGuoApi api , @NonNull SaiGuoContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(ISaiGuoApi.class);
        }
        return new SaiGuoPresenter(api,view);
    }

    public static FlowingRecordContract.Presenter inject(@Nullable IFlowingRecordApi api , @NonNull FlowingRecordContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IFlowingRecordApi.class);
        }
        return new FlowingRecordPresenter(api,view);
    }

    public static BalanceTransferContract.Presenter inject(@Nullable IBalanceTransferApi api , @NonNull BalanceTransferContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IBalanceTransferApi.class);
        }
        return new BalanceTransferPresenter(api,view);
    }

    public static BalancePlatformContract.Presenter inject(@Nullable IBalancePlatformApi api , @NonNull BalancePlatformContract.View view){
        if(null == api){
            api = Client.getRetrofit().create(IBalancePlatformApi.class);
        }
        return new BalancePlatformPresenter(api,view);
    }


    /**
     * 向检查更新控制器注入视图和网络接口
     * @param view
     * @param api
     * @return
     */
    public static CheckUpdateContract.Presenter inject(@NonNull CheckUpdateContract.View view, @Nullable ICheckVerUpdateApi api)
    {
        if(null == api)
        {
            api = Client.getRetrofit().create(ICheckVerUpdateApi.class);
        }
        return new CheckUpdatePresenter(view,api);
    }

    //彩票的接口
    //----------------------------------------------------------------------------------------------------------------------------------
    public static CPHallListContract.Presenter inject(@NonNull CPHallListContract.View view, @Nullable ICPHallListApi api)
    {
        if(null == api)
        {
            api = CPClient.getRetrofit().create(ICPHallListApi.class);
        }
        return new CPHallListPresenter(api,view);
    }

    public static CPListContract.Presenter inject(@NonNull CPListContract.View view, @Nullable ICPListApi api)
    {
        if(null == api)
        {
            api = CPClient.getRetrofit().create(ICPListApi.class);
        }
        return new CPListPresenter(api,view);
    }

    public static CPOrderContract.Presenter inject(@Nullable ICPOrderApi api, @NonNull CPOrderContract.View view)
    {
        if(null == api)
        {
            api = CPClient.getRetrofit().create(ICPOrderApi.class);
        }

        return new CPOrderPresenter(api,view);
    }

}
