package com.carlt.autogo.view.activity.user.accept;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.view.activity.more.safety.SafetyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 14:25  2018/9/13/013
 * @describe 认证完成
 */
public class IdfCompleteActivity extends BaseMvpActivity {


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv)
    TextView tv;
    private boolean isByIdcard = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_idf_complete;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
        isByIdcard = getIntent().getBooleanExtra("idcard", false);

        if(isByIdcard){
            img.setImageDrawable(getResources().getDrawable(R.mipmap.accepted_by_idcard));
        }else {
            img.setImageDrawable(getResources().getDrawable(R.mipmap.accepted_by_payment));
        }
        tvUserName.setText(getIntent().getStringExtra("name"));
    }



    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        Intent intent =new Intent(this, SafetyActivity.class);
        startActivity(intent);
        finish();
    }
}