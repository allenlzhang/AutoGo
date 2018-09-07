package com.carlt.autogo.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.presenter.register.IRegisterView;
import com.carlt.autogo.presenter.register.RegisterPresenter;
import com.carlt.autogo.view.activity.login.RegisterNextActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

@CreatePresenter(presenter = RegisterPresenter.class)
public class RegisterActivity extends BaseMvpActivity implements IRegisterView {

    @BindView(R.id.regist_user_phone)
    EditText registUserPhone;

    @BindView(R.id.regist_user_code)
    EditText registUserCode;

    @BindView(R.id.send_code)
    Button sendCode;

    @BindView(R.id.btn_register_next)
    Button btnRegisterNext;

    int count  = 60;
    Disposable disposable ;

    @PresenterVariable
    private RegisterPresenter mRegisterPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }


    @Override
    public void init() {
        setTitleText("注册");
        mRegisterPresenter.register();
    }

    @Override
    public void onRegisterFinish() {
        // TODO: 2018/9/4
      setTitleText("注册");
    }


    @OnClick({R.id.btn_register_next ,R.id.send_code})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.send_code:

                sendCode .setClickable(false);
                doSendCode();

                break;

            case R.id.btn_register_next:

                Intent intent = new Intent(this, RegisterNextActivity.class);
                startActivity(intent);

                break;

        }

    }

    @SuppressLint("CheckResult")
    private void doSendCode() {

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
