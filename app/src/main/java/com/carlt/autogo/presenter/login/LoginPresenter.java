package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    @SuppressLint("CheckResult")
    public void login(final Map<String, Object> params) {
        uuDialog.show();

        ObservableHelper.commonLogin(params)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        mView.loginFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();

                        ToastUtils.showLong(ObservableHelper.errorMsg);
                    }
                });
    }
}
