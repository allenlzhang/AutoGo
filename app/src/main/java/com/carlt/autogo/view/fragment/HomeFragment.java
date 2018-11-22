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
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.more.safety.FaceRecognitionSettingFirstActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.carlt.autogo.adapter.PopupAdapter;

import java.util.ArrayList;
import java.util.List;

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

    private PopupAdapter adapter;
    private List<String> list;
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
        adapter = new PopupAdapter(getData(), getContext());
        adapter.setClick(new PopupAdapter.OnItemClick() {
            @Override
            public void itemClick(int i) {
                popupWindow.dismiss();
                adapter.setSelected_position(i);
                tvCarType.setText(list.get(i));
            }
        });
    }


    private List<String> getData() {
        list = new ArrayList<>();
//        list.add("TXT1");
//        list.add("TXT2");
//        list.add("TXT3");
//        list.add("TXT4");
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (list.size() == 0) {
            homeRlLock.setVisibility(View.VISIBLE);
        }else {
            homeRlLock.setVisibility(View.GONE);
        }
    }

    private void showPopupWindow(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popupwindow, null, false);
        ListView mListView = contentView.findViewById(R.id.list_popup);
        mListView.setAdapter(adapter);
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
