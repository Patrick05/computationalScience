package de.bytedev.exercise8;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.frames.HistogramFrame;
import org.opensourcephysics.numerics.ODE;
import org.opensourcephysics.numerics.Verlet;

import java.awt.*;

/**
 * Info: Copied and edited from OSP
 */
public class LJParticles implements IParticleModel, Drawable, ODE {
    private Verlet odeSolver = new Verlet(this);
    private double state[];
    private double t;

    private int N; // number of particles
    private int nx; // number per row
    private int ny; // number per column
    private double ax[];
    private double ay[];
    private double lx, ly;
    private double rho;

    private String initialConfiguration;
    private double initialKineticEnergy;

    private double lastMeanPressure = 0;
    private double lastMeanTemperature = 0;
    private boolean isEquilibrium;

    private int steps = 0;

    private double totalPotentialEnergyAccumulator;
    private double totalKineticEnergyAccumulator;
    private double totalKineticEnergySquaredAccumulator;
    private double virialAccumulator;
    private double radius = 0.5; // radius of particles on screen

    /**
     * Default constructor.
     * @param nx
     * @param ny
     */
    public LJParticles(int nx, int ny, double lx, double ly) {
        this.t = 0;
        this.nx = nx;
        this.ny = ny;
        this.N = this.nx * this.ny;

        this.lx = lx;
        this.ly = ly;
        this.rho = this.N / (this.lx * this.ly);

        this.ax = new double[this.N];
        this.ay = new double[this.N];

        this.state = new double[1 + 4 * this.N];

        this.isEquilibrium = false;
    }

    public void initialize() {
        this.resetAverages();

        if (this.initialConfiguration.equals("triangular")) {
            this.setTriangularLattice();
        } else if (this.initialConfiguration.equals("rectangular")) {
            this.setRectangularLattice();
        } else {
            this.setRandomPositions();
        }
        this.setVelocities();
        this.computeAcceleration();
    }

    // end break
    // start break
    // setRandomPositions
    public void setRandomPositions() { // particles placed at random, but not closer than rMinimumSquared
        double rMinimumSquared = Math.pow(2.0, 1.0 / 3.0);
        boolean overlap;
        for (int i = 0; i < N; ++i) {
            do {
                overlap = false;
                state[4 * i] = lx * Math.random();   // x
                state[4 * i + 2] = ly * Math.random(); // y
                int j = 0;
                while ((j < i) && !overlap) {
                    double dx = pbcSeparation(state[4 * i] - state[4 * j], lx);
                    double dy = pbcSeparation(state[4 * i + 2] - state[4 * j + 2], ly);
                    if (dx * dx + dy * dy < rMinimumSquared) {
                        overlap = true;
                    }
                    j++;
                }
            } while (overlap);
        }
    }

    // end break
    // start break
    // setRectangularLattice
    public void setRectangularLattice() { // place particles on a rectangular lattice
        double dx = lx / nx; // distance between columns
        double dy = ly / ny; // distance between rows
        for (int ix = 0; ix < nx; ++ix) {   // loop through particles in a row
            for (int iy = 0; iy < ny; ++iy) { // loop through rows
                int i = ix + iy * ny;
                state[4 * i] = dx * (ix + 0.5);
                state[4 * i + 2] = dy * (iy + 0.5);
            }
        }
    }

    // end break
    // start break
    // setTriangularLattice
    public void setTriangularLattice() { // place particles on triangular lattice
        double dx = lx / nx; // distance between particles on same row
        double dy = ly / ny; // distance between rows
        for (int ix = 0; ix < nx; ++ix) {
            for (int iy = 0; iy < ny; ++iy) {
                int i = ix + iy * ny;
                state[4 * i + 2] = dy * (iy + 0.5);
                if (iy % 2 == 0) {
                    state[4 * i] = dx * (ix + 0.25);
                } else {
                    state[4 * i] = dx * (ix + 0.75);
                }
            }
        }
    }

