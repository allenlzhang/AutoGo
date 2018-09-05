package com.carlt.autogo.entry.user;

import java.util.List;

public class User {


    public boolean error;
    public List<ResultsBean> results;

    public static class ResultsBean {
        /**
         * _id : 5b7105eb9d212234189c24ce
         * createdAt : 2018-08-13T12:15:39.942Z
         * desc : 又一个Android权限管理器。
         * publishedAt : 2018-08-28T00:00:00.0Z
         * source : chrome
         * type : Android
         * url : https://github.com/yanzhenjie/AndPermission
         * used : true
         * who : lijinshanmx
         * images : ["https://ww1.sinaimg.cn/large/0073sXn7ly1fupho39u2qg30rs0rs0vl","https://ww1.sinaimg.cn/large/0073sXn7ly1fupho3kq10g31400tce3i","https://ww1.sinaimg.cn/large/0073sXn7ly1fupho3piw6g30rs0rswj8","https://ww1.sinaimg.cn/large/0073sXn7ly1fupho3vyj3g31bg0wjgvp","https://ww1.sinaimg.cn/large/0073sXn7ly1fupho41j70g30rs0rs0ut"]
         */

        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;
        public List<String> images;

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "_id='" + _id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
