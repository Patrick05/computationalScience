package de.bytedev.exercise7;

import de.bytedev.statistics.Walker;
import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

public class SimpleSelfAvoidingWalk extends AbstractSimulation {
    enum Param {
        N("N (total monomeres)"),
        NR_TESTS("amount of tests");

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    protected Walker walker;
    protected ExtendedPlotFrame walkerFrame = new ExtendedPlotFrame("x", "y", "WalkerFrame");
    protected ExtendedPlotFrame ratioFrame = new ExtendedPlotFrame("n", "%", "RatioFrame");
    protected ExtendedPlotFrame rFrame = new ExtendedPlotFrame("n", "r2", "RFrame");

    protected int N;
    protected int currentN;
    protected int nrTests;
    protected int currentTest;
    protected int successfulTests;

    @Override
    public void reset() {
        this.enableStepsPerDisplay(true);

        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();
        this.ratioFrame.clearData();
        this.rFrame.clearData();

        this.control.setValue(Param.N.toString(), 1000);
        this.control.setValue(Param.NR_TESTS.toString(), 1000);
    }

    @Override
    public void initialize() {
        this.walker = new Walker();

        this.walkerFrame.append(0, 0);
        this.walkerFrame.addDrawable(this.walker.getCircle());
        this.walkerFrame.setPreferredMinMax(-15, 15, -15, 15);

        this.N = this.control.getInt(Param.N.toString());
        this.currentN = 1;
        this.nrTests = this.control.getInt(Param.NR_TESTS.toString());
        this.currentTest = 0;
        this.successfulTests = 0;
    }

    @Override
    protected void doStep() {
        int countSuccessful = 0;
        double r2Sum = 0;
        for(int j = 0; j < this.nrTests; j++) {
            this.walkerFrame.setMessage("currentTest = "+j);
            if( this.letsWalk(this.currentN) ) {
                r2Sum += Math.pow(this.walker.getPosition().getLength(), 2);
                countSuccessful++;
            }
        }

        this.ratioFrame.append(this.currentN, (1.0*countSuccessful)/this.nrTests);
        this.rFrame.append(this.currentN, r2Sum/countSuccessful);

        if(this.currentN == this.N) {
            this.control.calculationDone("DONE!");
        }

        this.currentN++;
    }

    public boolean letsWalk(int n) {
        this.walker.reset();
        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();

        boolean successful = true;

        for(int i=0; i<n; i++) {
            if(this.walker.getHistory().size() > 0) {
                this.walker.moveRandomlyAvoiding(this.walker.getHistory().get(this.walker.getHistory().size()-1));
            } else {
                this.walker.move(Walker.Direction.UP);
            }

            this.walkerFrame.append(this.walker.getPosition().getX(), this.walker.getPosition().getY());

            if(this.walker.getHistory().size() > 2) {
                boolean selfIntersect = false;
                for (Vector2D position : this.walker.getHistory().subList(0, this.walker.getHistory().size() - 2)) {
                    if (position.getX() == this.walker.getPosition().getX() &&
                            position.getY() == this.walker.getPosition().getY()) {
                        selfIntersect = true;
                        break;
                    }
                }
                successful = !selfIntersect;
            }

            if(!successful) break;
        }

        return successful;
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new SimpleSelfAvoidingWalk());
    }
}