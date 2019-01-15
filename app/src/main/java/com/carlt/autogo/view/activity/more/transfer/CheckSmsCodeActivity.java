package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.MyCarActivity;
import com.carlt.autogo.view.activity.login.FaceLiveCheckActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 授权、过户短信验证
 * Author     : zhanglei
 * Date       : 2018/12/11
 */
public class CheckSmsCodeActivity extends BaseMvpActivity {
    @BindView(R.id.etLogoutPhone)
    EditText etLogoutPhone;
    @BindView(R.id.etLogoutCode)
    EditText etLogoutCode;
    @BindView(R.id.btnSendCode)
    Button   btnSendCode;
    @BindView(R.id.btnLogoutNext)
    Button   btnLogoutNext;
    private boolean isTransfer;
    private String  phone;


    @Override
    protected int getContentView() {
        return R.layout.activity_check_sms_code;
    }

    @Override
    public void init() {
        setTitleText("安全验证");
        Intent intent = getIntent();
        isTransfer = intent.getBooleanExtra("isTransfer", false);
        //        if (isTransfer) {
        //            int transferId = intent.getIntExtra("transferId", -1);
        //        } else {
        //            int authId = intent.getIntExtra("authId", -1);
        //        }
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        etLogoutPhone.setText(user.mobile);
    }

    @OnClick({R.id.btnSendCode, R.id.btnLogoutNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendCode:
                if (isTransfer) {
                    //过户
                    doSendCode(8);
                } else {
                    //授权
                    doSendCode(14);
                }

                break;
            case R.id.btnLogoutNext:
                //                unRegister();
                phone = etLogoutPhone.getText().toString().trim();
                String code = etLogoutCode.getText().toString().trim();
                HashMap<String, Object> map = new HashMap<>();
                UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                String mobile = info.mobile;
                if (!mobile.equals(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                map.put("mobile", phone);
                map.put("smsCode", code);
                if (isTransfer) {
                    map.put("type", 8);
                    doTransferCar(map);
                } else {
                    map.put("type", 14);
                    doAuthCar(map);
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doAuthCar(HashMap<String, Object> map) {
        dialog.show();
        int authId = getIntent().getIntExtra("authId", -1);
        final HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", authId);
        map1.put("status", 3);
        ClientFactory.def(UserService.class).checkSmsCode(map)
                .filter(new Predicate<BaseError>() {
                    @Override
                    public boolean test(BaseError error) throws Exception {
                        if (error.code == 0) {
                            return true;
                        } else {
                            showToast(error.msg);
                            dialog.dismiss();
                            return false;
                        }

                    }
                })
                .flatMap(new Function<BaseError, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(BaseError error) throws Exception {
                        return ClientFactory.def(CarService.class).modifyStatus(map1);
                    }
                })
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.code == 0) {

                            ToastUtils.showShort("操作成功");
                            closeActivity();
                        } else {
                            ToastUtils.showShort(carBaseInfo.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                        //                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void doTransferCar(HashMap<String, Object> map) {
        dialog.show();
        int transferId = getIntent().getIntExtra("transferId", -1);
        final HashMap<String, Object> map1 = new HashMap<>();
        map1.put("transferId", transferId);
        map1.put("isAgree", 1);
        ClientFactory.def(UserService.class).checkSmsCode(map)
                .filter(new Predicate<BaseError>() {
                    @Override
                    public boolean test(BaseError error) throws Exception {
                        if (error.code == 0) {
                            return true;
                        } else {
                            showToast(error.msg);
                            dialog.dismiss();
                            return false;
                        }

                    }
                })
                .flatMap(new Function<BaseError, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(BaseError error) throws Exception {
                        return ClientFactory.def(CarService.class).dealTransferCode(map1);
                    }
                })
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.code == 0) {

                            ToastUtils.showShort("操作成功");
                            jump2Activity();
                        } else {
                            ToastUtils.showShort(carBaseInfo.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                        //                        ToastUtils.showShort("操作失败");
                    }
                });
    }

    private void jump2Activity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransferQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransHandleActivity) {
                activity.finish();
            }
            if (activity instanceof AuthHandleActivity) {
                activity.finish();
            }
            if (activity instanceof FaceLiveCheckActivity) {
                activity.finish();
            }
        }
        Intent intent = new Intent(this, MyCarActivity.class);
        startActivity(intent);
        finish();
    }

    private void closeActivity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransferQRCodeActivity) {
                activity.finish();
            }
            if (activity instanceof TransHandleActivity) {
                activity.finish();
            }
            if (activity instanceof AuthHandleActivity) {
                activity.finish();
            }
            if (activity instanceof FaceLiveCheckActivity) {
                activity.finish();
            }
        }
        finish();
    }

    @SuppressLint("CheckResult")
    private void doSendCode(int type) {

        String phoneNum = etLogoutPhone.getText().toString().trim();
        boolean checkOk = RegexUtils.isMobileExact(phoneNum);
        if (!checkOk) {
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum, param, type);

        observable.subscribe(new Consumer<BaseError>() {
            @Override
            public void accept(BaseError baseError) throws Exception {
                if (baseError.msg != null) {
                    ToastUtils.showShort(baseError.msg);
                    btnSendCode.setClickable(true);
                    btnSendCode.setText("发送验证码");
                    count = 60;
                } else {
                    notifSendValidate();
                    showToast("短信下发成功");
                    btnSendCode.setClickable(false);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(throwable.getMessage());
            }
        });

    }

    int        count = 60;
    Disposable disposable;

    private void notifSendValidate() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (count <= 0) {
                            disposable.dispose();
                            btnSendCode.setClickable(true);
                            btnSendCode.setText("发送验证码");
                            count = 60;
                        } else {
                            count--;
                            btnSendCode.setText(count + "秒");
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
