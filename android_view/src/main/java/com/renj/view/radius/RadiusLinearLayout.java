package com.renj.view.radius;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.renj.view.R;
import com.renj.view.autolayout.AutoLinearLayout;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-16   0:13
 * <p>
 * 描述：指定圆角的 LinearLayout<br/>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusLinearLayout extends AutoLinearLayout {
    private final int DEFAULT_RADIUS = 0;
    public static final int TYPE_SOLID = 0; // 实线
    public static final int TYPE_DASH = 1;  // 虚线

    // 控件宽高
    private int width, height;
    // 圆角参数
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;
    // 边框参数
    private int solidWidth;
    private int solidColor;

    private Paint paint;
    private RadiusDrawable radiusDrawable;
    private ColorStateList colorStateList;

    public RadiusLinearLayout(Context context) {
        this(context, null, 0);
    }

    public RadiusLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadiusLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT < 18) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 读取圆角配置
        TypedArray radiusType = context.obtainStyledAttributes(attrs, R.styleable.RadiusView);
        radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_all, DEFAULT_RADIUS);
        leftTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftBottom, DEFAULT_RADIUS);

        solidWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_width, 0);
        solidColor = radiusType.getColor(R.styleable.RadiusView_solid_color, Color.TRANSPARENT);

        int dashGap = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_dashGap, 0);
        int dashWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_dashWidth, 0);
        int lineType = radiusType.getInt(R.styleable.RadiusView_solid_type, TYPE_SOLID);

        colorStateList = radiusType.getColorStateList(R.styleable.RadiusView_background_color);
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0xFF000000);
        }
        radiusType.recycle();

        // 角度边长不能小于0
        if (DEFAULT_RADIUS >= radius) radius = DEFAULT_RADIUS;
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS >= leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS >= rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS >= rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS >= leftBottomRadius) leftBottomRadius = radius;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(solidColor);
        paint.setStrokeWidth(solidWidth);

        if (lineType == TYPE_SOLID) {
            setLineTypeStyle(TYPE_SOLID, 0, 0, false);
        } else {
            setLineTypeStyle(TYPE_DASH, dashGap, dashWidth, false);
        }
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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        final Path path = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
        radiusDrawable = new RadiusDrawable(colorStateList, path);
        setBackground(radiusDrawable);

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getElevation());
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setConvexPath(path);
                }
            });
            setClipToOutline(true);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
            super.draw(canvas);

            // 边框
            if (solidWidth > 0)
                canvas.drawRect(RadiusUtils.calculateRectSocketPath(width, height, solidWidth), paint);
        } else {
            super.draw(canvas);

            // 边框
            if (solidWidth > 0) {
                Path[] result = RadiusUtils.calculateRadiusSocketPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height, solidWidth);
                canvas.drawPath(result[0], paint);
                canvas.drawPath(result[1], paint);
            }
        }
    }
}
