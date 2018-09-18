package com.carlt.autogo.application;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.mob.MobSDK;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:26
 */
public class AutoGoApp extends Application {
    public static Context  mAppContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        initUtils();
        //shareSdk配置
        MobSDK.init(this);

    }

    private void initUtils() {
        Utils.init(this);
        LogUtils.Config config = LogUtils.getConfig();
        config.setGlobalTag("AutoGo");
        //log总开关
        config.setLogSwitch(true);
    }
}
