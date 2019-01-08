package com.carlt.autogo.presenter.car;

import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.user.BaseError;

/**
 * Created by Marlon on 2019/1/8.
 */
public interface ICarDetailsView {
    void getCarInfoSuccess(CarInfo carInfo);
    void modifySuccess(BaseError baseError);
    void cancelAuthSuccess(BaseError baseError);
}
