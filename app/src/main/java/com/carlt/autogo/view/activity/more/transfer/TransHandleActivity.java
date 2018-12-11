package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.login.FaceLiveCheckActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Description : 过户处理页面
 * Author     : zhanglei
 * Date       : 2018/12/5
 */
public class TransHandleActivity extends BaseMvpActivity {

    @BindView(R.id.tvCarName)
    TextView tvCarName;
    //    @BindView(R.id.tvDuration)
    //    TextView tvDuration;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.btnAgree)
    Button   btnAgree;
    @BindView(R.id.btnRefuseAgree)
    Button   btnRefuseAgree;
    private int id;


    @Override
    protected int getContentView() {
        return R.layout.activity_trans_handle;
    }

    @Override
    public void init() {
        setTitleText("过户处理");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        String carName = intent.getStringExtra("carName");
        tvCarName.setText(carName);
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        tvAccount.setText(user.mobile);
    }

    @OnClick({R.id.btnAgree, R.id.btnRefuseAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAgree:
                //                                doAuthState(1);
                Intent intent = new Intent(this, FaceLiveCheckActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.Trans_Handle_Activity);
                intent.putExtra("transferId", id);
                startActivity(intent);
                break;
            case R.id.btnRefuseAgree:
                CommonDialog.createTwoBtnDialog(this, "您确定要拒绝吗", true, new CommonDialog.DialogWithTitleClick() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        doAuthState(2);
                    }
                });

                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doAuthState(int status) {
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("transferId", id);
        map.put("isAgree", status);
        ClientFactory.def(CarService.class).dealTransferCode(map)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        dialog.dismiss();
                        if (carBaseInfo.err == null) {
                            showToast("操作成功");
                        } else {
                            showToast("操作失败");
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
