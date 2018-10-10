package com.carlt.autogo.view.activity.more.safety;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;

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
    }
}
