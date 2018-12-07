package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.alipay.AuthResult;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.ShareLoginList;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.alipay.OrderInfoUtil2_0;
import com.carlt.autogo.view.activity.user.accept.IdfCompleteActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;

import java.util.HashMap;
import java.util.List;
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

/**
 * Created by Marlon on 2018/9/12.
 */
public class SafetyActivity extends BaseMvpActivity {
    /**
     * 身份认证
     */
    @BindView(R.id.ll_safety_identity_authentication)
    LinearLayout mLlIdentityAuthentication;

    @BindView(R.id.tv_safety_identity_authentication_status)
    TextView mTvIdentityAuthentication;

    /**
     * 人脸识别
     */
    @BindView(R.id.ll_safety_face_recognition)
    LinearLayout mLlFaceRecognition;

    @BindView(R.id.tv_safety_face_recognition_status)
    TextView mTvFaceRecognition;

    @BindView(R.id.iv_safety_face_recognition_status)
    ImageView mIvFaceRecognition;

    /**
     * 绑定手机
     */
    @BindView(R.id.ll_safety_binding_phone)
    LinearLayout mLlBindingPhone;

    @BindView(R.id.tv_safety_binding_phone)
    TextView mTvBindingPhone;

    /**
     * 登录密码管理
     */
    @BindView(R.id.ll_safety_login_pwd_management)
    LinearLayout mLlLoginPwdManagement;

    /**
     * 远程控制密码管理
     */
    @BindView(R.id.ll_safety_remote_pwd_management)
    LinearLayout mLlRemotePwdManagement;

    /**
     * 登录设备管理
     */
    @BindView(R.id.ll_safety_login_device_management)
    LinearLayout mLlLoginDeviceManagement;

    /**
     * 冻结账户
     */
    @BindView(R.id.ll_safety_frozen_account)
    LinearLayout mLlFrozenAccount;

