package com.carlt.autogo.entry.user;

public class User {


    /**
     * err : {"code":1002,"msg":"用户密码不符合规则"}
     */

    public ErrBean err;


    public static class ErrBean {
        /**
         * code : 1002
         * msg : 用户密码不符合规则
         */

        public int code;
        public String msg;

        @Override
        public String toString() {
            return "ErrBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "err=" + err +
                '}';
    }
}
