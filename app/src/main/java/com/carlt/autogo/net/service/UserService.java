package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.RetrievePassword;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.entry.user.UserRegister;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {


    //用户注册
    @POST("User/Register")
    Observable<UserRegister> userRegister(@Body Map<String, Object> param);

    //用户登录
    @POST("User/LoginByPwd")
    Observable<User>userLogin(@Body Map<String, Object> param);

    //修改密码
    @POST("User/RetrievePassword")
    Observable<RetrievePassword> userRetrievePassword (@Body Map<String, Object> param);

    //获取用户信息
    @POST("User/GetUserInfo")
    Observable<UserInfo>getUserInfo(@Body Map<String,String> token);

    //编辑用户资料
    @POST("User/Edit")
    Observable<BaseError>userEditInfi(@Body Map<String,Object> params) ;
}
