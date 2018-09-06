package com.carlt.autogo.view.activity;


import android.view.View;
import android.widget.Button;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.presenter.login.ILoginView;
import com.carlt.autogo.presenter.login.LoginPresenter;

import butterknife.BindView;

@CreatePresenter(presenter = LoginPresenter.class)
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {


    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {


    }


    @Override
    public void loginFinish() {
        // TODO: 2018/9/3 处理登录完成逻辑
    }
}
