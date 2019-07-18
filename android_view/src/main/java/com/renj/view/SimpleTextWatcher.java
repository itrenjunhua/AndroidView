package com.renj.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * 邮箱：itrenjunhua@163.com
 * <p>
 * 创建时间：2018-03-30   15:03
 * <p>
 * 描述：对 {@link TextWatcher} 接口的空实现，选择方法重写，避免重写三个方法
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
