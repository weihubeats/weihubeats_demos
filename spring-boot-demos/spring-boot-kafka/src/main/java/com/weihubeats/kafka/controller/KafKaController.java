package com.weihubeats.kafka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2023/12/22 15:06
 * @description:
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class KafKaController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/sendMsg")
    public void sendMsg() {
        kafkaTemplate.send("xiaozou", "小奏技术");
    }
}
