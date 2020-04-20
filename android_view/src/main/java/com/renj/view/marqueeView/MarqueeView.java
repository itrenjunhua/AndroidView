package com.renj.view.marqueeView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.renj.view.R;


/**
 * 自定义滚动布局
 */
public class MarqueeView extends ViewFlipper {

    private int interval = 3000;
    private boolean hasSetAnimDuration = false;
    private int animDuration = 1000;

    private int direction = DIRECTION_BOTTOM_TO_TOP;
    private static final int DIRECTION_BOTTOM_TO_TOP = 0;
    private static final int DIRECTION_TOP_TO_BOTTOM = 1;
    private static final int DIRECTION_RIGHT_TO_LEFT = 2;
    private static final int DIRECTION_LEFT_TO_RIGHT = 3;

    @AnimRes
    private int inAnimResId = R.anim.anim_bottom_in;
    @AnimRes
    private int outAnimResId = R.anim.anim_top_out;

    private int totalDataSize;
    private int currentPosition;
    private boolean isAnimStart = false;
    private MarqueeViewAdapter marqueeViewAdapter;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView, defStyleAttr, 0);

        interval = typedArray.getInteger(R.styleable.MarqueeView_mv_interval, interval);
        hasSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeView_mv_anim_duration);
        animDuration = typedArray.getInteger(R.styleable.MarqueeView_mv_anim_duration, animDuration);

        if (typedArray.hasValue(R.styleable.MarqueeView_mv_anim_direction)) {
            direction = typedArray.getInt(R.styleable.MarqueeView_mv_anim_direction, direction);
            switch (direction) {
                case DIRECTION_BOTTOM_TO_TOP:
                    inAnimResId = R.anim.anim_bottom_in;
                    outAnimResId = R.anim.anim_top_out;
                    break;
                case DIRECTION_TOP_TO_BOTTOM:
                    inAnimResId = R.anim.anim_top_in;
                    outAnimResId = R.anim.anim_bottom_out;
                    break;
                case DIRECTION_RIGHT_TO_LEFT:
                    inAnimResId = R.anim.anim_right_in;
                    outAnimResId = R.anim.anim_left_out;
                    break;
                case DIRECTION_LEFT_TO_RIGHT:
                    inAnimResId = R.anim.anim_left_in;
                    outAnimResId = R.anim.anim_right_out;
                    break;
            }
        } else {
            inAnimResId = R.anim.anim_bottom_in;
            outAnimResId = R.anim.anim_top_out;
        }

        typedArray.recycle();
        setFlipInterval(interval);
        setInAndOutAnimation(inAnimResId, outAnimResId);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setMarqueeViewAdapter(MarqueeViewAdapter adapter) {
        if (adapter == null) return;
        this.marqueeViewAdapter = adapter;
        this.marqueeViewAdapter.setContext(getContext());
        this.marqueeViewAdapter.setMarqueeView(this);
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResId 离开动画的resID
     */
    public void setInAndOutAnimation(@AnimRes int inAnimResId, @AnimRes int outAnimResId) {
        setInAndOutAnimation(inAnimResId, outAnimResId, animDuration);
    }

    /**
     * 设置进入动画和离开动画
     *
     * @param inAnimResId  进入动画的resID
     * @param outAnimResId 离开动画的resID
     * @param animDuration 动画时间
     */
    public void setInAndOutAnimation(@AnimRes int inAnimResId, @AnimRes int outAnimResId, int animDuration) {
        Animation inAnim = AnimationUtils.loadAnimation(getContext(), inAnimResId);
        if (hasSetAnimDuration) inAnim.setDuration(animDuration);
        setInAnimation(inAnim);

        Animation outAnim = AnimationUtils.loadAnimation(getContext(), outAnimResId);
        if (hasSetAnimDuration) outAnim.setDuration(animDuration);
        setOutAnimation(outAnim);
    }

    void updateTotalDataSize(boolean isResetStart, int totalDataSize) {
        this.totalDataSize = totalDataSize;
        if (isResetStart) {
            removeAllViews();
            clearAnimation();
            currentPosition = 0;
            startMarqueeFlipping();
        }
    }

    private void startMarqueeFlipping() {
        if (currentPosition >= 0 && currentPosition < totalDataSize) {
            addView(marqueeViewAdapter.getView(0));

            if (totalDataSize > 1) {
                // 预加载下一个
                addView(marqueeViewAdapter.getView(1));
                currentPosition = 1;
                startFlipping();
            }

            if (getInAnimation() != null) {
                getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (isAnimStart) {
                            animation.cancel();
                        }
                        isAnimStart = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        currentPosition += 1;
                        if (currentPosition < 0 || currentPosition >= totalDataSize) {
                            currentPosition = 0;
                        }
                        if (currentPosition >= 0 && currentPosition < totalDataSize) {
                            View view = marqueeViewAdapter.getView(currentPosition);
                            if (view.getParent() == null)
                                addView(view);
                        }
                        isAnimStart = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
    }
}
