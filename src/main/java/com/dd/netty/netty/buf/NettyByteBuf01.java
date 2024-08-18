package com.dd.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        /**
         * 创建一个ByteBuf
         * 说明
         * 1. 创建对象 该对象包含一个数组 是一个byte[10]
         * 2. 在netty 的buffer中 不需要使用flip 进行翻转
         *    底层维护了 readerIndex 和 writerIndex
         * 3. 通过readerIndex 和 writerIndex 和 capacity，将buffer分成三个区域
         *      0 -- readerIndex           已经读取的区域
         *      readerIndex -- writerIndex  可读的区域
         *      writerIndex -- capacity   可写的区域
         */
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        /**
         * public final int capacity() {
         *         return this.length;
         * }
         */
        System.out.println("capacity = " + buffer.capacity()); //10 初始化容量大小
        // 1.输出 使用指定下标输出 readIndex不会改变
        /*for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }*/

        // 2.使用readByte输出 readIndex会增加
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }

        System.out.println("执行完毕");
    }
}
