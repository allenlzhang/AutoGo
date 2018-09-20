package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.view.activity.login.FaceLoginActivity;
import com.carlt.autogo.view.activity.login.OtherActivity;
import com.carlt.autogo.view.activity.login.loginByPhoneActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginMoreDialog extends BaseDialog {

    @BindView(R.id.login_by_face)
    TextView loginByFace;

    @BindView(R.id.login_by_normal)
    TextView loginByNormal;

    @BindView(R.id.login_by_other)
    TextView loginByOther;

    @BindView(R.id.cancle)
    TextView cancle;

    public LoginMoreDialog(@NonNull Context context) {
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

    }


    @OnClick({R.id.cancle, R.id.login_by_face, R.id.login_by_normal, R.id.login_by_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
//                DialogDismiss();
                break;
            case R.id.login_by_face:
                //                人脸登录
                context.startActivity(new Intent(context, FaceLoginActivity.class));
                break;
            case R.id.login_by_normal:
                //                DialogDismiss();

                Intent intentPhone = new Intent(context, loginByPhoneActivity.class);
                context.startActivity(intentPhone);
                break;
            case R.id.login_by_other:
                Intent intentOhther = new Intent(context, OtherActivity.class);
                context.startActivity(intentOhther);

                break;
            default:
                break;

        }
        dialogDismiss();
    }

    private void dialogDismiss() {
        dismiss();

    }
}
