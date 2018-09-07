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
                LogUtils.e(message);
            }
        });
        loggingInterceptor.setLevel(BuildConfig.DEBUG_MODE? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
         okBuilder.addInterceptor(loggingInterceptor);
         builder.baseUrl(GlobalUrl.BASE_URL_TEST_YEMA);
         retrofit = builder.build();
    }




    private static class SingletonHolder{
        private static DefulatClient instance = new DefulatClient();

    }
    protected  static  DefulatClient getInstace(){
      return   SingletonHolder.instance;
    }

}
