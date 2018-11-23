package com.carlt.autogo.view.activity.more.transfer;

import android.os.Bundle;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

public class TransHandleActivity extends BaseMvpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_handle);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_trans_handle;
    }

    @Override
    public void init() {

    }
}
