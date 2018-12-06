package com.carlt.autogo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.CarBrandInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2018/11/21.
 * 车款Adapter
 */
public class CarBrandAdapter extends BaseQuickAdapter<CarBrandInfo.DataBean,BaseViewHolder> {

    private OnItemClickCallback callback;

    public void setCallback(OnItemClickCallback callback) {
        this.callback = callback;
    }

    public CarBrandAdapter(@Nullable List<CarBrandInfo.DataBean> data) {
        super(R.layout.item_car_model,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CarBrandInfo.DataBean item) {
        if (TextUtils.isEmpty(item.title)){
            helper.setText(R.id.model_title,item.year);
            helper.setVisible(R.id.model_title,true);
            helper.setGone(R.id.model_txt,false);
        }else {
            helper.setText(R.id.model_txt,item.title);
            helper.setVisible(R.id.model_txt,true);
            helper.setGone(R.id.model_title,false);
        }
        helper.setOnClickListener(R.id.model_txt, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemClick(item);
            }
        });
    }

}
