package de.bytedev.exercise3;

import de.bytedev.algorithms.ODE;
import de.bytedev.algorithms.RK45MultiStep;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.ODESolver;

import java.awt.*;

/**
 * This class holds the planets of the planetary system.
 */
public class PlanetGroup implements ODE, Drawable {

    public static final double GMsun = 4 * Math.PI * Math.PI;

    /*
     * The mass of the planets is given by their ratio related to the sun.
     */
    public static final double mRatio1 = 1.0E-3;
    public static final double mRatio2 = 4 * 1.0E-2;

    private Planet sun;
    private Planet sun2;
    private Planet p1;
    private Planet p2;

    private double t;

    private ODESolver odeSolver;

    /**
     * Initialize the planet group.
     * Creates a sun and two planets.
     */
    public PlanetGroup() {
        this.t = 0;

        this.sun = new Planet(1);
        this.sun2 = new Planet(1);
        this.sun2.setPosition(-6, 0);
        this.p1 = new Planet(PlanetGroup.mRatio1);
        this.p2 = new Planet(PlanetGroup.mRatio2);

        this.p1.addGravityObject(this.sun);
        this.p1.addGravityObject(this.sun2);
        this.p1.addGravityObject(this.p2);

        this.p2.addGravityObject(this.sun);
        this.p2.addGravityObject(this.sun2);
        this.p2.addGravityObject(this.p1);

        this.odeSolver = new RK45MultiStep(this);
    }

    /**
     * Updates the values.
     *
     * @param dt the time step
     */
    public void update(double dt) {
        this.odeSolver.setStepSize(dt);
        this.odeSolver.step();
    }

    public void clearDrawables() {
        this.sun.getCircle().clear();
        this.sun.getCircle().clear();
        this.p1.getCircle().clear();
        this.p2.getCircle().clear();
    }

    @Override
    public void setState(double[] state) {
        this.p1.setPosition( state[0], state[2] );
        this.p1.setVelocity( state[1], state[3] );

        this.p2.setPosition( state[4], state[6] );
        this.p2.setVelocity( state[5], state[7] );

        this.t = state[8];
    }

    @Override
    public double[] getState() {
        return new double[] {
                this.p1.getPosition().getX(),
                this.p1.getVelocity().getX(),
                this.p1.getPosition().getY(),
                this.p1.getVelocity().getY(),
                this.p2.getPosition().getX(),
                this.p2.getVelocity().getX(),
                this.p2.getPosition().getY(),
                this.p2.getVelocity().getY(),
                this.t
        };
    }

    @Override
    public void getRate(double[] state, double[] rate) {
        // state[]: x1, vx1, y1, vy1, x2, vx2, y2, vy2, t
        Vector2D p1a = this.p1.calcAccelerationByGravity();
        Vector2D p2a = this.p2.calcAccelerationByGravity();

        rate[0] = this.p1.getVelocity().getX();
        rate[1] = p1a.getX();
        rate[2] = this.p1.getVelocity().getY();
        rate[3] = p1a.getY();
        rate[4] = this.p2.getVelocity().getX();
        rate[5] = p2a.getX();
        rate[6] = this.p2.getVelocity().getY();
        rate[7] = p2a.getY();
        rate[8] = 1;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        this.sun.getCircle().draw(panel, g);
        this.sun2.getCircle().draw(panel, g);
        this.p1.getCircle().draw(panel, g);
        this.p2.getCircle().draw(panel, g);
    }
}
