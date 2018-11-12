package com.carlt.autogo.view.activity;

import android.annotation.SuppressLint;
import android.view.View;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.TokenUtil;
import com.carlt.autogo.view.activity.login.FaceLoginActivity;

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
        String token = SharepUtil.getPreferences().getString("token", "");
        int loginType = SharepUtil.getPreferences().getInt("loginType", -1);
        rlTitle.setVisibility(View.GONE);
        //判断token 是否过期，没有过期，直接请求获取用户信息，跳转主页，否则登录页面
        if (!TokenUtil.decodeToken(token)) {
            Map<String, String> map = new HashMap<>();

            map.put("token", token);
            ObservableHelper.getUserInfoByToken(map, loginType)
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            startActivity(MainActivity.class);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            go2Activity();
                            //                            startActivity(LoginActivity.class);
                        }
                    });

        } else {
            //            startActivity(LoginActivity.class);
            go2Activity();
        }


    }

    private void go2Activity() {
        UserInfo userInfo = SharepUtil.getBeanFromSp("user");
        boolean faceSwitch = SharepUtil.getPreferences().getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);

        if (userInfo != null) {
            if (userInfo.faceId != 0 && faceSwitch) {
                startActivity(FaceLoginActivity.class);
            } else {
                startActivity(LoginActivity.class);
            }
        } else {
            startActivity(LoginActivity.class);
        }
    }
}
