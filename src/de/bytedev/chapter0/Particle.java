package de.bytedev.chapter0;

import de.bytedev.utility.World;

/**
 * This class describes a single particle with radius of 0.
 */
public class Particle {

    private double initialY;
    private double initialV;

    private double y;
    private double v;
    private double livingTime;

    /**
     * Initialize a new particle.
     *
     * @param initialY
     * @param initialV
     * @param initialTime
     */
    public Particle(double initialY, double initialV, double initialTime) {
        this.initialV = initialV;
        this.initialY = initialY;

        this.y = initialY;
        this.v = initialV;
        this.livingTime = initialTime;
    }

    /**
     * Initialize a new particle with initial time equals to zero.
     *
     * @param initialY
     * @param initialV
     */
    public Particle(double initialY, double initialV) {
        this(initialY, initialV, 0);
    }

    public double getInitialY() {
        return initialY;
    }

    public double getInitialV() {
        return initialV;
    }

    public double getLivingTime() {
        return livingTime;
    }

    public double getY() {
        return y;
    }

    public double getV() {
        return v;
    }

    /**
     * This method returns true, if the particle hits the ground.
     * @return
     */
    public boolean hitsGround() {
        return this.y <= 0;
    }

    /**
     * Update the position and the velocity of the particle depending on the delta time.
     * Using the Euler algorithm.
     *
     * @param dt delta time
     */
    public void update(double dt) {
        this.y = this.y + this.v * dt;
        this.v = this.v - World.g * dt;

        this.livingTime += dt;
    }

    /**
     * Calculates the velocity of the particle by an analytical calculation.
     *
     * @param time The time.
     * @return Returns the analytical result.
     */
    public double analyticV(double time) {
        return this.initialV - World.g * time;
    }

    /**
     * Calculates the position of the particle by an analytical calculation.
     *
     * @param time The time.
     * @return Returns the analytical result.
     */
    public double analyticalY(double time) {
        return this.initialY + this.initialV * time - 0.5 * World.g * time * time;
    }

    @Override
    public String toString() {
        return "Particle[\n" +
                    "\tlivingTime = " + this.livingTime + "\n" +
                    "\ty = " + this.y + "\n" +
                    "\tv = " + this.v + "\n" +
                "]\n";
    }
}
