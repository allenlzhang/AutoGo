package com.carlt.autogo.view.activity.login;

import android.content.Context;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.presenter.login.LoginPresenter;
import com.carlt.autogo.presenter.register.IOtherRegisterView;
import com.carlt.autogo.presenter.register.OtherRegisterPresenter;
import com.carlt.autogo.presenter.register.RegisterPresenter;
import com.mob.MobSDK;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

@CreatePresenter(presenter = OtherRegisterPresenter.class)
public class OtherActivity extends BaseMvpActivity implements IOtherRegisterView {

    @BindView(R.id.login_payment)
    ImageView loginPayment;

    @BindView(R.id.login_wechat)
    ImageView loginWechat;
    private Platform plat;

    @PresenterVariable
    private OtherRegisterPresenter otherRegisterPresenter;

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

                LogUtils.e("login_wechat");
                otherRegisterPresenter.weChatLogin();
                doAuthorize();

                break;
        }

    }

    private void doAuthorize() {


        plat.showUser(null);
        plat.setPlatformActionListener((new MyPlatformActionListener()));
        plat.removeAccount(true);
        plat.SSOSetting(false);
        plat.authorize();

//        if (plat != null) {
//            plat.setPlatformActionListener(new MyPlatformActionListener());
//            if (plat.isAuthValid()) {
//                plat.removeAccount(true);
//                return;
//            }
//            plat.SSOSetting(true);
//            plat.authorize();
//        }

    }

    class MyPlatformActionListener implements PlatformActionListener {
        @Override
        public void onComplete(final Platform platform, int i, final HashMap<String, Object> hashMap) {
            LogUtils.e("onComplete");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MobSDK.getContext(), "Authorize Complete.", Toast.LENGTH_SHORT).show();
//                    if(platform.getName().equals("ShortMessage") && hashMap != null) {
//                        Toast.makeText(MobSDK.getContext(), "ShoreMessage Login Info:" + hashMap.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            throwable.printStackTrace();
            LogUtils.e("onError" + throwable.getMessage());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MobSDK.getContext(), "Authorize Failure", Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
           // Toast.makeText(MobSDK.getContext(), "Cancel Authorize", Toast.LENGTH_SHORT).show();
        }
    }
}
