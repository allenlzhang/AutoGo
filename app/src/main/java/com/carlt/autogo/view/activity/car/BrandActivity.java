package com.carlt.autogo.view.activity.car;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.BrandAdapter;
import com.carlt.autogo.adapter.OnItemClickListener;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.BrandInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/20.
 */
public class BrandActivity extends BaseMvpActivity {


    @BindView(R.id.layout_list)
    ListView layoutList;

    private BrandAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("品牌");
        adapter = new BrandAdapter(this,getData());
        layoutList.setAdapter(adapter);
        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o) {
                BrandInfo info = (BrandInfo) o;
                ToastUtils.showShort(info.getTitle());
            }
        });
    }

    private List<BrandInfo> getData() {
        List<BrandInfo> list = new ArrayList<>();
        BrandInfo brandInfo = new BrandInfo();
        brandInfo.setBrandLogo("logo");
        brandInfo.setId(1);
        brandInfo.setInitial("D");
        brandInfo.setLogoUrl("logoUrl");
        brandInfo.setTitle("大乘智享");
        brandInfo.setTitlePy("dachengzhixiang");
        brandInfo.setYear("2018");
        list.add(brandInfo);
        BrandInfo brandInfo1 = new BrandInfo();
        brandInfo1.setBrandLogo("logo");
        brandInfo1.setId(2);
        brandInfo1.setInitial("Y");
        brandInfo1.setLogoUrl("logoUrl");
        brandInfo1.setTitle("野马汽车");
        brandInfo1.setTitlePy("yemaqiche");
        brandInfo1.setYear("2018");
        list.add(brandInfo1);
        BrandInfo brandInfo4 = new BrandInfo();
        brandInfo4.setBrandLogo("logo");
        brandInfo4.setId(4);
        brandInfo4.setInitial("A");
        brandInfo4.setLogoUrl("logoUrl");
        brandInfo4.setTitle("奥迪");
        brandInfo4.setTitlePy("aodi");
        brandInfo4.setYear("2018");
        list.add(brandInfo4);
        BrandInfo brandInfo5 = new BrandInfo();
        brandInfo5.setBrandLogo("logo");
        brandInfo5.setId(5);
        brandInfo5.setInitial("A");
        brandInfo5.setLogoUrl("logoUrl");
        brandInfo5.setTitle("奥拓");
        brandInfo5.setTitlePy("aotuo");
        brandInfo5.setYear("2018");
        list.add(brandInfo5);
        BrandInfo brandInfo2 = new BrandInfo();
        brandInfo2.setBrandLogo("logo");
        brandInfo2.setId(3);
        brandInfo2.setInitial("Z");
        brandInfo2.setLogoUrl("logoUrl");
        brandInfo2.setTitle("众泰大迈");
        brandInfo2.setTitlePy("zhongtaidama");
        brandInfo2.setYear("2018");
        list.add(brandInfo2);
        Collections.sort(list);
        List<BrandInfo> list1 = new ArrayList<>();
        String initial = "";
        for (int i = 0; i < list.size() ; i++) {
            if (!TextUtils.equals(initial,list.get(i).getInitial())){
                BrandInfo info = new BrandInfo();
                info.setInitial(list.get(i).getInitial());
                list1.add(info);
                initial = list.get(i).getInitial();
            }
            list1.add(list.get(i));
        }
        return list1;
    }

}
