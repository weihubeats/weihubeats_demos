package com.weihubeats.boot.cache.redis.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : wh
 * @date : 2023/6/8 17:40
 * @description:
 */
@Data
@AllArgsConstructor
public class StudentVO implements Serializable {
    
    private Long id;
    
    private String name;
    
    private int age;

    public StudentVO() {
    }
}
