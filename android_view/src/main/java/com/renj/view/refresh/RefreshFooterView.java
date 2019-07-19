package com.renj.view.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.renj.view.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-06-18   11:22
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RefreshFooterView extends RelativeLayout implements SwipeTrigger, SwipeLoadMoreTrigger {
    private int mHeaderHeight;
    private TextView tvLoad;
    private ProgressBar pbLoad;

    public RefreshFooterView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshFooterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.module_view_refresh_footer_height);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoad = findViewById(R.id.tv_load);
        pbLoad = findViewById(R.id.pb_load);
    }

    @Override
    public void onLoadMore() {
        pbLoad.setVisibility(VISIBLE);
        tvLoad.setText("正在加载...");
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (-y > mHeaderHeight) {
                tvLoad.setText("释放加载");
            } else if (y < mHeaderHeight) {
                tvLoad.setText("上拉加载");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        pbLoad.setVisibility(GONE);
        tvLoad.setText("加载完成");
    }

    @Override
    public void onReset() {
        pbLoad.setVisibility(GONE);
        tvLoad.setText("上拉加载");
    }

}
