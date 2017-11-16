package de.bytedev.physics;

import de.bytedev.utility.Vector2D;

/**
 * This interface should be used to implement by an object with gravity.
 */
public interface IGravityObject {

    /**
     * Returns the position of the object on a 2D-Plane.
     *
     * @return position vector
     */
    public Vector2D getPosition();

    /**
     * Returns the mass of the object.
     *
     * @return the mass
     */
    public double getMass();
}
