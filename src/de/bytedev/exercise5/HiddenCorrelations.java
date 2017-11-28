package de.bytedev.exercise5;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.LinearCongurentialRandom;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.Scalar2DFrame;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class HiddenCorrelations extends AbstractSimulation {
    enum Param {
        K("k"),
        A("a"),
        C("c"),
        M("m"),
        X("x0"),
        ;

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return this.v;
        }
    }

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("xi", "xi+k", "HiddenCorrelations");

    private LinearCongurentialRandom random;
    private int k;
    private Queue<Double> queue;

    @Override
    public void initialize() {
        this.random = new LinearCongurentialRandom(
                this.control.getInt(Param.X.toString()),
                this.control.getInt(Param.A.toString()),
                this.control.getInt(Param.C.toString()),
                this.control.getInt(Param.M.toString())
        );

        this.k = this.control.getInt(Param.K.toString());

        this.queue = new LinkedBlockingDeque<>();
        this.plotFrame.newPlot();
        this.plotFrame.setConnected(false);
    }

    @Override
    protected void doStep() {
        double xik = this.random.nextDouble();
        this.queue.add(xik);

        if(this.queue.size() > this.k) {
            double xi = this.queue.poll();
            this.plotFrame.append(xi, xik);
        }
    }

    @Override
    public void reset() {
        this.control.setValue(Param.K.toString(), 1);
        this.control.setValue(Param.A.toString(), 16807);
        this.control.setValue(Param.C.toString(), 0);
        this.control.setValue(Param.M.toString(), Math.pow(2,31)-1);
        this.control.setValue(Param.X.toString(), 42);
        this.enableStepsPerDisplay(true);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new HiddenCorrelations());
    }
}
