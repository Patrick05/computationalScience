package de.bytedev.exercise7;

import de.bytedev.statistics.Walker;
import de.bytedev.utility.Vector2D;
import org.opensourcephysics.controls.SimulationControl;


public class RnRSelfAvoidingWalk extends SimpleSelfAvoidingWalk {


    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    protected void doStep() {
        int countSuccessful = 0;
        double r2Sum = 0;
        for(int j = 0; j < this.nrTests; j++) {
            this.walkerFrame.setMessage("currentTest = "+j);

            double r2 = this.letsRnRWalk(this.currentN);
            if(r2 > 0.0) {
                r2Sum += r2;
                countSuccessful++;
            }
        }

        this.ratioFrame.append(this.currentN, (1.0*countSuccessful)/this.nrTests);
        this.rFrame.append(Math.log(this.currentN), Math.log(r2Sum/countSuccessful));

        if(this.currentN == this.N) {
            this.control.calculationDone("DONE!");
        }

        this.currentN++;
    }

    public double letsRnRWalk(int n) {
        this.walker.reset();
        this.walkerFrame.clearData();
        this.walkerFrame.clearDrawables();

        double weightRSum = 0;
        double weightSum = 0;

        for(int i=1; i<=n; i++) {
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

            // break if the walker runs into a dead end
            if(weight == 0) {
                return 0.0;
            }

            weightSum += weight;
            weightRSum += weight * Math.pow(this.walker.getPosition().getLength(),2);
        }

        return weightRSum/weightSum;
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new RnRSelfAvoidingWalk());
    }
}
