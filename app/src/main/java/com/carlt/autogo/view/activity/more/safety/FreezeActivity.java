package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.FreezeCommitDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 */
public class FreezeActivity extends BaseMvpActivity {


    @BindView(R.id.img_freeze_head)
    ImageView imgFreezeHead;
    @BindView(R.id.tv_freeze_status)
    TextView  tvFreezeStatus;
    @BindView(R.id.tv_freeze_status_ino)
    TextView  tvFreezeStatusIno;

    /**
     * 冻结账户提交按钮
     */
    @BindView(R.id.btn_freeze_commit)
    Button         btnFreezeCommit;
    /**
     * 冻结账户父控件
     */
    @BindView(R.id.rl_user_freeze)
    RelativeLayout rlUserFreeze;

    /**
     * 解冻账户
     */
    @BindView(R.id.btn_unfreeze_commit)
    Button btnUnfreezeCommit;

    /**
     * 解冻账户界面 父控件
     */
    @BindView(R.id.rl_user_unfreeze)
    RelativeLayout rlUserUnfreeze;


    private FreezeCommitDialog freezeCommitDialog;

    private boolean fromMain = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_freeze;
    }


    @Override
    public void init() {
        fromMain = getIntent().getBooleanExtra("fromMain", false);
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        if (user.userFreeze == 1) {
            setTitleText("冻结账户");
        } else if (user.userFreeze == 2) {
            setTitleText("解冻账户");
        }

        freezeCommitDialog = new FreezeCommitDialog(this, R.style.DialogCommon);
        getUserInfo();
        tvBaseRight.setTextColor(getResources().getColor(R.color.colorBlue));
        tvBaseRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharepUtil.put(GlobalKey.USER_TOKEN, "");
                startActivity(LoginActivity.class);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getUserInfo() {
        dialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token", ""));
        ClientFactory.def(UserService.class).getUserInfo(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        dialog.dismiss();
                        if (userInfo.userFreeze == 1) {
                            rlUserFreeze.setVisibility(View.VISIBLE);
                            rlUserUnfreeze.setVisibility(View.GONE);
                            String mobile = SharepUtil.<UserInfo>getBeanFromSp("user").mobile;
                            StringBuilder builder = new StringBuilder(mobile);
                            tvFreezeStatusIno.setText("当前账号:" + builder.replace(3, 7, "****"));
                        } else {
                            tvBaseRight.setText("退出");
                            ivBaseBack.setVisibility(View.GONE);
                            rlUserFreeze.setVisibility(View.GONE);
                            rlUserUnfreeze.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable.toString());
                    }
                });
    }


    /**
     * 账户冻结提交
     * userFreeze 1正常 2冻结
     */
    @OnClick({R.id.btn_freeze_commit})
    public void onViewClicked() {

        freezeCommitDialog.setLisniter(new FreezeCommitDialog.FreezeCommitDialogClickLisniter() {
            @SuppressLint("CheckResult")
            @Override
            public void commit() {
                dialog.show();
                HashMap<String, Object> params = new HashMap<>();
                params.put(GlobalKey.USER_TOKEN, SharepUtil.getPreferences().getString(GlobalKey.USER_TOKEN, ""));
                params.put("password", "");
                params.put("isMd5", true);
                params.put("userFreeze", 2);
                LogUtils.e(params);
                ClientFactory.def(UserService.class).freeze(params)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseError>() {
                            @Override
                            public void accept(BaseError baseError) throws Exception {
                                dialog.dismiss();
                                if (baseError.msg == null) {
                                    UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
                                    info.userFreeze = 2;
                                    SharepUtil.putByBean(GlobalKey.USER_INFO, info);
                                    ToastUtils.showShort("冻结成功");
                                    ivBaseBack.setVisibility(View.GONE);
                                    //                                    tvBaseRight.setText("");
                                    setTitleText("解冻账户");
                                    tvBaseRight.setText("退出");
                                    rlUserFreeze.setVisibility(View.GONE);
                                    rlUserUnfreeze.setVisibility(View.VISIBLE);
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
        });
        freezeCommitDialog.show();
    }


    /**
     * 解除账号冻结提交
     */
    @OnClick(R.id.btn_unfreeze_commit)
    public void onUnFreeze() {

        Intent intent = new Intent(this, UnFreeezeActivity.class);
        intent.putExtra("fromMain", fromMain);
        startActivity(intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        UserInfo userInfo = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        if (keyCode == KeyEvent.KEYCODE_BACK && userInfo.userFreeze == 2) {
            SharepUtil.put(GlobalKey.USER_TOKEN, "");
            ActivityControl.removeAllActivity(this);
            startActivity(LoginActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }
}
