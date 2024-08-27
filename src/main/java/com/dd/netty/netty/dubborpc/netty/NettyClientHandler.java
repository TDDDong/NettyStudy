package com.dd.netty.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context; //上下文
    private String result; //返回的结果
    private String param; //客户端调用方法时 传入的参数

    //与服务器的连接创建后 就会被调用 这个方法是第一个执行的  (1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx; //其他方法会使用到ctx
    }

    //收到服务器的数据后 调用方法 (4)
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify(); //唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * (3) -> (5) 等待唤醒后还要返回结果 所以调用两次
     * 被代理对象调用 发送数据给服务器 -> wait  ->  等待被唤醒（channelRead接收到服务器返回的数据后）  ->  返回结果
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return result;
    }

    //(2)
    void setParam(String param) {
        this.param = param;
    }
}
