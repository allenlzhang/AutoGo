package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.MyInputFilter;
import com.carlt.autogo.utils.MyTextWatcher;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class UserBindPwdActivity extends BaseMvpActivity {

    @BindView(R.id.etPwd)
    EditText  etPwd;
    @BindView(R.id.img_passwd_toggle1)
    ImageView imgPasswdToggle1;
    @BindView(R.id.etConfirmPwd)
    EditText  etConfirmPwd;
    @BindView(R.id.img_passwd_toggle2)
    ImageView imgPasswdToggle2;
    @BindView(R.id.btnConfirm)
    Button    btnConfirm;
    @BindView(R.id.cb_law)
    CheckBox  cbLaw;
    private String pwd;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_bind_pwd;
    }

    @Override
    public void init() {
        setTitleText("设置密码");
        //密码输入屏蔽 中文其他不满足需求的字符
        etPwd.setFilters(new InputFilter[]{new MyInputFilter()});
        etConfirmPwd.setFilters(new InputFilter[]{new MyInputFilter()});


        etPwd.addTextChangedListener(new MyTextWatcher());
        etConfirmPwd.addTextChangedListener(new MyTextWatcher());
        cbLaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.img_passwd_toggle1, R.id.img_passwd_toggle2, R.id.btnConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_passwd_toggle1:
                passwdToggle(view.isSelected(), etPwd, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
            case R.id.img_passwd_toggle2:
                passwdToggle(view.isSelected(), etConfirmPwd, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
            case R.id.btnConfirm:
                setPwd();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void setPwd() {
        pwd = etPwd.getText().toString().trim();
        String confirmPwd = etConfirmPwd.getText().toString().trim();

        if (StringUtils.isEmpty(pwd)) {
            showToast("密码不能为空");
            return;
        } else if (pwd.length() < 6) {
            showToast("密码至少为6位");
            return;
        }
        if (StringUtils.isEmpty(confirmPwd)) {
            showToast("密码不能为空");
            return;
        } else if (confirmPwd.length() < 6) {
            showToast("密码至少为6位");
            return;
        }
        if (!RegexUtils.isMatch(GlobalKey.PWD_REGEX, pwd)) {
            ToastUtils.setMsgTextSize(15);
            showToast("密码有误,输入数字,字母,符号");
            return;
        }
        if (!RegexUtils.isMatch(GlobalKey.PWD_REGEX, confirmPwd)) {
            ToastUtils.setMsgTextSize(15);
            showToast("密码有误,输入数字,字母,符号");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            showToast("两次密码输入不一致");
            return;
        }
//        if (!cbLaw.isChecked()) {
//            showToast("您未同意服务条款!");
//            return;
//        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("newPassword", pwd);
        ClientFactory.def(UserService.class).userResetPwd(map)
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError error) throws Exception {
                        LogUtils.e(error);
                        if (error.code == 0) {
                            UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                            user.password = pwd;
                            SharepUtil.putByBean(GlobalKey.USER_INFO, user);
                            ActivityControl.removeAllActivity(UserBindPwdActivity.this);
                            startActivity(MainActivity.class);
                        } else {
                            ToastUtils.showShort(error.msg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        ToastUtils.showShort("设置失败，请检查网络");
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
