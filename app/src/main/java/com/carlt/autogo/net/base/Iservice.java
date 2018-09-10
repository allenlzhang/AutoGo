package com.carlt.autogo.net.base;

import java.nio.channels.Channel;

public interface Iservice {

    <T> T getService(final Class<T> service);

    void  changeUri(int id);
}
