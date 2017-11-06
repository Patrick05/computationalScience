/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package de.bytedev.exercise2;

import de.bytedev.utility.World;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.EulerRichardson;
import org.opensourcephysics.numerics.ODE;

import java.awt.*;

/**
 * Pendulum models the dynamics of a pendulum.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class Pendulum implements Drawable, ODE {
    private double l;
    private double[] state = new double[3];

    private Color color = Color.RED;
    private int pixRadius = 6;
    private EulerRichardson odeSolver = new EulerRichardson(this);

    public Pendulum(double length) {
        this.l = length;
    }

    /**
     * Apply single time step.
     *
     * @param dt
     */
    public void update(double dt) {
        this.odeSolver.setStepSize(dt);
        this.odeSolver.step();
    }

    /**
     * Sets the state.
     *
     * @param theta The current angle of the pendelum.
     * @param omega The current angular frequency (dtheta/dt).
     */
    public void setState(double theta, double omega) {
        this.state[0] = theta;
        this.state[1] = omega;
        this.state[2] = 0;
    }

    public void setTime(double t) {
        this.state[2] = t;
    }

    public double[] getState() {
        return this.state;
    }

    /**
     * Returns the current theta value.
     *
     * @return the current theta value
     */
    public double getTheta() {
        return this.state[0];
    }

    /**
     * Returns the current omega value.
     *
     * @return the current omega value
     */
    public double getOmega() {
        return this.state[1];
    }

    /**
     * Returns the time value.
     *
     * @return the time
     */
    public double getTime() {
        return this.state[2];
    }

    /**
     * Returns the omega0Squared value.
     * It depends on the gravity-force and the length of the pendelum.
     *
     * @return omega0squared
     */
    private double getOmega0Squared() {
        return World.g/this.l;
    }


    public void getRate(double[] state, double[] rate) {
        rate[0] = state[1];
        rate[1] = -this.getOmega0Squared() * Math.sin(state[0]);
        rate[2] = 1;
    }

    public void draw(DrawingPanel drawingPanel, Graphics g) {
        int xpivot = drawingPanel.xToPix(0);
        int ypivot = drawingPanel.yToPix(0);
        int xpix = drawingPanel.xToPix(Math.sin(state[0]));
        int ypix = drawingPanel.yToPix(-Math.cos(state[0]));
        g.setColor(Color.black);
        g.drawLine(xpivot, ypivot, xpix, ypix);                               // the string
        g.setColor(color);
        g.fillOval(xpix - pixRadius, ypix - pixRadius, 2 * pixRadius, 2 * pixRadius); // bob
    }
}