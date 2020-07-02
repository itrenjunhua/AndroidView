package com.renj.view.radius;

import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;

import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-04-19   23:21
 * <p>
 * 描述：渐变工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ShaderUtils {
    // 渐变类型
    public static final int SHADER_TYPE_LINEAR = 10; // 线性渐变
    public static final int SHADER_TYPE_RADIAL = 11; // 圆形渐变
    public static final int SHADER_TYPE_SWEEP = 12;  // 扫描渐变
    public static final int SHADER_TYPE_NONE = -1;   // 不要渐变

    // 线性渐变方向
    public static final int LINEAR_ORIENTATION_TOP_TO_BOTTOM = 110; // 从上到下
    public static final int LINEAR_ORIENTATION_BOTTOM_TO_TOP = 111; // 从下到上
    public static final int LINEAR_ORIENTATION_LEFT_TO_RIGHT = 220; // 从左到右
    public static final int LINEAR_ORIENTATION_RIGHT_TO_LEFT = 221; // 从右到左
    public static final int LINEAR_ORIENTATION_LEFT_TOP_TO_RIGHT_BOTTOM = 330; // 从左上到右下
    public static final int LINEAR_ORIENTATION_RIGHT_BOTTOM_TO_LEFT_TOP = 331; // 从右下到左上
    public static final int LINEAR_ORIENTATION_RIGHT_TOP_TO_LEFT_BOTTOM = 440; // 从右上到左下
    public static final int LINEAR_ORIENTATION_LEFT_BOTTOM_TO_RIGHT_TOP = 441; // 从左下到右上

    @IntDef(value = {SHADER_TYPE_LINEAR, SHADER_TYPE_RADIAL, SHADER_TYPE_SWEEP})
    public @interface ShaderType {
    }

    @IntDef(value = {LINEAR_ORIENTATION_TOP_TO_BOTTOM, LINEAR_ORIENTATION_BOTTOM_TO_TOP,
            LINEAR_ORIENTATION_LEFT_TO_RIGHT, LINEAR_ORIENTATION_RIGHT_TO_LEFT,
            LINEAR_ORIENTATION_LEFT_TOP_TO_RIGHT_BOTTOM, LINEAR_ORIENTATION_RIGHT_BOTTOM_TO_LEFT_TOP,
            LINEAR_ORIENTATION_RIGHT_TOP_TO_LEFT_BOTTOM, LINEAR_ORIENTATION_LEFT_BOTTOM_TO_RIGHT_TOP})
    public @interface LinearOrientation {
    }

    private ShaderUtils() {
    }

    public static Shader createShader(@ShaderType int shaderType, int width, int height, int[] colors, @LinearOrientation int orientation) {
        if (shaderType == SHADER_TYPE_LINEAR) {
            return createLinearGradient(width, height, colors, orientation);
        } else if (shaderType == SHADER_TYPE_RADIAL) {
            return createRadialGradient(width, height, colors);
        } else if (shaderType == SHADER_TYPE_SWEEP) {
            return createSweepGradient(width, height, colors);
        } else {
            return createLinearGradient(width, height, colors, orientation);
        }
    }

    public static LinearGradient createLinearGradient(int width, int height, int[] colors, @LinearOrientation int orientation) {
        LinearGradient linearGradient;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        if (LINEAR_ORIENTATION_TOP_TO_BOTTOM == orientation) {
            linearGradient = new LinearGradient(halfWidth, 0, halfWidth, height, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_BOTTOM_TO_TOP == orientation) {
            linearGradient = new LinearGradient(halfWidth, height, halfWidth, 0, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_LEFT_TO_RIGHT == orientation) {
            linearGradient = new LinearGradient(0, halfHeight, width, halfHeight, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_RIGHT_TO_LEFT == orientation) {
            linearGradient = new LinearGradient(width, halfHeight, 0, halfHeight, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_LEFT_TOP_TO_RIGHT_BOTTOM == orientation) {
            linearGradient = new LinearGradient(0, 0, width, height, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_RIGHT_BOTTOM_TO_LEFT_TOP == orientation) {
            linearGradient = new LinearGradient(width, height, 0, 0, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_RIGHT_TOP_TO_LEFT_BOTTOM == orientation) {
            linearGradient = new LinearGradient(width, 0, 0, height, colors, null, Shader.TileMode.CLAMP);
        } else if (LINEAR_ORIENTATION_LEFT_BOTTOM_TO_RIGHT_TOP == orientation) {
            linearGradient = new LinearGradient(0, height, width, 0, colors, null, Shader.TileMode.CLAMP);
        } else {
            linearGradient = new LinearGradient(halfWidth, 0, halfWidth, height, colors, null, Shader.TileMode.CLAMP);
        }
        return linearGradient;
    }

    public static RadialGradient createRadialGradient(int width, int height, int[] colors) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        return new RadialGradient(halfWidth, halfHeight, Math.min(halfWidth, halfHeight), colors, null, Shader.TileMode.CLAMP);
    }

    public static SweepGradient createSweepGradient(int width, int height, int[] colors) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        return new SweepGradient(halfWidth, halfHeight, colors, null);
    }

    public static int[] createColorsArray(int startColor, int middleColor, int endColor) {
        List<Integer> colors = new ArrayList<>();
        if (startColor != -1) {
            colors.add(startColor);
        }
        if (middleColor != -1) {
            colors.add(middleColor);
        }
        if (endColor != -1) {
            colors.add(endColor);
        }
        if (colors.size() > 0) {
            int[] shaderColors = new int[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                shaderColors[i] = colors.get(i);
            }
            return shaderColors;
        }
        return null;
    }
}
