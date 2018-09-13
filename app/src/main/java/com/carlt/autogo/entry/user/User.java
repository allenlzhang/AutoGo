package com.carlt.autogo.entry.user;

public class User {
    /**
     * err : {"code":2010,"msg":"用户名或密码错误"}
     */

    public ErrBean err;


    public static class ErrBean {
        /**
         * code : 2010
         * msg : 用户名或密码错误
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

    public String token;
    /**
     * code : 2008
     * msg : 验证码错误
     */
    @Override
    public String toString() {
        return "User{" +
                "err=" + err +
                '}';
    }
}
