package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.MyInputFilter;
import com.carlt.autogo.utils.MyTextWatcher;
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
 * Created by Marlon on 2018/9/13.
 */
public class ChangeLoginPwdActivity extends BaseMvpActivity {
    @BindView(R.id.edit_management_phone)
    EditText       editManagementPhone;
    @BindView(R.id.edit_management_code)
    EditText       editManagementCode;
    @BindView(R.id.btn_management_code)
    Button         btnManagementCode;
    @BindView(R.id.rl_management_code)
    RelativeLayout rlManagementCode;
    @BindView(R.id.edit_management_old_pwd)
    EditText       editManagementOldPwd;
    @BindView(R.id.edit_management_new_pwd)
    EditText       editManagementNewPwd;
    @BindView(R.id.edit_management_new_pwd_again)
    EditText       editManagementNewPwdAgain;
    @BindView(R.id.edit_management_confirm)
    Button         editManagementConfirm;
    @BindView(R.id.img_passwd_toggle1)
    ImageView      imgPasswdToggle1;
    @BindView(R.id.llOldPwd)
    LinearLayout   llOldPwd;
    @BindView(R.id.img_passwd_toggle2)
    ImageView      imgPasswdToggle2;
    @BindView(R.id.llNewPwd)
    LinearLayout   llNewPwd;
    @BindView(R.id.img_passwd_toggle3)
    ImageView      imgPasswdToggle3;
    @BindView(R.id.llPwdAgain)
    LinearLayout   llPwdAgain;

    private int type = 0;

    /**
     * 倒计时
     */
    private int count = 60;

//    private Timer timer = new Timer();
//
//    private TimerTask task;


    @Override
    protected int getContentView() {
        return R.layout.activity_change_login_pwd;
    }


    @Override
    public void init() {
        type = getIntent().getIntExtra("changePwd", 1);
        loadTypeView(type);

        //密码输入屏蔽 中文其他不满足需求的字符
        editManagementOldPwd.setFilters(new InputFilter[]{new MyInputFilter()});
        editManagementNewPwd.setFilters(new InputFilter[]{new MyInputFilter()});
        editManagementNewPwdAgain.setFilters(new InputFilter[]{new MyInputFilter()});


        editManagementOldPwd.addTextChangedListener(new MyTextWatcher());
        editManagementNewPwd.addTextChangedListener(new MyTextWatcher());
        editManagementNewPwdAgain.addTextChangedListener(new MyTextWatcher());
    }

//    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 111) {
//                count--;
//                if (count > 0) {
//                    btnManagementCode.setText(count + "秒后重发");
//                } else {
//                    if (timer != null) {
//                        if (task != null) {
//                            task.cancel();
//                        }
//                    }
//                    btnManagementCode.setEnabled(true);
//                    btnManagementCode.setText("重发验证码");
//                }
//            }
//            super.handleMessage(msg);
//
//        }
//    };

