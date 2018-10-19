package com.carlt.autogo.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.carlt.autogo.utils.CipherUtils;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:26
 */
public class AutoGoApp extends Application {
    public static Context mAppContext;
    public static String  MODEL_NAME;// 手机名称

    public static String MODEL;// 手机型号

    public static String ANDROID_VERSION;// 手机安卓系统版本号

    public static String DISPLAY;// UI定制系统（如mi ui,Flyme OS 4.5A）

    public static String NIMEI;//手机唯一标识吗 (新的)

    public static String IMEI;//手机唯一标识吗 (旧的)
    public static int    VERSION;

    public static String VERSION_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        initUtils();
        //shareSdk配置
        MobSDK.init(this);
        initLib();
        initBuild();
        getVersionInfo();
    }

    private void getVersionInfo() {
        PackageManager packageManager = getPackageManager();
        try {
            VERSION = packageManager.getPackageInfo(this.getPackageName(), 0).versionCode;
            VERSION_NAME = packageManager.getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            LogUtils.e(e);
        }
    }

    private void initBuild() {

        MODEL_NAME = Build.MODEL;
        ANDROID_VERSION = "android " + Build.VERSION.RELEASE;
        MODEL = MODEL_NAME + " " + ANDROID_VERSION;
        DISPLAY = "ui_sysinfo " + android.os.Build.DISPLAY;
        NIMEI = getNewUniquePsuedoID();
        //    IMEI=getUniquePsuedoID();
    }

    private void initUtils() {
        Utils.init(this);
        LogUtils.Config config = LogUtils.getConfig();
        config.setGlobalTag("AutoGo");
        //log总开关
        config.setLogSwitch(true);
    }

    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
        // setFaceConfig();
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        List<LivenessTypeEnum> livenessList = new ArrayList<>();
        livenessList.add(LivenessTypeEnum.Eye);
        config.setLivenessTypeList(livenessList);
        config.setLivenessRandom(false);
        config.setLivenessRandomCount(1);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);
        // 是否开启提示音
        config.setSound(false);

        FaceSDKManager.getInstance().setFaceConfig(config);
    }

    public static String getNewUniquePsuedoID() {
        // 主板 + CPU 类型 +
        String m_szDevIDShort = Build.BOARD
                + Build.CPU_ABI
                + Build.PRODUCT
                + Build.SERIAL
                + Build.HARDWARE;

        m_szDevIDShort = "ZT-" + UUID.nameUUIDFromBytes(m_szDevIDShort.getBytes()).toString();

        return CipherUtils.md5(m_szDevIDShort);
    }

    public static String getUniquePsuedoID() {

        String m_szDevIDShort = Build.BRAND + Build.CPU_ABI + Build.DEVICE
                + Build.HOST + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT
                + Build.TYPE + Build.USER + Build.FINGERPRINT + Build.SERIAL
                + Build.HARDWARE;
        m_szDevIDShort = "ZT-"
                + UUID.nameUUIDFromBytes(m_szDevIDShort.getBytes()).toString();
        return CipherUtils.md5(m_szDevIDShort);
    }

}
