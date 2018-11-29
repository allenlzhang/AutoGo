package com.carlt.autogo.entry.car;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/11/28 15:58
 */
public class CarAuthTimeInfo {

    public List<ListBean> list;

    @Override
    public String toString() {
        return "CarAuthTimeInfo{" +
                "list=" + list +
                '}';
    }

    public static class ListBean {
        /**
         * type : 7
         * name : 长期
         */

        public int type;
        public String name;
        public boolean isSelect;
        @Override
        public String toString() {
            return "ListBean{" +
                    "type=" + type +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
