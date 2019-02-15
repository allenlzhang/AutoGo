package com.carlt.autogo.view.activity.activate;

import android.content.Intent;
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
    private int carId;
    private int withTbox;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_activate;
    }

    @Override
    public void init() {
        setTitleText("设备激活");
        carId = getIntent().getIntExtra("carId", 0);
        withTbox = getIntent().getIntExtra("withTbox", -1);
    }

    @OnClick(R.id.btnACCNext)
    public void onViewClicked() {
        Intent intent = new Intent(this, DeviceActivateEditActivity.class);
        intent.putExtra("carId", carId);
        intent.putExtra("withTbox", withTbox);
        startActivity(intent);
        finish();
    }
}
