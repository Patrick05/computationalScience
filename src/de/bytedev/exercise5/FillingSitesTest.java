package de.bytedev.exercise5;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.LinearCongurentialRandom;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.frames.RasterFrame;
import org.opensourcephysics.frames.Scalar2DFrame;

public class FillingSitesTest extends AbstractSimulation {
    enum Param {
        L("L"),
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

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("n", "t", "FillingSitesTest");
    private Scalar2DFrame fillingFrame = new Scalar2DFrame("x", "y", "FillingSitesTest");

    private LinearCongurentialRandom random;
    private double[][] square;
    private int l;
    private double t;
    private int foundIndex;

    @Override
    public void initialize() {
        this.random = new LinearCongurentialRandom(
                this.control.getInt(Param.X.toString()),
                this.control.getInt(Param.A.toString()),
                this.control.getInt(Param.C.toString()),
                this.control.getInt(Param.M.toString())
        );

        this.l = this.control.getInt(Param.L.toString());
        this.t = 0;

        this.square = new double[this.l][this.l];

        for(int x = 0; x < this.l; x++) {
            for(int y = 0; y < this.l; y++) {
                this.square[x][y] = 0;
            }
        }

        this.foundIndex = 0;

        this.plotFrame.newPlot();

        this.fillingFrame.setPreferredMinMax(0, this.l, 0, this.l);
        this.fillingFrame.setAll(this.square);
        this.fillingFrame.setVisible(true);
        this.fillingFrame.convertToGrayscalePlot();
    }

    @Override
    protected void doStep() {
        int x = this.fillingFrame.xToIndex(((this.l) * this.random.nextDouble()));
        int y = this.fillingFrame.yToIndex(((this.l) * this.random.nextDouble()));

        if( this.square[x][y] == 0 ) {
            this.square[x][y] = 255;
            this.plotFrame.append(1, this.foundIndex, this.t);
            this.foundIndex++;

            this.fillingFrame.setAll(this.square);
        }

        this.t++;
    }

    @Override
    public void reset() {
        this.control.setValue(Param.L.toString(), 10);
        this.control.setValue(Param.A.toString(), 65539);
        this.control.setValue(Param.C.toString(), 0);
        this.control.setValue(Param.M.toString(), Math.pow(2,31));
        this.control.setValue(Param.X.toString(), 42);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new FillingSitesTest());
    }
}
