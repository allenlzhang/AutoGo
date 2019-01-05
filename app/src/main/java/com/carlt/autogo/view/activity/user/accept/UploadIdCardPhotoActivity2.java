package com.carlt.autogo.view.activity.user.accept;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.dltech21.ocr.IDCardEnum;
import io.github.dltech21.ocr.IdentityInfo;
import io.github.dltech21.ocr.OcrCameraActivity;
import io.github.dltech21.ocr.OcrConfig;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author wsq
 */
public class UploadIdCardPhotoActivity2 extends BaseMvpActivity {

    String[] mPermission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.img_delet_person_photo)
    ImageView      imgDeletPersonPhoto;
    @BindView(R.id.img_person)
    ImageView      imgPerson;
    @BindView(R.id.img_person_watermark)
    ImageView      imgPersonWatermark;
    //    @BindView(R.id.img_person_c)
    //    ImageView      imgPersonCamera;
    @BindView(R.id.img_delet_back_photo)
    ImageView      imgDeletBackPhoto;
    //    @BindView(R.id.img_idcard_back_c)
    //    ImageView      imgIdcardBackCamera;
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


    private static final int  CODE_GALLERY_REQUEST = 0xa0;
    private static final int  CODE_CAMERA_REQUEST  = 0xa1;
    private static final int  CODE_RESULT_REQUEST  = 0xa2;
    private              File AotugoImage          = new File(Environment.getExternalStorageDirectory().getPath() + "/Aotugo/Image");
    private              File fileUri              = new File(AotugoImage + "/photo.jpg");
    private              File fileCropUriface      = new File(AotugoImage + "/crop_photo_face.jpg");
    private              File fileCropUriBalce     = new File(AotugoImage + "/crop_photo_back.jpg");
    private              Uri  imageUri;
    private              Uri  cropImageUri;

    //    private String name;
    //    private String idCardNum;
    private String idNum;
    private String realName;

    private int carmeraTag = -1;

    DialogIdcardAccept dialogIdcardAccept;

    MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    private String hideName;

    @Override
    protected int getContentView() {
        return R.layout.activity_upload_id_card_photo2;
    }

    @Override
    public void init() {
        setTitleText("上传身份证件");
        hideName = getIntent().getStringExtra("name");
        idNum = getIntent().getStringExtra("idNum");
        realName = getIntent().getStringExtra("realName");
        String idCardNum = getIntent().getStringExtra("idcard");
        LogUtils.e("idNum = " + idNum + "\trealName = " + realName);
        tvName.setText(hideName + "\t" + idCardNum);
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
                        //                        doCarmera();
                    }
                });
            }

            @Override
            public void byAblum() {

                checkPermissions(mPermission, new PremissoinLisniter() {
                    @Override
                    public void createred() {
                        //                        doPhoto();
                    }
                });

            }
        });

        AndPermission.with(this)
                .runtime()
                .permission(needPermissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .start();
        enabledCommit();
    }

    protected String[] needPermissions = {
            Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE, Permission.CAMERA

    };

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
            facepath = "";
            scanIDNum = "";
            scanName = "";
            enabledCommit();
        } else {
            imgIdcardBack.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgBackWatermark.setVisibility(View.GONE);
            rlCenter2.setVisibility(View.VISIBLE);
            imgDeletBackPhoto.setVisibility(View.GONE);
            nationalpath = "";
            scanNational = "";
            enabledCommit();
        }
    }

    public static final int Face_Code     = 1001;
    public static final int National_Code = 1002;

    //拍照
    //    @OnClick({R.id.img_person_c, R.id.img_idcard_back_c})
    //    public void useCamera(View view) {
    //
    //        if (view.getId() == R.id.img_person_c) {
    //            //            carmeraTag = 0;
    //            OcrCameraActivity.open(this, IDCardEnum.FaceEmblem, Face_Code);
    //        } else {
    //            //            carmeraTag = 1;
    //            OcrCameraActivity.open(this, IDCardEnum.NationalEmblem, National_Code);
    //        }
    //        //        if (dialogIdcardAccept != null) {
    //        //            dialogIdcardAccept.show();
    //        //        }
    //
    //    }

    //提交
    @SuppressLint("CheckResult")
    @OnClick(R.id.idcard_upload_commit)
    public void commit() {

        //        Intent intent =new Intent(UploadIdCardPhotoActivity.this ,IdfCompleteActivity.class);
        //        intent.putExtra("name",tvName.getText().toString());
        //        intent.putExtra("idcard",true);
        //        startActivity(intent);
        //        localStringBuffer.append("姓名：").append(identityInfo.getName()).append("\n");
        //        localStringBuffer.append("身份号码：").append(identityInfo.getCertid()).append("\n");
        //        localStringBuffer.append("性别：").append(identityInfo.getSex()).append("\n");
        //        localStringBuffer.append("民族：").append(identityInfo.getFork()).append("\n");
        //        localStringBuffer.append("出生：").append(identityInfo.getBirthday()).append("\n");
        //        localStringBuffer.append("住址：").append(identityInfo.getAddress()).append("\n");
        //        localStringBuffer.append("签发机关：").append(identityInfo.getIssue_authority()).append("\n");
        //        localStringBuffer.append("有效期限：").append(identityInfo.getVaild_priod()).append("\n");
        //        localStringBuffer.append(identityInfo.getType() == IDCardEnum.FaceEmblem ? "人像面" : "国徽面").append("\n");
        //        if (identityInfo == null) {
        //            showToast("请上传身份证");
        //            return;
        //        }
        //        String certid = identityInfo.getCertid();
        //        String cardName = identityInfo.getName();
        if (TextUtils.isEmpty(facepath)) {
            showToast("请上传身份证人像页照片");
            return;
        }
        if (TextUtils.isEmpty(nationalpath)) {
            showToast("请上传国徽页照片");
            return;
        }
        if (!realName.equals(scanName)) {
            showToast("您上传的身份证和您填写的姓名不一致");
            return;
        }
        LogUtils.e("姓名====" + scanName + "---身份号码--" + scanIDNum);
        if (!idNum.equals(scanIDNum)) {
            showToast("您上传的身份证和您填写的身份证号不一致");
            return;
        }
        //        String issue_authority = identityInfo.getIssue_authority();
        if (!scanNational.contains("公安局")) {
            showToast("您的身份证签发机关不正确");
            return;
        }
        if (!TextUtils.isEmpty(vaildPriod)) {
            String[] split = vaildPriod.split("-");
            String endDateStr = split[1];
            LogUtils.e(endDateStr);
            if (!endDateStr.equals("长期")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

                try {
                    Date endDate = format.parse(endDateStr);
                    long time = endDate.getTime();
                    long currentTimeMillis = System.currentTimeMillis();
//                    currentTimeMillis = 2127916800000L;
                    if (currentTimeMillis > time) {
                        showToast("您的身份证已过期");
                        return;
                    }
                    LogUtils.e(time + "---currentTimeMillis---" + currentTimeMillis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
//        if (true) {
//            return;
//        }
        File faceFile = new File(facepath);
        File nationalFile = new File(nationalpath);
        dialog.show();
        ObservableSource<UpdateImageResultInfo> o1 = getUpdateImageResultInfoObservableSource(faceFile);
        ObservableSource<UpdateImageResultInfo> o2 = getUpdateImageResultInfoObservableSource(nationalFile);


        Observable.zip(o1, o2, new BiFunction<UpdateImageResultInfo, UpdateImageResultInfo, ImgId>() {
            @Override
            public ImgId apply(UpdateImageResultInfo resultInfo, UpdateImageResultInfo resultInfo2) throws Exception {
                ImgId imgId = new ImgId();
                imgId.faceImgId = resultInfo.message.id;
                imgId.nationalImgId = resultInfo2.message.id;
                imgId.idNum = scanIDNum;
                imgId.name = scanName;
                //                ArrayList<UpdateImageResultInfo> infos = new ArrayList<>();
                //                infos.add(resultInfo);
                //                infos.add(resultInfo2);
                return imgId;
            }
        }).flatMap(new Function<ImgId, ObservableSource<User>>() {
            @Override
            public ObservableSource<User> apply(ImgId imgId) throws Exception {
                //                addIdentity(imgId);
                Map<String, Object> map = new HashMap<>();
                map.put("name", imgId.name);
                map.put("number", imgId.idNum);
                map.put("front", imgId.faceImgId);
                map.put("back", imgId.nationalImgId);
                return ClientFactory.def(UserService.class).addIdentity(map);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        dialog.dismiss();
                        LogUtils.e("成功后返回--" + user);
                        if (user.code == 0) {
                            UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                            showToast("上传成功");
                            info.identityAuth = 2;
                            SharepUtil.putByBean(GlobalKey.USER_INFO, info);
                            Intent intent = new Intent(UploadIdCardPhotoActivity2.this, FaceAuthSettingActivity.class);
                            intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ID_Card);
                            intent.putExtra("hideName", hideName);
                            SharepUtil.put(GlobalKey.ID_CARD_NAME, hideName);
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
                        dialog.dismiss();
                        showToast("上传失败");
                        LogUtils.e(throwable);
                    }
                });


        //        Observable.just(faceFile, nationalFile)
        //                .flatMap(new Function<File, ObservableSource<UpdateImageResultInfo>>() {
        //                    @Override
        //                    public ObservableSource<UpdateImageResultInfo> apply(File file) throws Exception {
        //                        return getUpdateImageResultInfoObservableSource(file);
        //                    }
        //                })
        //                .subscribeOn(Schedulers.newThread())
        //                .observeOn(AndroidSchedulers.mainThread())
        //
        //                .subscribe(new Consumer<UpdateImageResultInfo>() {
        //                    @Override
        //                    public void accept(UpdateImageResultInfo resultInfo) throws Exception {
        //                        ToastUtils.showShort("上传成功");
        //                        dialog.dismiss();
        //                        LogUtils.e(resultInfo.toString());
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //
        //                    }
        //                });
    }


    public static class ImgId {
        public int    faceImgId;
        public int    nationalImgId;
        public String idNum;
        public String name;

        @Override
        public String toString() {
            return "ImgId{" +
                    "faceImgId=" + faceImgId +
                    ", nationalImgId=" + nationalImgId +
                    ", idNum='" + idNum + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    //    private void doCarmera() {
    //
    //
    //        imageUri = Uri.fromFile(fileUri);
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //            //通过FileProvider创建一个content类型的Uri
    //            imageUri = FileProvider.getUriForFile(UploadIdCardPhotoActivity2.this, "com.carlt.autogo.fileprovider", fileUri);
    //        }
    //        PhotoUtils.takePicture(UploadIdCardPhotoActivity2.this, imageUri, CODE_CAMERA_REQUEST);
    //    }
    //
    //    public void doPhoto() {
    //        PhotoUtils.openPic(UploadIdCardPhotoActivity2.this, CODE_GALLERY_REQUEST);
    //
    //    }

    @OnClick({R.id.ivPerson, R.id.ivIDBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPerson:
                OcrCameraActivity.open(this, IDCardEnum.FaceEmblem, Face_Code);
                //                showToast("正面");
                break;
            case R.id.ivIDBack:
                OcrCameraActivity.open(this, IDCardEnum.NationalEmblem, National_Code);
                //                showToast("国徽");
                break;
        }
    }

    //    private IdentityInfo identityInfo;
    String facepath;
    String nationalpath;
    String scanIDNum;
    String scanName;
    String scanNational;
    String vaildPriod;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            IdentityInfo identityInfo = (IdentityInfo) data.getSerializableExtra(OcrConfig.OCR_IDENTITYINFO);
            String certid = identityInfo.getCertid();
            if (certid != null) {
                scanIDNum = certid;
            }
            String name1 = identityInfo.getName();
            if (name1 != null) {
                scanName = name1;
            }
            String issue_authority = identityInfo.getIssue_authority();
            if (issue_authority != null) {
                scanNational = issue_authority;
            }
            //有效期
            String vaild_priod = identityInfo.getVaild_priod();
            if (vaild_priod != null) {
                LogUtils.e(vaild_priod);
                vaildPriod = vaild_priod;
            }
            switch (requestCode) {
                case Face_Code:
                    facepath = data.getStringExtra(OcrConfig.OCR_PHOTO_PATH);
                    Bitmap bitmap = creatWaterMarkBitmap(BitmapFactory.decodeFile(facepath));
                    imgPerson.setImageBitmap(bitmap);

                    rlCenter1.setVisibility(View.GONE);
                    imgDeletPersonPhoto.setVisibility(View.VISIBLE);

                    enabledCommit();
                    break;
                case National_Code:
                    nationalpath = data.getStringExtra(OcrConfig.OCR_PHOTO_PATH);
                    Bitmap bitmap1 = creatWaterMarkBitmap(BitmapFactory.decodeFile(nationalpath));
                    imgIdcardBack.setImageBitmap(bitmap1);

                    rlCenter2.setVisibility(View.GONE);
                    imgDeletBackPhoto.setVisibility(View.VISIBLE);

                    enabledCommit();
                    break;
                default:
            }
        }
    }

    //    @SuppressLint("CheckResult")
    //    public void setPicToView(final Bitmap picToView) {
    //        dialog.show();
    //        Disposable disposableImage = Observable.create(new ObservableOnSubscribe<Bitmap>() {
    //            @Override
    //            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
    //                emitter.onNext(picToView);
    //            }
    //        })
    //                .map(new Function<Bitmap, Bitmap>() {
    //                    @Override
    //                    public Bitmap apply(Bitmap bitmap) throws Exception {
    //                        return creatWaterMarkBitmap(bitmap);
    //                    }
    //                })
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .map(new Function<Bitmap, Bitmap>() {
    //                    @Override
    //                    public Bitmap apply(Bitmap bitmap) throws Exception {
    //                        showImg(bitmap);
    //                        return bitmap;
    //                    }
    //                })
    //                .observeOn(Schedulers.newThread())
    //                .map(new Function<Bitmap, File>() {
    //                    @Override
    //                    public File apply(Bitmap bitmap) throws Exception {
    //                        File filePic = saveWaterMarkBitmap(bitmap);
    //                        if (filePic == null)
    //                            return null;
    //                        return filePic;
    //                    }
    //                })
    //                .flatMap(new Function<File, ObservableSource<UpdateImageResultInfo>>() {
    //                    @Override
    //                    public ObservableSource<UpdateImageResultInfo> apply(File file) throws Exception {
    //                        return getUpdateImageResultInfoObservableSource(file);
    //                    }
    //                })
    //                .subscribeOn(Schedulers.newThread())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribe(new Consumer<UpdateImageResultInfo>() {
    //                               @Override
    //                               public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
    //                                   ToastUtils.showShort("上传成功");
    //                                   dialog.dismiss();
    //                                   LogUtils.e(updateImageResultInfo.toString());
    //                               }
    //                           }
    //                        , new Consumer<Throwable>() {
    //                            @Override
    //                            public void accept(Throwable throwable) throws Exception {
    //                                dialog.dismiss();
    //                                ToastUtils.showShort("上传失败");
    //                                LogUtils.e(throwable.toString());
    //                            }
    //                        });
    //        disposables.add(disposableImage);
    //    }

    /**
     * @param file
     *         要上传的图片文件
     * @return 返回请求图片上传接口的回调结果
     */
    private ObservableSource<UpdateImageResultInfo> getUpdateImageResultInfoObservableSource(File file) {
        RequestBody requestBody = MultipartBodyBuilder
                .addFormDataPart("type", "autogo/face")
                .addFormDataPart("fileOwner", "face")
                .addFormDataPart("uid", "9999999999")
                .addFormDataPart("name", "faceImage")
                .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        //图片上传
        return ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody);
    }

    /**
     * 都为示意图按钮不可点击
     */
    private void enabledCommit() {
        if (TextUtils.isEmpty(facepath) && TextUtils.isEmpty(nationalpath)) {
            idcardUploadCommit.setEnabled(false);
        } else {
            idcardUploadCommit.setEnabled(true);
        }
    }


    //    /**
    //     * @param bitmap
    //     *         水印bitmap
    //     * @return 返回水印图片保存地址
    //     */
    //    @Nullable
    //    private File saveWaterMarkBitmap(Bitmap bitmap) {
    //        File filePic;
    //        if (carmeraTag == 0) {
    //            filePic = (fileCropUriface);
    //        } else {
    //            filePic = (fileCropUriBalce);
    //        }
    //        try {
    //            FileOutputStream fos = new FileOutputStream(filePic);
    //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
    //            fos.flush();
    //            fos.close();
    //        } catch (IOException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //            return null;
    //        }
    //        return filePic;
    //    }

    //    private void showImg(Bitmap bitmap) {
    //        if (carmeraTag == 0) {
    //            imgPerson.setImageBitmap(bitmap);
    //            rlCenter1.setVisibility(View.GONE);
    //            imgDeletPersonPhoto.setVisibility(View.VISIBLE);
    //        }
    //        if (carmeraTag == 1) {
    //            imgIdcardBack.setImageBitmap(bitmap);
    //            // imgBackWatermark.setVisibility(View.VISIBLE);
    //            rlCenter2.setVisibility(View.GONE);
    //            imgDeletBackPhoto.setVisibility(View.VISIBLE);
    //        }
    //    }

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
        //        LogUtils.e(secondBitmap.getWidth() + "========" + secondBitmap.getHeight());
        LogUtils.e(firstBitmap.getWidth() + "========" + firstBitmap.getHeight());
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        //  m.setScale(w / imgPerson.getWidth(), h / imgPerson.getHeight());
        //  m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        RectF rectF = new RectF(w / 5, h / 4, w / 5 * 4, h / 4 * 3);
        canvas.drawBitmap(firstBitmap, 0, 0, null);
        //        canvas.drawBitmap(secondBitmap, (w - secondBitmap.getWidth()) / 2, (h - secondBitmap.getHeight()) / 2, null);
        canvas.drawBitmap(secondBitmap, null, rectF, null);
        firstBitmap.recycle();
        return b;
    }


}
