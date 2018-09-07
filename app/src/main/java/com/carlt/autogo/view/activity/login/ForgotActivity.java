package com.carlt.autogo.view.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

public class ForgotActivity extends BaseMvpActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_forgot;
    }

    @Override
    public void init() {
        setTitleText("忘记密码");
    }
}
