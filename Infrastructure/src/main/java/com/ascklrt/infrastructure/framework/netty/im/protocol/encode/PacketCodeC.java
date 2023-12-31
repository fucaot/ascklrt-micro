package com.ascklrt.infrastructure.framework.netty.im.protocol.encode;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import com.ascklrt.infrastructure.framework.netty.im.protocol.serializer.Serializer;
import com.ascklrt.infrastructure.framework.netty.im.protocol.serializer.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;

    public static final PacketCodeC INSTANCE = new PacketCodeC();

    public ByteBuf encode(Packet packet) {
        /**
         * 1. 创建ByteBuf对象，iobuffer，
         * I/O 缓冲区通常用于与操作系统或网络交互，以提高数据传输性能。
         * 这种缓冲区通常采用直接内存（off-heap），这有助于减少内存拷贝的开销，从而提高 I/O 操作的效率。
         */
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();

       return encode(buffer, packet);
    }

    public ByteBuf encode(ByteBuf buffer, Packet packet) {
        /**
         * 序列话对象
         */
        byte[] bytes = Serializer.DEFAULT.serializer(packet);

        buffer.writeInt(MAGIC_NUMBER);                                              // 第一部分魔数
        buffer.writeByte(packet.getVersion());                                      // 版本号
        buffer.writeByte(Serializer.DEFAULT.getSerializerAlgorithm().getCode());    // 序列化算法
        buffer.writeByte(packet.getCommand().getCode());                            // 指令
        buffer.writeInt(bytes.length);                                              // 数据长度
        buffer.writeBytes(bytes);                                                   // 数据
        return buffer;
    }

    public Packet decode(ByteBuf buffer) {
        int magicNumber = buffer.readInt();                                         // 第一部分魔数
        byte version = buffer.readByte();                                           // 版本号
        byte serializerAlgorithm = buffer.readByte();                               // 序列化算法
        byte command  = buffer.readByte();                                          // 指令
        int length = buffer.readInt();                                              // 数据长度
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        // 识别指令
        Class<? extends Packet> clz = Command.byCode(command).getClz();
        // 解码
        return SerializerAlgorithm.byCode(serializerAlgorithm).getSerializer().deserialize(clz, bytes);
    }
}
