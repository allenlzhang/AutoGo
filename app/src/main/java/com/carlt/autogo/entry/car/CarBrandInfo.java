package com.carlt.autogo.entry.car;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Marlon on 2018/11/21.
 */
public class CarBrandInfo {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Comparable<DataBean>{
        /**
         * id : 2179033
         * title : 野马F99 2009款 1.5L 旗舰型
         * year : 2018
         */

        private int id;
        private String title;
        private String year;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        @Override
        public int compareTo(@NonNull DataBean dataBean) {
            int i = Integer.parseInt(dataBean.year) - Integer.parseInt(this.year);
            return i == 0?dataBean.id - this.id:i;
        }
    }
}
