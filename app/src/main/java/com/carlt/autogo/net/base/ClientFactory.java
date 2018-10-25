package com.carlt.autogo.net.base;

public class ClientFactory {

    public static <T> T def(final Class<T> service) {
        return BaseRestClient.getDefual().getService(service);
    }

    public static <T> T getUpdateImageService(Class<T> service) {
        return BaseRestClient.getUpdateImage().getService(service);
    }

    public static void defChangeUrl(int id) {
        BaseRestClient.getDefual().changeUri(id);
        BaseRestClient.getUpdateImage().changeUri(id);
    }

}
