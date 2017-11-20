package de.bytedev.exercise3;

import de.bytedev.algorithms.ODE;
import de.bytedev.algorithms.RK45MultiStep;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.ODESolver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    private List<Planet> planets;
    private List<Planet> suns;

    private double t;

    private ODESolver odeSolver;

    /**
     * Initialize the planet group.
     * Creates a sun and two planets.
     */
    public PlanetGroup() {
        this.t = 0;

        this.planets = new ArrayList<>();
        this.suns = new ArrayList<>();
    }

    /**
     * Initialize ODE solver
     */
    public void initialize() {
        this.odeSolver = new RK45MultiStep(this);
    }

    /**
     * Returns the planet with the index.
     *
     * @param index
     * @return
     */
    public Planet getPlanet(int index) {
        return this.planets.get(index);
    }

    /**
     * Adds a planet with the given mass to the planet group.
     *
     * @param mass
     */
    public void addPlanet(double mass) {
        this.addPlanet(mass, Color.BLUE);
    }

    /**
     * Adds a planet with the given mass and color to the planet group.
     *
     * @param mass
     * @param color
     */
    public void addPlanet(double mass, Color color) {
        Planet p = new Planet(mass);
        p.setCircleColor(color);

        for(Planet o : this.planets) {
            o.addGravityObject(p);
            p.addGravityObject(o);
        }

        for(Planet o : this.suns) {
            p.addGravityObject(o);
        }

        this.planets.add(p);
    }

    /**
     * Adds a sun with the given mass to the planet group.
     *
     * @param mass
     */
    public void addSun(double mass) {
        Planet s = new Planet(mass);
        s.setCircleColor(Color.RED);

        for(Planet o : this.planets) {
            o.addGravityObject(s);
        }

        this.suns.add(s);
    }

    /**
     * Adds a sun with the given mass and position to the planet group.
     *
     * @param mass
     * @param pos
     */
    public void addSun(double mass, Vector2D pos) {
        Planet s = new Planet(mass);
        s.setPosition(pos);
        s.setCircleColor(Color.ORANGE);

        for(Planet o : this.planets) {
            o.addGravityObject(s);
        }

        this.suns.add(s);
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

    /**
     * Clear all drawables.
     */
    public void clearDrawables() {
        for (Planet s : this.suns) {
            s.getCircle().clear();
        }

        for(Planet p : this.planets) {
            p.getCircle().clear();
        }
    }

    @Override
    public void setState(double[] state) {

        for (int i = 0; i < this.planets.size(); i++) {
            Planet p = this.planets.get(i);
            int j = 4*i;

            p.setPosition( state[j], state[j+2] );
            p.setVelocity( state[j+1], state[j+3] );
        }

        this.t = state[4 * this.planets.size()];
    }

    @Override
    public double[] getState() {
        double[] state = new double[4 * this.planets.size() + 1];

        for (int i = 0; i < this.planets.size(); i++) {
            Planet p = this.planets.get(i);
            int j = 4*i;

            state[j] = p.getPosition().getX();
            state[j+1] = p.getVelocity().getX();
            state[j+2] = p.getPosition().getY();
            state[j+3] = p.getVelocity().getY();
        }

        state[4 * this.planets.size()] = this.t;

        return state;
    }

    @Override
    public void getRate(double[] state, double[] rate) {

        for (int i = 0; i < this.planets.size(); i++) {
            Planet p = this.planets.get(i);
            Vector2D a = p.calcAccelerationByGravity();
            int j = 4*i;

            rate[j] = p.getVelocity().getX();
            rate[j+1] = a.getX();
            rate[j+2] = p.getVelocity().getY();
            rate[j+3] = a.getY();
        }

        state[4 * this.planets.size()] = 1;
    }

    public double getTime() {
        System.out.println(this.t);
        return this.t;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        for (Planet s : this.suns) {
            s.getCircle().draw(panel, g);
        }

        for(Planet p : this.planets) {
            p.getCircle().draw(panel, g);
        }
    }
}
