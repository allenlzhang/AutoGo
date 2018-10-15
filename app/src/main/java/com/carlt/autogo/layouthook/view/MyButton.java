package com.carlt.autogo.layouthook.view;

import android.widget.Button;

import com.carlt.autogo.layouthook.ViewHandler;

public class MyButton  implements ViewHandler{
    Button button ;

    public MyButton(Button button) {
        this.button = button;
    }

    @Override
    public void hanlderView() {

    }
}
