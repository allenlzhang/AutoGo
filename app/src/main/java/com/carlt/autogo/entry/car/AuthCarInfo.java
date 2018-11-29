package com.carlt.autogo.entry.car;

import java.util.List;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/11/28 17:32
 */
public class AuthCarInfo {

    public List<MyCarBean> myCar;
    public List<MyCarBean> AuthCar;

    @Override
    public String toString() {
        return "AuthCarInfo{" +
                "myCar=" + myCar +
                ", AuthCar=" + AuthCar +
                '}';
    }

    public static class MyCarBean {
        /**
         * id : 5
         * brandTitle : 雪佛兰
         * modelTitle : 上汽通用雪佛兰
         * optionTitle : 科鲁兹
         * carName : 科鲁兹 2009款 1.6 SL AT
         */

        public int id;
        public String brandTitle;
        public String modelTitle;
        public String optionTitle;
        public String carName;

        @Override
        public String toString() {
            return "MyCarBean{" +
                    "id=" + id +
                    ", brandTitle='" + brandTitle + '\'' +
                    ", modelTitle='" + modelTitle + '\'' +
                    ", optionTitle='" + optionTitle + '\'' +
                    ", carName='" + carName + '\'' +
                    '}';
        }
    }
}
