package com.carlt.autogo.presenter.register;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserRegister;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:53
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    @SuppressLint("CheckResult")
    public void register ( final Map<String,Object> params) {
        // TODO: 2018/9/3 注册逻辑

        uuDialog.show();

        Disposable disposable = ObservableHelper.commonReg(params)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        mView.onRegisterFinish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable.getMessage());
                    }
                });

        disposables.add(disposable);


    }
}
