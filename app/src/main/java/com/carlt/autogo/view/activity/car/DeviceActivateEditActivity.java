package com.carlt.autogo.view.activity.car;

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
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.view.activity.activate.ActivateStepActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/12/3.
 * 设备激活
 */
public class DeviceActivateEditActivity extends BaseMvpActivity {
    @BindView(R.id.etPin)
    EditText etPin; //6位或者8位
    @BindView(R.id.etDeviceNum)
    EditText etDeviceNum;//16位
    @BindView(R.id.btnActivate)
    Button   btnActivate;
    private int    carId;
    private int    withTbox;
    private String deviceNum;

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
        if (withTbox == 1) {
            //前装
            etDeviceNum.setVisibility(View.GONE);
        } else {
            etDeviceNum.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnActivate)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPin.getText())) {
            ToastUtils.showShort("PIN码输入不能为空");
            return;
        }
        if (etDeviceNum.getVisibility() == View.VISIBLE) {
            deviceNum = etDeviceNum.getText().toString().trim();
            if (TextUtils.isEmpty(deviceNum)) {
                ToastUtils.showShort("设备号输入不能为空");
                return;
            }
        }

        deviceActive();

    }

    @SuppressLint("CheckResult")
    private void deviceActive() {
        dialog.show();
        Map<String, Object> params = new HashMap<>();
        params.put("carID", carId);
        params.put("PIN", etPin.getText().toString().toUpperCase());
        if (deviceNum != null) {
            params.put("deviceNum", deviceNum.toUpperCase());
        }
        ClientFactory.def(CarService.class).active(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError != null) {
                            if (!TextUtils.isEmpty(baseError.msg)) {
                                ToastUtils.showShort(baseError.msg);
                                if (baseError.code == 2213) {
                                    Intent intent = new Intent(DeviceActivateEditActivity.this, ActivateStepActivity.class);
                                    intent.putExtra("carId", carId);
                                    startActivity(intent);
                                }
                            } else {
                                ToastUtils.showShort("开始激活");
                                Intent intent = new Intent(DeviceActivateEditActivity.this, ActivateStepActivity.class);
                                intent.putExtra("carId", carId);
                                startActivity(intent);
                                finish();
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
}
