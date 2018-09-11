package com.carlt.autogo.view.activity.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.UUDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 14:19  2018/9/11/011
 * @describe  修改昵称
 */
public class ChangeNickNameActivity extends BaseMvpActivity {

    @BindView(R.id.ed_nick_name)EditText edNickName;
    @BindView(R.id.btn_nick_commit)Button btnNickCommit;


    private static final String TAG="ChangeNickNameActivity";

    UUDialog dialog ;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_nick_name;
    }

    @Override
    public void init() {
        setTitleText("修改昵称");
        dialog = new UUDialog(this);
    }

    @OnClick(R.id.btn_nick_commit)
    public void onClick(){
        String nickName =edNickName.getText().toString().trim();
        if(TextUtils.isEmpty(nickName)){
            ToastUtils.showShort("昵称不能为空");
            return;
        }

    }


}
