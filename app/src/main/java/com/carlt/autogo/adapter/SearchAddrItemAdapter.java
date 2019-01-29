package com.carlt.autogo.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.carlt.autogo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Description :
 * Author     : zhanglei
 * Date       : 2019/1/21
 */

public class SearchAddrItemAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

    private List<Tip> mDataList;
    AMapLocation current;
    String       text;

    public SearchAddrItemAdapter(List<Tip> data, AMapLocation aMapLocation) {
        super(R.layout.item_search_address, data);
        mDataList = data;
        current = aMapLocation;
    }


    @Override
    protected void convert(BaseViewHolder helper, Tip info) {
        String s = info.getName();
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(s) && s.contains(text)) {
            s = s.replace(text, "<font color='#3d93fd'>" + text + "</font>");
            helper.setText(R.id.item_address_txt_name, Html.fromHtml(s));
        } else {
            helper.setText(R.id.item_address_txt_name, s + "");
        }
        helper.setText(R.id.item_address_txt_detail, info.getAddress());
        LatLonPoint llp = info.getPoint();
        float[] ff = new float[4];
        AMapLocation.distanceBetween(current.getLatitude(), current.getLongitude(), llp.getLatitude(), llp.getLongitude(), ff);
        float dis = ff[0] / 1000;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        helper.setText(R.id.item_address_txt_distance, decimalFormat.format(dis) + "km");
    }

    public void setText(String s) {
        this.text = s;
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
        notifyDataSetChanged();
    }

}
