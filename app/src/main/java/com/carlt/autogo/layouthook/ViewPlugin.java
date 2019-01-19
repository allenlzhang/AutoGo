package com.carlt.autogo.layouthook;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.autogo.layouthook.view.MyButton;
import com.carlt.autogo.layouthook.view.MyEdtext;
import com.carlt.autogo.layouthook.view.MyImageView;
import com.carlt.autogo.layouthook.view.MyTextView;

public class ViewPlugin {


    /**
     * 处理TexxtView
     * @param textView
     */
    public static void pluginTexview(TextView textView) {

        new MyTextView(textView).hanlderView();

    }

    /**
     * 处理Button
     * @param button
     */
    public static void pluginButton(Button button) {
        new MyButton(button).hanlderView();

    }

    /**
     * 处理BimageView
     * @param imageView
     */
    public static void pluginImage(ImageView imageView) {

        new MyImageView(imageView).hanlderView();
    }

    /**
     * 处理editText
     * @param editText
     */
    public static void pluginEdtext(EditText editText) {
        new MyEdtext(editText).hanlderView();

    }

    /**
     * 处理listView
     * @param listView
     */
    public static void pluginAbsListView(AbsListView listView) {

    }

    /**
     * 处理relativeLayout
     * @param relativeLayout
     */
    public static void pluginRelativeLayout(RelativeLayout relativeLayout) {

    }

}
