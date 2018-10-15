package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.CipherUtils;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 * @time 16:52  2018/9/25/025
 * @describe 解冻账户 操作Activity
 */
public class UnFreeezeActivity extends BaseMvpActivity {

    /**
     * 用户名
     */
    @BindView(R.id.tv_user_unfreeze)
    TextView tvUserUnfreeze;
    /**
     * 密码
     */
    @BindView(R.id.ed_unfreeze_pwd)
    EditText edUnfreezePwd;
    /**
     * 密码显示隐藏
     */
    @BindView(R.id.img_passwd_toggle)
    ImageView imgPasswdToggle;
    /**
     * 下一步
     */
    @BindView(R.id.btn_unfreeze_next)
    Button btnUnfreezeNext;

    @BindView(R.id.rl_user_unfreeze)
    RelativeLayout rlUserUnfreeze;
    /**
     * 完成
     */
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.rl_head2)
    RelativeLayout rlHead2;

    private boolean fromMain = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_un_freeeze;
    }

    @Override
    public void init() {
        setTitleText("解冻账户");
        fromMain = getIntent().getBooleanExtra("fromMain", false);

        rlUserUnfreeze.setVisibility(View.VISIBLE);
        rlHead2.setVisibility(View.GONE);
        tvUserUnfreeze.setText("当前账号:" + SharepUtil.<UserInfo>getBeanFromSp("user").mobile + "");
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                if (userInfo.userFreeze == 2) {
                    finish();
                } else {
                    commit();
                }
            }
        });
    }

    private void commit() {
        if (fromMain) {
            startActivity(MainActivity.class);
        } else {
            startActivity(SafetyActivity.class);
        }
    }

    @OnClick({R.id.img_passwd_toggle, R.id.btn_unfreeze_next, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_passwd_toggle:
                passwdToggle(view.isSelected(), edUnfreezePwd, (ImageView) view);
                break;
            case R.id.btn_unfreeze_next:
                String pwd = edUnfreezePwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("密码为空");
                    return;
                }
                unFreeze(pwd);
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    /**
     * 解除冻结
     * userFreeze 1正常 2冻结
     *
     * @param pwd
     */
    @SuppressLint("CheckResult")
    private void unFreeze(String pwd) {
        dialog.show();
        HashMap<String, Object> params = new HashMap<>();
        params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, ""));
        params.put("password", CipherUtils.md5(pwd));
        params.put("isMd5", true);
        params.put("userFreeze", 1);
        ClientFactory.def(UserService.class).freeze(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError.msg == null) {
                            UserInfo userInfo = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                            userInfo.userFreeze = 1;
                            SharepUtil.putByBean(GlobalKey.USER_INFO,userInfo);
                            rlUserUnfreeze.setVisibility(View.GONE);
                            rlHead2.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtils.showShort(baseError.msg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.getMessage());
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
