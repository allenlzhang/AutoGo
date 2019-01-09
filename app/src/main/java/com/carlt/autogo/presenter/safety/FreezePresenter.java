package com.carlt.autogo.presenter.safety;

import android.annotation.SuppressLint;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/9.
 */
public class FreezePresenter extends BasePresenter<IFreezeView> {

    /**
     * 解除/冻结
     * userFreeze 1正常 2冻结
     *
     * @param pwd
     */
    @SuppressLint("CheckResult")
    public void freeze(int userFreeze,String pwd) {
        uuDialog.show();
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, ""));
        params.put("password", CipherUtils.md5(pwd));
        params.put("isMd5", true);
        params.put("userFreeze", userFreeze);
        ClientFactory.def(UserService.class).freeze(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.freezeSuccess(baseError);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable.getMessage());
                    }
                });
    }
}
