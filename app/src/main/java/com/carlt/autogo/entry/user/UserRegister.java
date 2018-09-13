package com.carlt.autogo.entry.user;

/**
 * @author wsq
 * @time 15:33  2018/9/13/013
 * @describe  用户注册返回实体类
 */
public class UserRegister {
    /**
     * code : 2008
     * msg : 验证码错误
     */

    public int code;
    public String msg;

    @Override
    public String toString() {
        return "UserRegister{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
