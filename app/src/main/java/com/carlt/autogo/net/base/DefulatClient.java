package com.carlt.autogo.net.base;

import com.blankj.utilcode.util.LogUtils;
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
                LogUtils.d(message);
            }
        });
        loggingInterceptor.setLevel(BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
         okBuilder.addInterceptor(loggingInterceptor);
         retrofit = builder.build();
    }




    private static class SingletonHolder{
        private static DefulatClient instance = new DefulatClient();

    }
    protected  static  DefulatClient getInstace(){
      return   SingletonHolder.instance;
    }

}
