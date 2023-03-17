package com.java.base.juc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author : wh
 * @date : 2023/3/17 10:12
 * @description:
 */
public class ConcurrentControlUtilsTest {

    @Test
    public void process() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ConcurrentControlUtils.process(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LocalDateTime now = LocalDateTime.now();
            System.out.println("发送消息成功 now time " + dateTimeFormatter.format(now));
        }, 10);
    }
}