package com.renj.view.recyclerview.binding;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-08-06   11:23
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public interface IBindingRecyclerCell<T, IDB extends ViewDataBinding> {
    void onAttachedToWindow(@NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding);

    /**
     * 返回 item 类型，子类必须实现。<b>并且添加到同一个 {@link BindingRecyclerAdapter} 的不同
     * {@link IBindingRecyclerCell} 的 {@link #getRecyclerItemType()} 返回值必须不同，否则发生 {@link ClassCastException}</b><br/>
     * <b>作用：用于在 {@link BindingRecyclerAdapter} 中区分不同的 item 类型。</b>
     *
     * @return item 类型值，用于区分不同的 item 类型
     */
    int getRecyclerItemType();

    T getItemData();

    /**
     * 创建 ViewHolder
     *
     * @param viewType 正常情况下该参数的值和 {@link #getRecyclerItemType()} 方法的返回值相等
     */
    @NonNull
    BindingRecyclerViewHolder onCreateViewHolder(@NonNull Context context, @NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull ViewGroup parent, int viewType);

    /**
     * 绑定 holder，{@link BindingRecyclerCell#itemData} 为 item 数据
     */
    void onBindViewHolder(@NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding, int position, T itemData);

    void onDetachedFromWindow(@NonNull BindingRecyclerAdapter recyclerAdapter, @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding);

    void onItemClick(@NonNull Context context, @NonNull BindingRecyclerAdapter recyclerAdapter,
                     @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding,
                     @NonNull View itemView, int position, T itemData);

    boolean onItemLongClick(@NonNull Context context, @NonNull BindingRecyclerAdapter recyclerAdapter,
                            @NonNull BindingRecyclerViewHolder holder, IDB viewDataBinding,
                            @NonNull View itemView, int position, T itemData);
}
