package com.data.structure.stack;

/**
 * @author : wh
 * @date : 2023/11/17 19:47
 * @description: 数组实现栈
 */
public class ArrayStack {

    /**
     * 数据
     */
    int[] stack;

    /**
     * 栈容量
     */
    int maxSize;

    /**
     * 栈顶
     */
    int top;

    /**
     * 栈是否已满
     * @return
     */
    public boolean isFull() {
        return stack.length == maxSize;
    }
    
}
