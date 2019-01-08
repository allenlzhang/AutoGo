package com.carlt.autogo.presenter.car;

import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;

/**
 * Created by Marlon on 2019/1/7.
 */
public interface ICarCertificationView {
    void selectCarSuccess(String carData);
    void updateImageFileSuccess(UpdateImageResultInfo updateImageResultInfo);
    void addCarSuccess(BaseError err);
}
