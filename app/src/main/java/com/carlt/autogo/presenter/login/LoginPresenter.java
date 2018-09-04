package com.carlt.autogo.presenter.login;

import android.util.Log;

import com.carlt.autogo.basemvp.BasePresenter;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public void login() {
        // TODO: 2018/9/3 登录逻辑

        Log.e("carlt----", mContext.toString());
        mView.loginFinish();
    }
}
