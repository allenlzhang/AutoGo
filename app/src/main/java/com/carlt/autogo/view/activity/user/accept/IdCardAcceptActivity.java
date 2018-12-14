package com.carlt.autogo.view.activity.user.accept;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 */
public class IdCardAcceptActivity extends BaseMvpActivity {

    @BindView(R.id.rl_rule1)
    RelativeLayout rlRule1;
    @BindView(R.id.rl_rule2)
    RelativeLayout rlRule2;
    @BindView(R.id.ed_id_accept_name)
    EditText       edIdAcceptName;
    @BindView(R.id.ed_idcard_accept_num)
    EditText       edIdcardAcceptNum;
    @BindView(R.id.btn_idcard_accepte)
    Button         btnIdcardAccepte;
    @BindView(R.id.tv_user_name)
    TextView       tvUserName;
    @BindView(R.id.btn_accept_face)
    Button         btnAcceptFace;

    String name; // 姓名
    String idCardNum;  //身份证号
    String hideName;
    String hideIdNume;

    @Override
    protected int getContentView() {
        return R.layout.activity_id_card_accept;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_idcard_accepte)
    public void onClick() {
        name = edIdAcceptName.getText().toString().trim();
        idCardNum = edIdcardAcceptNum.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("姓名为空");
            return;
        }

        if (!RegexUtils.isIDCard18(idCardNum) || RegexUtils.isIDCard15(idCardNum)) {
            ToastUtils.showShort("身份证号码不正确");
            return;
        }
        hideName = encrypt(name);
        hideIdNume = encrypt(idCardNum);
        Intent intent = new Intent(this, UploadIdCardPhotoActivity2.class);
        //        intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_ID_CARDACCEPT_ACTIVITY);
        intent.putExtra("name", hideName);
        intent.putExtra("idcard", hideIdNume);
        intent.putExtra("idNum", idCardNum);
        intent.putExtra("realName", name);
        startActivity(intent);
        finish();


    }

    @SuppressLint("CheckResult")
    private void beginingAccept() {

        rlRule1.setVisibility(View.GONE);
        rlRule2.setVisibility(View.VISIBLE);

        hideName = encrypt(name);
        hideIdNume = encrypt(idCardNum);

        tvUserName.setText(hideName + "\t" + hideIdNume);


    }


    private String encrypt(String str) {
        StringBuilder n = new StringBuilder();
        int len = str.length();
        if (len <= 2) {
            n.append(str.charAt(0) + "*");
            return n.toString();
        }
        n.append(str.charAt(0));
        for (int i = 1; i <= len - 2; i++) {
            n.append("*");
        }
        n.append(str.charAt(len - 1));
        return n.toString();
    }


}
