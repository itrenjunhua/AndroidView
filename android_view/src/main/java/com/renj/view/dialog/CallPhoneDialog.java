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
 * 创建时间：2017-07-07   15:39
 * <p>
 * 描述：拨打电话的Dialog
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class CallPhoneDialog extends Dialog implements View.OnClickListener {
    private Resources resources = getContext().getResources();
    private AnimationSet mModalInAnim;
    private View mDialogView;
    private TextView tvPhone, tvCancel, tvOk;
    private CallPhoneListener callPhoneListener;
    private String phoneNumber = "",
            cancelText = resources.getString(R.string.module_view_cancel),
            okText = resources.getString(R.string.module_view_call);
    private int phoneColor = resources.getColor(R.color.module_view_main_text),
            cancelColor = resources.getColor(R.color.module_view_dialog_bt_text),
            okColor = resources.getColor(R.color.module_view_dialog_bt_text);
    private int btSize = 16, phoneSize = 18;

    // 点击按钮时是否需要关闭 Dialog，默认关闭
    private boolean isDismiss = true;

    // 默认宽和高
    private int width = dip2px(280);
    private int height = WindowManager.LayoutParams.WRAP_CONTENT;

    /**
     * 创建对话框
     *
     * @param context 上下文
     * @return
     */
    public static CallPhoneDialog newInstance(@NonNull Context context) {
        return new CallPhoneDialog(context);
    }

    public CallPhoneDialog(@NonNull Context context) {
        super(context, R.style.view_alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_view_dialog_call_phone);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mModalInAnim = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.module_view_dialog_modal_in);

        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvOk = (TextView) findViewById(R.id.tv_ok);

        setPhoneNumber(phoneNumber);
        setPhoneColor(phoneColor);
        setCancelText(cancelText);
        setCancelColor(cancelColor);
        setConfirmText(okText);
        setConfirmColor(okColor);
        setBtSize(btSize);
        setPhoneSize(phoneSize);

        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }


    /**
     * 设置 {@link CustomDialog} 的宽和高，单位 px <br/>
     * 宽：默认 280dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param width  宽，默认 280dp
     * @param height 高，默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * @return
     */
    public CallPhoneDialog setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 设置 {@link CustomDialog} 的宽，单位 px <br/>
     * 宽：默认 280dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param width 宽，默认 280dp
     * @return
     */
    public CallPhoneDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 设置 {@link CustomDialog} 的高，单位 px <br/>
     * 宽：默认 280dp；<br/>
     * 高：默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     *
     * @param height 高，默认 {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * @return
     */
    public CallPhoneDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 设置监听
     *
     * @param callPhoneListener
     * @return
     */
    public CallPhoneDialog setCallPhoneListener(CallPhoneListener callPhoneListener) {
        this.callPhoneListener = callPhoneListener;
        return this;
    }

    /**
     * 设置监听并指定点击按钮时是否需要关闭dialog ，默认关闭
     *
     * @param isDismiss         点击按钮时是否需要关闭dialog ，默认关闭
     * @param callPhoneListener
     * @return
     */
    public CallPhoneDialog setCallPhoneListener(boolean isDismiss, CallPhoneListener callPhoneListener) {
        this.isDismiss = isDismiss;
        this.callPhoneListener = callPhoneListener;
        return this;
    }

    /**
     * 设置电话号码
     *
     * @param phoneNumber
     * @return
     */
    public CallPhoneDialog setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
        if (null != tvPhone)
            tvPhone.setText(phoneNumber);
        return this;
    }

    /**
     * 设置取消按钮文字
     *
     * @param cancelText
     * @return
     */
    public CallPhoneDialog setCancelText(@NonNull String cancelText) {
        this.cancelText = cancelText;
        if (null != tvCancel)
            tvCancel.setText(cancelText);
        return this;
    }

    /**
     * 设置确定按钮文字
     *
     * @param okText
     * @return
     */
    public CallPhoneDialog setConfirmText(@NonNull String okText) {
        this.okText = okText;
        if (null != tvOk)
            tvOk.setText(okText);
        return this;
    }

    /**
     * 设置按钮文字大小；单位 sp
     *
     * @param btSize
     * @return
     */
    public CallPhoneDialog setBtSize(int btSize) {
        this.btSize = btSize;
        if (null != tvOk)
            tvOk.setTextSize(Dimension.SP, btSize);
        if (null != tvCancel)
            tvCancel.setTextSize(Dimension.SP, btSize);
        return this;
    }

    /**
     * 设置电话号码文字大小；单位 sp
     *
     * @param phoneSize
     * @return
     */
    public CallPhoneDialog setPhoneSize(int phoneSize) {
        this.phoneSize = phoneSize;
        if (null != tvPhone)
            tvPhone.setTextSize(Dimension.SP, phoneSize);
        return this;
    }

    /**
     * 设置电话号码文字颜色
     *
     * @param phoneColor
     * @return
     */
    public CallPhoneDialog setPhoneColor(int phoneColor) {
        this.phoneColor = phoneColor;
        if (null != tvPhone && 0 != phoneColor)
            tvPhone.setTextColor(phoneColor);
        return this;
    }

    /**
     * 设置取消文字颜色
     *
     * @param cancelColor
     * @return
     */
    public CallPhoneDialog setCancelColor(int cancelColor) {
        this.cancelColor = cancelColor;
        if (null != tvCancel && 0 != cancelColor)
            tvCancel.setTextColor(cancelColor);
        return this;
    }

    /**
     * 设置确定文字颜色
     *
     * @param okColor
     * @return
     */
    public CallPhoneDialog setConfirmColor(int okColor) {
        this.okColor = okColor;
        if (null != tvOk && 0 != okColor)
            tvOk.setTextColor(okColor);
        return this;
    }

    /**
     * 设置触摸Dialog之外是否消失Dialog
     *
     * @param cancel
     * @return
     */
    public CallPhoneDialog setCanceledOnTouchOut(boolean cancel) {
        this.setCanceledOnTouchOutside(cancel);
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
        if (R.id.tv_cancel == vId) {
            if (isDismiss) this.dismiss();
            if (null != callPhoneListener)
                callPhoneListener.onCancel(this);
        } else if (R.id.tv_ok == vId) {
            if (isDismiss) this.dismiss();
            if (null != callPhoneListener)
                callPhoneListener.onConfirm(this);
        }
    }

    public int dip2px(float dipValue) {
        float scale = resources.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public interface CallPhoneListener {
        /**
         * 确定按钮
         *
         * @param dialog
         */
        void onConfirm(CallPhoneDialog dialog);

        /**
         * 取消按钮
         *
         * @param dialog
         */
        void onCancel(CallPhoneDialog dialog);
    }
}
