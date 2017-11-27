package de.bytedev.exercise5;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.LinearCongurentialRandom;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ShortTermCorrelations extends AbstractSimulation {
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

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("interval", "C(k)", "ShortTermCorrelations");

    private LinearCongurentialRandom random;
    private int k;
    private Queue<Double> queue;
    private int i;

    private double xiSum;
    private double xiSquaredSum;
    private double xikxiSum;

    @Override
    public void initialize() {
        this.random = new LinearCongurentialRandom(
                this.control.getInt(Param.X.toString()),
                this.control.getInt(Param.A.toString()),
                this.control.getInt(Param.C.toString()),
                this.control.getInt(Param.M.toString())
        );

        this.k = this.control.getInt(Param.K.toString());

        this.i = 1;
        this.xiSum = 0;
        this.xiSquaredSum = 0;
        this.xikxiSum = 0;

        this.queue = new LinkedBlockingDeque<>();
        this.plotFrame.newPlot();
    }

    @Override
    protected void doStep() {
        double xik = this.random.nextDouble();
        this.queue.add(xik);

        if(this.queue.size() > this.k) {
            double xi = this.queue.poll();

            this.xiSum += xi;
            this.xiSquaredSum += xi*xi;
            this.xikxiSum += xik*xi;

            double avgXikxi = this.xikxiSum / this.i;
            double avgXi = this.xiSum / this.i;
            double avgXiSquared = this.xiSquaredSum / this.i;

            double ck = (avgXikxi - Math.pow(avgXi,2))/(avgXiSquared - Math.pow(avgXi,2));

            this.plotFrame.append(this.i, ck);
            this.i++;
        }
    }

    @Override
    public void reset() {
        this.control.setValue(Param.K.toString(), 1);
        this.control.setValue(Param.A.toString(), 106);
        this.control.setValue(Param.C.toString(), 1283);
        this.control.setValue(Param.M.toString(), 6075);
        this.control.setValue(Param.X.toString(), 42);
        this.enableStepsPerDisplay(true);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new ShortTermCorrelations());
    }
}
