package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.WithOutCodeDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/9/13.
 */
@SuppressLint("Registered")
public class RemotePwdManagementActivity extends BaseMvpActivity implements WithOutCodeDialog.WithoutCodeListener,CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.ll_management_set_remote_pwd)
    LinearLayout llManagementSetRemotePwd;
    @BindView(R.id.ll_management_remember_remote_old_pwd)
    LinearLayout llManagementRememberRemoteOldPwd;
    @BindView(R.id.ll_management_forget_remote_old_pwd)
    LinearLayout llManagementForgetRemoteOldPwd;
    @BindView(R.id.ll_management_without_encryption_date)
    LinearLayout llManagementWithoutEncryptionDate;
    @BindView(R.id.tv_management_without_encryption_date)
    TextView tvManagementWithoutEncryptionDate;
    @BindView(R.id.cb_management_without_encryption)
    CheckBox cbManagementRemoteSwitch;

    public static final int SETREMOTEPWD = 0;   //设置远程密码

    public static final int REMEBERPWD = 1;     //记得远程密码

    public static final int FORGETPWD = 2;      //忘记远程密码

    private WithOutCodeDialog mDialog;


    @Override
    protected int getContentView() {
        return R.layout.activity_remote_pwd_management;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.management_remote_pwd));
        mDialog = new WithOutCodeDialog(RemotePwdManagementActivity.this, R.style.DialogCommon);
        mDialog.setListener(this);
        cbManagementRemoteSwitch.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.ll_management_set_remote_pwd, R.id.ll_management_remember_remote_old_pwd, R.id.ll_management_forget_remote_old_pwd, R.id.ll_management_without_encryption_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_management_set_remote_pwd:
                startActivity(SETREMOTEPWD);
                break;
            case R.id.ll_management_remember_remote_old_pwd:
                startActivity(REMEBERPWD);
                break;
            case R.id.ll_management_forget_remote_old_pwd:
                startActivity(FORGETPWD);
                break;
            case R.id.ll_management_without_encryption_date:
                mDialog.show();
                break;
        }
    }

    private void startActivity(int type) {
        Intent intent = new Intent(RemotePwdManagementActivity.this, ChangeRemotePwdActivity.class);
        intent.putExtra("resetRemote", type);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")

    @Override
    public void OnClickWithoutListener(String d) {
        int date = Integer.parseInt(d);
        if (date < 5 || date > 60) {
            ToastUtils.showShort("远程免密时间范围5~60min");
        } else {
            tvManagementWithoutEncryptionDate.setText(d + "分钟");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b){
            remoteSwitch(2);
        }else {
            remoteSwitch(1);
        }
    }

    /**
     * 远程开关
     * remotePwdSwitch 1 开 2 关
     */
    private void remoteSwitch(final int remotePwdSwitch){
        HashMap<String,Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN,""));
        params.put("remotePwdSwitch",remotePwdSwitch);
        dialog.show();
        Disposable disposableRemoteSwitch = ClientFactory.def(UserService.class).modifyRemoteSwitch(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError.msg == null){
                            if (remotePwdSwitch == 1){
                                cbManagementRemoteSwitch.setChecked(true);
                            }else {
                                cbManagementRemoteSwitch.setChecked(false);
                            }
                            ToastUtils.showShort("操作成功");
                        }else {
                            ToastUtils.showShort(baseError.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                });
        disposables.add(disposableRemoteSwitch);
    }
}
