package com.dd.netty.nio;

import java.nio.ByteBuffer;

/**
 * 用于测试 放入buffer 和 取出buffer 顺序不一致可能导致的问题
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        //类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('栋');
        byteBuffer.putShort((short) 4);

        //翻转
        byteBuffer.flip();

        //必须按存入的类型依次读取
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        /**
         * 假设这里使用getLong()
         * 会抛出异常
         * Exception in thread "main" java.nio.BufferUnderflowException
         */
        //System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getShort());
    }
}
