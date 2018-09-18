package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogChangeSex;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    UUDialog dialog ;
    @Override
    protected int getContentView() {


        return R.layout.activity_edit_user_info;
    }

    @Override
    public void init() {

        setTitleText("修改资料");
        dialogChangeSex = new DialogChangeSex(this);
        dialog =new UUDialog(this) ;

        Glide.with(this).load(SharepUtil.getPreferences().getString("headurl",""))
                .into(imgEdHeader);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        UserInfo info = SharepUtil.<UserInfo>getBeanFromSp("user");
        tvUserCarName.setText("" +  info.realName);
        tvSex.setText("" + info.sex);
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
                @SuppressLint("CheckResult")
                @Override
                public void getText( final String text , final int  sex) {
                    dialog.show();
                    commitEdUerSex(text, sex);

                }
            });
        }
    }

    @SuppressLint("CheckResult")
    private void commitEdUerSex(final String text, int sex) {
        Map<String,Object> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token",""));
        params.put("gender",sex );

        ClientFactory.def(UserService.class).userEditInfi(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if(baseError.msg != null){
                            ToastUtils.showLong(baseError.msg);
                        }else {
                            ToastUtils.showLong("编辑成功");
                            UserInfo userInfo =   SharepUtil.<UserInfo>getBeanFromSp("user");
                            userInfo.sex = text ;
                            SharepUtil.putByBean("user",userInfo);
                            tvSex.setText(text);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLong("编辑失败");
                        dialog.dismiss();
                    }
                });
    }


    @OnClick(R.id.img_edheader)
    public void edHeader(){

        Intent userAvatar = new Intent(this,PersonAvatarActivity.class);

        startActivity(userAvatar);
    }
}
