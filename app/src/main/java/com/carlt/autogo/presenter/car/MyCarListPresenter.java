package com.carlt.autogo.presenter.car;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/5.
 */
public class MyCarListPresenter extends BasePresenter<ICarListView> {

    @SuppressLint("CheckResult")
    public void getCarList(Map<String, Object> params) {
        uuDialog.show();
        ClientFactory.def(CarService.class).getMyCarList(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo info) throws Exception {
                        mView.getCarListSuccess(info);
                        uuDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        uuDialog.dismiss();
                    }
                });
    }

}
