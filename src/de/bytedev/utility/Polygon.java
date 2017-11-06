package de.bytedev.utility;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private List<Vector2D> points;

    public Polygon(List<Vector2D> points) {
        this.points = points;
    }

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void appendPoint(Vector2D point) {
        this.points.add(point);
    }

    public void appendPoint(double x, double y) {
        this.appendPoint(new Vector2D(x, y));
    }

    public List<Vector2D> getPoints() {
        return this.points;
    }

    public double calcArea() {
        double area = 0;
        for(int i = 0; i < this.points.size(); i++) {
            int nextPoint = i + 1;
            if( nextPoint == this.points.size() ) {
                nextPoint = 0;
            }

            Vector2D p1 = this.points.get(i);
            Vector2D p2 = this.points.get(nextPoint);

            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();

            area += p1.getY() * dx + (dx * dy) / 2;
        }

        return area;
    }
}
