package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.view.activity.user.accept.UploadIdCardPhotoActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description :
 * Author     : zhanglei
 * Date       : 2018/9/19
 */
public class FaceLoginActivity extends FaceLivenessActivity {
    public static final int FROM_LOGIN_ACTIVITY         = 11;
    public static final int FROM_ID_CARDACCEPT_ACTIVITY = 12;
    private int    isFrom;
    private String name;
    private String idcard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isFrom = intent.getIntExtra(GlobalKey.FROM_ACTIVITY, -1);
        switch (isFrom) {
            case FROM_LOGIN_ACTIVITY:
                tvFaceTitle.setText(getString(R.string.face_login_activity_title1));
                break;
            case FROM_ID_CARDACCEPT_ACTIVITY:
                tvFaceTitle.setText(getString(R.string.face_login_activity_title2));
                name = intent.getStringExtra("name");
                idcard = intent.getStringExtra("idcard");
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            mCamera.stopPreview();
            ToastUtils.showShort("人脸检测成功");

            // TODO: 2018/9/20 上传检测后的照片进行比对
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(1);
                }
            })
                    .delay(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            go2Activity();
                        }
                    });

            //             showMessageDialog("活体检测", "检测成功");
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            //             showMessageDialog("人脸检测系统", "登陆超时");
            ToastUtils.showShort("人脸检测超时");
        }
    }

    private void go2Activity() {
        switch (isFrom) {
            case FROM_LOGIN_ACTIVITY:
                //                    tvFaceTitle.setText(getString(R.string.face_login_activity_title1));
//                finish();
                break;
            case FROM_ID_CARDACCEPT_ACTIVITY:
                //                    tvFaceTitle.setText(getString(R.string.face_login_activity_title2));
                Intent intent = new Intent(this, UploadIdCardPhotoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("idcard", idcard);
                startActivity(intent);
                break;
            default:
                break;

        }
        finish();
    }
}
