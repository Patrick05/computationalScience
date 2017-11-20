package de.bytedev.utility;

/**
 * A simple 3D-Vector
 */
public class Vector3D {

    private double x;
    private double y;
    private double z;

    /**
     * Constructor for an specific x,y,z-Vector
     *
     * @param x
     * @param y
     * @param z
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a 3d-vector from the given 2d-vector
     *
     * @param v
     */
    public Vector3D(Vector2D v) {
        this(v.getX(), v.getY(), 0);
    }

    /**
     * Zero-Constructor. Creates a vector without dimension or orientation.
     */
    public Vector3D() {
        this(0,0,0);
    }

    /**
     * Returns the x-value of the vector
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-value of the vector
     *
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the z-value of the vector
     *
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * Adds the given vector to this one.
     *
     * @param a
     */
    public void add(Vector3D a) {
        this.x += a.getX();
        this.y += a.getY();
        this.z += a.getZ();
    }

    /**
     * Subtract the given vector from this one.
     *
     * @param a
     */
    public void sub(Vector3D a) {
        this.x -= a.getX();
        this.y -= a.getY();
        this.z -= a.getZ();
    }

    /**
     * Multiply this vector with the given value.
     *
     * @param v the value to multiply
     */
    public void mul(double v) {
        this.x *= v;
        this.y *= v;
        this.z *= v;
    }

    /**
     * Static method to add two vectors and put the result in a new object.
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(
                a.getX() + b.getX(),
                a.getY() + b.getY(),
                a.getZ() + b.getZ()
        );
    }

    /**
     * Static method to subtract two vectors and put the result in a new object.
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector3D sub(Vector3D a, Vector3D b) {
        return new Vector3D(
                a.getX() - b.getX(),
                a.getY() - b.getY(),
                a.getZ() - b.getZ()
        );
    }

    /**
     * Static method to multiply a vector with a value and put the result in a new object.
     *
     * @param a
     * @param v
     * @return
     */
    public static Vector3D mul(Vector3D a, double v) {
        return new Vector3D(
                a.getX() * v,
                a.getY() * v,
                a.getZ() * v
        );
    }

    /**
     * Static method to calculate the cross product of the two given vectors.
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector3D cross(Vector3D a, Vector3D b) {
        return new Vector3D(
            a.getY()*b.getZ() - a.getZ()*b.getY(),
            a.getZ()*b.getX() - a.getX()*b.getZ(),
            a.getX()*b.getY() - a.getY()*b.getX()
        );
    }
}
