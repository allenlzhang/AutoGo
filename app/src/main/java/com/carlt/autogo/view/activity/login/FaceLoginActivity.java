package com.carlt.autogo.view.activity.login;

import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.blankj.utilcode.util.ToastUtils;

import java.util.HashMap;

/**
  * Description : 
  * Author     : zhanglei
  * Date       : 2018/9/19
  */
public class FaceLoginActivity extends FaceLivenessActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
     @Override
     public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
         super.onLivenessCompletion(status, message, base64ImageMap);
         if (status == FaceStatusEnum.OK && mIsCompletion) {
             mCamera.stopPreview();
             //            Intent intent = new Intent(this,FaceScanLongActivity.class);
             //            startActivity(intent);
             //            finish();
             ToastUtils.showShort("活体检测成功");
//             showMessageDialog("活体检测", "检测成功");
         } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                 status == FaceStatusEnum.Error_LivenessTimeout  ||
                 status == FaceStatusEnum.Error_Timeout) {
//             showMessageDialog("人脸检测系统", "登陆超时");
             ToastUtils.showShort("活体检测超时");
         }
     }
}
