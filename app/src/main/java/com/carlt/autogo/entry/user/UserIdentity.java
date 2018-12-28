package com.carlt.autogo.entry.user;

/**
 * Created by Marlon on 2018/12/28.
 */
public class UserIdentity {
    public int uid; //用户ID
    public String name; // 姓名
    public String number; // 身份证号
    public int front ; // 身份证正面图片id
    public int back ; // 身份证背面图片id
    public long createTime; // 注册时间
    public BaseError err;

    @Override
    public String toString() {
        return "UserIdentity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", front=" + front +
                ", back=" + back +
                ", createTime=" + createTime +
                ", err=" + err +
                '}';
    }
}
