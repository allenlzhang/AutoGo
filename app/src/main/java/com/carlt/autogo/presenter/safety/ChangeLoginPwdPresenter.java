package com.carlt.autogo.presenter.safety;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/9.
 */
public class ChangeLoginPwdPresenter extends BasePresenter<IChangeLoginPwdView> {
    /**
     * 记得登录密码  确认
     * @param oldPwd
     * @param newPwd
     */
    public void doRememberPwdConfirm(String oldPwd, String newPwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
        params.put("oldPassword", CipherUtils.md5(oldPwd));
        params.put("newPassword", CipherUtils.md5(newPwd));
        params.put("isMd5", true);
        uuDialog.show();
        Disposable dispRememberPwd = ClientFactory.def(UserService.class).userResetPwd(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.userResetPwdSuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
        disposables.add(dispRememberPwd);
    }

    /**
     * 忘记原登录密码 确认
     * @param mobile
     * @param validate
     * @param newPwd
     */
    public void doForgetPwdConfirm(String mobile, String validate, String newPwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
        params.put("mobile", mobile);
        params.put("validate", validate);
        params.put("newPassword", CipherUtils.md5(newPwd));
        params.put("isMd5", true);
        uuDialog.show();
        Disposable disposableForgetPwd = ClientFactory.def(UserService.class).userRetrievePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.userRetrievePwdSuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
        disposables.add(disposableForgetPwd);
    }
}
