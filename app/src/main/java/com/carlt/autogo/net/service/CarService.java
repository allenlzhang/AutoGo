package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Marlon on 2018/11/26.
 */
public interface CarService {
    @POST("CarAuth/GetAuthSetting")
    Observable<CarAuthTimeInfo> getAuthSetting(@Body Map<String, Object> param);

    @POST("CarAuth/GetMyCarList")
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);
}
