package com.carlt.autogo.net.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.net.base.myretrofit.MyGsonConverterFactory;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.view.activity.LoginActivity;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


public abstract class BaseRestClient implements Iservice {
    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
    Retrofit.Builder     builder   = new Retrofit.Builder();
    Retrofit             retrofit;

    protected BaseRestClient() {
        //请求 超时 时间为5秒
        okBuilder.connectTimeout(15, TimeUnit.SECONDS);
        okBuilder.readTimeout(15, TimeUnit.SECONDS);
        builder.baseUrl(GlobalUrl.TEST_BASE_URL)
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        creat();
    }

    abstract void creat();

    @SuppressLint("CheckResult")
    @Override
    public <T> T getService(final Class<T> service) {

        final T t = retrofit.create(service);

        Object o = Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, final Object[] args) throws Throwable {
                if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {

                    //                    Toast.makeText(AutoGoApp.mAppContext,"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
                    ToastUtils.showShort("网络错误，请检查网络");
                    return Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                            ObservableHelper.errorMsg = "网络错误,请检查网络";
                            emitter.onNext(new Object());
                        }
                    });
                }
                Object o = method.invoke(t, args);
                if (o instanceof Observable) {
                    Observable observable = (Observable) o;
                    return observable
                            .filter(new Predicate() {
                                @Override
                                public boolean test(Object o) throws Exception {

                                    LogUtils.e(o + "==========================================");
                                    if (o instanceof BaseError) {
                                        BaseError error = (BaseError) o;
                                        if (error != null && error.code != 0) {

                                            if (error.code == GlobalKey.TOKEN_OUT || error.code == GlobalKey.TOKEN_ERROR || error.code == GlobalKey.TOKEN_ERROR1) {
                                                ActivityControl.removeAll();
                                                Activity activity = ActivityControl.mActivityList.get(0);
                                                Intent intent = new Intent(activity, LoginActivity.class);
                                                ToastUtils.showShort(R.string.token_err);
                                                activity.startActivity(intent);

                                                return false;
                                            } else {
                                                return true;
                                            }

                                        } else {
                                            return true;
                                        }

                                    } else {
                                        return doelse(o);

                                    }
                                }
                            })
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return o;

            }
        });

        return (T) o;

    }

    private boolean doelse(Object o) {

        Class classO = o.getClass();
        try {
            Field field = classO.getField("err");
            BaseError baseError = (BaseError) field.get(o);

            if (baseError != null && baseError.code != 0) {
                if (baseError.code == GlobalKey.TOKEN_OUT || baseError.code == GlobalKey.TOKEN_ERROR || baseError.code == GlobalKey.TOKEN_ERROR1) {
                    ActivityControl.removeAll();
                    Activity activity = ActivityControl.mActivityList.get(0);
                    Intent intent = new Intent(activity, LoginActivity.class);
                    ToastUtils.showShort(R.string.token_err);
                    activity.startActivity(intent);

                    return false;
                } else {
                    return true;
                }

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
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
