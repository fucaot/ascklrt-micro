package com.ascklrt.infrastructure.framework.netty.im.server;

import com.ascklrt.infrastructure.framework.netty.im.server.user.Session;
import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Boolean> LOGIN =  AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION =  AttributeKey.newInstance("session");
}
