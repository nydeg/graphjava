package com.algorithms.graph.structures;

public class Stack<T> {
    private final LinkedList<T> list;

    public Stack() {
        list = new LinkedList<>();
    }

    public void push(T element) {
        list.addFirst(element);
    }

    public T pop() {
        return list.removeFirst();
    }

    public T peek() {
        Array<T> array = list.toArray();
        return array.size() > 0 ? array.get(0) : null;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}