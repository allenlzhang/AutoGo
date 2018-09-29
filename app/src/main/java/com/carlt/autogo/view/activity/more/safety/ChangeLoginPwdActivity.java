package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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

    @Override
    protected int getContentView() {
        return R.layout.activity_change_login_pwd;
    }

    @Override
    public void init() {
        type = getIntent().getIntExtra("changePwd",0);
        loadTypeView(type);
    }

    @OnClick({R.id.btn_management_code, R.id.edit_management_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_code:
                break;
            case R.id.edit_management_confirm:
                doRememberPwdConfirm();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doRememberPwdConfirm() {
        String oldPwd = editManagementOldPwd.getText().toString().trim();
        String newPwd = editManagementNewPwd.getText().toString().trim() ;
        String newPwdAgain = editManagementNewPwdAgain.getText().toString().trim() ;

        if(TextUtils.isEmpty(oldPwd)  ||TextUtils.isEmpty(newPwd)  || TextUtils.isEmpty(newPwdAgain) ){
            ToastUtils.showShort("密码设置为空");
            return;
        }
        if(!newPwd.equals(newPwdAgain)){
            ToastUtils.showShort("两次密码不一致");
            return;
        }
        HashMap<String ,Object> params =new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN ,"'"));
        params.put("oldPassword",oldPwd);
        params.put("newPassword",newPwd);

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
