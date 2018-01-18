/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package de.bytedev.exercise9;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.DisplayFrame;

/**
 * OscillatorsApp displays a system of coupled oscillators in a drawing panel.
 * <p>
 * The separation between oscillators is one in the current model.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class OscillatorsApp extends AbstractSimulation {
    enum Param {
        DT("dt"),
        K("k"),
        MODE("mode"),
        N("number of particles"),
        RAND_U("random initial displacement"),
        TYPE("oscillator type: 0:static, 1:periodic, 2:free"),
        ;
        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private DisplayFrame displayFrame = new DisplayFrame("Position", "Displacement", "Oscillators");
    private AbstractOscillators oscillators;
    private ExtendedPlotFrame particlePeriodFrame = new ExtendedPlotFrame("time", "displacement", "Particle Period Frame");

    private double time;
    private double dt;

    /**
     * Initializes the simulation by creating a system of oscillators.
     */
    public void initialize() {
        this.time = 0;
        this.dt = this.control.getDouble(Param.DT.toString());

        int mode = this.control.getInt(Param.MODE.toString());
        int N = this.control.getInt(Param.N.toString());
        double k = this.control.getDouble(Param.K.toString());

        boolean randU = this.control.getBoolean(Param.RAND_U.toString());
        int type = this.control.getInt(Param.TYPE.toString());

        switch(type) {
            case 0:
                this.oscillators = new StaticBoundsOscillators(mode, N, k, randU);
                break;
            case 1:
                this.oscillators = new PeriodicBoundsOscillators(mode, N, k, randU);
                break;
            case 2:
                this.oscillators = new FreeBoundsOscillators(mode, N, k, randU);
                break;
            default:
                throw new IllegalArgumentException("Invalid type "+type);
        }



        this.displayFrame.setPreferredMinMax(0, N + 1, -1.5, 1.5);
        this.displayFrame.clearDrawables();
        this.displayFrame.setSquareAspect(false);
        this.displayFrame.addDrawable(this.oscillators);
    }

    /**
     * Does a time step
     */
    public void doStep() {
        this.oscillators.step(this.dt);

        this.displayFrame.setMessage("t = " + this.decimalFormat.format(this.time));
        this.particlePeriodFrame.append(this.time, this.oscillators.getNodeDisplacement(4));

        this.time += dt;
    }

    /**
     * Resets the oscillator program to its default values.
     */
    public void reset() {
        this.control.setValue(Param.N.toString(), 16);
        this.control.setValue(Param.MODE.toString(), 1);
        this.control.setValue(Param.DT.toString(), 0.5);
        this.control.setValue(Param.K.toString(), 1.0);
        this.control.setValue(Param.RAND_U.toString(), false);
        this.control.setValue(Param.TYPE.toString(), 0);

        this.enableStepsPerDisplay(true);
        super.setStepsPerDisplay(10);
        this.initialize();
    }

    /**
     * Creates the oscillator program from the command line
     *
     * @param args
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new OscillatorsApp());
    }
}