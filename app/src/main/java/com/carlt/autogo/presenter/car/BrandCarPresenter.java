package com.carlt.autogo.presenter.car;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2019/1/8.
 */
public class BrandCarPresenter extends BasePresenter<IBrandCarView> {
    public void clientGetData(int modelId) {
        uuDialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("modelId", modelId);
        Disposable disposable = ClientFactory.def(CarService.class).getBrandCar(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarBrandInfo>() {
                    @Override
                    public void accept(CarBrandInfo carBrandInfo) throws Exception {
                        LogUtils.e(carBrandInfo.toString());
                        uuDialog.dismiss();
                        mView.getBrandCarSuccess(carBrandInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
        disposables.add(disposable);

    }
}
