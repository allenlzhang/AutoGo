package com.carlt.autogo.view.fragment;

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

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.car.CarListInfo;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.more.safety.FaceRecognitionSettingFirstActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.carlt.autogo.adapter.CarPopupAdapter;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

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

    private CarPopupAdapter adapter;
    private PopupWindow popupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
//        tv.setText("首页");
//        CommonDialog.createDialogNotitle(getActivity(), "你还没有进行车辆认证", "", "稍候再说", "立即认证", new CommonDialog.DialogWithTitleClick() {
//            @Override
//            public void onRightClick() {
//                startActivity(new Intent(getActivity(), CarCertificationActivity.class));
//            }
//        });
        adapter = new CarPopupAdapter(getData(), getContext());
        adapter.setClick(new CarPopupAdapter.OnItemClick() {
            @Override
            public void itemClick(int i, CarListInfo.DataBean dataBean) {
                popupWindow.dismiss();
                adapter.setSelected_position(i);
                tvCarType.setText(dataBean.getCarName());
            }
        });

    }

    private CarListInfo getData() {
        String json = "{\"myCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":3,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":2,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0}],\"authCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
        String json2 = "\n" +
                "{\"myCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":3,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":2,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0}]}";

        Gson gson = new Gson();
        CarListInfo carListInfo = gson.fromJson(json,CarListInfo.class);

        return carListInfo;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (list.size() == 0) {
//            homeRlLock.setVisibility(View.VISIBLE);
//        }else {
//            homeRlLock.setVisibility(View.GONE);
//        }
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

    @OnClick({R.id.rl_home_chose_car_type, R.id.rlCarLocation,R.id.home_btn_lock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_home_chose_car_type:
                showPopupWindow(view);


                break;
            case R.id.rlCarLocation:
                break;
            case R.id.home_btn_lock:
                certification();
                break;
        }
    }
    private void certification(){
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 1){
            intent.setClass(getContext(),UserIdChooseActivity.class);
        }else if (user.faceId == 0){
            intent.setClass(getContext(),FaceRecognitionSettingFirstActivity.class);
        }else {
            intent.setClass(getContext(), CarCertificationActivity.class);
        }
        startActivity(intent);
    }
}
