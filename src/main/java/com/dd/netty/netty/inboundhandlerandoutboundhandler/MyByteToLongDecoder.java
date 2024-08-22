package com.dd.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder实际上实现了 ChannelInboundHandler
 * 因为对于入站的数据才需要解码
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收的数据 被调用多次 直到确定没有新的元素被添加list， 或者是ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空， 就会将list的内容传递给下一个channelinboundhandler处理
     *
     * @param channelHandlerContext 上下文对象
     * @param byteBuf               入站的ByteBuf
     * @param list                  List集合 将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyByteToLongDecoder decode被调用");
        //因为long 8个字节 需要判断有8个字节 才能读取一个long
        if (byteBuf.readableBytes() >= 8) {
            list.add(byteBuf.readLong());
        }
    }
}
