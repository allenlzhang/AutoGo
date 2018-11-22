package com.carlt.autogo.entry.car;

import android.support.annotation.NonNull;

/**
 * Created by Marlon on 2018/11/20.
 * 品牌info
 */
public class BrandInfo implements Comparable<BrandInfo>{
    private int id = 1;
    private String title ;       //名称
    private String titlePy;     //拼音
    private String initial;     //首字母
    private String year;
    private String logoUrl;     //原始品牌logo url
    private String brandLogo;   //完整的logo url

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePy() {
        return titlePy;
    }

    public void setTitlePy(String titlePy) {
        this.titlePy = titlePy;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    @Override
    public int compareTo(@NonNull BrandInfo brandInfo) {
        return this.initial.compareTo(brandInfo.initial);
    }
}
