package com.carlt.autogo.view.activity.user.accept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 17:07  2018/9/11/011
 * @describe 用户身份认证选择 界面
 */
public class UserIdChooseActivity extends BaseMvpActivity {

    @BindView(R.id.rl_payment_accept)RelativeLayout rlPaymentAccept;
    @BindView(R.id.rl_idcard_accept)RelativeLayout rlIdcardAccept;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_id_choose;
    }

    @Override
    public void init() {
        setTitleText("选择身份认证方式");
    }

    @OnClick({R.id.rl_payment_accept, R.id.rl_idcard_accept})
    public void onClick(View view){
        if(view.getId() == R.id.rl_payment_accept){

        }else {
            Intent intentId =new Intent(this, IdCardAcceptActivity.class);
            startActivity(intentId);
        }

    }
}
