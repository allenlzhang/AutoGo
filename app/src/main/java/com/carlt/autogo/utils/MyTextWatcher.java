package com.carlt.autogo.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author wsq
 * @time 11:26  2018/10/23/023
 * @describe  代码设置 Editext 内容长度
 */
public class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().length()>32){
            s.replace(32,s.length(),"");
        }
    }
}
