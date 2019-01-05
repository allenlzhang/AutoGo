package com.carlt.autogo.presenter.cartransfer;

import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:30
 */
public interface ITransferQRCodeView {
    void createTransferQRCode(CarBaseInfo info);

    void getMyCarList(AuthCarInfo info);


}
