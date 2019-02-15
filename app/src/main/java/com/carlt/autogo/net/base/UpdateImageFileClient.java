package com.carlt.autogo.net.base;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.BuildConfig;
import com.carlt.autogo.global.GlobalUrl;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class UpdateImageFileClient extends BaseRestClient {

    static String URL_NEXT[] = {GlobalUrl.UPDATE_IMG_TEST_URL, GlobalUrl.UPDATE_IMG_FORMAL_URL, GlobalUrl.UPDATE_IMG_FORMAL_URL};

    private UpdateImageFileClient() {
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
        loggingInterceptor.setLevel(BuildConfig.DEBUG_MODE ? HttpLoggingInterceptor.Level.HEADERS : HttpLoggingInterceptor.Level.NONE);
        okBuilder.addInterceptor(loggingInterceptor);

        okBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        //                        .header("Carlt-Access-Id", "19877415356991399877")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        builder.baseUrl(GlobalUrl.UPDATE_IMG_TEST_URL)
                .client(okBuilder.build());
        retrofit = builder.build();
    }

    @Override
    public void changeUri(int id) {
        LogUtils.e("imgurl====" + URL_NEXT[id]);
        if (id > 0 && id < URL_NEXT.length) {
            retrofit = null;
            builder.baseUrl(URL_NEXT[id]);
            retrofit = builder.build();
        }

    }

    private static class SingletonHolder {
        private static UpdateImageFileClient instance = new UpdateImageFileClient();

    }

    protected static UpdateImageFileClient getInstace() {
        return SingletonHolder.instance;
    }


}
