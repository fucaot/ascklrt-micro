package com.ascklrt.infrastructure.framework.netty.im.protocol.encode;

import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder，用于做解码转换为java对象的handler
 *
 * 通常情况下，无论在客户端还是在服务端，当我们收到数据后，首先要做的就是把二进制数据转换
 * 到Java对象，所以Netty很贴心地提供了一个父类，来专门做这个事情。我们看一下如何使用这个
 * 类来实现服务端的解码。
  */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}