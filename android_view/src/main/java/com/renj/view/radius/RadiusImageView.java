package com.renj.view.radius;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.renj.view.R;
import com.renj.view.autolayout.AutoImageView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-15   19:11
 * <p>
 * 描述：指定圆角的 ImageView
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusImageView extends AutoImageView {
    // 默认没有圆角
    private final int DEFAULT_RADIUS = 0;

    // 控件宽高
    private int width, height;
    // 圆角参数
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public RadiusImageView(Context context) {
        this(context, null);
    }

    public RadiusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (Build.VERSION.SDK_INT < 18) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 读取圆角配置
        TypedArray roundArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
        radius = roundArray.getDimensionPixelOffset(R.styleable.RoundView_round_radius, DEFAULT_RADIUS);
        leftTopRadius = roundArray.getDimensionPixelOffset(R.styleable.RoundView_round_leftTop_radius, DEFAULT_RADIUS);
        rightTopRadius = roundArray.getDimensionPixelOffset(R.styleable.RoundView_round_rightTop_radius, DEFAULT_RADIUS);
        rightBottomRadius = roundArray.getDimensionPixelOffset(R.styleable.RoundView_round_rightBottom_radius, DEFAULT_RADIUS);
        leftBottomRadius = roundArray.getDimensionPixelOffset(R.styleable.RoundView_round_leftBottom_radius, DEFAULT_RADIUS);
        roundArray.recycle();

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS == leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS == rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS == rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS == leftBottomRadius) leftBottomRadius = radius;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
            super.onDraw(canvas);
        } else {
            // int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
            // int maxRight = Math.max(rightTopRadius, rightBottomRadius);
            // int minWidth = maxLeft + maxRight;
            // int maxTop = Math.max(leftTopRadius, rightTopRadius);
            // int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
            // int minHeight = maxTop + maxBottom;
            // if (width >= minWidth && height > minHeight) {
            Path path = new Path();
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);

            canvas.clipPath(path);
            // }
            super.onDraw(canvas);
        }
    }
}
