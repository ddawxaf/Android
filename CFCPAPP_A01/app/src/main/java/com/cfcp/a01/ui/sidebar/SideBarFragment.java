package com.cfcp.a01.ui.sidebar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfcp.a01.R;
import com.cfcp.a01.base.BaseDialogFragment;
import com.cfcp.a01.base.BaseFragment;
import com.cfcp.a01.common.adapters.AutoSizeRVAdapter;
import com.cfcp.a01.data.LoginResult;
import com.cfcp.a01.ui.me.MeIconEvent;
import com.cfcp.a01.utils.GameLog;
import com.cfcp.a01.utils.NetworkUtils;
import com.cfcp.a01.widget.GridRvItemDecoration;
import com.cfcp.a01.widget.NExpandableListView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SideBarFragment extends BaseDialogFragment {
    @BindView(R.id.sidebarFrame)
    FrameLayout sidebarFrame;
    @BindView(R.id.sidebarUser)
    TextView sidebarUser;
    @BindView(R.id.sidebarDeposit)
    LinearLayout sidebarDeposit;
    @BindView(R.id.sidebarWithDraw)
    LinearLayout sidebarWithDraw;
    @BindView(R.id.sidebarRecyView)
    NExpandableListView sidebarRecyView;


    // 数据源
    private String[] groups = {
            "投注记录",
            "报表管理",
            "账号管理",
            "代理管理",
            "短信管理",
            "开奖结果",
            "返回大厅",
            "退出登录" };
    private String[][] children = {
            { "游戏记录", "追号记录" },
            { "充值记录", "个人报表","团队报表", "账变报表", "优惠活动详情" },
            { "个人总览", "修改密码", "密码设定","银行卡管理", "资料修改", "彩种信息" , "彩种额度"},
            { "团队总览", "用户列表","注册管理", "推广注册"  },
            { "站内短信", "网站公告" },
            {  },
            {  },
            {  }
    };
    private MyExpandableAdapter myExAdapter;
    static {

    }

    public static SideBarFragment newInstance() {
        SideBarFragment MeFragment = new SideBarFragment();

        return MeFragment;
    }

   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context ctxWithTheme = new ContextThemeWrapper(getActivity().getApplicationContext(),R.style.margintran);
        //通过生成的Context创建一个LayoutInflater
        LayoutInflater localLayoutInflater = inflater.cloneInContext(ctxWithTheme);
        return super.onCreateView(localLayoutInflater, container, savedInstanceState);
    }*/

    @Override
    public int setLayoutId() {
        return R.layout.fragment_sidebar;
    }


    @Override
    public void setEvents(View view,@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //sidebarFrame.getBackground().setAlpha(200);
        myExAdapter = new MyExpandableAdapter(getContext(), groups, children);
        sidebarRecyView.setAdapter(myExAdapter);
        sidebarRecyView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition){
                    case 5:
                        showMessage("开奖结果");
                        break;
                    case 6:
                        showMessage("返回大厅");
                        break;
                    case 7:
                        showMessage("退出登录");
                        break;
                }
                return false;
            }
        });
        sidebarRecyView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition){
                    case 0:
                        switch (childPosition){
                            case 0:
                                showMessage("游戏记录");
                                break;
                            case 1:
                                showMessage("追号记录");
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition){
                            case 0:
                                showMessage("充值记录");
                                break;
                            case 1:
                                showMessage("个人报表");
                                break;
                            case 2:
                                showMessage("团队报表");
                                break;
                            case 3:
                                showMessage("账变报表");
                                break;
                            case 4:
                                showMessage("优惠活动详情");
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition){
                            case 0:
                                showMessage("个人总览");
                                break;
                            case 1:
                                showMessage("修改密码");
                                break;
                            case 2:
                                showMessage("密码设定");
                                break;
                            case 3:
                                showMessage("银行卡管理");
                                break;
                            case 4:
                                showMessage("资料修改");
                                break;
                            case 5:
                                showMessage("彩种信息");
                                break;
                            case 6:
                                showMessage("彩种额度");
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition){
                            case 0:
                                showMessage("团队总览");
                                break;
                            case 1:
                                showMessage("用户列表");
                                break;
                            case 2:
                                showMessage("注册管理");
                                break;
                            case 3:
                                showMessage("推广注册");
                                break;
                        }
                        break;
                    case 4:
                        switch (childPosition){
                            case 0:
                                showMessage("站内短信");
                                break;
                            case 1:
                                showMessage("网站公告");
                                break;
                        }
                        break;

                }
                return false;
            }
        });
        sidebarRecyView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < groups.length; i++) {
                    if (groupPosition != i) {
                        sidebarRecyView.collapseGroup(i);
                    }
                }
            }
        });
    }




    @Subscribe
    public void onEventMain(LoginResult loginResult) {
        GameLog.log("================注册页需要消失的================");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.sidebarFrame,R.id.sidebarUser, R.id.sidebarDeposit, R.id.sidebarWithDraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sidebarFrame:
                hide();
                break;
            case R.id.sidebarUser:
                break;
            case R.id.sidebarDeposit:
                break;
            case R.id.sidebarWithDraw:
                break;
        }
    }
}
