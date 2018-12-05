package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarModelAdapter;
import com.carlt.autogo.adapter.OnItemClickListener;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.car.NewCarModelInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.google.gson.Gson;

import java.util.ArrayList;
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
 * 车型车款
 */
public class ModelActivity extends BaseMvpActivity {


    @BindView(R.id.layout_list)
    ListView layoutList;

    private static final int CODE_MODEL_RESULT = 0xb5;

    private CarModelAdapter adapter;
    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("车型");
        Intent intent = getIntent();
        CarModelInfo info = (CarModelInfo) intent.getSerializableExtra("model");
        if (info!=null) {
            adapter = new CarModelAdapter(this, getData(info));
            layoutList.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(Object o) {
                    NewCarModelInfo newCarModelInfo = (NewCarModelInfo) o;
                    Intent intent1 = new Intent(ModelActivity.this,BrandCarActivity.class);
                    intent1.putExtra("modelId",newCarModelInfo.dataBeanId);
                    startActivityForResult(intent1,CODE_MODEL_RESULT);
                }
            });
        }
        int brandId = intent.getIntExtra("brandId",-1);
        if (brandId != -1){
            clientGetData(brandId);
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

    private void clientGetData(int brandId){
        dialog.show();
        Map<String,Integer>map = new HashMap<>();
        map.put("brandId",brandId);
        Disposable disposable = ClientFactory.def(CarService.class).getModel(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarModelInfo>() {
                    @Override
                    public void accept(CarModelInfo carModelInfo) throws Exception {
                        dialog.dismiss();
                        if (carModelInfo.err!=null){
                            ToastUtils.showShort(carModelInfo.err.msg);
                        }else {
                            if (carModelInfo.items!=null) {
                                adapter = new CarModelAdapter(ModelActivity.this, getData(carModelInfo));
                                layoutList.setAdapter(adapter);
                                adapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o) {
                                        NewCarModelInfo newCarModelInfo = (NewCarModelInfo) o;
                                        Intent intent1 = new Intent(ModelActivity.this,BrandCarActivity.class);
                                        intent1.putExtra("modelId",newCarModelInfo.dataBeanId);
                                        startActivityForResult(intent1,CODE_MODEL_RESULT);
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
}
