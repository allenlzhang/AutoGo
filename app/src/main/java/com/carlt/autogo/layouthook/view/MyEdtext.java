package com.carlt.autogo.layouthook.view;

import android.widget.EditText;

import com.carlt.autogo.layouthook.ViewHandler;

public class MyEdtext implements ViewHandler {
    EditText editText;

    public MyEdtext(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void hanlderView() {

    }
}
