package com.carlt.autogo.view.activity;

import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.TokenUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseMvpActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }


    @SuppressLint("CheckResult")
    @Override
    public void init() {
        String token = SharepUtil.getPreferences().getString("token","");
        int loginType = SharepUtil.getPreferences().getInt("loginType",-1);

        //判断token 是否过期，没有过期，直接请求获取用户信息，跳转主页，否则登录页面
        if(!TokenUtil.decodeToken(token)){
            Map<String, String> map = new HashMap<String, String>();

            map.put("token", token);
            ObservableHelper.getUserInfoByToken(map,loginType)
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            startActivity(MainActivity.class);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            startActivity(LoginActivity.class);
                        }
                    });

        }else {
            startActivity(LoginActivity.class);
        }


    }
}
