package com.renj.view.radius;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-03-22   2:32
 * <p>
 * 描述：圆角矩形操作工具类
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class RadiusUtils {
    public static final int TYPE_RADIUS = 0;  // 圆角形状
    public static final int TYPE_OVAL = 1;    // 两边为椭圆
    public static final int TYPE_CIRCLE = 2;  // 整体为圆形

    /**
     * 计算带圆角边框的背景路径 ConvexPath<br/>
     * <b>ConvexPath 这里简单理解：圆角矩形的圆角度数大于矩形的高度或宽度(上下圆角度数的边长和大于高度或者左右圆角度数的边长大于高度，那么就不是 ConvexPath 了)</b>
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param leftBottomRadius  左下角圆角大小
     * @param rightBottomRadius 右下角圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @return 结果Path
     */
    public static Path calculateRadiusBgPath(int leftTopRadius, int rightTopRadius,
                                             int leftBottomRadius, int rightBottomRadius,
                                             int width, int height) {
        return calculateRadiusBgPath(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height, true);
    }

    /**
     * 计算带圆角边框的背景路径 ConvexPath<br/>
     * <b>ConvexPath 这里简单理解：圆角矩形的圆角度数大于矩形的高度或宽度(上下圆角度数的边长和大于高度或者左右圆角度数的边长大于高度，那么就不是 ConvexPath 了)</b>
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param leftBottomRadius  左下角圆角大小
     * @param rightBottomRadius 右下角圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @param isGetType         是否需要判断类型，当为 true 时可能返回非 Convex Path
     * @return 结果Path
     */
    public static Path calculateRadiusBgPath(int leftTopRadius, int rightTopRadius,
                                             int leftBottomRadius, int rightBottomRadius,
                                             int width, int height, boolean isGetType) {
        if (isGetType) {
            int type = getType(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
            if (type == TYPE_CIRCLE) {
                Path resultPath = new Path();
                RectF rectF = new RectF();
                rectF.set(0, 0, width, height);
                resultPath.addCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.width(), rectF.height()) / 2f, Path.Direction.CW);
                return resultPath;
            }

            if (type == TYPE_OVAL) {
                Path resultPath = new Path();
                if (leftTopRadius + leftBottomRadius >= height) {
                    // 左边半圆
                    RectF leftRectF = new RectF();
                    leftRectF.set(0, 0, height, height);
                    resultPath.addArc(leftRectF, 90, 180);
                    // 增加上边的线
                    resultPath.lineTo(width - height / 2f, 0);
                    // 右边半圆
                    RectF rightRectF = new RectF();
                    rightRectF.set(width - height, 0, width, height);
                    resultPath.addArc(rightRectF, -90, 180);
                    // 增加下边的线
                    resultPath.lineTo(height / 2f, height);
                } else {
                    // 上边半圆
                    RectF leftRectF = new RectF();
                    leftRectF.set(0, 0, width, width);
                    resultPath.addArc(leftRectF, 180, 180);
                    // 增加右边的线
                    resultPath.lineTo(width, height - width / 2f);
                    // 下边半圆
                    RectF rightRectF = new RectF();
                    rightRectF.set(0, height - width, width, height);
                    resultPath.addArc(rightRectF, 0, 180);
                    // 增加左边的线
                    resultPath.lineTo(0, width / 2f);
                }

                return resultPath;
            }
        }

        float leftTopRadiusLeft, leftTopRadiusTop; // 左上角
        float leftBottomRadiusLeft, leftBottomRadiusBottom; // 左下角
        float rightTopRadiusRight, rightTopRadiusTop; // 右上角
        float rightBottomRadiusRight, rightBottomRadiusBottom; // 右下角
        int[] sideTop = calculateRadiusLength(leftTopRadius, rightTopRadius, width); // 上同边
        int[] sideBottom = calculateRadiusLength(leftBottomRadius, rightBottomRadius, width); // 下同边
        int[] sideLeft = calculateRadiusLength(leftTopRadius, leftBottomRadius, height); // 左同边
        int[] sideRight = calculateRadiusLength(rightTopRadius, rightBottomRadius, height); // 右同边
        leftTopRadiusTop = sideTop[0];
        rightTopRadiusTop = sideTop[1];
        leftBottomRadiusBottom = sideBottom[0];
        rightBottomRadiusBottom = sideBottom[1];
        leftTopRadiusLeft = sideLeft[0];
        leftBottomRadiusLeft = sideLeft[1];
        rightTopRadiusRight = sideRight[0];
        rightBottomRadiusRight = sideRight[1];

        Path resultPath = new Path();
        // 四个角：右上，右下，左下，左上
        resultPath.moveTo(leftTopRadiusTop, 0);
        resultPath.lineTo(width - rightTopRadiusTop, 0);
        resultPath.quadTo(width, 0, width, rightTopRadiusRight);

        resultPath.lineTo(width, height - rightBottomRadiusRight);
        resultPath.quadTo(width, height, width - rightBottomRadiusBottom, height);

        resultPath.lineTo(leftBottomRadiusBottom, height);
        resultPath.quadTo(0, height, 0, height - leftBottomRadiusLeft);

        resultPath.lineTo(0, leftTopRadiusLeft);
        resultPath.quadTo(0, 0, leftTopRadiusTop, 0);
        return resultPath;
    }

    /**
     * 计算直角矩形边框路径
     *
     * @param width      宽
     * @param height     高
     * @param solidWidth 线宽
     * @return
     */
    public static Path calculateRectSocketPath(int width, int height, int solidWidth) {
        float newWidth = solidWidth / 2.0f;
        Path result = new Path();
        result.moveTo(newWidth, newWidth);
        result.lineTo(width - newWidth, newWidth);
        result.lineTo(width - newWidth, height - newWidth);
        result.lineTo(newWidth, height - newWidth);
        result.close();
        return result;
    }

    /**
     * 计算圆角边框的边框路径 ConvexPath<br/>
     * <b>ConvexPath 这里简单理解：圆角矩形的圆角度数大于矩形的高度或宽度(上下圆角度数的边长和大于高度或者左右圆角度数的边长大于高度，那么就不是 ConvexPath 了)</b>
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param leftBottomRadius  左下角圆角大小
     * @param rightBottomRadius 右下角圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @param solidWidth        边框宽度
     * @return 结果Path 数组，path[0]：四条边 path[1]：四个角的路径
     */
    public static Path[] calculateRadiusSocketPath(int leftTopRadius, int rightTopRadius,
                                                   int leftBottomRadius, int rightBottomRadius,
                                                   int width, int height, int solidWidth) {
        int type = getType(leftTopRadius, rightTopRadius, leftBottomRadius, rightBottomRadius, width, height);
        if (type == TYPE_CIRCLE) {
            Path[] result = new Path[1];
            Path resultPath = new Path();
            RectF rectF = new RectF();
            rectF.set(0, 0, width, height);
            // 对位置进行偏移线宽的一半，因为直接画线的话，有一半是画到画布外的，
            // 但是因为有圆角，圆角后面还有画布，导致角的线宽比边的线宽要宽
            // 矩形缩小边框的一半
            float newWidth = solidWidth / 2.0f;
            rectF.inset(newWidth, newWidth);
            resultPath.addCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.width(), rectF.height()) / 2f, Path.Direction.CW);
            result[0] = resultPath;
            return result;
        }

        if (type == TYPE_OVAL) {
            Path[] result = new Path[1];
            Path resultPath = new Path();
            // 对位置进行偏移线宽的一半，因为直接画线的话，有一半是画到画布外的，
            // 但是因为有圆角，圆角后面还有画布，导致角的线宽比边的线宽要宽
            // 矩形缩小边框的一半
            float newWidth = solidWidth / 2.0f;

            if (leftTopRadius + leftBottomRadius >= height) {
                // 左边半圆
                RectF leftRectF = new RectF();
                leftRectF.set(newWidth, newWidth, height, height - newWidth);
                resultPath.addArc(leftRectF, 90, 180);
                // 增加上边的线
                resultPath.lineTo(width - height / 2f, newWidth);
                // 右边半圆
                RectF rightRectF = new RectF();
                rightRectF.set(width - height, newWidth, width - newWidth, height - newWidth);
                resultPath.addArc(rightRectF, -90, 180);
                // 增加下边的线
                resultPath.lineTo(height / 2f, height - newWidth);
            } else {
                // 上边半圆
                RectF leftRectF = new RectF();
                leftRectF.set(newWidth, newWidth, width - newWidth, width);
                resultPath.addArc(leftRectF, 180, 180);
                // 增加右边的线
                resultPath.lineTo(width - newWidth, height - width / 2f);
                // 下边半圆
                RectF rightRectF = new RectF();
                rightRectF.set(newWidth, height - width, width - newWidth, height - newWidth);
                resultPath.addArc(rightRectF, 0, 180);
                // 增加左边的线
                resultPath.lineTo(newWidth, width / 2f);
            }

            result[0] = resultPath;
            return result;
        }

        Path[] result = new Path[2];
        Path solidPath = new Path();
        Path radiusPath = new Path();

        float leftTopRadiusLeft, leftTopRadiusTop; // 左上角
        float leftBottomRadiusLeft, leftBottomRadiusBottom; // 左下角
        float rightTopRadiusRight, rightTopRadiusTop; // 右上角
        float rightBottomRadiusRight, rightBottomRadiusBottom; // 右下角
        int[] sideTop = calculateRadiusLength(leftTopRadius, rightTopRadius, width); // 上同边
        int[] sideBottom = calculateRadiusLength(leftBottomRadius, rightBottomRadius, width); // 下同边
        int[] sideLeft = calculateRadiusLength(leftTopRadius, leftBottomRadius, height); // 左同边
        int[] sideRight = calculateRadiusLength(rightTopRadius, rightBottomRadius, height); // 右同边
        leftTopRadiusTop = sideTop[0];
        rightTopRadiusTop = sideTop[1];
        leftBottomRadiusBottom = sideBottom[0];
        rightBottomRadiusBottom = sideBottom[1];
        leftTopRadiusLeft = sideLeft[0];
        leftBottomRadiusLeft = sideLeft[1];
        rightTopRadiusRight = sideRight[0];
        rightBottomRadiusRight = sideRight[1];

        // 对位置进行偏移线宽的一半，因为直接画线的话，有一半是画到画布外的，
        // 但是因为有圆角，圆角后面还有画布，导致角的线宽比边的线宽要宽
        float newWidth = solidWidth / 2.0f;
        // 四条边路径
        solidPath.moveTo(leftTopRadiusTop, newWidth);
        solidPath.lineTo(width - rightTopRadiusTop, newWidth);

        solidPath.moveTo(width - newWidth, rightTopRadiusRight);
        solidPath.lineTo(width - newWidth, height - rightBottomRadiusRight);

        solidPath.moveTo(width - rightBottomRadiusBottom, height - newWidth);
        solidPath.lineTo(leftBottomRadiusBottom, height - newWidth);

        solidPath.moveTo(newWidth, height - leftBottomRadiusLeft);
        solidPath.lineTo(newWidth, leftTopRadiusLeft);

        // 四个角路径
        radiusPath.moveTo(newWidth, leftTopRadiusLeft);
        radiusPath.quadTo(newWidth, newWidth, leftTopRadiusTop, newWidth);

        radiusPath.moveTo(width - rightTopRadiusTop, newWidth);
        radiusPath.quadTo(width, newWidth, width - newWidth, rightTopRadiusRight);

        radiusPath.moveTo(width - newWidth, height - rightBottomRadiusRight);
        radiusPath.quadTo(width - newWidth, height - newWidth, width - rightBottomRadiusBottom, height - newWidth);

        radiusPath.moveTo(leftBottomRadiusBottom, height - newWidth);
        radiusPath.quadTo(newWidth, height - newWidth, newWidth, height - leftBottomRadiusLeft);

        result[0] = solidPath;
        result[1] = radiusPath;
        return result;
    }

    /**
     * 根据圆角大小和边框长度将圆角矩形作为什么图形处理
     *
     * @return TYPE_CIRCLE：圆形  TYPE_RADIUS：圆角矩形  TYPE_OVAL：两端作为椭圆
     */
    private static int getType(int leftTopRadius, int rightTopRadius,
                               int leftBottomRadius, int rightBottomRadius,
                               int width, int height) {
        boolean heightOval = (leftTopRadius + rightTopRadius >= width) && (leftBottomRadius + rightBottomRadius >= width);
        boolean widthOval = (leftTopRadius + leftBottomRadius >= height) && (rightTopRadius + rightBottomRadius >= height);

        if (widthOval && heightOval) {
            return TYPE_CIRCLE;
        }

        if (heightOval) {
            return TYPE_OVAL;
        }

        if (widthOval) {
            return TYPE_OVAL;
        }

        return TYPE_RADIUS;
    }

    /**
     * 根据同边的两个圆角的分别长度和边的长度，重新计算两个圆角该有的长度(防止两个圆角的同边长度之后大于总的长度)<br/>
     * 如：给出左上角上边的长度和右上角上边的长度，以及矩形的上边边长(矩形的长)，重新计算出左上角上边的长度和右上角上边的长度，
     * 防止左上角上边的长度和右上角上边的长度之和大于边长导致出错，如果大于边长时，根据比例计算。
     *
     * @param sameSide1     同边第一个值的原大小
     * @param sameSide2     同边第二个值的原大小
     * @param sameSideWidth 同边长度
     * @return int[]，长度为2，int[0]：同边第一个值的最终大小 int[1]：同边第二个值的最终大小
     */
    private static int[] calculateRadiusLength(int sameSide1, int sameSide2, int sameSideWidth) {
        int[] result = new int[2];
        if (sameSide1 > 0 && sameSide2 > 0) {
            int topRadiusWidth = sameSide1 + sameSide2;
            if (topRadiusWidth > sameSideWidth) {
                result[0] = (int) ((sameSide1 * 1.0 / topRadiusWidth) * sameSideWidth);
                result[1] = (int) ((sameSide2 * 1.0 / topRadiusWidth) * sameSideWidth);
            } else {
                result[0] = sameSide1;
                result[1] = sameSide2;
            }
        } else if (sameSide1 > 0) {
            result[0] = sameSide1 > sameSideWidth ? sameSideWidth : sameSide1;
            result[1] = 0;
        } else if (sameSide2 > 0) {
            result[0] = 0;
            result[1] = sameSide2 > sameSideWidth ? sameSideWidth : sameSide2;
        } else {
            result[0] = 0;
            result[1] = 0;
        }
        return result;
    }
}
