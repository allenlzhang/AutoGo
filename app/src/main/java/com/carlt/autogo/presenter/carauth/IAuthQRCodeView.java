package com.carlt.autogo.presenter.carauth;

import android.view.View;

import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:30
 */
public interface IAuthQRCodeView {
    void createAuthQRCode(CarBaseInfo info);

    void getMyCarList(AuthCarInfo info, View v);

    void getAuthTimes(CarAuthTimeInfo info,View v);

}
