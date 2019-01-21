package com.carlt.autogo.presenter.remote;

import com.carlt.autogo.R;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.car.RemoteInfo;
import com.carlt.autogo.entry.remote.CurCarConfigInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2019/1/17.
 */
public class RemotePresenter extends BasePresenter<IRemoteView> {
    public void curCarConfig(){
        String json = "{\"code\":200,\"data\":{\"autorestart\":0,\"remoteStart\":1,\"SLCarLocating\":1,\"autoCloseWinSw\":0,\"PSTsupervise\":1,\"AUTOpadlock\":0,\"Dooropen\":0,\"Doorunlocked\":0,\"Carwindow\":0,\"Carprevention\":1,\"remoteWinSw\":0,\"remoteLocked\":1,\"remoteAirconditioner\":1,\"remoteTrunk\":1,\"remoteSwitchSkylight\":0,\"navigationSync\":1,\"directPSTsupervise\":1,\"remoteItemCount\":11,\"remoteAirconditioner_item\":\"5,4,3,2\",\"remoteSkylightPry\":1,\"remoteCharger\":0},\"msg\":\"\",\"version\":\"v100\",\"request\":\"\\/200\\/car\\/curCarConfig\"}";
        Gson gson = new Gson();
        CurCarConfigInfo info = gson.fromJson(json,CurCarConfigInfo.class);
        RemoteInfo remoteInfo;
        List<RemoteInfo> infoList = new ArrayList<>();
        if (info!=null){
            if (info.data.remoteStart != 255){
                remoteInfo = new RemoteInfo();
                remoteInfo.id_img1 = R.mipmap.remote_engine_title;
                remoteInfo.id_img2 = R.mipmap.remote_engine_icon;
                infoList.add(remoteInfo);
            }
            if (info.data.remoteAirconditioner != 255){
                remoteInfo = new RemoteInfo();
                remoteInfo.id_img1 = R.mipmap.remote_air_title;
                remoteInfo.id_img2 = R.mipmap.remote_air_icon;
                infoList.add(remoteInfo);
            }
            if (info.data.remoteWinSw != 255){
                remoteInfo = new RemoteInfo();
                remoteInfo.id_img1 = R.mipmap.remote_car_window_title;
                remoteInfo.id_img2 = R.mipmap.remote_car_window_icon;
                infoList.add(remoteInfo);
            }
            if (info.data.remoteSwitchSkylight != 255||info.data.remoteSkylightPry != 255){
                remoteInfo = new RemoteInfo();
                remoteInfo.id_img1 = R.mipmap.remote_sky_window_title;
                remoteInfo.id_img2 = R.mipmap.remote_sky_window_icon;
                infoList.add(remoteInfo);
            }
            if (info.data.SLCarLocating != 255||info.data.remoteTrunk != 255){
                remoteInfo = new RemoteInfo();
                remoteInfo.id_img1 = R.mipmap.remote_other_title;
                remoteInfo.id_img2 = R.mipmap.remote_other_icon;
                infoList.add(remoteInfo);
            }
        }
        mView.curCarConfigSuccess(infoList);
    }
}
