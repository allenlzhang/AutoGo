package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.user.BaseError;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Marlon on 2018/11/26.
 */
public interface CarService {

    @POST("Transfer/CheckStatus")
        //轮询查询过户状态
    Observable<CarBaseInfo> checkTransferStatus(@Body Map<String, Object> param);

    @POST("Transfer/Launch")
        //发起过户
    Observable<CarBaseInfo> launchTransfer(@Body Map<String, Object> param);

    @POST("Transfer/ScanCode")
        //扫描过户二维码
    Observable<CarBaseInfo> scanTransferCode(@Body Map<String, Object> param);

    @POST("Transfer/Deal")
        //处理过户
    Observable<CarBaseInfo> dealTransferCode(@Body Map<String, Object> param);

    @POST("CarAuth/GetAuthSetting")
        //获取授权配置
    Observable<CarAuthTimeInfo> getAuthSetting(@Body Map<String, Object> param);

    @POST("CarAuth/CheckStatus")
        //校验二维码状态
    Observable<CarBaseInfo> checkStatus(@Body Map<String, Object> param);

    @POST("CarAuth/GetById")
        //获取授权id获取授权信息
    Observable<CarBaseInfo> getById(@Body Map<String, Object> param);


    @POST("CarAuth/ModifyStatus")
        //同意授权/拒绝授权
    Observable<CarBaseInfo> modifyStatus(@Body Map<String, Object> param);

    @POST("CarAuth/GetMyCarList")
        //获取我的车辆和被授权车辆
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);

    @POST("CarAuth/CreateAuthQrcode")
        //生成授权二维码
    Observable<CarBaseInfo> createAuthQrcode(@Body Map<String, Object> param);

    @POST("CarAuth/ScanQrcode")
        //扫描二维码
    Observable<CarBaseInfo> scanQrcode(@Body Map<String, Object> param);

    @POST("BrandProduct/GetModel")
        //获取车系车型
    Observable<CarModelInfo> getModel(@Body Map<String, Integer> param);

    @POST("BrandProduct/GetBrandCar")
        //获取车款
    Observable<CarBrandInfo> getBrandCar(@Body Map<String, Integer> param);

    @POST("Bind/AddCar")
        //车辆认证
    Observable<BaseError> addCar(@Body Map<String, Object> param);

    @POST("Car/GetCarInfo")
        //获取车辆信息
    Observable<CarInfo> getCarInfo(@Body Map<String, Integer> param);

    @POST("CarAuth/CancelAuth")//取消授权
    Observable<BaseError> cancelAuth(@Body Map<String,Integer> param);
}
