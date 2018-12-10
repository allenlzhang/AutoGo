package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/12/3 11:23
 */
public class CarBaseInfo {
    public BaseError err;
    public int       id;
    public int       checkStatus;
    public int       duration;
    public int       transferId;
    public int       status;
    public String    mobile;
    public String    carName;
    public int       code;
    public String    msg;

    @Override
    public String toString() {
        return "CarBaseInfo{" +
                "error=" + err +
                ", id=" + id +
                ", checkStatus=" + checkStatus +
                ", duration=" + duration +
                ", mobile='" + mobile + '\'' +
                ", carName='" + carName + '\'' +
                '}';
    }
}
