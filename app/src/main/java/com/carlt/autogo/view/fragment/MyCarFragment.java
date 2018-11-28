package com.carlt.autogo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.car.CarListInfo;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * Created by Marlon on 2018/11/21.
 */
public class MyCarFragment extends BaseMvpFragment {
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    @BindView(R.id.fragment_iv_myCar_add)
    ImageView fragmentIvMyCarAdd;

    private MyCarAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        final List<CarListInfo.DataBean> list = getData();
        if (list.size() >= 5) {
            fragmentIvMyCarAdd.setVisibility(View.GONE);
        }
        adapter = new MyCarAdapter(getContext(), list, MyCarAdapter.MYCAR);
        fragmentLvMyCar.setAdapter(adapter);
        fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarListInfo.DataBean item = (CarListInfo.DataBean)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(mContext, CarDetailsActivity.class);
                if (item.getAuthStatus() != 3){
                    if (item.getRemoteStatus() == 2) {
                        intent.putExtra("remoteActivating",true);
                        intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE1);
                    }else if (item.getRemoteStatus() == 3){
                        intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE2);
                    }else {
                        intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE1);
                    }
                }else{
                    intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE3);
                }
                startActivity(intent);
            }
        });
    }

    private List<CarListInfo.DataBean> getData() {
        String json = "{\"myCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
        Gson gson = new Gson();
        CarListInfo carListInfo = gson.fromJson(json, CarListInfo.class);
        List<CarListInfo.DataBean> infos = new ArrayList<>();
        if (carListInfo != null && carListInfo.getMyCarList() != null) {
            if (carListInfo.getMyCarList().size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    infos.add(carListInfo.getMyCarList().get(i));
                }
            } else {
                infos.addAll(carListInfo.getMyCarList());
            }
        }
        return infos;
    }

    @OnClick(R.id.fragment_iv_myCar_add)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), CarCertificationActivity.class);
        startActivity(intent);
    }


}
