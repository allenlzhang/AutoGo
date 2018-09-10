package com.carlt.autogo.net.base;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class DefulatClient  extends BaseRestClient {

   static   String URL_NEXT [] = { GlobalUrl.U1_DORIDE_TEST ,GlobalUrl.U1_DORIDE_PRE ,GlobalUrl.U1_DORIDE} ;
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

    @Override
    public void changeUri(int id) {
        if (id > 0 && id < URL_NEXT.length){
            retrofit = null ;
            builder.baseUrl(URL_NEXT [ id ]);
            retrofit = builder.build();
        }

    }

    private static class SingletonHolder{
        private static DefulatClient instance = new DefulatClient();

    }
    protected  static  DefulatClient getInstace(){
      return   SingletonHolder.instance;
    }


}
