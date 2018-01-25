package de.bytedev.exercise10;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Circle;

import java.util.concurrent.ThreadLocalRandom;

public class Particle extends Circle {

    public Particle(double x, double y) {
        super(x, y);
    }

    public Vector2D moveRandomly() {
        double randAngle = ThreadLocalRandom.current().nextDouble(0, 2*Math.PI);

        Vector2D direction = new Vector2D(randAngle);

        double randScale = ThreadLocalRandom.current().nextDouble(0, 1);

        direction.mul(randScale);

        this.move(direction);
        return direction;
    }

    public void move(Vector2D delta) {
        this.setXY(
                this.getX() + delta.getX(),
                this.getY() + delta.getY()
        );
    }

    public Vector2D getPosition() {
        return new Vector2D(this.x, this.y);
    }

}
