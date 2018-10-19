package com.carlt.autogo.net.base;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DefulatClient  extends BaseRestClient {

   static   String URL_NEXT [] = { GlobalUrl.BASE_URL ,GlobalUrl.BASE_URL ,GlobalUrl.BASE_URL} ;
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
        loggingInterceptor.setLevel(BuildConfig.DEBUG_MODE? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
         okBuilder.addInterceptor(loggingInterceptor);

        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Carlt-Access-Id", "18644515396614518644")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
         builder.baseUrl(GlobalUrl.BASE_URL)
                 .client(okBuilder.build());
         retrofit = builder.build();
    }

    @Override
    public void changeUri(int id) {
        if ( id > 0 && id < URL_NEXT.length ){
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
