package com.dd.netty.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 这里使用ReplayingDecoder来进行解码
 * 无需判断字节长度
 * 该方法已经帮我们判断了
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyMessageDecoder decode 被调用");
        //客户端发送的消息对象 通过客户端的编码器 先发送对象的长度length 在发送对象的内容content
        //所以这里可以读取对象的长度 创建对应长度的数组来接收消息内容
        int length = byteBuf.readInt();

        byte[] content = new byte[length];
        byteBuf.readBytes(content);

        //封装成 MessageProtocol 对象， 放入list中 传递给下一个handler进行业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);

        list.add(messageProtocol);
    }
}
