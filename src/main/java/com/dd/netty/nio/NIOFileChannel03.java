package com.dd.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        //创建输入流 读取文件
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        //创建输出流 将文件拷贝到对应地址
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        //创建一个byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            //记得重置byteBuffer
            /**
             * public final Buffer clear() {
             *         position = 0;
             *         limit = capacity;
             *         mark = -1;
             *         return this;
             * }
             */
            byteBuffer.clear();
            //从channel01中将数据写入到buffer中
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {
                break;
            }
            //从buffer中读取数据
            /**
             * 要读取数据之前一定要记得转换读写模式！！！
             */
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }
        //关闭流
        fileInputStream.close();
        fileOutputStream.close();
    }
}
