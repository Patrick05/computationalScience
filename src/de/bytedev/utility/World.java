package de.bytedev.utility;

public class World {
    public static final double g = 9.81;
    public static final double G = 6.674 * 1.0E-11;

    public static final double rEarth = 6.37 * 1.0E6;
    public static final double mEarth = 5.9722 * 1.0E24;


    public static double gravityAt(double yAboveZero, double mParticle) {
        return (mParticle * World.mEarth * World.G) / Math.pow(World.rEarth + yAboveZero, 2);
    }

    public static double accelerationAt(double yAboveZero) {
        return (World.mEarth * World.G) / Math.pow(World.rEarth + yAboveZero, 2);
    }
}
