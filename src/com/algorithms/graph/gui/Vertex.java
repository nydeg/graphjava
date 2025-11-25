package com.algorithms.graph.gui;

import com.algorithms.graph.structures.Point;

public class Vertex {
    private String label;
    private Point position;
    private static final int RADIUS = 20;

    public Vertex(String label, int x, int y) {
        this.label = label;
        this.position = new Point(x, y);
    }

    public String getLabel() {
        return label;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public boolean contains(int x, int y) {
        double distance = Math.sqrt(
                Math.pow(x - position.x, 2) +
                        Math.pow(y - position.y, 2)
        );
        return distance <= RADIUS;
    }

    public int getRadius() {
        return RADIUS;
    }
}
