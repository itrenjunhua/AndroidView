package com.renj.view.autolayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.renj.view.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2018-01-29   17:52
 * <p>
 * 描述：通过宽高比例自动适配的图片控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class AutoImageView extends AppCompatImageView implements IAutoLayout {
    // 自动适配的类型，0：宽适配 1：高适配
    private int auto_type = 1;
    private int auto_width;
    private int auto_height;


    public AutoImageView(Context context) {
        this(context, null, 0);
    }

    public AutoImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoWidthHeightView);
        auto_height = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_height, 0);
        auto_width = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_width, 0);
        auto_type = typedArray.getInt(R.styleable.AutoWidthHeightView_auto_view_type, -1);
        typedArray.recycle();
    }

    /**
     * @docRoot
     */
    @Override
    public void setAutoViewInfo(int autoWidth, int autoHeight) {
        this.auto_width = autoWidth;
        this.auto_height = autoHeight;
        requestLayout();
    }

    /**
     * @docRoot
     */
    @Override
    public void setAutoViewInfo(@IAutoLayout.AutoType int autoType, int autoWidth, int autoHeight) {
        this.auto_type = autoType;
        this.auto_width = autoWidth;
        this.auto_height = autoHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (auto_type == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

            int viewWidth = getMeasuredWidth();
            int viewHeight = getMeasuredHeight();
            switch (auto_type) {
                case 0: // 动态计算出控件宽
                    viewWidth = (int) (viewHeight * ((auto_width * 1.0f) / auto_height));
                    break;
                case 1: // 动态计算出控件高
                    viewHeight = (int) (viewWidth * ((auto_height * 1.0f) / auto_width));
                    break;
            }

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
