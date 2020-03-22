package com.renj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
            paddingTop = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_top_elevation, elevation);
            paddingRight = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_right_elevation, elevation);
            paddingBottom = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_bottom_elevation, elevation);

            int radius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_radius, 0);
            leftTopRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_leftTop_radius, radius);
            rightTopRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_rightTop_radius, radius);
            rightBottomRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_rightBottom_radius, radius);
            leftBottomRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowMaskLayout_sm_layout_leftBottom_radius, radius);

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
        Path path = calculateRadiusPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius,
                width - paddingLeft - paddingRight, height - paddingTop - paddingBottom);
        path.offset(paddingLeft, paddingTop); // 对路径进行偏移，修改绘制位置
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制阴影
     */
    private void drawShadowLayer(Canvas canvas) {
        paint.setColor(shadowMaskColor);
        paint.setShadowLayer(shadowMaskSize, 0, 0, shadowMaskColor);
        Path path = calculateRadiusPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius,
                width - paddingLeft - paddingRight, height - paddingTop - paddingBottom);
        path.offset(paddingLeft, paddingTop); // 对路径进行偏移，修改绘制位置
        canvas.drawPath(path, paint);
    }

    // ----------------------- 以下代码来自 RadiusUtils.java 类 ---------------------------- //

    /**
     * 根据四个圆角大小和矩形边长计算出真实的4四个圆角的8条边长后的Path
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param leftBottomRadius  左下角圆角大小
     * @param rightBottomRadius 右下角圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @return 结果Path
     */
    private Path calculateRadiusPath(int leftTopRadius, int rightTopRadius,
                                     int leftBottomRadius, int rightBottomRadius,
                                     int width, int height) {

        int leftTopRadiusLeft, leftTopRadiusTop; // 左上角
        int leftBottomRadiusLeft, leftBottomRadiusBottom; // 左下角
        int rightTopRadiusRight, rightTopRadiusTop; // 右上角
        int rightBottomRadiusRight, rightBottomRadiusBottom; // 右下角
        int[] sideTop = calculateRadiusLength(leftTopRadius, rightTopRadius, width); // 上同边
        int[] sideBottom = calculateRadiusLength(leftBottomRadius, rightBottomRadius, width); // 下同边
        int[] sideLeft = calculateRadiusLength(leftTopRadius, leftBottomRadius, height); // 左同边
        int[] sideRight = calculateRadiusLength(rightTopRadius, rightBottomRadius, height); // 右同边
        leftTopRadiusTop = sideTop[0];
        rightTopRadiusTop = sideTop[1];
        leftBottomRadiusBottom = sideBottom[0];
        rightBottomRadiusBottom = sideBottom[1];
        leftTopRadiusLeft = sideLeft[0];
        leftBottomRadiusLeft = sideLeft[1];
        rightTopRadiusRight = sideRight[0];
        rightBottomRadiusRight = sideRight[1];

        Path resultPath = new Path();
        //四个角：右上，右下，左下，左上
        resultPath.moveTo(leftTopRadiusTop, 0);
        resultPath.lineTo(width - rightTopRadiusTop, 0);
        resultPath.quadTo(width, 0, width, rightTopRadiusRight);

        resultPath.lineTo(width, height - rightBottomRadiusRight);
        resultPath.quadTo(width, height, width - rightBottomRadiusBottom, height);

        resultPath.lineTo(leftBottomRadiusBottom, height);
        resultPath.quadTo(0, height, 0, height - leftBottomRadiusLeft);

        resultPath.lineTo(0, leftTopRadiusLeft);
        resultPath.quadTo(0, 0, leftTopRadiusTop, 0);
        return resultPath;
    }


    /**
     * 根据同边的两个圆角的分别长度和边的长度，重新计算两个圆角该有的长度(防止两个圆角的同边长度之后大于总的长度)<br/>
     * 如：给出左上角上边的长度和右上角上边的长度，以及矩形的上边边长(矩形的长)，重新计算出左上角上边的长度和右上角上边的长度，
     * 防止左上角上边的长度和右上角上边的长度之和大于边长导致出错，如果大于边长时，根据比例计算。
     *
     * @param sameSide1     同边第一个值的原大小
     * @param sameSide2     同边第二个值的原大小
     * @param sameSideWidth 同边长度
     * @return int[]，长度为2，int[0]：同边第一个值的最终大小 int[1]：同边第二个值的最终大小
     */
    private int[] calculateRadiusLength(int sameSide1, int sameSide2, int sameSideWidth) {
        int[] result = new int[2];
        if (sameSide1 > 0 && sameSide2 > 0) {
            int topRadiusWidth = sameSide1 + sameSide2;
            if (topRadiusWidth > sameSideWidth) {
                result[0] = (int) ((sameSide1 * 1.0 / topRadiusWidth) * sameSideWidth);
                result[1] = (int) ((sameSide2 * 1.0 / topRadiusWidth) * sameSideWidth);
            } else {
                result[0] = sameSide1;
                result[1] = sameSide2;
            }
        } else if (sameSide1 > 0) {
            result[0] = sameSide1 > sameSideWidth ? sameSideWidth : sameSide1;
            result[1] = 0;
        } else if (sameSide2 > 0) {
            result[0] = 0;
            result[1] = sameSide2 > sameSideWidth ? sameSideWidth : sameSide2;
        } else {
            result[0] = 0;
            result[1] = 0;
        }
        return result;
    }
}
