package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.text.TextUtils;
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
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.view.activity.MainActivity;

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
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 */
public class UserBindPhoneActivity extends BaseMvpActivity {


    /**
     * 绑定手机
     */
    @BindView(R.id.bind_user_phone)
    EditText bindUserPhone;

    /**
     * 填写验证码
     */
    @BindView(R.id.bind_user_code)
    EditText  bindUserCode;
    /**
     * 发送验证码
     */
    @BindView(R.id.send_code)
    Button    sendCode;
    /**
     * 提交
     */
    @BindView(R.id.btn_register_commit)
    Button    btnRegisterCommit;
    @BindView(R.id.etPwd)
    EditText  etPwd;
    @BindView(R.id.iv_pwd_toggle)
    ImageView ivPwdToggle;

    /**
     * 验证码倒计时
     */
    int count = 60;

    /**
     * 轮询取消
     */
    Disposable disposable;

    /**
     * 第三方平台的 id
     */
    String openId;
    /**
     * 三方平台类型 1 支付宝 2  微信
     */
    int    openType;
    private String pwd;


    @Override
    protected int getContentView() {
        return R.layout.activity_user_bind_phone;
    }

    @Override
    public void init() {
        setTitleText("用户绑定手机");
        openId = getIntent().getExtras().getString("openId");
        openType = getIntent().getExtras().getInt("openType");
        ivPwdToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwdToggle(v.isSelected(), etPwd, (ImageView) v);
                v.setSelected(!v.isSelected());
            }
        });
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.send_code)
    public void sendCode() {

        doSendCode();
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

    /**
     * 提交绑定 信息
     */
    @OnClick(R.id.btn_register_commit)
    public void onViewClicked() {
        final String phoneNum = bindUserPhone.getText().toString().trim();
        String code = bindUserCode.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        boolean checkOk = RegexUtils.isMobileExact(phoneNum);
        if (!checkOk) {
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("验证码不能为空!");
            return;
        }
        if (!pwd.equals("-")) {
            if (StringUtils.isEmpty(pwd)) {
                ToastUtils.showLong("密码不能为空");
                return;
            } else if (pwd.length() < 6) {
                ToastUtils.showLong("密码至少为6位");
                return;
            }

            if (!RegexUtils.isMatch(GlobalKey.PWD_REGEX, pwd)) {
                ToastUtils.setMsgTextSize(15);
                ToastUtils.showLong("密码有误,输入数字,字母,符号");
                return;
            }

        }
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("openId", openId);
        parmas.put("openType", openType);
        parmas.put("mobile", phoneNum);
        parmas.put("validate", code);
        parmas.put("regType", GlobalKey.RegStateByOther);
        doOtherRegister(parmas);

    }

    /**
     * 调用三方注册绑定手机接口
     * @param parmas
     *         请求参数
     */
    @SuppressLint("CheckResult")
    private void doOtherRegister(final Map<String, Object> parmas) {

        dialog.show();
        final HashMap<String, Object> map = new HashMap<>();
        map.put("newPassword", pwd);
        ObservableHelper.commonReg(parmas, UserBindPhoneActivity.this)
                .flatMap(new Function<String, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(String s) throws Exception {
                        dialog.dismiss();
                        ToastUtils.showShort(s);
                        return ClientFactory.def(UserService.class).userResetPwd(map);
                    }
                })
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        LogUtils.e(error);
                        if (error.code == 0) {
                            startActivity(MainActivity.class);
                        } else {
                            ToastUtils.showShort(error.msg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.toString());
                    }
                });
        //                .subscribe(new Consumer<String>() {
        //                    @Override
        //                    public void accept(String s) throws Exception {
        //                        dialog.dismiss();
        //                        ToastUtils.showShort(s);
        //                        startActivity(MainActivity.class);
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e(throwable.toString());
        //                    }
        //                });


        // disposables.add(disposable);
    }

    @SuppressLint("CheckResult")
    private void doSendCode() {

        final String phoneNum = bindUserPhone.getText().toString().trim();
        boolean checkOk = RegexUtils.isMobileExact(phoneNum);
        if (!checkOk) {
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("mobile", phoneNum);

        //        //获取短信验证码token
        //        @POST("User/GetSmsToken")
        //        Observable<SmsToken> getSmsToken(@Body Map<String,String> params);

        Disposable disposableSend = ObservableHelper.sendValidate(phoneNum, param, 13)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
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
                        LogUtils.e(throwable.toString());

                    }
                });
        disposables.add(disposableSend);
    }

    /**
     * 更新验证码刷新
     */
    @SuppressLint("CheckResult")
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
                            sendCode.setClickable(false);
                            count--;
                            sendCode.setText(count + "秒");
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
