package com.carlt.autogo.view.activity.more.safety;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

/**
 * Created by Marlon on 2018/9/12.
 */
public class LoginPwdManagementActivity extends BaseMvpActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_login_pwd_management;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.safety_login_pwd_management));
    }
}
