package com.renj.view.marqueeView;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.renj.view.R;


/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-04-17   16:33
 * <p>
 * 描述：文字类型的 MarqueeView
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class TextMarqueeAdapter extends MarqueeViewAdapter<String> {

    private int textSize = 12;
    private int textColor = Color.parseColor("#333333");

    public TextMarqueeAdapter() {
    }

    public TextMarqueeAdapter(int textSize, int textColor) {
        this.textSize = textSize;
        this.textColor = textColor;
    }

    @Override
    public View getView(int position, MarqueeView marqueeView, String itemData) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_view_maquee_text_item, null);
        TextView textView = view.findViewById(R.id.marquee_text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setText(itemData);
        return view;
    }
}
