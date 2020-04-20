package com.renj.view.marqueeView;

import android.content.Context;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-04-17   15:36
 * <p>
 * 描述：MarqueeView 适配器
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public abstract class MarqueeViewAdapter<T> {
    private List<T> itemDataList = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private MarqueeView marqueeView;
    protected Context mContext;

    void setContext(Context context) {
        this.mContext = context;
    }

    void setMarqueeView(MarqueeView marqueeView) {
        this.marqueeView = marqueeView;
        if (ListUtils.notEmpty(this.itemDataList)) {
            List<T> temp = new ArrayList<>();
            for (T t : this.itemDataList) {
                temp.add(t);
            }
            setItemDataList(temp);
        } else {
            setItemDataList(new ArrayList<T>());
        }
    }

    View getView(final int position) {
        if ((viewList.size() == itemDataList.size()) && (position >= 0 && position < viewList.size())) {
            return viewList.get(position);
        } else {
            T itemData = itemDataList.get(position);
            final View view = getView(position, marqueeView, itemData);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, MarqueeViewAdapter.this, view);
                    }
                }
            });
            viewList.add(view);
            return view;
        }
    }

    public void setItemDataList(List<T> dataList) {
        this.viewList.clear();
        this.itemDataList.clear();
        if (!ListUtils.isEmpty(dataList)) {
            this.itemDataList.addAll(dataList);
            if (marqueeView != null) {
                marqueeView.updateTotalDataSize(true, this.itemDataList.size());
            }
        }
    }

    public void addItemDataList(List<T> dataList) {
        if (!ListUtils.isEmpty(dataList)) {
            this.itemDataList.addAll(dataList);
            if (marqueeView != null) {
                marqueeView.updateTotalDataSize(false, this.itemDataList.size());
            }
        }
    }

    public List<T> getDataList() {
        return itemDataList;
    }

    public T getDataByPosition(int position) {
        if (position >= 0 && position < itemDataList.size()) {
            return itemDataList.get(position);
        }
        return null;
    }

    public abstract View getView(int position, MarqueeView marqueeView, T itemData);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, MarqueeViewAdapter marqueeViewAdapter, View view);
    }
}
