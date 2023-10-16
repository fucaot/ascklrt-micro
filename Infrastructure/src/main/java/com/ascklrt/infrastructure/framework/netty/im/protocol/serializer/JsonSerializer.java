package com.ascklrt.infrastructure.framework.netty.im.protocol.serializer;

import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

public class JsonSerializer implements Serializer{

    @Override
    public SerializerAlgorithm getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serializer(Object object) {
        return JSONUtil.toJsonStr(object).getBytes();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
