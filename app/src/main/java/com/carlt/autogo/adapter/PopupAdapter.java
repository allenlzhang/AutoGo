package com.carlt.autogo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.carlt.autogo.R;
import com.carlt.autogo.entry.car.CarListInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlon on 2018/11/16.
 */
public class PopupAdapter extends BaseAdapter{
    private CarListInfo info;
    private LayoutInflater inflater;
    private int selected_position = 0;
    private OnItemClick click;
    private List<CarListInfo.DataBean> list = new ArrayList<>();
    private Context context;
    public void setClick(OnItemClick click) {
        this.click = click;
    }

    public void setSelected_position(int selected_position) {
        this.selected_position = selected_position;

    }

    public PopupAdapter(CarListInfo info, Context context) {
        this.context = context;
        this.info = info;
        inflater = LayoutInflater.from(context);
        if (info!=null){
            if (info.getMyCarList()!=null){
                list.addAll(info.getMyCarList());
            }
            if (info.getAuthCarList()!=null){
                list.addAll(info.getAuthCarList());
            }
        }

    }

    @Override
    public int getCount() {
        if ( info!=null){
            if (info.getMyCarList()!=null&&info.getAuthCarList()!=null){
                return info.getMyCarList().size()+info.getAuthCarList().size();
            }else{
                if (info.getMyCarList()!=null){
                    return info.getMyCarList().size();
                }else if (info.getAuthCarList()!=null){
                    return info.getAuthCarList().size();
                }else {
                    return 0;
                }

            }
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
            view = inflater.inflate(R.layout.item_popup,viewGroup,false);
            holder.mTxt = view.findViewById(R.id.item_popup_txt);
            holder.mTitle = view.findViewById(R.id.item_popup_title);
            holder.mLine1 = view.findViewById(R.id.item_popup_line1);
            holder.mLine2 = view.findViewById(R.id.item_popup_line2);
            holder.mSelected = view.findViewById(R.id.item_popup_selected_line);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTitle.setVisibility(View.GONE);
        holder.mLine1.setVisibility(View.GONE);
        holder.mLine2.setVisibility(View.GONE);
        if (info!=null&&info.getMyCarList()!=null&&info.getMyCarList().size()>0&&i == 0){
            holder.mTitle.setVisibility(View.VISIBLE);
            holder.mLine2.setVisibility(View.VISIBLE);
            holder.mTitle.setText("我的车辆");
        }
        if (info!=null&&info.getAuthCarList()!=null&&info.getAuthCarList().size()>0){
            if (info.getMyCarList()!=null&&info.getMyCarList().size()>0&&i==info.getMyCarList().size()){
                holder.mTitle.setVisibility(View.VISIBLE);
                holder.mLine2.setVisibility(View.VISIBLE);
                holder.mLine1.setVisibility(View.VISIBLE);
                holder.mTitle.setText("被授权车辆");
            }else {
                if (i==0){
                    holder.mTitle.setVisibility(View.VISIBLE);
                    holder.mLine2.setVisibility(View.VISIBLE);
                    holder.mTitle.setText("被授权车辆");
                }
            }

        }
        final CarListInfo.DataBean dataBean = list.get(i);
        holder.mTxt.setText(dataBean.getCarName());
        if (selected_position == i){
            holder.mTxt.setBackgroundColor(context.getResources().getColor(R.color.colorCarPopup));
            holder.mSelected.setVisibility(View.VISIBLE);
            holder.mTxt.getPaint().setFakeBoldText(true);
        }else {
            holder.mTxt.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            holder.mTxt.getPaint().setFakeBoldText(false);
            holder.mSelected.setVisibility(View.GONE);
        }
        holder.mTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.itemClick(i,dataBean);
            }
        });
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    class ViewHolder{
        View mLine1;
        View mLine2;
        TextView mTxt;
        TextView mTitle;
        View mSelected;
    }
    public interface OnItemClick{
        void itemClick(int i,CarListInfo.DataBean dataBean);
    }
}