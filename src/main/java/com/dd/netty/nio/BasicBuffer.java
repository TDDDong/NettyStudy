package com.dd.netty.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //即分配一个大小为5 存放int类型数据的buffer
        /**
         * 底层实际上是数组  final int[] hb;
         * 数据实际上存放在数组中
         */
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向里面存放数据
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i * 2);
        }

        /**
         * 需要读写切换！！！！！！！！！！！
         * public final Buffer flip() {
         *         limit = position;
         *         position = 0;
         *         mark = -1;
         *         return this;
         *     }
         */
        intBuffer.flip();
        //设置读取的下标
        intBuffer.position(1);
        //设置最大可读下标
        intBuffer.limit(3);

        //读取数据
        /**
         * public final boolean hasRemaining() {
         *         return position < limit;
         * }
         */
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
