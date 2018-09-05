package com.carlt.autogo.view.activity;

import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.presenter.register.IRegisterView;
import com.carlt.autogo.presenter.register.RegisterPresenter;

import butterknife.BindView;

@CreatePresenter(presenter = RegisterPresenter.class)
public class RegisterActivity extends BaseMvpActivity implements IRegisterView {


    @BindView(R.id.tv)
    TextView tv;
    @PresenterVariable
    private RegisterPresenter mRegisterPresenter;

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }


    @Override
    public void init() {
        mRegisterPresenter.register();
    }

    @Override
    public void onRegisterFinish() {
        // TODO: 2018/9/4
        tv.setText("注册成功");
    }


}
