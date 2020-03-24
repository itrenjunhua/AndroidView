package com.renj.view.radius;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

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
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMENSION = 2;

    // 控件宽高
    private int width, height;
    // 圆角参数
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    // 画图片的画笔
    private Paint bitmapPaint;
    // 画边框的画笔
    private Paint solidPaint;
    // 3x3 矩阵，主要用于缩小放大
    private Matrix matrix;
    //渲染图像，使用图像为绘制图形着色
    private BitmapShader bitmapShader;

    // 边框参数
    private int solidWidth;
    private int solidColor;

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
        TypedArray radiusType = context.obtainStyledAttributes(attrs, R.styleable.RadiusView);
        radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_all, DEFAULT_RADIUS);
        leftTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_radius_leftBottom, DEFAULT_RADIUS);

        solidWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_solid_width, 0);
        solidColor = radiusType.getColor(R.styleable.RadiusView_solid_color, Color.TRANSPARENT);
        radiusType.recycle();

        // 角度边长不能小于0
        if (DEFAULT_RADIUS >= radius) radius = DEFAULT_RADIUS;
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS >= leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS >= rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS >= rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS >= leftBottomRadius) leftBottomRadius = radius;

        matrix = new Matrix();
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setDither(true);

        solidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        solidPaint.setDither(true);
        solidPaint.setStyle(Paint.Style.STROKE);
        solidPaint.setColor(solidColor);
        solidPaint.setStrokeWidth(solidWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getElevation());
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    Path path = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
                    outline.setConvexPath(path);
                }
            });
            setClipToOutline(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
            super.onDraw(canvas);

            // 边框
            if (solidWidth > 0)
                canvas.drawRect(RadiusUtils.calculateRectSocketPath(width, height, solidWidth), solidPaint);
        } else {
            Path path = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
            Bitmap bitmap = getBitmapFromDrawable(getDrawable());
            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            float scale = 1.0f;
            if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
            }
            // shader的变换矩阵，这里主要用于放大或者缩小
            matrix.setScale(scale, scale);
            // 设置变换矩阵
            bitmapShader.setLocalMatrix(matrix);
            // 设置shader
            bitmapPaint.setShader(bitmapShader);
            canvas.drawPath(path, bitmapPaint);

            // 边框
            if (solidWidth > 0) {
                Path[] result = RadiusUtils.calculateRadiusSocketPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height, solidWidth);
                canvas.drawPath(result[0], solidPaint);
                canvas.drawPath(result[1], solidPaint);
            }
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
