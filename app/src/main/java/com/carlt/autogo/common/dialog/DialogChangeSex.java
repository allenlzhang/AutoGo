package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogChangeSex extends BaseDialog {
    ItemOnclickListner listner;
    static int MAN    = 1;
    static int WOMAN  = 2;
    static int UNKONW = 3;
    @BindView(R.id.login_by_face)
    TextView loginByFace;
    @BindView(R.id.tvLine1)
    TextView tvLine1;
    @BindView(R.id.login_by_normal)
    TextView loginByNormal;

    @BindView(R.id.login_by_other)
    TextView loginByOther;

    @BindView(R.id.cancle)
    TextView cancle;
    public DialogChangeSex(@NonNull Context context) {
        super(context);
    }

    @Override
    void setWindowParams() {
        layoutParams.gravity = Gravity.BOTTOM;
    }

    @Override
    int setRes() {
        return R.layout.dialog_login_more;
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

                if (listner != null) {
                    listner.getText(loginByFace.getText().toString(), MAN);
                }
                DialogDismiss();
                break;
            case R.id.login_by_normal:

                if (listner != null) {
                    listner.getText(loginByNormal.getText().toString(), WOMAN);
                }
                DialogDismiss();
                break;
            case R.id.login_by_other:

                if (listner != null) {
                    listner.getText(loginByOther.getText().toString(), UNKONW);
                }
                DialogDismiss();
                break;
        }
    }

    private void DialogDismiss() {
        dismiss();

    }

    public void setListner(ItemOnclickListner listner) {
        this.listner = listner;
    }

    public interface ItemOnclickListner {

        void getText(String text, int sex);

    }
}
