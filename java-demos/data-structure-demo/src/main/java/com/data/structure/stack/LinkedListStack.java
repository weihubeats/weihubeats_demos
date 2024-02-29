package com.data.structure.stack;

/**
 * @author : wh
 * @date : 2024/2/29 10:50
 * @description:
 */
public class LinkedListStack<T> {

    private Node<T> head;

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        Node<T> topNode = head;
        head = head.next;
        return topNode.data;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public static void main(String[] args) {
        LinkedListStack<String> stack = new LinkedListStack<>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        stack.push("5");

        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }

}

class Node<T> {
    T data;

    Node<T> next;

    public Node(T data) {
        this.data = data;
    }

}
