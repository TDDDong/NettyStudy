package com.dd.netty.nio.groupchat;


import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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
                int count = selector.select();
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
                            //处理读 专门写一个方法
                            readData(key);
                        }

                        //删除当前的key，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待客户端连接....");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                listenChannel.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //处理读事件的方法
    public void readData(SelectionKey key) {
        //获取到关联的channel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int count = channel.read(byteBuffer);

            if (count > 0) {
                //把缓存区的数据转为字符串
                String msg = new String(byteBuffer.array());

                System.out.println("from 客户端：" + msg.trim());

                //向其他的客户端转发消息，专门写一个方法来处理
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    //转发消息给其他客户端
    public void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中.....");
        //遍历 所有注册到selector上的socketChannel，并排除channel
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入channel中
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
