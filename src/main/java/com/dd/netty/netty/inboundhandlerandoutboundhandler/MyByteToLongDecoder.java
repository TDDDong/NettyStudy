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
