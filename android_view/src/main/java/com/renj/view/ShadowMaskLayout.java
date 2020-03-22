package com.renj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-22   13:53
 * <p>
 * 描述：阴影或者发光效果布局
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ShadowMaskLayout extends FrameLayout {

    public static final int TYPE_SHADOW = 0;
    public static final int TYPE_MASK = 1;

    // 阴影还是发光，默认阴影
    private int shadowMaskType = TYPE_SHADOW;
    // 阴影或者发光大小
    private int shadowMaskSize = 0;
    // 阴影或者发光颜色
    private int shadowMaskColor = Color.BLACK;
    // 各个方向的占位
    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;
    // 圆角参数
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;
    // 阴影偏移值
    private int shadowDx = 0;
    private int shadowDy = 0;

    // 控件的宽和高
    private int width, height;
    private Paint paint;

    public ShadowMaskLayout(@NonNull Context context) {
        this(context, null);
    }

    public ShadowMaskLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowMaskLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        // 关闭硬件加速，setShadowLayer()只有文字绘制阴影支持硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowMaskLayout);
        if (typedArray != null) {
            shadowMaskType = typedArray.getInt(R.styleable.ShadowMaskLayout_sm_layout_type, TYPE_SHADOW);
            shadowMaskColor = typedArray.getColor(R.styleable.ShadowMaskLayout_sm_layout_color, Color.BLACK);
            shadowMaskSize = typedArray.getInteger(R.styleable.ShadowMaskLayout_sm_layout_size, 0);

            // 取各个方向的大小
            int elevation = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_elevation, 0);
            paddingLeft = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_left_elevation, elevation);
            paddingTop = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_Top_elevation, elevation);
            paddingRight = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_right_elevation, elevation);
            paddingBottom = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_bottom_elevation, elevation);

            int radius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_radius, 0);
            leftTopRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_leftTop_radius, radius);
            rightTopRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_rightTop_radius, radius);
            rightBottomRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_rightBottom_radius, radius);
            leftBottomRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_leftBottom_radius, radius);

            // 阴影偏移值
            shadowDx = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_shadow_dx, 0);
            shadowDy = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_shadow_dy, 0);
            typedArray.recycle();
        }

        if (paddingLeft < 0) paddingLeft = 0;
        if (paddingTop < 0) paddingTop = 0;
        if (paddingRight < 0) paddingRight = 0;
        if (paddingBottom < 0) paddingBottom = 0;

        if (leftTopRadius < 0) leftTopRadius = 0;
        if (rightTopRadius < 0) rightTopRadius = 0;
        if (rightBottomRadius < 0) rightBottomRadius = 0;
        if (leftBottomRadius < 0) leftBottomRadius = 0;

        this.setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 阴影或者发光占位
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shadowMaskType == TYPE_SHADOW) {
            // 阴影效果
            drawShadowLayer(canvas);
        } else if (shadowMaskType == TYPE_MASK) {
            // 发光效果
            drawMaskFilter(canvas);
        }
        super.onDraw(canvas);
    }

    /**
     * 绘制发光
     */
    private void drawMaskFilter(Canvas canvas) {
        paint.setColor(shadowMaskColor);
        paint.setMaskFilter(new BlurMaskFilter(shadowMaskSize, BlurMaskFilter.Blur.NORMAL)); // 内外发光
        RectF rectF = new RectF(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        canvas.drawRoundRect(rectF, leftTopRadius, leftTopRadius, paint);
    }

    /**
     * 绘制阴影
     */
    private void drawShadowLayer(Canvas canvas) {
        paint.setShadowLayer(shadowMaskSize, shadowDx, shadowDy, shadowMaskColor);
        RectF rectF = new RectF(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        canvas.drawRoundRect(rectF, leftTopRadius, leftTopRadius, paint);
    }
}
