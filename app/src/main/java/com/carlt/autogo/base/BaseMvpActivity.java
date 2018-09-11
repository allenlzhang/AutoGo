package com.carlt.autogo.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.basemvp.BaseMvpView;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.basemvp.PresenterDispatch;
import com.carlt.autogo.basemvp.PresenterProviders;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseMvpView {

    @BindView(R.id.tv_base_title)
    TextView  tvBaseTitle;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.rlTitle)
    protected RelativeLayout rlTitle;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.tv_base_right)TextView tvBaseRight;
    private PresenterProviders mPresenterProviders;
    private PresenterDispatch  mPresenterDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_base);
        initBaseViews();
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);
        ButterKnife.bind(this);
        mPresenterDispatch.attachView(this, this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        init();
    }

    private void initBaseViews() {
        flContent = findViewById(R.id.flContent);
        tvBaseTitle = findViewById(R.id.tv_base_title);
        rlTitle = findViewById(R.id.rlTitle);
        ivBaseBack = findViewById(R.id.iv_base_back);
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        flContent.addView(getLayoutInflater().inflate(getContentView(), null));
    }

    protected void setTitleText(String text) {
        tvBaseTitle.setText(text);
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


    public void setBaseBackStyle(Drawable drawable){
        ivBaseBack.setClickable(true);
        ivBaseBack.setImageDrawable(drawable);
    }

    //设置Header背景颜色,字体颜色
    public void setHeadColor (int pColor, int tettleColor ,int righthColor ,int leftColor  ){

        if(pColor != 0){
            rlTitle.setBackgroundColor(pColor);
        }
        if(tettleColor != 0){
            tvBaseTitle.setTextColor(tettleColor);
        }

        if(righthColor != 0){
            tvBaseRight.setTextColor(righthColor);
        }

        if(leftColor != 0){
            ivBaseBack.setBackgroundColor(leftColor);
        }

    }

    //展示头部右边样式
    public void showHeaderRight(String text){

        tvBaseRight.setText(text);
    }

}
