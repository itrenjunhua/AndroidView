package com.renj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2019-01-29   18:47
 * <p>
 * 描述：带清除按钮的编辑框
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class ClearAbleEditText extends AppCompatEditText {
    private static final int DRAWABLE_LEFT = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DelIconShowTime.ALWAYS_SHOW,
            DelIconShowTime.ALWAYS_HIND,
            DelIconShowTime.HAS_CONTENT_SHOW,
            DelIconShowTime.HAS_CONTENT_FOCUS_SHOW,
    })
    public @interface DelIconShowTime {
        /**
         * 总是显示右边删除图片(一直显示)
         */
        int ALWAYS_SHOW = 0x0000;
        /**
         * 总是隐藏右边图片(不显示)
         */
        int ALWAYS_HIND = 0x0001;
        /**
         * 有内容时显示(不管是否有焦点)
         */
        int HAS_CONTENT_SHOW = 0x0002;
        /**
         * 有内容并且有焦点时显示
         */
        int HAS_CONTENT_FOCUS_SHOW = 0x0003;
    }

    /**
     * 内容改变监听对象
     */
    private MyTextWatcher mTextWatcher;
    /**
     * 自定义的文字改变监听对象
     */
    private OnTextChangeListener mOnTextChangeListener;
    /**
     * 自定义焦点改变监听对象
     */
    private OnMyFocusChangeListener mOnMyFocusChangeListener;
    /**
     * 指定右边删除图片显示的时间，默认有内容并且有焦点时显示
     */
    private int mDelIconTime = DelIconShowTime.HAS_CONTENT_FOCUS_SHOW;
    /**
     * 右边删除按钮图片对象
     */
    private Drawable mClearDrawable;
    private Drawable[] mCompoundDrawables;

    public ClearAbleEditText(Context context) {
        this(context, null);
    }

    public ClearAbleEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearAbleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initListener();
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClearAbleEditText);
        mClearDrawable = typedArray.getDrawable(R.styleable.ClearAbleEditText_delete_icon);
        mDelIconTime = typedArray.getInt(R.styleable.ClearAbleEditText_del_show_time, DelIconShowTime.HAS_CONTENT_FOCUS_SHOW);
        typedArray.recycle();

        // 右边清除图片
        if (null == mClearDrawable) { // 没有定义就使用默认的图片
            mClearDrawable = getContext().getResources().getDrawable(R.mipmap.module_view_delete_edit);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        mCompoundDrawables = getCompoundDrawables();
        mClearDrawable.setVisible(false, false); // 最开始默认隐藏
        setCompoundDrawables(mCompoundDrawables[DRAWABLE_LEFT], mCompoundDrawables[DRAWABLE_TOP],
                mClearDrawable.isVisible() ? mClearDrawable : null,
                mCompoundDrawables[DRAWABLE_BOTTOM]);
    }

    /**
     * 初始化其他的相关内容
     */
    private void initListener() {
        // 初始化并设置监听
        mTextWatcher = new MyTextWatcher();
        addTextChangedListener(mTextWatcher);

        // 焦点改变监听
        focusChangeListener();
    }

    /**
     * 焦点改变监听
     */
    private void focusChangeListener() {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                changeDelShowStatus(getText(), hasFocus);
                if (null != mOnMyFocusChangeListener)
                    mOnMyFocusChangeListener.onFocusChange(v, hasFocus);
            }
        });
    }

    /**
     * 设置右边删除图片
     *
     * @param delDrawable 右边的图片
     * @return
     */
    public ClearAbleEditText setDelDrawable(@NonNull Drawable delDrawable) {
        if (delDrawable == null) return this;
        this.mClearDrawable = delDrawable;
        return this;
    }

    /**
     * 设置右边删除图片资源id
     *
     * @param delDrawableId 右边的图片资源id
     * @return
     */
    public ClearAbleEditText setDelDrawable(int delDrawableId) {
        this.mClearDrawable = getResources().getDrawable(delDrawableId);
        return this;
    }

    /**
     * 设置右边删除图片显示的时间，默认有内容并且有焦点时显示
     *
     * @param delIconTime 设置右边删除图片显示的时间，取值<br/>
     *                    XEditText.ALWAYS_SHOW：总是显示(一直显示)<br/>
     *                    XEditText.ALWAYS_HIND：总是隐藏(从不显示)<br/>
     *                    XEditText.HAS_CONTENT_SHOW：有内容时显示(不管焦点)<br/>
     *                    XEditText.HAS_CONTENT_FOCUS_SHOW：有内容并且有焦点时显示<br/>
     * @return
     */
    public ClearAbleEditText setDelIconShowTime(@DelIconShowTime int delIconTime) {
        this.mDelIconTime = delIconTime;
        return this;
    }

    /**
     * 设置文字改变监听
     *
     * @param onTextChangeListener
     * @return
     */
    public ClearAbleEditText setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
        return this;
    }

    /**
     * 设置焦点改变监听
     *
     * @param onMyFocusChangeListener
     * @return
     */
    public ClearAbleEditText setOnMyFocusChangeListener(OnMyFocusChangeListener onMyFocusChangeListener) {
        this.mOnMyFocusChangeListener = onMyFocusChangeListener;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        if (mClearDrawable.isVisible() && MotionEvent.ACTION_UP == event.getAction()
                && eventX > getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicHeight()) {
            setText("");
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据是否有内容和焦点以及设置的显示状态改变右边图片的显示状态
     *
     * @param text
     * @param hasFocus
     */
    private void changeDelShowStatus(Editable text, boolean hasFocus) {
        if (DelIconShowTime.ALWAYS_SHOW == mDelIconTime) { // 总是显示
            mClearDrawable.setVisible(true, false);
        } else if (DelIconShowTime.HAS_CONTENT_SHOW == mDelIconTime) { // 有内容就显示
            if (null != text && !TextUtils.isEmpty(text.toString()))
                mClearDrawable.setVisible(true, false);
            else mClearDrawable.setVisible(false, false);
        } else if (DelIconShowTime.HAS_CONTENT_FOCUS_SHOW == mDelIconTime) { // 有内容并且有焦点时显示
            if (null != text && !TextUtils.isEmpty(text.toString()) && hasFocus)
                mClearDrawable.setVisible(true, false);
            else mClearDrawable.setVisible(false, false);
        } else { // 总是隐藏
            mClearDrawable.setVisible(false, false);
        }
        setCompoundDrawables(mCompoundDrawables[DRAWABLE_LEFT], mCompoundDrawables[DRAWABLE_TOP],
                mClearDrawable.isVisible() ? mClearDrawable : null,
                mCompoundDrawables[DRAWABLE_BOTTOM]);
    }

    /**
     * 文字改变监听
     */
    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            changeDelShowStatus(s, isFocused());
            if (null != mOnTextChangeListener)
                mOnTextChangeListener.afterTextChanged(s);
        }
    }

    /**
     * 提供给开发者调用的文字改变监听
     */
    public interface OnTextChangeListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    /**
     * 提供给开发者调用的焦点改变监听
     */
    public interface OnMyFocusChangeListener {
        void onFocusChange(View view, boolean hasFocus);
    }
}
