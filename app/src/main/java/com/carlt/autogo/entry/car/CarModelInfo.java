package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 车系车型
 */
public class CarModelInfo implements Serializable{
    public List<ItemsBean> items;

    public int level;

    public BaseError err;

    public static class ItemsBean implements Serializable{
        /**
         * id : 2580
         * title : 大乘汽车
         * data : [{"id":2584,"title":"大乘G60"},{"id":2583,"title":"E20"}]
         */

        public int id;
        public String title;
        public List<DataBean> data;


        public static class DataBean implements Serializable{
            /**
             * id : 2584
             * title : 大乘G60
             */

            public int id;
            public String title;

        }
    }
}
