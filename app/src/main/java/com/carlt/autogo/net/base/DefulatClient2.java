package com.carlt.autogo.net.base;

import android.util.Log;

import com.carlt.autogo.global.GlobalUrl;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefulatClient2 extends BaseRestClient {
    private DefulatClient2() {
        super();
    }

    @Override
    void creat() {
    }

    @Override
    public <T> T getService(Class<T> service) {
        return SingletonHolder.retrofit.create(service);
    }

    private static class SingletonHolder{
        private static DefulatClient2 instace = new DefulatClient2();
        private static Retrofit retrofit = builder.build();


    }

    public static  DefulatClient2 newInstance () {
        return SingletonHolder.instace;

    }
}
