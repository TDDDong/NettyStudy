package com.dd.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        //将通道设置为非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)) {
            while(!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他操作...");
            }

            String str = "hello, dd~~";
            //wrap方法可以生成指定长度的byteBuffer
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
            //发送数据，将byteBuffer数据写入channel
            socketChannel.write(byteBuffer);
            System.in.read();
        }
    }
}
