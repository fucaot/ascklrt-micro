package com.ascklrt.infrastructure.framework.netty.im.protocol.serializer;

import java.util.Arrays;
import java.util.Objects;

public enum SerializerAlgorithm {

    /**
     * JSON序列化标识
     */
    JSON((byte)1, new JsonSerializer());
    ;

    private final byte code;
    private final Serializer serializer;

    SerializerAlgorithm(byte code, Serializer serializer) {
        this.code = code;
        this.serializer = serializer;
    }

    public byte getCode() {
        return code;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public static SerializerAlgorithm byCode(byte code) {
        return Arrays.stream(SerializerAlgorithm.values())
                .filter(v -> Objects.equals(v.getCode(), code)).findFirst().get();
    }
}
