package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/12/3 11:23
 */
public class CarBaseInfo {
    public BaseError error;
    public int       id;
    public int       checkStatus;
    public int       duration;
    public String    mobile;
    public String    carName;

    @Override
    public String toString() {
        return "CarBaseInfo{" +
                "error=" + error +
                ", id=" + id +
                ", checkStatus=" + checkStatus +
                ", duration=" + duration +
                ", mobile='" + mobile + '\'' +
                ", carName='" + carName + '\'' +
                '}';
    }
}
