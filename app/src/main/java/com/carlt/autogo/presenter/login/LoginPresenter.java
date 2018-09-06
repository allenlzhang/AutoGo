package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.net.base.RestClientFactory;
import com.carlt.autogo.net.service.UserService;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
