package com.carlt.autogo.entry.remote;

import java.util.List;

/**
 * Created by Marlon on 2019/1/17.
 */
public class RemoteLogInfo {
    /**
     * code : 200
     * data : {"list":[{"logtype":"11","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"71","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"53","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"64","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"62","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"21","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"41","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"11","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"},{"logtype":"52","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"},{"logtype":"64","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"}],"has_next":1}
     * msg : 操作成功
     * version : v100
     * request : /126/carRelated/getRemoteOperationLog
     */

    private int code;
    private DataBean data;
    private String msg;
    private String version;
    private String request;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public static class DataBean {
        /**
         * list : [{"logtype":"11","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"71","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"53","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:03"},{"logtype":"64","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"62","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"21","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"41","log_device_name":"zhang的 iPhone","log_result":"1","logtime":"2019-01-07 15:02"},{"logtype":"11","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"},{"logtype":"52","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"},{"logtype":"64","log_device_name":"MI 6X","log_result":"1","logtime":"2019-01-07 11:30"}]
         * has_next : 1
         */

        private int has_next;
        private List<ListBean> list;

        public int getHas_next() {
            return has_next;
        }

        public void setHas_next(int has_next) {
            this.has_next = has_next;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * logtype : 11
             * log_device_name : zhang的 iPhone
             * log_result : 1
             * logtime : 2019-01-07 15:03
             */

            private String logtype;
            private String log_device_name;
            private String log_result;
            private String logtime;

            public String getLogtype() {
                return logtype;
            }

            public void setLogtype(String logtype) {
                this.logtype = logtype;
            }

            public String getLog_device_name() {
                return log_device_name;
            }

            public void setLog_device_name(String log_device_name) {
                this.log_device_name = log_device_name;
            }

            public String getLog_result() {
                return log_result;
            }

            public void setLog_result(String log_result) {
                this.log_result = log_result;
            }

            public String getLogtime() {
                return logtime;
            }

            public void setLogtime(String logtime) {
                this.logtime = logtime;
            }
        }
    }
    /**
     * {"code":200,"data":{"list":[{"logtype":"11","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:03"},{"logtype":"71","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:03"},{"logtype":"53","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:03"},{"logtype":"64","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:02"},{"logtype":"62","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:02"},{"logtype":"21","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:02"},{"logtype":"41","log_device_name":"zhang的 iPhone","log_result":"1",
     * "logtime":"2019-01-07 15:02"},{"logtype":"11","log_device_name":"MI 6X","log_result":"1",
     * "logtime":"2019-01-07 11:30"},{"logtype":"52","log_device_name":"MI 6X","log_result":"1",
     * "logtime":"2019-01-07 11:30"},{"logtype":"64","log_device_name":"MI 6X","log_result":"1",
     * "logtime":"2019-01-07 11:30"}],"has_next":1},"msg":"操作成功","version":"v100","request":"\/126\/carRelated\/getRemoteOperationLog"}
     *
     */

}
