package com.dd.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));

        String fileName = "testZeroCopy.zip";
        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        //准备发送
        long startTime = System.currentTimeMillis();

        /**
         * 在linux下一个transferTo 方法就可以完成传输
         * 在windows下 一次调用transferTo 只能发送8m，就需要分段传输文件，而且要记录传输时的位置
         * transferTo 底层使用到零拷贝
         *
         * Many operating systems can transfer bytes directly from the filesystem cache to the target channel without actually copying them.
         */
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数 = " + transferCount + " 耗时： " + (System.currentTimeMillis() - startTime));

        //关闭
        fileChannel.close();
    }
}
