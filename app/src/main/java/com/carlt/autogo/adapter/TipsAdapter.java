package com.carlt.autogo.adapter;

import android.text.TextUtils;

import com.amap.api.services.help.Tip;
import com.carlt.autogo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2019/1/22.
 */
public class TipsAdapter extends BaseQuickAdapter<Tip,BaseViewHolder> {
    public TipsAdapter(List<Tip> data) {
        super(R.layout.item_poi, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tip item) {
        helper.setText(R.id.poi_field_id,item.getName())
                .setText(R.id.poi_value_id, TextUtils.isEmpty(item.getAddress())?item.getDistrict():item.getAddress());

    }
}
