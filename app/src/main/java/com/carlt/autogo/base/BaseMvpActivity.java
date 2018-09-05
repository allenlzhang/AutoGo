package com.carlt.autogo.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BaseMvpView;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.basemvp.PresenterDispatch;
import com.carlt.autogo.basemvp.PresenterProviders;

import butterknife.ButterKnife;


public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseMvpView {

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch  mPresenterDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getContentView());
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);
        ButterKnife.bind(this);
        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected abstract int getContentView();

    public abstract void init();

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showLoading(Boolean isShow) {

    }

    protected void showToast(String txt) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.showShort(txt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDispatch.detachView();
        //        unbinder.unbind();
    }
}
