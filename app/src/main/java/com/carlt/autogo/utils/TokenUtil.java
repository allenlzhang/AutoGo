package com.carlt.autogo.utils;

import android.provider.Settings;

import com.baidu.idl.face.platform.utils.Base64Utils;
import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.alipay.Base64;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author wsq
 * @time 16:05  2018/10/22/022
 * @describe  对token处理工具
 */
public class TokenUtil {
    public static String decodeToken(String toekn){

     String temp  =  toekn.replace(".","=");
     String code =temp.split("=")[1];
      byte[] bytes =  Base64Utils.decode(code,0);
      String result =new String(bytes);
      Date date = new Date();
      // 7 天的秒数
      int svenDay =604800;
      Long curr = System.currentTimeMillis()/1000;

        try {
            JSONObject jsonObject = new  JSONObject(result);
            result = jsonObject.get("exp").toString();

            Long d =curr-svenDay;
            Long pre = Long.valueOf(result) - d ;
            if(pre >= 0){
                SharepUtil.putBoolean(GlobalKey.TOKENT_OUTOF_TIME,false);

            }else {
                SharepUtil.putBoolean(GlobalKey.TOKENT_OUTOF_TIME,true);
            }
            LogUtils.e("=================================");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result ;
    }
}
