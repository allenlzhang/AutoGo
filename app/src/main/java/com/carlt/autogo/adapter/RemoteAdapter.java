package com.carlt.autogo.adapter;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.RemoteInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Marlon on 2018/11/26.
 */
public class RemoteAdapter extends BaseQuickAdapter<RemoteInfo,BaseViewHolder> {
    public RemoteAdapter(List<RemoteInfo> data) {
        super(R.layout.item_remote, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteInfo item) {
        int adapterPosition = helper.getAdapterPosition();
        if (adapterPosition%2 ==  0){
            helper.setImageResource(R.id.item_remote_iv1,item.id_img1);
            helper.setImageResource(R.id.item_remote_iv2,item.id_img2);
        }else {
            helper.setImageResource(R.id.item_remote_iv1,item.id_img2);
            helper.setImageResource(R.id.item_remote_iv2,item.id_img1);
        }
    }
}
