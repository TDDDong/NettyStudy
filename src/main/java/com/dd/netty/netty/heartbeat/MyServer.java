package com.dd.netty.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class MyServer {
    public static void main(String[] args) throws Exception {
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //默认为 cpu核数 * 2 = 24

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入一个netty 提供的 IdleStateHandler
                            /**
                             * 说明
                             * 1.IdleStateHandler 是 netty提供的处理空闲状态的处理器
                             * 2.int readerIdleTimeSeconds ： 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                             * 3.int writerIdleTimeSeconds ： 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                             * 4.int allIdleTimeSeconds ： 表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                             * 5.当IdleStateHandler触发后， 就会传递给管道的下一个handler去处理
                             *   通过调用（触发）下一个handler的userEventTriggered，再该方法中去处理IdleStateEvent（读空闲，写空闲，读写空闲）
                             */
                            /**
                             * 这里需要心跳检测机制的原因是： handlerRemoved不一定能检测到客户端的离线操作 对服务器是无感的 所以需要服务器发送心跳检测包给客户端
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7));
                            //加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
