package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.baidu.idl.face.platform.utils.Base64Utils;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/10/15 15:16
 */
public class FaceLoginPresenter extends BasePresenter<IFaceLoginView> {
    @SuppressLint("CheckResult")
    public void updateFaceImage(HashMap<String, String> base64ImageMap) {
        uuDialog.show();
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

                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<UpdateImageResultInfo>() {
                            @Override
                            public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                                mView.updateFaceImageFinish(updateImageResultInfo);
                                LogUtils.e(updateImageResultInfo.toString());
                                uuDialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogUtils.e(throwable.toString());
                                uuDialog.dismiss();
                            }
                        });


            }

        }
    }

    private static byte[] base64ToByte(String base64Data) {
        return Base64Utils.decode(base64Data, Base64Utils.NO_WRAP);
        //        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
