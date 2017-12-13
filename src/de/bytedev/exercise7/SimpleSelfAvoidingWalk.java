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

    protected int N;
    protected int nrTests;
    protected int currentTest;
    protected int successfulTests;

    protected double xSum;
    protected double xSquaredSum;
    protected double ySum;
    protected double ySquaredSum;

    @Override
    public void reset() {
        this.enableStepsPerDisplay(true);

        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();

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
        this.nrTests = this.control.getInt(Param.NR_TESTS.toString());
        this.currentTest = 0;
        this.successfulTests = 0;

        this.xSum = 0;
        this.xSquaredSum = 0;
        this.ySum = 0;
        this.ySquaredSum = 0;
    }

    @Override
    protected void doStep() {
        this.walker.reset();
        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();

        boolean successful = true;

        for(int i=0; i<this.N; i++) {
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

        if(successful) {
            this.successfulTests++;
        }

        this.xSum += this.walker.getPosition().getX();
        this.xSquaredSum += Math.pow(this.walker.getPosition().getX(), 2);
        this.ySum += this.walker.getPosition().getY();
        this.ySquaredSum += Math.pow(this.walker.getPosition().getY(), 2);

        this.currentTest++;
        this.walkerFrame.setMessage("currentTest = "+this.currentTest);

        if(this.currentTest == this.nrTests) {
            double avgX = this.xSum/this.nrTests;
            double avgXSquared = this.xSquaredSum/this.nrTests;
            double avgY = this.ySum/this.nrTests;
            double avgYSquared = this.ySquaredSum/this.nrTests;

            this.control.calculationDone(
                    "DONE!\n"+
                    this.successfulTests+ " / "+this.nrTests+" are successful! ("+(1.0*this.successfulTests)/this.nrTests+")\n"+
                    "<R²> = "+(avgXSquared - Math.pow(avgX, 2) + avgYSquared - Math.pow(avgY, 2))
            );
        }
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new SimpleSelfAvoidingWalk());
    }
}