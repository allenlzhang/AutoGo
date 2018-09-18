package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.carlt.autogo.R;
import com.carlt.autogo.view.activity.login.OtherActivity;

import butterknife.OnClick;

public class DialogChangeSex  extends LoginMoreDialog {
    ItemOnclickListner listner ;
    static  int MAN = 1 ;
    static  int WOMAN = 2 ;
    static  int UNKONW = 3;

    public DialogChangeSex(@NonNull Context context) {
        super(context);
    }

    @Override
    void setWindowParams() {
        super.setWindowParams();
    }

    @Override
    void init() {
        loginByFace.setText("男");
        loginByNormal.setText("女");
        loginByOther.setText("保密");
    }

    @OnClick({R.id.cancle, R.id.login_by_face, R.id.login_by_normal, R.id.login_by_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                DialogDismiss();
                break;
            case R.id.login_by_face:

                if(listner != null){
                    listner.getText(loginByFace.getText().toString(),MAN);
                }
                DialogDismiss();
                break;
            case R.id.login_by_normal:

                if(listner != null){
                    listner.getText(loginByNormal.getText().toString() ,WOMAN);
                }
                DialogDismiss();
                break;
            case R.id.login_by_other:

                if(listner != null){
                    listner.getText(loginByOther.getText().toString(),UNKONW);
                }
                DialogDismiss();
                break;
        }
    }

    private void DialogDismiss() {
        dismiss();

    }
    public void setListner(ItemOnclickListner listner){
        this.listner = listner ;
    }
    public   interface ItemOnclickListner {

        void getText(String text ,int sex);

    }
}
