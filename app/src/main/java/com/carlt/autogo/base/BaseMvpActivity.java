package com.carlt.autogo.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.carlt.autogo.view.activity.user.PersonAvatarActivity;

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
    private int requestCodePermsiision = 1020;
    public PremissoinLisniter lisniter ;

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
    public void requestPermissions(int requestCode, String permission) {
        if (permission != null && permission.length() > 0) {
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    // 检查是否有权限
                    int hasPer = checkSelfPermission(permission);
                    if (hasPer != PackageManager.PERMISSION_GRANTED) {
                        // 是否应该显示权限请求
                        boolean isShould = shouldShowRequestPermissionRationale(permission);
                        requestPermissions(new String[]{permission}, requestCode);
                    }
                } else {

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /** 6.0 检查权限检查
     * @param mPermissions   需要申请的检查项
     * @param lisniter  回调接口,只有成功 才回调
     * @return ture  验证通过直接返回  , false 未授权,请求授权
     *
     */

    public void checkPermissions(String[] mPermissions , PremissoinLisniter lisniter) {
        this.lisniter = lisniter ;
        boolean created = true ;

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            lisniter.createred();
            return;
        }

        for(int  i = 0 ;i < mPermissions.length ; i++ ){
            if(ActivityCompat.checkSelfPermission(BaseMvpActivity.this, mPermissions[i])  == PackageManager.PERMISSION_DENIED){
                created =false ;
            }
        }
        if(created && lisniter!= null) {
            lisniter.createred();
        }else {
            ActivityCompat.requestPermissions(BaseMvpActivity.this, mPermissions, requestCodePermsiision);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == requestCodePermsiision) {
            boolean created = true ;
            for(int i= 0 ; i < grantResults.length ; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    created = false ;
                    ToastUtils.showShort("部分权限获取失败，正常功可能受到影响");
                }
            }
            if(created && lisniter!= null){
                lisniter.createred();
            }
        }
    }


    /**
     * 授权监听
     * createred 为授权成功 回调
     */
    public interface PremissoinLisniter {
      void createred();

    }
}
