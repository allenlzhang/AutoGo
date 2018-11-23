package com.carlt.autogo.adapter;

import com.carlt.autogo.R;
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

public class CarNameItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> infos;

    public CarNameItemAdapter(List<String> data) {
        super(R.layout.item_car_name, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String info) {
        helper.setText(R.id.tvName, info);

    }


}
