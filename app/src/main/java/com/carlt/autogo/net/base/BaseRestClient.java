package com.carlt.autogo.net.base;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.global.GlobalUrl;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseRestClient implements Iservice {
    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
    Retrofit.Builder builder = new Retrofit.Builder();
    Retrofit retrofit ;
    protected BaseRestClient() {
        //请求 超时 时间为5秒
        okBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okBuilder.readTimeout(15, TimeUnit.SECONDS);
        builder.baseUrl(GlobalUrl.BASE_URL)
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        creat();
    }

    abstract void creat();
    @Override
    public <T> T getService(Class<T> service) {
        return retrofit.create(service);
    }

    protected static Iservice getDefual(){
        return DefulatClient.getInstace();
    }

    protected static Iservice getDefual2(){
        return DefulatClient2.getInstance();
    }

}
