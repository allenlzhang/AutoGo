package com.carlt.autogo.view.activity.user.accept;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.alipay.AuthResult;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.alipay.OrderInfoUtil2_0;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 */
public class UserIdChooseActivity extends BaseMvpActivity {

    @BindView(R.id.rl_payment_accept)
    RelativeLayout rlPaymentAccept;
    @BindView(R.id.rl_idcard_accept)
    RelativeLayout rlIdcardAccept;
    private UserInfo user;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_id_choose;
    }

    @Override
    public void init() {
        setTitleText("选择身份认证方式");

    }

    @Override
    protected void onResume() {
        super.onResume();
        user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        LogUtils.e(user.toString());
    }

    @OnClick({R.id.rl_payment_accept, R.id.rl_idcard_accept})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_payment_accept:
                //支付宝认证
                if (user.alipayAuth == 2) {
                    startActivity(FaceAuthSettingActivity.class, false);
                } else {
                    authALiPay();
                }
                break;
            case R.id.rl_idcard_accept:
                showToast("此功能暂未开放");
                //            Intent intentId = new Intent(this, IdCardAcceptActivity.class);
                //            startActivity(intentId);
                break;
            default:
                break;
        }

    }

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
    public static final String RSA2_PRIVATE    = "";
    public static final String AliPay_Auth_URL = "http://test.linewin.cc:8888/app/User/AlipayAuth";
    public static final String RSA_PRIVATE     = "MIIEowIBAAKCAQEA1LUwhOyN1hOnrV3g2COyqpcUax8Fq1/0Xuy74ZphNpszZ21c\n" +
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

    /**
     * 支付宝账户授权业务
     * @param
     */
    @SuppressLint("CheckResult")
    public void authALiPay() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
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
                AuthTask authTask = new AuthTask(UserIdChooseActivity.this);
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
                            ToastUtils.showShort("授权失败");
                            return false;
                        }

                    }
                })
                .flatMap(new Function<AuthResult, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(AuthResult authResult) throws Exception {
                        LogUtils.e("======" + authResult.toString());
                        Map<String, String> map = new HashMap<>();
                        map.put("token", SharepUtil.preferences.getString("token", ""));
                        map.put("authCode", authResult.authCode);
                        map.put("openId", authResult.user_id);

                        return ClientFactory.def(UserService.class).authAliPay(map);
                    }
                })

                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError er) throws Exception {
                        LogUtils.e(er.toString());
                        if (er.code == 0) {
                            user.alipayAuth = 2;
                            SharepUtil.putByBean(GlobalKey.USER_INFO, user);
                            startActivity(FaceAuthSettingActivity.class, false);
                        } else {
                            showToast(er.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.toString());
                    }
                });
        //                .subscribe(new Consumer<AuthResult>() {
        //                    @Override
        //                    public void accept(AuthResult authResult) throws Exception {
        //                        LogUtils.e("-----" + authResult.toString());
        //                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //                        map.put("token", SharepUtil.preferences.getString("token", ""));
        //                        map.put("authCode", authResult.authCode);
        //                        map.put("openId", authResult.user_id);
        //                        Gson gson = new Gson();
        //                        String json = gson.toJson(map);
        //                        LogUtils.e("--json---" + json);
        //                        OkGo.<String>post(AliPay_Auth_URL)
        //                                .headers("Carlt-Access-Id", "19877415356991399877")
        //                                .headers("Content-Type", "application/json")
        //                                .upJson(json)
        //                                .execute(new StringCallback() {
        //                                    @Override
        //                                    public void onSuccess(Response<String> response) {
        //                                        LogUtils.e("-----" + response.body());
        //                                    }
        //
        //                                    @Override
        //                                    public void onError(Response<String> response) {
        //                                        super.onError(response);
        //                                        LogUtils.e("-----" + response.body());
        //                                    }
        //                                });
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        LogUtils.e(throwable.toString());
        //                    }
        //                });

    }
}
