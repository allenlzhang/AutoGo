package com.carlt.autogo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.CarBrandInfo;

import java.util.List;

/**
 * Created by Marlon on 2018/11/21.
 * 车款Adapter
 */
public class CarBrandAdapter extends BaseAdapter {

    private List<CarBrandInfo.DataBean> list;

    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CarBrandAdapter(Context context, List<CarBrandInfo.DataBean> list) {
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
            holder.title = view.findViewById(R.id.model_title);
            holder.txt = view.findViewById(R.id.model_txt);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final CarBrandInfo.DataBean dataBean = list.get(i);
        if (TextUtils.isEmpty(dataBean.getTitle())){
            holder.title.setText(dataBean.getYear());
            holder.txt.setVisibility(View.GONE);
        }else {
            holder.txt.setText(dataBean.getTitle());
            holder.title.setVisibility(View.GONE);
        }

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(dataBean);
            }
        });

        return view;
    }

    class ViewHolder{
        TextView title;
        TextView txt;
    }

}
