package com.java.base.juc;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

/**
 * @author : wh
 * @date : 2023/10/11 11:24
 * @description:
 */
public class ConcurrentLinkedHashMapDemo {

    public static void main(String[] args) {
        ConcurrentLinkedHashMap<Integer, Integer> map = new ConcurrentLinkedHashMap.Builder<Integer, Integer>()
                .maximumWeightedCapacity(3)
                .weigher(Weighers.singleton())
                .build();
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.forEach((k, v) -> System.out.println("k:" + k + " value:" + v));

        
    }
    
}
