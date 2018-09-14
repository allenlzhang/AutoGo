package com.carlt.autogo.view.activity.more.safety;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

/**
 * Created by Marlon on 2018/9/13.
 */
public class LoginDeviceManagementActivity extends BaseMvpActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_login_device_management;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.management_login_device));
    }
}
