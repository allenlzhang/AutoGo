package com.carlt.autogo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.carlt.autogo.application.AutoGoApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharepUtil {
    public static SharedPreferences preferences = AutoGoApp.mAppContext.getSharedPreferences("autoGo", Context.MODE_PRIVATE);


    // SP 保存实体类
    public static <T> boolean putByBean(String key, T t) {

        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, ObjStr);
            editor.commit();
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }

    public static <T extends Object> T getBeanFromSp(@NonNull String keyNme) {
        byte[] bytes = Base64.decode(preferences.getString(keyNme, ""), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    public static void put(@NonNull final String key, final String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putBoolean(@NonNull String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static SharedPreferences getPreferences() {

        return preferences;
    }

    public static void cleanUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.remove("user");
        editor.commit();
    }

}
