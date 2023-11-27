package com.ascklrt.infrastructure.framework.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class ByteBufTest {

    public static void maina(String[] args) {
        // 使用默认分配器创建ByteBuf，初识容量为9，最大可扩展至容量为100
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("create", buffer);

        // write方法改变写指针，写完之后指针未到capacity的时候，buff仍然可写
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("write(1, 2, 3, 4)", buffer);

        // write方法改变写指针，写完之后指针未到capacity的时候，buff仍然可写，写完int类型之后，写指针增加4
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

        // write方法改变写指针，写完之后写指针等于capacity的时候，buffer不可写
        buffer.writeBytes(new byte[]{5});
        print("weiteByte(5)", buffer);


        // write方法改变写指针，写的时候发现不可写则开始扩容，扩容之后capacity随即改变
        buffer.writeBytes(new byte[]{6});
        print("writeByte(6)开始扩容", buffer);


        // get方法不改变读写指针
        System.out.println("buffer.getByte(3)" + buffer.getByte(0));
        System.out.println("buffer.getShort(3)" + buffer.getShort(1));
        System.out.println("buffer.getInt(3)" + buffer.getInt(0));
        print("get()", buffer);

        // set方法不改变写指针
        buffer.setByte(buffer.readableBytes() + 1, 0);
        print("setByte()", buffer);
    }

    private static void print(String action, ByteBuf buffer) {
        System.out.println("——————————after " + action + "——————————");
        System.out.println("buffer.capacity() : " + buffer.capacity());
        System.out.println("buffer.maxCapacity() : " + buffer.maxCapacity());
        System.out.println("buffer.readerIndex() : " + buffer.readerIndex());
        System.out.println("buffer.readableBytes() : " + buffer.readableBytes());
        System.out.println("buffer.isReadable() : " + buffer.isReadable());
        System.out.println("buffer.writerIndex() : " + buffer.writerIndex());
        System.out.println("buffer.writableBytes() : " + buffer.writableBytes());
        System.out.println("buffer.isWritable() : " + buffer.isWritable());
        System.out.println("buffer.maxWritableBytes() : " + buffer.maxWritableBytes());
        System.out.println("");
    }
}
