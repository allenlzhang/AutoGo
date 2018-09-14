package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/13.
 */
@SuppressLint("Registered")
public class RemotePwdManagementActivity extends BaseMvpActivity {


    @BindView(R.id.ll_management_set_remote_pwd)
    LinearLayout llManagementSetRemotePwd;
    @BindView(R.id.ll_management_remember_remote_old_pwd)
    LinearLayout llManagementRememberRemoteOldPwd;
    @BindView(R.id.ll_management_forget_remote_old_pwd)
    LinearLayout llManagementForgetRemoteOldPwd;
    @BindView(R.id.ll_management_without_encryption_date)
    LinearLayout llManagementWithoutEncryptionDate;

    public static final int SETREMOTEPED = 0;

    public static final int REMEBERPWD = 1;

    public static final int FORGETPWD = 2;

    @Override
    protected int getContentView() {
        return R.layout.activity_remote_pwd_management;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.management_remote_pwd));
    }

    @OnClick({R.id.ll_management_set_remote_pwd, R.id.ll_management_remember_remote_old_pwd, R.id.ll_management_forget_remote_old_pwd, R.id.ll_management_without_encryption_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_management_set_remote_pwd:
                startActivity(SETREMOTEPED);
                break;
            case R.id.ll_management_remember_remote_old_pwd:
                startActivity(REMEBERPWD);
                break;
            case R.id.ll_management_forget_remote_old_pwd:
                startActivity(FORGETPWD);
                break;
            case R.id.ll_management_without_encryption_date:
                break;
        }
    }

    private void startActivity(int type){
        Intent intent = new Intent(RemotePwdManagementActivity.this,ChangeRemotePwdActivity.class);
        intent.putExtra("resetRemote",type);
        startActivity(intent);
    }

}
