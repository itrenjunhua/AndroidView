package com.renj.view.radius;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.DashPathEffect;
import android.graphics.Outline;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.renj.view.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-15   19:11
 * <p>
 * 描述：指定圆角的 TextView<br/>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusTextView extends AppCompatTextView {
    // 默认没有圆角
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
    private ColorStateList solidColorStateList;
    private DashPathEffect dashPathEffect = null;

    private RadiusDrawable radiusDrawable;
    private ColorStateList bgColorStateList;

    public RadiusTextView(Context context) {
        this(context, null);
    }

    public RadiusTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 读取圆角配置
        TypedArray radiusType = context.obtainStyledAttributes(attrs, R.styleable.RadiusView);
        radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_all, DEFAULT_RADIUS);
        leftTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftBottom, DEFAULT_RADIUS);

        solidWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_width, 0);
        solidColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_solid_color);

        int dashGap = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_dashGap, 0);
        int dashWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_dashWidth, 0);
        int lineType = radiusType.getInt(R.styleable.RadiusView_solid_type, TYPE_SOLID);

        bgColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_background_color);
        radiusType.recycle();

        // 角度边长不能小于0
        if (DEFAULT_RADIUS >= radius) radius = DEFAULT_RADIUS;
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS >= leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS >= rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS >= rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS >= leftBottomRadius) leftBottomRadius = radius;


        if (bgColorStateList == null) {
            bgColorStateList = ColorStateList.valueOf(0xFF000000);
        }
        if (solidColorStateList == null) {
            solidColorStateList = ColorStateList.valueOf(0xFF000000);
        }

        if (lineType == TYPE_DASH) {
            dashPathEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
        } else {
            dashPathEffect = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        final Path bgPath = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius,
                leftBottomRadius, rightBottomRadius, width, height);
        // 边框
        if (solidWidth > 0) {
            List<Path> solidPath = new ArrayList<>();
            if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                    rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
                solidPath.add(RadiusUtils.calculateRectSocketPath(width, height, solidWidth));
            } else {
                Path[] solidPathArray = RadiusUtils.calculateRadiusSocketPath(leftTopRadius, rightTopRadius,
                        leftBottomRadius, rightBottomRadius, width, height, solidWidth);
                solidPath = Arrays.asList(solidPathArray);
            }
            radiusDrawable = new RadiusDrawable(bgColorStateList, bgPath, solidColorStateList, solidPath, solidWidth, dashPathEffect);
        } else {
            radiusDrawable = new RadiusDrawable(bgColorStateList, bgPath);
        }
        setBackground(radiusDrawable);

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getElevation());
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setConvexPath(bgPath);
                }
            });
            setClipToOutline(true);
        }
    }
}
