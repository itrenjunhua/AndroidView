package com.renj.view.recyclerview.binding;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-08-06   11:24
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class BindingRecyclerCell<T, IDB extends ViewDataBinding> implements IBindingRecyclerCell<T, IDB> {
    /**
     * item 数据
     */
    protected T itemData;

    public BindingRecyclerCell(T itemData) {
        this.itemData = itemData;
    }

    @Override
    public T getItemData() {
        return itemData;
    }

    @Override
    public void onAttachedToWindow(@NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding) {

    }

    /**
     * 如有必要，释放资源
     *
     * @param holder
     */
    @Override
    public void onDetachedFromWindow(@NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding) {

    }

    @Override
    public void onItemClick(@NonNull Context context, @NonNull BindingRecyclerAdapter recyclerAdapter,
                            @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding,
                            @NonNull View itemView, int position, T itemData) {

    }

    @Override
    public boolean onItemLongClick(@NonNull Context context, @NonNull BindingRecyclerAdapter recyclerAdapter,
                                   @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding,
                                   @NonNull View itemView, int position, T itemData) {
        return false;
    }
}
