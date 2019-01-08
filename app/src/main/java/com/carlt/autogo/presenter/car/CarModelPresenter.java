package com.carlt.autogo.presenter.car;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.CarModelInfo;
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
public class CarModelPresenter extends BasePresenter<ICarModelView> {

    public void clientGetData(int brandId){
        uuDialog.show();
        Map<String,Integer> map = new HashMap<>();
        map.put("brandId",brandId);
        Disposable disposable = ClientFactory.def(CarService.class).getModel(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarModelInfo>() {
                    @Override
                    public void accept(CarModelInfo carModelInfo) throws Exception {
                        uuDialog.dismiss();
                        mView.getModelSuccess(carModelInfo);
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
