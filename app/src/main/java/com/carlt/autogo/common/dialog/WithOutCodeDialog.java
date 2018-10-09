package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.carlt.autogo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/29.
 */
public class WithOutCodeDialog extends BaseDialog {

    @BindView(R.id.edit_dialog_without_code)
    EditText editDialogWithoutCode;
    private WithoutCodeListener listener;


    public void setListener(WithoutCodeListener listener) {
        this.listener = listener;
    }

    public WithOutCodeDialog(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    void setWindowParams() {

    }

    @Override
    int setRes() {
        return R.layout.dialog_without_code;
    }

    @Override
    void init() {

    }

    @OnClick({R.id.edit_dialog_without_code_cancel, R.id.edit_dialog_without_code_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_dialog_without_code_cancel:
                dismiss();
                break;
            case R.id.edit_dialog_without_code_confirm:
                if (listener != null) {
                    listener.OnClickWithoutListener(editDialogWithoutCode.getText().toString());
                }
                dismiss();
                break;
        }
    }

    public interface WithoutCodeListener {
        void OnClickWithoutListener(String d);
    }
}
