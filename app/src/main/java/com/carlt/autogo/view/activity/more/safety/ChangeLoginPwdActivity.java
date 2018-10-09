package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.RetrievePassword;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/9/13.
 */
public class ChangeLoginPwdActivity extends BaseMvpActivity {
    @BindView(R.id.edit_management_phone)
    EditText editManagementPhone;
    @BindView(R.id.edit_management_code)
    EditText editManagementCode;
    @BindView(R.id.btn_management_code)
    Button btnManagementCode;
    @BindView(R.id.rl_management_code)
    RelativeLayout rlManagementCode;
    @BindView(R.id.edit_management_old_pwd)
    EditText editManagementOldPwd;
    @BindView(R.id.edit_management_new_pwd)
    EditText editManagementNewPwd;
    @BindView(R.id.edit_management_new_pwd_again)
    EditText editManagementNewPwdAgain;
    @BindView(R.id.edit_management_confirm)
    Button editManagementConfirm;

    private int type = 0;

    /**
     * 倒计时
     */
    private int count = 60;

    private Timer timer = new Timer();

    private TimerTask task;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_login_pwd;
    }

    @Override
    public void init() {
        type = getIntent().getIntExtra("changePwd",0);
        loadTypeView(type);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 111){
                count--;
                if (count > 0) {
                    btnManagementCode.setText(count + "秒后重发");
                } else {
                    if (timer != null) {
                        if (task != null) {
                            task.cancel();
                        }
                    }
                    btnManagementCode.setEnabled(true);
                    btnManagementCode.setText("重发验证码");
                }
            }
            super.handleMessage(msg);

        }
    };

    @OnClick({R.id.btn_management_code, R.id.edit_management_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_code:
                sendCode();
                break;
            case R.id.edit_management_confirm:
                doPwdConfirm();
                break;
        }
    }
    //发送验证码类型(1=注册,2=找回密码,3=修改密码,4=修改手机,5=绑定微信,6=修改手机[旧手机号],7=远程密码重置,8=车辆过户,9=主机认证,10=更换设备,11=登录,12=注销)
    @SuppressLint("CheckResult")
    private void sendCode(){
        count = 60;
        btnManagementCode.setEnabled(false);
        btnManagementCode.setText(count + "秒后重发");
        task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 111;
                mHandler.sendMessage(msg);
            }
        };
        timer.schedule(task,1000,1000);
        final String phone = editManagementPhone.getText().toString().trim();
        Map<String,String> params = new HashMap<>();
        params.put("mobile",phone);

        ClientFactory.def(UserService.class).getSmsToken(params)
                .flatMap(new Function<SmsToken, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(SmsToken smsToken) throws Exception {
                        if (smsToken.msg != null){
                            ToastUtils.showShort(smsToken.msg.msg);
                            return null;
                        }else {
                            String token = smsToken.token;
                            Map<String, Object> map = new HashMap();
                            map.put("mobile", phone);
                            map.put("type", 2);
                            map.put("token", token);
                            return ClientFactory.def(UserService.class).SendSmsCode(map);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError s) throws Exception {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        btnManagementCode.setEnabled(true);
                        btnManagementCode.setText("重发验证码");
                        if (s.msg != null){
                            ToastUtils.showShort(s.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (timer != null) {
                            if (task != null) {
                                task.cancel();
                            }
                        }
                        btnManagementCode.setEnabled(true);
                        btnManagementCode.setText("重发验证码");
                        LogUtils.e(throwable.getMessage());
                    }
                });

    }

    private void doPwdConfirm() {
        String phone = editManagementPhone.getText().toString().trim();
        String code = editManagementCode.getText().toString().trim();
        String oldPwd = editManagementOldPwd.getText().toString().trim();
        String newPwd = editManagementNewPwd.getText().toString().trim() ;
        String newPwdAgain = editManagementNewPwdAgain.getText().toString().trim() ;


        switch (type){
            case LoginPwdManagementActivity.REMEMBER:
                if (TextUtils.isEmpty(oldPwd)){
                    ToastUtils.showShort("原密码为空");
                    break;
                }
                if(TextUtils.isEmpty(newPwd)  || TextUtils.isEmpty(newPwdAgain) ){
                    ToastUtils.showShort("密码设置为空");
                    break;
                }
                if(!newPwd.equals(newPwdAgain)){
                    ToastUtils.showShort("两次密码不一致");
                    break;
                }
                doRememberPwdConfirm(oldPwd,newPwd);
                break;
            case LoginPwdManagementActivity.FORGET:
                if (TextUtils.isEmpty(phone)){
                    ToastUtils.showShort("请输入手机号码");
                    break;
                }
                if (TextUtils.isEmpty(code)){
                    ToastUtils.showShort("验证码为空");
                    break;
                }
                if(TextUtils.isEmpty(newPwd)  || TextUtils.isEmpty(newPwdAgain) ){
                    ToastUtils.showShort("密码设置为空");
                    break;
                }
                if(!TextUtils.equals(newPwd,newPwdAgain)){
                    ToastUtils.showShort("两次密码不一致");
                    break;
                }
                doForgetPwdConfirm(phone,code,newPwd);
                break;
        }

    }

    /**
     * 记得登录密码  确认
     * @param oldPwd
     * @param newPwd
     */
    private void doRememberPwdConfirm(String oldPwd,String newPwd){
        HashMap<String ,Object> params =new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN ,"'"));
        params.put("oldPassword",oldPwd);
        params.put("newPassword",newPwd);
        params.put("isMd5",true);
        dialog.show();
        Disposable dispRememberPwd = ClientFactory.def(UserService.class).userResetPwd(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(5, TimeUnit.SECONDS)
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        dialog.dismiss();
                        if(user.err== null){
                            ToastUtils.showShort("修改成功");
                            finish();
                        }else {
                            ToastUtils.showShort(user.err.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.toString());
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
    private void doForgetPwdConfirm(String mobile,String validate,String newPwd){
        HashMap<String ,Object> params =new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN ,"'"));
        params.put("mobile",mobile);
        params.put("validate",validate);
        params.put("newPassword", CipherUtils.md5(newPwd));
        params.put("isMd5",true);
        dialog.show();
        Disposable disposableForgetPwd = ClientFactory.def(UserService.class).userRetrievePassword(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(2,TimeUnit.SECONDS)
                .subscribe(new Consumer<RetrievePassword>() {
                    @Override
                    public void accept(RetrievePassword retrievePassword) throws Exception {
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
     * @param type  0 记得密码  1 忘记密码
     */
    private void loadTypeView(int type){
        switch (type){
            case LoginPwdManagementActivity.REMEMBER:
                editManagementPhone.setVisibility(View.GONE);
                rlManagementCode.setVisibility(View.GONE);
                editManagementOldPwd.setVisibility(View.VISIBLE);
                setTitleText(getResources().getString(R.string.management_change_login_pwd));
                break;
            case LoginPwdManagementActivity.FORGET:
                editManagementPhone.setVisibility(View.VISIBLE);
                rlManagementCode.setVisibility(View.VISIBLE);
                editManagementOldPwd.setVisibility(View.GONE);
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
