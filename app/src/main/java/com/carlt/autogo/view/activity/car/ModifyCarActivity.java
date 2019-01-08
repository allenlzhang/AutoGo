package com.carlt.autogo.view.activity.car;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.entry.car.BrandInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.presenter.car.CarCertificationPresenter;
import com.carlt.autogo.presenter.car.CarDetailsPresenter;
import com.carlt.autogo.presenter.car.ICarCertificationView;
import com.carlt.autogo.presenter.car.ICarDetailsView;
import com.carlt.autogo.view.activity.activate.DeviceActivateActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

// 车辆认证
@CreatePresenter(presenter = {CarCertificationPresenter.class, CarDetailsPresenter.class})
public class ModifyCarActivity extends BaseMvpActivity implements ICarCertificationView,ICarDetailsView{

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

    private static final int CODE_ADDCAR_REQUEST  = 0xb3;

    private CarBrandInfo.DataBean dataBean; //车款信息
    private int                   withTbox;
    private int                   carId;
    @PresenterVariable
    CarCertificationPresenter carCertificationPresenter;
    @PresenterVariable
    CarDetailsPresenter carDetailsPresenter;

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
        carId = getIntent().getIntExtra("carId", 0);
        withTbox = getIntent().getIntExtra("withTbox", 0);
        carDetailsPresenter.ClientGetCarInfo(carId);
    }

    private CarInfo mCarInfo;

    private void setData(CarInfo carInfo) {
        mCarInfo = carInfo;
        txtVehicleCertificationAdd.setText(carInfo.carName);
        editVehicleCertificationVin.setText(carInfo.vin);
    }

    @OnClick({R.id.btn_vehicle_certification, R.id.ll_vehicle_certification_add, R.id.iv_vehicle_certification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_vehicle_certification:
                modifyCar();
                break;
            case R.id.ll_vehicle_certification_add:
                carCertificationPresenter.filter();
                break;
            case R.id.iv_vehicle_certification:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void modifyCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", mCarInfo.id);
        if (TextUtils.isEmpty(txtVehicleCertificationAdd.getText())) {
            ToastUtils.showShort("请添加爱车");
            return;
        } else {
            if (dataBean == null) {
                showToast("你还没有更换车款");
                return;
            } else {
                map.put("brandCarId", dataBean.id);
            }

        }
        carDetailsPresenter.modify(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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

    @Override
    public void selectCarSuccess(String carData) {
        parseFilter(carData);
    }

    @Override
    public void updateImageFileSuccess(UpdateImageResultInfo updateImageResultInfo) {

    }

    @Override
    public void addCarSuccess(BaseError err) {

    }

    @Override
    public void getCarInfoSuccess(CarInfo carInfo) {
        if (carInfo.err != null) {
            ToastUtils.showShort(carInfo.err.msg);
        } else {
            setData(carInfo);
        }
    }

    @Override
    public void modifySuccess(BaseError baseError) {
        if (!TextUtils.isEmpty(baseError.msg)) {
            ToastUtils.showShort(baseError.msg);
        } else {
            ToastUtils.showShort("修改成功");
            Intent intent = new Intent(ModifyCarActivity.this, DeviceActivateActivity.class);
            intent.putExtra("carId", carId);
            intent.putExtra("withTbox", withTbox);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void cancelAuthSuccess(BaseError baseError) {

    }
}
