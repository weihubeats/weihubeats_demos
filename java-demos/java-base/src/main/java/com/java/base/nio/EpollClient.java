package com.java.base.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2023/9/20 09:55
 * @description:
 */
public class EpollClient {

    private static final long SLEEP_PERIOD = 5;

    private final String host;

    private final int port;

    public EpollClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EpollClient("127.0.0.1", 9919).start();
    }

    public void start() throws Exception {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        out.println("test epoll");
        System.out.println("1. CLIENT CONNECTED AND WROTE MESSAGE");
        TimeUnit.SECONDS.sleep(SLEEP_PERIOD);

    }
}
