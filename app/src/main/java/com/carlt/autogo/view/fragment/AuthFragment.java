package com.carlt.autogo.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.car.CarListInfo;
import com.google.gson.Gson;
import java.util.List;
import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/22.
 */
public class AuthFragment extends BaseMvpFragment {
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    @BindView(R.id.fragment_iv_myCar_add)
    ImageView fragmentIvMyCarAdd;

    private MyCarAdapter adapter;

    private List<CarListInfo.DataBean> listInfos;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        fragmentIvMyCarAdd.setVisibility(View.GONE);
        getData();
        adapter = new MyCarAdapter(getContext(),listInfos,MyCarAdapter.AUTHCAR);
        fragmentLvMyCar.setAdapter(adapter);
    }
    private List<CarListInfo.DataBean> getData(){
        String json = "{\"authCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
        Gson gson = new Gson();
        CarListInfo info = gson.fromJson(json,CarListInfo.class);
        listInfos = info.getAuthCarList();

        return listInfos;
    }
}
