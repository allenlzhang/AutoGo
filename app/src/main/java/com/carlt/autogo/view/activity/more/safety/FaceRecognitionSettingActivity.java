package com.carlt.autogo.view.activity.more.safety;

import android.os.Bundle;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
 /**
  * Description : 人脸识别设置页面
  * Author     : zhanglei
  * Date       : 2018/9/26
  */
public class FaceRecognitionSettingActivity extends BaseMvpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_face_recognition_setting);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_face_recognition_setting;
    }

    @Override
    public void init() {
        setTitleText("人脸识别");
    }
}
