package com.renj.view.recyclerview.binding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：renjunhua@anlovek.com
 * <p>
 * 创建时间：2019-08-06   11:22
 * <p>
 * 描述：
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class BindingRecyclerViewHolder<IDB extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected IDB viewDataBinding;

    public BindingRecyclerViewHolder(@NonNull IDB viewDataBinding) {
        super(viewDataBinding.getRoot());
        this.viewDataBinding = viewDataBinding;

        // 增加单击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemViewClickListener) mOnItemViewClickListener.onItemViewClick(v);
            }
        });

        // 增加长按事件
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mOnItemViewLongClickListener)
                    return mOnItemViewLongClickListener.onItemLongViewClick(v);
                return false;
            }
        });
    }

    public BindingRecyclerViewHolder(@NonNull Context context, @NonNull ViewGroup parent, @LayoutRes int layoutId) {
        this((IDB) DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false));
    }

    public View getItemView() {
        return itemView;
    }

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    /* ====================== item click listener event ======================= */
    private OnItemViewClickListener mOnItemViewClickListener;
    private OnItemViewLongClickListener mOnItemViewLongClickListener;

    /**
     * 设置单击监听
     *
     * @param onItemViewClickListener {@link OnItemViewClickListener} 对象
     */
    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.mOnItemViewClickListener = onItemViewClickListener;
    }

    /**
     * 设置长按监听
     *
     * @param onItemViewLongClickListener {@link OnItemViewLongClickListener} 对象
     */
    public void setOnItemViewLongClickListener(OnItemViewLongClickListener onItemViewLongClickListener) {
        this.mOnItemViewLongClickListener = onItemViewLongClickListener;
    }

    /**
     * item 单击监听接口
     */
    public interface OnItemViewClickListener {
        void onItemViewClick(View itemView);
    }

    /**
     * item 长按监听接口
     */
    public interface OnItemViewLongClickListener {
        boolean onItemLongViewClick(View itemView);
    }
}
