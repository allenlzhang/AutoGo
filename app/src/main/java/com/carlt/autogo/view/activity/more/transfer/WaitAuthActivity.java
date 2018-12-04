package com.carlt.autogo.view.activity.more.transfer;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

public class WaitAuthActivity extends BaseMvpActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wait_auth);
//    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wait_auth;
    }

    @Override
    public void init() {
       setTitleText("等待授权");
    }
}
