package com.carlt.autogo.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.baidu.idl.face.platform.utils.Base64Utils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.TokenUtil;
import com.carlt.autogo.view.activity.user.UserBindPhoneActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author wsq
 */
public class ObservableHelper {

    public static String   errorMsg = "";
    static        String[] sexs     = {"男", "女", "保密"};

    /**
     * @param params
     *         接口参数
     * @return
     */
    public static Observable<String> commonLogin(final Map<String, Object> params) {
        return commonLogin(params, null);
    }

    ;

    public static Observable<String> commonLogin(final Map<String, Object> params, final Context context) {
        params.put("version", AutoGoApp.VERSION);
        params.put("moveDeviceName", AutoGoApp.MODEL_NAME);
        params.put("loginModel", AutoGoApp.MODEL);
        params.put("loginSoftType", "Android");
        params.put("moveDeviceid", AutoGoApp.IMEI);

      //  判断token 是否过期   false 没有过期   true，token 过期
//        if(!SharepUtil.getPreferences().getBoolean(GlobalKey.TOKENT_OUTOF_TIME,false)){
//            String token = SharepUtil.getPreferences().getString("token","");
//            HashMap map = new HashMap() ;
//            map.put("token", token);
//            return ObservableHelper.getUserInfoByToken(map, (int) params.get("loginType"), (String) params.get("pwdReally"));
//        }

        return ClientFactory.def(UserService.class).commonLogin(params)
                .flatMap(new Function<User, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(User User) throws Exception {
                        if (User.err != null) {
                            errorMsg = User.err.msg;
                            if (context != null) {
                                String uId = (String) params.get("openId");
                                Intent intent = new Intent(context, UserBindPhoneActivity.class);
                                intent.putExtra("openId", uId);
                                intent.putExtra("openType", (Integer) params.get("openType"));
                                //保存微信或者支付宝登录请求参数
                                context.startActivity(intent);
                            }
                            return null;
                        } else {
                            Map<String, String> token = new HashMap<String, String>();

                            token.put("token", User.token);;
                            SharepUtil.put(GlobalKey.USER_TOKEN, User.token);
                            SharepUtil.putInt(GlobalKey.LOGINTYPE, (Integer) params.get("loginType"));

                            return ObservableHelper.getUserInfoByToken(token, (int) params.get("loginType"), (String) params.get("pwdReally"));
                        }

                    }
                });
    }

    /**
     * @param params
     *         通用注册方法
     * @return
     */
    public static Observable<String> commonReg(final Map<String, Object> params) {

        return commonReg(params, null);
    }

    public static Observable<String> commonReg(final Map<String, Object> params, final Context context) {
        params.put("version", AutoGoApp.VERSION);
        params.put("moveDeviceName", AutoGoApp.MODEL_NAME);
        params.put("loginModel", AutoGoApp.MODEL);
        params.put("loginSoftType", "Android");

        return ClientFactory.def(UserService.class).commonReg(params)
                .flatMap(new Function<BaseError, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(BaseError baseError) throws Exception {
                        if (baseError.code == 0) {
                            params.remove("validate");
                            int type = (int) params.get("regType");
                            if (type == (GlobalKey.RegStateByPWd)) {
                                params.put("loginType", GlobalKey.loginStateByPWd);
                                return ObservableHelper.commonLogin(params);
                            } else {
                                params.put("loginType", GlobalKey.loginStateByOther);
                                return ObservableHelper.commonLogin(params, context);
                            }

                        } else {
                            ToastUtils.showShort(baseError.msg);
                            return null;
                        }
                    }
                });

    }

    /**
     * 根据token 获取用户信息
     */
    public static Observable<String> getUserInfoByToken(Map<String, String> token, final int loginState) {

        return getUserInfoByToken(token, loginState, "");
    }

    public static Observable<String> getUserInfoByToken(Map<String, String> token, final int loginState, final String pwd) {

        return ClientFactory.def(UserService.class).getUserInfo(token)
                .map(new Function<UserInfo, String>() {
                    @Override
                    public String apply(UserInfo userInfo) throws Exception {
                        if (userInfo.err != null) {
                            errorMsg = userInfo.err.msg;
                            return null;
                        } else {
                            int index = userInfo.gender - 1;

                            if (index >= 0 && index < sexs.length) {
                                userInfo.sex = sexs[index];
                                userInfo.loginState = loginState;
                                userInfo.password = pwd;
                            }
                            SharepUtil.<UserInfo>putByBean("user", userInfo);
                            GlobalKey.FACE_LOGIN_SWITCH = userInfo.mobile;
                            GlobalKey.Remote_Switch = userInfo.mobile.concat("Remote_Switch");
                        }
                        errorMsg = "";
                        return "登录成功";
                    }
                });
    }


    /**
     * 用户发送验证码
     */
    //发送验证码类型(1=注册,2=找回密码,3=修改密码,4=修改手机,5=绑定微信,6=修改手机[旧手机号],7=远程密码重置,8=车辆过户,9=主机认证,10=更换设备,11=登录,12=注销)
    @SuppressLint("CheckResult")
    public static Observable<BaseError> sendValidate(final String phoneNum, Map<String, String> params, final int type) {

        return ClientFactory.def(UserService.class).getSmsToken(params)
                .flatMap(new Function<SmsToken, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(SmsToken smsToken) throws Exception {
                        if (smsToken.msg != null) {
                            ToastUtils.showShort(smsToken.msg.msg);
                            return null;
                        } else {
                            String token = smsToken.token;
                            Map<String, Object> map = new HashMap();
                            map.put("mobile", phoneNum);
                            map.put("type", type);
                            map.put("smsToken", token);
                            return ClientFactory.def(UserService.class).SendSmsCode(map);
                        }
                    }
                });

    }


    /**
     * 用户三方登录
     */
    @SuppressLint("CheckResult")
    public static Observable<String> loginByOpenApi(final Map<String, Object> params, final BaseMvpActivity baseMvpActivity, final int openType) {


        return ClientFactory.def(UserService.class).loginByOpenApi(params)
                .flatMap(new Function<User, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(User user) throws Exception {
                        if (user.err != null) {
                            // ToastUtils.showShort(user.err.msg);
                            String uId = (String) params.get("openId");
                            if (baseMvpActivity != null) {
                                Intent intent = new Intent(baseMvpActivity, UserBindPhoneActivity.class);
                                intent.putExtra("openId", uId);
                                intent.putExtra("openType", openType);
                                //保存微信或者支付宝登录请求参数
                                baseMvpActivity.startActivity(intent);
                            }

                            return null;
                        } else {

                            Map<String, String> token = new HashMap<String, String>();
                            token.put("token", user.token);
                            SharepUtil.put(GlobalKey.USER_TOKEN, user.token);
                            return ClientFactory.def(UserService.class).getUserInfo(token);

                        }
                    }
                })
                .map(new Function<UserInfo, String>() {
                    @Override
                    public String apply(UserInfo userInfo) throws Exception {
                        if (userInfo.err != null) {
                            errorMsg = userInfo.err.msg;
                            return null;
                        } else {
                            userInfo.loginState = GlobalKey.loginStateByOther;
                            SharepUtil.<UserInfo>putByBean("user", userInfo);
                            errorMsg = "";
                            return "登录成功";
                        }

                    }
                });
    }

}
