package com.carlt.autogo.view.activity.car;

import android.widget.Button;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/12/3.
 * 设备激活
 */
public class DeviceActivateActivity extends BaseMvpActivity {
    @BindView(R.id.btnACCNext)
    Button btnACCNext;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_activate;
    }

    @Override
    public void init() {
        setTitleText("设备激活");
    }

    @OnClick(R.id.btnACCNext)
    public void onViewClicked() {
        startActivity(DeviceActivateEditActivity.class,false);
    }
}
