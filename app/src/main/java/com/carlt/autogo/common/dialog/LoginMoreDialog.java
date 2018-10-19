package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.login.FaceLoginActivity;
import com.carlt.autogo.view.activity.login.LoginByPhoneActivity;
import com.carlt.autogo.view.activity.login.OtherActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginMoreDialog extends BaseDialog {

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
    private boolean isFaceLoginActivity;

    public LoginMoreDialog(@NonNull Context context, boolean isFace) {
        super(context);
        this.isFaceLoginActivity = isFace;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserInfo user = null;
        try {
            user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        } catch (Exception e) {
            user = null;
        }

        boolean isFaceLogin = SharepUtil.preferences.getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);
        LogUtils.e("----" + user);
        LogUtils.e("isFaceLogin----" + isFaceLogin);
        if (!isFaceLoginActivity) {
            loginByFace.setVisibility(View.VISIBLE);
            tvLine1.setVisibility(View.VISIBLE);
        } else {
            loginByFace.setVisibility(View.GONE);
            tvLine1.setVisibility(View.GONE);
        }
        //        if (user == null) {
        //            loginByFace.setVisibility(View.GONE);
        //            tvLine1.setVisibility(View.GONE);
        //        } else {
        //            if (user.alipayAuth == 2 && user.faceId != 0 && isFaceLogin && !isFaceLoginActivity) {
        //                loginByFace.setVisibility(View.VISIBLE);
        //                tvLine1.setVisibility(View.VISIBLE);
        //            } else {
        //                loginByFace.setVisibility(View.GONE);
        //                tvLine1.setVisibility(View.GONE);
        //            }
        //        }

    }

    @OnClick({R.id.cancle, R.id.login_by_face, R.id.login_by_normal, R.id.login_by_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                //                DialogDismiss();
                break;
            case R.id.login_by_face:
                //                人脸登录
                Intent intent = new Intent(context, FaceLoginActivity.class);
                //                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_LOGIN_ACTIVITY);
                context.startActivity(intent);
                break;
            case R.id.login_by_normal:
                //                DialogDismiss();

                Intent intentPhone = new Intent(context, LoginByPhoneActivity.class);
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
