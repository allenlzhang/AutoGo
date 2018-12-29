package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.BrandAdapter;
import com.carlt.autogo.adapter.OnItemClickCallback;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.BrandInfo;
import com.carlt.autogo.entry.car.CarBrandInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/20.
 * 品牌
 */
public class BrandActivity extends BaseMvpActivity {


    @BindView(R.id.layout_list)
    RecyclerView layoutList;

    private BrandAdapter adapter;

    private static final int CODE_BRAND_REQUEST = 0xb6;

    @Override
    protected int getContentView() {
        return R.layout.layout_list;
    }

    @Override
    public void init() {
        setTitleText("品牌");
        Intent intent = getIntent();
        BrandInfo info = (BrandInfo) intent.getSerializableExtra("brand");
        layoutList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layoutList.setLayoutManager(linearLayoutManager);
        adapter = new BrandAdapter(getData(info));
        layoutList.setAdapter(adapter);
        adapter.setCallback(new OnItemClickCallback() {
            @Override
            public void onItemClick(Object o) {
                BrandInfo.BrandData info = (BrandInfo.BrandData) o;
                Intent intent1 = new Intent(BrandActivity.this, ModelActivity.class);
                intent1.putExtra("brandId", info.id);
                LogUtils.e(info.id);
                startActivityForResult(intent1, CODE_BRAND_REQUEST);
            }
        });
    }

    private List<BrandInfo.BrandData> getData(BrandInfo brandInfo) {
        List<BrandInfo.BrandData> list = brandInfo.items;
        Collections.sort(list);
        List<BrandInfo.BrandData> list1 = new ArrayList<>();
        String initial = "";
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.equals(initial, list.get(i).initial)) {
                BrandInfo.BrandData info = new BrandInfo.BrandData();
                info.initial = list.get(i).initial;
                list1.add(info);
                initial = list.get(i).initial;
            }
            list1.add(list.get(i));
        }
        return list1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_BRAND_REQUEST) {
                CarBrandInfo.DataBean dataBean = (CarBrandInfo.DataBean) data.getSerializableExtra("carName");
                Intent intent = new Intent();
                intent.putExtra("carName", dataBean);
                this.setResult(RESULT_OK, intent);
                this.finish();
            }
        }
    }
}
