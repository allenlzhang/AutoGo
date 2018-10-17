package com.carlt.autogo.global;

/**
 * Created by HAT on 2017/12/27.
 */

public class GlobalKey {
    public static final String Authentication   = "1";
    public static final String UnAuthentication = "0";


    public static final String USER_TOKEN          = "token";
    public static final String USER_INFO           = "user";
    public static final String USER_MEMBER_ID      = "member_id";
    public static final String FROM_ACTIVITY       = "From_Activity";
    public static final String FACE_LOGIN_SWITCH   = "FACE_LOGIN_SWITCH";
    public static final String PROCESS_SAFE_SWITCH = "PROCESS_SAFE_SWITCH";
    public static final String IDENTITY_AUTH       = "IDENTITY_AUTH";

    //正则密码验证 (字母 数字 字符 长度6~32)
    public static final String PWD_REGEX = "^[A-Za-z0-9-+=<>;,./~!@#$%^*\\[.*\\]\\{.*\\}]{6,32}+$";
    //昵称匹配(字母 数字 汉字)
    public static final String NICK_NAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    public static final int loginStateByPWd = 0;
    public static final int loginStateByPhone = 1;
    public static final int loginStateByOther = 2;


}
