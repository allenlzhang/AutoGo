package com.carlt.autogo.presenter.user;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/10.
 */
public class EditUserInfoPresenter extends BasePresenter<IEditUserInfoView> {
    @SuppressLint("CheckResult")
    public void commitEdUerSex(final String text, final int sex) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token", ""));
        params.put("gender", sex);
        uuDialog.show();
        ClientFactory.def(UserService.class).userEditInfo(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.userEditInfoSuccess(baseError,text,sex);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLong("编辑失败");
                        uuDialog.dismiss();
                    }
                });
    }
}
