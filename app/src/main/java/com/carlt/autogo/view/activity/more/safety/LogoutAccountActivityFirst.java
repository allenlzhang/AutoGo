package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LogoutAccountActivityFirst extends BaseMvpActivity {

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
    }

    @OnClick({R.id.btnSendCode, R.id.btnLogoutNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendCode:
                String PhoneNum = etLogoutPhone.getText().toString();
                if (TextUtils.isEmpty(PhoneNum)) {
                    ToastUtils.showShort("手机号为空");
                    return;
                }
                btnSendCode.setClickable(false);
                notifSendValidate();
                break;
            case R.id.btnLogoutNext:
                startActivity(new Intent(this, LogoutAccountActivitySecond.class));
                break;
        }
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
}
