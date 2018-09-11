package com.carlt.autogo.view.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogChangeSex;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 16:49  2018/9/11/011
 * @describe 修改资料
 */
public class EditUserInfoActivity extends BaseMvpActivity{

    @BindView(R.id.img_edheader) ImageView imgEdHeader;
    @BindView(R.id.tv_user_car_name) TextView tvUserCarName;
    @BindView(R.id.img_back1) ImageView imgBack1;
    @BindView(R.id.tv_sex) TextView tvSex;
    @BindView(R.id.img_back2) ImageView imgBack2;
    DialogChangeSex dialogChangeSex ;
    @Override
    protected int getContentView() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    public void init() {

        setTitleText("修改资料");
        dialogChangeSex = new DialogChangeSex(this);
    }

    @OnClick({R.id.img_back1 ,R.id.img_back2})
    public void function(View view){
        if(view.getId() == R.id.img_back1){
            // 修改昵称
            Intent intentNickName = new Intent(this,ChangeNickNameActivity.class);
            startActivity(intentNickName);

        }else {
            // 修改性别
            dialogChangeSex.show();
            dialogChangeSex.setListner(new DialogChangeSex.ItemOnclickListner() {
                @Override
                public void getText(String text) {
                    tvSex.setText(text);
                }
            });

        }

    }


    @OnClick(R.id.img_edheader)
    public void edHeader(){

        Intent userAvatar = new Intent(this,PersonAvatarActivity.class);

        startActivity(userAvatar);
    }
}
