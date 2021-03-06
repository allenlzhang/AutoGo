package com.carlt.autogo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarPopupAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.car.ICarListView;
import com.carlt.autogo.presenter.car.MyCarListPresenter;
import com.carlt.autogo.presenter.fragment.HomePresenter;
import com.carlt.autogo.presenter.fragment.IHomeView;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.MainActivity;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.carlt.autogo.view.activity.home.NavigationSynchronizeToCarActivity;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.carlt.autogo.widget.MaxListView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description: 首页fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
@CreatePresenter(presenter = {HomePresenter.class, MyCarListPresenter.class})
public class HomeFragment extends BaseMvpFragment implements IHomeView, ICarListView {

    @BindView(R.id.tvCarType)
    TextView       tvCarType;
    @BindView(R.id.rl_home_chose_car_type)
    RelativeLayout rlHomeChoseCarType;
    @BindView(R.id.tvDrivingTime)
    TextView       tvDrivingTime;
    @BindView(R.id.tvDrivingMileage)
    TextView       tvDrivingMileage;
    @BindView(R.id.tvAverageSpeed)
    TextView       tvAverageSpeed;
    @BindView(R.id.tvAverageOil)
    TextView       tvAverageOil;
    @BindView(R.id.rlCarLocation)
    RelativeLayout rlCarLocation;
    @BindView(R.id.home_btn_lock)
    Button         homeBtnLock;
    @BindView(R.id.home_rl_lock)
    RelativeLayout homeRlLock;
    @BindView(R.id.rlCarState)
    RelativeLayout rlCarState;
    @BindView(R.id.rlTirePressure)
    RelativeLayout rlTirePressure;
    @BindView(R.id.rlCarLog)
    RelativeLayout rlCarLog;
    @BindView(R.id.rlNav)
    RelativeLayout rlNav;
    @BindView(R.id.ivCarChose)
    ImageView      ivCarChose;
    @BindView(R.id.rlTesting)
    RelativeLayout rlTesting;

    private CarPopupAdapter adapter;
    private PopupWindow     popupWindow;

    private SingletonCar singletonCar;

    @PresenterVariable
    HomePresenter      homePresenter;
    @PresenterVariable
    MyCarListPresenter carListPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        singletonCar = SingletonCar.getInstance();

    }

    /**
     * 是否绑定车辆
     * @param info
     */
    private void isBindCar(AuthCarInfo info) {
        if (info != null) {
            if (info.myCar != null && info.myCar.size() > 0) {
                singletonCar.setBound(true);
            } else if (info.authCar != null && info.authCar.size() > 0) {
                singletonCar.setBound(true);
            } else {
                singletonCar.setBound(false);
            }
        } else {
            singletonCar.setBound(false);
        }
    }

    @SuppressLint("CheckResult")
    private void ClientGetData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 3);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        carListPresenter.getCarList(map);
    }

    private void parseGetMyList(AuthCarInfo info) {
        if (info.err != null) {
            ToastUtils.showShort(info.err.msg);
        } else {
            adapter = new CarPopupAdapter(info, getContext());
            adapter.setClick(new CarPopupAdapter.OnItemClick() {
                @Override
                public void itemClick(int tag, AuthCarInfo.MyCarBean dataBean) {
                    popupWindow.dismiss();
                    adapter.setSelected_position(tag);
                    singletonCar.setCarTag(tag);
                    tvCarType.setText(dataBean.carName);
                    singletonCar.setCarBean(dataBean);
                }
            });
            int tag = singletonCar.getCarTag();
            if (tag != 0) {
                adapter.setSelected_position(tag);
            } else {
                if (info.myCar != null && info.myCar.size() > 0) {
                    tvCarType.setText(info.myCar.get(0).carName);
                    singletonCar.setCarBean(info.myCar.get(0));
                } else if (info.authCar != null && info.authCar.size() > 0) {
                    tvCarType.setText(info.authCar.get(0).carName);
                    singletonCar.setCarBean(info.authCar.get(0));
                } else {
                    tvCarType.setText("品牌车型");
                }
            }
            isBindCar(info);
            boolean isBound = singletonCar.isBound();
            if (isBound) {
                homeRlLock.setVisibility(View.GONE);
            } else {
                homeRlLock.setVisibility(View.VISIBLE);
            }
            if (adapter.getCount() > 1) {
                ivCarChose.setVisibility(View.VISIBLE);
            } else {
                ivCarChose.setVisibility(View.GONE);
            }
            tvCarType.setSelected(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (singletonCar.getCarBean() == null) {
            adapter = new CarPopupAdapter(null, mContext);
            tvCarType.setText("品牌车型");
        }
        ClientGetData();
    }

    private void showPopupWindow(View view) {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_car_popupwindow, null, false);
        MaxListView mListView = contentView.findViewById(R.id.list_popup);
        mListView.setAdapter(adapter);
        mListView.setListViewHeight(getPopupWindowHeight(view));
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        popupWindow = new PopupWindow(contentView,
                view.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);
        ivCarChose.setImageResource(R.mipmap.car_list_close);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivCarChose.setImageResource(R.mipmap.car_list_open);
            }
        });
    }

    /**
     * 根据屏幕适配PopupWindow显示高度
     * @param view
     * @return
     */
    private int getPopupWindowHeight(View view) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int windowHeight = dm.heightPixels;
        int bottomLayoutHeight = ((MainActivity) getActivity()).bottomTabs.getHeight();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int viewBottom = location[1] + view.getHeight();
        return windowHeight - bottomLayoutHeight - viewBottom - 50;
    }

    @OnClick({R.id.rl_home_chose_car_type, R.id.rlCarLocation, R.id.home_btn_lock, R.id.rlCarState,
            R.id.rlTirePressure, R.id.rlCarLog, R.id.rlNav, R.id.rlTesting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_home_chose_car_type:
                if (adapter.getCount() > 1) {
                    showPopupWindow(view);
                }
                break;
            case R.id.rlCarLocation:
                if (isActivated()) {

                }
                //                startActivity(new Intent(mContext, CarLocationActivity.class));
                break;
            case R.id.home_btn_lock:
                certification();
                break;
            case R.id.rlCarState:
                if (isActivated()) {

                }
                break;
            case R.id.rlTirePressure:
                if (isActivated()) {

                }
                //                startActivity(new Intent(mContext, CarTiresStateActivity.class));
                break;
            case R.id.rlCarLog:
                if (isActivated()) {

                }
                break;
            case R.id.rlNav:
                if (isActivated()) {

                }
                go2FindCarActivity();
                break;
            case R.id.rlTesting:
                if (isActivated()) {

                }
                //                startActivity(new Intent(mContext, CarTestingActivity.class));


                break;
        }
    }

    private void go2FindCarActivity() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.e(data.toString());
                        showToast("未获取到权限，导航同步功能不可用");

                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.e(data.toString());
                        startActivity(new Intent(mActivity, NavigationSynchronizeToCarActivity.class));
                    }
                }).start();
    }


    private boolean isActivated() {
        // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
        AuthCarInfo.MyCarBean dataBean = singletonCar.getCarBean();
        if (dataBean != null) {
            int remoteStatus = dataBean.remoteStatus;
            if (remoteStatus == 0) {
                showCommonDialog("设备还未激活", "去激活", false);
                return false;
            } else if (remoteStatus == 1 || remoteStatus == 3) {
                showCommonDialog("设备正在激活", "查看详情", true);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断身份认证
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

    @Override
    public void getCarListSuccess(AuthCarInfo carInfo) {
        parseGetMyList(carInfo);
    }

}
