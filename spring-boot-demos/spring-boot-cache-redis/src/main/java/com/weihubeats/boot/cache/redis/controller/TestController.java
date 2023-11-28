package com.weihubeats.boot.cache.redis.controller;

import com.google.common.collect.Lists;
import com.weihubeats.boot.cache.redis.vo.StudentVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2023/11/28 15:02
 * @description:
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/test")
    @Cacheable(value = "student")
    public  List<StudentVO> test(String name) {
        return mockSelectSql();
    }

    private List<StudentVO> mockSelectSql() {
        log.info("开始查询数据");
        List<StudentVO> studentVOS = Lists.newArrayList(new StudentVO(1L, "小奏技术", 18), new StudentVO(2L, "小奏技术1", 19));
        return studentVOS;
    }
    
}
