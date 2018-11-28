package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Marlon on 2018/11/27.
 */
public class CarDetailsActivity extends BaseMvpActivity {
    @BindView(R.id.tvDetailsModel)
    TextView tvDetailsModel;
    @BindView(R.id.tvDetailsBuyTime)
    TextView tvDetailsBuyTime;
    @BindView(R.id.tvDetailsServiceCycle)
    TextView tvDetailsServiceCycle;
    @BindView(R.id.tvDetailsServiceTime)
    TextView tvDetailsServiceTime;
    @BindView(R.id.tvDetailsInsureTime)
    TextView tvDetailsInsureTime;
    @BindView(R.id.tvDetailsAnnualTime)
    TextView tvDetailsAnnualTime;
    @BindView(R.id.tvDetailsAuthStartTime)
    TextView tvDetailsAuthStartTime;
    @BindView(R.id.llDetailsAuthStart)
    LinearLayout llDetailsAuthStart;
    @BindView(R.id.tvDetailsAuthEndTime)
    TextView tvDetailsAuthEndTime;
    @BindView(R.id.llDetailsAuthEnd)
    LinearLayout llDetailsAuthEnd;
    @BindView(R.id.llDetails)
    LinearLayout llDetails;
    @BindView(R.id.ivDetailsRemoteState)
    ImageView ivDetailsRemoteState;
    @BindView(R.id.tvDetailsRemoteState)
    TextView tvDetailsRemoteState;
    @BindView(R.id.ivDetailsRecorderState)
    ImageView ivDetailsRecorderState;
    @BindView(R.id.tvDetailsRecorderState)
    TextView tvDetailsRecorderState;
    @BindView(R.id.ivDetailsMachineState)
    ImageView ivDetailsMachineState;
    @BindView(R.id.tvDetailsMachineState)
    TextView tvDetailsMachineState;
    @BindView(R.id.llDetailsDevice)
    LinearLayout llDetailsDevice;
    @BindView(R.id.btnCancelAuth)
    Button btnCancelAuth;
    @BindView(R.id.tvDetailsRemote)
    TextView tvDetailsRemote;
    @BindView(R.id.tvDetailsRecorder)
    TextView tvDetailsRecorder;
    @BindView(R.id.tvDetailsMachine)
    TextView tvDetailsMachine;

    private int type = DETAILS_TYPE1;
    private boolean isRemoteActivating = false;  //远程是否激活中
    public static final int DETAILS_TYPE1 = 0;  //未激活未授权（我的爱车详情）
    public static final int DETAILS_TYPE2 = 1;  //已激活未授权（我的爱车详情）
    public static final int DETAILS_TYPE3 = 2;  //已激活授权中（我的爱车详情）
    public static final int DETAILS_TYPE4 = 3;  //（被授权车辆详情）

    @Override
    protected int getContentView() {
        return R.layout.activity_car_details;
    }

    @Override
    public void init() {
        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        type = intent.getIntExtra("type", DETAILS_TYPE1);
        isRemoteActivating = intent.getBooleanExtra("remoteActivating", false);
        switch (type) {
            case DETAILS_TYPE1:
                llDetails.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                if (isRemoteActivating) {
                    ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activating_big);
                    tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorActivating));
                    tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorActivating));
                } else {
                    ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activate_big);
                    tvDetailsRemote.setTextColor(getResources().getColor(R.color.textColorGray));
                    tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.textColorGray));
                }
                break;
            case DETAILS_TYPE2:
                llDetailsAuthStart.setVisibility(View.GONE);
                llDetailsAuthEnd.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
                break;
            case DETAILS_TYPE3:
                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
                break;
            case DETAILS_TYPE4:
                llDetailsDevice.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                break;
        }
    }
}
