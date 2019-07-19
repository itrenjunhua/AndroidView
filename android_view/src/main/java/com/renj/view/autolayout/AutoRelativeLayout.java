package com.renj.view.autolayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.renj.view.R;


/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2018-05-04   10:35
 * <p>
 * 描述：通过设置宽高比例自动适配相对布局控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class AutoRelativeLayout extends RelativeLayout {
    // 自动适配的类型，0：宽适配 1：高适配
    private int auto_type = 1;
    private int auto_width;
    private int auto_height;

    public AutoRelativeLayout(Context context) {
        this(context,null);
    }

    public AutoRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoWidthHeightView);

        auto_height = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_height, 0);
        auto_width = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_width, 0);
        auto_type = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_type, 1);

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        switch (auto_type) {
            case 0: // 动态计算出控件宽
                viewWidth = (int) (viewHeight * ((auto_width * 1.0f) / auto_height));
                break;
            case 1: // 动态计算出控件高
                viewHeight = (int) (viewWidth * ((auto_height * 1.0f) / auto_width));
                break;
            default:
                break;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
