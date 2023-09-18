package io.github.weihubeats.gateway.pay.controller;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2023/9/18 11:08
 * @description:
 */
@RestController
@RequestMapping
public class PayController {

    @GetMapping("/pay")
    public String pay(Long timeout) throws Exception {
        System.out.printf("timeout: %s", timeout);
        if (Objects.nonNull(timeout)) {
            TimeUnit.SECONDS.sleep(timeout);
        }
        return "return pay";
    }
}
