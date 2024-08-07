package com.dd.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 用于使用transferFrom方法
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {

        //创建文件的输入流和输出流
        FileInputStream fileInputStream = new FileInputStream("F:\\StudyProject\\NettyStudy\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\StudyProject\\NettyStudy\\a1.jpg");

        //创建对应的channel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferFrom方法进行复制
        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
