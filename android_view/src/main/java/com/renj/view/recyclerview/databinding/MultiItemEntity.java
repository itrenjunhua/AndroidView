package com.renj.view.recyclerview.databinding;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-12-28   17:23
 * <p>
 * 描述：多种条目类型时数据可以实现该类，或者直接使用 {@link SimpleMultiItemEntity}
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface MultiItemEntity {
    /**
     * 返回当前数据的条目类型值，用于区分列表中的不同数据
     *
     * @return 当前数据的条目类型值，用于区分列表中的不同数据
     */
    int getItemType();
}
