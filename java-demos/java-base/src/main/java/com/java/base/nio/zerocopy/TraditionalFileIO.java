package com.java.base.nio.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : wh
 * @date : 2023/9/14 16:34
 * @description:
 */
public class TraditionalFileIO {

    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        String sourceFile = currentDir + "/java-demos/java-base/src/main/resources/mapped.txt";
        String destinationFile = currentDir + "/java-demos/java-base/src/main/resources/mappedCopy.txt";

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
