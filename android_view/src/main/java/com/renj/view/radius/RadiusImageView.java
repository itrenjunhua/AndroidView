package com.renj.view.radius;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
    public static final int TYPE_SOLID = 0; // 实线
    public static final int TYPE_DASH = 1;  // 虚线

    // 控件宽高
    private int width, height;
    // 圆角参数
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

    // 是否需要强制重新布局
    private boolean forceRefreshLayout;

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
        int radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_all, DEFAULT_RADIUS);
        leftTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_leftBottom, DEFAULT_RADIUS);

        solidWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_width, 0);
        solidColor = radiusType.getColor(R.styleable.RadiusView_rv_solid_color, Color.TRANSPARENT);

        int dashGap = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_dashGap, 0);
        int dashWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_dashWidth, 0);
        int lineType = radiusType.getInt(R.styleable.RadiusView_rv_solid_type, TYPE_SOLID);
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
            solidPaint.setPathEffect(dashPathEffect);
        } else {
            solidPaint.setPathEffect(null);
        }
        if (invalidate) invalidate();
    }

    public void setSolidColor(int color) {
        solidPaint.setColor(color);
        forceRefreshLayout();
    }

    public void setRadius(int radius) {
        if (radius >= 0) {
            leftTopRadius = radius;
            rightTopRadius = radius;
            rightBottomRadius = radius;
            leftBottomRadius = radius;
            forceRefreshLayout();
        }
    }

    public void setRadius(int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius) {
        this.leftTopRadius = leftTopRadius;
        this.rightTopRadius = rightTopRadius;
        this.rightBottomRadius = rightBottomRadius;
        this.leftBottomRadius = leftBottomRadius;
        forceRefreshLayout();
    }

    public void setLeftTopRadius(int leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
        forceRefreshLayout();
    }

    public void setRightTopRadius(int rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
        forceRefreshLayout();
    }

    public void setRightBottomRadius(int rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
        forceRefreshLayout();
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        forceRefreshLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 没有发生改变，并且不需要强制刷新就不在重新layout
        if (!changed && !this.forceRefreshLayout) {
            return;
        }
        this.forceRefreshLayout = false;

        width = getWidth();
        height = getHeight();

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = Math.max(getElevation(), getTranslationZ());
            if (elevation > 0) {
                setElevation(elevation);
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        Path path = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height, false);
                        outline.setConvexPath(path);
                    }
                });
                setClipToOutline(true);
            }
        }
    }

    private void forceRefreshLayout() {
        this.forceRefreshLayout = true;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (leftTopRadius <= DEFAULT_RADIUS && leftBottomRadius <= DEFAULT_RADIUS &&
                rightTopRadius <= DEFAULT_RADIUS && rightBottomRadius <= DEFAULT_RADIUS) {
            super.onDraw(canvas);

            // 边框
            if (solidWidth > 0)
                canvas.drawPath(RadiusUtils.calculateRectSocketPath(width, height, solidWidth), solidPaint);
        } else {
            Path path = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
            Bitmap bitmap = getBitmapFromDrawable(getDrawable());
            if (bitmap != null) {
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                configureBounds(getDrawable());
                // 设置变换矩阵
                bitmapShader.setLocalMatrix(matrix);
                // 设置shader
                bitmapPaint.setShader(bitmapShader);
                canvas.drawPath(path, bitmapPaint);
            }

            // 边框
            if (solidWidth > 0) {
                Path[] result = RadiusUtils.calculateRadiusSocketPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height, solidWidth);
                canvas.drawPath(result[0], solidPaint);
                canvas.drawPath(result[1], solidPaint);
            }
        }
    }

    // 获取图片资源
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

    // 根据 ScaleType 进行矩阵变换
    private void configureBounds(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        final ScaleType scaleType = getScaleType();
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        final int vWidth = width - getPaddingLeft() - getPaddingRight();
        final int vHeight = height - getPaddingTop() - getPaddingBottom();
        final boolean fits = (intrinsicWidth < 0 || vWidth == intrinsicWidth)
                && (intrinsicHeight < 0 || vHeight == intrinsicHeight);

        if (intrinsicWidth <= 0 || intrinsicHeight <= 0 || ScaleType.FIT_XY == scaleType) {
            matrix = null;
        } else {
            if (ScaleType.MATRIX == scaleType) {
                matrix = null;
            } else if (fits) {
                matrix = null;
            } else if (ScaleType.CENTER == scaleType) {
                if (matrix == null)
                    matrix = new Matrix();
                matrix.setTranslate(Math.round((vWidth - intrinsicWidth) * 0.5f),
                        Math.round((vHeight - intrinsicHeight) * 0.5f));
            } else if (ScaleType.CENTER_CROP == scaleType) {
                float scale;
                float dx = 0, dy = 0;

                if (intrinsicWidth * vHeight > vWidth * intrinsicHeight) {
                    scale = (float) vHeight / (float) intrinsicHeight;
                    dx = (vWidth - intrinsicWidth * scale) * 0.5f;
                } else {
                    scale = (float) vWidth / (float) intrinsicWidth;
                    dy = (vHeight - intrinsicHeight * scale) * 0.5f;
                }

                if (matrix == null)
                    matrix = new Matrix();
                matrix.setScale(scale, scale);
                matrix.postTranslate(Math.round(dx), Math.round(dy));
            } else if (ScaleType.CENTER_INSIDE == scaleType) {
                float scale;
                float dx;
                float dy;

                if (intrinsicWidth <= vWidth && intrinsicHeight <= vHeight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vWidth / (float) intrinsicWidth,
                            (float) vHeight / (float) intrinsicHeight);
                }

                dx = Math.round((vWidth - intrinsicWidth * scale) * 0.5f);
                dy = Math.round((vHeight - intrinsicHeight * scale) * 0.5f);

                if (matrix == null)
                    matrix = new Matrix();
                matrix.setScale(scale, scale);
                matrix.postTranslate(dx, dy);
            } else {
                RectF mTempSrc = new RectF();
                RectF mTempDst = new RectF();
                mTempSrc.set(0, 0, intrinsicWidth, intrinsicHeight);
                mTempDst.set(0, 0, vWidth, vHeight);
                if (matrix == null)
                    matrix = new Matrix();
                matrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
            }
        }
    }
}
