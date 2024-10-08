package com.dd.netty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * TestServerInitializer 的顶层父类是ChannelHandler 其本质上也是个handler
 * 故pipeline底层维护了一个由ChannelHandlerContent组成的双向链表
 * 链表的头指针是TestServerInitializer  后面才指向addLast添加的handler
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入一个netty提供的HttpServerCodec codec => [coder - decoder]
        /**
         * HttpServerCodec 说明
         * 1.HttpServerCodec 是 netty提供的处理http的 编-解码器
         */
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2.添加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());

        System.out.println("ok~~");
    }
}
