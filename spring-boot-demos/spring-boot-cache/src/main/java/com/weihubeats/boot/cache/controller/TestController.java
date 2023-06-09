package com.weihubeats.boot.cache.controller;

import com.google.common.collect.Lists;
import com.weihubeats.boot.cache.service.StudentService;
import com.weihubeats.boot.cache.vo.StudentVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2023/6/8 17:38
 * @description:
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final StudentService service;
    
    
    @GetMapping("/test")
    @CachePut(cacheNames = "student", key = "#name")
    public  List<StudentVO> test(String name) {
        return mockSelectSql();
    }

    @GetMapping("/test1")
    @CachePut(cacheNames = "student", key = "#name")
    public  List<StudentVO> test1(String name) {
        return service.mockSelectSql();
    }
    
    private List<StudentVO> mockSelectSql() {
        log.info("开始查询数据");
        List<StudentVO> studentVOS = Lists.newArrayList(new StudentVO(1L, "小奏技术", 18), new StudentVO(2L, "小奏技术1", 19));
        return studentVOS;
    }
}
