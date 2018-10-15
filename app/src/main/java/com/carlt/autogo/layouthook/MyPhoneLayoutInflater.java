package com.carlt.autogo.layouthook;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

/**
 * @author wsq
 * @time 16:03  2018/10/15/015
 * @describe  自定义LayoutInflater 类, 复制源码PhoneLayoutInflate,自定义onCreateView方法,定制View属性
 *
 * 参考 源码LayoutInflater加载View过程, Activit setContentView 方法
 * 参考 源码 ContextImpl ,PolicyManager.makeNewLayoutInflater();
 */
public class MyPhoneLayoutInflater extends LayoutInflater {

    /**
     * 将获取的 name属性,拼接成类的全路径名,用于反射,创建view
     */
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
    };

    /**
     * Instead of instantiating directly, you should retrieve an instance
     * through {@link Context#getSystemService}
     *
     * @param context The Context in which in which to find resources and other
     *                application-specific things.
     *
     * @see Context#getSystemService
     */
    public MyPhoneLayoutInflater(Context context) {
        super(context);

    }

    protected MyPhoneLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    /** Override onCreateView to instantiate names that correspond to the
     widgets known to the Widget factory. If we don't find a match,
     call through to our super class.
     */
    @Override protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {


        for (String prefix : sClassPrefixList) {
            try {
                LogUtils.e(name);
                View view = createView(name, prefix, attrs);
                if (view != null) {

                    switch (name){
                        case "TextView":
                            ViemPlugin.pluginTexview((TextView) view);
                            break;
                        case "Button":
                            ViemPlugin.pluginButton((Button) view);
                            break;

                        case "ImageView":
                            ViemPlugin.pluginImage((ImageView) view);
                            break;

                        case "EditText":
                            ViemPlugin.pluginEdtext((EditText) view);
                            break;
                        case "AbsListView":
                            ViemPlugin.pluginAbsListView((AbsListView) view);
                            break;

                        case "RelativeLayout":
                            ViemPlugin.pluginRelativeLayout((RelativeLayout) view);
                            break;
                    }
                    return view;
                }
            } catch (ClassNotFoundException e) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }

        return super.onCreateView(name, attrs);
    }
    public LayoutInflater cloneInContext(Context newContext) {
        return new MyPhoneLayoutInflater(this, newContext);
    }

}
