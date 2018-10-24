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
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.presenter.unregister.IUnRegisterView;
import com.carlt.autogo.presenter.unregister.UnRegisterPresenter;
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

@CreatePresenter(presenter = UnRegisterPresenter.class)
public class UnRegisterActivity extends BaseMvpActivity<UnRegisterPresenter> implements IUnRegisterView {

    @BindView(R.id.etLogoutPhone)
    EditText etLogoutPhone;
    @BindView(R.id.etLogoutCode)
    EditText etLogoutCode;
    @BindView(R.id.btnSendCode)
    Button   btnSendCode;
    @BindView(R.id.btnLogoutNext)
    Button   btnLogoutNext;


    @Override
    protected int getContentView() {
        return R.layout.activity_logout_account;
    }

    @Override
    public void init() {
        setTitleText("注销账户");
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        etLogoutPhone.setText(user.mobile);
    }

    @OnClick({R.id.btnSendCode, R.id.btnLogoutNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendCode:
                doSendCode();
                break;
            case R.id.btnLogoutNext:
                //                unRegister();
                String phone = etLogoutPhone.getText().toString().trim();
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
                map.put("validate", code);
                getPresenter().unRegister(map);
                break;
        }
    }


    @SuppressLint("CheckResult")
    private void doSendCode() {

        String phoneNum = etLogoutPhone.getText().toString().trim();
        boolean checkOk = RegexUtils.isMobileExact(phoneNum);
        if (!checkOk) {
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum, param, 12);

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

    int count = 60;
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
    public void unRegisterFinish(BaseError e) {
        if (e.code == 0) {
            //            注销成功
            showToast("注销成功");
            SharepUtil.cleanAllKey();
            ActivityControl.removeAllActivity(this);
            startActivity(LoginActivity.class,true);
        } else {
            showToast(e.msg);
        }
    }
}
