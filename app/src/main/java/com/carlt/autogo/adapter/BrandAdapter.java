package com.carlt.autogo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.BrandInfo;
import com.carlt.autogo.entry.car.BrandInfo.BrandData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 品牌adapter
 */
public class BrandAdapter extends BaseQuickAdapter<BrandData,BaseViewHolder>{
    private OnItemClickCallback callback;

    public void setCallback(OnItemClickCallback callback) {
        this.callback = callback;
    }

    public BrandAdapter(@Nullable List<BrandData> data) {
        super(R.layout.item_car_model,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BrandData item) {
        if (TextUtils.isEmpty(item.title)){
            helper.setText(R.id.model_title,item.initial);
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
