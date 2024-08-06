package com.dd.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.每个请求都需要创建独立的线程，与对应的客户端进行数据读或者写操作
 * 2.当并发数较大时，需要创建大量的线程来处理连接，系统资源占用较大
 * 3.连接建立后，如果当前线程暂时没有数据可读，则线程就会阻塞在read操作上，造成线程资源浪费
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        //线程池机制
        //1.新建线程池
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //2.创建serverSocket接收请求
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while(true) {
            System.out.println("主线程id："+Thread.currentThread().getId()+"  主线程名字="+Thread.currentThread().getName());
            System.out.println("等待连接........");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            newCachedThreadPool.execute(() -> {
                handler(socket);
            });
        }
    }

    public static void handler(Socket socket) {
        try {
            System.out.println("线程id："+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                System.out.println("read......");
                System.out.println("线程id："+Thread.currentThread().getId()+"名字="+Thread.currentThread().getName());
                //将信息通过输入流写入到bytes数组中
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //读取0-read位置的字节并转化为字符串
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭与client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
