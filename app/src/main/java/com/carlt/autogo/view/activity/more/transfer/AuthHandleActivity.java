package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Description : 授权处理页面
 * Author     : zhanglei
 * Date       : 2018/12/5
 */
public class AuthHandleActivity extends BaseMvpActivity {


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

    @SuppressLint("CheckResult")
    private void initCarInfo() {
        dialog.show();
        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        ClientFactory.def(CarService.class).getById(map)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        LogUtils.e(carBaseInfo);
                        tvAuthAccount.setText(carBaseInfo.mobile);
                        tvAuthCar.setText(carBaseInfo.carName);
                        tvAuthDuration.setText(carBaseInfo.duration + "");
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    @OnClick({R.id.btnAgree, R.id.btnRefuseAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAgree:
                doAuthState(3);
                break;
            case R.id.btnRefuseAgree:
                CommonDialog.createTwoBtnDialog(this, "您确定要拒绝吗", true, new CommonDialog.DialogWithTitleClick() {
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
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        ClientFactory.def(CarService.class).modifyStatus(map)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.code == 0) {
                            showToast("操作成功");
                        } else {
                            showToast(carBaseInfo.msg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        showToast("操作失败");
                    }
                });
    }


}
