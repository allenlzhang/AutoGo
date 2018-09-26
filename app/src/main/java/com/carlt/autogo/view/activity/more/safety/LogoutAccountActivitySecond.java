package com.carlt.autogo.view.activity.more.safety;

import android.widget.Button;
import android.widget.EditText;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LogoutAccountActivitySecond extends BaseMvpActivity {

    @BindView(R.id.etLogoutPwd)
    EditText etLogoutPwd;
    @BindView(R.id.btnConfirm)
    Button   btnConfirm;


    @Override
    protected int getContentView() {
        return R.layout.activity_logout_account_second;
    }

    @Override
    public void init() {
        setTitleText("注销账户");
    }

    @OnClick(R.id.btnConfirm)
    public void onViewClicked() {
    }
}
