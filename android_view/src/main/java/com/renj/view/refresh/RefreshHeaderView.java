package com.renj.view.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
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
public class RefreshHeaderView extends RelativeLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private int mHeaderHeight;
    private ProgressBar pbRefresh;
    private ImageView ivArrow;
    private TextView tvRefresh;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public RefreshHeaderView(@NonNull Context context) {
        this(context,null);
    }

    public RefreshHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.module_view_refresh_header_height);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.module_view_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.module_view_rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvRefresh = findViewById(R.id.tv_refresh);
        pbRefresh = findViewById(R.id.pb_refresh);
        ivArrow = findViewById(R.id.iv_arrow);
    }

    @Override
    public void onRefresh() {
        ivArrow.clearAnimation();
        pbRefresh.setVisibility(VISIBLE);
        ivArrow.setVisibility(GONE);
        tvRefresh.setText("正在刷新...");
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivArrow.setVisibility(VISIBLE);
            pbRefresh.setVisibility(GONE);
            if (y > mHeaderHeight) {
                tvRefresh.setText("释放刷新");
                if (!rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }
                tvRefresh.setText("下拉刷新");
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        ivArrow.clearAnimation();
        pbRefresh.setVisibility(GONE);
        ivArrow.setVisibility(GONE);
        tvRefresh.setText("刷新完成");
    }

    @Override
    public void onReset() {
        ivArrow.clearAnimation();
        pbRefresh.setVisibility(GONE);
        ivArrow.setVisibility(VISIBLE);
        tvRefresh.setText("下拉刷新");
    }
}
