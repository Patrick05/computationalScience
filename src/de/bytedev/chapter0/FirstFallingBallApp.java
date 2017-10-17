/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package de.bytedev.chapter0;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractCalculation;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.CalculationControl;
import org.opensourcephysics.controls.SimulationControl;

/**
 * FirstFallingBallApp computes the time for a ball to fall 10 meters and displays the variables.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class FirstFallingBallApp extends AbstractSimulation {

    enum Param {
        INITIAL_TIME("Initial Time"),
        INITIAL_VELOCITY("Initial Velocity"),
        TIME_STEP("Time Step");
        

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("t", "y", "Freier Fall");
    private Particle particle;
    private double t;
    private double v;
    private double dt;

    /**
     * Starts the Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        //CalculationControl.createApp(new FirstFallingBallApp());
        SimulationControl.createApp(new FirstFallingBallApp());
    }

    @Override
    public void initialize() {
        super.initialize();

        this.t = this.control.getDouble(Param.INITIAL_TIME.toString());
        this.v = this.control.getDouble(Param.INITIAL_VELOCITY.toString());
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());

        this.particle = new Particle(10, this.v, this.t);

        this.plotFrame.newPlot();
    }

    @Override
    public void startRunning() {
        super.startRunning();
    }

    @Override
    protected void doStep() {
        if( this.particle.hitsGround() ) {
            this.control.calculationDone("Particle hits the ground!");
        }

        this.particle.update(this.dt);
        this.t = this.t + this.dt;

        this.plotFrame.append(t, this.particle.getY());
    }

    @Override
    public void stopRunning() {
        super.stopRunning();

        this.control.println("Results");
        this.control.println("final time = " + t);
        this.control.println("numerical:\n" + this.particle.toString() );
        this.control.println("analytic: y = " + this.particle.analyticalY(t) + " v = " + this.particle.analyticV(t));
    }

    @Override
    public void reset() {
        this.control.setValue(Param.INITIAL_TIME.toString(), 0);
        this.control.setValue(Param.INITIAL_VELOCITY.toString(), 0);
        this.control.setValue(Param.TIME_STEP.toString(), 0.01);

        this.plotFrame.clearData();
    }
}