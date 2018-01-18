package de.bytedev.exercise9;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.display.FunctionDrawer;
import org.opensourcephysics.numerics.Function;

import java.awt.*;

/**
 * OscillatorsMode models a chain of oscillators in a single mode.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class OscillatorsMode implements Drawable, Function {
    private static final double OMEGA_SQUARED = 1; // equals k/m
    private FunctionDrawer functionDrawer;         // draws the initial condition
    private double omega;                          // oscillation frequency of mode
    private double wavenumber;                     // wavenumber = 2*pi/wavelength
    private double amplitude;

    /**
     * Constructs OscillatorsMode with the given mode and number of particles.
     * <p>
     * The particle separation is one in this model.
     *
     * @param mode int
     * @param N    int
     */
    OscillatorsMode(int mode, int N) {
        this.amplitude = Math.sqrt(2.0 / (N + 1));
        this.omega = 2 * Math.sqrt(OMEGA_SQUARED) * Math.abs(Math.sin(mode * Math.PI / N / 2));
        this.wavenumber = Math.PI * mode / (N + 1);
        this.functionDrawer = new FunctionDrawer(this);
        this.functionDrawer.initialize(0, N + 1, 300, false); // draws the initial displacement
        this.functionDrawer.color = Color.LIGHT_GRAY;
    }

    /**
     * Evaluates the displacement for an oscillator at postion x
     *
     * @param x postion along chain
     * @return the displacement
     */
    public double evaluate(double x) {
        return this.amplitude * Math.sin(x * this.wavenumber);
    }

    /**
     * Draws the normal mode's initial condtion
     *
     * @param panel DrawingPanel
     * @param g     Graphics
     */
    public void draw(DrawingPanel panel, Graphics g) {
        functionDrawer.draw(panel, g);
    }
}