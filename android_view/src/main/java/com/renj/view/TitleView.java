package com.renj.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2020-06-18   0:13
 * <p>
 * 描述：公共标题控件
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class TitleView extends RelativeLayout {
    private Resources resources = getContext().getResources();
    // 控件
    private ImageView ivTitleBack;
    private TextView tvTitleContent;
    private ImageView ivRightImg;
    private TextView tvRightText;
    private ViewStub vsRightCustom;
    private View viewLine;
    private View customRightView;

    // 控件属性
    private String titleContent, rightText;
    private Drawable backIcon, rightIcon;
    private boolean backViewShow, rightTextShow, rightImageShow, rightCustomShow, bottomLineShow;
    private float titleTextSize, rightTextSize;
    private int titleTextColor, rightTextColor;
    private int rightCustomLayoutId;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CenterItemView);
        backIcon = typedArray.getDrawable(R.styleable.TitleView_title_view_back_icon);
        backViewShow = typedArray.getBoolean(R.styleable.TitleView_title_view_back_show, true);
        if (backIcon == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backIcon = resources.getDrawable(R.mipmap.module_view_back, null);
            } else {
                backIcon = resources.getDrawable(R.mipmap.module_view_back);
            }
        }

        titleContent = typedArray.getString(R.styleable.TitleView_title_view_title);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_title_view_title_size, resources.getDimensionPixelSize(R.dimen.module_view_title_size));
        titleTextColor = typedArray.getColor(R.styleable.TitleView_title_view_title_color, resources.getColor(R.color.module_view_main_text));

        rightText = typedArray.getString(R.styleable.TitleView_title_view_right_text);
        rightTextSize = typedArray.getDimensionPixelSize(R.styleable.TitleView_title_view_right_text_size, resources.getDimensionPixelSize(R.dimen.module_view_text_size));
        rightTextColor = typedArray.getColor(R.styleable.TitleView_title_view_right_text_color, resources.getColor(R.color.module_view_gray_text));
        rightTextShow = typedArray.getBoolean(R.styleable.TitleView_title_view_right_text_show, false);

        rightIcon = typedArray.getDrawable(R.styleable.TitleView_title_view_right_img_icon);
        rightImageShow = typedArray.getBoolean(R.styleable.TitleView_title_view_right_img_show, false);

        rightCustomLayoutId = typedArray.getResourceId(R.styleable.TitleView_title_view_right_custom_layout, 0);
        rightCustomShow = typedArray.getBoolean(R.styleable.TitleView_title_view_right_custom_layout_show, false);

        bottomLineShow = typedArray.getBoolean(R.styleable.TitleView_title_view_bottom_line_show, true);
        typedArray.recycle();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.module_view_title_view, this, true);

        ivTitleBack = findViewById(R.id.iv_title_back);
        tvTitleContent = findViewById(R.id.tv_title_content);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvRightText = findViewById(R.id.tv_right_text);
        vsRightCustom = findViewById(R.id.vs_right_custom);
        viewLine = findViewById(R.id.v_title_line);

        ivTitleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBackViewClickListener != null)
                    onBackViewClickListener.onBackClick();
            }
        });

        ivRightImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightImageClickListener != null)
                    onRightImageClickListener.onImageClick();
            }
        });

        tvRightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightTextClickListener != null)
                    onRightTextClickListener.onTextClick();
            }
        });

        // 设置默认数据
        setBackViewShow(backViewShow);
        setBackViewIcon(backIcon);

        setTitleSize(titleTextSize);
        setTitleColor(titleTextColor);
        setTitleContent(titleContent);

        setRightTextShow(rightTextShow);
        setRightTextSize(rightTextSize);
        setRightTextColor(rightTextColor);
        setRightTextContent(rightText);

        setRightImgShow(rightImageShow);
        setRightImageIcon(rightIcon);

        setCustomRightViewShow(rightCustomShow);
        setCustomRightView(rightCustomLayoutId);

        setBottomLineShow(bottomLineShow);
    }


    /*************** 设置控件样式 ***************/
    public ImageView getBackView() {
        return ivTitleBack;
    }

    public TitleView setBackViewIcon(@DrawableRes int drawableId) {
        ivTitleBack.setImageResource(drawableId);
        return this;
    }

    public TitleView setBackViewIcon(Drawable drawable) {
        if (drawable != null)
            ivTitleBack.setImageDrawable(drawable);
        return this;
    }

    public TitleView setBackViewShow(boolean backViewShow) {
        ivTitleBack.setVisibility(backViewShow ? VISIBLE : GONE);
        return this;
    }

    public TextView getTitleContentView() {
        return tvTitleContent;
    }

    public TitleView setTitleContent(String titleContent) {
        if (titleContent == null) titleContent = "";
        tvTitleContent.setText(titleContent);
        return this;
    }

    public TitleView setTitleSize(float titleSize) {
        tvTitleContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(titleSize));
        return this;
    }

    public TitleView setTitleColor(int titleColorValue) {
        tvTitleContent.setTextColor(titleColorValue);
        return this;
    }

    public TitleView setRightTextShow(boolean showRightText) {
        tvRightText.setVisibility(showRightText ? VISIBLE : GONE);
        return this;
    }

    public TitleView setRightTextContent(String rightTextContent) {
        if (rightTextContent == null) rightTextContent = "";
        tvRightText.setText(rightTextContent);
        return this;
    }

    public TitleView setRightTextSize(float rightTextSize) {
        tvRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(rightTextSize));
        return this;
    }

    public TitleView setRightTextColor(int rightTextColorValue) {
        tvRightText.setTextColor(rightTextColorValue);
        return this;
    }

    public TitleView setRightImgShow(boolean showRightImg) {
        ivRightImg.setVisibility(showRightImg ? VISIBLE : GONE);
        return this;
    }

    public TitleView setRightImageIcon(@DrawableRes int drawableId) {
        ivRightImg.setImageResource(drawableId);
        return this;
    }

    public TitleView setRightImageIcon(Drawable drawable) {
        if (drawable != null)
            ivRightImg.setImageDrawable(drawable);
        return this;
    }

    public TitleView setCustomRightViewShow(boolean showCustomRightView) {
        vsRightCustom.setVisibility(showCustomRightView ? VISIBLE : GONE);
        return this;
    }

    public TitleView setCustomRightView(@LayoutRes int layoutId) {
        vsRightCustom.setLayoutResource(layoutId);
        customRightView = vsRightCustom.inflate();
        if (onRightCustomViewInflateListener != null) {
            onRightCustomViewInflateListener.onCustomViewFinish(customRightView);
        }
        return this;
    }

    public View getCustomRightView() {
        return customRightView;
    }

    public TitleView setBottomLineShow(boolean showBottomLine) {
        viewLine.setVisibility(showBottomLine ? VISIBLE : GONE);
        return this;
    }

    public View getViewLine() {
        return viewLine;
    }


    /*************** 各种监听 ***************/
    private OnBackViewClickListener onBackViewClickListener;
    private OnRightTextClickListener onRightTextClickListener;
    private OnRightImageClickListener onRightImageClickListener;
    private OnRightCustomViewInflateListener onRightCustomViewInflateListener;

    /**
     * 标题返回按钮点击监听
     */
    public TitleView setOnBackViewClickListener(OnBackViewClickListener onBackViewClickListener) {
        this.onBackViewClickListener = onBackViewClickListener;
        return this;
    }

    /**
     * 标题右边文字点击监听
     */
    public TitleView setOnRightTextClickListener(OnRightTextClickListener onRightTextClickListener) {
        this.onRightTextClickListener = onRightTextClickListener;
        return this;
    }

    /**
     * 标题右边图片点击监听
     */
    public TitleView setOnRightImageClickListener(OnRightImageClickListener onRightImageClickListener) {
        this.onRightImageClickListener = onRightImageClickListener;
        return this;
    }

    /**
     * 自定义右边布局完成监听
     */
    public TitleView setOnRightCustomViewInflateListener(OnRightCustomViewInflateListener onRightCustomViewInflateListener) {
        this.onRightCustomViewInflateListener = onRightCustomViewInflateListener;
        return this;
    }

    public interface OnBackViewClickListener {
        void onBackClick();
    }

    public interface OnRightTextClickListener {
        void onTextClick();
    }

    public interface OnRightImageClickListener {
        void onImageClick();
    }

    public interface OnRightCustomViewInflateListener {
        void onCustomViewFinish(View customView);
    }

    private int sp2px(float spValue) {
        float scale = resources.getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }
}
