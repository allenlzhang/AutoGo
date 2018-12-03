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
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/11/27.
 * 车辆详情
 */
public class CarDetailsActivity extends BaseMvpActivity {
    @BindView(R.id.tvDetailsModel) TextView tvDetailsModel;//车型

    @BindView(R.id.llDetails) LinearLayout llDetails;//详情模块
    @BindView(R.id.tvDetailsBuyTime) TextView tvDetailsBuyTime;//购车时间
    @BindView(R.id.tvDetailsServiceCycle) TextView tvDetailsServiceCycle;//上次保养里程
    @BindView(R.id.tvDetailsServiceTime) TextView tvDetailsServiceTime;//上次保养时间
    @BindView(R.id.tvDetailsInsureTime) TextView tvDetailsInsureTime;//上次投保时间
    @BindView(R.id.tvDetailsAnnualTime) TextView tvDetailsAnnualTime;//上次年检时间
    @BindView(R.id.tvDetailsAuthStartTime) TextView tvDetailsAuthStartTime;//授权开始时间
    @BindView(R.id.llDetailsAuthStart) LinearLayout llDetailsAuthStart;
    @BindView(R.id.tvDetailsAuthEndTime) TextView tvDetailsAuthEndTime;//授权结束时间
    @BindView(R.id.llDetailsAuthEnd) LinearLayout llDetailsAuthEnd;

    @BindView(R.id.llDetailsDevice) LinearLayout llDetailsDevice;//设备模块
    @BindView(R.id.llDetailsRemote) LinearLayout llDetailsRemote;//远程
    @BindView(R.id.ivDetailsRemoteState) ImageView ivDetailsRemoteState;//远程激活icon
    @BindView(R.id.tvDetailsRemoteState) TextView tvDetailsRemoteState;//远程激活状态
    @BindView(R.id.llDetailsRecorder) LinearLayout llDetailsRecorder;//记录仪
    @BindView(R.id.ivDetailsRecorderState) ImageView ivDetailsRecorderState;//记录仪icon
    @BindView(R.id.tvDetailsRecorderState) TextView tvDetailsRecorderState;//记录仪状态
    @BindView(R.id.llDetailsMachine) LinearLayout llDetailsMachine;//车机
    @BindView(R.id.ivDetailsMachineState) ImageView ivDetailsMachineState;//车机icon
    @BindView(R.id.tvDetailsMachineState) TextView tvDetailsMachineState;//车机状态
    @BindView(R.id.tvDetailsRemote) TextView tvDetailsRemote;//远程txt
    @BindView(R.id.tvDetailsRecorder) TextView tvDetailsRecorder;//记录仪txt
    @BindView(R.id.tvDetailsMachine) TextView tvDetailsMachine;//车机txt

    @BindView(R.id.btnCancelAuth) Button btnCancelAuth;//取消授权

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
        setTitleText("车辆详情");
        initView();
    }

    private void initView() {
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

    @OnClick({R.id.llDetailsRemote, R.id.llDetailsRecorder, R.id.llDetailsMachine, R.id.btnCancelAuth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llDetailsRemote:
                clickRemote();
                break;
            case R.id.llDetailsRecorder:
                break;
            case R.id.llDetailsMachine:
                break;
            case R.id.btnCancelAuth:
                break;
        }
    }

    private void clickRemote(){
        if (type == DETAILS_TYPE1){
            if (isRemoteActivating){
                startActivity(DeviceActivateEditActivity.class,false);
            }else {
                startActivity(DeviceActivateActivity.class,false);
            }
        }else {}
    }
}
