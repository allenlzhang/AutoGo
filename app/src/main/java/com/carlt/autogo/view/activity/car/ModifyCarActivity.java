package com.carlt.autogo.view.activity.car;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.BrandInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.PhotoUtils;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.gildutils.GlideCircleTransform;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

// 车辆认证
public class ModifyCarActivity extends BaseMvpActivity {

    String[] mPermission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    @BindView(R.id.ll_vehicle_certification_add)
    LinearLayout llVehicleCertificationAdd;
    @BindView(R.id.edit_vehicle_certification_vin)
    EditText     editVehicleCertificationVin;
    @BindView(R.id.edit_vehicle_certification_engine_num)
    EditText     editVehicleCertificationEngineNum;
    @BindView(R.id.iv_vehicle_certification)
    ImageView    ivVehicleCertification;
    @BindView(R.id.btn_vehicle_certification)
    Button       btnVehicleCertification;
    @BindView(R.id.ll_vehicle_certification)
    LinearLayout llVehicleCertification;
    @BindView(R.id.txt_vehicle_certification_add)
    TextView     txtVehicleCertificationAdd;

    private static final int CODE_GALLERY_REQUEST = 0xb0;
    private static final int CODE_CAMERA_REQUEST  = 0xb1;
    private static final int CODE_RESULT_REQUEST  = 0xb2;
    private static final int CODE_ADDCAR_REQUEST  = 0xb3;


    private File AotugoImage = new File(Environment.getExternalStorageDirectory().getPath() + "/Aotugo/Image");
    private File fileUri     = new File(AotugoImage + "/certificationPhoto.jpg");
    private File fileCropUri = new File(AotugoImage + "/cropCertificationPhoto.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    PopupWindow popupWindow;

    MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

    private int avatarId; //车辆凭证附件ID

    private CarBrandInfo.DataBean dataBean; //车款信息
    private int                   withTbox;
    private int                   carId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_car;
    }

    @Override
    public void init() {
        setTitleText("车辆认证");
        //        initPopup();
        editVehicleCertificationVin.setTransformationMethod(new ReplacementTransformationMethod() {
            @Override
            protected char[] getOriginal() {
                char[] small = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                return small;
            }

            @Override
            protected char[] getReplacement() {
                char[] big = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return big;
            }
        });

        getCarInfo();
    }

