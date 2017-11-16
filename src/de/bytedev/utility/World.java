package de.bytedev.utility;

public class World {
    public static final double g = 9.81;
    public static final double G = 6.674 * 1.0E-11;

    public static final double rEarth = 6.37 * 1.0E6;
    public static final double mEarth = 5.9722 * 1.0E24;

    public static final double mSun = 1.989 * 1.0E30;
    public static final double mMercury = 3.285 * 1.0E23;
    public static final double mVenus = 4.876 * 1.0E24;

    // GM in units of (AU)^3/(yr)^2
    public static final double GMsun = 4 * Math.PI * Math.PI;
    public static final double GMplanet1 = 0.04 * GMsun;
    public static final double GMplanet2 = 0.001 * GMsun;


    public static double gravityAt(double yAboveZero, double mParticle) {
        return (mParticle * World.mEarth * World.G) / Math.pow(World.rEarth + yAboveZero, 2);
    }

    public static double accelerationAt(double yAboveZero) {
        return (World.mEarth * World.G) / Math.pow(World.rEarth + yAboveZero, 2);
    }
}
