package com.carlt.autogo.global;

/**
 * Description : url管理
 */
public interface GlobalUrl {
//    public static final String TEST_BASE_URL         = "http://test.linewin.cc:8888/app/";
    public static final String TEST_BASE_URL         = "http://192.168.10.184:8080/app/";
    public final static String PRE_BASE_URL          = "http://pre-autogoapi.geni4s.com/app/";
    public final static String FORMAL_BASE_URL       = "http://pre-autogoapi.geni4s.com/app";
    public static final String UPDATE_IMG_TEST_URL   = "http://tmanageadmin.linewin.cc";
    public static final String UPDATE_IMG_FORMAL_URL = "http://manageadmin.geni4s.com";

    //开发阶段先介入 大乘接口
    public static final String BASE_URL_TEST_YEMA = "http://dorideapi.linewin.cc/110/";

    //大乘域名 测试服
    public final static String U1_DORIDE_TEST = "http://dorideapi.linewin.cc/";

    //大乘域名 预发布服

    // 众泰大乘API域名 正式服
    public final static String U1_DORIDE = "http://dorideapi.geni4s.com/";

}
