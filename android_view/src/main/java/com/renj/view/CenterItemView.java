package com.renj.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
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

        setIconViewInfo();   // icon
        setNameViewInfo();   // name
        setValueViewInfo();  // value
        setArrowViewInfo();  // arrow
    }

    // ***********【start】 Icon信息设置部分 【start】********** //

    /**
     * 设置Icon控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyIconInfo()} 方法。</b>
     *
     * @param iconViewIsShow Icon控件是否显示
     */
    public CenterItemView modifyIconInfo(boolean iconViewIsShow) {
        this.iconViewIsShow = iconViewIsShow;
        return this;
    }

    /**
     * 设置Icon控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyIconInfo()} 方法。</b>
     *
     * @param iconWidth  Icon控件宽
     * @param iconHeight Icon控件高
     */
    public CenterItemView modifyIconInfo(int iconWidth, int iconHeight) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        return this;
    }

    /**
     * 设置Icon控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyIconInfo()} 方法。</b>
     *
     * @param iconResImage Icon控件图片
     */
    public CenterItemView modifyIconInfo(Drawable iconResImage) {
        this.iconResImage = iconResImage;
        return this;
    }

    /**
     * 设置Icon控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyIconInfo()} 方法。</b>
     *
     * @param iconResId Icon控件图片
     */
    public CenterItemView modifyIconInfo(@DrawableRes int iconResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.iconResImage = resources.getDrawable(iconResId, null);
        } else {
            this.iconResImage = resources.getDrawable(iconResId);
        }
        return this;
    }

    /**
     * 将修改的Icon控件信息应用
     */
    public void applyModifyIconInfo() {
        setIconViewInfo();
    }
    // ***********【end】 Icon信息设置部分 【end】********** //


    // ***********【start】 Name信息设置部分 【start】********** //

    /**
     * 设置Name控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyNameInfo()} 方法。</b>
     *
     * @param nameText 显示内容
     */
    public CenterItemView modifyNameText(String nameText) {
        this.nameText = nameText;
        return this;
    }

    /**
     * 设置Name控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyNameInfo()} 方法。</b>
     *
     * @param nameTextColor 文字颜色
     */
    public CenterItemView modifyNameTextColor(int nameTextColor) {
        this.nameTextColor = nameTextColor;
        return this;
    }

    /**
     * 设置Name控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyNameInfo()} 方法。</b>
     *
     * @param nameTextSize 文字大小 单位: sp
     */
    public CenterItemView modifyNameTextSize(int nameTextSize) {
        this.nameTextSize = sp2px(nameTextSize);
        return this;
    }

    /**
     * 设置Name控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyNameInfo()} 方法。</b>
     *
     * @param nameMarginStart 与icon之间的间距
     */
    public CenterItemView modifyNameTextMargin(int nameMarginStart) {
        this.nameMarginStart = nameMarginStart;
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyNameInfo()} 方法。</b>
     *
     * @param nameTextResId 显示内容
     */
    public CenterItemView modifyNameText(@StringRes int nameTextResId) {
        this.nameText = resources.getString(nameTextResId);
        return this;
    }

    /**
     * 将修改的Name控件信息应用
     */
    public void applyModifyNameInfo() {
        setNameViewInfo();
    }

    // ***********【end】 Name信息设置部分 【end】********** //


    // ***********【start】 Value信息设置部分 【start】********** //

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueViewIsShow 是否显示Value控件
     */
    public CenterItemView modifyValueShow(boolean valueViewIsShow) {
        this.valueViewIsShow = valueViewIsShow;
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueText 显示内容
     */
    public CenterItemView modifyValueText(String valueText) {
        this.valueText = valueText;
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueTextResId 显示内容
     */
    public CenterItemView modifyValueText(@StringRes int valueTextResId) {
        this.valueText = resources.getString(valueTextResId);
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueTextColor 文字颜色
     */
    public CenterItemView modifyValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueTextSize 文字大小 单位: sp
     */
    public CenterItemView modifyValueTextSize(int valueTextSize) {
        this.valueTextSize = sp2px(valueTextSize);
        return this;
    }

    /**
     * 设置Value控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyValueInfo()} 方法。</b>
     *
     * @param valueMarginStart 与icon之间的间距
     */
    public CenterItemView modifyValueTextMargin(int valueMarginStart) {
        this.valueMarginStart = valueMarginStart;
        return this;
    }

    /**
     * 将修改的Value控件信息应用
     */
    public void applyModifyValueInfo() {
        setValueViewInfo();
    }

    // ***********【end】 Value信息设置部分 【end】********** //


    // ***********【start】 箭头信息设置部分 【start】********** //

    /**
     * 设置箭头控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyArrowInfo()} 方法。</b>
     *
     * @param arrowViewIsShow 箭头控件是否显示
     */
    public CenterItemView modifyArrowInfo(boolean arrowViewIsShow) {
        this.arrowViewIsShow = arrowViewIsShow;
        return this;
    }

    /**
     * 设置箭头控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyArrowInfo()} 方法。</b>
     *
     * @param arrowWidth  箭头控件宽
     * @param arrowHeight 箭头控件高
     */
    public CenterItemView modifyArrowInfo(int arrowWidth, int arrowHeight) {
        this.arrowWidth = arrowWidth;
        this.arrowHeight = arrowHeight;
        return this;
    }

    /**
     * 设置箭头控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyArrowInfo()} 方法。</b>
     *
     * @param arrowResImage 箭头控件图片
     */
    public CenterItemView modifyArrowInfo(Drawable arrowResImage) {
        this.arrowResImage = arrowResImage;
        return this;
    }

    /**
     * 设置Icon控件信息。<b>注意：如果需要将修改的信息生效，请调用 {@link #applyModifyArrowInfo()} 方法。</b>
     *
     * @param arrowResId arrow控件图片
     */
    public CenterItemView modifyArrowInfo(@DrawableRes int arrowResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.arrowResImage = resources.getDrawable(arrowResId, null);
        } else {
            this.arrowResImage = resources.getDrawable(arrowResId);
        }
        return this;
    }

    /**
     * 将修改的箭头控件信息应用
     */
    public void applyModifyArrowInfo() {
        setArrowViewInfo();
    }

    // ***********【end】 箭头信息设置部分 【end】********** //

    /**
     * 设置Icon控件信息
     */
    private void setIconViewInfo() {
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
    }

    /**
     * 设置Name控件信息
     */
    private void setNameViewInfo() {
        tvName.setTextColor(nameTextColor);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, nameTextSize);
        tvName.setText(nameText);
        if (iconViewIsShow) {
            LayoutParams layoutParams = (LayoutParams) tvName.getLayoutParams();
            layoutParams.leftMargin = nameMarginStart;
            tvName.setLayoutParams(layoutParams);
        }
    }

    /**
     * 设置Value控件信息
     */
    private void setValueViewInfo() {
        if (valueViewIsShow) {
            tvValue.setVisibility(VISIBLE);
            tvValue.setTextColor(valueTextColor);
            tvValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
            tvValue.setText(valueText);
            LayoutParams layoutParams = (LayoutParams) tvValue.getLayoutParams();
            layoutParams.leftMargin = valueMarginStart;
            tvValue.setLayoutParams(layoutParams);
        } else {
            tvValue.setVisibility(GONE);
        }
    }

    /**
     * 设置箭头控件信息
     */
    private void setArrowViewInfo() {
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

    /**
     * 将修改的所有控件信息应用，调用该方法相当于同时调用了 {@link #applyModifyIconInfo()}、
     * {@link #applyModifyNameInfo()}、{@link #applyModifyValueInfo()}、
     * {@link #applyModifyArrowInfo()} 四个方法
     */
    public void applyModifyAllInfo() {
        applyModifyIconInfo();
        applyModifyNameInfo();
        applyModifyValueInfo();
        applyModifyArrowInfo();
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
