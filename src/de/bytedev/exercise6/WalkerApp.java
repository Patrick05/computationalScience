package de.bytedev.exercise6;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

public class WalkerApp extends AbstractSimulation {
    enum Param {
        WALKERS("walkers");

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }


    private Walker2D walker2D;
    private ExtendedPlotFrame walkerFrame = new ExtendedPlotFrame("x", "y", "WalkerFrame");
    private ExtendedPlotFrame avgFrame = new ExtendedPlotFrame("N", "avg", "AverageFrame");
    private int steps;

    @Override
    public void reset() {
        this.enableStepsPerDisplay(true);
        this.control.setValue(Param.WALKERS.toString(), 2000);

        this.steps = 0;
        this.walkerFrame.clearDrawables();
        this.avgFrame.clearData();
    }

    @Override
    public void initialize() {
        this.walker2D = new Walker2D();
        this.walker2D.addWalkers(
                this.control.getInt(Param.WALKERS.toString())
        );

        this.walkerFrame.addDrawable(this.walker2D);
        this.walkerFrame.setPreferredMinMax(-100, 100, -100, 100);
    }

    @Override
    protected void doStep() {
        this.walker2D.moveRandomly();

        this.walkerFrame.setMessage("Current Step: "+this.steps);

        double avgX = this.walker2D.calcAvgX();
        double avgY = this.walker2D.calcAvgY();
        double avgXSquared = this.walker2D.calcAvgXSquared();
        double avgYSquared = this.walker2D.calcAvgYSquared();
        double avgRSquared = avgXSquared - Math.pow(avgX, 2) + avgYSquared - Math.pow(avgY, 2);


        this.avgFrame.append(0, this.steps, avgX);
        this.avgFrame.append(1, this.steps, avgY);
        this.avgFrame.append(2, this.steps, avgRSquared);



        this.steps++;
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new WalkerApp());
    }
}
