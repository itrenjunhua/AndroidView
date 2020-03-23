package com.renj.view.radius;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
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
 * <b>注意：使用的裁剪画布形式，目前该api不支持抗锯齿效果</b>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusLinearLayout extends AutoLinearLayout {
    private final int DEFAULT_RADIUS = 0;

    // 控件宽高
    private int width, height;
    // 圆角参数
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

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
        if (Build.VERSION.SDK_INT < 18) setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 读取圆角配置
        TypedArray roundArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusView);
        radius = roundArray.getDimensionPixelSize(R.styleable.RadiusView_radius_all, DEFAULT_RADIUS);
        leftTopRadius = roundArray.getDimensionPixelSize(R.styleable.RadiusView_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = roundArray.getDimensionPixelSize(R.styleable.RadiusView_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = roundArray.getDimensionPixelSize(R.styleable.RadiusView_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = roundArray.getDimensionPixelSize(R.styleable.RadiusView_radius_leftBottom, DEFAULT_RADIUS);
        roundArray.recycle();

        // 角度边长不能小于0
        if (DEFAULT_RADIUS >= radius) radius = DEFAULT_RADIUS;
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS >= leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS >= rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS >= rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS >= leftBottomRadius) leftBottomRadius = radius;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
            super.draw(canvas);
        } else {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            // 修正四个角的各个方向的长度，防止产生非 凸起路径(ConvexPath)，导致outline.setConvexPath()方法失败
            // 所以4个圆角，应该有8个长度(每个圆角都由两个长度构成)
            final Path path = RadiusUtils.calculateRadiusPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
            canvas.clipPath(path);

            // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(path);
                    }
                });
                setClipToOutline(true);
            }
            super.draw(canvas);
        }
    }
}
