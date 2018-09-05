package com.carlt.autogo.net.base;

public interface Iservice {
    <T> T getService(final Class<T> service);
}
