package com.carlt.autogo.net.base;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;
import com.flyco.tablayout.widget.MsgView;
import com.google.gson.internal.bind.MapTypeAdapterFactory;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Retrofit;
import retrofit2.http.Headers;

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
                        .header("Carlt-Access-Id", "19877415356991399877")
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
