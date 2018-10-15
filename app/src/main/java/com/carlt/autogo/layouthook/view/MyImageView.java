package com.carlt.autogo.layouthook.view;

import android.widget.ImageView;

import com.carlt.autogo.layouthook.ViewHandler;

public class MyImageView implements ViewHandler {
    ImageView imageView ;

    public MyImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void hanlderView() {

    }
}
