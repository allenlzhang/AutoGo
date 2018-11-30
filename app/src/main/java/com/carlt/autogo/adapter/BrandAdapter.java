package com.carlt.autogo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.BrandInfo.BrandData;

import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 品牌adapter
 */
public class BrandAdapter extends BaseAdapter{

    private List<BrandData> list;

    private LayoutInflater inflater;

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public BrandAdapter(Context context, List<BrandData> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_car_model,null,false);
            holder.mTitle = view.findViewById(R.id.model_title);
            holder.mContext = view.findViewById(R.id.model_txt);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final BrandData info = list.get(i);
        if (TextUtils.isEmpty(info.title)){
            holder.mTitle.setText(info.initial);
            holder.mContext.setVisibility(View.GONE);
        }else {
            holder.mTitle.setVisibility(View.GONE);
            holder.mContext.setText(info.title);
        }
        holder.mContext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(info);
            }
        });
        return view;
    }

    class ViewHolder{
        TextView mTitle;
        TextView mContext;
    }
}
