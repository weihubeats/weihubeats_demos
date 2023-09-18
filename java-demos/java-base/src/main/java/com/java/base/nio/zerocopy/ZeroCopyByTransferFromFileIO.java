package com.java.base.nio.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author : wh
 * @date : 2023/9/14 16:39
 * @description:
 */
public class ZeroCopyByTransferFromFileIO {

    public static void main(String[] args) {
        String sourceFile = "path/to/source/file";
        String destinationFile = "path/to/destination/file";

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationFile);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel destinationChannel = fos.getChannel()) {

            long transferedBytes = destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            System.out.println("File copied successfully. Total bytes transferred: " + transferedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
