package com.carlt.autogo.view.activity;


import android.app.Dialog;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.BaseDialog;
import com.carlt.autogo.common.dialog.LoginMoreDialog;
import com.carlt.autogo.presenter.login.ILoginView;
import com.carlt.autogo.presenter.login.LoginPresenter;
import com.carlt.autogo.view.activity.login.ForgotActivity;

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

    @BindView(R.id.version_code) TextView versionCode;

    @BindView(R.id.user_phone) EditText userPhone;

    @BindView(R.id.user_pwd) EditText userPWd;

    @BindView(R.id.passwd_toggle) ImageView passwdToggle;

    @BindView(R.id.login_commit) Button loginCommit;

    @BindView(R.id.forgot_passwd) TextView forgotPasswd;

    @BindView(R.id.user_regist) TextView userRegist;

    @BindView(R.id.btn_more) Button btnMore;

    int time =20 ;
     Disposable disposable ;
    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        setTitleText("登录");
        setBaseBackStyle(getResources().getDrawable(R.drawable.common_close_select));

    }


    @OnClick({R.id.btn_more, R.id.user_regist, R.id.login_commit, R.id.passwd_toggle, R.id.forgot_passwd})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_more:

                BaseDialog baseDialog = new LoginMoreDialog(this);
                baseDialog.show();


                break;
            case R.id.user_regist:
                Intent intentRegist = new Intent(this, RegisterActivity.class);
                startActivity(intentRegist);

                break;
            case R.id.login_commit:
                getPresenter().login();
                break;
            case R.id.passwd_toggle:

                passwdToggle.setSelected(!passwdToggle.isSelected());
                passwdToggle(passwdToggle.isSelected());

                break;
            case R.id.forgot_passwd:
                Intent intentForgot = new Intent(this, ForgotActivity.class);
                startActivity(intentForgot);
                break;

        }
    }


    @Override
    public void loginFinish() {

    }

    private void passwdToggle(boolean selected) {

        if (selected) {
            userPWd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwdToggle.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_hide));
            passwdToggle.setTag("off");
        } else {
            userPWd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwdToggle.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_show));
            passwdToggle.setTag("on");
        }

    }

}
