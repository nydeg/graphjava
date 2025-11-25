package com.algorithms.graph.main;

import com.algorithms.graph.structures.Array;

public interface Graph<V> {
    void addVertex(V v);
    void addEdge(V from, V to, int weight);
    void removeVertex(V v);
    void removeEdge(V from, V to);
    Array<V> getAdjacent(V v);
    void dfs(V start);
    void bfs(V start);
    boolean containsVertex(V v);
    int getVertexCount();
    int getEdgeCount();
    boolean isDirected();
}