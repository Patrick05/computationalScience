package de.bytedev.exercise6;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Walker2D implements Drawable {

    private List<Walker> walkers = new ArrayList<>();

    public void addWalker() {
        this.walkers.add(new Walker());
    }

    public void addWalkers(int amount) {
        for(int i=0; i<amount; i++) {
            this.addWalker();
        }
    }

    public List<Walker> getWalkers() {
        return this.walkers;
    }

    public void moveRandomly() {
        for(Walker walker : this.walkers) {
            walker.moveRandomly();
        }
    }

    public double calcAvgX() {
        int i = this.walkers.size();
        double posSum = 0.0;

        for(Walker walker : this.walkers) {
            posSum += walker.getPosition().getX();
        }

        return posSum/i;
    }

    public double calcAvgXSquared() {
        int i = this.walkers.size();
        double posSum = 0.0;

        for(Walker walker : this.walkers) {
            posSum += Math.pow(walker.getPosition().getX(), 2);
        }

        return posSum/i;
    }

    public double calcAvgY() {
        int i = this.walkers.size();
        double posSum = 0.0;

        for(Walker walker : this.walkers) {
            posSum += walker.getPosition().getY();
        }

        return posSum/i;
    }

    public double calcAvgYSquared() {
        int i = this.walkers.size();
        double posSum = 0.0;

        for(Walker walker : this.walkers) {
            posSum += Math.pow(walker.getPosition().getY(), 2);
        }

        return posSum/i;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        for(Walker walker : this.walkers) {
            walker.getCircle().draw(panel, g);
        }
    }
}
