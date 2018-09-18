package com.carlt.autogo.presenter.register;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserRegister;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

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
    public void register(Map<String,Object> params) {
        // TODO: 2018/9/3 注册逻辑

        uuDialog.show();

        Disposable disposable =  ClientFactory.def(UserService.class).userRegister(params)
                .filter(new Predicate<UserRegister>() {
                    @Override
                    public boolean test(UserRegister userRegister) throws Exception {
                        return userRegister != null;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserRegister>() {
                    @Override
                    public void accept(UserRegister userRegister) throws Exception {
                        uuDialog.dismiss();
                        if(userRegister.code == 0){
                            ToastUtils.showShort("注册成功");
                            mView.onRegisterFinish();
                        }else {
                            ToastUtils.showShort(userRegister.msg);
                        }
                        LogUtils.e(userRegister.toString());
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
