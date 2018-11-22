package com.carlt.autogo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.CarModelInfo;
import com.carlt.autogo.entry.car.NewCarModelInfo;

import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 车系车型adapter
 */
public class CarModelAdapter extends BaseAdapter {

    private List<NewCarModelInfo> list;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CarModelAdapter(Context context, List<NewCarModelInfo> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list !=null) {
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
            holder.context = view.findViewById(R.id.model_txt);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        final NewCarModelInfo newCarModelInfo = list.get(i);
        if (TextUtils.isEmpty(newCarModelInfo.getDataBeanTitle())){
            holder.title.setText(newCarModelInfo.getTitle());
            holder.context.setVisibility(View.GONE);
        }else {
            holder.title.setVisibility(View.GONE);
            holder.context.setText(newCarModelInfo.getDataBeanTitle());
        }
        holder.context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(newCarModelInfo);
            }
        });

        return view;
    }

    class ViewHolder{
        TextView title;
        TextView context;
    }
}
