package com.hgapp.a6668.homepage.cplist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgapp.a6668.CPInjections;
import com.hgapp.a6668.HGApplication;
import com.hgapp.a6668.R;
import com.hgapp.a6668.base.BaseSlidingActivity;
import com.hgapp.a6668.common.adapters.AutoSizeAdapter;
import com.hgapp.a6668.common.adapters.AutoSizeRVAdapter;
import com.hgapp.a6668.common.util.ACache;
import com.hgapp.a6668.common.util.HGConstant;
import com.hgapp.a6668.common.util.TimeHelper;
import com.hgapp.a6668.data.CPBJSCResult;
import com.hgapp.a6668.data.CPJSFTResult;
import com.hgapp.a6668.data.CPJSK2Result;
import com.hgapp.a6668.data.CPJSKSResult;
import com.hgapp.a6668.data.CPJSSCResult;
import com.hgapp.a6668.data.CPLastResult;
import com.hgapp.a6668.data.CPLeftInfoResult;
import com.hgapp.a6668.data.CPNextIssueResult;
import com.hgapp.a6668.data.CPXYNCResult;
import com.hgapp.a6668.data.CQ1FCResult;
import com.hgapp.a6668.data.CQ2FCResult;
import com.hgapp.a6668.data.CQ3FCResult;
import com.hgapp.a6668.data.CQ5FCResult;
import com.hgapp.a6668.data.CQSSCResult;
import com.hgapp.a6668.data.PCDDResult;
import com.hgapp.a6668.data.PersonBalanceResult;
import com.hgapp.a6668.homepage.HomePageIcon;
import com.hgapp.a6668.homepage.cplist.bet.BetCPOrderDialog;
import com.hgapp.a6668.homepage.cplist.events.CPOrderSuccessEvent;
import com.hgapp.a6668.homepage.cplist.events.LeftEvents;
import com.hgapp.a6668.homepage.cplist.events.LeftMenuEvents;
import com.hgapp.a6668.homepage.cplist.order.CPOrderContract;
import com.hgapp.common.util.Check;
import com.hgapp.common.util.GameLog;
import com.hgapp.common.util.TimeUtils;
import com.huangzj.slidingmenu.SlidingMenu;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CPOrderFragment extends BaseSlidingActivity implements CPOrderContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";/*
    @BindView(R.id.drawer_layout)
    HGDrawerLayout drawer_layout;*/
    @BindView(R.id.llCPOrderAll)
    LinearLayout llCPOrderAll;
    @BindView(R.id.cpOrderLotteryOpen1)
    RecyclerView cpOrderLotteryOpen1;
    @BindView(R.id.cpOrderLotteryOpen2)
    RecyclerView cpOrderLotteryOpen2;
    /*@BindView(R.id.cpOrderGameList)
    RecyclerView cpOrderGameList;*/
    @BindView(R.id.cpOrderListLeft)
    RecyclerView cpOrderListLeft;
    /*@BindView(R.id.cpOrderListViewtLeft)
    ListView cpOrderListViewtLeft;*/
    @BindView(R.id.cpOrderLayout)
    LinearLayout cpOrderLayout;
    @BindView(R.id.cpOrderTab)
    TabLayout cpOrderTab;
    @BindView(R.id.cpOrderRXRadio)
    TextView cpOrderRXRadio;
    @BindView(R.id.cpOrderRXLine)
    TextView cpOrderRXLine;
    @BindView(R.id.cpOrderListRight)
    RecyclerView cpOrderListRight;
   /* @BindView(R.id.cpOrderListViewRight)
    ListView cpOrderListViewRight;*/
    @BindView(R.id.cpOrderUserMoney)
    TextView cpOrderUserMoney;
    @BindView(R.id.cpOrderTitle)
    TextView cpOrderTitle;
    @BindView(R.id.cpOrderLotteryLastTime)
    TextView cpOrderLotteryLastTime;
    @BindView(R.id.cpOrderLotteryNextTime)
    TextView cpOrderLotteryNextTime;
    @BindView(R.id.rightCloseLotteryTime)
    TextView rightCloseLotteryTime;
    @BindView(R.id.rightOpenLotteryTime)
    TextView rightOpenLotteryTime;
    @BindView(R.id.cpOrderReset)
    TextView cpOrderReset;
    @BindView(R.id.cpOrderNoYet)
    TextView cpOrderNoYet;
    @BindView(R.id.cpOrderSubmit)
    TextView cpOrderSubmit;
    @BindView(R.id.cpOrderNumber)
    TextView cpOrderNumber;
    @BindView(R.id.cpOrderGold)
    EditText cpOrderGold;

    private static List<HomePageIcon> cpGameList = new ArrayList<HomePageIcon>();
    private static List<LeftEvents> cpLeftEventList = new ArrayList<LeftEvents>();

    private static List<String> cpLeftEventList1 = new ArrayList<String>();
    private static List<String> cpLeftEventList2 = new ArrayList<String>();
    List<CPOrderContentListResult> cpOrderContentListResults;
    private static List<CPOrderAllResult> allResultList = new ArrayList<CPOrderAllResult>();
    List<CPOrderContentListResult> data  = new ArrayList<>();
    private int postionAll;
    private int rX0 = 0;
    private int xiazhuValue = 0;
    private String rX2,rX3,rX4,rX5;
    private CPOrederListRightGameAdapter cpOrederListRightGameAdapter;
    private CPOreder2ListRightGameAdapter cpOreder2ListRightGameAdapter;
    private CPOrederContentGameAdapter cpOrederContentGameAdapter;
    MyAdapter myAdapter;
    /*@BindView(R.id.main_swipemenu)
    SwipeMenu mainSwipemenu;*/
    SlidingMenu slidingLeftMenu;
    private String userName, userMoney, fshowtype, M_League, getArgParam4, fromType;
    CPOrderContract.Presenter presenter;
    private ScheduledExecutorService executorService;
    private onLotteryTimeThread lotteryTimeThread = new onLotteryTimeThread();
    private ScheduledExecutorService executorEndService;
    private onWaitingEndThread onWaitingEndThread = new onWaitingEndThread();
    private long sendAuthTime = HGConstant.ACTION_SEND_LEAGUE_TIME_M;
    private long sendEndTime = HGConstant.ACTION_SEND_LEAGUE_TIME_T;
    private String agMoney, hgMoney;
    private String titleName = "";
    private String dzTitileName = "";
    private String orderStype = "bjsc";
    private String  x_session_token = "";
    private String group ="";
    /** 北京赛车    game_code 51
     *  重庆时时彩    game_code 2
     *  极速赛车    game_code 189
     *  极速飞艇    game_code 222
     *  分分彩    game_code 207
     *  三分彩    game_code 407
     *  五分彩    game_code 507
     *  腾讯二分彩    game_code 607
     *  PC蛋蛋    game_code 304
     *  江苏快3    game_code 159
     *  幸运农场    game_code 47
     *  快乐十分    game_code 3
     *  香港六合彩  game_code 69
     *  极速快三    game_code 384
     *
     */
    private String  game_code = "";
    private String round = "";
    private String  type = "0";
    private int index =0;

    private boolean isCloseLottery = false;
    static {
        //注意事项  每次投注成功之后都需要刷新一下用户的金额 ，且是全局的金额都需要变动  需要发送一下全部的 Money  message 去
        cpGameList.add(new HomePageIcon("系统菜单", R.mipmap.home_hgty));
        cpGameList.add(new HomePageIcon("返回大厅", R.mipmap.home_hgty));
        cpGameList.add(new HomePageIcon("北京赛车(PK10)", R.mipmap.home_hgty));
        cpGameList.add(new HomePageIcon("重庆时时彩", R.mipmap.home_vrcp));
        cpGameList.add(new HomePageIcon("极速赛车", R.mipmap.home_qipai));
        cpGameList.add(new HomePageIcon("极速飞艇", R.mipmap.home_hgty));
        cpGameList.add(new HomePageIcon("分分彩", R.mipmap.home_lhj));
        cpGameList.add(new HomePageIcon("三分彩", R.mipmap.home_lhj));
        cpGameList.add(new HomePageIcon("五分彩", R.mipmap.home_lhj));
        cpGameList.add(new HomePageIcon("腾讯二分彩", R.mipmap.home_lhj));
        cpGameList.add(new HomePageIcon("PC蛋蛋", R.mipmap.home_ag));
        cpGameList.add(new HomePageIcon("江苏鼓宝(快3)", R.mipmap.home_ag));
        cpGameList.add(new HomePageIcon("幸运农场", R.mipmap.home_ag));
        cpGameList.add(new HomePageIcon("广东快乐十分", R.mipmap.home_vrcp));
        cpGameList.add(new HomePageIcon("香港六合彩", R.mipmap.home_lhj));
        cpGameList.add(new HomePageIcon("极速快三", R.mipmap.home_lhj));
        cpLeftEventList.add(new LeftEvents("两面", "1", false));
        cpLeftEventList.add(new LeftEvents("1-5球", "2", true));
        cpLeftEventList.add(new LeftEvents("前中后", "3", false));
        cpLeftEventList.add(new LeftEvents("两面", "4", false));
        cpLeftEventList.add(new LeftEvents("1-5球", "8", true));
        cpLeftEventList.add(new LeftEvents("前中后", "9", false));
        cpLeftEventList.add(new LeftEvents("两面", "7", false));
        cpLeftEventList.add(new LeftEvents("1-5球", "6", true));
        cpLeftEventList.add(new LeftEvents("前中后", "10", false));
        cpLeftEventList.add(new LeftEvents("两面", "5", false));
        /*cpLeftEventList2.add("3");
        cpLeftEventList2.add("小");
        cpLeftEventList2.add("单");
        cpLeftEventList2.add("虎");
        cpLeftEventList2.add("龙");
        cpLeftEventList2.add("虎");
        cpLeftEventList2.add("虎");
        cpLeftEventList2.add("龙");*/


    }

   /* public static CPOrderFragment newInstance(List<String> param1) {
        CPOrderFragment fragment = new CPOrderFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, ArrayListHelper.convertListToArrayList(param1));
        CPInjections.inject(null, fragment);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CPInjections.inject(null, this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        game_code = intent.getStringExtra("gameId");
        titleName = intent.getStringExtra("gameName");
        if(0!= setLayoutId())
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_base,null,false);

            FrameLayout contentLayout = (FrameLayout)view.findViewById(R.id.layout_content);
            View contentview = LayoutInflater.from(getContext()).inflate(setLayoutId(),null,false);
            contentLayout.addView(contentview);

            AutoUtils.auto(view);
            ButterKnife.bind(this,view);
            hideLoadingView();
            setContentView(view);
            setBehindContentView(R.layout.left_menu_frame);
            setEvents(savedInstanceState);
        }

        showLeftMenu();

        /*if (getArguments() != null) {
            game_code = getArguments().getStringArrayList(ARG_PARAM1).get(0);
            titleName = getArguments().getStringArrayList(ARG_PARAM1).get(1);
            fshowtype = getArguments().getStringArrayList(ARG_PARAM1).get(2);// 用以判断是电子还是真人
        }*/
        EventBus.getDefault().register(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_cp_order;
    }

    private void XYNC(CPXYNCResult cpbjscResult){
        for(int k = 0; k < 10; ++k) {
            CPOrderAllResult allResult = new CPOrderAllResult();
            switch (k) {
                case 0:
                    allResult.setEventChecked(true);
                    allResult.setOrderAllName("两面");

                    List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    cpOrderContentListResult.setOrderContentListName("总和");
                    cpOrderContentListResult.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                    cpOrderContentResult3.setOrderName("总和大");
                    cpOrderContentResult3.setFullName("");
                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata2024());
                    cpOrderContentResult3.setOrderId("2024");
                    cpOrderContentResultList.add(cpOrderContentResult3);

                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                    cpOrderContentResult4.setOrderName("总和小");
                    cpOrderContentResult4.setFullName("");
                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata2025());
                    cpOrderContentResult4.setOrderId("2025");
                    cpOrderContentResultList.add(cpOrderContentResult4);

                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                    cpOrderContentResult5.setOrderName("总和单");
                    cpOrderContentResult5.setFullName("");
                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata2026());
                    cpOrderContentResult5.setOrderId("2026");
                    cpOrderContentResultList.add(cpOrderContentResult5);

                    CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                    cpOrderContentResult6.setOrderName("总和双");
                    cpOrderContentResult6.setFullName("");
                    cpOrderContentResult6.setOrderState(cpbjscResult.getdata2027());
                    cpOrderContentResult6.setOrderId("2027");
                    cpOrderContentResultList.add(cpOrderContentResult6);

                    CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                    cpOrderContentResult7.setOrderName("总尾大");
                    cpOrderContentResult7.setFullName("");
                    cpOrderContentResult7.setOrderState(cpbjscResult.getdata2028());
                    cpOrderContentResult7.setOrderId("2028");
                    cpOrderContentResultList.add(cpOrderContentResult7);

                    CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                    cpOrderContentResult8.setOrderName("总尾小");
                    cpOrderContentResult8.setFullName("");
                    cpOrderContentResult8.setOrderState(cpbjscResult.getdata2029());
                    cpOrderContentResult8.setOrderId("2029");
                    cpOrderContentResultList.add(cpOrderContentResult8);

                    CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                    cpOrderContentResult9.setOrderName("龙");
                    cpOrderContentResult9.setFullName("");
                    cpOrderContentResult9.setOrderState(cpbjscResult.getdata2030());
                    cpOrderContentResult9.setOrderId("2030");
                    cpOrderContentResultList.add(cpOrderContentResult9);

                    CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                    cpOrderContentResult10.setOrderName("虎");
                    cpOrderContentResult10.setFullName("");
                    cpOrderContentResult10.setOrderState(cpbjscResult.getdata2031());
                    cpOrderContentResult10.setOrderId("2031");
                    cpOrderContentResultList.add(cpOrderContentResult10);


                    CPOrderContentListResult cpOrderContentListResult1 = new CPOrderContentListResult();
                    cpOrderContentListResult1.setOrderContentListName("第一球");
                    cpOrderContentListResult1.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult2009 = new CPOrderContentResult();
                    cpOrderContentResult2009.setOrderName("大");
                    cpOrderContentResult2009.setFullName("第一球");
                    cpOrderContentResult2009.setOrderState(cpbjscResult.getdata12009());
                    cpOrderContentResult2009.setOrderId("1-2009");
                    cpOrderContentResultList1.add(cpOrderContentResult2009);

                    CPOrderContentResult cpOrderContentResult2010 = new CPOrderContentResult();
                    cpOrderContentResult2010.setOrderName("小");
                    cpOrderContentResult2010.setFullName("第一球");
                    cpOrderContentResult2010.setOrderState(cpbjscResult.getdata12010());
                    cpOrderContentResult2010.setOrderId("1-2010");
                    cpOrderContentResultList1.add(cpOrderContentResult2010);

                    CPOrderContentResult cpOrderContentResult2011 = new CPOrderContentResult();
                    cpOrderContentResult2011.setOrderName("单");
                    cpOrderContentResult2011.setFullName("第一球");
                    cpOrderContentResult2011.setOrderState(cpbjscResult.getdata12011());
                    cpOrderContentResult2011.setOrderId("1-2011");
                    cpOrderContentResultList1.add(cpOrderContentResult2011);

                    CPOrderContentResult cpOrderContentResult2012 = new CPOrderContentResult();
                    cpOrderContentResult2012.setOrderName("双");
                    cpOrderContentResult2012.setFullName("第一球");
                    cpOrderContentResult2012.setOrderState(cpbjscResult.getdata12012());
                    cpOrderContentResult2012.setOrderId("1-2012");
                    cpOrderContentResultList1.add(cpOrderContentResult2012);

                    CPOrderContentResult cpOrderContentResult2013 = new CPOrderContentResult();
                    cpOrderContentResult2013.setOrderName("尾大");
                    cpOrderContentResult2013.setFullName("第一球");
                    cpOrderContentResult2013.setOrderState(cpbjscResult.getdata12013());
                    cpOrderContentResult2013.setOrderId("1-2013");
                    cpOrderContentResultList1.add(cpOrderContentResult2013);

                    CPOrderContentResult cpOrderContentResult2014 = new CPOrderContentResult();
                    cpOrderContentResult2014.setOrderName("小");
                    cpOrderContentResult2014.setFullName("第一球");
                    cpOrderContentResult2014.setOrderState(cpbjscResult.getdata12014());
                    cpOrderContentResult2014.setOrderId("1-2014");
                    cpOrderContentResultList1.add(cpOrderContentResult2014);

                    CPOrderContentResult cpOrderContentResult2015 = new CPOrderContentResult();
                    cpOrderContentResult2015.setOrderName("合单");
                    cpOrderContentResult2015.setFullName("第一球");
                    cpOrderContentResult2015.setOrderState(cpbjscResult.getdata12015());
                    cpOrderContentResult2015.setOrderId("1-2015");
                    cpOrderContentResultList1.add(cpOrderContentResult2015);

                    CPOrderContentResult cpOrderContentResult2016 = new CPOrderContentResult();
                    cpOrderContentResult2016.setOrderName("合双");
                    cpOrderContentResult2016.setFullName("第一球");
                    cpOrderContentResult2016.setOrderState(cpbjscResult.getdata12016());
                    cpOrderContentResult2016.setOrderId("1-2016");
                    cpOrderContentResultList1.add(cpOrderContentResult2016);


                    CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                    cpOrderContentListResult2.setOrderContentListName("第二球");
                    cpOrderContentListResult2.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult22009 = new CPOrderContentResult();
                    cpOrderContentResult22009.setOrderName("大");
                    cpOrderContentResult22009.setFullName("第二球");
                    cpOrderContentResult22009.setOrderState(cpbjscResult.getdata22009());
                    cpOrderContentResult22009.setOrderId("2-2009");
                    cpOrderContentResultList2.add(cpOrderContentResult22009);

                    CPOrderContentResult cpOrderContentResult22010 = new CPOrderContentResult();
                    cpOrderContentResult22010.setOrderName("小");
                    cpOrderContentResult22010.setFullName("第二球");
                    cpOrderContentResult22010.setOrderState(cpbjscResult.getdata22010());
                    cpOrderContentResult22010.setOrderId("2-2010");
                    cpOrderContentResultList2.add(cpOrderContentResult22010);

                    CPOrderContentResult cpOrderContentResult22011 = new CPOrderContentResult();
                    cpOrderContentResult22011.setOrderName("单");
                    cpOrderContentResult22011.setFullName("第二球");
                    cpOrderContentResult22011.setOrderState(cpbjscResult.getdata22011());
                    cpOrderContentResult22011.setOrderId("2-2011");
                    cpOrderContentResultList2.add(cpOrderContentResult22011);

                    CPOrderContentResult cpOrderContentResult22012 = new CPOrderContentResult();
                    cpOrderContentResult22012.setOrderName("双");
                    cpOrderContentResult22012.setFullName("第二球");
                    cpOrderContentResult22012.setOrderState(cpbjscResult.getdata22012());
                    cpOrderContentResult22012.setOrderId("2-2012");
                    cpOrderContentResultList2.add(cpOrderContentResult22012);

                    CPOrderContentResult cpOrderContentResult22013 = new CPOrderContentResult();
                    cpOrderContentResult22013.setOrderName("尾大");
                    cpOrderContentResult22013.setFullName("第二球");
                    cpOrderContentResult22013.setOrderState(cpbjscResult.getdata22013());
                    cpOrderContentResult22013.setOrderId("2-2013");
                    cpOrderContentResultList2.add(cpOrderContentResult22013);

                    CPOrderContentResult cpOrderContentResult22014 = new CPOrderContentResult();
                    cpOrderContentResult22014.setOrderName("小");
                    cpOrderContentResult22014.setFullName("第二球");
                    cpOrderContentResult22014.setOrderState(cpbjscResult.getdata22014());
                    cpOrderContentResult22014.setOrderId("2-2014");
                    cpOrderContentResultList2.add(cpOrderContentResult22014);

                    CPOrderContentResult cpOrderContentResult22015 = new CPOrderContentResult();
                    cpOrderContentResult22015.setOrderName("合单");
                    cpOrderContentResult22015.setFullName("第二球");
                    cpOrderContentResult22015.setOrderState(cpbjscResult.getdata22015());
                    cpOrderContentResult22015.setOrderId("2-2015");
                    cpOrderContentResultList2.add(cpOrderContentResult22015);

                    CPOrderContentResult cpOrderContentResult22016 = new CPOrderContentResult();
                    cpOrderContentResult22016.setOrderName("合双");
                    cpOrderContentResult22016.setFullName("第二球");
                    cpOrderContentResult22016.setOrderState(cpbjscResult.getdata22016());
                    cpOrderContentResult22016.setOrderId("2-2016");
                    cpOrderContentResultList2.add(cpOrderContentResult22016);

                    CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                    cpOrderContentListResult3.setOrderContentListName("第三球");
                    cpOrderContentListResult3.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult32009 = new CPOrderContentResult();
                    cpOrderContentResult32009.setOrderName("大");
                    cpOrderContentResult32009.setFullName("第三球");
                    cpOrderContentResult32009.setOrderState(cpbjscResult.getdata32009());
                    cpOrderContentResult32009.setOrderId("3-2009");
                    cpOrderContentResultList3.add(cpOrderContentResult32009);

                    CPOrderContentResult cpOrderContentResult32010 = new CPOrderContentResult();
                    cpOrderContentResult32010.setOrderName("小");
                    cpOrderContentResult32010.setFullName("第三球");
                    cpOrderContentResult32010.setOrderState(cpbjscResult.getdata32010());
                    cpOrderContentResult32010.setOrderId("3-2010");
                    cpOrderContentResultList3.add(cpOrderContentResult32010);

                    CPOrderContentResult cpOrderContentResult32011 = new CPOrderContentResult();
                    cpOrderContentResult32011.setOrderName("单");
                    cpOrderContentResult32011.setFullName("第三球");
                    cpOrderContentResult32011.setOrderState(cpbjscResult.getdata32011());
                    cpOrderContentResult32011.setOrderId("3-2011");
                    cpOrderContentResultList3.add(cpOrderContentResult32011);

                    CPOrderContentResult cpOrderContentResult32012 = new CPOrderContentResult();
                    cpOrderContentResult32012.setOrderName("双");
                    cpOrderContentResult32012.setFullName("第三球");
                    cpOrderContentResult32012.setOrderState(cpbjscResult.getdata32012());
                    cpOrderContentResult32012.setOrderId("3-2012");
                    cpOrderContentResultList3.add(cpOrderContentResult32012);

                    CPOrderContentResult cpOrderContentResult32013 = new CPOrderContentResult();
                    cpOrderContentResult32013.setOrderName("尾大");
                    cpOrderContentResult32013.setFullName("第三球");
                    cpOrderContentResult32013.setOrderState(cpbjscResult.getdata32013());
                    cpOrderContentResult32013.setOrderId("3-2013");
                    cpOrderContentResultList3.add(cpOrderContentResult32013);

                    CPOrderContentResult cpOrderContentResult32014 = new CPOrderContentResult();
                    cpOrderContentResult32014.setOrderName("小");
                    cpOrderContentResult32014.setFullName("第三球");
                    cpOrderContentResult32014.setOrderState(cpbjscResult.getdata32014());
                    cpOrderContentResult32014.setOrderId("3-2014");
                    cpOrderContentResultList3.add(cpOrderContentResult32014);

                    CPOrderContentResult cpOrderContentResult32015 = new CPOrderContentResult();
                    cpOrderContentResult32015.setOrderName("合单");
                    cpOrderContentResult32015.setFullName("第三球");
                    cpOrderContentResult32015.setOrderState(cpbjscResult.getdata32015());
                    cpOrderContentResult32015.setOrderId("3-2015");
                    cpOrderContentResultList3.add(cpOrderContentResult32015);

                    CPOrderContentResult cpOrderContentResult32016 = new CPOrderContentResult();
                    cpOrderContentResult32016.setOrderName("合双");
                    cpOrderContentResult32016.setFullName("第三球");
                    cpOrderContentResult32016.setOrderState(cpbjscResult.getdata32016());
                    cpOrderContentResult32016.setOrderId("3-2016");
                    cpOrderContentResultList3.add(cpOrderContentResult32016);

                    CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                    cpOrderContentListResult4.setOrderContentListName("第四球");
                    cpOrderContentListResult4.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult42009 = new CPOrderContentResult();
                    cpOrderContentResult42009.setOrderName("大");
                    cpOrderContentResult42009.setFullName("第四球");
                    cpOrderContentResult42009.setOrderState(cpbjscResult.getdata42009());
                    cpOrderContentResult42009.setOrderId("4-2009");
                    cpOrderContentResultList4.add(cpOrderContentResult42009);

                    CPOrderContentResult cpOrderContentResult42010 = new CPOrderContentResult();
                    cpOrderContentResult42010.setOrderName("小");
                    cpOrderContentResult42010.setFullName("第四球");
                    cpOrderContentResult42010.setOrderState(cpbjscResult.getdata42010());
                    cpOrderContentResult42010.setOrderId("4-2010");
                    cpOrderContentResultList4.add(cpOrderContentResult42010);

                    CPOrderContentResult cpOrderContentResult42011 = new CPOrderContentResult();
                    cpOrderContentResult42011.setOrderName("单");
                    cpOrderContentResult42011.setFullName("第四球");
                    cpOrderContentResult42011.setOrderState(cpbjscResult.getdata42011());
                    cpOrderContentResult42011.setOrderId("4-2011");
                    cpOrderContentResultList4.add(cpOrderContentResult42011);

                    CPOrderContentResult cpOrderContentResult42012 = new CPOrderContentResult();
                    cpOrderContentResult42012.setOrderName("双");
                    cpOrderContentResult42012.setFullName("第四球");
                    cpOrderContentResult42012.setOrderState(cpbjscResult.getdata42012());
                    cpOrderContentResult42012.setOrderId("4-2012");
                    cpOrderContentResultList4.add(cpOrderContentResult42012);

                    CPOrderContentResult cpOrderContentResult42013 = new CPOrderContentResult();
                    cpOrderContentResult42013.setOrderName("尾大");
                    cpOrderContentResult42013.setFullName("第四球");
                    cpOrderContentResult42013.setOrderState(cpbjscResult.getdata42013());
                    cpOrderContentResult42013.setOrderId("4-2013");
                    cpOrderContentResultList4.add(cpOrderContentResult42013);

                    CPOrderContentResult cpOrderContentResult42014 = new CPOrderContentResult();
                    cpOrderContentResult42014.setOrderName("小");
                    cpOrderContentResult42014.setFullName("第四球");
                    cpOrderContentResult42014.setOrderState(cpbjscResult.getdata42014());
                    cpOrderContentResult42014.setOrderId("4-2014");
                    cpOrderContentResultList4.add(cpOrderContentResult42014);

                    CPOrderContentResult cpOrderContentResult42015 = new CPOrderContentResult();
                    cpOrderContentResult42015.setOrderName("合单");
                    cpOrderContentResult42015.setFullName("第四球");
                    cpOrderContentResult42015.setOrderState(cpbjscResult.getdata42015());
                    cpOrderContentResult42015.setOrderId("4-2015");
                    cpOrderContentResultList4.add(cpOrderContentResult42015);

                    CPOrderContentResult cpOrderContentResult42016 = new CPOrderContentResult();
                    cpOrderContentResult42016.setOrderName("合双");
                    cpOrderContentResult42016.setFullName("第四球");
                    cpOrderContentResult42016.setOrderState(cpbjscResult.getdata42016());
                    cpOrderContentResult42016.setOrderId("4-2016");
                    cpOrderContentResultList4.add(cpOrderContentResult42016);

                    CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                    cpOrderContentListResult5.setOrderContentListName("第五球");
                    cpOrderContentListResult5.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult52009 = new CPOrderContentResult();
                    cpOrderContentResult52009.setOrderName("大");
                    cpOrderContentResult52009.setFullName("第五球");
                    cpOrderContentResult52009.setOrderState(cpbjscResult.getdata52009());
                    cpOrderContentResult52009.setOrderId("5-2009");
                    cpOrderContentResultList5.add(cpOrderContentResult52009);

                    CPOrderContentResult cpOrderContentResult52010 = new CPOrderContentResult();
                    cpOrderContentResult52010.setOrderName("小");
                    cpOrderContentResult52010.setFullName("第五球");
                    cpOrderContentResult52010.setOrderState(cpbjscResult.getdata52010());
                    cpOrderContentResult52010.setOrderId("5-2010");
                    cpOrderContentResultList5.add(cpOrderContentResult52010);

                    CPOrderContentResult cpOrderContentResult52011 = new CPOrderContentResult();
                    cpOrderContentResult52011.setOrderName("单");
                    cpOrderContentResult52011.setFullName("第五球");
                    cpOrderContentResult52011.setOrderState(cpbjscResult.getdata52011());
                    cpOrderContentResult52011.setOrderId("5-2011");
                    cpOrderContentResultList5.add(cpOrderContentResult52011);

                    CPOrderContentResult cpOrderContentResult52012 = new CPOrderContentResult();
                    cpOrderContentResult52012.setOrderName("双");
                    cpOrderContentResult52012.setFullName("第五球");
                    cpOrderContentResult52012.setOrderState(cpbjscResult.getdata52012());
                    cpOrderContentResult52012.setOrderId("5-2012");
                    cpOrderContentResultList5.add(cpOrderContentResult52012);

                    CPOrderContentResult cpOrderContentResult52013 = new CPOrderContentResult();
                    cpOrderContentResult52013.setOrderName("尾大");
                    cpOrderContentResult52013.setFullName("第五球");
                    cpOrderContentResult52013.setOrderState(cpbjscResult.getdata52013());
                    cpOrderContentResult52013.setOrderId("5-2013");
                    cpOrderContentResultList5.add(cpOrderContentResult52013);

                    CPOrderContentResult cpOrderContentResult52014 = new CPOrderContentResult();
                    cpOrderContentResult52014.setOrderName("小");
                    cpOrderContentResult52014.setFullName("第五球");
                    cpOrderContentResult52014.setOrderState(cpbjscResult.getdata52014());
                    cpOrderContentResult52014.setOrderId("5-2014");
                    cpOrderContentResultList5.add(cpOrderContentResult52014);

                    CPOrderContentResult cpOrderContentResult52015 = new CPOrderContentResult();
                    cpOrderContentResult52015.setOrderName("合单");
                    cpOrderContentResult52015.setFullName("第五球");
                    cpOrderContentResult52015.setOrderState(cpbjscResult.getdata52015());
                    cpOrderContentResult52015.setOrderId("5-2015");
                    cpOrderContentResultList5.add(cpOrderContentResult52015);

                    CPOrderContentResult cpOrderContentResult52016 = new CPOrderContentResult();
                    cpOrderContentResult52016.setOrderName("合双");
                    cpOrderContentResult52016.setFullName("第五球");
                    cpOrderContentResult52016.setOrderState(cpbjscResult.getdata52016());
                    cpOrderContentResult52016.setOrderId("4-2016");
                    cpOrderContentResultList5.add(cpOrderContentResult52016);

                    CPOrderContentListResult cpOrderContentListResult6 = new CPOrderContentListResult();
                    cpOrderContentListResult6.setOrderContentListName("第六球");
                    cpOrderContentListResult6.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult62009 = new CPOrderContentResult();
                    cpOrderContentResult62009.setOrderName("大");
                    cpOrderContentResult62009.setFullName("第六球");
                    cpOrderContentResult62009.setOrderState(cpbjscResult.getdata62009());
                    cpOrderContentResult62009.setOrderId("6-2009");
                    cpOrderContentResultList6.add(cpOrderContentResult62009);

                    CPOrderContentResult cpOrderContentResult62010 = new CPOrderContentResult();
                    cpOrderContentResult62010.setOrderName("小");
                    cpOrderContentResult62010.setFullName("第六球");
                    cpOrderContentResult62010.setOrderState(cpbjscResult.getdata62010());
                    cpOrderContentResult62010.setOrderId("6-2010");
                    cpOrderContentResultList6.add(cpOrderContentResult62010);

                    CPOrderContentResult cpOrderContentResult62011 = new CPOrderContentResult();
                    cpOrderContentResult62011.setOrderName("单");
                    cpOrderContentResult62011.setFullName("第六球");
                    cpOrderContentResult62011.setOrderState(cpbjscResult.getdata62011());
                    cpOrderContentResult62011.setOrderId("6-2011");
                    cpOrderContentResultList6.add(cpOrderContentResult62011);

                    CPOrderContentResult cpOrderContentResult62012 = new CPOrderContentResult();
                    cpOrderContentResult62012.setOrderName("双");
                    cpOrderContentResult62012.setFullName("第六球");
                    cpOrderContentResult62012.setOrderState(cpbjscResult.getdata62012());
                    cpOrderContentResult62012.setOrderId("6-2012");
                    cpOrderContentResultList6.add(cpOrderContentResult62012);

                    CPOrderContentResult cpOrderContentResult62013 = new CPOrderContentResult();
                    cpOrderContentResult62013.setOrderName("尾大");
                    cpOrderContentResult62013.setFullName("第六球");
                    cpOrderContentResult62013.setOrderState(cpbjscResult.getdata62013());
                    cpOrderContentResult62013.setOrderId("6-2013");
                    cpOrderContentResultList6.add(cpOrderContentResult62013);

                    CPOrderContentResult cpOrderContentResult62014 = new CPOrderContentResult();
                    cpOrderContentResult62014.setOrderName("小");
                    cpOrderContentResult62014.setFullName("第六球");
                    cpOrderContentResult62014.setOrderState(cpbjscResult.getdata62014());
                    cpOrderContentResult62014.setOrderId("6-2014");
                    cpOrderContentResultList6.add(cpOrderContentResult62014);

                    CPOrderContentResult cpOrderContentResult62015 = new CPOrderContentResult();
                    cpOrderContentResult62015.setOrderName("合单");
                    cpOrderContentResult62015.setFullName("第六球");
                    cpOrderContentResult62015.setOrderState(cpbjscResult.getdata62015());
                    cpOrderContentResult62015.setOrderId("6-2015");
                    cpOrderContentResultList6.add(cpOrderContentResult62015);

                    CPOrderContentResult cpOrderContentResult62016 = new CPOrderContentResult();
                    cpOrderContentResult62016.setOrderName("合双");
                    cpOrderContentResult62016.setFullName("第六球");
                    cpOrderContentResult62016.setOrderState(cpbjscResult.getdata62016());
                    cpOrderContentResult62016.setOrderId("6-2016");
                    cpOrderContentResultList6.add(cpOrderContentResult62016);

                    CPOrderContentListResult cpOrderContentListResult7 = new CPOrderContentListResult();
                    cpOrderContentListResult7.setOrderContentListName("第七球");
                    cpOrderContentListResult7.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult72009 = new CPOrderContentResult();
                    cpOrderContentResult72009.setOrderName("大");
                    cpOrderContentResult72009.setFullName("第七球");
                    cpOrderContentResult72009.setOrderState(cpbjscResult.getdata72009());
                    cpOrderContentResult72009.setOrderId("7-2009");
                    cpOrderContentResultList7.add(cpOrderContentResult72009);

                    CPOrderContentResult cpOrderContentResult72010 = new CPOrderContentResult();
                    cpOrderContentResult72010.setOrderName("小");
                    cpOrderContentResult72010.setFullName("第七球");
                    cpOrderContentResult72010.setOrderState(cpbjscResult.getdata72010());
                    cpOrderContentResult72010.setOrderId("7-2010");
                    cpOrderContentResultList7.add(cpOrderContentResult72010);

                    CPOrderContentResult cpOrderContentResult72011 = new CPOrderContentResult();
                    cpOrderContentResult72011.setOrderName("单");
                    cpOrderContentResult72011.setFullName("第七球");
                    cpOrderContentResult72011.setOrderState(cpbjscResult.getdata72011());
                    cpOrderContentResult72011.setOrderId("7-2011");
                    cpOrderContentResultList7.add(cpOrderContentResult72011);

                    CPOrderContentResult cpOrderContentResult72012 = new CPOrderContentResult();
                    cpOrderContentResult72012.setOrderName("双");
                    cpOrderContentResult72012.setFullName("第七球");
                    cpOrderContentResult72012.setOrderState(cpbjscResult.getdata72012());
                    cpOrderContentResult72012.setOrderId("7-2012");
                    cpOrderContentResultList7.add(cpOrderContentResult72012);

                    CPOrderContentResult cpOrderContentResult72013 = new CPOrderContentResult();
                    cpOrderContentResult72013.setOrderName("尾大");
                    cpOrderContentResult72013.setFullName("第七球");
                    cpOrderContentResult72013.setOrderState(cpbjscResult.getdata72013());
                    cpOrderContentResult72013.setOrderId("7-2013");
                    cpOrderContentResultList7.add(cpOrderContentResult72013);

                    CPOrderContentResult cpOrderContentResult72014 = new CPOrderContentResult();
                    cpOrderContentResult72014.setOrderName("小");
                    cpOrderContentResult72014.setFullName("第七球");
                    cpOrderContentResult72014.setOrderState(cpbjscResult.getdata72014());
                    cpOrderContentResult72014.setOrderId("7-2014");
                    cpOrderContentResultList7.add(cpOrderContentResult72014);

                    CPOrderContentResult cpOrderContentResult72015 = new CPOrderContentResult();
                    cpOrderContentResult72015.setOrderName("合单");
                    cpOrderContentResult72015.setFullName("第七球");
                    cpOrderContentResult72015.setOrderState(cpbjscResult.getdata72015());
                    cpOrderContentResult72015.setOrderId("7-2015");
                    cpOrderContentResultList7.add(cpOrderContentResult72015);

                    CPOrderContentResult cpOrderContentResult72016 = new CPOrderContentResult();
                    cpOrderContentResult72016.setOrderName("合双");
                    cpOrderContentResult72016.setFullName("第七球");
                    cpOrderContentResult72016.setOrderState(cpbjscResult.getdata72016());
                    cpOrderContentResult72016.setOrderId("7-2016");
                    cpOrderContentResultList7.add(cpOrderContentResult72016);

                    CPOrderContentListResult cpOrderContentListResult8 = new CPOrderContentListResult();
                    cpOrderContentListResult8.setOrderContentListName("第八球");
                    cpOrderContentListResult8.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult82009 = new CPOrderContentResult();
                    cpOrderContentResult82009.setOrderName("大");
                    cpOrderContentResult82009.setFullName("第八球");
                    cpOrderContentResult82009.setOrderState(cpbjscResult.getdata82009());
                    cpOrderContentResult82009.setOrderId("8-2009");
                    cpOrderContentResultList8.add(cpOrderContentResult82009);

                    CPOrderContentResult cpOrderContentResult82010 = new CPOrderContentResult();
                    cpOrderContentResult82010.setOrderName("小");
                    cpOrderContentResult82010.setFullName("第八球");
                    cpOrderContentResult82010.setOrderState(cpbjscResult.getdata82010());
                    cpOrderContentResult82010.setOrderId("8-2010");
                    cpOrderContentResultList8.add(cpOrderContentResult82010);

                    CPOrderContentResult cpOrderContentResult82011 = new CPOrderContentResult();
                    cpOrderContentResult82011.setOrderName("单");
                    cpOrderContentResult82011.setFullName("第八球");
                    cpOrderContentResult82011.setOrderState(cpbjscResult.getdata82011());
                    cpOrderContentResult82011.setOrderId("8-2011");
                    cpOrderContentResultList8.add(cpOrderContentResult82011);

                    CPOrderContentResult cpOrderContentResult82012 = new CPOrderContentResult();
                    cpOrderContentResult82012.setOrderName("双");
                    cpOrderContentResult82012.setFullName("第八球");
                    cpOrderContentResult82012.setOrderState(cpbjscResult.getdata82012());
                    cpOrderContentResult82012.setOrderId("8-2012");
                    cpOrderContentResultList8.add(cpOrderContentResult82012);

                    CPOrderContentResult cpOrderContentResult82013 = new CPOrderContentResult();
                    cpOrderContentResult82013.setOrderName("尾大");
                    cpOrderContentResult82013.setFullName("第八球");
                    cpOrderContentResult82013.setOrderState(cpbjscResult.getdata82013());
                    cpOrderContentResult82013.setOrderId("8-2013");
                    cpOrderContentResultList8.add(cpOrderContentResult82013);

                    CPOrderContentResult cpOrderContentResult82014 = new CPOrderContentResult();
                    cpOrderContentResult82014.setOrderName("小");
                    cpOrderContentResult82014.setFullName("第八球");
                    cpOrderContentResult82014.setOrderState(cpbjscResult.getdata82014());
                    cpOrderContentResult82014.setOrderId("8-2014");
                    cpOrderContentResultList8.add(cpOrderContentResult82014);

                    CPOrderContentResult cpOrderContentResult82015 = new CPOrderContentResult();
                    cpOrderContentResult82015.setOrderName("合单");
                    cpOrderContentResult82015.setFullName("第八球");
                    cpOrderContentResult82015.setOrderState(cpbjscResult.getdata82015());
                    cpOrderContentResult82015.setOrderId("8-2015");
                    cpOrderContentResultList8.add(cpOrderContentResult82015);

                    CPOrderContentResult cpOrderContentResult82016 = new CPOrderContentResult();
                    cpOrderContentResult82016.setOrderName("合双");
                    cpOrderContentResult82016.setFullName("第八球");
                    cpOrderContentResult82016.setOrderState(cpbjscResult.getdata82016());
                    cpOrderContentResult82016.setOrderId("8-2016");
                    cpOrderContentResultList8.add(cpOrderContentResult82016);


                    cpOrderContentListResult.setData(cpOrderContentResultList);
                    cpOrderContentListResult1.setData(cpOrderContentResultList1);
                    cpOrderContentListResult2.setData(cpOrderContentResultList2);
                    cpOrderContentListResult3.setData(cpOrderContentResultList3);
                    cpOrderContentListResult4.setData(cpOrderContentResultList4);
                    cpOrderContentListResult5.setData(cpOrderContentResultList5);
                    cpOrderContentListResult6.setData(cpOrderContentResultList6);
                    cpOrderContentListResult7.setData(cpOrderContentResultList7);
                    cpOrderContentListResult8.setData(cpOrderContentResultList8);

                    CPOrderContentListResult .add(cpOrderContentListResult);
                    CPOrderContentListResult .add(cpOrderContentListResult1);
                    CPOrderContentListResult .add(cpOrderContentListResult2);
                    CPOrderContentListResult .add(cpOrderContentListResult3);
                    CPOrderContentListResult .add(cpOrderContentListResult4);
                    CPOrderContentListResult .add(cpOrderContentListResult5);
                    CPOrderContentListResult .add(cpOrderContentListResult6);
                    CPOrderContentListResult .add(cpOrderContentListResult7);
                    CPOrderContentListResult .add(cpOrderContentListResult8);

                    allResult.setData(CPOrderContentListResult);
                    allResultList.add(allResult);
                    break;
                case 1:

                    allResult.setOrderAllName("第一球");

                    List<CPOrderContentListResult> CPOrderContentListResult11 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult11 = new CPOrderContentListResult();
                    cpOrderContentListResult11.setOrderContentListName("第一球");
                    cpOrderContentListResult11.setShowType("QIU");
                    cpOrderContentListResult11.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList11 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                    cpOrderContentResult11.setOrderName("1");
                    cpOrderContentResult11.setFullName("第一球");
                    cpOrderContentResult11.setOrderState(cpbjscResult.getdata1());
                    cpOrderContentResult11.setOrderId("1");
                    cpOrderContentResultList11.add(cpOrderContentResult11);

                    CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                    cpOrderContentResult12.setOrderName("2");
                    cpOrderContentResult12.setFullName("第一球");
                    cpOrderContentResult12.setOrderState(cpbjscResult.getdata2());
                    cpOrderContentResult12.setOrderId("2");
                    cpOrderContentResultList11.add(cpOrderContentResult12);

                    CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                    cpOrderContentResult13.setOrderName("3");
                    cpOrderContentResult13.setFullName("第一球");
                    cpOrderContentResult13.setOrderState(cpbjscResult.getdata3());
                    cpOrderContentResult13.setOrderId("3");
                    cpOrderContentResultList11.add(cpOrderContentResult13);

                    CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                    cpOrderContentResult14.setOrderName("4");
                    cpOrderContentResult14.setFullName("第一球");
                    cpOrderContentResult14.setOrderState(cpbjscResult.getdata4());
                    cpOrderContentResult14.setOrderId("4");
                    cpOrderContentResultList11.add(cpOrderContentResult14);

                    CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                    cpOrderContentResult15.setOrderName("5");
                    cpOrderContentResult15.setFullName("第一球");
                    cpOrderContentResult15.setOrderState(cpbjscResult.getdata5());
                    cpOrderContentResult15.setOrderId("5");
                    cpOrderContentResultList11.add(cpOrderContentResult15);

                    CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                    cpOrderContentResult16.setOrderName("6");
                    cpOrderContentResult16.setFullName("第一球");
                    cpOrderContentResult16.setOrderState(cpbjscResult.getdata6());
                    cpOrderContentResult16.setOrderId("6");
                    cpOrderContentResultList11.add(cpOrderContentResult16);

                    CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                    cpOrderContentResult17.setOrderName("7");
                    cpOrderContentResult17.setFullName("第一球");
                    cpOrderContentResult17.setOrderState(cpbjscResult.getdata7());
                    cpOrderContentResult17.setOrderId("7");
                    cpOrderContentResultList11.add(cpOrderContentResult17);

                    CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                    cpOrderContentResult18.setOrderName("8");
                    cpOrderContentResult18.setFullName("第一球");
                    cpOrderContentResult18.setOrderState(cpbjscResult.getdata8());
                    cpOrderContentResult18.setOrderId("8");
                    cpOrderContentResultList11.add(cpOrderContentResult18);

                    CPOrderContentResult cpOrderContentResult19 = new CPOrderContentResult();
                    cpOrderContentResult19.setOrderName("9");
                    cpOrderContentResult19.setFullName("第一球");
                    cpOrderContentResult19.setOrderState(cpbjscResult.getdata9());
                    cpOrderContentResult19.setOrderId("9");
                    cpOrderContentResultList11.add(cpOrderContentResult19);

                    CPOrderContentResult cpOrderContentResult110 = new CPOrderContentResult();
                    cpOrderContentResult110.setOrderName("10");
                    cpOrderContentResult110.setFullName("第一球");
                    cpOrderContentResult110.setOrderState(cpbjscResult.getdata10());
                    cpOrderContentResult110.setOrderId("10");
                    cpOrderContentResultList11.add(cpOrderContentResult110);

                    CPOrderContentResult cpOrderContentResult111 = new CPOrderContentResult();
                    cpOrderContentResult111.setOrderName("11");
                    cpOrderContentResult111.setFullName("第一球");
                    cpOrderContentResult111.setOrderState(cpbjscResult.getdata11());
                    cpOrderContentResult111.setOrderId("11");
                    cpOrderContentResultList11.add(cpOrderContentResult111);

                    CPOrderContentResult cpOrderContentResult112 = new CPOrderContentResult();
                    cpOrderContentResult112.setOrderName("12");
                    cpOrderContentResult112.setFullName("第一球");
                    cpOrderContentResult112.setOrderState(cpbjscResult.getdata12());
                    cpOrderContentResult112.setOrderId("12");
                    cpOrderContentResultList11.add(cpOrderContentResult112);

                    CPOrderContentResult cpOrderContentResult113 = new CPOrderContentResult();
                    cpOrderContentResult113.setOrderName("13");
                    cpOrderContentResult113.setFullName("第一球");
                    cpOrderContentResult113.setOrderState(cpbjscResult.getdata13());
                    cpOrderContentResult113.setOrderId("13");
                    cpOrderContentResultList11.add(cpOrderContentResult113);

                    CPOrderContentResult cpOrderContentResult114 = new CPOrderContentResult();
                    cpOrderContentResult114.setOrderName("14");
                    cpOrderContentResult114.setFullName("第一球");
                    cpOrderContentResult114.setOrderState(cpbjscResult.getdata14());
                    cpOrderContentResult114.setOrderId("14");
                    cpOrderContentResultList11.add(cpOrderContentResult114);

                    CPOrderContentResult cpOrderContentResult115 = new CPOrderContentResult();
                    cpOrderContentResult115.setOrderName("15");
                    cpOrderContentResult115.setFullName("第一球");
                    cpOrderContentResult115.setOrderState(cpbjscResult.getdata15());
                    cpOrderContentResult115.setOrderId("15");
                    cpOrderContentResultList11.add(cpOrderContentResult115);

                    CPOrderContentResult cpOrderContentResult116 = new CPOrderContentResult();
                    cpOrderContentResult116.setOrderName("16");
                    cpOrderContentResult116.setFullName("第一球");
                    cpOrderContentResult116.setOrderState(cpbjscResult.getdata16());
                    cpOrderContentResult116.setOrderId("16");
                    cpOrderContentResultList11.add(cpOrderContentResult116);

                    CPOrderContentResult cpOrderContentResult117 = new CPOrderContentResult();
                    cpOrderContentResult117.setOrderName("17");
                    cpOrderContentResult117.setFullName("第一球");
                    cpOrderContentResult117.setOrderState(cpbjscResult.getdata17());
                    cpOrderContentResult117.setOrderId("17");
                    cpOrderContentResultList11.add(cpOrderContentResult117);

                    CPOrderContentResult cpOrderContentResult118 = new CPOrderContentResult();
                    cpOrderContentResult118.setOrderName("18");
                    cpOrderContentResult118.setFullName("第一球");
                    cpOrderContentResult118.setOrderState(cpbjscResult.getdata18());
                    cpOrderContentResult118.setOrderId("18");
                    cpOrderContentResultList11.add(cpOrderContentResult118);

                    CPOrderContentListResult cpOrderContentListResult12 = new CPOrderContentListResult();
                    cpOrderContentListResult12.setOrderContentListName("");
                    cpOrderContentListResult12.setShowType("QIU");
                    cpOrderContentListResult12.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList12 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult119 = new CPOrderContentResult();
                    cpOrderContentResult119.setOrderName("19");
                    cpOrderContentResult119.setFullName("第一球");
                    cpOrderContentResult119.setOrderState(cpbjscResult.getdata19());
                    cpOrderContentResult119.setOrderId("19");
                    cpOrderContentResultList12.add(cpOrderContentResult119);

                    CPOrderContentResult cpOrderContentResult120 = new CPOrderContentResult();
                    cpOrderContentResult120.setOrderName("20");
                    cpOrderContentResult120.setFullName("第一球");
                    cpOrderContentResult120.setOrderState(cpbjscResult.getdata20());
                    cpOrderContentResult120.setOrderId("20");
                    cpOrderContentResultList12.add(cpOrderContentResult120);

                    CPOrderContentListResult cpOrderContentListResult13 = new CPOrderContentListResult();
                    cpOrderContentListResult13.setOrderContentListName("");
                    cpOrderContentListResult13.setShowType("ZI");
                    cpOrderContentListResult13.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList13 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult12009 = new CPOrderContentResult();
                    cpOrderContentResult12009.setOrderName("大");
                    cpOrderContentResult12009.setFullName("第一球");
                    cpOrderContentResult12009.setOrderState(cpbjscResult.getdata2009());
                    cpOrderContentResult12009.setOrderId("2009");
                    cpOrderContentResultList13.add(cpOrderContentResult12009);

                    CPOrderContentResult cpOrderContentResult12010 = new CPOrderContentResult();
                    cpOrderContentResult12010.setOrderName("小");
                    cpOrderContentResult12010.setFullName("第一球");
                    cpOrderContentResult12010.setOrderState(cpbjscResult.getdata2010());
                    cpOrderContentResult12010.setOrderId("2010");
                    cpOrderContentResultList13.add(cpOrderContentResult12010);

                    CPOrderContentResult cpOrderContentResult12011 = new CPOrderContentResult();
                    cpOrderContentResult12011.setOrderName("单");
                    cpOrderContentResult12011.setFullName("第一球");
                    cpOrderContentResult12011.setOrderState(cpbjscResult.getdata2011());
                    cpOrderContentResult12011.setOrderId("2011");
                    cpOrderContentResultList13.add(cpOrderContentResult12011);

                    CPOrderContentResult cpOrderContentResult12012 = new CPOrderContentResult();
                    cpOrderContentResult12012.setOrderName("双");
                    cpOrderContentResult12012.setFullName("第一球");
                    cpOrderContentResult12012.setOrderState(cpbjscResult.getdata2012());
                    cpOrderContentResult12012.setOrderId("2012");
                    cpOrderContentResultList13.add(cpOrderContentResult12012);

                    CPOrderContentResult cpOrderContentResult12013 = new CPOrderContentResult();
                    cpOrderContentResult12013.setOrderName("尾大");
                    cpOrderContentResult12013.setFullName("第一球");
                    cpOrderContentResult12013.setOrderState(cpbjscResult.getdata2013());
                    cpOrderContentResult12013.setOrderId("2013");
                    cpOrderContentResultList13.add(cpOrderContentResult12013);

                    CPOrderContentResult cpOrderContentResult12014 = new CPOrderContentResult();
                    cpOrderContentResult12014.setOrderName("尾小");
                    cpOrderContentResult12014.setFullName("第一球");
                    cpOrderContentResult12014.setOrderState(cpbjscResult.getdata2014());
                    cpOrderContentResult12014.setOrderId("2014");
                    cpOrderContentResultList13.add(cpOrderContentResult12014);

                    CPOrderContentResult cpOrderContentResult12015 = new CPOrderContentResult();
                    cpOrderContentResult12015.setOrderName("合单");
                    cpOrderContentResult12015.setFullName("第一球");
                    cpOrderContentResult12015.setOrderState(cpbjscResult.getdata2015());
                    cpOrderContentResult12015.setOrderId("2015");
                    cpOrderContentResultList13.add(cpOrderContentResult12015);

                    CPOrderContentResult cpOrderContentResult12016 = new CPOrderContentResult();
                    cpOrderContentResult12016.setOrderName("合双");
                    cpOrderContentResult12016.setFullName("第一球");
                    cpOrderContentResult12016.setOrderState(cpbjscResult.getdata2016());
                    cpOrderContentResult12016.setOrderId("2016");
                    cpOrderContentResultList13.add(cpOrderContentResult12016);

                    CPOrderContentResult cpOrderContentResult12017 = new CPOrderContentResult();
                    cpOrderContentResult12017.setOrderName("东");
                    cpOrderContentResult12017.setFullName("第一球");
                    cpOrderContentResult12017.setOrderState(cpbjscResult.getdata2017());
                    cpOrderContentResult12017.setOrderId("2017");
                    cpOrderContentResultList13.add(cpOrderContentResult12017);

                    CPOrderContentResult cpOrderContentResult12018 = new CPOrderContentResult();
                    cpOrderContentResult12018.setOrderName("南");
                    cpOrderContentResult12018.setFullName("第一球");
                    cpOrderContentResult12018.setOrderState(cpbjscResult.getdata2018());
                    cpOrderContentResult12018.setOrderId("2018");
                    cpOrderContentResultList13.add(cpOrderContentResult12018);

                    CPOrderContentResult cpOrderContentResult12019 = new CPOrderContentResult();
                    cpOrderContentResult12019.setOrderName("西");
                    cpOrderContentResult12019.setFullName("第一球");
                    cpOrderContentResult12019.setOrderState(cpbjscResult.getdata2019());
                    cpOrderContentResult12019.setOrderId("2019");
                    cpOrderContentResultList13.add(cpOrderContentResult12019);

                    CPOrderContentResult cpOrderContentResult12020 = new CPOrderContentResult();
                    cpOrderContentResult12020.setOrderName("北");
                    cpOrderContentResult12020.setFullName("第一球");
                    cpOrderContentResult12020.setOrderState(cpbjscResult.getdata2020());
                    cpOrderContentResult12020.setOrderId("2020");
                    cpOrderContentResultList13.add(cpOrderContentResult12020);

                    CPOrderContentListResult cpOrderContentListResult14 = new CPOrderContentListResult();
                    cpOrderContentListResult14.setOrderContentListName("");
                    cpOrderContentListResult14.setShowType("ZI");
                    cpOrderContentListResult14.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList14 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult12021 = new CPOrderContentResult();
                    cpOrderContentResult12021.setOrderName("中");
                    cpOrderContentResult12021.setFullName("第一球");
                    cpOrderContentResult12021.setOrderState(cpbjscResult.getdata2021());
                    cpOrderContentResult12021.setOrderId("2021");
                    cpOrderContentResultList14.add(cpOrderContentResult12021);

                    CPOrderContentResult cpOrderContentResult12022 = new CPOrderContentResult();
                    cpOrderContentResult12022.setOrderName("发");
                    cpOrderContentResult12022.setFullName("第一球");
                    cpOrderContentResult12022.setOrderState(cpbjscResult.getdata2022());
                    cpOrderContentResult12022.setOrderId("2022");
                    cpOrderContentResultList14.add(cpOrderContentResult12022);

                    CPOrderContentResult cpOrderContentResult12023 = new CPOrderContentResult();
                    cpOrderContentResult12023.setOrderName("白");
                    cpOrderContentResult12023.setFullName("第一球");
                    cpOrderContentResult12023.setOrderState(cpbjscResult.getdata2023());
                    cpOrderContentResult12023.setOrderId("2023");
                    cpOrderContentResultList14.add(cpOrderContentResult12023);


                    cpOrderContentListResult11.setData(cpOrderContentResultList11);
                    cpOrderContentListResult12.setData(cpOrderContentResultList12);
                    cpOrderContentListResult13.setData(cpOrderContentResultList13);
                    cpOrderContentListResult14.setData(cpOrderContentResultList14);
                    CPOrderContentListResult11.add(cpOrderContentListResult11);
                    CPOrderContentListResult11.add(cpOrderContentListResult12);
                    CPOrderContentListResult11.add(cpOrderContentListResult13);
                    CPOrderContentListResult11.add(cpOrderContentListResult14);

                    allResult.setData(CPOrderContentListResult11);
                    allResultList.add(allResult);
                    break;
                case 2:

                    allResult.setOrderAllName("第二球");

                    List<CPOrderContentListResult> CPOrderContentListResult21 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult21 = new CPOrderContentListResult();
                    cpOrderContentListResult21.setOrderContentListName("第二球");
                    cpOrderContentListResult21.setShowType("QIU");
                    cpOrderContentListResult21.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList21 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                    cpOrderContentResult21.setOrderName("1");
                    cpOrderContentResult21.setFullName("第二球");
                    cpOrderContentResult21.setOrderState(cpbjscResult.getdata1());
                    cpOrderContentResult21.setOrderId("1");
                    cpOrderContentResultList21.add(cpOrderContentResult21);

                    CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                    cpOrderContentResult22.setOrderName("2");
                    cpOrderContentResult22.setFullName("第二球");
                    cpOrderContentResult22.setOrderState(cpbjscResult.getdata2());
                    cpOrderContentResult22.setOrderId("2");
                    cpOrderContentResultList21.add(cpOrderContentResult22);

                    CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                    cpOrderContentResult23.setOrderName("3");
                    cpOrderContentResult23.setFullName("第二球");
                    cpOrderContentResult23.setOrderState(cpbjscResult.getdata3());
                    cpOrderContentResult23.setOrderId("3");
                    cpOrderContentResultList21.add(cpOrderContentResult23);

                    CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                    cpOrderContentResult24.setOrderName("4");
                    cpOrderContentResult24.setFullName("第二球");
                    cpOrderContentResult24.setOrderState(cpbjscResult.getdata4());
                    cpOrderContentResult24.setOrderId("4");
                    cpOrderContentResultList21.add(cpOrderContentResult24);

                    CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                    cpOrderContentResult25.setOrderName("5");
                    cpOrderContentResult25.setFullName("第二球");
                    cpOrderContentResult25.setOrderState(cpbjscResult.getdata5());
                    cpOrderContentResult25.setOrderId("5");
                    cpOrderContentResultList21.add(cpOrderContentResult25);

                    CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                    cpOrderContentResult26.setOrderName("6");
                    cpOrderContentResult26.setFullName("第二球");
                    cpOrderContentResult26.setOrderState(cpbjscResult.getdata6());
                    cpOrderContentResult26.setOrderId("6");
                    cpOrderContentResultList21.add(cpOrderContentResult26);

                    CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                    cpOrderContentResult27.setOrderName("7");
                    cpOrderContentResult27.setFullName("第二球");
                    cpOrderContentResult27.setOrderState(cpbjscResult.getdata7());
                    cpOrderContentResult27.setOrderId("7");
                    cpOrderContentResultList21.add(cpOrderContentResult27);

                    CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                    cpOrderContentResult28.setOrderName("8");
                    cpOrderContentResult28.setFullName("第二球");
                    cpOrderContentResult28.setOrderState(cpbjscResult.getdata8());
                    cpOrderContentResult28.setOrderId("8");
                    cpOrderContentResultList21.add(cpOrderContentResult28);

                    CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                    cpOrderContentResult29.setOrderName("9");
                    cpOrderContentResult29.setFullName("第二球");
                    cpOrderContentResult29.setOrderState(cpbjscResult.getdata9());
                    cpOrderContentResult29.setOrderId("9");
                    cpOrderContentResultList21.add(cpOrderContentResult29);

                    CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
                    cpOrderContentResult210.setOrderName("10");
                    cpOrderContentResult210.setFullName("第二球");
                    cpOrderContentResult210.setOrderState(cpbjscResult.getdata10());
                    cpOrderContentResult210.setOrderId("10");
                    cpOrderContentResultList21.add(cpOrderContentResult210);

                    CPOrderContentResult cpOrderContentResult211 = new CPOrderContentResult();
                    cpOrderContentResult211.setOrderName("11");
                    cpOrderContentResult211.setFullName("第二球");
                    cpOrderContentResult211.setOrderState(cpbjscResult.getdata11());
                    cpOrderContentResult211.setOrderId("11");
                    cpOrderContentResultList21.add(cpOrderContentResult211);

                    CPOrderContentResult cpOrderContentResult212 = new CPOrderContentResult();
                    cpOrderContentResult212.setOrderName("12");
                    cpOrderContentResult212.setFullName("第二球");
                    cpOrderContentResult212.setOrderState(cpbjscResult.getdata12());
                    cpOrderContentResult212.setOrderId("12");
                    cpOrderContentResultList21.add(cpOrderContentResult212);

                    CPOrderContentResult cpOrderContentResult213 = new CPOrderContentResult();
                    cpOrderContentResult213.setOrderName("13");
                    cpOrderContentResult213.setFullName("第二球");
                    cpOrderContentResult213.setOrderState(cpbjscResult.getdata13());
                    cpOrderContentResult213.setOrderId("13");
                    cpOrderContentResultList21.add(cpOrderContentResult213);

                    CPOrderContentResult cpOrderContentResult214 = new CPOrderContentResult();
                    cpOrderContentResult214.setOrderName("14");
                    cpOrderContentResult214.setFullName("第二球");
                    cpOrderContentResult214.setOrderState(cpbjscResult.getdata14());
                    cpOrderContentResult214.setOrderId("14");
                    cpOrderContentResultList21.add(cpOrderContentResult214);


                    CPOrderContentResult cpOrderContentResult215 = new CPOrderContentResult();
                    cpOrderContentResult215.setOrderName("15");
                    cpOrderContentResult215.setFullName("第二球");
                    cpOrderContentResult215.setOrderState(cpbjscResult.getdata15());
                    cpOrderContentResult215.setOrderId("15");
                    cpOrderContentResultList21.add(cpOrderContentResult215);

                    CPOrderContentResult cpOrderContentResult216 = new CPOrderContentResult();
                    cpOrderContentResult216.setOrderName("16");
                    cpOrderContentResult216.setFullName("第二球");
                    cpOrderContentResult216.setOrderState(cpbjscResult.getdata16());
                    cpOrderContentResult216.setOrderId("16");
                    cpOrderContentResultList21.add(cpOrderContentResult216);

                    CPOrderContentResult cpOrderContentResult217 = new CPOrderContentResult();
                    cpOrderContentResult217.setOrderName("17");
                    cpOrderContentResult217.setFullName("第二球");
                    cpOrderContentResult217.setOrderState(cpbjscResult.getdata17());
                    cpOrderContentResult217.setOrderId("17");
                    cpOrderContentResultList21.add(cpOrderContentResult217);

                    CPOrderContentResult cpOrderContentResult218 = new CPOrderContentResult();
                    cpOrderContentResult218.setOrderName("18");
                    cpOrderContentResult218.setFullName("第二球");
                    cpOrderContentResult218.setOrderState(cpbjscResult.getdata18());
                    cpOrderContentResult218.setOrderId("18");
                    cpOrderContentResultList21.add(cpOrderContentResult218);

                    CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
                    cpOrderContentListResult22.setOrderContentListName("");
                    cpOrderContentListResult22.setShowType("QIU");
                    cpOrderContentListResult22.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult219 = new CPOrderContentResult();
                    cpOrderContentResult219.setOrderName("19");
                    cpOrderContentResult219.setFullName("第二球");
                    cpOrderContentResult219.setOrderState(cpbjscResult.getdata19());
                    cpOrderContentResult219.setOrderId("19");
                    cpOrderContentResultList22.add(cpOrderContentResult219);

                    CPOrderContentResult cpOrderContentResult220 = new CPOrderContentResult();
                    cpOrderContentResult220.setOrderName("20");
                    cpOrderContentResult220.setFullName("第二球");
                    cpOrderContentResult220.setOrderState(cpbjscResult.getdata20());
                    cpOrderContentResult220.setOrderId("20");
                    cpOrderContentResultList22.add(cpOrderContentResult220);

                    CPOrderContentListResult cpOrderContentListResult23 = new CPOrderContentListResult();
                    cpOrderContentListResult23.setOrderContentListName("");
                    cpOrderContentListResult23.setShowType("ZI");
                    cpOrderContentListResult23.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList23 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult222009 = new CPOrderContentResult();
                    cpOrderContentResult222009.setOrderName("大");
                    cpOrderContentResult222009.setFullName("第二球");
                    cpOrderContentResult222009.setOrderState(cpbjscResult.getdata2009());
                    cpOrderContentResult222009.setOrderId("2009");
                    cpOrderContentResultList23.add(cpOrderContentResult222009);

                    CPOrderContentResult cpOrderContentResult222010 = new CPOrderContentResult();
                    cpOrderContentResult222010.setOrderName("小");
                    cpOrderContentResult222010.setFullName("第二球");
                    cpOrderContentResult222010.setOrderState(cpbjscResult.getdata2010());
                    cpOrderContentResult222010.setOrderId("2010");
                    cpOrderContentResultList23.add(cpOrderContentResult222010);

                    CPOrderContentResult cpOrderContentResult222011 = new CPOrderContentResult();
                    cpOrderContentResult222011.setOrderName("单");
                    cpOrderContentResult222011.setFullName("第二球");
                    cpOrderContentResult222011.setOrderState(cpbjscResult.getdata2011());
                    cpOrderContentResult222011.setOrderId("2011");
                    cpOrderContentResultList23.add(cpOrderContentResult222011);

                    CPOrderContentResult cpOrderContentResult222012 = new CPOrderContentResult();
                    cpOrderContentResult222012.setOrderName("双");
                    cpOrderContentResult222012.setFullName("第二球");
                    cpOrderContentResult222012.setOrderState(cpbjscResult.getdata2012());
                    cpOrderContentResult222012.setOrderId("2012");
                    cpOrderContentResultList23.add(cpOrderContentResult222012);

                    CPOrderContentResult cpOrderContentResult222013 = new CPOrderContentResult();
                    cpOrderContentResult222013.setOrderName("尾大");
                    cpOrderContentResult222013.setFullName("第二球");
                    cpOrderContentResult222013.setOrderState(cpbjscResult.getdata2013());
                    cpOrderContentResult222013.setOrderId("2013");
                    cpOrderContentResultList23.add(cpOrderContentResult222013);

                    CPOrderContentResult cpOrderContentResult222014 = new CPOrderContentResult();
                    cpOrderContentResult222014.setOrderName("尾小");
                    cpOrderContentResult222014.setFullName("第二球");
                    cpOrderContentResult222014.setOrderState(cpbjscResult.getdata2014());
                    cpOrderContentResult222014.setOrderId("2014");
                    cpOrderContentResultList23.add(cpOrderContentResult222014);

                    CPOrderContentResult cpOrderContentResult222015 = new CPOrderContentResult();
                    cpOrderContentResult222015.setOrderName("合单");
                    cpOrderContentResult222015.setFullName("第二球");
                    cpOrderContentResult222015.setOrderState(cpbjscResult.getdata2015());
                    cpOrderContentResult222015.setOrderId("2015");
                    cpOrderContentResultList23.add(cpOrderContentResult222015);

                    CPOrderContentResult cpOrderContentResult222016 = new CPOrderContentResult();
                    cpOrderContentResult222016.setOrderName("合双");
                    cpOrderContentResult222016.setFullName("第二球");
                    cpOrderContentResult222016.setOrderState(cpbjscResult.getdata2016());
                    cpOrderContentResult222016.setOrderId("2016");
                    cpOrderContentResultList23.add(cpOrderContentResult222016);

                    CPOrderContentResult cpOrderContentResult222017 = new CPOrderContentResult();
                    cpOrderContentResult222017.setOrderName("东");
                    cpOrderContentResult222017.setFullName("第二球");
                    cpOrderContentResult222017.setOrderState(cpbjscResult.getdata2017());
                    cpOrderContentResult222017.setOrderId("2017");
                    cpOrderContentResultList23.add(cpOrderContentResult222017);

                    CPOrderContentResult cpOrderContentResult222018 = new CPOrderContentResult();
                    cpOrderContentResult222018.setOrderName("南");
                    cpOrderContentResult222018.setFullName("第二球");
                    cpOrderContentResult222018.setOrderState(cpbjscResult.getdata2018());
                    cpOrderContentResult222018.setOrderId("2018");
                    cpOrderContentResultList23.add(cpOrderContentResult222018);

                    CPOrderContentResult cpOrderContentResult222019 = new CPOrderContentResult();
                    cpOrderContentResult222019.setOrderName("西");
                    cpOrderContentResult222019.setFullName("第二球");
                    cpOrderContentResult222019.setOrderState(cpbjscResult.getdata2019());
                    cpOrderContentResult222019.setOrderId("2019");
                    cpOrderContentResultList23.add(cpOrderContentResult222019);

                    CPOrderContentResult cpOrderContentResult1222020 = new CPOrderContentResult();
                    cpOrderContentResult1222020.setOrderName("北");
                    cpOrderContentResult1222020.setFullName("第二球");
                    cpOrderContentResult1222020.setOrderState(cpbjscResult.getdata2020());
                    cpOrderContentResult1222020.setOrderId("2020");
                    cpOrderContentResultList23.add(cpOrderContentResult1222020);

                    CPOrderContentListResult cpOrderContentListResult24 = new CPOrderContentListResult();
                    cpOrderContentListResult24.setOrderContentListName("");
                    cpOrderContentListResult24.setShowType("ZI");
                    cpOrderContentListResult24.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList24 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult222021 = new CPOrderContentResult();
                    cpOrderContentResult222021.setOrderName("中");
                    cpOrderContentResult222021.setFullName("第二球");
                    cpOrderContentResult222021.setOrderState(cpbjscResult.getdata2021());
                    cpOrderContentResult222021.setOrderId("2021");
                    cpOrderContentResultList24.add(cpOrderContentResult222021);

                    CPOrderContentResult cpOrderContentResult222022 = new CPOrderContentResult();
                    cpOrderContentResult222022.setOrderName("发");
                    cpOrderContentResult222022.setFullName("第二球");
                    cpOrderContentResult222022.setOrderState(cpbjscResult.getdata2022());
                    cpOrderContentResult222022.setOrderId("2022");
                    cpOrderContentResultList24.add(cpOrderContentResult222022);

                    CPOrderContentResult cpOrderContentResult222023 = new CPOrderContentResult();
                    cpOrderContentResult222023.setOrderName("白");
                    cpOrderContentResult222023.setFullName("第二球");
                    cpOrderContentResult222023.setOrderState(cpbjscResult.getdata2023());
                    cpOrderContentResult222023.setOrderId("2023");
                    cpOrderContentResultList24.add(cpOrderContentResult222023);


                    cpOrderContentListResult21.setData(cpOrderContentResultList21);
                    cpOrderContentListResult22.setData(cpOrderContentResultList22);
                    cpOrderContentListResult23.setData(cpOrderContentResultList23);
                    cpOrderContentListResult24.setData(cpOrderContentResultList24);
                    CPOrderContentListResult21.add(cpOrderContentListResult21);
                    CPOrderContentListResult21.add(cpOrderContentListResult22);
                    CPOrderContentListResult21.add(cpOrderContentListResult23);
                    CPOrderContentListResult21.add(cpOrderContentListResult24);

                    allResult.setData(CPOrderContentListResult21);
                    allResultList.add(allResult);
                    break;
                case 3:
                    /*allResult.setOrderAllName("第三球");

                    List<CPOrderContentListResult> CPOrderContentListResult31 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult31 = new CPOrderContentListResult();
                    cpOrderContentListResult31.setOrderContentListName("第三球");
                    cpOrderContentListResult31.setShowType("QIU");
                    cpOrderContentListResult31.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList31 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                    cpOrderContentResult31.setOrderName("1");
                    cpOrderContentResult31.setFullName("第三球");
                    cpOrderContentResult31.setOrderState(cpbjscResult.getdata1());
                    cpOrderContentResult31.setOrderId("1");
                    cpOrderContentResultList31.add(cpOrderContentResult31);

                    CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                    cpOrderContentResult32.setOrderName("2");
                    cpOrderContentResult32.setFullName("第三球");
                    cpOrderContentResult32.setOrderState(cpbjscResult.getdata2());
                    cpOrderContentResult32.setOrderId("2");
                    cpOrderContentResultList31.add(cpOrderContentResult32);

                    CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                    cpOrderContentResult33.setOrderName("3");
                    cpOrderContentResult33.setFullName("第三球");
                    cpOrderContentResult33.setOrderState(cpbjscResult.getdata3());
                    cpOrderContentResult33.setOrderId("3");
                    cpOrderContentResultList31.add(cpOrderContentResult33);

                    CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                    cpOrderContentResult44.setOrderName("4");
                    cpOrderContentResult44.setFullName("第三球");
                    cpOrderContentResult44.setOrderState(cpbjscResult.getdata4());
                    cpOrderContentResult44.setOrderId("3");
                    cpOrderContentResultList31.add(cpOrderContentResult44);

                    CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                    cpOrderContentResult45.setOrderName("5");
                    cpOrderContentResult45.setFullName("第三球");
                    cpOrderContentResult45.setOrderState(cpbjscResult.getdata5());
                    cpOrderContentResult45.setOrderId("5");
                    cpOrderContentResultList31.add(cpOrderContentResult45);

                    CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                    cpOrderContentResult46.setOrderName("6");
                    cpOrderContentResult46.setFullName("第三球");
                    cpOrderContentResult46.setOrderState(cpbjscResult.getdata6());
                    cpOrderContentResult46.setOrderId("6");
                    cpOrderContentResultList31.add(cpOrderContentResult46);

                    CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                    cpOrderContentResult47.setOrderName("7");
                    cpOrderContentResult47.setFullName("第三球");
                    cpOrderContentResult47.setOrderState(cpbjscResult.getdata7());
                    cpOrderContentResult47.setOrderId("7");
                    cpOrderContentResultList31.add(cpOrderContentResult47);

                    CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                    cpOrderContentResult48.setOrderName("8");
                    cpOrderContentResult48.setFullName("第三球");
                    cpOrderContentResult48.setOrderState(cpbjscResult.getdata8());
                    cpOrderContentResult48.setOrderId("8");
                    cpOrderContentResultList31.add(cpOrderContentResult48);

                    CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                    cpOrderContentResult49.setOrderName("9");
                    cpOrderContentResult49.setFullName("第三球");
                    cpOrderContentResult49.setOrderState(cpbjscResult.getdata9());
                    cpOrderContentResult49.setOrderId("9");
                    cpOrderContentResultList31.add(cpOrderContentResult49);

                    CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                    cpOrderContentResult410.setOrderName("10");
                    cpOrderContentResult410.setFullName("第三球");
                    cpOrderContentResult410.setOrderState(cpbjscResult.getdata10());
                    cpOrderContentResult410.setOrderId("10");
                    cpOrderContentResultList31.add(cpOrderContentResult410);

                    CPOrderContentResult cpOrderContentResult411 = new CPOrderContentResult();
                    cpOrderContentResult411.setOrderName("11");
                    cpOrderContentResult411.setFullName("第三球");
                    cpOrderContentResult411.setOrderState(cpbjscResult.getdata11());
                    cpOrderContentResult411.setOrderId("11");
                    cpOrderContentResultList31.add(cpOrderContentResult411);

                    CPOrderContentResult cpOrderContentResult412 = new CPOrderContentResult();
                    cpOrderContentResult412.setOrderName("12");
                    cpOrderContentResult412.setFullName("第三球");
                    cpOrderContentResult412.setOrderState(cpbjscResult.getdata12());
                    cpOrderContentResult412.setOrderId("12");
                    cpOrderContentResultList31.add(cpOrderContentResult412);

                    CPOrderContentResult cpOrderContentResult413 = new CPOrderContentResult();
                    cpOrderContentResult413.setOrderName("13");
                    cpOrderContentResult413.setFullName("第三球");
                    cpOrderContentResult413.setOrderState(cpbjscResult.getdata13());
                    cpOrderContentResult413.setOrderId("13");
                    cpOrderContentResultList31.add(cpOrderContentResult413);

                    CPOrderContentResult cpOrderContentResult414 = new CPOrderContentResult();
                    cpOrderContentResult414.setOrderName("14");
                    cpOrderContentResult414.setFullName("第三球");
                    cpOrderContentResult414.setOrderState(cpbjscResult.getdata14());
                    cpOrderContentResult414.setOrderId("14");
                    cpOrderContentResultList31.add(cpOrderContentResult414);


                    CPOrderContentResult cpOrderContentResult415 = new CPOrderContentResult();
                    cpOrderContentResult415.setOrderName("15");
                    cpOrderContentResult415.setFullName("第三球");
                    cpOrderContentResult415.setOrderState(cpbjscResult.getdata15());
                    cpOrderContentResult415.setOrderId("15");
                    cpOrderContentResultList31.add(cpOrderContentResult415);

                    CPOrderContentResult cpOrderContentResult416 = new CPOrderContentResult();
                    cpOrderContentResult416.setOrderName("16");
                    cpOrderContentResult416.setFullName("第三球");
                    cpOrderContentResult416.setOrderState(cpbjscResult.getdata16());
                    cpOrderContentResult416.setOrderId("16");
                    cpOrderContentResultList31.add(cpOrderContentResult416);

                    CPOrderContentResult cpOrderContentResult417 = new CPOrderContentResult();
                    cpOrderContentResult417.setOrderName("17");
                    cpOrderContentResult417.setFullName("第三球");
                    cpOrderContentResult417.setOrderState(cpbjscResult.getdata17());
                    cpOrderContentResult417.setOrderId("17");
                    cpOrderContentResultList31.add(cpOrderContentResult417);

                    CPOrderContentResult cpOrderContentResult418 = new CPOrderContentResult();
                    cpOrderContentResult418.setOrderName("18");
                    cpOrderContentResult418.setFullName("第三球");
                    cpOrderContentResult418.setOrderState(cpbjscResult.getdata18());
                    cpOrderContentResult418.setOrderId("18");
                    cpOrderContentResultList31.add(cpOrderContentResult418);

                    CPOrderContentListResult cpOrderContentListResult32 = new CPOrderContentListResult();
                    cpOrderContentListResult32.setOrderContentListName("");
                    cpOrderContentListResult32.setShowType("QIU");
                    cpOrderContentListResult32.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList32 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult319 = new CPOrderContentResult();
                    cpOrderContentResult319.setOrderName("19");
                    cpOrderContentResult319.setFullName("第三球");
                    cpOrderContentResult319.setOrderState(cpbjscResult.getdata19());
                    cpOrderContentResult319.setOrderId("19");
                    cpOrderContentResultList32.add(cpOrderContentResult319);

                    CPOrderContentResult cpOrderContentResult320 = new CPOrderContentResult();
                    cpOrderContentResult320.setOrderName("20");
                    cpOrderContentResult320.setFullName("第三球");
                    cpOrderContentResult320.setOrderState(cpbjscResult.getdata20());
                    cpOrderContentResult320.setOrderId("20");
                    cpOrderContentResultList32.add(cpOrderContentResult320);

                    CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
                    cpOrderContentListResult33.setOrderContentListName("");
                    cpOrderContentListResult33.setShowType("ZI");
                    cpOrderContentListResult33.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult332009 = new CPOrderContentResult();
                    cpOrderContentResult332009.setOrderName("大");
                    cpOrderContentResult332009.setFullName("第三球");
                    cpOrderContentResult332009.setOrderState(cpbjscResult.getdata2009());
                    cpOrderContentResult332009.setOrderId("2009");
                    cpOrderContentResultList33.add(cpOrderContentResult332009);

                    CPOrderContentResult cpOrderContentResult332010 = new CPOrderContentResult();
                    cpOrderContentResult332010.setOrderName("小");
                    cpOrderContentResult332010.setFullName("第三球");
                    cpOrderContentResult332010.setOrderState(cpbjscResult.getdata2010());
                    cpOrderContentResult332010.setOrderId("2010");
                    cpOrderContentResultList33.add(cpOrderContentResult332010);

                    CPOrderContentResult cpOrderContentResult332011 = new CPOrderContentResult();
                    cpOrderContentResult332011.setOrderName("单");
                    cpOrderContentResult332011.setFullName("第三球");
                    cpOrderContentResult332011.setOrderState(cpbjscResult.getdata2011());
                    cpOrderContentResult332011.setOrderId("2011");
                    cpOrderContentResultList33.add(cpOrderContentResult332011);

                    CPOrderContentResult cpOrderContentResult332012 = new CPOrderContentResult();
                    cpOrderContentResult332012.setOrderName("双");
                    cpOrderContentResult332012.setFullName("第三球");
                    cpOrderContentResult332012.setOrderState(cpbjscResult.getdata2012());
                    cpOrderContentResult332012.setOrderId("2012");
                    cpOrderContentResultList33.add(cpOrderContentResult332012);

                    CPOrderContentResult cpOrderContentResult332013 = new CPOrderContentResult();
                    cpOrderContentResult332013.setOrderName("尾大");
                    cpOrderContentResult332013.setFullName("第三球");
                    cpOrderContentResult332013.setOrderState(cpbjscResult.getdata2013());
                    cpOrderContentResult332013.setOrderId("2013");
                    cpOrderContentResultList33.add(cpOrderContentResult332013);

                    CPOrderContentResult cpOrderContentResult332014 = new CPOrderContentResult();
                    cpOrderContentResult332014.setOrderName("尾小");
                    cpOrderContentResult332014.setFullName("第三球");
                    cpOrderContentResult332014.setOrderState(cpbjscResult.getdata2014());
                    cpOrderContentResult332014.setOrderId("2014");
                    cpOrderContentResultList33.add(cpOrderContentResult332014);

                    CPOrderContentResult cpOrderContentResult332015 = new CPOrderContentResult();
                    cpOrderContentResult332015.setOrderName("合单");
                    cpOrderContentResult332015.setFullName("第三球");
                    cpOrderContentResult332015.setOrderState(cpbjscResult.getdata2015());
                    cpOrderContentResult332015.setOrderId("2015");
                    cpOrderContentResultList33.add(cpOrderContentResult332015);

                    CPOrderContentResult cpOrderContentResult332016 = new CPOrderContentResult();
                    cpOrderContentResult332016.setOrderName("合双");
                    cpOrderContentResult332016.setFullName("第三球");
                    cpOrderContentResult332016.setOrderState(cpbjscResult.getdata2016());
                    cpOrderContentResult332016.setOrderId("2016");
                    cpOrderContentResultList33.add(cpOrderContentResult332016);

                    CPOrderContentResult cpOrderContentResult332017 = new CPOrderContentResult();
                    cpOrderContentResult332017.setOrderName("东");
                    cpOrderContentResult332017.setFullName("第三球");
                    cpOrderContentResult332017.setOrderState(cpbjscResult.getdata2017());
                    cpOrderContentResult332017.setOrderId("2017");
                    cpOrderContentResultList33.add(cpOrderContentResult332017);

                    CPOrderContentResult cpOrderContentResult332018 = new CPOrderContentResult();
                    cpOrderContentResult332018.setOrderName("南");
                    cpOrderContentResult332018.setFullName("第三球");
                    cpOrderContentResult332018.setOrderState(cpbjscResult.getdata2018());
                    cpOrderContentResult332018.setOrderId("2018");
                    cpOrderContentResultList33.add(cpOrderContentResult332018);

                    CPOrderContentResult cpOrderContentResult332019 = new CPOrderContentResult();
                    cpOrderContentResult332019.setOrderName("西");
                    cpOrderContentResult332019.setFullName("第三球");
                    cpOrderContentResult332019.setOrderState(cpbjscResult.getdata2019());
                    cpOrderContentResult332019.setOrderId("2019");
                    cpOrderContentResultList33.add(cpOrderContentResult332019);

                    CPOrderContentResult cpOrderContentResult1332020 = new CPOrderContentResult();
                    cpOrderContentResult1332020.setOrderName("北");
                    cpOrderContentResult1332020.setFullName("第三球");
                    cpOrderContentResult1332020.setOrderState(cpbjscResult.getdata2020());
                    cpOrderContentResult1332020.setOrderId("2020");
                    cpOrderContentResultList33.add(cpOrderContentResult1332020);

                    CPOrderContentListResult cpOrderContentListResult34 = new CPOrderContentListResult();
                    cpOrderContentListResult34.setOrderContentListName("");
                    cpOrderContentListResult34.setShowType("ZI");
                    cpOrderContentListResult34.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList34 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult332021 = new CPOrderContentResult();
                    cpOrderContentResult332021.setOrderName("中");
                    cpOrderContentResult332021.setFullName("第三球");
                    cpOrderContentResult332021.setOrderState(cpbjscResult.getdata2021());
                    cpOrderContentResult332021.setOrderId("2021");
                    cpOrderContentResultList34.add(cpOrderContentResult332021);

                    CPOrderContentResult cpOrderContentResult332022 = new CPOrderContentResult();
                    cpOrderContentResult332022.setOrderName("发");
                    cpOrderContentResult332022.setFullName("第三球");
                    cpOrderContentResult332022.setOrderState(cpbjscResult.getdata2022());
                    cpOrderContentResult332022.setOrderId("2022");
                    cpOrderContentResultList34.add(cpOrderContentResult332022);

                    CPOrderContentResult cpOrderContentResult332023 = new CPOrderContentResult();
                    cpOrderContentResult332023.setOrderName("白");
                    cpOrderContentResult332023.setFullName("第三球");
                    cpOrderContentResult332023.setOrderState(cpbjscResult.getdata2023());
                    cpOrderContentResult332023.setOrderId("2023");
                    cpOrderContentResultList34.add(cpOrderContentResult332023);

                    cpOrderContentListResult31.setData(cpOrderContentResultList31);
                    cpOrderContentListResult32.setData(cpOrderContentResultList32);
                    cpOrderContentListResult33.setData(cpOrderContentResultList33);
                    cpOrderContentListResult34.setData(cpOrderContentResultList34);
                    CPOrderContentListResult31.add(cpOrderContentListResult31);
                    CPOrderContentListResult31.add(cpOrderContentListResult32);
                    CPOrderContentListResult31.add(cpOrderContentListResult33);
                    CPOrderContentListResult31.add(cpOrderContentListResult34);

                    allResult.setData(CPOrderContentListResult31);
                    allResultList.add(allResult);*/

                    onXyncIdex(allResult,cpbjscResult,"第三球");
                    break;
                case 4:
                    onXyncIdex(allResult,cpbjscResult,"第四球");
                    break;
                case 5:
                    onXyncIdex(allResult,cpbjscResult,"第五球");
                    break;
                case 6:
                    onXyncIdex(allResult,cpbjscResult,"第六球");
                    break;
                case 7:
                    onXyncIdex(allResult,cpbjscResult,"第七球");
                    break;
                case 8:
                    onXyncIdex(allResult,cpbjscResult,"第八球");
                    break;
                case 9:
                    allResult.setOrderAllName("连码");
                    List<CPOrderContentListResult> CPOrderContentListResult91 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult91 = new CPOrderContentListResult();
                    //cpOrderContentListResult91.setOrderContentListName("赔率:"+cpbjscResult.getdata2023());
                    rX2 = cpbjscResult.getdata2032();
                    rX3 = cpbjscResult.getdata2035();
                    rX4 = cpbjscResult.getdata2038();
                    rX5 = cpbjscResult.getdata2039();

                    cpOrderContentListResult91.setShowType("QIU");
                    cpOrderContentListResult91.setShowNumber(3);

                    List<CPOrderContentResult> cpOrderContentResultList91 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                    cpOrderContentResult31.setOrderName("1");
                    cpOrderContentResult31.setFullName("");
                    cpOrderContentResult31.setOrderState("");
                    cpOrderContentResult31.setOrderId("1");
                    cpOrderContentResultList91.add(cpOrderContentResult31);

                    CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                    cpOrderContentResult32.setOrderName("2");
                    cpOrderContentResult32.setFullName("");
                    cpOrderContentResult32.setOrderState("");
                    cpOrderContentResult32.setOrderId("2");
                    cpOrderContentResultList91.add(cpOrderContentResult32);

                    CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                    cpOrderContentResult33.setOrderName("3");
                    cpOrderContentResult33.setFullName("");
                    cpOrderContentResult33.setOrderState("");
                    cpOrderContentResult33.setOrderId("3");
                    cpOrderContentResultList91.add(cpOrderContentResult33);

                    CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                    cpOrderContentResult44.setOrderName("4");
                    cpOrderContentResult44.setFullName("");
                    cpOrderContentResult44.setOrderState("");
                    cpOrderContentResult44.setOrderId("4");
                    cpOrderContentResultList91.add(cpOrderContentResult44);

                    CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                    cpOrderContentResult45.setOrderName("5");
                    cpOrderContentResult45.setFullName("");
                    cpOrderContentResult45.setOrderState("");
                    cpOrderContentResult45.setOrderId("5");
                    cpOrderContentResultList91.add(cpOrderContentResult45);

                    CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                    cpOrderContentResult46.setOrderName("6");
                    cpOrderContentResult46.setFullName("");
                    cpOrderContentResult46.setOrderState("");
                    cpOrderContentResult46.setOrderId("6");
                    cpOrderContentResultList91.add(cpOrderContentResult46);

                    CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                    cpOrderContentResult47.setOrderName("7");
                    cpOrderContentResult47.setFullName("");
                    cpOrderContentResult47.setOrderState("");
                    cpOrderContentResult47.setOrderId("7");
                    cpOrderContentResultList91.add(cpOrderContentResult47);

                    CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                    cpOrderContentResult48.setOrderName("8");
                    cpOrderContentResult48.setFullName("");
                    cpOrderContentResult48.setOrderState("");
                    cpOrderContentResult48.setOrderId("8");
                    cpOrderContentResultList91.add(cpOrderContentResult48);

                    CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                    cpOrderContentResult49.setOrderName("9");
                    cpOrderContentResult49.setFullName("");
                    cpOrderContentResult49.setOrderState("");
                    cpOrderContentResult49.setOrderId("9");
                    cpOrderContentResultList91.add(cpOrderContentResult49);

                    CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                    cpOrderContentResult410.setOrderName("10");
                    cpOrderContentResult410.setFullName("");
                    cpOrderContentResult410.setOrderState("");
                    cpOrderContentResult410.setOrderId("10");
                    cpOrderContentResultList91.add(cpOrderContentResult410);

                    CPOrderContentResult cpOrderContentResult411 = new CPOrderContentResult();
                    cpOrderContentResult411.setOrderName("11");
                    cpOrderContentResult411.setFullName("");
                    cpOrderContentResult411.setOrderState("");
                    cpOrderContentResult411.setOrderId("11");
                    cpOrderContentResultList91.add(cpOrderContentResult411);

                    CPOrderContentResult cpOrderContentResult412 = new CPOrderContentResult();
                    cpOrderContentResult412.setOrderName("12");
                    cpOrderContentResult412.setFullName("");
                    cpOrderContentResult412.setOrderState("");
                    cpOrderContentResult412.setOrderId("12");
                    cpOrderContentResultList91.add(cpOrderContentResult412);

                    CPOrderContentResult cpOrderContentResult413 = new CPOrderContentResult();
                    cpOrderContentResult413.setOrderName("13");
                    cpOrderContentResult413.setFullName("");
                    cpOrderContentResult413.setOrderState("");
                    cpOrderContentResult413.setOrderId("13");
                    cpOrderContentResultList91.add(cpOrderContentResult413);

                    CPOrderContentResult cpOrderContentResult414 = new CPOrderContentResult();
                    cpOrderContentResult414.setOrderName("14");
                    cpOrderContentResult414.setFullName("");
                    cpOrderContentResult414.setOrderState("");
                    cpOrderContentResult414.setOrderId("14");
                    cpOrderContentResultList91.add(cpOrderContentResult414);


                    CPOrderContentResult cpOrderContentResult415 = new CPOrderContentResult();
                    cpOrderContentResult415.setOrderName("15");
                    cpOrderContentResult415.setFullName("");
                    cpOrderContentResult415.setOrderState("");
                    cpOrderContentResult415.setOrderId("15");
                    cpOrderContentResultList91.add(cpOrderContentResult415);

                    CPOrderContentResult cpOrderContentResult416 = new CPOrderContentResult();
                    cpOrderContentResult416.setOrderName("16");
                    cpOrderContentResult416.setFullName("");
                    cpOrderContentResult416.setOrderState("");
                    cpOrderContentResult416.setOrderId("16");
                    cpOrderContentResultList91.add(cpOrderContentResult416);

                    CPOrderContentResult cpOrderContentResult417 = new CPOrderContentResult();
                    cpOrderContentResult417.setOrderName("17");
                    cpOrderContentResult417.setFullName("");
                    cpOrderContentResult417.setOrderState("");
                    cpOrderContentResult417.setOrderId("17");
                    cpOrderContentResultList91.add(cpOrderContentResult417);

                    CPOrderContentResult cpOrderContentResult418 = new CPOrderContentResult();
                    cpOrderContentResult418.setOrderName("18");
                    cpOrderContentResult418.setFullName("");
                    cpOrderContentResult418.setOrderState("");
                    cpOrderContentResult418.setOrderId("18");
                    cpOrderContentResultList91.add(cpOrderContentResult418);


                    CPOrderContentListResult cpOrderContentListResult92 = new CPOrderContentListResult();
                    cpOrderContentListResult92.setOrderContentListName("");
                    cpOrderContentListResult92.setShowType("QIU");
                    cpOrderContentListResult92.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList92 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult419 = new CPOrderContentResult();
                    cpOrderContentResult419.setOrderName("19");
                    cpOrderContentResult419.setFullName("");
                    cpOrderContentResult419.setOrderState("");
                    cpOrderContentResult419.setOrderId("19");
                    cpOrderContentResultList92.add(cpOrderContentResult419);

                    CPOrderContentResult cpOrderContentResult420 = new CPOrderContentResult();
                    cpOrderContentResult420.setOrderName("20");
                    cpOrderContentResult420.setFullName("");
                    cpOrderContentResult420.setOrderState("");
                    cpOrderContentResult420.setOrderId("20");
                    cpOrderContentResultList92.add(cpOrderContentResult420);

                    cpOrderContentListResult91.setData(cpOrderContentResultList91);
                    cpOrderContentListResult92.setData(cpOrderContentResultList92);

                    CPOrderContentListResult91.add(cpOrderContentListResult91);
                    CPOrderContentListResult91.add(cpOrderContentListResult92);

                    allResult.setData(CPOrderContentListResult91);
                    allResultList.add(allResult);
                    break;
            }
        }
    }

    private void onXyncIdex(CPOrderAllResult allResult,CPXYNCResult cpbjscResult,String otherName ){
        allResult.setOrderAllName(otherName);
        List<CPOrderContentListResult> CPOrderContentListResult31 = new ArrayList<CPOrderContentListResult>();
        CPOrderContentListResult cpOrderContentListResult31 = new CPOrderContentListResult();
        cpOrderContentListResult31.setOrderContentListName(otherName);
        cpOrderContentListResult31.setShowType("QIU");
        cpOrderContentListResult31.setShowNumber(3);

        List<CPOrderContentResult> cpOrderContentResultList31 = new ArrayList<>();
        CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
        cpOrderContentResult31.setOrderName("1");
        cpOrderContentResult31.setFullName(otherName);
        cpOrderContentResult31.setOrderState(cpbjscResult.getdata1());
        cpOrderContentResult31.setOrderId("1");
        cpOrderContentResultList31.add(cpOrderContentResult31);

        CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
        cpOrderContentResult32.setOrderName("2");
        cpOrderContentResult32.setFullName(otherName);
        cpOrderContentResult32.setOrderState(cpbjscResult.getdata2());
        cpOrderContentResult32.setOrderId("2");
        cpOrderContentResultList31.add(cpOrderContentResult32);

        CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
        cpOrderContentResult33.setOrderName("3");
        cpOrderContentResult33.setFullName(otherName);
        cpOrderContentResult33.setOrderState(cpbjscResult.getdata3());
        cpOrderContentResult33.setOrderId("3");
        cpOrderContentResultList31.add(cpOrderContentResult33);

        CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
        cpOrderContentResult44.setOrderName("4");
        cpOrderContentResult44.setFullName(otherName);
        cpOrderContentResult44.setOrderState(cpbjscResult.getdata4());
        cpOrderContentResult44.setOrderId("4");
        cpOrderContentResultList31.add(cpOrderContentResult44);

        CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
        cpOrderContentResult45.setOrderName("5");
        cpOrderContentResult45.setFullName(otherName);
        cpOrderContentResult45.setOrderState(cpbjscResult.getdata5());
        cpOrderContentResult45.setOrderId("5");
        cpOrderContentResultList31.add(cpOrderContentResult45);

        CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
        cpOrderContentResult46.setOrderName("6");
        cpOrderContentResult46.setFullName(otherName);
        cpOrderContentResult46.setOrderState(cpbjscResult.getdata6());
        cpOrderContentResult46.setOrderId("6");
        cpOrderContentResultList31.add(cpOrderContentResult46);

        CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
        cpOrderContentResult47.setOrderName("7");
        cpOrderContentResult47.setFullName(otherName);
        cpOrderContentResult47.setOrderState(cpbjscResult.getdata7());
        cpOrderContentResult47.setOrderId("7");
        cpOrderContentResultList31.add(cpOrderContentResult47);

        CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
        cpOrderContentResult48.setOrderName("8");
        cpOrderContentResult48.setFullName(otherName);
        cpOrderContentResult48.setOrderState(cpbjscResult.getdata8());
        cpOrderContentResult48.setOrderId("8");
        cpOrderContentResultList31.add(cpOrderContentResult48);

        CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
        cpOrderContentResult49.setOrderName("9");
        cpOrderContentResult49.setFullName(otherName);
        cpOrderContentResult49.setOrderState(cpbjscResult.getdata9());
        cpOrderContentResult49.setOrderId("9");
        cpOrderContentResultList31.add(cpOrderContentResult49);

        CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
        cpOrderContentResult410.setOrderName("10");
        cpOrderContentResult410.setFullName(otherName);
        cpOrderContentResult410.setOrderState(cpbjscResult.getdata10());
        cpOrderContentResult410.setOrderId("10");
        cpOrderContentResultList31.add(cpOrderContentResult410);

        CPOrderContentResult cpOrderContentResult411 = new CPOrderContentResult();
        cpOrderContentResult411.setOrderName("11");
        cpOrderContentResult411.setFullName(otherName);
        cpOrderContentResult411.setOrderState(cpbjscResult.getdata11());
        cpOrderContentResult411.setOrderId("11");
        cpOrderContentResultList31.add(cpOrderContentResult411);

        CPOrderContentResult cpOrderContentResult412 = new CPOrderContentResult();
        cpOrderContentResult412.setOrderName("12");
        cpOrderContentResult412.setFullName(otherName);
        cpOrderContentResult412.setOrderState(cpbjscResult.getdata12());
        cpOrderContentResult412.setOrderId("12");
        cpOrderContentResultList31.add(cpOrderContentResult412);

        CPOrderContentResult cpOrderContentResult413 = new CPOrderContentResult();
        cpOrderContentResult413.setOrderName("13");
        cpOrderContentResult413.setFullName(otherName);
        cpOrderContentResult413.setOrderState(cpbjscResult.getdata13());
        cpOrderContentResult413.setOrderId("13");
        cpOrderContentResultList31.add(cpOrderContentResult413);

        CPOrderContentResult cpOrderContentResult414 = new CPOrderContentResult();
        cpOrderContentResult414.setOrderName("14");
        cpOrderContentResult414.setFullName(otherName);
        cpOrderContentResult414.setOrderState(cpbjscResult.getdata14());
        cpOrderContentResult414.setOrderId("14");
        cpOrderContentResultList31.add(cpOrderContentResult414);


        CPOrderContentResult cpOrderContentResult415 = new CPOrderContentResult();
        cpOrderContentResult415.setOrderName("15");
        cpOrderContentResult415.setFullName(otherName);
        cpOrderContentResult415.setOrderState(cpbjscResult.getdata15());
        cpOrderContentResult415.setOrderId("15");
        cpOrderContentResultList31.add(cpOrderContentResult415);

        CPOrderContentResult cpOrderContentResult416 = new CPOrderContentResult();
        cpOrderContentResult416.setOrderName("16");
        cpOrderContentResult416.setFullName(otherName);
        cpOrderContentResult416.setOrderState(cpbjscResult.getdata16());
        cpOrderContentResult416.setOrderId("16");
        cpOrderContentResultList31.add(cpOrderContentResult416);

        CPOrderContentResult cpOrderContentResult417 = new CPOrderContentResult();
        cpOrderContentResult417.setOrderName("17");
        cpOrderContentResult417.setFullName(otherName);
        cpOrderContentResult417.setOrderState(cpbjscResult.getdata17());
        cpOrderContentResult417.setOrderId("17");
        cpOrderContentResultList31.add(cpOrderContentResult417);

        CPOrderContentResult cpOrderContentResult418 = new CPOrderContentResult();
        cpOrderContentResult418.setOrderName("18");
        cpOrderContentResult418.setFullName(otherName);
        cpOrderContentResult418.setOrderState(cpbjscResult.getdata18());
        cpOrderContentResult418.setOrderId("18");
        cpOrderContentResultList31.add(cpOrderContentResult418);

        CPOrderContentListResult cpOrderContentListResult32 = new CPOrderContentListResult();
        cpOrderContentListResult32.setOrderContentListName("");
        cpOrderContentListResult32.setShowType("QIU");
        cpOrderContentListResult32.setShowNumber(2);

        List<CPOrderContentResult> cpOrderContentResultList32 = new ArrayList<>();
        CPOrderContentResult cpOrderContentResult319 = new CPOrderContentResult();
        cpOrderContentResult319.setOrderName("19");
        cpOrderContentResult319.setFullName(otherName);
        cpOrderContentResult319.setOrderState(cpbjscResult.getdata19());
        cpOrderContentResult319.setOrderId("19");
        cpOrderContentResultList32.add(cpOrderContentResult319);

        CPOrderContentResult cpOrderContentResult320 = new CPOrderContentResult();
        cpOrderContentResult320.setOrderName("20");
        cpOrderContentResult320.setFullName(otherName);
        cpOrderContentResult320.setOrderState(cpbjscResult.getdata20());
        cpOrderContentResult320.setOrderId("20");
        cpOrderContentResultList32.add(cpOrderContentResult320);

        CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
        cpOrderContentListResult33.setOrderContentListName("");
        cpOrderContentListResult33.setShowType("ZI");
        cpOrderContentListResult33.setShowNumber(2);

        List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();
        CPOrderContentResult cpOrderContentResult332009 = new CPOrderContentResult();
        cpOrderContentResult332009.setOrderName("大");
        cpOrderContentResult332009.setFullName(otherName);
        cpOrderContentResult332009.setOrderState(cpbjscResult.getdata2009());
        cpOrderContentResult332009.setOrderId("2009");
        cpOrderContentResultList33.add(cpOrderContentResult332009);

        CPOrderContentResult cpOrderContentResult332010 = new CPOrderContentResult();
        cpOrderContentResult332010.setOrderName("小");
        cpOrderContentResult332010.setFullName(otherName);
        cpOrderContentResult332010.setOrderState(cpbjscResult.getdata2010());
        cpOrderContentResult332010.setOrderId("2010");
        cpOrderContentResultList33.add(cpOrderContentResult332010);

        CPOrderContentResult cpOrderContentResult332011 = new CPOrderContentResult();
        cpOrderContentResult332011.setOrderName("单");
        cpOrderContentResult332011.setFullName(otherName);
        cpOrderContentResult332011.setOrderState(cpbjscResult.getdata2011());
        cpOrderContentResult332011.setOrderId("2011");
        cpOrderContentResultList33.add(cpOrderContentResult332011);

        CPOrderContentResult cpOrderContentResult332012 = new CPOrderContentResult();
        cpOrderContentResult332012.setOrderName("双");
        cpOrderContentResult332012.setFullName(otherName);
        cpOrderContentResult332012.setOrderState(cpbjscResult.getdata2012());
        cpOrderContentResult332012.setOrderId("2012");
        cpOrderContentResultList33.add(cpOrderContentResult332012);

        CPOrderContentResult cpOrderContentResult332013 = new CPOrderContentResult();
        cpOrderContentResult332013.setOrderName("尾大");
        cpOrderContentResult332013.setFullName(otherName);
        cpOrderContentResult332013.setOrderState(cpbjscResult.getdata2013());
        cpOrderContentResult332013.setOrderId("2013");
        cpOrderContentResultList33.add(cpOrderContentResult332013);

        CPOrderContentResult cpOrderContentResult332014 = new CPOrderContentResult();
        cpOrderContentResult332014.setOrderName("尾小");
        cpOrderContentResult332014.setFullName(otherName);
        cpOrderContentResult332014.setOrderState(cpbjscResult.getdata2014());
        cpOrderContentResult332014.setOrderId("2014");
        cpOrderContentResultList33.add(cpOrderContentResult332014);

        CPOrderContentResult cpOrderContentResult332015 = new CPOrderContentResult();
        cpOrderContentResult332015.setOrderName("合单");
        cpOrderContentResult332015.setFullName(otherName);
        cpOrderContentResult332015.setOrderState(cpbjscResult.getdata2015());
        cpOrderContentResult332015.setOrderId("2015");
        cpOrderContentResultList33.add(cpOrderContentResult332015);

        CPOrderContentResult cpOrderContentResult332016 = new CPOrderContentResult();
        cpOrderContentResult332016.setOrderName("合双");
        cpOrderContentResult332016.setFullName(otherName);
        cpOrderContentResult332016.setOrderState(cpbjscResult.getdata2016());
        cpOrderContentResult332016.setOrderId("2016");
        cpOrderContentResultList33.add(cpOrderContentResult332016);

        CPOrderContentResult cpOrderContentResult332017 = new CPOrderContentResult();
        cpOrderContentResult332017.setOrderName("东");
        cpOrderContentResult332017.setFullName(otherName);
        cpOrderContentResult332017.setOrderState(cpbjscResult.getdata2017());
        cpOrderContentResult332017.setOrderId("2017");
        cpOrderContentResultList33.add(cpOrderContentResult332017);

        CPOrderContentResult cpOrderContentResult332018 = new CPOrderContentResult();
        cpOrderContentResult332018.setOrderName("南");
        cpOrderContentResult332018.setFullName(otherName);
        cpOrderContentResult332018.setOrderState(cpbjscResult.getdata2018());
        cpOrderContentResult332018.setOrderId("2018");
        cpOrderContentResultList33.add(cpOrderContentResult332018);

        CPOrderContentResult cpOrderContentResult332019 = new CPOrderContentResult();
        cpOrderContentResult332019.setOrderName("西");
        cpOrderContentResult332019.setFullName(otherName);
        cpOrderContentResult332019.setOrderState(cpbjscResult.getdata2019());
        cpOrderContentResult332019.setOrderId("2019");
        cpOrderContentResultList33.add(cpOrderContentResult332019);

        CPOrderContentResult cpOrderContentResult1332020 = new CPOrderContentResult();
        cpOrderContentResult1332020.setOrderName("北");
        cpOrderContentResult1332020.setFullName(otherName);
        cpOrderContentResult1332020.setOrderState(cpbjscResult.getdata2020());
        cpOrderContentResult1332020.setOrderId("2020");
        cpOrderContentResultList33.add(cpOrderContentResult1332020);

        CPOrderContentListResult cpOrderContentListResult34 = new CPOrderContentListResult();
        cpOrderContentListResult34.setOrderContentListName("");
        cpOrderContentListResult34.setShowType("ZI");
        cpOrderContentListResult34.setShowNumber(3);

        List<CPOrderContentResult> cpOrderContentResultList34 = new ArrayList<>();
        CPOrderContentResult cpOrderContentResult332021 = new CPOrderContentResult();
        cpOrderContentResult332021.setOrderName("中");
        cpOrderContentResult332021.setFullName(otherName);
        cpOrderContentResult332021.setOrderState(cpbjscResult.getdata2021());
        cpOrderContentResult332021.setOrderId("2021");
        cpOrderContentResultList34.add(cpOrderContentResult332021);

        CPOrderContentResult cpOrderContentResult332022 = new CPOrderContentResult();
        cpOrderContentResult332022.setOrderName("发");
        cpOrderContentResult332022.setFullName(otherName);
        cpOrderContentResult332022.setOrderState(cpbjscResult.getdata2022());
        cpOrderContentResult332022.setOrderId("2022");
        cpOrderContentResultList34.add(cpOrderContentResult332022);

        CPOrderContentResult cpOrderContentResult332023 = new CPOrderContentResult();
        cpOrderContentResult332023.setOrderName("白");
        cpOrderContentResult332023.setFullName(otherName);
        cpOrderContentResult332023.setOrderState(cpbjscResult.getdata2023());
        cpOrderContentResult332023.setOrderId("2023");
        cpOrderContentResultList34.add(cpOrderContentResult332023);

        cpOrderContentListResult31.setData(cpOrderContentResultList31);
        cpOrderContentListResult32.setData(cpOrderContentResultList32);
        cpOrderContentListResult33.setData(cpOrderContentResultList33);
        cpOrderContentListResult34.setData(cpOrderContentResultList34);
        CPOrderContentListResult31.add(cpOrderContentListResult31);
        CPOrderContentListResult31.add(cpOrderContentListResult32);
        CPOrderContentListResult31.add(cpOrderContentListResult33);
        CPOrderContentListResult31.add(cpOrderContentListResult34);

        allResult.setData(CPOrderContentListResult31);
        allResultList.add(allResult);

    }

    private void JSKS(CPJSKSResult cpbjscResult){
        for(int k = 0; k < 10; ++k){
            CPOrderAllResult allResult = new CPOrderAllResult();
            switch (k){
                case 0:
                    allResult.setEventChecked(true);
                    allResult.setOrderAllName("和值");
                    List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    cpOrderContentListResult.setOrderContentListName("和值");
                    cpOrderContentListResult.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                    cpOrderContentResult3.setOrderName("3点");
                    cpOrderContentResult3.setFullName("和值");
                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata1611112());
                    cpOrderContentResult3.setOrderId("1611-112");
                    cpOrderContentResultList.add(cpOrderContentResult3);

                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                    cpOrderContentResult4.setOrderName("4点");
                    cpOrderContentResult4.setFullName("和值");
                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata151516());
                    cpOrderContentResult4.setOrderId("1515-16");
                    cpOrderContentResultList.add(cpOrderContentResult4);

                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                    cpOrderContentResult5.setOrderName("5点");
                    cpOrderContentResult5.setFullName("和值");
                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata151617());
                    cpOrderContentResult5.setOrderId("1516-17");
                    cpOrderContentResultList.add(cpOrderContentResult5);

                    CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                    cpOrderContentResult6.setOrderName("6点");
                    cpOrderContentResult6.setFullName("和值");
                    cpOrderContentResult6.setOrderState(cpbjscResult.getdata151718());
                    cpOrderContentResult6.setOrderId("1517-18");
                    cpOrderContentResultList.add(cpOrderContentResult6);

                    CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                    cpOrderContentResult7.setOrderName("7点");
                    cpOrderContentResult7.setFullName("和值");
                    cpOrderContentResult7.setOrderState(cpbjscResult.getdata151819());
                    cpOrderContentResult7.setOrderId("1518-19");
                    cpOrderContentResultList.add(cpOrderContentResult7);

                    CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                    cpOrderContentResult8.setOrderName("8点");
                    cpOrderContentResult8.setFullName("和值");
                    cpOrderContentResult8.setOrderState(cpbjscResult.getdata151920());
                    cpOrderContentResult8.setOrderId("1519-20");
                    cpOrderContentResultList.add(cpOrderContentResult8);


                    CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                    cpOrderContentResult9.setOrderName("9点");
                    cpOrderContentResult9.setFullName("和值");
                    cpOrderContentResult9.setOrderState(cpbjscResult.getdata152021());
                    cpOrderContentResult9.setOrderId("1520-21");
                    cpOrderContentResultList.add(cpOrderContentResult9);

                    CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                    cpOrderContentResult10.setOrderName("10点");
                    cpOrderContentResult10.setFullName("和值");
                    cpOrderContentResult10.setOrderState(cpbjscResult.getdata152122());
                    cpOrderContentResult10.setOrderId("1521-22");
                    cpOrderContentResultList.add(cpOrderContentResult10);

                    CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                    cpOrderContentResult11.setOrderName("11点");
                    cpOrderContentResult11.setFullName("和值");
                    cpOrderContentResult11.setOrderState(cpbjscResult.getdata152223());
                    cpOrderContentResult11.setOrderId("1522-23");
                    cpOrderContentResultList.add(cpOrderContentResult11);

                    CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                    cpOrderContentResult12.setOrderName("12点");
                    cpOrderContentResult12.setFullName("和值");
                    cpOrderContentResult12.setOrderState(cpbjscResult.getdata152324());
                    cpOrderContentResult12.setOrderId("1523-24");
                    cpOrderContentResultList.add(cpOrderContentResult12);

                    CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                    cpOrderContentResult13.setOrderName("13点");
                    cpOrderContentResult13.setFullName("和值");
                    cpOrderContentResult13.setOrderState(cpbjscResult.getdata152425());
                    cpOrderContentResult13.setOrderId("1524-25");
                    cpOrderContentResultList.add(cpOrderContentResult13);

                    
                    CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                    cpOrderContentResult14.setOrderName("14点");
                    cpOrderContentResult14.setFullName("和值");
                    cpOrderContentResult14.setOrderState(cpbjscResult.getdata152526());
                    cpOrderContentResult14.setOrderId("1525-26");
                    cpOrderContentResultList.add(cpOrderContentResult14);

                    CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                    cpOrderContentResult15.setOrderName("15点");
                    cpOrderContentResult15.setFullName("和值");
                    cpOrderContentResult15.setOrderState(cpbjscResult.getdata152627());
                    cpOrderContentResult15.setOrderId("1526-27");
                    cpOrderContentResultList.add(cpOrderContentResult15);

                    CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                    cpOrderContentResult16.setOrderName("16点");
                    cpOrderContentResult16.setFullName("和值");
                    cpOrderContentResult16.setOrderState(cpbjscResult.getdata152728());
                    cpOrderContentResult16.setOrderId("1527-28");
                    cpOrderContentResultList.add(cpOrderContentResult16);

                    CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                    cpOrderContentResult17.setOrderName("17点");
                    cpOrderContentResult17.setFullName("和值");
                    cpOrderContentResult17.setOrderState(cpbjscResult.getdata152829());
                    cpOrderContentResult17.setOrderId("1528-29");
                    cpOrderContentResultList.add(cpOrderContentResult17);

                    CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                    cpOrderContentResult18.setOrderName("18点");
                    cpOrderContentResult18.setFullName("和值");
                    cpOrderContentResult18.setOrderState(cpbjscResult.getdata1612113());
                    cpOrderContentResult18.setOrderId("1612-113");
                    cpOrderContentResultList.add(cpOrderContentResult18);


                    cpOrderContentListResult.setData(cpOrderContentResultList);
                    CPOrderContentListResult .add(cpOrderContentListResult);

                    allResult.setData(CPOrderContentListResult);

                    allResultList.add(allResult);
                    break;
                case 1:
                    allResult.setOrderAllName("大小单双");
                    List<CPOrderContentListResult> CPOrderContentListResult2 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                    cpOrderContentListResult2.setOrderContentListName("大小单双");
                    cpOrderContentListResult2.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                    cpOrderContentResult21.setOrderName("大");
                    cpOrderContentResult21.setFullName("");
                    cpOrderContentResult21.setOrderState(cpbjscResult.getdata15067());
                    cpOrderContentResult21.setOrderId("1506-7");
                    cpOrderContentResultList2.add(cpOrderContentResult21);

                    CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                    cpOrderContentResult22.setOrderName("小");
                    cpOrderContentResult22.setFullName("");
                    cpOrderContentResult22.setOrderState(cpbjscResult.getdata15078());
                    cpOrderContentResult22.setOrderId("1507-8");
                    cpOrderContentResultList2.add(cpOrderContentResult22);

                    CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                    cpOrderContentResult23.setOrderName("单");
                    cpOrderContentResult23.setFullName("");
                    cpOrderContentResult23.setOrderState(cpbjscResult.getdata155657());
                    cpOrderContentResult23.setOrderId("1556-57");
                    cpOrderContentResultList2.add(cpOrderContentResult23);

                    CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                    cpOrderContentResult24.setOrderName("双");
                    cpOrderContentResult24.setFullName("");
                    cpOrderContentResult24.setOrderState(cpbjscResult.getdata155758());
                    cpOrderContentResult24.setOrderId("1557-58");
                    cpOrderContentResultList2.add(cpOrderContentResult24);

                    cpOrderContentListResult2.setData(cpOrderContentResultList2);

                    CPOrderContentListResult2.add(cpOrderContentListResult2);
                    allResult.setData(CPOrderContentListResult2);
                    allResultList.add(allResult);
                    break;
                case 2:
                    allResult.setOrderAllName("通选");
                    List<CPOrderContentListResult> CPOrderContentListResult3 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                    cpOrderContentListResult3.setOrderContentListName("通选");
                    cpOrderContentListResult3.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                    cpOrderContentResult31.setOrderName("豹子");
                    cpOrderContentResult31.setFullName("");
                    cpOrderContentResult31.setOrderState(cpbjscResult.getdata15089());
                    cpOrderContentResult31.setOrderId("1508-9");
                    cpOrderContentResultList3.add(cpOrderContentResult31);

                    CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                    cpOrderContentResult32.setOrderName("顺子");
                    cpOrderContentResult32.setFullName("");
                    cpOrderContentResult32.setOrderState(cpbjscResult.getdata155859());
                    cpOrderContentResult32.setOrderId("1558-59");
                    cpOrderContentResultList3.add(cpOrderContentResult32);

                    CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                    cpOrderContentResult33.setOrderName("对子");
                    cpOrderContentResult33.setFullName("");
                    cpOrderContentResult33.setOrderState(cpbjscResult.getdata155960());
                    cpOrderContentResult33.setOrderId("1559-60");
                    cpOrderContentResultList3.add(cpOrderContentResult33);

                    CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                    cpOrderContentResult34.setOrderName("三不同");
                    cpOrderContentResult34.setFullName("");
                    cpOrderContentResult34.setOrderState(cpbjscResult.getdata156061());
                    cpOrderContentResult34.setOrderId("1560-61");
                    cpOrderContentResultList3.add(cpOrderContentResult34);

                    cpOrderContentListResult3.setData(cpOrderContentResultList3);

                    CPOrderContentListResult3.add(cpOrderContentListResult3);
                    allResult.setData(CPOrderContentListResult3);
                    allResultList.add(allResult);
                    break;
                case 3:

                    allResult.setOrderAllName("三同号");
                    List<CPOrderContentListResult> CPOrderContentListResult4 = new ArrayList<CPOrderContentListResult>();
                    
                    CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                    cpOrderContentListResult4.setOrderContentListName("三同号");
                    cpOrderContentListResult4.setShowNumber(2);
                    cpOrderContentListResult4.setShowType("DANIEL");
                    
                    List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                    cpOrderContentResult41.setOrderName("1_1_1");
                    cpOrderContentResult41.setFullName("三同号");
                    cpOrderContentResult41.setOrderState(cpbjscResult.getdata150910());
                    cpOrderContentResult41.setOrderId("1509-10");
                    cpOrderContentResultList4.add(cpOrderContentResult41);

                    CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                    cpOrderContentResult42.setOrderName("2_2_2");
                    cpOrderContentResult42.setFullName("三同号");
                    cpOrderContentResult42.setOrderState(cpbjscResult.getdata151011());
                    cpOrderContentResult42.setOrderId("1510-11");
                    cpOrderContentResultList4.add(cpOrderContentResult42);

                    CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                    cpOrderContentResult43.setOrderName("3_3_3");
                    cpOrderContentResult43.setFullName("三同号");
                    cpOrderContentResult43.setOrderState(cpbjscResult.getdata151112());
                    cpOrderContentResult43.setOrderId("1511-12");
                    cpOrderContentResultList4.add(cpOrderContentResult43);

                    CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                    cpOrderContentResult44.setOrderName("4_4_4");
                    cpOrderContentResult44.setFullName("三同号");
                    cpOrderContentResult44.setOrderState(cpbjscResult.getdata151213());
                    cpOrderContentResult44.setOrderId("1512-13");
                    cpOrderContentResultList4.add(cpOrderContentResult44);

                    CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                    cpOrderContentResult45.setOrderName("5_5_5");
                    cpOrderContentResult45.setFullName("三同号");
                    cpOrderContentResult45.setOrderState(cpbjscResult.getdata151314());
                    cpOrderContentResult45.setOrderId("1513-14");
                    cpOrderContentResultList4.add(cpOrderContentResult45);

                    CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                    cpOrderContentResult46.setOrderName("6_6_6");
                    cpOrderContentResult46.setFullName("三同号");
                    cpOrderContentResult46.setOrderState(cpbjscResult.getdata151415());
                    cpOrderContentResult46.setOrderId("1514-15");
                    cpOrderContentResultList4.add(cpOrderContentResult46);

                    cpOrderContentListResult4.setData(cpOrderContentResultList4);

                    CPOrderContentListResult4.add(cpOrderContentListResult4);
                    allResult.setData(CPOrderContentListResult4);
                    allResultList.add(allResult);
                    break;
                case 4:

                    allResult.setOrderAllName("三不同");
                    List<CPOrderContentListResult> CPOrderContentListResult5 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                    cpOrderContentListResult5.setOrderContentListName("三不同");
                    cpOrderContentListResult5.setShowNumber(2);
                    cpOrderContentListResult5.setShowType("DANIEL");

                    List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult123 = new CPOrderContentResult();
                    cpOrderContentResult123.setOrderName("1_2_3");
                    cpOrderContentResult123.setFullName("三不同");
                    cpOrderContentResult123.setOrderState(cpbjscResult.getdata156162());
                    cpOrderContentResult123.setOrderId("1561-62");
                    cpOrderContentResultList5.add(cpOrderContentResult123);

                    CPOrderContentResult cpOrderContentResult124 = new CPOrderContentResult();
                    cpOrderContentResult124.setOrderName("1_2_4");
                    cpOrderContentResult124.setFullName("三不同");
                    cpOrderContentResult124.setOrderState(cpbjscResult.getdata156263());
                    cpOrderContentResult124.setOrderId("1562-63");
                    cpOrderContentResultList5.add(cpOrderContentResult124);

                    CPOrderContentResult cpOrderContentResult125 = new CPOrderContentResult();
                    cpOrderContentResult125.setOrderName("1_2_5");
                    cpOrderContentResult125.setFullName("三不同");
                    cpOrderContentResult125.setOrderState(cpbjscResult.getdata156364());
                    cpOrderContentResult125.setOrderId("1563-64");
                    cpOrderContentResultList5.add(cpOrderContentResult125);

                    CPOrderContentResult cpOrderContentResult126 = new CPOrderContentResult();
                    cpOrderContentResult126.setOrderName("1_2_6");
                    cpOrderContentResult126.setFullName("三不同");
                    cpOrderContentResult126.setOrderState(cpbjscResult.getdata156465());
                    cpOrderContentResult126.setOrderId("1564-65");
                    cpOrderContentResultList5.add(cpOrderContentResult126);

                    CPOrderContentResult cpOrderContentResult134 = new CPOrderContentResult();
                    cpOrderContentResult134.setOrderName("1_3_4");
                    cpOrderContentResult134.setFullName("三不同");
                    cpOrderContentResult134.setOrderState(cpbjscResult.getdata156566());
                    cpOrderContentResult134.setOrderId("1565-66");
                    cpOrderContentResultList5.add(cpOrderContentResult134);

                    CPOrderContentResult cpOrderContentResult135 = new CPOrderContentResult();
                    cpOrderContentResult135.setOrderName("1_3_5");
                    cpOrderContentResult135.setFullName("三不同");
                    cpOrderContentResult135.setOrderState(cpbjscResult.getdata156667());
                    cpOrderContentResult135.setOrderId("1566-67");
                    cpOrderContentResultList5.add(cpOrderContentResult135);

                    CPOrderContentResult cpOrderContentResult136 = new CPOrderContentResult();
                    cpOrderContentResult136.setOrderName("1_3_6");
                    cpOrderContentResult136.setFullName("三不同");
                    cpOrderContentResult136.setOrderState(cpbjscResult.getdata156768());
                    cpOrderContentResult136.setOrderId("1567-68");
                    cpOrderContentResultList5.add(cpOrderContentResult136);

                    CPOrderContentResult cpOrderContentResult145 = new CPOrderContentResult();
                    cpOrderContentResult145.setOrderName("1_4_5");
                    cpOrderContentResult145.setFullName("三不同");
                    cpOrderContentResult145.setOrderState(cpbjscResult.getdata156869());
                    cpOrderContentResult145.setOrderId("1568-69");
                    cpOrderContentResultList5.add(cpOrderContentResult145);

                    CPOrderContentResult cpOrderContentResult146 = new CPOrderContentResult();
                    cpOrderContentResult146.setOrderName("1_4_6");
                    cpOrderContentResult146.setFullName("三不同");
                    cpOrderContentResult146.setOrderState(cpbjscResult.getdata156970());
                    cpOrderContentResult146.setOrderId("1569-70");
                    cpOrderContentResultList5.add(cpOrderContentResult146);

                    CPOrderContentResult cpOrderContentResult156 = new CPOrderContentResult();
                    cpOrderContentResult156.setOrderName("1_5_6");
                    cpOrderContentResult156.setFullName("三不同");
                    cpOrderContentResult156.setOrderState(cpbjscResult.getdata157071());
                    cpOrderContentResult156.setOrderId("1570-71");
                    cpOrderContentResultList5.add(cpOrderContentResult156);

                    CPOrderContentResult cpOrderContentResult234 = new CPOrderContentResult();
                    cpOrderContentResult234.setOrderName("2_3_4");
                    cpOrderContentResult234.setFullName("三不同");
                    cpOrderContentResult234.setOrderState(cpbjscResult.getdata157172());
                    cpOrderContentResult234.setOrderId("1571-72");
                    cpOrderContentResultList5.add(cpOrderContentResult234);

                    CPOrderContentResult cpOrderContentResult235 = new CPOrderContentResult();
                    cpOrderContentResult235.setOrderName("2_3_5");
                    cpOrderContentResult235.setFullName("三不同");
                    cpOrderContentResult235.setOrderState(cpbjscResult.getdata157273());
                    cpOrderContentResult235.setOrderId("1572-73");
                    cpOrderContentResultList5.add(cpOrderContentResult235);

                    CPOrderContentResult cpOrderContentResult236 = new CPOrderContentResult();
                    cpOrderContentResult236.setOrderName("2_3_6");
                    cpOrderContentResult236.setFullName("三不同");
                    cpOrderContentResult236.setOrderState(cpbjscResult.getdata157374());
                    cpOrderContentResult236.setOrderId("1573-74");
                    cpOrderContentResultList5.add(cpOrderContentResult236);

                    CPOrderContentResult cpOrderContentResult245 = new CPOrderContentResult();
                    cpOrderContentResult245.setOrderName("2_4_5");
                    cpOrderContentResult245.setFullName("三不同");
                    cpOrderContentResult245.setOrderState(cpbjscResult.getdata157475());
                    cpOrderContentResult245.setOrderId("1574-75");
                    cpOrderContentResultList5.add(cpOrderContentResult245);

                    CPOrderContentResult cpOrderContentResult246 = new CPOrderContentResult();
                    cpOrderContentResult246.setOrderName("2_4_6");
                    cpOrderContentResult246.setFullName("三不同");
                    cpOrderContentResult246.setOrderState(cpbjscResult.getdata157576());
                    cpOrderContentResult246.setOrderId("1575-76");
                    cpOrderContentResultList5.add(cpOrderContentResult246);

                    CPOrderContentResult cpOrderContentResult256 = new CPOrderContentResult();
                    cpOrderContentResult256.setOrderName("2_5_6");
                    cpOrderContentResult256.setFullName("三不同");
                    cpOrderContentResult256.setOrderState(cpbjscResult.getdata157677());
                    cpOrderContentResult256.setOrderId("1576-77");
                    cpOrderContentResultList5.add(cpOrderContentResult256);

                    CPOrderContentResult cpOrderContentResult345 = new CPOrderContentResult();
                    cpOrderContentResult345.setOrderName("3_4_5");
                    cpOrderContentResult345.setFullName("三不同");
                    cpOrderContentResult345.setOrderState(cpbjscResult.getdata157778());
                    cpOrderContentResult345.setOrderId("157778");
                    cpOrderContentResultList5.add(cpOrderContentResult345);

                    CPOrderContentResult cpOrderContentResult346 = new CPOrderContentResult();
                    cpOrderContentResult346.setOrderName("3_4_6");
                    cpOrderContentResult346.setFullName("三不同");
                    cpOrderContentResult346.setOrderState(cpbjscResult.getdata157879());
                    cpOrderContentResult346.setOrderId("1578-79");
                    cpOrderContentResultList5.add(cpOrderContentResult346);

                    CPOrderContentResult cpOrderContentResult356 = new CPOrderContentResult();
                    cpOrderContentResult356.setOrderName("3_5_6");
                    cpOrderContentResult356.setFullName("三不同");
                    cpOrderContentResult356.setOrderState(cpbjscResult.getdata157980());
                    cpOrderContentResult356.setOrderId("1579-80");
                    cpOrderContentResultList5.add(cpOrderContentResult356);

                    CPOrderContentResult cpOrderContentResult456 = new CPOrderContentResult();
                    cpOrderContentResult456.setOrderName("4_5_6");
                    cpOrderContentResult456.setFullName("三不同");
                    cpOrderContentResult456.setOrderState(cpbjscResult.getdata158081());
                    cpOrderContentResult456.setOrderId("1580-81");
                    cpOrderContentResultList5.add(cpOrderContentResult456);

                    cpOrderContentListResult5.setData(cpOrderContentResultList5);

                    CPOrderContentListResult5.add(cpOrderContentListResult5);
                    allResult.setData(CPOrderContentListResult5);
                    allResultList.add(allResult);

                    break;
                case 5:

                    allResult.setOrderAllName("二同号复选");

                    List<CPOrderContentListResult> CPOrderContentListResult6 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult6 = new CPOrderContentListResult();
                    cpOrderContentListResult6.setOrderContentListName("二同号复选");
                    cpOrderContentListResult6.setShowNumber(2);
                    cpOrderContentListResult6.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult111 = new CPOrderContentResult();
                    cpOrderContentResult111.setOrderName("1_1");
                    cpOrderContentResult111.setFullName("二同号复选");
                    cpOrderContentResult111.setOrderState(cpbjscResult.getdata154445());
                    cpOrderContentResult111.setOrderId("1544-45");
                    cpOrderContentResultList6.add(cpOrderContentResult111);

                    CPOrderContentResult cpOrderContentResult222 = new CPOrderContentResult();
                    cpOrderContentResult222.setOrderName("2_2");
                    cpOrderContentResult222.setFullName("二同号复选");
                    cpOrderContentResult222.setOrderState(cpbjscResult.getdata154546());
                    cpOrderContentResult222.setOrderId("1545-46");
                    cpOrderContentResultList6.add(cpOrderContentResult222);

                    CPOrderContentResult cpOrderContentResult333 = new CPOrderContentResult();
                    cpOrderContentResult333.setOrderName("3_3");
                    cpOrderContentResult333.setFullName("二同号复选");
                    cpOrderContentResult333.setOrderState(cpbjscResult.getdata154647());
                    cpOrderContentResult333.setOrderId("1546-47");
                    cpOrderContentResultList6.add(cpOrderContentResult333);

                    CPOrderContentResult cpOrderContentResult444 = new CPOrderContentResult();
                    cpOrderContentResult444.setOrderName("4_4");
                    cpOrderContentResult444.setFullName("二同号复选");
                    cpOrderContentResult444.setOrderState(cpbjscResult.getdata154748());
                    cpOrderContentResult444.setOrderId("1547-48");
                    cpOrderContentResultList6.add(cpOrderContentResult444);

                    CPOrderContentResult cpOrderContentResult555 = new CPOrderContentResult();
                    cpOrderContentResult555.setOrderName("5_5");
                    cpOrderContentResult555.setFullName("二同号复选");
                    cpOrderContentResult555.setOrderState(cpbjscResult.getdata154849());
                    cpOrderContentResult555.setOrderId("1548-49");
                    cpOrderContentResultList6.add(cpOrderContentResult555);

                    CPOrderContentResult cpOrderContentResult666 = new CPOrderContentResult();
                    cpOrderContentResult666.setOrderName("6_6");
                    cpOrderContentResult666.setFullName("二同号复选");
                    cpOrderContentResult666.setOrderState(cpbjscResult.getdata154950());
                    cpOrderContentResult666.setOrderId("1549-50");
                    cpOrderContentResultList6.add(cpOrderContentResult666);

                    cpOrderContentListResult6.setData(cpOrderContentResultList6);

                    CPOrderContentListResult6.add(cpOrderContentListResult6);
                    allResult.setData(CPOrderContentListResult6);
                    allResultList.add(allResult);

                    break;
                case 6:

                    allResult.setOrderAllName("二同号单选");

                    List<CPOrderContentListResult> CPOrderContentListResult7 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult7 = new CPOrderContentListResult();
                    cpOrderContentListResult7.setOrderContentListName("二同号单选");
                    cpOrderContentListResult7.setShowNumber(2);
                    cpOrderContentListResult7.setShowType("DANIEL");

                    List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult112 = new CPOrderContentResult();
                    cpOrderContentResult112.setOrderName("1_1_2");
                    cpOrderContentResult112.setFullName("二同号单选");
                    cpOrderContentResult112.setOrderState(cpbjscResult.getdata158182());
                    cpOrderContentResult112.setOrderId("1581-82");
                    cpOrderContentResultList7.add(cpOrderContentResult112);

                    CPOrderContentResult cpOrderContentResult113 = new CPOrderContentResult();
                    cpOrderContentResult113.setOrderName("1_1_3");
                    cpOrderContentResult113.setFullName("二同号单选");
                    cpOrderContentResult113.setOrderState(cpbjscResult.getdata158283());
                    cpOrderContentResult113.setOrderId("1582-83");
                    cpOrderContentResultList7.add(cpOrderContentResult113);

                    CPOrderContentResult cpOrderContentResult114 = new CPOrderContentResult();
                    cpOrderContentResult114.setOrderName("1_1_4");
                    cpOrderContentResult114.setFullName("二同号单选");
                    cpOrderContentResult114.setOrderState(cpbjscResult.getdata158384());
                    cpOrderContentResult114.setOrderId("1583-84");
                    cpOrderContentResultList7.add(cpOrderContentResult114);

                    CPOrderContentResult cpOrderContentResult115 = new CPOrderContentResult();
                    cpOrderContentResult115.setOrderName("1_1_5");
                    cpOrderContentResult115.setFullName("二同号单选");
                    cpOrderContentResult115.setOrderState(cpbjscResult.getdata158485());
                    cpOrderContentResult115.setOrderId("1584-85");
                    cpOrderContentResultList7.add(cpOrderContentResult115);

                    CPOrderContentResult cpOrderContentResult116 = new CPOrderContentResult();
                    cpOrderContentResult116.setOrderName("1_1_6");
                    cpOrderContentResult116.setFullName("二同号单选");
                    cpOrderContentResult116.setOrderState(cpbjscResult.getdata158586());
                    cpOrderContentResult116.setOrderId("1585-86");
                    cpOrderContentResultList7.add(cpOrderContentResult116);

                    CPOrderContentResult cpOrderContentResult221 = new CPOrderContentResult();
                    cpOrderContentResult221.setOrderName("2_2_1");
                    cpOrderContentResult221.setFullName("二同号单选");
                    cpOrderContentResult221.setOrderState(cpbjscResult.getdata158687());
                    cpOrderContentResult221.setOrderId("1586-87");
                    cpOrderContentResultList7.add(cpOrderContentResult221);

                    CPOrderContentResult cpOrderContentResult223 = new CPOrderContentResult();
                    cpOrderContentResult223.setOrderName("2_2_3");
                    cpOrderContentResult223.setFullName("二同号单选");
                    cpOrderContentResult223.setOrderState(cpbjscResult.getdata158788());
                    cpOrderContentResult223.setOrderId("1587-88");
                    cpOrderContentResultList7.add(cpOrderContentResult223);

                    CPOrderContentResult cpOrderContentResult224 = new CPOrderContentResult();
                    cpOrderContentResult224.setOrderName("2_2_4");
                    cpOrderContentResult224.setFullName("二同号单选");
                    cpOrderContentResult224.setOrderState(cpbjscResult.getdata158889());
                    cpOrderContentResult224.setOrderId("1588-89");
                    cpOrderContentResultList7.add(cpOrderContentResult224);

                    CPOrderContentResult cpOrderContentResult225 = new CPOrderContentResult();
                    cpOrderContentResult225.setOrderName("2_2_5");
                    cpOrderContentResult225.setFullName("二同号单选");
                    cpOrderContentResult225.setOrderState(cpbjscResult.getdata158990());
                    cpOrderContentResult225.setOrderId("1589-90");
                    cpOrderContentResultList7.add(cpOrderContentResult225);

                    CPOrderContentResult cpOrderContentResult226 = new CPOrderContentResult();
                    cpOrderContentResult226.setOrderName("2_2_6");
                    cpOrderContentResult226.setFullName("二同号单选");
                    cpOrderContentResult226.setOrderState(cpbjscResult.getdata159091());
                    cpOrderContentResult226.setOrderId("1590-91");
                    cpOrderContentResultList7.add(cpOrderContentResult226);

                    CPOrderContentResult cpOrderContentResult331 = new CPOrderContentResult();
                    cpOrderContentResult331.setOrderName("3_3_1");
                    cpOrderContentResult331.setFullName("二同号单选");
                    cpOrderContentResult331.setOrderState(cpbjscResult.getdata159192());
                    cpOrderContentResult331.setOrderId("1591-92");
                    cpOrderContentResultList7.add(cpOrderContentResult331);

                    CPOrderContentResult cpOrderContentResult332 = new CPOrderContentResult();
                    cpOrderContentResult332.setOrderName("3_3_2");
                    cpOrderContentResult332.setFullName("二同号单选");
                    cpOrderContentResult332.setOrderState(cpbjscResult.getdata159293());
                    cpOrderContentResult332.setOrderId("1592-93");
                    cpOrderContentResultList7.add(cpOrderContentResult332);

                    CPOrderContentResult cpOrderContentResult334 = new CPOrderContentResult();
                    cpOrderContentResult334.setOrderName("3_3_4");
                    cpOrderContentResult334.setFullName("二同号单选");
                    cpOrderContentResult334.setOrderState(cpbjscResult.getdata159394());
                    cpOrderContentResult334.setOrderId("1593-94");
                    cpOrderContentResultList7.add(cpOrderContentResult334);

                    CPOrderContentResult cpOrderContentResult335 = new CPOrderContentResult();
                    cpOrderContentResult335.setOrderName("3_3_5");
                    cpOrderContentResult335.setFullName("二同号单选");
                    cpOrderContentResult335.setOrderState(cpbjscResult.getdata159495());
                    cpOrderContentResult335.setOrderId("1594-95");
                    cpOrderContentResultList7.add(cpOrderContentResult335);

                    CPOrderContentResult cpOrderContentResult336 = new CPOrderContentResult();
                    cpOrderContentResult336.setOrderName("3_3_6");
                    cpOrderContentResult336.setFullName("二同号单选");
                    cpOrderContentResult336.setOrderState(cpbjscResult.getdata159596());
                    cpOrderContentResult336.setOrderId("1595-96");
                    cpOrderContentResultList7.add(cpOrderContentResult336);

                    CPOrderContentResult cpOrderContentResult441 = new CPOrderContentResult();
                    cpOrderContentResult441.setOrderName("4_4_1");
                    cpOrderContentResult441.setFullName("二同号单选");
                    cpOrderContentResult441.setOrderState(cpbjscResult.getdata159697());
                    cpOrderContentResult441.setOrderId("1596-97");
                    cpOrderContentResultList7.add(cpOrderContentResult441);

                    CPOrderContentResult cpOrderContentResult442 = new CPOrderContentResult();
                    cpOrderContentResult442.setOrderName("4_4_2");
                    cpOrderContentResult442.setFullName("二同号单选");
                    cpOrderContentResult442.setOrderState(cpbjscResult.getdata159798());
                    cpOrderContentResult442.setOrderId("1597-98");
                    cpOrderContentResultList7.add(cpOrderContentResult442);

                    CPOrderContentResult cpOrderContentResult443 = new CPOrderContentResult();
                    cpOrderContentResult443.setOrderName("4_4_3");
                    cpOrderContentResult443.setFullName("二同号单选");
                    cpOrderContentResult443.setOrderState(cpbjscResult.getdata159899());
                    cpOrderContentResult443.setOrderId("1598-99");
                    cpOrderContentResultList7.add(cpOrderContentResult443);

                    CPOrderContentResult cpOrderContentResult445 = new CPOrderContentResult();
                    cpOrderContentResult445.setOrderName("4_4_5");
                    cpOrderContentResult445.setFullName("二同号单选");
                    cpOrderContentResult445.setOrderState(cpbjscResult.getdata1599100());
                    cpOrderContentResult445.setOrderId("1599-100");
                    cpOrderContentResultList7.add(cpOrderContentResult445);

                    CPOrderContentResult cpOrderContentResult446 = new CPOrderContentResult();
                    cpOrderContentResult446.setOrderName("4_4_6");
                    cpOrderContentResult446.setFullName("二同号单选");
                    cpOrderContentResult446.setOrderState(cpbjscResult.getdata1600101());
                    cpOrderContentResult446.setOrderId("1600-101");
                    cpOrderContentResultList7.add(cpOrderContentResult446);

                    CPOrderContentResult cpOrderContentResult551 = new CPOrderContentResult();
                    cpOrderContentResult551.setOrderName("5_5_1");
                    cpOrderContentResult551.setFullName("二同号单选");
                    cpOrderContentResult551.setOrderState(cpbjscResult.getdata1601102());
                    cpOrderContentResult551.setOrderId("1601-102");
                    cpOrderContentResultList7.add(cpOrderContentResult551);

                    CPOrderContentResult cpOrderContentResult552 = new CPOrderContentResult();
                    cpOrderContentResult552.setOrderName("5_5_2");
                    cpOrderContentResult552.setFullName("二同号单选");
                    cpOrderContentResult552.setOrderState(cpbjscResult.getdata1602103());
                    cpOrderContentResult552.setOrderId("1602-103");
                    cpOrderContentResultList7.add(cpOrderContentResult552);

                    CPOrderContentResult cpOrderContentResult553 = new CPOrderContentResult();
                    cpOrderContentResult553.setOrderName("5_5_3");
                    cpOrderContentResult553.setFullName("二同号单选");
                    cpOrderContentResult553.setOrderState(cpbjscResult.getdata1603104());
                    cpOrderContentResult553.setOrderId("1603-104");
                    cpOrderContentResultList7.add(cpOrderContentResult553);

                    CPOrderContentResult cpOrderContentResult554 = new CPOrderContentResult();
                    cpOrderContentResult554.setOrderName("5_5_4");
                    cpOrderContentResult554.setFullName("二同号单选");
                    cpOrderContentResult554.setOrderState(cpbjscResult.getdata1604105());
                    cpOrderContentResult554.setOrderId("1604-105");
                    cpOrderContentResultList7.add(cpOrderContentResult554);

                    CPOrderContentResult cpOrderContentResult556 = new CPOrderContentResult();
                    cpOrderContentResult556.setOrderName("5_5_6");
                    cpOrderContentResult556.setFullName("二同号单选");
                    cpOrderContentResult556.setOrderState(cpbjscResult.getdata1605106());
                    cpOrderContentResult556.setOrderId("1605-106");
                    cpOrderContentResultList7.add(cpOrderContentResult556);


                    CPOrderContentResult cpOrderContentResult661 = new CPOrderContentResult();
                    cpOrderContentResult661.setOrderName("6_6_1");
                    cpOrderContentResult661.setFullName("二同号单选");
                    cpOrderContentResult661.setOrderState(cpbjscResult.getdata1606107());
                    cpOrderContentResult661.setOrderId("1606-107");
                    cpOrderContentResultList7.add(cpOrderContentResult661);

                    CPOrderContentResult cpOrderContentResult662 = new CPOrderContentResult();
                    cpOrderContentResult662.setOrderName("6_6_2");
                    cpOrderContentResult662.setFullName("二同号单选");
                    cpOrderContentResult662.setOrderState(cpbjscResult.getdata1607108());
                    cpOrderContentResult662.setOrderId("1607-108");
                    cpOrderContentResultList7.add(cpOrderContentResult662);

                    CPOrderContentResult cpOrderContentResult663 = new CPOrderContentResult();
                    cpOrderContentResult663.setOrderName("6_6_3");
                    cpOrderContentResult663.setFullName("二同号单选");
                    cpOrderContentResult663.setOrderState(cpbjscResult.getdata1608109());
                    cpOrderContentResult663.setOrderId("1608-109");
                    cpOrderContentResultList7.add(cpOrderContentResult663);

                    CPOrderContentResult cpOrderContentResult664 = new CPOrderContentResult();
                    cpOrderContentResult664.setOrderName("6_6_4");
                    cpOrderContentResult664.setFullName("二同号单选");
                    cpOrderContentResult664.setOrderState(cpbjscResult.getdata1609110());
                    cpOrderContentResult664.setOrderId("1609-110");
                    cpOrderContentResultList7.add(cpOrderContentResult664);

                    CPOrderContentResult cpOrderContentResult665 = new CPOrderContentResult();
                    cpOrderContentResult665.setOrderName("6_6_5");
                    cpOrderContentResult665.setFullName("二同号单选");
                    cpOrderContentResult665.setOrderState(cpbjscResult.getdata1610111());
                    cpOrderContentResult665.setOrderId("1610-111");
                    cpOrderContentResultList7.add(cpOrderContentResult665);

                    cpOrderContentListResult7.setData(cpOrderContentResultList7);

                    CPOrderContentListResult7.add(cpOrderContentListResult7);
                    allResult.setData(CPOrderContentListResult7);
                    allResultList.add(allResult);

                    break;

                case 7:


                    allResult.setOrderAllName("二不同号");
                    List<CPOrderContentListResult> CPOrderContentListResult8 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult8 = new CPOrderContentListResult();
                    cpOrderContentListResult8.setOrderContentListName("二不同号");
                    cpOrderContentListResult8.setShowNumber(2);
                    cpOrderContentListResult8.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult812 = new CPOrderContentResult();
                    cpOrderContentResult812.setOrderName("1_2");
                    cpOrderContentResult812.setFullName("二不同号");
                    cpOrderContentResult812.setOrderState(cpbjscResult.getdata152930());
                    cpOrderContentResult812.setOrderId("1529-30");
                    cpOrderContentResultList8.add(cpOrderContentResult812);

                    CPOrderContentResult cpOrderContentResult813 = new CPOrderContentResult();
                    cpOrderContentResult813.setOrderName("1_3");
                    cpOrderContentResult813.setFullName("二不同号");
                    cpOrderContentResult813.setOrderState(cpbjscResult.getdata153031());
                    cpOrderContentResult813.setOrderId("1530-31");
                    cpOrderContentResultList8.add(cpOrderContentResult813);

                    CPOrderContentResult cpOrderContentResult814 = new CPOrderContentResult();
                    cpOrderContentResult814.setOrderName("1_4");
                    cpOrderContentResult814.setFullName("二不同号");
                    cpOrderContentResult814.setOrderState(cpbjscResult.getdata153132());
                    cpOrderContentResult814.setOrderId("1531-32");
                    cpOrderContentResultList8.add(cpOrderContentResult814);

                    CPOrderContentResult cpOrderContentResult815 = new CPOrderContentResult();
                    cpOrderContentResult815.setOrderName("1_5");
                    cpOrderContentResult815.setFullName("二不同号");
                    cpOrderContentResult815.setOrderState(cpbjscResult.getdata153233());
                    cpOrderContentResult815.setOrderId("1532-33");
                    cpOrderContentResultList8.add(cpOrderContentResult815);

                    CPOrderContentResult cpOrderContentResult816 = new CPOrderContentResult();
                    cpOrderContentResult816.setOrderName("1_6");
                    cpOrderContentResult816.setFullName("二不同号");
                    cpOrderContentResult816.setOrderState(cpbjscResult.getdata153334());
                    cpOrderContentResult816.setOrderId("1533-34");
                    cpOrderContentResultList8.add(cpOrderContentResult816);

                    CPOrderContentResult cpOrderContentResult823 = new CPOrderContentResult();
                    cpOrderContentResult823.setOrderName("2_3");
                    cpOrderContentResult823.setFullName("二不同号");
                    cpOrderContentResult823.setOrderState(cpbjscResult.getdata153435());
                    cpOrderContentResult823.setOrderId("1534-35");
                    cpOrderContentResultList8.add(cpOrderContentResult823);

                    CPOrderContentResult cpOrderContentResult824 = new CPOrderContentResult();
                    cpOrderContentResult824.setOrderName("2_4");
                    cpOrderContentResult824.setFullName("二不同号");
                    cpOrderContentResult824.setOrderState(cpbjscResult.getdata153536());
                    cpOrderContentResult824.setOrderId("1535-36");
                    cpOrderContentResultList8.add(cpOrderContentResult824);

                    CPOrderContentResult cpOrderContentResult825 = new CPOrderContentResult();
                    cpOrderContentResult825.setOrderName("2_5");
                    cpOrderContentResult825.setFullName("二不同号");
                    cpOrderContentResult825.setOrderState(cpbjscResult.getdata153637());
                    cpOrderContentResult825.setOrderId("1536-37");
                    cpOrderContentResultList8.add(cpOrderContentResult825);

                    CPOrderContentResult cpOrderContentResult826 = new CPOrderContentResult();
                    cpOrderContentResult826.setOrderName("2_5");
                    cpOrderContentResult826.setFullName("二不同号");
                    cpOrderContentResult826.setOrderState(cpbjscResult.getdata153738());
                    cpOrderContentResult826.setOrderId("1537-38");
                    cpOrderContentResultList8.add(cpOrderContentResult826);

                    CPOrderContentResult cpOrderContentResult834 = new CPOrderContentResult();
                    cpOrderContentResult834.setOrderName("3_4");
                    cpOrderContentResult834.setFullName("二不同号");
                    cpOrderContentResult834.setOrderState(cpbjscResult.getdata153839());
                    cpOrderContentResult834.setOrderId("1538-39");
                    cpOrderContentResultList8.add(cpOrderContentResult834);

                    CPOrderContentResult cpOrderContentResult835 = new CPOrderContentResult();
                    cpOrderContentResult835.setOrderName("3_5");
                    cpOrderContentResult835.setFullName("二不同号");
                    cpOrderContentResult835.setOrderState(cpbjscResult.getdata153940());
                    cpOrderContentResult835.setOrderId("1539-40");
                    cpOrderContentResultList8.add(cpOrderContentResult835);


                    CPOrderContentResult cpOrderContentResult836 = new CPOrderContentResult();
                    cpOrderContentResult836.setOrderName("3_6");
                    cpOrderContentResult836.setFullName("二不同号");
                    cpOrderContentResult836.setOrderState(cpbjscResult.getdata154041());
                    cpOrderContentResult836.setOrderId("1540-41");
                    cpOrderContentResultList8.add(cpOrderContentResult836);

                    CPOrderContentResult cpOrderContentResult845 = new CPOrderContentResult();
                    cpOrderContentResult845.setOrderName("4_5");
                    cpOrderContentResult845.setFullName("二不同号");
                    cpOrderContentResult845.setOrderState(cpbjscResult.getdata154142());
                    cpOrderContentResult845.setOrderId("1541-42");
                    cpOrderContentResultList8.add(cpOrderContentResult845);

                    CPOrderContentResult cpOrderContentResult846 = new CPOrderContentResult();
                    cpOrderContentResult846.setOrderName("4_6");
                    cpOrderContentResult846.setFullName("二不同号");
                    cpOrderContentResult846.setOrderState(cpbjscResult.getdata154243());
                    cpOrderContentResult846.setOrderId("1542-43");
                    cpOrderContentResultList8.add(cpOrderContentResult846);

                    CPOrderContentResult cpOrderContentResult856 = new CPOrderContentResult();
                    cpOrderContentResult856.setOrderName("5_6");
                    cpOrderContentResult856.setFullName("二不同号");
                    cpOrderContentResult856.setOrderState(cpbjscResult.getdata154344());
                    cpOrderContentResult856.setOrderId("1543-44");
                    cpOrderContentResultList8.add(cpOrderContentResult856);

                    cpOrderContentListResult8.setData(cpOrderContentResultList8);

                    CPOrderContentListResult8.add(cpOrderContentListResult8);
                    allResult.setData(CPOrderContentListResult8);
                    allResultList.add(allResult);


                    break;
                case 8:

                    allResult.setOrderAllName("猜必出");


                    List<CPOrderContentListResult> CPOrderContentListResult9 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult9 = new CPOrderContentListResult();
                    cpOrderContentListResult9.setOrderContentListName("猜必出");
                    cpOrderContentListResult9.setShowNumber(2);
                    cpOrderContentListResult9.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList9 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult91 = new CPOrderContentResult();
                    cpOrderContentResult91.setOrderName("1_");
                    cpOrderContentResult91.setFullName("猜必出");
                    cpOrderContentResult91.setOrderState(cpbjscResult.getdata15001());
                    cpOrderContentResult91.setOrderId("1500-1");
                    cpOrderContentResultList9.add(cpOrderContentResult91);

                    CPOrderContentResult cpOrderContentResult92 = new CPOrderContentResult();
                    cpOrderContentResult92.setOrderName("2_");
                    cpOrderContentResult92.setFullName("猜必出");
                    cpOrderContentResult92.setOrderState(cpbjscResult.getdata15012());
                    cpOrderContentResult92.setOrderId("1501-2");
                    cpOrderContentResultList9.add(cpOrderContentResult92);

                    CPOrderContentResult cpOrderContentResult93 = new CPOrderContentResult();
                    cpOrderContentResult93.setOrderName("3_");
                    cpOrderContentResult93.setFullName("猜必出");
                    cpOrderContentResult93.setOrderState(cpbjscResult.getdata15023());
                    cpOrderContentResult93.setOrderId("1502-3");
                    cpOrderContentResultList9.add(cpOrderContentResult93);

                    CPOrderContentResult cpOrderContentResult94 = new CPOrderContentResult();
                    cpOrderContentResult94.setOrderName("4_");
                    cpOrderContentResult94.setFullName("猜必出");
                    cpOrderContentResult94.setOrderState(cpbjscResult.getdata15034());
                    cpOrderContentResult94.setOrderId("1503-4");
                    cpOrderContentResultList9.add(cpOrderContentResult94);

                    CPOrderContentResult cpOrderContentResult95 = new CPOrderContentResult();
                    cpOrderContentResult95.setOrderName("5_");
                    cpOrderContentResult95.setFullName("猜必出");
                    cpOrderContentResult95.setOrderState(cpbjscResult.getdata15045());
                    cpOrderContentResult95.setOrderId("1504-5");
                    cpOrderContentResultList9.add(cpOrderContentResult95);

                    CPOrderContentResult cpOrderContentResult96 = new CPOrderContentResult();
                    cpOrderContentResult96.setOrderName("6_");
                    cpOrderContentResult96.setFullName("猜必出");
                    cpOrderContentResult96.setOrderState(cpbjscResult.getdata15056());
                    cpOrderContentResult96.setOrderId("1505-6");
                    cpOrderContentResultList9.add(cpOrderContentResult96);

                    cpOrderContentListResult9.setData(cpOrderContentResultList9);

                    CPOrderContentListResult9.add(cpOrderContentListResult9);
                    allResult.setData(CPOrderContentListResult9);
                    allResultList.add(allResult);

                    break;
                case 9:


                    allResult.setOrderAllName("猜必不出");

                    List<CPOrderContentListResult> CPOrderContentListResult10 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult10 = new CPOrderContentListResult();
                    cpOrderContentListResult10.setOrderContentListName("猜必不出");
                    cpOrderContentListResult10.setShowNumber(2);
                    cpOrderContentListResult10.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList10 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult101 = new CPOrderContentResult();
                    cpOrderContentResult101.setOrderName("1_");
                    cpOrderContentResult101.setFullName("猜必不出");
                    cpOrderContentResult101.setOrderState(cpbjscResult.getdata155051());
                    cpOrderContentResult101.setOrderId("1550-51");
                    cpOrderContentResultList10.add(cpOrderContentResult101);

                    CPOrderContentResult cpOrderContentResult102 = new CPOrderContentResult();
                    cpOrderContentResult102.setOrderName("2_");
                    cpOrderContentResult102.setFullName("猜必不出");
                    cpOrderContentResult102.setOrderState(cpbjscResult.getdata155152());
                    cpOrderContentResult102.setOrderId("1551-52");
                    cpOrderContentResultList10.add(cpOrderContentResult102);

                    CPOrderContentResult cpOrderContentResult103 = new CPOrderContentResult();
                    cpOrderContentResult103.setOrderName("3_");
                    cpOrderContentResult103.setFullName("猜必不出");
                    cpOrderContentResult103.setOrderState(cpbjscResult.getdata155253());
                    cpOrderContentResult103.setOrderId("1552-53");
                    cpOrderContentResultList10.add(cpOrderContentResult103);

                    CPOrderContentResult cpOrderContentResult104 = new CPOrderContentResult();
                    cpOrderContentResult104.setOrderName("4_");
                    cpOrderContentResult104.setFullName("猜必不出");
                    cpOrderContentResult104.setOrderState(cpbjscResult.getdata155354());
                    cpOrderContentResult104.setOrderId("1553-54");
                    cpOrderContentResultList10.add(cpOrderContentResult104);

                    CPOrderContentResult cpOrderContentResult105 = new CPOrderContentResult();
                    cpOrderContentResult105.setOrderName("5_");
                    cpOrderContentResult105.setFullName("猜必不出");
                    cpOrderContentResult105.setOrderState(cpbjscResult.getdata155455());
                    cpOrderContentResult105.setOrderId("1554-55");
                    cpOrderContentResultList10.add(cpOrderContentResult105);

                    CPOrderContentResult cpOrderContentResult106 = new CPOrderContentResult();
                    cpOrderContentResult106.setOrderName("6_");
                    cpOrderContentResult106.setFullName("猜必不出");
                    cpOrderContentResult106.setOrderState(cpbjscResult.getdata155556());
                    cpOrderContentResult106.setOrderId("1555-56");
                    cpOrderContentResultList10.add(cpOrderContentResult106);

                    cpOrderContentListResult10.setData(cpOrderContentResultList10);

                    CPOrderContentListResult10.add(cpOrderContentListResult10);
                    allResult.setData(CPOrderContentListResult10);
                    allResultList.add(allResult);

                    break;



            }

        }



    }

    private void JSKS2(CPJSK2Result cpbjscResult){
        for(int k = 0; k < 10; ++k){
            CPOrderAllResult allResult = new CPOrderAllResult();
            switch (k){
                case 0:
                    allResult.setEventChecked(true);
                    allResult.setOrderAllName("和值");
                    List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    cpOrderContentListResult.setOrderContentListName("和值");
                    cpOrderContentListResult.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                    cpOrderContentResult3.setOrderName("3点");
                    cpOrderContentResult3.setFullName("和值");
                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata2611112());
                    cpOrderContentResult3.setOrderId("2611-112");
                    cpOrderContentResultList.add(cpOrderContentResult3);

                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                    cpOrderContentResult4.setOrderName("4点");
                    cpOrderContentResult4.setFullName("和值");
                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata251516());
                    cpOrderContentResult4.setOrderId("2515-16");
                    cpOrderContentResultList.add(cpOrderContentResult4);

                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                    cpOrderContentResult5.setOrderName("5点");
                    cpOrderContentResult5.setFullName("和值");
                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata251617());
                    cpOrderContentResult5.setOrderId("2516-17");
                    cpOrderContentResultList.add(cpOrderContentResult5);

                    CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                    cpOrderContentResult6.setOrderName("6点");
                    cpOrderContentResult6.setFullName("和值");
                    cpOrderContentResult6.setOrderState(cpbjscResult.getdata251718());
                    cpOrderContentResult6.setOrderId("2517-18");
                    cpOrderContentResultList.add(cpOrderContentResult6);

                    CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                    cpOrderContentResult7.setOrderName("7点");
                    cpOrderContentResult7.setFullName("和值");
                    cpOrderContentResult7.setOrderState(cpbjscResult.getdata251819());
                    cpOrderContentResult7.setOrderId("2518-19");
                    cpOrderContentResultList.add(cpOrderContentResult7);

                    CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                    cpOrderContentResult8.setOrderName("8点");
                    cpOrderContentResult8.setFullName("和值");
                    cpOrderContentResult8.setOrderState(cpbjscResult.getdata251920());
                    cpOrderContentResult8.setOrderId("2519-20");
                    cpOrderContentResultList.add(cpOrderContentResult8);


                    CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                    cpOrderContentResult9.setOrderName("9点");
                    cpOrderContentResult9.setFullName("和值");
                    cpOrderContentResult9.setOrderState(cpbjscResult.getdata252021());
                    cpOrderContentResult9.setOrderId("2520-21");
                    cpOrderContentResultList.add(cpOrderContentResult9);

                    CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                    cpOrderContentResult10.setOrderName("10点");
                    cpOrderContentResult10.setFullName("和值");
                    cpOrderContentResult10.setOrderState(cpbjscResult.getdata252122());
                    cpOrderContentResult10.setOrderId("2521-22");
                    cpOrderContentResultList.add(cpOrderContentResult10);

                    CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                    cpOrderContentResult11.setOrderName("11点");
                    cpOrderContentResult11.setFullName("和值");
                    cpOrderContentResult11.setOrderState(cpbjscResult.getdata252223());
                    cpOrderContentResult11.setOrderId("2522-23");
                    cpOrderContentResultList.add(cpOrderContentResult11);

                    CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                    cpOrderContentResult12.setOrderName("12点");
                    cpOrderContentResult12.setFullName("和值");
                    cpOrderContentResult12.setOrderState(cpbjscResult.getdata252324());
                    cpOrderContentResult12.setOrderId("2523-24");
                    cpOrderContentResultList.add(cpOrderContentResult12);

                    CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                    cpOrderContentResult13.setOrderName("13点");
                    cpOrderContentResult13.setFullName("和值");
                    cpOrderContentResult13.setOrderState(cpbjscResult.getdata252425());
                    cpOrderContentResult13.setOrderId("2524-25");
                    cpOrderContentResultList.add(cpOrderContentResult13);


                    CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                    cpOrderContentResult14.setOrderName("14点");
                    cpOrderContentResult14.setFullName("和值");
                    cpOrderContentResult14.setOrderState(cpbjscResult.getdata252526());
                    cpOrderContentResult14.setOrderId("2525-26");
                    cpOrderContentResultList.add(cpOrderContentResult14);

                    CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                    cpOrderContentResult15.setOrderName("15点");
                    cpOrderContentResult15.setFullName("和值");
                    cpOrderContentResult15.setOrderState(cpbjscResult.getdata252627());
                    cpOrderContentResult15.setOrderId("2526-27");
                    cpOrderContentResultList.add(cpOrderContentResult15);

                    CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                    cpOrderContentResult16.setOrderName("16点");
                    cpOrderContentResult16.setFullName("和值");
                    cpOrderContentResult16.setOrderState(cpbjscResult.getdata252728());
                    cpOrderContentResult16.setOrderId("2527-28");
                    cpOrderContentResultList.add(cpOrderContentResult16);

                    CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                    cpOrderContentResult17.setOrderName("17点");
                    cpOrderContentResult17.setFullName("和值");
                    cpOrderContentResult17.setOrderState(cpbjscResult.getdata252829());
                    cpOrderContentResult17.setOrderId("2528-29");
                    cpOrderContentResultList.add(cpOrderContentResult17);

                    CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                    cpOrderContentResult18.setOrderName("18点");
                    cpOrderContentResult18.setFullName("和值");
                    cpOrderContentResult18.setOrderState(cpbjscResult.getdata2612113());
                    cpOrderContentResult18.setOrderId("2612-113");
                    cpOrderContentResultList.add(cpOrderContentResult18);


                    cpOrderContentListResult.setData(cpOrderContentResultList);
                    CPOrderContentListResult .add(cpOrderContentListResult);

                    allResult.setData(CPOrderContentListResult);

                    allResultList.add(allResult);
                    break;
                case 1:
                    allResult.setOrderAllName("大小单双");
                    List<CPOrderContentListResult> CPOrderContentListResult2 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                    cpOrderContentListResult2.setOrderContentListName("大小单双");
                    cpOrderContentListResult2.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                    cpOrderContentResult21.setOrderName("大");
                    cpOrderContentResult21.setFullName("");
                    cpOrderContentResult21.setOrderState(cpbjscResult.getdata25067());
                    cpOrderContentResult21.setOrderId("2506-7");
                    cpOrderContentResultList2.add(cpOrderContentResult21);

                    CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                    cpOrderContentResult22.setOrderName("小");
                    cpOrderContentResult22.setFullName("");
                    cpOrderContentResult22.setOrderState(cpbjscResult.getdata25078());
                    cpOrderContentResult22.setOrderId("2507-8");
                    cpOrderContentResultList2.add(cpOrderContentResult22);

                    CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                    cpOrderContentResult23.setOrderName("单");
                    cpOrderContentResult23.setFullName("");
                    cpOrderContentResult23.setOrderState(cpbjscResult.getdata255657());
                    cpOrderContentResult23.setOrderId("2556-57");
                    cpOrderContentResultList2.add(cpOrderContentResult23);

                    CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                    cpOrderContentResult24.setOrderName("双");
                    cpOrderContentResult24.setFullName("");
                    cpOrderContentResult24.setOrderState(cpbjscResult.getdata255758());
                    cpOrderContentResult24.setOrderId("2557-58");
                    cpOrderContentResultList2.add(cpOrderContentResult24);

                    cpOrderContentListResult2.setData(cpOrderContentResultList2);

                    CPOrderContentListResult2.add(cpOrderContentListResult2);
                    allResult.setData(CPOrderContentListResult2);
                    allResultList.add(allResult);
                    break;
                case 2:
                    allResult.setOrderAllName("通选");
                    List<CPOrderContentListResult> CPOrderContentListResult3 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                    cpOrderContentListResult3.setOrderContentListName("通选");
                    cpOrderContentListResult3.setShowNumber(2);

                    List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                    cpOrderContentResult31.setOrderName("豹子");
                    cpOrderContentResult31.setFullName("");
                    cpOrderContentResult31.setOrderState(cpbjscResult.getdata25089());
                    cpOrderContentResult31.setOrderId("2508-9");
                    cpOrderContentResultList3.add(cpOrderContentResult31);

                    CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                    cpOrderContentResult32.setOrderName("顺子");
                    cpOrderContentResult32.setFullName("");
                    cpOrderContentResult32.setOrderState(cpbjscResult.getdata255859());
                    cpOrderContentResult32.setOrderId("2558-59");
                    cpOrderContentResultList3.add(cpOrderContentResult32);

                    CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                    cpOrderContentResult33.setOrderName("对子");
                    cpOrderContentResult33.setFullName("");
                    cpOrderContentResult33.setOrderState(cpbjscResult.getdata255960());
                    cpOrderContentResult33.setOrderId("2559-60");
                    cpOrderContentResultList3.add(cpOrderContentResult33);

                    CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                    cpOrderContentResult34.setOrderName("三不同");
                    cpOrderContentResult34.setFullName("");
                    cpOrderContentResult34.setOrderState(cpbjscResult.getdata256061());
                    cpOrderContentResult34.setOrderId("2560-61");
                    cpOrderContentResultList3.add(cpOrderContentResult34);

                    cpOrderContentListResult3.setData(cpOrderContentResultList3);

                    CPOrderContentListResult3.add(cpOrderContentListResult3);
                    allResult.setData(CPOrderContentListResult3);
                    allResultList.add(allResult);
                    break;
                case 3:

                    allResult.setOrderAllName("三同号");
                    List<CPOrderContentListResult> CPOrderContentListResult4 = new ArrayList<CPOrderContentListResult>();

                    CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                    cpOrderContentListResult4.setOrderContentListName("三同号");
                    cpOrderContentListResult4.setShowNumber(2);
                    cpOrderContentListResult4.setShowType("DANIEL");

                    List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                    cpOrderContentResult41.setOrderName("1_1_1");
                    cpOrderContentResult41.setFullName("三同号");
                    cpOrderContentResult41.setOrderState(cpbjscResult.getdata250910());
                    cpOrderContentResult41.setOrderId("2509-10");
                    cpOrderContentResultList4.add(cpOrderContentResult41);

                    CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                    cpOrderContentResult42.setOrderName("2_2_2");
                    cpOrderContentResult42.setFullName("三同号");
                    cpOrderContentResult42.setOrderState(cpbjscResult.getdata251011());
                    cpOrderContentResult42.setOrderId("2510-11");
                    cpOrderContentResultList4.add(cpOrderContentResult42);

                    CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                    cpOrderContentResult43.setOrderName("3_3_3");
                    cpOrderContentResult43.setFullName("三同号");
                    cpOrderContentResult43.setOrderState(cpbjscResult.getdata251112());
                    cpOrderContentResult43.setOrderId("2511-12");
                    cpOrderContentResultList4.add(cpOrderContentResult43);

                    CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                    cpOrderContentResult44.setOrderName("4_4_4");
                    cpOrderContentResult44.setFullName("三同号");
                    cpOrderContentResult44.setOrderState(cpbjscResult.getdata251213());
                    cpOrderContentResult44.setOrderId("2512-13");
                    cpOrderContentResultList4.add(cpOrderContentResult44);

                    CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                    cpOrderContentResult45.setOrderName("5_5_5");
                    cpOrderContentResult45.setFullName("三同号");
                    cpOrderContentResult45.setOrderState(cpbjscResult.getdata251314());
                    cpOrderContentResult45.setOrderId("2513-14");
                    cpOrderContentResultList4.add(cpOrderContentResult45);

                    CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                    cpOrderContentResult46.setOrderName("6_6_6");
                    cpOrderContentResult46.setFullName("三同号");
                    cpOrderContentResult46.setOrderState(cpbjscResult.getdata251415());
                    cpOrderContentResult46.setOrderId("2514-15");
                    cpOrderContentResultList4.add(cpOrderContentResult46);

                    cpOrderContentListResult4.setData(cpOrderContentResultList4);

                    CPOrderContentListResult4.add(cpOrderContentListResult4);
                    allResult.setData(CPOrderContentListResult4);
                    allResultList.add(allResult);
                    break;
                case 4:

                    allResult.setOrderAllName("三不同");
                    List<CPOrderContentListResult> CPOrderContentListResult5 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                    cpOrderContentListResult5.setOrderContentListName("三不同");
                    cpOrderContentListResult5.setShowNumber(2);
                    cpOrderContentListResult5.setShowType("DANIEL");

                    List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult123 = new CPOrderContentResult();
                    cpOrderContentResult123.setOrderName("1_2_3");
                    cpOrderContentResult123.setFullName("三不同");
                    cpOrderContentResult123.setOrderState(cpbjscResult.getdata256162());
                    cpOrderContentResult123.setOrderId("2561-62");
                    cpOrderContentResultList5.add(cpOrderContentResult123);

                    CPOrderContentResult cpOrderContentResult124 = new CPOrderContentResult();
                    cpOrderContentResult124.setOrderName("1_2_4");
                    cpOrderContentResult124.setFullName("三不同");
                    cpOrderContentResult124.setOrderState(cpbjscResult.getdata256263());
                    cpOrderContentResult124.setOrderId("2562-63");
                    cpOrderContentResultList5.add(cpOrderContentResult124);

                    CPOrderContentResult cpOrderContentResult125 = new CPOrderContentResult();
                    cpOrderContentResult125.setOrderName("1_2_5");
                    cpOrderContentResult125.setFullName("三不同");
                    cpOrderContentResult125.setOrderState(cpbjscResult.getdata256364());
                    cpOrderContentResult125.setOrderId("2563-64");
                    cpOrderContentResultList5.add(cpOrderContentResult125);

                    CPOrderContentResult cpOrderContentResult126 = new CPOrderContentResult();
                    cpOrderContentResult126.setOrderName("1_2_6");
                    cpOrderContentResult126.setFullName("三不同");
                    cpOrderContentResult126.setOrderState(cpbjscResult.getdata256465());
                    cpOrderContentResult126.setOrderId("2564-65");
                    cpOrderContentResultList5.add(cpOrderContentResult126);

                    CPOrderContentResult cpOrderContentResult134 = new CPOrderContentResult();
                    cpOrderContentResult134.setOrderName("1_3_4");
                    cpOrderContentResult134.setFullName("三不同");
                    cpOrderContentResult134.setOrderState(cpbjscResult.getdata256566());
                    cpOrderContentResult134.setOrderId("2565-66");
                    cpOrderContentResultList5.add(cpOrderContentResult134);

                    CPOrderContentResult cpOrderContentResult135 = new CPOrderContentResult();
                    cpOrderContentResult135.setOrderName("1_3_5");
                    cpOrderContentResult135.setFullName("三不同");
                    cpOrderContentResult135.setOrderState(cpbjscResult.getdata256667());
                    cpOrderContentResult135.setOrderId("2566-67");
                    cpOrderContentResultList5.add(cpOrderContentResult135);

                    CPOrderContentResult cpOrderContentResult136 = new CPOrderContentResult();
                    cpOrderContentResult136.setOrderName("1_3_6");
                    cpOrderContentResult136.setFullName("三不同");
                    cpOrderContentResult136.setOrderState(cpbjscResult.getdata256768());
                    cpOrderContentResult136.setOrderId("2567-68");
                    cpOrderContentResultList5.add(cpOrderContentResult136);

                    CPOrderContentResult cpOrderContentResult145 = new CPOrderContentResult();
                    cpOrderContentResult145.setOrderName("1_4_5");
                    cpOrderContentResult145.setFullName("三不同");
                    cpOrderContentResult145.setOrderState(cpbjscResult.getdata256869());
                    cpOrderContentResult145.setOrderId("2568-69");
                    cpOrderContentResultList5.add(cpOrderContentResult145);

                    CPOrderContentResult cpOrderContentResult146 = new CPOrderContentResult();
                    cpOrderContentResult146.setOrderName("1_4_6");
                    cpOrderContentResult146.setFullName("三不同");
                    cpOrderContentResult146.setOrderState(cpbjscResult.getdata256970());
                    cpOrderContentResult146.setOrderId("2569-70");
                    cpOrderContentResultList5.add(cpOrderContentResult146);

                    CPOrderContentResult cpOrderContentResult156 = new CPOrderContentResult();
                    cpOrderContentResult156.setOrderName("1_5_6");
                    cpOrderContentResult156.setFullName("三不同");
                    cpOrderContentResult156.setOrderState(cpbjscResult.getdata257071());
                    cpOrderContentResult156.setOrderId("2570-71");
                    cpOrderContentResultList5.add(cpOrderContentResult156);

                    CPOrderContentResult cpOrderContentResult234 = new CPOrderContentResult();
                    cpOrderContentResult234.setOrderName("2_3_4");
                    cpOrderContentResult234.setFullName("三不同");
                    cpOrderContentResult234.setOrderState(cpbjscResult.getdata257172());
                    cpOrderContentResult234.setOrderId("2571-72");
                    cpOrderContentResultList5.add(cpOrderContentResult234);

                    CPOrderContentResult cpOrderContentResult235 = new CPOrderContentResult();
                    cpOrderContentResult235.setOrderName("2_3_5");
                    cpOrderContentResult235.setFullName("三不同");
                    cpOrderContentResult235.setOrderState(cpbjscResult.getdata257273());
                    cpOrderContentResult235.setOrderId("2572-73");
                    cpOrderContentResultList5.add(cpOrderContentResult235);

                    CPOrderContentResult cpOrderContentResult236 = new CPOrderContentResult();
                    cpOrderContentResult236.setOrderName("2_3_6");
                    cpOrderContentResult236.setFullName("三不同");
                    cpOrderContentResult236.setOrderState(cpbjscResult.getdata257374());
                    cpOrderContentResult236.setOrderId("2573-74");
                    cpOrderContentResultList5.add(cpOrderContentResult236);

                    CPOrderContentResult cpOrderContentResult245 = new CPOrderContentResult();
                    cpOrderContentResult245.setOrderName("2_4_5");
                    cpOrderContentResult245.setFullName("三不同");
                    cpOrderContentResult245.setOrderState(cpbjscResult.getdata257475());
                    cpOrderContentResult245.setOrderId("2574-75");
                    cpOrderContentResultList5.add(cpOrderContentResult245);

                    CPOrderContentResult cpOrderContentResult246 = new CPOrderContentResult();
                    cpOrderContentResult246.setOrderName("2_4_6");
                    cpOrderContentResult246.setFullName("三不同");
                    cpOrderContentResult246.setOrderState(cpbjscResult.getdata257576());
                    cpOrderContentResult246.setOrderId("2575-76");
                    cpOrderContentResultList5.add(cpOrderContentResult246);

                    CPOrderContentResult cpOrderContentResult256 = new CPOrderContentResult();
                    cpOrderContentResult256.setOrderName("2_5_6");
                    cpOrderContentResult256.setFullName("三不同");
                    cpOrderContentResult256.setOrderState(cpbjscResult.getdata257677());
                    cpOrderContentResult256.setOrderId("2576-77");
                    cpOrderContentResultList5.add(cpOrderContentResult256);

                    CPOrderContentResult cpOrderContentResult345 = new CPOrderContentResult();
                    cpOrderContentResult345.setOrderName("3_4_5");
                    cpOrderContentResult345.setFullName("三不同");
                    cpOrderContentResult345.setOrderState(cpbjscResult.getdata257778());
                    cpOrderContentResult345.setOrderId("257778");
                    cpOrderContentResultList5.add(cpOrderContentResult345);

                    CPOrderContentResult cpOrderContentResult346 = new CPOrderContentResult();
                    cpOrderContentResult346.setOrderName("3_4_6");
                    cpOrderContentResult346.setFullName("三不同");
                    cpOrderContentResult346.setOrderState(cpbjscResult.getdata257879());
                    cpOrderContentResult346.setOrderId("2578-79");
                    cpOrderContentResultList5.add(cpOrderContentResult346);

                    CPOrderContentResult cpOrderContentResult356 = new CPOrderContentResult();
                    cpOrderContentResult356.setOrderName("3_5_6");
                    cpOrderContentResult356.setFullName("三不同");
                    cpOrderContentResult356.setOrderState(cpbjscResult.getdata257980());
                    cpOrderContentResult356.setOrderId("2579-80");
                    cpOrderContentResultList5.add(cpOrderContentResult356);

                    CPOrderContentResult cpOrderContentResult456 = new CPOrderContentResult();
                    cpOrderContentResult456.setOrderName("4_5_6");
                    cpOrderContentResult456.setFullName("三不同");
                    cpOrderContentResult456.setOrderState(cpbjscResult.getdata258081());
                    cpOrderContentResult456.setOrderId("2580-81");
                    cpOrderContentResultList5.add(cpOrderContentResult456);

                    cpOrderContentListResult5.setData(cpOrderContentResultList5);

                    CPOrderContentListResult5.add(cpOrderContentListResult5);
                    allResult.setData(CPOrderContentListResult5);
                    allResultList.add(allResult);

                    break;
                case 5:

                    allResult.setOrderAllName("二同号复选");

                    List<CPOrderContentListResult> CPOrderContentListResult6 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult6 = new CPOrderContentListResult();
                    cpOrderContentListResult6.setOrderContentListName("二同号复选");
                    cpOrderContentListResult6.setShowNumber(2);
                    cpOrderContentListResult6.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult111 = new CPOrderContentResult();
                    cpOrderContentResult111.setOrderName("1_1");
                    cpOrderContentResult111.setFullName("二同号复选");
                    cpOrderContentResult111.setOrderState(cpbjscResult.getdata254445());
                    cpOrderContentResult111.setOrderId("2544-45");
                    cpOrderContentResultList6.add(cpOrderContentResult111);

                    CPOrderContentResult cpOrderContentResult222 = new CPOrderContentResult();
                    cpOrderContentResult222.setOrderName("2_2");
                    cpOrderContentResult222.setFullName("二同号复选");
                    cpOrderContentResult222.setOrderState(cpbjscResult.getdata254546());
                    cpOrderContentResult222.setOrderId("2545-46");
                    cpOrderContentResultList6.add(cpOrderContentResult222);

                    CPOrderContentResult cpOrderContentResult333 = new CPOrderContentResult();
                    cpOrderContentResult333.setOrderName("3_3");
                    cpOrderContentResult333.setFullName("二同号复选");
                    cpOrderContentResult333.setOrderState(cpbjscResult.getdata254647());
                    cpOrderContentResult333.setOrderId("2546-47");
                    cpOrderContentResultList6.add(cpOrderContentResult333);

                    CPOrderContentResult cpOrderContentResult444 = new CPOrderContentResult();
                    cpOrderContentResult444.setOrderName("4_4");
                    cpOrderContentResult444.setFullName("二同号复选");
                    cpOrderContentResult444.setOrderState(cpbjscResult.getdata254748());
                    cpOrderContentResult444.setOrderId("2547-48");
                    cpOrderContentResultList6.add(cpOrderContentResult444);

                    CPOrderContentResult cpOrderContentResult555 = new CPOrderContentResult();
                    cpOrderContentResult555.setOrderName("5_5");
                    cpOrderContentResult555.setFullName("二同号复选");
                    cpOrderContentResult555.setOrderState(cpbjscResult.getdata254849());
                    cpOrderContentResult555.setOrderId("2548-49");
                    cpOrderContentResultList6.add(cpOrderContentResult555);

                    CPOrderContentResult cpOrderContentResult666 = new CPOrderContentResult();
                    cpOrderContentResult666.setOrderName("6_6");
                    cpOrderContentResult666.setFullName("二同号复选");
                    cpOrderContentResult666.setOrderState(cpbjscResult.getdata254950());
                    cpOrderContentResult666.setOrderId("2549-50");
                    cpOrderContentResultList6.add(cpOrderContentResult666);

                    cpOrderContentListResult6.setData(cpOrderContentResultList6);

                    CPOrderContentListResult6.add(cpOrderContentListResult6);
                    allResult.setData(CPOrderContentListResult6);
                    allResultList.add(allResult);

                    break;
                case 6:

                    allResult.setOrderAllName("二同号单选");

                    List<CPOrderContentListResult> CPOrderContentListResult7 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult7 = new CPOrderContentListResult();
                    cpOrderContentListResult7.setOrderContentListName("二同号单选");
                    cpOrderContentListResult7.setShowNumber(2);
                    cpOrderContentListResult7.setShowType("DANIEL");

                    List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult112 = new CPOrderContentResult();
                    cpOrderContentResult112.setOrderName("1_1_2");
                    cpOrderContentResult112.setFullName("二同号单选");
                    cpOrderContentResult112.setOrderState(cpbjscResult.getdata258182());
                    cpOrderContentResult112.setOrderId("2581-82");
                    cpOrderContentResultList7.add(cpOrderContentResult112);

                    CPOrderContentResult cpOrderContentResult113 = new CPOrderContentResult();
                    cpOrderContentResult113.setOrderName("1_1_3");
                    cpOrderContentResult113.setFullName("二同号单选");
                    cpOrderContentResult113.setOrderState(cpbjscResult.getdata258283());
                    cpOrderContentResult113.setOrderId("2582-83");
                    cpOrderContentResultList7.add(cpOrderContentResult113);

                    CPOrderContentResult cpOrderContentResult114 = new CPOrderContentResult();
                    cpOrderContentResult114.setOrderName("1_1_4");
                    cpOrderContentResult114.setFullName("二同号单选");
                    cpOrderContentResult114.setOrderState(cpbjscResult.getdata258384());
                    cpOrderContentResult114.setOrderId("2583-84");
                    cpOrderContentResultList7.add(cpOrderContentResult114);

                    CPOrderContentResult cpOrderContentResult115 = new CPOrderContentResult();
                    cpOrderContentResult115.setOrderName("1_1_5");
                    cpOrderContentResult115.setFullName("二同号单选");
                    cpOrderContentResult115.setOrderState(cpbjscResult.getdata258485());
                    cpOrderContentResult115.setOrderId("2584-85");
                    cpOrderContentResultList7.add(cpOrderContentResult115);

                    CPOrderContentResult cpOrderContentResult116 = new CPOrderContentResult();
                    cpOrderContentResult116.setOrderName("1_1_6");
                    cpOrderContentResult116.setFullName("二同号单选");
                    cpOrderContentResult116.setOrderState(cpbjscResult.getdata258586());
                    cpOrderContentResult116.setOrderId("2585-86");
                    cpOrderContentResultList7.add(cpOrderContentResult116);

                    CPOrderContentResult cpOrderContentResult221 = new CPOrderContentResult();
                    cpOrderContentResult221.setOrderName("2_2_1");
                    cpOrderContentResult221.setFullName("二同号单选");
                    cpOrderContentResult221.setOrderState(cpbjscResult.getdata258687());
                    cpOrderContentResult221.setOrderId("2586-87");
                    cpOrderContentResultList7.add(cpOrderContentResult221);

                    CPOrderContentResult cpOrderContentResult223 = new CPOrderContentResult();
                    cpOrderContentResult223.setOrderName("2_2_3");
                    cpOrderContentResult223.setFullName("二同号单选");
                    cpOrderContentResult223.setOrderState(cpbjscResult.getdata258788());
                    cpOrderContentResult223.setOrderId("2587-88");
                    cpOrderContentResultList7.add(cpOrderContentResult223);

                    CPOrderContentResult cpOrderContentResult224 = new CPOrderContentResult();
                    cpOrderContentResult224.setOrderName("2_2_4");
                    cpOrderContentResult224.setFullName("二同号单选");
                    cpOrderContentResult224.setOrderState(cpbjscResult.getdata258889());
                    cpOrderContentResult224.setOrderId("2588-89");
                    cpOrderContentResultList7.add(cpOrderContentResult224);

                    CPOrderContentResult cpOrderContentResult225 = new CPOrderContentResult();
                    cpOrderContentResult225.setOrderName("2_2_5");
                    cpOrderContentResult225.setFullName("二同号单选");
                    cpOrderContentResult225.setOrderState(cpbjscResult.getdata258990());
                    cpOrderContentResult225.setOrderId("2589-90");
                    cpOrderContentResultList7.add(cpOrderContentResult225);

                    CPOrderContentResult cpOrderContentResult226 = new CPOrderContentResult();
                    cpOrderContentResult226.setOrderName("2_2_6");
                    cpOrderContentResult226.setFullName("二同号单选");
                    cpOrderContentResult226.setOrderState(cpbjscResult.getdata259091());
                    cpOrderContentResult226.setOrderId("2590-91");
                    cpOrderContentResultList7.add(cpOrderContentResult226);

                    CPOrderContentResult cpOrderContentResult331 = new CPOrderContentResult();
                    cpOrderContentResult331.setOrderName("3_3_1");
                    cpOrderContentResult331.setFullName("二同号单选");
                    cpOrderContentResult331.setOrderState(cpbjscResult.getdata259192());
                    cpOrderContentResult331.setOrderId("2591-92");
                    cpOrderContentResultList7.add(cpOrderContentResult331);

                    CPOrderContentResult cpOrderContentResult332 = new CPOrderContentResult();
                    cpOrderContentResult332.setOrderName("3_3_2");
                    cpOrderContentResult332.setFullName("二同号单选");
                    cpOrderContentResult332.setOrderState(cpbjscResult.getdata259293());
                    cpOrderContentResult332.setOrderId("2592-93");
                    cpOrderContentResultList7.add(cpOrderContentResult332);

                    CPOrderContentResult cpOrderContentResult334 = new CPOrderContentResult();
                    cpOrderContentResult334.setOrderName("3_3_4");
                    cpOrderContentResult334.setFullName("二同号单选");
                    cpOrderContentResult334.setOrderState(cpbjscResult.getdata259394());
                    cpOrderContentResult334.setOrderId("2593-94");
                    cpOrderContentResultList7.add(cpOrderContentResult334);

                    CPOrderContentResult cpOrderContentResult335 = new CPOrderContentResult();
                    cpOrderContentResult335.setOrderName("3_3_5");
                    cpOrderContentResult335.setFullName("二同号单选");
                    cpOrderContentResult335.setOrderState(cpbjscResult.getdata259495());
                    cpOrderContentResult335.setOrderId("2594-95");
                    cpOrderContentResultList7.add(cpOrderContentResult335);

                    CPOrderContentResult cpOrderContentResult336 = new CPOrderContentResult();
                    cpOrderContentResult336.setOrderName("3_3_6");
                    cpOrderContentResult336.setFullName("二同号单选");
                    cpOrderContentResult336.setOrderState(cpbjscResult.getdata259596());
                    cpOrderContentResult336.setOrderId("2595-96");
                    cpOrderContentResultList7.add(cpOrderContentResult336);

                    CPOrderContentResult cpOrderContentResult441 = new CPOrderContentResult();
                    cpOrderContentResult441.setOrderName("4_4_1");
                    cpOrderContentResult441.setFullName("二同号单选");
                    cpOrderContentResult441.setOrderState(cpbjscResult.getdata259697());
                    cpOrderContentResult441.setOrderId("2596-97");
                    cpOrderContentResultList7.add(cpOrderContentResult441);

                    CPOrderContentResult cpOrderContentResult442 = new CPOrderContentResult();
                    cpOrderContentResult442.setOrderName("4_4_2");
                    cpOrderContentResult442.setFullName("二同号单选");
                    cpOrderContentResult442.setOrderState(cpbjscResult.getdata259798());
                    cpOrderContentResult442.setOrderId("2597-98");
                    cpOrderContentResultList7.add(cpOrderContentResult442);

                    CPOrderContentResult cpOrderContentResult443 = new CPOrderContentResult();
                    cpOrderContentResult443.setOrderName("4_4_3");
                    cpOrderContentResult443.setFullName("二同号单选");
                    cpOrderContentResult443.setOrderState(cpbjscResult.getdata259899());
                    cpOrderContentResult443.setOrderId("2598-99");
                    cpOrderContentResultList7.add(cpOrderContentResult443);

                    CPOrderContentResult cpOrderContentResult445 = new CPOrderContentResult();
                    cpOrderContentResult445.setOrderName("4_4_5");
                    cpOrderContentResult445.setFullName("二同号单选");
                    cpOrderContentResult445.setOrderState(cpbjscResult.getdata2599100());
                    cpOrderContentResult445.setOrderId("2599-100");
                    cpOrderContentResultList7.add(cpOrderContentResult445);

                    CPOrderContentResult cpOrderContentResult446 = new CPOrderContentResult();
                    cpOrderContentResult446.setOrderName("4_4_6");
                    cpOrderContentResult446.setFullName("二同号单选");
                    cpOrderContentResult446.setOrderState(cpbjscResult.getdata2600101());
                    cpOrderContentResult446.setOrderId("2600-101");
                    cpOrderContentResultList7.add(cpOrderContentResult446);

                    CPOrderContentResult cpOrderContentResult551 = new CPOrderContentResult();
                    cpOrderContentResult551.setOrderName("5_5_1");
                    cpOrderContentResult551.setFullName("二同号单选");
                    cpOrderContentResult551.setOrderState(cpbjscResult.getdata2601102());
                    cpOrderContentResult551.setOrderId("2601-102");
                    cpOrderContentResultList7.add(cpOrderContentResult551);

                    CPOrderContentResult cpOrderContentResult552 = new CPOrderContentResult();
                    cpOrderContentResult552.setOrderName("5_5_2");
                    cpOrderContentResult552.setFullName("二同号单选");
                    cpOrderContentResult552.setOrderState(cpbjscResult.getdata2602103());
                    cpOrderContentResult552.setOrderId("2602-103");
                    cpOrderContentResultList7.add(cpOrderContentResult552);

                    CPOrderContentResult cpOrderContentResult553 = new CPOrderContentResult();
                    cpOrderContentResult553.setOrderName("5_5_3");
                    cpOrderContentResult553.setFullName("二同号单选");
                    cpOrderContentResult553.setOrderState(cpbjscResult.getdata2603104());
                    cpOrderContentResult553.setOrderId("2603-104");
                    cpOrderContentResultList7.add(cpOrderContentResult553);

                    CPOrderContentResult cpOrderContentResult554 = new CPOrderContentResult();
                    cpOrderContentResult554.setOrderName("5_5_4");
                    cpOrderContentResult554.setFullName("二同号单选");
                    cpOrderContentResult554.setOrderState(cpbjscResult.getdata2604105());
                    cpOrderContentResult554.setOrderId("2604-105");
                    cpOrderContentResultList7.add(cpOrderContentResult554);

                    CPOrderContentResult cpOrderContentResult556 = new CPOrderContentResult();
                    cpOrderContentResult556.setOrderName("5_5_6");
                    cpOrderContentResult556.setFullName("二同号单选");
                    cpOrderContentResult556.setOrderState(cpbjscResult.getdata2605106());
                    cpOrderContentResult556.setOrderId("2605-106");
                    cpOrderContentResultList7.add(cpOrderContentResult556);


                    CPOrderContentResult cpOrderContentResult661 = new CPOrderContentResult();
                    cpOrderContentResult661.setOrderName("6_6_1");
                    cpOrderContentResult661.setFullName("二同号单选");
                    cpOrderContentResult661.setOrderState(cpbjscResult.getdata2606107());
                    cpOrderContentResult661.setOrderId("2606-107");
                    cpOrderContentResultList7.add(cpOrderContentResult661);

                    CPOrderContentResult cpOrderContentResult662 = new CPOrderContentResult();
                    cpOrderContentResult662.setOrderName("6_6_2");
                    cpOrderContentResult662.setFullName("二同号单选");
                    cpOrderContentResult662.setOrderState(cpbjscResult.getdata2607108());
                    cpOrderContentResult662.setOrderId("2607-108");
                    cpOrderContentResultList7.add(cpOrderContentResult662);

                    CPOrderContentResult cpOrderContentResult663 = new CPOrderContentResult();
                    cpOrderContentResult663.setOrderName("6_6_3");
                    cpOrderContentResult663.setFullName("二同号单选");
                    cpOrderContentResult663.setOrderState(cpbjscResult.getdata2608109());
                    cpOrderContentResult663.setOrderId("2608-109");
                    cpOrderContentResultList7.add(cpOrderContentResult663);

                    CPOrderContentResult cpOrderContentResult664 = new CPOrderContentResult();
                    cpOrderContentResult664.setOrderName("6_6_4");
                    cpOrderContentResult664.setFullName("二同号单选");
                    cpOrderContentResult664.setOrderState(cpbjscResult.getdata2609110());
                    cpOrderContentResult664.setOrderId("2609-110");
                    cpOrderContentResultList7.add(cpOrderContentResult664);

                    CPOrderContentResult cpOrderContentResult665 = new CPOrderContentResult();
                    cpOrderContentResult665.setOrderName("6_6_5");
                    cpOrderContentResult665.setFullName("二同号单选");
                    cpOrderContentResult665.setOrderState(cpbjscResult.getdata2610111());
                    cpOrderContentResult665.setOrderId("2610-111");
                    cpOrderContentResultList7.add(cpOrderContentResult665);

                    cpOrderContentListResult7.setData(cpOrderContentResultList7);

                    CPOrderContentListResult7.add(cpOrderContentListResult7);
                    allResult.setData(CPOrderContentListResult7);
                    allResultList.add(allResult);

                    break;

                case 7:


                    allResult.setOrderAllName("二不同号");
                    List<CPOrderContentListResult> CPOrderContentListResult8 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult8 = new CPOrderContentListResult();
                    cpOrderContentListResult8.setOrderContentListName("二不同号");
                    cpOrderContentListResult8.setShowNumber(2);
                    cpOrderContentListResult8.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult812 = new CPOrderContentResult();
                    cpOrderContentResult812.setOrderName("1_2");
                    cpOrderContentResult812.setFullName("二不同号");
                    cpOrderContentResult812.setOrderState(cpbjscResult.getdata252930());
                    cpOrderContentResult812.setOrderId("2529-30");
                    cpOrderContentResultList8.add(cpOrderContentResult812);

                    CPOrderContentResult cpOrderContentResult813 = new CPOrderContentResult();
                    cpOrderContentResult813.setOrderName("1_3");
                    cpOrderContentResult813.setFullName("二不同号");
                    cpOrderContentResult813.setOrderState(cpbjscResult.getdata253031());
                    cpOrderContentResult813.setOrderId("2530-31");
                    cpOrderContentResultList8.add(cpOrderContentResult813);

                    CPOrderContentResult cpOrderContentResult814 = new CPOrderContentResult();
                    cpOrderContentResult814.setOrderName("1_4");
                    cpOrderContentResult814.setFullName("二不同号");
                    cpOrderContentResult814.setOrderState(cpbjscResult.getdata253132());
                    cpOrderContentResult814.setOrderId("2531-32");
                    cpOrderContentResultList8.add(cpOrderContentResult814);

                    CPOrderContentResult cpOrderContentResult815 = new CPOrderContentResult();
                    cpOrderContentResult815.setOrderName("1_5");
                    cpOrderContentResult815.setFullName("二不同号");
                    cpOrderContentResult815.setOrderState(cpbjscResult.getdata253233());
                    cpOrderContentResult815.setOrderId("2532-33");
                    cpOrderContentResultList8.add(cpOrderContentResult815);

                    CPOrderContentResult cpOrderContentResult816 = new CPOrderContentResult();
                    cpOrderContentResult816.setOrderName("1_6");
                    cpOrderContentResult816.setFullName("二不同号");
                    cpOrderContentResult816.setOrderState(cpbjscResult.getdata253334());
                    cpOrderContentResult816.setOrderId("2533-34");
                    cpOrderContentResultList8.add(cpOrderContentResult816);

                    CPOrderContentResult cpOrderContentResult823 = new CPOrderContentResult();
                    cpOrderContentResult823.setOrderName("2_3");
                    cpOrderContentResult823.setFullName("二不同号");
                    cpOrderContentResult823.setOrderState(cpbjscResult.getdata253435());
                    cpOrderContentResult823.setOrderId("2534-35");
                    cpOrderContentResultList8.add(cpOrderContentResult823);

                    CPOrderContentResult cpOrderContentResult824 = new CPOrderContentResult();
                    cpOrderContentResult824.setOrderName("2_4");
                    cpOrderContentResult824.setFullName("二不同号");
                    cpOrderContentResult824.setOrderState(cpbjscResult.getdata253536());
                    cpOrderContentResult824.setOrderId("2535-36");
                    cpOrderContentResultList8.add(cpOrderContentResult824);

                    CPOrderContentResult cpOrderContentResult825 = new CPOrderContentResult();
                    cpOrderContentResult825.setOrderName("2_5");
                    cpOrderContentResult825.setFullName("二不同号");
                    cpOrderContentResult825.setOrderState(cpbjscResult.getdata253637());
                    cpOrderContentResult825.setOrderId("2536-37");
                    cpOrderContentResultList8.add(cpOrderContentResult825);

                    CPOrderContentResult cpOrderContentResult826 = new CPOrderContentResult();
                    cpOrderContentResult826.setOrderName("2_5");
                    cpOrderContentResult826.setFullName("二不同号");
                    cpOrderContentResult826.setOrderState(cpbjscResult.getdata253738());
                    cpOrderContentResult826.setOrderId("2537-38");
                    cpOrderContentResultList8.add(cpOrderContentResult826);

                    CPOrderContentResult cpOrderContentResult834 = new CPOrderContentResult();
                    cpOrderContentResult834.setOrderName("3_4");
                    cpOrderContentResult834.setFullName("二不同号");
                    cpOrderContentResult834.setOrderState(cpbjscResult.getdata253839());
                    cpOrderContentResult834.setOrderId("2538-39");
                    cpOrderContentResultList8.add(cpOrderContentResult834);

                    CPOrderContentResult cpOrderContentResult835 = new CPOrderContentResult();
                    cpOrderContentResult835.setOrderName("3_5");
                    cpOrderContentResult835.setFullName("二不同号");
                    cpOrderContentResult835.setOrderState(cpbjscResult.getdata253940());
                    cpOrderContentResult835.setOrderId("2539-40");
                    cpOrderContentResultList8.add(cpOrderContentResult835);


                    CPOrderContentResult cpOrderContentResult836 = new CPOrderContentResult();
                    cpOrderContentResult836.setOrderName("3_6");
                    cpOrderContentResult836.setFullName("二不同号");
                    cpOrderContentResult836.setOrderState(cpbjscResult.getdata254041());
                    cpOrderContentResult836.setOrderId("2540-41");
                    cpOrderContentResultList8.add(cpOrderContentResult836);

                    CPOrderContentResult cpOrderContentResult845 = new CPOrderContentResult();
                    cpOrderContentResult845.setOrderName("4_5");
                    cpOrderContentResult845.setFullName("二不同号");
                    cpOrderContentResult845.setOrderState(cpbjscResult.getdata254142());
                    cpOrderContentResult845.setOrderId("2541-42");
                    cpOrderContentResultList8.add(cpOrderContentResult845);

                    CPOrderContentResult cpOrderContentResult846 = new CPOrderContentResult();
                    cpOrderContentResult846.setOrderName("4_6");
                    cpOrderContentResult846.setFullName("二不同号");
                    cpOrderContentResult846.setOrderState(cpbjscResult.getdata254243());
                    cpOrderContentResult846.setOrderId("2542-43");
                    cpOrderContentResultList8.add(cpOrderContentResult846);

                    CPOrderContentResult cpOrderContentResult856 = new CPOrderContentResult();
                    cpOrderContentResult856.setOrderName("5_6");
                    cpOrderContentResult856.setFullName("二不同号");
                    cpOrderContentResult856.setOrderState(cpbjscResult.getdata254344());
                    cpOrderContentResult856.setOrderId("2543-44");
                    cpOrderContentResultList8.add(cpOrderContentResult856);

                    cpOrderContentListResult8.setData(cpOrderContentResultList8);

                    CPOrderContentListResult8.add(cpOrderContentListResult8);
                    allResult.setData(CPOrderContentListResult8);
                    allResultList.add(allResult);


                    break;
                case 8:

                    allResult.setOrderAllName("猜必出");


                    List<CPOrderContentListResult> CPOrderContentListResult9 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult9 = new CPOrderContentListResult();
                    cpOrderContentListResult9.setOrderContentListName("猜必出");
                    cpOrderContentListResult9.setShowNumber(2);
                    cpOrderContentListResult9.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList9 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult91 = new CPOrderContentResult();
                    cpOrderContentResult91.setOrderName("1_");
                    cpOrderContentResult91.setFullName("猜必出");
                    cpOrderContentResult91.setOrderState(cpbjscResult.getdata25001());
                    cpOrderContentResult91.setOrderId("2500-1");
                    cpOrderContentResultList9.add(cpOrderContentResult91);

                    CPOrderContentResult cpOrderContentResult92 = new CPOrderContentResult();
                    cpOrderContentResult92.setOrderName("2_");
                    cpOrderContentResult92.setFullName("猜必出");
                    cpOrderContentResult92.setOrderState(cpbjscResult.getdata25012());
                    cpOrderContentResult92.setOrderId("2501-2");
                    cpOrderContentResultList9.add(cpOrderContentResult92);

                    CPOrderContentResult cpOrderContentResult93 = new CPOrderContentResult();
                    cpOrderContentResult93.setOrderName("3_");
                    cpOrderContentResult93.setFullName("猜必出");
                    cpOrderContentResult93.setOrderState(cpbjscResult.getdata25023());
                    cpOrderContentResult93.setOrderId("2502-3");
                    cpOrderContentResultList9.add(cpOrderContentResult93);

                    CPOrderContentResult cpOrderContentResult94 = new CPOrderContentResult();
                    cpOrderContentResult94.setOrderName("4_");
                    cpOrderContentResult94.setFullName("猜必出");
                    cpOrderContentResult94.setOrderState(cpbjscResult.getdata25034());
                    cpOrderContentResult94.setOrderId("2503-4");
                    cpOrderContentResultList9.add(cpOrderContentResult94);

                    CPOrderContentResult cpOrderContentResult95 = new CPOrderContentResult();
                    cpOrderContentResult95.setOrderName("5_");
                    cpOrderContentResult95.setFullName("猜必出");
                    cpOrderContentResult95.setOrderState(cpbjscResult.getdata25045());
                    cpOrderContentResult95.setOrderId("2504-5");
                    cpOrderContentResultList9.add(cpOrderContentResult95);

                    CPOrderContentResult cpOrderContentResult96 = new CPOrderContentResult();
                    cpOrderContentResult96.setOrderName("6_");
                    cpOrderContentResult96.setFullName("猜必出");
                    cpOrderContentResult96.setOrderState(cpbjscResult.getdata25056());
                    cpOrderContentResult96.setOrderId("2505-6");
                    cpOrderContentResultList9.add(cpOrderContentResult96);

                    cpOrderContentListResult9.setData(cpOrderContentResultList9);

                    CPOrderContentListResult9.add(cpOrderContentListResult9);
                    allResult.setData(CPOrderContentListResult9);
                    allResultList.add(allResult);

                    break;
                case 9:


                    allResult.setOrderAllName("猜必不出");

                    List<CPOrderContentListResult> CPOrderContentListResult10 = new ArrayList<CPOrderContentListResult>();
                    CPOrderContentListResult cpOrderContentListResult10 = new CPOrderContentListResult();
                    cpOrderContentListResult10.setOrderContentListName("猜必不出");
                    cpOrderContentListResult10.setShowNumber(2);
                    cpOrderContentListResult10.setShowType("DANIEL_");

                    List<CPOrderContentResult> cpOrderContentResultList10 = new ArrayList<>();
                    CPOrderContentResult cpOrderContentResult101 = new CPOrderContentResult();
                    cpOrderContentResult101.setOrderName("1_");
                    cpOrderContentResult101.setFullName("猜必不出");
                    cpOrderContentResult101.setOrderState(cpbjscResult.getdata255051());
                    cpOrderContentResult101.setOrderId("2550-51");
                    cpOrderContentResultList10.add(cpOrderContentResult101);

                    CPOrderContentResult cpOrderContentResult102 = new CPOrderContentResult();
                    cpOrderContentResult102.setOrderName("2_");
                    cpOrderContentResult102.setFullName("猜必不出");
                    cpOrderContentResult102.setOrderState(cpbjscResult.getdata255152());
                    cpOrderContentResult102.setOrderId("2551-52");
                    cpOrderContentResultList10.add(cpOrderContentResult102);

                    CPOrderContentResult cpOrderContentResult103 = new CPOrderContentResult();
                    cpOrderContentResult103.setOrderName("3_");
                    cpOrderContentResult103.setFullName("猜必不出");
                    cpOrderContentResult103.setOrderState(cpbjscResult.getdata255253());
                    cpOrderContentResult103.setOrderId("2552-53");
                    cpOrderContentResultList10.add(cpOrderContentResult103);

                    CPOrderContentResult cpOrderContentResult104 = new CPOrderContentResult();
                    cpOrderContentResult104.setOrderName("4_");
                    cpOrderContentResult104.setFullName("猜必不出");
                    cpOrderContentResult104.setOrderState(cpbjscResult.getdata255354());
                    cpOrderContentResult104.setOrderId("2553-54");
                    cpOrderContentResultList10.add(cpOrderContentResult104);

                    CPOrderContentResult cpOrderContentResult105 = new CPOrderContentResult();
                    cpOrderContentResult105.setOrderName("5_");
                    cpOrderContentResult105.setFullName("猜必不出");
                    cpOrderContentResult105.setOrderState(cpbjscResult.getdata255455());
                    cpOrderContentResult105.setOrderId("2554-55");
                    cpOrderContentResultList10.add(cpOrderContentResult105);

                    CPOrderContentResult cpOrderContentResult106 = new CPOrderContentResult();
                    cpOrderContentResult106.setOrderName("6_");
                    cpOrderContentResult106.setFullName("猜必不出");
                    cpOrderContentResult106.setOrderState(cpbjscResult.getdata255556());
                    cpOrderContentResult106.setOrderId("2555-56");
                    cpOrderContentResultList10.add(cpOrderContentResult106);

                    cpOrderContentListResult10.setData(cpOrderContentResultList10);

                    CPOrderContentListResult10.add(cpOrderContentListResult10);
                    allResult.setData(CPOrderContentListResult10);
                    allResultList.add(allResult);

                    break;



            }

        }



    }

    private void PCDD(PCDDResult cpbjscResult){
        for (int k = 0; k < 2; ++k) {
            CPOrderAllResult allResult = new CPOrderAllResult();
            if(k==0){
                allResult.setEventChecked(true);
                allResult.setOrderAllName("混合");
                List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("混合");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("大");
                cpOrderContentResult1.setFullName("混合");
                cpOrderContentResult1.setOrderState(cpbjscResult.getData_5031());
                cpOrderContentResult1.setOrderId("5031");
                cpOrderContentResultList.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("小");
                cpOrderContentResult2.setFullName("混合");
                cpOrderContentResult2.setOrderState(cpbjscResult.getData_5032());
                cpOrderContentResult2.setOrderId("5032");
                cpOrderContentResultList.add(cpOrderContentResult2);

                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("单");
                cpOrderContentResult29.setFullName("混合");
                cpOrderContentResult29.setOrderState(cpbjscResult.getData_5029());
                cpOrderContentResult29.setOrderId("5029");
                cpOrderContentResultList.add(cpOrderContentResult29);

                CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                cpOrderContentResult30.setOrderName("双");
                cpOrderContentResult30.setFullName("混合");
                cpOrderContentResult30.setOrderState(cpbjscResult.getData_5030());
                cpOrderContentResult30.setOrderId("5030");
                cpOrderContentResultList.add(cpOrderContentResult30);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("大单");
                cpOrderContentResult33.setFullName("混合");
                cpOrderContentResult33.setOrderState(cpbjscResult.getData_5033());
                cpOrderContentResult33.setOrderId("5033");
                cpOrderContentResultList.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("小单");
                cpOrderContentResult34.setFullName("混合");
                cpOrderContentResult34.setOrderState(cpbjscResult.getData_5034());
                cpOrderContentResult34.setOrderId("5034");
                cpOrderContentResultList.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("大双");
                cpOrderContentResult35.setFullName("混合");
                cpOrderContentResult35.setOrderState(cpbjscResult.getData_5035());
                cpOrderContentResult35.setOrderId("5035");
                cpOrderContentResultList.add(cpOrderContentResult35);

                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("小双");
                cpOrderContentResult36.setFullName("混合");
                cpOrderContentResult36.setOrderState(cpbjscResult.getData_5036());
                cpOrderContentResult36.setOrderId("5036");
                cpOrderContentResultList.add(cpOrderContentResult36);

                cpOrderContentListResult.setData(cpOrderContentResultList);

                CPOrderContentListResult.add(cpOrderContentListResult);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("");
                cpOrderContentListResult2.setShowNumber(3);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("极大");
                cpOrderContentResult37.setFullName("混合");
                cpOrderContentResult37.setOrderState(cpbjscResult.getData_5037());
                cpOrderContentResult37.setOrderId("5037");
                cpOrderContentResultList2.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("极小");
                cpOrderContentResult38.setFullName("混合");
                cpOrderContentResult38.setOrderState(cpbjscResult.getData_5038());
                cpOrderContentResult38.setOrderId("5038");
                cpOrderContentResultList2.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("豹子");
                cpOrderContentResult42.setFullName("混合");
                cpOrderContentResult42.setOrderState(cpbjscResult.getData_5042());
                cpOrderContentResult42.setOrderId("5042");
                cpOrderContentResultList2.add(cpOrderContentResult42);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("红波");
                cpOrderContentResult39.setFullName("混合");
                cpOrderContentResult39.setOrderState(cpbjscResult.getData_5039());
                cpOrderContentResult39.setOrderId("5039");
                cpOrderContentResultList2.add(cpOrderContentResult39);

                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("蓝波");
                cpOrderContentResult41.setFullName("混合");
                cpOrderContentResult41.setOrderState(cpbjscResult.getData_5041());
                cpOrderContentResult41.setOrderId("5041");
                cpOrderContentResultList2.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                cpOrderContentResult40.setOrderName("绿波");
                cpOrderContentResult40.setFullName("混合");
                cpOrderContentResult40.setOrderState(cpbjscResult.getData_5040());
                cpOrderContentResult40.setOrderId("5040");
                cpOrderContentResultList2.add(cpOrderContentResult40);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult.add(cpOrderContentListResult2);

                allResult.setData(CPOrderContentListResult);
            }else{
                allResult.setOrderAllName("特码");
                List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("特码");
                cpOrderContentListResult.setShowNumber(3);
                cpOrderContentListResult.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult00 = new CPOrderContentResult();
                cpOrderContentResult00.setOrderName("0");
                cpOrderContentResult00.setFullName("特码");
                cpOrderContentResult00.setOrderState(cpbjscResult.getData_5001());
                cpOrderContentResult00.setOrderId("5001");
                cpOrderContentResultList.add(cpOrderContentResult00);

                CPOrderContentResult cpOrderContentResult01 = new CPOrderContentResult();
                cpOrderContentResult01.setOrderName("1");
                cpOrderContentResult01.setFullName("特码");
                cpOrderContentResult01.setOrderState(cpbjscResult.getData_5002());
                cpOrderContentResult01.setOrderId("5002");
                cpOrderContentResultList.add(cpOrderContentResult01);

                CPOrderContentResult cpOrderContentResult02 = new CPOrderContentResult();
                cpOrderContentResult02.setOrderName("2");
                cpOrderContentResult02.setFullName("特码");
                cpOrderContentResult02.setOrderState(cpbjscResult.getData_5003());
                cpOrderContentResult02.setOrderId("5003");
                cpOrderContentResultList.add(cpOrderContentResult02);

                CPOrderContentResult cpOrderContentResult03 = new CPOrderContentResult();
                cpOrderContentResult03.setOrderName("3");
                cpOrderContentResult03.setFullName("特码");
                cpOrderContentResult03.setOrderState(cpbjscResult.getData_5004());
                cpOrderContentResult03.setOrderId("5004");
                cpOrderContentResultList.add(cpOrderContentResult03);

                CPOrderContentResult cpOrderContentResult04 = new CPOrderContentResult();
                cpOrderContentResult04.setOrderName("4");
                cpOrderContentResult04.setFullName("特码");
                cpOrderContentResult04.setOrderState(cpbjscResult.getData_5005());
                cpOrderContentResult04.setOrderId("5005");
                cpOrderContentResultList.add(cpOrderContentResult04);

                CPOrderContentResult cpOrderContentResult05 = new CPOrderContentResult();
                cpOrderContentResult05.setOrderName("5");
                cpOrderContentResult05.setFullName("特码");
                cpOrderContentResult05.setOrderState(cpbjscResult.getData_5006());
                cpOrderContentResult05.setOrderId("5006");
                cpOrderContentResultList.add(cpOrderContentResult05);

                CPOrderContentResult cpOrderContentResult06 = new CPOrderContentResult();
                cpOrderContentResult06.setOrderName("6");
                cpOrderContentResult06.setFullName("特码");
                cpOrderContentResult06.setOrderState(cpbjscResult.getData_5007());
                cpOrderContentResult06.setOrderId("5007");
                cpOrderContentResultList.add(cpOrderContentResult06);

                CPOrderContentResult cpOrderContentResult07 = new CPOrderContentResult();
                cpOrderContentResult07.setOrderName("7");
                cpOrderContentResult07.setFullName("特码");
                cpOrderContentResult07.setOrderState(cpbjscResult.getData_5008());
                cpOrderContentResult07.setOrderId("5008");
                cpOrderContentResultList.add(cpOrderContentResult07);

                CPOrderContentResult cpOrderContentResult08 = new CPOrderContentResult();
                cpOrderContentResult08.setOrderName("8");
                cpOrderContentResult08.setFullName("特码");
                cpOrderContentResult08.setOrderState(cpbjscResult.getData_5009());
                cpOrderContentResult08.setOrderId("5009");
                cpOrderContentResultList.add(cpOrderContentResult08);

                CPOrderContentResult cpOrderContentResult09 = new CPOrderContentResult();
                cpOrderContentResult09.setOrderName("9");
                cpOrderContentResult09.setFullName("特码");
                cpOrderContentResult09.setOrderState(cpbjscResult.getData_5010());
                cpOrderContentResult09.setOrderId("5010");
                cpOrderContentResultList.add(cpOrderContentResult09);

                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("10");
                cpOrderContentResult10.setFullName("特码");
                cpOrderContentResult10.setOrderState(cpbjscResult.getData_5011());
                cpOrderContentResult10.setOrderId("5011");
                cpOrderContentResultList.add(cpOrderContentResult10);

                CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                cpOrderContentResult11.setOrderName("11");
                cpOrderContentResult11.setFullName("特码");
                cpOrderContentResult11.setOrderState(cpbjscResult.getData_5012());
                cpOrderContentResult11.setOrderId("5012");
                cpOrderContentResultList.add(cpOrderContentResult11);

                CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                cpOrderContentResult12.setOrderName("12");
                cpOrderContentResult12.setFullName("特码");
                cpOrderContentResult12.setOrderState(cpbjscResult.getData_5013());
                cpOrderContentResult12.setOrderId("5013");
                cpOrderContentResultList.add(cpOrderContentResult12);

                CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                cpOrderContentResult13.setOrderName("13");
                cpOrderContentResult13.setFullName("特码");
                cpOrderContentResult13.setOrderState(cpbjscResult.getData_5014());
                cpOrderContentResult13.setOrderId("5014");
                cpOrderContentResultList.add(cpOrderContentResult13);

                CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                cpOrderContentResult14.setOrderName("14");
                cpOrderContentResult14.setFullName("特码");
                cpOrderContentResult14.setOrderState(cpbjscResult.getData_5015());
                cpOrderContentResult14.setOrderId("5015");
                cpOrderContentResultList.add(cpOrderContentResult14);

                CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                cpOrderContentResult15.setOrderName("15");
                cpOrderContentResult15.setFullName("特码");
                cpOrderContentResult15.setOrderState(cpbjscResult.getData_5016());
                cpOrderContentResult15.setOrderId("5016");
                cpOrderContentResultList.add(cpOrderContentResult15);

                CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                cpOrderContentResult16.setOrderName("16");
                cpOrderContentResult16.setFullName("特码");
                cpOrderContentResult16.setOrderState(cpbjscResult.getData_5017());
                cpOrderContentResult16.setOrderId("5017");
                cpOrderContentResultList.add(cpOrderContentResult16);

                CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                cpOrderContentResult17.setOrderName("17");
                cpOrderContentResult17.setFullName("特码");
                cpOrderContentResult17.setOrderState(cpbjscResult.getData_5018());
                cpOrderContentResult17.setOrderId("5018");
                cpOrderContentResultList.add(cpOrderContentResult17);

                CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                cpOrderContentResult18.setOrderName("18");
                cpOrderContentResult18.setFullName("特码");
                cpOrderContentResult18.setOrderState(cpbjscResult.getData_5019());
                cpOrderContentResult18.setOrderId("5019");
                cpOrderContentResultList.add(cpOrderContentResult18);

                CPOrderContentResult cpOrderContentResult19 = new CPOrderContentResult();
                cpOrderContentResult19.setOrderName("19");
                cpOrderContentResult19.setFullName("特码");
                cpOrderContentResult19.setOrderState(cpbjscResult.getData_5020());
                cpOrderContentResult19.setOrderId("5020");
                cpOrderContentResultList.add(cpOrderContentResult19);

                CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                cpOrderContentResult20.setOrderName("20");
                cpOrderContentResult20.setFullName("特码");
                cpOrderContentResult20.setOrderState(cpbjscResult.getData_5021());
                cpOrderContentResult20.setOrderId("5021");
                cpOrderContentResultList.add(cpOrderContentResult20);

                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("21");
                cpOrderContentResult21.setFullName("特码");
                cpOrderContentResult21.setOrderState(cpbjscResult.getData_5022());
                cpOrderContentResult21.setOrderId("5022");
                cpOrderContentResultList.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("22");
                cpOrderContentResult22.setFullName("特码");
                cpOrderContentResult22.setOrderState(cpbjscResult.getData_5023());
                cpOrderContentResult22.setOrderId("5023");
                cpOrderContentResultList.add(cpOrderContentResult22);

                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("23");
                cpOrderContentResult23.setFullName("特码");
                cpOrderContentResult23.setOrderState(cpbjscResult.getData_5024());
                cpOrderContentResult23.setOrderId("5024");
                cpOrderContentResultList.add(cpOrderContentResult23);

                cpOrderContentListResult.setData(cpOrderContentResultList);

                CPOrderContentListResult.add(cpOrderContentListResult);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("");
                cpOrderContentListResult2.setShowNumber(2);
                cpOrderContentListResult2.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("24");
                cpOrderContentResult25.setFullName("特码");
                cpOrderContentResult25.setOrderState(cpbjscResult.getData_5025());
                cpOrderContentResult25.setOrderId("5025");
                cpOrderContentResultList2.add(cpOrderContentResult25);

                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("25");
                cpOrderContentResult26.setFullName("特码");
                cpOrderContentResult26.setOrderState(cpbjscResult.getData_5026());
                cpOrderContentResult26.setOrderId("5026");
                cpOrderContentResultList2.add(cpOrderContentResult26);

                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("26");
                cpOrderContentResult27.setFullName("特码");
                cpOrderContentResult27.setOrderState(cpbjscResult.getData_5027());
                cpOrderContentResult27.setOrderId("5027");
                cpOrderContentResultList2.add(cpOrderContentResult27);

                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("27");
                cpOrderContentResult28.setFullName("特码");
                cpOrderContentResult28.setOrderState(cpbjscResult.getData_5028());
                cpOrderContentResult28.setOrderId("5028");
                cpOrderContentResultList2.add(cpOrderContentResult28);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult.add(cpOrderContentListResult2);

                allResult.setData(CPOrderContentListResult);
            }
            allResultList.add(allResult);
        }
    }


    private List<CPOrderContentListResult>  CQSSC(CQSSCResult cpbjscResult,int index){
        List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
            if(index==0){
                for (int l = 0; l < 7; ++l) {
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    switch (l) {
                        case 0:
                            cpOrderContentListResult.setOrderContentListName("第一球");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                            cpOrderContentResult0.setOrderName("大");
                            cpOrderContentResult0.setFullName("第一球");
                            cpOrderContentResult0.setOrderState(cpbjscResult.getData_11005());
                            cpOrderContentResult0.setOrderId("1-1005");
                            cpOrderContentResultList.add(cpOrderContentResult0);

                            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                            cpOrderContentResult1.setOrderName("小");
                            cpOrderContentResult1.setFullName("第一球");
                            cpOrderContentResult1.setOrderState(cpbjscResult.getData_11006());
                            cpOrderContentResult1.setOrderId("1-1006");
                            cpOrderContentResultList.add(cpOrderContentResult1);

                            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                            cpOrderContentResult2.setOrderName("单");
                            cpOrderContentResult2.setFullName("第一球");
                            cpOrderContentResult2.setOrderState(cpbjscResult.getData_11007());
                            cpOrderContentResult2.setOrderId("1-1007");
                            cpOrderContentResultList.add(cpOrderContentResult2);

                            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                            cpOrderContentResult3.setOrderName("双");
                            cpOrderContentResult3.setFullName("第一球");
                            cpOrderContentResult3.setOrderState(cpbjscResult.getData_11008());
                            cpOrderContentResult3.setOrderId("1-1008");
                            cpOrderContentResultList.add(cpOrderContentResult3);
                            cpOrderContentListResult.setData(cpOrderContentResultList);

                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                        case 1:
                            cpOrderContentListResult.setOrderContentListName("第二球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                            cpOrderContentResult20.setOrderName("大");
                            cpOrderContentResult20.setFullName("第二球");
                            cpOrderContentResult20.setOrderState(cpbjscResult.getData_21005());
                            cpOrderContentResult20.setOrderId("2-1005");
                            cpOrderContentResultList2.add(cpOrderContentResult20);

                            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                            cpOrderContentResult21.setOrderName("小");
                            cpOrderContentResult21.setFullName("第二球");
                            cpOrderContentResult21.setOrderState(cpbjscResult.getData_21006());
                            cpOrderContentResult21.setOrderId("2-1006");
                            cpOrderContentResultList2.add(cpOrderContentResult21);

                            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                            cpOrderContentResult22.setOrderName("单");
                            cpOrderContentResult22.setFullName("第二球");
                            cpOrderContentResult22.setOrderState(cpbjscResult.getData_21007());
                            cpOrderContentResult22.setOrderId("2-1007");
                            cpOrderContentResultList2.add(cpOrderContentResult22);

                            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                            cpOrderContentResult23.setOrderName("双");
                            cpOrderContentResult23.setFullName("第二球");
                            cpOrderContentResult23.setOrderState(cpbjscResult.getData_21008());
                            cpOrderContentResult23.setOrderId("2-1008");
                            cpOrderContentResultList2.add(cpOrderContentResult23);

                            cpOrderContentListResult.setData(cpOrderContentResultList2);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                        case 2:
                            cpOrderContentListResult.setOrderContentListName("第三球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                            cpOrderContentResult30.setOrderName("大");
                            cpOrderContentResult30.setFullName("第三球");
                            cpOrderContentResult30.setOrderState(cpbjscResult.getData_31005());
                            cpOrderContentResult30.setOrderId("3-1005");
                            cpOrderContentResultList3.add(cpOrderContentResult30);

                            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                            cpOrderContentResult31.setOrderName("小");
                            cpOrderContentResult31.setFullName("第三球");
                            cpOrderContentResult31.setOrderState(cpbjscResult.getData_31006());
                            cpOrderContentResult31.setOrderId("3-1006");
                            cpOrderContentResultList3.add(cpOrderContentResult31);

                            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                            cpOrderContentResult32.setOrderName("单");
                            cpOrderContentResult32.setFullName("第三球");
                            cpOrderContentResult32.setOrderState(cpbjscResult.getData_31007());
                            cpOrderContentResult32.setOrderId("3-1007");
                            cpOrderContentResultList3.add(cpOrderContentResult32);

                            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                            cpOrderContentResult33.setOrderName("双");
                            cpOrderContentResult33.setFullName("第三球");
                            cpOrderContentResult33.setOrderState(cpbjscResult.getData_31008());
                            cpOrderContentResult33.setOrderId("3-1008");
                            cpOrderContentResultList3.add(cpOrderContentResult33);

                            cpOrderContentListResult.setData(cpOrderContentResultList3);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);

                            break;
                        case 3:
                            cpOrderContentListResult.setOrderContentListName("第四球");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                            cpOrderContentResult40.setOrderName("大");
                            cpOrderContentResult40.setFullName("第四球");
                            cpOrderContentResult40.setOrderState(cpbjscResult.getData_41005());
                            cpOrderContentResult40.setOrderId("4-1005");
                            cpOrderContentResultList4.add(cpOrderContentResult40);

                            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                            cpOrderContentResult41.setOrderName("小");
                            cpOrderContentResult41.setFullName("第四球");
                            cpOrderContentResult41.setOrderState(cpbjscResult.getData_41006());
                            cpOrderContentResult41.setOrderId("4-1006");
                            cpOrderContentResultList4.add(cpOrderContentResult41);

                            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                            cpOrderContentResult42.setOrderName("单");
                            cpOrderContentResult42.setFullName("第四球");
                            cpOrderContentResult42.setOrderState(cpbjscResult.getData_41007());
                            cpOrderContentResult42.setOrderId("4-1007");
                            cpOrderContentResultList4.add(cpOrderContentResult42);

                            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                            cpOrderContentResult43.setOrderName("双");
                            cpOrderContentResult43.setFullName("第四球");
                            cpOrderContentResult43.setOrderState(cpbjscResult.getData_41008());
                            cpOrderContentResult43.setOrderId("4-1008");
                            cpOrderContentResultList4.add(cpOrderContentResult43);

                            cpOrderContentListResult.setData(cpOrderContentResultList4);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                        case 4:
                            cpOrderContentListResult.setOrderContentListName("第五球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                            cpOrderContentResult50.setOrderName("大");
                            cpOrderContentResult50.setFullName("第五球");
                            cpOrderContentResult50.setOrderState(cpbjscResult.getData_51005());
                            cpOrderContentResult50.setOrderId("5-1005");
                            cpOrderContentResultList5.add(cpOrderContentResult50);

                            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                            cpOrderContentResult51.setOrderName("小");
                            cpOrderContentResult51.setFullName("第五球");
                            cpOrderContentResult51.setOrderState(cpbjscResult.getData_51006());
                            cpOrderContentResult51.setOrderId("5-1006");
                            cpOrderContentResultList5.add(cpOrderContentResult51);

                            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                            cpOrderContentResult52.setOrderName("单");
                            cpOrderContentResult52.setFullName("第五球");
                            cpOrderContentResult52.setOrderState(cpbjscResult.getData_51007());
                            cpOrderContentResult52.setOrderId("5-1007");
                            cpOrderContentResultList5.add(cpOrderContentResult52);

                            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                            cpOrderContentResult53.setOrderName("双");
                            cpOrderContentResult53.setFullName("第五球");
                            cpOrderContentResult53.setOrderState(cpbjscResult.getData_51008());
                            cpOrderContentResult53.setOrderId("5-1008");
                            cpOrderContentResultList5.add(cpOrderContentResult53);

                            cpOrderContentListResult.setData(cpOrderContentResultList5);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                        case 5:
                            cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                            cpOrderContentResult60.setOrderName("总和大");
                            cpOrderContentResult60.setFullName("");
                            cpOrderContentResult60.setOrderState(cpbjscResult.getData_1009());
                            cpOrderContentResult60.setOrderId("1009");
                            cpOrderContentResultList6.add(cpOrderContentResult60);

                            CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                            cpOrderContentResult61.setOrderName("总和小");
                            cpOrderContentResult61.setFullName("");
                            cpOrderContentResult61.setOrderState(cpbjscResult.getData_1010());
                            cpOrderContentResult61.setOrderId("1010");
                            cpOrderContentResultList6.add(cpOrderContentResult61);

                            CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                            cpOrderContentResult62.setOrderName("总和单");
                            cpOrderContentResult62.setFullName("");
                            cpOrderContentResult62.setOrderState(cpbjscResult.getData_1011());
                            cpOrderContentResult62.setOrderId("1011");
                            cpOrderContentResultList6.add(cpOrderContentResult62);

                            CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                            cpOrderContentResult63.setOrderName("总和双");
                            cpOrderContentResult63.setFullName("");
                            cpOrderContentResult63.setOrderState(cpbjscResult.getData_1012());
                            cpOrderContentResult63.setOrderId("1012");
                            cpOrderContentResultList6.add(cpOrderContentResult63);

                            cpOrderContentListResult.setData(cpOrderContentResultList6);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                        case 6:
                            cpOrderContentListResult.setOrderContentListName("");
                            cpOrderContentListResult.setShowNumber(3);


                            List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                            cpOrderContentResult70.setOrderName("龙");
                            cpOrderContentResult70.setFullName("");
                            cpOrderContentResult70.setOrderState(cpbjscResult.getData_1013());
                            cpOrderContentResult70.setOrderId("1013");
                            cpOrderContentResultList7.add(cpOrderContentResult70);

                            CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                            cpOrderContentResult71.setOrderName("虎");
                            cpOrderContentResult71.setFullName("");
                            cpOrderContentResult71.setOrderState(cpbjscResult.getData_1014());
                            cpOrderContentResult71.setOrderId("1014");
                            cpOrderContentResultList7.add(cpOrderContentResult71);

                            CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                            cpOrderContentResult72.setOrderName("和");
                            cpOrderContentResult72.setFullName("");
                            cpOrderContentResult72.setOrderState(cpbjscResult.getData_1015());
                            cpOrderContentResult72.setOrderId("1015");
                            cpOrderContentResultList7.add(cpOrderContentResult72);

                            cpOrderContentListResult.setData(cpOrderContentResultList7);
                            cPOrderContentListResultAll.add(cpOrderContentListResult);
                            break;
                    }
                }
                return cPOrderContentListResultAll;
            }else if(index==1){
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("第一球");
                cpOrderContentListResult.setShowNumber(2);
                cpOrderContentListResult.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                cpOrderContentResult0.setOrderName("0");
                cpOrderContentResult0.setFullName("第一球");
                cpOrderContentResult0.setOrderState(cpbjscResult.getData_10000());
                cpOrderContentResult0.setOrderId("1000-0");
                cpOrderContentResultList.add(cpOrderContentResult0);

                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("第一球");
                cpOrderContentResult1.setOrderState(cpbjscResult.getData_10001());
                cpOrderContentResult1.setOrderId("1000-1");
                cpOrderContentResultList.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("第一球");
                cpOrderContentResult2.setOrderState(cpbjscResult.getData_10002());
                cpOrderContentResult2.setOrderId("1000-2");
                cpOrderContentResultList.add(cpOrderContentResult2);

                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("第一球");
                cpOrderContentResult3.setOrderState(cpbjscResult.getData_10003());
                cpOrderContentResult3.setOrderId("1000-3");
                cpOrderContentResultList.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("第一球");
                cpOrderContentResult4.setOrderState(cpbjscResult.getData_10004());
                cpOrderContentResult4.setOrderId("1000-4");
                cpOrderContentResultList.add(cpOrderContentResult4);


                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("第一球");
                cpOrderContentResult5.setOrderState(cpbjscResult.getData_10005());
                cpOrderContentResult5.setOrderId("1000-5");
                cpOrderContentResultList.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("第一球");
                cpOrderContentResult6.setOrderState(cpbjscResult.getData_10006());
                cpOrderContentResult6.setOrderId("1000-6");
                cpOrderContentResultList.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("第一球");
                cpOrderContentResult7.setOrderState(cpbjscResult.getData_10007());
                cpOrderContentResult7.setOrderId("1000-7");
                cpOrderContentResultList.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("第一球");
                cpOrderContentResult8.setOrderState(cpbjscResult.getData_10008());
                cpOrderContentResult8.setOrderId("1000-8");
                cpOrderContentResultList.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("第一球");
                cpOrderContentResult9.setOrderState(cpbjscResult.getData_10009());
                cpOrderContentResult9.setOrderId("1000-9");
                cpOrderContentResultList.add(cpOrderContentResult9);

                cpOrderContentListResult.setData(cpOrderContentResultList);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("第二球");
                cpOrderContentListResult2.setShowNumber(2);
                cpOrderContentListResult2.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                cpOrderContentResult20.setOrderName("0");
                cpOrderContentResult20.setFullName("第二球");
                cpOrderContentResult20.setOrderState(cpbjscResult.getData_10010());
                cpOrderContentResult20.setOrderId("1001-0");
                cpOrderContentResultList2.add(cpOrderContentResult20);

                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("第二球");
                cpOrderContentResult21.setOrderState(cpbjscResult.getData_10011());
                cpOrderContentResult21.setOrderId("1001-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("第二球");
                cpOrderContentResult22.setOrderState(cpbjscResult.getData_10012());
                cpOrderContentResult22.setOrderId("1001-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("第二球");
                cpOrderContentResult23.setOrderState(cpbjscResult.getData_10013());
                cpOrderContentResult23.setOrderId("1001-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);


                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("第二球");
                cpOrderContentResult24.setOrderState(cpbjscResult.getData_10014());
                cpOrderContentResult24.setOrderId("1001-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("第二球");
                cpOrderContentResult25.setOrderState(cpbjscResult.getData_10015());
                cpOrderContentResult25.setOrderId("1001-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);


                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("第二球");
                cpOrderContentResult26.setOrderState(cpbjscResult.getData_10016());
                cpOrderContentResult26.setOrderId("1001-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);


                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("第二球");
                cpOrderContentResult27.setOrderState(cpbjscResult.getData_10017());
                cpOrderContentResult27.setOrderId("1001-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);


                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("第二球");
                cpOrderContentResult28.setOrderState(cpbjscResult.getData_10018());
                cpOrderContentResult28.setOrderId("1001-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);


                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("第二球");
                cpOrderContentResult29.setOrderState(cpbjscResult.getData_10019());
                cpOrderContentResult29.setOrderId("1001-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);


                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第三球");
                cpOrderContentListResult3.setShowNumber(2);
                cpOrderContentListResult3.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                cpOrderContentResult30.setOrderName("0");
                cpOrderContentResult30.setFullName("第三球");
                cpOrderContentResult30.setOrderState(cpbjscResult.getData_10020());
                cpOrderContentResult30.setOrderId("1002-0");
                cpOrderContentResultList3.add(cpOrderContentResult30);

                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第三球");
                cpOrderContentResult31.setOrderState(cpbjscResult.getData_10021());
                cpOrderContentResult31.setOrderId("1002-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第三球");
                cpOrderContentResult32.setOrderState(cpbjscResult.getData_10022());
                cpOrderContentResult32.setOrderId("1002-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第三球");
                cpOrderContentResult33.setOrderState(cpbjscResult.getData_10023());
                cpOrderContentResult33.setOrderId("1002-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第三球");
                cpOrderContentResult34.setOrderState(cpbjscResult.getData_10024());
                cpOrderContentResult34.setOrderId("1002-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第三球");
                cpOrderContentResult35.setOrderState(cpbjscResult.getData_10025());
                cpOrderContentResult35.setOrderId("1002-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);


                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第三球");
                cpOrderContentResult36.setOrderState(cpbjscResult.getData_10026());
                cpOrderContentResult36.setOrderId("1002-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第三球");
                cpOrderContentResult37.setOrderState(cpbjscResult.getData_10027());
                cpOrderContentResult37.setOrderId("1002-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第三球");
                cpOrderContentResult38.setOrderState(cpbjscResult.getData_10028());
                cpOrderContentResult38.setOrderId("1002-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第三球");
                cpOrderContentResult39.setOrderState(cpbjscResult.getData_10029());
                cpOrderContentResult39.setOrderId("1002-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第四球");
                cpOrderContentListResult4.setShowNumber(2);
                cpOrderContentListResult4.setShowType("QIU");
                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                cpOrderContentResult40.setOrderName("0");
                cpOrderContentResult40.setFullName("第四球");
                cpOrderContentResult40.setOrderState(cpbjscResult.getData_10030());
                cpOrderContentResult40.setOrderId("1003-0");
                cpOrderContentResultList4.add(cpOrderContentResult40);

                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第四球");
                cpOrderContentResult41.setOrderState(cpbjscResult.getData_10031());
                cpOrderContentResult41.setOrderId("1003-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第四球");
                cpOrderContentResult42.setOrderState(cpbjscResult.getData_10032());
                cpOrderContentResult42.setOrderId("1003-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);

                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第四球");
                cpOrderContentResult43.setOrderState(cpbjscResult.getData_10033());
                cpOrderContentResult43.setOrderId("1003-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第四球");
                cpOrderContentResult44.setOrderState(cpbjscResult.getData_10034());
                cpOrderContentResult44.setOrderId("1003-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第四球");
                cpOrderContentResult45.setOrderState(cpbjscResult.getData_10035());
                cpOrderContentResult45.setOrderId("1003-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第四球");
                cpOrderContentResult46.setOrderState(cpbjscResult.getData_10036());
                cpOrderContentResult46.setOrderId("1003-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第四球");
                cpOrderContentResult47.setOrderState(cpbjscResult.getData_10037());
                cpOrderContentResult47.setOrderId("1003-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第四球");
                cpOrderContentResult48.setOrderState(cpbjscResult.getData_10038());
                cpOrderContentResult48.setOrderId("1003-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第四球");
                cpOrderContentResult49.setOrderState(cpbjscResult.getData_10039());
                cpOrderContentResult49.setOrderId("1003-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);


                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第五球");
                cpOrderContentListResult5.setShowNumber(2);
                cpOrderContentListResult3.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                cpOrderContentResult50.setOrderName("0");
                cpOrderContentResult50.setFullName("第五球");
                cpOrderContentResult50.setOrderState(cpbjscResult.getData_10040());
                cpOrderContentResult50.setOrderId("1004-0");
                cpOrderContentResultList5.add(cpOrderContentResult50);

                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第五球");
                cpOrderContentResult51.setOrderState(cpbjscResult.getData_10041());
                cpOrderContentResult51.setOrderId("1004-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第五球");
                cpOrderContentResult52.setOrderState(cpbjscResult.getData_10042());
                cpOrderContentResult52.setOrderId("1004-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);

                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第五球");
                cpOrderContentResult53.setOrderState(cpbjscResult.getData_10043());
                cpOrderContentResult53.setOrderId("1004-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第五球");
                cpOrderContentResult54.setOrderState(cpbjscResult.getData_10044());
                cpOrderContentResult54.setOrderId("1004-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第五球");
                cpOrderContentResult55.setOrderState(cpbjscResult.getData_10045());
                cpOrderContentResult55.setOrderId("1004-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第五球");
                cpOrderContentResult56.setOrderState(cpbjscResult.getData_10046());
                cpOrderContentResult56.setOrderId("1004-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第五球");
                cpOrderContentResult57.setOrderState(cpbjscResult.getData_10047());
                cpOrderContentResult57.setOrderId("1004-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第五球");
                cpOrderContentResult58.setOrderState(cpbjscResult.getData_10048());
                cpOrderContentResult58.setOrderId("1004-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第五球");
                cpOrderContentResult59.setOrderState(cpbjscResult.getData_10049());
                cpOrderContentResult59.setOrderId("1004-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult4);
                cPOrderContentListResultAll.add(cpOrderContentListResult5);

                return cPOrderContentListResultAll;
            }else if(index==2){
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("前三");
                cpOrderContentListResult.setShowType("ZI");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("豹子");
                cpOrderContentResult1.setFullName("前三");
                cpOrderContentResult1.setOrderState(cpbjscResult.getData_1016());
                cpOrderContentResult1.setOrderId("1016");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("顺子");
                cpOrderContentResult2.setFullName("前三");
                cpOrderContentResult2.setOrderState(cpbjscResult.getData_1017());
                cpOrderContentResult2.setOrderId("1017");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("对子");
                cpOrderContentResult3.setFullName("前三");
                cpOrderContentResult3.setOrderState(cpbjscResult.getData_1018());
                cpOrderContentResult3.setOrderId("1018");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("半顺");
                cpOrderContentResult4.setFullName("前三");
                cpOrderContentResult4.setOrderState(cpbjscResult.getData_1019());
                cpOrderContentResult4.setOrderId("1019");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult11 = new CPOrderContentListResult();
                cpOrderContentListResult11.setOrderContentListName("");
                cpOrderContentListResult11.setShowType("ZI");
                cpOrderContentListResult11.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList11 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("杂六");
                cpOrderContentResult5.setFullName("前三");
                cpOrderContentResult5.setOrderState(cpbjscResult.getData_1020());
                cpOrderContentResult5.setOrderId("1020");
                cpOrderContentResultList11.add(cpOrderContentResult5);

                cpOrderContentListResult11.setData(cpOrderContentResultList11);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("中三");
                cpOrderContentListResult2.setShowType("ZI");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("豹子");
                cpOrderContentResult21.setFullName("中三");
                cpOrderContentResult21.setOrderState(cpbjscResult.getData_1021());
                cpOrderContentResult21.setOrderId("1021");
                cpOrderContentResultList2.add(cpOrderContentResult21);


                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("顺子");
                cpOrderContentResult22.setFullName("中三");
                cpOrderContentResult22.setOrderState(cpbjscResult.getData_1022());
                cpOrderContentResult22.setOrderId("1022");
                cpOrderContentResultList2.add(cpOrderContentResult22);

                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("对子");
                cpOrderContentResult23.setFullName("中三");
                cpOrderContentResult23.setOrderState(cpbjscResult.getData_1023());
                cpOrderContentResult23.setOrderId("1023");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("半顺");
                cpOrderContentResult24.setFullName("中三");
                cpOrderContentResult24.setOrderState(cpbjscResult.getData_1024());
                cpOrderContentResult24.setOrderId("1024");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
                cpOrderContentListResult22.setOrderContentListName("");
                cpOrderContentListResult22.setShowType("ZI");
                cpOrderContentListResult22.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("杂六");
                cpOrderContentResult25.setFullName("中三");
                cpOrderContentResult25.setOrderState(cpbjscResult.getData_1025());
                cpOrderContentResult25.setOrderId("1025");
                cpOrderContentResultList22.add(cpOrderContentResult25);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                cpOrderContentListResult22.setData(cpOrderContentResultList22);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("后三");
                cpOrderContentListResult3.setShowType("ZI");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("豹子");
                cpOrderContentResult31.setFullName("后三");
                cpOrderContentResult31.setOrderState(cpbjscResult.getData_1026());
                cpOrderContentResult31.setOrderId("1026");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("顺子");
                cpOrderContentResult32.setFullName("后三");
                cpOrderContentResult32.setOrderState(cpbjscResult.getData_1027());
                cpOrderContentResult32.setOrderId("1027");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("对子");
                cpOrderContentResult33.setFullName("后三");
                cpOrderContentResult33.setOrderState(cpbjscResult.getData_1028());
                cpOrderContentResult33.setOrderId("1028");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("半顺");
                cpOrderContentResult34.setFullName("后三");
                cpOrderContentResult34.setOrderState(cpbjscResult.getData_1029());
                cpOrderContentResult34.setOrderId("1029");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
                cpOrderContentListResult33.setOrderContentListName("");
                cpOrderContentListResult33.setShowType("ZI");
                cpOrderContentListResult33.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("杂六");
                cpOrderContentResult35.setFullName("后三");
                cpOrderContentResult35.setOrderState(cpbjscResult.getData_1030());
                cpOrderContentResult35.setOrderId("1030");
                cpOrderContentResultList33.add(cpOrderContentResult35);

                cpOrderContentListResult33.setData(cpOrderContentResultList33);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult11);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult22);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult33);
                return cPOrderContentListResultAll;
            }
            return cPOrderContentListResultAll;
    }

    private List<CPOrderContentListResult>  CQSSC1(CQ1FCResult cpbjscResult,int index){
        List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
        if(index==0){
            for (int l = 0; l < 7; ++l) {
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                switch (l) {
                    case 0:
                        cpOrderContentListResult.setOrderContentListName("第一球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                        cpOrderContentResult0.setOrderName("大");
                        cpOrderContentResult0.setFullName("第一球");
                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata11105());
                        cpOrderContentResult0.setOrderId("1-1105");
                        cpOrderContentResultList.add(cpOrderContentResult0);

                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                        cpOrderContentResult1.setOrderName("小");
                        cpOrderContentResult1.setFullName("第一球");
                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata11106());
                        cpOrderContentResult1.setOrderId("1-1106");
                        cpOrderContentResultList.add(cpOrderContentResult1);

                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                        cpOrderContentResult2.setOrderName("单");
                        cpOrderContentResult2.setFullName("第一球");
                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata11107());
                        cpOrderContentResult2.setOrderId("1-1107");
                        cpOrderContentResultList.add(cpOrderContentResult2);

                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                        cpOrderContentResult3.setOrderName("双");
                        cpOrderContentResult3.setFullName("第一球");
                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata11108());
                        cpOrderContentResult3.setOrderId("1-1108");
                        cpOrderContentResultList.add(cpOrderContentResult3);
                        cpOrderContentListResult.setData(cpOrderContentResultList);

                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 1:
                        cpOrderContentListResult.setOrderContentListName("第二球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                        cpOrderContentResult20.setOrderName("大");
                        cpOrderContentResult20.setFullName("第二球");
                        cpOrderContentResult20.setOrderState(cpbjscResult.getdata21105());
                        cpOrderContentResult20.setOrderId("2-1105");
                        cpOrderContentResultList2.add(cpOrderContentResult20);

                        CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                        cpOrderContentResult21.setOrderName("小");
                        cpOrderContentResult21.setFullName("第二球");
                        cpOrderContentResult21.setOrderState(cpbjscResult.getdata21106());
                        cpOrderContentResult21.setOrderId("2-1106");
                        cpOrderContentResultList2.add(cpOrderContentResult21);

                        CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                        cpOrderContentResult22.setOrderName("单");
                        cpOrderContentResult22.setFullName("第二球");
                        cpOrderContentResult22.setOrderState(cpbjscResult.getdata21107());
                        cpOrderContentResult22.setOrderId("2-1107");
                        cpOrderContentResultList2.add(cpOrderContentResult22);

                        CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                        cpOrderContentResult23.setOrderName("双");
                        cpOrderContentResult23.setFullName("第二球");
                        cpOrderContentResult23.setOrderState(cpbjscResult.getdata21108());
                        cpOrderContentResult23.setOrderId("2-1108");
                        cpOrderContentResultList2.add(cpOrderContentResult23);

                        cpOrderContentListResult.setData(cpOrderContentResultList2);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 2:
                        cpOrderContentListResult.setOrderContentListName("第三球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                        cpOrderContentResult30.setOrderName("大");
                        cpOrderContentResult30.setFullName("第三球");
                        cpOrderContentResult30.setOrderState(cpbjscResult.getdata31105());
                        cpOrderContentResult30.setOrderId("3-1105");
                        cpOrderContentResultList3.add(cpOrderContentResult30);

                        CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                        cpOrderContentResult31.setOrderName("小");
                        cpOrderContentResult31.setFullName("第三球");
                        cpOrderContentResult31.setOrderState(cpbjscResult.getdata31106());
                        cpOrderContentResult31.setOrderId("3-1106");
                        cpOrderContentResultList3.add(cpOrderContentResult31);

                        CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                        cpOrderContentResult32.setOrderName("单");
                        cpOrderContentResult32.setFullName("第三球");
                        cpOrderContentResult32.setOrderState(cpbjscResult.getdata31107());
                        cpOrderContentResult32.setOrderId("3-1107");
                        cpOrderContentResultList3.add(cpOrderContentResult32);

                        CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                        cpOrderContentResult33.setOrderName("双");
                        cpOrderContentResult33.setFullName("第三球");
                        cpOrderContentResult33.setOrderState(cpbjscResult.getdata31108());
                        cpOrderContentResult33.setOrderId("3-1108");
                        cpOrderContentResultList3.add(cpOrderContentResult33);

                        cpOrderContentListResult.setData(cpOrderContentResultList3);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);

                        break;
                    case 3:
                        cpOrderContentListResult.setOrderContentListName("第四球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                        cpOrderContentResult40.setOrderName("大");
                        cpOrderContentResult40.setFullName("第四球");
                        cpOrderContentResult40.setOrderState(cpbjscResult.getdata41105());
                        cpOrderContentResult40.setOrderId("4-1105");
                        cpOrderContentResultList4.add(cpOrderContentResult40);

                        CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                        cpOrderContentResult41.setOrderName("小");
                        cpOrderContentResult41.setFullName("第四球");
                        cpOrderContentResult41.setOrderState(cpbjscResult.getdata41106());
                        cpOrderContentResult41.setOrderId("4-1106");
                        cpOrderContentResultList4.add(cpOrderContentResult41);

                        CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                        cpOrderContentResult42.setOrderName("单");
                        cpOrderContentResult42.setFullName("第四球");
                        cpOrderContentResult42.setOrderState(cpbjscResult.getdata41107());
                        cpOrderContentResult42.setOrderId("4-1107");
                        cpOrderContentResultList4.add(cpOrderContentResult42);

                        CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                        cpOrderContentResult43.setOrderName("双");
                        cpOrderContentResult43.setFullName("第四球");
                        cpOrderContentResult43.setOrderState(cpbjscResult.getdata41108());
                        cpOrderContentResult43.setOrderId("4-1108");
                        cpOrderContentResultList4.add(cpOrderContentResult43);

                        cpOrderContentListResult.setData(cpOrderContentResultList4);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 4:
                        cpOrderContentListResult.setOrderContentListName("第五球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                        cpOrderContentResult50.setOrderName("大");
                        cpOrderContentResult50.setFullName("第五球");
                        cpOrderContentResult50.setOrderState(cpbjscResult.getdata51105());
                        cpOrderContentResult50.setOrderId("5-1105");
                        cpOrderContentResultList5.add(cpOrderContentResult50);

                        CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                        cpOrderContentResult51.setOrderName("小");
                        cpOrderContentResult51.setFullName("第五球");
                        cpOrderContentResult51.setOrderState(cpbjscResult.getdata51106());
                        cpOrderContentResult51.setOrderId("5-1106");
                        cpOrderContentResultList5.add(cpOrderContentResult51);

                        CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                        cpOrderContentResult52.setOrderName("单");
                        cpOrderContentResult52.setFullName("第五球");
                        cpOrderContentResult52.setOrderState(cpbjscResult.getdata51107());
                        cpOrderContentResult52.setOrderId("5-1107");
                        cpOrderContentResultList5.add(cpOrderContentResult52);

                        CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                        cpOrderContentResult53.setOrderName("双");
                        cpOrderContentResult53.setFullName("第五球");
                        cpOrderContentResult53.setOrderState(cpbjscResult.getdata51108());
                        cpOrderContentResult53.setOrderId("5-1108");
                        cpOrderContentResultList5.add(cpOrderContentResult53);

                        cpOrderContentListResult.setData(cpOrderContentResultList5);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 5:
                        cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                        cpOrderContentResult60.setOrderName("总和大");
                        cpOrderContentResult60.setFullName("");
                        cpOrderContentResult60.setOrderState(cpbjscResult.getdata1109());
                        cpOrderContentResult60.setOrderId("1109");
                        cpOrderContentResultList6.add(cpOrderContentResult60);

                        CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                        cpOrderContentResult61.setOrderName("总和小");
                        cpOrderContentResult61.setFullName("");
                        cpOrderContentResult61.setOrderState(cpbjscResult.getdata1110());
                        cpOrderContentResult61.setOrderId("1110");
                        cpOrderContentResultList6.add(cpOrderContentResult61);

                        CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                        cpOrderContentResult62.setOrderName("总和单");
                        cpOrderContentResult62.setFullName("");
                        cpOrderContentResult62.setOrderState(cpbjscResult.getdata1111());
                        cpOrderContentResult62.setOrderId("1111");
                        cpOrderContentResultList6.add(cpOrderContentResult62);

                        CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                        cpOrderContentResult63.setOrderName("总和双");
                        cpOrderContentResult63.setFullName("");
                        cpOrderContentResult63.setOrderState(cpbjscResult.getdata1112());
                        cpOrderContentResult63.setOrderId("1112");
                        cpOrderContentResultList6.add(cpOrderContentResult63);

                        cpOrderContentListResult.setData(cpOrderContentResultList6);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 6:
                        cpOrderContentListResult.setOrderContentListName("");
                        cpOrderContentListResult.setShowNumber(3);


                        List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                        cpOrderContentResult70.setOrderName("龙");
                        cpOrderContentResult70.setFullName("");
                        cpOrderContentResult70.setOrderState(cpbjscResult.getdata1113());
                        cpOrderContentResult70.setOrderId("1113");
                        cpOrderContentResultList7.add(cpOrderContentResult70);

                        CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                        cpOrderContentResult71.setOrderName("虎");
                        cpOrderContentResult71.setFullName("");
                        cpOrderContentResult71.setOrderState(cpbjscResult.getdata1114());
                        cpOrderContentResult71.setOrderId("1114");
                        cpOrderContentResultList7.add(cpOrderContentResult71);

                        CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                        cpOrderContentResult72.setOrderName("和");
                        cpOrderContentResult72.setFullName("");
                        cpOrderContentResult72.setOrderState(cpbjscResult.getdata1115());
                        cpOrderContentResult72.setOrderId("1115");
                        cpOrderContentResultList7.add(cpOrderContentResult72);

                        cpOrderContentListResult.setData(cpOrderContentResultList7);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                }
            }
            return cPOrderContentListResultAll;
        }else if(index==1){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("第一球");
            cpOrderContentListResult.setShowNumber(2);
            cpOrderContentListResult.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
            cpOrderContentResult0.setOrderName("0");
            cpOrderContentResult0.setFullName("第一球");
            cpOrderContentResult0.setOrderState(cpbjscResult.getdata11000());
            cpOrderContentResult0.setOrderId("1100-0");
            cpOrderContentResultList.add(cpOrderContentResult0);

            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("第一球");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata11001());
            cpOrderContentResult1.setOrderId("1100-1");
            cpOrderContentResultList.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("第一球");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata11002());
            cpOrderContentResult2.setOrderId("1100-2");
            cpOrderContentResultList.add(cpOrderContentResult2);

            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("第一球");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata11003());
            cpOrderContentResult3.setOrderId("1100-3");
            cpOrderContentResultList.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("第一球");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata11004());
            cpOrderContentResult4.setOrderId("1100-4");
            cpOrderContentResultList.add(cpOrderContentResult4);


            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("第一球");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata11005());
            cpOrderContentResult5.setOrderId("1100-5");
            cpOrderContentResultList.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("第一球");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata11006());
            cpOrderContentResult6.setOrderId("1100-6");
            cpOrderContentResultList.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("第一球");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata11007());
            cpOrderContentResult7.setOrderId("1100-7");
            cpOrderContentResultList.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("第一球");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata11008());
            cpOrderContentResult8.setOrderId("1100-8");
            cpOrderContentResultList.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("第一球");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata11009());
            cpOrderContentResult9.setOrderId("1100-9");
            cpOrderContentResultList.add(cpOrderContentResult9);

            cpOrderContentListResult.setData(cpOrderContentResultList);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("第二球");
            cpOrderContentListResult2.setShowNumber(2);
            cpOrderContentListResult2.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
            cpOrderContentResult20.setOrderName("0");
            cpOrderContentResult20.setFullName("第二球");
            cpOrderContentResult20.setOrderState(cpbjscResult.getdata11010());
            cpOrderContentResult20.setOrderId("1101-0");
            cpOrderContentResultList2.add(cpOrderContentResult20);

            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("第二球");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata11011());
            cpOrderContentResult21.setOrderId("1101-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("第二球");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata11012());
            cpOrderContentResult22.setOrderId("1101-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("第二球");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata11013());
            cpOrderContentResult23.setOrderId("1101-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);


            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("第二球");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata11014());
            cpOrderContentResult24.setOrderId("1101-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("第二球");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata11015());
            cpOrderContentResult25.setOrderId("1101-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);


            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("第二球");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata11016());
            cpOrderContentResult26.setOrderId("1101-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);


            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("第二球");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata11017());
            cpOrderContentResult27.setOrderId("1101-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);


            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("第二球");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata11018());
            cpOrderContentResult28.setOrderId("1101-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);


            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("第二球");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata11019());
            cpOrderContentResult29.setOrderId("1101-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);


            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第三球");
            cpOrderContentListResult3.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
            cpOrderContentResult30.setOrderName("0");
            cpOrderContentResult30.setFullName("第三球");
            cpOrderContentResult30.setOrderState(cpbjscResult.getdata11020());
            cpOrderContentResult30.setOrderId("1102-0");
            cpOrderContentResultList3.add(cpOrderContentResult30);

            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第三球");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata11021());
            cpOrderContentResult31.setOrderId("1102-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第三球");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata11022());
            cpOrderContentResult32.setOrderId("1102-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第三球");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata11023());
            cpOrderContentResult33.setOrderId("1102-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第三球");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata11024());
            cpOrderContentResult34.setOrderId("1102-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第三球");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata11025());
            cpOrderContentResult35.setOrderId("1102-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);


            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第三球");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata11026());
            cpOrderContentResult36.setOrderId("1102-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第三球");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata11027());
            cpOrderContentResult37.setOrderId("1102-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第三球");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata11028());
            cpOrderContentResult38.setOrderId("1102-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第三球");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata11029());
            cpOrderContentResult39.setOrderId("1102-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第四球");
            cpOrderContentListResult4.setShowNumber(2);
            cpOrderContentListResult4.setShowType("QIU");
            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
            cpOrderContentResult40.setOrderName("0");
            cpOrderContentResult40.setFullName("第四球");
            cpOrderContentResult40.setOrderState(cpbjscResult.getdata11030());
            cpOrderContentResult40.setOrderId("1103-0");
            cpOrderContentResultList4.add(cpOrderContentResult40);

            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第四球");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata11031());
            cpOrderContentResult41.setOrderId("1103-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第四球");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata11032());
            cpOrderContentResult42.setOrderId("1103-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);

            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第四球");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata11033());
            cpOrderContentResult43.setOrderId("1103-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第四球");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata11034());
            cpOrderContentResult44.setOrderId("1103-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第四球");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata11035());
            cpOrderContentResult45.setOrderId("1103-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第四球");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata11036());
            cpOrderContentResult46.setOrderId("1103-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第四球");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata11037());
            cpOrderContentResult47.setOrderId("1103-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第四球");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata11038());
            cpOrderContentResult48.setOrderId("1103-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第四球");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata11039());
            cpOrderContentResult49.setOrderId("1103-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);


            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第五球");
            cpOrderContentListResult5.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
            cpOrderContentResult50.setOrderName("0");
            cpOrderContentResult50.setFullName("第五球");
            cpOrderContentResult50.setOrderState(cpbjscResult.getdata11040());
            cpOrderContentResult50.setOrderId("1104-0");
            cpOrderContentResultList5.add(cpOrderContentResult50);

            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第五球");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata11041());
            cpOrderContentResult51.setOrderId("1104-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第五球");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata11042());
            cpOrderContentResult52.setOrderId("1104-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);

            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第五球");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata11043());
            cpOrderContentResult53.setOrderId("1104-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第五球");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata11044());
            cpOrderContentResult54.setOrderId("1104-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第五球");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata11045());
            cpOrderContentResult55.setOrderId("1104-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第五球");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata11046());
            cpOrderContentResult56.setOrderId("1104-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第五球");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata11047());
            cpOrderContentResult57.setOrderId("1104-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第五球");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata11048());
            cpOrderContentResult58.setOrderId("1104-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第五球");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata11049());
            cpOrderContentResult59.setOrderId("1104-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult4);
            cPOrderContentListResultAll.add(cpOrderContentListResult5);

            return cPOrderContentListResultAll;
        }else if(index==2){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("前三");
            cpOrderContentListResult.setShowType("ZI");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("豹子");
            cpOrderContentResult1.setFullName("前三");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata1116());
            cpOrderContentResult1.setOrderId("1116");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("顺子");
            cpOrderContentResult2.setFullName("前三");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata1117());
            cpOrderContentResult2.setOrderId("1117");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("对子");
            cpOrderContentResult3.setFullName("前三");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata1118());
            cpOrderContentResult3.setOrderId("1118");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("半顺");
            cpOrderContentResult4.setFullName("前三");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata1119());
            cpOrderContentResult4.setOrderId("1119");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult11 = new CPOrderContentListResult();
            cpOrderContentListResult11.setOrderContentListName("");
            cpOrderContentListResult11.setShowType("ZI");
            cpOrderContentListResult11.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList11 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("杂六");
            cpOrderContentResult5.setFullName("前三");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata1120());
            cpOrderContentResult5.setOrderId("1120");
            cpOrderContentResultList11.add(cpOrderContentResult5);

            cpOrderContentListResult11.setData(cpOrderContentResultList11);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("中三");
            cpOrderContentListResult2.setShowType("ZI");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("豹子");
            cpOrderContentResult21.setFullName("中三");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata1121());
            cpOrderContentResult21.setOrderId("1121");
            cpOrderContentResultList2.add(cpOrderContentResult21);


            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("顺子");
            cpOrderContentResult22.setFullName("中三");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata1122());
            cpOrderContentResult22.setOrderId("1122");
            cpOrderContentResultList2.add(cpOrderContentResult22);

            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("对子");
            cpOrderContentResult23.setFullName("中三");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata1123());
            cpOrderContentResult23.setOrderId("1123");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("半顺");
            cpOrderContentResult24.setFullName("中三");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata1124());
            cpOrderContentResult24.setOrderId("1124");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
            cpOrderContentListResult22.setOrderContentListName("");
            cpOrderContentListResult22.setShowType("ZI");
            cpOrderContentListResult22.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("杂六");
            cpOrderContentResult25.setFullName("中三");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata1125());
            cpOrderContentResult25.setOrderId("1125");
            cpOrderContentResultList22.add(cpOrderContentResult25);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            cpOrderContentListResult22.setData(cpOrderContentResultList22);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("后三");
            cpOrderContentListResult3.setShowType("ZI");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("豹子");
            cpOrderContentResult31.setFullName("后三");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata1126());
            cpOrderContentResult31.setOrderId("1126");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("顺子");
            cpOrderContentResult32.setFullName("后三");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata1127());
            cpOrderContentResult32.setOrderId("1127");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("对子");
            cpOrderContentResult33.setFullName("后三");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata1128());
            cpOrderContentResult33.setOrderId("1128");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("半顺");
            cpOrderContentResult34.setFullName("后三");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata1129());
            cpOrderContentResult34.setOrderId("1129");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
            cpOrderContentListResult33.setOrderContentListName("");
            cpOrderContentListResult33.setShowType("ZI");
            cpOrderContentListResult33.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("杂六");
            cpOrderContentResult35.setFullName("后三");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata1130());
            cpOrderContentResult35.setOrderId("1130");
            cpOrderContentResultList33.add(cpOrderContentResult35);

            cpOrderContentListResult33.setData(cpOrderContentResultList33);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult11);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult22);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult33);
            return cPOrderContentListResultAll;
        }
        return cPOrderContentListResultAll;
    }

    private List<CPOrderContentListResult>  CQSSC2(CQ2FCResult cpbjscResult,int index){
        List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
        if(index==0){
            for (int l = 0; l < 7; ++l) {
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                switch (l) {
                    case 0:
                        cpOrderContentListResult.setOrderContentListName("第一球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                        cpOrderContentResult0.setOrderName("大");
                        cpOrderContentResult0.setFullName("第一球");
                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata11405());
                        cpOrderContentResult0.setOrderId("1-1405");
                        cpOrderContentResultList.add(cpOrderContentResult0);

                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                        cpOrderContentResult1.setOrderName("小");
                        cpOrderContentResult1.setFullName("第一球");
                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata11406());
                        cpOrderContentResult1.setOrderId("1-1406");
                        cpOrderContentResultList.add(cpOrderContentResult1);

                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                        cpOrderContentResult2.setOrderName("单");
                        cpOrderContentResult2.setFullName("第一球");
                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata11407());
                        cpOrderContentResult2.setOrderId("1-1407");
                        cpOrderContentResultList.add(cpOrderContentResult2);

                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                        cpOrderContentResult3.setOrderName("双");
                        cpOrderContentResult3.setFullName("第一球");
                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata11408());
                        cpOrderContentResult3.setOrderId("1-1408");
                        cpOrderContentResultList.add(cpOrderContentResult3);
                        cpOrderContentListResult.setData(cpOrderContentResultList);

                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 1:
                        cpOrderContentListResult.setOrderContentListName("第二球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                        cpOrderContentResult20.setOrderName("大");
                        cpOrderContentResult20.setFullName("第二球");
                        cpOrderContentResult20.setOrderState(cpbjscResult.getdata21405());
                        cpOrderContentResult20.setOrderId("2-1405");
                        cpOrderContentResultList2.add(cpOrderContentResult20);

                        CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                        cpOrderContentResult21.setOrderName("小");
                        cpOrderContentResult21.setFullName("第二球");
                        cpOrderContentResult21.setOrderState(cpbjscResult.getdata21406());
                        cpOrderContentResult21.setOrderId("2-1406");
                        cpOrderContentResultList2.add(cpOrderContentResult21);

                        CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                        cpOrderContentResult22.setOrderName("单");
                        cpOrderContentResult22.setFullName("第二球");
                        cpOrderContentResult22.setOrderState(cpbjscResult.getdata21407());
                        cpOrderContentResult22.setOrderId("2-1407");
                        cpOrderContentResultList2.add(cpOrderContentResult22);

                        CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                        cpOrderContentResult23.setOrderName("双");
                        cpOrderContentResult23.setFullName("第二球");
                        cpOrderContentResult23.setOrderState(cpbjscResult.getdata21408());
                        cpOrderContentResult23.setOrderId("2-1408");
                        cpOrderContentResultList2.add(cpOrderContentResult23);

                        cpOrderContentListResult.setData(cpOrderContentResultList2);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 2:
                        cpOrderContentListResult.setOrderContentListName("第三球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                        cpOrderContentResult30.setOrderName("大");
                        cpOrderContentResult30.setFullName("第三球");
                        cpOrderContentResult30.setOrderState(cpbjscResult.getdata31405());
                        cpOrderContentResult30.setOrderId("3-1405");
                        cpOrderContentResultList3.add(cpOrderContentResult30);

                        CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                        cpOrderContentResult31.setOrderName("小");
                        cpOrderContentResult31.setFullName("第三球");
                        cpOrderContentResult31.setOrderState(cpbjscResult.getdata31406());
                        cpOrderContentResult31.setOrderId("3-1406");
                        cpOrderContentResultList3.add(cpOrderContentResult31);

                        CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                        cpOrderContentResult32.setOrderName("单");
                        cpOrderContentResult32.setFullName("第三球");
                        cpOrderContentResult32.setOrderState(cpbjscResult.getdata31407());
                        cpOrderContentResult32.setOrderId("3-1407");
                        cpOrderContentResultList3.add(cpOrderContentResult32);

                        CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                        cpOrderContentResult33.setOrderName("双");
                        cpOrderContentResult33.setFullName("第三球");
                        cpOrderContentResult33.setOrderState(cpbjscResult.getdata31408());
                        cpOrderContentResult33.setOrderId("3-1408");
                        cpOrderContentResultList3.add(cpOrderContentResult33);

                        cpOrderContentListResult.setData(cpOrderContentResultList3);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);

                        break;
                    case 3:
                        cpOrderContentListResult.setOrderContentListName("第四球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                        cpOrderContentResult40.setOrderName("大");
                        cpOrderContentResult40.setFullName("第四球");
                        cpOrderContentResult40.setOrderState(cpbjscResult.getdata41405());
                        cpOrderContentResult40.setOrderId("4-1405");
                        cpOrderContentResultList4.add(cpOrderContentResult40);

                        CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                        cpOrderContentResult41.setOrderName("小");
                        cpOrderContentResult41.setFullName("第四球");
                        cpOrderContentResult41.setOrderState(cpbjscResult.getdata41406());
                        cpOrderContentResult41.setOrderId("4-1406");
                        cpOrderContentResultList4.add(cpOrderContentResult41);

                        CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                        cpOrderContentResult42.setOrderName("单");
                        cpOrderContentResult42.setFullName("第四球");
                        cpOrderContentResult42.setOrderState(cpbjscResult.getdata41407());
                        cpOrderContentResult42.setOrderId("4-1407");
                        cpOrderContentResultList4.add(cpOrderContentResult42);

                        CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                        cpOrderContentResult43.setOrderName("双");
                        cpOrderContentResult43.setFullName("第四球");
                        cpOrderContentResult43.setOrderState(cpbjscResult.getdata41408());
                        cpOrderContentResult43.setOrderId("4-1408");
                        cpOrderContentResultList4.add(cpOrderContentResult43);

                        cpOrderContentListResult.setData(cpOrderContentResultList4);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 4:
                        cpOrderContentListResult.setOrderContentListName("第五球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                        cpOrderContentResult50.setOrderName("大");
                        cpOrderContentResult50.setFullName("第五球");
                        cpOrderContentResult50.setOrderState(cpbjscResult.getdata51405());
                        cpOrderContentResult50.setOrderId("5-1405");
                        cpOrderContentResultList5.add(cpOrderContentResult50);

                        CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                        cpOrderContentResult51.setOrderName("小");
                        cpOrderContentResult51.setFullName("第五球");
                        cpOrderContentResult51.setOrderState(cpbjscResult.getdata51406());
                        cpOrderContentResult51.setOrderId("5-1406");
                        cpOrderContentResultList5.add(cpOrderContentResult51);

                        CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                        cpOrderContentResult52.setOrderName("单");
                        cpOrderContentResult52.setFullName("第五球");
                        cpOrderContentResult52.setOrderState(cpbjscResult.getdata51407());
                        cpOrderContentResult52.setOrderId("5-1407");
                        cpOrderContentResultList5.add(cpOrderContentResult52);

                        CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                        cpOrderContentResult53.setOrderName("双");
                        cpOrderContentResult53.setFullName("第五球");
                        cpOrderContentResult53.setOrderState(cpbjscResult.getdata51408());
                        cpOrderContentResult53.setOrderId("5-1408");
                        cpOrderContentResultList5.add(cpOrderContentResult53);

                        cpOrderContentListResult.setData(cpOrderContentResultList5);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 5:
                        cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                        cpOrderContentResult60.setOrderName("总和大");
                        cpOrderContentResult60.setFullName("");
                        cpOrderContentResult60.setOrderState(cpbjscResult.getdata1409());
                        cpOrderContentResult60.setOrderId("1409");
                        cpOrderContentResultList6.add(cpOrderContentResult60);

                        CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                        cpOrderContentResult61.setOrderName("总和小");
                        cpOrderContentResult61.setFullName("");
                        cpOrderContentResult61.setOrderState(cpbjscResult.getdata1410());
                        cpOrderContentResult61.setOrderId("1410");
                        cpOrderContentResultList6.add(cpOrderContentResult61);

                        CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                        cpOrderContentResult62.setOrderName("总和单");
                        cpOrderContentResult62.setFullName("");
                        cpOrderContentResult62.setOrderState(cpbjscResult.getdata1411());
                        cpOrderContentResult62.setOrderId("1411");
                        cpOrderContentResultList6.add(cpOrderContentResult62);

                        CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                        cpOrderContentResult63.setOrderName("总和双");
                        cpOrderContentResult63.setFullName("");
                        cpOrderContentResult63.setOrderState(cpbjscResult.getdata1412());
                        cpOrderContentResult63.setOrderId("1412");
                        cpOrderContentResultList6.add(cpOrderContentResult63);

                        cpOrderContentListResult.setData(cpOrderContentResultList6);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 6:
                        cpOrderContentListResult.setOrderContentListName("");
                        cpOrderContentListResult.setShowNumber(3);


                        List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                        cpOrderContentResult70.setOrderName("龙");
                        cpOrderContentResult70.setFullName("");
                        cpOrderContentResult70.setOrderState(cpbjscResult.getdata1413());
                        cpOrderContentResult70.setOrderId("1413");
                        cpOrderContentResultList7.add(cpOrderContentResult70);

                        CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                        cpOrderContentResult71.setOrderName("虎");
                        cpOrderContentResult71.setFullName("");
                        cpOrderContentResult71.setOrderState(cpbjscResult.getdata1414());
                        cpOrderContentResult71.setOrderId("1414");
                        cpOrderContentResultList7.add(cpOrderContentResult71);

                        CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                        cpOrderContentResult72.setOrderName("和");
                        cpOrderContentResult72.setFullName("");
                        cpOrderContentResult72.setOrderState(cpbjscResult.getdata1415());
                        cpOrderContentResult72.setOrderId("1415");
                        cpOrderContentResultList7.add(cpOrderContentResult72);

                        cpOrderContentListResult.setData(cpOrderContentResultList7);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                }
            }
            return cPOrderContentListResultAll;
        }else if(index==1){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("第一球");
            cpOrderContentListResult.setShowNumber(2);
            cpOrderContentListResult.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
            cpOrderContentResult0.setOrderName("0");
            cpOrderContentResult0.setFullName("第一球");
            cpOrderContentResult0.setOrderState(cpbjscResult.getdata14000());
            cpOrderContentResult0.setOrderId("1400-0");
            cpOrderContentResultList.add(cpOrderContentResult0);

            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("第一球");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata14001());
            cpOrderContentResult1.setOrderId("1400-1");
            cpOrderContentResultList.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("第一球");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata14002());
            cpOrderContentResult2.setOrderId("1400-2");
            cpOrderContentResultList.add(cpOrderContentResult2);

            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("第一球");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata14003());
            cpOrderContentResult3.setOrderId("1400-3");
            cpOrderContentResultList.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("第一球");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata14004());
            cpOrderContentResult4.setOrderId("1400-4");
            cpOrderContentResultList.add(cpOrderContentResult4);


            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("第一球");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata14005());
            cpOrderContentResult5.setOrderId("1400-5");
            cpOrderContentResultList.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("第一球");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata14006());
            cpOrderContentResult6.setOrderId("1400-6");
            cpOrderContentResultList.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("第一球");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata14007());
            cpOrderContentResult7.setOrderId("1400-7");
            cpOrderContentResultList.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("第一球");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata14008());
            cpOrderContentResult8.setOrderId("1400-8");
            cpOrderContentResultList.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("第一球");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata14009());
            cpOrderContentResult9.setOrderId("1400-9");
            cpOrderContentResultList.add(cpOrderContentResult9);

            cpOrderContentListResult.setData(cpOrderContentResultList);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("第二球");
            cpOrderContentListResult2.setShowNumber(2);
            cpOrderContentListResult2.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
            cpOrderContentResult20.setOrderName("0");
            cpOrderContentResult20.setFullName("第二球");
            cpOrderContentResult20.setOrderState(cpbjscResult.getdata14010());
            cpOrderContentResult20.setOrderId("1401-0");
            cpOrderContentResultList2.add(cpOrderContentResult20);

            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("第二球");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata14011());
            cpOrderContentResult21.setOrderId("1401-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("第二球");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata14012());
            cpOrderContentResult22.setOrderId("1401-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("第二球");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata14013());
            cpOrderContentResult23.setOrderId("1401-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);


            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("第二球");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata14014());
            cpOrderContentResult24.setOrderId("1401-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("第二球");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata14015());
            cpOrderContentResult25.setOrderId("1401-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);


            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("第二球");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata14016());
            cpOrderContentResult26.setOrderId("1401-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);


            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("第二球");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata14017());
            cpOrderContentResult27.setOrderId("1401-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);


            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("第二球");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata14018());
            cpOrderContentResult28.setOrderId("1401-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);


            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("第二球");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata14019());
            cpOrderContentResult29.setOrderId("1401-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);


            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第三球");
            cpOrderContentListResult3.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
            cpOrderContentResult30.setOrderName("0");
            cpOrderContentResult30.setFullName("第三球");
            cpOrderContentResult30.setOrderState(cpbjscResult.getdata14020());
            cpOrderContentResult30.setOrderId("1402-0");
            cpOrderContentResultList3.add(cpOrderContentResult30);

            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第三球");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata14021());
            cpOrderContentResult31.setOrderId("1402-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第三球");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata14022());
            cpOrderContentResult32.setOrderId("1402-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第三球");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata14023());
            cpOrderContentResult33.setOrderId("1402-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第三球");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata14024());
            cpOrderContentResult34.setOrderId("1402-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第三球");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata14025());
            cpOrderContentResult35.setOrderId("1402-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);


            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第三球");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata14026());
            cpOrderContentResult36.setOrderId("1402-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第三球");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata14027());
            cpOrderContentResult37.setOrderId("1402-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第三球");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata14028());
            cpOrderContentResult38.setOrderId("1402-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第三球");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata14029());
            cpOrderContentResult39.setOrderId("1402-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第四球");
            cpOrderContentListResult4.setShowNumber(2);
            cpOrderContentListResult4.setShowType("QIU");
            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
            cpOrderContentResult40.setOrderName("0");
            cpOrderContentResult40.setFullName("第四球");
            cpOrderContentResult40.setOrderState(cpbjscResult.getdata14030());
            cpOrderContentResult40.setOrderId("1403-0");
            cpOrderContentResultList4.add(cpOrderContentResult40);

            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第四球");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata14031());
            cpOrderContentResult41.setOrderId("1403-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第四球");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata14032());
            cpOrderContentResult42.setOrderId("1403-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);

            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第四球");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata14033());
            cpOrderContentResult43.setOrderId("1403-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第四球");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata14034());
            cpOrderContentResult44.setOrderId("1403-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第四球");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata14035());
            cpOrderContentResult45.setOrderId("1403-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第四球");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata14036());
            cpOrderContentResult46.setOrderId("1403-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第四球");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata14037());
            cpOrderContentResult47.setOrderId("1403-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第四球");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata14038());
            cpOrderContentResult48.setOrderId("1403-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第四球");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata14039());
            cpOrderContentResult49.setOrderId("1403-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);


            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第五球");
            cpOrderContentListResult5.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
            cpOrderContentResult50.setOrderName("0");
            cpOrderContentResult50.setFullName("第五球");
            cpOrderContentResult50.setOrderState(cpbjscResult.getdata14040());
            cpOrderContentResult50.setOrderId("1404-0");
            cpOrderContentResultList5.add(cpOrderContentResult50);

            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第五球");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata14041());
            cpOrderContentResult51.setOrderId("1404-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第五球");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata14042());
            cpOrderContentResult52.setOrderId("1404-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);

            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第五球");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata14043());
            cpOrderContentResult53.setOrderId("1404-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第五球");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata14044());
            cpOrderContentResult54.setOrderId("1404-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第五球");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata14045());
            cpOrderContentResult55.setOrderId("1404-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第五球");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata14046());
            cpOrderContentResult56.setOrderId("1404-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第五球");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata14047());
            cpOrderContentResult57.setOrderId("1404-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第五球");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata14048());
            cpOrderContentResult58.setOrderId("1404-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第五球");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata14049());
            cpOrderContentResult59.setOrderId("1404-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult4);
            cPOrderContentListResultAll.add(cpOrderContentListResult5);

            return cPOrderContentListResultAll;
        }else if(index==2){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("前三");
            cpOrderContentListResult.setShowType("ZI");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("豹子");
            cpOrderContentResult1.setFullName("前三");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata1416());
            cpOrderContentResult1.setOrderId("1416");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("顺子");
            cpOrderContentResult2.setFullName("前三");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata1417());
            cpOrderContentResult2.setOrderId("1417");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("对子");
            cpOrderContentResult3.setFullName("前三");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata1418());
            cpOrderContentResult3.setOrderId("1418");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("半顺");
            cpOrderContentResult4.setFullName("前三");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata1419());
            cpOrderContentResult4.setOrderId("1419");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult14 = new CPOrderContentListResult();
            cpOrderContentListResult14.setOrderContentListName("");
            cpOrderContentListResult14.setShowType("ZI");
            cpOrderContentListResult14.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList14 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("杂六");
            cpOrderContentResult5.setFullName("前三");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata1420());
            cpOrderContentResult5.setOrderId("1420");
            cpOrderContentResultList14.add(cpOrderContentResult5);

            cpOrderContentListResult14.setData(cpOrderContentResultList14);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("中三");
            cpOrderContentListResult2.setShowType("ZI");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("豹子");
            cpOrderContentResult21.setFullName("中三");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata1421());
            cpOrderContentResult21.setOrderId("1421");
            cpOrderContentResultList2.add(cpOrderContentResult21);


            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("顺子");
            cpOrderContentResult22.setFullName("中三");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata1422());
            cpOrderContentResult22.setOrderId("1422");
            cpOrderContentResultList2.add(cpOrderContentResult22);

            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("对子");
            cpOrderContentResult23.setFullName("中三");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata1423());
            cpOrderContentResult23.setOrderId("1423");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("半顺");
            cpOrderContentResult24.setFullName("中三");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata1424());
            cpOrderContentResult24.setOrderId("1424");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
            cpOrderContentListResult22.setOrderContentListName("");
            cpOrderContentListResult22.setShowType("ZI");
            cpOrderContentListResult22.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("杂六");
            cpOrderContentResult25.setFullName("中三");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata1425());
            cpOrderContentResult25.setOrderId("1425");
            cpOrderContentResultList22.add(cpOrderContentResult25);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            cpOrderContentListResult22.setData(cpOrderContentResultList22);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("后三");
            cpOrderContentListResult3.setShowType("ZI");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("豹子");
            cpOrderContentResult31.setFullName("后三");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata1426());
            cpOrderContentResult31.setOrderId("1426");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("顺子");
            cpOrderContentResult32.setFullName("后三");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata1427());
            cpOrderContentResult32.setOrderId("1427");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("对子");
            cpOrderContentResult33.setFullName("后三");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata1428());
            cpOrderContentResult33.setOrderId("1428");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("半顺");
            cpOrderContentResult34.setFullName("后三");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata1429());
            cpOrderContentResult34.setOrderId("1429");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
            cpOrderContentListResult33.setOrderContentListName("");
            cpOrderContentListResult33.setShowType("ZI");
            cpOrderContentListResult33.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("杂六");
            cpOrderContentResult35.setFullName("后三");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata1430());
            cpOrderContentResult35.setOrderId("1430");
            cpOrderContentResultList33.add(cpOrderContentResult35);

            cpOrderContentListResult33.setData(cpOrderContentResultList33);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult14);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult22);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult33);
            return cPOrderContentListResultAll;
        }
        return cPOrderContentListResultAll;
    }

    private List<CPOrderContentListResult>  CQSSC3(CQ3FCResult cpbjscResult,int index){
        List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
        if(index==0){
            for (int l = 0; l < 7; ++l) {
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                switch (l) {
                    case 0:
                        cpOrderContentListResult.setOrderContentListName("第一球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                        cpOrderContentResult0.setOrderName("大");
                        cpOrderContentResult0.setFullName("第一球");
                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata11205());
                        cpOrderContentResult0.setOrderId("1-1205");
                        cpOrderContentResultList.add(cpOrderContentResult0);

                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                        cpOrderContentResult1.setOrderName("小");
                        cpOrderContentResult1.setFullName("第一球");
                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata11206());
                        cpOrderContentResult1.setOrderId("1-1206");
                        cpOrderContentResultList.add(cpOrderContentResult1);

                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                        cpOrderContentResult2.setOrderName("单");
                        cpOrderContentResult2.setFullName("第一球");
                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata11207());
                        cpOrderContentResult2.setOrderId("1-1207");
                        cpOrderContentResultList.add(cpOrderContentResult2);

                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                        cpOrderContentResult3.setOrderName("双");
                        cpOrderContentResult3.setFullName("第一球");
                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata11208());
                        cpOrderContentResult3.setOrderId("1-1208");
                        cpOrderContentResultList.add(cpOrderContentResult3);
                        cpOrderContentListResult.setData(cpOrderContentResultList);

                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 1:
                        cpOrderContentListResult.setOrderContentListName("第二球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                        cpOrderContentResult20.setOrderName("大");
                        cpOrderContentResult20.setFullName("第二球");
                        cpOrderContentResult20.setOrderState(cpbjscResult.getdata21205());
                        cpOrderContentResult20.setOrderId("2-1205");
                        cpOrderContentResultList2.add(cpOrderContentResult20);

                        CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                        cpOrderContentResult21.setOrderName("小");
                        cpOrderContentResult21.setFullName("第二球");
                        cpOrderContentResult21.setOrderState(cpbjscResult.getdata21206());
                        cpOrderContentResult21.setOrderId("2-1206");
                        cpOrderContentResultList2.add(cpOrderContentResult21);

                        CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                        cpOrderContentResult22.setOrderName("单");
                        cpOrderContentResult22.setFullName("第二球");
                        cpOrderContentResult22.setOrderState(cpbjscResult.getdata21207());
                        cpOrderContentResult22.setOrderId("2-1207");
                        cpOrderContentResultList2.add(cpOrderContentResult22);

                        CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                        cpOrderContentResult23.setOrderName("双");
                        cpOrderContentResult23.setFullName("第二球");
                        cpOrderContentResult23.setOrderState(cpbjscResult.getdata21208());
                        cpOrderContentResult23.setOrderId("2-1208");
                        cpOrderContentResultList2.add(cpOrderContentResult23);

                        cpOrderContentListResult.setData(cpOrderContentResultList2);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 2:
                        cpOrderContentListResult.setOrderContentListName("第三球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                        cpOrderContentResult30.setOrderName("大");
                        cpOrderContentResult30.setFullName("第三球");
                        cpOrderContentResult30.setOrderState(cpbjscResult.getdata31205());
                        cpOrderContentResult30.setOrderId("3-1205");
                        cpOrderContentResultList3.add(cpOrderContentResult30);

                        CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                        cpOrderContentResult31.setOrderName("小");
                        cpOrderContentResult31.setFullName("第三球");
                        cpOrderContentResult31.setOrderState(cpbjscResult.getdata31206());
                        cpOrderContentResult31.setOrderId("3-1206");
                        cpOrderContentResultList3.add(cpOrderContentResult31);

                        CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                        cpOrderContentResult32.setOrderName("单");
                        cpOrderContentResult32.setFullName("第三球");
                        cpOrderContentResult32.setOrderState(cpbjscResult.getdata31207());
                        cpOrderContentResult32.setOrderId("3-1207");
                        cpOrderContentResultList3.add(cpOrderContentResult32);

                        CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                        cpOrderContentResult33.setOrderName("双");
                        cpOrderContentResult33.setFullName("第三球");
                        cpOrderContentResult33.setOrderState(cpbjscResult.getdata31208());
                        cpOrderContentResult33.setOrderId("3-1208");
                        cpOrderContentResultList3.add(cpOrderContentResult33);

                        cpOrderContentListResult.setData(cpOrderContentResultList3);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);

                        break;
                    case 3:
                        cpOrderContentListResult.setOrderContentListName("第四球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                        cpOrderContentResult40.setOrderName("大");
                        cpOrderContentResult40.setFullName("第四球");
                        cpOrderContentResult40.setOrderState(cpbjscResult.getdata41205());
                        cpOrderContentResult40.setOrderId("4-1205");
                        cpOrderContentResultList4.add(cpOrderContentResult40);

                        CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                        cpOrderContentResult41.setOrderName("小");
                        cpOrderContentResult41.setFullName("第四球");
                        cpOrderContentResult41.setOrderState(cpbjscResult.getdata41206());
                        cpOrderContentResult41.setOrderId("4-1206");
                        cpOrderContentResultList4.add(cpOrderContentResult41);

                        CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                        cpOrderContentResult42.setOrderName("单");
                        cpOrderContentResult42.setFullName("第四球");
                        cpOrderContentResult42.setOrderState(cpbjscResult.getdata41207());
                        cpOrderContentResult42.setOrderId("4-1207");
                        cpOrderContentResultList4.add(cpOrderContentResult42);

                        CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                        cpOrderContentResult43.setOrderName("双");
                        cpOrderContentResult43.setFullName("第四球");
                        cpOrderContentResult43.setOrderState(cpbjscResult.getdata41208());
                        cpOrderContentResult43.setOrderId("4-1208");
                        cpOrderContentResultList4.add(cpOrderContentResult43);

                        cpOrderContentListResult.setData(cpOrderContentResultList4);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 4:
                        cpOrderContentListResult.setOrderContentListName("第五球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                        cpOrderContentResult50.setOrderName("大");
                        cpOrderContentResult50.setFullName("第五球");
                        cpOrderContentResult50.setOrderState(cpbjscResult.getdata51205());
                        cpOrderContentResult50.setOrderId("5-1205");
                        cpOrderContentResultList5.add(cpOrderContentResult50);

                        CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                        cpOrderContentResult51.setOrderName("小");
                        cpOrderContentResult51.setFullName("第五球");
                        cpOrderContentResult51.setOrderState(cpbjscResult.getdata51206());
                        cpOrderContentResult51.setOrderId("5-1206");
                        cpOrderContentResultList5.add(cpOrderContentResult51);

                        CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                        cpOrderContentResult52.setOrderName("单");
                        cpOrderContentResult52.setFullName("第五球");
                        cpOrderContentResult52.setOrderState(cpbjscResult.getdata51207());
                        cpOrderContentResult52.setOrderId("5-1207");
                        cpOrderContentResultList5.add(cpOrderContentResult52);

                        CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                        cpOrderContentResult53.setOrderName("双");
                        cpOrderContentResult53.setFullName("第五球");
                        cpOrderContentResult53.setOrderState(cpbjscResult.getdata51208());
                        cpOrderContentResult53.setOrderId("5-1208");
                        cpOrderContentResultList5.add(cpOrderContentResult53);

                        cpOrderContentListResult.setData(cpOrderContentResultList5);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 5:
                        cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                        cpOrderContentResult60.setOrderName("总和大");
                        cpOrderContentResult60.setFullName("");
                        cpOrderContentResult60.setOrderState(cpbjscResult.getdata1209());
                        cpOrderContentResult60.setOrderId("1209");
                        cpOrderContentResultList6.add(cpOrderContentResult60);

                        CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                        cpOrderContentResult61.setOrderName("总和小");
                        cpOrderContentResult61.setFullName("");
                        cpOrderContentResult61.setOrderState(cpbjscResult.getdata1210());
                        cpOrderContentResult61.setOrderId("1210");
                        cpOrderContentResultList6.add(cpOrderContentResult61);

                        CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                        cpOrderContentResult62.setOrderName("总和单");
                        cpOrderContentResult62.setFullName("");
                        cpOrderContentResult62.setOrderState(cpbjscResult.getdata1211());
                        cpOrderContentResult62.setOrderId("1211");
                        cpOrderContentResultList6.add(cpOrderContentResult62);

                        CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                        cpOrderContentResult63.setOrderName("总和双");
                        cpOrderContentResult63.setFullName("");
                        cpOrderContentResult63.setOrderState(cpbjscResult.getdata1212());
                        cpOrderContentResult63.setOrderId("1212");
                        cpOrderContentResultList6.add(cpOrderContentResult63);

                        cpOrderContentListResult.setData(cpOrderContentResultList6);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 6:
                        cpOrderContentListResult.setOrderContentListName("");
                        cpOrderContentListResult.setShowNumber(3);


                        List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                        cpOrderContentResult70.setOrderName("龙");
                        cpOrderContentResult70.setFullName("");
                        cpOrderContentResult70.setOrderState(cpbjscResult.getdata1213());
                        cpOrderContentResult70.setOrderId("1213");
                        cpOrderContentResultList7.add(cpOrderContentResult70);

                        CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                        cpOrderContentResult71.setOrderName("虎");
                        cpOrderContentResult71.setFullName("");
                        cpOrderContentResult71.setOrderState(cpbjscResult.getdata1214());
                        cpOrderContentResult71.setOrderId("1214");
                        cpOrderContentResultList7.add(cpOrderContentResult71);

                        CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                        cpOrderContentResult72.setOrderName("和");
                        cpOrderContentResult72.setFullName("");
                        cpOrderContentResult72.setOrderState(cpbjscResult.getdata1215());
                        cpOrderContentResult72.setOrderId("1215");
                        cpOrderContentResultList7.add(cpOrderContentResult72);

                        cpOrderContentListResult.setData(cpOrderContentResultList7);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                }
            }
            return cPOrderContentListResultAll;
        }else if(index==1){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("第一球");
            cpOrderContentListResult.setShowNumber(2);
            cpOrderContentListResult.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
            cpOrderContentResult0.setOrderName("0");
            cpOrderContentResult0.setFullName("第一球");
            cpOrderContentResult0.setOrderState(cpbjscResult.getdata12000());
            cpOrderContentResult0.setOrderId("1200-0");
            cpOrderContentResultList.add(cpOrderContentResult0);

            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("第一球");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata12001());
            cpOrderContentResult1.setOrderId("1200-1");
            cpOrderContentResultList.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("第一球");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata12002());
            cpOrderContentResult2.setOrderId("1200-2");
            cpOrderContentResultList.add(cpOrderContentResult2);

            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("第一球");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata12003());
            cpOrderContentResult3.setOrderId("1200-3");
            cpOrderContentResultList.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("第一球");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata12004());
            cpOrderContentResult4.setOrderId("1200-4");
            cpOrderContentResultList.add(cpOrderContentResult4);


            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("第一球");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata12005());
            cpOrderContentResult5.setOrderId("1200-5");
            cpOrderContentResultList.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("第一球");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata12006());
            cpOrderContentResult6.setOrderId("1200-6");
            cpOrderContentResultList.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("第一球");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata12007());
            cpOrderContentResult7.setOrderId("1200-7");
            cpOrderContentResultList.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("第一球");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata12008());
            cpOrderContentResult8.setOrderId("1200-8");
            cpOrderContentResultList.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("第一球");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata12009());
            cpOrderContentResult9.setOrderId("1200-9");
            cpOrderContentResultList.add(cpOrderContentResult9);

            cpOrderContentListResult.setData(cpOrderContentResultList);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("第二球");
            cpOrderContentListResult2.setShowNumber(2);
            cpOrderContentListResult2.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
            cpOrderContentResult20.setOrderName("0");
            cpOrderContentResult20.setFullName("第二球");
            cpOrderContentResult20.setOrderState(cpbjscResult.getdata12010());
            cpOrderContentResult20.setOrderId("1201-0");
            cpOrderContentResultList2.add(cpOrderContentResult20);

            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("第二球");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata12011());
            cpOrderContentResult21.setOrderId("1201-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("第二球");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata12012());
            cpOrderContentResult22.setOrderId("1201-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("第二球");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata12013());
            cpOrderContentResult23.setOrderId("1201-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);


            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("第二球");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata12014());
            cpOrderContentResult24.setOrderId("1201-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("第二球");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata12015());
            cpOrderContentResult25.setOrderId("1201-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);


            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("第二球");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata12016());
            cpOrderContentResult26.setOrderId("1201-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);


            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("第二球");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata12017());
            cpOrderContentResult27.setOrderId("1201-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);


            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("第二球");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata12018());
            cpOrderContentResult28.setOrderId("1201-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);


            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("第二球");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata12019());
            cpOrderContentResult29.setOrderId("1201-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);


            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第三球");
            cpOrderContentListResult3.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
            cpOrderContentResult30.setOrderName("0");
            cpOrderContentResult30.setFullName("第三球");
            cpOrderContentResult30.setOrderState(cpbjscResult.getdata12020());
            cpOrderContentResult30.setOrderId("1202-0");
            cpOrderContentResultList3.add(cpOrderContentResult30);

            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第三球");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata12021());
            cpOrderContentResult31.setOrderId("1202-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第三球");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata12022());
            cpOrderContentResult32.setOrderId("1202-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第三球");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata12023());
            cpOrderContentResult33.setOrderId("1202-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第三球");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata12024());
            cpOrderContentResult34.setOrderId("1202-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第三球");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata12025());
            cpOrderContentResult35.setOrderId("1202-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);


            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第三球");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata12026());
            cpOrderContentResult36.setOrderId("1202-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第三球");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata12027());
            cpOrderContentResult37.setOrderId("1202-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第三球");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata12028());
            cpOrderContentResult38.setOrderId("1202-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第三球");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata12029());
            cpOrderContentResult39.setOrderId("1202-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第四球");
            cpOrderContentListResult4.setShowNumber(2);
            cpOrderContentListResult4.setShowType("QIU");
            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
            cpOrderContentResult40.setOrderName("0");
            cpOrderContentResult40.setFullName("第四球");
            cpOrderContentResult40.setOrderState(cpbjscResult.getdata12030());
            cpOrderContentResult40.setOrderId("1203-0");
            cpOrderContentResultList4.add(cpOrderContentResult40);

            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第四球");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata12031());
            cpOrderContentResult41.setOrderId("1203-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第四球");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata12032());
            cpOrderContentResult42.setOrderId("1203-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);

            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第四球");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata12033());
            cpOrderContentResult43.setOrderId("1203-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第四球");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata12034());
            cpOrderContentResult44.setOrderId("1203-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第四球");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata12035());
            cpOrderContentResult45.setOrderId("1203-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第四球");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata12036());
            cpOrderContentResult46.setOrderId("1203-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第四球");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata12037());
            cpOrderContentResult47.setOrderId("1203-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第四球");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata12038());
            cpOrderContentResult48.setOrderId("1203-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第四球");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata12039());
            cpOrderContentResult49.setOrderId("1203-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);


            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第五球");
            cpOrderContentListResult5.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
            cpOrderContentResult50.setOrderName("0");
            cpOrderContentResult50.setFullName("第五球");
            cpOrderContentResult50.setOrderState(cpbjscResult.getdata12040());
            cpOrderContentResult50.setOrderId("1204-0");
            cpOrderContentResultList5.add(cpOrderContentResult50);

            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第五球");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata12041());
            cpOrderContentResult51.setOrderId("1204-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第五球");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata12042());
            cpOrderContentResult52.setOrderId("1204-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);

            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第五球");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata12043());
            cpOrderContentResult53.setOrderId("1204-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第五球");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata12044());
            cpOrderContentResult54.setOrderId("1204-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第五球");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata12045());
            cpOrderContentResult55.setOrderId("1204-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第五球");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata12046());
            cpOrderContentResult56.setOrderId("1204-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第五球");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata12047());
            cpOrderContentResult57.setOrderId("1204-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第五球");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata12048());
            cpOrderContentResult58.setOrderId("1204-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第五球");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata12049());
            cpOrderContentResult59.setOrderId("1204-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult4);
            cPOrderContentListResultAll.add(cpOrderContentListResult5);

            return cPOrderContentListResultAll;
        }else if(index==2){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("前三");
            cpOrderContentListResult.setShowType("ZI");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("豹子");
            cpOrderContentResult1.setFullName("前三");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata1216());
            cpOrderContentResult1.setOrderId("1216");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("顺子");
            cpOrderContentResult2.setFullName("前三");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata1217());
            cpOrderContentResult2.setOrderId("1217");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("对子");
            cpOrderContentResult3.setFullName("前三");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata1218());
            cpOrderContentResult3.setOrderId("1218");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("半顺");
            cpOrderContentResult4.setFullName("前三");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata1219());
            cpOrderContentResult4.setOrderId("1219");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult12 = new CPOrderContentListResult();
            cpOrderContentListResult12.setOrderContentListName("");
            cpOrderContentListResult12.setShowType("ZI");
            cpOrderContentListResult12.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList12 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("杂六");
            cpOrderContentResult5.setFullName("前三");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata1220());
            cpOrderContentResult5.setOrderId("1220");
            cpOrderContentResultList12.add(cpOrderContentResult5);

            cpOrderContentListResult12.setData(cpOrderContentResultList12);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("中三");
            cpOrderContentListResult2.setShowType("ZI");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("豹子");
            cpOrderContentResult21.setFullName("中三");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata1221());
            cpOrderContentResult21.setOrderId("1221");
            cpOrderContentResultList2.add(cpOrderContentResult21);


            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("顺子");
            cpOrderContentResult22.setFullName("中三");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata1222());
            cpOrderContentResult22.setOrderId("1222");
            cpOrderContentResultList2.add(cpOrderContentResult22);

            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("对子");
            cpOrderContentResult23.setFullName("中三");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata1223());
            cpOrderContentResult23.setOrderId("1223");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("半顺");
            cpOrderContentResult24.setFullName("中三");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata1224());
            cpOrderContentResult24.setOrderId("1224");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
            cpOrderContentListResult22.setOrderContentListName("");
            cpOrderContentListResult22.setShowType("ZI");
            cpOrderContentListResult22.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("杂六");
            cpOrderContentResult25.setFullName("中三");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata1225());
            cpOrderContentResult25.setOrderId("1225");
            cpOrderContentResultList22.add(cpOrderContentResult25);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            cpOrderContentListResult22.setData(cpOrderContentResultList22);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("后三");
            cpOrderContentListResult3.setShowType("ZI");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("豹子");
            cpOrderContentResult31.setFullName("后三");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata1226());
            cpOrderContentResult31.setOrderId("1226");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("顺子");
            cpOrderContentResult32.setFullName("后三");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata1227());
            cpOrderContentResult32.setOrderId("1227");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("对子");
            cpOrderContentResult33.setFullName("后三");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata1228());
            cpOrderContentResult33.setOrderId("1228");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("半顺");
            cpOrderContentResult34.setFullName("后三");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata1229());
            cpOrderContentResult34.setOrderId("1229");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
            cpOrderContentListResult33.setOrderContentListName("");
            cpOrderContentListResult33.setShowType("ZI");
            cpOrderContentListResult33.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("杂六");
            cpOrderContentResult35.setFullName("后三");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata1230());
            cpOrderContentResult35.setOrderId("1230");
            cpOrderContentResultList33.add(cpOrderContentResult35);

            cpOrderContentListResult33.setData(cpOrderContentResultList33);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult12);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult22);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult33);
            return cPOrderContentListResultAll;
        }
        return cPOrderContentListResultAll;
    }

    private List<CPOrderContentListResult>  CQSSC5(CQ5FCResult cpbjscResult,int index){
        List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
        if(index==0){
            for (int l = 0; l < 7; ++l) {
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                switch (l) {
                    case 0:
                        cpOrderContentListResult.setOrderContentListName("第一球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                        cpOrderContentResult0.setOrderName("大");
                        cpOrderContentResult0.setFullName("第一球");
                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata11305());
                        cpOrderContentResult0.setOrderId("1-1305");
                        cpOrderContentResultList.add(cpOrderContentResult0);

                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                        cpOrderContentResult1.setOrderName("小");
                        cpOrderContentResult1.setFullName("第一球");
                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata11306());
                        cpOrderContentResult1.setOrderId("1-1306");
                        cpOrderContentResultList.add(cpOrderContentResult1);

                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                        cpOrderContentResult2.setOrderName("单");
                        cpOrderContentResult2.setFullName("第一球");
                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata11307());
                        cpOrderContentResult2.setOrderId("1-1307");
                        cpOrderContentResultList.add(cpOrderContentResult2);

                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                        cpOrderContentResult3.setOrderName("双");
                        cpOrderContentResult3.setFullName("第一球");
                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata11308());
                        cpOrderContentResult3.setOrderId("1-1308");
                        cpOrderContentResultList.add(cpOrderContentResult3);
                        cpOrderContentListResult.setData(cpOrderContentResultList);

                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 1:
                        cpOrderContentListResult.setOrderContentListName("第二球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                        cpOrderContentResult20.setOrderName("大");
                        cpOrderContentResult20.setFullName("第二球");
                        cpOrderContentResult20.setOrderState(cpbjscResult.getdata21305());
                        cpOrderContentResult20.setOrderId("2-1305");
                        cpOrderContentResultList2.add(cpOrderContentResult20);

                        CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                        cpOrderContentResult21.setOrderName("小");
                        cpOrderContentResult21.setFullName("第二球");
                        cpOrderContentResult21.setOrderState(cpbjscResult.getdata21306());
                        cpOrderContentResult21.setOrderId("2-1306");
                        cpOrderContentResultList2.add(cpOrderContentResult21);

                        CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                        cpOrderContentResult22.setOrderName("单");
                        cpOrderContentResult22.setFullName("第二球");
                        cpOrderContentResult22.setOrderState(cpbjscResult.getdata21307());
                        cpOrderContentResult22.setOrderId("2-1307");
                        cpOrderContentResultList2.add(cpOrderContentResult22);

                        CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                        cpOrderContentResult23.setOrderName("双");
                        cpOrderContentResult23.setFullName("第二球");
                        cpOrderContentResult23.setOrderState(cpbjscResult.getdata21308());
                        cpOrderContentResult23.setOrderId("2-1308");
                        cpOrderContentResultList2.add(cpOrderContentResult23);

                        cpOrderContentListResult.setData(cpOrderContentResultList2);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 2:
                        cpOrderContentListResult.setOrderContentListName("第三球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                        cpOrderContentResult30.setOrderName("大");
                        cpOrderContentResult30.setFullName("第三球");
                        cpOrderContentResult30.setOrderState(cpbjscResult.getdata31305());
                        cpOrderContentResult30.setOrderId("3-1305");
                        cpOrderContentResultList3.add(cpOrderContentResult30);

                        CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                        cpOrderContentResult31.setOrderName("小");
                        cpOrderContentResult31.setFullName("第三球");
                        cpOrderContentResult31.setOrderState(cpbjscResult.getdata31306());
                        cpOrderContentResult31.setOrderId("3-1306");
                        cpOrderContentResultList3.add(cpOrderContentResult31);

                        CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                        cpOrderContentResult32.setOrderName("单");
                        cpOrderContentResult32.setFullName("第三球");
                        cpOrderContentResult32.setOrderState(cpbjscResult.getdata31307());
                        cpOrderContentResult32.setOrderId("3-1307");
                        cpOrderContentResultList3.add(cpOrderContentResult32);

                        CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                        cpOrderContentResult33.setOrderName("双");
                        cpOrderContentResult33.setFullName("第三球");
                        cpOrderContentResult33.setOrderState(cpbjscResult.getdata31308());
                        cpOrderContentResult33.setOrderId("3-1308");
                        cpOrderContentResultList3.add(cpOrderContentResult33);

                        cpOrderContentListResult.setData(cpOrderContentResultList3);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);

                        break;
                    case 3:
                        cpOrderContentListResult.setOrderContentListName("第四球");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                        cpOrderContentResult40.setOrderName("大");
                        cpOrderContentResult40.setFullName("第四球");
                        cpOrderContentResult40.setOrderState(cpbjscResult.getdata41305());
                        cpOrderContentResult40.setOrderId("4-1305");
                        cpOrderContentResultList4.add(cpOrderContentResult40);

                        CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                        cpOrderContentResult41.setOrderName("小");
                        cpOrderContentResult41.setFullName("第四球");
                        cpOrderContentResult41.setOrderState(cpbjscResult.getdata41306());
                        cpOrderContentResult41.setOrderId("4-1306");
                        cpOrderContentResultList4.add(cpOrderContentResult41);

                        CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                        cpOrderContentResult42.setOrderName("单");
                        cpOrderContentResult42.setFullName("第四球");
                        cpOrderContentResult42.setOrderState(cpbjscResult.getdata41307());
                        cpOrderContentResult42.setOrderId("4-1307");
                        cpOrderContentResultList4.add(cpOrderContentResult42);

                        CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                        cpOrderContentResult43.setOrderName("双");
                        cpOrderContentResult43.setFullName("第四球");
                        cpOrderContentResult43.setOrderState(cpbjscResult.getdata41308());
                        cpOrderContentResult43.setOrderId("4-1308");
                        cpOrderContentResultList4.add(cpOrderContentResult43);

                        cpOrderContentListResult.setData(cpOrderContentResultList4);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 4:
                        cpOrderContentListResult.setOrderContentListName("第五球");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                        cpOrderContentResult50.setOrderName("大");
                        cpOrderContentResult50.setFullName("第五球");
                        cpOrderContentResult50.setOrderState(cpbjscResult.getdata51305());
                        cpOrderContentResult50.setOrderId("5-1305");
                        cpOrderContentResultList5.add(cpOrderContentResult50);

                        CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                        cpOrderContentResult51.setOrderName("小");
                        cpOrderContentResult51.setFullName("第五球");
                        cpOrderContentResult51.setOrderState(cpbjscResult.getdata51306());
                        cpOrderContentResult51.setOrderId("5-1306");
                        cpOrderContentResultList5.add(cpOrderContentResult51);

                        CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                        cpOrderContentResult52.setOrderName("单");
                        cpOrderContentResult52.setFullName("第五球");
                        cpOrderContentResult52.setOrderState(cpbjscResult.getdata51307());
                        cpOrderContentResult52.setOrderId("5-1307");
                        cpOrderContentResultList5.add(cpOrderContentResult52);

                        CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                        cpOrderContentResult53.setOrderName("双");
                        cpOrderContentResult53.setFullName("第五球");
                        cpOrderContentResult53.setOrderState(cpbjscResult.getdata51308());
                        cpOrderContentResult53.setOrderId("5-1308");
                        cpOrderContentResultList5.add(cpOrderContentResult53);

                        cpOrderContentListResult.setData(cpOrderContentResultList5);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 5:
                        cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                        cpOrderContentListResult.setShowNumber(2);

                        List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                        cpOrderContentResult60.setOrderName("总和大");
                        cpOrderContentResult60.setFullName("");
                        cpOrderContentResult60.setOrderState(cpbjscResult.getdata1309());
                        cpOrderContentResult60.setOrderId("1309");
                        cpOrderContentResultList6.add(cpOrderContentResult60);

                        CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                        cpOrderContentResult61.setOrderName("总和小");
                        cpOrderContentResult61.setFullName("");
                        cpOrderContentResult61.setOrderState(cpbjscResult.getdata1310());
                        cpOrderContentResult61.setOrderId("1310");
                        cpOrderContentResultList6.add(cpOrderContentResult61);

                        CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                        cpOrderContentResult62.setOrderName("总和单");
                        cpOrderContentResult62.setFullName("");
                        cpOrderContentResult62.setOrderState(cpbjscResult.getdata1311());
                        cpOrderContentResult62.setOrderId("1311");
                        cpOrderContentResultList6.add(cpOrderContentResult62);

                        CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                        cpOrderContentResult63.setOrderName("总和双");
                        cpOrderContentResult63.setFullName("");
                        cpOrderContentResult63.setOrderState(cpbjscResult.getdata1312());
                        cpOrderContentResult63.setOrderId("1312");
                        cpOrderContentResultList6.add(cpOrderContentResult63);

                        cpOrderContentListResult.setData(cpOrderContentResultList6);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                    case 6:
                        cpOrderContentListResult.setOrderContentListName("");
                        cpOrderContentListResult.setShowNumber(3);


                        List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                        CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                        cpOrderContentResult70.setOrderName("龙");
                        cpOrderContentResult70.setFullName("");
                        cpOrderContentResult70.setOrderState(cpbjscResult.getdata1313());
                        cpOrderContentResult70.setOrderId("1313");
                        cpOrderContentResultList7.add(cpOrderContentResult70);

                        CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                        cpOrderContentResult71.setOrderName("虎");
                        cpOrderContentResult71.setFullName("");
                        cpOrderContentResult71.setOrderState(cpbjscResult.getdata1314());
                        cpOrderContentResult71.setOrderId("1314");
                        cpOrderContentResultList7.add(cpOrderContentResult71);

                        CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                        cpOrderContentResult72.setOrderName("和");
                        cpOrderContentResult72.setFullName("");
                        cpOrderContentResult72.setOrderState(cpbjscResult.getdata1315());
                        cpOrderContentResult72.setOrderId("1315");
                        cpOrderContentResultList7.add(cpOrderContentResult72);

                        cpOrderContentListResult.setData(cpOrderContentResultList7);
                        cPOrderContentListResultAll.add(cpOrderContentListResult);
                        break;
                }
            }
            return cPOrderContentListResultAll;
        }else if(index==1){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("第一球");
            cpOrderContentListResult.setShowNumber(2);
            cpOrderContentListResult.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
            cpOrderContentResult0.setOrderName("0");
            cpOrderContentResult0.setFullName("第一球");
            cpOrderContentResult0.setOrderState(cpbjscResult.getdata13000());
            cpOrderContentResult0.setOrderId("1300-0");
            cpOrderContentResultList.add(cpOrderContentResult0);

            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("第一球");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata13001());
            cpOrderContentResult1.setOrderId("1300-1");
            cpOrderContentResultList.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("第一球");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata13002());
            cpOrderContentResult2.setOrderId("1300-2");
            cpOrderContentResultList.add(cpOrderContentResult2);

            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("第一球");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata13003());
            cpOrderContentResult3.setOrderId("1300-3");
            cpOrderContentResultList.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("第一球");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata13004());
            cpOrderContentResult4.setOrderId("1300-4");
            cpOrderContentResultList.add(cpOrderContentResult4);


            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("第一球");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata13005());
            cpOrderContentResult5.setOrderId("1300-5");
            cpOrderContentResultList.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("第一球");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata13006());
            cpOrderContentResult6.setOrderId("1300-6");
            cpOrderContentResultList.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("第一球");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata13007());
            cpOrderContentResult7.setOrderId("1300-7");
            cpOrderContentResultList.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("第一球");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata13008());
            cpOrderContentResult8.setOrderId("1300-8");
            cpOrderContentResultList.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("第一球");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata13009());
            cpOrderContentResult9.setOrderId("1300-9");
            cpOrderContentResultList.add(cpOrderContentResult9);

            cpOrderContentListResult.setData(cpOrderContentResultList);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("第二球");
            cpOrderContentListResult2.setShowNumber(2);
            cpOrderContentListResult2.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
            cpOrderContentResult20.setOrderName("0");
            cpOrderContentResult20.setFullName("第二球");
            cpOrderContentResult20.setOrderState(cpbjscResult.getdata13010());
            cpOrderContentResult20.setOrderId("1301-0");
            cpOrderContentResultList2.add(cpOrderContentResult20);

            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("第二球");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata13011());
            cpOrderContentResult21.setOrderId("1301-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("第二球");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata13012());
            cpOrderContentResult22.setOrderId("1301-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("第二球");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata13013());
            cpOrderContentResult23.setOrderId("1301-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);


            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("第二球");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata13013());
            cpOrderContentResult24.setOrderId("1301-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("第二球");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata13015());
            cpOrderContentResult25.setOrderId("1301-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);


            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("第二球");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata13016());
            cpOrderContentResult26.setOrderId("1301-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);


            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("第二球");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata13017());
            cpOrderContentResult27.setOrderId("1301-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);


            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("第二球");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata13018());
            cpOrderContentResult28.setOrderId("1301-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);


            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("第二球");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata13019());
            cpOrderContentResult29.setOrderId("1301-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);


            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第三球");
            cpOrderContentListResult3.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
            cpOrderContentResult30.setOrderName("0");
            cpOrderContentResult30.setFullName("第三球");
            cpOrderContentResult30.setOrderState(cpbjscResult.getdata13020());
            cpOrderContentResult30.setOrderId("1302-0");
            cpOrderContentResultList3.add(cpOrderContentResult30);

            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第三球");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata13021());
            cpOrderContentResult31.setOrderId("1302-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第三球");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata13022());
            cpOrderContentResult32.setOrderId("1302-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第三球");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata13023());
            cpOrderContentResult33.setOrderId("1302-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第三球");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata13024());
            cpOrderContentResult34.setOrderId("1302-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第三球");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata13025());
            cpOrderContentResult35.setOrderId("1302-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);


            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第三球");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata13026());
            cpOrderContentResult36.setOrderId("1302-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第三球");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata13027());
            cpOrderContentResult37.setOrderId("1302-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第三球");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata13028());
            cpOrderContentResult38.setOrderId("1302-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第三球");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata13029());
            cpOrderContentResult39.setOrderId("1302-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第四球");
            cpOrderContentListResult4.setShowNumber(2);
            cpOrderContentListResult4.setShowType("QIU");
            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
            cpOrderContentResult40.setOrderName("0");
            cpOrderContentResult40.setFullName("第四球");
            cpOrderContentResult40.setOrderState(cpbjscResult.getdata13030());
            cpOrderContentResult40.setOrderId("1303-0");
            cpOrderContentResultList4.add(cpOrderContentResult40);

            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第四球");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata13031());
            cpOrderContentResult41.setOrderId("1303-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第四球");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata13032());
            cpOrderContentResult42.setOrderId("1303-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);

            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第四球");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata13033());
            cpOrderContentResult43.setOrderId("1303-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第四球");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata13034());
            cpOrderContentResult44.setOrderId("1303-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第四球");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata13035());
            cpOrderContentResult45.setOrderId("1303-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第四球");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata13036());
            cpOrderContentResult46.setOrderId("1303-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第四球");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata13037());
            cpOrderContentResult47.setOrderId("1303-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第四球");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata13038());
            cpOrderContentResult48.setOrderId("1303-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第四球");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata13039());
            cpOrderContentResult49.setOrderId("1303-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);


            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第五球");
            cpOrderContentListResult5.setShowNumber(2);
            cpOrderContentListResult3.setShowType("QIU");

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
            cpOrderContentResult50.setOrderName("0");
            cpOrderContentResult50.setFullName("第五球");
            cpOrderContentResult50.setOrderState(cpbjscResult.getdata13040());
            cpOrderContentResult50.setOrderId("1304-0");
            cpOrderContentResultList5.add(cpOrderContentResult50);

            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第五球");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata13041());
            cpOrderContentResult51.setOrderId("1304-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第五球");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata13042());
            cpOrderContentResult52.setOrderId("1304-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);

            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第五球");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata13043());
            cpOrderContentResult53.setOrderId("1304-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第五球");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata13044());
            cpOrderContentResult54.setOrderId("1304-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第五球");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata13045());
            cpOrderContentResult55.setOrderId("1304-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第五球");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata13046());
            cpOrderContentResult56.setOrderId("1304-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第五球");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata13047());
            cpOrderContentResult57.setOrderId("1304-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第五球");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata13048());
            cpOrderContentResult58.setOrderId("1304-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第五球");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata13049());
            cpOrderContentResult59.setOrderId("1304-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult4);
            cPOrderContentListResultAll.add(cpOrderContentListResult5);

            return cPOrderContentListResultAll;
        }else if(index==2){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("前三");
            cpOrderContentListResult.setShowType("ZI");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("豹子");
            cpOrderContentResult1.setFullName("前三");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata1316());
            cpOrderContentResult1.setOrderId("1316");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("顺子");
            cpOrderContentResult2.setFullName("前三");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata1317());
            cpOrderContentResult2.setOrderId("1317");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("对子");
            cpOrderContentResult3.setFullName("前三");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata1318());
            cpOrderContentResult3.setOrderId("1318");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("半顺");
            cpOrderContentResult4.setFullName("前三");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata1319());
            cpOrderContentResult4.setOrderId("1319");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult13 = new CPOrderContentListResult();
            cpOrderContentListResult13.setOrderContentListName("");
            cpOrderContentListResult13.setShowType("ZI");
            cpOrderContentListResult13.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList13 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("杂六");
            cpOrderContentResult5.setFullName("前三");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata1320());
            cpOrderContentResult5.setOrderId("1320");
            cpOrderContentResultList13.add(cpOrderContentResult5);

            cpOrderContentListResult13.setData(cpOrderContentResultList13);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("中三");
            cpOrderContentListResult2.setShowType("ZI");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("豹子");
            cpOrderContentResult21.setFullName("中三");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata1321());
            cpOrderContentResult21.setOrderId("1321");
            cpOrderContentResultList2.add(cpOrderContentResult21);


            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("顺子");
            cpOrderContentResult22.setFullName("中三");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata1322());
            cpOrderContentResult22.setOrderId("1322");
            cpOrderContentResultList2.add(cpOrderContentResult22);

            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("对子");
            cpOrderContentResult23.setFullName("中三");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata1323());
            cpOrderContentResult23.setOrderId("1323");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("半顺");
            cpOrderContentResult24.setFullName("中三");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata1324());
            cpOrderContentResult24.setOrderId("1324");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
            cpOrderContentListResult22.setOrderContentListName("");
            cpOrderContentListResult22.setShowType("ZI");
            cpOrderContentListResult22.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("杂六");
            cpOrderContentResult25.setFullName("中三");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata1325());
            cpOrderContentResult25.setOrderId("1325");
            cpOrderContentResultList22.add(cpOrderContentResult25);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            cpOrderContentListResult22.setData(cpOrderContentResultList22);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("后三");
            cpOrderContentListResult3.setShowType("ZI");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("豹子");
            cpOrderContentResult31.setFullName("后三");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata1326());
            cpOrderContentResult31.setOrderId("1326");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("顺子");
            cpOrderContentResult32.setFullName("后三");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata1327());
            cpOrderContentResult32.setOrderId("1327");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("对子");
            cpOrderContentResult33.setFullName("后三");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata1328());
            cpOrderContentResult33.setOrderId("1328");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("半顺");
            cpOrderContentResult34.setFullName("后三");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata1329());
            cpOrderContentResult34.setOrderId("1329");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
            cpOrderContentListResult33.setOrderContentListName("");
            cpOrderContentListResult33.setShowType("ZI");
            cpOrderContentListResult33.setShowNumber(1);

            List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("杂六");
            cpOrderContentResult35.setFullName("后三");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata1330());
            cpOrderContentResult35.setOrderId("1330");
            cpOrderContentResultList33.add(cpOrderContentResult35);

            cpOrderContentListResult33.setData(cpOrderContentResultList33);

            cPOrderContentListResultAll.add(cpOrderContentListResult);
            cPOrderContentListResultAll.add(cpOrderContentListResult13);
            cPOrderContentListResultAll.add(cpOrderContentListResult2);
            cPOrderContentListResultAll.add(cpOrderContentListResult22);
            cPOrderContentListResultAll.add(cpOrderContentListResult3);
            cPOrderContentListResultAll.add(cpOrderContentListResult33);
            return cPOrderContentListResultAll;
        }
        return cPOrderContentListResultAll;
    }

    private void CQSSC(CQSSCResult cpbjscResult){
        for(int index=0;index<3;++index){
            CPOrderAllResult allResult = new CPOrderAllResult();
            if(index==0){
                allResult.setEventChecked(true);
                List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                for (int l = 0; l < 7; ++l) {
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    switch (l) {
                        case 0:
                            cpOrderContentListResult.setOrderContentListName("第一球");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                            cpOrderContentResult0.setOrderName("大");
                            cpOrderContentResult0.setFullName("第一球");
                            cpOrderContentResult0.setOrderState(cpbjscResult.getData_11005());
                            cpOrderContentResult0.setOrderId("1-1005");
                            cpOrderContentResultList.add(cpOrderContentResult0);

                            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                            cpOrderContentResult1.setOrderName("小");
                            cpOrderContentResult1.setFullName("第一球");
                            cpOrderContentResult1.setOrderState(cpbjscResult.getData_11006());
                            cpOrderContentResult1.setOrderId("1-1006");
                            cpOrderContentResultList.add(cpOrderContentResult1);

                            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                            cpOrderContentResult2.setOrderName("单");
                            cpOrderContentResult2.setFullName("第一球");
                            cpOrderContentResult2.setOrderState(cpbjscResult.getData_11007());
                            cpOrderContentResult2.setOrderId("1-1007");
                            cpOrderContentResultList.add(cpOrderContentResult2);

                            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                            cpOrderContentResult3.setOrderName("双");
                            cpOrderContentResult3.setFullName("第一球");
                            cpOrderContentResult3.setOrderState(cpbjscResult.getData_11008());
                            cpOrderContentResult3.setOrderId("1-1008");
                            cpOrderContentResultList.add(cpOrderContentResult3);
                            cpOrderContentListResult.setData(cpOrderContentResultList);

                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 1:
                            cpOrderContentListResult.setOrderContentListName("第二球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                            cpOrderContentResult20.setOrderName("大");
                            cpOrderContentResult20.setFullName("第二球");
                            cpOrderContentResult20.setOrderState(cpbjscResult.getData_21005());
                            cpOrderContentResult20.setOrderId("1-2005");
                            cpOrderContentResultList2.add(cpOrderContentResult20);

                            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                            cpOrderContentResult21.setOrderName("小");
                            cpOrderContentResult21.setFullName("第二球");
                            cpOrderContentResult21.setOrderState(cpbjscResult.getData_21006());
                            cpOrderContentResult21.setOrderId("1-2006");
                            cpOrderContentResultList2.add(cpOrderContentResult21);

                            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                            cpOrderContentResult22.setOrderName("单");
                            cpOrderContentResult22.setFullName("第二球");
                            cpOrderContentResult22.setOrderState(cpbjscResult.getData_21007());
                            cpOrderContentResult22.setOrderId("1-2007");
                            cpOrderContentResultList2.add(cpOrderContentResult22);

                            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                            cpOrderContentResult23.setOrderName("双");
                            cpOrderContentResult23.setFullName("第二球");
                            cpOrderContentResult23.setOrderState(cpbjscResult.getData_21008());
                            cpOrderContentResult23.setOrderId("1-2008");
                            cpOrderContentResultList2.add(cpOrderContentResult23);

                            cpOrderContentListResult.setData(cpOrderContentResultList2);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 2:
                            cpOrderContentListResult.setOrderContentListName("第三球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                            cpOrderContentResult30.setOrderName("大");
                            cpOrderContentResult30.setFullName("第三球");
                            cpOrderContentResult30.setOrderState(cpbjscResult.getData_31005());
                            cpOrderContentResult30.setOrderId("1-3005");
                            cpOrderContentResultList3.add(cpOrderContentResult30);

                            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                            cpOrderContentResult31.setOrderName("小");
                            cpOrderContentResult31.setFullName("第三球");
                            cpOrderContentResult31.setOrderState(cpbjscResult.getData_31006());
                            cpOrderContentResult31.setOrderId("1-3006");
                            cpOrderContentResultList3.add(cpOrderContentResult31);

                            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                            cpOrderContentResult32.setOrderName("单");
                            cpOrderContentResult32.setFullName("第三球");
                            cpOrderContentResult32.setOrderState(cpbjscResult.getData_31007());
                            cpOrderContentResult32.setOrderId("1-3007");
                            cpOrderContentResultList3.add(cpOrderContentResult32);

                            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                            cpOrderContentResult33.setOrderName("双");
                            cpOrderContentResult33.setFullName("第三球");
                            cpOrderContentResult33.setOrderState(cpbjscResult.getData_31008());
                            cpOrderContentResult33.setOrderId("1-3008");
                            cpOrderContentResultList3.add(cpOrderContentResult33);

                            cpOrderContentListResult.setData(cpOrderContentResultList3);
                            CPOrderContentListResult.add(cpOrderContentListResult);

                            break;
                        case 3:
                            cpOrderContentListResult.setOrderContentListName("第四球");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                            cpOrderContentResult40.setOrderName("大");
                            cpOrderContentResult40.setFullName("第四球");
                            cpOrderContentResult40.setOrderState(cpbjscResult.getData_41005());
                            cpOrderContentResult40.setOrderId("1-4005");
                            cpOrderContentResultList4.add(cpOrderContentResult40);

                            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                            cpOrderContentResult41.setOrderName("小");
                            cpOrderContentResult41.setFullName("第四球");
                            cpOrderContentResult41.setOrderState(cpbjscResult.getData_41006());
                            cpOrderContentResult41.setOrderId("1-4006");
                            cpOrderContentResultList4.add(cpOrderContentResult41);

                            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                            cpOrderContentResult42.setOrderName("单");
                            cpOrderContentResult42.setFullName("第四球");
                            cpOrderContentResult42.setOrderState(cpbjscResult.getData_41007());
                            cpOrderContentResult42.setOrderId("1-4007");
                            cpOrderContentResultList4.add(cpOrderContentResult42);

                            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                            cpOrderContentResult43.setOrderName("双");
                            cpOrderContentResult43.setFullName("第四球");
                            cpOrderContentResult43.setOrderState(cpbjscResult.getData_41008());
                            cpOrderContentResult43.setOrderId("1-4008");
                            cpOrderContentResultList4.add(cpOrderContentResult43);

                            cpOrderContentListResult.setData(cpOrderContentResultList4);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 4:
                            cpOrderContentListResult.setOrderContentListName("第五球");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                            cpOrderContentResult50.setOrderName("大");
                            cpOrderContentResult50.setFullName("第五球");
                            cpOrderContentResult50.setOrderState(cpbjscResult.getData_51005());
                            cpOrderContentResult50.setOrderId("1-5005");
                            cpOrderContentResultList5.add(cpOrderContentResult50);

                            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                            cpOrderContentResult51.setOrderName("小");
                            cpOrderContentResult51.setFullName("第五球");
                            cpOrderContentResult51.setOrderState(cpbjscResult.getData_51006());
                            cpOrderContentResult51.setOrderId("1-5006");
                            cpOrderContentResultList5.add(cpOrderContentResult51);

                            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                            cpOrderContentResult52.setOrderName("单");
                            cpOrderContentResult52.setFullName("第五球");
                            cpOrderContentResult52.setOrderState(cpbjscResult.getData_51007());
                            cpOrderContentResult52.setOrderId("1-5007");
                            cpOrderContentResultList5.add(cpOrderContentResult52);

                            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                            cpOrderContentResult53.setOrderName("双");
                            cpOrderContentResult53.setFullName("第五球");
                            cpOrderContentResult53.setOrderState(cpbjscResult.getData_51008());
                            cpOrderContentResult53.setOrderId("1-5008");
                            cpOrderContentResultList5.add(cpOrderContentResult53);

                            cpOrderContentListResult.setData(cpOrderContentResultList5);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 5:
                            cpOrderContentListResult.setOrderContentListName("总和、龙虎");
                            cpOrderContentListResult.setShowNumber(2);

                            List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult60 = new CPOrderContentResult();
                            cpOrderContentResult60.setOrderName("总和大");
                            cpOrderContentResult60.setFullName("");
                            cpOrderContentResult60.setOrderState(cpbjscResult.getData_1009());
                            cpOrderContentResult60.setOrderId("1009");
                            cpOrderContentResultList6.add(cpOrderContentResult60);

                            CPOrderContentResult cpOrderContentResult61 = new CPOrderContentResult();
                            cpOrderContentResult61.setOrderName("总和小");
                            cpOrderContentResult61.setFullName("");
                            cpOrderContentResult61.setOrderState(cpbjscResult.getData_1010());
                            cpOrderContentResult61.setOrderId("1010");
                            cpOrderContentResultList6.add(cpOrderContentResult61);

                            CPOrderContentResult cpOrderContentResult62 = new CPOrderContentResult();
                            cpOrderContentResult62.setOrderName("总和单");
                            cpOrderContentResult62.setFullName("");
                            cpOrderContentResult62.setOrderState(cpbjscResult.getData_1011());
                            cpOrderContentResult62.setOrderId("1011");
                            cpOrderContentResultList6.add(cpOrderContentResult62);

                            CPOrderContentResult cpOrderContentResult63 = new CPOrderContentResult();
                            cpOrderContentResult63.setOrderName("总和双");
                            cpOrderContentResult63.setFullName("");
                            cpOrderContentResult63.setOrderState(cpbjscResult.getData_1012());
                            cpOrderContentResult63.setOrderId("1012");
                            cpOrderContentResultList6.add(cpOrderContentResult63);

                            cpOrderContentListResult.setData(cpOrderContentResultList6);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 6:
                            cpOrderContentListResult.setOrderContentListName("");
                            cpOrderContentListResult.setShowNumber(3);


                            List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                            CPOrderContentResult cpOrderContentResult70 = new CPOrderContentResult();
                            cpOrderContentResult70.setOrderName("龙");
                            cpOrderContentResult70.setFullName("");
                            cpOrderContentResult70.setOrderState(cpbjscResult.getData_1013());
                            cpOrderContentResult70.setOrderId("1013");
                            cpOrderContentResultList7.add(cpOrderContentResult70);

                            CPOrderContentResult cpOrderContentResult71 = new CPOrderContentResult();
                            cpOrderContentResult71.setOrderName("虎");
                            cpOrderContentResult71.setFullName("");
                            cpOrderContentResult71.setOrderState(cpbjscResult.getData_1014());
                            cpOrderContentResult71.setOrderId("1014");
                            cpOrderContentResultList7.add(cpOrderContentResult71);

                            CPOrderContentResult cpOrderContentResult72 = new CPOrderContentResult();
                            cpOrderContentResult72.setOrderName("和");
                            cpOrderContentResult72.setFullName("");
                            cpOrderContentResult72.setOrderState(cpbjscResult.getData_1015());
                            cpOrderContentResult72.setOrderId("1015");
                            cpOrderContentResultList7.add(cpOrderContentResult72);

                            cpOrderContentListResult.setData(cpOrderContentResultList7);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                    }
                }
                allResult.setOrderAllName("两面");
                allResult.setData(CPOrderContentListResult);
            }else if(index==1){
                List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
                allResult.setOrderAllName("1-5球");
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("第一球");
                cpOrderContentListResult.setShowNumber(2);
                cpOrderContentListResult.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                cpOrderContentResult0.setOrderName("0");
                cpOrderContentResult0.setFullName("第一球");
                cpOrderContentResult0.setOrderState(cpbjscResult.getData_10000());
                cpOrderContentResult0.setOrderId("1000-0");
                cpOrderContentResultList.add(cpOrderContentResult0);

                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("第一球");
                cpOrderContentResult1.setOrderState(cpbjscResult.getData_10001());
                cpOrderContentResult1.setOrderId("1000-1");
                cpOrderContentResultList.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("第一球");
                cpOrderContentResult2.setOrderState(cpbjscResult.getData_10002());
                cpOrderContentResult2.setOrderId("1000-2");
                cpOrderContentResultList.add(cpOrderContentResult2);

                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("第一球");
                cpOrderContentResult3.setOrderState(cpbjscResult.getData_10003());
                cpOrderContentResult3.setOrderId("1000-3");
                cpOrderContentResultList.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("第一球");
                cpOrderContentResult4.setOrderState(cpbjscResult.getData_10004());
                cpOrderContentResult4.setOrderId("1000-4");
                cpOrderContentResultList.add(cpOrderContentResult4);


                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("第一球");
                cpOrderContentResult5.setOrderState(cpbjscResult.getData_10005());
                cpOrderContentResult5.setOrderId("1000-5");
                cpOrderContentResultList.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("第一球");
                cpOrderContentResult6.setOrderState(cpbjscResult.getData_10006());
                cpOrderContentResult6.setOrderId("1000-6");
                cpOrderContentResultList.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("第一球");
                cpOrderContentResult7.setOrderState(cpbjscResult.getData_10007());
                cpOrderContentResult7.setOrderId("1000-7");
                cpOrderContentResultList.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("第一球");
                cpOrderContentResult8.setOrderState(cpbjscResult.getData_10008());
                cpOrderContentResult8.setOrderId("1000-8");
                cpOrderContentResultList.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("第一球");
                cpOrderContentResult9.setOrderState(cpbjscResult.getData_10009());
                cpOrderContentResult9.setOrderId("1000-9");
                cpOrderContentResultList.add(cpOrderContentResult9);

                cpOrderContentListResult.setData(cpOrderContentResultList);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("第二球");
                cpOrderContentListResult2.setShowNumber(2);
                cpOrderContentListResult2.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                cpOrderContentResult20.setOrderName("0");
                cpOrderContentResult20.setFullName("第二球");
                cpOrderContentResult20.setOrderState(cpbjscResult.getData_10010());
                cpOrderContentResult20.setOrderId("1001-0");
                cpOrderContentResultList2.add(cpOrderContentResult20);

                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("第二球");
                cpOrderContentResult21.setOrderState(cpbjscResult.getData_10011());
                cpOrderContentResult21.setOrderId("1001-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("第二球");
                cpOrderContentResult22.setOrderState(cpbjscResult.getData_10012());
                cpOrderContentResult22.setOrderId("1001-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("第二球");
                cpOrderContentResult23.setOrderState(cpbjscResult.getData_10013());
                cpOrderContentResult23.setOrderId("1001-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);


                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("第二球");
                cpOrderContentResult24.setOrderState(cpbjscResult.getData_10014());
                cpOrderContentResult24.setOrderId("1001-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("第二球");
                cpOrderContentResult25.setOrderState(cpbjscResult.getData_10015());
                cpOrderContentResult25.setOrderId("1001-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);


                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("第二球");
                cpOrderContentResult26.setOrderState(cpbjscResult.getData_10016());
                cpOrderContentResult26.setOrderId("1001-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);


                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("第二球");
                cpOrderContentResult27.setOrderState(cpbjscResult.getData_10017());
                cpOrderContentResult27.setOrderId("1001-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);


                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("第二球");
                cpOrderContentResult28.setOrderState(cpbjscResult.getData_10018());
                cpOrderContentResult28.setOrderId("1001-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);


                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("第二球");
                cpOrderContentResult29.setOrderState(cpbjscResult.getData_10019());
                cpOrderContentResult29.setOrderId("1001-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);


                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第三球");
                cpOrderContentListResult3.setShowNumber(2);
                cpOrderContentListResult3.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult30 = new CPOrderContentResult();
                cpOrderContentResult30.setOrderName("0");
                cpOrderContentResult30.setFullName("第三球");
                cpOrderContentResult30.setOrderState(cpbjscResult.getData_10020());
                cpOrderContentResult30.setOrderId("1002-0");
                cpOrderContentResultList3.add(cpOrderContentResult30);

                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第三球");
                cpOrderContentResult31.setOrderState(cpbjscResult.getData_10021());
                cpOrderContentResult31.setOrderId("1002-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第三球");
                cpOrderContentResult32.setOrderState(cpbjscResult.getData_10022());
                cpOrderContentResult32.setOrderId("1002-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第三球");
                cpOrderContentResult33.setOrderState(cpbjscResult.getData_10023());
                cpOrderContentResult33.setOrderId("1002-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第三球");
                cpOrderContentResult34.setOrderState(cpbjscResult.getData_10024());
                cpOrderContentResult34.setOrderId("1002-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第三球");
                cpOrderContentResult35.setOrderState(cpbjscResult.getData_10025());
                cpOrderContentResult35.setOrderId("1002-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);


                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第三球");
                cpOrderContentResult36.setOrderState(cpbjscResult.getData_10026());
                cpOrderContentResult36.setOrderId("1002-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第三球");
                cpOrderContentResult37.setOrderState(cpbjscResult.getData_10027());
                cpOrderContentResult37.setOrderId("1002-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第三球");
                cpOrderContentResult38.setOrderState(cpbjscResult.getData_10028());
                cpOrderContentResult38.setOrderId("1002-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第三球");
                cpOrderContentResult39.setOrderState(cpbjscResult.getData_10029());
                cpOrderContentResult39.setOrderId("1002-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第四球");
                cpOrderContentListResult4.setShowNumber(2);
                cpOrderContentListResult4.setShowType("QIU");
                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult40 = new CPOrderContentResult();
                cpOrderContentResult40.setOrderName("0");
                cpOrderContentResult40.setFullName("第四球");
                cpOrderContentResult40.setOrderState(cpbjscResult.getData_10030());
                cpOrderContentResult40.setOrderId("1003-0");
                cpOrderContentResultList4.add(cpOrderContentResult40);

                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第四球");
                cpOrderContentResult41.setOrderState(cpbjscResult.getData_10031());
                cpOrderContentResult41.setOrderId("1003-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第四球");
                cpOrderContentResult42.setOrderState(cpbjscResult.getData_10032());
                cpOrderContentResult42.setOrderId("1003-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);

                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第四球");
                cpOrderContentResult43.setOrderState(cpbjscResult.getData_10033());
                cpOrderContentResult43.setOrderId("1003-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第四球");
                cpOrderContentResult44.setOrderState(cpbjscResult.getData_10034());
                cpOrderContentResult44.setOrderId("1003-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第四球");
                cpOrderContentResult45.setOrderState(cpbjscResult.getData_10035());
                cpOrderContentResult45.setOrderId("1003-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第四球");
                cpOrderContentResult46.setOrderState(cpbjscResult.getData_10036());
                cpOrderContentResult46.setOrderId("1003-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第四球");
                cpOrderContentResult47.setOrderState(cpbjscResult.getData_10037());
                cpOrderContentResult47.setOrderId("1003-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第四球");
                cpOrderContentResult48.setOrderState(cpbjscResult.getData_10038());
                cpOrderContentResult48.setOrderId("1003-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第四球");
                cpOrderContentResult49.setOrderState(cpbjscResult.getData_10039());
                cpOrderContentResult49.setOrderId("1003-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);


                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第五球");
                cpOrderContentListResult5.setShowNumber(2);
                cpOrderContentListResult3.setShowType("QIU");

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult50 = new CPOrderContentResult();
                cpOrderContentResult50.setOrderName("0");
                cpOrderContentResult50.setFullName("第五球");
                cpOrderContentResult50.setOrderState(cpbjscResult.getData_10040());
                cpOrderContentResult50.setOrderId("1004-0");
                cpOrderContentResultList5.add(cpOrderContentResult50);

                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第五球");
                cpOrderContentResult51.setOrderState(cpbjscResult.getData_10041());
                cpOrderContentResult51.setOrderId("1004-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第五球");
                cpOrderContentResult52.setOrderState(cpbjscResult.getData_10042());
                cpOrderContentResult52.setOrderId("1004-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);

                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第五球");
                cpOrderContentResult53.setOrderState(cpbjscResult.getData_10043());
                cpOrderContentResult53.setOrderId("1004-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第五球");
                cpOrderContentResult54.setOrderState(cpbjscResult.getData_10044());
                cpOrderContentResult54.setOrderId("1004-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第五球");
                cpOrderContentResult55.setOrderState(cpbjscResult.getData_10045());
                cpOrderContentResult55.setOrderId("1004-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第五球");
                cpOrderContentResult56.setOrderState(cpbjscResult.getData_10046());
                cpOrderContentResult56.setOrderId("1004-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第五球");
                cpOrderContentResult57.setOrderState(cpbjscResult.getData_10047());
                cpOrderContentResult57.setOrderId("1004-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第五球");
                cpOrderContentResult58.setOrderState(cpbjscResult.getData_10048());
                cpOrderContentResult58.setOrderId("1004-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第五球");
                cpOrderContentResult59.setOrderState(cpbjscResult.getData_10049());
                cpOrderContentResult59.setOrderId("1004-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult4);
                cPOrderContentListResultAll.add(cpOrderContentListResult5);

                allResult.setData(cPOrderContentListResultAll);
            }else if(index==2){
                allResult.setOrderAllName("前中后");
                List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("前三");
                cpOrderContentListResult.setShowType("ZI");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("豹子");
                cpOrderContentResult1.setFullName("前三");
                cpOrderContentResult1.setOrderState(cpbjscResult.getData_1016());
                cpOrderContentResult1.setOrderId("1016");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("顺子");
                cpOrderContentResult2.setFullName("前三");
                cpOrderContentResult2.setOrderState(cpbjscResult.getData_1017());
                cpOrderContentResult2.setOrderId("1017");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("对子");
                cpOrderContentResult3.setFullName("前三");
                cpOrderContentResult3.setOrderState(cpbjscResult.getData_1018());
                cpOrderContentResult3.setOrderId("1018");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("半顺");
                cpOrderContentResult4.setFullName("前三");
                cpOrderContentResult4.setOrderState(cpbjscResult.getData_1019());
                cpOrderContentResult4.setOrderId("1019");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult11 = new CPOrderContentListResult();
                cpOrderContentListResult11.setOrderContentListName("");
                cpOrderContentListResult11.setShowType("ZI");
                cpOrderContentListResult11.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList11 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("杂六");
                cpOrderContentResult5.setFullName("前三");
                cpOrderContentResult5.setOrderState(cpbjscResult.getData_1020());
                cpOrderContentResult5.setOrderId("1020");
                cpOrderContentResultList11.add(cpOrderContentResult5);

                cpOrderContentListResult11.setData(cpOrderContentResultList11);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("中三");
                cpOrderContentListResult2.setShowType("ZI");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("豹子");
                cpOrderContentResult21.setFullName("中三");
                cpOrderContentResult21.setOrderState(cpbjscResult.getData_1021());
                cpOrderContentResult21.setOrderId("1021");
                cpOrderContentResultList2.add(cpOrderContentResult21);


                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("顺子");
                cpOrderContentResult22.setFullName("中三");
                cpOrderContentResult22.setOrderState(cpbjscResult.getData_1022());
                cpOrderContentResult22.setOrderId("1022");
                cpOrderContentResultList2.add(cpOrderContentResult22);

                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("对子");
                cpOrderContentResult23.setFullName("中三");
                cpOrderContentResult23.setOrderState(cpbjscResult.getData_1023());
                cpOrderContentResult23.setOrderId("1023");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("半顺");
                cpOrderContentResult24.setFullName("中三");
                cpOrderContentResult24.setOrderState(cpbjscResult.getData_1024());
                cpOrderContentResult24.setOrderId("1024");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentListResult cpOrderContentListResult22 = new CPOrderContentListResult();
                cpOrderContentListResult22.setOrderContentListName("");
                cpOrderContentListResult22.setShowType("ZI");
                cpOrderContentListResult22.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList22 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("杂六");
                cpOrderContentResult25.setFullName("中三");
                cpOrderContentResult25.setOrderState(cpbjscResult.getData_1025());
                cpOrderContentResult25.setOrderId("1025");
                cpOrderContentResultList22.add(cpOrderContentResult25);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                cpOrderContentListResult22.setData(cpOrderContentResultList22);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("后三");
                cpOrderContentListResult3.setShowType("ZI");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("豹子");
                cpOrderContentResult31.setFullName("后三");
                cpOrderContentResult31.setOrderState(cpbjscResult.getData_1026());
                cpOrderContentResult31.setOrderId("1026");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("顺子");
                cpOrderContentResult32.setFullName("后三");
                cpOrderContentResult32.setOrderState(cpbjscResult.getData_1027());
                cpOrderContentResult32.setOrderId("1027");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("对子");
                cpOrderContentResult33.setFullName("后三");
                cpOrderContentResult33.setOrderState(cpbjscResult.getData_1028());
                cpOrderContentResult33.setOrderId("1028");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("半顺");
                cpOrderContentResult34.setFullName("后三");
                cpOrderContentResult34.setOrderState(cpbjscResult.getData_1029());
                cpOrderContentResult34.setOrderId("1029");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult33 = new CPOrderContentListResult();
                cpOrderContentListResult33.setOrderContentListName("");
                cpOrderContentListResult33.setShowType("ZI");
                cpOrderContentListResult33.setShowNumber(1);

                List<CPOrderContentResult> cpOrderContentResultList33 = new ArrayList<>();

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("杂六");
                cpOrderContentResult35.setFullName("后三");
                cpOrderContentResult35.setOrderState(cpbjscResult.getData_1030());
                cpOrderContentResult35.setOrderId("1030");
                cpOrderContentResultList33.add(cpOrderContentResult35);

                cpOrderContentListResult33.setData(cpOrderContentResultList33);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult11);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult22);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult33);
                allResult.setData(cPOrderContentListResultAll);
            }
            allResultList.add(index,allResult);
        }
    }

    private List<CPOrderContentListResult>   BJPK10(CPBJSCResult cpbjscResult,int k){
        List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
        if(k==0){
                for (int l = 0; l < 11; ++l) {
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    switch (l) {
                        case 0:
                            cpOrderContentListResult.setOrderContentListName("冠亚和");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("冠军大");
                                        cpOrderContentResult0.setFullName("冠、亚军和");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3017());
                                        cpOrderContentResult0.setOrderId("3017");
                                        cpOrderContentResultList.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("冠军小");
                                        cpOrderContentResult1.setFullName("冠、亚军和");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3018());
                                        cpOrderContentResult1.setOrderId("3018");
                                        cpOrderContentResultList.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("冠军单");
                                        cpOrderContentResult2.setFullName("冠、亚军和");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3019());
                                        cpOrderContentResult2.setOrderId("3019");
                                        cpOrderContentResultList.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("冠军双");
                                        cpOrderContentResult3.setFullName("冠、亚军和");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3020());
                                        cpOrderContentResult3.setOrderId("3020");
                                        cpOrderContentResultList.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 1:
                            cpOrderContentListResult.setOrderContentListName("冠军");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("冠军");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30013011());
                                        cpOrderContentResult0.setOrderId("3001-3011");
                                        cpOrderContentResultList1.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("冠军");
                                        cpOrderContentResult1.setOrderId("3001-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30013012());
                                        cpOrderContentResultList1.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("冠军");
                                        cpOrderContentResult2.setOrderId("3001-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30013013());
                                        cpOrderContentResultList1.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("冠军");
                                        cpOrderContentResult3.setOrderId("3001-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30013014());
                                        cpOrderContentResultList1.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("冠军");
                                        cpOrderContentResult4.setOrderId("3001-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30013015());
                                        cpOrderContentResultList1.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("冠军");
                                        cpOrderContentResult5.setOrderId("3001-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30013016());
                                        cpOrderContentResultList1.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList1);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 2:
                            cpOrderContentListResult.setOrderContentListName("亚军");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("亚军");
                                        cpOrderContentResult0.setOrderId("3002-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30023011());
                                        cpOrderContentResultList2.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("亚军");
                                        cpOrderContentResult1.setOrderId("3002-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30023012());
                                        cpOrderContentResultList2.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("亚军");
                                        cpOrderContentResult2.setOrderId("3002-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30023013());
                                        cpOrderContentResultList2.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("亚军");
                                        cpOrderContentResult3.setOrderId("3002-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30023014());
                                        cpOrderContentResultList2.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("亚军");
                                        cpOrderContentResult4.setOrderId("3002-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30023015());
                                        cpOrderContentResultList2.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("亚军");
                                        cpOrderContentResult5.setOrderId("3002-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30023016());
                                        cpOrderContentResultList2.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList2);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 3:
                            cpOrderContentListResult.setOrderContentListName("第三名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第三名");
                                        cpOrderContentResult0.setOrderId("3003-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30033011());
                                        cpOrderContentResultList3.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第三名");
                                        cpOrderContentResult1.setOrderId("3003-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30033012());
                                        cpOrderContentResultList3.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第三名");
                                        cpOrderContentResult2.setOrderId("3003-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30033013());
                                        cpOrderContentResultList3.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第三名");
                                        cpOrderContentResult3.setOrderId("3003-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30033014());
                                        cpOrderContentResultList3.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第三名");
                                        cpOrderContentResult4.setOrderId("3003-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30033015());
                                        cpOrderContentResultList3.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第三名");
                                        cpOrderContentResult5.setOrderId("3003-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30033016());
                                        cpOrderContentResultList3.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList3);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 4:
                            cpOrderContentListResult.setOrderContentListName("第四名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第四名");
                                        cpOrderContentResult0.setOrderId("3004-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30043011());
                                        cpOrderContentResultList4.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第四名");
                                        cpOrderContentResult1.setOrderId("3004-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30043012());
                                        cpOrderContentResultList4.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第四名");
                                        cpOrderContentResult2.setOrderId("3004-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30043013());
                                        cpOrderContentResultList4.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第四名");
                                        cpOrderContentResult3.setOrderId("3004-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30043014());
                                        cpOrderContentResultList4.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第四名");
                                        cpOrderContentResult4.setOrderId("3004-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30043015());
                                        cpOrderContentResultList4.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第四名");
                                        cpOrderContentResult5.setOrderId("3004-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30043016());
                                        cpOrderContentResultList4.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList4);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 5:
                            cpOrderContentListResult.setOrderContentListName("第五名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第五名");
                                        cpOrderContentResult0.setOrderId("3005-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30053011());
                                        cpOrderContentResultList5.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第五名");
                                        cpOrderContentResult1.setOrderId("3005-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30053012());
                                        cpOrderContentResultList5.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第五名");
                                        cpOrderContentResult2.setOrderId("3005-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30053013());
                                        cpOrderContentResultList5.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第五名");
                                        cpOrderContentResult3.setOrderId("3005-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30053014());
                                        cpOrderContentResultList5.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第五名");
                                        cpOrderContentResult4.setOrderId("3005-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30053015());
                                        cpOrderContentResultList5.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第五名");
                                        cpOrderContentResult5.setOrderId("3005-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30053016());
                                        cpOrderContentResultList5.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList5);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 6:
                            cpOrderContentListResult.setOrderContentListName("第六名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第六名");
                                        cpOrderContentResult0.setOrderId("3006-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30063011());
                                        cpOrderContentResultList6.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第六名");
                                        cpOrderContentResult1.setOrderId("3006-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30063012());
                                        cpOrderContentResultList6.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第六名");
                                        cpOrderContentResult2.setOrderId("3006-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30063013());
                                        cpOrderContentResultList6.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第六名");
                                        cpOrderContentResult3.setOrderId("3006-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30063014());
                                        cpOrderContentResultList6.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList6);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 7:
                            cpOrderContentListResult.setOrderContentListName("第七名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第七名");
                                        cpOrderContentResult0.setOrderId("3007-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30073011());
                                        cpOrderContentResultList7.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第七名");
                                        cpOrderContentResult1.setOrderId("3007-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30073012());
                                        cpOrderContentResultList7.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第七名");
                                        cpOrderContentResult2.setOrderId("3007-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30073013());
                                        cpOrderContentResultList7.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第七名");
                                        cpOrderContentResult3.setOrderId("3007-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30073014());
                                        cpOrderContentResultList7.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList7);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 8:
                            cpOrderContentListResult.setOrderContentListName("第八名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第八名");
                                        cpOrderContentResult0.setOrderId("3008-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30083011());
                                        cpOrderContentResultList8.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第八名");
                                        cpOrderContentResult1.setOrderId("3008-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30083012());
                                        cpOrderContentResultList8.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第八名");
                                        cpOrderContentResult2.setOrderId("3008-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30083013());
                                        cpOrderContentResultList8.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第八名");
                                        cpOrderContentResult3.setOrderId("3008-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30083014());
                                        cpOrderContentResultList8.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList8);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 9:
                            cpOrderContentListResult.setOrderContentListName("第九名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList9 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第九名");
                                        cpOrderContentResult0.setOrderId("3009-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30093011());
                                        cpOrderContentResultList9.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第九名");
                                        cpOrderContentResult1.setOrderId("3009-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30093012());
                                        cpOrderContentResultList9.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第九名");
                                        cpOrderContentResult2.setOrderId("3009-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30093013());
                                        cpOrderContentResultList9.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第九名");
                                        cpOrderContentResult3.setOrderId("3009-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30093014());
                                        cpOrderContentResultList9.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList9);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 10:
                            cpOrderContentListResult.setOrderContentListName("第十名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList10 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第十名");
                                        cpOrderContentResult0.setOrderId("3010-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30103011());
                                        cpOrderContentResultList10.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第十名");
                                        cpOrderContentResult1.setOrderId("3010-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30103012());
                                        cpOrderContentResultList10.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第十名");
                                        cpOrderContentResult2.setOrderId("3010-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30103013());
                                        cpOrderContentResultList10.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第十名");
                                        cpOrderContentResult3.setOrderId("3010-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30103014());
                                        cpOrderContentResultList10.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList10);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                    }
                }
                return CPOrderContentListResult;
            }else if(k==1){
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("冠、亚军 组合");
                cpOrderContentListResult.setShowNumber(2);
                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                cpOrderContentResult0.setOrderName("冠军大");
                cpOrderContentResult0.setFullName("冠、亚军和");
                cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3017());
                cpOrderContentResult0.setOrderId("3017");
                cpOrderContentResultList.add(cpOrderContentResult0);

                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("冠军小");
                cpOrderContentResult1.setFullName("冠、亚军和");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3018());
                cpOrderContentResult1.setOrderId("3018");
                cpOrderContentResultList.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("冠军单");
                cpOrderContentResult2.setFullName("冠、亚军和");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3019());
                cpOrderContentResult2.setOrderId("3019");
                cpOrderContentResultList.add(cpOrderContentResult2);

                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("冠军双");
                cpOrderContentResult3.setFullName("冠、亚军和");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3020());
                cpOrderContentResult3.setOrderId("3020");
                cpOrderContentResultList.add(cpOrderContentResult3);


                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setShowNumber(3);
                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("3");
                cpOrderContentResult4.setFullName("冠、亚军和");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30213());
                cpOrderContentResult4.setOrderId("3021-3");
                cpOrderContentResultList2.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("4");
                cpOrderContentResult5.setFullName("冠、亚军和");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30214());
                cpOrderContentResult5.setOrderId("3021-4");
                cpOrderContentResultList2.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("5");
                cpOrderContentResult6.setFullName("冠、亚军和");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30215());
                cpOrderContentResult6.setOrderId("3021-5");
                cpOrderContentResultList2.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("6");
                cpOrderContentResult7.setFullName("冠、亚军和");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30216());
                cpOrderContentResult7.setOrderId("3021-6");
                cpOrderContentResultList2.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("7");
                cpOrderContentResult8.setFullName("冠、亚军和");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30217());
                cpOrderContentResult8.setOrderId("3021-7");
                cpOrderContentResultList2.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("8");
                cpOrderContentResult9.setFullName("冠、亚军和");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30218());
                cpOrderContentResult9.setOrderId("3021-8");
                cpOrderContentResultList2.add(cpOrderContentResult9);

                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("9");
                cpOrderContentResult10.setFullName("冠、亚军和");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_30219());
                cpOrderContentResult10.setOrderId("3021-9");
                cpOrderContentResultList2.add(cpOrderContentResult10);

                CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                cpOrderContentResult11.setOrderName("10");
                cpOrderContentResult11.setFullName("冠、亚军和");
                cpOrderContentResult11.setOrderState(cpbjscResult.getdata_302110());
                cpOrderContentResult11.setOrderId("3021-10");
                cpOrderContentResultList2.add(cpOrderContentResult11);

                CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                cpOrderContentResult12.setOrderName("11");
                cpOrderContentResult12.setFullName("冠、亚军和");
                cpOrderContentResult12.setOrderState(cpbjscResult.getdata_302111());
                cpOrderContentResult12.setOrderId("3021-11");
                cpOrderContentResultList2.add(cpOrderContentResult12);

                CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                cpOrderContentResult13.setOrderName("12");
                cpOrderContentResult13.setFullName("冠、亚军和");
                cpOrderContentResult13.setOrderState(cpbjscResult.getdata_302112());
                cpOrderContentResult13.setOrderId("3021-12");
                cpOrderContentResultList2.add(cpOrderContentResult13);

                CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                cpOrderContentResult14.setOrderName("13");
                cpOrderContentResult14.setFullName("冠、亚军和");
                cpOrderContentResult14.setOrderState(cpbjscResult.getdata_302113());
                cpOrderContentResult14.setOrderId("3021-13");
                cpOrderContentResultList2.add(cpOrderContentResult14);

                CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                cpOrderContentResult15.setOrderName("14");
                cpOrderContentResult15.setFullName("冠、亚军和");
                cpOrderContentResult15.setOrderState(cpbjscResult.getdata_302114());
                cpOrderContentResult15.setOrderId("3021-14");
                cpOrderContentResultList2.add(cpOrderContentResult15);

                CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                cpOrderContentResult16.setOrderName("15");
                cpOrderContentResult16.setFullName("冠、亚军和");
                cpOrderContentResult16.setOrderState(cpbjscResult.getdata_302115());
                cpOrderContentResult16.setOrderId("3021-15");
                cpOrderContentResultList2.add(cpOrderContentResult16);

                CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                cpOrderContentResult17.setOrderName("16");
                cpOrderContentResult17.setFullName("冠、亚军和");
                cpOrderContentResult17.setOrderState(cpbjscResult.getdata_302116());
                cpOrderContentResult17.setOrderId("3021-16");
                cpOrderContentResultList2.add(cpOrderContentResult17);

                CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                cpOrderContentResult18.setOrderName("17");
                cpOrderContentResult18.setFullName("冠、亚军和");
                cpOrderContentResult18.setOrderState(cpbjscResult.getdata_302117());
                cpOrderContentResult18.setOrderId("3021-17");
                cpOrderContentResultList2.add(cpOrderContentResult18);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setShowNumber(2);
                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult19 = new CPOrderContentResult();
                cpOrderContentResult19.setOrderName("18");
                cpOrderContentResult19.setFullName("冠、亚军和");
                cpOrderContentResult19.setOrderState(cpbjscResult.getdata_302118());
                cpOrderContentResult19.setOrderId("3021-18");
                cpOrderContentResultList3.add(cpOrderContentResult19);

                CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                cpOrderContentResult20.setOrderName("19");
                cpOrderContentResult20.setFullName("冠、亚军和");
                cpOrderContentResult20.setOrderState(cpbjscResult.getdata_302119());
                cpOrderContentResult20.setOrderId("3021-19");
                cpOrderContentResultList3.add(cpOrderContentResult20);
                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                cpOrderContentListResult.setData(cpOrderContentResultList);
                cpOrderContentListResult2.setData(cpOrderContentResultList2);
                cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            return CPOrderContentListResult;
            }else if(k==2){
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("冠军");
                cpOrderContentListResult.setShowType("TU");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("冠军");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30011());
                cpOrderContentResult1.setOrderId("3001-1");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("冠军");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30012());
                cpOrderContentResult2.setOrderId("3001-2");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("冠军");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30013());
                cpOrderContentResult3.setOrderId("3001-3");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("冠军");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30014());
                cpOrderContentResult4.setOrderId("3001-4");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("冠军");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30015());
                cpOrderContentResult5.setOrderId("3001-5");
                cpOrderContentResultList1.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("冠军");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30016());
                cpOrderContentResult6.setOrderId("3001-6");
                cpOrderContentResultList1.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("冠军");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30017());
                cpOrderContentResult7.setOrderId("3001-7");
                cpOrderContentResultList1.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("冠军");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30018());
                cpOrderContentResult8.setOrderId("3001-8");
                cpOrderContentResultList1.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("冠军");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30019());
                cpOrderContentResult9.setOrderId("3001-9");
                cpOrderContentResultList1.add(cpOrderContentResult9);


                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("10");
                cpOrderContentResult10.setFullName("冠军");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_300110());
                cpOrderContentResult10.setOrderId("3001-10");
                cpOrderContentResultList1.add(cpOrderContentResult10);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("亚军");
                cpOrderContentListResult2.setShowType("TU");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("亚军");
                cpOrderContentResult21.setOrderState(cpbjscResult.getdata_30021());
                cpOrderContentResult21.setOrderId("3002-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("亚军");
                cpOrderContentResult22.setOrderState(cpbjscResult.getdata_30022());
                cpOrderContentResult22.setOrderId("3002-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("亚军");
                cpOrderContentResult23.setOrderState(cpbjscResult.getdata_30023());
                cpOrderContentResult23.setOrderId("3002-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("亚军");
                cpOrderContentResult24.setOrderState(cpbjscResult.getdata_30024());
                cpOrderContentResult24.setOrderId("3002-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("亚军");
                cpOrderContentResult25.setOrderState(cpbjscResult.getdata_30025());
                cpOrderContentResult25.setOrderId("3001-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);

                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("亚军");
                cpOrderContentResult26.setOrderState(cpbjscResult.getdata_30026());
                cpOrderContentResult26.setOrderId("3002-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);

                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("亚军");
                cpOrderContentResult27.setOrderState(cpbjscResult.getdata_30027());
                cpOrderContentResult27.setOrderId("3002-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);

                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("亚军");
                cpOrderContentResult28.setOrderState(cpbjscResult.getdata_30028());
                cpOrderContentResult28.setOrderId("3002-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);

                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("亚军");
                cpOrderContentResult29.setOrderState(cpbjscResult.getdata_30029());
                cpOrderContentResult29.setOrderId("3002-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);


                CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
                cpOrderContentResult210.setOrderName("10");
                cpOrderContentResult210.setFullName("亚军");
                cpOrderContentResult210.setOrderState(cpbjscResult.getdata_300210());
                cpOrderContentResult210.setOrderId("3002-10");
                cpOrderContentResultList2.add(cpOrderContentResult210);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第三名");
                cpOrderContentListResult3.setShowType("TU");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第三名");
                cpOrderContentResult31.setOrderState(cpbjscResult.getdata_30031());
                cpOrderContentResult31.setOrderId("3003-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第三名");
                cpOrderContentResult32.setOrderState(cpbjscResult.getdata_30032());
                cpOrderContentResult32.setOrderId("3003-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第三名");
                cpOrderContentResult33.setOrderState(cpbjscResult.getdata_30033());
                cpOrderContentResult33.setOrderId("3003-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第三名");
                cpOrderContentResult34.setOrderState(cpbjscResult.getdata_30034());
                cpOrderContentResult34.setOrderId("3003-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第三名");
                cpOrderContentResult35.setOrderState(cpbjscResult.getdata_30035());
                cpOrderContentResult35.setOrderId("3003-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);

                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第三名");
                cpOrderContentResult36.setOrderState(cpbjscResult.getdata_30036());
                cpOrderContentResult36.setOrderId("3003-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第三名");
                cpOrderContentResult37.setOrderState(cpbjscResult.getdata_30037());
                cpOrderContentResult37.setOrderId("3003-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第三名");
                cpOrderContentResult38.setOrderState(cpbjscResult.getdata_30038());
                cpOrderContentResult38.setOrderId("3003-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第三名");
                cpOrderContentResult39.setOrderState(cpbjscResult.getdata_30039());
                cpOrderContentResult39.setOrderId("3003-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);


                CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
                cpOrderContentResult310.setOrderName("10");
                cpOrderContentResult310.setFullName("第三名");
                cpOrderContentResult310.setOrderState(cpbjscResult.getdata_300310());
                cpOrderContentResult310.setOrderId("3003-10");
                cpOrderContentResultList3.add(cpOrderContentResult310);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第四名");
                cpOrderContentListResult4.setShowType("TU");
                cpOrderContentListResult4.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第四名");
                cpOrderContentResult41.setOrderState(cpbjscResult.getdata_30041());
                cpOrderContentResult41.setOrderId("3004-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第四名");
                cpOrderContentResult42.setOrderState(cpbjscResult.getdata_30042());
                cpOrderContentResult42.setOrderId("3004-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);


                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第四名");
                cpOrderContentResult43.setOrderState(cpbjscResult.getdata_30043());
                cpOrderContentResult43.setOrderId("3004-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第四名");
                cpOrderContentResult44.setOrderState(cpbjscResult.getdata_30044());
                cpOrderContentResult44.setOrderId("3004-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第四名");
                cpOrderContentResult45.setOrderState(cpbjscResult.getdata_30045());
                cpOrderContentResult45.setOrderId("3004-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第四名");
                cpOrderContentResult46.setOrderState(cpbjscResult.getdata_30046());
                cpOrderContentResult46.setOrderId("3004-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第四名");
                cpOrderContentResult47.setOrderState(cpbjscResult.getdata_30047());
                cpOrderContentResult47.setOrderId("3004-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第四名");
                cpOrderContentResult48.setOrderState(cpbjscResult.getdata_30048());
                cpOrderContentResult48.setOrderId("3004-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第四名");
                cpOrderContentResult49.setOrderState(cpbjscResult.getdata_30049());
                cpOrderContentResult49.setOrderId("3004-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);


                CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                cpOrderContentResult410.setOrderName("10");
                cpOrderContentResult410.setFullName("第四名");
                cpOrderContentResult410.setOrderState(cpbjscResult.getdata_300410());
                cpOrderContentResult410.setOrderId("3004-10");
                cpOrderContentResultList4.add(cpOrderContentResult410);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);

                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第五名");
                cpOrderContentListResult5.setShowType("TU");
                cpOrderContentListResult5.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第五名");
                cpOrderContentResult51.setOrderState(cpbjscResult.getdata_30051());
                cpOrderContentResult51.setOrderId("3005-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第五名");
                cpOrderContentResult52.setOrderState(cpbjscResult.getdata_30052());
                cpOrderContentResult52.setOrderId("3005-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);


                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第五名");
                cpOrderContentResult53.setOrderState(cpbjscResult.getdata_30053());
                cpOrderContentResult53.setOrderId("3005-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第五名");
                cpOrderContentResult54.setOrderState(cpbjscResult.getdata_30054());
                cpOrderContentResult54.setOrderId("3005-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第五名");
                cpOrderContentResult55.setOrderState(cpbjscResult.getdata_30055());
                cpOrderContentResult55.setOrderId("3005-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第五名");
                cpOrderContentResult56.setOrderState(cpbjscResult.getdata_30056());
                cpOrderContentResult56.setOrderId("3005-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第五名");
                cpOrderContentResult57.setOrderState(cpbjscResult.getdata_30057());
                cpOrderContentResult57.setOrderId("3005-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第五名");
                cpOrderContentResult58.setOrderState(cpbjscResult.getdata_30058());
                cpOrderContentResult58.setOrderId("3005-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第五名");
                cpOrderContentResult59.setOrderState(cpbjscResult.getdata_30059());
                cpOrderContentResult59.setOrderId("3005-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);


                CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
                cpOrderContentResult510.setOrderName("10");
                cpOrderContentResult510.setFullName("第五名");
                cpOrderContentResult510.setOrderState(cpbjscResult.getdata_300410());
                cpOrderContentResult510.setOrderId("3004-10");
                cpOrderContentResultList5.add(cpOrderContentResult510);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            CPOrderContentListResult.add(cpOrderContentListResult4);
            CPOrderContentListResult.add(cpOrderContentListResult5);
            return CPOrderContentListResult;
            }else{
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("第六名");
                cpOrderContentListResult.setShowType("TU");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("第六名");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30061());
                cpOrderContentResult1.setOrderId("3006-1");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("第六名");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30062());
                cpOrderContentResult2.setOrderId("3006-2");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("第六名");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30063());
                cpOrderContentResult3.setOrderId("3006-3");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("第六名");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30064());
                cpOrderContentResult4.setOrderId("3006-4");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("第六名");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30065());
                cpOrderContentResult5.setOrderId("3006-5");
                cpOrderContentResultList1.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("第六名");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30066());
                cpOrderContentResult6.setOrderId("3006-6");
                cpOrderContentResultList1.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("第六名");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30067());
                cpOrderContentResult7.setOrderId("3006-7");
                cpOrderContentResultList1.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("第六名");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30068());
                cpOrderContentResult8.setOrderId("3006-8");
                cpOrderContentResultList1.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("第六名");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30069());
                cpOrderContentResult9.setOrderId("3006-9");
                cpOrderContentResultList1.add(cpOrderContentResult9);


                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("10");
                cpOrderContentResult10.setFullName("第六名");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_300610());
                cpOrderContentResult10.setOrderId("3006-10");
                cpOrderContentResultList1.add(cpOrderContentResult10);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("第七名");
                cpOrderContentListResult2.setShowType("TU");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("第七名");
                cpOrderContentResult21.setOrderState(cpbjscResult.getdata_30071());
                cpOrderContentResult21.setOrderId("3007-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("第七名");
                cpOrderContentResult22.setOrderState(cpbjscResult.getdata_30072());
                cpOrderContentResult22.setOrderId("3007-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("第七名");
                cpOrderContentResult23.setOrderState(cpbjscResult.getdata_30073());
                cpOrderContentResult23.setOrderId("3007-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("第七名");
                cpOrderContentResult24.setOrderState(cpbjscResult.getdata_30074());
                cpOrderContentResult24.setOrderId("3007-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("第七名");
                cpOrderContentResult25.setOrderState(cpbjscResult.getdata_30075());
                cpOrderContentResult25.setOrderId("3007-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);

                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("第七名");
                cpOrderContentResult26.setOrderState(cpbjscResult.getdata_30076());
                cpOrderContentResult26.setOrderId("3007-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);

                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("第七名");
                cpOrderContentResult27.setOrderState(cpbjscResult.getdata_30077());
                cpOrderContentResult27.setOrderId("3007-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);

                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("第七名");
                cpOrderContentResult28.setOrderState(cpbjscResult.getdata_30078());
                cpOrderContentResult28.setOrderId("3007-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);

                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("第七名");
                cpOrderContentResult29.setOrderState(cpbjscResult.getdata_30079());
                cpOrderContentResult29.setOrderId("3007-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);


                CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
                cpOrderContentResult210.setOrderName("10");
                cpOrderContentResult210.setFullName("第七名");
                cpOrderContentResult210.setOrderState(cpbjscResult.getdata_300710());
                cpOrderContentResult210.setOrderId("3007-10");
                cpOrderContentResultList2.add(cpOrderContentResult210);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第八名");
                cpOrderContentListResult3.setShowType("TU");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第八名");
                cpOrderContentResult31.setOrderState(cpbjscResult.getdata_30081());
                cpOrderContentResult31.setOrderId("3008-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第八名");
                cpOrderContentResult32.setOrderState(cpbjscResult.getdata_30082());
                cpOrderContentResult32.setOrderId("3008-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第八名");
                cpOrderContentResult33.setOrderState(cpbjscResult.getdata_30083());
                cpOrderContentResult33.setOrderId("3008-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第八名");
                cpOrderContentResult34.setOrderState(cpbjscResult.getdata_30084());
                cpOrderContentResult34.setOrderId("3008-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第八名");
                cpOrderContentResult35.setOrderState(cpbjscResult.getdata_30085());
                cpOrderContentResult35.setOrderId("3008-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);

                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第八名");
                cpOrderContentResult36.setOrderState(cpbjscResult.getdata_30086());
                cpOrderContentResult36.setOrderId("3008-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第八名");
                cpOrderContentResult37.setOrderState(cpbjscResult.getdata_30037());
                cpOrderContentResult37.setOrderId("3003-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第八名");
                cpOrderContentResult38.setOrderState(cpbjscResult.getdata_30088());
                cpOrderContentResult38.setOrderId("3008-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第八名");
                cpOrderContentResult39.setOrderState(cpbjscResult.getdata_30089());
                cpOrderContentResult39.setOrderId("3008-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);


                CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
                cpOrderContentResult310.setOrderName("10");
                cpOrderContentResult310.setFullName("第八名");
                cpOrderContentResult310.setOrderState(cpbjscResult.getdata_300810());
                cpOrderContentResult310.setOrderId("3008-10");
                cpOrderContentResultList3.add(cpOrderContentResult310);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第九名");
                cpOrderContentListResult4.setShowType("TU");
                cpOrderContentListResult4.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第九名");
                cpOrderContentResult41.setOrderState(cpbjscResult.getdata_30091());
                cpOrderContentResult41.setOrderId("3009-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第九名");
                cpOrderContentResult42.setOrderState(cpbjscResult.getdata_30092());
                cpOrderContentResult42.setOrderId("3009-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);


                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第九名");
                cpOrderContentResult43.setOrderState(cpbjscResult.getdata_30093());
                cpOrderContentResult43.setOrderId("3009-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第九名");
                cpOrderContentResult44.setOrderState(cpbjscResult.getdata_30094());
                cpOrderContentResult44.setOrderId("3009-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第九名");
                cpOrderContentResult45.setOrderState(cpbjscResult.getdata_30095());
                cpOrderContentResult45.setOrderId("3009-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第九名");
                cpOrderContentResult46.setOrderState(cpbjscResult.getdata_30096());
                cpOrderContentResult46.setOrderId("3009-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第九名");
                cpOrderContentResult47.setOrderState(cpbjscResult.getdata_30097());
                cpOrderContentResult47.setOrderId("3009-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第九名");
                cpOrderContentResult48.setOrderState(cpbjscResult.getdata_30098());
                cpOrderContentResult48.setOrderId("3009-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第九名");
                cpOrderContentResult49.setOrderState(cpbjscResult.getdata_30099());
                cpOrderContentResult49.setOrderId("3009-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);


                CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                cpOrderContentResult410.setOrderName("10");
                cpOrderContentResult410.setFullName("第九名");
                cpOrderContentResult410.setOrderState(cpbjscResult.getdata_300910());
                cpOrderContentResult410.setOrderId("3009-10");
                cpOrderContentResultList4.add(cpOrderContentResult410);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);

                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第十名");
                cpOrderContentListResult5.setShowType("TU");
                cpOrderContentListResult5.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第十名");
                cpOrderContentResult51.setOrderState(cpbjscResult.getdata_30101());
                cpOrderContentResult51.setOrderId("3010-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第十名");
                cpOrderContentResult52.setOrderState(cpbjscResult.getdata_30102());
                cpOrderContentResult52.setOrderId("3010-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);


                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第十名");
                cpOrderContentResult53.setOrderState(cpbjscResult.getdata_30103());
                cpOrderContentResult53.setOrderId("3010-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第十名");
                cpOrderContentResult54.setOrderState(cpbjscResult.getdata_30104());
                cpOrderContentResult54.setOrderId("3010-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第十名");
                cpOrderContentResult55.setOrderState(cpbjscResult.getdata_30105());
                cpOrderContentResult55.setOrderId("3010-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第十名");
                cpOrderContentResult56.setOrderState(cpbjscResult.getdata_30106());
                cpOrderContentResult56.setOrderId("3010-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第十名");
                cpOrderContentResult57.setOrderState(cpbjscResult.getdata_30107());
                cpOrderContentResult57.setOrderId("3010-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第十名");
                cpOrderContentResult58.setOrderState(cpbjscResult.getdata_30108());
                cpOrderContentResult58.setOrderId("3010-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第十名");
                cpOrderContentResult59.setOrderState(cpbjscResult.getdata_30109());
                cpOrderContentResult59.setOrderId("3010-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);


                CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
                cpOrderContentResult510.setOrderName("10");
                cpOrderContentResult510.setFullName("第十名");
                cpOrderContentResult510.setOrderState(cpbjscResult.getdata_301010());
                cpOrderContentResult510.setOrderId("3010-10");
                cpOrderContentResultList5.add(cpOrderContentResult510);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            CPOrderContentListResult.add(cpOrderContentListResult4);
            CPOrderContentListResult.add(cpOrderContentListResult5);
            return CPOrderContentListResult;
            }
        }

    private List<CPOrderContentListResult>   JSSC(CPJSSCResult cpbjscResult,int k){
        List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
        if(k==0){
            for (int l = 0; l < 11; ++l) {
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                switch (l) {
                    case 0:
                        cpOrderContentListResult.setOrderContentListName("冠亚和");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("冠军大");
                                    cpOrderContentResult0.setFullName("冠、亚军和");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3217());
                                    cpOrderContentResult0.setOrderId("3217");
                                    cpOrderContentResultList.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("冠军小");
                                    cpOrderContentResult1.setFullName("冠、亚军和");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3218());
                                    cpOrderContentResult1.setOrderId("3218");
                                    cpOrderContentResultList.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("冠军单");
                                    cpOrderContentResult2.setFullName("冠、亚军和");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3219());
                                    cpOrderContentResult2.setOrderId("3219");
                                    cpOrderContentResultList.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("冠军双");
                                    cpOrderContentResult3.setFullName("冠、亚军和");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3220());
                                    cpOrderContentResult3.setOrderId("3220");
                                    cpOrderContentResultList.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 1:
                        cpOrderContentListResult.setOrderContentListName("冠军");
                        cpOrderContentListResult.setShowNumber(3);
                        List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                        for (int j = 0; j < 6; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("冠军");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32013213());
                                    cpOrderContentResult0.setOrderId("3201-3213");
                                    cpOrderContentResultList1.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("冠军");
                                    cpOrderContentResult1.setOrderId("3201-3211");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32013211());
                                    cpOrderContentResultList1.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("龙");
                                    cpOrderContentResult2.setFullName("冠军");
                                    cpOrderContentResult2.setOrderId("3201-3215");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32013215());
                                    cpOrderContentResultList1.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("双");
                                    cpOrderContentResult3.setFullName("冠军");
                                    cpOrderContentResult3.setOrderId("3201-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32013214());
                                    cpOrderContentResultList1.add(cpOrderContentResult3);
                                    break;
                                case 4:
                                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                    cpOrderContentResult4.setOrderName("小");
                                    cpOrderContentResult4.setFullName("冠军");
                                    cpOrderContentResult4.setOrderId("3201-3212");
                                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32013212());
                                    cpOrderContentResultList1.add(cpOrderContentResult4);
                                    break;
                                case 5:
                                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                    cpOrderContentResult5.setOrderName("虎");
                                    cpOrderContentResult5.setFullName("冠军");
                                    cpOrderContentResult5.setOrderId("3201-3216");
                                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32013216());
                                    cpOrderContentResultList1.add(cpOrderContentResult5);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList1);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 2:
                        cpOrderContentListResult.setOrderContentListName("亚军");
                        cpOrderContentListResult.setShowNumber(3);
                        List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                        for (int j = 0; j < 6; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("亚军");
                                    cpOrderContentResult0.setOrderId("3202-3213");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32023213());
                                    cpOrderContentResultList2.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("亚军");
                                    cpOrderContentResult1.setOrderId("3202-3211");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32023211());
                                    cpOrderContentResultList2.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("龙");
                                    cpOrderContentResult2.setFullName("亚军");
                                    cpOrderContentResult2.setOrderId("3202-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32023213());
                                    cpOrderContentResultList2.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("双");
                                    cpOrderContentResult3.setFullName("亚军");
                                    cpOrderContentResult3.setOrderId("3202-3215");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32023215());
                                    cpOrderContentResultList2.add(cpOrderContentResult3);
                                    break;
                                case 4:
                                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                    cpOrderContentResult4.setOrderName("小");
                                    cpOrderContentResult4.setFullName("亚军");
                                    cpOrderContentResult4.setOrderId("3202-3215");
                                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32023215());
                                    cpOrderContentResultList2.add(cpOrderContentResult4);
                                    break;
                                case 5:
                                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                    cpOrderContentResult5.setOrderName("虎");
                                    cpOrderContentResult5.setFullName("亚军");
                                    cpOrderContentResult5.setOrderId("3202-3216");
                                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32023216());
                                    cpOrderContentResultList2.add(cpOrderContentResult5);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList2);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 3:
                        cpOrderContentListResult.setOrderContentListName("第三名");
                        cpOrderContentListResult.setShowNumber(3);
                        List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                        for (int j = 0; j < 6; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第三名");
                                    cpOrderContentResult0.setOrderId("3203-3213");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32033213());
                                    cpOrderContentResultList3.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第三名");
                                    cpOrderContentResult1.setOrderId("3203-3211");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32033211());
                                    cpOrderContentResultList3.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("龙");
                                    cpOrderContentResult2.setFullName("第三名");
                                    cpOrderContentResult2.setOrderId("3203-3215");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32033215());
                                    cpOrderContentResultList3.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("双");
                                    cpOrderContentResult3.setFullName("第三名");
                                    cpOrderContentResult3.setOrderId("3203-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32033214());
                                    cpOrderContentResultList3.add(cpOrderContentResult3);
                                    break;
                                case 4:
                                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                    cpOrderContentResult4.setOrderName("小");
                                    cpOrderContentResult4.setFullName("第三名");
                                    cpOrderContentResult4.setOrderId("3203-3212");
                                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32033212());
                                    cpOrderContentResultList3.add(cpOrderContentResult4);
                                    break;
                                case 5:
                                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                    cpOrderContentResult5.setOrderName("虎");
                                    cpOrderContentResult5.setFullName("第三名");
                                    cpOrderContentResult5.setOrderId("3203-3216");
                                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32033216());
                                    cpOrderContentResultList3.add(cpOrderContentResult5);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList3);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 4:
                        cpOrderContentListResult.setOrderContentListName("第四名");
                        cpOrderContentListResult.setShowNumber(3);
                        List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                        for (int j = 0; j < 6; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第四名");
                                    cpOrderContentResult0.setOrderId("3204-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32043211());
                                    cpOrderContentResultList4.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第四名");
                                    cpOrderContentResult1.setOrderId("3204-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32043212());
                                    cpOrderContentResultList4.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("龙");
                                    cpOrderContentResult2.setFullName("第四名");
                                    cpOrderContentResult2.setOrderId("3204-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32043213());
                                    cpOrderContentResultList4.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("双");
                                    cpOrderContentResult3.setFullName("第四名");
                                    cpOrderContentResult3.setOrderId("3204-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32043214());
                                    cpOrderContentResultList4.add(cpOrderContentResult3);
                                    break;
                                case 4:
                                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                    cpOrderContentResult4.setOrderName("小");
                                    cpOrderContentResult4.setFullName("第四名");
                                    cpOrderContentResult4.setOrderId("3204-3215");
                                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32043215());
                                    cpOrderContentResultList4.add(cpOrderContentResult4);
                                    break;
                                case 5:
                                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                    cpOrderContentResult5.setOrderName("虎");
                                    cpOrderContentResult5.setFullName("第四名");
                                    cpOrderContentResult5.setOrderId("3204-3216");
                                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32043216());
                                    cpOrderContentResultList4.add(cpOrderContentResult5);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList4);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 5:
                        cpOrderContentListResult.setOrderContentListName("第五名");
                        cpOrderContentListResult.setShowNumber(3);
                        List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                        for (int j = 0; j < 6; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第五名");
                                    cpOrderContentResult0.setOrderId("3205-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32053211());
                                    cpOrderContentResultList5.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第五名");
                                    cpOrderContentResult1.setOrderId("3205-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32053212());
                                    cpOrderContentResultList5.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("龙");
                                    cpOrderContentResult2.setFullName("第五名");
                                    cpOrderContentResult2.setOrderId("3205-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32053213());
                                    cpOrderContentResultList5.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("双");
                                    cpOrderContentResult3.setFullName("第五名");
                                    cpOrderContentResult3.setOrderId("3205-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32053214());
                                    cpOrderContentResultList5.add(cpOrderContentResult3);
                                    break;
                                case 4:
                                    CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                    cpOrderContentResult4.setOrderName("小");
                                    cpOrderContentResult4.setFullName("第五名");
                                    cpOrderContentResult4.setOrderId("3205-3215");
                                    cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32053215());
                                    cpOrderContentResultList5.add(cpOrderContentResult4);
                                    break;
                                case 5:
                                    CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                    cpOrderContentResult5.setOrderName("虎");
                                    cpOrderContentResult5.setFullName("第五名");
                                    cpOrderContentResult5.setOrderId("3205-3216");
                                    cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32053216());
                                    cpOrderContentResultList5.add(cpOrderContentResult5);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList5);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 6:
                        cpOrderContentListResult.setOrderContentListName("第六名");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第六名");
                                    cpOrderContentResult0.setOrderId("3206-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32063211());
                                    cpOrderContentResultList6.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第六名");
                                    cpOrderContentResult1.setOrderId("3206-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32063212());
                                    cpOrderContentResultList6.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("双");
                                    cpOrderContentResult2.setFullName("第六名");
                                    cpOrderContentResult2.setOrderId("3206-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32063213());
                                    cpOrderContentResultList6.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("小");
                                    cpOrderContentResult3.setFullName("第六名");
                                    cpOrderContentResult3.setOrderId("3206-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32063214());
                                    cpOrderContentResultList6.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList6);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 7:
                        cpOrderContentListResult.setOrderContentListName("第七名");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第七名");
                                    cpOrderContentResult0.setOrderId("3207-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32073211());
                                    cpOrderContentResultList7.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第七名");
                                    cpOrderContentResult1.setOrderId("3207-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32073212());
                                    cpOrderContentResultList7.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("双");
                                    cpOrderContentResult2.setFullName("第七名");
                                    cpOrderContentResult2.setOrderId("3207-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32073213());
                                    cpOrderContentResultList7.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("小");
                                    cpOrderContentResult3.setFullName("第七名");
                                    cpOrderContentResult3.setOrderId("3207-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32073214());
                                    cpOrderContentResultList7.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList7);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 8:
                        cpOrderContentListResult.setOrderContentListName("第八名");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第八名");
                                    cpOrderContentResult0.setOrderId("3208-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32083211());
                                    cpOrderContentResultList8.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第八名");
                                    cpOrderContentResult1.setOrderId("3208-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32083212());
                                    cpOrderContentResultList8.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("双");
                                    cpOrderContentResult2.setFullName("第八名");
                                    cpOrderContentResult2.setOrderId("3208-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32083213());
                                    cpOrderContentResultList8.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("小");
                                    cpOrderContentResult3.setFullName("第八名");
                                    cpOrderContentResult3.setOrderId("3208-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32083214());
                                    cpOrderContentResultList8.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList8);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 9:
                        cpOrderContentListResult.setOrderContentListName("第九名");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList9 = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第九名");
                                    cpOrderContentResult0.setOrderId("3209-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32093211());
                                    cpOrderContentResultList9.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第九名");
                                    cpOrderContentResult1.setOrderId("3209-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32093212());
                                    cpOrderContentResultList9.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("双");
                                    cpOrderContentResult2.setFullName("第九名");
                                    cpOrderContentResult2.setOrderId("3209-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32093213());
                                    cpOrderContentResultList9.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("小");
                                    cpOrderContentResult3.setFullName("第九名");
                                    cpOrderContentResult3.setOrderId("3209-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32093214());
                                    cpOrderContentResultList9.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList9);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                    case 10:
                        cpOrderContentListResult.setOrderContentListName("第十名");
                        cpOrderContentListResult.setShowNumber(2);
                        List<CPOrderContentResult> cpOrderContentResultList10 = new ArrayList<>();
                        for (int j = 0; j < 4; ++j) {
                            switch (j) {
                                case 0:
                                    CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                    cpOrderContentResult0.setOrderName("单");
                                    cpOrderContentResult0.setFullName("第十名");
                                    cpOrderContentResult0.setOrderId("3210-3211");
                                    cpOrderContentResult0.setOrderState(cpbjscResult.getdata_32103211());
                                    cpOrderContentResultList10.add(cpOrderContentResult0);
                                    break;
                                case 1:
                                    CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                    cpOrderContentResult1.setOrderName("大");
                                    cpOrderContentResult1.setFullName("第十名");
                                    cpOrderContentResult1.setOrderId("3210-3212");
                                    cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32103212());
                                    cpOrderContentResultList10.add(cpOrderContentResult1);
                                    break;
                                case 2:
                                    CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                    cpOrderContentResult2.setOrderName("双");
                                    cpOrderContentResult2.setFullName("第十名");
                                    cpOrderContentResult2.setOrderId("3210-3213");
                                    cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32103213());
                                    cpOrderContentResultList10.add(cpOrderContentResult2);
                                    break;
                                case 3:
                                    CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                    cpOrderContentResult3.setOrderName("小");
                                    cpOrderContentResult3.setFullName("第十名");
                                    cpOrderContentResult3.setOrderId("3210-3214");
                                    cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32103214());
                                    cpOrderContentResultList10.add(cpOrderContentResult3);
                                    break;
                            }
                        }
                        cpOrderContentListResult.setData(cpOrderContentResultList10);
                        CPOrderContentListResult.add(cpOrderContentListResult);
                        break;
                }
            }
            return CPOrderContentListResult;
        }else if(k==1){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("冠、亚军 组合");
            cpOrderContentListResult.setShowNumber(2);
            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
            cpOrderContentResult0.setOrderName("冠军大");
            cpOrderContentResult0.setFullName("冠、亚军和");
            cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3217());
            cpOrderContentResult0.setOrderId("3217");
            cpOrderContentResultList.add(cpOrderContentResult0);

            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("冠军小");
            cpOrderContentResult1.setFullName("冠、亚军和");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3218());
            cpOrderContentResult1.setOrderId("3218");
            cpOrderContentResultList.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("冠军单");
            cpOrderContentResult2.setFullName("冠、亚军和");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3219());
            cpOrderContentResult2.setOrderId("3219");
            cpOrderContentResultList.add(cpOrderContentResult2);

            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("冠军双");
            cpOrderContentResult3.setFullName("冠、亚军和");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3220());
            cpOrderContentResult3.setOrderId("3220");
            cpOrderContentResultList.add(cpOrderContentResult3);


            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setShowNumber(3);
            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("3");
            cpOrderContentResult4.setFullName("冠、亚军和");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32213());
            cpOrderContentResult4.setOrderId("3221-3");
            cpOrderContentResultList2.add(cpOrderContentResult4);

            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("4");
            cpOrderContentResult5.setFullName("冠、亚军和");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32214());
            cpOrderContentResult5.setOrderId("3221-4");
            cpOrderContentResultList2.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("5");
            cpOrderContentResult6.setFullName("冠、亚军和");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata_32215());
            cpOrderContentResult6.setOrderId("3221-5");
            cpOrderContentResultList2.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("6");
            cpOrderContentResult7.setFullName("冠、亚军和");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata_32216());
            cpOrderContentResult7.setOrderId("3221-6");
            cpOrderContentResultList2.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("7");
            cpOrderContentResult8.setFullName("冠、亚军和");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata_32217());
            cpOrderContentResult8.setOrderId("3221-7");
            cpOrderContentResultList2.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("8");
            cpOrderContentResult9.setFullName("冠、亚军和");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata_32218());
            cpOrderContentResult9.setOrderId("3221-8");
            cpOrderContentResultList2.add(cpOrderContentResult9);

            CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
            cpOrderContentResult10.setOrderName("9");
            cpOrderContentResult10.setFullName("冠、亚军和");
            cpOrderContentResult10.setOrderState(cpbjscResult.getdata_32219());
            cpOrderContentResult10.setOrderId("3221-9");
            cpOrderContentResultList2.add(cpOrderContentResult10);

            CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
            cpOrderContentResult11.setOrderName("10");
            cpOrderContentResult11.setFullName("冠、亚军和");
            cpOrderContentResult11.setOrderState(cpbjscResult.getdata_322110());
            cpOrderContentResult11.setOrderId("3221-10");
            cpOrderContentResultList2.add(cpOrderContentResult11);

            CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
            cpOrderContentResult12.setOrderName("11");
            cpOrderContentResult12.setFullName("冠、亚军和");
            cpOrderContentResult12.setOrderState(cpbjscResult.getdata_322111());
            cpOrderContentResult12.setOrderId("3221-11");
            cpOrderContentResultList2.add(cpOrderContentResult12);

            CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
            cpOrderContentResult13.setOrderName("12");
            cpOrderContentResult13.setFullName("冠、亚军和");
            cpOrderContentResult13.setOrderState(cpbjscResult.getdata_322112());
            cpOrderContentResult13.setOrderId("3221-12");
            cpOrderContentResultList2.add(cpOrderContentResult13);

            CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
            cpOrderContentResult14.setOrderName("13");
            cpOrderContentResult14.setFullName("冠、亚军和");
            cpOrderContentResult14.setOrderState(cpbjscResult.getdata_322113());
            cpOrderContentResult14.setOrderId("3221-13");
            cpOrderContentResultList2.add(cpOrderContentResult14);

            CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
            cpOrderContentResult15.setOrderName("14");
            cpOrderContentResult15.setFullName("冠、亚军和");
            cpOrderContentResult15.setOrderState(cpbjscResult.getdata_322114());
            cpOrderContentResult15.setOrderId("3221-14");
            cpOrderContentResultList2.add(cpOrderContentResult15);

            CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
            cpOrderContentResult16.setOrderName("15");
            cpOrderContentResult16.setFullName("冠、亚军和");
            cpOrderContentResult16.setOrderState(cpbjscResult.getdata_322115());
            cpOrderContentResult16.setOrderId("3221-15");
            cpOrderContentResultList2.add(cpOrderContentResult16);

            CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
            cpOrderContentResult17.setOrderName("16");
            cpOrderContentResult17.setFullName("冠、亚军和");
            cpOrderContentResult17.setOrderState(cpbjscResult.getdata_322116());
            cpOrderContentResult17.setOrderId("3221-16");
            cpOrderContentResultList2.add(cpOrderContentResult17);

            CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
            cpOrderContentResult18.setOrderName("17");
            cpOrderContentResult18.setFullName("冠、亚军和");
            cpOrderContentResult18.setOrderState(cpbjscResult.getdata_322117());
            cpOrderContentResult18.setOrderId("3221-17");
            cpOrderContentResultList2.add(cpOrderContentResult18);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setShowNumber(2);
            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult19 = new CPOrderContentResult();
            cpOrderContentResult19.setOrderName("18");
            cpOrderContentResult19.setFullName("冠、亚军和");
            cpOrderContentResult19.setOrderState(cpbjscResult.getdata_322118());
            cpOrderContentResult19.setOrderId("3221-18");
            cpOrderContentResultList3.add(cpOrderContentResult19);

            CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
            cpOrderContentResult20.setOrderName("19");
            cpOrderContentResult20.setFullName("冠、亚军和");
            cpOrderContentResult20.setOrderState(cpbjscResult.getdata_322119());
            cpOrderContentResult20.setOrderId("3221-19");
            cpOrderContentResultList3.add(cpOrderContentResult20);
            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            cpOrderContentListResult.setData(cpOrderContentResultList);
            cpOrderContentListResult2.setData(cpOrderContentResultList2);
            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            return CPOrderContentListResult;
        }else if(k==2){
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("冠军");
            cpOrderContentListResult.setShowType("TU");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("冠军");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32011());
            cpOrderContentResult1.setOrderId("3201-1");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("冠军");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32012());
            cpOrderContentResult2.setOrderId("3201-2");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("冠军");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32013());
            cpOrderContentResult3.setOrderId("3201-3");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("冠军");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32014());
            cpOrderContentResult4.setOrderId("3201-4");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("冠军");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32015());
            cpOrderContentResult5.setOrderId("3201-5");
            cpOrderContentResultList1.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("冠军");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata_32016());
            cpOrderContentResult6.setOrderId("3201-6");
            cpOrderContentResultList1.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("冠军");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata_32017());
            cpOrderContentResult7.setOrderId("3201-7");
            cpOrderContentResultList1.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("冠军");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata_32018());
            cpOrderContentResult8.setOrderId("3201-8");
            cpOrderContentResultList1.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("冠军");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata_32019());
            cpOrderContentResult9.setOrderId("3201-9");
            cpOrderContentResultList1.add(cpOrderContentResult9);


            CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
            cpOrderContentResult10.setOrderName("10");
            cpOrderContentResult10.setFullName("冠军");
            cpOrderContentResult10.setOrderState(cpbjscResult.getdata_320110());
            cpOrderContentResult10.setOrderId("3201-10");
            cpOrderContentResultList1.add(cpOrderContentResult10);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("亚军");
            cpOrderContentListResult2.setShowType("TU");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("亚军");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata_32021());
            cpOrderContentResult21.setOrderId("3202-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("亚军");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata_32022());
            cpOrderContentResult22.setOrderId("3202-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("亚军");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata_32023());
            cpOrderContentResult23.setOrderId("3202-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("亚军");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata_32024());
            cpOrderContentResult24.setOrderId("3202-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("亚军");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata_32025());
            cpOrderContentResult25.setOrderId("3201-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);

            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("亚军");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata_32026());
            cpOrderContentResult26.setOrderId("3202-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);

            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("亚军");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata_32027());
            cpOrderContentResult27.setOrderId("3202-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);

            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("亚军");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata_32028());
            cpOrderContentResult28.setOrderId("3202-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);

            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("亚军");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata_32029());
            cpOrderContentResult29.setOrderId("3202-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);


            CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
            cpOrderContentResult210.setOrderName("10");
            cpOrderContentResult210.setFullName("亚军");
            cpOrderContentResult210.setOrderState(cpbjscResult.getdata_320210());
            cpOrderContentResult210.setOrderId("3202-10");
            cpOrderContentResultList2.add(cpOrderContentResult210);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第三名");
            cpOrderContentListResult3.setShowType("TU");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第三名");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata_32031());
            cpOrderContentResult31.setOrderId("3203-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第三名");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata_32032());
            cpOrderContentResult32.setOrderId("3203-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第三名");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata_32033());
            cpOrderContentResult33.setOrderId("3203-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第三名");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata_32034());
            cpOrderContentResult34.setOrderId("3203-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第三名");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata_32035());
            cpOrderContentResult35.setOrderId("3203-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);

            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第三名");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata_32036());
            cpOrderContentResult36.setOrderId("3203-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第三名");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata_32037());
            cpOrderContentResult37.setOrderId("3203-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第三名");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata_32038());
            cpOrderContentResult38.setOrderId("3203-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第三名");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata_32039());
            cpOrderContentResult39.setOrderId("3203-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);


            CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
            cpOrderContentResult310.setOrderName("10");
            cpOrderContentResult310.setFullName("第三名");
            cpOrderContentResult310.setOrderState(cpbjscResult.getdata_320310());
            cpOrderContentResult310.setOrderId("3203-10");
            cpOrderContentResultList3.add(cpOrderContentResult310);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第四名");
            cpOrderContentListResult4.setShowType("TU");
            cpOrderContentListResult4.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第四名");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata_32041());
            cpOrderContentResult41.setOrderId("3204-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第四名");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata_32042());
            cpOrderContentResult42.setOrderId("3204-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);


            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第四名");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata_32043());
            cpOrderContentResult43.setOrderId("3204-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第四名");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata_32044());
            cpOrderContentResult44.setOrderId("3204-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第四名");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata_32045());
            cpOrderContentResult45.setOrderId("3204-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第四名");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata_32046());
            cpOrderContentResult46.setOrderId("3204-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第四名");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata_32047());
            cpOrderContentResult47.setOrderId("3204-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第四名");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata_32048());
            cpOrderContentResult48.setOrderId("3204-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第四名");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata_32049());
            cpOrderContentResult49.setOrderId("3204-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);


            CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
            cpOrderContentResult410.setOrderName("10");
            cpOrderContentResult410.setFullName("第四名");
            cpOrderContentResult410.setOrderState(cpbjscResult.getdata_320410());
            cpOrderContentResult410.setOrderId("3204-10");
            cpOrderContentResultList4.add(cpOrderContentResult410);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);

            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第五名");
            cpOrderContentListResult5.setShowType("TU");
            cpOrderContentListResult5.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第五名");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata_32051());
            cpOrderContentResult51.setOrderId("3205-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第五名");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata_32052());
            cpOrderContentResult52.setOrderId("3205-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);


            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第五名");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata_32053());
            cpOrderContentResult53.setOrderId("3205-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第五名");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata_32054());
            cpOrderContentResult54.setOrderId("3205-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第五名");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata_32055());
            cpOrderContentResult55.setOrderId("3205-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第五名");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata_32056());
            cpOrderContentResult56.setOrderId("3205-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第五名");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata_32057());
            cpOrderContentResult57.setOrderId("3205-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第五名");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata_32058());
            cpOrderContentResult58.setOrderId("3205-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第五名");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata_32059());
            cpOrderContentResult59.setOrderId("3205-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);


            CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
            cpOrderContentResult510.setOrderName("10");
            cpOrderContentResult510.setFullName("第五名");
            cpOrderContentResult510.setOrderState(cpbjscResult.getdata_320410());
            cpOrderContentResult510.setOrderId("3204-10");
            cpOrderContentResultList5.add(cpOrderContentResult510);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            CPOrderContentListResult.add(cpOrderContentListResult4);
            CPOrderContentListResult.add(cpOrderContentListResult5);
            return CPOrderContentListResult;
        }else{
            CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
            cpOrderContentListResult.setOrderContentListName("第六名");
            cpOrderContentListResult.setShowType("TU");
            cpOrderContentListResult.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
            cpOrderContentResult1.setOrderName("1");
            cpOrderContentResult1.setFullName("第六名");
            cpOrderContentResult1.setOrderState(cpbjscResult.getdata_32061());
            cpOrderContentResult1.setOrderId("3206-1");
            cpOrderContentResultList1.add(cpOrderContentResult1);

            CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
            cpOrderContentResult2.setOrderName("2");
            cpOrderContentResult2.setFullName("第六名");
            cpOrderContentResult2.setOrderState(cpbjscResult.getdata_32062());
            cpOrderContentResult2.setOrderId("3206-2");
            cpOrderContentResultList1.add(cpOrderContentResult2);


            CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
            cpOrderContentResult3.setOrderName("3");
            cpOrderContentResult3.setFullName("第六名");
            cpOrderContentResult3.setOrderState(cpbjscResult.getdata_32063());
            cpOrderContentResult3.setOrderId("3206-3");
            cpOrderContentResultList1.add(cpOrderContentResult3);

            CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
            cpOrderContentResult4.setOrderName("4");
            cpOrderContentResult4.setFullName("第六名");
            cpOrderContentResult4.setOrderState(cpbjscResult.getdata_32064());
            cpOrderContentResult4.setOrderId("3206-4");
            cpOrderContentResultList1.add(cpOrderContentResult4);

            CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
            cpOrderContentResult5.setOrderName("5");
            cpOrderContentResult5.setFullName("第六名");
            cpOrderContentResult5.setOrderState(cpbjscResult.getdata_32065());
            cpOrderContentResult5.setOrderId("3206-5");
            cpOrderContentResultList1.add(cpOrderContentResult5);

            CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
            cpOrderContentResult6.setOrderName("6");
            cpOrderContentResult6.setFullName("第六名");
            cpOrderContentResult6.setOrderState(cpbjscResult.getdata_32066());
            cpOrderContentResult6.setOrderId("3206-6");
            cpOrderContentResultList1.add(cpOrderContentResult6);

            CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
            cpOrderContentResult7.setOrderName("7");
            cpOrderContentResult7.setFullName("第六名");
            cpOrderContentResult7.setOrderState(cpbjscResult.getdata_32067());
            cpOrderContentResult7.setOrderId("3206-7");
            cpOrderContentResultList1.add(cpOrderContentResult7);

            CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
            cpOrderContentResult8.setOrderName("8");
            cpOrderContentResult8.setFullName("第六名");
            cpOrderContentResult8.setOrderState(cpbjscResult.getdata_32068());
            cpOrderContentResult8.setOrderId("3206-8");
            cpOrderContentResultList1.add(cpOrderContentResult8);

            CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
            cpOrderContentResult9.setOrderName("9");
            cpOrderContentResult9.setFullName("第六名");
            cpOrderContentResult9.setOrderState(cpbjscResult.getdata_32069());
            cpOrderContentResult9.setOrderId("3206-9");
            cpOrderContentResultList1.add(cpOrderContentResult9);


            CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
            cpOrderContentResult10.setOrderName("10");
            cpOrderContentResult10.setFullName("第六名");
            cpOrderContentResult10.setOrderState(cpbjscResult.getdata_320610());
            cpOrderContentResult10.setOrderId("3206-10");
            cpOrderContentResultList1.add(cpOrderContentResult10);

            cpOrderContentListResult.setData(cpOrderContentResultList1);

            CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
            cpOrderContentListResult2.setOrderContentListName("第七名");
            cpOrderContentListResult2.setShowType("TU");
            cpOrderContentListResult2.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
            cpOrderContentResult21.setOrderName("1");
            cpOrderContentResult21.setFullName("第七名");
            cpOrderContentResult21.setOrderState(cpbjscResult.getdata_32071());
            cpOrderContentResult21.setOrderId("3207-1");
            cpOrderContentResultList2.add(cpOrderContentResult21);

            CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
            cpOrderContentResult22.setOrderName("2");
            cpOrderContentResult22.setFullName("第七名");
            cpOrderContentResult22.setOrderState(cpbjscResult.getdata_32072());
            cpOrderContentResult22.setOrderId("3207-2");
            cpOrderContentResultList2.add(cpOrderContentResult22);


            CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
            cpOrderContentResult23.setOrderName("3");
            cpOrderContentResult23.setFullName("第七名");
            cpOrderContentResult23.setOrderState(cpbjscResult.getdata_32073());
            cpOrderContentResult23.setOrderId("3207-3");
            cpOrderContentResultList2.add(cpOrderContentResult23);

            CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
            cpOrderContentResult24.setOrderName("4");
            cpOrderContentResult24.setFullName("第七名");
            cpOrderContentResult24.setOrderState(cpbjscResult.getdata_32074());
            cpOrderContentResult24.setOrderId("3207-4");
            cpOrderContentResultList2.add(cpOrderContentResult24);

            CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
            cpOrderContentResult25.setOrderName("5");
            cpOrderContentResult25.setFullName("第七名");
            cpOrderContentResult25.setOrderState(cpbjscResult.getdata_32075());
            cpOrderContentResult25.setOrderId("3207-5");
            cpOrderContentResultList2.add(cpOrderContentResult25);

            CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
            cpOrderContentResult26.setOrderName("6");
            cpOrderContentResult26.setFullName("第七名");
            cpOrderContentResult26.setOrderState(cpbjscResult.getdata_32076());
            cpOrderContentResult26.setOrderId("3207-6");
            cpOrderContentResultList2.add(cpOrderContentResult26);

            CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
            cpOrderContentResult27.setOrderName("7");
            cpOrderContentResult27.setFullName("第七名");
            cpOrderContentResult27.setOrderState(cpbjscResult.getdata_32077());
            cpOrderContentResult27.setOrderId("3207-7");
            cpOrderContentResultList2.add(cpOrderContentResult27);

            CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
            cpOrderContentResult28.setOrderName("8");
            cpOrderContentResult28.setFullName("第七名");
            cpOrderContentResult28.setOrderState(cpbjscResult.getdata_32078());
            cpOrderContentResult28.setOrderId("3207-8");
            cpOrderContentResultList2.add(cpOrderContentResult28);

            CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
            cpOrderContentResult29.setOrderName("9");
            cpOrderContentResult29.setFullName("第七名");
            cpOrderContentResult29.setOrderState(cpbjscResult.getdata_32079());
            cpOrderContentResult29.setOrderId("3207-9");
            cpOrderContentResultList2.add(cpOrderContentResult29);


            CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
            cpOrderContentResult210.setOrderName("10");
            cpOrderContentResult210.setFullName("第七名");
            cpOrderContentResult210.setOrderState(cpbjscResult.getdata_320710());
            cpOrderContentResult210.setOrderId("3207-10");
            cpOrderContentResultList2.add(cpOrderContentResult210);

            cpOrderContentListResult2.setData(cpOrderContentResultList2);

            CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
            cpOrderContentListResult3.setOrderContentListName("第八名");
            cpOrderContentListResult3.setShowType("TU");
            cpOrderContentListResult3.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
            cpOrderContentResult31.setOrderName("1");
            cpOrderContentResult31.setFullName("第八名");
            cpOrderContentResult31.setOrderState(cpbjscResult.getdata_32081());
            cpOrderContentResult31.setOrderId("3208-1");
            cpOrderContentResultList3.add(cpOrderContentResult31);

            CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
            cpOrderContentResult32.setOrderName("2");
            cpOrderContentResult32.setFullName("第八名");
            cpOrderContentResult32.setOrderState(cpbjscResult.getdata_32082());
            cpOrderContentResult32.setOrderId("3208-2");
            cpOrderContentResultList3.add(cpOrderContentResult32);

            CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
            cpOrderContentResult33.setOrderName("3");
            cpOrderContentResult33.setFullName("第八名");
            cpOrderContentResult33.setOrderState(cpbjscResult.getdata_32083());
            cpOrderContentResult33.setOrderId("3208-3");
            cpOrderContentResultList3.add(cpOrderContentResult33);

            CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
            cpOrderContentResult34.setOrderName("4");
            cpOrderContentResult34.setFullName("第八名");
            cpOrderContentResult34.setOrderState(cpbjscResult.getdata_32084());
            cpOrderContentResult34.setOrderId("3208-4");
            cpOrderContentResultList3.add(cpOrderContentResult34);

            CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
            cpOrderContentResult35.setOrderName("5");
            cpOrderContentResult35.setFullName("第八名");
            cpOrderContentResult35.setOrderState(cpbjscResult.getdata_32085());
            cpOrderContentResult35.setOrderId("3208-5");
            cpOrderContentResultList3.add(cpOrderContentResult35);

            CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
            cpOrderContentResult36.setOrderName("6");
            cpOrderContentResult36.setFullName("第八名");
            cpOrderContentResult36.setOrderState(cpbjscResult.getdata_32086());
            cpOrderContentResult36.setOrderId("3208-6");
            cpOrderContentResultList3.add(cpOrderContentResult36);

            CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
            cpOrderContentResult37.setOrderName("7");
            cpOrderContentResult37.setFullName("第八名");
            cpOrderContentResult37.setOrderState(cpbjscResult.getdata_32037());
            cpOrderContentResult37.setOrderId("3203-7");
            cpOrderContentResultList3.add(cpOrderContentResult37);

            CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
            cpOrderContentResult38.setOrderName("8");
            cpOrderContentResult38.setFullName("第八名");
            cpOrderContentResult38.setOrderState(cpbjscResult.getdata_32088());
            cpOrderContentResult38.setOrderId("3208-8");
            cpOrderContentResultList3.add(cpOrderContentResult38);

            CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
            cpOrderContentResult39.setOrderName("9");
            cpOrderContentResult39.setFullName("第八名");
            cpOrderContentResult39.setOrderState(cpbjscResult.getdata_32089());
            cpOrderContentResult39.setOrderId("3208-9");
            cpOrderContentResultList3.add(cpOrderContentResult39);


            CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
            cpOrderContentResult310.setOrderName("10");
            cpOrderContentResult310.setFullName("第八名");
            cpOrderContentResult310.setOrderState(cpbjscResult.getdata_320810());
            cpOrderContentResult310.setOrderId("3208-10");
            cpOrderContentResultList3.add(cpOrderContentResult310);

            cpOrderContentListResult3.setData(cpOrderContentResultList3);

            CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
            cpOrderContentListResult4.setOrderContentListName("第九名");
            cpOrderContentListResult4.setShowType("TU");
            cpOrderContentListResult4.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
            cpOrderContentResult41.setOrderName("1");
            cpOrderContentResult41.setFullName("第九名");
            cpOrderContentResult41.setOrderState(cpbjscResult.getdata_32091());
            cpOrderContentResult41.setOrderId("3209-1");
            cpOrderContentResultList4.add(cpOrderContentResult41);

            CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
            cpOrderContentResult42.setOrderName("2");
            cpOrderContentResult42.setFullName("第九名");
            cpOrderContentResult42.setOrderState(cpbjscResult.getdata_32092());
            cpOrderContentResult42.setOrderId("3209-2");
            cpOrderContentResultList4.add(cpOrderContentResult42);


            CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
            cpOrderContentResult43.setOrderName("3");
            cpOrderContentResult43.setFullName("第九名");
            cpOrderContentResult43.setOrderState(cpbjscResult.getdata_32093());
            cpOrderContentResult43.setOrderId("3209-3");
            cpOrderContentResultList4.add(cpOrderContentResult43);

            CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
            cpOrderContentResult44.setOrderName("4");
            cpOrderContentResult44.setFullName("第九名");
            cpOrderContentResult44.setOrderState(cpbjscResult.getdata_32094());
            cpOrderContentResult44.setOrderId("3209-4");
            cpOrderContentResultList4.add(cpOrderContentResult44);

            CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
            cpOrderContentResult45.setOrderName("5");
            cpOrderContentResult45.setFullName("第九名");
            cpOrderContentResult45.setOrderState(cpbjscResult.getdata_32095());
            cpOrderContentResult45.setOrderId("3209-5");
            cpOrderContentResultList4.add(cpOrderContentResult45);

            CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
            cpOrderContentResult46.setOrderName("6");
            cpOrderContentResult46.setFullName("第九名");
            cpOrderContentResult46.setOrderState(cpbjscResult.getdata_32096());
            cpOrderContentResult46.setOrderId("3209-6");
            cpOrderContentResultList4.add(cpOrderContentResult46);

            CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
            cpOrderContentResult47.setOrderName("7");
            cpOrderContentResult47.setFullName("第九名");
            cpOrderContentResult47.setOrderState(cpbjscResult.getdata_32097());
            cpOrderContentResult47.setOrderId("3209-7");
            cpOrderContentResultList4.add(cpOrderContentResult47);

            CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
            cpOrderContentResult48.setOrderName("8");
            cpOrderContentResult48.setFullName("第九名");
            cpOrderContentResult48.setOrderState(cpbjscResult.getdata_32098());
            cpOrderContentResult48.setOrderId("3209-8");
            cpOrderContentResultList4.add(cpOrderContentResult48);

            CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
            cpOrderContentResult49.setOrderName("9");
            cpOrderContentResult49.setFullName("第九名");
            cpOrderContentResult49.setOrderState(cpbjscResult.getdata_32099());
            cpOrderContentResult49.setOrderId("3209-9");
            cpOrderContentResultList4.add(cpOrderContentResult49);


            CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
            cpOrderContentResult410.setOrderName("10");
            cpOrderContentResult410.setFullName("第九名");
            cpOrderContentResult410.setOrderState(cpbjscResult.getdata_320910());
            cpOrderContentResult410.setOrderId("3209-10");
            cpOrderContentResultList4.add(cpOrderContentResult410);

            cpOrderContentListResult4.setData(cpOrderContentResultList4);

            CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
            cpOrderContentListResult5.setOrderContentListName("第十名");
            cpOrderContentListResult5.setShowType("TU");
            cpOrderContentListResult5.setShowNumber(2);

            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
            CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
            cpOrderContentResult51.setOrderName("1");
            cpOrderContentResult51.setFullName("第十名");
            cpOrderContentResult51.setOrderState(cpbjscResult.getdata_32101());
            cpOrderContentResult51.setOrderId("3210-1");
            cpOrderContentResultList5.add(cpOrderContentResult51);

            CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
            cpOrderContentResult52.setOrderName("2");
            cpOrderContentResult52.setFullName("第十名");
            cpOrderContentResult52.setOrderState(cpbjscResult.getdata_32102());
            cpOrderContentResult52.setOrderId("3210-2");
            cpOrderContentResultList5.add(cpOrderContentResult52);


            CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
            cpOrderContentResult53.setOrderName("3");
            cpOrderContentResult53.setFullName("第十名");
            cpOrderContentResult53.setOrderState(cpbjscResult.getdata_32103());
            cpOrderContentResult53.setOrderId("3210-3");
            cpOrderContentResultList5.add(cpOrderContentResult53);

            CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
            cpOrderContentResult54.setOrderName("4");
            cpOrderContentResult54.setFullName("第十名");
            cpOrderContentResult54.setOrderState(cpbjscResult.getdata_32104());
            cpOrderContentResult54.setOrderId("3210-4");
            cpOrderContentResultList5.add(cpOrderContentResult54);

            CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
            cpOrderContentResult55.setOrderName("5");
            cpOrderContentResult55.setFullName("第十名");
            cpOrderContentResult55.setOrderState(cpbjscResult.getdata_32105());
            cpOrderContentResult55.setOrderId("3210-5");
            cpOrderContentResultList5.add(cpOrderContentResult55);

            CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
            cpOrderContentResult56.setOrderName("6");
            cpOrderContentResult56.setFullName("第十名");
            cpOrderContentResult56.setOrderState(cpbjscResult.getdata_32106());
            cpOrderContentResult56.setOrderId("3210-6");
            cpOrderContentResultList5.add(cpOrderContentResult56);

            CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
            cpOrderContentResult57.setOrderName("7");
            cpOrderContentResult57.setFullName("第十名");
            cpOrderContentResult57.setOrderState(cpbjscResult.getdata_32107());
            cpOrderContentResult57.setOrderId("3210-7");
            cpOrderContentResultList5.add(cpOrderContentResult57);

            CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
            cpOrderContentResult58.setOrderName("8");
            cpOrderContentResult58.setFullName("第十名");
            cpOrderContentResult58.setOrderState(cpbjscResult.getdata_32108());
            cpOrderContentResult58.setOrderId("3210-8");
            cpOrderContentResultList5.add(cpOrderContentResult58);

            CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
            cpOrderContentResult59.setOrderName("9");
            cpOrderContentResult59.setFullName("第十名");
            cpOrderContentResult59.setOrderState(cpbjscResult.getdata_32109());
            cpOrderContentResult59.setOrderId("3210-9");
            cpOrderContentResultList5.add(cpOrderContentResult59);


            CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
            cpOrderContentResult510.setOrderName("10");
            cpOrderContentResult510.setFullName("第十名");
            cpOrderContentResult510.setOrderState(cpbjscResult.getdata_321010());
            cpOrderContentResult510.setOrderId("3210-10");
            cpOrderContentResultList5.add(cpOrderContentResult510);

            cpOrderContentListResult5.setData(cpOrderContentResultList5);

            CPOrderContentListResult.add(cpOrderContentListResult);
            CPOrderContentListResult.add(cpOrderContentListResult2);
            CPOrderContentListResult.add(cpOrderContentListResult3);
            CPOrderContentListResult.add(cpOrderContentListResult4);
            CPOrderContentListResult.add(cpOrderContentListResult5);
            return CPOrderContentListResult;
        }
    }

    private void BJPK10(CPBJSCResult cpbjscResult){

        for (int k = 0; k < 4; ++k) {
            CPOrderAllResult allResult = new CPOrderAllResult();
            if(k==0){
                allResult.setEventChecked(true);
                List<CPOrderContentListResult> CPOrderContentListResult = new ArrayList<CPOrderContentListResult>();
                for (int l = 0; l < 11; ++l) {
                    CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                    switch (l) {
                        case 0:
                            cpOrderContentListResult.setOrderContentListName("冠亚和");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("冠军大");
                                        cpOrderContentResult0.setFullName("冠、亚军和");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3017());
                                        cpOrderContentResult0.setOrderId("3017");
                                        cpOrderContentResultList.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("冠军小");
                                        cpOrderContentResult1.setFullName("冠、亚军和");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3018());
                                        cpOrderContentResult1.setOrderId("3018");
                                        cpOrderContentResultList.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("冠军单");
                                        cpOrderContentResult2.setFullName("冠、亚军和");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3019());
                                        cpOrderContentResult2.setOrderId("3019");
                                        cpOrderContentResultList.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("冠军双");
                                        cpOrderContentResult3.setFullName("冠、亚军和");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3020());
                                        cpOrderContentResult3.setOrderId("3020");
                                        cpOrderContentResultList.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 1:
                            cpOrderContentListResult.setOrderContentListName("冠军");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("冠军");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30013011());
                                        cpOrderContentResult0.setOrderId("3001-3011");
                                        cpOrderContentResultList1.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("冠军");
                                        cpOrderContentResult1.setOrderId("3001-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30013012());
                                        cpOrderContentResultList1.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("冠军");
                                        cpOrderContentResult2.setOrderId("3001-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30013013());
                                        cpOrderContentResultList1.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("冠军");
                                        cpOrderContentResult3.setOrderId("3001-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30013014());
                                        cpOrderContentResultList1.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("冠军");
                                        cpOrderContentResult4.setOrderId("3001-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30013015());
                                        cpOrderContentResultList1.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("冠军");
                                        cpOrderContentResult5.setOrderId("3001-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30013016());
                                        cpOrderContentResultList1.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList1);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 2:
                            cpOrderContentListResult.setOrderContentListName("亚军");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("亚军");
                                        cpOrderContentResult0.setOrderId("3002-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30023011());
                                        cpOrderContentResultList2.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("亚军");
                                        cpOrderContentResult1.setOrderId("3002-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30023012());
                                        cpOrderContentResultList2.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("亚军");
                                        cpOrderContentResult2.setOrderId("3002-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30023013());
                                        cpOrderContentResultList2.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("亚军");
                                        cpOrderContentResult3.setOrderId("3002-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30023014());
                                        cpOrderContentResultList2.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("亚军");
                                        cpOrderContentResult4.setOrderId("3002-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30023015());
                                        cpOrderContentResultList2.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("亚军");
                                        cpOrderContentResult5.setOrderId("3002-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30023016());
                                        cpOrderContentResultList2.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList2);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 3:
                            cpOrderContentListResult.setOrderContentListName("第三名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第三名");
                                        cpOrderContentResult0.setOrderId("3003-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30033011());
                                        cpOrderContentResultList3.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第三名");
                                        cpOrderContentResult1.setOrderId("3003-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30033012());
                                        cpOrderContentResultList3.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第三名");
                                        cpOrderContentResult2.setOrderId("3003-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30033013());
                                        cpOrderContentResultList3.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第三名");
                                        cpOrderContentResult3.setOrderId("3003-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30033014());
                                        cpOrderContentResultList3.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第三名");
                                        cpOrderContentResult4.setOrderId("3003-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30033015());
                                        cpOrderContentResultList3.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第三名");
                                        cpOrderContentResult5.setOrderId("3003-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30033016());
                                        cpOrderContentResultList3.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList3);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 4:
                            cpOrderContentListResult.setOrderContentListName("第四名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第四名");
                                        cpOrderContentResult0.setOrderId("3004-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30043011());
                                        cpOrderContentResultList4.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第四名");
                                        cpOrderContentResult1.setOrderId("3004-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30043012());
                                        cpOrderContentResultList4.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第四名");
                                        cpOrderContentResult2.setOrderId("3004-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30043013());
                                        cpOrderContentResultList4.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第四名");
                                        cpOrderContentResult3.setOrderId("3004-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30043014());
                                        cpOrderContentResultList4.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第四名");
                                        cpOrderContentResult4.setOrderId("3004-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30043015());
                                        cpOrderContentResultList4.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第四名");
                                        cpOrderContentResult5.setOrderId("3004-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30043016());
                                        cpOrderContentResultList4.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList4);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 5:
                            cpOrderContentListResult.setOrderContentListName("第五名");
                            cpOrderContentListResult.setShowNumber(3);
                            List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                            for (int j = 0; j < 6; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第五名");
                                        cpOrderContentResult0.setOrderId("3005-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30053011());
                                        cpOrderContentResultList5.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第五名");
                                        cpOrderContentResult1.setOrderId("3005-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30053012());
                                        cpOrderContentResultList5.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("龙");
                                        cpOrderContentResult2.setFullName("第五名");
                                        cpOrderContentResult2.setOrderId("3005-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30053013());
                                        cpOrderContentResultList5.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("双");
                                        cpOrderContentResult3.setFullName("第五名");
                                        cpOrderContentResult3.setOrderId("3005-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30053014());
                                        cpOrderContentResultList5.add(cpOrderContentResult3);
                                        break;
                                    case 4:
                                        CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                                        cpOrderContentResult4.setOrderName("小");
                                        cpOrderContentResult4.setFullName("第五名");
                                        cpOrderContentResult4.setOrderId("3005-3015");
                                        cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30053015());
                                        cpOrderContentResultList5.add(cpOrderContentResult4);
                                        break;
                                    case 5:
                                        CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                                        cpOrderContentResult5.setOrderName("虎");
                                        cpOrderContentResult5.setFullName("第五名");
                                        cpOrderContentResult5.setOrderId("3005-3016");
                                        cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30053016());
                                        cpOrderContentResultList5.add(cpOrderContentResult5);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList5);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 6:
                            cpOrderContentListResult.setOrderContentListName("第六名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList6 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第六名");
                                        cpOrderContentResult0.setOrderId("3006-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30063011());
                                        cpOrderContentResultList6.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第六名");
                                        cpOrderContentResult1.setOrderId("3006-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30063012());
                                        cpOrderContentResultList6.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第六名");
                                        cpOrderContentResult2.setOrderId("3006-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30063013());
                                        cpOrderContentResultList6.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第六名");
                                        cpOrderContentResult3.setOrderId("3006-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30063014());
                                        cpOrderContentResultList6.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList6);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 7:
                            cpOrderContentListResult.setOrderContentListName("第七名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList7 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第七名");
                                        cpOrderContentResult0.setOrderId("3007-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30073011());
                                        cpOrderContentResultList7.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第七名");
                                        cpOrderContentResult1.setOrderId("3007-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30073012());
                                        cpOrderContentResultList7.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第七名");
                                        cpOrderContentResult2.setOrderId("3007-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30073013());
                                        cpOrderContentResultList7.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第七名");
                                        cpOrderContentResult3.setOrderId("3007-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30073014());
                                        cpOrderContentResultList7.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList7);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 8:
                            cpOrderContentListResult.setOrderContentListName("第八名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList8 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第八名");
                                        cpOrderContentResult0.setOrderId("3008-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30083011());
                                        cpOrderContentResultList8.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第八名");
                                        cpOrderContentResult1.setOrderId("3008-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30083012());
                                        cpOrderContentResultList8.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第八名");
                                        cpOrderContentResult2.setOrderId("3008-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30083013());
                                        cpOrderContentResultList8.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第八名");
                                        cpOrderContentResult3.setOrderId("3008-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30083014());
                                        cpOrderContentResultList8.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList8);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 9:
                            cpOrderContentListResult.setOrderContentListName("第九名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList9 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第九名");
                                        cpOrderContentResult0.setOrderId("3009-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30093011());
                                        cpOrderContentResultList9.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第九名");
                                        cpOrderContentResult1.setOrderId("3009-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30093012());
                                        cpOrderContentResultList9.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第九名");
                                        cpOrderContentResult2.setOrderId("3009-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30093013());
                                        cpOrderContentResultList9.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第九名");
                                        cpOrderContentResult3.setOrderId("3009-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30093014());
                                        cpOrderContentResultList9.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList9);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                        case 10:
                            cpOrderContentListResult.setOrderContentListName("第十名");
                            cpOrderContentListResult.setShowNumber(2);
                            List<CPOrderContentResult> cpOrderContentResultList10 = new ArrayList<>();
                            for (int j = 0; j < 4; ++j) {
                                switch (j) {
                                    case 0:
                                        CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                                        cpOrderContentResult0.setOrderName("单");
                                        cpOrderContentResult0.setFullName("第十名");
                                        cpOrderContentResult0.setOrderId("3010-3011");
                                        cpOrderContentResult0.setOrderState(cpbjscResult.getdata_30103011());
                                        cpOrderContentResultList10.add(cpOrderContentResult0);
                                        break;
                                    case 1:
                                        CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                                        cpOrderContentResult1.setOrderName("大");
                                        cpOrderContentResult1.setFullName("第十名");
                                        cpOrderContentResult1.setOrderId("3010-3012");
                                        cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30103012());
                                        cpOrderContentResultList10.add(cpOrderContentResult1);
                                        break;
                                    case 2:
                                        CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                                        cpOrderContentResult2.setOrderName("双");
                                        cpOrderContentResult2.setFullName("第十名");
                                        cpOrderContentResult2.setOrderId("3010-3013");
                                        cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30103013());
                                        cpOrderContentResultList10.add(cpOrderContentResult2);
                                        break;
                                    case 3:
                                        CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                                        cpOrderContentResult3.setOrderName("小");
                                        cpOrderContentResult3.setFullName("第十名");
                                        cpOrderContentResult3.setOrderId("3010-3014");
                                        cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30103014());
                                        cpOrderContentResultList10.add(cpOrderContentResult3);
                                        break;
                                }
                            }
                            cpOrderContentListResult.setData(cpOrderContentResultList10);
                            CPOrderContentListResult.add(cpOrderContentListResult);
                            break;
                    }
                }
                allResult.setOrderAllName("两面");
                allResult.setData(CPOrderContentListResult);
            }else if(k==1){

                List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
                allResult.setOrderAllName("冠亚和");
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("冠、亚军 组合");
                cpOrderContentListResult.setShowNumber(2);
                List<CPOrderContentResult> cpOrderContentResultList = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult0 = new CPOrderContentResult();
                cpOrderContentResult0.setOrderName("冠军大");
                cpOrderContentResult0.setFullName("冠、亚军和");
                cpOrderContentResult0.setOrderState(cpbjscResult.getdata_3017());
                cpOrderContentResult0.setOrderId("3017");
                cpOrderContentResultList.add(cpOrderContentResult0);

                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("冠军小");
                cpOrderContentResult1.setFullName("冠、亚军和");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_3018());
                cpOrderContentResult1.setOrderId("3018");
                cpOrderContentResultList.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("冠军单");
                cpOrderContentResult2.setFullName("冠、亚军和");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_3019());
                cpOrderContentResult2.setOrderId("3019");
                cpOrderContentResultList.add(cpOrderContentResult2);

                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("冠军双");
                cpOrderContentResult3.setFullName("冠、亚军和");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_3020());
                cpOrderContentResult3.setOrderId("3020");
                cpOrderContentResultList.add(cpOrderContentResult3);


                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setShowNumber(3);
                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("3");
                cpOrderContentResult4.setFullName("冠、亚军和");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30213());
                cpOrderContentResult4.setOrderId("3021-3");
                cpOrderContentResultList2.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("4");
                cpOrderContentResult5.setFullName("冠、亚军和");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30214());
                cpOrderContentResult5.setOrderId("3021-4");
                cpOrderContentResultList2.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("5");
                cpOrderContentResult6.setFullName("冠、亚军和");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30215());
                cpOrderContentResult6.setOrderId("3021-5");
                cpOrderContentResultList2.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("6");
                cpOrderContentResult7.setFullName("冠、亚军和");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30216());
                cpOrderContentResult7.setOrderId("3021-6");
                cpOrderContentResultList2.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("7");
                cpOrderContentResult8.setFullName("冠、亚军和");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30217());
                cpOrderContentResult8.setOrderId("3021-7");
                cpOrderContentResultList2.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("8");
                cpOrderContentResult9.setFullName("冠、亚军和");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30218());
                cpOrderContentResult9.setOrderId("3021-8");
                cpOrderContentResultList2.add(cpOrderContentResult9);

                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("9");
                cpOrderContentResult10.setFullName("冠、亚军和");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_30219());
                cpOrderContentResult10.setOrderId("3021-9");
                cpOrderContentResultList2.add(cpOrderContentResult10);

                CPOrderContentResult cpOrderContentResult11 = new CPOrderContentResult();
                cpOrderContentResult11.setOrderName("10");
                cpOrderContentResult11.setFullName("冠、亚军和");
                cpOrderContentResult11.setOrderState(cpbjscResult.getdata_302110());
                cpOrderContentResult11.setOrderId("3021-10");
                cpOrderContentResultList2.add(cpOrderContentResult11);

                CPOrderContentResult cpOrderContentResult12 = new CPOrderContentResult();
                cpOrderContentResult12.setOrderName("11");
                cpOrderContentResult12.setFullName("冠、亚军和");
                cpOrderContentResult12.setOrderState(cpbjscResult.getdata_302111());
                cpOrderContentResult12.setOrderId("3021-11");
                cpOrderContentResultList2.add(cpOrderContentResult12);

                CPOrderContentResult cpOrderContentResult13 = new CPOrderContentResult();
                cpOrderContentResult13.setOrderName("12");
                cpOrderContentResult13.setFullName("冠、亚军和");
                cpOrderContentResult13.setOrderState(cpbjscResult.getdata_302112());
                cpOrderContentResult13.setOrderId("3021-12");
                cpOrderContentResultList2.add(cpOrderContentResult13);

                CPOrderContentResult cpOrderContentResult14 = new CPOrderContentResult();
                cpOrderContentResult14.setOrderName("13");
                cpOrderContentResult14.setFullName("冠、亚军和");
                cpOrderContentResult14.setOrderState(cpbjscResult.getdata_302113());
                cpOrderContentResult14.setOrderId("3021-13");
                cpOrderContentResultList2.add(cpOrderContentResult14);

                CPOrderContentResult cpOrderContentResult15 = new CPOrderContentResult();
                cpOrderContentResult15.setOrderName("14");
                cpOrderContentResult15.setFullName("冠、亚军和");
                cpOrderContentResult15.setOrderState(cpbjscResult.getdata_302114());
                cpOrderContentResult15.setOrderId("3021-14");
                cpOrderContentResultList2.add(cpOrderContentResult15);

                CPOrderContentResult cpOrderContentResult16 = new CPOrderContentResult();
                cpOrderContentResult16.setOrderName("15");
                cpOrderContentResult16.setFullName("冠、亚军和");
                cpOrderContentResult16.setOrderState(cpbjscResult.getdata_302115());
                cpOrderContentResult16.setOrderId("3021-15");
                cpOrderContentResultList2.add(cpOrderContentResult16);

                CPOrderContentResult cpOrderContentResult17 = new CPOrderContentResult();
                cpOrderContentResult17.setOrderName("16");
                cpOrderContentResult17.setFullName("冠、亚军和");
                cpOrderContentResult17.setOrderState(cpbjscResult.getdata_302116());
                cpOrderContentResult17.setOrderId("3021-16");
                cpOrderContentResultList2.add(cpOrderContentResult17);

                CPOrderContentResult cpOrderContentResult18 = new CPOrderContentResult();
                cpOrderContentResult18.setOrderName("17");
                cpOrderContentResult18.setFullName("冠、亚军和");
                cpOrderContentResult18.setOrderState(cpbjscResult.getdata_302117());
                cpOrderContentResult18.setOrderId("3021-17");
                cpOrderContentResultList2.add(cpOrderContentResult18);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setShowNumber(2);
                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult19 = new CPOrderContentResult();
                cpOrderContentResult19.setOrderName("18");
                cpOrderContentResult19.setFullName("冠、亚军和");
                cpOrderContentResult19.setOrderState(cpbjscResult.getdata_302118());
                cpOrderContentResult19.setOrderId("3021-18");
                cpOrderContentResultList3.add(cpOrderContentResult19);

                CPOrderContentResult cpOrderContentResult20 = new CPOrderContentResult();
                cpOrderContentResult20.setOrderName("19");
                cpOrderContentResult20.setFullName("冠、亚军和");
                cpOrderContentResult20.setOrderState(cpbjscResult.getdata_302119());
                cpOrderContentResult20.setOrderId("3021-19");
                cpOrderContentResultList3.add(cpOrderContentResult20);
                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                cpOrderContentListResult.setData(cpOrderContentResultList);
                cpOrderContentListResult2.setData(cpOrderContentResultList2);
                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);

                allResult.setData(cPOrderContentListResultAll);
            }else if(k==2){
                allResult.setOrderAllName("1-5名");
                List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("冠军");
                cpOrderContentListResult.setShowType("TU");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("冠军");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30011());
                cpOrderContentResult1.setOrderId("3001-1");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("冠军");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30012());
                cpOrderContentResult2.setOrderId("3001-2");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("冠军");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30013());
                cpOrderContentResult3.setOrderId("3001-3");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("冠军");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30014());
                cpOrderContentResult4.setOrderId("3001-4");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("冠军");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30015());
                cpOrderContentResult5.setOrderId("3001-5");
                cpOrderContentResultList1.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("冠军");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30016());
                cpOrderContentResult6.setOrderId("3001-6");
                cpOrderContentResultList1.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("冠军");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30017());
                cpOrderContentResult7.setOrderId("3001-7");
                cpOrderContentResultList1.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("冠军");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30018());
                cpOrderContentResult8.setOrderId("3001-8");
                cpOrderContentResultList1.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("冠军");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30019());
                cpOrderContentResult9.setOrderId("3001-9");
                cpOrderContentResultList1.add(cpOrderContentResult9);


                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("10");
                cpOrderContentResult10.setFullName("冠军");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_300110());
                cpOrderContentResult10.setOrderId("3001-10");
                cpOrderContentResultList1.add(cpOrderContentResult10);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("亚军");
                cpOrderContentListResult2.setShowType("TU");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("亚军");
                cpOrderContentResult21.setOrderState(cpbjscResult.getdata_30021());
                cpOrderContentResult21.setOrderId("3002-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("亚军");
                cpOrderContentResult22.setOrderState(cpbjscResult.getdata_30022());
                cpOrderContentResult22.setOrderId("3002-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("亚军");
                cpOrderContentResult23.setOrderState(cpbjscResult.getdata_30023());
                cpOrderContentResult23.setOrderId("3002-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("亚军");
                cpOrderContentResult24.setOrderState(cpbjscResult.getdata_30024());
                cpOrderContentResult24.setOrderId("3002-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("亚军");
                cpOrderContentResult25.setOrderState(cpbjscResult.getdata_30025());
                cpOrderContentResult25.setOrderId("3001-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);

                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("亚军");
                cpOrderContentResult26.setOrderState(cpbjscResult.getdata_30026());
                cpOrderContentResult26.setOrderId("3002-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);

                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("亚军");
                cpOrderContentResult27.setOrderState(cpbjscResult.getdata_30027());
                cpOrderContentResult27.setOrderId("3002-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);

                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("亚军");
                cpOrderContentResult28.setOrderState(cpbjscResult.getdata_30028());
                cpOrderContentResult28.setOrderId("3002-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);

                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("亚军");
                cpOrderContentResult29.setOrderState(cpbjscResult.getdata_30029());
                cpOrderContentResult29.setOrderId("3002-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);


                CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
                cpOrderContentResult210.setOrderName("10");
                cpOrderContentResult210.setFullName("亚军");
                cpOrderContentResult210.setOrderState(cpbjscResult.getdata_300210());
                cpOrderContentResult210.setOrderId("3002-10");
                cpOrderContentResultList2.add(cpOrderContentResult210);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第三名");
                cpOrderContentListResult3.setShowType("TU");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第三名");
                cpOrderContentResult31.setOrderState(cpbjscResult.getdata_30031());
                cpOrderContentResult31.setOrderId("3003-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第三名");
                cpOrderContentResult32.setOrderState(cpbjscResult.getdata_30032());
                cpOrderContentResult32.setOrderId("3003-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第三名");
                cpOrderContentResult33.setOrderState(cpbjscResult.getdata_30033());
                cpOrderContentResult33.setOrderId("3003-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第三名");
                cpOrderContentResult34.setOrderState(cpbjscResult.getdata_30034());
                cpOrderContentResult34.setOrderId("3003-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第三名");
                cpOrderContentResult35.setOrderState(cpbjscResult.getdata_30035());
                cpOrderContentResult35.setOrderId("3003-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);

                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第三名");
                cpOrderContentResult36.setOrderState(cpbjscResult.getdata_30036());
                cpOrderContentResult36.setOrderId("3003-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第三名");
                cpOrderContentResult37.setOrderState(cpbjscResult.getdata_30037());
                cpOrderContentResult37.setOrderId("3003-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第三名");
                cpOrderContentResult38.setOrderState(cpbjscResult.getdata_30038());
                cpOrderContentResult38.setOrderId("3003-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第三名");
                cpOrderContentResult39.setOrderState(cpbjscResult.getdata_30039());
                cpOrderContentResult39.setOrderId("3003-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);


                CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
                cpOrderContentResult310.setOrderName("10");
                cpOrderContentResult310.setFullName("第三名");
                cpOrderContentResult310.setOrderState(cpbjscResult.getdata_300310());
                cpOrderContentResult310.setOrderId("3003-10");
                cpOrderContentResultList3.add(cpOrderContentResult310);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第四名");
                cpOrderContentListResult4.setShowType("TU");
                cpOrderContentListResult4.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第四名");
                cpOrderContentResult41.setOrderState(cpbjscResult.getdata_30041());
                cpOrderContentResult41.setOrderId("3004-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第四名");
                cpOrderContentResult42.setOrderState(cpbjscResult.getdata_30042());
                cpOrderContentResult42.setOrderId("3004-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);


                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第四名");
                cpOrderContentResult43.setOrderState(cpbjscResult.getdata_30043());
                cpOrderContentResult43.setOrderId("3004-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第四名");
                cpOrderContentResult44.setOrderState(cpbjscResult.getdata_30044());
                cpOrderContentResult44.setOrderId("3004-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第四名");
                cpOrderContentResult45.setOrderState(cpbjscResult.getdata_30045());
                cpOrderContentResult45.setOrderId("3004-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第四名");
                cpOrderContentResult46.setOrderState(cpbjscResult.getdata_30046());
                cpOrderContentResult46.setOrderId("3004-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第四名");
                cpOrderContentResult47.setOrderState(cpbjscResult.getdata_30047());
                cpOrderContentResult47.setOrderId("3004-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第四名");
                cpOrderContentResult48.setOrderState(cpbjscResult.getdata_30048());
                cpOrderContentResult48.setOrderId("3004-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第四名");
                cpOrderContentResult49.setOrderState(cpbjscResult.getdata_30049());
                cpOrderContentResult49.setOrderId("3004-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);


                CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                cpOrderContentResult410.setOrderName("10");
                cpOrderContentResult410.setFullName("第四名");
                cpOrderContentResult410.setOrderState(cpbjscResult.getdata_300410());
                cpOrderContentResult410.setOrderId("3004-10");
                cpOrderContentResultList4.add(cpOrderContentResult410);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);

                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第五名");
                cpOrderContentListResult5.setShowType("TU");
                cpOrderContentListResult5.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第五名");
                cpOrderContentResult51.setOrderState(cpbjscResult.getdata_30051());
                cpOrderContentResult51.setOrderId("3005-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第五名");
                cpOrderContentResult52.setOrderState(cpbjscResult.getdata_30052());
                cpOrderContentResult52.setOrderId("3005-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);


                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第五名");
                cpOrderContentResult53.setOrderState(cpbjscResult.getdata_30053());
                cpOrderContentResult53.setOrderId("3005-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第五名");
                cpOrderContentResult54.setOrderState(cpbjscResult.getdata_30054());
                cpOrderContentResult54.setOrderId("3005-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第五名");
                cpOrderContentResult55.setOrderState(cpbjscResult.getdata_30055());
                cpOrderContentResult55.setOrderId("3005-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第五名");
                cpOrderContentResult56.setOrderState(cpbjscResult.getdata_30056());
                cpOrderContentResult56.setOrderId("3005-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第五名");
                cpOrderContentResult57.setOrderState(cpbjscResult.getdata_30057());
                cpOrderContentResult57.setOrderId("3005-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第五名");
                cpOrderContentResult58.setOrderState(cpbjscResult.getdata_30058());
                cpOrderContentResult58.setOrderId("3005-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第五名");
                cpOrderContentResult59.setOrderState(cpbjscResult.getdata_30059());
                cpOrderContentResult59.setOrderId("3005-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);


                CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
                cpOrderContentResult510.setOrderName("10");
                cpOrderContentResult510.setFullName("第五名");
                cpOrderContentResult510.setOrderState(cpbjscResult.getdata_300410());
                cpOrderContentResult510.setOrderId("3004-10");
                cpOrderContentResultList5.add(cpOrderContentResult510);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult4);
                cPOrderContentListResultAll.add(cpOrderContentListResult5);
                allResult.setData(cPOrderContentListResultAll);
            }else{
                allResult.setOrderAllName("6-10名");
                List<CPOrderContentListResult> cPOrderContentListResultAll = new ArrayList<CPOrderContentListResult>();
                CPOrderContentListResult cpOrderContentListResult = new CPOrderContentListResult();
                cpOrderContentListResult.setOrderContentListName("第六名");
                cpOrderContentListResult.setShowType("TU");
                cpOrderContentListResult.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList1 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult1 = new CPOrderContentResult();
                cpOrderContentResult1.setOrderName("1");
                cpOrderContentResult1.setFullName("第六名");
                cpOrderContentResult1.setOrderState(cpbjscResult.getdata_30061());
                cpOrderContentResult1.setOrderId("3006-1");
                cpOrderContentResultList1.add(cpOrderContentResult1);

                CPOrderContentResult cpOrderContentResult2 = new CPOrderContentResult();
                cpOrderContentResult2.setOrderName("2");
                cpOrderContentResult2.setFullName("第六名");
                cpOrderContentResult2.setOrderState(cpbjscResult.getdata_30062());
                cpOrderContentResult2.setOrderId("3006-2");
                cpOrderContentResultList1.add(cpOrderContentResult2);


                CPOrderContentResult cpOrderContentResult3 = new CPOrderContentResult();
                cpOrderContentResult3.setOrderName("3");
                cpOrderContentResult3.setFullName("第六名");
                cpOrderContentResult3.setOrderState(cpbjscResult.getdata_30063());
                cpOrderContentResult3.setOrderId("3006-3");
                cpOrderContentResultList1.add(cpOrderContentResult3);

                CPOrderContentResult cpOrderContentResult4 = new CPOrderContentResult();
                cpOrderContentResult4.setOrderName("4");
                cpOrderContentResult4.setFullName("第六名");
                cpOrderContentResult4.setOrderState(cpbjscResult.getdata_30064());
                cpOrderContentResult4.setOrderId("3006-4");
                cpOrderContentResultList1.add(cpOrderContentResult4);

                CPOrderContentResult cpOrderContentResult5 = new CPOrderContentResult();
                cpOrderContentResult5.setOrderName("5");
                cpOrderContentResult5.setFullName("第六名");
                cpOrderContentResult5.setOrderState(cpbjscResult.getdata_30065());
                cpOrderContentResult5.setOrderId("3006-5");
                cpOrderContentResultList1.add(cpOrderContentResult5);

                CPOrderContentResult cpOrderContentResult6 = new CPOrderContentResult();
                cpOrderContentResult6.setOrderName("6");
                cpOrderContentResult6.setFullName("第六名");
                cpOrderContentResult6.setOrderState(cpbjscResult.getdata_30066());
                cpOrderContentResult6.setOrderId("3006-6");
                cpOrderContentResultList1.add(cpOrderContentResult6);

                CPOrderContentResult cpOrderContentResult7 = new CPOrderContentResult();
                cpOrderContentResult7.setOrderName("7");
                cpOrderContentResult7.setFullName("第六名");
                cpOrderContentResult7.setOrderState(cpbjscResult.getdata_30067());
                cpOrderContentResult7.setOrderId("3006-7");
                cpOrderContentResultList1.add(cpOrderContentResult7);

                CPOrderContentResult cpOrderContentResult8 = new CPOrderContentResult();
                cpOrderContentResult8.setOrderName("8");
                cpOrderContentResult8.setFullName("第六名");
                cpOrderContentResult8.setOrderState(cpbjscResult.getdata_30068());
                cpOrderContentResult8.setOrderId("3006-8");
                cpOrderContentResultList1.add(cpOrderContentResult8);

                CPOrderContentResult cpOrderContentResult9 = new CPOrderContentResult();
                cpOrderContentResult9.setOrderName("9");
                cpOrderContentResult9.setFullName("第六名");
                cpOrderContentResult9.setOrderState(cpbjscResult.getdata_30069());
                cpOrderContentResult9.setOrderId("3006-9");
                cpOrderContentResultList1.add(cpOrderContentResult9);


                CPOrderContentResult cpOrderContentResult10 = new CPOrderContentResult();
                cpOrderContentResult10.setOrderName("10");
                cpOrderContentResult10.setFullName("第六名");
                cpOrderContentResult10.setOrderState(cpbjscResult.getdata_300610());
                cpOrderContentResult10.setOrderId("3006-10");
                cpOrderContentResultList1.add(cpOrderContentResult10);

                cpOrderContentListResult.setData(cpOrderContentResultList1);

                CPOrderContentListResult cpOrderContentListResult2 = new CPOrderContentListResult();
                cpOrderContentListResult2.setOrderContentListName("第七名");
                cpOrderContentListResult2.setShowType("TU");
                cpOrderContentListResult2.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList2 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult21 = new CPOrderContentResult();
                cpOrderContentResult21.setOrderName("1");
                cpOrderContentResult21.setFullName("第七名");
                cpOrderContentResult21.setOrderState(cpbjscResult.getdata_30071());
                cpOrderContentResult21.setOrderId("3007-1");
                cpOrderContentResultList2.add(cpOrderContentResult21);

                CPOrderContentResult cpOrderContentResult22 = new CPOrderContentResult();
                cpOrderContentResult22.setOrderName("2");
                cpOrderContentResult22.setFullName("第七名");
                cpOrderContentResult22.setOrderState(cpbjscResult.getdata_30072());
                cpOrderContentResult22.setOrderId("3007-2");
                cpOrderContentResultList2.add(cpOrderContentResult22);


                CPOrderContentResult cpOrderContentResult23 = new CPOrderContentResult();
                cpOrderContentResult23.setOrderName("3");
                cpOrderContentResult23.setFullName("第七名");
                cpOrderContentResult23.setOrderState(cpbjscResult.getdata_30073());
                cpOrderContentResult23.setOrderId("3007-3");
                cpOrderContentResultList2.add(cpOrderContentResult23);

                CPOrderContentResult cpOrderContentResult24 = new CPOrderContentResult();
                cpOrderContentResult24.setOrderName("4");
                cpOrderContentResult24.setFullName("第七名");
                cpOrderContentResult24.setOrderState(cpbjscResult.getdata_30074());
                cpOrderContentResult24.setOrderId("3007-4");
                cpOrderContentResultList2.add(cpOrderContentResult24);

                CPOrderContentResult cpOrderContentResult25 = new CPOrderContentResult();
                cpOrderContentResult25.setOrderName("5");
                cpOrderContentResult25.setFullName("第七名");
                cpOrderContentResult25.setOrderState(cpbjscResult.getdata_30075());
                cpOrderContentResult25.setOrderId("3007-5");
                cpOrderContentResultList2.add(cpOrderContentResult25);

                CPOrderContentResult cpOrderContentResult26 = new CPOrderContentResult();
                cpOrderContentResult26.setOrderName("6");
                cpOrderContentResult26.setFullName("第七名");
                cpOrderContentResult26.setOrderState(cpbjscResult.getdata_30076());
                cpOrderContentResult26.setOrderId("3007-6");
                cpOrderContentResultList2.add(cpOrderContentResult26);

                CPOrderContentResult cpOrderContentResult27 = new CPOrderContentResult();
                cpOrderContentResult27.setOrderName("7");
                cpOrderContentResult27.setFullName("第七名");
                cpOrderContentResult27.setOrderState(cpbjscResult.getdata_30077());
                cpOrderContentResult27.setOrderId("3007-7");
                cpOrderContentResultList2.add(cpOrderContentResult27);

                CPOrderContentResult cpOrderContentResult28 = new CPOrderContentResult();
                cpOrderContentResult28.setOrderName("8");
                cpOrderContentResult28.setFullName("第七名");
                cpOrderContentResult28.setOrderState(cpbjscResult.getdata_30078());
                cpOrderContentResult28.setOrderId("3007-8");
                cpOrderContentResultList2.add(cpOrderContentResult28);

                CPOrderContentResult cpOrderContentResult29 = new CPOrderContentResult();
                cpOrderContentResult29.setOrderName("9");
                cpOrderContentResult29.setFullName("第七名");
                cpOrderContentResult29.setOrderState(cpbjscResult.getdata_30079());
                cpOrderContentResult29.setOrderId("3007-9");
                cpOrderContentResultList2.add(cpOrderContentResult29);


                CPOrderContentResult cpOrderContentResult210 = new CPOrderContentResult();
                cpOrderContentResult210.setOrderName("10");
                cpOrderContentResult210.setFullName("第七名");
                cpOrderContentResult210.setOrderState(cpbjscResult.getdata_300710());
                cpOrderContentResult210.setOrderId("3007-10");
                cpOrderContentResultList2.add(cpOrderContentResult210);

                cpOrderContentListResult2.setData(cpOrderContentResultList2);

                CPOrderContentListResult cpOrderContentListResult3 = new CPOrderContentListResult();
                cpOrderContentListResult3.setOrderContentListName("第八名");
                cpOrderContentListResult3.setShowType("TU");
                cpOrderContentListResult3.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList3 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult31 = new CPOrderContentResult();
                cpOrderContentResult31.setOrderName("1");
                cpOrderContentResult31.setFullName("第八名");
                cpOrderContentResult31.setOrderState(cpbjscResult.getdata_30081());
                cpOrderContentResult31.setOrderId("3008-1");
                cpOrderContentResultList3.add(cpOrderContentResult31);

                CPOrderContentResult cpOrderContentResult32 = new CPOrderContentResult();
                cpOrderContentResult32.setOrderName("2");
                cpOrderContentResult32.setFullName("第八名");
                cpOrderContentResult32.setOrderState(cpbjscResult.getdata_30082());
                cpOrderContentResult32.setOrderId("3008-2");
                cpOrderContentResultList3.add(cpOrderContentResult32);

                CPOrderContentResult cpOrderContentResult33 = new CPOrderContentResult();
                cpOrderContentResult33.setOrderName("3");
                cpOrderContentResult33.setFullName("第八名");
                cpOrderContentResult33.setOrderState(cpbjscResult.getdata_30083());
                cpOrderContentResult33.setOrderId("3008-3");
                cpOrderContentResultList3.add(cpOrderContentResult33);

                CPOrderContentResult cpOrderContentResult34 = new CPOrderContentResult();
                cpOrderContentResult34.setOrderName("4");
                cpOrderContentResult34.setFullName("第八名");
                cpOrderContentResult34.setOrderState(cpbjscResult.getdata_30084());
                cpOrderContentResult34.setOrderId("3008-4");
                cpOrderContentResultList3.add(cpOrderContentResult34);

                CPOrderContentResult cpOrderContentResult35 = new CPOrderContentResult();
                cpOrderContentResult35.setOrderName("5");
                cpOrderContentResult35.setFullName("第八名");
                cpOrderContentResult35.setOrderState(cpbjscResult.getdata_30085());
                cpOrderContentResult35.setOrderId("3008-5");
                cpOrderContentResultList3.add(cpOrderContentResult35);

                CPOrderContentResult cpOrderContentResult36 = new CPOrderContentResult();
                cpOrderContentResult36.setOrderName("6");
                cpOrderContentResult36.setFullName("第八名");
                cpOrderContentResult36.setOrderState(cpbjscResult.getdata_30086());
                cpOrderContentResult36.setOrderId("3008-6");
                cpOrderContentResultList3.add(cpOrderContentResult36);

                CPOrderContentResult cpOrderContentResult37 = new CPOrderContentResult();
                cpOrderContentResult37.setOrderName("7");
                cpOrderContentResult37.setFullName("第八名");
                cpOrderContentResult37.setOrderState(cpbjscResult.getdata_30037());
                cpOrderContentResult37.setOrderId("3003-7");
                cpOrderContentResultList3.add(cpOrderContentResult37);

                CPOrderContentResult cpOrderContentResult38 = new CPOrderContentResult();
                cpOrderContentResult38.setOrderName("8");
                cpOrderContentResult38.setFullName("第八名");
                cpOrderContentResult38.setOrderState(cpbjscResult.getdata_30088());
                cpOrderContentResult38.setOrderId("3008-8");
                cpOrderContentResultList3.add(cpOrderContentResult38);

                CPOrderContentResult cpOrderContentResult39 = new CPOrderContentResult();
                cpOrderContentResult39.setOrderName("9");
                cpOrderContentResult39.setFullName("第八名");
                cpOrderContentResult39.setOrderState(cpbjscResult.getdata_30089());
                cpOrderContentResult39.setOrderId("3008-9");
                cpOrderContentResultList3.add(cpOrderContentResult39);


                CPOrderContentResult cpOrderContentResult310 = new CPOrderContentResult();
                cpOrderContentResult310.setOrderName("10");
                cpOrderContentResult310.setFullName("第八名");
                cpOrderContentResult310.setOrderState(cpbjscResult.getdata_300810());
                cpOrderContentResult310.setOrderId("3008-10");
                cpOrderContentResultList3.add(cpOrderContentResult310);

                cpOrderContentListResult3.setData(cpOrderContentResultList3);

                CPOrderContentListResult cpOrderContentListResult4 = new CPOrderContentListResult();
                cpOrderContentListResult4.setOrderContentListName("第九名");
                cpOrderContentListResult4.setShowType("TU");
                cpOrderContentListResult4.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList4 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult41 = new CPOrderContentResult();
                cpOrderContentResult41.setOrderName("1");
                cpOrderContentResult41.setFullName("第九名");
                cpOrderContentResult41.setOrderState(cpbjscResult.getdata_30091());
                cpOrderContentResult41.setOrderId("3009-1");
                cpOrderContentResultList4.add(cpOrderContentResult41);

                CPOrderContentResult cpOrderContentResult42 = new CPOrderContentResult();
                cpOrderContentResult42.setOrderName("2");
                cpOrderContentResult42.setFullName("第九名");
                cpOrderContentResult42.setOrderState(cpbjscResult.getdata_30092());
                cpOrderContentResult42.setOrderId("3009-2");
                cpOrderContentResultList4.add(cpOrderContentResult42);


                CPOrderContentResult cpOrderContentResult43 = new CPOrderContentResult();
                cpOrderContentResult43.setOrderName("3");
                cpOrderContentResult43.setFullName("第九名");
                cpOrderContentResult43.setOrderState(cpbjscResult.getdata_30093());
                cpOrderContentResult43.setOrderId("3009-3");
                cpOrderContentResultList4.add(cpOrderContentResult43);

                CPOrderContentResult cpOrderContentResult44 = new CPOrderContentResult();
                cpOrderContentResult44.setOrderName("4");
                cpOrderContentResult44.setFullName("第九名");
                cpOrderContentResult44.setOrderState(cpbjscResult.getdata_30094());
                cpOrderContentResult44.setOrderId("3009-4");
                cpOrderContentResultList4.add(cpOrderContentResult44);

                CPOrderContentResult cpOrderContentResult45 = new CPOrderContentResult();
                cpOrderContentResult45.setOrderName("5");
                cpOrderContentResult45.setFullName("第九名");
                cpOrderContentResult45.setOrderState(cpbjscResult.getdata_30095());
                cpOrderContentResult45.setOrderId("3009-5");
                cpOrderContentResultList4.add(cpOrderContentResult45);

                CPOrderContentResult cpOrderContentResult46 = new CPOrderContentResult();
                cpOrderContentResult46.setOrderName("6");
                cpOrderContentResult46.setFullName("第九名");
                cpOrderContentResult46.setOrderState(cpbjscResult.getdata_30096());
                cpOrderContentResult46.setOrderId("3009-6");
                cpOrderContentResultList4.add(cpOrderContentResult46);

                CPOrderContentResult cpOrderContentResult47 = new CPOrderContentResult();
                cpOrderContentResult47.setOrderName("7");
                cpOrderContentResult47.setFullName("第九名");
                cpOrderContentResult47.setOrderState(cpbjscResult.getdata_30097());
                cpOrderContentResult47.setOrderId("3009-7");
                cpOrderContentResultList4.add(cpOrderContentResult47);

                CPOrderContentResult cpOrderContentResult48 = new CPOrderContentResult();
                cpOrderContentResult48.setOrderName("8");
                cpOrderContentResult48.setFullName("第九名");
                cpOrderContentResult48.setOrderState(cpbjscResult.getdata_30098());
                cpOrderContentResult48.setOrderId("3009-8");
                cpOrderContentResultList4.add(cpOrderContentResult48);

                CPOrderContentResult cpOrderContentResult49 = new CPOrderContentResult();
                cpOrderContentResult49.setOrderName("9");
                cpOrderContentResult49.setFullName("第九名");
                cpOrderContentResult49.setOrderState(cpbjscResult.getdata_30099());
                cpOrderContentResult49.setOrderId("3009-9");
                cpOrderContentResultList4.add(cpOrderContentResult49);


                CPOrderContentResult cpOrderContentResult410 = new CPOrderContentResult();
                cpOrderContentResult410.setOrderName("10");
                cpOrderContentResult410.setFullName("第九名");
                cpOrderContentResult410.setOrderState(cpbjscResult.getdata_300910());
                cpOrderContentResult410.setOrderId("3009-10");
                cpOrderContentResultList4.add(cpOrderContentResult410);

                cpOrderContentListResult4.setData(cpOrderContentResultList4);

                CPOrderContentListResult cpOrderContentListResult5 = new CPOrderContentListResult();
                cpOrderContentListResult5.setOrderContentListName("第十名");
                cpOrderContentListResult5.setShowType("TU");
                cpOrderContentListResult5.setShowNumber(2);

                List<CPOrderContentResult> cpOrderContentResultList5 = new ArrayList<>();
                CPOrderContentResult cpOrderContentResult51 = new CPOrderContentResult();
                cpOrderContentResult51.setOrderName("1");
                cpOrderContentResult51.setFullName("第十名");
                cpOrderContentResult51.setOrderState(cpbjscResult.getdata_30101());
                cpOrderContentResult51.setOrderId("3010-1");
                cpOrderContentResultList5.add(cpOrderContentResult51);

                CPOrderContentResult cpOrderContentResult52 = new CPOrderContentResult();
                cpOrderContentResult52.setOrderName("2");
                cpOrderContentResult52.setFullName("第十名");
                cpOrderContentResult52.setOrderState(cpbjscResult.getdata_30102());
                cpOrderContentResult52.setOrderId("3010-2");
                cpOrderContentResultList5.add(cpOrderContentResult52);


                CPOrderContentResult cpOrderContentResult53 = new CPOrderContentResult();
                cpOrderContentResult53.setOrderName("3");
                cpOrderContentResult53.setFullName("第十名");
                cpOrderContentResult53.setOrderState(cpbjscResult.getdata_30103());
                cpOrderContentResult53.setOrderId("3010-3");
                cpOrderContentResultList5.add(cpOrderContentResult53);

                CPOrderContentResult cpOrderContentResult54 = new CPOrderContentResult();
                cpOrderContentResult54.setOrderName("4");
                cpOrderContentResult54.setFullName("第十名");
                cpOrderContentResult54.setOrderState(cpbjscResult.getdata_30104());
                cpOrderContentResult54.setOrderId("3010-4");
                cpOrderContentResultList5.add(cpOrderContentResult54);

                CPOrderContentResult cpOrderContentResult55 = new CPOrderContentResult();
                cpOrderContentResult55.setOrderName("5");
                cpOrderContentResult55.setFullName("第十名");
                cpOrderContentResult55.setOrderState(cpbjscResult.getdata_30105());
                cpOrderContentResult55.setOrderId("3010-5");
                cpOrderContentResultList5.add(cpOrderContentResult55);

                CPOrderContentResult cpOrderContentResult56 = new CPOrderContentResult();
                cpOrderContentResult56.setOrderName("6");
                cpOrderContentResult56.setFullName("第十名");
                cpOrderContentResult56.setOrderState(cpbjscResult.getdata_30106());
                cpOrderContentResult56.setOrderId("3010-6");
                cpOrderContentResultList5.add(cpOrderContentResult56);

                CPOrderContentResult cpOrderContentResult57 = new CPOrderContentResult();
                cpOrderContentResult57.setOrderName("7");
                cpOrderContentResult57.setFullName("第十名");
                cpOrderContentResult57.setOrderState(cpbjscResult.getdata_30107());
                cpOrderContentResult57.setOrderId("3010-7");
                cpOrderContentResultList5.add(cpOrderContentResult57);

                CPOrderContentResult cpOrderContentResult58 = new CPOrderContentResult();
                cpOrderContentResult58.setOrderName("8");
                cpOrderContentResult58.setFullName("第十名");
                cpOrderContentResult58.setOrderState(cpbjscResult.getdata_30108());
                cpOrderContentResult58.setOrderId("3010-8");
                cpOrderContentResultList5.add(cpOrderContentResult58);

                CPOrderContentResult cpOrderContentResult59 = new CPOrderContentResult();
                cpOrderContentResult59.setOrderName("9");
                cpOrderContentResult59.setFullName("第十名");
                cpOrderContentResult59.setOrderState(cpbjscResult.getdata_30109());
                cpOrderContentResult59.setOrderId("3010-9");
                cpOrderContentResultList5.add(cpOrderContentResult59);


                CPOrderContentResult cpOrderContentResult510 = new CPOrderContentResult();
                cpOrderContentResult510.setOrderName("10");
                cpOrderContentResult510.setFullName("第十名");
                cpOrderContentResult510.setOrderState(cpbjscResult.getdata_301010());
                cpOrderContentResult510.setOrderId("3010-10");
                cpOrderContentResultList5.add(cpOrderContentResult510);

                cpOrderContentListResult5.setData(cpOrderContentResultList5);

                cPOrderContentListResultAll.add(cpOrderContentListResult);
                cPOrderContentListResultAll.add(cpOrderContentListResult2);
                cPOrderContentListResultAll.add(cpOrderContentListResult3);
                cPOrderContentListResultAll.add(cpOrderContentListResult4);
                cPOrderContentListResultAll.add(cpOrderContentListResult5);
                allResult.setData(cPOrderContentListResultAll);
            }
            allResultList.add(allResult);

        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void showLeftMenu(){
        CPLeftMenuFragment cpLeftMenuFragment = CPLeftMenuFragment.newInstance(Arrays.asList("1","2","3"));
//        MenuLeftFragment  menuLeftFragment = new MenuLeftFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.left_menu_frame_id, cpLeftMenuFragment).commit();
        slidingLeftMenu = getSlidingMenu();
        slidingLeftMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        slidingLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingLeftMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingLeftMenu.setShadowDrawable(R.color.cp_order_bg);

        // 设置滑动菜单视图的宽度
        slidingLeftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingLeftMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
//        slidingLeftMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        //为侧滑菜单设置布局
        /*slidingLeftMenu.setMenu(R.layout.left_menu);
        RecyclerView recyclerView = slidingLeftMenu.getMenu().findViewById(R.id.cpOrderGameList);
        LinearLayoutManager linearLayoutManagerRight = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManagerRight);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        CPOrederGameAdapter cpOrederListRightGameAdapter = new CPOrederGameAdapter(getContext(), R.layout.item_cp_order_list, cpGameList);
        recyclerView.setAdapter(cpOrederListRightGameAdapter);*/
    }

    private void onResponseCQResult(){
        int size2 = cpOrderContentListResults.size();
        for(int j=0;j<size2;++j){
            CPOrderContentListResult cpOrderContentListResult = cpOrderContentListResults.get(j);
            int size3 = cpOrderContentListResult.getData().size();
            for(int k=0;k<size3;++k){
                CPOrderContentResult cpOrderContentResult = cpOrderContentListResult.getData().get(k);
                if(CPBetManager.getSingleton().inContain(type+"_"+cpOrderContentResult.getOrderId())){
                    cpOrderContentResult.setChecked(true);
                }
            }
        }
        cpOreder2ListRightGameAdapter = new CPOreder2ListRightGameAdapter(getContext(), R.layout.item_cp_order_content1,cpOrderContentListResults);
        cpOrderListRight.setAdapter(cpOreder2ListRightGameAdapter);
        cpOreder2ListRightGameAdapter.notifyDataSetChanged();
    }

    @Override
    public void postRateInfoResult(CQSSCResult cqsscResult) {
        cpOrderContentListResults =  CQSSC(cqsscResult,index);
        onResponseCQResult();
    }

    @Override
    public void postRateInfoBjscResult(CPBJSCResult cpbjscResult) {
        cpOrderContentListResults =  BJPK10(cpbjscResult,index);
        onResponseCQResult();
    }

    @Override
    public void postRateInfoJsscResult(CPJSSCResult cpbjscResult) {

        onResponseCQResult();
    }

    @Override
    public void postRateInfoJsftResult(CPJSFTResult cpbjscResult) {

        onResponseCQResult();
    }

    @Override
    public void postRateInfo1FCResult(CQ1FCResult cqffcResult) {
        cpOrderContentListResults =  CQSSC1(cqffcResult,index);
        onResponseCQResult();
    }

    @Override
    public void postRateInfo2FCResult(CQ2FCResult cqffcResult) {
        cpOrderContentListResults =  CQSSC2(cqffcResult,index);
        onResponseCQResult();
    }

    @Override
    public void postRateInfo3FCResult(CQ3FCResult cqffcResult) {
        cpOrderContentListResults =  CQSSC3(cqffcResult,index);
        onResponseCQResult();
    }

    @Override
    public void postRateInfo5FCResult(CQ5FCResult cqffcResult) {
        cpOrderContentListResults =  CQSSC5(cqffcResult,index);
        onResponseCQResult();
    }

    /*@Override
    public void postRateInfo6Result(CQSSCResult cqsscResult) {
        CQSSC(cqsscResult,1);
    }

    @Override
    public void postRateInfo1Result(CQSSCResult cqsscResult) {
        CQSSC(cqsscResult,2);
        showContentView(0);
    }*/

    @Override
    public void postLastResultResult(CPLastResult cpLastResult) {

        cpOrderLotteryLastTime.setText(cpLastResult.getIssue()+"期");
        cpLeftEventList2.clear();
        cpLeftEventList1.clear();
        String[] dataList  =  cpLastResult.getNums().split(",");
        int dataListSize = dataList.length;
        int total= 0;
        for(int k=0;k<dataListSize;++k){
            cpLeftEventList1.add(dataList[k]);
            total += Integer.parseInt(dataList[k]);
        }

        /** 北京赛车    game_code 51
         *  重庆时时彩    game_code 2
         *  极速赛车    game_code 189
         *  极速飞艇    game_code 222
         *  分分彩    game_code 207
         *  三分彩    game_code 407
         *  五分彩    game_code 507
         *  腾讯二分彩    game_code 607
         *  PC蛋蛋    game_code 304
         *  江苏快3    game_code 159
         *  幸运农场    game_code 47
         *  快乐十分    game_code 3
         *  香港六合彩  game_code 69
         *  极速快三    game_code 384
         *
         */
        switch (game_code){
            case "2":
            case "207":
            case "407":
            case "507":
            case "607":
                group = "group2";
                cpLeftEventList2.add(total+"");
                cpLeftEventList2.add((total >= 23)?"大":"小");
                cpLeftEventList2.add((total % 2 ==1)?"单":"双");
                /*if(Integer.parseInt(dataList[0])>Integer.parseInt(dataList[4])){
                    cpLeftEventList2.add("龙");
                }else if(Integer.parseInt(dataList[0])==Integer.parseInt(dataList[4])){
                    cpLeftEventList2.add("和");
                }else{
                    cpLeftEventList2.add("虎");
                }*/
                cpLeftEventList2.add(Integer.parseInt(dataList[0])>=Integer.parseInt(dataList[4])? Integer.parseInt(dataList[0])>Integer.parseInt(dataList[4])?"龙":"和":"虎");
                cpOrderLotteryOpen1.setAdapter(new OpenQIUGameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList1));
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
            case "51":
            case "189":
            case "222":
            case "168"://幸运飞艇 暂无
                group = "group1";
                cpLeftEventList2.add(Integer.parseInt(dataList[0])+Integer.parseInt(dataList[1])+"");
                cpLeftEventList2.add((Integer.parseInt(dataList[0])+Integer.parseInt(dataList[1]))>11?"大":"小");
                cpLeftEventList2.add(((Integer.parseInt(dataList[0])+Integer.parseInt(dataList[1]))%2 ==1)?"单":"双");
                cpLeftEventList2.add(Integer.parseInt(dataList[0])>Integer.parseInt(dataList[9])?"龙":"虎");
                cpLeftEventList2.add(Integer.parseInt(dataList[1])>Integer.parseInt(dataList[8])?"龙":"虎");
                cpLeftEventList2.add(Integer.parseInt(dataList[2])>Integer.parseInt(dataList[7])?"龙":"虎");
                cpLeftEventList2.add(Integer.parseInt(dataList[3])>Integer.parseInt(dataList[6])?"龙":"虎");
                cpLeftEventList2.add(Integer.parseInt(dataList[4])>Integer.parseInt(dataList[5])?"龙":"虎");
                cpOrderLotteryOpen1.setAdapter(new Open1GameAdapter(getContext(), R.layout.item_cp_order_open_1, cpLeftEventList1));
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
            case "47":
            case "3":
                cpLeftEventList2.add(total+"");
                cpLeftEventList2.add((total >= 84)?total > 84?"大":"和":"小");
                cpLeftEventList2.add((total % 2 == 1) ? "单":"双");
                cpLeftEventList2.add((total % 10 >= 5) ? "大":"小");
                group = "group3";
                cpOrderLotteryOpen1.setAdapter(new OpenQIUGameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList1));
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
            case "21"://广东11选5 暂无
                cpLeftEventList2.add(total+"");
                cpLeftEventList2.add((total >= 30)?total > 30?"大":"和":"小");
                cpLeftEventList2.add((total % 2 == 1) ? "单":"双");
                cpLeftEventList2.add((total % 10 >= 5) ? "大":"小");
                cpLeftEventList2.add((Integer.parseInt(dataList[0])>Integer.parseInt(dataList[4])) ? "龙":"虎");
                group = "group4";
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
            case "65"://"北京快乐8" 暂无
                group = "group5";
                break;
            case "159":
            case "384":
                cpLeftEventList2.add(total+"");
                cpLeftEventList2.add((total >= 11) ? "大":"小");
                group = "group6";
                cpOrderLotteryOpen1.setAdapter(new OpenK3GameAdapter(getContext(), R.layout.item_cp_order_open_1, cpLeftEventList1));
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
            case "69":
                group = "group7";
                //香港六合彩的没有二
                break;
            case "304":
                cpLeftEventList2.add(total+"");
                cpLeftEventList2.add((total > 13) ? "大":"小");
                cpLeftEventList2.add((total % 2 == 1) ? "单":"双");
                group = "group8";
                cpLeftEventList1.add("=");
                cpLeftEventList1.add(""+total);
                cpOrderLotteryOpen1.setAdapter(new OpenQIUGameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList1));
                cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));
                break;
        }



    }

    @Override
    public void postNextIssueResult(CPNextIssueResult cpNextIssueResult) {
        isCloseLottery = false;
        cpOrderGold.setClickable(true);
        cpOrderGold.setFocusable(true);
        cpOrderGold.setFocusableInTouchMode(true);
        cpOrderGold.requestFocus();
        cpOrderNoYet.setVisibility(View.GONE);
        round =cpNextIssueResult.getIssue();
        cpOrderLotteryNextTime.setText(round+"期");
        String systTime =TimeUtils.convertToDetailTime(System.currentTimeMillis());
        sendEndTime = TimeHelper.timeToSecond(cpNextIssueResult.getEndtime(),systTime)+20;
//        sendEndTime = TimeHelper.timeToSecond("2018-11-28 11:28:00","2018-11-28 11:20:15");
        sendAuthTime = TimeHelper.timeToSecond(cpNextIssueResult.getLotteryTime(),systTime)+20;
        GameLog.log("getEndtime："+cpNextIssueResult.getEndtime()+"systTime："+systTime);
        GameLog.log("封盘时间："+sendEndTime+"开奖时间："+sendAuthTime);
        onSartTime();
    }

    @Override
    public void postCPLeftInfoResult(CPLeftInfoResult cpLeftInfoResult) {
        cpOrderUserMoney.setText(cpLeftInfoResult.getMoney());
        GameLog.log("彩票的用户金额 "+cpLeftInfoResult.getMoney());
    }

    @Override
    public void postRateInfoPCDDResult(PCDDResult pcddResult) {
        GameLog.log("PC蛋蛋的数据： "+pcddResult.toString());
        PCDD(pcddResult);
        showContentView(0);
    }

    @Override
    public void postRateInfoJsk3Result(CPJSKSResult cpjsksResult) {
        GameLog.log("江苏快3的数据： "+cpjsksResult.toString());
        JSKS(cpjsksResult);
        showContentView(0);
    }
    @Override
    public void postRateInfoJsk32Result(CPJSK2Result cpjsksResult) {
        GameLog.log("江苏快3的数据： "+cpjsksResult.toString());
        JSKS2(cpjsksResult);
        showContentView(0);
    }


    class CPLeftMenuGameAdapter extends AutoSizeRVAdapter<HomePageIcon> {
        private Context context;

        public CPLeftMenuGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, HomePageIcon data, final int position) {
            /*if(position==0){
                TextView tv =  holder.getView(R.id.tv_item_game_name);
                tv.setGravity(Gravity.CENTER);
            }*/
            holder.setText(R.id.itemOrderLeftListTV, data.getIconName());
            holder.setOnClickListener(R.id.itemOrderLeftListTV, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        return;
                    }else if(position==1){
                        pop();
                        return;
                    }
                    slidingLeftMenu.toggle();

                }
            });
        }
    }


    private void initViewCqsscData(){
        initCqsscLeftData();
        index = 0;
        cpOrderListLeft.setAdapter(new CPOrederCQListLeftGameAdapter(getContext(), R.layout.item_cp_order_left_list, allResultList));
    }

    private void initViewBjscData(){
        initBjscLeftData();
        index = 0;
        cpOrderListLeft.setAdapter(new CPOrederCQListLeftGameAdapter(getContext(), R.layout.item_cp_order_left_list, allResultList));
    }


    private void onChangeData(){
        allResultList.clear();
        //北京赛车
       /* String  data = getFromAssets("data_bak.json");
        //重庆时时彩
        String  data2 = getFromAssets("cqssc.txt");
        //PC蛋蛋
        String  data3 = getFromAssets("pcdd.txt");
        //PC蛋蛋
        String  data4 = getFromAssets("jsks.txt");
        //幸运农场
        String  data5 = getFromAssets("xync.txt");
        //GameLog.log("屏幕的宽度："+data);
        Gson gson = new Gson();
        if("bjsc".equals(orderStype)){
            CPBJSCResult2 cpbjscResult2 = gson.fromJson(data,CPBJSCResult2.class);
            BJPK10(cpbjscResult2);
           *//* CPXYNCResult cpbjscResult2 = gson.fromJson(data5,CPXYNCResult.class);
            XYNC(cpbjscResult2);*//*
            *//*CPJSKSResult cqsscResult4 = gson.fromJson(data4,CPJSKSResult.class);
            JSKS(cqsscResult4);*//*
        }else if("cqssc".equals(orderStype)){
            CQSSCResult cqsscResult = gson.fromJson(data2,CQSSCResult.class);
            CQSSC(cqsscResult);
        }else if("pcdd".equals(orderStype)){
            PCDDResult cqsscResult3 = gson.fromJson(data3,PCDDResult.class);
            PCDD(cqsscResult3);
        }else if("jsks".equals(orderStype)){
            CPJSKSResult cqsscResult4 = gson.fromJson(data4,CPJSKSResult.class);
            JSKS(cqsscResult4);
        }*/
        //showContentView(0);
        /** 北京赛车    game_code 51
         *  重庆时时彩    game_code 2
         *  极速赛车    game_code 189
         *  极速飞艇    game_code 222
         *  分分彩    game_code 207
         *  三分彩    game_code 407
         *  五分彩    game_code 507
         *  腾讯二分彩    game_code 607
         *  PC蛋蛋    game_code 304
         *  江苏快3    game_code 159
         *  幸运农场    game_code 47
         *  快乐十分    game_code 3
         *  香港六合彩  game_code 69
         *  极速快三    game_code 384
         */
        presenter.postCPLeftInfo("1",x_session_token);
        presenter.postNextIssue(game_code,x_session_token);
        presenter.postLastResult(game_code,x_session_token);
        type = "0";
        switch (game_code){
            case "51":
                initViewBjscData();
                presenter.postRateInfoBjsc(game_code,type,x_session_token);
                break;
            case "189":
                initViewBjscData();
                presenter.postRateInfoJssc(game_code,type,x_session_token);
                break;
            case "222":
                initViewBjscData();
                presenter.postRateInfoJsft(game_code,type,x_session_token);
                break;
            case "2":
                initViewCqsscData();
                presenter.postRateInfo(game_code,type,x_session_token);
                break;
            case "207":
                initViewCqsscData();
                presenter.postRateInfo1FC(game_code,type,x_session_token);
                break;
            case "607"://时时彩的请求方式
                initViewCqsscData();
                presenter.postRateInfo2FC(game_code,type,x_session_token);
                break;
            case "407":
                initViewCqsscData();
                presenter.postRateInfo3FC(game_code,type,x_session_token);
                break;
            case "507":
                initViewCqsscData();
                presenter.postRateInfo5FC(game_code,type,x_session_token);
                break;
            case "304"://PC蛋蛋
                //type = "undefined";
                presenter.postRateInfoPCDD(game_code,x_session_token);
                break;
            case "159"://江苏快3
                type = "1";
                presenter.postRateInfoJsk3(game_code,type,x_session_token);
                break;
            case "384":// 极速快3
                //type = "undefined";
                type = "1";
                presenter.postRateInfoJsk32(game_code,type,x_session_token);
                break;
        }

    }

    private void initTablayout(){
        /*cpOrderTab.setVisibility(View.VISIBLE);
        cpOrderRXRadio.setVisibility(View.VISIBLE);
        cpOrderRXLine.setVisibility(View.VISIBLE);*/
        cpOrderRXRadio.setText("赔率:"+rX2);
        cpOrderTab.addTab(cpOrderTab.newTab().setText("任选二"));
        cpOrderTab.addTab(cpOrderTab.newTab().setText("任选三"));
        cpOrderTab.addTab(cpOrderTab.newTab().setText("任选四"));
        cpOrderTab.addTab(cpOrderTab.newTab().setText("任选五"));
        cpOrderTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onResetData();
                switch (tab.getPosition()){
                    case 0:
                        rX0 = 2;
                        cpOrderRXRadio.setText("赔率:"+rX2);
                        break;
                    case 1:
                        rX0 = 3;
                        cpOrderRXRadio.setText("赔率:"+rX3);
                        break;
                    case 2:
                        rX0 = 4;
                        cpOrderRXRadio.setText("赔率:"+rX4);
                        break;
                    case 3:
                        rX0 = 5;
                        cpOrderRXRadio.setText("赔率:"+rX5);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initCqsscLeftData(){
        allResultList.clear();
        CPOrderAllResult cpOrderAllResult1 = new CPOrderAllResult();
        cpOrderAllResult1.setEventChecked(true);
        cpOrderAllResult1.setType("0");
        cpOrderAllResult1.setOrderAllName("两面");

        CPOrderAllResult cpOrderAllResult2 = new CPOrderAllResult();
        cpOrderAllResult2.setOrderAllName("1-5球");
        cpOrderAllResult2.setType("6");

        CPOrderAllResult cpOrderAllResult3 = new CPOrderAllResult();
        cpOrderAllResult3.setOrderAllName("前中后");
        cpOrderAllResult3.setType("1");
        allResultList.add(cpOrderAllResult1);
        allResultList.add(cpOrderAllResult2);
        allResultList.add(cpOrderAllResult3);

    }

    private void initBjscLeftData(){
        allResultList.clear();
        CPOrderAllResult cpOrderAllResult1 = new CPOrderAllResult();
        cpOrderAllResult1.setEventChecked(true);
        cpOrderAllResult1.setType("0");
        cpOrderAllResult1.setOrderAllName("两面");

        CPOrderAllResult cpOrderAllResult2 = new CPOrderAllResult();
        cpOrderAllResult2.setOrderAllName("冠亚和");
        cpOrderAllResult2.setType("1");

        CPOrderAllResult cpOrderAllResult3 = new CPOrderAllResult();
        cpOrderAllResult3.setOrderAllName("1-5名");
        cpOrderAllResult3.setType("2");

        CPOrderAllResult cpOrderAllResult4 = new CPOrderAllResult();
        cpOrderAllResult4.setOrderAllName("6-10名");
        cpOrderAllResult4.setType("3");
        allResultList.add(cpOrderAllResult1);
        allResultList.add(cpOrderAllResult2);
        allResultList.add(cpOrderAllResult3);
        allResultList.add(cpOrderAllResult4);

    }

    @Override
    public void setEvents(@Nullable Bundle savedInstanceState) {
        initTablayout();
        CPBetManager.getSingleton().onClearData();
        x_session_token = ACache.get(HGApplication.instance().getApplicationContext()).getAsString(HGConstant.APP_CP_X_SESSION_TOKEN);
        onChangeData();
        setSystemUIVisible(false);
        cpOrderTitle.setText(titleName);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer_layout, R.string.account, R.string.password);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();*/

        /*String  data = getFromAssets("data.json").replace("-","_");
        GameLog.log("屏幕的宽度："+data);
        CpBJSCResult cpBJSCResult = JSON.parseObject(data, CpBJSCResult.class);
        GameLog.log("屏幕的宽度："+cpBJSCResult.toString());*/

        //CPBJSCResult2 cpBJSCResult = JSON.parseObject(data, CPBJSCResult2.class);
//        /GameLog.log("屏幕的宽度："+cpbjscResult2.toString());

        /*WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);*/
        /*mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;*/
        //mainSwipemenu.setMenuOffset(metrics.widthPixels-Integer.parseInt(SizeUtil.Dp2Px(getContext(),50)+""));
       /* LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        cpOrderGameList.setLayoutManager(gridLayoutManager);
        cpOrderGameList.setHasFixedSize(true);
        cpOrderGameList.setNestedScrollingEnabled(false);
        cpOrderGameList.setAdapter(new CPOrederGameAdapter(getContext(), R.layout.item_cp_order_list, cpGameList));*/
        LinearLayoutManager linearLayoutManagerLeft = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        cpOrderListLeft.setLayoutManager(linearLayoutManagerLeft);
        cpOrderListLeft.setHasFixedSize(true);
        cpOrderListLeft.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManagerRight = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        cpOrderListRight.setLayoutManager(linearLayoutManagerRight);
        cpOrderListRight.setHasFixedSize(true);
        cpOrderListRight.setNestedScrollingEnabled(false);


        //cpOrderListViewtLeft.setAdapter(new CPOrederListViewLeftGameAdapter(getContext(), R.layout.item_cp_order_left_list, allResultList));
        LinearLayoutManager cpOrderLotteryOpen11 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        cpOrderLotteryOpen1.setLayoutManager(cpOrderLotteryOpen11);
        cpOrderLotteryOpen1.setHasFixedSize(true);
        cpOrderLotteryOpen1.setNestedScrollingEnabled(false);
        //cpOrderLotteryOpen1.setAdapter(new Open1GameAdapter(getContext(), R.layout.item_cp_order_open_1, cpLeftEventList));

        LinearLayoutManager cpOrderLotteryOpen22 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        cpOrderLotteryOpen2.setLayoutManager(cpOrderLotteryOpen22);
        cpOrderLotteryOpen2.setHasFixedSize(true);
        cpOrderLotteryOpen2.setNestedScrollingEnabled(false);
       // cpOrderLotteryOpen2.setAdapter(new Open2GameAdapter(getContext(), R.layout.item_cp_order_open_2, cpLeftEventList2));

    }

    /**
     * 设置筛子图片
     */
    class OpenK3GameAdapter extends AutoSizeRVAdapter<String> {
        private Context context;

        public OpenK3GameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, String data, final int position) {
            switch (data){
                case "01":
                case "1":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_1);
                    break;
                case "02":
                case "2":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_2);
                    break;
                case "03":
                case "3":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_3);
                    break;
                case "04":
                case "4":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_4);
                    break;
                case "05":
                case "5":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_5);
                    break;
                case "06":
                case "6":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.s_6);
                    break;
            }

        }
    }


    /**
     * 设置图片
     */
    class Open1GameAdapter extends AutoSizeRVAdapter<String> {
        private Context context;

        public Open1GameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, String data, final int position) {
            switch (data){
                case "01":
                case "1":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_one);
                    break;
                case "02":
                case "2":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_two);
                    break;
                case "03":
                case "3":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_three);
                    break;
                case "04":
                case "4":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_four);
                    break;
                case "05":
                case "5":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_five);
                    break;
                case "06":
                case "6":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_six);
                    break;
                case "07":
                case "7":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_seven);
                    break;
                case "08":
                case "8":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_eight);
                    break;
                case "09":
                case "9":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_nine);
                    break;
                case "10":
                    holder.setImageResource(R.id.itemOrderOpen1,R.mipmap.cp_order_ten);
                    break;
            }

        }
    }



    /**
     * 设置文字+球
     */
    class OpenQIUGameAdapter extends AutoSizeRVAdapter<String> {
        private Context context;

        public OpenQIUGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, String data, final int position) {
            holder.setBackgroundRes(R.id.itemOrderOpen2,R.mipmap.cp_qiu);
            if(data.length()==2){
                if("0".equals(data.substring(0,1))){
                    holder.setText(R.id.itemOrderOpen2,data.substring(1,2));
                }else{
                    holder.setText(R.id.itemOrderOpen2,data);
                }
            }else{
                holder.setText(R.id.itemOrderOpen2,data);
            }
        }
    }

    /**
     * 设置文字
     */
    class Open2GameAdapter extends AutoSizeRVAdapter<String> {
        private Context context;

        public Open2GameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, String data, final int position) {
            if(data.length()==3){
               TextView textView  =  holder.getView(R.id.itemOrderOpen2);
                textView.setTextSize(10);
            }
            holder.setText(R.id.itemOrderOpen2,data);
        }
    }


    class CPOrederListViewLeftGameAdapter extends AutoSizeAdapter<CPOrderAllResult> {
        private Context context;

        public CPOrederListViewLeftGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(final com.zhy.adapter.abslistview.ViewHolder holder, CPOrderAllResult data, final int position) {
            if(data.isEventChecked()){
                holder.setTextColor(R.id.itemOrderLeftListTV, R.color.title_text);
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
            }else{
                //holder.setTextColor(R.id.itemOrderLeftListTV, R.color.textview_normal);
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
            }
            holder.setText(R.id.itemOrderLeftListTV, data.getOrderAllName());
            holder.setOnClickListener(R.id.itemOrderLeftListTV, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefreshRight(position);
                    for(int i=0;i<allResultList.size();++i){
                        allResultList.get(i).setEventChecked(false);
                    }
                    holder.setTextColor(R.id.itemOrderLeftListTV, R.color.title_text);
                    allResultList.get(position).setEventChecked(true);
                    /*if(data.isEventChecked()){
                        data.setEventChecked(false);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
                    }else{
                        data.setEventChecked(true);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
                    }*/
                    notifyDataSetChanged();
                }
            });
        }
    }

    class CPOrederCQListLeftGameAdapter extends AutoSizeRVAdapter<CPOrderAllResult> {
        private Context context;

        public CPOrederCQListLeftGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }


        @Override
        protected void convert(final ViewHolder holder,final CPOrderAllResult data, final int position) {
           /* if(data.isEventChecked()){
                holder.setImageResource(R.id.itemOrderLeftListIV,R.drawable.cp_circle_checked);
            }else{
                holder.setImageResource(R.id.itemOrderLeftListIV,R.drawable.cp_circle_normal);
            }*/
            if(data.isEventChecked()){
                holder.setTextColorRes(R.id.itemOrderLeftListTV, R.color.title_text);
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
            }else{
                holder.setTextColorRes(R.id.itemOrderLeftListTV, R.color.n_edittext_hint);
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
            }
            holder.setText(R.id.itemOrderLeftListTV, data.getOrderAllName());
            holder.setOnClickListener(R.id.itemOrderLeftListTV, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = position;
                    type = data.getType();
                    onRefreshRightCQ(data.getType());
                    for(int i=0;i<allResultList.size();++i){
                        allResultList.get(i).setEventChecked(false);
                    }
                    //holder.setTextColor(R.id.itemOrderLeftListTV, R.color.title_text);
                    allResultList.get(position).setEventChecked(true);
                    /*if(data.isEventChecked()){
                        data.setEventChecked(false);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
                    }else{
                        data.setEventChecked(true);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
                    }*/
                    notifyDataSetChanged();
                }
            });
        }
    }

    class CPOrederListLeftGameAdapter extends AutoSizeRVAdapter<CPOrderAllResult> {
        private Context context;

        public CPOrederListLeftGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }


        @Override
        protected void convert(final ViewHolder holder,final CPOrderAllResult data, final int position) {
           /* if(data.isEventChecked()){
                holder.setImageResource(R.id.itemOrderLeftListIV,R.drawable.cp_circle_checked);
            }else{
                holder.setImageResource(R.id.itemOrderLeftListIV,R.drawable.cp_circle_normal);
            }*/
            if(data.isEventChecked()){
                holder.setTextColorRes(R.id.itemOrderLeftListTV, R.color.title_text);
                GameLog.log(position+" 设置颜色值11111111111111111111111111111");
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
            }else{
                holder.setTextColorRes(R.id.itemOrderLeftListTV, R.color.n_edittext_hint);
                GameLog.log(position+" 设置颜色值222222222222222222222222222222");
                holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
            }
            holder.setText(R.id.itemOrderLeftListTV, data.getOrderAllName());
            holder.setOnClickListener(R.id.itemOrderLeftListTV, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefreshRight(position);
                    for(int i=0;i<allResultList.size();++i){
                        allResultList.get(i).setEventChecked(false);
                    }
                    //holder.setTextColor(R.id.itemOrderLeftListTV, R.color.title_text);
                    allResultList.get(position).setEventChecked(true);
                    /*if(data.isEventChecked()){
                        data.setEventChecked(false);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_normal);
                    }else{
                        data.setEventChecked(true);
                        holder.setBackgroundRes(R.id.itemOrderLeftListTV,R.drawable.bg_cp_oder_left_checked);
                    }*/
                    notifyDataSetChanged();
                }
            });
        }
    }


    public class DepositListAdapter extends AutoSizeAdapter<CPOrderContentListResult> {
        private Context context;

        public DepositListAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            this.context = context;
        }

        @Override
        protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, final CPOrderContentListResult data, final int position) {
            holder.setText(R.id.cpOrderContentName1, data.getOrderContentListName());
            GridLayoutManager gridLayoutManager = null;
            GameLog.log("当前的试试："+data.getShowNumber());
            if(data.getShowNumber()==3){
                gridLayoutManager= new GridLayoutManager(getContext(), 3, OrientationHelper.VERTICAL, false);
            }else{
                gridLayoutManager= new GridLayoutManager(getContext(), 2, OrientationHelper.VERTICAL, false);
            }
            RecyclerView recyclerView = holder.getView(R.id.cpOrderContentList1);
            recyclerView.setLayoutManager(gridLayoutManager);
            /*recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(true);*/

            // recyclerView.addItemDecoration(new GridRvItemDecoration(getContext()));
            cpOrederContentGameAdapter = null;
            cpOrederContentGameAdapter = new CPOrederContentGameAdapter(getContext(), R.layout.item_cp_order_content2, data.getData());
            recyclerView.setAdapter(cpOrederContentGameAdapter);
        }
    }

    private void showContentView(int postion){
        postionAll = postion;
        //cpOrderListViewRight.setAdapter(new DepositListAdapter(getContext(), R.layout.item_cp_order_content1, allResultList.get(postionAll).getData()));

        //cpOrederListRightGameAdapter = new CPOrederListRightGameAdapter(getContext(), R.layout.item_cp_order_content1, allResultList.get(postion).getData());
//        data.clear();
//        data.addAll(allResultList.get(postion).getData());
        cpOrderListLeft.setAdapter(new CPOrederListLeftGameAdapter(getContext(), R.layout.item_cp_order_left_list, allResultList));
        cpOrederListRightGameAdapter = new CPOrederListRightGameAdapter(getContext(), R.layout.item_cp_order_content1, allResultList.get(postionAll).getData());
        cpOrderListRight.setAdapter(cpOrederListRightGameAdapter);
        cpOrederListRightGameAdapter.notifyDataSetChanged();
        /*cpOrderUserMoney.post(new Runnable() {
            @Override
            public void run() {
                *//*myAdapter = new MyAdapter(data);
                cpOrderListRight.setAdapter(myAdapter);*//*
                cpOrederListRightGameAdapter = new CPOrederListRightGameAdapter(getContext(), R.layout.item_cp_order_content1, allResultList.get(postionAll).getData());
                cpOrderListRight.setAdapter(cpOrederListRightGameAdapter);
                //cpOrderListRight.setLayoutManager(new LinearLayoutManager(getActivity()));
                //myAdapter.notifyDataSetChanged();
                cpOrderListRight.scrollToPosition(0);
                cpOrederListRightGameAdapter.notifyDataSetChanged();
            }
        });*/

        //cpOrederListRightGameAdapter.setDataChange(allResultList.get(postion).getData());
        //cpOrederListRightGameAdapter.notifyDataSetChanged();
    }

    private void onRefreshRightCQ(String position){
        switch (game_code){
            case "51":
                presenter.postRateInfoBjsc(game_code,type,x_session_token);
                break;
            case "189":
                presenter.postRateInfoJssc(game_code,type,x_session_token);
                break;
            case "222":
                presenter.postRateInfoJsft(game_code,type,x_session_token);
                break;
            case "2":
                presenter.postRateInfo(game_code,type,x_session_token);
                break;
            case "207":
                presenter.postRateInfo1FC(game_code,type,x_session_token);
                break;
            case "607"://时时彩的请求方式
                presenter.postRateInfo2FC(game_code,type,x_session_token);
                break;
            case "407":
                presenter.postRateInfo3FC(game_code,type,x_session_token);
                break;
            case "507":
                presenter.postRateInfo5FC(game_code,type,x_session_token);
                break;
            case "304"://PC蛋蛋
                //type = "undefined";
                presenter.postRateInfoPCDD(game_code,x_session_token);
                break;
            case "159"://江苏快3
                type = "1";
                presenter.postRateInfoJsk3(game_code,type,x_session_token);
                break;
            case "384":// 极速快3
                //type = "undefined";
                type = "1";
                presenter.postRateInfoJsk32(game_code,type,x_session_token);
                break;
        }
    }


    private void onRefreshRight(int position){

        showMessage("刷新后边的数据"+position);
        showContentView(position);
        if(position==9){
            if(game_code.equals("47")||game_code.equals("3")){
                rX0 = 2;
                onResetData();
                cpOrderLayout.setVisibility(View.VISIBLE);
            }else{
                cpOrderLayout.setVisibility(View.GONE);
            }
        }else{

            if(rX0 == 2||rX0 ==3 ||rX0 ==42||rX0 ==5){
                rX0 = 0;
                onResetData();
            }
            /*cpOrderTab.setVisibility(View.GONE);
            cpOrderRXRadio.setVisibility(View.GONE);
            cpOrderRXLine.setVisibility(View.GONE);*/
            cpOrderLayout.setVisibility(View.GONE);
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        List<CPOrderContentListResult>  datas;
        public MyAdapter(List<CPOrderContentListResult>  datas) {
            this.datas = datas;
        }
        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cp_order_content1,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(datas.get(position).getOrderContentListName());
            GridLayoutManager gridLayoutManager = null;
            if(position==1||position==2||position==3||position==4||position==5){
                gridLayoutManager= new GridLayoutManager(getContext(), 3, OrientationHelper.VERTICAL, false);
            }else{
                gridLayoutManager= new GridLayoutManager(getContext(), 2, OrientationHelper.VERTICAL, false);
            }
            viewHolder.recyclerView.setLayoutManager(gridLayoutManager);
            cpOrederContentGameAdapter = null;
            cpOrederContentGameAdapter = new CPOrederContentGameAdapter(getContext(), R.layout.item_cp_order_content2, datas.get(position).getData());
            viewHolder.recyclerView.setAdapter(cpOrederContentGameAdapter);
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;
            public RecyclerView recyclerView;
            public ViewHolder(View view){
                super(view);
                mTextView = (TextView) view.findViewById(R.id.cpOrderContentName1);
                recyclerView =  (RecyclerView) view.findViewById(R.id.cpOrderContentList1);
            }
        }
    }

    class CPOrederListRightGameAdapter extends AutoSizeRVAdapter<CPOrderContentListResult> {
        private Context context;
        private List<CPOrderContentListResult>  datas;
        public CPOrederListRightGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        public void setDataChange(List<CPOrderContentListResult>  datas){
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(ViewHolder holder, CPOrderContentListResult data, final int position) {
            if(Check.isEmpty(data.getOrderContentListName())){
                holder.setVisible(R.id.cpOrderContentName1, false);
                holder.setVisible(R.id.cpOrderContentLine, false);
            }else{
                holder.setText(R.id.cpOrderContentName1, data.getOrderContentListName());
                holder.setVisible(R.id.cpOrderContentName1, true);
                holder.setVisible(R.id.cpOrderContentLine, true);
            }
            GridLayoutManager gridLayoutManager = null;
            if(data.getShowNumber()==3){
                gridLayoutManager= new GridLayoutManager(getContext(), 3, OrientationHelper.VERTICAL, false);
            }else if(data.getShowNumber()==2){
                gridLayoutManager= new GridLayoutManager(getContext(), 2, OrientationHelper.VERTICAL, false);
            }else{
                gridLayoutManager= new GridLayoutManager(getContext(), 1, OrientationHelper.VERTICAL, false);
            }
            RecyclerView recyclerView = holder.getView(R.id.cpOrderContentList1);
            recyclerView.setLayoutManager(gridLayoutManager);
            cpOrederContentGameAdapter = null;
            cpOrederContentGameAdapter = new CPOrederContentGameAdapter(getContext(), R.layout.item_cp_order_content2, data.getData(),data.getShowType());
            recyclerView.setAdapter(cpOrederContentGameAdapter);
        }
    }


    class CPOreder2ListRightGameAdapter extends AutoSizeRVAdapter<CPOrderContentListResult> {
        private Context context;
        private List<CPOrderContentListResult>  datas;
        public CPOreder2ListRightGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        public void setDataChange(List<CPOrderContentListResult>  datas){
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(ViewHolder holder, CPOrderContentListResult data, final int position) {
            if(Check.isEmpty(data.getOrderContentListName())){
                holder.setVisible(R.id.cpOrderContentName1, false);
                holder.setVisible(R.id.cpOrderContentLine, false);
            }else{
                holder.setText(R.id.cpOrderContentName1, data.getOrderContentListName());
                holder.setVisible(R.id.cpOrderContentName1, true);
                holder.setVisible(R.id.cpOrderContentLine, true);
            }
            GridLayoutManager gridLayoutManager = null;
            if(data.getShowNumber()==3){
                gridLayoutManager= new GridLayoutManager(getContext(), 3, OrientationHelper.VERTICAL, false);
            }else if(data.getShowNumber()==2){
                gridLayoutManager= new GridLayoutManager(getContext(), 2, OrientationHelper.VERTICAL, false);
            }else{
                gridLayoutManager= new GridLayoutManager(getContext(), 1, OrientationHelper.VERTICAL, false);
            }
            RecyclerView recyclerView = holder.getView(R.id.cpOrderContentList1);
            recyclerView.setLayoutManager(gridLayoutManager);
            CPOreder2ContentGameAdapter cpOrederContentGameAdapter = null;
            cpOrederContentGameAdapter = new CPOreder2ContentGameAdapter(getContext(), R.layout.item_cp_order_content2, data.getData(),data.getShowType());
            recyclerView.setAdapter(cpOrederContentGameAdapter);
        }
    }

    class CPOreder2ContentGameAdapter extends AutoSizeRVAdapter<CPOrderContentResult> {
        private Context context;
        private String showType;

        public CPOreder2ContentGameAdapter(Context context, int layoutId, List datas,String showType) {
            super(context, layoutId, datas);
            context = context;
            this.showType = showType;
        }
        public CPOreder2ContentGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }


        private void onSetKSImageView01(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_6);
                    break;
            }
        }

        private void onSetKSImageView02(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_6);
                    break;
            }
        }

        private void onSetKSImageView03(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_6);
                    break;
            }
        }

        @Override
        protected void convert(ViewHolder holder,final CPOrderContentResult data, final int position) {
            switch (showType){
                case "TU":
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    holder.setVisible(R.id.cpOrderContentName2,false);
                    holder.setVisible(R.id.cpOrderContentIm2,true);
                    switch (data.getOrderName()){
                        case "1":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_one);
                            break;
                        case "2":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_two);
                            break;
                        case "3":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_three);
                            break;
                        case "4":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_four);
                            break;
                        case "5":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_five);
                            break;
                        case "6":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_six);
                            break;
                        case "7":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_seven);
                            break;
                        case "8":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_eight);
                            break;
                        case "9":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_nine);
                            break;
                        case "10":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_ten);
                            break;
                    }

                    break;
                case "QIU":
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    holder.setText(R.id.cpOrderContentName2, data.getOrderName());
                    holder.setBackgroundRes(R.id.cpOrderContentName2,R.mipmap.cp_qiu);
                    break;
                case "DANIEL":
                    holder.setVisible(R.id.cpOrderContentKS, true);
                    holder.setVisible(R.id.cpOrderContentName2, false);
                    holder.setVisible(R.id.cpOrderContentState04, false);
                    ArrayList<String> dataList = new ArrayList<>();
                    String[] sdata = data.getOrderName().split("_");
                    onSetKSImageView01(sdata[0],holder);
                    onSetKSImageView02(sdata[1],holder);
                    onSetKSImageView03(sdata[2],holder);
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    break;
                case "DANIEL_":
                    holder.setVisible(R.id.cpOrderContentKS, true);
                    holder.setVisible(R.id.cpOrderContentState04, true);
                    holder.setVisible(R.id.cpOrderContentNormal, false);
                    String[] sdat_a = data.getOrderName().split("_");
                    if(sdat_a.length==2){
                        onSetKSImageView01(sdat_a[0],holder);
                        onSetKSImageView02(sdat_a[1],holder);
                        holder.setVisible(R.id.cpOrderContentIm03, false);
                    }else{
                        onSetKSImageView01(sdat_a[0],holder);
                        holder.setVisible(R.id.cpOrderContentIm02, false);
                        holder.setVisible(R.id.cpOrderContentIm03, false);
                    }
                    holder.setVisible(R.id.cpOrderContentName2, false);
                    holder.setVisible(R.id.cpOrderContentState, false);
                    holder.setText(R.id.cpOrderContentState04, data.getOrderState());
                    break;
                default:
                    holder.setText(R.id.cpOrderContentName2, data.getOrderName());
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    break;
            }

            if(data.isChecked()){
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.cp_order_tv_clicked);
            }else{
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.title_text);
            }
            holder.setOnClickListener(R.id.cpOrderContentItem, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isCloseLottery){
                        onResetData();
                        return;
                    }
                    String name  = data.getFullName().equals("")?(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName()):data.getFullName()+" - "+(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName());
                    if("_".equals(name.substring(name.length() -1, name.length()))){
                        name = name.substring(0, name.length() -1);
                    }
                    if(rX0 == 2|| rX0 == 3){
                        if(!CPBetManager.getSingleton().inContain(type+"_"+data.getOrderId())){
                            int size = CPBetManager.getSingleton().onListSize();
                            if(size>=8){
                                showMessage("不允许超过8个选项");
                                return;
                            }
                        }
                    }else if(rX0 == 4|| rX0 == 5){
                        if(!CPBetManager.getSingleton().inContain(type+"_"+data.getOrderId())){
                            int size = CPBetManager.getSingleton().onListSize();
                            if(size>=6){
                                showMessage("不允许超过6个选项");
                                return;
                            }
                        }
                    }

                    CPBetManager.getSingleton().onAddData(type+"",data.getOrderId(),name,data.getOrderState(),type+"_"+data.getOrderId());
                    if(!data.isChecked()){
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(true);
                        data.setChecked(true);
                    }else{
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(false);
                        data.setChecked(false);
                    }
                    notifyDataSetChanged();
                    if(rX0 == 2 || rX0 == 3|| rX0 == 4||rX0 == 5){
                        if(!renXuan2()){
                            return;
                        }
                    }else{
                        cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(CPBetManager.getSingleton().onListSize()+"")+"注"));
                    }
                    GameLog.log("下注的id是："+data.getOrderId());
                    //myAdapter.notifyDataSetChanged();

                    /*cpOrederListRightGameAdapter.notifyDataSetChanged();
                    cpOrderListRight.scrollTo(10,0);*/
                }
            });
        }
    }

    class CPOrederContentGameAdapter extends AutoSizeRVAdapter<CPOrderContentResult> {
        private Context context;
        private String showType;

        public CPOrederContentGameAdapter(Context context, int layoutId, List datas,String showType) {
            super(context, layoutId, datas);
            context = context;
            this.showType = showType;
        }
        public CPOrederContentGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }


        private void onSetKSImageView01(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm01,R.mipmap.s_6);
                    break;
            }
        }

        private void onSetKSImageView02(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm02,R.mipmap.s_6);
                    break;
            }
        }

        private void onSetKSImageView03(String data,ViewHolder holder){
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm03,R.mipmap.s_6);
                    break;
            }
        }

        @Override
        protected void convert(ViewHolder holder,final CPOrderContentResult data, final int position) {
            switch (showType){
                case "TU":
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    holder.setVisible(R.id.cpOrderContentName2,false);
                    holder.setVisible(R.id.cpOrderContentIm2,true);
                    switch (data.getOrderName()){
                        case "1":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_one);
                            break;
                        case "2":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_two);
                            break;
                        case "3":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_three);
                            break;
                        case "4":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_four);
                            break;
                        case "5":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_five);
                            break;
                        case "6":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_six);
                            break;
                        case "7":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_seven);
                            break;
                        case "8":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_eight);
                            break;
                        case "9":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_nine);
                            break;
                        case "10":
                            holder.setBackgroundRes(R.id.cpOrderContentIm2,R.mipmap.cp_ten);
                            break;
                    }

                    break;
                case "QIU":
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    holder.setText(R.id.cpOrderContentName2, data.getOrderName());
                    holder.setBackgroundRes(R.id.cpOrderContentName2,R.mipmap.cp_qiu);
                    break;
                case "DANIEL":
                    holder.setVisible(R.id.cpOrderContentKS, true);
                    holder.setVisible(R.id.cpOrderContentName2, false);
                    holder.setVisible(R.id.cpOrderContentState04, false);
                    ArrayList<String> dataList = new ArrayList<>();
                    String[] sdata = data.getOrderName().split("_");
                    onSetKSImageView01(sdata[0],holder);
                    onSetKSImageView02(sdata[1],holder);
                    onSetKSImageView03(sdata[2],holder);
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    break;
                case "DANIEL_":
                    holder.setVisible(R.id.cpOrderContentKS, true);
                    holder.setVisible(R.id.cpOrderContentState04, true);
                    holder.setVisible(R.id.cpOrderContentNormal, false);
                    String[] sdat_a = data.getOrderName().split("_");
                    if(sdat_a.length==2){
                        onSetKSImageView01(sdat_a[0],holder);
                        onSetKSImageView02(sdat_a[1],holder);
                        holder.setVisible(R.id.cpOrderContentIm03, false);
                    }else{
                        onSetKSImageView01(sdat_a[0],holder);
                        holder.setVisible(R.id.cpOrderContentIm02, false);
                        holder.setVisible(R.id.cpOrderContentIm03, false);
                    }
                    holder.setVisible(R.id.cpOrderContentName2, false);
                    holder.setVisible(R.id.cpOrderContentState, false);
                    holder.setText(R.id.cpOrderContentState04, data.getOrderState());
                    break;
                default:
                    holder.setText(R.id.cpOrderContentName2, data.getOrderName());
                    holder.setText(R.id.cpOrderContentState, data.getOrderState());
                    break;
            }

            if(data.isChecked()){
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.cp_order_tv_clicked);
            }else{
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.title_text);
            }
            holder.setOnClickListener(R.id.cpOrderContentItem, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isCloseLottery){
                        onResetData();
                        return;
                    }
                    String name  = data.getFullName().equals("")?(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName()):data.getFullName()+" - "+(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName());
                    if("_".equals(name.substring(name.length() -1, name.length()))){
                        name = name.substring(0, name.length() -1);
                    }
                    if(rX0 == 2|| rX0 == 3){
                        if(!CPBetManager.getSingleton().inContain(postionAll+"_"+data.getOrderId())){
                            int size = CPBetManager.getSingleton().onListSize();
                            if(size>=8){
                                showMessage("不允许超过8个选项");
                                return;
                            }
                        }
                    }else if(rX0 == 4|| rX0 == 5){
                        if(!CPBetManager.getSingleton().inContain(postionAll+"_"+data.getOrderId())){
                            int size = CPBetManager.getSingleton().onListSize();
                            if(size>=6){
                                showMessage("不允许超过6个选项");
                                return;
                            }
                        }
                    }

                    CPBetManager.getSingleton().onAddData(postionAll+"",data.getOrderId(),name,data.getOrderState(),postionAll+"_"+data.getOrderId());
                    if(!data.isChecked()){
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(true);
                        data.setChecked(true);
                    }else{
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(false);
                        data.setChecked(false);
                    }
                    notifyDataSetChanged();
                    if(rX0 == 2 || rX0 == 3|| rX0 == 4||rX0 == 5){
                        if(!renXuan2()){
                            return;
                        }
                    }else{
                        cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(CPBetManager.getSingleton().onListSize()+"")+"注"));
                    }
                    GameLog.log("下注的id是："+data.getOrderId());
                    //myAdapter.notifyDataSetChanged();

                    /*cpOrederListRightGameAdapter.notifyDataSetChanged();
                    cpOrderListRight.scrollTo(10,0);*/
                }
            });
        }
    }

    class CPOrederContentGameAdapter2 extends AutoSizeRVAdapter<CPOrderContentResult> {
        private Context context;
        private String showType;

        public CPOrederContentGameAdapter2(Context context, int layoutId, List datas,String showType) {
            super(context, layoutId, datas);
            context = context;
            this.showType = showType;
        }
        public CPOrederContentGameAdapter2(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder,final CPOrderContentResult data, final int position) {
            holder.setVisible(R.id.cpOrderContentName2,false);
            RecyclerView recyclerView = holder.getView(R.id.cpOrderContentRecyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusableInTouchMode(false);
            recyclerView.setFocusable(false);
            recyclerView.setClickable(false);
            CPOrederContentGameAdapter3 cpOrederContentGameAdapter = null;
            ArrayList<String> dataList = new ArrayList<>();
            String[] sdata = data.getOrderName().split("_");
            for(int k=0;k<sdata.length;++k){
                dataList.add(sdata[k]);
            }
            holder.setText(R.id.cpOrderContentState, data.getOrderState());
            cpOrederContentGameAdapter = new CPOrederContentGameAdapter3(getContext(), R.layout.item_cp_order_content3, dataList,"");
            recyclerView.setAdapter(cpOrederContentGameAdapter);
            if(data.isChecked()){
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.cp_order_tv_clicked);
            }else{
                holder.setBackgroundRes(R.id.cpOrderContentItem,R.color.title_text);
            }
            holder.setOnClickListener(R.id.cpOrderContentItem, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isCloseLottery){
                        onResetData();
                        return;
                    }
                    String name  = data.getFullName().equals("")?(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName()):data.getFullName()+" - "+(data.getOrderName().equals("龙")?"蛇":data.getOrderName().equals("虎")?"兔":data.getOrderName());
                    CPBetManager.getSingleton().onAddData(postionAll+"",data.getOrderId(),name,data.getOrderState(),postionAll+"_"+data.getOrderId());
                    if(!data.isChecked()){
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(true);
                        data.setChecked(true);
                    }else{
                        //allResultList.get(postionAll).getData().get(postions).getData().get(position).setChecked(false);
                        data.setChecked(false);
                    }
                    GameLog.log("下注的id是："+data.getOrderId());
                    //myAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(CPBetManager.getSingleton().onListSize()+"")+"注"));
                    /*cpOrederListRightGameAdapter.notifyDataSetChanged();
                    cpOrderListRight.scrollTo(10,0);*/
                }
            });
        }

    }



    class CPOrederContentGameAdapter3 extends AutoSizeRVAdapter<String> {
        private Context context;
        private String showType;

        public CPOrederContentGameAdapter3(Context context, int layoutId, List datas,String showType) {
            super(context, layoutId, datas);
            context = context;
            this.showType = showType;
        }
        public CPOrederContentGameAdapter3(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder,final String data, final int position) {
            switch (data){
                case "1":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_1);
                    break;
                case "2":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_2);
                    break;
                case "3":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_3);
                    break;
                case "4":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_4);
                    break;
                case "5":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_5);
                    break;
                case "6":
                    holder.setBackgroundRes(R.id.cpOrderContentIm3,R.mipmap.s_6);
                    break;
            }
        }

    }


    //标记为红色
    private String onMarkRed(String sign){
        return " <font color='#fdb22b'>" + sign+"</font>";
    }

    class CPOrederGameAdapter extends AutoSizeRVAdapter<HomePageIcon> {
        private Context context;

        public CPOrederGameAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            context = context;
        }

        @Override
        protected void convert(ViewHolder holder, HomePageIcon data, final int position) {
            if(position==0){
               TextView tv =  holder.getView(R.id.tv_item_game_name);
                tv.setGravity(Gravity.CENTER);
            }
            holder.setText(R.id.tv_item_game_name, data.getIconName());
            holder.setOnClickListener(R.id.tv_item_game_name, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        return;
                    }else if(position==1){
                        finish();
                        return;
                    }
                    onCpGameItemClick(position);
                }
            });
        }
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
    }

    @Override
    public void setPresenter(CPOrderContract.Presenter presenter) {

        this.presenter = presenter;
    }

    /*@Override
    protected List<IPresenter> presenters() {
        return Arrays.asList((IPresenter) presenter);
    }*/


    @Subscribe
    public void onPersonBalanceResult(PersonBalanceResult personBalanceResult) {
        GameLog.log("通过发送消息得的的数据" + personBalanceResult.getBalance_ag());
        agMoney = personBalanceResult.getBalance_ag();
        hgMoney = personBalanceResult.getBalance_hg();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        /*if (mainSwipemenu.isMenuShowing()) {
            mainSwipemenu.hideMenu();
        }*/

//        drawer_layout.closeDrawer(GravityCompat.START);
    }

    //等待时长
    class onLotteryTimeThread implements Runnable {
        @Override
        public void run() {
            if (sendAuthTime-- <= 0) {
                sendAuthTime = 0;
                if(null!=executorService){
                    executorService.shutdownNow();
                    executorService.shutdown();
                    executorService = null;
                }
                rightCloseLotteryTime.post(new Runnable() {
                    @Override
                    public void run() {
                        rightOpenLotteryTime.setText("开奖中");
                        presenter.postNextIssue(game_code,x_session_token);
                        presenter.postLastResult(game_code,x_session_token);
                        GameLog.log("开奖中  请求下一个盘口");
                        /*rightCloseLotteryTime.post(new Runnable() {
                            @Override
                            public void run() {
                                if(rightCloseLotteryTime!=null){
                                    rightCloseLotteryTime.setText("已封盘");
                                    //GameLog.log(getString(R.string.n_register_phone_waiting) + sendAuthTime + "s");
                                }
                            }
                        });*/
                       // presenter.postNextIssue(game_code,x_session_token);
                    }
                });
            } else {
                rightCloseLotteryTime.post(new Runnable() {
                    @Override
                    public void run() {
                        if(rightOpenLotteryTime!=null){
                            rightOpenLotteryTime.setText(TimeHelper.getTimeString(sendAuthTime));
                            //GameLog.log(getString(R.string.n_register_phone_waiting) + sendAuthTime + "s");
                        }
                    }
                });
            }
        }
    }
    class onWaitingEndThread implements Runnable {
        @Override
        public void run() {
            if (sendEndTime-- <= 0) {
                sendEndTime = 0;
                isCloseLottery = true;
                if(null!=executorEndService){
                    executorEndService.shutdownNow();
                    executorEndService.shutdown();
                    executorEndService = null;
                }
                rightCloseLotteryTime.post(new Runnable() {
                    @Override
                    public void run() {
                        cpOrderGold.setClickable(false);
                        cpOrderGold.setFocusable(false);
                        cpOrderGold.setFocusableInTouchMode(false);
                        cpOrderNoYet.setVisibility(View.VISIBLE);
                        onResetData();
                        rightCloseLotteryTime.setText("已封盘");
                        GameLog.log("已封盘  等待开奖");
                    }
                });
            } else {
                rightCloseLotteryTime.post(new Runnable() {
                    @Override
                    public void run() {
                        if(rightCloseLotteryTime!=null){
                            rightCloseLotteryTime.setText(TimeHelper.getTimeString(sendEndTime));
                            //GameLog.log(getString(R.string.n_register_phone_waiting) + sendAuthTime + "s");
                        }
                    }
                });
            }
        }
    }


    private void onSartTime(){
        onSendAuthCode();
        onSendEndCode();
    }

    //计数器，用于倒计时使用
    private void onSendAuthCode() {
        GameLog.log("-----开始-----");
        if(null!=executorService){
            executorService.shutdownNow();
            executorService.shutdown();
            executorService = null;
        }

        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(lotteryTimeThread, 0, 1000, TimeUnit.MILLISECONDS);
    }

    //计数器，用于倒计时使用
    private void onSendEndCode() {
        GameLog.log("-----开始-----");
        if(null!=executorEndService){
            executorEndService.shutdownNow();
            executorEndService.shutdown();
            executorEndService = null;
        }

        executorEndService = Executors.newScheduledThreadPool(1);
        executorEndService.scheduleAtFixedRate(onWaitingEndThread, 0, 1000, TimeUnit.MILLISECONDS);
    }


    private void onResetDataChecked(){
        /*int size  = allResultList.size();
        for(int i=0;i<size;i++){
            CPOrderAllResult cpOrderAllResult =  allResultList.get(i);
            int size2 = cpOrderAllResult.getData().size();
            for(int j=0;j<size2;++j){
                CPOrderContentListResult cpOrderContentListResult = cpOrderAllResult.getData().get(j);
                int size3 = cpOrderContentListResult.getData().size();
                for(int k=0;k<size3;++k){
                    CPOrderContentResult cpOrderContentResult = cpOrderContentListResult.getData().get(k);
                    cpOrderContentResult.setChecked(false);
                }
            }
        }*/

        int size2 = cpOrderContentListResults.size();
        for(int j=0;j<size2;++j){
            CPOrderContentListResult cpOrderContentListResult = cpOrderContentListResults.get(j);
            int size3 = cpOrderContentListResult.getData().size();
            for(int k=0;k<size3;++k){
                CPOrderContentResult cpOrderContentResult = cpOrderContentListResult.getData().get(k);
                cpOrderContentResult.setChecked(false);
            }
        }
    }

    private void onResetData(){
        onResetDataChecked();
                /*cpOrederListRightGameAdapter = new CPOrederListRightGameAdapter(getContext(), R.layout.item_cp_order_content1, allResultList.get(postionAll).getData());
                cpOrderListRight.setAdapter(cpOrederListRightGameAdapter);*/
        cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
        CPBetManager.getSingleton().onClearData();
        cpOrderGold.setText("");
        if(!Check.isNull(cpOreder2ListRightGameAdapter))
            cpOreder2ListRightGameAdapter.notifyDataSetChanged();
        if(!Check.isNull(cpOrederListRightGameAdapter))
            cpOrederListRightGameAdapter.notifyDataSetChanged();
        GameLog.log("重置了 ");
    }

    @OnClick({R.id.cpOrderTitle,R.id.cpOrderShow,R.id.llCPOrderAll,R.id.cpOrderMenu,R.id.cpOrderReset,R.id.cpOrderSubmit})
    public void onClickedView(View view ){
        switch (view.getId()){
            case R.id.cpOrderSubmit:
                String gold = cpOrderGold.getText().toString();
                if(Check.isEmpty(gold)){
                    showMessage("请输入投注金额");
                    return;
                }
                if(CPBetManager.getSingleton().onListSize()>0){
                    BetCPOrderDialog.newInstance(CPBetManager.getSingleton().onShowViewListData(),gold,game_code,round,x_session_token).show(getSupportFragmentManager());
                }else{
                    showMessage("请选择玩法");
                }
                break;
            case R.id.cpOrderReset:
                onResetData();
//                allResultList.;
                break;
            case R.id.cpOrderTitle:
            case R.id.cpOrderShow:
                slidingLeftMenu.toggle();
                /*if (mainSwipemenu.isMenuShowing()) {
                    mainSwipemenu.hideMenu();
                } else {
                    mainSwipemenu.showMenu();
                }*/
                /*if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                }*/
//                drawer_layout.openDrawer(cpOrderListLeft);
                break;
            case R.id.llCPOrderAll:
                /*if (mainSwipemenu.isMenuShowing()) {
                    mainSwipemenu.hideMenu();
                }*/
                /*if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                }*/
                break;
            case R.id.cpOrderMenu:
                showMessage("开发中。。。");
                break;
        }

    }

    private void onCpGameItemClick(int position) {
        switch (position){
            case 2:
                orderStype = "bjsc";
                break;
            case 3:
                orderStype = "cqssc";
                break;
            case 10:
                orderStype = "pcdd";
                break;
        }
        onChangeData();
        slidingLeftMenu.toggle();
        cpOrderTitle.setText(cpGameList.get(position).getIconName());
        GameLog.log("你点击了"+cpGameList.get(position).getIconName());
        /*if (mainSwipemenu.isMenuShowing()) {
            mainSwipemenu.hideMenu();
        } else {
            mainSwipemenu.showMenu();
        }*/
//        drawer_layout.closeDrawer(GravityCompat.START);
        onSartTime();
    }

    @Subscribe
    public void onEventMain(CPOrderSuccessEvent cpOrderSuccessEvent){
        presenter.postCPLeftInfo("",x_session_token);
        onResetDataChecked();
        if(!Check.isNull(cpOrederListRightGameAdapter))
        cpOrederListRightGameAdapter.notifyDataSetChanged();
        if(!Check.isNull(cpOreder2ListRightGameAdapter))
            cpOreder2ListRightGameAdapter.notifyDataSetChanged();
        CPBetManager.getSingleton().onClearData();
        cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
        cpOrderGold.setText("");
    }


    @Subscribe
    public void onEventMain(LeftMenuEvents leftMenuEvents){
        GameLog.log("LeftMenuEvents "+leftMenuEvents.toString());
        slidingLeftMenu.toggle();
        if (game_code != leftMenuEvents.getEventId()) {
            CPBetManager.getSingleton().onClearData();
            cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
            titleName = leftMenuEvents.getEventName();
            cpOrderTitle.setText(titleName);
            game_code = leftMenuEvents.getEventId();
            presenter.postNextIssue(game_code, x_session_token);
            presenter.postLastResult(game_code, x_session_token);
            switch (game_code) {
                case "2":
                case "207":
                case "407":
                case "507":
                case "607":
                    orderStype = "cqssc";
                    break;
                case "51":
                case "189":
                case "222":
                case "168"://幸运飞艇 暂无
                    orderStype = "bjsc";
                    break;
                case "47":
                case "3":
                    break;
                case "21"://广东11选5 暂无
                    break;
                case "65"://"北京快乐8" 暂无
                    group = "group5";
                    break;
                case "159":
                case "384":
                    break;
                case "69":
                    group = "group7";
                    //香港六合彩的没有二
                    break;
                case "304":
                    orderStype = "pcdd";
                    break;
            }

            onChangeData();
            //onSartTime();
        }
    }

    private boolean renXuan2(){
        int initSize = CPBetManager.getSingleton().onListSize();
        switch (rX0){
            case 2:
                if(initSize>=2){
                    xiazhuValue = initSize * (initSize - 1) / 2;
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(xiazhuValue+"")+"注"));
                    return true;
                }else{
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
                }
                break;
            case 3:
                //Cm n =m!/n!/(m-n)!
                if(initSize>=3){
                    xiazhuValue = initSize*(initSize - 1)*(initSize - 2)/6;
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(xiazhuValue+"")+"注"));
                    return true;
                }else{
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
                }
                break;
            case 4:
                if(initSize>=4){
                    xiazhuValue = initSize*(initSize - 1)*(initSize - 2)*(initSize - 3)/24;
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(xiazhuValue+"")+"注"));
                    return true;
                }else{
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
                }
                break;
            case 5:
                if(initSize>=5){
                    xiazhuValue = initSize*(initSize - 1)*(initSize - 2)*(initSize - 3)*(initSize - 4)/120;
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(xiazhuValue+"")+"注"));
                    return true;
                }else{
                    cpOrderNumber.setText(Html.fromHtml("已选中"+onMarkRed(0+"")+"注"));
                }
                break;
        }
        return false;

    }


}
