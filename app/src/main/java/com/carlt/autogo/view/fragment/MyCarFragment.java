package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.car.ICarListView;
import com.carlt.autogo.presenter.car.MyCarListPresenter;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.carlt.autogo.view.activity.more.safety.FaceAuthSettingActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/21.
 */
@CreatePresenter(presenter = MyCarListPresenter.class)
public class MyCarFragment extends BaseMvpFragment<MyCarListPresenter> implements ICarListView {
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    View bottomView;
    private              MyCarAdapter adapter;
    private static final int          CODE_ADDCAR_REQUEST = 1111;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        adapter = new MyCarAdapter(mContext, null, MyCarAdapter.MYCAR);
        if (bottomView == null) {
            bottomView = LayoutInflater.from(mContext).inflate(R.layout.layout_add_car_bottom, null);
            ImageView mIvAdd = bottomView.findViewById(R.id.layout_iv_myCar_add);
            if (fragmentLvMyCar.getFooterViewsCount() == 0) {
                fragmentLvMyCar.addFooterView(bottomView);
            }
            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    certification();
                }
            });
        } else {
            if (fragmentLvMyCar.getFooterViewsCount() == 0) {
                fragmentLvMyCar.addFooterView(bottomView);
            }
        }
        fragmentLvMyCar.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ClientGetMyCar();
    }

    private List<AuthCarInfo.MyCarBean> getData(AuthCarInfo carListInfo) {
        List<AuthCarInfo.MyCarBean> infos = new ArrayList<>();
        if (carListInfo != null && carListInfo.myCar != null) {
            if (carListInfo.myCar.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    infos.add(carListInfo.myCar.get(i));
                }
            } else {
                infos.addAll(carListInfo.myCar);
            }
        }
        return infos;
    }

    @SuppressLint("CheckResult")
    private void ClientGetMyCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        getPresenter().getCarList(map);
    }

    private void parseGetMyCarList(AuthCarInfo authCarInfo) {
        if (authCarInfo.err != null) {
            ToastUtils.showShort(authCarInfo.err.msg);
        } else {
            List<AuthCarInfo.MyCarBean> list = getData(authCarInfo);
            if (list.size() >= 5 && bottomView != null) {
                fragmentLvMyCar.removeFooterView(bottomView);
            }
            //            adapter = new MyCarAdapter(mContext, list, MyCarAdapter.MYCAR);
            //            fragmentLvMyCar.setAdapter(adapter);
            adapter.update(list);
            LogUtils.e("listView---count---" + adapter.getCount());
            fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AuthCarInfo.MyCarBean item = (AuthCarInfo.MyCarBean) adapterView.getItemAtPosition(i);
                    if (item == null) {
                        return;
                    }
                    Intent intent = new Intent(mContext, CarDetailsActivity.class);
                    intent.putExtra("id", item.id);
                    if (item.authStatus != 2) {
                        if (item.remoteStatus == 1) {
                            intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE1);
                        } else if (item.remoteStatus == 2) {
                            intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE2);
                        } else {
                            intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE1);
                        }
                    } else {
                        intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE3);
                    }
                    startActivity(intent);
                }
            });
            if (list.size() > 0) {
                SingletonCar.getInstance().setBound(true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_ADDCAR_REQUEST) {
                ClientGetMyCar();
            }
        }
    }

    private void certification() {
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 2) {
            if (user.faceId == 0) {
                intent.setClass(mContext, FaceAuthSettingActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ALiPay_Auth);
                startActivity(intent);
            } else {
                intent.setClass(mContext, CarCertificationActivity.class);
                startActivityForResult(intent, CODE_ADDCAR_REQUEST);
            }
        } else if (user.identityAuth == 2) {
            if (user.faceId == 0) {
                intent.setClass(mContext, FaceAuthSettingActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceAuthSettingActivity.From_ID_Card);
                startActivity(intent);
            } else {
                intent.setClass(mContext, CarCertificationActivity.class);
                startActivityForResult(intent, CODE_ADDCAR_REQUEST);
            }

        } else {
            intent.setClass(mContext, UserIdChooseActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void getCarListSuccess(AuthCarInfo info) {
        SingletonCar.getInstance().setBound(true);
        SingletonCar.getInstance().setCarBean(info.myCar.get(0));
        parseGetMyCarList(info);
    }
}
