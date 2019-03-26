package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;

import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.presenter.ObservableHelper;

import java.util.Map;

import io.reactivex.functions.Consumer;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    @SuppressLint("CheckResult")
    public void login(final Map<String, Object> params) {
        mView.showLoading(true);

        ObservableHelper.commonLogin(params)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mView.complete();
                        mView.loginFinish(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.complete();
                        mView.showError(ObservableHelper.errorMsg);
//                        ToastUtils.showLong(ObservableHelper.errorMsg);
                    }
                });
    }
}
