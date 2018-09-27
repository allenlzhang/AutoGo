package com.carlt.autogo.view.activity.user.accept;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.PhotoUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author wsq
 * @time 10:33  2018/9/12/012
 * @describe  上传身份证照片 页面
 */
public class UploadIdCardPhotoActivity extends BaseMvpActivity {

    String[] mPermission = {Manifest.permission.CAMERA ,Manifest.permission.READ_EXTERNAL_STORAGE };
    @BindView(R.id.tv_name)TextView tvName;
    @BindView(R.id.img_delet_person_photo)ImageView imgDeletPersonPhoto;
    @BindView(R.id.img_person)ImageView imgPerson;
    @BindView(R.id.img_person_watermark)ImageView imgPersonWatermark;
    @BindView(R.id.img_person_c)ImageView imgPersonCamera;
    @BindView(R.id.img_delet_back_photo)ImageView imgDeletBackPhoto;
    @BindView(R.id.img_idcard_back_c)ImageView imgIdcardBackCamera;
    @BindView(R.id.img_idcard_back)ImageView imgIdcardBack;
    @BindView(R.id.img_back_watermark)ImageView imgBackWatermark;
    @BindView(R.id.idcard_upload_commit)Button idcardUploadCommit;
    @BindView(R.id.rl_center)RelativeLayout rlCenter1;
    @BindView(R.id.rl_center2)RelativeLayout rlCenter2;


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File AotugoImage =new File(Environment.getExternalStorageDirectory().getPath()+"/Aotugo/Image");
    private File fileUri = new File(AotugoImage + "/photo.jpg");
    private File fileCropUriface = new File(AotugoImage + "/crop_photo_face.jpg");
    private File fileCropUriBalce = new File(AotugoImage + "/crop_photo_back.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private String name;
    private String idCardNum;

    private int carmeraTag = -1;

    DialogIdcardAccept dialogIdcardAccept;

    MultipartBody.Builder MultipartBodyBuilder =   new MultipartBody.Builder().setType(MultipartBody.FORM);

    @Override
    protected int getContentView() {
        return R.layout.activity_upload_id_card_photo;
    }

    @Override
    public void init() {
        setTitleText("上传身份证件");
        name = getIntent().getStringExtra("name");
        idCardNum = getIntent().getStringExtra("idcard");
        tvName.setText(name + "\t" + idCardNum);
        dialogIdcardAccept = new DialogIdcardAccept(this);
        if(  !AotugoImage.exists()){
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
    @OnClick({R.id.img_delet_person_photo ,R.id.img_delet_back_photo })
    public void deletImage(View view){
        if(view.getId() == R.id.img_delet_person_photo){
            imgPerson.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgPersonWatermark.setVisibility(View.GONE);
            rlCenter1.setVisibility(View.VISIBLE);
            imgDeletPersonPhoto.setVisibility(View.GONE);

        }else {
            imgIdcardBack.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgBackWatermark.setVisibility(View.GONE);
            rlCenter2.setVisibility(View.VISIBLE);
            imgDeletBackPhoto.setVisibility(View.GONE);
        }
    }

    //拍照
    @OnClick({R.id.img_person_c ,R.id.img_idcard_back_c})
    public void useCamera(View view){

        if(view.getId() == R.id.img_person_c){
            carmeraTag = 0;
        }else {
            carmeraTag = 1;
        }
        if(dialogIdcardAccept != null){
            dialogIdcardAccept.show();
        }

    }

    //提交
    @SuppressLint("CheckResult")
    @OnClick(R.id.idcard_upload_commit)
    public void commit(){

//        Intent intent =new Intent(UploadIdCardPhotoActivity.this ,IdfCompleteActivity.class);
//        intent.putExtra("name",tvName.getText().toString());
//        intent.putExtra("idcard",true);
//        startActivity(intent);

    }


    private void doCarmera() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(UploadIdCardPhotoActivity.this, "com.carlt.autogo.fileprovider", fileUri);
        }
        PhotoUtils.takePicture(UploadIdCardPhotoActivity.this, imageUri, CODE_CAMERA_REQUEST);
    }

    public void doPhoto(){
        PhotoUtils.openPic(UploadIdCardPhotoActivity.this, CODE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 310;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    if(carmeraTag == 0){
                        cropImageUri = Uri.fromFile(fileCropUriface);
                    }else {
                        cropImageUri = Uri.fromFile(fileCropUriBalce);
                    }

                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调 ;
                    if(carmeraTag == 0){
                        cropImageUri = Uri.fromFile(fileCropUriface);
                    }else {
                        cropImageUri = Uri.fromFile(fileCropUriBalce);
                    }
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    }

                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    setPicToView(bitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("CheckResult")
    public void setPicToView(final Bitmap picToView) {
        dialog.show();
     Disposable disposableImage = Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                emitter.onNext(picToView);
            }
        })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        return creatWaterMarkBitmap(bitmap);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap bitmap) throws Exception {
                        showImg(bitmap);
                        return bitmap;
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Function<Bitmap, File>() {
                    @Override
                    public File apply(Bitmap bitmap) throws Exception {
                        File filePic = saveWaterMarkBitmap(bitmap);
                        if (filePic == null) return null;
                        return filePic;
                    }
                })
                .flatMap(new Function<File, ObservableSource<UpdateImageResultInfo>>() {
                    @Override
                    public ObservableSource<UpdateImageResultInfo> apply(File file) throws Exception {
                        return getUpdateImageResultInfoObservableSource(file);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateImageResultInfo>() {
                               @Override
                               public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                                   ToastUtils.showShort("上传成功");
                                   dialog.dismiss();
                                   LogUtils.e(updateImageResultInfo.toString());
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                ToastUtils.showShort("上传失败");
                                LogUtils.e(throwable.toString());
                            }
                        });
        disposables.add(disposableImage);
    }

