package com.carlt.autogo.presenter.carauth;

import com.carlt.autogo.basemvp.BaseMvpView;
import com.carlt.autogo.entry.car.CarBaseInfo;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:30
 */
public interface IAuthHandleView extends BaseMvpView {
    void getCarInfo(CarBaseInfo info);

    void refuseAuth(CarBaseInfo info);

}
