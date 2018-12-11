package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marlon on 2018/11/22.
 */
public class AuthCarInfo {
    public List<MyCarBean> myCar;

    public List<MyCarBean> authCar;

    public BaseError err;

    @Override
    public String toString() {
        return "AuthCarInfo{" +
                "myCar=" + myCar +
                ", authCar=" + authCar +
                ", err=" + err +
                '}';
    }

    public class MyCarBean implements Serializable{
        public int id;
        public String brandTitle; // 品牌名称
        public String modelTitle; // 车系名称
        public String optionTitle; // 车型名称
        public String carName; // 车辆名称
        public String carLogo; // 车辆logo
        public long authEndTime; // 授权结束时间
        public int authStatus; // 授权状态,1未授权，2授权中
        public int remoteStatus; // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
        public int recodeStatus; // 行车记录仪激活状态
        public int machineStatus; // 车机激活状态

        @Override
        public String toString() {
            return "MyCarBean{" +
                    "id=" + id +
                    ", brandTitle='" + brandTitle + '\'' +
                    ", modelTitle='" + modelTitle + '\'' +
                    ", optionTitle='" + optionTitle + '\'' +
                    ", carName='" + carName + '\'' +
                    ", carLogo='" + carLogo + '\'' +
                    ", authEndTime=" + authEndTime +
                    ", authStatus=" + authStatus +
                    ", remoteStatus=" + remoteStatus +
                    ", recodeStatus=" + recodeStatus +
                    ", machineStatus=" + machineStatus +
                    '}';
        }
    }






}
