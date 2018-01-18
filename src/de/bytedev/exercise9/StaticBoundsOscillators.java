package de.bytedev.exercise9;

public class StaticBoundsOscillators extends AbstractOscillators {

    public StaticBoundsOscillators(int mode, int N, double kValues, boolean randU) {
        super(mode, N, kValues, randU);
    }

    @Override
    public void getRate(double[] state, double[] rate) {

        for(int i=0; i < this.getState().length - 2; i += 2 ) {
            // nodes change their displacement by their velocity
            rate[i] = state[i+1];

            if(i == 0 || i >= this.getState().length-3) {
                // first and last node should be static
                rate[i+1] = 0;
            } else {
                int nodeIndex = i/2;

                rate[i+1] = -1*(2*this.getNodeDisplacement(nodeIndex)-this.getNodeDisplacement(nodeIndex-1)-this.getNodeDisplacement(nodeIndex+1));
                //rate[i+1] = - this.getSpringRateBetweenNodes(nodeIndex, nodeIndex+1) * (this.getNodeDisplacement(nodeIndex) - this.getNodeDisplacement(nodeIndex+1))
                //            - this.getSpringRateBetweenNodes(nodeIndex, nodeIndex-1) * (this.getNodeDisplacement(nodeIndex) - this.getNodeDisplacement(nodeIndex-1));
            }

        }

        rate[this.getNumNodes()*2] = 1; // dt/dt = 1
    }
}
