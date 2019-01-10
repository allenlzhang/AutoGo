package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.DialogChangeSex;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.user.EditUserInfoPresenter;
import com.carlt.autogo.presenter.user.IEditUserInfoView;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.gildutils.GlideCircleTransform;

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
@CreatePresenter(presenter = EditUserInfoPresenter.class)
public class EditUserInfoActivity extends BaseMvpActivity<EditUserInfoPresenter> implements IEditUserInfoView{

    @BindView(R.id.img_edheader)
    ImageView imgEdHeader;
    @BindView(R.id.tv_user_nick_name)
    TextView tvUserNickName;
    @BindView(R.id.rl_user_nick_name)
    RelativeLayout rlUserNickName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.rl_ed_head1)
    RelativeLayout rlEdHead1;
    DialogChangeSex dialogChangeSex;



    private UserInfo userInfo;

    @Override
    protected int getContentView() {

        return R.layout.activity_edit_user_info;
    }

    @Override
    public void init() {

        setTitleText("修改资料");
        dialogChangeSex = new DialogChangeSex(this);


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        userInfo = SharepUtil.<UserInfo>getBeanFromSp("user");
        tvUserNickName.setText("" + userInfo.realName);
        tvSex.setText("" + userInfo.sex);


        Glide.with(this)
                .load(userInfo.avatarFile)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .error(R.mipmap.iv_def_head)
                .transform(new GlideCircleTransform(this))
                .into(imgEdHeader);
    }

    @OnClick({R.id.rl_user_nick_name, R.id.rl_sex})
    public void function(View view) {
        if (view.getId() == R.id.rl_user_nick_name) {
            // 修改昵称
            Intent intentNickName = new Intent(this, ChangeNickNameActivity.class);
            startActivity(intentNickName);

        } else {
            // 修改性别
            dialogChangeSex.show();
            dialogChangeSex.setListner(new DialogChangeSex.ItemOnclickListner() {
                @SuppressLint("CheckResult")
                @Override
                public void getText(final String text, final int sex) {
                    getPresenter().commitEdUerSex(text, sex);

                }
            });
        }
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.rl_ed_head1)
    public void edHeader() {

        startActivity(PersonAvatarActivity.class, false);
    }

    @Override
    public void userEditInfoSuccess(BaseError baseError,String text,int sex) {
        if (baseError.msg != null) {
            ToastUtils.showLong(baseError.msg);
        } else {
            ToastUtils.showLong("编辑成功");
            UserInfo userInfo = SharepUtil.<UserInfo>getBeanFromSp("user");
            userInfo.sex = text;
            userInfo.gender = sex;
            SharepUtil.putByBean("user", userInfo);
            tvSex.setText(text);
        }
    }
}
