package com.dd.netty.netty.dubborpc.publicinterface;

//这个是接口 是服务提供方和服务消费方都需要的
public interface HelloService {
    String hello(String msg);
}
