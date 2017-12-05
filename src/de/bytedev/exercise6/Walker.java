package de.bytedev.exercise6;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Circle;

public class Walker {
    private Vector2D position;
    private Circle circle;

    public Walker() {
        this.position = new Vector2D();
        this.circle = new Circle();
    }

    public void moveRandomly() {
        double rand = Math.random();

        if( 0.0 <= rand && rand < 0.25 ) {
            this.position.add(new Vector2D(1, 0));
        } else if ( 0.25 <= rand && rand < 0.5 ) {
            this.position.add(new Vector2D(0, -1));
        } else if ( 0.5 <= rand && rand < 0.75 ) {
            this.position.add(new Vector2D(-1, 0));
        } else if ( 0.75 <= rand && rand <= 1.0 ) {
            this.position.add(new Vector2D(0, 1));
        }

        this.circle.setXY(this.position.getX(), this.position.getY());
    }

    public Vector2D getPosition() {
        return position;
    }

    public Circle getCircle() {
        return circle;
    }
}
