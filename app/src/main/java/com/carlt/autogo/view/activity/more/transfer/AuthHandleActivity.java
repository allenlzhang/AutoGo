package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.carauth.AuthHandlePresenter;
import com.carlt.autogo.presenter.carauth.IAuthHandleView;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.MyTimeUtils;
import com.carlt.autogo.view.activity.login.FaceLiveCheckActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 授权处理页面
 * Author     : zhanglei
 * Date       : 2018/12/5
 */
@CreatePresenter(presenter = AuthHandlePresenter.class)
public class AuthHandleActivity extends BaseMvpActivity<AuthHandlePresenter> implements IAuthHandleView {


    @BindView(R.id.tvAuthCar)
    TextView tvAuthCar;
    @BindView(R.id.tvAuthDuration)
    TextView tvAuthDuration;
    @BindView(R.id.tvAuthAccount)
    TextView tvAuthAccount;
    @BindView(R.id.btnAgree)
    Button   btnAgree;
    @BindView(R.id.btnRefuseAgree)
    Button   btnRefuseAgree;
    private int id;

    @Override
    protected int getContentView() {
        return R.layout.activity_auth_handle;
    }


    @Override
    public void init() {
        setTitleText("授权处理");
        initCarInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        interval();
    }

    int        count = 600;
    Disposable disposable;

    private void interval() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (count <= 0) {
                            CommonDialog.createOneBtnDialog(AuthHandleActivity.this, "过户取消", false, new CommonDialog.DialogOneBtnClick() {
                                @Override
                                public void onOneBtnClick() {
                                    finish();
                                }
                            });
                            disposable.dispose();
                        } else {
                            count--;
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @SuppressLint("CheckResult")
    private void initCarInfo() {
        //        dialog.show();
        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        getPresenter().getAuthInfo(map);
        //        ClientFactory.def(CarService.class).getById(map)
        //                .subscribe(new Consumer<CarBaseInfo>() {
        //                    @Override
        //                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
        //                        LogUtils.e(carBaseInfo);
        //                        tvAuthAccount.setText(carBaseInfo.mobile);
        //                        tvAuthCar.setText(carBaseInfo.carName);
        //                        String s = MyTimeUtils.formatDateTime(carBaseInfo.duration);
        //                        tvAuthDuration.setText(s);
        //                        dialog.dismiss();
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                    }
        //                });
    }

    @Override
    public void getCarInfo(CarBaseInfo carBaseInfo) {
        LogUtils.e(carBaseInfo);
        tvAuthAccount.setText(carBaseInfo.mobile);
        tvAuthCar.setText(carBaseInfo.carName);
        String s = MyTimeUtils.formatDateTime(carBaseInfo.duration);
        LogUtils.e(s);
        tvAuthDuration.setText(s);
    }

    @Override
    public void refuseAuth(CarBaseInfo carBaseInfo) {
        if (carBaseInfo.code == 0) {
            showToast("操作成功");
            closeActivity();
        } else {
            showToast(carBaseInfo.msg);
            finish();
        }

    }

    @OnClick({R.id.btnAgree, R.id.btnRefuseAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAgree:
                //                doAuthState(3);
                Intent intent = new Intent(this, FaceLiveCheckActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.Auth_Handle_Activity);
                intent.putExtra("authId", id);
                startActivity(intent);
                break;
            case R.id.btnRefuseAgree:
                String mobile = tvAuthAccount.getText().toString().trim();
                CommonDialog.createTwoBtnDialog(this, "是否拒绝将爱车授权给" + "“" + mobile + "”" + "使用？", true, new CommonDialog.DialogWithTitleClick() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        doAuthState(4);
                    }
                });

                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doAuthState(int status) {
        //        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        getPresenter().refuseAuth(map);
        //        ClientFactory.def(CarService.class).modifyStatus(map)
        //                .subscribe(new Consumer<CarBaseInfo>() {
        //                    @Override
        //                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
        //                        dialog.dismiss();
        //                        if (carBaseInfo.code == 0) {
        //                            showToast("操作成功");
        //                            closeActivity();
        //                        } else {
        //                            showToast(carBaseInfo.msg);
        //                        }
        //
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                        showToast("操作失败");
        //                    }
        //                });
    }

    @Override
    public void finish() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }

        }
        super.finish();

    }

    private void closeActivity() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof AuthQRCodeActivity) {
                activity.finish();
            }

        }
        finish();
    }


}
