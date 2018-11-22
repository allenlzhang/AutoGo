package com.carlt.autogo.view.activity.car;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarModelAdapter;
import com.carlt.autogo.adapter.OnItemClickListener;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.car.NewCarModelInfo;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/20.
 */
public class ModelActivity extends BaseMvpActivity {


    @BindView(R.id.layout_list)
    ListView layoutList;

    private CarModelAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("车型");
        adapter = new CarModelAdapter(this,getData());
        layoutList.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                NewCarModelInfo newCarModelInfo = (NewCarModelInfo) o;
                ToastUtils.showShort(newCarModelInfo.getTitle()+"---"+newCarModelInfo.getDataBeanTitle());
            }
        });
    }
    private List<NewCarModelInfo> getData(){
        List<NewCarModelInfo> list = new ArrayList<>();
        String json = "{\"code\":200,\"data\":[{\"id\":157,\"title\":\"一汽大众奥迪\",\"data\":[{\"id\":486,\"title\":\"奥迪A4L\"},{\"id\":2572,\"title\":\"奥迪A6L\"}]},{\"id\":157,\"title\":\"奥迪(进口)\",\"data\":[{\"id\":486,\"title\":\"奥迪TT\"}]}],\"msg\":\"\",\"version\":\"v100\",\"request\":\"\\/200\\/comm\\/getModelList\"}";
        Gson gson = new Gson();
        CarModelInfo carModelInfo= gson.fromJson(json, CarModelInfo.class);
        List<CarModelInfo.DataBeanX> dataBeanXES = carModelInfo.getData();
        NewCarModelInfo info;
        for (int i = 0; i <dataBeanXES.size() ; i++) {
            CarModelInfo.DataBeanX dataBeanX = dataBeanXES.get(i);
            info = new NewCarModelInfo();
            info.setId(dataBeanX.getId());
            info.setTitle(dataBeanX.getTitle());
            list.add(info);
            for (int j = 0; j <dataBeanX.getData().size() ; j++) {
                CarModelInfo.DataBeanX.DataBean dataBean = dataBeanX.getData().get(j);
                info = new NewCarModelInfo();
                info.setId(dataBeanX.getId());
                info.setTitle(dataBeanX.getTitle());
                info.setDataBeanId(dataBean.getId());
                info.setDataBeanTitle(dataBean.getTitle());
                list.add(info);
            }
        }
        return list;
    }

}
