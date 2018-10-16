package com.carlt.autogo.entry.user;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/10/15 18:06
 */
public class LoginLogInfo {

    /**
     * logs : [{"account":"18700996667","deviceName":"vivo Y55A","loginModel":"vivo Y55A android 6.0.1","time":1539597555,"version":1},{"account":"18700996667","time":1539595866},{"account":"18700996667","time":1539595840},{"account":"18700996667","time":1539595346},{"account":"18700996667","time":1539594551},{"account":"18700996667","time":1539594522},{"account":"18700996667","time":1539594250},{"account":"18700996667","time":1539593105},{"account":"18700996667","time":1539593011},{"account":"18700996667","time":1539592870},{"account":"18700996667","time":1539592859},{"account":"18700996667","time":1539592806},{"account":"18700996667","time":1539592745},{"account":"18700996667","time":1539592731},{"account":"18700996667","time":1539592648},{"account":"18700996667","time":1539592636},{"account":"18700996667","time":1539592435},{"account":"18700996667","time":1539592422},{"account":"18700996667","time":1539592377},{"account":"18700996667","time":1539592350}]
     * total : 526
     */

    public int total;
    public List<LogsBean> logs;

    @Override
    public String toString() {
        return "LoginLogInfo{" +
                "total=" + total +
                ", logs=" + logs +
                '}';
    }

    public static class LogsBean {
        /**
         * account : 18700996667
         * deviceName : vivo Y55A
         * loginModel : vivo Y55A android 6.0.1
         * time : 1539597555
         * version : 1
         */

        public String account;
        public String deviceName;
        public String loginModel;
        public int    time;
        public int    version;

        @Override
        public String toString() {
            return "LogsBean{" +
                    "account='" + account + '\'' +
                    ", deviceName='" + deviceName + '\'' +
                    ", loginModel='" + loginModel + '\'' +
                    ", time=" + time +
                    ", version=" + version +
                    '}';
        }
    }
}
