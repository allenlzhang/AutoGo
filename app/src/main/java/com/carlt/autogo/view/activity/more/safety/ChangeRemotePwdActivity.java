package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.presenter.safety.ChangeRemotePwdPresenter;
import com.carlt.autogo.presenter.safety.IChangeRemotePwdView;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.widget.PwdEditText;

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
 * Created by Marlon on 2018/9/14.
 */
@CreatePresenter(presenter = ChangeRemotePwdPresenter.class)
public class ChangeRemotePwdActivity extends BaseMvpActivity<ChangeRemotePwdPresenter> implements IChangeRemotePwdView{
    @BindView(R.id.edit_management_remote_phone)
    EditText       editManagementRemotePhone;
    @BindView(R.id.edit_management_remote_code)
    EditText       editManagementRemoteCode;
    @BindView(R.id.btn_management_remote_code)
    Button         btnManagementRemoteCode;
    @BindView(R.id.rl_management_remote_code)
    RelativeLayout rlManagementRemoteCode;
    @BindView(R.id.tv_management_edit_remote_old_pwd)
    TextView       tvManagementEditRemoteOldPwd;
    @BindView(R.id.et_management_remote_old_pwd)
    PwdEditText    etManagementRemoteOldPwd;
    @BindView(R.id.et_management_remote_new_pwd)
    PwdEditText    etManagementRemoteNewPwd;
    @BindView(R.id.et_management_remote_new_pwd_again)
    PwdEditText    etManagementRemoteNewPwdAgain;
    @BindView(R.id.btn_management_remote_confirm)
    Button         btnManagementRemoteConfirm;

    int type = 0;

