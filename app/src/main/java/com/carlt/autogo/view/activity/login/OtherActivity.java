package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.alipay.AuthResult;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.presenter.register.IOtherRegisterView;
import com.carlt.autogo.presenter.register.OtherRegisterPresenter;
import com.carlt.autogo.utils.alipay.OrderInfoUtil2_0;
import com.carlt.autogo.view.activity.MainActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


@CreatePresenter(presenter = OtherRegisterPresenter.class)
public class OtherActivity extends BaseMvpActivity implements IOtherRegisterView {

    @BindView(R.id.login_payment)
    ImageView loginPayment;

    @BindView(R.id.login_wechat)
    ImageView loginWechat;
    private Platform plat;
    UUDialog dialog;

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018090661230641";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID       = "2088131979649430";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = System.currentTimeMillis() + "";
    Gson gson = new Gson();
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE  = "MIIEowIBAAKCAQEA1LUwhOyN1hOnrV3g2COyqpcUax8Fq1/0Xuy74ZphNpszZ21c\n" +
            "2CnzXjzbD54kyW8EOOGeDPJvNsWrjyas9VsSxE0nXVARz3yyMBMtNEq42+o8Sd+F\n" +
            "l2PWHOc/uH3BjXM80kmqvonD5sGCYR4wRcThXKuZR9t0ahEZkiH5WNFghSyQEWXH\n" +
            "xvoNNfR5sElArZLS86Ey1R7U5Z0BX9GcRyfXldER61oA71EfOnLoh/XXjtr78ja4\n" +
            "Iq7XJKiYkXqKNTSgHhbQe8g8Qw7xb040Dw9ljOV99q4RoTlQUp7NEmwQYne+b920\n" +
            "TFm67AMVCL9KPCLZneuwfIyXiLidy4RDcWusPwIDAQABAoIBAHxxfO9njhu+6Ayv\n" +
            "rcoGKJNRTGrXfXOj8c5PZiQ5M8LEzjAmdXkqIe3yYJ8kgJUw1CGLLfKZK6O5xgM8\n" +
            "N1hcbgBWuz8Gh0UCySUDMyEyzjArPBZkJT5K9It4Z3lCaaid0Omem7xZ0TkB4Yt8\n" +
            "I/XNl6Ol6Ul3BQbA9lrecYkbBgMi26g/ISF/Ul93aN2/M+IVqdyHL3b2MJ5N5uSU\n" +
            "ed+/JVzes3Kfl+vkMV4PuusO7MIAkA7D3EkZmP2Az2Y8cN2QeLoQIZn+7jZAoSTZ\n" +
            "rDOKBH36PcnPTUK2qoDbhAujOTL0KLT7x83I4eiBLxGAxiPa2Tfi6UKbOh1/ldHC\n" +
            "1jTjoaECgYEA95YM03LpZHgvEcIoqfUYKn20rdjQP1ORIXUMrWQWb5x0dLedst3v\n" +
            "AN8pqxXtuJNsSwyyxxH5hu5s4Q0M1JFE/lVPs/M1KrfhgFWOksbLfKj0qbJlOYJU\n" +
            "Dls49AY7UTUWSJhcZqoX9o5tlt+h5RyOYR9hlwLAOxg10e6AM7ut1KkCgYEA2++0\n" +
            "Y9bcD4Eso8JvJl53WMQCGUnhOMznuAAPGHEMib62d7krkA0OVI6HjeUgaYq7vYUk\n" +
            "ynsHlWpriwegtmozL3YdJnsjJsSQs9Fyfj96KjBl4zV+tIG9GHeGSnOC58HjqMOy\n" +
            "1uklZJXBPeK3WZTsh6VG7+VsnJW6qJniKDO7oqcCgYB49TPa8h8YWKcWU/ct4XIn\n" +
            "ADcPYAeNiLQ01kyVQXq8mIFErJg81LR5ho2C6jmznUKg/9kv7JYt17oV0RxdlYNR\n" +
            "hjhkQAPPlPp0ilgXWUv84UZ2yZMun3+Va/fNMGY2bGswmE88UQlYCYl81lDv8B2X\n" +
            "UY+KCWwE8vUIVS8JFz370QKBgA4XaqBm+MyJgzmu0H5N2Sm3zUafhC2me0Y/wrfX\n" +
            "Lm146an8FD1ziLKEO00tHAmHGQZIjJXokhDRs7hBsdteLI9i6vU3OIZYW8kZp1NN\n" +
            "b99aIH0RRV/9XAdASTtl8UI2GwcKGuBIKdwq+Cml6Ad2Uj1SqpveQDo8IayBx4q5\n" +
            "3dDLAoGBAJ17zUU/ko8fFxcOwlyPnDIMaXsIxjQ/e726+WEB/3c+Zlnazy2yzxiy\n" +
            "dwWsWi0aGRfYMzU7XxYk1853zsJEosgvJLbqlu8Cj8uFj/E02f4E++rDW5JGz7ez\n" +
            "8WIzdo8RIXSTegItyq/XSMeFthpZVgNfwM7pRR1UAb0+IACP1XKs";



