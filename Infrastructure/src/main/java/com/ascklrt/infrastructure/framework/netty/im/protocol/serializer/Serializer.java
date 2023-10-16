package com.ascklrt.infrastructure.framework.netty.im.protocol.serializer;

public interface Serializer {

   Serializer DEFAULT = new JsonSerializer();

    /**
     * 序列化算法
     */
    SerializerAlgorithm getSerializerAlgorithm();

    /**
     * Java对象转换为二进制数据
     */
    byte[] serializer(Object object);

    /**
     * 二进制数据转换为Java对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
