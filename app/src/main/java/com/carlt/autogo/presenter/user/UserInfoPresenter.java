package com.carlt.autogo.presenter.user;

import android.annotation.SuppressLint;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/9.
 */
public class UserInfoPresenter extends BasePresenter<IUserInfoView> {
    @SuppressLint("CheckResult")
    public void getUserInfo() {
        uuDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token", ""));
        ClientFactory.def(UserService.class).getUserInfo(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        uuDialog.dismiss();
                        mView.getUserInfoSuccess(userInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable.toString());
                    }
                });
    }
}
