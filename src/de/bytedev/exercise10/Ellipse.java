package de.bytedev.exercise10;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

import java.awt.*;

public class Ellipse implements Drawable {

    private double a;
    private double b;

    public Ellipse(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public double getE() {
        return Math.sqrt( Math.pow(this.a, 2) - Math.pow(this.b, 2));
    }

    public Vector2D getF1() {
        return new Vector2D(-this.getE(), 0);
    }

    public Vector2D getF2() {
        return new Vector2D(this.getE(), 0);
    }

    public boolean intersects(Vector2D position) {
        return ( Vector2D.sub(position, this.getF1()).getLength() + Vector2D.sub(position, this.getF2()).getLength() < this.a*2 );
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {

        double lastX = -this.a;
        double lastY = 0;

        for(double x = -this.a; x < this.a; x += 0.1) {
            double y = (this.b / this.a) * Math.sqrt( Math.pow(this.a, 2) - Math.pow(x, 2));

            g.drawLine( panel.xToPix(lastX), panel.yToPix(lastY), panel.xToPix(x), panel.yToPix(y) );
            g.drawLine( panel.xToPix(lastX), panel.yToPix(-lastY), panel.xToPix(x), panel.yToPix(-y) );

            lastX = x;
            lastY = y;
        }
    }
}
