package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    @SuppressLint("CheckResult")
    public void login(Map<String, Object> params) {
        uuDialog.show();

        ClientFactory.def(UserService.class).userLogin(params)
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user != null;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        LogUtils.e(user);
                        uuDialog.dismiss();
                        if(user.err != null){
                            ToastUtils.showShort(user.err.msg);
                        }else {
                            ToastUtils.showShort("登录成功");
                            mView.loginFinish();
                        }
                    }
                },new CommonThrowable<Throwable>());

    }
}
