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

    /**
     * 设置背景颜色状态列表
     *
     * @param bgColorStateList 背景颜色状态列表
     */
    void setBackgroundColor(ColorStateList bgColorStateList);

    /**
     * 设置边框虚线样式
     *
     * @param dashPathEffect 边框虚线样式
     */
    void setSolidDashPathEffect(DashPathEffect dashPathEffect);

    /**
     * 设置边框线颜色
     *
     * @param color 边框线颜色
     */
    void setSolidColor(int color);

    /**
     * 设置边框颜色状态列表
     *
     * @param solidColorStateList 边框颜色状态列表
     */
    void setSolidColor(ColorStateList solidColorStateList);

    /**
     * 设置圆角，四个圆角大小一样
     *
     * @param radius 圆角大小
     */
    void setRadius(int radius);

    /**
     * 设置圆角大小，分别设置
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param rightBottomRadius 左下角圆角大小
     * @param leftBottomRadius  右下角圆角大小
     */
    void setRadius(int leftTopRadius, int rightTopRadius, int rightBottomRadius, int leftBottomRadius);

    /**
     * 设置左上角圆角大小
     *
     * @param leftTopRadius 左上角圆角大小
     */
    void setLeftTopRadius(int leftTopRadius);

    /**
     * 设置右上角圆角大小
     *
     * @param rightTopRadius 右上角圆角大小
     */
    void setRightTopRadius(int rightTopRadius);

    /**
     * 设置右下角圆角大小
     *
     * @param rightBottomRadius 右下角圆角大小
     */
    void setRightBottomRadius(int rightBottomRadius);

    /**
     * 设置左下角圆角大小
     *
     * @param leftBottomRadius 左下角圆角大小
     */
    void setLeftBottomRadius(int leftBottomRadius);

    /**
     * 设置背景渐变信息
     *
     * @param shapeType   渐变类型
     * @param shapeColors 渐变颜色数组
     */
    void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors);

    /**
     * 设置背景渐变信息
     *
     * @param shapeType               渐变类型
     * @param shapeColors             渐变颜色数组
     * @param shaderLinearOrientation 渐变方向
     */
    void setShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors, @ShaderUtils.LinearOrientation int shaderLinearOrientation);

    /**
     * 设置边框渐变信息
     *
     * @param shapeType   渐变类型
     * @param shapeColors 渐变颜色数组
     */
    void setSolidShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors);

    /**
     * 设置边框渐变信息
     *
     * @param shapeType               渐变类型
     * @param shapeColors             渐变颜色数组
     * @param shaderLinearOrientation 渐变方向
     */
    void setSolidShaderInfo(@ShaderUtils.ShaderType int shapeType, int[] shapeColors, @ShaderUtils.LinearOrientation int shaderLinearOrientation);
}
