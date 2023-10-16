package com.ascklrt.infrastructure.framework.netty.im.protocol;

import com.ascklrt.infrastructure.framework.netty.im.protocol.command.Command;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.serializer.Serializer;
import com.ascklrt.infrastructure.framework.netty.im.protocol.serializer.SerializerAlgorithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PacketCodeC {

    private static final int MAGIC_NUMBER = 0x12345678;

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
        int magicNumber = buffer.readInt();
        byte version = buffer.readByte();
        byte serializerAlgorithm = buffer.readByte();
        byte command  = buffer.readByte();
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        // 识别指令
        Class<? extends Packet> clz = Command.byCode(command).getClz();
        // 解码
        return SerializerAlgorithm.byCode(serializerAlgorithm).getSerializer().deserialize(clz, bytes);
    }
}
