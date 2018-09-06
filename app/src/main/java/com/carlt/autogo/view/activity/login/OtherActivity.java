package com.carlt.autogo.view.activity.login;

import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.presenter.login.LoginPresenter;
import com.carlt.autogo.presenter.register.IOtherRegisterView;
import com.carlt.autogo.presenter.register.OtherRegisterPresenter;
import com.carlt.autogo.presenter.register.RegisterPresenter;

import butterknife.BindView;
import butterknife.OnClick;

@CreatePresenter(presenter = OtherRegisterPresenter.class)
public class OtherActivity extends BaseMvpActivity implements IOtherRegisterView {

    @BindView(R.id.login_payment)
    ImageView loginPayment;

    @BindView(R.id.login_wechat)
    ImageView loginWechat;

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
    }

    @OnClick({R.id.login_payment ,R.id.login_wechat })
    public void onClick (View view ){
        switch (view.getId()){

            case R.id.login_payment:

                otherRegisterPresenter.paymentLogin();

                break;

            case R.id.login_wechat:

                otherRegisterPresenter.weChatLogin();
                break;
        }

    }
}