    /**
     * @param file 要上传的图片文件
     * @return 返回请求图片上传接口的回调结果
     */
    private ObservableSource<UpdateImageResultInfo> getUpdateImageResultInfoObservableSource(File file) {
        RequestBody requestBody = MultipartBodyBuilder
                .addFormDataPart("type", "autogo/face")
                .addFormDataPart("fileOwner","face")
                .addFormDataPart("uid", "9999999999")
                .addFormDataPart("name", "faceImage")
                .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        //图片上传
        return ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody);
    }


    /**
     * @param bitmap 水印bitmap
     * @return 返回水印图片保存地址
     */
    @Nullable
    private File saveWaterMarkBitmap(Bitmap bitmap) {
        File filePic;
        if(carmeraTag == 0){
            filePic = (fileCropUriface);
        }else {
            filePic = (fileCropUriBalce);
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
        if(carmeraTag == 0){
            imgPerson.setImageBitmap(bitmap);
            rlCenter1.setVisibility(View.GONE);
            imgDeletPersonPhoto.setVisibility(View.VISIBLE);
        }
        if(carmeraTag == 1){
            imgIdcardBack.setImageBitmap(bitmap);
            // imgBackWatermark.setVisibility(View.VISIBLE);
            rlCenter2.setVisibility(View.GONE);
            imgDeletBackPhoto.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param bitmap
     * @return
     *
     * 利用canvas 叠加两种图片(加水印)
     */
    @NonNull
    private Bitmap creatWaterMarkBitmap(Bitmap bitmap) {
        Bitmap firstBitmap = bitmap ;
        Bitmap  secondBitmap = ( (BitmapDrawable)imgPersonWatermark.getDrawable()).getBitmap();
        Bitmap b = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),firstBitmap.getConfig());
        Canvas canvas = new Canvas(b);
        float w = b.getWidth();
        float h = b.getHeight();
        LogUtils.e(secondBitmap.getWidth() + "========" + secondBitmap.getHeight());
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
      //  m.setScale(w / imgPerson.getWidth(), h / imgPerson.getHeight());
      //  m.setScale(w / secondBitmap.getWidth(), h / secondBitmap.getHeight());
        canvas.drawBitmap(firstBitmap, 0,0, null);
        canvas.drawBitmap(secondBitmap, (w-secondBitmap.getWidth())/2,(h-secondBitmap.getHeight())/2, null);
        firstBitmap.recycle();
        return b;
    }
}
