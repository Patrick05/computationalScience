package de.bytedev.exercise2;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;

public class PendulumApp extends AbstractSimulation {
    enum Param {
        PENDULUM_LENGTH("Pendulum Length [m]"),
        INITIAL_THETA("Initial angle (theta) [deg]"),
        INITIAL_OMEGA("Initial angular frequency (omega)"),
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
    private ExtendedPlotFrame phaseFrame = new ExtendedPlotFrame("Theta", "Omega", "Omega vs. Theta");
    private DisplayFrame displayFrame = new DisplayFrame("Pendulum");

    private Pendulum pendulum;
    private double dt;

    /**
     * Constructs the PendulumApp and intializes the display.
     */
    public PendulumApp() {
        this.displayFrame.setPreferredMinMax(-1.2, 1.2, -1.2, 1.2);
    }

    /**
     * Initializes the simulation.
     */
    public void initialize() {
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());

        double l = this.control.getDouble(Param.PENDULUM_LENGTH.toString());
        this.pendulum = new Pendulum(l);

        double theta = Math.toRadians(this.control.getDouble(Param.INITIAL_THETA.toString()));
        double omega = this.control.getDouble(Param.INITIAL_OMEGA.toString());
        this.pendulum.setState(theta, omega);

        this.displayFrame.addDrawable(this.pendulum);
        this.phaseFrame.newPlot();
        this.plotFrame.newPlot();
    }

    /**
     * Does a time step.
     */
    public void doStep() {
        this.plotFrame.append(
                this.pendulum.getTime(),
                Math.toDegrees(this.pendulum.getTheta())
        );

        this.phaseFrame.append(
                Math.toDegrees(this.pendulum.getTheta()),
                this.pendulum.getOmega()
        );

        pendulum.update(this.dt);
    }

    /**
     * Resets the simulation.
     */
    public void reset() {
        this.control.setValue(Param.PENDULUM_LENGTH.toString(), 3);
        this.control.setValue(Param.INITIAL_THETA.toString(), 30);
        this.control.setValue(Param.INITIAL_OMEGA.toString(), 0);
        this.control.setValue(Param.TIME_STEP.toString(), 0.1);

        this.enableStepsPerDisplay(true);
    }

    /**
     * Starts the Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) { // creates a simulation control structure using this class
        SimulationControl.createApp(new PendulumApp());
    }
}