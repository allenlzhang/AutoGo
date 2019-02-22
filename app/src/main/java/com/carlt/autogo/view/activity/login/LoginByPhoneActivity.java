package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginByPhoneActivity extends BaseMvpActivity {

    @BindView(R.id.user_phone)
    EditText userPhone;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.send_code)
    Button   sendCode;
    @BindView(R.id.login_commit)
    Button   loginCommit;
    int        count = 60;
    Disposable disposable;
    UUDialog   uuDialog;
    private String errorMsg;

    @Override
    protected int getContentView() {
        return R.layout.activity_login_by_phone;
    }

    @Override
    public void init() {
        setTitleText("验证码登录");
        uuDialog = new UUDialog(this, R.style.DialogCommon);
        UserInfo userInfo = SharepUtil.getBeanFromSp("user");
        if (userInfo != null) {
            userPhone.setText(userInfo.mobile);
        }

    }

    @OnClick({R.id.send_code, R.id.login_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            // 发送验证码
            case R.id.send_code:

                String PhoneNum = userPhone.getText().toString();
                if (TextUtils.isEmpty(PhoneNum)) {
                    ToastUtils.showShort("手机号为空");
                    return;
                }
                sendCode.setClickable(false);
                Map<String, String> params = new HashMap<>();
                params.put("mobile", PhoneNum);
                sendValidate(PhoneNum, params);
                break;

            case R.id.login_commit:
                doCommit();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void sendValidate(final String phoneNum, Map<String, String> params) {

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum, params, 11);

        observable.subscribe(new Consumer<BaseError>() {
            @Override
            public void accept(BaseError baseError) throws Exception {
                if (baseError.msg != null) {
                    ToastUtils.showShort(baseError.msg);
                    sendCode.setClickable(true);
                    sendCode.setText("发送验证码");
                    count = 60;
                } else {
                    notifSendValidate();
                    ToastUtils.showShort("短信下发成功");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(throwable.getMessage());
            }
        });

    }

    @SuppressLint("CheckResult")
    private void doCommit() {

        String PhoneNum = userPhone.getText().toString();
        String code = userPwd.getText().toString();
        if (TextUtils.isEmpty(PhoneNum) || TextUtils.isEmpty(code)) {
            ToastUtils.showShort("手机号或者验证码为空");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile", PhoneNum);
        params.put("validate", code);
        params.put("loginType", GlobalKey.loginStateByPhone);


        ObservableHelper.commonLogin(params)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        startActivity(MainActivity.class);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(ObservableHelper.errorMsg);
                    }
                });

    }

    private void notifSendValidate() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (count <= 0) {
                            disposable.dispose();
                            sendCode.setClickable(true);
                            sendCode.setText("发送验证码");
                            count = 60;
                        } else {
                            count--;
                            sendCode.setText(count + "秒");
                        }
                    }
                });
    }
}
