package com.carlt.autogo.view.activity;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.ObservableHelper;
import com.carlt.autogo.presenter.register.IRegisterView;
import com.carlt.autogo.presenter.register.RegisterPresenter;
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
  * Description : 注册页面
  * @Author     : zhanglei
  * @Date       : 2018/9/10
  */

@CreatePresenter(presenter = RegisterPresenter.class)
public class RegisterActivity extends BaseMvpActivity implements IRegisterView {

    @BindView(R.id.regist_user_phone) EditText registUserPhone;
    @BindView(R.id.regist_user_code) EditText registUserCode;
    @BindView(R.id.send_code) Button sendCode;
    @BindView(R.id.ed_register_pwd) EditText edRegisterPwd;
    @BindView(R.id.ed_register_pwd_d) EditText edRegisterPwdD;
    @BindView(R.id.cb_law) CheckBox cbLaw;
    @BindView(R.id.tv_law) TextView tvLaw;
    @BindView(R.id.btn_register_commit) Button btnRegisterCommit;
    @BindView(R.id.img_passwd_toggle) ImageView imgPasswdToggle;
    @BindView(R.id.img_passwd_toggle_d) ImageView imgPasswdToggleD;

    int count  = 60;
    Disposable disposable ;

    @PresenterVariable
    private RegisterPresenter presenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void init() {
        setTitleText("注册");

    }

    @Override
    public void onRegisterFinish() {
        // TODO: 2018/9/4
        startActivity(LoginActivity.class);
    }

    @OnClick({R.id.send_code})
    public void onClick(View view){
        doSendCode();
    }

    @OnClick({R.id.img_passwd_toggle_d, R.id.img_passwd_toggle})
    public void toggle(View view) {
        if (view.getId() == R.id.img_passwd_toggle) {
            passwdToggle(view.isSelected(), edRegisterPwd, (ImageView) view);
        } else {
            passwdToggle(view.isSelected(), edRegisterPwdD, (ImageView) view);
        }
        view.setSelected(!view.isSelected());
    }

    @SuppressLint("CheckResult")
    private void doSendCode() {

        String phoneNum = registUserPhone.getText().toString().trim();
        boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
        if(!checkOk){
            showToast("请输入正确手机号!");
            return;
        }
        Map<String ,String> param =new HashMap<>();
        param.put("mobile", phoneNum);

        Observable<BaseError> observable = ObservableHelper.sendValidate(phoneNum,param,1);

        observable.subscribe(new Consumer<BaseError>() {
            @Override
            public void accept(BaseError baseError) throws Exception {
                if(baseError.msg != null){
                    showToast(baseError.msg );
                    sendCode.setClickable(true);
                    sendCode.setText("发送验证码");
                    count =60;
                }else {
                    notifSendValidate();
                    showToast("短信下发成功" );
                    sendCode.setClickable(false);
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

    @OnClick(R.id.btn_register_commit)
    public void registeCommit() {

        String phoneNum = registUserPhone.getText().toString().trim();
        String pwd = edRegisterPwd.getText().toString().toString();
        String pwdD = edRegisterPwdD.getText().toString().toString();
        String code =registUserCode.getText().toString().trim() ;

       if( CheckValues(phoneNum,pwd,pwdD,code)){
           if(!cbLaw.isChecked()){
               showToast("您未同意服务条款!");
               return;
           }
           Map<String, Object> params = new HashMap<>();
           params.put("mobile", phoneNum);
           params.put("password", pwd);
           params.put("validate", code);
           params.put("version", 1);
           params.put("moveDeviceName", AutoGoApp.MODEL_NAME);
           params.put("loginModel", AutoGoApp.MODEL);
           params.put("loginSoftType", "Android");
           doRegiste(params);
       }

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

    private void doRegiste(Map<String, Object> params) {
        presenter.register(params);
    }

    public static boolean CheckValues( String phoneNum ,String pwd ,String pwdD ,String code ){
        String regex = "^[A-Za-z0-9-+=;,./~!@#$%^*\\[.*\\]]{6,32}+$";
        if(!phoneNum.equals("-")){
            boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
            if(!checkOk){
                ToastUtils.showShort("请输入正确手机号!");
                return false;
            }
        }
        if(!code.equals("-")){
            if( StringUtils.isEmpty(code)){
                ToastUtils.showShort("验证码为空");
                return false;
            }
        }
        if(!pwd.equals("-") || !pwdD.equals("-")){
            if (!RegexUtils.isMatch(GlobalKey.PWD_REGEX, pwd) || !RegexUtils.isMatch(GlobalKey.PWD_REGEX, pwd)) {
                ToastUtils.setMsgTextSize(15);
                ToastUtils.showLong("密码有误,输入数字,字母,符号,长度6到32");
                return false;
            }
            if (!pwd.equals(pwdD)) {
                ToastUtils.showShort("两次密码不一致");
                return false;
            }
        }

        return  true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(disposable != null){
            disposable.dispose();
        }

    }
}
