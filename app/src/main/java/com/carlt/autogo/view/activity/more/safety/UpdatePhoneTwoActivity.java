package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
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
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.LoginActivity;

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
public class UpdatePhoneTwoActivity extends BaseMvpActivity {

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
        return R.layout.activity_update_phone_two;
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
                confirm();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void confirm() {
        final String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String oldCode = getIntent().getStringExtra("oldCode");
        final UserInfo user = SharepUtil.getBeanFromSp("user");
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }
        LogUtils.e(oldCode);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("newValidate", code);
        map.put("oldValidate", oldCode);
        ClientFactory.def(UserService.class).resetMobile(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        if (error.code == 0) {
                            showToast("修改成功");
                            user.mobile = phone;
                            SharepUtil.putByBean(GlobalKey.USER_INFO, user);
                            ActivityControl.removeAllActivity(UpdatePhoneTwoActivity.this);
                            startActivity(LoginActivity.class);
                        } else {
                            showToast(error.msg);
                        }
                        LogUtils.e(error);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }


    @SuppressLint("CheckResult")
    private void doSendCode() {

        String phoneNum = etPhone.getText().toString().trim();
        boolean checkOk = RegexUtils.isMobileExact(phoneNum);
        if (!checkOk) {
            showToast("请输入正确手机号!");
            return;
        }
        //        UserInfo user = SharepUtil.getBeanFromSp("user");
        //        if (!user.mobile.equals(phoneNum)) {
        //            showToast("请输入当前登录手机号");
        //            return;
        //        }
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum, param, 4);

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
