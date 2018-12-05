package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

/**
 * Created by Marlon on 2018/12/4.
 */
public class CarInfo {
    public int id; // id
    public int cuscarId; // 车辆档案id
    public int dealerid; // 所属经销商ID
    public int deviceid; // 设备id
    public String brandTitle; // 品牌名称
    public String modelTitle; // 车系名称
    public String optionTitle; // 车型名称
    public String carName; // 车辆名称
    public String orgId; // 组织id
    public int brandid; // 品牌ID
    public int modelid; // 车系ID
    public int optionid; // 车型ID
    public String carLogo; // 车辆logo
    public int buyDate; // 购车时间
    public int maintenMiles; // 上次保养里程
    public int maintenDate; // 上次保养时间
    public int maintenNextMiles; // 下次保养公里数
    public int maintenNextDate; // 下次保养日期
    public int applicantDate; // 上次投保时间
    public int inspectTime; // 上次年检时间
    public int authStartTime; // 授权开始时间
    public int authEndTime; // 授权结束时间
    public int authStatus; // 授权状态,1未授权，2授权中
    public int authType; // 授权类型，1授权别人，2 被授权
    public int remoteStatus; // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
    public int recodeStatus; // 行车记录仪激活状态
    public int machineStatus; // 车机激活状态
    public int driver_license_img; // 驾驶证照片
    public String pledge; // 押金信息
    public String insurance; // 投保公司信息
    public int chargetime; // 定时充电时间
    public int register_time; // 注册到车管所时间
    public int next_inspect_time; // 下次年检时间
    public BaseError err; // 错误描述

    @Override
    public String toString() {
        return "CarInfo{" +
                "id=" + id +
                ", cuscarId=" + cuscarId +
                ", dealerid=" + dealerid +
                ", deviceid=" + deviceid +
                ", brandTitle='" + brandTitle + '\'' +
                ", modelTitle='" + modelTitle + '\'' +
                ", optionTitle='" + optionTitle + '\'' +
                ", carName='" + carName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", brandid=" + brandid +
                ", modelid=" + modelid +
                ", optionid=" + optionid +
                ", carLogo='" + carLogo + '\'' +
                ", buyDate=" + buyDate +
                ", maintenMiles=" + maintenMiles +
                ", maintenDate=" + maintenDate +
                ", maintenNextMiles=" + maintenNextMiles +
                ", maintenNextDate=" + maintenNextDate +
                ", applicantDate=" + applicantDate +
                ", inspectTime=" + inspectTime +
                ", authStartTime=" + authStartTime +
                ", authEndTime=" + authEndTime +
                ", authStatus=" + authStatus +
                ", authType=" + authType +
                ", remoteStatus=" + remoteStatus +
                ", recodeStatus=" + recodeStatus +
                ", machineStatus=" + machineStatus +
                ", driver_license_img=" + driver_license_img +
                ", pledge='" + pledge + '\'' +
                ", insurance='" + insurance + '\'' +
                ", chargetime=" + chargetime +
                ", register_time=" + register_time +
                ", next_inspect_time=" + next_inspect_time +
                ", err=" + err +
                '}';
    }
}
