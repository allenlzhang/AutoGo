package com.carlt.autogo.view.activity.car;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;
import com.carlt.autogo.utils.PhotoUtils;
import com.carlt.autogo.view.activity.user.accept.UploadIdCardPhotoActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class CarCertificationActivity extends BaseMvpActivity {

    String[] mPermission = {Manifest.permission.CAMERA ,Manifest.permission.READ_EXTERNAL_STORAGE };
    @BindView(R.id.ll_vehicle_certification_add)
    LinearLayout llVehicleCertificationAdd;
    @BindView(R.id.edit_vehicle_certification_vin)
    EditText editVehicleCertificationVin;
    @BindView(R.id.edit_vehicle_certification_engine_num)
    EditText editVehicleCertificationEngineNum;
    @BindView(R.id.iv_vehicle_certification)
    ImageView ivVehicleCertification;
    @BindView(R.id.btn_vehicle_certification)
    Button btnVehicleCertification;
    int type = 0;
    @BindView(R.id.ll_vehicle_certification)
    LinearLayout llVehicleCertification;


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    private File AotugoImage =new File(Environment.getExternalStorageDirectory().getPath()+"/Aotugo/Image");
    private File fileUri = new File(AotugoImage + "/certificationPhoto.jpg");
    private File fileCropUri = new File(AotugoImage + "/cropCertificationPhoto.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_car_certification);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_vehicle_certification;
    }

    @Override
    public void init() {
        setTitleText("车辆认证");
    }

    private void showPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_camera_popup, null, false);
        TextView mTxtCamera = view.findViewById(R.id.tvCameraPopup);
        TextView mTxtAlbum = view.findViewById(R.id.tvAlbumPopup);
        TextView mTxtCancel = view.findViewById(R.id.tvCancelPopup);
        WindowManager manager = this.getWindowManager();
        popupWindow = new PopupWindow(view, manager.getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(llVehicleCertification, Gravity.BOTTOM,0,0);
        popupWindow.setTouchable(false);
        mTxtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions(mPermission, new PremissoinLisniter() {
                    @Override
                    public void createred() {
                        doCamera();
                    }
                });
            }
        });
        mTxtAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions(mPermission, new PremissoinLisniter() {
                    @Override
                    public void createred() {
                        doAlbum();
                    }
                });
            }
        });
        mTxtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void doCamera() {
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", fileUri);
        }
        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
    }

    public void doAlbum(){
        PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);

    }


    @OnClick({R.id.btn_vehicle_certification, R.id.ll_vehicle_certification_add,R.id.iv_vehicle_certification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_vehicle_certification:
                startActivity(new Intent(this, CarActivateActivity.class));
                break;
            case R.id.ll_vehicle_certification_add:

                if (type == 0) {
                    type = 1;
                    startActivity(BrandActivity.class, false);
                } else if (type == 1) {
                    type = 2;
                    startActivity(ModelActivity.class, false);
                } else if (type == 2) {
                    type = 0;
                    startActivity(BrandCarActivity.class, false);
                }
                break;
            case R.id.iv_vehicle_certification:
                showPopup();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 310;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                        cropImageUri = Uri.fromFile(fileCropUri);

                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调 ;
                        cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    }

                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1.5, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    setPicToView(bitmap);
                    ivVehicleCertification.setImageBitmap(bitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
