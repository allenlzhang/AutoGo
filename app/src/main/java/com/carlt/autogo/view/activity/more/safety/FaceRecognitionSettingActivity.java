package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;

import butterknife.BindView;

/**
 * Description : 人脸识别设置页面
 * Author     : zhanglei
 * Date       : 2018/9/26
 */
public class FaceRecognitionSettingActivity extends BaseMvpActivity {

    @BindView(R.id.cbFaceLogin)
    CheckBox cbFaceLogin;
    @BindView(R.id.cbSafe)
    CheckBox cbSafe;


    @Override
    protected int getContentView() {
        return R.layout.activity_face_recognition_setting;
    }

    @Override
    public void init() {
        boolean aBoolean = SharepUtil.getPreferences().getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);
        boolean safe = SharepUtil.getPreferences().getBoolean(GlobalKey.PROCESS_SAFE_SWITCH, false);
        cbFaceLogin.setChecked(aBoolean);
        cbSafe.setChecked(safe);
        setTitleText("人脸识别");
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        LogUtils.e("----" + user.toString());
        if (user.alipayAuth == 2 && user.faceId != 0) {
            cbFaceLogin.setEnabled(true);
            cbSafe.setEnabled(true);
            cbFaceLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharepUtil.putBoolean(GlobalKey.FACE_LOGIN_SWITCH, isChecked);
                }
            });
            cbSafe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharepUtil.putBoolean(GlobalKey.PROCESS_SAFE_SWITCH, isChecked);
                }
            });
        } else {
            cbFaceLogin.setEnabled(false);
            cbSafe.setEnabled(false);
            cbSafe.setChecked(false);
            cbFaceLogin.setChecked(false);
            CommonDialog.createDialogNotitle(this, "温馨提示", "请进行身份认证", "取消", "确定", false, new CommonDialog.DialogWithTitleClick() {
                @Override
                public void onRightClick() {
                    startActivity(new Intent(FaceRecognitionSettingActivity.this, UserIdChooseActivity.class));
                    finish();
                }

                @Override
                public void onLeftClick() {
                    finish();
                }
            });
        }

    }
}
