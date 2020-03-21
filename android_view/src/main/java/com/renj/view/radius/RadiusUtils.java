package com.renj.view.radius;

import android.graphics.Path;

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
    /**
     * 根据四个圆角大小和矩形边长计算出真实的4四个圆角的8条边长后的Path
     *
     * @param leftTopRadius     左上角圆角大小
     * @param rightTopRadius    右上角圆角大小
     * @param leftBottomRadius  左下角圆角大小
     * @param rightBottomRadius 右下角圆角大小
     * @param width             矩形宽
     * @param height            矩形高
     * @return 结果Path
     */
    public static Path calculateRadiusPath(int leftTopRadius, int rightTopRadius,
                                           int leftBottomRadius, int rightBottomRadius,
                                           int width, int height) {

        int leftTopRadiusLeft, leftTopRadiusTop; // 左上角
        int leftBottomRadiusLeft, leftBottomRadiusBottom; // 左下角
        int rightTopRadiusRight, rightTopRadiusTop; // 右上角
        int rightBottomRadiusRight, rightBottomRadiusBottom; // 右下角
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
        //四个角：右上，右下，左下，左上
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
