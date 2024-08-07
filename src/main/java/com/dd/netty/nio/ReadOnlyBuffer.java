package com.dd.netty.nio;

import java.nio.ByteBuffer;

/**
 * 测试只读buffer
 * 往只读buffer中放数据会抛异常
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        //创建一个buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        //往buffer中放数据
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        //翻转buffer
        byteBuffer.flip();

        //将buffer设置为只读
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass()); //class java.nio.HeapByteBufferR

        //读取数据
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        /**
         * 对于只读buffer
         * 放入数据时会报错
         * Exception in thread "main" java.nio.ReadOnlyBufferException
         */
        readOnlyBuffer.put((byte) 100);
    }
}
