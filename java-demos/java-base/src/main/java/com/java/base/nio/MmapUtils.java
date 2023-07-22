package com.java.base.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : wh
 * @date : 2023/7/22 08:32
 * @description:
 */
public class MmapUtils {

    public File commitLogFile;
    public MappedByteBuffer mappedByteBuffer;
    public int mappedSize = 0;
    public int writePos = 0;


    public MmapUtils(String commitLogPath, int mappedSize) {
        this.commitLogFile = new File(commitLogPath);
        if(!commitLogFile.exists()){
            try {
                commitLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.mappedSize = mappedSize;
        try {
            mappedByteBuffer = new RandomAccessFile(commitLogFile, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, mappedSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 往磁盘写数据
     *
     * @param content
     * @return
     */
    public int writeFile(String content) {
        mappedByteBuffer.put(content.getBytes());
        //强制刷盘
        mappedByteBuffer.force();
        writePos = mappedByteBuffer.position();
        return 1;
    }


    /**
     * 从磁盘中读取数据
     *
     * @param len
     * @return
     */
    public byte[] readContent(int len) {
        mappedByteBuffer.position(0);
        byte[] dest = new byte[len];
        int j = 0;
        for (int i = 0; i < mappedSize; i++) {
            byte b = mappedByteBuffer.get();
            if (b != 0) {
                dest[j++] = b;
            }
        }
        return dest;
    }

}
