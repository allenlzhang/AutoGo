package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.SmsToken;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

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
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author wsq
 * @time 16:24  2018/10/9/009
 * @describe 用户绑定手机页面
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
    EditText bindUserCode;
    /**
     * 发送验证码
     */
    @BindView(R.id.send_code)
    Button sendCode;
    /**
     * 提交
     */
    @BindView(R.id.btn_register_commit)
    Button btnRegisterCommit;

    /**
     * 验证码倒计时
     */
    int count  = 60;

    /**
     * 轮询取消
     */
    Disposable disposable ;

    /**
     * 第三方平台的 id
     */
    String openId ;
    /**
     * 三方平台类型 1 支付宝 2  微信
     */
    int openType ;
    @Override
    protected int getContentView() {
        return R.layout.activity_user_bind_phone;
    }

    @Override
    public void init() {
        setTitleText("用户绑定手机");
        openId = getIntent().getExtras().getString("openId");
        openType = getIntent().getExtras().getInt("openType");
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.send_code)
    public void sendCode() {

        doSendCode();
    }


    /**
     * 提交绑定 信息
     */
    @OnClick(R.id.btn_register_commit)
    public void onViewClicked() {
        final String phoneNum = bindUserPhone.getText().toString().trim();
        String code = bindUserCode.getText().toString().trim();
        boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
//        if(!checkOk){
//            ToastUtils.showShort("请输入正确手机号!");
//            return;
//        }
//
//        if(TextUtils.isEmpty(code)){
//            ToastUtils.showShort("验证码不能为空!");
//            return;
//        }

        Map<String ,Object> parmas = new HashMap<>();
        parmas.put("openId",openId);
        parmas.put("openType",openType);
        parmas.put("mobile",phoneNum);
        parmas.put("validate",code);

        doOtherRegister(parmas);

    }

    /**
     * 调用三方注册绑定手机接口
     * @param parmas  请求参数
     */
    private void doOtherRegister(Map<String, Object> parmas) {

        Disposable disposable = ClientFactory.def(UserService.class).registerByOpenApi(parmas)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError user) throws Exception {
                        if(user.msg != null){
                            ToastUtils.showShort(user.msg);
                        }else {
                            ToastUtils.showShort("注册成功!");
                            LogUtils.e(user,toString());
                            finish();
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.toString());
                    }
                });


        disposables.add(disposable);
    }

    @SuppressLint("CheckResult")
    private void doSendCode() {

        final String phoneNum = bindUserPhone.getText().toString().trim();
        boolean  checkOk =  RegexUtils.isMobileExact(phoneNum) ;
        if(!checkOk){
            ToastUtils.showShort("请输入正确手机号!");
            return;
        }

        Map<String ,String> param =new HashMap<>();
        param.put("mobile", phoneNum);

//        //获取短信验证码token
//        @POST("User/GetSmsToken")
//        Observable<SmsToken> getSmsToken(@Body Map<String,String> params);

        Disposable disposableSend =    ClientFactory.def(UserService.class).getSmsToken(param)
                .flatMap(new Function<SmsToken, ObservableSource<BaseError>>() {
                    @Override
                    public ObservableSource<BaseError> apply(SmsToken smsToken) throws Exception {
                        if (smsToken.msg != null){
                            ToastUtils.showShort(smsToken.msg.msg);
                            return null;
                        }else {
                            String token = smsToken.token;
                            Map<String, Object> map = new HashMap();
                            map.put("mobile", phoneNum);
                            map.put("type", 1);
                            map.put("smsToken", token);
                            return ClientFactory.def(UserService.class).SendSmsCode(map);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        if(baseError.msg != null){
                            ToastUtils.showShort(baseError.msg );
                            sendCode.setClickable(true);
                            sendCode.setText("发送验证码");
                            count =60;
                        }else {
                            notifSendValidate();
                            ToastUtils.showShort("短信下发成功" );

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
        disposable =   Observable.interval(1, TimeUnit.SECONDS)
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
                            sendCode.setClickable(false);
                            count -- ;
                            sendCode.setText(count + "秒");
                        }
                    }
                });
    }
}
