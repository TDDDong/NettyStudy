package com.dd.netty.netty.websocket;

import com.dd.netty.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
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

                            //因为是基于http协议 使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写 添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 说明
                             * 1.http数据再传输过程中是分段 HttpObjectAggregator 可以将多个段聚合
                             * 2.这就是为什么 当浏览器发送大量数据时 就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明
                             * 1.对应websocket 它的数据是以 帧（frame） 形式传递
                             * 2.可以看到WebSocketFrame 下面有六个子类    TextWebSocketFrame是其中一个
                             * 3.浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                             * 4.WebSocketServerProtocolHandler 核心功能是将 http协议升级为ws协议 保持长连接
                             * 5. 101状态码 代表浏览器协议被切换为ws协议
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler 处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
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
