package com.carlt.autogo.view.activity.login;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.login.LoginPresenter;
import com.carlt.autogo.presenter.register.IOtherRegisterView;
import com.carlt.autogo.presenter.register.OtherRegisterPresenter;
import com.carlt.autogo.presenter.register.RegisterPresenter;
import com.carlt.autogo.utils.SharepUtil;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


@CreatePresenter(presenter = OtherRegisterPresenter.class)
public class OtherActivity extends BaseMvpActivity implements IOtherRegisterView {

    @BindView(R.id.login_payment)
    ImageView loginPayment;

    @BindView(R.id.login_wechat)
    ImageView loginWechat;
    private Platform plat;
    UUDialog dialog ;

    @PresenterVariable
    private OtherRegisterPresenter otherRegisterPresenter;
    private String errorMsg;

    @Override
    protected int getContentView() {
        return R.layout.activity_other;
    }

    @Override
    public void init() {
        setTitleText("第三方登录");
        setBaseBackStyle(getResources().getDrawable(R.drawable.common_close_select));
        Platform[] list = ShareSDK.getPlatformList();
        if(list != null){
            plat =list[0];
        }
        dialog = new UUDialog(this);
    }

    @OnClick({R.id.login_payment ,R.id.login_wechat })
    public void onClick (View view ){
        switch (view.getId()){

            case R.id.login_payment:

                otherRegisterPresenter.paymentLogin();
                AuthTask task = new AuthTask(this);
               // task.authV2()
                break;

            case R.id.login_wechat:
                dialog.show();
                otherRegisterPresenter.weChatLogin();
                doAuthorize();

                break;
        }

    }

    private void doAuthorize() {

        plat.setPlatformActionListener((new MyPlatformActionListener()));
        plat.removeAccount(true);
        plat.SSOSetting(false);
        plat.authorize();

    }

    class MyPlatformActionListener implements PlatformActionListener {
        @Override
        public void onComplete(final Platform platform, int i, final HashMap<String, Object> hashMap) {
            LogUtils.e("onComplete");

            platform.setPlatformActionListener(new PlatformActionListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    LogUtils.e(hashMap);

                    HashMap<String,Object> params = new HashMap<>();
                    params.put("openId",hashMap.get("openid"));
                    params.put("type",2);

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    dialog.dismiss();
                    ToastUtils.showShort("登录失败");
                    LogUtils.e(throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    dialog.dismiss();
                    ToastUtils.showShort("登录取消");
                }
            });
            platform.showUser(null);

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            throwable.printStackTrace();
            dialog.dismiss();
            ToastUtils.showShort("登录失败");
            LogUtils.e(throwable.toString());

        }

        @Override
        public void onCancel(Platform platform, int i) {
            ToastUtils.showShort("取消登录");
            dialog.dismiss();
        }
    }
}
