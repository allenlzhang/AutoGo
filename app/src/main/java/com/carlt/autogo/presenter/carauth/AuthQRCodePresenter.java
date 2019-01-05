package com.carlt.autogo.presenter.carauth;

import android.annotation.SuppressLint;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
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
public class AuthQRCodePresenter extends BasePresenter<IAuthQRCodeView> {
    @SuppressLint("CheckResult")
    public void createAuthQrcode(Map<String, Object> params) {
        uuDialog.show();

        ClientFactory.def(CarService.class).createAuthQrcode(params)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carInfo) throws Exception {
                        mView.createAuthQRCode(carInfo);
                        uuDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        uuDialog.dismiss();
                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getMyCarList(Map<String, Object> params, final View v) {
        uuDialog.show();

        ClientFactory.def(CarService.class).getMyCarList(params)
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo carInfo) throws Exception {
                        mView.getMyCarList(carInfo, v);
                        uuDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAuthTimes(Map<String, Object> params, final View v) {
        uuDialog.show();

        ClientFactory.def(CarService.class).getAuthSetting(params)
                .subscribe(new Consumer<CarAuthTimeInfo>() {
                    @Override
                    public void accept(CarAuthTimeInfo info) throws Exception {
                        uuDialog.dismiss();
                        mView.getAuthTimes(info, v);
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
