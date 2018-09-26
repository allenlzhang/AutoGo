package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.carlt.autogo.R;

import butterknife.OnClick;

public class InputPwdDialog extends BaseDialog {


    public DialogBtnClickListener lisniter;

    public InputPwdDialog(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    void setWindowParams() {

    }

    @Override
    int setRes() {
        return R.layout.dialog_input_pwd;
    }

    @Override
    void init() {

    }

    @OnClick({R.id.tvCancel, R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvConfirm:

                if (lisniter != null) {
                    lisniter.onClick();
                }
                dismiss();
                break;
        }
    }

    public void setOnClickListener(DialogBtnClickListener lisniter) {
        this.lisniter = lisniter;
    }

    public interface DialogBtnClickListener {
        void onClick();
    }
}