    /**
     * 倒计时
     */
    private int count = 60;

//    private Timer timer = new Timer();

//    private TimerTask task;
    private UserInfo  info;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_remote_pwd;
    }

    @Override
    public void init() {
        type = getIntent().getIntExtra("resetRemote", 0);
        loadTypeView(type);
        info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
    }


    @OnClick({R.id.btn_management_remote_code, R.id.btn_management_remote_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_remote_code:
                sendCode();
                break;
            case R.id.btn_management_remote_confirm:
                doConfirm(SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
                break;
        }
    }

//    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 111) {
//                count--;
//                if (count > 0) {
//                    btnManagementRemoteCode.setText(count + "秒后重发");
//                } else {
//                    if (timer != null) {
//                        if (task != null) {
//                            task.cancel();
//                        }
//                    }
//                    btnManagementRemoteCode.setEnabled(true);
//                    btnManagementRemoteCode.setText("重发验证码");
//                }
//            }
//            super.handleMessage(msg);
//
//        }
//    };

    private void doConfirm(String token) {
        String mobile = editManagementRemotePhone.getText().toString();
        String code = editManagementRemoteCode.getText().toString();
        String remoteOldPwd = etManagementRemoteOldPwd.getText().toString();
        String remoteNewPwd = etManagementRemoteNewPwd.getText().toString();
        String remoteNewPwdAgain = etManagementRemoteNewPwdAgain.getText().toString();
        switch (type) {
            case RemotePwdManagementActivity.SETREMOTEPWD:
                if (TextUtils.isEmpty(remoteNewPwd) || TextUtils.isEmpty(remoteNewPwdAgain)) {
                    showToast("远程密码不能为空");
                    return;
                }
                if (remoteNewPwd.length() < 6 || remoteNewPwdAgain.length() < 6) {
                    showToast("密码至少为6位数字");
                    return;
                }
                if (!TextUtils.equals(remoteNewPwd, remoteNewPwdAgain)) {
                    showToast("两次密码不一致");
                    return;
                }
                info.remotePwd = CipherUtils.md5(remoteNewPwd);
                getPresenter().setRemotePwdClient(token, remoteNewPwd);

                break;
            case RemotePwdManagementActivity.REMEBERPWD:
                if (TextUtils.isEmpty(remoteOldPwd) || TextUtils.isEmpty(remoteNewPwd) || TextUtils.isEmpty(remoteNewPwdAgain)) {
                    showToast("远程密码不能为空");
                    return;
                }
                if (remoteOldPwd.length() < 6 || remoteNewPwd.length() < 6 || remoteNewPwdAgain.length() < 6) {
                    showToast("密码至少为6位数字");
                    return;
                }
                if (!TextUtils.equals(remoteNewPwd, remoteNewPwdAgain)) {
                    showToast("两次密码不一致");
                    return;
                }
                getPresenter().modifyRemotePwd(token, remoteOldPwd, remoteNewPwd);

                break;
            case RemotePwdManagementActivity.FORGETPWD:
                UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                if (TextUtils.isEmpty(mobile) && !mobile.equals(user.mobile)) {
                    showToast("手机号码不正确");
                    return;
                }
                if (remoteNewPwd.length() < 6 || remoteNewPwdAgain.length() < 6) {
                    showToast("密码至少为6位数字");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(remoteNewPwd) || TextUtils.isEmpty(remoteNewPwdAgain)) {
                    showToast("远程密码不能为空");
                    return;
                }
                if (!TextUtils.equals(remoteNewPwd, remoteNewPwdAgain)) {
                    showToast("两次密码不一致");
                    return;
                }
                getPresenter().remenberRemotePassword(token, mobile, code, remoteNewPwd);
                break;
        }
    }

    private void loadTypeView(int type) {
        switch (type) {
            case RemotePwdManagementActivity.SETREMOTEPWD:
                editManagementRemotePhone.setVisibility(View.GONE);
                rlManagementRemoteCode.setVisibility(View.GONE);
                tvManagementEditRemoteOldPwd.setVisibility(View.GONE);
                etManagementRemoteOldPwd.setVisibility(View.GONE);
                btnManagementRemoteConfirm.setText(getResources().getString(R.string.make_sure));
                setTitleText(getResources().getString(R.string.management_set_remote_control_pwd));
                break;
            case RemotePwdManagementActivity.REMEBERPWD:
                editManagementRemotePhone.setVisibility(View.GONE);
                rlManagementRemoteCode.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_reset_remote_pwd));
                break;
            case RemotePwdManagementActivity.FORGETPWD:
                tvManagementEditRemoteOldPwd.setVisibility(View.GONE);
                etManagementRemoteOldPwd.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_forget_remote_pwd));
                break;
        }
    }

    //发送验证码类型(1=注册,2=找回密码,3=修改密码,4=修改手机,5=绑定微信,6=修改手机[旧手机号],7=远程密码重置,8=车辆过户,9=主机认证,10=更换设备,11=登录,12=注销)
    @SuppressLint("CheckResult")
    private void sendCode() {
        //        count = 60;
        //        btnManagementRemoteCode.setEnabled(false);
        //        btnManagementRemoteCode.setText(count + "秒后重发");
        //        task = new TimerTask() {
        //            @Override
        //            public void run() {
        //                Message msg = new Message();
        //                msg.what = 111;
        //                mHandler.sendMessage(msg);
        //            }
        //        };
        //        timer.schedule(task, 1000, 1000);
        String phone = editManagementRemotePhone.getText().toString().trim();
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        if (!TextUtils.equals(phone, user.mobile)) {
            showToast("手机号码不正确");
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("mobile", phone);
        Observable<BaseError> observable = ObservableHelper.sendValidate(phone, param, 7);

        observable.subscribe(new Consumer<BaseError>() {
            @Override
            public void accept(BaseError baseError) throws Exception {
                if (baseError.msg != null) {
                    showToast(baseError.msg);
                    btnManagementRemoteCode.setClickable(true);
                    btnManagementRemoteCode.setText("发送验证码");
                    count = 60;
                } else {
                    notifySendValidate();
                    showToast("短信下发成功");
                    btnManagementRemoteCode.setClickable(false);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtils.e(throwable.getMessage());
            }
        });


        //        Map<String, String> params = new HashMap<>();
        //        params.put("mobile", phone);
        //
        //
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
        //                            map.put("type", 7);
        //                            map.put("smsToken", token);
        //                            map.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, "'"));
        //                            LogUtils.e(map);
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
        //                            btnManagementRemoteCode.setEnabled(true);
        //                            btnManagementRemoteCode.setText("重发验证码");
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
        //                        btnManagementRemoteCode.setEnabled(true);
        //                        btnManagementRemoteCode.setText("重发验证码");
        //                        LogUtils.e(throwable.getMessage());
        //                    }
        //                });

    }

    Disposable disposable;

    private void notifySendValidate() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (count <= 0) {
                            disposable.dispose();
                            btnManagementRemoteCode.setClickable(true);
                            btnManagementRemoteCode.setText("发送验证码");
                            count = 60;
                        } else {
                            count--;
                            btnManagementRemoteCode.setText(count + "秒");
                        }
                    }
                });
    }

    @Override
    public void setRemotePwdSuccess(BaseError baseError) {
        if (baseError.msg == null) {
            showToast("设置成功");
            SharepUtil.putByBean(GlobalKey.USER_INFO, info);
            finish();
        } else {
            showToast(baseError.msg);
        }
    }

    @Override
    public void modifyRemotePwd(BaseError baseError) {
        if (baseError.msg == null) {
            showToast("修改成功");
            finish();
        } else {
            showToast(baseError.msg);
        }
    }

    @Override
    public void resetRemotePwd(BaseError baseError) {
        if (baseError.msg == null) {
            showToast("修改成功");
            finish();
        } else {
            showToast(baseError.msg);
        }
    }
}
