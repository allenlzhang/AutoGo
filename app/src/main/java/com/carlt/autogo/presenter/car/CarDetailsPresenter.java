package com.carlt.autogo.presenter.car;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/8.
 */
public class CarDetailsPresenter extends BasePresenter<ICarDetailsView> {
    /**
     * 获取车辆信息
     *
     * @param id
     */
    @SuppressLint("CheckResult")
    public void ClientGetCarInfo(int id) {
        uuDialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("id", id);
        ClientFactory.def(CarService.class).getCarInfo(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarInfo>() {
                    @Override
                    public void accept(CarInfo carInfo) {
                        uuDialog.dismiss();
                        mView.getCarInfoSuccess(carInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    /**
     * 编辑车辆
     * @param map
     */
    @SuppressLint("CheckResult")
    public void modify(Map<String, Object> map){
        uuDialog.show();
        ClientFactory.def(CarService.class).modify(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.modifySuccess(baseError);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    /**
     * 取消授权
     */
    @SuppressLint("CheckResult")
    public void cancelAuth(int authId) {
        uuDialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("id", authId);
        ClientFactory.def(CarService.class).cancelAuth(map).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        uuDialog.dismiss();
                        mView.cancelAuthSuccess(baseError);
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
