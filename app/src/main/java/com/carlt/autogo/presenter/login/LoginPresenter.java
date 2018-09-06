package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;

import com.carlt.autogo.basemvp.BasePresenter;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    @SuppressLint("CheckResult")
    public void login() {
        // TODO: 2018/9/3 登录逻辑
        mView.loginFinish();
    }
}
