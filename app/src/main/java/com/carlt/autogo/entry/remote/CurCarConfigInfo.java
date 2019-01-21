package com.carlt.autogo.entry.remote;

/**
 * Created by Marlon on 2019/1/17.
 */
public class CurCarConfigInfo {
    /**
     * code : 200
     * data : {"autorestart":0,"remoteStart":1,"SLCarLocating":1,"autoCloseWinSw":0,"PSTsupervise":1,"AUTOpadlock":0,"Dooropen":0,"Doorunlocked":0,"Carwindow":0,"Carprevention":1,"remoteWinSw":0,"remoteLocked":1,"remoteAirconditioner":1,"remoteTrunk":1,"remoteSwitchSkylight":0,"navigationSync":1,"directPSTsupervise":1,"remoteItemCount":11,"remoteAirconditioner_item":"5,4,3,2","remoteSkylightPry":1,"remoteCharger":0}
     * msg :
     * version : v100
     * request : /200/car/curCarConfig
     */

    public int code;
    public DataBean data;
    public String msg;
    public String version;
    public String request;

    public static class DataBean {
        /**
         * autorestart : 0
         * remoteStart : 1
         * SLCarLocating : 1
         * autoCloseWinSw : 0
         * PSTsupervise : 1
         * AUTOpadlock : 0
         * Dooropen : 0
         * Doorunlocked : 0
         * Carwindow : 0
         * Carprevention : 1
         * remoteWinSw : 0
         * remoteLocked : 1
         * remoteAirconditioner : 1
         * remoteTrunk : 1
         * remoteSwitchSkylight : 0
         * navigationSync : 1
         * directPSTsupervise : 1
         * remoteItemCount : 11
         * remoteAirconditioner_item : 5,4,3,2
         * remoteSkylightPry : 1
         * remoteCharger : 0
         */

        public int autorestart;
        public int remoteStart;//发动机
        public int SLCarLocating;//（其他）
        public int autoCloseWinSw;
        public int PSTsupervise;
        public int AUTOpadlock;
        public int Dooropen;
        public int Doorunlocked;
        public int Carwindow;
        public int Carprevention;
        public int remoteWinSw;//车窗
        public int remoteLocked;
        public int remoteAirconditioner;//空调
        public int remoteTrunk;//（其他）
        public int remoteSwitchSkylight;//（天窗）
        public int navigationSync;
        public int directPSTsupervise;
        public int remoteItemCount;
        public String remoteAirconditioner_item;
        public int remoteSkylightPry;//(天窗)
        public int remoteCharger;

    }
}
