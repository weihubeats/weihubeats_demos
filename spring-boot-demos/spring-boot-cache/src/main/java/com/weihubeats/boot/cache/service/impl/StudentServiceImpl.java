package com.weihubeats.boot.cache.service.impl;

import com.google.common.collect.Lists;
import com.weihubeats.boot.cache.service.StudentService;
import com.weihubeats.boot.cache.vo.StudentVO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : wh
 * @date : 2023/6/8 18:15
 * @description:
 */
@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Override
    public List<StudentVO> mockSelectSql() {
        log.info("开始查询数据");
        List<StudentVO> studentVOS = Lists.newArrayList(new StudentVO(1L, "小奏技术", 18), new StudentVO(2L, "小奏技术1", 19));
        return studentVOS;
    }
}
