package com.dd.netty.netty.simple;

import io.netty.buffer.ByteBuf;
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
        /**
         * 比如这里有一个非常耗时的业务 -> 异步执行 -> 提交该channel对应的 NIOEventLoop 的taskQueue中
         */

        //解决方案1  用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~o(=•ェ•=)m222", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        /**
         * 这里再加入一个任务 等待20秒执行 实际执行效果是： 先等待上一个任务执行10秒结束后才开始执行当前任务 （因为只有一个主线程！！！）
         * 其中taskQueue的size为2 表示有两个任务在任务队列中
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~o(=•ェ•=)m333", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        //解决方案2 用户自定义的定时任务
        /**
         * 这个任务是存放在 NioEventLoop 的 scheduledTaskQueue中
         */
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~o(=•ェ•=)m444", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("go on ........");

        /*System.out.println("server ctx = " + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());*/
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
