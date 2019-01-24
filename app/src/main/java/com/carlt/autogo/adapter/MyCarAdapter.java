package com.carlt.autogo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.autogo.R;
import com.carlt.autogo.application.AutoGoApp;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.utils.MyTimeUtils;
import com.carlt.autogo.utils.gildutils.GlideCircleTransform;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Marlon on 2018/11/22.
 */
public class MyCarAdapter extends BaseAdapter {

    private List<AuthCarInfo.MyCarBean> list;

    private LayoutInflater inflater;

    public static final int MYCAR = 1;

    public static final int AUTHCAR = 2;

    private int TYPE = MYCAR;

    private Context mContext;

    private long time;

    public MyCarAdapter(Context context, List<AuthCarInfo.MyCarBean> list, int TYPE) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.TYPE = TYPE;
        try {
            time = MyTimeUtils.FORMAT_DAY.parse("2038-01-01").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void update(List<AuthCarInfo.MyCarBean> list){
        if (list!=null) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (list != null) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_my_car, null, false);
            initView(view, holder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AuthCarInfo.MyCarBean info = list.get(i);
        if (!TextUtils.isEmpty(info.carLogo)) {
            Glide.with(AutoGoApp.mAppContext)
                    .load(info.carLogo)
                    .placeholder(R.mipmap.ic_car_log_default)
                    .error(R.mipmap.ic_car_log_default)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .transform(new GlideCircleTransform(mContext))
                    .into(holder.mLogo);
        } else {
            holder.mLogo.setImageResource(R.mipmap.ic_car_log_default);
        }
        if (info.authStatus == 2 && TYPE == MYCAR) {
            holder.mIvAuthState.setVisibility(View.VISIBLE);
        } else {
            holder.mIvAuthState.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(info.optionTitle)){
            holder.mTxtModel.setText(info.optionTitle);
        }else {
            holder.mTxtModel.setText(R.string.app_def_txt);
        }

        if (!TextUtils.isEmpty(info.carName)){
            holder.mTxtCarBrand.setText(info.carName);
        }else {
            holder.mTxtCarBrand.setText(R.string.app_def_txt);
        }
        if (TYPE == MYCAR){
            holder.mTxtAuthEndTime.setVisibility(View.GONE);
            holder.mLlState.setVisibility(View.VISIBLE);
            if (info.remoteStatus == 1||info.remoteStatus == 3){
                holder.mIvRemoteState.setImageResource(R.mipmap.ic_remote_activating);
            }else if (info.remoteStatus == 2){
                holder.mIvRemoteState.setImageResource(R.mipmap.ic_remote_activated);
            }else {
                holder.mIvRemoteState.setImageResource(R.mipmap.ic_remote_activate);
            }

            if (info.recodeStatus == 1){
                holder.mIvRecorderState.setImageResource(R.mipmap.ic_recorder);
            }else if (info.recodeStatus== 2){
                holder.mIvRecorderState.setImageResource(R.mipmap.ic_recorder);
            }else {
                holder.mIvRecorderState.setImageResource(R.mipmap.ic_recorder);
            }

            if (info.machineStatus == 1){
                holder.mIvMachineState.setImageResource(R.mipmap.ic_car_machine);
            }else if (info.machineStatus == 2){
                holder.mIvMachineState.setImageResource(R.mipmap.ic_car_machine);
            }else {
                holder.mIvMachineState.setImageResource(R.mipmap.ic_car_machine);
            }

        }else {
            holder.mTxtAuthEndTime.setVisibility(View.VISIBLE);
            holder.mLlState.setVisibility(View.GONE);
            if (info.authEndTime!=0) {
                if (info.authEndTime*1000>=time){
                    holder.mTxtAuthEndTime.setText("授权截止时间：永久");
                }else {
                    holder.mTxtAuthEndTime.setText("授权截止时间："+MyTimeUtils.formatDateSecend(info.authEndTime));
                }
            }else {
                holder.mTxtAuthEndTime.setText("授权截止时间：--");
            }
        }

            return view;
    }

    private void initView(View view, ViewHolder holder) {
        holder.mLogo = view.findViewById(R.id.item_myCar_logo);
        holder.mIvAuthState = view.findViewById(R.id.item_myCar_auth_state);
        holder.mTxtModel = view.findViewById(R.id.item_myCar_model);
        holder.mTxtCarBrand = view.findViewById(R.id.item_myCar_brandCar);
        holder.mTxtAuthEndTime = view.findViewById(R.id.item_myCar_authEndTime);
        holder.mLlState = view.findViewById(R.id.item_ll_state);
        holder.mIvRemoteState = view.findViewById(R.id.item_myCar_remote);
        holder.mIvRecorderState = view.findViewById(R.id.item_myCar_recorder);
        holder.mIvMachineState = view.findViewById(R.id.item_myCar_machine);
    }

    class ViewHolder {
        ImageView mLogo;
        ImageView mIvAuthState; //img授权状态
        TextView mTxtModel;         //车型
        TextView mTxtCarBrand;      //车款
        TextView mTxtAuthEndTime;   //授权截止时间
        LinearLayout mLlState;      //远程 记录仪 车机状态父控件
        ImageView mIvRemoteState;   //远程激活状态
        ImageView mIvRecorderState; //记录仪激活状态
        ImageView mIvMachineState;  //车机激活状态
    }
}
