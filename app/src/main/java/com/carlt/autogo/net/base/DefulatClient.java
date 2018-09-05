package com.carlt.autogo.net.base;

import android.util.Log;

import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class DefulatClient  extends BaseRestClient {
    private DefulatClient() {
        super();
    }

    @Override
    void creat() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("http =",message) ;
            }
        });
        loggingInterceptor.setLevel(BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
         okBuilder.addInterceptor(loggingInterceptor);
    }

    @Override
    public <T> T getService(Class<T> service) {

        return SingletonHolder.retrofit.create(service);
    }

    private static class SingletonHolder{
        private static DefulatClient instace = new DefulatClient();
        private static  Retrofit retrofit = builder.build();

    }

    public static  DefulatClient newInstance () {
        return SingletonHolder.instace;
    }
}
