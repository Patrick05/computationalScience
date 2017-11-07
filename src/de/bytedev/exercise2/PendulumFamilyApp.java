package de.bytedev.exercise2;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.Polygon;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;

import java.util.Vector;

public class PendulumFamilyApp extends AbstractSimulation {
    enum Param {
        AMOUNT_PENDULUMS("Pendulum amount"),
        RAND_PARAMS("Random Params"),
        TIME_STEP("Time Step"),;

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("Time", "Theta", "Theta versus time");
    private ExtendedPlotFrame phaseFrame = new ExtendedPlotFrame("Omega", "Theta", "Omega vs. Theta");
    private Area area = new Area();

    private Pendulum[] pendulums;
    private double dt;

    /**
     * Initializes the simulation.
     */
    public void initialize() {
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());

        boolean randParams = this.control.getBoolean(Param.RAND_PARAMS.toString());

        int n = this.control.getInt(Param.AMOUNT_PENDULUMS.toString());
        this.pendulums = new Pendulum[n];

        for(int i = 0; i < n; i++) {
            Pendulum p = new Pendulum(4);

            if(randParams) {
                double theta = Math.toRadians(Math.random() * 10);
                double omega = Math.toRadians(Math.random() * 2);
                p.setState(theta, omega);
            } else {
                p.setState(
                        (i >= n/2) ? Math.toRadians(n - i) : Math.toRadians(1.0 + i),
                        (i >= n/2) ? Math.toRadians(-1) : Math.toRadians(1)
                );
            }

            this.pendulums[i] = p;
        }

        this.phaseFrame.addDrawable(this.area);
    }

    /**
     * Does a time step.
     */
    public void doStep() {

        Polygon polygon = new Polygon();

        for(int i = 0; i < this.pendulums.length; i++) {
            Pendulum p = this.pendulums[i];

            this.plotFrame.append(
                    i,
                    p.getTime(),
                    Math.toDegrees(p.getTheta())
            );

            this.phaseFrame.append(
                    i,
                    Math.toDegrees(p.getTheta()),
                    Math.toDegrees(p.getOmega())
            );

            polygon.appendPoint(Math.toDegrees(p.getTheta()), Math.toDegrees(p.getOmega()));

            p.update(this.dt);
        }

        this.area.setPolygon(polygon);

        this.control.println("Area: " + polygon.calcArea());
    }

    /**
     * Resets the simulation.
     */
    public void reset() {
        this.control.setValue(Param.AMOUNT_PENDULUMS.toString(), 15);
        this.control.setValue(Param.RAND_PARAMS.toString(), 1);
        this.control.setValue(Param.TIME_STEP.toString(), 0.1);

        this.enableStepsPerDisplay(true);
    }

    /**
     * Starts the Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) { // creates a simulation control structure using this class
        SimulationControl.createApp(new PendulumFamilyApp());
    }
}