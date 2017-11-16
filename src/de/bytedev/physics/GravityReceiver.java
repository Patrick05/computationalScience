package de.bytedev.physics;

import de.bytedev.utility.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * The GravityReceiver is an object, which is accelerated by other gravity objects.
 */
public class GravityReceiver {

    private Vector2D position;
    private List<IGravityObject> gravityObjects;
    private double GMsun = 4 * Math.PI * Math.PI;

    /**
     * Default constructor
     */
    public GravityReceiver() {
        this.position = new Vector2D();
        this.gravityObjects = new ArrayList<>();
    }

    /**
     * This constructor sets the initial position of the GravityReceiver.
     *
     * @param position the position of the GravityReceiver
     */
    public GravityReceiver(Vector2D position) {
        this.position = position;
        this.gravityObjects = new ArrayList<>();
    }

    /**
     * Returns the position of the GravityReceiver.
     *
     * @return the position of the GravityReceiver
     */
    public Vector2D getPosition() {
        return this.position;
    }

    /**
     * Sets the position of the GravityReceiver.
     *
     * @param position the position of the GravityReceiver
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Sets the position of the GravityReceiver.
     *
     * @param x the x-coordinate of the GravityReceiver
     * @param y the y-coordinate of the GravityReceiver
     */
    public void setPosition(double x, double y) {
        this.position = new Vector2D(x, y);
    }

    /**
     * Adds the given gravity object.
     *
     * @param gravityObject
     */
    public void addGravityObject(IGravityObject gravityObject) {
        if( ! this.gravityObjects.contains(gravityObject) ) {
            this.gravityObjects.add(gravityObject);
        }
    }

    /**
     * Remove the given gravity object.
     *
     * @param gravityObject
     */
    public void removeGravityObject(IGravityObject gravityObject) {
        if( this.gravityObjects.contains(gravityObject) ) {
            this.gravityObjects.remove(gravityObject);
        }
    }

    /**
     * Calculates the acceleration of the GravityReceiver by the gravity objects.
     *
     * @return the acceleration vector
     */
    public Vector2D calcAccelerationByGravity() {
        Vector2D a = new Vector2D();

        for( IGravityObject gravityObject : this.gravityObjects ) {
            Vector2D r = Vector2D.sub( this.getPosition(), gravityObject.getPosition() );
            r.mul( - gravityObject.getMass() * this.GMsun / Math.pow(r.getLength(), 3) );
            a.add(r);
        }

        return a;
    }

}
