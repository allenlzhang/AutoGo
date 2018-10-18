package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.SharepUtil;

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

/**
 * Description : 修改手机号码
 * Author     : zhanglei
 * Date       : 2018/10/17
 */
public class UpdatePhoneOneActivity extends BaseMvpActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btnSendCode)
    Button   btnSendCode;
    @BindView(R.id.btnNext)
    Button   btnNext;
    private int count = 60;
    private Disposable disposable;

    @Override
    protected int getContentView() {
        return R.layout.activity_update_phone;
    }

    @Override
    public void init() {
        setTitleText("修改手机号码");
    }

    @OnClick({R.id.btnSendCode, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendCode:
                doSendCode();
                break;
            case R.id.btnNext:
                nextType();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void nextType() {
        String phone = etPhone.getText().toString().trim();
        final String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }
        UserInfo user = SharepUtil.getBeanFromSp("user");
        if (!user.mobile.equals(phone)) {
            showToast("请输入当前登录手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("validate", code);
        map.put("version", AutoGoApp.VERSION);
        map.put("moveDeviceName", AutoGoApp.MODEL_NAME);
        map.put("loginModel", AutoGoApp.MODEL);
        map.put("loginSoftType", "Android");
        ClientFactory.def(UserService.class).authMobile(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        if (error.code == 0) {
                            Intent intent = new Intent(UpdatePhoneOneActivity.this, UpdatePhoneTwoActivity.class);
                            //        intent.putExtra("oldPhone", phone);
                            intent.putExtra("oldCode", code);
                            startActivity(intent);
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

    @SuppressLint("CheckResult")
    private void doSendCode() {

        String phoneNum = etPhone.getText().toString().trim();

        UserInfo user = SharepUtil.getBeanFromSp("user");
        if (!user.mobile.equals(phoneNum)) {
            showToast("请输入当前登录手机号");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum, param, 6);

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
                    ToastUtils.showShort("短信下发成功");
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
}
