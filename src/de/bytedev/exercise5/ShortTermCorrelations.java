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
        K_MAX("max k"),
        N("N"),
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
    private int kMax;
    private int kCur;
    private int N;
    private double[] randomList;

    @Override
    public void initialize() {
        this.random = new LinearCongurentialRandom(
                this.control.getInt(Param.X.toString()),
                this.control.getInt(Param.A.toString()),
                this.control.getInt(Param.C.toString()),
                this.control.getInt(Param.M.toString())
        );

        this.N = this.control.getInt(Param.N.toString());
        this.kMax = this.control.getInt(Param.K_MAX.toString());
        this.kCur = 0;

        this.randomList = new double[this.N];
        for(int i=0; i<N; i++) {
            this.randomList[i] = this.random.nextDouble();
        }

        this.plotFrame.newPlot();
    }

    @Override
    protected void doStep() {
        if(this.kCur >= this.kMax) {
            this.control.calculationDone("DONE!");
        }
        this.plotFrame.append(this.kCur, this.autocorrelation(this.kCur, this.randomList));
        this.kCur++;
    }

    public double autocorrelation(int k, double[] numbers) {
        double n = numbers.length - k;
        double xiSum = 0;
        double xiSquaredSum = 0;
        double xikxiSum = 0;

        for(int i = 0; i < n; i++) {
            xiSum += numbers[i];
            xiSquaredSum += Math.pow(numbers[i], 2);
            xikxiSum += numbers[i]*numbers[i+k];
        }

        double xiAvg = xiSum/n;
        double xiSquaredAvg = xiSquaredSum/n;
        double xikxiAvg = xikxiSum/n;

        return (xikxiAvg - Math.pow(xiAvg, 2))/(xiSquaredAvg - Math.pow(xiAvg, 2));
    }

    @Override
    public void reset() {
        this.control.setValue(Param.N.toString(), 1000);
        this.control.setValue(Param.K_MAX.toString(), 20);
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
