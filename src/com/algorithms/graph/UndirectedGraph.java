package com.algorithms.graph;

import com.algorithms.graph.structures.*;

public class UndirectedGraph<V> extends AbstractGraph<V> {
    public UndirectedGraph() {
        super(false);
    }

    @Override
    public void addEdge(V from, V to, int weight) {
        addVertex(from);
        addVertex(to);

        Array<Edge<V>> edgesFrom = adjacencyList.get(from);
        Array<Edge<V>> edgesTo = adjacencyList.get(to);

        Edge<V> edgeFromTo = new Edge<>(to, weight);
        Edge<V> edgeToFrom = new Edge<>(from, weight);

        // Remove existing edges if present
        removeEdgeFromList(edgesFrom, edgeFromTo);
        removeEdgeFromList(edgesTo, edgeToFrom);

        edgesFrom.add(edgeFromTo);
        edgesTo.add(edgeToFrom);

        edgeCount++;
    }

    @Override
    public void removeVertex(V v) {
        if (!adjacencyList.containsKey(v)) {
            return;
        }

        // Remove all edges connected to this vertex
        Array<Edge<V>> edges = adjacencyList.get(v);
        for (int i = 0; i < edges.size(); i++) {
            removeEdgeFromVertex(edges.get(i).target, v);
        }

        edgeCount -= edges.size();
        adjacencyList.remove(v);
    }

    @Override
    public void removeEdge(V from, V to) {
        if (adjacencyList.containsKey(from) && adjacencyList.containsKey(to)) {
            Array<Edge<V>> edgesFrom = adjacencyList.get(from);
            Array<Edge<V>> edgesTo = adjacencyList.get(to);

            int initialSizeFrom = edgesFrom.size();
            int initialSizeTo = edgesTo.size();

            removeEdgeFromList(edgesFrom, new Edge<>(to, 0));
            removeEdgeFromList(edgesTo, new Edge<>(from, 0));

            edgeCount -= ((initialSizeFrom - edgesFrom.size()) +
                    (initialSizeTo - edgesTo.size())) / 2;
        }
    }

    @Override
    public int getEdgeCount() {
        return edgeCount;
    }

    private void removeEdgeFromList(Array<Edge<V>> edges, Edge<V> edgeToRemove) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).equals(edgeToRemove)) {
                edges.remove(i);
                break;
            }
        }
    }
}