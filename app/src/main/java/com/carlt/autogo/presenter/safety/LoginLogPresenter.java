package com.carlt.autogo.presenter.safety;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.adapter.LoginLogItemAdapter;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.LoginLogInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/9.
 */
public class LoginLogPresenter extends BasePresenter<ILoginLogView> {
    @SuppressLint("CheckResult")
    public void loginLog(int limit,int offset){
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", limit);
        map.put("offset", offset);
        ClientFactory.def(UserService.class).loginLog(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginLogInfo>() {
                    @Override
                    public void accept(LoginLogInfo info) throws Exception {
                        LogUtils.e("----" + info);
                        mView.loginLogSuccess(info);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e("----" + throwable);
                    }
                });
    }
}
