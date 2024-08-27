package com.dd.netty.netty.dubborpc.provider;

import com.dd.netty.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    //当有消费方调用该方法时 返回一个结果
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" + msg);
        //根据msg 返回不同的结果
        if (msg != null) {
            return "你好客户端, 我已经收到你的消息 [" + msg + "]";
        } else {
            return "你好客户端, 我已经收到你的消息 ";
        }
    }
}
