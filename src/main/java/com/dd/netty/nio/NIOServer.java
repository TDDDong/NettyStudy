package com.dd.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        //创建ServerScoketChannel  服务端创建一个channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建Selectors
        Selector selector = Selector.open();

        //绑定端口6666 在服务端进行监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将serverSocketChannel 注册到 selector 中 配置关心的事件OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        /**
         * selector.keys()            当前注册的所有keys
         * selector.selectedKeys()    selector监听到有事件发生的keys数量
         */
        System.out.println("注册后的selectionKeys数量：" + selector.keys().size());

        //循环等待客户端连接
        while(true) {
            //等待1s，如果没有事件发生，则返回
             if(selector.select(1000) == 0) {
                 System.out.println("服务器等待了1s，无连接");
                 continue;
             }

            /**
             *  如果返回的值>0,就获取到相关的selectionKey集合
             *     1.如果返回的值>0，表示已经获取到关注的事件（有可能是不同的事件）
             *     2.selector.selectedKeys() 返回关注事件的集合
             *     3.通过selectionkeys 反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectionKeys数量：" + selectionKeys.size());

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应处理
                if (key.isAcceptable()) { //如果是OP_ACCEPT，证明有新的客户端连接
                    /**
                     * 该客户端生成一个SocketChannel
                     * 第一次连接需要由serverSocketChannel 创建 socketChannel
                     *
                     *
                     * key.channel()                -->  sun.nio.ch.ServerSocketChannelImpl[/0:0:0:0:0:0:0:0:6666]
                     * serverSocketChannel.accept() -->  java.nio.channels.SocketChannel[connected local=/127.0.0.1:6666 remote=/127.0.0.1:65281]
                     */
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成一个socketChannel"+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将客户端的channel注册到selector中去（同时绑定一个buffer）
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 注册的selectionKeys数量：" + selector.keys().size());
                }

                if (key.isReadable()) {
                    //SocketChannel 继承 SelectableChannel
                    //通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端"+ new String(buffer.array()));
                }
                //手动从集合中移出当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
