package com.java.base.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2023/9/20 09:58
 * @description:
 */
public class EpollServer {

    private static final int BUFFER_SIZE = 8192;

    private static final long SLEEP_PERIOD = 5L; // 5 seconds

    private final int port;

    public EpollServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        new EpollServer(9919).start();

        Thread t = Thread.currentThread();
        
    }

    public void start() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(port));
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        SocketChannel clientChannel = null;

        System.out.println("0. SERVER STARTED TO LISTEN");
        
        while (true) {
            int select = selector.select();
            if (select == 0) {
                System.out.println("select wakes up with zero");
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                int ops = selectionKey.interestOps();

                try {
                    // process new connection
                    if ((ops & SelectionKey.OP_ACCEPT) != 0) {
                        clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);

                        // register channel to selector
                        clientChannel.register(selector, SelectionKey.OP_READ, null);
                        System.out.println("2. SERVER ACCEPTED AND REGISTER READ OP : client - " + clientChannel.socket().getInetAddress());
                    }

                    if ((ops & SelectionKey.OP_READ) != 0) {
                        // read client message
                        System.out.println("3. SERVER READ DATA FROM client - " + clientChannel.socket().getInetAddress());
                        readClient((SocketChannel) selectionKey.channel(), buffer);

                        // deregister OP_READ
                        System.out.println("PREV SET : " + selectionKey.interestOps());
                        selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_READ);
                        System.out.println("NEW SET : " + selectionKey.interestOps());

                        TimeUnit.SECONDS.sleep(SLEEP_PERIOD);
                        new WriterThread(clientChannel).start();
                    }

                } finally {
                    // remove from selected key set
                    iterator.remove();
                }

            }

        }

    }

    public void readClient(SocketChannel channel, ByteBuffer buffer) throws IOException {
        try {
            buffer.clear();

            int nRead = channel.read(buffer);

            if (nRead < 0) {
                channel.close();
                return;
            }

            if (buffer.position() != 0) {
                int size = buffer.position();
                buffer.flip();
                byte[] bytes = new byte[size];
                buffer.get(bytes);
                System.out.println("RECVED : " + new String(bytes));
            }
        } catch (IOException e) {
            System.err.println("IO Error : " + e.getMessage());
            channel.close();
        }
    }

    static class WriterThread extends Thread {
        private final SocketChannel clientChannel;

        public WriterThread(SocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        public void run() {
            try {
                writeClient(clientChannel);
                System.out.println("5. SERVER WRITE DATA TO client - " + clientChannel.socket().getInetAddress());
            } catch (IOException e) {
                System.err.println("5. SERVER WRITE DATA FAILED : " + e);
            }
        }

        public void writeClient(SocketChannel channel) throws IOException {
            try {
                ByteBuffer buffer = ByteBuffer.wrap("zwxydfdssdfsd".getBytes());
                int total = buffer.limit();

                int totalWrote = 0;
                int nWrote = 0;

                while ((nWrote = channel.write(buffer)) >= 0) {
                    totalWrote += nWrote;
                    if (totalWrote == total) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("IO Error : " + e.getMessage());
                channel.close();
            }
        }

    }

}
