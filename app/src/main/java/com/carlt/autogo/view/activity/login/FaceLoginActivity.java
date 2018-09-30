package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.view.activity.user.accept.UploadIdCardPhotoActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
            for (Map.Entry<String, String> stringStringEntry : base64ImageMap.entrySet()) {
                LogUtils.e("----" + stringStringEntry.getKey());
            }

            updateFaceImage(base64ImageMap);

            // TODO: 2018/9/20 上传检测后的照片进行比对


            //            Observable.create(new ObservableOnSubscribe<Integer>() {
            //                @Override
            //                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            //                    emitter.onNext(1);
            //                }
            //            })
            //                    .delay(3, TimeUnit.SECONDS)
            //                    .subscribeOn(Schedulers.newThread())
            //                    .observeOn(AndroidSchedulers.mainThread())
            //                    .subscribe(new Consumer<Integer>() {
            //                        @Override
            //                        public void accept(Integer integer) throws Exception {
            //                            go2Activity();
            //                        }
            //                    });

            //             showMessageDialog("活体检测", "检测成功");
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            //             showMessageDialog("人脸检测系统", "登陆超时");
            ToastUtils.showShort("人脸检测超时");
        }
    }

    public static final String imgUrl = "http://tmanageadmin.linewin.cc/image/uploadOssImage";

    @SuppressLint("CheckResult")
    private void updateFaceImage(HashMap<String, String> base64ImageMap) {
        String faceImagePath = Environment.getExternalStorageDirectory().toString() + "/autoGo/faceImage/";
        String faceImageName = "autogo" + System.currentTimeMillis();
        if (base64ImageMap.containsKey("bestImage0")) {
            String bestImage0 = base64ImageMap.get("bestImage0");
            byte[] bytes = base64ToByte(bestImage0);
            //            LogUtils.e(bytes);
            File file = byte2File(bytes, faceImagePath, faceImageName);
            if (file != null) {

                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("type", "autogo/face")
                        .addFormDataPart("fileOwner","face")
                        .addFormDataPart("uid", "9999999999")
                        .addFormDataPart("name", "faceImage")
                        .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .build();

                ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<UpdateImageResultInfo>() {
                            @Override
                            public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {

                                LogUtils.e(updateImageResultInfo.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(throwable.toString());
                            }
                        });


            }

        }


    }

    private static byte[] base64ToByte(String base64Data) {
        return Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        //        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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

    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            //            if (file.exists()) {
            //                file.delete();
            //            }
            boolean newFile = file.createNewFile();
            LogUtils.e("---" + newFile);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}