    // end break
    // start break
    // setVelocities
    public void setVelocities() {
        double vxSum = 0.0;
        double vySum = 0.0;
        for (int i = 0; i < N; ++i) {            // assign random initial velocities
            state[4 * i + 1] = Math.random() - 0.5; // vx
            state[4 * i + 3] = Math.random() - 0.5; // vy
            vxSum += state[4 * i + 1];
            vySum += state[4 * i + 3];
        }
        // zero center of mass momentum
        double vxcm = vxSum / N; // center of mass momentum (velocity)
        double vycm = vySum / N;
        for (int i = 0; i < N; ++i) {
            state[4 * i + 1] -= vxcm;
            state[4 * i + 3] -= vycm;
        }
        double v2sum = 0; // rescale velocities to obtain desired initial kinetic energy
        for (int i = 0; i < N; ++i) {
            v2sum += state[4 * i + 1] * state[4 * i + 1] + state[4 * i + 3] * state[4 * i + 3];
        }
        double kineticEnergyPerParticle = 0.5 * v2sum / N;
        double rescale = Math.sqrt(initialKineticEnergy / kineticEnergyPerParticle);
        for (int i = 0; i < N; ++i) {
            state[4 * i + 1] *= rescale;
            state[4 * i + 3] *= rescale;
        }
    }

    // end break
    // start break
    // averages
    public double getMeanTemperature() {
        return this.totalKineticEnergyAccumulator / (this.N * this.steps);
    }

    public double getMeanEnergy() {
        return this.totalKineticEnergyAccumulator / this.steps + this.totalPotentialEnergyAccumulator / this.steps;
    }

    public double getMeanPressure() {
        double meanVirial;
        meanVirial = this.virialAccumulator / this.steps;
        return 1.0 + 0.5 * meanVirial / (this.N * getMeanTemperature()); // quantity PA/NkT
    }

    /**
     * Gets the heat capacity.
     * <p>
     * Errata: On page 276 in Eq. 8.11, after the equal sign, the first appearance of N should be 1/N
     * and in Eq. 8.12 after the minus sign 1/N should be N. We thank Mike Cooke for pointing out this error.
     *
     * @return double
     */
    public double getHeatCapacity() {
        double meanTemperature = getMeanTemperature();
        double meanKineticEnergySquared = this.totalKineticEnergySquaredAccumulator / this.steps;
        double meanKineticEnergy = this.totalKineticEnergyAccumulator / this.steps;
        // heat capacity related to fluctuations of kinetic energy
        double sigma2 = meanKineticEnergySquared - meanKineticEnergy * meanKineticEnergy;
        double denom = 1.0 - sigma2 / (this.N * meanTemperature * meanTemperature);
        return this.N / denom;
    }

