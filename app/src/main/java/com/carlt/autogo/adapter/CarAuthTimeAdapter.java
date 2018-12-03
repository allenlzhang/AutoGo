package com.carlt.autogo.adapter;

import android.support.annotation.NonNull;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Description:
 * Copyright  :
 * Company    : HangAnTong
 * Author     : zhangLei
 * Date       : 2016/12/7 15:18
 */

public class CarAuthTimeAdapter extends BaseQuickAdapter<CarAuthTimeInfo.ListBean, BaseViewHolder> {

    private List<String> infos;

    private int defItem = -1;

    public CarAuthTimeAdapter(List<CarAuthTimeInfo.ListBean> data, int pos) {
        super(R.layout.item_car_name, data);
        this.defItem = pos;
    }


    @Override
    protected void convert(BaseViewHolder helper, CarAuthTimeInfo.ListBean info) {
        helper.setText(R.id.tvName, info.name);
        helper.setVisible(R.id.ivSelect, info.isSelect);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (defItem != -1) {
            if (defItem == position) {
                holder.setVisible(R.id.ivSelect, true);
            } else {
                holder.setVisible(R.id.ivSelect, false);
            }
        }
    }
}
