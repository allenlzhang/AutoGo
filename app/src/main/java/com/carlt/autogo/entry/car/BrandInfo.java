package com.carlt.autogo.entry.car;

import android.support.annotation.NonNull;

import com.carlt.autogo.entry.user.BaseError;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marlon on 2018/11/20.
 * 品牌info
 */
public class BrandInfo implements Serializable{

    public List<BrandData> items;

    public int level;

    public BaseError err;

    public static class BrandData implements Comparable<BrandData>,Serializable{
        public int id = 1;
        public String title ;       //名称
        public String titlePy;     //拼音
        public String initial;     //首字母
        public String year;
        public String logoUrl;     //原始品牌logo url
        public String brandLogo;   //完整的logo url

        @Override
        public int compareTo(@NonNull BrandData brandData) {
            return this.initial.compareTo(brandData.initial);
        }
    }




}
