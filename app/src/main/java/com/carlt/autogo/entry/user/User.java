package com.carlt.autogo.entry.user;

import android.annotation.SuppressLint;


public class User  {
    /**
     * err : {"code":2010,"msg":"用户名或密码错误"}
     */

    public String token;

    public BaseError err;


    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", err=" + err +
                '}';
    }
}
