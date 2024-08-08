package com.dd.netty.nio.groupchat;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(6667));
            //设置为非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector中 监听的事件为OP_ACCEPT
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            //循环处理
            while(true) {
                int count = selector.select(2000);
                if (count > 0) { //有事件处理
                    //遍历获取selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        //取出selectionKey
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            //设置为非阻塞
                            sc.configureBlocking(false);
                            //将sc注册到selector中
                            sc.register(selector, SelectionKey.OP_READ);

                            System.out.println(sc.getRemoteAddress() + "上线 ");
                        }
                        if (key.isReadable()) { //通道发送read事件 即通道是可读的

                        }
                    }
                } else {
                    System.out.println("等待客户端连接....");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
    public static void main(String[] args) {

    }
}
