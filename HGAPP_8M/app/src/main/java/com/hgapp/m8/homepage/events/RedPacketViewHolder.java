package com.hgapp.m8.homepage.events;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgapp.m8.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 红包弹框
 * @date 2017/11/27  15:12
 */

public class RedPacketViewHolder {

    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.iv_open)
    ImageView mIvOpen;
    @BindView(R.id.ivGetMoney)
    ImageView ivGetMoney;

    private FrameAnimation mFrameAnimation;
    private Context mContext;
    private OnRedPacketDialogClickListener mListener;

    private int[] mImgResIds = new int[]{
            R.mipmap.icon_open_red_packet1,
            R.mipmap.icon_open_red_packet2,
            R.mipmap.icon_open_red_packet3,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet5,
            R.mipmap.icon_open_red_packet6,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet7,
            R.mipmap.icon_open_red_packet8,
            R.mipmap.icon_open_red_packet9,
            R.mipmap.icon_open_red_packet4,
            R.mipmap.icon_open_red_packet10,
            R.mipmap.icon_open_red_packet11,
    };

    public RedPacketViewHolder(Context context, View view) {
        mContext = context;
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.iv_close, R.id.iv_open,R.id.ivGetMoney})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                stopAnim();
                if (mListener != null) {
                    mListener.onCloseClick();
                }
                break;

            case R.id.iv_open:
                if (mFrameAnimation != null) {
                    //如果正在转动，则直接返回
                    return;
                }
                startAnim();

                if (mListener != null) {
                    mListener.onOpenClick();
                }
                mIvOpen.setVisibility(View.VISIBLE);
                mIvOpen.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopAnim();
                        mIvClose.setVisibility(View.GONE);
                        mIvOpen.setVisibility(View.GONE);
                        //mTvMsg.setText("恭喜您\n\n抽到幸运红包38.88元");
                        ivGetMoney.setVisibility(View.VISIBLE);
                        /*if (mListener != null) {
                            mListener.onCloseClick();
                        }*/
                    }
                },2000);
                break;
            case R.id.ivGetMoney:
                if (mListener != null) {
                    mListener.onCloseClick();
                    mIvClose.setVisibility(View.GONE);
                    mIvOpen.setVisibility(View.VISIBLE);
                    ivGetMoney.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setData(RedPacketEntity entity) {
        /*RequestOptions options = new RequestOptions();
        options.centerCrop()
                .circleCrop();

        Glide.with(mContext)
                .load(entity.avatar)
                .apply(options)
                .into(mIvAvatar);*/

        mTvName.setText(entity.name);
        mTvMsg.setText(entity.remark);
    }

    public void setData(String entity) {
        /*RequestOptions options = new RequestOptions();
        options.centerCrop()
                .circleCrop();

        Glide.with(mContext)
                .load(entity.avatar)
                .apply(options)
                .into(mIvAvatar);*/
        mTvMsg.setText("恭喜您\n抽到幸运红包"+entity+"元");
    }

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(mIvOpen, mImgResIds, 125, true);
        mFrameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
                Log.i("", "start");
            }

            @Override
            public void onAnimationEnd() {
                Log.i("", "end");
            }

            @Override
            public void onAnimationRepeat() {
                Log.i("", "repeat");
            }

            @Override
            public void onAnimationPause() {
                mIvOpen.setBackgroundResource(R.mipmap.icon_open_red_packet1);
            }
        });
    }

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }

    public void setOnRedPacketDialogClickListener(OnRedPacketDialogClickListener listener) {
        mListener = listener;
    }
}
