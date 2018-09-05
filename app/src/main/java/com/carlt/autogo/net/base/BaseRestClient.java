package com.carlt.autogo.net.base;

import android.util.Log;

import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//网络配置类
public abstract class BaseRestClient implements Iservice {
    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
    static Retrofit.Builder builder = new Retrofit.Builder();

    public BaseRestClient() {
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


}
