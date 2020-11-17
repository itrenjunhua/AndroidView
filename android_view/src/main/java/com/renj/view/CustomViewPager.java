package com.renj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2018-05-10   18:10
 * <p>
 * 描述：可以设置是否能滑动的 {@link ViewPager}，嵌套时可以禁用子ViewPager滑动
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CustomViewPager extends ViewPager {
    private boolean isCanScroll;
    private boolean interceptInnerViewPagerScroll;

    public CustomViewPager(@NonNull Context context) {
        this(context, null);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPager);
        isCanScroll = typedArray.getBoolean(R.styleable.CustomViewPager_is_can_scroll, true);
        interceptInnerViewPagerScroll = typedArray.getBoolean(R.styleable.CustomViewPager_intercept_inner_viewpager, false);
        typedArray.recycle();
    }

    /**
     * 获取是否可以滑动
     *
     * @return true：可以滑动 false：不可以滑动
     */
    public boolean isCanScroll() {
        return isCanScroll;
    }

    /**
     * 设置是否可以滑动
     *
     * @param canScroll true：可以滑动 false：不可以滑动
     */
    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    /**
     * 设置是否拦截子 {@link ViewPager} 的滑动事件（ViewPager嵌套ViewPager并且子ViewPager禁止左右滑动）
     *
     * @param interceptInnerViewPagerScroll true：拦截 false：不拦截
     */
    public void setInterceptInnerViewPagerScroll(boolean interceptInnerViewPagerScroll) {
        this.interceptInnerViewPagerScroll = interceptInnerViewPagerScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return isCanScroll && super.onInterceptTouchEvent(ev);
        } catch (
                IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return isCanScroll && super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (interceptInnerViewPagerScroll) {
            if ((v != this) && (v instanceof ViewPager)) {
                return false;
            }
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
