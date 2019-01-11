package com.carlt.autogo.presenter.carauth;

import android.annotation.SuppressLint;

import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.Map;

import io.reactivex.functions.Consumer;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:29
 */
public class AuthHandlePresenter extends BasePresenter<IAuthHandleView> {
    @SuppressLint("CheckResult")
    public void getStepInfos(Map<String, Object> params) {
        uuDialog.show();

        ClientFactory.def(CarService.class).getById(params)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        uuDialog.dismiss();
                        mView.getCarInfo(carBaseInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
//                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void refuseAuth(Map<String, Object> params) {
        uuDialog.show();

        ClientFactory.def(CarService.class).modifyStatus(params)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        uuDialog.dismiss();
                        mView.refuseAuth(carBaseInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();

//                        ToastUtils.showShort("操作失败");
                    }
                });
    }
}
