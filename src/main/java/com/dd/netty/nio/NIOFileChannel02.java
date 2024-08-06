package com.dd.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        //创建文件
        File file = new File("F:\\StudyProject\\NettyStudy\\file01.txt");
        //获取文件的输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        //根据fileInputStream创建对应的 FileChannel -> 实际类型是FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将数据从channel中读到buffer中
        fileChannel.read(byteBuffer);

        //直接转换成String并输出
        System.out.println(new String(byteBuffer.array()));

        //关闭输入流
        fileInputStream.close();
    }
}
