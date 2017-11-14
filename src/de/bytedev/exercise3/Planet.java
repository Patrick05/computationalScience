package de.bytedev.exercise3;

import de.bytedev.utility.Vector2D;
import de.bytedev.utility.World;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.ODESolver;
import org.opensourcephysics.numerics.RK45MultiStep;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Planet implements Drawable {
    private Vector2D position;
    private Vector2D velocity;
    private double GM;

    private Mass mass = new Mass();

    private List<Planet> others;

    public Planet(double GM) {
        this.GM = GM;
        this.others = new ArrayList<>();

        this.position = new Vector2D();
        this.velocity = new Vector2D();
    }

    public void setState(double x, double vx, double y, double vy, double t) {
        this.setPosition(x, y);
        this.setVelocity(vx, vy);

        this.mass.setXY( this.getPosition().getX(), this.getPosition().getY() );
    }

    public void setPosition(double x, double y) {
        this.position = new Vector2D(x, y);
    }

    public void setVelocity(double x, double y) {
        this.velocity = new Vector2D(x, y);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    public double getGM() {
        return this.GM;
    }


    public void addOtherPlanet(Planet o) {
        this.others.add(o);
    }

    public Vector2D calcAccelerationBySun() {
        Vector2D r = this.getPosition();
        r.mul(- World.GMsun / Math.pow(r.getLength(), 3) );
        return r;
    }

    public Vector2D calcAccelerationByPlanet(int index) {
        Planet other = this.others.get(index);

        Vector2D r = Vector2D.sub( this.getPosition(), other.getPosition() );
        r.mul( - this.getGM() / Math.pow(r.getLength(), 3) );
        return r;
    }

    public Vector2D calcAcceleration() {
        Vector2D a = new Vector2D();

        if(Planet2App.SUN_GRAV_ENABLED) {
            a.add( this.calcAccelerationBySun() );
        }

        if(Planet2App.PLANET_GRAV_ENABLED) {
            for(int i = 0; i < this.others.size(); i++ ) {
                a.add(this.calcAccelerationByPlanet(i));
            }
        }

        return a;
    }

    public void draw(DrawingPanel panel, Graphics g) {
        this.mass.draw(panel, g);
    }

    /**
     * Class Mass
     */
    class Mass extends Circle {
        Trail trail = new Trail();

        /**
         * Draws the mass
         *
         * @param panel
         * @param g
         */
        public void draw(DrawingPanel panel, Graphics g) {
            trail.draw(panel, g);
            super.draw(panel, g);
        }

        /**
         * Clears the trail.
         */
        void clear() {
            trail.clear();
        }

        /**
         * Sets the postion and adds to the trail.
         *
         * @param x
         * @param y
         */
        public void setXY(double x, double y) {
            super.setXY(x, y);
            trail.addPoint(x, y);
        }
    }
}
