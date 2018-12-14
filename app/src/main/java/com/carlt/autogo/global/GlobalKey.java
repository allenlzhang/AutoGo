package com.carlt.autogo.global;

/**
 * Created by HAT on 2017/12/27.
 */

public class GlobalKey {
    public static final String Authentication   = "1";
    public static final String UnAuthentication = "0";
    public static final String TEST_ACCESSID    = "18644515396614518644";   //autoGo 测试
//    public static final String TEST_ACCESSID    = "12938315356991092938"; //大乘测试
    public static final String PRE_ACCESSID     = "10590215396563070590";
    public static final String AUTH_REGEX       = "carlt_car_auth_id=";
    public static final String TRANSFER_REGEX   = "carlt_car_transfer_id=";

    public static final String USER_TOKEN              = "token";
    public static final String USER_INFO               = "user";
    public static final String USER_MEMBER_ID          = "member_id";
    public static final String FROM_ACTIVITY           = "From_Activity";
    public static       String FACE_LOGIN_SWITCH       = "FACE_LOGIN_SWITCH";
    public static final String PROCESS_SAFE_SWITCH     = "PROCESS_SAFE_SWITCH";
    public static       String Remote_Switch           = "Remote_Switch";
    public static final String IDENTITY_AUTH           = "IDENTITY_AUTH";
    public static final String REMOTE_SECRET_FREE_TIME = "REMOTE_SECRET_FREE_TIME";
    public static final String TOKENT_OUTOF_TIME       = "exp";
    //    public static final String Face_Login              = "Face_Login";
    public static final String LOGINTYPE               = "loginType";

    //正则密码验证 (字母 数字 字符 长度6~32)
    public static final String PWD_REGEX       = "^[A-Za-z0-9-+=<>;,./~!@#$%^*\\[.*\\]\\{.*\\}]{6,32}+$";
    public static final String PWD_REGEX2      = "^[A-Za-z0-9-+=<>;,./~!@#$%^*\\[.*\\]\\{.*\\}]{0,32}+$";
    //昵称匹配(字母 数字 汉字)
    public static final String NICK_NAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    //登录类型
    public static final int loginStateByPWd   = 1;
    public static final int loginStateByPhone = 2;
    public static final int loginStateByOther = 3;
    public static final int loginStateByFace  = 4;

    //注册类型
    public static final int RegStateByPWd   = 1;
    public static final int RegStateByOther = 2;

    //账号token 被踢 error key
    public static final int TOKEN_OUT = 2066;


    public static final String CAR_INFO = "carInfo";    //车辆基本信息

    public static final String CAR_IS_BOUND = "isBindCar"; //是否绑定车辆

    public static final String CAR_TAG = "carTag";  //当前车辆标记
}
