package com.carlt.autogo.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.user.UserBindPhoneActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 * @time 14:27  2018/10/10/010
 * @describe  主要是提取用户相关的常用重复接口
 */
public class UserPresenter {

   public static  String errorMsg ;
    static String[] sexs = {"男","女","保密"};
    /**
     * 根据token 获取用户信息
     */
   public static Observable<String> getUserInfoByToken(Map<String, String> token){

       return  ClientFactory.def(UserService.class).getUserInfo(token)
               .map(new Function<UserInfo, String>() {
           @Override
           public String apply(UserInfo userInfo) throws Exception {
               if(userInfo.err != null){
                   errorMsg = userInfo.err.msg ;
                   return null;
               }else {
                   int index = userInfo.gender-1;

                   if(index>=0 && index < sexs.length){
                       userInfo.sex = sexs[index];
                   }
                   LogUtils.e(userInfo.sex);
                   SharepUtil.<UserInfo>putByBean("user", userInfo) ;
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
    public static Observable<BaseError> sendValidate(final String phoneNum, Map<String, String> params ,final int type){

      return   ClientFactory.def(UserService.class).getSmsToken(params)
                .flatMap(new Function<SmsToken, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(SmsToken smsToken) throws Exception {
                        if (smsToken.msg != null){
                            ToastUtils.showShort(smsToken.msg.msg);
                            return null;
                        }else {
                            String token = smsToken.token;
                            Map<String, Object> map = new HashMap();
                            map.put("mobile", phoneNum);
                            map.put("type", type);
                            map.put("smsToken", token);
                            return ClientFactory.def(UserService.class).SendSmsCode(map);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }



    /**
     * 用户三方登录
     */
    @SuppressLint("CheckResult")
    public static Observable<String> loginByOpenApi(final Map<String, Object>  params ,final BaseMvpActivity baseMvpActivity){

     return    ClientFactory.def(UserService.class).loginByOpenApi(params)
                .flatMap(new Function<User, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(User user) throws Exception {
                        if(user.err != null){
                            ToastUtils.showShort(user.err.msg);
                            String uId  = (String) params.get("openId");
                            Intent intent  = new Intent(baseMvpActivity,UserBindPhoneActivity.class);
                            intent.putExtra("openId",uId);
                            intent.putExtra("openType",1);
                            baseMvpActivity. startActivity(intent);
                            return  null ;
                        }else {

                            Map<String, String> token =   new HashMap<String, String>();
                            token.put("token",user.token);
                            SharepUtil.put(GlobalKey.USER_TOKEN,user.token);
                            return ClientFactory.def(UserService.class).getUserInfo(token);

                        }
                    }
                })
                .map(new Function<UserInfo, String>() {
                    @Override
                    public String apply(UserInfo userInfo) throws Exception {
                        if(userInfo.err != null){
                            errorMsg = userInfo.err.msg ;
                            return null;
                        }else {
                            SharepUtil.<UserInfo>putByBean("user", userInfo) ;
                            errorMsg = "" ;
                            return "登录成功";
                        }

                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }



}
