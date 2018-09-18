package com.carlt.autogo.presenter.login;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
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
                .flatMap(new Function<User, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(User user) throws Exception {
                        if(user.err != null){
                            errorMsg = user.err.msg ;
                            return  null;
                        }else {
                            Map<String, String> token =   new HashMap<String, String>();
                            token.put("token",user.token);
                            SharepUtil.put("token",user.token);
                            return ClientFactory.def(UserService.class).getUserInfo(token);
                        }
                    }
                })
                .map(new Function<UserInfo, String>() {
                    @Override
                    public String apply(UserInfo userInfo) throws Exception {
                        if(userInfo.err != null){
                            errorMsg = userInfo.err.msg ;
                            return null;
                        }else {
                            SharepUtil.<UserInfo>putByBean("user", userInfo) ;
                        }
                        return "登录成功";
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        mView.loginFinish();
                    }
                },new CommonThrowable<Throwable>());
    }
}
