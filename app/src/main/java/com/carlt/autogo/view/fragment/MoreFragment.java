package com.carlt.autogo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.more.safety.SafetyActivity;
import com.carlt.autogo.view.activity.user.EditUserInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Description : 更多fragment
 *
 * @Author : zhanglei
 * @Date : 2018/9/10
 */
public class MoreFragment extends BaseMvpFragment {
    /**
     * 性别
     */
    @BindView(R.id.iv_more_sex)
    ImageView mIvSex;

    /**
     * 编辑资料
     */
    @BindView(R.id.tv_more_edit_profile)
    TextView mTvEditProfile;

    /**
     * 账户与安全
     */
    @BindView(R.id.ll_more_accounts_and_security)
    LinearLayout mLlAccountsAndSecurity;

    /**
     * 设置
     */
    @BindView(R.id.ll_more_setting)
    LinearLayout mLlSetting;

    /**
     * 头像
     */
    @BindView(R.id.iv_more_head_sculpture)
    ImageView ivMoreHeadSculpture;

    /**
     * 车主昵称
     */
    @BindView(R.id.tv_more_nickname)
    TextView tvMoreNickname;


    private UserInfo userInfo;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void init() {

    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = SharepUtil.getBeanFromSp("user");
        tvMoreNickname.setText(userInfo.realName +"");

        if(userInfo.gender ==1 ){
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sex_men));
        }else {
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sex_women));
        }

        Glide.with(this)
                .load(SharepUtil.getPreferences().getString("headurl",""))
                .into(mIvSex);
    }

    @OnClick({R.id.tv_more_edit_profile, R.id.ll_more_accounts_and_security, R.id.ll_more_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_more_edit_profile:
                Intent edit_profile =new Intent(mContext,EditUserInfoActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.ll_more_accounts_and_security:
                Intent intent = new Intent(getContext(), SafetyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_more_setting:
                break;
        }
    }
}
