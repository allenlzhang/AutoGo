package com.carlt.autogo.entry.car;

/**
 * Created by Marlon on 2018/12/7.
 */
public class SingletonCar {

    private static SingletonCar singletonCar = null;

    private SingletonCar (){}

    public static SingletonCar getInstance(){
        if (singletonCar == null){
            synchronized (SingletonCar.class){
                if (singletonCar == null) {
                    singletonCar = new SingletonCar();
                }
            }
        }
        return singletonCar;
    }

    private AuthCarInfo.MyCarBean myCarBean;    //当前车辆基本信息

    private boolean isBound;    //是否绑定车辆

    private int carTag;     //当前车辆标记

    public AuthCarInfo.MyCarBean getMyCarBean() {
        return myCarBean;
    }

    public void setMyCarBean(AuthCarInfo.MyCarBean myCarBean) {
        this.myCarBean = myCarBean;
    }

    public boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
    }

    public int getCarTag() {
        return carTag;
    }

    public void setCarTag(int carTag) {
        this.carTag = carTag;
    }

    public void initCar(){
        myCarBean = null;
        isBound = false;
        carTag = 0;
    }
}
