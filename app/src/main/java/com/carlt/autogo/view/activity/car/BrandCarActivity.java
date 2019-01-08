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
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.presenter.car.BrandCarPresenter;
import com.carlt.autogo.presenter.car.IBrandCarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/20.
 * 车款
 */
@CreatePresenter(presenter = BrandCarPresenter.class)
public class BrandCarActivity extends BaseMvpActivity<BrandCarPresenter> implements IBrandCarView {

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
            getPresenter().clientGetData(modelId);
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

    private void showCommonDialog(final CarBrandInfo.DataBean dataBean){
        CommonDialog.createTwoBtnDialog(this, "您选择的车型是:" + dataBean.title, true, new CommonDialog.DialogWithTitleClick() {
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

    @Override
    public void getBrandCarSuccess(CarBrandInfo carBrandInfo) {
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
}
