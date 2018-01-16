package de.bytedev.exercise8;

public interface IParticleModel {

    /**
     * Sets the state of the model.
     *
     * @param state
     */
    void setState(double[] state);

    /**
     * Returns the state of the model.
     *
     * @return
     */
    double[] getState();

    /**
     * Sets the initial configuration of the model.
     *
     * @param initialConfiguration
     */
    void setInitialConfiguration(String initialConfiguration);

    /**
     * Returns the initial configuration of the model.
     *
     * @return
     */
    String getInitialConfiguration();

    /**
     * Sets the x-acceleration of the model.
     *
     * @param ax
     */
    void setAx(double[] ax);

    /**
     * Returns the x-acceleration of the model.
     *
     * @return
     */
    double[] getAx();

    /**
     * Sets the y-acceleration of the model.
     *
     * @param ay
     */
    void setAy(double[] ay);

    /**
     * Returns the y-acceleration of the model.
     *
     * @return
     */
    double[] getAy();

    /**
     * Returns the time of the model.
     *
     * @return
     */
    double getTime();

    /**
     * Returns the total amount of particles of the model.
     *
     * @return
     */
    int getN();

    /**
     * Sets lx.
     *
     * @param lx
     */
    void setLx(double lx);

    /**
     * Returns the with of the Area.
     *
     * @return
     */
    double getLx();

    /**
     * Sets ly.
     *
     * @param ly
     */
    void setLy(double ly);

    /**
     * Returns the height of the area.
     *
     * @return
     */
    double getLy();

    /**
     * Returns rho.
     *
     * @return
     */
    double getRho();

    /**
     * Returns true if equilibrium.
     *
     * @return
     */
    boolean isEquilibrium();

    /**
     * Returns the amount of steps.
     *
     * @return
     */
    int getSteps();

    /**
     * Sets the initial kinetic energy
     *
     * @param initialKineticEnergy
     */
    void setInitialKineticEnergy(double initialKineticEnergy);

    /**
     * Returns the initial kinetic energy
     *
     * @return
     */
    double getInitialKineticEnergy();

    /**
     * Resets the averages.
     */
    void resetAverages();
}
