package com.renj.view.marqueeView;

import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-04-21   0:18
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
class ListUtils {
    static <T> boolean isEmpty(List<T> dataList) {
        return dataList == null || dataList.isEmpty();
    }

    static <T> boolean notEmpty(List<T> dataList) {
        return !isEmpty(dataList);
    }
}
