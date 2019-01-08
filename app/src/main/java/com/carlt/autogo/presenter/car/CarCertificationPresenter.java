package com.carlt.autogo.presenter.car;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UpdateImageResultInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Marlon on 2019/1/7.
 */
public class CarCertificationPresenter extends BasePresenter<ICarCertificationView> {
    MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    /**
     * 添加车辆
     */
    public void filter() {
        uuDialog.show();
        Gson gson = new Gson();
        if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {
            ToastUtils.showShort("网络错误，请检查网络");
            uuDialog.dismiss();
        }else {
            OkGo.<String>post(GlobalUrl.TEST_BASE_URL + "BrandProduct/AutoFilter")
                    .headers("Content-Type", "application/json")
                    .headers("Carlt-Access-Id", GlobalKey.TEST_ACCESSID)
                    .headers("Carlt-Token", SharepUtil.getPreferences().getString("token", ""))
                    .upJson(gson.toJson(new HashMap<>()))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            uuDialog.dismiss();
                            mView.selectCarSuccess(response.body());
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            uuDialog.dismiss();
                            LogUtils.e(response);
                        }


                    });
        }
    }

    /**
     * 图像上传
     *
     * @param file
     */
    @SuppressLint("CheckResult")
    public void updateImageFile(File file) {
        uuDialog.show();
        RequestBody requestBody = MultipartBodyBuilder
                .addFormDataPart("type", "autogo/face")
                .addFormDataPart("fileOwner", "face")
                .addFormDataPart("uid", "9999999999")
                .addFormDataPart("name", "faceImage")
                .addFormDataPart("faceImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        //图片上传
        ClientFactory.getUpdateImageService(UserService.class).updateImageFile(requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateImageResultInfo>() {
                    @Override
                    public void accept(UpdateImageResultInfo updateImageResultInfo) throws Exception {
                        uuDialog.dismiss();
                        mView.updateImageFileSuccess(updateImageResultInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    /**
     * 添加车辆 下一步按钮
     *
     * @return
     */
    @SuppressLint("CheckResult")
    public void addCar(Map<String, Object> map) {
        uuDialog.show();
        ClientFactory.def(CarService.class).addCar(map).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError err) throws Exception {
                        uuDialog.dismiss();
                        mView.addCarSuccess(err);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }
}
