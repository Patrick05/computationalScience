package de.bytedev.exercise9;

public class PeriodicBoundsOscillators extends AbstractOscillators {

    public PeriodicBoundsOscillators(int mode, int N, double kValues, boolean randU) {
        super(mode, N, kValues, randU);
    }

    @Override
    public void getRate(double[] state, double[] rate) {

        for(int i=0; i < this.getState().length - 2; i += 2 ) {
            int nodeIndex = i/2;

            // nodes change their displacement by their velocity
            rate[i] = state[i+1];

            if(nodeIndex == 0) {
                rate[i+1] = -1*(2*this.getNodeDisplacement(nodeIndex)-this.getNodeDisplacement(this.getNumNodes()-1)-this.getNodeDisplacement(nodeIndex+1));
            }
            else if (nodeIndex == this.getNumNodes()-1) {
                rate[i+1] = -1*(2*this.getNodeDisplacement(nodeIndex)-this.getNodeDisplacement(nodeIndex-1)-this.getNodeDisplacement(0));
            }
            else {
                rate[i+1] = -1*(2*this.getNodeDisplacement(nodeIndex)-this.getNodeDisplacement(nodeIndex-1)-this.getNodeDisplacement(nodeIndex+1));
            }

        }

        rate[this.getNumNodes()*2] = 1; // dt/dt = 1
    }
}
