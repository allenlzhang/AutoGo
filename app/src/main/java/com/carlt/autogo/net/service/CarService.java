package com.carlt.autogo.net.service;

import com.carlt.autogo.entry.car.ActivateStepInfo;
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
    //轮询查询过户状态
    @POST("Transfer/CheckStatus")
    Observable<CarBaseInfo> checkTransferStatus(@Body Map<String, Object> param);

    //获取授权id获取授权信息
    @POST("Transfer/GetById")
    Observable<CarBaseInfo> getByTransferId(@Body Map<String, Object> param);

    //发起过户
    @POST("Transfer/Launch")
    Observable<CarBaseInfo> launchTransfer(@Body Map<String, Object> param);

    //扫描过户二维码
    @POST("Transfer/ScanCode")
    Observable<CarBaseInfo> scanTransferCode(@Body Map<String, Object> param);

    //处理过户
    @POST("Transfer/Deal")
    Observable<CarBaseInfo> dealTransferCode(@Body Map<String, Object> param);

    //获取授权配置
    @POST("CarAuth/GetAuthSetting")
    Observable<CarAuthTimeInfo> getAuthSetting(@Body Map<String, Object> param);

    //校验二维码状态
    @POST("CarAuth/CheckStatus")
    Observable<CarBaseInfo> checkStatus(@Body Map<String, Object> param);

    //获取授权id获取授权信息
    @POST("CarAuth/GetById")
    Observable<CarBaseInfo> getById(@Body Map<String, Object> param);

    //同意授权/拒绝授权
    @POST("CarAuth/ModifyStatus")
    Observable<CarBaseInfo> modifyStatus(@Body Map<String, Object> param);

    //获取我的车辆和被授权车辆
    @POST("CarAuth/GetMyCarList")
    Observable<AuthCarInfo> getMyCarList(@Body Map<String, Object> param);

    //生成授权二维码
    @POST("CarAuth/CreateAuthQrcode")
    Observable<CarBaseInfo> createAuthQrcode(@Body Map<String, Object> param);

    //扫描二维码
    @POST("CarAuth/ScanQrcode")
    Observable<CarBaseInfo> scanQrcode(@Body Map<String, Object> param);

    //获取车系车型
    @POST("BrandProduct/GetModel")
    Observable<CarModelInfo> getModel(@Body Map<String, Integer> param);

    //获取车款
    @POST("BrandProduct/GetBrandCar")
    Observable<CarBrandInfo> getBrandCar(@Body Map<String, Integer> param);

    //车辆认证
    @POST("Bind/AddCar")
    Observable<BaseError> addCar(@Body Map<String, Object> param);

    //获取车辆信息
    @POST("Car/GetCarInfo")
    Observable<CarInfo> getCarInfo(@Body Map<String, Integer> param);

    //编辑车辆
    @POST("Car/Modify")
    Observable<BaseError> modify(@Body Map<String, Object> param);

    //取消授权
    @POST("CarAuth/CancelAuth")
    Observable<BaseError> cancelAuth(@Body Map<String, Integer> param);

    // 激活设备
    @POST("DeviceActive/active")
    Observable<BaseError> active(@Body Map<String, Object> param);

    // 获取激活日志
    @POST("DeviceActive/getLogs")
    Observable<ActivateStepInfo> getLogs(@Body Map<String, Object> param);
}
