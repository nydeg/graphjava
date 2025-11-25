package com.algorithms.graph;

import com.algorithms.graph.structures.*;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected final HashMap<V, Array<Edge<V>>> adjacencyList;
    protected final boolean directed;
    protected int edgeCount;

    public AbstractGraph(boolean directed) {
        this.adjacencyList = new HashMap<>();
        this.directed = directed;
        this.edgeCount = 0;
    }

    protected static class Edge<V> {
        V target;
        int weight;

        Edge(V target, int weight) {
            this.target = target;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge<?> edge = (Edge<?>) obj;
            return target.equals(edge.target);
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }
    }

    @Override
    public void addVertex(V v) {
        if (v == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (!adjacencyList.containsKey(v)) {
            adjacencyList.put(v, new Array<>());
        }
    }

    @Override
    public Array<V> getAdjacent(V v) {
        if (!adjacencyList.containsKey(v)) {
            throw new RuntimeException("Vertex " + v + " not found");
        }
        Array<Edge<V>> edges = adjacencyList.get(v);
        Array<V> adjacentVertices = new Array<>();
        for (int i = 0; i < edges.size(); i++) {
            adjacentVertices.add(edges.get(i).target);
        }
        return adjacentVertices;
    }

    @Override
    public void dfs(V start) {
        String result = dfsString(start);
        System.out.println(result);
    }

    @Override
    public String dfsString(V start) {
        if (!adjacencyList.containsKey(start)) {
            throw new RuntimeException("Start vertex " + start + " not found");
        }

        HashMap<V, Boolean> visited = new HashMap<>();
        Array<V> vertices = adjacencyList.keys();
        for (int i = 0; i < vertices.size(); i++) {
            visited.put(vertices.get(i), false);
        }

        StringBuilder result = new StringBuilder();
        Stack<V> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            V current = stack.pop();
            if (!visited.get(current)) {
                visited.put(current, true);
                result.append(current).append(" ");

                Array<Edge<V>> edges = adjacencyList.get(current);
                for (int i = edges.size() - 1; i >= 0; i--) {
                    V neighbor = edges.get(i).target;
                    if (!visited.get(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return result.toString().trim();
    }

    @Override
    public void bfs(V start) {
        String result = bfsString(start);
        System.out.println(result);
    }

    @Override
    public String bfsString(V start) {
        if (!adjacencyList.containsKey(start)) {
            throw new RuntimeException("Start vertex " + start + " not found");
        }

        HashMap<V, Boolean> visited = new HashMap<>();
        Array<V> vertices = adjacencyList.keys();
        for (int i = 0; i < vertices.size(); i++) {
            visited.put(vertices.get(i), false);
        }

        StringBuilder result = new StringBuilder();
        Queue<V> queue = new Queue<>();
        visited.put(start, true);
        queue.enqueue(start);

        while (!queue.isEmpty()) {
            V current = queue.dequeue();
            result.append(current).append(" ");

            Array<Edge<V>> edges = adjacencyList.get(current);
            for (int i = 0; i < edges.size(); i++) {
                V neighbor = edges.get(i).target;
                if (!visited.get(neighbor)) {
                    visited.put(neighbor, true);
                    queue.enqueue(neighbor);
                }
            }
        }
        return result.toString().trim();
    }

    @Override
    public boolean containsVertex(V v) {
        return adjacencyList.containsKey(v);
    }

    @Override
    public int getVertexCount() {
        return adjacencyList.size();
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public Array<V> getVertices() {
        return adjacencyList.keys();
    }

    @Override
    public Integer getEdgeWeight(V from, V to) {
        if (!adjacencyList.containsKey(from)) {
            return null;
        }

        Array<Edge<V>> edges = adjacencyList.get(from);
        for (int i = 0; i < edges.size(); i++) {
            Edge<V> edge = edges.get(i);
            if (edge.target.equals(to)) {
                return edge.weight;
            }
        }
        return null;
    }

    protected void removeEdgeFromVertex(V from, V to) {
        Array<Edge<V>> edges = adjacencyList.get(from);
        if (edges != null) {
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).target.equals(to)) {
                    edges.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public String bellmanFord(V start) {
        if (!adjacencyList.containsKey(start)) {
            return "Стартовая вершина " + start + " не найдена";
        }

        Array<EdgeTriple<V>> edges = getAllEdges();

        HashMap<V, Integer> distances = new HashMap<>();
        HashMap<V, V> previous = new HashMap<>();
        Array<V> vertices = adjacencyList.keys();

        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            distances.put(vertex, Integer.MAX_VALUE);
            previous.put(vertex, null);
        }
        distances.put(start, 0);

        // Основной цикл релаксации (|V| - 1 раз)
        for (int i = 0; i < vertices.size() - 1; i++) {
            boolean changed = false;

            // Релаксация всех рёбер
            for (int j = 0; j < edges.size(); j++) {
                EdgeTriple<V> edge = edges.get(j);
                V u = edge.from;
                V v = edge.to;
                int weight = edge.weight;

                if (distances.get(u) != Integer.MAX_VALUE &&
                        distances.get(u) + weight < distances.get(v)) {
                    distances.put(v, distances.get(u) + weight);
                    previous.put(v, u);
                    changed = true;
                }
            }

            if (!changed) {
                break;
            }
        }

        // Проверка на отрицательные циклы
        String negativeCycleCheck = checkNegativeCycles(edges, distances);
        if (negativeCycleCheck != null) {
            return negativeCycleCheck;
        }

        return buildBellmanFordResult(start, distances, previous);
    }

    // Вспомогательный класс для хранения информации о рёбрах
    private static class EdgeTriple<V> {
        V from;
        V to;
        int weight;

        EdgeTriple(V from, V to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private Array<EdgeTriple<V>> getAllEdges() {
        Array<EdgeTriple<V>> edges = new Array<>();
        Array<V> vertices = adjacencyList.keys();

        for (int i = 0; i < vertices.size(); i++) {
            V from = vertices.get(i);
            Array<Edge<V>> outgoingEdges = adjacencyList.get(from);

            for (int j = 0; j < outgoingEdges.size(); j++) {
                Edge<V> edge = outgoingEdges.get(j);
                edges.add(new EdgeTriple<>(from, edge.target, edge.weight));
            }
        }

        return edges;
    }

    // Проверка на отрицательные циклы
    private String checkNegativeCycles(Array<EdgeTriple<V>> edges, HashMap<V, Integer> distances) {
        for (int i = 0; i < edges.size(); i++) {
            EdgeTriple<V> edge = edges.get(i);
            V u = edge.from;
            V v = edge.to;
            int weight = edge.weight;

            if (distances.get(u) != Integer.MAX_VALUE &&
                    distances.get(u) + weight < distances.get(v)) {
                return "Обнаружен отрицательный цикл! Алгоритм не может найти корректные кратчайшие пути.";
            }
        }
        return null;
    }

    private String buildBellmanFordResult(V start, HashMap<V, Integer> distances, HashMap<V, V> previous) {
        StringBuilder result = new StringBuilder();
        result.append("=== АЛГОРИТМ БЕЛЛМАНА-ФОРДА ===\n");
        result.append("Стартовая вершина: ").append(start).append("\n\n");
        result.append("Кратчайшие расстояния:\n");

        Array<V> vertices = distances.keys();
        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            int distance = distances.get(vertex);

            if (distance == Integer.MAX_VALUE) {
                result.append(vertex).append(": недостижима\n");
            } else {
                result.append(vertex).append(": ").append(distance);

                // Восстанавливаем путь
                Array<V> path = reconstructPath(previous, start, vertex);
                if (path.size() > 1) {
                    result.append(" (путь: ").append(path.toString()).append(")");
                }
                result.append("\n");
            }
        }

        result.append("\nОсобенности алгоритма:\n");
        result.append("- Работает с отрицательными весами рёбер\n");
        result.append("- Обнаруживает отрицательные циклы\n");
        result.append("- Временная сложность: O(V*E)\n");

        return result.toString();
    }


    @Override
    public String dijkstra(V start) {
        if (!adjacencyList.containsKey(start)) {
            return "Стартовая вершина " + start + " не найдена";
        }

        // Проверяем наличие отрицательных весов
        if (hasNegativeWeights()) {
            return "Ошибка: алгоритм Дейкстры не работает с отрицательными весами";
        }

        HashMap<V, Integer> distances = new HashMap<>();
        HashMap<V, V> previous = new HashMap<>();
        HashMap<V, Boolean> visited = new HashMap<>();
        Array<V> vertices = adjacencyList.keys();

        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            distances.put(vertex, Integer.MAX_VALUE);
            visited.put(vertex, false);
            previous.put(vertex, null);
        }
        distances.put(start, 0);

        for (int i = 0; i < vertices.size(); i++) {
            V current = findMinDistanceVertex(distances, visited);
            if (current == null) {
                break;
            }

            visited.put(current, true);

            // Обновляем расстояния до всех соседей
            Array<V> neighbors = getAdjacent(current);
            for (int j = 0; j < neighbors.size(); j++) {
                V neighbor = neighbors.get(j);
                if (!visited.get(neighbor)) {
                    Integer weight = getEdgeWeight(current, neighbor);
                    if (weight != null) {
                        int newDistance = distances.get(current) + weight;
                        if (newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            previous.put(neighbor, current);
                        }
                    }
                }
            }
        }

        return buildDijkstraResult(start, distances, previous);
    }

    private boolean hasNegativeWeights() {
        Array<V> vertices = adjacencyList.keys();
        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            Array<Edge<V>> edges = adjacencyList.get(vertex);
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(j).weight < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private V findMinDistanceVertex(HashMap<V, Integer> distances, HashMap<V, Boolean> visited) {
        int minDistance = Integer.MAX_VALUE;
        V minVertex = null;

        Array<V> vertices = distances.keys();
        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            if (!visited.get(vertex) && distances.get(vertex) <= minDistance) {
                minDistance = distances.get(vertex);
                minVertex = vertex;
            }
        }

        return minVertex;
    }

    private String buildDijkstraResult(V start, HashMap<V, Integer> distances, HashMap<V, V> previous) {
        StringBuilder result = new StringBuilder();
        result.append("=== АЛГОРИТМ ДЕЙКСТРЫ ===\n");
        result.append("Стартовая вершина: ").append(start).append("\n\n");
        result.append("Кратчайшие расстояния:\n");

        Array<V> vertices = distances.keys();
        for (int i = 0; i < vertices.size(); i++) {
            V vertex = vertices.get(i);
            int distance = distances.get(vertex);

            if (distance == Integer.MAX_VALUE) {
                result.append(vertex).append(": недостижима\n");
            } else {
                result.append(vertex).append(": ").append(distance);

                // Восстанавливаем путь
                Array<V> path = reconstructPath(previous, start, vertex);
                if (path.size() > 1) {
                    result.append(" (путь: ").append(path.toString()).append(")");
                }
                result.append("\n");
            }
        }

        return result.toString();
    }

    private Array<V> reconstructPath(HashMap<V, V> previous, V start, V target) {
        Array<V> path = new Array<>();
        V current = target;

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        if (path.size() > 0 && path.get(0).equals(start)) {
            return path;
        } else {
            return new Array<>();
        }
    }
}