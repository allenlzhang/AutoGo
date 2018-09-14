package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/13.
 */
public class ChangeLoginPwdActivity extends BaseMvpActivity {
    @BindView(R.id.edit_management_phone)
    EditText editManagementPhone;
    @BindView(R.id.edit_management_code)
    EditText editManagementCode;
    @BindView(R.id.btn_management_code)
    Button btnManagementCode;
    @BindView(R.id.rl_management_code)
    RelativeLayout rlManagementCode;
    @BindView(R.id.edit_management_old_pwd)
    EditText editManagementOldPwd;
    @BindView(R.id.edit_management_new_pwd)
    EditText editManagementNewPwd;
    @BindView(R.id.edit_management_new_pwd_again)
    EditText editManagementNewPwdAgain;
    @BindView(R.id.edit_management_confirm)
    Button editManagementConfirm;

    private int type = 0;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_login_pwd;
    }

    @Override
    public void init() {
        type = getIntent().getIntExtra("changePwd",0);
        loadTypeView(type);
    }

    @OnClick({R.id.btn_management_code, R.id.edit_management_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_code:
                break;
            case R.id.edit_management_confirm:
                break;
        }
    }

    private void loadTypeView(int type){
        switch (type){
            case LoginPwdManagementActivity.remember:
                editManagementPhone.setVisibility(View.GONE);
                rlManagementCode.setVisibility(View.GONE);
                editManagementOldPwd.setVisibility(View.VISIBLE);
                setTitleText(getResources().getString(R.string.management_change_login_pwd));
                break;
            case LoginPwdManagementActivity.forget:
                editManagementPhone.setVisibility(View.VISIBLE);
                rlManagementCode.setVisibility(View.VISIBLE);
                editManagementOldPwd.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_forget_login_pwd));
                break;
        }


    }
}
