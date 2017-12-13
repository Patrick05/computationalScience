package de.bytedev.exercise7;

import de.bytedev.statistics.Walker;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.controls.SimulationControl;


public class RnRSelfAvoidingWalk extends SimpleSelfAvoidingWalk {

    protected double weightedRSum;
    protected double weightSum;

    @Override
    public void initialize() {
        super.initialize();

        this.weightedRSum = 0;
        this.weightSum = 0;
    }

    @Override
    protected void doStep() {
        this.walker.reset();
        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();

        this.xSum = 0;
        this.xSquaredSum = 0;
        this.ySum = 0;
        this.ySquaredSum = 0;
        this.weightedRSum = 0;
        this.weightSum = 0;
        this.currentTest++;

        for(int i=1; i<=this.N; i++) {
            double weight = 1;

            if(this.walker.getHistory().size() > 0) {
                weight = this.walker.moveRandomlyAvoiding(this.walker.getHistory());
            } else {
                this.walker.move(Walker.Direction.UP);
            }

            this.walkerFrame.append(this.walker.getPosition().getX(), this.walker.getPosition().getY());

            if(this.walker.getHistory().size() > 2) {
                if( this.walker.getPosition().getX() == this.walker.getHistory().get(this.walker.getHistory().size()-2).getX() &&
                    this.walker.getPosition().getY() == this.walker.getHistory().get(this.walker.getHistory().size()-2).getY()) {
                    break;
                }
            }

            this.xSum += this.walker.getPosition().getX();
            this.xSquaredSum += Math.pow(this.walker.getPosition().getX(), 2);
            this.ySum += this.walker.getPosition().getY();
            this.ySquaredSum += Math.pow(this.walker.getPosition().getY(), 2);

            this.weightSum += weight;

            double avgX = this.xSum/i;
            double avgXSquared = this.xSquaredSum/i;
            double avgY = this.ySum/i;
            double avgYSquared = this.ySquaredSum/i;

            this.weightedRSum = weight*(avgXSquared - Math.pow(avgX, 2) + avgYSquared - Math.pow(avgY, 2));
        }

        this.walkerFrame.setMessage("currentTest = "+this.currentTest);

        this.control.calculationDone("DONE!\n<R²> = "+(this.weightedRSum/this.weightSum));
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new RnRSelfAvoidingWalk());
    }
}
