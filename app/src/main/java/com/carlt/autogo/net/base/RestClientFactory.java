package com.carlt.autogo.net.base;

import com.carlt.autogo.net.base.DefulatClient;
import com.carlt.autogo.net.base.DefulatClient2;
import com.carlt.autogo.net.base.Iservice;
import com.carlt.autogo.net.base.BaseRestClient;

public class RestClientFactory {

    public static Iservice creatDef(){
        return BaseRestClient.getDefual();

    }
    public static Iservice creat2(){
       return BaseRestClient.getDefual2();
    }
}
