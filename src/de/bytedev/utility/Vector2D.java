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
     * Copy constructor
     *
     * @param vector2D
     */
    public Vector2D(Vector2D vector2D) {
        this(vector2D.getX(), vector2D.getY());
    }

    /**
     * Zero-Constructor. Creates a vector without dimension or orientation.
     */
    public Vector2D() {
        this(0,0);
    }

    /**
     * Constructor for vector by angle.
     *
     * @param angle
     */
    public Vector2D(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
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
     * Inverts the direction of the vector.
     */
    public Vector2D invert() {
        this.x *= -1;
        this.y *= -1;

        return this;
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

    /**
     * Resets the vector to origin (0|0)
     */
    public void setToOrigin() {
        this.x = 0;
        this.y = 0;
    }
}
