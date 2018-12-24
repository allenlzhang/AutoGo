package com.carlt.autogo.view.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.BaseDialog;
import com.carlt.autogo.common.dialog.LoginMoreDialog;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.presenter.login.ILoginView;
import com.carlt.autogo.presenter.login.LoginPresenter;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.more.safety.ChangeLoginPwdActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@CreatePresenter(presenter = LoginPresenter.class)
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.version_code)
    TextView versionCode;

    @BindView(R.id.user_phone)
    EditText userPhone;

    @BindView(R.id.user_pwd)
    EditText userPWd;

    @BindView(R.id.passwd_toggle)
    ImageView passwdToggle;

    @BindView(R.id.login_commit)
    Button loginCommit;

    /**
     * 忘记密码
     */
    @BindView(R.id.forgot_passwd)
    TextView forgotPasswd;

    /**
     * 注册
     */
    @BindView(R.id.user_regist)
    TextView userRegist;

    @BindView(R.id.btn_more)
    Button btnMore;

    @BindView(R.id.btn_changeUrl)
    Button btnChangeUrl;

    //    String[] tag = {"测试服", "正式服", "预发布"};
    String[] tag  = {"测试服", "预发布"};
    int      next = 0;

    Disposable disposable;
    private String savePwd;

    @Override
    protected int getContentView() {

        return R.layout.activity_login;
    }

    protected String[] needPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,

    };

    @Override
    public void init() {
        setTitleText("登录");
        hideTitle();
        versionCode.setText("V" + AutoGoApp.VERSION_NAME);
        setBaseBackStyle(getResources().getDrawable(R.drawable.common_close_select));
        AndPermission.with(this)
                .runtime()
                .permission(needPermissions)
                .onGranted(new Action<List<String>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onAction(List<String> data) {
                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        AutoGoApp.IMEI = telephonyManager.getDeviceId();
                        LogUtils.e(data.toString());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.e(data.toString());
                        if (data.get(data.size() - 1).equals(Manifest.permission.READ_PHONE_STATE)) {
                            showToast("部分权限获取失败，应用即将退出");
                            Observable.create(new ObservableOnSubscribe<Integer>() {
                                @Override
                                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                    emitter.onNext(1);
                                }
                            }).delay(2, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) throws Exception {
                                            finish();
                                        }
                                    });

                        }

                    }
                }).start();
        btnChangeUrl.setText(tag[next]);
        ClientFactory.defChangeUrl(next);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = SharepUtil.getBeanFromSp("user");
        //        boolean faceSwitch = SharepUtil.getPreferences().getBoolean(GlobalKey.FACE_LOGIN_SWITCH, false);

        if (userInfo != null) {
            //            if (userInfo.faceId != 0 && faceSwitch) {
            //                startActivity(FaceLoginActivity.class);
            //            }
            if (userInfo.loginState == GlobalKey.loginStateByPWd || userInfo.loginState == GlobalKey.loginStateByOther) {
                userPhone.setText(userInfo.mobile);
//                userPWd.setText(userInfo.password);
//                savePwd = userInfo.password;
            } else {
                userPhone.setText(userInfo.mobile);
            }
        }
    }

    @OnClick({R.id.btn_more, R.id.user_regist, R.id.login_commit, R.id.passwd_toggle, R.id.forgot_passwd})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_more:
                String phone = userPhone.getText().toString().trim();
                BaseDialog baseDialog = new LoginMoreDialog(this, false, phone);
                baseDialog.show();
                break;
            case R.id.user_regist:
                Intent intentRegist = new Intent(this, RegisterActivity.class);
                startActivity(intentRegist);

                break;
            case R.id.login_commit:

                String uPhone = userPhone.getText().toString().trim();
                String pwd = userPWd.getText().toString().trim();


                if (TextUtils.isEmpty(uPhone)) {
                    ToastUtils.showShort("请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("请输入密码");
                    return;
                }


                Map<String, Object> params = new HashMap<>();

                params.put("mobile", uPhone);
                params.put("password", CipherUtils.md5(pwd));
                params.put("loginType", GlobalKey.loginStateByPWd);
                params.put("pwdReally", pwd);

                getPresenter().login(params);

                break;
            case R.id.passwd_toggle:


                passwdToggle(passwdToggle.isSelected());
                passwdToggle.setSelected(!passwdToggle.isSelected());
                break;
            case R.id.forgot_passwd:
                Intent intent = new Intent(this, ChangeLoginPwdActivity.class);
                intent.putExtra("Login", true);
                startActivity(intent);
                break;

        }
    }

    @OnClick(R.id.btn_changeUrl)
    public void changeUrl() {

        btnChangeUrl.setText(tag[next]);
        ClientFactory.defChangeUrl(next);
        next++;
        if (next == tag.length) {
            next = 0;
        }

    }

    @Override
    public void loginFinish() {
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        user.password = userPWd.getText().toString().trim();
        SharepUtil.putByBean(GlobalKey.USER_INFO, user);
        startActivity(MainActivity.class);
    }

    private void passwdToggle(boolean selected) {

        if (selected) {
            userPWd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwdToggle.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_hide));
        } else {
            userPWd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwdToggle.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_show));
        }

    }

    @Override
    public void onBackPressed() {
        ActivityControl.removeAll();
        super.onBackPressed();
    }

}
