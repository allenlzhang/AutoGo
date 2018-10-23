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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author wsq
 * @time 16:05  2018/10/22/022
 * @describe 对token处理工具
 */
public class TokenUtil {
    public static Boolean decodeToken(String toekn) {

        boolean isOut = false;
        String code ="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String temp = toekn.replace(".", "=");
        if(temp.split("=").length>=2){
            code = temp.split("=")[1];
        }

        byte[] bytes = Base64Utils.decode(code, 0);
        String result = new String(bytes);

        // 7 天的秒数
        int svenDay = 604800;
        //当前时间 秒数
        Long curr = System.currentTimeMillis() / 1000;

        try {
            JSONObject jsonObject = new JSONObject(result);
            result = jsonObject.get("exp").toString();
            Long d = curr - svenDay;
            Long pre = Long.valueOf(result) - d;

            if (pre >= 0) {
                isOut = false;

            } else {
                isOut = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isOut;
    }
}
