package com.carlt.autogo.layouthook.view;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.layouthook.ViewHandler;

public class MyTextView implements ViewHandler{

    TextView textView;

    public MyTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void hanlderView() {

     //   textView.setTextColor(Color.RED);
    }
}
