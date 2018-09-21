package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.carlt.autogo.utils.SharepUtil;


import java.sql.Time;
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
    @BindView(R.id.btn_freeze_commit)
    Button btnFreezeCommit;
    @BindView(R.id.rl_user_freeze)
    RelativeLayout rlUserFreeze;


    private FreezeCommitDialog freezeCommitDialog ;
    @Override
    protected int getContentView() {
        return R.layout.activity_freeze;
    }

    @Override
    public void init() {

        setTitleText("冻结账户");
        freezeCommitDialog =new FreezeCommitDialog(this,R.style.DialogCommon);

        tvFreezeStatusIno.setText("当前账号:" + SharepUtil.<UserInfo>getBeanFromSp("user").mobile + "");

    }


    /**
     * 账户冻结提交
     */
    @OnClick(R.id.btn_freeze_commit)
    public void onViewClicked() {
        freezeCommitDialog.setLisniter(new FreezeCommitDialog.FreezeCommitDialogClickLisniter() {
            @SuppressLint("CheckResult")
            @Override
            public void commit() {
                dialog.show();

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
}
