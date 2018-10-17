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

        ClientFactory.def(UserService.class).userLogin(params)
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
                            LogUtils.e(user.token);
                            return ObservableHelper.getUserInfoByToken(token ,GlobalKey.loginStateByPWd, (String) params.get("password"));
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        mView.loginFinish();
                    }
                }, new CommonThrowable<Throwable>());
    }
}
