package com.carlt.autogo.net.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.net.base.myretrofit.MyGsonConverterFactory;
import com.carlt.autogo.net.service.UserService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseRestClient implements Iservice {
    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
    Retrofit.Builder     builder   = new Retrofit.Builder();
    Retrofit retrofit;
    protected BaseRestClient() {
        //请求 超时 时间为5秒
        okBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okBuilder.readTimeout(15, TimeUnit.SECONDS);
        builder.baseUrl(GlobalUrl.BASE_URL)
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        creat();
    }

    abstract void creat();


    @SuppressLint("CheckResult")
    @Override
    public <T> T getService(final Class<T> service){
        final   T t =   retrofit.create(service);
        Object o = Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {

                    Toast.makeText(AutoGoApp.mAppContext,"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
                    return Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        }
                    });
                }
                Object o =  method.invoke(t,args);
                if(o instanceof  Observable){
                    Observable observable = (Observable) o;
                    return  observable
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return o;

            }
        });

        return (T) o;

    }
    protected static Iservice getDefual() {
        return DefulatClient.getInstace();
    }
    protected static Iservice getUpdateImage() {
        return UpdateImageFileClient.getInstace();
    }
    protected static Iservice getDefual2() {
        return DefulatClient2.getInstance();
    }

}
