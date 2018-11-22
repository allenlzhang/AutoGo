package com.carlt.autogo.entry.car;

import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 车系车型
 */
public class CarModelInfo {


    /**
     * code : 200
     * data : [{"id":157,"title":"一汽大众奥迪","data":[{"id":486,"title":"奥迪A4L"},{"id":2572,"title":"奥迪A6L"}]},{"id":157,"title":"奥迪(进口)","data":[{"id":486,"title":"奥迪TT"}]}]
     * msg :
     * version : v100
     * request : /200/comm/getModelList
     */

    private List<DataBeanX> data;

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * id : 157
         * title : 一汽大众奥迪
         * data : [{"id":486,"title":"奥迪A4L"},{"id":2572,"title":"奥迪A6L"}]
         */

        private int id;
        private String title;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 486
             * title : 奥迪A4L
             */

            private int id;
            private String title;

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

            @Override
            public String toString() {
                return "DataBean{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        '}';
            }

        }

        @Override
        public String toString() {
            return "DataBeanX{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

}
