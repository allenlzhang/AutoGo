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
      //  builder.baseUrl(GlobalUrl.BASE_URL2);
        retrofit = builder.build();
    }

    private static class SingletonHolder{
        private static DefulatClient2 instance = new DefulatClient2();

    }

    protected static DefulatClient2 getInstance(){

        return  SingletonHolder.instance ;
    }
}
