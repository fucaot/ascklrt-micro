package com.ascklrt.infrastructure.framework.netty.im.server;

import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Boolean> LOGIN =  AttributeKey.newInstance("login");
}
