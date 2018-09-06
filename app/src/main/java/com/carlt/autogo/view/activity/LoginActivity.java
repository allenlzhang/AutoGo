package com.carlt.autogo.view.activity;


import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.presenter.login.ILoginView;
import com.carlt.autogo.presenter.login.LoginPresenter;

@CreatePresenter(presenter = LoginPresenter.class)
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {



    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {

        getPresenter().login();
    }


    @Override
    public void loginFinish() {
        // TODO: 2018/9/3 处理登录完成逻辑
    }
}
