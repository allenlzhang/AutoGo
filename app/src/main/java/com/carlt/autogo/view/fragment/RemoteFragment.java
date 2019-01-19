package com.carlt.autogo.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.RemoteAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.common.popup.AirConditionerPopup;
import com.carlt.autogo.common.popup.EnginePopup;
import com.carlt.autogo.common.popup.OtherPopup;
import com.carlt.autogo.common.popup.SkyWindowPopup;
import com.carlt.autogo.common.popup.WindowDoorPopup;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.RemoteInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 远程fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class RemoteFragment extends BaseMvpFragment {
    @BindView(R.id.tv_base_title)
    TextView       tvBaseTitle;
    @BindView(R.id.iv_base_back)
    ImageView      ivBaseBack;
    @BindView(R.id.tv_base_right)
    TextView       tvBaseRight;
    @BindView(R.id.rvListRemote)
    RecyclerView   rvListRemote;
    @BindView(R.id.remote_btn_lock)
    Button         remoteBtnLock;
    @BindView(R.id.remote_rl_lock)
    RelativeLayout remoteRlLock;

    int title[] = {R.mipmap.remote_engine_title, R.mipmap.remote_air_title, R.mipmap.remote_car_window_title,
            R.mipmap.remote_sky_window_title, R.mipmap.remote_other_title};
    int icon[]  = {R.mipmap.remote_engine_icon, R.mipmap.remote_air_icon, R.mipmap.remote_car_window_icon,
            R.mipmap.remote_sky_window_icon, R.mipmap.remote_other_icon};

    private SingletonCar        singletonCar;
    private EnginePopup         mEnginePopup;
    private AirConditionerPopup mConditionerPopup;
    private OtherPopup          mOtherPopup;
    private SkyWindowPopup      mSkyWindowPopup;
    private WindowDoorPopup     mWindowDoorPopup;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_remote;
    }

    @Override
    protected void init() {
        singletonCar = SingletonCar.getInstance();
        tvBaseTitle.setText("远程");
        ivBaseBack.setImageResource(R.drawable.ic_remote_car_state_bg);
        tvBaseRight.setBackground(getResources().getDrawable(R.drawable.ic_remote_log_bg));
        rvListRemote.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rvListRemote.setLayoutManager(linearLayoutManager);
        RemoteAdapter adapter = new RemoteAdapter(getData());
        rvListRemote.setAdapter(adapter);
        mEnginePopup = new EnginePopup(mActivity);
        //        mEnginePopup.setShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true));
        //        mEnginePopup.setDismissAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(false));

        mConditionerPopup = new AirConditionerPopup(mActivity);
        mOtherPopup = new OtherPopup(mActivity);
        mSkyWindowPopup = new SkyWindowPopup(mActivity);
        mWindowDoorPopup = new WindowDoorPopup(mActivity);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int width = view.getWidth();
                int height = view.getHeight();
                int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                mEnginePopup.setWidth(width - SizeUtils.dp2px(24));
                mSkyWindowPopup.setWidth(width - SizeUtils.dp2px(24));
                mWindowDoorPopup.setWidth(width - SizeUtils.dp2px(24));
                mOtherPopup.setWidth(width - SizeUtils.dp2px(24));
                mEnginePopup.setHeight(height);
                mSkyWindowPopup.setHeight(height);
                mOtherPopup.setHeight(height);
//                mWindowDoorPopup.setHeight(height);
                mConditionerPopup.setWidth(width - SizeUtils.dp2px(24));
                //                mEnginePopup.setHeight(SizeUtils.dp2px(135));

                switch (position) {
                    case 0:
                        if (firstCompletelyVisibleItemPosition <= position && position <= lastCompletelyVisibleItemPosition) {

                            mEnginePopup.showPopupWindow(view);
                        } else {
                            mEnginePopup.setPopupGravity(Gravity.CENTER);
                            mEnginePopup.showPopupWindow();
                        }
                        break;
                    case 1:
                        mConditionerPopup.setPopupGravity(Gravity.CENTER);
                        mConditionerPopup.showPopupWindow();
                        break;
                    case 2:
                        mWindowDoorPopup.showPopupWindow(view);
                        break;
                    case 3:
                        mSkyWindowPopup.showPopupWindow(view);
                        break;
                    case 4:
                        mOtherPopup.showPopupWindow(view);
                        break;
                    default:
                        break;
                }


            }
        });

    }

    private void showCommonDialog(String title, String rightTxt, final boolean isActivated) {
        CommonDialog.createDialogNotitle(mContext, title, "", "确定", rightTxt, true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent();
                if (isActivated) {
                    intent.setClass(mContext, CarDetailsActivity.class);
                } else {
                    intent.setClass(mContext, CarDetailsActivity.class);
                }
                intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE1);
                intent.putExtra("id", singletonCar.getCarBean().id);
                startActivity(intent);
            }
        });
    }

    private List<RemoteInfo> getData() {

        List<RemoteInfo> infos = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            RemoteInfo remoteInfo = new RemoteInfo();
            remoteInfo.id_img1 = title[i];
            remoteInfo.id_img2 = icon[i];
            infos.add(remoteInfo);
        }
        return infos;
    }

    @OnClick({R.id.iv_base_back, R.id.tv_base_right, R.id.remote_btn_lock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_base_back:
                if (isActivated()) {
                }
                break;
            case R.id.tv_base_right:
                if (isActivated()) {
                }
                break;
            case R.id.remote_btn_lock:
                certification();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isBound = singletonCar.isBound();
        if (isBound) {
            remoteRlLock.setVisibility(View.GONE);
        } else {
            remoteRlLock.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断是否认证
     */
    private void certification() {
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 2) {
            if (user.faceId == 0) {
                intent.setClass(mContext, FaceAuthSettingActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ALiPay_Auth);
            } else {
                intent.setClass(mContext, CarCertificationActivity.class);
            }
        } else if (user.identityAuth == 2) {
            if (user.faceId == 0) {
                intent.setClass(mContext, FaceAuthSettingActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ID_Card);
            } else {
                intent.setClass(mContext, CarCertificationActivity.class);
            }
        } else {
            intent.setClass(mContext, UserIdChooseActivity.class);
        }
        startActivity(intent);
    }

    /**
     * 是否激活设备
     * @return
     */
    private boolean isActivated() {
        AuthCarInfo.MyCarBean carInfo = singletonCar.getCarBean();
        if (carInfo != null) {
            int remoteStatus = carInfo.remoteStatus;
            if (remoteStatus == 0) {
                showCommonDialog("设备还未激活", "去激活", false);
                return false;
            } else if (remoteStatus == 1 || remoteStatus == 3) {
                showCommonDialog("设备正在激活中", "查看详情", true);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
