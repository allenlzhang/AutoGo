package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/12.
 */
public class LoginPwdManagementActivity extends BaseMvpActivity {
    /**
     * 记住原密码
     */
    @BindView(R.id.ll_management_remember_old_pwd)
    LinearLayout llManagementRememberOldPwd;

    /**
     * 忘记原密码
     */
    @BindView(R.id.ll_management_forget_old_pwd)
    LinearLayout llManagementForgetOldPwd;

    public static final int remember = 0;   //记得原密码

    public static final int forget = 1; //忘记原密码

    @Override
    protected int getContentView() {
        return R.layout.activity_login_pwd_management;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.safety_login_pwd_management));
    }


    @OnClick({R.id.ll_management_remember_old_pwd, R.id.ll_management_forget_old_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_management_remember_old_pwd:
                startActivity(remember);
                break;
            case R.id.ll_management_forget_old_pwd:
                startActivity(forget);
                break;
        }
    }

    private void startActivity(int type){
        Intent intent1 = new Intent(LoginPwdManagementActivity.this,ChangeLoginPwdActivity.class);
        intent1.putExtra("changePwd",type);
        startActivity(intent1);
    }

}
