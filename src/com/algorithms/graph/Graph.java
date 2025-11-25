package com.algorithms.graph;

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
    Array<V> getVertices();
    String dfsString(V start);
    String bfsString(V start);
    Integer getEdgeWeight(V from, V to);
    String dijkstra(V start);
    String bellmanFord(V start);
}