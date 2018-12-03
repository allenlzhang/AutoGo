package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarPopupAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.DeviceActivateActivity;
import com.carlt.autogo.view.activity.more.safety.FaceRecognitionSettingFirstActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: 首页fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class HomeFragment extends BaseMvpFragment {

    @BindView(R.id.tvCarType)
    TextView tvCarType;
    @BindView(R.id.rl_home_chose_car_type)
    RelativeLayout rlHomeChoseCarType;
    @BindView(R.id.tvDrivingTime)
    TextView tvDrivingTime;
    @BindView(R.id.tvDrivingMileage)
    TextView tvDrivingMileage;
    @BindView(R.id.tvAverageSpeed)
    TextView tvAverageSpeed;
    @BindView(R.id.tvAverageOil)
    TextView tvAverageOil;
    @BindView(R.id.rlCarLocation)
    RelativeLayout rlCarLocation;
    @BindView(R.id.home_btn_lock)
    Button homeBtnLock;
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

    private CarPopupAdapter adapter;
    private PopupWindow popupWindow;
    private AuthCarInfo carListInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {


    }

//    private AuthCarInfo getData() {
//        String json = "{\"myCar\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵\n" +
//                "型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0}]}";
//        String json = "{\"myCar\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":3,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":2,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0}],\"authCar\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
//        String json2 = "\n" +
//                "{\"myCar\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":3,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":2,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0}]}";
//
//        Gson gson = new Gson();
//        carListInfo = gson.fromJson(json, AuthCarInfo.class);
//        return carListInfo;
//    }

    private void isBindCar(AuthCarInfo info){
        if (info != null) {
            if (info.myCar!= null&&info.myCar.size() > 0 ){
                SharepUtil.putBoolean("isBindCar", true);
            }else if (info.authCar != null && info.authCar.size() > 0) {
                SharepUtil.putBoolean("isBindCar", true);
            } else {
                SharepUtil.putBoolean("isBindCar", false);
            }
        } else {
            SharepUtil.putBoolean("isBindCar", false);
        }
    }

    @SuppressLint("CheckResult")
    private void ClientGetData(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",3);
        ClientFactory.def(CarService.class).getMyCarList(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo info) throws Exception {
                        if (info.err!=null){
                            ToastUtils.showShort(info.err.msg);
                        }else {
                            adapter = new CarPopupAdapter(info, getContext());
                            adapter.setClick(new CarPopupAdapter.OnItemClick() {
                                @Override
                                public void itemClick(int i, AuthCarInfo.MyCarBean dataBean) {
                                    popupWindow.dismiss();
                                    adapter.setSelected_position(i);
                                    tvCarType.setText(dataBean.carName);
                                    SharepUtil.putByBean("carInfo", dataBean);
                                }
                            });
                            isBindCar(info);
                            if (SharepUtil.getPreferences().getBoolean("isBindCar", false)) {
                                homeRlLock.setVisibility(View.GONE);
                            } else {
                                homeRlLock.setVisibility(View.VISIBLE);
                            }
                            if (info.myCar!=null&&info.myCar.size()>0){
                                tvCarType.setText(info.myCar.get(0).carName);
                                SharepUtil.putByBean("carInfo", info.myCar.get(0));
                            }else if (info.authCar!=null&&info.authCar.size()>0){
                                tvCarType.setText(info.authCar.get(0).carName);
                                SharepUtil.putByBean("carInfo", info.authCar.get(0));
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ClientGetData();
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_car_popupwindow, null, false);
        ListView mListView = contentView.findViewById(R.id.list_popup);
        mListView.setAdapter(adapter);

        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        popupWindow = new PopupWindow(contentView,
                view.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view);
    }

    @OnClick({R.id.rl_home_chose_car_type, R.id.rlCarLocation, R.id.home_btn_lock,R.id.rlCarState,
    R.id.rlTirePressure,R.id.rlCarLog,R.id.rlNav})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_home_chose_car_type:
                showPopupWindow(view);


                break;
            case R.id.rlCarLocation:
                if (isActivated()){

                }
                break;
            case R.id.home_btn_lock:
                certification();
                break;
            case R.id.rlCarState:
                if (isActivated()){

                }
                break;
            case R.id.rlTirePressure:
                if (isActivated()){

                }
                break;
            case R.id.rlCarLog:
                if (isActivated()){

                }
                break;
            case R.id.rlNav:
                if (isActivated()){

                }
                break;
        }
    }

    private boolean isActivated(){
        // 远程激活状态,设备激活状态 0-未激活  1-正在激活  2-激活成功  3-激活失败
        AuthCarInfo.MyCarBean dataBean = SharepUtil.getBeanFromSp("carInfo");
        if (dataBean!=null) {
            int remoteStatus = dataBean.remoteStatus;
            if (remoteStatus == 0) {
                showCommonDialog("设备还未激活","去激活",false);
                return false;
            } else if (remoteStatus == 1) {
                showCommonDialog("设备正在激活","查看详情",true);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void certification() {
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 1) {
            intent.setClass(mContext, UserIdChooseActivity.class);
        } else if (user.faceId == 0) {
            intent.setClass(mContext, FaceRecognitionSettingFirstActivity.class);
        } else {
            intent.setClass(mContext, CarCertificationActivity.class);
        }
        startActivity(intent);
    }

    private void showCommonDialog(String title,String rightTxt,final boolean isActivated) {
        CommonDialog.createDialogNotitle(mContext, title, "", "确定", rightTxt, true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent();
                if (isActivated){

                }else {
                    intent.setClass(mContext, DeviceActivateActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
