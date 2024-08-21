package com.dd.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        /**
         * 出站时是从尾开始执行 也就是先执行业务handler发送数据 再通过encoder对数据进行编码
         */
        //加入一个出站的handler 对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());
        //加入一个自定义的handler 处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
