package com.gmcp.gm.ui.main;

import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmcp.gm.CFApplication;
import com.gmcp.gm.CFConstant;
import com.gmcp.gm.Injections;
import com.gmcp.gm.R;
import com.gmcp.gm.common.base.IPresenter;
import com.gmcp.gm.common.base.event.StartBrotherEvent;
import com.gmcp.gm.common.utils.ACache;
import com.gmcp.gm.common.utils.Check;
import com.gmcp.gm.common.utils.GameLog;
import com.gmcp.gm.common.utils.PackageUtil;
import com.gmcp.gm.common.utils.ToastUtils;
import com.gmcp.gm.common.utils.Utils;
import com.gmcp.gm.common.widget.NoTouchViewPager;
import com.gmcp.gm.data.CheckUpgradeResult;
import com.gmcp.gm.ui.chat.ChatFragment;
import com.gmcp.gm.ui.event.EventFragment;
import com.gmcp.gm.ui.home.HomeFragment;
import com.gmcp.gm.ui.lottery.LotteryResultFragment;
import com.gmcp.gm.ui.main.upgrade.CheckUpdateContract;
import com.gmcp.gm.ui.main.upgrade.UpgradeDialog;
import com.gmcp.gm.ui.me.MeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.yokeyword.fragmentation.SupportFragment;

public class MainFragment extends SupportFragment implements CheckUpdateContract.View {//implements HomeContract.View

    @BindView(R.id.viewPager)
    NoTouchViewPager viewPager;
    @BindView(R.id.tab)
    PageNavigationView tab;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIVE = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];

    //List<SupportFragment> stringList = new ArrayList<SupportFragment>();
    CheckUpdateContract.Presenter presenter;


    public static MainFragment newInstance() {
        MainFragment homeFragment = new MainFragment();
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = ChatFragment.newInstance();
            mFragments[THIRD] = EventFragment.newInstance();
            mFragments[FOURTH] = LotteryResultFragment.newInstance();
            mFragments[FIVE] = MeFragment.newInstance();
            /*loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);*/
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(HomeFragment.class);
            mFragments[SECOND] = findChildFragment(ChatFragment.class);
            mFragments[THIRD] = findChildFragment(EventFragment.class);
            mFragments[FOURTH] = findChildFragment(LotteryResultFragment.class);
            mFragments[FIVE] = findChildFragment(MeFragment.class);
        }

        /*stringList.add(HomeFragment.newInstance());
        stringList.add(ChatFragment.newInstance());
        stringList.add(EventFragment.newInstance());
        stringList.add(LotteryResultFragment.newInstance());
        stringList.add(MeFragment.newInstance());*/

        setEvents(savedInstanceState);
        return view;
    }


    private void setEvents(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //presenters();
        presenter = Injections.inject(this, null);
        presenter.checkupdate();

        NavigationController navigationController = tab.custom()
                .addItem(newItem(R.drawable.main_game,R.drawable.main_game_click,"游戏大厅"))
                .addItem(newItem(R.drawable.main_chat,R.drawable.main_chat_click,"在线聊天"))
                .addItem(newRoundItem(R.drawable.main_activity,R.drawable.main_activity_click,"优惠活动"))
                .addItem(newItem(R.drawable.main_lottery,R.drawable.main_lottery_click,"开奖结果"))
                .addItem(newItem(R.drawable.main_me,R.drawable.main_me_click,"用户中心"))
                .build();

        viewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager(),mFragments));
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        /*presenter.postBanner("");
        presenter.postNotice("","1");
        presenter.postWinNews("",System.currentTimeMillis()+"");*/
    }

    @Override
    public void wantShowMessage(CheckUpgradeResult checkUpgradeResult) {

        PackageInfo packageInfo = PackageUtil.getAppPackageInfo(Utils.getContext());
        if(Check.isNull(packageInfo)){
            GameLog.log("检查更新失败，获取不到app版本号");
            throw new RuntimeException("检查更新失败，获取不到app版本号");
        }
        ACache.get(getContext()).put(CFConstant.USERNAME_SERVICE_URL,checkUpgradeResult.getCustom_service());
        ACache.get(getContext()).put("USERNAME_SERVICE_Is_reg_qq",checkUpgradeResult.getIs_reg_qq());
        ACache.get(getContext()).put("USERNAME_SERVICE_isOpenGuest",checkUpgradeResult.getIsOpenGuest());
        ACache.get(getContext()).put("isOpenGuest_ky_try_game_url",checkUpgradeResult.getKy_trygame_url());
        ACache.get(getContext()).put("isOpenGuest_ly_try_game_url",checkUpgradeResult.getLy_trygame_url());
        String localver = packageInfo.versionName;
        GameLog.log("当前APP的版本号是："+localver);
        if(!localver.equals(checkUpgradeResult.getVersion())){
            UpgradeDialog.newInstance(checkUpgradeResult).show(getFragmentManager());
        }
    }

    /*@Override
    public void showMessage(String message) {
        showMessage(message);
    }*/

    @Override
    public void setPresenter(CheckUpdateContract.Presenter presenter) {
        this.presenter = presenter;
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private SupportFragment[] mFragments;

        public MyViewPagerAdapter(FragmentManager fm, SupportFragment[] fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }


    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text){
        SpecialTab mainTab = new SpecialTab(CFApplication.instance().getApplicationContext());
        mainTab.initialize(drawable,checkedDrawable,text);
        mainTab.setTextDefaultColor(Color.parseColor("#404040"));
        mainTab.setTextCheckedColor(0xFFFF0000);
        return mainTab;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable,int checkedDrawable,String text){
        SpecialTabRound mainTab = new SpecialTabRound(CFApplication.instance().getApplicationContext());
        mainTab.initialize(drawable,checkedDrawable,text);
        mainTab.setTextDefaultColor(Color.parseColor("#404040"));
        mainTab.setTextCheckedColor(0xFFFF0000);
        return mainTab;
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment, event.launchmode);
    }

   @Override
    public void showMessage(String message) {
        ToastUtils.showLongToast(message);
    }

   /*  @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }*/

    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMain(MainEvent mainEvent) {
        viewPager.setCurrentItem(mainEvent.postion);
    }



}
