package com.renj.view.radius;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.renj.view.R;
import com.renj.view.autolayout.AutoRelativeLayout;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-16   0:17
 * <p>
 * 描述：指定圆角的 RelativeLayout<br/>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusRelativeLayout extends AutoRelativeLayout {
    private final int DEFAULT_RADIUS = 0;

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

    public RadiusRelativeLayout(Context context) {
        this(context, null);
    }

    public RadiusRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadiusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
