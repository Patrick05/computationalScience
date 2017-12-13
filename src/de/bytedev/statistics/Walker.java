package de.bytedev.statistics;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Circle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class describes a 2d walker.
 */
public class Walker {
    /**
     * The directions to move the walker.
     */
    public enum Direction {
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);

        private Vector2D delta;

        Direction(double deltaX, double deltaY) {
            this.delta = new Vector2D(deltaX, deltaY);
        }

        public Vector2D getDelta() {
            return delta;
        }
    }

    private List<Vector2D> history;
    private Vector2D position;
    private Circle circle;

    /**
     * Default constructor of the walker.
     * Initialize position, drawable and history-list.
     */
    public Walker() {
        this.position = new Vector2D();
        this.circle = new Circle();
        this.history = new LinkedList<>();
    }

    /**
     * Returns the current position of the walker.
     *
     * @return
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Returns the circle-drawable of the walker.
     *
     * @return
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * Returns the position history of the walker.
     *
     * @return
     */
    public List<Vector2D> getHistory() {
        return history;
    }

    /**
     * Returns the neighbour position in the given direction.
     *
     * @param direction
     * @return
     */
    public Vector2D getNeighbour(Direction direction) {
        return Vector2D.add(this.position, direction.getDelta());
    }

    /**
     * Move the walker in the given direction.
     *
     * @param direction
     */
    public void move(Direction direction) {
        this.history.add(new Vector2D(this.position));
        this.position.add(direction.getDelta());
        this.circle.setXY(this.position.getX(), this.position.getY());
    }

    /**
     * Resets the walker and delete the history.
     */
    public void reset() {
        this.history.clear();
        this.position.setToOrigin();
        this.circle.setXY(0, 0);
    }

    /**
     * Move the walker in a random direction.
     */
    public void moveRandomly() {
        int rand = ThreadLocalRandom.current().nextInt(0, Direction.values().length);

        switch (rand) {
            case 0:
                this.move(Direction.UP);
                break;
            case 1:
                this.move(Direction.RIGHT);
                break;
            case 2:
                this.move(Direction.DOWN);
                break;
            case 3:
                this.move(Direction.LEFT);
                break;
        }
    }

    /**
     * Move the walker in a random direction but avoid the given positions.
     *
     * @param avoidingPositions
     * @return
     */
    public double moveRandomlyAvoiding(List<Vector2D> avoidingPositions) {
        List<Direction> possibleDirections = new ArrayList<>();

        for(Direction direction : Direction.values()) {
            boolean isInList = false;
            for(Vector2D avoidingPosition : avoidingPositions) {
                if(avoidingPosition.getX() == this.getNeighbour(direction).getX() &&
                   avoidingPosition.getY() == this.getNeighbour(direction).getY()) {
                    isInList = true;
                    break;
                }
            }

            if(!isInList) {
                possibleDirections.add(direction);
            }
        }

        if(possibleDirections.size() == 0) {
            return 0;
        }

        int choosenDirection = ThreadLocalRandom.current().nextInt(0, possibleDirections.size());
        this.move(possibleDirections.get(choosenDirection));
        return possibleDirections.size()/3.0;
    }

    /**
     * Move the walker in a random direction but avoid the given one.
     *
     * @param avoidingPosition
     * @return
     */
    public double moveRandomlyAvoiding(Vector2D avoidingPosition) {
        List<Vector2D> avoidingPositions = new ArrayList<>();
        avoidingPositions.add(avoidingPosition);
        return this.moveRandomlyAvoiding(avoidingPositions);
    }
}
