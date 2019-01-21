package com.carlt.autogo.presenter.remote;

import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.remote.RemoteLogInfo;
import com.google.gson.Gson;

/**
 * Created by Marlon on 2019/1/17.
 */
public class RemoteLogPresenter extends BasePresenter<IRemoteLogView> {
    public void remoteLog(){
        String json = "{\"code\":200,\"data\":{\"list\":[{\"logtype\":\"11\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:03\"},{\"logtype\":\"71\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:03\"},{\"logtype\":\"53\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:03\"},{\"logtype\":\"64\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:02\"},{\"logtype\":\"62\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:02\"},{\"logtype\":\"21\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:02\"},{\"logtype\":\"41\",\"log_device_name\":\"zhang的 iPhone\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 15:02\"},{\"logtype\":\"11\",\"log_device_name\":\"MI 6X\",\"log_result\":\"1\",\"logtime\":\"2019-01-07 11:30\"}],\"has_next\":1},\"msg\":\"操作成功\",\"version\":\"v100\",\"request\":\"\\/126\\/carRelated\\/getRemoteOperationLog\"}";
        Gson gson = new Gson();
        RemoteLogInfo info = gson.fromJson(json,RemoteLogInfo.class);
        mView.getRemoteLogSuccess(info);
    }
}
