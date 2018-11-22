package com.carlt.autogo.view.activity.car;

import android.os.Bundle;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarBrandAdapter;
import com.carlt.autogo.adapter.CarModelAdapter;
import com.carlt.autogo.adapter.OnItemClickListener;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Marlon on 2018/11/20.
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
        adapter = new CarBrandAdapter(this, getData());
        layoutList.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) o;
                ToastUtils.showShort(dataBean.getTitle());
            }
        });
    }

    private List<CarBrandInfo.DataBean> getData() {
        List<CarBrandInfo.DataBean> list = new ArrayList<>();
        String json = "{\"data\":[{\"id\":2179033,\"title\":\"野马F99 2009款 1.5L 旗舰型\",\"year\":\"2018\"},{\"id\":2224318,\"title\":\"大乘汽车G60 1.5T 5MT 尊享型\",\"year\":\"2017\"},{\"id\":2179032,\"title\":\"野马F99 2009款 1.5L 舒适型\",\"year\":\"2018\"},{\"id\":2224317,\"title\":\"大乘汽车G60 1.5T 5MT 豪华型\",\"year\":\"2017\"}]}";
        Gson gson = new Gson();
        CarBrandInfo info = gson.fromJson(json, CarBrandInfo.class);
        List<CarBrandInfo.DataBean> dataBeans = info.getData();
        Collections.sort(dataBeans);
        String year = "";
        for (int i = 0; i < dataBeans.size(); i++) {
            if (!year.equals(dataBeans.get(i).getYear())) {
                CarBrandInfo.DataBean data = new CarBrandInfo.DataBean();
                data.setYear(dataBeans.get(i).getYear());
                list.add(data);
                year = dataBeans.get(i).getYear();
            }
            list.add(dataBeans.get(i));
        }

        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
