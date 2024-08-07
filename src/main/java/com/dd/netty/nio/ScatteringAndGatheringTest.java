package com.dd.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 可以想象数据量很大的情况下 写入多个buffer中（buffer数组） 所以时分散（Scattering）
 * 再从多个buffer中读取出来 所以叫聚合（Gathering）
 *
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入 【分散】
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到serverSocket 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建两个buffer数组 模拟数据量过大时写入不同buffer的场景
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接 并为客户端分配一个SocketChannel  （使用telnet 127.0.0.1 7000 模拟客户端连接）
        /**
         * 这里可以理解为 serverSocketChannel为客户端分配了sockertChannel 与之进行通讯 （双向通讯）
         * 客户端 -- socketChannel -- buffer[] -- serverSocketChannel -- 服务端
         */
        SocketChannel socketChannel = serverSocketChannel.accept();
        //定义最大读取长度
        int messageLength = 8;

        while (true) {
            int byteRead = 0;
            while(byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                //输出每个buffer中的对应信息
                Arrays.asList(byteBuffers).stream().map(buffer -> "position="+ buffer.position()+",limit="+buffer.limit())
                        .forEach(System.out::println);
            }

            //将buffer进行读写翻转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //清除并重置buffer属性
            Arrays.asList(byteBuffers).forEach(buffer ->{
                buffer.clear();
            });

            System.out.println("byteRead="+byteRead+" byteWrite="+byteWrite+" messagelength="+messageLength);
        }
    }
}
