package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.car.AddCarInfo;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarModelInfo;

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

    @POST("CarAuth/CheckStatus")
    Observable<CarBaseInfo> checkStatus(@Body Map<String, Object> param);

    @POST("CarAuth/GetById")
    Observable<CarBaseInfo> getById(@Body Map<String, Object> param);

    @POST("CarAuth/ModifyStatus")
    Observable<CarBaseInfo> modifyStatus(@Body Map<String, Object> param);

    @POST("CarAuth/GetMyCarList")
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);

    @POST("CarAuth/CreateAuthQrcode")
    Observable<CarBaseInfo> createAuthQrcode(@Body Map<String, Object> param);

    @POST("CarAuth/ScanQrcode")
    Observable<CarBaseInfo> scanQrcode(@Body Map<String, Object> param);

    @POST("BrandProduct/GetModel")
    Observable<CarModelInfo> getModel(@Body Map<String, Integer> param);

    @POST("BrandProduct/GetBrandCar")
    Observable<CarBrandInfo> getBrandCar(@Body Map<String, Integer> param);

    @POST("Bind/AddCar")
    Observable<AddCarInfo> addCar(@Body Map<String, Object> param);


}
