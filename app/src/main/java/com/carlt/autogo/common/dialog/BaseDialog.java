package com.carlt.autogo.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.carlt.autogo.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract  class BaseDialog extends Dialog  {

    Context context ;
    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    Unbinder unbinder ;

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.dialog_login_more);
        setWindowParams();
        getWindow().setAttributes(layoutParams);
        this.context = context;

    }
    public BaseDialog(@NonNull Context context ,int style) {
        super(context,style);
        setWindowParams();
        getWindow().setAttributes(layoutParams);
        this.context = context;
        init();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(setRes() == 0){
            return;
        }
        setContentView(setRes());
        unbinder = ButterKnife.bind(this);

        init();
    }
    abstract  void  setWindowParams();
    abstract  int setRes();
//    abstract View setView();
    abstract  void init();

}
