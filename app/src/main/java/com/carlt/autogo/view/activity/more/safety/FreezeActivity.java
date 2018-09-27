package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;
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
 * @time 15:42  2018/9/21/021
 * @describe 账户冻结
 */
public class FreezeActivity extends BaseMvpActivity {


    @BindView(R.id.img_freeze_head)
    ImageView imgFreezeHead;
    @BindView(R.id.tv_freeze_status)
    TextView tvFreezeStatus;
    @BindView(R.id.tv_freeze_status_ino)
    TextView tvFreezeStatusIno;

    /**
     * 冻结账户提交按钮
     */
    @BindView(R.id.btn_freeze_commit)
    Button btnFreezeCommit;
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

    @Override
    protected int getContentView() {
        return R.layout.activity_freeze;
    }


    @Override
    public void init() {
        freezeCommitDialog = new FreezeCommitDialog(this, R.style.DialogCommon);
        getUserInfo();

    }

    @SuppressLint("CheckResult")
    private void getUserInfo() {
        dialog.show();
        Map<String,String> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token",""));
        ClientFactory.def(UserService.class).getUserInfo(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        dialog.dismiss();
                        if(userInfo.userFreeze == 0){
                            setTitleText("冻结账户");
                            rlUserFreeze.setVisibility(View.VISIBLE);
                            rlUserUnfreeze.setVisibility(View.GONE);

                            tvFreezeStatusIno.setText("当前账号:" + SharepUtil.<UserInfo>getBeanFromSp("user").mobile + "");
                        }else {
                            setTitleText("解冻账户");
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
     */
    @OnClick({R.id.btn_freeze_commit})
    public void onViewClicked() {

        freezeCommitDialog.setLisniter(new FreezeCommitDialog.FreezeCommitDialogClickLisniter() {
            @SuppressLint("CheckResult")
            @Override
            public void commit() {
                dialog.show();

                LogUtils.e(SharepUtil.getPreferences().getString("token", ""));
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                    }
                })
                        .delay(4, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                dialog.dismiss();
                                ToastUtils.showShort("冻结成功");
                                //解冻状态
                                init();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
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
    public void onUnFreeze(){

        Intent intent = new Intent(this,UnFreeezeActivity.class );
        startActivity( intent );
        finish();

    }
}
