package com.carlt.autogo.view.activity.user.accept;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 17:42  2018/9/11/011
 * @describe 身份认证 界面
 */
public class IdCardAcceptActivity extends BaseMvpActivity {

    @BindView(R.id.rl_rule1) RelativeLayout rlRule1;
    @BindView(R.id.rl_rule2) RelativeLayout rlRule2;
    @BindView(R.id.ed_id_accept_name) EditText edIdAcceptName;
    @BindView(R.id.ed_idcard_accept_num) EditText edIdcardAcceptNum;
    @BindView(R.id.btn_idcard_accepte) Button btnIdcardAccepte;


    @Override
    protected int getContentView() {
        return R.layout.activity_id_card_accept;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
    }

    @OnClick(R.id.btn_idcard_accepte)
    public void onClick(){
        String name = edIdAcceptName.getText().toString().trim();
        String  idCardNum = edIdcardAcceptNum.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort("姓名为空");
            return;
        }

        if(!RegexUtils.isIDCard18(idCardNum) || RegexUtils.isIDCard15(idCardNum)  ){
            ToastUtils.showShort("身份证号码不正确");
            return;
        }


    }
}
