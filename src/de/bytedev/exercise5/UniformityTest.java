package de.bytedev.exercise5;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.LinearCongurentialRandom;
import org.opensourcephysics.controls.AbstractCalculation;
import org.opensourcephysics.controls.CalculationControl;

import java.util.ArrayList;
import java.util.List;

public class UniformityTest extends AbstractCalculation {

    enum Param {
        N("N (generated Numbers)"),
        BINS("bins"),
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

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("bin", "values", "UniformityTest");

    @Override
    public void calculate() {
        LinearCongurentialRandom rand = new LinearCongurentialRandom(
                this.control.getInt(Param.X.toString()),
                this.control.getInt(Param.A.toString()),
                this.control.getInt(Param.C.toString()),
                this.control.getInt(Param.M.toString())
        );

        int nrBins = this.control.getInt(Param.BINS.toString());
        int n = this.control.getInt(Param.N.toString());

        this.plotFrame.setPreferredMinMaxX(0, nrBins);

        List<Double> generatedNumbers = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            generatedNumbers.add( rand.nextDouble() );
        }

        for(int bin = 0; bin < nrBins; bin++) {
            int count = 0;
            double intervalStart = (1.0*bin)/nrBins;
            double intervalEnd = intervalStart + 1.0/nrBins;

            for(Double value : generatedNumbers) {
                if( value >= intervalStart && value < intervalEnd ) {
                    count++;
                }
            }

            plotFrame.append(bin, (1.0*count)/n);
        }

        plotFrame.newPlot();
    }

    @Override
    public void reset() {
        this.control.setValue(Param.N.toString(), 1000);
        this.control.setValue(Param.BINS.toString(), 100);
        this.control.setValue(Param.A.toString(), 106);
        this.control.setValue(Param.C.toString(), 1283);
        this.control.setValue(Param.M.toString(), 6075);
        this.control.setValue(Param.X.toString(), 42);
    }

    public static void main(String[] args) {
        CalculationControl.createApp(new UniformityTest());
    }
}
