package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarModelAdapter;
import com.carlt.autogo.adapter.OnItemClickCallback;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.car.NewCarModelInfo;
import com.carlt.autogo.presenter.car.CarModelPresenter;
import com.carlt.autogo.presenter.car.ICarModelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/20.
 * 车型车款
 */
@CreatePresenter(presenter = CarModelPresenter.class)
public class ModelActivity extends BaseMvpActivity<CarModelPresenter> implements ICarModelView {


    @BindView(R.id.layout_list)
    RecyclerView layoutList;

    private static final int CODE_MODEL_RESULT = 0xb5;

    private CarModelAdapter adapter;
    private int brandId;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("车型");
        Intent intent = getIntent();
        CarModelInfo info = (CarModelInfo) intent.getSerializableExtra("model");
        layoutList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutList.setLayoutManager(linearLayoutManager);
        layoutList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        if (info!=null) {
            adapter = new CarModelAdapter(getData(info));
            layoutList.setAdapter(adapter);
            adapter.setCallback(new OnItemClickCallback() {
                @Override
                public void onItemClick(Object o) {
                    NewCarModelInfo newCarModelInfo = (NewCarModelInfo) o;
                    Intent intent1 = new Intent(ModelActivity.this,BrandCarActivity.class);
                    intent1.putExtra("modelId",newCarModelInfo.dataBeanId);
                    intent1.putExtra("brandId",brandId);
                    startActivityForResult(intent1,CODE_MODEL_RESULT);
                }
            });
        }
        brandId = intent.getIntExtra("brandId",-1);
        if (brandId != -1){
            getPresenter().clientGetData(brandId);
        }


    }
    private List<NewCarModelInfo> getData(CarModelInfo carModelInfo){
        List<NewCarModelInfo> list = new ArrayList<>();
//        String json = "{\"code\":200,\"data\":[{\"id\":157,\"title\":\"一汽大众奥迪\",\"data\":[{\"id\":486,\"title\":\"奥迪A4L\"},{\"id\":2572,\"title\":\"奥迪A6L\"}]},{\"id\":157,\"title\":\"奥迪(进口)\",\"data\":[{\"id\":486,\"title\":\"奥迪TT\"}]}],\"msg\":\"\",\"version\":\"v100\",\"request\":\"\\/200\\/comm\\/getModelList\"}";
//        Gson gson = new Gson();
//        CarModelInfo carModelInfo= gson.fromJson(json, CarModelInfo.class);
        List<CarModelInfo.ItemsBean> dataBeanXES = carModelInfo.items;
        NewCarModelInfo info;
        for (int i = 0; i <dataBeanXES.size() ; i++) {
            CarModelInfo.ItemsBean dataBeanX = dataBeanXES.get(i);
            info = new NewCarModelInfo();
            info.id = dataBeanX.id;
            info.title = dataBeanX.title;
            list.add(info);
            for (int j = 0; j <dataBeanX.data.size() ; j++) {
                CarModelInfo.ItemsBean.DataBean dataBean = dataBeanX.data.get(j);
                info = new NewCarModelInfo();
                info.id = dataBeanX.id;
                info.title = dataBeanX.title;
                info.dataBeanId = dataBean.id;
                info.dataBeanTitle = dataBean.title;
                list.add(info);
            }
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_MODEL_RESULT:
                    CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) data.getSerializableExtra("carName");
                    Intent intent = new Intent();
                    intent.putExtra("carName",dataBean);
                    ModelActivity.this.setResult(RESULT_OK,intent);
                    this.finish();
                    break;

            }}}

    @Override
    public void getModelSuccess(CarModelInfo carModelInfo) {
        if (carModelInfo.err!=null){
            ToastUtils.showShort(carModelInfo.err.msg);
        }else {
            if (carModelInfo.items!=null) {
                adapter = new CarModelAdapter(getData(carModelInfo));
                layoutList.setAdapter(adapter);
                adapter.setCallback(new OnItemClickCallback() {
                    @Override
                    public void onItemClick(Object o) {
                        NewCarModelInfo newCarModelInfo = (NewCarModelInfo) o;
                        Intent intent1 = new Intent(ModelActivity.this,BrandCarActivity.class);
                        intent1.putExtra("modelId",newCarModelInfo.dataBeanId);
                        LogUtils.e(newCarModelInfo.dataBeanId);
                        startActivityForResult(intent1,CODE_MODEL_RESULT);
                    }
                });
            }
        }
    }
}
