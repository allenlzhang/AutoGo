package com.carlt.autogo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carlt.autogo.R;

import java.util.List;

/**
 * Created by Marlon on 2018/11/16.
 */
public class PopupAdapter extends BaseAdapter{
    private List<String> list;
    private LayoutInflater inflater;
    private int selected_position = 0;
    private OnItemClick click;

    public void setClick(OnItemClick click) {
        this.click = click;
    }

    public void setSelected_position(int selected_position) {
        this.selected_position = selected_position;

    }

    public PopupAdapter(List<String> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list !=null){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_popup,null,false);
            holder.mTxt = view.findViewById(R.id.item_popup_txt);
            holder.mRadioBtn = view.findViewById(R.id.item_popup_rb);
            holder.mLl = view.findViewById(R.id.item_ll);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTxt.setText(list.get(i));
        holder.mRadioBtn.setEnabled(false);
        if (selected_position == i){
            holder.mRadioBtn.setChecked(true);
        }else {
            holder.mRadioBtn.setChecked(false);
        }
        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.itemClick(i);
            }
        });
        return view;
    }

    class ViewHolder{
        LinearLayout mLl;
        TextView mTxt;
        RadioButton mRadioBtn;
    }
    public interface OnItemClick{
        void itemClick(int i);
    }
}
