package com.carlt.autogo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.adapter.RemoteAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarListInfo;
import com.carlt.autogo.entry.car.RemoteInfo;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.more.safety.FaceRecognitionSettingFirstActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description: 远程fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class RemoteFragment extends BaseMvpFragment {
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_right)
    TextView tvBaseRight;
    @BindView(R.id.rvListRemote)
    RecyclerView rvListRemote;
    @BindView(R.id.remote_btn_lock)
    Button remoteBtnLock;
    @BindView(R.id.remote_rl_lock)
    RelativeLayout remoteRlLock;

    CarListInfo.DataBean carInfo;
    int title[] = {R.mipmap.remote_engine_title, R.mipmap.remote_air_title, R.mipmap.remote_car_window_title,
            R.mipmap.remote_sky_window_title, R.mipmap.remote_other_title};
    int icon[] = {R.mipmap.remote_engine_icon, R.mipmap.remote_air_icon, R.mipmap.remote_car_window_icon,
            R.mipmap.remote_sky_window_icon, R.mipmap.remote_other_icon};
    @Override
    public int getLayoutId() {
        return R.layout.fragment_remote;
    }

    @Override
    protected void init() {
        tvBaseTitle.setText("远程");
        ivBaseBack.setImageResource(R.drawable.ic_remote_car_state_bg);
        tvBaseRight.setBackground(getResources().getDrawable(R.drawable.ic_remote_log_bg));
        rvListRemote.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListRemote.setLayoutManager(linearLayoutManager);
        RemoteAdapter adapter = new RemoteAdapter(getData());
        rvListRemote.setAdapter(adapter);
        carInfo = SharepUtil.getBeanFromSp("carInfo");
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int remoteStatus = carInfo.getRemoteStatus();
                if (remoteStatus == 1){
                    showCommonDialog("设备还未激活");
                }else if (remoteStatus == 2){
                    showCommonDialog("设备正在激活中");
                }
            }
        });
    }

    private void showCommonDialog(String title){
        CommonDialog.createDialogNotitle(mContext, title, "", "确定", "查看详情", true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private List<RemoteInfo> getData() {

        List<RemoteInfo> infos = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            RemoteInfo remoteInfo = new RemoteInfo();
            remoteInfo.setId_img1(title[i]);
            remoteInfo.setId_img2(icon[i]);
            infos.add(remoteInfo);
        }
        return infos;
    }

    @OnClick({R.id.iv_base_back, R.id.tv_base_right,R.id.remote_btn_lock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_base_back:
                break;
            case R.id.tv_base_right:
                break;
            case R.id.remote_btn_lock:
                certification();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharepUtil.getPreferences().getBoolean("isBindCar", false)) {
            remoteRlLock.setVisibility(View.GONE);
        } else {
            remoteRlLock.setVisibility(View.VISIBLE);
        }
    }

    private void certification(){
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 1){
            intent.setClass(mContext,UserIdChooseActivity.class);
        }else if (user.faceId == 0){
            intent.setClass(mContext,FaceRecognitionSettingFirstActivity.class);
        }else {
            intent.setClass(mContext, CarCertificationActivity.class);
        }
        startActivity(intent);
    }

}