    /**
     * 获取车辆信息
     */
    @SuppressLint("CheckResult")
    private void getCarInfo() {
        dialog.show();
        carId = getIntent().getIntExtra("carId", 0);
        withTbox = getIntent().getIntExtra("withTbox", 0);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", carId);
        ClientFactory.def(CarService.class).getCarInfo(map)
                .subscribe(new Consumer<CarInfo>() {
                    @Override
                    public void accept(CarInfo carInfo) {
                        dialog.dismiss();
                        if (carInfo.err != null) {
                            ToastUtils.showShort(carInfo.err.msg);
                        } else {
                            setData(carInfo);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    private void setData(CarInfo carInfo) {
        txtVehicleCertificationAdd.setText(carInfo.carName);
        editVehicleCertificationVin.setText(carInfo.standCarNo);
        Glide.with(this)
                .load(carInfo.buyCarImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                //                .error(R.mipmap.iv_def_head)
                .transform(new GlideCircleTransform(this))
                .into(ivVehicleCertification);
    }

    private void initPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_camera_popup, null, false);
        TextView mTxtCamera = view.findViewById(R.id.tvCameraPopup);
        TextView mTxtAlbum = view.findViewById(R.id.tvAlbumPopup);
        TextView mTxtCancel = view.findViewById(R.id.tvCancelPopup);
        WindowManager manager = this.getWindowManager();
        popupWindow = new PopupWindow(view, manager.getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.BottomDialogAnimation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
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

    /**
     * 屏幕透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 打开相机
     */
    private void doCamera() {
        popupWindow.dismiss();
        imageUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", fileUri);
        }
        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
    }

    /**
     * 打开相册
     */
    public void doAlbum() {
        popupWindow.dismiss();
        PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
    }


    @OnClick({R.id.btn_vehicle_certification, R.id.ll_vehicle_certification_add, R.id.iv_vehicle_certification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_vehicle_certification:
                //                startActivity(new Intent(this, CarActivateActivity.class));
                modifyCar();
                break;
            case R.id.ll_vehicle_certification_add:
                filter();
                break;
            case R.id.iv_vehicle_certification:
                //                if (popupWindow.isShowing()) {
                //                    popupWindow.dismiss();
                //                } else {
                //                    popupWindow.showAtLocation(llVehicleCertification, Gravity.BOTTOM, 0, 0);
                //                    backgroundAlpha(0.7f);
                //                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void modifyCar() {
        Map<String, Object> map = new HashMap<>();
        if (TextUtils.isEmpty(txtVehicleCertificationAdd.getText())) {
            ToastUtils.showShort("请添加爱车");
            return;
        } else {
            map.put("brandCarId", dataBean.id);
        }
        if (TextUtils.isEmpty(editVehicleCertificationVin.getText().toString().toUpperCase())) {
            ToastUtils.showShort("请输入车辆车架号");
            return;
        } else {
            map.put("vin", editVehicleCertificationVin.getText().toString());
        }
        //        if (TextUtils.isEmpty(editVehicleCertificationEngineNum.getText())) {
        //            TextUtils.isEmpty("请输入车辆发动机号");
        //            return;
        //        } else {
        //            map.put("engineNum", editVehicleCertificationEngineNum.getText().toString());
        //        }
        //        if (avatarId == 0) {
        //            ToastUtils.showShort("请上传购车凭证");
        //            return;
        //        } else {
        //            map.put("certificateId", avatarId);
        //        }
        dialog.show();
        ClientFactory.def(CarService.class).modify(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError err) throws Exception {
                        dialog.dismiss();
                        if (!TextUtils.isEmpty(err.msg)) {
                            ToastUtils.showShort(err.msg);
                        } else {
                            ToastUtils.showShort("修改成功");
                            Intent intent = new Intent(ModifyCarActivity.this, DeviceActivateActivity.class);
                            intent.putExtra("carId", carId);
                            intent.putExtra("withTbox", withTbox);
                            startActivity(intent);
                            //                            ModifyCarActivity.this.setResult(RESULT_OK);
                            //                            ModifyCarActivity.this.finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);

                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, ivVehicleCertification.getWidth(), ivVehicleCertification.getHeight(), CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调 ;
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    }

                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, ivVehicleCertification.getWidth(), ivVehicleCertification.getHeight(), CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    updateImageFile(fileCropUri);
                    break;
                case CODE_ADDCAR_REQUEST:
                    dataBean = (CarBrandInfo.DataBean) data.getSerializableExtra("carName");
                    int modelId = data.getIntExtra("modelId", -1);
                    int Optionid = data.getIntExtra("Optionid", -1);
                    int brandId = data.getIntExtra("brandId", -1);
                    LogUtils.e(modelId + "----" + brandId + "----" + Optionid);
                    txtVehicleCertificationAdd.setText(dataBean.title);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 添加车辆
     */
    private void filter() {
        dialog.show();
        Gson gson = new Gson();
        OkGo.<String>post(GlobalUrl.TEST_BASE_URL + "BrandProduct/AutoFilter")
                .headers("Content-Type", "application/json")
                .headers("Carlt-Access-Id", GlobalKey.TEST_ACCESSID)
                .headers("Carlt-Token", SharepUtil.getPreferences().getString("token", ""))
                .upJson(gson.toJson(new HashMap<>()))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dialog.dismiss();
                        parseFilter(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                        LogUtils.e(response);
                    }


                });
    }

    private void parseFilter(String body) {
        int level = 0;
        if (body != null) {
            JSONObject object = null;
            try {
                object = new JSONObject(body);
                level = object.optInt("level", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            Intent intent = new Intent();
            if (level == 1) {
                BrandInfo info = gson.fromJson(body, BrandInfo.class);
                if (info.err != null) {
                    ToastUtils.showShort(info.err.msg);
                } else {
                    intent.setClass(ModifyCarActivity.this, BrandActivity.class);
                    intent.putExtra("brand", info);
                    startActivityForResult(intent, CODE_ADDCAR_REQUEST);
                }
            } else if (level == 2) {
                CarModelInfo info = gson.fromJson(body, CarModelInfo.class);
                if (info.err != null) {
                    ToastUtils.showShort(info.err.msg);
                } else {
                    intent.setClass(ModifyCarActivity.this, ModelActivity.class);
                    intent.putExtra("model", info);
                    startActivityForResult(intent, CODE_ADDCAR_REQUEST);
                }
            } else if (level == 3) {
                CarBrandInfo info = gson.fromJson(body, CarBrandInfo.class);
                if (info.err != null) {
                    ToastUtils.showShort(info.err.msg);
                } else {
                    intent.setClass(ModifyCarActivity.this, BrandCarActivity.class);
                    intent.putExtra("brandCar", info);
                    startActivityForResult(intent, CODE_ADDCAR_REQUEST);
                }
            } else {
                LogUtils.e("CarCertificationActivity---数据类型错误");
            }
        }
    }


    /**
     * 图像上传
     * @param file
     */
    @SuppressLint("CheckResult")
    private void updateImageFile(File file) {
        dialog.show();
        RequestBody requestBody = MultipartBodyBuilder
                .addFormDataPart("type", "autogo/face")
                .addFormDataPart("fileOwner", "face")
                .addFormDataPart("uid", "9999999999")
                .addFormDataPart("name", "faceImage")
                .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        //图片上传
        ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateImageResultInfo>() {
                    @Override
                    public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                        dialog.dismiss();
                        if (updateImageResultInfo.err != null) {
                            ToastUtils.showShort(updateImageResultInfo.err.msg);
                        } else {
                            if (updateImageResultInfo.message != null) {
                                avatarId = updateImageResultInfo.message.id;
                                Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, ModifyCarActivity.this);
                                ivVehicleCertification.setImageBitmap(bitmap);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }
}
