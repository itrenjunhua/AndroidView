package com.renj.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2019-10-31   14:20
 * <p>
 * 描述：类似个人中心条目(左侧图标+文字name+文字value+右侧箭头样式)控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CenterItemView extends LinearLayout {
    private Resources resources = getContext().getResources();

    private final int DEFAULT_PADDING = dip2px(12);    // 两边距离
    private final int DEFAULT_TEXT_SIZE = sp2px(16);    // 文字大小 sp
    private final int DEFAULT_ICON_SIZE = dip2px(24);  // icon图标大小
    private final int DEFAULT_ARROW_SIZE = dip2px(16); // 箭头图片大小
    private final int DEFAULT_VIEW_MARGIN = dip2px(6); // 控件之间的margin值
    private final int DEFAULT_MAIN_TEXT_COLOR = Color.parseColor("#FF333333");  // 主要文字颜色
    private final int DEFAULT_VALUE_TEXT_COLOR = Color.parseColor("#FF999999"); // 值要文字颜色

    // 控件
    private ImageView ivIcon, ivArrow;
    private TextView tvName, tvValue;

    // 各种数据值,控制控件的位置和样式
    private int viewPaddingStart;
    private int viewPaddingEnd;

    private boolean iconViewIsShow;
    private int iconWidth;
    private int iconHeight;
    private Drawable iconResImage;

    private float nameTextSize;
    private int nameTextColor;
    private String nameText = "";
    private int nameMarginStart;

    private boolean valueViewIsShow;
    private float valueTextSize;
    private int valueTextColor;
    private String valueText = "";
    private int valueMarginStart;

    private boolean arrowViewIsShow;
    private int arrowWidth;
    private int arrowHeight;
    private Drawable arrowResImage;
    private int arrowMarginStart;

    public CenterItemView(Context context) {
        this(context, null);
    }

    public CenterItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CenterItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        // 获取 attrs 属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CenterItemView);
        viewPaddingStart = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_viewPaddingStart, DEFAULT_PADDING);
        viewPaddingEnd = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_viewPaddingEnd, DEFAULT_PADDING);

        iconViewIsShow = typedArray.getBoolean(R.styleable.CenterItemView_centerItemView_iconViewIsShow, false);
        iconWidth = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_iconWidth, DEFAULT_ICON_SIZE);
        iconHeight = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_iconHeight, DEFAULT_ICON_SIZE);
        iconResImage = typedArray.getDrawable(R.styleable.CenterItemView_centerItemView_iconResImage);

        nameTextSize = typedArray.getDimension(R.styleable.CenterItemView_centerItemView_nameTextSize, DEFAULT_TEXT_SIZE);
        nameTextColor = typedArray.getColor(R.styleable.CenterItemView_centerItemView_nameTextColor, DEFAULT_MAIN_TEXT_COLOR);
        nameText = typedArray.getString(R.styleable.CenterItemView_centerItemView_nameText);
        nameMarginStart = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_nameMarginStart, DEFAULT_VIEW_MARGIN);

        valueViewIsShow = typedArray.getBoolean(R.styleable.CenterItemView_centerItemView_valueViewIsShow, false);
        valueTextSize = typedArray.getDimension(R.styleable.CenterItemView_centerItemView_valueTextSize, DEFAULT_TEXT_SIZE);
        valueTextColor = typedArray.getColor(R.styleable.CenterItemView_centerItemView_valueTextColor, DEFAULT_VALUE_TEXT_COLOR);
        valueText = typedArray.getString(R.styleable.CenterItemView_centerItemView_valueText);
        valueMarginStart = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_valueMarginStart, DEFAULT_VIEW_MARGIN);

        arrowViewIsShow = typedArray.getBoolean(R.styleable.CenterItemView_centerItemView_arrowViewIsShow, true);
        arrowWidth = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_arrowWidth, DEFAULT_ARROW_SIZE);
        arrowHeight = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_arrowHeight, DEFAULT_ARROW_SIZE);
        arrowResImage = typedArray.getDrawable(R.styleable.CenterItemView_centerItemView_arrowResImage);
        arrowMarginStart = (int) typedArray.getDimension(R.styleable.CenterItemView_centerItemView_arrowMarginStart, DEFAULT_VIEW_MARGIN);

        typedArray.recycle();
    }

    private void initView(Context context) {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);

        // 布局中使用 <merge> 标签之后,布局内容自动填充到当前控件中
        // 调用 addView() 方法抛出 android.view.ViewGroup.resetResolvedLayoutDirection(ViewGroup.java:7372) 异常
        LayoutInflater.from(context).inflate(R.layout.module_view_center_item_view, this, true);
        ivIcon = findViewById(R.id.iv_center_icon);
        ivArrow = findViewById(R.id.iv_center_arrow);
        tvName = findViewById(R.id.tv_center_name);
        tvValue = findViewById(R.id.tv_center_value);

        // 根据 attrs 对控件的样式和内容进行设置
        this.setPadding(viewPaddingStart, 0, viewPaddingEnd, 0);

        // icon
        if (iconViewIsShow) {
            ivIcon.setVisibility(VISIBLE);
            LayoutParams layoutParams = new LayoutParams(iconWidth, iconHeight);
            ivIcon.setLayoutParams(layoutParams);
            if (iconResImage != null) {
                ivIcon.setBackground(iconResImage);
            }
        } else {
            ivIcon.setVisibility(GONE);
        }

        // name
        tvName.setTextColor(nameTextColor);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameTextSize);
        tvName.setText(nameText);
        if (iconViewIsShow) {
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tvName.getLayoutParams();
            layoutParams.leftMargin = nameMarginStart;
            tvName.setLayoutParams(layoutParams);
        }

        // value
        if (valueViewIsShow) {
            tvValue.setVisibility(VISIBLE);
            tvValue.setTextColor(valueTextColor);
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
            tvValue.setText(valueText);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) tvValue.getLayoutParams();
            layoutParams.leftMargin = valueMarginStart;
            tvValue.setLayoutParams(layoutParams);
        } else {
            tvValue.setVisibility(GONE);
        }

        // arrow
        if (arrowViewIsShow) {
            ivArrow.setVisibility(VISIBLE);
            LayoutParams layoutParams = new LayoutParams(arrowWidth, arrowHeight);
            layoutParams.leftMargin = arrowMarginStart;
            ivArrow.setLayoutParams(layoutParams);
            if (arrowResImage != null) {
                ivArrow.setBackground(arrowResImage);
            }
        } else {
            ivArrow.setVisibility(GONE);
        }

    }

    private int dip2px(float dipValue) {
        float scale = resources.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float scale = resources.getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
