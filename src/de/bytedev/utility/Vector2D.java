package de.bytedev.utility;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0,0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
