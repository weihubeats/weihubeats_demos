package com.data.structure.stack;

import java.util.Arrays;

/**
 * @author : wh
 * @date : 2023/11/17 19:47
 * @description: 数组实现栈 todo 泛型+自动扩容
 */
public class ArrayStack<T> {

    /**
     * 数据
     */
    T[] elements;

    /**
     * 栈顶
     */
    int top;

    public ArrayStack(int capacity) {
        elements = (T[])new Object[capacity];
        this.top = -1;
    }

    public boolean push(T value) {
        if (top == elements.length - 1) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        elements[++top] = value;
        return true;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈空");

        }
        return elements[top--];

    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("栈空");
        }
        return elements[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    public static void main(String[] args) {
        ArrayStack arrayStack = new ArrayStack(5);
        arrayStack.push("1");
        arrayStack.push("2");
        arrayStack.push("3");
        arrayStack.push("4");
        arrayStack.push("5");

        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());
        System.out.println(arrayStack.pop());

    }

}
