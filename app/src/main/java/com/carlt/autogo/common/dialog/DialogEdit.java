package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.autogo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/12/25.
 */
public class DialogEdit extends BaseDialog{

    @BindView(R.id.tvDialogEditTitle)
    TextView tvDialogEditTitle;
    @BindView(R.id.tvDialogEdit)
    EditText tvDialogEdit;

    private DialogConfirmClickListener listener;

    public DialogEdit(@NonNull Context context, int style) {
        super(context, style);
    }

    @Override
    void setWindowParams() {

    }

    @Override
    int setRes() {
        return R.layout.dialog_edit;
    }

    @Override
    void init() {
    }

    public void setOnClickListener(DialogConfirmClickListener listener) {
        this.listener = listener;
    }

    @OnClick({R.id.tvDialogEditCancel, R.id.tvDialogEditConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDialogEditCancel:
                dismiss();
                break;
            case R.id.tvDialogEditConfirm:
                if (listener != null) {
                    listener.onClick(tvDialogEdit.getText().toString());
                }
                dismiss();
                break;
        }
    }

    public interface DialogConfirmClickListener {
        void onClick(String txt);
    }
}
