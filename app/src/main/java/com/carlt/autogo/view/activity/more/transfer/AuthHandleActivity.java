package com.carlt.autogo.view.activity.more.transfer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthHandleActivity extends BaseMvpActivity {

    @BindView(R.id.tvCarName)
    TextView tvCarName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.btnAgree)
    Button   btnAgree;
    @BindView(R.id.btnRefuseAgree)
    Button   btnRefuseAgree;



    @Override
    protected int getContentView() {
        return R.layout.activity_auth_handle;
    }

    @Override
    public void init() {
        setTitleText("授权处理");
    }

    @OnClick({R.id.btnAgree, R.id.btnRefuseAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAgree:
                break;
            case R.id.btnRefuseAgree:
                break;
        }
    }
}
