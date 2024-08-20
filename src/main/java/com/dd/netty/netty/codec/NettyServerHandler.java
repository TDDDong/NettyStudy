package com.dd.netty.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1.我们自定义一个Handler 需要继承netty 规定好的某个HandlerAdapter（规范）
 * 2.这时我们自定义一个Handler， 才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据（这里我们可以读取客户端发送的消息）
    /**
     * @param ctx  ChannelHandlerContext ctx 上下文对象，含有管道pipeline 通道channel 地址
     * @param msg  Object msg                就是客户端发送的数据 默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取从客户端发送的StudentPojo.Student对象
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据 id=" + student.getId() + " 名字=" + student.getName());
    }

    //读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * writeAndFlush 是 write + flush
         * 将数据写入到缓存，并刷新
         * 一般讲， 我们对这个发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~o(=•ェ•=)m111", CharsetUtil.UTF_8));
    }

    //处理异常 一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
