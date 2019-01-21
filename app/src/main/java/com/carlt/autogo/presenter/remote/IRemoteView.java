package com.carlt.autogo.presenter.remote;

import com.carlt.autogo.entry.car.RemoteInfo;

import java.util.List;

/**
 * Created by Marlon on 2019/1/17.
 */
public interface IRemoteView {
    void curCarConfigSuccess(List<RemoteInfo> infoList);
}
