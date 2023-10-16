package com.ascklrt.infrastructure.framework.netty.im.protocol.encode;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty提供了一个特殊的ChannelHandler来专门处理编码逻辑，不需要每一次将响应写到对
 * 端的时候都调用一次编码逻辑进行编码，也不需要自行创建ByteBuf。这个类被叫作
 * MessageToByteEncoder，从字面意思可以看出，它的功能就是将对象转换到二进制数据
 */
public class PacketEncode extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodeC.INSTANCE.encode(byteBuf, packet);
    }
}
