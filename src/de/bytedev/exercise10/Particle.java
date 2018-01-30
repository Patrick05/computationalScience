package de.bytedev.exercise10;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Circle;
import org.opensourcephysics.display.InteractiveCircle;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Particle extends InteractiveCircle {

    private double charge;

    public Particle(double x, double y, double charge) {
        super(x, y);
        this.charge = charge;

        if(this.charge > 0) {
            this.color = Color.RED;
        } else {
            this.color = Color.BLUE;
        }
    }

    public double getCharge() {
        return this.charge;
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
