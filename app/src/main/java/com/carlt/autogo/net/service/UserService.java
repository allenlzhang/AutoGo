package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.LoginLogInfo;
import com.carlt.autogo.entry.user.ShareLoginList;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserIdentity;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.entry.user.UserRegister;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {


    //用户注册
    @POST("User/Register")
    Observable<UserRegister> userRegister(@Body Map<String, Object> param);

    //用户登录
    @POST("User/LoginByPwd")
    Observable<User> userLogin(@Body Map<String, Object> param);

    //找回密码
    @POST("User/RetrievePassword")
    Observable<BaseError> userRetrievePassword(@Body Map<String, Object> param);

    //获取用户信息
    @POST("User/GetUserInfo")
    Observable<UserInfo> getUserInfo(@Body Map<String, String> token);

    //编辑用户资料
    @POST("User/Edit")
    Observable<BaseError> userEditInfo(@Body Map<String, Object> params);

    //三方登录
    @POST("User/LoginByOpenApi")
    Observable<User> loginByOpenApi(@Body Map<String, Object> params);

    //手机短信登录
    @POST("User/LoginByCaptcha")
    Observable<User> loginByPhone(@Body Map<String, Object> params);

    //修改密码
    @POST("User/ResetPwd")
    Observable<BaseError> userResetPwd(@Body Map<String, Object> params);

    //上传图片
    @POST("image/uploadOssImage")
    Observable<UpdateImageResultInfo> updateImageFile(@Body RequestBody params);

    //获取短信验证码token
    @POST("SmsCode/GetSmsToken")
    Observable<SmsToken> getSmsToken(@Body Map<String, String> params);

    //校验验证码
    @POST("SmsCode/CheckSmsCode")
    Observable<BaseError> checkSmsCode(@Body Map<String, Object> params);

    //发送短信验证码
    @POST("SmsCode/SendSmsCode")
    Observable<BaseError> SendSmsCode(@Body Map<String, Object> params);

    //支付宝授权
    @POST("User/AlipayAuth")
    Observable<BaseError> authAliPay(@Body Map<String, String> params);

    //上传图片id
    @POST("User/SetFace")
    Observable<User> setFace(@Body Map<String, Object> params);

    @POST("User/CompareFace")
    Observable<BaseError> compareFace(@Body Map<String, Object> params);


    //设置远程密码
    @POST("User/SetRemotePassword")
    Observable<BaseError> SetRemotePassword(@Body Map<String, String> params);

    //修改远程密码
    @POST("User/ModifyRemotePassword")
    Observable<BaseError> modifyRemotePassword(@Body Map<String, String> params);

    //重置远程密码
    @POST("User/ResetRemotePassword")
    Observable<BaseError> resetRemotePassword(@Body Map<String, Object> params);

    //三方注册
    @POST("User/RegisterByOpenApi")
    Observable<BaseError> registerByOpenApi(@Body Map<String, Object> params);

    //三方绑定
    @POST("UserShare/ShareLoginBind")
    Observable<BaseError> shareLoginBind(@Body Map<String, Object> params);

    // 解绑第三方账号
    @POST("UserShare/ShareLoginUnbind")
    Observable<BaseError> shareLoginUnbind(@Body Map<String, Object> params);

    //获取已绑定所有第三方账号
    @POST("UserShare/GetShareLogin")
    Observable<ShareLoginList> getShareLogin(@Body Map<String, Object> params);

    //查询是否设置第三方登录
    @POST("UserShare/CheckShareLogin")
    Observable<BaseError> checkShareLogin(@Body Map<String, Object> params);

    //人脸登录
    @POST("User/LoginByFace")
    Observable<User> LoginByFace(@Body Map<String, Object> params);

    //免密开关
    @POST("User/ModifyRemoteSwitch")
    Observable<BaseError> modifyRemoteSwitch(@Body Map<String, Object> params);

    // 用户冻结、解冻
    @POST("User/Freeze")
    Observable<BaseError> freeze(@Body Map<String, Object> params);

    // 用户登录信息
    @POST("User/LoginLog")
    Observable<LoginLogInfo> loginLog(@Body Map<String, Object> params);

    // 注销
    @POST("User/UnRegister")
    Observable<BaseError> unRegister(@Body Map<String, Object> params);

    // 修改手机
    @POST("User/ResetMobile")
    Observable<BaseError> resetMobile(@Body Map<String, Object> params);

    //用户统一登录
    @POST("User/Login")
    Observable<User> commonLogin(@Body Map<String, Object> params);

    //用户统一注册
    @POST("User/Reg")
    Observable<BaseError> commonReg(@Body Map<String, Object> params);

    //验证旧手机
    @POST("User/AuthMobile")
    Observable<BaseError> authMobile(@Body Map<String, Object> params);

    //检测是否设置人脸
    @POST("User/CheckFace")
    Observable<User> checkFace(@Body Map<String, Object> params);

    // 身份证认证相关接口
    @POST("Identity/AddIdentity")
    Observable<BaseError> addIdentity(@Body Map<String, Object> params);

    @POST("Identity/GetIdentity")
    Observable<UserIdentity> getIdentity(@Body Map<String, Object> params);
}
