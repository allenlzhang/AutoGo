package com.carlt.autogo.view.activity.car;

import android.os.Bundle;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

public class CarActivateActivity extends BaseMvpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car_activate);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_car_activate;
    }

    @Override
    public void init() {
        setTitleText("激活");
    }
}
