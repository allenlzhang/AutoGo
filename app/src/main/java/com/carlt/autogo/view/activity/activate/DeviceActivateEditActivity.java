package com.carlt.autogo.view.activity.activate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.presenter.activite.DeviceActivatePresenter;
import com.carlt.autogo.presenter.activite.IDeviceActivateView;
import com.carlt.autogo.presenter.car.CarDetailsPresenter;
import com.carlt.autogo.presenter.car.ICarDetailsView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/12/3.
 * 设备激活
 */
@CreatePresenter(presenter = {DeviceActivatePresenter.class, CarDetailsPresenter.class})
public class DeviceActivateEditActivity extends BaseMvpActivity implements IDeviceActivateView, ICarDetailsView {
    @BindView(R.id.etPin)
    EditText etPin; //6位或者8位
    @BindView(R.id.etDeviceNum)
    EditText etDeviceNum;//16位
    @BindView(R.id.btnActivate)
    Button   btnActivate;
    private int    carId;
    private int    withTbox;
    private String deviceNum;
    @PresenterVariable
    DeviceActivatePresenter mActivatePresenter;
    @PresenterVariable
    CarDetailsPresenter     mCarDetailsPresenter;
    private String pinCode;

    @Override
    protected int getContentView() {
        return R.layout.activity_activate_edit;
    }

    @Override
    public void init() {
        setTitleText("设备激活");
        etPin.setTransformationMethod(method);
        etDeviceNum.setTransformationMethod(method);
        carId = getIntent().getIntExtra("carId", 0);
        withTbox = getIntent().getIntExtra("withTbox", 0);
        LogUtils.e("withTbox----" + withTbox);
        if (withTbox == 1) {
            //前装
            etDeviceNum.setVisibility(View.GONE);
        } else if (withTbox == 2) {
            //后装
            etPin.setVisibility(View.GONE);
            etDeviceNum.setVisibility(View.VISIBLE);
        } else {
            etDeviceNum.setVisibility(View.GONE);
        }
        mCarDetailsPresenter.ClientGetCarInfo(carId);
    }

    @Override
    public void getCarInfoSuccess(CarInfo carInfo) {
        String deviceNum = carInfo.deviceNum;
        if (!TextUtils.isEmpty(deviceNum)) {
            etDeviceNum.setEnabled(false);
            etDeviceNum.setText(deviceNum);
        }
    }

    @Override
    public void modifySuccess(BaseError baseError) {

    }

    @Override
    public void cancelAuthSuccess(BaseError baseError) {

    }

    @OnClick(R.id.btnActivate)
    public void onViewClicked() {
        if (etPin.getVisibility() == View.VISIBLE) {
            pinCode = etPin.getText().toString().trim();
            if (TextUtils.isEmpty(pinCode)) {
                ToastUtils.showShort("请输入PIN码");
                return;
            }
            if (!checkTxt(pinCode) || !(pinCode.length() == 8 || pinCode.length() == 6)) {
                ToastUtils.showShort("PIN码输入有误");
                return;
            }
        }

        if (etDeviceNum.getVisibility() == View.VISIBLE) {
            deviceNum = etDeviceNum.getText().toString().trim();
            if (TextUtils.isEmpty(deviceNum)) {
                ToastUtils.showShort("请输入设备号");
                return;
            }
            if (!checkTxt(deviceNum) || deviceNum.length() != 16) {
                ToastUtils.showShort("设备号输入有误");
                return;
            }
        }

        //        if (!checkTxt(etDeviceNum.getText().toString())||etDeviceNum.getText().length() != 16) {
        //            ToastUtils.showShort("设备号输入有误");
        //
        //        }
        //        if (!checkTxt(etPin.getText().toString()) || etPin.getText().length() != 8) {
        //            ToastUtils.showShort("PIN码输入有误");
        //            return;
        //        }

        deviceActive();

    }

    @SuppressLint("CheckResult")
    private void deviceActive() {
        Map<String, Object> params = new HashMap<>();
        params.put("carID", carId);
        if (pinCode != null) {
            params.put("PIN", pinCode.toUpperCase());
        }

        if (deviceNum != null) {
            params.put("deviceNum", deviceNum.toUpperCase());
        }
        mActivatePresenter.deviceActive(params);
    }

    ReplacementTransformationMethod method = new ReplacementTransformationMethod() {
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
    };

    public boolean checkTxt(String str) {
        String strPattern = "^[A-Za-z0-9]*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    public void activeSuccess(BaseError baseError) {
        if (baseError != null) {
            if (!TextUtils.isEmpty(baseError.msg)) {
                ToastUtils.showShort(baseError.msg);
            } else {
                ToastUtils.showShort("开始激活");
                Intent intent = new Intent(DeviceActivateEditActivity.this, ActivateStepActivity.class);
                intent.putExtra("carId", carId);
                intent.putExtra("withTbox", withTbox);
                startActivity(intent);
                finish();
            }
        }
    }


}
