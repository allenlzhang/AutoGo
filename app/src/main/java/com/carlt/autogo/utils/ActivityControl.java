package com.carlt.autogo.utils;

import android.app.Activity;
import android.content.Intent;

import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.view.activity.LoginActivity;
import com.carlt.autogo.view.activity.login.FaceLoginActivity;

import java.util.ArrayList;

/**
 * Description: activity管理类
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/10/12 15:43
 */
public class ActivityControl {
    public static ArrayList<Activity> mActivityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        if (null != activity) {
            mActivityList.add(activity);

        }
    }

    public static void removeAllActivity(Activity currentActivity) {
        for (Activity activity : mActivityList) {

            if (currentActivity.equals(activity)) {
                continue;
            }
            activity.finish();
        }
    }

    public static void removeAll() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    public static void removeFreezeActivity() {
        for (Activity a : mActivityList) {
            if (a.getLocalClassName().equals("view.activity.more.safety.FreezeActivity")) {
                a.finish();
            }
        }
    }

    public static void logout(final Activity context) {

        CommonDialog.createDialogNotitle(context, "确定退出登录？", "", "取消", "确定", true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                SharepUtil.put(GlobalKey.USER_TOKEN, "");
                SingletonCar.getInstance().initCar();
                //                SharepUtil.putBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);
                //                SharepUtil.putBoolean(GlobalKey.PROCESS_SAFE_SWITCH, false);
                UserInfo userInfo = SharepUtil.getBeanFromSp("user");
                userInfo.password = "";
                SharepUtil.putByBean(GlobalKey.USER_INFO, userInfo);
                boolean faceSwitch = SharepUtil.getPreferences().getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);
                //                if (userInfo != null) {
                Intent intent;
                if (userInfo.faceId != 0 && faceSwitch) {
                    intent = new Intent(context, FaceLoginActivity.class);
                } else {
                    intent = new Intent(context, LoginActivity.class);
                }
                context.startActivity(intent);
                context.finish();
                //                }

            }
        });
    }


}
