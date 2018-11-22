package com.carlt.autogo.entry.car;

/**
 * Created by Marlon on 2018/11/21.
 * 车系车型 转成 adapter需要的 info
 */
public class NewCarModelInfo {
    private int id;                 //车系id
    private String title;           //车系名
    private int dataBeanId;         //车型id
    private String dataBeanTitle;   //车型名

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

    public int getDataBeanId() {
        return dataBeanId;
    }

    public void setDataBeanId(int dataBeanId) {
        this.dataBeanId = dataBeanId;
    }

    public String getDataBeanTitle() {
        return dataBeanTitle;
    }

    public void setDataBeanTitle(String dataBeanTitle) {
        this.dataBeanTitle = dataBeanTitle;
    }

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
