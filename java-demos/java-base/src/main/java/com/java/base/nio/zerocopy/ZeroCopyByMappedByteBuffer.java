package com.java.base.nio.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author : wh
 * @date : 2023/9/14 16:42
 * @description:
 */
public class ZeroCopyByMappedByteBuffer {

    public static void main(String[] args) {
        String sourceFile = "path/to/source/file";
        String destinationFile = "path/to/destination/file";

        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel(); 
             FileChannel destinationChannel = new FileOutputStream(destinationFile).getChannel()) {

            long fileSize = sourceChannel.size();
            MappedByteBuffer buffer = sourceChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            destinationChannel.write(buffer);

            System.out.println("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
