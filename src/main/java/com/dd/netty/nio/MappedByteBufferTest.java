package com.dd.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明
 * 1. MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改  操作系统不需要拷贝一次！！！
 * 2. 注意参数配置中的第二个参数是指可以直接修改的起始位置 第三个参数是指可以修改的字节范围
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");

        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1： FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2： 0 ：可以直接修改的起始位置
         * 参数3： 5 ： 指的是映射到内存的大小，即将1.txt的多少个字节映射到内存中 实际可以修改的字节范围就是5个字节
         *
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        /**
         * 可映射的内存大小为5个字节 并不是指下标为5 故此处如果修改下标为5位置的数据就会抛异常
         * Exception in thread "main" java.lang.IndexOutOfBoundsException
         */
        //mappedByteBuffer.put(5, (byte) 'Y');

        randomAccessFile.close();
        System.out.println("修改成功~~");
    }
}
