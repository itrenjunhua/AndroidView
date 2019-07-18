package com.renj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2018-05-10   18:10
 * <p>
 * 描述：可以设置是否能滑动的 {@link ViewPager}
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class MyViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public MyViewPager(@NonNull Context context) {
        this(context, null);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);
        isCanScroll = typedArray.getBoolean(R.styleable.MyViewPager_is_can_scroll, true);
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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll ? super.onInterceptTouchEvent(ev) : false;
    }
}
