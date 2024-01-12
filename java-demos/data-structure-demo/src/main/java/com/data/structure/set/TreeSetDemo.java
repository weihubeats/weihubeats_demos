package com.data.structure.set;

import java.util.TreeSet;

/**
 * @author : wh
 * @date : 2023/12/1 09:53
 * @description:
 */
public class TreeSetDemo {

    public static void main(String[] args) {
        /**
         * 
         */
        TreeSet<Integer> values = new TreeSet<>();
        values.add(3);
        values.add(5);
        values.add(1);
        values.add(10);
        values.add(7);

        // 输出的是排序好的集合元素
        System.out.println(values);

        // 输出集合中第一个元素
        System.out.println(values.first()); // 输出 1

        // 集合中最后一个元素
        System.out.println(values.last()); // 输出 10

        // 输出小于5的子集 ( 不包含5 )
        System.out.println(values.headSet(5)); // 输出 [1, 3]

        // 输出大于5的子集，如果Set中有5，则包含5
        System.out.println(values.tailSet(5)); // 输出 [5, 7, 10]

        // 返回大于2小于8的子集
        System.out.println(values.subSet(2, 8)); // 输出 [3, 5, 7]

    }
}