    private void passwdToggle(boolean selected, EditText editText, ImageView imageView) {

        if (selected) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_hide));

        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_show));

        }

    }

    @OnClick({R.id.btn_management_code, R.id.edit_management_confirm, R.id.img_passwd_toggle1, R.id.img_passwd_toggle2, R.id.img_passwd_toggle3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_code:
                sendCode();
                break;
            case R.id.edit_management_confirm:
                doPwdConfirm();
                break;
            case R.id.img_passwd_toggle1:
                passwdToggle(view.isSelected(), editManagementOldPwd, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
            case R.id.img_passwd_toggle2:
                passwdToggle(view.isSelected(), editManagementNewPwd, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
            case R.id.img_passwd_toggle3:
                passwdToggle(view.isSelected(), editManagementNewPwdAgain, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
        }
    }

    String phone;

    //发送验证码类型(1=注册,2=找回密码,3=修改密码,4=修改手机,5=绑定微信,6=修改手机[旧手机号],7=远程密码重置,8=车辆过户,9=主机认证,10=更换设备,11=登录,12=注销)
    @SuppressLint("CheckResult")
    private void sendCode() {
        phone = editManagementPhone.getText().toString().trim();
//        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
//        if (!TextUtils.equals(phone, user.mobile)) {
//            showToast("手机号码不正确");
//            return;
//        }
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号码");
            LogUtils.e(phone);
            return;
        }
//        btnManagementCode.setEnabled(false);
//        btnManagementCode.setText(count + "秒后重发");
        Map<String, String> param = new HashMap<>();
        param.put("mobile", phone);
        Observable<BaseError> observable = ObservableHelper.sendValidate(phone, param, 2);

        observable.subscribe(new Consumer<BaseError>() {
            @Override
            public void accept(BaseError baseError) throws Exception {
                if (baseError.msg != null) {
                    showToast(baseError.msg);
                    btnManagementCode.setClickable(true);
                    btnManagementCode.setText("发送验证码");
                    count = 60;
                } else {
                    notifSendValidate();
                    showToast("短信下发成功");
                    btnManagementCode.setClickable(false);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(throwable.getMessage());
            }
        });


//        task = new TimerTask() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what = 111;
//                mHandler.sendMessage(msg);
//            }
//        };
//        timer.schedule(task, 1000, 1000);
//        //        final String phone = editManagementPhone.getText().toString().trim();
//        Map<String, String> params = new HashMap<>();
//        params.put("mobile", phone);
//
//        ClientFactory.def(UserService.class).getSmsToken(params)
//                .flatMap(new Function<SmsToken, ObservableSource<BaseError>>() {
//                    @Override
//                    public ObservableSource<BaseError> apply(SmsToken smsToken) throws Exception {
//                        if (smsToken.msg != null) {
//                            showToast(smsToken.msg.msg);
//                            return null;
//                        } else {
//                            String token = smsToken.token;
//                            Map<String, Object> map = new HashMap();
//                            map.put("mobile", phone);
//                            map.put("type", 2);
//                            map.put("smsToken", token);
//                            return ClientFactory.def(UserService.class).SendSmsCode(map);
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<BaseError>() {
//                    @Override
//                    public void accept(BaseError s) throws Exception {
//                        if (s.msg != null) {
//                            if (timer != null) {
//                                if (task != null) {
//                                    task.cancel();
//                                }
//                            }
//                            btnManagementCode.setEnabled(true);
//                            btnManagementCode.setText("重发验证码");
//                            showToast(s.msg);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        if (timer != null) {
//                            if (task != null) {
//                                task.cancel();
//                            }
//                        }
//                        //                        showToast("请检查网络");
//                        btnManagementCode.setEnabled(true);
//                        btnManagementCode.setText("重发验证码");
//                        LogUtils.e(throwable.getMessage());
//                    }
//                });

    }
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
                            btnManagementCode.setClickable(true);
                            btnManagementCode.setText("发送验证码");
                            count = 60;
                        } else {
                            count--;
                            btnManagementCode.setText(count + "秒");
                        }
                    }
                });
    }
    private void doPwdConfirm() {
        String phone = editManagementPhone.getText().toString().trim();
        String code = editManagementCode.getText().toString().trim();
        String oldPwd = editManagementOldPwd.getText().toString().trim();
        String newPwd = editManagementNewPwd.getText().toString().trim();
        String newPwdAgain = editManagementNewPwdAgain.getText().toString().trim();


        switch (type) {
            case LoginPwdManagementActivity.REMEMBER:
                if (TextUtils.isEmpty(oldPwd)) {
                    showToast("原密码为空");
                    break;
                }
                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdAgain)) {
                    showToast("密码设置为空");
                    break;
                } else if (newPwd.length() < 6 || newPwdAgain.length() < 6) {
                    showToast("新密码至少为6位");
                    break;
                }
                if (!newPwd.equals(newPwdAgain)) {
                    showToast("新密码，确认密码不一致");
                    break;
                }
                doRememberPwdConfirm(oldPwd, newPwd);
                break;
            case LoginPwdManagementActivity.FORGET:
                UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                if (TextUtils.isEmpty(phone) || !phone.equals(user.mobile)) {
                    showToast("手机号码不正确");
                    break;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("验证码为空");
                    break;
                }
                if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwdAgain)) {
                    showToast("密码设置为空");
                    break;
                } else if (newPwd.length() < 6 || newPwdAgain.length() < 6) {
                    showToast("新密码至少为6位");
                    break;
                }
                if (!TextUtils.equals(newPwd, newPwdAgain)) {
                    showToast("新密码，确认密码不一致");
                    break;
                }
                doForgetPwdConfirm(phone, code, newPwd);
                break;
        }

    }

    /**
     * 记得登录密码  确认
     * @param oldPwd
     * @param newPwd
     */
    private void doRememberPwdConfirm(String oldPwd, String newPwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
        params.put("oldPassword", CipherUtils.md5(oldPwd));
        params.put("newPassword", CipherUtils.md5(newPwd));
        params.put("isMd5", true);
        dialog.show();
        Disposable dispRememberPwd = ClientFactory.def(UserService.class).userResetPwd(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError.msg == null) {
                            showToast("修改成功");
                            ActivityControl.removeAllActivity(ChangeLoginPwdActivity.this);
                            startActivity(LoginActivity.class);
                        } else {
                            showToast(baseError.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
        disposables.add(dispRememberPwd);
    }

    /**
     * 忘记原登录密码 确认
     * @param mobile
     * @param validate
     * @param newPwd
     */
    private void doForgetPwdConfirm(String mobile, String validate, String newPwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
        params.put("mobile", mobile);
        params.put("validate", validate);
        params.put("newPassword", CipherUtils.md5(newPwd));
        params.put("isMd5", true);
        dialog.show();
        Disposable disposableForgetPwd = ClientFactory.def(UserService.class).userRetrievePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        if (baseError.msg == null) {
                            showToast("修改成功");
                            ActivityControl.removeAllActivity(ChangeLoginPwdActivity.this);
                            startActivity(LoginActivity.class);
                        } else {
                            showToast(baseError.msg);
                        }
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                });
        disposables.add(disposableForgetPwd);
    }

    /**
     * 加载不同类型的界面
     * @param type
     *         0 记得密码  1 忘记密码
     */
    private void loadTypeView(int type) {
        switch (type) {
            case LoginPwdManagementActivity.REMEMBER:
                editManagementPhone.setVisibility(View.GONE);
                rlManagementCode.setVisibility(View.GONE);
                //                editManagementOldPwd.setVisibility(View.VISIBLE);
                llOldPwd.setVisibility(View.VISIBLE);
                setTitleText(getResources().getString(R.string.management_change_login_pwd));
                break;
            case LoginPwdManagementActivity.FORGET:
                editManagementPhone.setVisibility(View.VISIBLE);
                rlManagementCode.setVisibility(View.VISIBLE);
                //                editManagementOldPwd.setVisibility(View.GONE);
                llOldPwd.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_forget_login_pwd));
                break;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
