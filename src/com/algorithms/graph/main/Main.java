package com.algorithms.graph.main;

import com.algorithms.graph.gui.GraphGUI;
import com.algorithms.graph.DirectedGraph;
import com.algorithms.graph.Graph;
import com.algorithms.graph.UndirectedGraph;
import com.algorithms.graph.structures.Array;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ПРИЛОЖЕНИЕ \"ГРАФ\" ===");
        System.out.println("Выберите режим работы:");
        System.out.println("1 - Графический интерфейс");
        System.out.println("2 - Консольный интерфейс");
        System.out.println("3 - Быстрый тест");
        System.out.print("Ваш выбор: ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                launchGraphicalInterface();
                break;
            case 2:
                launchConsoleInterface();
                break;
            case 3:
                runQuickTest();
                break;
            default:
                System.out.println("Неверный выбор, запускаю графический интерфейс...");
                launchGraphicalInterface();
        }
    }

    private static void launchGraphicalInterface() {
        System.out.println("Запуск графического интерфейса...");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GraphGUI graphGUI = new GraphGUI();
                graphGUI.setVisible(true);
            }
        });
    }

    private static void launchConsoleInterface() {
        System.out.println("\n=== КОНСОЛЬНЫЙ РЕЖИМ ===");

        Graph<String> graph = new DirectedGraph<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nДоступные операции:");
            System.out.println("1 - Добавить вершину");
            System.out.println("2 - Добавить ребро");
            System.out.println("3 - Удалить вершину");
            System.out.println("4 - Удалить ребро");
            System.out.println("5 - Показать смежные вершины");
            System.out.println("6 - Обход в глубину (DFS)");
            System.out.println("7 - Обход в ширину (BFS)");
            System.out.println("8 - Информация о графе");
            System.out.println("0 - Выход");
            System.out.print("Выберите операцию: ");

            int operation = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (operation) {
                case 1:
                    System.out.print("Введите вершину: ");
                    String vertex = scanner.nextLine();
                    graph.addVertex(vertex);
                    System.out.println("Вершина " + vertex + " добавлена");
                    break;

                case 2:
                    System.out.print("Введите вершину 'из': ");
                    String from = scanner.nextLine();
                    System.out.print("Введите вершину 'в': ");
                    String to = scanner.nextLine();
                    System.out.print("Введите вес: ");
                    int weight = scanner.nextInt();
                    graph.addEdge(from, to, weight);
                    System.out.println("Ребро " + from + " -> " + to + " добавлено");
                    break;

                case 3:
                    System.out.print("Введите вершину для удаления: ");
                    String vertexToRemove = scanner.nextLine();
                    graph.removeVertex(vertexToRemove);
                    System.out.println("Вершина " + vertexToRemove + " удалена");
                    break;

                case 4:
                    System.out.print("Введите вершину 'из': ");
                    String fromToRemove = scanner.nextLine();
                    System.out.print("Введите вершину 'в': ");
                    String toToRemove = scanner.nextLine();
                    graph.removeEdge(fromToRemove, toToRemove);
                    System.out.println("Ребро " + fromToRemove + " -> " + toToRemove + " удалено");
                    break;

                case 5:
                    System.out.print("Введите вершину: ");
                    String vertexForAdj = scanner.nextLine();
                    Array<String> adjacent = graph.getAdjacent(vertexForAdj);
                    System.out.println("Смежные вершины: " + adjacent.toString());
                    break;

                case 6:
                    System.out.print("Введите стартовую вершину для DFS: ");
                    String startDfs = scanner.nextLine();
                    System.out.print("DFS: ");
                    graph.dfs(startDfs);
                    break;

                case 7:
                    System.out.print("Введите стартовую вершину для BFS: ");
                    String startBfs = scanner.nextLine();
                    System.out.print("BFS: ");
                    graph.bfs(startBfs);
                    break;

                case 8:
                    System.out.println("Вершин: " + graph.getVertexCount());
                    System.out.println("Рёбер: " + graph.getEdgeCount());
                    System.out.println("Тип: " + (graph.isDirected() ? "Ориентированный" : "Неориентированный"));
                    break;

                case 0:
                    System.out.println("Выход из консольного режима");
                    return;

                default:
                    System.out.println("Неверная операция");
            }
        }
    }

    private static void runQuickTest() {
        System.out.println("\n=== БЫСТРЫЙ ТЕСТ ===");

        Graph<String> directedGraph = new DirectedGraph<>();
        directedGraph.addVertex("A");
        directedGraph.addVertex("B");
        directedGraph.addVertex("C");
        directedGraph.addEdge("A", "B", 1);
        directedGraph.addEdge("B", "C", 2);
        directedGraph.addEdge("A", "C", 3);

        System.out.println("Ориентированный граф:");
        System.out.println("Вершины: " + directedGraph.getVertexCount());
        System.out.println("Рёбра: " + directedGraph.getEdgeCount());
        System.out.print("DFS из A: ");
        directedGraph.dfs("A");
        System.out.print("BFS из A: ");
        directedGraph.bfs("A");

        Graph<String> undirectedGraph = new UndirectedGraph<>();
        undirectedGraph.addVertex("X");
        undirectedGraph.addVertex("Y");
        undirectedGraph.addVertex("Z");
        undirectedGraph.addEdge("X", "Y", 1);
        undirectedGraph.addEdge("Y", "Z", 2);

        System.out.println("\nНеориентированный граф:");
        System.out.println("Вершины: " + undirectedGraph.getVertexCount());
        System.out.println("Рёбра: " + undirectedGraph.getEdgeCount());
        System.out.print("DFS из X: ");
        undirectedGraph.dfs("X");

        System.out.println("\nТест завершен! Запускаю графический интерфейс...");
        launchGraphicalInterface();
    }
}