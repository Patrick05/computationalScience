package de.bytedev.exercise3;

import de.bytedev.physics.GravityReceiver;
import de.bytedev.physics.IEnergySystem;
import de.bytedev.physics.IGravityObject;
import de.bytedev.ui.TrailedCircle;
import de.bytedev.utility.Vector2D;
import de.bytedev.utility.Vector3D;

import java.awt.*;
import java.util.List;

public class Planet extends GravityReceiver implements IGravityObject, IEnergySystem {

    private Vector2D velocity;
    private double mass;

    private TrailedCircle circle;

    /**
     * Default constructor which creates a planet without mass and velocity at (0|0).
     */
    public Planet() {
        super();
        this.mass = 0;
        this.velocity = new Vector2D();

        this.circle = new TrailedCircle();
    }

    /**
     * Creates a planet with the given mass but without velocity at (0|0).
     *
     * @param mass the mass of the planet
     */
    public Planet(double mass) {
        super();
        this.mass = mass;
        this.velocity = new Vector2D();

        this.circle = new TrailedCircle();
    }

    /**
     * Creates a planet with the given mass, velocity at the given position.
     *
     * @param mass the mass of the planet
     * @param position the initial position of the planet
     * @param velocity the initial velocity of the planet
     */
    public Planet(double mass, Vector2D position, Vector2D velocity) {
        super(position);
        this.mass = mass;
        this.velocity = velocity;

        this.circle = new TrailedCircle(position.getX(), position.getY());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMass() {
        return this.mass;
    }

    /**
     * Sets the mass of the planet by the given value.
     *
     * @param mass the new mass of the planet
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Sets the position of the planet.
     *
     * @param position the position of the planet
     */
    public void setPosition(Vector2D position) {
        super.setPosition(position);
        this.circle.setXY(position.getX(), position.getY());
    }

    /**
     * Sets the position of the planet.
     *
     * @param x the x-coordinate of the planet position
     * @param y the y-coordinate of the planet position
     */
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        this.circle.setXY(x, y);
    }

    /**
     * Returns the velocity of the planet.
     *
     * @return the current velocity
     */
    public Vector2D getVelocity() {
        return this.velocity;
    }

    /**
     * Sets the velocity of the planet.
     *
     * @param velocity the velocity vector
     */
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Sets the velocity of the planet.
     *
     * @param x the velocity in x-direction
     * @param y the velocity in y-direction
     */
    public void setVelocity(double x, double y) {
        this.velocity = new Vector2D(x, y);
    }

    /**
     * Sets the color of the TrailedCircle-drawable.
     *
     * @param c
     */
    public void setCircleColor(Color c) {
        this.circle.color = c;
    }

    /**
     * Returns the TrailedCircle-drawable.
     *
     * @return the trailed circle
     */
    public TrailedCircle getCircle() {
        return circle;
    }

    /**
     * Calculates the angular momentum of the planet.
     *
     * @return
     */
    public Vector3D calcAngularMomentum() {
        Vector3D pos3d = new Vector3D(this.getPosition());
        Vector3D vel3d = new Vector3D(this.getVelocity());

        return Vector3D.cross(
                pos3d,
                Vector3D.mul(vel3d, this.getMass())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPotentialEnergy() {
        List<IGravityObject> gravityObjectList = this.getGravityObjects();
        double potEnergy = 0;

        for(IGravityObject gravityObject : gravityObjectList) {
            potEnergy -= (gravityObject.getMass() * PlanetGroup.GMsun * this.getMass()) / this.getPosition().getLength();
        }

        return potEnergy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getKineticEnergy() {
        Vector2D v = this.getVelocity();
        return 0.5 * this.getMass() * (Math.pow(v.getX(), 2) + Math.pow(v.getY(),2));
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public void setState(double x, double vx, double y, double vy) {
        this.setPosition(x, y);
        this.setVelocity(vx, vy);
    }
}
