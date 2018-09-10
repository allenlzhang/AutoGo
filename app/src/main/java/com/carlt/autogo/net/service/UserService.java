package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.user.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    //用户发送手机验证码
    @FormUrlEncoded
    @POST("user/setValidate")
    Observable<Object> getValidate(@Field("mobile") String mobile);

    //用户注册
    @FormUrlEncoded
    @POST
    Observable<Object> userRegister(@FieldMap Map<String, Object> param);

    @GET("api/data/Android/10/1")
    Observable<User> getTest();
}
