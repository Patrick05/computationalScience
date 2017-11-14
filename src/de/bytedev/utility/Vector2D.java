package de.bytedev.utility;

/**
 * A simple Vector on a 2D-Plane
 */
public class Vector2D {

    private double x;
    private double y;

    /**
     * Constructor for an specific x,y-Vector
     *
     * @param x
     * @param y
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Zero-Constructor. Creates a vector without dimension or orientation.
     */
    public Vector2D() {
        this(0,0);
    }

    /**
     * Returns the x-value of the vector.
     *
     * @return
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y-value of the vector
     * @return
     */
    public double getY() {
        return this.y;
    }

    /**
     * Returns the length of the vector
     *
     * @return
     */
    public double getLength() {
        return Math.sqrt( Math.pow(this.x, 2) + Math.pow(this.y, 2) );
    }

    /**
     * Adds the given vector to this one.
     *
     * @param a
     */
    public void add(Vector2D a) {
        this.x += a.getX();
        this.y += a.getY();
    }

    /**
     * Subtract the given vector from this one.
     *
     * @param a
     */
    public void sub(Vector2D a) {
        this.x -= a.getX();
        this.y -= a.getY();
    }

    /**
     * Multiply this vector with the given value.
     *
     * @param v the value to multiply
     */
    public void mul(double v) {
        this.x *= v;
        this.y *= v;
    }

    /**
     * Static method to add two vectors and put the result in a new object.
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(
                a.getX() + b.getX(),
                a.getY() + b.getY()
        );
    }

    /**
     * Static method to subtract two vectors and put the result in a new object.
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(
                a.getX() - b.getX(),
                a.getY() - b.getY()
        );
    }

    /**
     * Static method to multiply a vector with a value and put the result in a new object.
     *
     * @param a
     * @param v
     * @return
     */
    public static Vector2D mul(Vector2D a, double v) {
        return new Vector2D(
                a.getX() * v,
                a.getY() * v
        );
    }
}
