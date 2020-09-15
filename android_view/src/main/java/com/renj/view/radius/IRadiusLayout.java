package com.renj.view.radius;

import android.content.res.ColorStateList;
import android.graphics.DashPathEffect;
import android.support.annotation.IntDef;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-09-15   16:29
 * <p>
 * 描述：圆角布局/控件接口类(不包含RadiusImageView)
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IRadiusLayout {
    int DEFAULT_RADIUS = 0; // 默认没有圆角
    int SOLID_TYPE_SOLID = 0; // 实线
    int SOLID_TYPE_DASH = 1;  // 虚线

    void setBackgroundColor(ColorStateList bgColorStateList);

    void setSolidDashPathEffect(DashPathEffect dashPathEffect);

    void setSolidColor(int color);

    void setSolidColor(ColorStateList solidColorStateList);

    void setRadius(int radius);

    void setRadius(int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius);

    void setLeftTopRadius(int leftTopRadius);

    void setRightTopRadius(int rightTopRadius);

    void setRightBottomRadius(int rightBottomRadius);

    void setLeftBottomRadius(int leftBottomRadius);

    void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors);

    void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors, @ShaderUtils.LinearOrientation int shaderLinearOrientation);

    void setSolidShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors);

    void setSolidShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors, @ShaderUtils.LinearOrientation int shaderLinearOrientation);
}
