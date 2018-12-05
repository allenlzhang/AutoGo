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
import com.carlt.autogo.entry.car.NewCarModelInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 车系车型adapter
 */
public class CarModelAdapter extends BaseQuickAdapter<NewCarModelInfo,BaseViewHolder> {
    private OnItemClickCallback callback;

    public void setCallback(OnItemClickCallback callback) {
        this.callback = callback;
    }

    public CarModelAdapter(@Nullable List<NewCarModelInfo> data) {
        super(R.layout.item_car_model,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NewCarModelInfo item) {
        if (TextUtils.isEmpty(item.dataBeanTitle)){
            helper.setText(R.id.model_title,item.title);
            helper.setVisible(R.id.model_title,true);
            helper.setGone(R.id.model_txt,false);
        }else {
            helper.setGone(R.id.model_title,false);
            helper.setVisible(R.id.model_txt,true);
            helper.setText(R.id.model_txt,item.dataBeanTitle);
        }
        helper.setOnClickListener(R.id.model_txt, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemClick(item);
            }
        });

    }

}
