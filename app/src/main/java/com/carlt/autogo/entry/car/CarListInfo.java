package com.carlt.autogo.entry.car;

import java.util.List;

/**
 * Created by Marlon on 2018/11/22.
 */
public class CarListInfo {
    private List<DataBean> myCarList;

    private List<DataBean> authCarList;

    public List<DataBean> getMyCarList() {
        return myCarList;
    }

    public void setMyCarList(List<DataBean> myCarLists) {
        this.myCarList = myCarLists;
    }

    public List<DataBean> getAuthCarList() {
        return authCarList;
    }

    public void setAuthCarList(List<DataBean> authCarLists) {
        this.authCarList = authCarLists;
    }

    public class DataBean{
        private int id;
        private String brandTitle; // 品牌名称
        private String modelTitle; // 车系名称
        private String optionTitle; // 车型名称
        private String carName; // 车辆名称
        private String carLogo; // 车辆logo
        private int authEndTime; // 授权结束时间
        private int authStatus; // 授权状态
        private int remoteStatus; // 远程激活状态
        private int recodeStatus; // 行车记录仪激活状态
        private int machineStatus; // 车机激活状态

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBrandTitle() {
            return brandTitle;
        }

        public void setBrandTitle(String brandTitle) {
            this.brandTitle = brandTitle;
        }

        public String getModelTitle() {
            return modelTitle;
        }

        public void setModelTitle(String modelTitle) {
            this.modelTitle = modelTitle;
        }

        public String getOptionTitle() {
            return optionTitle;
        }

        public void setOptionTitle(String optionTitle) {
            this.optionTitle = optionTitle;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getCarLogo() {
            return carLogo;
        }

        public void setCarLogo(String carLogo) {
            this.carLogo = carLogo;
        }

        public int getAuthEndTime() {
            return authEndTime;
        }

        public void setAuthEndTime(int authEndTime) {
            this.authEndTime = authEndTime;
        }

        public int getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(int authStatus) {
            this.authStatus = authStatus;
        }

        public int getRemoteStatus() {
            return remoteStatus;
        }

        public void setRemoteStatus(int remoteStatus) {
            this.remoteStatus = remoteStatus;
        }

        public int getRecodeStatus() {
            return recodeStatus;
        }

        public void setRecodeStatus(int recodeStatus) {
            this.recodeStatus = recodeStatus;
        }

        public int getMachineStatus() {
            return machineStatus;
        }

        public void setMachineStatus(int machineStatus) {
            this.machineStatus = machineStatus;
        }
    }






}
