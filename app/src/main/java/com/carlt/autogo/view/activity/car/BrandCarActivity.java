package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarBrandAdapter;
import com.carlt.autogo.adapter.CarModelAdapter;
import com.carlt.autogo.adapter.OnItemClickListener;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.BrandInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/11/20.
 * 车款
 */
public class BrandCarActivity extends BaseMvpActivity {

    @BindView(R.id.layout_list)
    ListView layoutList;

    private CarBrandAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("车款");
        Intent intent = getIntent();
        CarBrandInfo info = (CarBrandInfo) intent.getSerializableExtra("brandCar");
        if (info != null) {
            adapter = new CarBrandAdapter(this, getData(info));
            layoutList.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(Object o) {
                    CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) o;
                    Intent intent1 = new Intent();
                    intent1.putExtra("carName", dataBean.title);
                    BrandCarActivity.this.setResult(RESULT_OK, intent1);
                }
            });
        }
        int modelId = intent.getIntExtra("modelId", -1);
        if (modelId != -1) {
            clientGetData(modelId);
        }

    }

    private List<CarBrandInfo.DataBean> getData(CarBrandInfo info) {
        List<CarBrandInfo.DataBean> list = new ArrayList<>();
        List<CarBrandInfo.DataBean> dataBeans = info.items;
        Collections.sort(dataBeans);
        String year = "";
        for (int i = 0; i < dataBeans.size(); i++) {
            if (!year.equals(dataBeans.get(i).year)) {
                CarBrandInfo.DataBean data = new CarBrandInfo.DataBean();
                data.year = dataBeans.get(i).year;
                list.add(data);
                year = dataBeans.get(i).year;
            }
            list.add(dataBeans.get(i));
        }

        return list;
    }

    private void clientGetData(int modelId) {
        dialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("modelId", modelId);
        Disposable disposable = ClientFactory.def(CarService.class).getBrandCar(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarBrandInfo>() {
                    @Override
                    public void accept(CarBrandInfo carBrandInfo) throws Exception {
                        dialog.dismiss();
                        if (carBrandInfo.err != null) {
                            ToastUtils.showShort(carBrandInfo.err.msg);
                        } else {
                            if (carBrandInfo.items != null) {
                                adapter = new CarBrandAdapter(BrandCarActivity.this, getData(carBrandInfo));
                                layoutList.setAdapter(adapter);
                                adapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o) {
                                        CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) o;
                                        Intent intent1 = new Intent();
                                        intent1.putExtra("carName", dataBean);
                                        BrandCarActivity.this.setResult(RESULT_OK,intent1);
                                        BrandCarActivity.this.finish();
                                    }
                                });
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
        disposables.add(disposable);

    }

}
