package com.carlt.autogo.view.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.BaseDialog;
import com.carlt.autogo.common.dialog.LoginMoreDialog;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.LoginActivity;
import com.carlt.autogo.view.activity.RegisterActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceLoginActivity extends BaseMvpActivity {

    @BindView(R.id.ic_car)
    ImageView icCar;
    @BindView(R.id.app_name)
    TextView  appName;
    @BindView(R.id.version_code)
    TextView  versionCode;
    @BindView(R.id.user_phone)
    EditText  userPhone;


    @Override
    protected int getContentView() {
        return R.layout.activity_face_login;
    }

    @Override
    public void init() {

        setTitleText("人脸登录");
        hideTitle();
        UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        if (info != null) {
            String mobile = info.mobile;
            if (!TextUtils.isEmpty(mobile)) {
                userPhone.setText(mobile);
            }
        }

    }

    @OnClick({R.id.btn_changeUrl, R.id.login_commit, R.id.forgot_passwd, R.id.user_regist, R.id.btn_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_changeUrl:
                break;
            case R.id.login_commit:
                String phone = userPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入手机号");
                    return;
                }
                Intent intent1 = new Intent(this, FaceLiveCheckActivity.class);
                intent1.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_LOGIN_ACTIVITY);
                intent1.putExtra("mobile", phone);
                startActivity(intent1);
                break;
            case R.id.forgot_passwd:
                //                密码登录
//                UserInfo userInfo = SharepUtil.getBeanFromSp("user");
//                if (userInfo != null) {
//                    userInfo.faceId = 0;
//                    SharepUtil.putByBean(GlobalKey.USER_INFO, userInfo);
//                }
//                SharepUtil.putBoolean(GlobalKey.Face_Login,false);
                startActivity(LoginActivity.class);
                break;
            case R.id.user_regist:
                Intent intentRegist = new Intent(this, RegisterActivity.class);
                startActivity(intentRegist);
                break;
            case R.id.btn_more:
                BaseDialog baseDialog = new LoginMoreDialog(this, true, null);
                baseDialog.show();
                break;
        }
    }
}
