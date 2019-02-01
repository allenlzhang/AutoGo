package com.carlt.autogo.entry.user;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/12/6 16:53
 */
public class ShareLoginList {

    public List<ListBean> list;
    public BaseError err;

    @Override
    public String toString() {
        return "ShareLoginList{" +
                "list=" + list +
                ", err=" + err +
                '}';
    }

    public static class ListBean {
        /**
         * openType : 1
         * CreateTime : 1544085802
         */

        public int openType;
        public int CreateTime;
        public String nickName;

        @Override
        public String toString() {
            return "ListBean{" +
                    "openType=" + openType +
                    ", CreateTime=" + CreateTime +
                    ", nickName='" + nickName + '\'' +
                    '}';
        }
    }
}
