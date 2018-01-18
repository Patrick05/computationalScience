package de.bytedev.exercise9;

public class FreeBoundsOscillators extends AbstractOscillators {

    public FreeBoundsOscillators(int mode, int N, double kValues, boolean randU) {
        super(mode, N, kValues, randU);

        this.getState()[0] = this.getState()[2];
        this.getState()[ this.getState().length-3 ] = this.getState()[ this.getState().length - 5 ];
    }

    @Override
    public void getRate(double[] state, double[] rate) {

        state[0] = state[2];
        state[ state.length-3 ] = state[ state.length - 5 ];

        for(int i=0; i < this.getState().length - 2; i += 2 ) {
            // nodes change their displacement by their velocity
            rate[i] = state[i+1];

            if(i == 0) {
                rate[i] = state[i+3];
            } else if(i == this.getState().length - 3) {
                rate[i] = state[i-1];
            }

            if(i == 0 || i >= this.getState().length-3) {
                // first and last node should be static
                rate[i+1] = 0;
            } else {
                int nodeIndex = i / 2;
                rate[i + 1] = -1 * (2 * this.getNodeDisplacement(nodeIndex) - this.getNodeDisplacement(nodeIndex - 1) - this.getNodeDisplacement(nodeIndex + 1));
            }
        }

        rate[this.getNumNodes()*2] = 1; // dt/dt = 1
    }
}
