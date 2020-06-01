package com.renj.view.autolayout;

import android.support.annotation.IntDef;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-06-01   23:33
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IAutoLayout {
    int AUTO_TYPE_WIDTH = 0;
    int AUTO_TYPE_HEIGHT = 1;

    @IntDef(value = {AUTO_TYPE_WIDTH, AUTO_TYPE_HEIGHT})
    @interface AutoType {
    }

    /**
     * 设置控件宽高信息，并重新布局
     *
     * @param autoWidth
     * @param autoHeight
     */
    void setAutoViewInfo(int autoWidth, int autoHeight);

    /**
     * 设置控件宽高信息，并重新布局
     *
     * @param autoWidth
     * @param autoHeight
     */
    void setAutoViewInfo(@AutoType int autoType, int autoWidth, int autoHeight);
}
