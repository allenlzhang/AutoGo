package com.carlt.autogo.adapter;

import android.support.annotation.NonNull;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.AuthCarInfo;
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

public class CarNameItemAdapter extends BaseQuickAdapter<AuthCarInfo.MyCarBean, BaseViewHolder> {

    private List<String> infos;
    private int defItem = -1;
    public CarNameItemAdapter(List<AuthCarInfo.MyCarBean> data,int pos) {
        super(R.layout.item_car_name, data);
        this.defItem = pos;
    }
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthCarInfo.MyCarBean info) {
        helper.setText(R.id.tvName, info.carName);

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
