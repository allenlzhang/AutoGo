package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.presenter.car.ICarListView;
import com.carlt.autogo.presenter.car.MyCarListPresenter;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/11/22.
 */
@CreatePresenter(presenter = MyCarListPresenter.class)
public class AuthFragment extends BaseMvpFragment<MyCarListPresenter> implements ICarListView{
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    @BindView(R.id.fragment_ll_not_data)
    RelativeLayout fragmentLlNotData;

    private MyCarAdapter adapter;

    private List<AuthCarInfo.MyCarBean> listInfo;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        fragmentLlNotData.setVisibility(View.GONE);
        adapter = new MyCarAdapter(getContext(), null, MyCarAdapter.AUTHCAR);
        fragmentLvMyCar.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ClientGetMyCar();
    }

    @SuppressLint("CheckResult")
    private void ClientGetMyCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);  //1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        getPresenter().getCarList(map);
    }

    private void parseGetMyCarList(AuthCarInfo authCarInfo) {
        if (authCarInfo.err != null) {
            ToastUtils.showShort(authCarInfo.err.msg);
        } else {
            listInfo = authCarInfo.authCar;
            if (listInfo != null && listInfo.size() > 0) {
                adapter.update(listInfo);
                fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(mContext, CarDetailsActivity.class);
                        intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE4);
                        intent.putExtra("id", listInfo.get(i).id);
                        startActivity(intent);
                    }
                });
                fragmentLlNotData.setVisibility(View.GONE);
            } else {
                fragmentLlNotData.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void getCarListSuccess(AuthCarInfo info) {
        parseGetMyCarList(info);
    }
}
