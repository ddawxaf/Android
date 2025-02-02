package com.youjie.cfcpnew.view.springdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.youjie.cfcpnew.R;
import com.youjie.cfcpnew.utils.DensityUtil;


/**
 * 说明
 * 创建时间 2017/1/15.
 */

public class SpringDiaLog {
    private Activity mContext;
    private View mContentView; //弹框内容视图布局ID

    private View.OnClickListener mCloseButtonListener;//关闭按钮点击事件
    private boolean isShowCloseButton = true;//是否显示关闭按钮
    private boolean isCanceledOnTouchOutside = true; //是否点击外围触发关闭事件
    private int mStartAnimAngle = 270;//开始动画角度,0代表从右往左,逆时针算
    private int mContentViewWidth = 280;//内容视图宽度
    private int mContentViewHeight = 350;//内容视图高度

    private ImageView mCloseButton;//关闭按钮
    private ViewGroup androidContentView;//屏幕根视图
    private View mRootView;//根视图
    private RelativeLayout mContainerView; //内容视图背景视图
    private RelativeLayout mAnimationView;//动画View;
    private FrameLayout mContentView_FrameLayout;
    private boolean isShowing;//弹框是否显示
    private double heightY;
    private double widthX;
    private boolean isUseAnimation = true;//是否使用动画

    public SpringDiaLog(Activity mContext, View mContentView) {
        this.mContext = mContext;
        this.mContentView = mContentView;
        initView();
    }

    /**
     * 说明 初始化控件
     * 创建时间 2017/1/15 上午11:20
     */
    @SuppressLint("InflateParams")
    private void initView() {
        initDisplayOpinion();
        double radius = Math.sqrt(DensityUtil.screenhightPx * DensityUtil.screenhightPx + DensityUtil.screenWidthPx * DensityUtil.screenWidthPx);
        heightY = -Math.sin(Math.toRadians(mStartAnimAngle)) * radius;
        widthX = Math.cos(Math.toRadians(mStartAnimAngle)) * radius;
        androidContentView = (ViewGroup) mContext.getWindow().getDecorView();
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_spring_dialog, null);
        if (mRootView != null) {
            mCloseButton =  mRootView.findViewById(R.id.iv_close);
            mCloseButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_close));
            mContainerView =  mRootView.findViewById(R.id.contentView);
            mAnimationView =  mRootView.findViewById(R.id.anim_container);
            mContentView_FrameLayout = mRootView.findViewById(R.id.fl_content_container);
        } else {
            Log.e("控件初始化失败", "LayoutInflater获取根视图失败！");
        }
    }

    /**
     * 说明 初始化屏幕宽高
     * 创建时间 2017/1/17 下午1:50
     */
    private void initDisplayOpinion() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        DensityUtil.density = dm.density;
        DensityUtil.densityDPI = dm.densityDpi;
        DensityUtil.screenWidthPx = dm.widthPixels;
        DensityUtil.screenhightPx = dm.heightPixels;
        DensityUtil.screenWidthDip = DensityUtil.px2dp(mContext, dm.widthPixels);
        DensityUtil.screenHightDip = DensityUtil.px2dp(mContext, dm.heightPixels);
    }

    /**
     * 说明 显示弹框
     * 创建时间 2017/1/15 上午11:33
     */
    public void show() {
        if (mRootView != null) {
            isShowing = true;
            if (isShowCloseButton) {
                mCloseButton.setOnClickListener(view -> {
                    isShowing =false;
                    if (mCloseButtonListener != null) {
                        mCloseButtonListener.onClick(view);
                    }
                    if (!isUseAnimation) {
                        androidContentView.removeView(mRootView);
                    } else {
                        AnimSpring.getInstance(mAnimationView).startTranslationAnim(0, 0, -widthX, -heightY);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> androidContentView.removeView(mRootView), 400);
                    }
                });
            } else {
                mCloseButton.setVisibility(View.GONE);
                if (isCanceledOnTouchOutside) {
                    mRootView.setOnClickListener(view -> {
                        if (!isUseAnimation) {
                            androidContentView.removeView(mRootView);
                        } else {
                            AnimSpring.getInstance(mAnimationView).startTranslationAnim(0, 0, -widthX, -heightY);
                            Handler handler = new Handler();
                            handler.postDelayed(() -> androidContentView.removeView(mRootView), 400);
                        }
                    });
                }
            }

            //设置内容视图布局大小
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = DensityUtil.dp2px(mContext, mContentViewWidth);
            layoutParams.height = DensityUtil.dp2px(mContext, mContentViewHeight);
            mContainerView.setLayoutParams(layoutParams);

            //加入视图操作
            mContentView_FrameLayout.removeAllViews();
            mContentView_FrameLayout.addView(mContentView);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            androidContentView.addView(mRootView, params);

            if (isUseAnimation) {
                //加入视图动画
                double radius = Math.sqrt(DensityUtil.screenhightPx * DensityUtil.screenhightPx + DensityUtil.screenWidthPx * DensityUtil.screenWidthPx);
                heightY = -Math.sin(Math.toRadians(mStartAnimAngle)) * radius;
                widthX = Math.cos(Math.toRadians(mStartAnimAngle)) * radius;
                AnimSpring.getInstance(mAnimationView).startTranslationAnim(widthX, heightY, 0, 0);
            }
        } else {
            Log.e("控件初始化失败", "LayoutInflater获取根视图失败！");
        }
    }

    /**
     * 说明 关闭弹框
     * 创建时间 2017/1/17 下午12:41
     */
    public void close() {
        if (isShowing) {
            if (!isUseAnimation) {
                androidContentView.removeView(mRootView);
            } else {
                AnimSpring.getInstance(mAnimationView).startTranslationAnim(0, 0, -widthX, -heightY);
                Handler handler = new Handler();
                handler.postDelayed(() -> androidContentView.removeView(mRootView), 400);
            }
            isShowing = false;
        } else {
            Log.e("关闭失败", "弹框未显示！");
        }
    }

    //属性初始化

    public SpringDiaLog setShowCloseButton(boolean showCloseButton) {
        isShowCloseButton = showCloseButton;
        return this;
    }

    public SpringDiaLog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        isCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public SpringDiaLog setStartAnimAngle(int mStartAnimAngle) {
        this.mStartAnimAngle = mStartAnimAngle;
        return this;
    }

    public SpringDiaLog setContentViewWidth(int mContentViewWidth) {
        this.mContentViewWidth = mContentViewWidth;
        return this;
    }

    public SpringDiaLog setContentViewHeight(int mContentViewHeight) {
        this.mContentViewHeight = mContentViewHeight;
        return this;
    }

    public SpringDiaLog setUseAnimation(boolean useAnimation) {
        isUseAnimation = useAnimation;
        return this;
    }

    public boolean getIsClose() {
        return isShowing;
    }
}
