package com.carlt.autogo.presenter.activite;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/8.
 */
public class DeviceActivatePresenter extends BasePresenter<IDeviceActivateView> {
    @SuppressLint("CheckResult")
    public void deviceActive(Map<String, Object> params) {
        uuDialog.show();
        ClientFactory.def(CarService.class).active(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.activeSuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }
}
