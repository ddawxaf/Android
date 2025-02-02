package com.hfcp.hf.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hfcp.hf.R;
import com.hfcp.hf.common.base.useraction.UserActionHandler;
import com.hfcp.hf.common.utils.Check;
import com.hfcp.hf.common.utils.InputMethodUtils;
import com.hfcp.hf.common.utils.Timber;
import com.hfcp.hf.common.utils.ToastUtils;
import com.hfcp.hf.common.widget.GifView;
import com.flurry.android.FlurryAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;


/**
 * Created by Daniel on 2018/7/4.
 * 每一个功能的fragment都需要继承此base fragment
 */

public abstract class BaseFragment extends BaseMainFragment implements IMessageView {
    private final boolean LOG_ON = true;

    //public SoftKeyBoardListener SoftKeyBoardListener;
    @Nullable
    Unbinder unbinder;
    private View layoutLoading;
    private GifView ivloading;
    private boolean forbidShowingLoadingView = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (LOG_ON) {
            Timber.d("onAttach  " + this.getClass().getSimpleName());
        }
        //SoftKeyBoardListener = new SoftKeyBoardListener(getActivity());
        UserActionHandler.getInstance().onFragmentStart(this);
    }

    //设置布局文件的id
    public abstract int setLayoutId();

    //初始化一些必要的数据，如第一次进来请求刷新等
    public abstract void setEvents(@Nullable Bundle savedInstanceState);

    public void showToast(CharSequence text) {
        ToastUtils.showShortToastSafe(text);
    }

    public void showToast(@StringRes int resId) {
        ToastUtils.showShortToastSafe(resId);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showLongToast(message);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (LOG_ON) {
            Timber.d("onCreateView  " + this.getClass().getSimpleName());
        }
        int layoutId = setLayoutId();
        if (0 != layoutId) {
            View view = inflater.inflate(R.layout.fragment_base, container, false);

            FrameLayout contentLayout = view.findViewById(R.id.layout_content);

            View contentview = inflater.inflate(layoutId, null, false);
            contentLayout.addView(contentview);
            unbinder = ButterKnife.bind(this, view);
            layoutLoading = view.findViewById(R.id.layout_loading);
            layoutLoading.setOnClickListener(null);
            ivloading = view.findViewById(R.id.iv_loading);
            hideLoadingView();
            setEvents(savedInstanceState);
            return view;
        }
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (LOG_ON) {
            Timber.d("onDetach  " + this.getClass().getSimpleName());
        }
        //添加 有注册监听，但没有注销监听。
        //SoftKeyBoardListener = null;
        UserActionHandler.getInstance().onFragmentStop(this);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (LOG_ON) {
            Timber.d("onViewCreated " + this.getClass().getSimpleName());
        }
        //初始化控制器
        if (null != presenters()) {
            for (IPresenter presenter : presenters()) {
                if (null != presenter) {
                    presenter.start();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LOG_ON) {
            Timber.d("onStart  " + this.getClass().getSimpleName());
        }
        FlurryAgent.onStartSession(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LOG_ON) {
            Timber.d("onResume  " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
        if (LOG_ON) {
            Timber.d("onPause  " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (LOG_ON) {
            Timber.d("onStop  " + this.getClass().getSimpleName());
        }
        FlurryAgent.onEndSession(getContext());
    }

    //跳转到客服页面,统一管理
    public void onStartOnlineService(String url) {
        //此处预留给
        //EventBus.getDefault().post(new StartBrotherEvent(ServiceFragment.newInstance("","")));
       /* Intent intent = new Intent(getContext(),ServiceOnlineActivity.class);
        intent.putExtra("contractservice",url);
        getActivity().startActivity(intent);*/
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (LOG_ON) {
            Timber.d("onDestroyView  " + this.getClass().getSimpleName());
        }
        if (unbinder != null) {
            unbinder.unbind();
        }

        //销毁控制器
        if (null != presenters()) {
            for (IPresenter presenter : presenters()) {
                if (null != presenter) {
                    presenter.destroy();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LOG_ON) {
            Timber.d("onDestroy  " + this.getClass().getSimpleName());
        }
    }

    /**
     * 如果要让{@code PNBaseFragment}管理控制器，请复写此方法
     *
     * @return
     */
    protected List<IPresenter> presenters() {
        return null;
    }

    /**
     * 因为{@linkplain android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}中
     * 只能完成必要的初始化工作，不能做过多的初始化工作，否则影响界面描绘效果
     * 所以，您可以选择复写本方法，完成剩余的初始化工作，例如给{@code ListView}设置{@code adapter}
     *
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (LOG_ON) {
            Timber.d("onLazyInitView  " + this.getClass().getSimpleName());
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    protected void forbidShowingLoadingView() {
        forbidShowingLoadingView = true;
    }

    public void showLoadingView() {
        if (forbidShowingLoadingView) {
            return;
        }
        if (!Check.isNull(layoutLoading) && View.VISIBLE != layoutLoading.getVisibility()) {
            layoutLoading.setVisibility(View.VISIBLE);
            ivloading.setMovieResource(R.raw.loading);
            ivloading.setPaused(false);
        }

    }

    public void hideLoadingView() {
        if (null == layoutLoading) {
            return;
        }
        if (View.VISIBLE == layoutLoading.getVisibility()) {
            ivloading.setPaused(true);
            layoutLoading.setVisibility(View.GONE);
        }
    }

    public void hideKeyboard() {
        InputMethodUtils.hideSoftInput(getView());
    }
}
