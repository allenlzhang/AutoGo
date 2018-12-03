package com.carlt.autogo.view.activity.car;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/12/3.
 * 设备激活
 */
public class DeviceActivateEditActivity extends BaseMvpActivity {
    @BindView(R.id.etPin)
    EditText etPin;
    @BindView(R.id.etDeviceNum)
    EditText etDeviceNum;
    @BindView(R.id.btnActivate)
    Button btnActivate;

    @Override
    protected int getContentView() {
        return R.layout.activity_activate_edit;
    }

    @Override
    public void init() {
        setTitleText("设备激活");
    }

    @OnClick(R.id.btnActivate)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPin.getText())){
            ToastUtils.showShort("PIN码输入不能为空");
        }else if (TextUtils.isEmpty(etDeviceNum.getText())){
            ToastUtils.showShort("设备号输入不能为空");
        }else {

        }
    }
}
