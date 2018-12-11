package com.carlt.autogo.presenter.unregister;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/10/15 11:48
 */
public class UnRegisterPresenter extends BasePresenter<IUnRegisterView> {
    @SuppressLint("CheckResult")
    public void unRegister(Map<String, Object> params) {
        uuDialog.show();
        ClientFactory.def(UserService.class).unRegister(params)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        LogUtils.e("========" + baseError.toString());
                        uuDialog.dismiss();
                        mView.unRegisterFinish(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
    }
//    @SuppressLint("CheckResult")
//    public void checkSmsCode(Map<String, Object> params) {
//        uuDialog.show();
//        ClientFactory.def(UserService.class).checkSmsCode(params)
//
//                .subscribe(new Consumer<BaseError>() {
//                    @Override
//                    public void accept(BaseError baseError) throws Exception {
//                        LogUtils.e("========" + baseError.toString());
//                        uuDialog.dismiss();
//                        mView.checkSmsCodeFinish(baseError);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        uuDialog.dismiss();
//                    }
//                });
//    }
}
