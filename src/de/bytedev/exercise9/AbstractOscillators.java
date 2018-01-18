package de.bytedev.exercise9;

import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.*;

import java.awt.*;
import java.util.Random;

/**
 * Oscillators models the analytic soution of a chain of oscillators.
 * <p>
 * Students should implement the ODE interface to
 * complete the exercise in "An Introduction to Computer Simulation Methods."
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public abstract class AbstractOscillators implements ODE, Drawable {
    private ODESolver odeSolver;

    /**
     * [ u1, v1, u2, v2, ... , t ]
     */
    private double[] state;
    private int N;

    /**
     * Spring rates between nodes.
     * The rate n is of the spring between node n and n+1
     */
    private double[] k;

    private OscillatorsMode normalMode;

    /**
     * Constructs a chain of coupled oscillators in the given mode and number of oscillators.
     *
     * @param mode int
     * @param N    int
     * @param kValues double
     * @param randU boolean
     */
    public AbstractOscillators(int mode, int N, double kValues, boolean randU) {
        this.N = N;

        this.state = new double[ (this.N+2)*2 + 1 ];
        this.k = new double[this.N+1];

        this.normalMode = new OscillatorsMode(mode, this.N);

        Random rand = new Random();

        // Set initial displacement and velocity
        for(int i=0; i < this.state.length - 2; i += 2 ) {
            int x = i/2;

            if(i == 0 || i == this.state.length-3) {
                // first and last node should not be displaced and does not moving
                this.state[i] = 0;
                this.state[i+1] = 0;
            } else {
                if(randU) {
                    this.state[i] = rand.nextDouble() - 0.5;
                } else {
                    this.state[i] = this.normalMode.evaluate(x);
                }
                this.state[i + 1] = 0;
            }

            if( x < this.N ) {
                this.k[x] = kValues;
            }
        }

        // Set initial time to 0
        this.state[ this.state.length-1 ] = 0;

        this.odeSolver = new Verlet(this);
    }

    /**
     * Returns the displacement of a specific node in the oscillator-chain.
     *
     * @param nodeIndex
     * @return
     */
    public double getNodeDisplacement(int nodeIndex) {
        if( nodeIndex >= 0 && nodeIndex < this.getNumNodes() ) {
            int stateIndex = nodeIndex*2;

            return this.state[stateIndex];
        } else {
            throw new IndexOutOfBoundsException("The node with index "+nodeIndex+" does not exist. Number of nodes: "+this.getNumNodes());
        }
    }

    /**
     * Returns the velocity of a specific node in the oscillator-chain.
     *
     * @param nodeIndex
     * @return
     */
    private double getNodeVelocity(int nodeIndex) {
        if( nodeIndex >= 0 && nodeIndex < this.getNumNodes() ) {
            int stateIndex = nodeIndex*2;

            return this.state[stateIndex + 1];
        } else {
            throw new IndexOutOfBoundsException("The node with index "+nodeIndex+" does not exist. Number of nodes: "+this.getNumNodes());
        }
    }

    /**
     * Returns the spring rate of the spring between the two given nodes.
     *
     * @param aNodeIndex
     * @param bNodeIndex
     * @return
     */
    private double getSpringRateBetweenNodes(int aNodeIndex, int bNodeIndex) {
        if( !(aNodeIndex >= 0 && aNodeIndex < this.getNumNodes()) ) {
            throw new IndexOutOfBoundsException("The first node with index "+aNodeIndex+" does not exist. Number of nodes: "+this.getNumNodes());
        }

        if( !(bNodeIndex >= 0 && bNodeIndex < this.getNumNodes()) ) {
            throw new IndexOutOfBoundsException("The second node with index "+bNodeIndex+" does not exist. Number of nodes: "+this.getNumNodes());
        }

        if( Math.abs(aNodeIndex - bNodeIndex) != 1 ) {
            throw new IllegalArgumentException("The first and the second node have to be neighbours.");
        }

        return this.k[Math.min(aNodeIndex, bNodeIndex)];
    }

    /**
     * Returns the number of nodes in the oscillator-chain.
     *
     * @return
     */
    public int getNumNodes() {
        return (this.state.length - 1) / 2;
    }

    /**
     * Steps the time using the given time step.
     *
     * @param dt
     */
    public void step(double dt) {
        this.odeSolver.setStepSize(dt);
        this.odeSolver.step();
    }

    @Override
    public double[] getState() {
        return this.state;
    }

    @Override
    public void draw(DrawingPanel drawingPanel, Graphics g) {
        Circle circle = new Circle();

        this.normalMode.draw(drawingPanel, g); // draw initial condition

        for (int i = 0; i < this.state.length -2 ; i += 2) {
            circle.setXY(i/2, this.state[i]);
            circle.draw(drawingPanel, g);
        }
    }
}
