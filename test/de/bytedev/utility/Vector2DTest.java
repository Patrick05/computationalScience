package de.bytedev.utility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2DTest {

    @Test
    void createTest() {
        Vector2D v1 = new Vector2D();

        assertEquals(0, v1.getX());
        assertEquals(0, v1.getY());

        Vector2D v2 = new Vector2D(5,-7);

        assertEquals(5, v2.getX());
        assertEquals(-7, v2.getY());
    }

    @Test
    void lengthTest() {
        Vector2D v1 = new Vector2D();

        assertEquals(0, v1.getLength());

        Vector2D v2 = new Vector2D(5,-7);

        assertEquals(8.6023, v2.getLength(), 0.0001);

        Vector2D v3 = new Vector2D(5,0);

        assertEquals(5, v3.getLength());
    }

    @Test
    void addTest() {
        Vector2D v1 = new Vector2D(5,-7);
        Vector2D v2 = new Vector2D(-2,3);

        Vector2D v3 = Vector2D.add(v1, v2);

        assertEquals(3, v3.getX());
        assertEquals(-4, v3.getY());
    }

    @Test
    void subTest() {
        Vector2D v1 = new Vector2D(5,-7);
        Vector2D v2 = new Vector2D(-2,3);

        Vector2D v3 = Vector2D.sub(v1, v2);

        assertEquals(7, v3.getX());
        assertEquals(-10, v3.getY());
    }

    @Test
    void mulTest() {
        Vector2D v1 = new Vector2D(5,-7);

        Vector2D v2 = Vector2D.mul(v1, 2.5);

        assertEquals(12.5, v2.getX());
        assertEquals(-17.5, v2.getY());
    }
}
