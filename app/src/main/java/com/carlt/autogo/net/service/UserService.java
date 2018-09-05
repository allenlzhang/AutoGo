package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.user.User;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    //用户输入密码后的接口
    @FormUrlEncoded
    @POST("RobotV1/TerminalLogin/InputPsw")
    Observable<User> inPutPsw(@FieldMap Map<String,Object> params);

    @GET("api/data/Android/10/1")
    Observable<User> getTest();
}
