package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/11/22.
 */
public class AuthFragment extends BaseMvpFragment {
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    @BindView(R.id.fragment_iv_myCar_add)
    ImageView fragmentIvMyCarAdd;

    private MyCarAdapter adapter;

    private List<AuthCarInfo.MyCarBean> listInfos;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        fragmentIvMyCarAdd.setVisibility(View.GONE);
        ClientGetMyCar();
    }
//    private List<AuthCarInfo.MyCarBean> getData(AuthCarInfo info){
//        String json = "{\"authCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
//        Gson gson = new Gson();
//        AuthCarInfo info = gson.fromJson(json,AuthCarInfo.class);
//        listInfos = info.authCar;
//
//        return listInfos;
//    }
    @SuppressLint("CheckResult")
    private void ClientGetMyCar(){
        Map<String,Integer> map = new HashMap<>();
        map.put("type",2);
        map.put("isShowActive",2);
        ClientFactory.def(CarService.class).getMyCarList(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo authCarInfo) throws Exception {
                        if (authCarInfo.err!=null){
                            ToastUtils.showShort(authCarInfo.err.msg);
                        }else {
                            fragmentIvMyCarAdd.setVisibility(View.GONE);
                            listInfos = authCarInfo.authCar;
                            adapter = new MyCarAdapter(getContext(),listInfos,MyCarAdapter.AUTHCAR);
                            fragmentLvMyCar.setAdapter(adapter);
                            fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(mContext, CarDetailsActivity.class);
                                    intent.putExtra("type",CarDetailsActivity.DETAILS_TYPE4);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }
}
