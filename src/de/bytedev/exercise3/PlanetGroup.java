package de.bytedev.exercise3;

import de.bytedev.algorithms.ODE;
import de.bytedev.algorithms.RK45MultiStep;
import de.bytedev.utility.Vector2D;
import de.bytedev.utility.World;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;
import org.opensourcephysics.numerics.ODESolver;

import java.awt.*;

public class PlanetGroup implements ODE, Drawable {

    private Planet p1;
    private Planet p2;
    private double t;

    private ODESolver odeSolver;

    public PlanetGroup() {
        this.p1 = new Planet(World.GMplanet1);
        this.p2 = new Planet(World.GMplanet2);

        this.p1.addOtherPlanet(this.p2);
        this.p2.addOtherPlanet(this.p1);

        this.t = 0;

        this.odeSolver = new RK45MultiStep(this);
    }

    public void setState(Vector2D p1, Vector2D v1, Vector2D p2, Vector2D v2, double t) {

        this.p1.setPosition(
                p1.getX(),
                p1.getY()
        );

        this.p1.setVelocity(
                v1.getX(),
                v1.getY()
        );

        this.p2.setPosition(
                p2.getX(),
                p2.getY()
        );

        this.p2.setVelocity(
                v2.getX(),
                v2.getY()
        );

        this.t = t;
    }

    public void update(double dt) {
        this.odeSolver.setStepSize(dt);
        this.odeSolver.step();
    }

    @Override
    public void setState(double[] state) {
        this.p1.setState(
                state[0],
                state[1],
                state[2],
                state[3],
                state[8]
        );

        this.p2.setState(
                state[4],
                state[5],
                state[6],
                state[7],
                state[8]
        );
    }

    @Override
    public double[] getState() {
        return new double[] {
                this.p1.getPosition().getX(),
                this.p1.getVelocity().getX(),
                this.p1.getPosition().getY(),
                this.p1.getVelocity().getY(),
                this.p2.getPosition().getX(),
                this.p2.getVelocity().getX(),
                this.p2.getPosition().getY(),
                this.p2.getVelocity().getY(),
                this.t
        };
    }

    @Override
    public void getRate(double[] state, double[] rate) {
        // state[]: x1, vx1, y1, vy1, x2, vx2, y2, vy2, t
        Vector2D p1a = this.p1.calcAcceleration();
        Vector2D p2a = this.p2.calcAcceleration();

        rate[0] = this.p1.getVelocity().getX();
        rate[1] = p1a.getX();
        rate[2] = this.p1.getVelocity().getY();
        rate[3] = p1a.getY();
        rate[4] = this.p2.getVelocity().getX();
        rate[5] = p2a.getX();
        rate[6] = this.p2.getVelocity().getY();
        rate[7] = p2a.getY();
        rate[8] = 1;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        this.p1.draw(panel, g);
        this.p2.draw(panel, g);
    }
}
