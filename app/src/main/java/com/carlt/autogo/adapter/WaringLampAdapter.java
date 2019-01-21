package com.carlt.autogo.adapter;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.home.WaringItemInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2019/1/21.
 */
public class WaringLampAdapter extends BaseQuickAdapter<WaringItemInfo, BaseViewHolder> {
    public WaringLampAdapter(List<WaringItemInfo> data) {
        super(R.layout.item_waring_lamp, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaringItemInfo item) {
        helper.setBackgroundRes(R.id.ivItemWaringLamp, item.img)
                .setText(R.id.txtItemWaringLamp, item.txt)
                .setTextColor(R.id.txtItemWaringLamp, item.color);
    }

}
