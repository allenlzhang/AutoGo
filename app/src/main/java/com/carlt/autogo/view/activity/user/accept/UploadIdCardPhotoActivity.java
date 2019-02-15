package com.carlt.autogo.view.activity.user.accept;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.PhotoUtils;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * @author wsq
 */
public class UploadIdCardPhotoActivity extends BaseMvpActivity {

    String[] mPermission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.img_delet_person_photo)
    ImageView      imgDeletPersonPhoto;
    @BindView(R.id.img_person)
    ImageView      imgPerson;
    @BindView(R.id.img_person_watermark)
    ImageView      imgPersonWatermark;
    @BindView(R.id.img_person_c)
    ImageView      imgPersonCamera;
    @BindView(R.id.img_delet_back_photo)
    ImageView      imgDeletBackPhoto;
    @BindView(R.id.img_idcard_back_c)
    ImageView      imgIdcardBackCamera;
    @BindView(R.id.img_idcard_back)
    ImageView      imgIdcardBack;
    @BindView(R.id.img_back_watermark)
    ImageView      imgBackWatermark;
    @BindView(R.id.idcard_upload_commit)
    Button         idcardUploadCommit;
    @BindView(R.id.rl_center)
    RelativeLayout rlCenter1;
    @BindView(R.id.rl_center2)
    RelativeLayout rlCenter2;


    private static final int    CODE_GALLERY_REQUEST  = 0xa0;
    private static final int    CODE_CAMERA_REQUEST   = 0xa1;
    private static final int    CODE_RESULT_REQUEST   = 0xa2;
    private              File   AotugoImage           = new File(Environment.getExternalStorageDirectory().getPath() + "/Aotugo/Image");
    private              File   fileUri               = new File(AotugoImage + "/photo.jpg");
    private              File   fileCropUriface       = new File(AotugoImage + "/crop_photo_face.jpg");
    private              File   waterFileCropUriface  = new File(AotugoImage + "/water_crop_photo_face.jpg");
    private              File   waterFileCropUriBalce = new File(AotugoImage + "/water_crop_photo_back.jpg");
    private              File   fileCropUriBalce      = new File(AotugoImage + "/crop_photo_back.jpg");
    private              Uri    imageUri;
    private              Uri    cropImageUri;
    private              File   IdCardFront;
    private              File   waterIdCardFront;
    private              File   IdCardBack;
    private              File   waterIdCardBack;
    private              String name;
    private              String realName;
    private              String idRealNum;
    private              String idCardNum;

    private int carmeraTag = -1;

    DialogIdcardAccept dialogIdcardAccept;

    MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    @Override
    protected int getContentView() {
        return R.layout.activity_upload_id_card_photo;
    }

    @Override
    public void init() {
        setTitleText("上传身份证件");
        enabledCommit();
        name = getIntent().getStringExtra("name");
        realName = getIntent().getStringExtra("realName");
        realName = getIntent().getStringExtra("realName");
        idRealNum = getIntent().getStringExtra("idNum");
        idCardNum = getIntent().getStringExtra("idcard");
        tvName.setText(name + "\t" + idCardNum);
        dialogIdcardAccept = new DialogIdcardAccept(this);
        if (!AotugoImage.exists()) {
            AotugoImage.mkdirs();
        }
        dialogIdcardAccept.setListner(new DialogIdcardAccept.ItemOnclickListner() {
            @Override
            public void byCermera() {

                checkPermissions(mPermission, new PremissoinLisniter() {
                    @Override
                    public void createred() {
                        doCarmera();
                    }
                });
            }

            @Override
            public void byAblum() {

                checkPermissions(mPermission, new PremissoinLisniter() {
                    @Override
                    public void createred() {
                        doPhoto();
                    }
                });

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //删除从新拍照
    @OnClick({R.id.img_delet_person_photo, R.id.img_delet_back_photo})
    public void deletImage(View view) {
        if (view.getId() == R.id.img_delet_person_photo) {
            imgPerson.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgPersonWatermark.setVisibility(View.GONE);
            rlCenter1.setVisibility(View.VISIBLE);
            imgDeletPersonPhoto.setVisibility(View.GONE);

        } else {
            imgIdcardBack.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgBackWatermark.setVisibility(View.GONE);
            rlCenter2.setVisibility(View.VISIBLE);
            imgDeletBackPhoto.setVisibility(View.GONE);
        }
    }

    //拍照
    @OnClick({R.id.img_person_c, R.id.img_idcard_back_c})
    public void useCamera(View view) {

        if (view.getId() == R.id.img_person_c) {
            carmeraTag = 0;
        } else {
            carmeraTag = 1;
        }
        if (dialogIdcardAccept != null) {
            dialogIdcardAccept.show();
        }

    }


    //提交
    @SuppressLint("CheckResult")
    @OnClick(R.id.idcard_upload_commit)
    public void commit() {
        if (IdCardFront == null) {
            showToast("请上传身份证人像页照片");
            return;
        }
        if (IdCardBack == null) {
            showToast("请上传国徽页照片");
            return;
        }
        dialog.show();
        List<File> filesList = new ArrayList<>();
        filesList.add(IdCardFront);
        filesList.add(IdCardBack);
        filesList.add(waterIdCardFront);
        filesList.add(waterIdCardBack);

        LogUtils.e("压缩前----" + IdCardFront.length() + "--1---" + filesList.get(1).length() + "--2---"
                + filesList.get(2).length() + "--3---" + filesList.get(3).length());

        Flowable.just(filesList)
                .observeOn(Schedulers.io())
                .map(new Function<List<File>, List<File>>() {
                    @Override
                    public List<File> apply(List<File> files) throws Exception {
                        return Luban.with(UploadIdCardPhotoActivity.this).load(files).get();
                    }
                })
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        LogUtils.e("压缩后----" + files.get(0).length() + "--1---" + files.get(1).length() + "--2---"
                                + files.get(2).length() + "--3---" + files.get(3).length());

                        ObservableSource<UpdateImageResultInfo> o1 = getUpdateImageResultInfoObservableSource(files.get(0));
                        ObservableSource<UpdateImageResultInfo> o2 = getUpdateImageResultInfoObservableSource(files.get(1));
                        ObservableSource<UpdateImageResultInfo> o3 = getUpdateImageResultInfoObservableSource(files.get(2));
                        ObservableSource<UpdateImageResultInfo> o4 = getUpdateImageResultInfoObservableSource(files.get(3));

                        checkInfo(o1, o2, o3, o4);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.toString());
                    }
                });
        //        ObservableSource<UpdateImageResultInfo> o1 = getUpdateImageResultInfoObservableSource(IdCardFront);
        //        ObservableSource<UpdateImageResultInfo> o2 = getUpdateImageResultInfoObservableSource(IdCardBack);
        //        ObservableSource<UpdateImageResultInfo> o3 = getUpdateImageResultInfoObservableSource(waterIdCardFront);
        //        ObservableSource<UpdateImageResultInfo> o4 = getUpdateImageResultInfoObservableSource(waterIdCardBack);
        //        Disposable disposableImage = Observable.zip(o1, o2, o3, o4, new Function4<UpdateImageResultInfo, UpdateImageResultInfo, UpdateImageResultInfo, UpdateImageResultInfo, ImgId>() {
        //            @Override
        //            public ImgId apply(UpdateImageResultInfo updateImageResultInfo, UpdateImageResultInfo updateImageResultInfo2, UpdateImageResultInfo updateImageResultInfo3, UpdateImageResultInfo updateImageResultInfo4) throws Exception {
        //                ImgId imgId = new ImgId();
        //                imgId.faceImgId = updateImageResultInfo.message.id;
        //                imgId.nationalImgId = updateImageResultInfo2.message.id;
        //                imgId.waterFaceImgId = updateImageResultInfo3.message.id;
        //                imgId.waterNationalImgId = updateImageResultInfo4.message.id;
        //                imgId.idNum = idRealNum;
        //                imgId.name = realName;
        //                return imgId;
        //            }
        //        }).flatMap(new Function<ImgId, ObservableSource<BaseError>>() {
        //            @Override
        //            public ObservableSource<BaseError> apply(ImgId imgId) throws Exception {
        //                //                addIdentity(imgId);
        //                Map<String, Object> map = new HashMap<>();
        //                map.put("name", imgId.name);
        //                map.put("number", imgId.idNum);
        //                map.put("front", imgId.faceImgId);
        //                map.put("back", imgId.nationalImgId);
        //                map.put("watermarkFront", imgId.waterFaceImgId);
        //                map.put("watermarkBack", imgId.waterNationalImgId);
        //                return ClientFactory.def(UserService.class).addIdentity(map);
        //            }
        //        })
        //                .subscribeOn(Schedulers.newThread())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Consumer<BaseError>() {
        //                    @Override
        //                    public void accept(BaseError user) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e("成功后返回--" + user);
        //                        if (user.code == 0) {
        //                            UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        //                            showToast("上传成功");
        //                            info.identityAuth = 2;
        //                            SharepUtil.putByBean(GlobalKey.USER_INFO, info);
        //                            Intent intent = new Intent(UploadIdCardPhotoActivity.this, FaceAuthSettingActivity.class);
        //                            intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ID_Card);
        //                            intent.putExtra("hideName", name);
        //                            SharepUtil.put(GlobalKey.ID_CARD_NAME, name);
        //                            startActivity(intent);
        //                            for (Activity activity : ActivityControl.mActivityList) {
        //                                if (activity instanceof IdCardAcceptActivity) {
        //                                    activity.finish();
        //                                }
        //                                if (activity instanceof UserIdChooseActivity) {
        //                                    activity.finish();
        //                                }
        //                            }
        //
        //                            finish();
        //                        } else {
        //                            showToast(user.msg);
        //                        }
        //
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        LogUtils.e(throwable.toString());
        //                        dialog.dismiss();
        //                        showToast("上传失败");
        //                        LogUtils.e(throwable);
        //                    }
        //                });
        //
        //        disposables.add(disposableImage);

    }

    private void checkInfo(ObservableSource<UpdateImageResultInfo> o1,
                           ObservableSource<UpdateImageResultInfo> o2,
                           ObservableSource<UpdateImageResultInfo> o3,
                           ObservableSource<UpdateImageResultInfo> o4) {
        Disposable disposableImage = Observable.zip(o1, o2, o3, o4, new Function4<UpdateImageResultInfo, UpdateImageResultInfo, UpdateImageResultInfo, UpdateImageResultInfo, ImgId>() {
            @Override
            public ImgId apply(UpdateImageResultInfo updateImageResultInfo, UpdateImageResultInfo updateImageResultInfo2, UpdateImageResultInfo updateImageResultInfo3, UpdateImageResultInfo updateImageResultInfo4) throws Exception {
                ImgId imgId = new ImgId();
                imgId.faceImgId = updateImageResultInfo.message.id;
                imgId.nationalImgId = updateImageResultInfo2.message.id;
                imgId.waterFaceImgId = updateImageResultInfo3.message.id;
                imgId.waterNationalImgId = updateImageResultInfo4.message.id;
                imgId.idNum = idRealNum;
                imgId.name = realName;
                return imgId;
            }
        }).flatMap(new Function<ImgId, ObservableSource<BaseError>>() {
            @Override
            public ObservableSource<BaseError> apply(ImgId imgId) throws Exception {
                //                addIdentity(imgId);
                Map<String, Object> map = new HashMap<>();
                map.put("name", imgId.name);
                map.put("number", imgId.idNum);
                map.put("front", imgId.faceImgId);
                map.put("back", imgId.nationalImgId);
                map.put("watermarkFront", imgId.waterFaceImgId);
                map.put("watermarkBack", imgId.waterNationalImgId);
                return ClientFactory.def(UserService.class).addIdentity(map);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError user) throws Exception {
                        dialog.dismiss();
                        LogUtils.e("成功后返回--" + user);
                        if (user.code == 0) {
                            UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                            showToast("上传成功");
                            info.identityAuth = 2;
                            SharepUtil.putByBean(GlobalKey.USER_INFO, info);
                            Intent intent = new Intent(UploadIdCardPhotoActivity.this, FaceAuthSettingActivity.class);
                            intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ID_Card);
                            intent.putExtra("hideName", name);
                            SharepUtil.put(GlobalKey.ID_CARD_NAME, name);
                            startActivity(intent);
                            for (Activity activity : ActivityControl.mActivityList) {
                                if (activity instanceof IdCardAcceptActivity) {
                                    activity.finish();
                                }
                                if (activity instanceof UserIdChooseActivity) {
                                    activity.finish();
                                }
                            }

                            finish();
                        } else {
                            showToast(user.msg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.toString());
                        dialog.dismiss();
                        showToast("上传失败");
                        LogUtils.e(throwable);
                    }
                });

        disposables.add(disposableImage);
    }

    public static class ImgId {
        public int    faceImgId;
        public int    nationalImgId;
        public int    waterNationalImgId;
        public int    waterFaceImgId;
        public String idNum;
        public String name;

        @Override
        public String toString() {
            return "ImgId{" +
                    "faceImgId=" + faceImgId +
                    ", nationalImgId=" + nationalImgId +
                    ", waterNationalImgId=" + waterNationalImgId +
                    ", waterFaceImgId=" + waterFaceImgId +
                    ", idNum='" + idNum + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private void doCarmera() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(UploadIdCardPhotoActivity.this, "com.carlt.autogo.fileprovider", fileUri);
        }
        PhotoUtils.takePicture(UploadIdCardPhotoActivity.this, imageUri, CODE_CAMERA_REQUEST);
    }

    public void doPhoto() {
        PhotoUtils.openPic(UploadIdCardPhotoActivity.this, CODE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 310;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    if (carmeraTag == 0) {
                        IdCardFront = fileCropUriface;
                        cropImageUri = Uri.fromFile(fileCropUriface);
                    } else {
                        IdCardBack = fileCropUriBalce;
                        cropImageUri = Uri.fromFile(fileCropUriBalce);
                    }

                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    enabledCommit();
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (carmeraTag == 0) {
                        IdCardFront = fileCropUriface;
                        cropImageUri = Uri.fromFile(fileCropUriface);
                    } else {
                        IdCardBack = fileCropUriBalce;
                        cropImageUri = Uri.fromFile(fileCropUriBalce);
                    }
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    }

                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    enabledCommit();
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    Bitmap bitmap1 = creatWaterMarkBitmap(bitmap);
                    showImg(bitmap1);
                    saveWaterMarkBitmap(bitmap1);
                    //                    if (carmeraTag == 0) {
                    //                        waterIdCardFront = saveWaterMarkBitmap(bitmap1);
                    //                    } else {
                    //                        waterIdCardBack = saveWaterMarkBitmap(bitmap1);
                    //                    }
                    //                    setPicToView(bitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * @param file
     *         要上传的图片文件
     * @return 返回请求图片上传接口的回调结果
     */

    @SuppressLint("CheckResult")
    private ObservableSource<UpdateImageResultInfo> getUpdateImageResultInfoObservableSource(File file) {


        RequestBody requestBody = MultipartBodyBuilder
                .addFormDataPart("type", "autogo/identity")
                .addFormDataPart("fileOwner", "identity")
                .addFormDataPart("uid", "9999999999")
                .addFormDataPart("name", "faceImage")
                .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        //图片上传
        return ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody);


    }


    /**
     * @param bitmap
     *         水印bitmap
     * @return 返回水印图片保存地址
     */
    @Nullable
    private File saveWaterMarkBitmap(Bitmap bitmap) {
        File filePic;
        if (carmeraTag == 0) {
            filePic = waterFileCropUriface;
            waterIdCardFront = waterFileCropUriface;
        } else {
            filePic = waterFileCropUriBalce;
            waterIdCardBack = waterFileCropUriBalce;
        }
        try {
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return filePic;
    }

    private void showImg(Bitmap bitmap) {
        if (carmeraTag == 0) {
            imgPerson.setImageBitmap(bitmap);
            rlCenter1.setVisibility(View.GONE);
            imgDeletPersonPhoto.setVisibility(View.VISIBLE);
        }
        if (carmeraTag == 1) {
            imgIdcardBack.setImageBitmap(bitmap);
            // imgBackWatermark.setVisibility(View.VISIBLE);
            rlCenter2.setVisibility(View.GONE);
            imgDeletBackPhoto.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param bitmap
     * @return 利用canvas 叠加两种图片(加水印)
     */
    @NonNull
    private Bitmap creatWaterMarkBitmap(Bitmap bitmap) {
        Bitmap firstBitmap = bitmap;
        Bitmap secondBitmap = ((BitmapDrawable) imgPersonWatermark.getDrawable()).getBitmap();
        Bitmap b = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(b);
        float w = b.getWidth();
        float h = b.getHeight();
        LogUtils.e(secondBitmap.getWidth() + "========" + secondBitmap.getHeight());
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        //  m.setScale(w / imgPerson.getWidth(), h / imgPerson.getHeight());
        //  m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        canvas.drawBitmap(secondBitmap, (w - secondBitmap.getWidth()) / 2, (h - secondBitmap.getHeight()) / 2, null);
        firstBitmap.recycle();
        return b;
    }

    /**
     * 都为示意图按钮不可点击
     */
    private void enabledCommit() {
        if (IdCardBack == null && IdCardFront == null) {
            idcardUploadCommit.setEnabled(false);
        } else {
            idcardUploadCommit.setEnabled(true);
        }
    }
}
