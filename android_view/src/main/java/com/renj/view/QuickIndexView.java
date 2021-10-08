package com.renj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2021-10-08   16:01
 * <p>
 * 描述：快速索引控件（主要用于列表）
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class QuickIndexView extends View {
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_TOUCH_SIZE = 16;
    private static final int DEFAULT_TEXT_TOUCH_COLOR = Color.GRAY;
    private static final String[] DEFAULT_TEXT_LIST = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z", "#"};

    private float viewWidth;
    private float cellHeight;
    private float cellMaxHeight;
    private Paint textPaint;

    // 文字大小和颜色
    private int textSize;
    private int textColor;
    // 触摸文字大小和颜色(当前点击位置文字大小和颜色)
    private int textTouchSize;
    private int textTouchColor;
    // 绘制的文案
    private final List<String> drawTextList = new ArrayList<>();

    public QuickIndexView(Context context) {
        this(context, null, 0);
    }

    public QuickIndexView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QuickIndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexView);
        textSize = typedArray.getInt(R.styleable.QuickIndexView_qiv_text_size, sp2px(DEFAULT_TEXT_SIZE));
        textColor = typedArray.getInt(R.styleable.QuickIndexView_qiv_text_color, DEFAULT_TEXT_COLOR);
        textTouchSize = typedArray.getInt(R.styleable.QuickIndexView_qiv_text_touch_size, sp2px(DEFAULT_TEXT_TOUCH_SIZE));
        textTouchColor = typedArray.getInt(R.styleable.QuickIndexView_qiv_text_touch_color, DEFAULT_TEXT_TOUCH_COLOR);

        cellMaxHeight = typedArray.getInt(R.styleable.QuickIndexView_qiv_text_cell_max_height, 0);
        typedArray.recycle();


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 默认文字大小和颜色
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        drawTextList.addAll(Arrays.asList(DEFAULT_TEXT_LIST));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制
        for (int i = 0; i < drawTextList.size(); i++) {
            if (i == touchIndex) {
                textPaint.setColor(textTouchColor);
                textPaint.setTextSize(textTouchSize);
            } else {
                textPaint.setColor(textColor);
                textPaint.setTextSize(textSize);
            }
            @SuppressLint("DrawAllocation") Rect rect = new Rect();
            textPaint.getTextBounds(drawTextList.get(i), 0, 1, rect);
            int width = (int) (viewWidth * 1.0 / 2 - rect.width() * 1.0 / 2);
            int height = (int) (cellHeight * 1.0 / 2 + rect.height() * 1.0 / 2 + i * cellHeight);

            canvas.drawText(drawTextList.get(i), width, height, textPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getMeasuredWidth();
        cellHeight = getMeasuredHeight() * 1f / drawTextList.size();

        if (cellMaxHeight > 0) {
            cellHeight = Math.min(cellHeight, cellMaxHeight);
        }
    }

    int touchIndex = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                for (int i = 0; i < drawTextList.size(); i++) {
                    if (y > i * cellHeight && y < (i + 1) * cellHeight) {
                        if (touchIndex != i) {
                            if (onIndexChangedListener != null) {
                                onIndexChangedListener.onIndexChanged(i, drawTextList.get(i));
                            }
                            touchIndex = i;
                            invalidate();
                        }
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    private OnIndexChangedListener onIndexChangedListener;

    public void setOnIndexChangedListener(OnIndexChangedListener listener) {
        this.onIndexChangedListener = listener;
    }

    public interface OnIndexChangedListener {
        void onIndexChanged(int position, String content);
    }

    private int sp2px(float spValue) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
