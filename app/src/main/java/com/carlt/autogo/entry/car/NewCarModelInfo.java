package com.carlt.autogo.entry.car;

/**
 * Created by Marlon on 2018/11/21.
 * 车系车型 转成 adapter需要的 info
 */
public class NewCarModelInfo {
    public int id;                 //车系id
    public String title;           //车系名
    public int dataBeanId;         //车型id
    public String dataBeanTitle;   //车型名

    @Override
    public String toString() {
        return "NewCarModelInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dataBeanId=" + dataBeanId +
                ", dataBeanTitle='" + dataBeanTitle + '\'' +
                '}';
    }
}
