package com.carlt.autogo.net.base.rxlife;
// 控制网络请求生命周期
public interface RxLife {

    void onPause();
    void onStop();
    void onDestroy();

}
