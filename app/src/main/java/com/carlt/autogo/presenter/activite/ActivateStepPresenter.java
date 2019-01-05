package com.carlt.autogo.presenter.activite;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.ActivateStepInfo;
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

public class ActivateStepPresenter extends BasePresenter<IActivateStepView> {
    @SuppressLint("CheckResult")
    public void getStepInfos(Map<String, Object> params, boolean isLoading) {
        if (isLoading) {
            uuDialog.show();
        } else {
            uuDialog.dismiss();
        }
        ClientFactory.def(CarService.class).getLogs(params)
                .subscribe(new Consumer<ActivateStepInfo>() {
                    @Override
                    public void accept(ActivateStepInfo info) throws Exception {
                        LogUtils.e(info);
                        mView.getStepInfoFinish(info);
                        uuDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        mView.getStepInfoErr(throwable);
                    }
                });
    }
}
