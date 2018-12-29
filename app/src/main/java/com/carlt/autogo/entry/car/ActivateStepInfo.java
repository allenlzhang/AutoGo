package com.carlt.autogo.entry.car;

import com.carlt.autogo.entry.user.BaseError;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/12/26 16:55
 */
public class ActivateStepInfo {

    public List<LogsBean>  logs;
    public List<StepsBean> steps;
    public BaseError       err;

    @Override
    public String toString() {
        return "ActivateStepInfo{" +
                "logs=" + logs +
                ", steps=" + steps +
                ", err=" + err +
                '}';
    }

    public static class LogsBean {
        /**
         * isSuccess : true
         * title : 核实车辆信息中
         */

        public boolean isSuccess;
        public String  title;
        public String  failReason;

        @Override
        public String toString() {
            return "LogsBean{" +
                    "isSuccess=" + isSuccess +
                    ", title='" + title + '\'' +
                    ", failReason='" + failReason + '\'' +
                    '}';
        }
    }

    public static class StepsBean {
        /**
         * title : 核实车辆信息中
         * description : 此过程视运营商网络状况，可能需要15分钟至24小时
         */
        public int isSuccess = -1;
        public int failCode  = -1; //2226-车型排量错误 2201-PIN码错误
        public String title;
        public String description = "";
        public String failReason  = "";

        @Override
        public String toString() {
            return "StepsBean{" +
                    "isSuccess=" + isSuccess +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", failReason='" + failReason + '\'' +
                    '}';
        }
    }
}
