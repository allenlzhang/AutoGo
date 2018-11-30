package com.carlt.autogo.entry.car;

import android.support.annotation.NonNull;

import com.carlt.autogo.entry.user.BaseError;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marlon on 2018/11/21.
 */
public class CarBrandInfo implements Serializable{


    public List<DataBean> items;

    public int level;

    public BaseError err;

    public static class DataBean implements Comparable<DataBean>,Serializable{
        /**
         * id : 2179033
         * title : 野马F99 2009款 1.5L 旗舰型
         * year : 2018
         */

        public int id;
        public String title;
        public String year;


        @Override
        public int compareTo(@NonNull DataBean dataBean) {
            int i = Integer.parseInt(dataBean.year) - Integer.parseInt(this.year);
            return i == 0?dataBean.id - this.id:i;
        }
    }
}
