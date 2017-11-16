package de.bytedev.ui;

import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.Trail;

import java.awt.*;

/**
 * This class extends the Circle class of the OpenSourcePhysics library and
 * adds a trail which follows the circle.
 */
public class TrailedCircle extends Circle {

    private Trail trail;

    /**
     * Default constructor
     */
    public TrailedCircle() {
        super();
        this.trail = new Trail();
    }

    /**
     * Constructor, which sets the initial position.
     *
     * @param x
     * @param y
     */
    public TrailedCircle(double x, double y) {
        super(x, y);
        this.trail = new Trail();
    }

    /**
     * Draws the mass
     *
     * @param panel
     * @param g
     */
    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        trail.draw(panel, g);
        super.draw(panel, g);
    }

    /**
     * Clears the trail.
     */
    public void clear() {
        trail.clear();
    }

    /**
     * Sets the postion and adds to the trail.
     *
     * @param x
     * @param y
     */
    @Override
    public void setXY(double x, double y) {
        super.setXY(x, y);
        trail.addPoint(x, y);
    }
}
