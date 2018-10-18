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

        Disposable disposable =  ClientFactory.def(UserService.class).userRegister(params)
                .filter(new Predicate<UserRegister>() {
                    @Override
                    public boolean test(UserRegister userRegister) throws Exception {
                        return userRegister != null;
                    }
                })
                //获取用户token
                .flatMap(new Function<UserRegister, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(UserRegister userRegister) throws Exception {
                        if(userRegister.code == 0){
                            HashMap<String,Object> mapToken = new HashMap();
                            mapToken.put("mobile", params.get("mobile"));
                            mapToken.put("password", params.get("password"));

                            mapToken.put("moveDeviceName", AutoGoApp.MODEL_NAME);
                            mapToken.put("loginModel", AutoGoApp.MODEL);
                            mapToken.put("loginSoftType", "Android");
                            return  ClientFactory.def(UserService.class).userLogin(mapToken);
                        }else {
                            ToastUtils.showShort(userRegister.msg);
                            return  null ;
                        }

                    }
                })
                .flatMap(new Function<User, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(User user) throws Exception {
                        if (user.err != null) {
                            errorMsg = user.err.msg;
                            return null;
                        } else {
                            Map<String, String> token = new HashMap<String, String>();
                            token.put("token", user.token);
                            SharepUtil.put(GlobalKey.USER_TOKEN, user.token);
                            return ObservableHelper.getUserInfoByToken(token ,GlobalKey.loginStateByPWd, (String) params.get("password"));
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
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
