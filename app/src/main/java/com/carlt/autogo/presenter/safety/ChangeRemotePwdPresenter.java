package com.carlt.autogo.presenter.safety;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.CipherUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/10.
 */
public class ChangeRemotePwdPresenter extends BasePresenter<IChangeRemotePwdView> {
    /**
     * 设置远程密码
     */
    public void setRemotePwdClient(String token, String remoteNewPwd) {

        HashMap<String, String> params = new HashMap();
        params.put(GlobalKey.USER_TOKEN, token);
        params.put("remotePwd", CipherUtils.md5(remoteNewPwd));
        uuDialog.show();
        Disposable disposableSetRemotePwd = ClientFactory.def(UserService.class).SetRemotePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.setRemotePwdSuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
        disposables.add(disposableSetRemotePwd);
    }

    /**
     * 修改远程密码 记得密码
     */
    public void modifyRemotePwd(String token, String remoteOldPwd, String remoteNewPwd) {
        HashMap<String, String> params = new HashMap();
        params.put(GlobalKey.USER_TOKEN, token);
        params.put("oldremotePwd", CipherUtils.md5(remoteOldPwd));
        params.put("newRemotePwd", CipherUtils.md5(remoteNewPwd));
        LogUtils.e(params);
        Log.i("modifyRemotePwd", "{\"token\":\"" + token + "\",\"oldremotePwd\":\"" + CipherUtils.md5(remoteOldPwd) + "\",\"newRemotePwd\":\"" + CipherUtils.md5(remoteNewPwd) + "\"}");
        uuDialog.show();
        Disposable disposableModifyRemotePwd = ClientFactory.def(UserService.class).modifyRemotePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.modifyRemotePwd(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
        disposables.add(disposableModifyRemotePwd);
    }

    /**
     * 修改远程密码 忘记密码
     */
    public void remenberRemotePassword(String token, String mobile, String validateCode, String remotePwd) {
        HashMap<String, Object> params = new HashMap();
        params.put(GlobalKey.USER_TOKEN, token);
        params.put("mobile", mobile);
        params.put("validateCode", validateCode);
        params.put("remotePwd", CipherUtils.md5(remotePwd));
        params.put("checkIdentity", false);
        uuDialog.show();
        Disposable disposableModifyRemotePwd = ClientFactory.def(UserService.class).resetRemotePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.resetRemotePwd(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
        disposables.add(disposableModifyRemotePwd);
    }
}
