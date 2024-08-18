package com.dd.netty.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组 管理所有的channel
    //GlobalEventExecutor.INSTANCE 是全局的事件执行器 是一个单例
    private static ChannelGroup channelGroup =  new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * handlerAdded 表示连接建立 一旦连接 第一个被执行
     * 将当前channel 加入到 channelGroup
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户端加入群聊的消息推送给其他在线的客户端
        /**
         * 该方法会将channelGroup中所有的channel遍历 并发送消息
         * 我们不需要自己遍历
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天, 时间是：" + sdf.format(new Date()) +"\n");
        channelGroup.add(channel);
    }

    /**
     * 断开连接 将xx客户离开的消息推送给当前在线的用户
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了 时间是：" + sdf.format(new Date()) +"\n");
        //handlerRemoved一旦被触发 当前的channel会自动被移除出channelGroup分组 无需手动调用remove方法
        System.out.println("channelGroup size = " + channelGroup.size());
    }

    //表示channel 处于活动状态 提示服务端 xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    //表示channel 处于非活动状态 提示服务端 xx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前channel
        Channel channel = ctx.channel();
        //遍历channelGroup 根据不同的情况 回送不同的消息
        channelGroup.forEach(ch -> {
            if(channel != ch) { //不是当前的channel 转发消息
                ch.writeAndFlush("当前时间：" + sdf.format(new Date()) + " [客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            } else { //回显自己发送的消息给自己
                ch.writeAndFlush("当前时间：" + sdf.format(new Date()) + " [自己]发送了消息" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
