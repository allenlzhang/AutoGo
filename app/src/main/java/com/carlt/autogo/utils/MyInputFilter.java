package com.carlt.autogo.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.carlt.autogo.global.GlobalKey;

/**
 * @author wsq
 * @time 15:19  2018/10/22/022
 * @describe Editext 屏蔽类
 */
public class MyInputFilter implements InputFilter {


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 密码输入框屏蔽中文，其他不满足需求的字符


        if(!RegexUtils.isMatch(GlobalKey.PWD_REGEX2, source.toString()) ){
            return "";
        }

        return null;
    }
}
