package com.carlt.autogo.net.base;

import com.carlt.autogo.net.base.DefulatClient;
import com.carlt.autogo.net.base.DefulatClient2;
import com.carlt.autogo.net.base.Iservice;
import com.carlt.autogo.net.base.BaseRestClient;

public class ClientFactory {

    public static <T> T def(final Class<T> service){
        return  BaseRestClient.getDefual().getService(service);
    }
    public static void defChangeUrl(int id){
        BaseRestClient.getDefual().changeUri(id);
    }

}
