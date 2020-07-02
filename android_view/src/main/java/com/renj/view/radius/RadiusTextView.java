package com.renj.view.radius;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Shader;
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
    private final int DEFAULT_RADIUS = 0; // 默认没有圆角
    public static final int TYPE_SOLID = 0; // 实线
    public static final int TYPE_DASH = 1;  // 虚线

    // 控件宽高
    private int width, height;
    // 圆角参数
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
    // 渐变背景
    private int[] bgShaderColors; // 背景渐变颜色值，优先级高于 bgColorStateList
    private int bgShaderType; // 渐变类型
    private int bgShaderLinearOrientation; // 线性渐变方向
    // 是否需要强制重新布局
    private boolean forceRefreshLayout;

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
        int radius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_all, DEFAULT_RADIUS);
        leftTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_leftTop, DEFAULT_RADIUS);
        rightTopRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_rightTop, DEFAULT_RADIUS);
        rightBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_rightBottom, DEFAULT_RADIUS);
        leftBottomRadius = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_radius_leftBottom, DEFAULT_RADIUS);

        solidWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_width, 0);
        solidColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_rv_solid_color);

        int dashGap = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_dashGap, 0);
        int dashWidth = radiusType.getDimensionPixelSize(R.styleable.RadiusView_rv_solid_dashWidth, 0);
        int lineType = radiusType.getInt(R.styleable.RadiusView_rv_solid_type, TYPE_SOLID);

        bgColorStateList = radiusType.getColorStateList(R.styleable.RadiusView_rv_background_color);

        // 获取渐变信息
        int startColor = radiusType.getColor(R.styleable.RadiusView_rv_shader_start_color, -1);
        int middleColor = radiusType.getColor(R.styleable.RadiusView_rv_shader_middle_color, -1);
        int endColor = radiusType.getColor(R.styleable.RadiusView_rv_shader_end_color, -1);
        bgShaderType = radiusType.getInt(R.styleable.RadiusView_rv_shader_type, -1);
        bgShaderLinearOrientation = radiusType.getInt(R.styleable.RadiusView_rv_shader_linear_orientation, ShaderUtils.LINEAR_ORIENTATION_TOP_TO_BOTTOM);
        bgShaderColors = ShaderUtils.createColorsArray(startColor, middleColor, endColor);
        if (bgShaderColors == null)
            bgShaderType = ShaderUtils.SHADER_TYPE_NONE;

        radiusType.recycle();

        // 角度边长不能小于0
        if (DEFAULT_RADIUS >= radius) radius = DEFAULT_RADIUS;
        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (DEFAULT_RADIUS >= leftTopRadius) leftTopRadius = radius;
        if (DEFAULT_RADIUS >= rightTopRadius) rightTopRadius = radius;
        if (DEFAULT_RADIUS >= rightBottomRadius) rightBottomRadius = radius;
        if (DEFAULT_RADIUS >= leftBottomRadius) leftBottomRadius = radius;


        if (bgColorStateList == null) {
            bgColorStateList = ColorStateList.valueOf(Color.TRANSPARENT);
        }
        if (solidColorStateList == null) {
            solidColorStateList = ColorStateList.valueOf(Color.TRANSPARENT);
        }

        if (lineType == TYPE_DASH) {
            dashPathEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
        } else {
            dashPathEffect = null;
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        this.bgColorStateList = ColorStateList.valueOf(color);
        this.bgShaderType = ShaderUtils.SHADER_TYPE_NONE;
        if (radiusDrawable != null) {
            radiusDrawable.setBackground(bgColorStateList, solidColorStateList);
        }
        forceRefreshLayout();
    }

    public void setBackgroundColor(ColorStateList bgColorStateList) {
        this.bgColorStateList = bgColorStateList;
        this.bgShaderType = ShaderUtils.SHADER_TYPE_NONE;
        if (radiusDrawable != null) {
            radiusDrawable.setBackground(this.bgColorStateList, solidColorStateList);
        }
        forceRefreshLayout();
    }

    public void setSolidColor(int color) {
        this.solidColorStateList = ColorStateList.valueOf(color);
        if (radiusDrawable != null) {
            radiusDrawable.setBackground(this.bgColorStateList, solidColorStateList);
        }
        forceRefreshLayout();
    }

    public void setSolidColor(ColorStateList solidColorStateList) {
        this.solidColorStateList = solidColorStateList;
        if (radiusDrawable != null) {
            radiusDrawable.setBackground(bgColorStateList, this.solidColorStateList);
        }
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

    public void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors) {
        setShaderInfo(shapeType, shapeColors, ShaderUtils.LINEAR_ORIENTATION_TOP_TO_BOTTOM);
    }

    public void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors, @ShaderUtils.LinearOrientation int shaderLinearOrientation) {
        if (shapeColors == null || shapeColors.length <= 0)
            return;
        this.bgShaderType = shapeType;
        this.bgShaderColors = shapeColors;
        this.bgShaderLinearOrientation = shaderLinearOrientation;
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

        final Path bgPath = setBackground();

        // 手动设置阴影，使用裁剪后的路径，防止阴影直角矩形显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = Math.max(getElevation(), getTranslationZ());
            if (elevation > 0) {
                setElevation(elevation);
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        if (bgPath.isConvex()) {
                            outline.setConvexPath(bgPath);
                        } else {
                            outline.setConvexPath(RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius,
                                    leftBottomRadius, rightBottomRadius, width, height, false));
                        }
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

    private Path setBackground() {
        Path bgPath = RadiusUtils.calculateRadiusBgPath(leftTopRadius, rightTopRadius,
                leftBottomRadius, rightBottomRadius, width, height);

        Shader bgShader = null;
        if (bgShaderType != ShaderUtils.SHADER_TYPE_NONE) {
            bgShader = ShaderUtils.createShader(bgShaderType, width, height, bgShaderColors, bgShaderLinearOrientation);
        }

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

            if (bgShader == null)
                radiusDrawable = new RadiusDrawable(bgColorStateList, bgPath, solidColorStateList, solidPath, solidWidth, dashPathEffect);
            else
                radiusDrawable = new RadiusDrawable(bgShader, bgPath, solidColorStateList, solidPath, solidWidth, dashPathEffect);
        } else {
            if (bgShader == null)
                radiusDrawable = new RadiusDrawable(bgColorStateList, bgPath);
            else
                radiusDrawable = new RadiusDrawable(bgShader, bgPath);
        }
        setBackground(radiusDrawable);
        return bgPath;
    }
}
