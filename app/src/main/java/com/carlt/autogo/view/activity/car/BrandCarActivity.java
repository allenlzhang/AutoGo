package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarBrandAdapter;
import com.carlt.autogo.adapter.OnItemClickCallback;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
    RecyclerView layoutList;

    private CarBrandAdapter adapter;
    private int modelId;
    private int brandId;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("车款");
        Intent intent = getIntent();
        CarBrandInfo info = (CarBrandInfo) intent.getSerializableExtra("brandCar");
        layoutList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutList.setLayoutManager(linearLayoutManager);
        layoutList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        if (info != null) {
            adapter = new CarBrandAdapter(getData(info));
            layoutList.setAdapter(adapter);
            adapter.setCallback(new OnItemClickCallback() {
                @Override
                public void onItemClick(Object o) {
                    CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) o;
                    showCommonDialog(dataBean);
                }
            });
        }
        modelId = intent.getIntExtra("modelId", -1);
        brandId = intent.getIntExtra("brandId", -1);
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
                        LogUtils.e(carBrandInfo.toString());
                        dialog.dismiss();
                        if (carBrandInfo.err != null) {
                            ToastUtils.showShort(carBrandInfo.err.msg);
                        } else {
                            if (carBrandInfo.items != null) {
                                adapter = new CarBrandAdapter(getData(carBrandInfo));
                                layoutList.setAdapter(adapter);
                                adapter.setCallback(new OnItemClickCallback() {
                                    @Override
                                    public void onItemClick(Object o) {
                                        CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) o;
                                        showCommonDialog(dataBean);

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

    private void showCommonDialog(final CarBrandInfo.DataBean dataBean){
        CommonDialog.createTwoBtnDialog(this, "当前选择" + dataBean.title + "是否确定", true, new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent();
                int Optionid = dataBean.id;
                LogUtils.e(Optionid);
                intent.putExtra("carName",dataBean);
                intent.putExtra("Optionid",Optionid);
                intent.putExtra("modelId",modelId);
                intent.putExtra("brandId",brandId);
                BrandCarActivity.this.setResult(RESULT_OK,intent);
                BrandCarActivity.this.finish();
            }
        });
    }

}