    // end break
    // start break
    // computeAcceleration
    public void computeAcceleration() {
        for (int i = 0; i < N; i++) {
            this.ax[i] = 0;
            this.ay[i] = 0;
        }
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                double dx = pbcSeparation(state[4 * i] - state[4 * j], lx);
                double dy = pbcSeparation(state[4 * i + 2] - state[4 * j + 2], ly);
                double r2 = dx * dx + dy * dy;
                double oneOverR2 = 1.0 / r2;
                double oneOverR6 = oneOverR2 * oneOverR2 * oneOverR2;
                double fOverR = 48.0 * oneOverR6 * (oneOverR6 - 0.5) * oneOverR2;
                double fx = fOverR * dx; // force in x-direction
                double fy = fOverR * dy; // force in y-direction
                this.ax[i] += fx;           // use Newton's third law
                this.ay[i] += fy;
                this.ax[j] -= fx;
                this.ay[j] -= fy;
                totalPotentialEnergyAccumulator += 4.0 * (oneOverR6 * oneOverR6 - oneOverR6);
                virialAccumulator += dx * fx + dy * fy;
            }
        }
    }

    // end break
    // start break
    // pbcSeparation
    private double pbcSeparation(double ds, double L) {
        if (ds > 0) {
            while (ds > 0.5 * L) {
                ds -= L;
            }
        } else {
            while (ds < -0.5 * L) {
                ds += L;
            }
        }
        return ds;
    }

    // end break
    // start break
    // pbcPosition
    private double pbcPosition(double s, double L) {
        if (s > 0) {
            while (s > L) {
                s -= L;
            }
        } else {
            while (s < 0) {
                s += L;
            }
        }
        return s;
    }

    // end break
    // start break
    // odeMethods
    @Override
    public void getRate(double[] state, double[] rate) {
        // getRate is called twice for each call to step.
        // accelerations computed for every other call to getRate because
        // new velocity is computed from previous and current acceleration.
        // Previous acceleration is saved in step method of Verlet.
        if (this.odeSolver.getRateCounter() == 1) {
            computeAcceleration();
        }
        for (int i = 0; i < this.N; i++) {
            rate[4 * i] = state[4 * i + 1];   // rates for positions are velocities
            rate[4 * i + 2] = state[4 * i + 3]; // vy
            rate[4 * i + 1] = this.ax[i];        // rate for velocity is acceleration
            rate[4 * i + 3] = this.ay[i];
        }
        rate[4 * this.N] = 1; // dt/dt = 1
    }

    @Override
    public void setState(double[] state) {
        this.state = state;
    }

    @Override
    public double[] getState() {
        return this.state;
    }

    @Override
    public void setInitialConfiguration(String initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
    }

    @Override
    public String getInitialConfiguration() {
        return this.initialConfiguration;
    }

    @Override
    public void setAx(double[] ax) {
        this.ax = ax;
    }

    @Override
    public double[] getAx() {
        return this.ax;
    }

    @Override
    public void setAy(double[] ay) {
        this.ay = ay;
    }

    @Override
    public double[] getAy() {
        return this.ay;
    }

    @Override
    public double getTime() {
        return this.t;
    }

    @Override
    public int getN() {
        return this.N;
    }

    @Override
    public void setLx(double lx) {
        this.lx = lx;
    }

    @Override
    public double getLx() {
        return this.lx;
    }

    @Override
    public void setLy(double ly) {
        this.ly = ly;
    }

    @Override
    public double getLy() {
        return this.ly;
    }

    @Override
    public double getRho() {
        return this.rho;
    }

    @Override
    public boolean isEquilibrium() {
        return this.isEquilibrium;
    }

    @Override
    public int getSteps() {
        return this.steps;
    }

    @Override
    public void setInitialKineticEnergy(double initialKineticEnergy) {
        this.initialKineticEnergy = initialKineticEnergy;
    }

    @Override
    public double getInitialKineticEnergy() {
        return this.initialKineticEnergy;
    }

    @Override
    public void resetAverages() {
        this.steps = 0;
        this.virialAccumulator = 0;
        this.totalPotentialEnergyAccumulator = 0;
        this.totalKineticEnergyAccumulator = 0;
        this.totalKineticEnergySquaredAccumulator = 0;
    }

    public void update(double dt, HistogramFrame xVelocityHistogram) {
        this.odeSolver.setStepSize(dt);
        this.odeSolver.step();
        double totalKineticEnergy = 0;
        for (int i = 0; i < N; i++) {
            totalKineticEnergy += (state[4 * i + 1] * state[4 * i + 1] + state[4 * i + 3] * state[4 * i + 3]);
            xVelocityHistogram.append(state[4 * i + 1]);
            state[4 * i] = pbcPosition(state[4 * i], lx);
            state[4 * i + 2] = pbcPosition(state[4 * i + 2], ly);
        }
        totalKineticEnergy *= 0.5;
        steps++;
        totalKineticEnergyAccumulator += totalKineticEnergy;
        totalKineticEnergySquaredAccumulator += totalKineticEnergy * totalKineticEnergy;

        if(!this.isEquilibrium && this.steps % 500 == 0) {
            if( Math.abs(this.lastMeanPressure - this.getMeanPressure()) < 0.05 && Math.abs(this.lastMeanTemperature - this.getMeanTemperature()) < 0.05) {
                this.isEquilibrium = true;
            } else {
                this.lastMeanPressure = this.getMeanPressure();
                this.lastMeanTemperature = this.getMeanTemperature();
                this.resetAverages();
            }
        }

        t += dt;
    }

    // end break
    // start break
    // draw
    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        if (state == null) {
            return;
        }
        int pxRadius = Math.abs(panel.xToPix(radius) - panel.xToPix(0));
        int pyRadius = Math.abs(panel.yToPix(radius) - panel.yToPix(0));
        g.setColor(Color.red);
        for (int i = 0; i < N; i++) {
            int xpix = panel.xToPix(state[4 * i]) - pxRadius;
            int ypix = panel.yToPix(state[4 * i + 2]) - pyRadius;
            g.fillOval(xpix, ypix, 2 * pxRadius, 2 * pyRadius);
        } // draw central cell boundary
        g.setColor(Color.black);
        int xpix = panel.xToPix(0);
        int ypix = panel.yToPix(ly);
        int lx = panel.xToPix(this.lx) - panel.xToPix(0);
        int ly = panel.yToPix(0) - panel.yToPix(this.ly);
        g.drawRect(xpix, ypix, lx, ly);
    }
    // end break
}