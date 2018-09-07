package com.carlt.autogo.view.activity.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.presenter.register.IRegisterView;

import butterknife.BindView;

public class RegisterNextActivity extends BaseMvpActivity  {

    @BindView(R.id.ed_register_pwd)
    EditText edRegisterPwd;

    @BindView(R.id.ed_register_pwd_d)
    EditText edRegisterPwdD;

    @BindView(R.id.cb_law)
    CheckBox cbLaw;

    @BindView(R.id.tv_law)
    TextView tvLaw;

    @BindView(R.id.btn_register_commit)
    Button btnRegisterCommit;


    @Override
    protected int getContentView() {
        return R.layout.activity_register_next;
    }

    @Override
    public void init() {
        setTitleText("注册");
    }


}
