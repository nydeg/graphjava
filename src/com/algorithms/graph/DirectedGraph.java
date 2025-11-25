package com.algorithms.graph;

import com.algorithms.graph.structures.*;

public class DirectedGraph<V> extends AbstractGraph<V> {
    public DirectedGraph() {
        super(true);
    }

    @Override
    public void addEdge(V from, V to, int weight) {
        addVertex(from);
        addVertex(to);

        Array<Edge<V>> edges = adjacencyList.get(from);
        Edge<V> newEdge = new Edge<>(to, weight);

        // Remove existing edge if present
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).equals(newEdge)) {
                edges.remove(i);
                break;
            }
        }
        edges.add(newEdge);

        edgeCount++;
    }

    @Override
    public void removeVertex(V v) {
        if (!adjacencyList.containsKey(v)) {
            return;
        }

        // Remove all outgoing edges
        edgeCount -= adjacencyList.get(v).size();
        adjacencyList.remove(v);

        // Remove all incoming edges
        Array<V> vertices = adjacencyList.keys();
        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            Array<Edge<V>> edges = adjacencyList.get(vertex);
            int initialSize = edges.size();

            for (int j = edges.size() - 1; j >= 0; j--) {
                if (edges.get(j).target.equals(v)) {
                    edges.remove(j);
                }
            }
            edgeCount -= (initialSize - edges.size());
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        if (adjacencyList.containsKey(from)) {
            Array<Edge<V>> edges = adjacencyList.get(from);
            int initialSize = edges.size();

            for (int i = edges.size() - 1; i >= 0; i--) {
                if (edges.get(i).target.equals(to)) {
                    edges.remove(i);
                }
            }
            edgeCount -= (initialSize - edges.size());
        }
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }
}