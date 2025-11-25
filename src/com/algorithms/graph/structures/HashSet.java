package com.algorithms.graph.structures;

public class HashSet<T> {
    private final HashMap<T, Boolean> map;

    public HashSet() {
        this.map = new HashMap<>();
    }

    public void add(T element) {
        map.put(element, true);
    }

    public boolean contains(T element) {
        return map.get(element) != null;
    }

    public boolean remove(T element) {
        return map.remove(element) != null;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        Array<T> keys = map.keys();
        for (int i = 0; i < keys.size(); i++) {
            map.remove(keys.get(i));
        }
    }

    public Array<T> toArray() {
        return map.keys();
    }
}