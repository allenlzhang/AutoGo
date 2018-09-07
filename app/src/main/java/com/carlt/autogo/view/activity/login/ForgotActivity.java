package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.net.base.RestClientFactory;
import com.carlt.autogo.net.service.UserService;

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

public class ForgotActivity extends BaseMvpActivity {

    @BindView(R.id.ed_forgot_user_phone)EditText edForgotUserPhone;
    @BindView(R.id.ed_forgot_user_code)EditText edForgotUserCode;

    @BindView(R.id.ed_forgot_pwd)EditText edForgotPwd;
    @BindView(R.id.ed_forgot_pwd_d)EditText edForgotPwdD;

    @BindView(R.id.img_passwd_toggle)ImageView imgPasswdToggle;
    @BindView(R.id.img_passwd_toggle_d)ImageView imgPasswdToggleD;

    @BindView(R.id.btn_forgot_commit)Button btnForgotCommit;
    @BindView(R.id.btn_send_code)Button btnSendCode;

    Disposable disposable;
    int count = 60 ; ;

    @Override
    protected int getContentView() {
        return R.layout.activity_forgot;
    }



    @OnClick({R.id.btn_send_code})
    public void onClick(View view){
        doSendCode();
    }
    @OnClick(R.id.btn_forgot_commit)
    public void forgotCommit(){
        String phoneNum = edForgotUserPhone.getText().toString().trim();
        boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
        if(!checkOk){
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }

        if( StringUtils.isEmpty(edForgotUserCode.getText())){
            ToastUtils.showShort("验证码为空");
            return;
        }

        String regex = "^[A-Za-z0-9]{6,11}+$";

        String pwd = edForgotPwd.getText().toString().toString();
        String pwdD = edForgotPwdD.getText().toString().toString();

        if (!RegexUtils.isMatch(regex, pwd) || !RegexUtils.isMatch(regex, pwd)) {
            ToastUtils.setMsgTextSize(15);
            ToastUtils.showLong("密码有误,输入数字或者字母,长度6到10");

            return;
        }

        if (!pwd.equals(pwdD)) {
            ToastUtils.showShort("两次密码不一致");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", "");
        params.put("password", "");
        params.put("validate", "");
        params.put("move_deviceid", "");
        params.put("move_device_name", "");
        params.put("originate","");

        doForgotCommit(params);
    }

    private void doForgotCommit(Map<String, Object> params) {


    }

    @OnClick({R.id.img_passwd_toggle_d, R.id.img_passwd_toggle})

    public void toggle(View view) {

        if (view.getId() == R.id.img_passwd_toggle) {
            passwdToggle(view.isSelected(), edForgotPwd, (ImageView) view);
        } else {
            passwdToggle(view.isSelected(), edForgotPwdD, (ImageView) view);
        }
        view.setSelected(!view.isSelected());
    }

    @Override
    public void init() {
        setTitleText("忘记密码");
    }


    @SuppressLint("CheckResult")
    private void doSendCode() {

        String phoneNum = edForgotUserPhone.getText().toString().trim();
        boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
        if(!checkOk){
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }

        RestClientFactory.creatDef().getService(UserService.class).getValidate(phoneNum)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ToastUtils.showShort("下发成功");
                        btnSendCode .setClickable(false);
                        notifSendValidate();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.getMessage());
                        ToastUtils.showShort("下发失败");
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
                            btnSendCode.setClickable(true);
                            btnSendCode.setText("发送验证码");
                            count =60;
                        }else {
                            count -- ;
                            btnSendCode.setText(count + "秒");
                        }
                    }
                });
    }

    private void passwdToggle(boolean selected, EditText editText, ImageView imageView) {

        if (selected) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_hide));

        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_show));

        }

    }
}
