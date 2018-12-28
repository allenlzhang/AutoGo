package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.MyCarAdapter;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        ClientGetMyCar();
    }

    @SuppressLint("CheckResult")
    private void ClientGetMyCar(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",2);  //1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive",2);//默认1不显示，2显示设备等激活状态
        ClientFactory.def(CarService.class).getMyCarList(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo authCarInfo) throws Exception {
                        parseGetMyCarList(authCarInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    private void parseGetMyCarList(AuthCarInfo authCarInfo){
        if (authCarInfo.err != null) {
            ToastUtils.showShort(authCarInfo.err.msg);
        } else {
            fragmentIvMyCarAdd.setVisibility(View.GONE);
            listInfos = authCarInfo.authCar;
            adapter = new MyCarAdapter(getContext(), listInfos, MyCarAdapter.AUTHCAR);
            fragmentLvMyCar.setAdapter(adapter);
            fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(mContext, CarDetailsActivity.class);
                    intent.putExtra("type", CarDetailsActivity.DETAILS_TYPE4);
                    intent.putExtra("id",listInfos.get(i).id);
                    startActivity(intent);
                }
            });
        }
    }
}
