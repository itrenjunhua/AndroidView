package com.renj.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-23   1:40
 * <p>
 * 描述：画线控件(水平/垂直实线/虚线)
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class LineView extends View {
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ORIENTATION_HORIZONTAL, ORIENTATION_VERTICAL})
    public @interface Orientation {
    }

    public static final int TYPE_SOLID = 0; // 实线
    public static final int TYPE_DASH = 1;  // 虚线

    private float lineWidth;
    private int lineOrientation;
    private Paint paint;
    private float width, height;

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineView, defStyleAttr, 0);
        lineWidth = typedArray.getDimension(R.styleable.LineView_line_width, 0);
        lineOrientation = typedArray.getInt(R.styleable.LineView_line_orientation, ORIENTATION_HORIZONTAL);
        float dashGap = typedArray.getDimension(R.styleable.LineView_line_dashGap, 0);
        float dashWidth = typedArray.getDimension(R.styleable.LineView_line_dashWidth, 0);
        int lineType = typedArray.getInt(R.styleable.LineView_line_type, TYPE_SOLID);
        ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.LineView_line_color);
        typedArray.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        if (lineType == TYPE_SOLID) {
            setLineTypeStyle(TYPE_SOLID, 0, 0, false);
        } else {
            setLineTypeStyle(TYPE_DASH, dashGap, dashWidth, false);
        }
        setColorStateList(colorStateList, false);
    }

    /**
     * 设置线宽度，若不大于0，以控件宽度或者高度作为线的宽度。水平线：控件高 垂直线：控件宽
     *
     * @param lineWidth 宽度
     */
    private void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        // 设置线宽，水平：控件高 垂直：控件宽
        if (lineOrientation == ORIENTATION_HORIZONTAL) {
            paint.setStrokeWidth(lineWidth <= 0 ? height : lineWidth);
        } else {
            paint.setStrokeWidth(lineWidth <= 0 ? width : lineWidth);
        }
        invalidate();
    }

    /**
     * 设置线方向
     *
     * @param lineOrientation 线方向
     */
    private void setLineOrientation(@Orientation int lineOrientation) {
        this.lineOrientation = lineOrientation;
        invalidate();
    }

    /**
     * 设置线类型为实线 {@link #TYPE_SOLID}，与 {@link #setLineTypeStyle(float, float)} 方法会相互覆盖
     */
    private void setSolidLine() {
        setLineTypeStyle(TYPE_SOLID, 0, 0, true);
    }

    /**
     * 设置虚线样式，会自动将线类型改为 虚线{@link #TYPE_DASH}，与 {@link #setSolidLine()} 方法会相互覆盖
     *
     * @param dashGap   虚线间隔
     * @param dashWidth 虚线宽度
     */
    public void setLineTypeStyle(float dashGap, float dashWidth) {
        setLineTypeStyle(TYPE_DASH, dashGap, dashWidth, true);
    }

    /**
     * 设置线颜色
     *
     * @param color 颜色
     */
    private void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    /**
     * 设置线颜色
     *
     * @param colorStateList 颜色
     */
    private void setColorStateList(ColorStateList colorStateList) {
        setColorStateList(colorStateList, true);
    }

    // 设置线颜色
    private void setColorStateList(ColorStateList colorStateList, boolean invalidate) {
        colorStateList = colorStateList != null ? colorStateList : ColorStateList.valueOf(Color.TRANSPARENT);
        int lineColor = colorStateList.getColorForState(getDrawableState(), 0);
        paint.setColor(lineColor);
        if (invalidate) invalidate();
    }

    // 设置线的类型和虚线样式
    private void setLineTypeStyle(int lineType, float dashGap, float dashWidth, boolean invalidate) {
        if (lineType == TYPE_DASH) {
            DashPathEffect dashPathEffect = null;
            if (dashWidth > 0) {
                dashPathEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
            }
            paint.setPathEffect(dashPathEffect);
        } else {
            paint.setPathEffect(null);
        }
        if (invalidate) invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        // 设置线宽，水平：控件高 垂直：控件宽
        if (lineOrientation == ORIENTATION_HORIZONTAL) {
            paint.setStrokeWidth(lineWidth <= 0 ? height : lineWidth);
        } else {
            paint.setStrokeWidth(lineWidth <= 0 ? width : lineWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (lineOrientation == ORIENTATION_VERTICAL) {
            canvas.drawLine(getPaddingLeft(), getPaddingTop(), getPaddingLeft(), height - getPaddingBottom(), paint);
        } else {
            canvas.drawLine(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), getPaddingTop(), paint);
        }
    }
}
