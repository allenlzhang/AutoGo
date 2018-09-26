package com.carlt.autogo.view.activity.more.safety;

import android.os.Bundle;
import android.widget.Button;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.InputPwdDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description : 第一次人脸设置
 * Author     : zhanglei
 * Date       : 2018/9/26
 */
public class FaceRecognitionSettingFirstActivity extends BaseMvpActivity {

    @BindView(R.id.btnSetFace)
    Button btnSetFace;
    private InputPwdDialog inputPwdDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_face_recognition_setting);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_face_recognition_setting_first;
    }

    @Override
    public void init() {
        setTitleText("人脸识别");
        inputPwdDialog = new InputPwdDialog(this, R.style.DialogCommon);
    }

    @OnClick(R.id.btnSetFace)
    public void onViewClicked() {
        inputPwdDialog.show();
    }
}