    HashMap<String, Object> params = new HashMap<>();
    @PresenterVariable
    private OtherRegisterPresenter otherRegisterPresenter;
    private String                 errorMsg;

    @Override
    protected int getContentView() {
        return R.layout.activity_other;
    }

    @Override
    public void init() {
        setTitleText("第三方登录");
        setBaseBackStyle(getResources().getDrawable(R.drawable.common_close_select));
        Platform[] list = ShareSDK.getPlatformList();
        if (list != null) {
            plat = list[0];
        }

        dialog = new UUDialog(this, R.style.DialogCommon);
        params.put("moveDeviceName", AutoGoApp.MODEL_NAME);
        params.put("loginModel", AutoGoApp.MODEL);
        params.put("loginSoftType", "Android");
    }

    @OnClick({R.id.login_payment, R.id.login_wechat})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login_payment:
                otherRegisterPresenter.paymentLogin();
                authV2();
                break;

            case R.id.login_wechat:
            //    dialog.show();
                otherRegisterPresenter.weChatLogin();
                doAuthorize(plat, new MyPlatformActionListener(dialog));
                break;
        }

    }

    public static void doAuthorize(Platform plat ,MyPlatformActionListener listener) {

        plat.setPlatformActionListener((listener));
        plat.removeAccount(true);
        plat.SSOSetting(false);
        plat.authorize();

    }

    public class MyPlatformActionListener implements PlatformActionListener {
        Dialog dialog ;

       public MyPlatformActionListener(Dialog dialog) {
           this.dialog = dialog;
       }

       @Override
        public void onComplete(final Platform platform, int i, final HashMap<String, Object> hashMap) {
            LogUtils.e("onComplete");

            platform.setPlatformActionListener(new PlatformActionListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onComplete(Platform platform, final int i, HashMap<String, Object> hashMap) {
                    LogUtils.e(hashMap);
                    final String unionid = (String) hashMap.get("unionid");

                    params.put("openId", unionid);
                    params.put("openType", 2);
                    params.put("loginType", GlobalKey.loginStateByOther);
                    LogUtils.e(params);
                    ObservableHelper.commonLogin(params ,OtherActivity.this)
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    dialog.dismiss();
                                    ToastUtils.showShort(s);
                                    startActivity(MainActivity.class);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    dialog.dismiss();
                                    //   ToastUtils.showShort(ObservableHelper.errorMsg);
                                    LogUtils.e(throwable.toString());
                                }
                            });

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    dialog.dismiss();
                    ToastUtils.showShort("登录失败");

                    LogUtils.e(throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    dialog.dismiss();
                    ToastUtils.showShort("登录取消");
                }
            });
            platform.showUser(null);

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            throwable.printStackTrace();
            dialog.dismiss();

            String error = throwable.toString();
            LogUtils.e(error);
            if(error.contains("NotExistException")){
                ToastUtils.setMsgTextSize(13);
                ToastUtils.showShort("请先在应用商店下载该应用再进行该操作");

            }  else if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {
                ToastUtils.showShort("网络错误，请检查网络");

            }

            else {
                ToastUtils.showShort("登录失败");
            }

            LogUtils.e(throwable.toString());

        }

        @Override
        public void onCancel(Platform platform, int i) {
            ToastUtils.showShort("取消登录");
            dialog.dismiss();
        }
    }

    /**
     * 支付宝账户授权业务
     * @param
     */
    @SuppressLint("CheckResult")
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE)) || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;

        Observable.create(new ObservableOnSubscribe<AuthResult>() {
            @Override
            public void subscribe(ObservableEmitter<AuthResult> emitter) throws Exception {
                AuthTask authTask = new AuthTask(OtherActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);
                AuthResult authResult = new AuthResult(result, true);
                emitter.onNext(authResult);
            }
        })
                .filter(new Predicate<AuthResult>() {
                    @Override
                    public boolean test(AuthResult authResult) throws Exception {
                        String resultStatus = authResult.getResultStatus();
                        // 判断resultStatus 为“9000”且result_code
                        // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                        if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                            // 获取alipay_open_id，调支付时作为参数extern_token 的value
                            // 传入，则支付账户为该授权账户
                            return true;
                        } else {
                            // 其他状态值则为授权失败

                            if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {
                                ToastUtils.showShort("网络错误，请检查网络");

                            }else {
                                ToastUtils.showShort("登录失败");
                            }

                            return false;
                        }
                    }
                })

                .flatMap(new Function<AuthResult, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(AuthResult authResult) throws Exception {
                        params.put("openId", authResult.user_id);
                        params.put("openType", 1);
                        params.put("loginType", GlobalKey.loginStateByOther);
                        return ObservableHelper.commonLogin(params, OtherActivity.this);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        dialog.dismiss();
                        ToastUtils.showShort(s);
                        startActivity(MainActivity.class);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.toString());
                    }
                });

    }
}
