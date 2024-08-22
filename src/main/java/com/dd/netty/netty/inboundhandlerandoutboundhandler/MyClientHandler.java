package com.dd.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("服务器ip=" + channelHandlerContext.channel().remoteAddress());
        System.out.println("收到的服务器消息=" + aLong);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);//发送的是一个long

        /**
         * 分析
         * 1."abcdabcdabcdabcd"是16个字节
         * 2.该处理器的前一个handler 是 MyLongTpByteEncoder （出站时从后往前调用）
         * 3.MyLongTpByteEncoder 的父类是 MessageToByteEncoder
         * 4.父类 MessageToByteEncoder中的write代码
         *
         *  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
         *         ByteBuf buf = null;
         *
         *         try {
         *             if (this.acceptOutboundMessage(msg)) {  //在这里会判断当前的msg类型是不是应该处理的类型， 如果是的话就进行encode
         *                 I cast = msg;
         *                 buf = this.allocateBuffer(ctx, msg, this.preferDirect);
         *
         *                 try {
         *                     this.encode(ctx, cast, buf);
         *                 } finally {
         *                     ReferenceCountUtil.release(msg);
         *                 }
         *
         *                 if (buf.isReadable()) {
         *                     ctx.write(buf, promise);
         *                 } else {
         *                     buf.release();
         *                     ctx.write(Unpooled.EMPTY_BUFFER, promise);
         *                 }
         *
         *                 buf = null;
         *             } else {
         *                 ctx.write(msg, promise);  //如果不是要处理的类型  就跳过encode 直接发送
         *             }
         *         }
         */
        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
