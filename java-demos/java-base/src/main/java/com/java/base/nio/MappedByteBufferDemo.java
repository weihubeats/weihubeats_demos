package com.java.base.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *@author : wh
 *@date : 2023/7/21 11:38
 *@description:
 */
public class MappedByteBufferDemo {


	public static void main(String[] args) {
		String currentDir = System.getProperty("user.dir");
		try (RandomAccessFile file = new RandomAccessFile(currentDir + "/java-demos/java-base/src/main/resources/mapped.txt", "r");
			 FileChannel channel = file.getChannel()) {

			long fileSize = channel.size();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

			// 读取文件数据
			byte[] data = new byte[(int) fileSize];
			buffer.get(data);

			// 处理文件数据
			String content = new String(data);
			System.out.println(content);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
