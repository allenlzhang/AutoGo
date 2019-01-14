package com.carlt.autogo.presenter.safety;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.ShareLoginList;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by Marlon on 2019/1/14.
 */
public class SafetyPresenter extends BasePresenter<ISafetyView> {

    @SuppressLint("CheckResult")
    public void checkShareLoginList() {
        ClientFactory.def(UserService.class).getShareLogin(new HashMap<String, Object>())
                .subscribe(new Consumer<ShareLoginList>() {
                    @Override
                    public void accept(ShareLoginList info) throws Exception {
                        mView.getShareLoginSuccess(info);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void unBindALiAuth(final int type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openType", type);
        ClientFactory.def(UserService.class).shareLoginUnbind(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        mView.shareLoginUnbindSuccess(error,type);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
