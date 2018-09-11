package com.carlt.autogo.presenter.register;


import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.requsetbody.RequestBodyLogin;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:53
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {
    @SuppressLint("CheckResult")
    public void register(Map params) {
        // TODO: 2018/9/3 注册逻辑

        uuDialog.show();

        Disposable disposable = ClientFactory.def(UserService.class).getValidate(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {

                        uuDialog.dismiss();

                    }
                }, new CommonThrowable<Throwable>());

        disposables.add(disposable);

        mView.onRegisterFinish();
    }
}
