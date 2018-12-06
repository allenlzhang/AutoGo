package com.carlt.autogo.entry.user;

public class User {
    /**
     * err : {"code":2010,"msg":"用户名或密码错误"}
     */

    public String token;
    public String msg;
    public int    isSet;
    public int    code;

    public BaseError err;

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", isSet=" + isSet +
                ", err=" + err +
                '}';
    }
}
