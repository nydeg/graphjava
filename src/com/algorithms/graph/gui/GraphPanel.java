package com.algorithms.graph.gui;

import com.algorithms.graph.Graph;
import com.algorithms.graph.structures.Array;
import com.algorithms.graph.structures.HashMap;
import com.algorithms.graph.structures.Point;
import com.algorithms.graph.structures.HashSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GraphPanel extends JPanel {
    private Graph<String> graph;
    private HashMap<String, Vertex> vertices;
    private Vertex selectedVertex;
    private int dragOffsetX, dragOffsetY;
    private boolean isDirected;

    public GraphPanel(Graph<String> graph) {
        this.graph = graph;
        this.vertices = new HashMap<>();
        this.selectedVertex = null;
        this.isDirected = true;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));

        addMouseListeners();

        if (graph != null) {
            initializeVertices();
        }
    }

    public void setDirected(boolean directed) {
        this.isDirected = directed;
        repaint();
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Проверяем, кликнули ли на вершину
                if (vertices == null) return;

                Array<String> vertexLabels = vertices.keys();
                for (int i = 0; i < vertexLabels.size(); i++) {
                    String label = vertexLabels.get(i);
                    Vertex vertex = vertices.get(label);
                    if (vertex != null && vertex.contains(e.getX(), e.getY())) {
                        selectedVertex = vertex;
                        dragOffsetX = e.getX() - vertex.getPosition().x;
                        dragOffsetY = e.getY() - vertex.getPosition().y;
                        break;
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedVertex = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedVertex != null) {
                    selectedVertex.setPosition(
                            e.getX() - dragOffsetX,
                            e.getY() - dragOffsetY
                    );
                    repaint();
                }
            }
        });
    }

    private void initializeVertices() {
        if (graph == null) return;

        Array<String> vertexLabels = graph.getVertices();
        int centerX = 400;
        int centerY = 300;
        int radius = 200;

        for (int i = 0; i < vertexLabels.size(); i++) {
            String label = vertexLabels.get(i);
            double angle = 2 * Math.PI * i / vertexLabels.size();
            int x = (int)(centerX + radius * Math.cos(angle));
            int y = (int)(centerY + radius * Math.sin(angle));

            vertices.put(label, new Vertex(label, x, y));
        }
    }

    public void addVertex(String label) {
        if (vertices == null) {
            vertices = new HashMap<>();
        }

        int x = 100 + (int)(Math.random() * 600);
        int y = 100 + (int)(Math.random() * 400);
        vertices.put(label, new Vertex(label, x, y));
        repaint();
    }

    public void removeVertex(String label) {
        if (vertices != null) {
            vertices.remove(label);
            repaint();
        }
    }

    public void updateGraph(Graph<String> newGraph) {
        this.graph = newGraph;

        if (newGraph == null) return;
        Array<String> newVertices = newGraph.getVertices();
        HashMap<String, Vertex> updatedVertices = new HashMap<>();

        for (int i = 0; i < newVertices.size(); i++) {
            String label = newVertices.get(i);
            Vertex existingVertex = vertices != null ? vertices.get(label) : null;
            if (existingVertex != null) {
                updatedVertices.put(label, existingVertex);
            } else {
                // Новая вершина - размещаем случайно
                int x = 100 + (int)(Math.random() * 600);
                int y = 100 + (int)(Math.random() * 400);
                updatedVertices.put(label, new Vertex(label, x, y));
            }
        }

        vertices = updatedVertices;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null || vertices == null) {
            g.setColor(Color.BLACK);
            g.drawString("Граф не инициализирован", 10, 20);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем рёбра
        drawEdges(g2d);

        // Рисуем вершины
        drawVertices(g2d);
    }

    private void drawEdges(Graphics2D g2d) {
        Array<String> vertexLabels = vertices.keys();

        HashSet<String> drawnEdges = new HashSet<>();

        for (int i = 0; i < vertexLabels.size(); i++) {
            String fromLabel = vertexLabels.get(i);
            Vertex fromVertex = vertices.get(fromLabel);

            Array<String> adjacent = graph.getAdjacent(fromLabel);
            for (int j = 0; j < adjacent.size(); j++) {
                String toLabel = adjacent.get(j);
                Vertex toVertex = vertices.get(toLabel);

                if (fromVertex != null && toVertex != null) {
                    Point from = fromVertex.getPosition();
                    Point to = toVertex.getPosition();

                    // Создаем ключ для ребра (для избежания дублирования в неориентированном графе)
                    String edgeKey = isDirected ?
                            fromLabel + "->" + toLabel :
                            fromLabel.compareTo(toLabel) < 0 ? fromLabel + "-" + toLabel : toLabel + "-" + fromLabel;

                    if (fromLabel.equals(toLabel)) {
                        drawLoop(g2d, fromVertex, fromLabel);
                    } else {
                        g2d.setColor(Color.GRAY);
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawLine(from.x, from.y, to.x, to.y);

                        Integer weight = graph.getEdgeWeight(fromLabel, toLabel);

                        if ((isDirected || !drawnEdges.contains(edgeKey)) && weight != null) {
                            drawEdgeWeight(g2d, from, to, weight);
                            drawnEdges.add(edgeKey);
                        }

                        if (isDirected) {
                            drawArrow(g2d, from, to);
                        }
                    }
                }
            }
        }
    }

    private void drawEdgeWeight(Graphics2D g2d, Point from, Point to, int weight) {
        int midX = (from.x + to.x) / 2;
        int midY = (from.y + to.y) / 2;

        int offsetX = (to.y - from.y) / 10;
        int offsetY = (from.x - to.x) / 10;

        g2d.setColor(Color.BLUE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        g2d.setColor(new Color(255, 255, 255, 200));
        String weightText = String.valueOf(weight);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(weightText);
        int textHeight = fm.getHeight();
        g2d.fillRoundRect(midX + offsetX - textWidth/2 - 2, midY + offsetY - textHeight/2 - 2,
                textWidth + 4, textHeight + 4, 5, 5);

        // Рисуем текст веса
        g2d.setColor(Color.BLUE);
        g2d.drawString(weightText, midX + offsetX - textWidth/2, midY + offsetY + textHeight/4);
    }

    private void drawLoop(Graphics2D g2d, Vertex vertex, String vertexLabel) {
        Point center = vertex.getPosition();
        int radius = vertex.getRadius();

        int loopSize = 30;
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(center.x - radius, center.y - radius - loopSize,
                radius * 2, loopSize * 2);

        // Получаем реальный вес петли из графа
        Integer weight = graph.getEdgeWeight(vertexLabel, vertexLabel);

        // Рисуем вес петли
        if (weight != null) {
            g2d.setColor(Color.BLUE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String weightText = String.valueOf(weight);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(weightText);
            g2d.drawString(weightText, center.x - textWidth/2, center.y - radius - loopSize - 5);
        }

        // Для ориентированного графа рисуем стрелку на петле
        if (isDirected) {
            int arrowX = center.x;
            int arrowY = center.y - radius - loopSize + 5;
            g2d.fillPolygon(
                    new int[]{arrowX, arrowX - 5, arrowX + 5},
                    new int[]{arrowY, arrowY - 8, arrowY - 8},
                    3
            );
        }
    }

    private void drawArrow(Graphics2D g2d, Point from, Point to) {
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int arrowLength = 15;

        // Вычисляем точку на краю целевой вершины
        int endX = to.x - (int)(Math.cos(angle) * 20);
        int endY = to.y - (int)(Math.sin(angle) * 20);

        // Рисуем стрелку
        int x1 = endX - (int)(arrowLength * Math.cos(angle - Math.PI / 6));
        int y1 = endY - (int)(arrowLength * Math.sin(angle - Math.PI / 6));
        int x2 = endX - (int)(arrowLength * Math.cos(angle + Math.PI / 6));
        int y2 = endY - (int)(arrowLength * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(
                new int[]{endX, x1, x2},
                new int[]{endY, y1, y2},
                3
        );
    }

    private void drawVertices(Graphics2D g2d) {
        Array<String> vertexLabels = vertices.keys();

        for (int i = 0; i < vertexLabels.size(); i++) {
            String label = vertexLabels.get(i);
            Vertex vertex = vertices.get(label);
            if (vertex == null) continue;

            Point pos = vertex.getPosition();
            int radius = vertex.getRadius();

            // Рисуем круг вершины
            g2d.setColor(vertex == selectedVertex ? Color.YELLOW : Color.LIGHT_GRAY);
            g2d.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);

            // Рисуем обводку
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2);

            // Рисуем текст
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getHeight();
            g2d.drawString(label, pos.x - textWidth / 2, pos.y + textHeight / 4);
        }
    }
}