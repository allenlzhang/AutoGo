package com.carlt.autogo.view.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.baidu.idl.face.platform.utils.Base64Utils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;
import com.carlt.autogo.view.activity.car.MyCarActivity;
import com.carlt.autogo.view.activity.more.transfer.AuthHandleActivity;
import com.carlt.autogo.view.activity.more.transfer.AuthQRCodeActivity;
import com.carlt.autogo.view.activity.more.transfer.CheckSmsCodeActivity;
import com.carlt.autogo.view.activity.more.transfer.TransHandleActivity;
import com.carlt.autogo.view.activity.more.transfer.TransferQRCodeActivity;
import com.carlt.autogo.view.activity.user.accept.IdfCompleteActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Description :
 * Author     : zhanglei
 * Date       : 2018/9/19
 */
public class FaceLiveCheckActivity extends FaceLivenessActivity {
    public static final int      FROM_LOGIN_ACTIVITY         = 11;
    public static final int      FROM_ID_CARDACCEPT_ACTIVITY = 12;
    public static final int      FROM_ALIPAY_AUTH            = 13;
    public static final int      Trans_Handle_Activity       = 14;
    public static final int      Auth_Handle_Activity        = 15;
    private             int      isFrom;
    //    private String   name;
    //    private String   idcard;
    public              UUDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityControl.addActivity(this);
        dialog = new UUDialog(this, R.style.DialogCommon);
        initData();
    }

    private void initData() {

        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.e(data.toString());
                        //                        for (String datum : data) {
                        //                            if (datum.equals(Manifest.permission.CAMERA)) {
                        //                                ToastUtils.showShort("未获取到相机权限");
                        //                            }
                        //                        }

                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.e(data.toString());
                    }
                }).start();
        Intent intent = getIntent();
        isFrom = intent.getIntExtra(GlobalKey.FROM_ACTIVITY, -1);
        switch (isFrom) {
            case FROM_LOGIN_ACTIVITY:
                tvFaceTitle.setText(getString(R.string.face_login_activity_title1));
                break;
            case FROM_ID_CARDACCEPT_ACTIVITY:
                tvFaceTitle.setText(getString(R.string.face_login_activity_title2));
                //                name = intent.getStringExtra("name");
                //                idcard = intent.getStringExtra("idcard");
                break;
            case FROM_ALIPAY_AUTH:
                tvFaceTitle.setText(getString(R.string.face_login_activity_title2));

                break;
            case Trans_Handle_Activity:
            case Auth_Handle_Activity:
                tvFaceTitle.setText("安全验证");
                tvMsg.setVisibility(View.VISIBLE);
                break;
            default:

                break;
        }
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSms = new Intent(FaceLiveCheckActivity.this, CheckSmsCodeActivity.class);
                switch (isFrom) {
                    case Trans_Handle_Activity:
                        int transferId = getIntent().getIntExtra("transferId", -1);
                        intentSms.putExtra("isTransfer", true);
                        intentSms.putExtra("transferId", transferId);
                        break;
                    case Auth_Handle_Activity:
                        int authId = getIntent().getIntExtra("authId", -1);
                        intentSms.putExtra("isTransfer", false);
                        intentSms.putExtra("authId", authId);
                        break;
                }
                startActivity(intentSms);
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
            mCamera.stopPreview();
            //            ToastUtils.showShort("人脸检测成功");
            for (Map.Entry<String, String> stringStringEntry : base64ImageMap.entrySet()) {
                LogUtils.e("----" + stringStringEntry.getKey());
            }

            updateFaceImage(base64ImageMap);


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


    @SuppressLint("CheckResult")
    private void updateFaceImage(HashMap<String, String> base64ImageMap) {
        dialog.show();
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
                        .addFormDataPart("fileOwner", "face")
                        .addFormDataPart("uid", "9999999999")
                        .addFormDataPart("name", "faceImage")
                        .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .build();

                ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody)
                        //                        .flatMap(new Function<UpdateImageResultInfo, ObservableSource<User>>() {
                        //                            @Override
                        //                            public ObservableSource<User> apply(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                        //                                int id = updateImageResultInfo.message.id;
                        //                                Map<String, String> map = new HashMap<>();
                        //                                map.put("token", SharepUtil.preferences.getString("token", ""));
                        //                                map.put("faceId ", id + "");
                        //                                return ClientFactory.def(UserService.class).setFace(map);
                        //                            }
                        //                        })

                        //                        .subscribe(new Consumer<User>() {
                        //                            @Override
                        //                            public void accept(User user) throws Exception {
                        //                                if (user.err == null) {
                        //                                    ToastUtils.showShort("设置成功");
                        //                                    go2Activity();
                        //                                } else {
                        //                                    ToastUtils.showShort("设置失败");
                        //                                }
                        //                            }
                        //                        });
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<UpdateImageResultInfo>() {
                            @Override
                            public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                                switch (isFrom) {
                                    case FROM_ALIPAY_AUTH:
                                    case FROM_ID_CARDACCEPT_ACTIVITY:
                                        //支付宝和身份证认证
                                        authMethod(updateImageResultInfo);
                                        break;
                                    case FROM_LOGIN_ACTIVITY:
                                        //人脸登录逻辑
                                        faceLogin(updateImageResultInfo);
                                        break;
                                    case Trans_Handle_Activity:
                                        //过户人脸验证
                                        int transferId = getIntent().getIntExtra("transferId", -1);
                                        checkFaceForTrans(updateImageResultInfo, transferId, 1);
                                        break;
                                    case Auth_Handle_Activity:
                                        int authId = getIntent().getIntExtra("authId", -1);
                                        checkFaceForAuth(updateImageResultInfo, authId, 3);
                                        break;
                                }
                                LogUtils.e(updateImageResultInfo.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(throwable.toString());
                                dialog.dismiss();
                            }
                        });


            }

        }


    }

    @SuppressLint("CheckResult")
    private void checkFaceForAuth(UpdateImageResultInfo updateImageResultInfo, int authId, int status) {
        Map<String, Object> map = new HashMap<>();
        int id = updateImageResultInfo.message.id;
        map.put("faceId", id);

        final HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", authId);
        map1.put("status", status);
        ClientFactory.def(UserService.class).compareFace(map)
                .filter(new Predicate<BaseError>() {
                    @Override
                    public boolean test(BaseError error) throws Exception {
                        if (error.code == 0) {
                            return true;
                        } else {
                            ToastUtils.showShort(error.msg);
                            dialog.dismiss();
                            finish();
                            return false;
                        }
                        //                        return error.code == 0;
                    }
                })
                .flatMap(new Function<BaseError, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(BaseError error) throws Exception {
                        return ClientFactory.def(CarService.class).modifyStatus(map1);

                    }
                })

                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.code == 0) {

                            ToastUtils.showShort("操作成功");
                            closeActivity();
                        } else {
                            ToastUtils.showShort(carBaseInfo.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                        //                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void checkFaceForTrans(UpdateImageResultInfo updateImageResultInfo, int transferId, int status) {
        Map<String, Object> map = new HashMap<>();
        int id = updateImageResultInfo.message.id;
        map.put("faceId", id);

        final HashMap<String, Object> map1 = new HashMap<>();
        map1.put("transferId", transferId);
        map1.put("isAgree", status);
        ClientFactory.def(UserService.class).compareFace(map)
                .filter(new Predicate<BaseError>() {
                    @Override
                    public boolean test(BaseError error) throws Exception {
                        if (error.code == 0) {

                            return true;
                        } else {
                            ToastUtils.showShort(error.msg);
                            dialog.dismiss();
                            finish();
                            return false;
                        }
                        //                        return error.code == 0;
                    }
                })
                .flatMap(new Function<BaseError, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(BaseError user) throws Exception {
                        return ClientFactory.def(CarService.class).dealTransferCode(map1);
                    }
                })
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.code == 0) {

                            ToastUtils.showShort("操作成功");
                            //                            closeActivity();
                            jump2Activity();
                        } else {
                            ToastUtils.showShort(carBaseInfo.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                        //                        ToastUtils.showShort("操作失败");
                    }
                });

    }

    private void jump2Activity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransferQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransHandleActivity) {
                activity.finish();
            }
            if (activity instanceof AuthHandleActivity) {
                activity.finish();
            }
        }
        Intent intent = new Intent(this, MyCarActivity.class);
        startActivity(intent);
        finish();
    }

    private void closeActivity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransferQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransHandleActivity) {
                activity.finish();
            }
            if (activity instanceof AuthHandleActivity) {
                activity.finish();
            }
        }
        finish();
    }

    public static final String faceLoginUrl = "http://test.linewin.cc:8888/app/User/LoginByFace";

    /**
     * Description : 人脸登录逻辑
     * Author     : zhanglei
     * Date       : 2018/10/10
     */
    @SuppressLint("CheckResult")
    private void faceLogin(UpdateImageResultInfo updateImageResultInfo) {
        //        UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        //        String mobile = info.mobile;
        String mobile1 = getIntent().getStringExtra("mobile");
        int id = updateImageResultInfo.message.id;
        Map<String, Object> map = new HashMap<>();
        map.put("loginType", GlobalKey.loginStateByFace);
        map.put("mobile", mobile1);
        map.put("faceId", id);
        map.put("version", AutoGoApp.VERSION);
        map.put("moveDeviceName", AutoGoApp.MODEL_NAME);
        map.put("loginModel", AutoGoApp.MODEL);
        map.put("loginSoftType", "Android");
        //        Gson gson = new Gson();
        //        String json = gson.toJson(map);
        //        OkGo.<String>post(faceLoginUrl)
        //                .headers("Carlt-Access-Id", "19877415356991399877")
        //                .upJson(json)
        //                .execute(new StringCallback() {
        //                    @Override
        //                    public void onSuccess(Response<String> response) {
        //                        LogUtils.e("====" + response.body());
        //                    }
        //                });

        ClientFactory.def(UserService.class).commonLogin(map)

                .flatMap(new Function<User, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(User user) throws Exception {
                        LogUtils.e("---" + user.toString());
                        dialog.dismiss();
                        if (user.err != null) {
                            //                            errorMsg = user.err.msg;
                            dialog.dismiss();
                            ToastUtils.showShort(user.err.msg);
                            ActivityControl.removeAllActivity(FaceLiveCheckActivity.this);
                            startActivity(new Intent(FaceLiveCheckActivity.this, FaceLoginActivity.class));
                            finish();
                            return null;
                        } else {
                            Map<String, String> token = new HashMap<>();
                            token.put("token", user.token);
                            SharepUtil.put(GlobalKey.USER_TOKEN, user.token);
                            return ObservableHelper.getUserInfoByToken(token, GlobalKey.loginStateByOther);
                        }
                    }

                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        dialog.dismiss();

                        ToastUtils.showShort(s);
                        go2Activity();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.toString());
                    }
                });
    }

    /**
     * Description : 身份认证逻辑
     * Author     : zhanglei
     * Date       : 2018/10/10
     */
    @SuppressLint("CheckResult")
    private void authMethod(UpdateImageResultInfo updateImageResultInfo) {
        int id = updateImageResultInfo.message.id;
        Map<String, Object> map = new HashMap<>();
        map.put("token", SharepUtil.preferences.getString(GlobalKey.USER_TOKEN, ""));
        map.put("faceId", id);
        ClientFactory.def(UserService.class).setFace(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        LogUtils.e("=====" + user);
                        if (user.err == null) {

                            ToastUtils.showShort("设置成功");


                            //                            switch (isFrom) {
                            //                                case FROM_ALIPAY_AUTH:
                            //
                            //                                    break;
                            //                                case FROM_ID_CARDACCEPT_ACTIVITY:
                            //
                            //                                    break;
                            //                            }

                            go2Activity();
                        } else {
                            ToastUtils.showShort("设置失败");
                        }
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    private static byte[] base64ToByte(String base64Data) {
        return Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        //        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void go2Activity() {
        UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        info.faceId = 1;


        switch (isFrom) {
            case FROM_LOGIN_ACTIVITY:
                //                    tvFaceTitle.setText(getString(R.string.face_login_activity_title1));
                //                finish();
                ActivityControl.removeAllActivity(this);
                startActivity(new Intent(this, MainActivity.class));
                break;
            case FROM_ID_CARDACCEPT_ACTIVITY:
                //                    tvFaceTitle.setText(getString(R.string.face_login_activity_title2));
                info.identityAuth = 2;
                String hideName = getIntent().getStringExtra("hideName");
                Intent intent = new Intent(this, IdfCompleteActivity.class);
                //                intent.putExtra("name", name);
                intent.putExtra("idcard", true);
                intent.putExtra("hideName", hideName);
                startActivity(intent);
                break;
            case FROM_ALIPAY_AUTH:
                info.alipayAuth = 2;
                Intent intent1 = new Intent(this, IdfCompleteActivity.class);
                intent1.putExtra("idcard", false);
                startActivity(intent1);
                break;
            default:
                break;

        }
        SharepUtil.putByBean(GlobalKey.USER_INFO, info);
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
