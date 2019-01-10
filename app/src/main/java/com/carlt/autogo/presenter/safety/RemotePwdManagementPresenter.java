package com.carlt.autogo.presenter.safety;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/10.
 */
public class RemotePwdManagementPresenter extends BasePresenter<IRemotePwdManagementView> {
    /**
     * 远程开关
     * remotePwdSwitch 1 开 2 关
     */
    public void remoteSwitch(final int remotePwdSwitch) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, ""));
        params.put("remotePwdSwitch", remotePwdSwitch);
        uuDialog.show();
        Disposable disposableRemoteSwitch = ClientFactory.def(UserService.class).modifyRemoteSwitch(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.remoteSwitchSuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
        disposables.add(disposableRemoteSwitch);
    }
}
