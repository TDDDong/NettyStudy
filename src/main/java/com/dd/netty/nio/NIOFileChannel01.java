package com.dd.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "hello,dd!";
        //声明一个文件输出流并指定输出位置
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\StudyProject\\NettyStudy\\file01.txt");

        //将输出流转换为FileChannel
        //FileChannel的真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //声明一个byteBuffer 用于接收str内容
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        //写入数据之后要读取一定要切换！！！！
        byteBuffer.flip();

        //将内容从byteBuffer中读入到channel中
        //站在channel的角度 是从byteBuffer中write到channel中的
        fileChannel.write(byteBuffer);

        //关闭输出流
        fileOutputStream.close();
    }
}
