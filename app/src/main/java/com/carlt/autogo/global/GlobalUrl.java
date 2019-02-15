package com.carlt.autogo.global;

/**
 * Description : url管理
 */
public class GlobalUrl {
    public static final String TEST_BASE_URL         = "http://test.linewin.cc:8888/app/";
    //    public static final String TEST_BASE_URL         = "http://192.168.10.184:8080/app/";
    public final static String PRE_BASE_URL          = "http://pre-autogoapi.geni4s.com/app/";
    public final static String FORMAL_BASE_URL       = "http://autogoapi.geni4s.com/app/";
    public static final String UPDATE_IMG_TEST_URL   = "http://tmanageadmin.linewin.cc";
    public static final String UPDATE_IMG_FORMAL_URL = "http://manageadmin.geni4s.com";

    //开发阶段先介入 大乘接口
    public static final String BASE_URL_TEST_YEMA = "http://dorideapi.linewin.cc/110/";

    //大乘域名 测试服
    public final static String U1_DORIDE_TEST = "http://dorideapi.linewin.cc/";

    //大乘域名 预发布服

    // 众泰大乘API域名 正式服
    public final static String U1_DORIDE = "http://dorideapi.geni4s.com/";

    public final static int VERSION_FORMAL      = 11;// 正式服
    public final static int VERSION_PRE_RELEASE = 22;// 预发布服
    public final static int VERSION_TEST        = 33;// 测试服
    public static       int version_flag        = VERSION_TEST;

    public static String getAutoGoUrl() {
        String url = "";
        switch (version_flag) {
            case VERSION_FORMAL:
                url = FORMAL_BASE_URL;
                break;
            case VERSION_PRE_RELEASE:
                url = PRE_BASE_URL;
                break;
            case VERSION_TEST:
                url = TEST_BASE_URL;
                break;
        }
        return url;
    }


}
