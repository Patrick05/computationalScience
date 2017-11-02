package de.bytedev.exercise1;

import de.bytedev.utility.World;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.EulerRichardson;
import org.opensourcephysics.numerics.ODE;

import java.awt.*;


public class Projectile implements Drawable, ODE {

    // {x, vx, y, vy, t}
    private double[] state = new double[5];
    private double initialV;
    private double initialY;
    private double m;

    private EulerRichardson odeSolver = new EulerRichardson(this);

    private static final int PIX_RADIUS = 6;

    public Projectile(double initialX, double initialY, double initialAngle, double initialV, double m) {
        this.initialV = initialV;
        this.initialY = initialY;
        this.m = m;
        double vx = initialV * Math.cos(Math.toRadians(initialAngle));
        double vy = initialV * Math.sin(Math.toRadians(initialAngle));

        this.setState(initialX, vx, initialY, vy);
    }

    public void update(double dt) {
        // possible??
        odeSolver.setStepSize(dt);
        odeSolver.step();
    }

    /**
     * Sets the state.
     *
     * @param x
     * @param vx
     * @param y
     * @param vy
     */
    public void setState(double x, double vx, double y, double vy) {
        this.state[0] = x;
        this.state[1] = vx;
        this.state[2] = y;
        this.state[3] = vy;
        this.state[4] = 0;
    }

    public double getX() {
        return this.state[0];
    }

    public double getY() {
        return this.state[2];
    }

    /**
     * This method returns true, if the projectile hits the ground.
     * @return
     */
    public boolean hitsGround() {
        return this.getY() <= 0;
    }

    public double analyticRmax() {
        return (this.initialV * Math.sqrt( Math.pow(this.initialV,2) + 2 * World.g * this.initialY)) / World.g;
    }

    @Override
    public double[] getState() {
        return state;
    }

    @Override
    public void getRate(double[] state, double[] rate) {
        rate[0] = state[1]; // rate of change of x
        rate[1] = 0;        // rate of change of vx
        rate[2] = state[3]; // rate of change of y

        if(ProjectileApp.USE_DYNAMIC_GRAVITY) {
            rate[3] = -World.accelerationAt(this.state[2]); // rate of change of vy
        } else {
            rate[3] = -World.g;
        }

        rate[4] = 1;        // dt/dt = 1
    }

    @Override
    public void draw(DrawingPanel drawingPanel, Graphics g) {
        int xpix = drawingPanel.xToPix(state[0]);
        int ypix = drawingPanel.yToPix(state[2]);
        g.setColor(Color.red);
        g.fillOval(xpix-PIX_RADIUS, ypix-PIX_RADIUS, 2*PIX_RADIUS, 2*PIX_RADIUS);
        g.setColor(Color.green);
        int xmin = drawingPanel.xToPix(-100);
        int xmax = drawingPanel.xToPix(100);
        int y0 = drawingPanel.yToPix(0);
        g.drawLine(xmin, y0, xmax, y0); // draw a line to represent the ground
    }
}