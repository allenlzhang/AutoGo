package com.carlt.autogo.entry.user;

import java.io.Serializable;

public class UserInfo implements Serializable {


    /**
     * id : 70855
     * mobile : 18813194723
     * gender : 3
     * realName : c3miv32wrb
     */

    public int       id;
    public String    mobile;
    public int       gender;
    public String    realName;
    public String    token;
    public BaseError err;
    public String    sex = "保密";
    public String    password;
    public String    headUrl;
    public int       userFreeze;            // 用户冻结
    public int       accountState;         // 账户状态，车联网平台　１正常，２冻结
    public String    avatarFile;        // 用户头像文件路径
    public int       alipayAuth;           // 支付宝实名认证状态  1-未认证  2-已认证
    public String    faceFile;          // 人脸图片文件路径
    public int       faceId;          // 人脸图片文件路径
    public int       logoutState;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", gender=" + gender +
                ", realName='" + realName + '\'' +
                ", token='" + token + '\'' +
                ", err=" + err +
                ", sex='" + sex + '\'' +
                ", password='" + password + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", userFreeze=" + userFreeze +
                ", accountState=" + accountState +
                ", avatarFile='" + avatarFile + '\'' +
                ", alipayAuth=" + alipayAuth +
                ", faceFile='" + faceFile + '\'' +
                ", faceId=" + faceId +
                ", logoutState=" + logoutState +
                '}';
    }
}
