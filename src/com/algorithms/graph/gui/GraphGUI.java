package com.algorithms.graph.gui;

import com.algorithms.graph.DirectedGraph;
import com.algorithms.graph.Graph;
import com.algorithms.graph.UndirectedGraph;
import com.algorithms.graph.structures.Array;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphGUI extends JFrame {
    private Graph<String> graph;
    private GraphPanel graphPanel;
    private JComboBox<String> graphTypeComboBox;
    private JTextField vertexField;
    private JTextField fromField;
    private JTextField toField;
    private JTextField weightField;
    private JTextField startVertexField;
    private JTextArea outputArea;
    private JButton createGraphButton;
    private JButton addVertexButton;
    private JButton addEdgeButton;
    private JButton removeVertexButton;
    private JButton removeEdgeButton;
    private JButton dfsButton;
    private JButton bfsButton;
    private JButton dijkstraButton;
    private JButton bellmanFordButton;
    private JButton showAdjacentButton;
    private JButton showInfoButton;
    private JButton testGraphButton;

    public GraphGUI() {
        // Сначала создаем граф, потом инициализируем GUI
        graph = new DirectedGraph<>(); // по умолчанию
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Визуализатор графов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Панель управления слева
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.WEST);

        // Центральная панель для графа и консоли
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Графическая панель в центре
        graphPanel = new GraphPanel(graph);
        centerPanel.add(graphPanel, BorderLayout.CENTER);

        // Консоль сбоку (справа) вместо снизу
        JPanel consolePanel = createConsolePanel();
        centerPanel.add(consolePanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createConsolePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Консоль вывода"));
        panel.setPreferredSize(new Dimension(400, 0));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(380, 0));

        // Панель с кнопками для консоли
        JPanel consoleButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton clearConsoleButton = new JButton("Очистить консоль");
        clearConsoleButton.addActionListener(e -> outputArea.setText(""));
        consoleButtonsPanel.add(clearConsoleButton);

        panel.add(consoleButtonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Управление"));

        // Выбор типа графа
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));

        JPanel typeSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typeSelectionPanel.add(new JLabel("Тип графа:"));
        graphTypeComboBox = new JComboBox<>(new String[]{"Ориентированный", "Неориентированный"});
        graphTypeComboBox.setPreferredSize(new Dimension(150, 25));
        typeSelectionPanel.add(graphTypeComboBox);
        typePanel.add(typeSelectionPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createGraphButton = new JButton("Создать граф");
        createGraphButton.addActionListener(new CreateGraphListener());
        buttonPanel.add(createGraphButton);

        testGraphButton = new JButton("Тестовый граф");
        testGraphButton.addActionListener(new TestGraphListener());
        buttonPanel.add(testGraphButton);

        typePanel.add(buttonPanel);
        panel.add(typePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Работа с вершинами
        panel.add(createSectionPanel("Вершины"));

        JPanel vertexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        vertexPanel.add(new JLabel("Вершина:"));
        vertexField = new JTextField(8);
        vertexPanel.add(vertexField);

        addVertexButton = createButton("Добавить", new AddVertexListener());
        vertexPanel.add(addVertexButton);

        removeVertexButton = createButton("Удалить", new RemoveVertexListener());
        vertexPanel.add(removeVertexButton);

        showAdjacentButton = createButton("Смежные", new ShowAdjacentListener());
        vertexPanel.add(showAdjacentButton);

        panel.add(vertexPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Работа с рёбрами
        panel.add(createSectionPanel("Рёбра"));

        JPanel edgePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        edgePanel.add(new JLabel("Из:"));
        fromField = new JTextField(8);
        edgePanel.add(fromField);

        edgePanel.add(new JLabel("В:"));
        toField = new JTextField(8);
        edgePanel.add(toField);

        edgePanel.add(new JLabel("Вес:"));
        weightField = new JTextField(8);
        weightField.setText("1");
        edgePanel.add(weightField);

        panel.add(edgePanel);

        JPanel edgeButtonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        addEdgeButton = createButton("Добавить ребро", new AddEdgeListener());
        removeEdgeButton = createButton("Удалить ребро", new RemoveEdgeListener());

        edgeButtonPanel.add(addEdgeButton);
        edgeButtonPanel.add(removeEdgeButton);
        panel.add(edgeButtonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Алгоритмы - делаем кнопки компактными
        panel.add(createSectionPanel("Алгоритмы"));

        JPanel startVertexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        startVertexPanel.add(new JLabel("Стартовая вершина:"));
        startVertexField = new JTextField(8);
        startVertexPanel.add(startVertexField);
        panel.add(startVertexPanel);

        // Создаем компактные кнопки алгоритмов
        JPanel algoPanel = new JPanel(new GridLayout(3, 2, 3, 3)); // Уменьшаем отступы между кнопками

        // Создаем компактные кнопки с короткими названиями
        dfsButton = createCompactButton("DFS", new DFSListener());
        bfsButton = createCompactButton("BFS", new BFSListener());
        dijkstraButton = createCompactButton("Дейкстра", new DijkstraListener());
        bellmanFordButton = createCompactButton("Беллман", new BellmanFordListener()); // Сокращаем название
        showInfoButton = createCompactButton("Инфо", new ShowInfoListener());

        algoPanel.add(dfsButton);
        algoPanel.add(bfsButton);
        algoPanel.add(dijkstraButton);
        algoPanel.add(bellmanFordButton);
        algoPanel.add(showInfoButton);

        // Добавляем пустую ячейку для выравнивания
        JLabel emptyLabel = new JLabel();
        emptyLabel.setPreferredSize(new Dimension(10, 10));
        algoPanel.add(emptyLabel);

        panel.add(algoPanel);

        return panel;
    }

    private JButton createCompactButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setFont(new Font("Arial", Font.PLAIN, 10)); // Уменьшаем шрифт
        button.setMargin(new Insets(2, 4, 2, 4)); // Уменьшаем отступы внутри кнопки
        button.setPreferredSize(new Dimension(70, 25)); // Фиксируем размер кнопки
        return button;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html><b>" + title + "</b></html>"));
        return panel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        return button;
    }

    private void appendOutput(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void clearOutput() {
        outputArea.setText("");
    }

    private class CreateGraphListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int typeIndex = graphTypeComboBox.getSelectedIndex();
            if (typeIndex == 0) {
                graph = new DirectedGraph<>();
            } else {
                graph = new UndirectedGraph<>();
            }
            graphPanel.updateGraph(graph);
            graphPanel.setDirected(typeIndex == 0); // Сообщаем панели о типе графа
            clearOutput();
            appendOutput("Создан новый " + (typeIndex == 0 ? "ориентированный" : "неориентированный") + " граф");
        }
    }

    private class TestGraphListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearOutput();
            appendOutput("Загружаем тестовый граф...");

            int typeIndex = graphTypeComboBox.getSelectedIndex();
            if (typeIndex == 0) {
                graph = new DirectedGraph<>();
            } else {
                graph = new UndirectedGraph<>();
            }

            String[] testVertices = {"A", "B", "C", "D", "E"};
            for (String vertex : testVertices) {
                graph.addVertex(vertex);
                appendOutput("Добавлена вершина: " + vertex);
            }

            graph.addEdge("A", "B", 5);
            graph.addEdge("A", "C", 3);
            graph.addEdge("B", "D", 2);
            graph.addEdge("C", "D", 7);
            graph.addEdge("D", "E", 4);
            graph.addEdge("E", "A", 6);
            graph.addEdge("C", "C", 1);

            if (graph.isDirected()) {
                graph.addEdge("B", "A", 8);
                graph.addEdge("D", "B", 9);
            }

            appendOutput("Добавлены все рёбра тестового графа");
            appendOutput("Включая петлю на вершине C с весом 1");

            graphPanel.updateGraph(graph);
            graphPanel.setDirected(typeIndex == 0); // Сообщаем панели о типе графа

            appendOutput("Тестовый граф успешно загружен!");
            appendOutput("Вершин: " + graph.getVertexCount() + ", Рёбер: " + graph.getEdgeCount());
            appendOutput("Для тестирования Дейкстры используйте стартовую вершину A");
            appendOutput("Для тестирования Беллмана-Форда можно добавить рёбра с отрицательными весами");
        }
    }

    private class AddVertexListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = vertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите название вершины");
                return;
            }

            try {
                graph.addVertex(vertex);
                graphPanel.addVertex(vertex);
                appendOutput("Добавлена вершина: " + vertex);
                vertexField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class RemoveVertexListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = vertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите название вершины");
                return;
            }

            try {
                graph.removeVertex(vertex);
                graphPanel.removeVertex(vertex);
                appendOutput("Удалена вершина: " + vertex);
                vertexField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class ShowAdjacentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = vertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите название вершины");
                return;
            }

            try {
                Array<String> adjacent = graph.getAdjacent(vertex);
                appendOutput("Смежные с " + vertex + " вершины: " + adjacent.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class AddEdgeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            String weightText = weightField.getText().trim();

            if (from.isEmpty() || to.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите вершины 'Из' и 'В'");
                return;
            }

            try {
                int weight = Integer.parseInt(weightText);
                graph.addEdge(from, to, weight);
                graphPanel.repaint();

                if (from.equals(to)) {
                    appendOutput("Добавлена петля: " + from + " → " + to + " (вес: " + weight + ")");
                } else {
                    appendOutput("Добавлено ребро: " + from + " → " + to + " (вес: " + weight + ")");
                }

                fromField.setText("");
                toField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Вес должен быть числом");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class RemoveEdgeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();

            if (from.isEmpty() || to.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите вершины 'Из' и 'В'");
                return;
            }

            try {
                graph.removeEdge(from, to);
                graphPanel.repaint();

                if (from.equals(to)) {
                    appendOutput("Удалена петля: " + from + " → " + to);
                } else {
                    appendOutput("Удалено ребро: " + from + " → " + to);
                }

                fromField.setText("");
                toField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class DFSListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = startVertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите стартовую вершину");
                return;
            }

            try {
                appendOutput("DFS из " + vertex + ":");
                String dfsResult = graph.dfsString(vertex);
                appendOutput(dfsResult);
                appendOutput(""); // Пустая строка для разделения
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class BFSListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = startVertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите стартовую вершину");
                return;
            }

            try {
                appendOutput("BFS из " + vertex + ":");
                String bfsResult = graph.bfsString(vertex);
                appendOutput(bfsResult);
                appendOutput(""); // Пустая строка для разделения
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class DijkstraListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = startVertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите стартовую вершину");
                return;
            }

            try {
                appendOutput("Запуск алгоритма Дейкстры...");
                String dijkstraResult = graph.dijkstra(vertex);
                appendOutput(dijkstraResult);
                appendOutput(""); // Пустая строка для разделения
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class BellmanFordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String vertex = startVertexField.getText().trim();
            if (vertex.isEmpty()) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Введите стартовую вершину");
                return;
            }

            try {
                appendOutput("Запуск алгоритма Беллмана-Форда...");
                String bellmanFordResult = graph.bellmanFord(vertex);
                appendOutput(bellmanFordResult);
                appendOutput(""); // Пустая строка для разделения
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GraphGUI.this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private class ShowInfoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            appendOutput("=== Информация о графе ===");
            appendOutput("Тип: " + (graph.isDirected() ? "Ориентированный" : "Неориентированный"));
            appendOutput("Вершин: " + graph.getVertexCount());
            appendOutput("Рёбер: " + graph.getEdgeCount());
            appendOutput("=========================");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                GraphGUI gui = new GraphGUI();
//                gui.setVisible(true);
//            }
//        });
//    }
}