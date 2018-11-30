package com.carlt.autogo.net.base;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.utils.SharepUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class DefulatClient extends BaseRestClient {

    //    static  String URL_NEXT[] = {GlobalUrl.TEST_BASE_URL, GlobalUrl.PRE_BASE_URL, GlobalUrl.FORMAL_BASE_URL};
    static  String URL_NEXT[]     = {GlobalUrl.TEST_BASE_URL, GlobalUrl.PRE_BASE_URL};
    static  String URL_ACCESSID[] = {GlobalKey.TEST_ACCESSID, GlobalKey.PRE_ACCESSID};
    //    static  String URL_ACCESSID[] = {GlobalKey.TEST_ACCESSID, GlobalKey.PRE_ACCESSID, GlobalKey.PRE_ACCESSID};
    private int    id             = 0;

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
        loggingInterceptor.setLevel(BuildConfig.DEBUG_MODE ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        okBuilder.addInterceptor(loggingInterceptor);

        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Carlt-Access-Id", GlobalKey.TEST_ACCESSID)
                        .header("Carlt-Token", SharepUtil.getPreferences().getString("token",""))
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        builder.baseUrl(GlobalUrl.TEST_BASE_URL)
                .client(okBuilder.build());
        retrofit = builder.build();
    }

    @Override
    public void changeUri(final int id) {
        LogUtils.e("====" + id);
        if (id < URL_NEXT.length) {
            retrofit = null;

            okBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Carlt-Access-Id", URL_ACCESSID[id])
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });
            builder.baseUrl(URL_NEXT[id])
                    .client(okBuilder.build());
            retrofit = builder.build();
        }

    }

    private static class SingletonHolder {
        private static DefulatClient instance = new DefulatClient();

    }

    protected static DefulatClient getInstace() {
        return SingletonHolder.instance;
    }


}
