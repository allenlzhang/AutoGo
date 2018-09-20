package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class loginByPhoneActivity extends BaseMvpActivity {

    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.version_code)
    TextView versionCode;
    @BindView(R.id.user_phone)
    EditText userPhone;
    @BindView(R.id.user_pwd)
    EditText userPwd;
    @BindView(R.id.send_code)
    Button sendCode;
    @BindView(R.id.login_commit)
    Button loginCommit;


    int count  = 60;
    Disposable disposable ;
    UUDialog uuDialog ;
    private  String errorMsg;
    @Override
    protected int getContentView() {
        return R.layout.activity_login_by_phone;
    }

    @Override
    public void init() {
        setTitleText("短信登录");
        uuDialog =new UUDialog(this);
    }


    @OnClick({R.id.send_code, R.id.login_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code:

             ;    String PhoneNum = userPhone.getText().toString();
                if( TextUtils.isEmpty(PhoneNum) ){
                    ToastUtils.showShort("手机号为空");
                    return;
                }
                sendCode.setClickable(false);
                notifSendValidate();
                break;
            case R.id.login_commit:
                doCommit();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doCommit() {

        String PhoneNum = userPhone.getText().toString();
        String code = userPwd.getText().toString();
        if(TextUtils.isEmpty(PhoneNum) || TextUtils.isEmpty(code) ){
            ToastUtils.showShort("手机号或者验证码为空");
            return;
        }

        HashMap <String,Object> params = new HashMap<>();
        params.put("mobile",PhoneNum);
        params.put("validate",code);

        ClientFactory.def(UserService.class).loginByPhone(params)
                .flatMap(new Function<User, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(User user) throws Exception {
                        if(user.err != null){
                            errorMsg = user.err.msg ;
                            return  null;
                        }else {
                            Map<String, String> token =   new HashMap<String, String>();
                            token.put("token",user.token);
                            SharepUtil.put("token",user.token);
                            return ClientFactory.def(UserService.class).getUserInfo(token);
                        }
                    }
                })
                .map(new Function<UserInfo, String>() {
                    @Override
                    public String apply(UserInfo userInfo) throws Exception {
                        if(userInfo.err != null){
                            errorMsg = userInfo.err.msg ;
                            return null;
                        }else {
                            SharepUtil.<UserInfo>putByBean("user", userInfo) ;
                            SharepUtil.put("headurl","http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLDx6ZPo7iak6rDRsiaDK4JYhMYfUzbWicUsqTS97xGcCZqXD4OEbFfFLo5rI5icsUdXASrRk50I2ZJ9g/132");
                        }
                        return "登录成功";
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        uuDialog.dismiss();
                        ToastUtils.showShort(s);
                        Intent intent = new Intent(loginByPhoneActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort(errorMsg);
                        uuDialog.dismiss();
                    }
                });

    }



    private void notifSendValidate() {
        disposable  =  Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(count <= 0 ){
                            disposable.dispose();
                            sendCode.setClickable(true);
                            sendCode.setText("发送验证码");
                            count =60;
                        }else {
                            count -- ;
                            sendCode.setText(count + "秒");
                        }
                    }
                });
    }
}
