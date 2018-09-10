package com.carlt.autogo.presenter.register;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
    public void register(String params) {
        // TODO: 2018/9/3 注册逻辑

        uuDialog.show();

    Disposable disposable =  ClientFactory.def(UserService.class).getValidate(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonThrowable<Object>(){
                    @Override
                    public void accept(Object o) throws Exception {
                        super.accept(o);

                    }
                },new CommonThrowable());

          disposables.add(disposable);

        mView.onRegisterFinish();
    }
}
