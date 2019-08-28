package com.renj.view.recyclerview.binding;

import android.util.ArrayMap;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-08-28   23:16
 * <p>
 * 描述：当需要在Adapter中保存一些数据时使用该Adapter，否则使用{@link BindingRecyclerAdapter};<br/>
 * 该类提供一个{@link android.util.ArrayMap} 类型的成员变量{@code adapterData}用于保存数据.
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class BindingDataRecyclerAdapter<T extends IBindingRecyclerCell> extends BindingRecyclerAdapter<T> {
    public ArrayMap<Object, Object> adapterData = new ArrayMap<>();

    public ArrayMap<Object, Object> getAdapterData() {
        return adapterData;
    }

    public void setAdapterData(ArrayMap<Object, Object> adapterData) {
        this.adapterData = adapterData;
    }
}
