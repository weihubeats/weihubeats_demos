package com.weihubeats.netty.demo.buffer;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * @author : wh
 * @date : 2023/5/31 09:48
 * @description:
 */
public class BufferTests {
    
    @Test
    public void test() {
        //1. 创建一个capacity为10的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);

        System.out.println("----------初始化------------");
        printBufferInfo(buffer);

        //2. 向buffer中存放数据
        byte[] bytes = {1, 2, 3, 4, 5};
        buffer.put(bytes);

        System.out.println("----------存放数据------------");
        printBufferInfo(buffer);

        //3. 切换buffer到读模式
        buffer.flip();

        System.out.println("----------切换到读模式------------");
        printBufferInfo(buffer);

        //4. 读取数据
        byte[] readBytes = new byte[buffer.remaining()];
        buffer.get(readBytes, 0, buffer.remaining());

        System.out.println("----------读取数据------------");
        printBufferInfo(buffer);

        //5. 清空buffer
        buffer.clear();

        System.out.println("----------清空buffer------------");
        printBufferInfo(buffer);
    }

    public static void printBufferInfo(ByteBuffer buffer) {
        System.out.println("position: " + buffer.position());
        System.out.println("limit: " + buffer.limit());
        System.out.println("capacity: " + buffer.capacity());
        System.out.println();
    }
    
    
    
}
