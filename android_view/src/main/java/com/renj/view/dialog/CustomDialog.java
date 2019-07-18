package com.renj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.renj.view.R;


/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-07-07   16:20
 * <p>
 * 描述：自定义对话框，可以设置是否显示标题、设置标题内容、主要信息、确认、取消等文字信息和监听，默认不显示标题
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CustomDialog extends Dialog implements View.OnClickListener {
    private Resources resources = getContext().getResources();
    private AnimationSet mModalInAnim;
    private View mDialogView;
    private TextView tvDialogContent, tvCancel, tvOk, tvDialogTitle;
    private CustomDialogListener customDialogListener;
    private String confirmText = resources.getString(R.string.module_view_confirm),
            cancelText = resources.getString(R.string.module_view_cancel),
            dialogContentText = "", title = "";
    private int confirmColor = resources.getColor(R.color.module_view_dialog_bt_text),
            cancelColor = resources.getColor(R.color.module_view_dialog_bt_text),
            dialogContentColor = resources.getColor(R.color.module_view_main_text),
            titleColor = resources.getColor(R.color.module_view_main_text);
    private int btTextSize = 16, dialogContentSize = 15, titleSize = 18;
    private boolean showTitle = false;

    // 点击按钮时是否需要关闭dialog ，默认关闭
    private boolean isDismiss = true;

    // 默认宽和高
    private int width = dip2px(300);
    private int height = WindowManager.LayoutParams.WRAP_CONTENT;

    /**
     * 创建对话框
     *
     * @param context 上下文
     * @return
     */
    public static CustomDialog newInstance(@NonNull Context context) {
        return new CustomDialog(context);
    }

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.view_alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_view_dialog_custom);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mModalInAnim = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.module_view_dialog_modal_in);

        tvDialogTitle = (TextView) findViewById(R.id.tv_title_dialog);
        tvDialogContent = (TextView) findViewById(R.id.tv_dialog_content);
        tvCancel = (TextView) findViewById(R.id.tv_dialog_cancel);
        tvOk = (TextView) findViewById(R.id.tv_dialog_ok);

        setTitleContent(title);
        isShowTitle(showTitle);
        setDialogContent(dialogContentText);
        setCancelText(cancelText);
        setConfirmText(confirmText);
        setButtonTextSize(btTextSize);
        setContentSize(dialogContentSize);
        setTitleSize(titleSize);
        setConfirmColor(confirmColor);
        setCancelColor(cancelColor);
        setContentColor(dialogContentColor);
        setTitleColor(titleColor);

        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    /**
     * 设置 {@link CustomDialog} 的宽和高，单位 px <br/>
     * 宽：默认 300dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param width  宽，默认 300dp
     * @param height 高，默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * @return
     */
    public CustomDialog setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置 {@link CustomDialog} 的宽，单位 px <br/>
     * 宽：默认 300dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param width 宽，默认 300dp
     * @return
     */
    public CustomDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 设置 {@link CustomDialog} 的高，单位 px <br/>
     * 宽：默认 300dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param height 高，默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * @return
     */
    public CustomDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 是否需要显示标题
     *
     * @param showTitle
     * @return
     */
    public CustomDialog isShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        if (tvDialogTitle != null) {
            if (this.showTitle) {
                tvDialogTitle.setVisibility(View.VISIBLE);
            } else {
                tvDialogTitle.setVisibility(View.GONE);
            }
        }
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title
     * @return
     */
    public CustomDialog setTitleContent(@NonNull String title) {
        this.title = title;
        if (tvDialogTitle != null) {
            tvDialogTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置对话框内容
     *
     * @param content
     * @return
     */
    public CustomDialog setDialogContent(@NonNull String content) {
        this.dialogContentText = content;
        if (tvDialogContent != null) {
            tvDialogContent.setText(content);
        }
        return this;
    }

    /**
     * 设置取消按钮内容
     *
     * @param cancelText
     * @return
     */
    public CustomDialog setCancelText(@NonNull String cancelText) {
        this.cancelText = cancelText;
        if (tvCancel != null) tvCancel.setText(cancelText);
        return this;
    }

    /**
     * 设置确定按钮内容
     *
     * @param confirmText
     * @return
     */
    public CustomDialog setConfirmText(@NonNull String confirmText) {
        this.confirmText = confirmText;
        if (tvOk != null) tvOk.setText(confirmText);
        return this;
    }

    /**
     * 设置按钮文字大小 单位：sp
     *
     * @param btTextSize 文字大小 单位：sp
     * @return
     */
    public CustomDialog setButtonTextSize(int btTextSize) {
        this.btTextSize = btTextSize;
        if (tvCancel != null) tvCancel.setTextSize(Dimension.SP, btTextSize);
        if (tvOk != null) tvOk.setTextSize(Dimension.SP, btTextSize);
        return this;
    }

    /**
     * 设置内容文字大小 单位：sp
     *
     * @param contentSize 文字大小 单位：sp
     * @return
     */
    public CustomDialog setContentSize(int contentSize) {
        this.dialogContentSize = contentSize;
        if (tvDialogContent != null) tvDialogContent.setTextSize(Dimension.SP, contentSize);
        return this;
    }

    /**
     * 设置标题文字大小 单位：sp
     *
     * @param titleSize 文字大小 单位：sp
     * @return
     */
    public CustomDialog setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        if (tvDialogTitle != null) tvDialogTitle.setTextSize(Dimension.SP, titleSize);
        return this;
    }

    /**
     * 设置确认按钮颜色
     *
     * @param confirmColor 颜色
     * @return
     */
    public CustomDialog setConfirmColor(int confirmColor) {
        this.confirmColor = confirmColor;
        if (tvOk != null) tvOk.setTextColor(confirmColor);
        return this;
    }

    /**
     * 设置取消按钮颜色
     *
     * @param cancelColor 颜色
     * @return
     */
    public CustomDialog setCancelColor(int cancelColor) {
        this.cancelColor = cancelColor;
        if (tvCancel != null) tvCancel.setTextColor(cancelColor);
        return this;
    }

    /**
     * 设置内容按钮颜色
     *
     * @param contentColor 颜色
     * @return
     */
    public CustomDialog setContentColor(int contentColor) {
        this.dialogContentColor = contentColor;
        if (tvDialogContent != null) tvDialogContent.setTextColor(contentColor);
        return this;
    }

    /**
     * 设置确认按钮颜色
     *
     * @param titleColor 颜色
     * @return
     */
    public CustomDialog setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        if (tvDialogTitle != null) tvDialogTitle.setTextColor(titleColor);
        return this;
    }

    /**
     * 设置触摸Dialog之外是否消失Dialog
     *
     * @param cancel
     * @return
     */
    public CustomDialog setCanceledOnTouchOut(boolean cancel) {
        this.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置监听
     *
     * @param customDialogListener
     * @return
     */
    public CustomDialog setCustomDialogListener(CustomDialogListener customDialogListener) {
        this.customDialogListener = customDialogListener;
        return this;
    }

    /**
     * 设置监听并指定点击按钮时是否需要关闭dialog ，默认关闭
     *
     * @param isDismiss            点击按钮时是否需要关闭dialog ，默认关闭
     * @param customDialogListener
     * @return
     */
    public CustomDialog setCustomDialogListener(boolean isDismiss, CustomDialogListener customDialogListener) {
        this.isDismiss = isDismiss;
        this.customDialogListener = customDialogListener;
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 指定宽高
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = width;
        lp.height = height;
        this.getWindow().setAttributes(lp);

        mDialogView.startAnimation(mModalInAnim);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (R.id.tv_dialog_cancel == vId) {
            if (isDismiss) this.dismiss();
            if (customDialogListener != null) {
                customDialogListener.onCancel(this);
            }
        } else if (R.id.tv_dialog_ok == vId) {
            if (isDismiss) this.dismiss();
            if (customDialogListener != null) {
                customDialogListener.onConfirm(this);
            }
        }
    }

    public int dip2px(float dipValue) {
        float scale = resources.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 按钮监听
     */
    public interface CustomDialogListener {
        /**
         * 点击确定按钮
         *
         * @param dialog
         */
        void onConfirm(CustomDialog dialog);

        /**
         * 点击取消按钮
         *
         * @param dialog
         */
        void onCancel(CustomDialog dialog);
    }
}
