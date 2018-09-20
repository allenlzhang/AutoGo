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
import com.carlt.autogo.utils.PhotoUtils;
import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author wsq
 * @time 10:33  2018/9/12/012
 * @describe  上传身份证照片 页面
 */
public class UploadIdCardPhotoActivity extends BaseMvpActivity {
    private int  requestCodeCarmera = 1001;
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
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private String name;
    private String idCardNum;

    private int carmeraTag = -1;
    private  int carmeraOrPhoto = -1 ;

    DialogIdcardAccept dialogIdcardAccept;
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
        dialogIdcardAccept.setListner(new DialogIdcardAccept.ItemOnclickListner() {
            @Override
            public void byCermera() {
                carmeraOrPhoto =0 ;
                checkPermission();
                doCarmera();
            }

            @Override
            public void byAblum() {
                carmeraOrPhoto = 1;
                checkPermission();
                doPhoto();
            }
        });
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(UploadIdCardPhotoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(UploadIdCardPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                )
        {
            ActivityCompat.requestPermissions(UploadIdCardPhotoActivity.this, mPermission, requestCodeCarmera);
            return;

        }
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
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("11");
            }
        }).delay(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Intent intent =new Intent(UploadIdCardPhotoActivity.this ,IdfCompleteActivity.class);
                        intent.putExtra("name",tvName.getText().toString());
                        intent.putExtra("idcard",true);
                        startActivity(intent);
                    }
                });

    }

    //6.0 权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == requestCodeCarmera) {
          boolean denied = false;

          for(int i=1 ; i<grantResults.length ;i++){
              if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                  denied = true;
                  ToastUtils.showShort("部分权限获取失败，正常功能受到影响");
              }
          }

          if(!denied){
              if(carmeraOrPhoto == 0){
                  doCarmera();
              }else if(carmeraOrPhoto == 1){
                  doPhoto();
              }
          }

        }
    }

    private void doCarmera() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(UploadIdCardPhotoActivity.this, "com.carlt.autogo.fileprovider", fileUri);
        PhotoUtils.takePicture(UploadIdCardPhotoActivity.this, imageUri, CODE_CAMERA_REQUEST);
    }

    public void doPhoto(){
        PhotoUtils.openPic(UploadIdCardPhotoActivity.this, CODE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    setPicToView(bitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPicToView(Bitmap picToView) {

        Bitmap  firstBitmap = picToView;
        Bitmap  secondBitmap = ( (BitmapDrawable)imgPersonWatermark.getDrawable()).getBitmap();
        LogUtils.e(secondBitmap.getRowBytes() * secondBitmap.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        float w = firstBitmap.getWidth();
        float h = firstBitmap.getHeight();
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        m.setScale(w / imgPerson.getWidth(), h / imgPerson.getHeight());
        canvas.drawBitmap(firstBitmap, 0,0, null);
        canvas.drawBitmap(secondBitmap, m, null);

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
}
