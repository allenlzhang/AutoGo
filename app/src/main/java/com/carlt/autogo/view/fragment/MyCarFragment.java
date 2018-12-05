package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.carlt.autogo.view.activity.more.safety.FaceRecognitionSettingFirstActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/11/21.
 */
public class MyCarFragment extends BaseMvpFragment {
    @BindView(R.id.fragment_lv_myCar)
    ListView fragmentLvMyCar;
    @BindView(R.id.fragment_iv_myCar_add)
    ImageView fragmentIvMyCarAdd;

    private MyCarAdapter adapter;
    private static final  int CODE_ADDCAR_REQUEST = 1111;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_car;
    }

    @Override
    protected void init() {
        ClientGetMyCar();
    }

    private List<AuthCarInfo.MyCarBean> getData(AuthCarInfo carListInfo) {
//        String json = "{\"myCarList\":[{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":0,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":3,\"recodeStatus\":0,\"machineStatus\":0},{id:1,\"brandTitle\":\"大乘汽车\",\"modelTitle\":\"大乘\",\"optionTitle\":\"大乘 G70S\",\"carName\":\"2019款 2.0T 自动尊贵型\",\"carLogo\":\"\",\"authEndTime\":1542877148,\"authStatus\":0,\"remoteStatus\":1,\"recodeStatus\":0,\"machineStatus\":0}]}";
//        Gson gson = new Gson();
//        AuthCarInfo carListInfo = gson.fromJson(json, AuthCarInfo.class);
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
    private void ClientGetMyCar(){
        Map<String,Object> map = new HashMap<>();
        map.put("type",1);
        map.put("isShowActive",2);
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
            List<AuthCarInfo.MyCarBean> list = getData(authCarInfo);
            if (list.size() >= 5) {
                fragmentIvMyCarAdd.setVisibility(View.GONE);
            }
            adapter = new MyCarAdapter(getContext(), list, MyCarAdapter.MYCAR);
            fragmentLvMyCar.setAdapter(adapter);
            fragmentLvMyCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AuthCarInfo.MyCarBean item = (AuthCarInfo.MyCarBean) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(mContext, CarDetailsActivity.class);
                    intent.putExtra("id",item.id);
                    if (item.authStatus != 2) {
                        if (item.remoteStatus == 1) {
                            intent.putExtra("remoteActivating", true);
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
        }
    }

    @OnClick(R.id.fragment_iv_myCar_add)
    public void onViewClicked() {
        certification();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == CODE_ADDCAR_REQUEST){
                ClientGetMyCar();
            }
        }
    }

    private void certification() {
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        Intent intent = new Intent();
        if (user.alipayAuth == 1) {
            intent.setClass(mContext, UserIdChooseActivity.class);
            startActivity(intent);
        } else if (user.faceId == 0) {
            intent.setClass(mContext, FaceRecognitionSettingFirstActivity.class);
            startActivity(intent);
        } else {
            intent.setClass(mContext, CarCertificationActivity.class);
            startActivityForResult(intent,CODE_ADDCAR_REQUEST);
        }
    }
}