    /**
     * 注销账户
     */
    @BindView(R.id.ll_safety_cancellation_account)
    LinearLayout   mLlCancellationAccount;
    @BindView(R.id.cbWechatLogin)
    CheckBox       cbWechatLogin;
    @BindView(R.id.cbALiPayLogin)
    CheckBox       cbALiPayLogin;
    @BindView(R.id.rlWechatSwitch)
    RelativeLayout rlWechatSwitch;
    @BindView(R.id.rlAliSwitch)
    RelativeLayout rlAliSwitch;
    //    private LogoutTipDialog mTipDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_safety;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.more_accounts_and_security));
        //        mTipDialog = new LogoutTipDialog(this, R.style.DialogCommon);
        //        mTipDialog.setLisniter(new LogoutTipDialog.LogoutTipDialogClickLisniter() {
        //            @Override
        //            public void commit() {
        //                startActivity(new Intent(SafetyActivity.this, LogoutAccountActivityFirst.class));
        //            }
        //        });
        Platform[] list = ShareSDK.getPlatformList();
        if (list != null) {
            plat = list[0];
        }
        checkShareLoginList();
        rlAliSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbALiPayLogin.isChecked()) {
                    showUnBindAliTip("你确定要解绑支付宝吗", 1);
                } else {
                    showBindAliTip();
                }
            }
        });
        rlWechatSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbWechatLogin.isChecked()) {
                    showUnBindAliTip("你确定要解绑微信吗", 2);
                } else {
                    showBindWechatTip();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //        String isIdentity = SharepUtil.getPreferences().getString(GlobalKey.IDENTITY_AUTH, "");
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        boolean faceLogin = SharepUtil.getPreferences().getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);
        LogUtils.e("----" + user.toString());
        if (user.identityAuth == 2 || user.alipayAuth == 2 && user.faceId != 0) {
            mTvIdentityAuthentication.setText("已认证");
            mLlIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent safety_identity = new Intent(SafetyActivity.this, IdfCompleteActivity.class);
                    safety_identity.putExtra("idcard", false);
                    startActivity(safety_identity);
                }
            });
        } else {
            mTvIdentityAuthentication.setText("未认证");
            mLlIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent safety_identity = new Intent(SafetyActivity.this, UserIdChooseActivity.class);
                    startActivity(safety_identity);
                }
            });
        }
        if (faceLogin && user.identityAuth == 2 ||user.alipayAuth == 2 && user.faceId != 0) {
            mTvFaceRecognition.setText("已开启");
            mIvFaceRecognition.setVisibility(View.VISIBLE);
        } else {
            mTvFaceRecognition.setText("未开启");
            mIvFaceRecognition.setVisibility(View.GONE);
        }
        showBindingPhone(user.mobile);


    }

    @SuppressLint("CheckResult")
    private void checkShareLoginList() {
        ClientFactory.def(UserService.class).getShareLogin(new HashMap<String, Object>())
                .subscribe(new Consumer<ShareLoginList>() {
                    @Override
                    public void accept(ShareLoginList info) throws Exception {
                        LogUtils.e(info.toString());
                        List<ShareLoginList.ListBean> list = info.list;
                        for (ShareLoginList.ListBean listBean : list) {
                            if (listBean.openType == 1) {
                                //支付宝绑定
                                cbALiPayLogin.setChecked(true);
                            } else if (listBean.openType == 2) {
                                //微信绑定
                                cbWechatLogin.setChecked(true);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                            LogUtils.e(throwable);
                    }
                });
    }

    private void showUnBindAliTip(String content, final int type) {
        CommonDialog.createTwoBtnDialog(this, content, true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                //                cbALiPayLogin.setChecked(false);
                unBindALiAuth(type);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void unBindALiAuth(final int type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openType", type);
        ClientFactory.def(UserService.class).shareLoginUnbind(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        if (error.code == 0) {
                            showToast("解绑成功");
                            if (type == 1) {
                                cbALiPayLogin.setChecked(false);
                            } else {
                                cbWechatLogin.setChecked(false);
                            }
                        } else {
                            showToast(error.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    /**
     * 手机号中间*展示
     * @param mobile
     */
    private void showBindingPhone(String mobile) {
        if (!TextUtils.isEmpty(mobile) && mobile.length() > 6) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mobile.length(); i++) {
                char c = mobile.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            mTvBindingPhone.setText(sb.toString());
        }
    }

    @OnClick({R.id.ll_safety_identity_authentication, R.id.ll_safety_face_recognition, R.id.ll_safety_binding_phone, R.id.ll_safety_login_pwd_management, R.id.ll_safety_remote_pwd_management, R.id.ll_safety_login_device_management, R.id.ll_safety_frozen_account, R.id.ll_safety_cancellation_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_safety_identity_authentication:

                break;
            case R.id.ll_safety_face_recognition:
                startActivity(FaceRecognitionSettingActivity.class, false);
                break;
            case R.id.ll_safety_binding_phone:
                startActivity(UpdatePhoneOneActivity.class, false);
                break;
            case R.id.ll_safety_login_pwd_management:
                //                Intent intent = new Intent(SafetyActivity.this, LoginPwdManagementActivity.class);
                startActivity(LoginPwdManagementActivity.class, false);
                break;
            case R.id.ll_safety_remote_pwd_management:
                //                Intent intent1 = new Intent(SafetyActivity.this, RemotePwdManagementActivity.class);

                startActivity(RemotePwdManagementActivity.class, false);

                break;
            case R.id.ll_safety_login_device_management:
                //                Intent intentDecide = new Intent(SafetyActivity.this, LoginDeviceManagementActivity.class);
                startActivity(LoginDeviceManagementActivity.class, false);
                break;
            case R.id.ll_safety_frozen_account:

                //                Intent intentFreeze = new Intent(this, FreezeActivity.class);
                startActivity(FreezeActivity.class, false);
                break;
            case R.id.ll_safety_cancellation_account:
                //                mTipDialog.show();
                startActivity(UnRegisterNoticeActivity.class, false);
                break;
        }
    }

    private Platform plat;

    private void showBindWechatTip() {
        CommonDialog.createTwoBtnDialog(this, "你确定要绑定微信吗", true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                //                cbALiPayLogin.setChecked(true);
                doAuthorize(plat, new MyPlatformActionListener(dialog));
            }
        });
    }

    public static void doAuthorize(Platform plat, MyPlatformActionListener listener) {

        plat.setPlatformActionListener((listener));
        plat.removeAccount(true);
        plat.SSOSetting(false);
        plat.authorize();

    }

    private void showBindAliTip() {
        CommonDialog.createTwoBtnDialog(this, "你确定要绑定支付宝吗", true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                //                cbALiPayLogin.setChecked(true);
                authALiPay();
            }
        });
    }

    public class MyPlatformActionListener implements PlatformActionListener {
        Dialog dialog;

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
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("openId", unionid);
                    params.put("openType", 2);
                    LogUtils.e(params);
                    ClientFactory.def(UserService.class).shareLoginBind(params)
                            .subscribe(new Consumer<BaseError>() {
                                @Override
                                public void accept(BaseError er) throws Exception {
                                    if (er.code == 0) {
                                        showToast("绑定成功");
                                        cbWechatLogin.setChecked(true);
                                    } else {
                                        showToast(er.msg);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    LogUtils.e(throwable);
                                }
                            });

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    dialog.dismiss();
                    ToastUtils.showShort("绑定失败");

                    LogUtils.e(throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    dialog.dismiss();
                    ToastUtils.showShort("绑定取消");
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
            if (error.contains("NotExistException")) {
                ToastUtils.setMsgTextSize(13);
                ToastUtils.showShort("请先在应用商店下载该应用再进行该操作");

            } else if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {
                ToastUtils.showShort("网络错误，请检查网络");

            } else {
                ToastUtils.showShort("登录失败");
            }

            LogUtils.e(throwable.toString());

        }

        @Override
        public void onCancel(Platform platform, int i) {
            ToastUtils.showShort("取消授权");
            dialog.dismiss();
        }
    }

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018090661230641";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID             = "2088131979649430";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID       = System.currentTimeMillis() + "";
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
                AuthTask authTask = new AuthTask(SafetyActivity.this);
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
                        Map<String, Object> map = new HashMap<>();
                        map.put("openType", 1);
                        map.put("openId", authResult.user_id);

                        return ClientFactory.def(UserService.class).shareLoginBind(map);
                    }
                })

                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError er) throws Exception {
                        if (er.code == 0) {
                            showToast("绑定成功");
                            cbALiPayLogin.setChecked(true);
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


    }
}
