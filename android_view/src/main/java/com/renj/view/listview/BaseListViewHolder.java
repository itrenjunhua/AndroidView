package com.renj.view.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2019-07-19   13:40
 * <p>
 * 描述：ListView/GridView 控件 ViewHolder 基类 {@link BaseListAdapter}
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class BaseListViewHolder<T> {
    // 保存当前 item 的所有控件id，减少 findViewById 次数
    private SparseArray<View> itemViews = new SparseArray<>();
    protected View mItemRootView;

    public BaseListViewHolder(Context context, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(getItemViewLayoutId(), parent, false);
        bindView(itemView);
    }

    public BaseListViewHolder(View itemView) {
        bindView(itemView);
    }

    private void bindView(View itemView) {
        if (itemView != null) {
            mItemRootView = itemView;
            mItemRootView.setTag(this);
        }
    }

    public View getItemView() {
        return mItemRootView;
    }

    public TextView getTextView(@IdRes int vId) {
        return getView(vId);
    }

    public void setText(@IdRes int vId, @NonNull CharSequence content) {
        getTextView(vId).setText(content);
    }

    public void setText(@IdRes int vId, @StringRes int strId) {
        getTextView(vId).setText(strId);
    }

    public Button getButton(@IdRes int vId) {
        return getView(vId);
    }

    public void setEnabled(@IdRes int vId, boolean enable) {
        getButton(vId).setEnabled(enable);
    }

    public ImageView getImageView(@IdRes int vId) {
        return getView(vId);
    }

    public void setBitmap(@IdRes int vId, @NonNull Bitmap bitmap) {
        getImageView(vId).setImageBitmap(bitmap);
    }

    public void setBitmap(@IdRes int vId, @DrawableRes int resId) {
        getImageView(vId).setImageResource(resId);

    }

    public <T extends View> T getView(@IdRes int vId) {
        View view = itemViews.get(vId);
        if (view == null) {
            view = getItemView().findViewById(vId);
            itemViews.put(vId, view);
        }
        return (T) view;
    }

    /**
     * 如果使用 {@link #BaseListViewHolder(Context, ViewGroup)} 构造方法子类必须重写该方法，获取条目布局资源ID
     *
     * @return
     */
    public int getItemViewLayoutId() {
        return 0;
    }

    /**
     * 设置条目的数据
     *
     * @param position 当前的位置
     * @param data     当前位置的数据
     */
    public abstract void setData(int position, T data);
}
