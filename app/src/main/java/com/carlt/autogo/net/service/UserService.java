package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.RetrievePassword;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.entry.user.User;
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
    Observable<RetrievePassword> userRetrievePassword(@Body Map<String, Object> param);

    //获取用户信息
    @POST("User/GetUserInfo")
    Observable<UserInfo> getUserInfo(@Body Map<String, String> token);

    //编辑用户资料
    @POST("User/Edit")
    Observable<BaseError> userEditInfi(@Body Map<String, Object> params);

    //三方登录
    @POST("User/LoginByOpenApi")
    Observable<UserInfo> loginByOpenApi(@Body Map<String, Object> params);

    //手机短信登录
    @POST("User/LoginByCaptcha")
    Observable<User> loginByPhone(@Body Map<String, Object> params);

    //修改密码
    @POST("User/ResetPwd")
    Observable<User> userResetPwd(@Body Map<String, Object> params);

    //上传图片
    @POST("image/uploadOssImage")
    Observable<UpdateImageResultInfo> updateImageFile(@Body  RequestBody params);

    //获取短信验证码token
    @POST("User/GetSmsToken")
    Observable<SmsToken> getSmsToken(@Body Map<String,String> params);

    //发送短信验证码
    @POST("User/SendSmsCode")
    Observable<BaseError> SendSmsCode(@Body Map<String,Object> params);
}
