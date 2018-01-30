package de.bytedev.exercise10;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Model implements Drawable {

    private List<Particle> particles;

    /**
     * Creates a new model with an empty list of particles.
     */
    public Model() {
        this.particles = new ArrayList<>();
    }

    /**
     * Adds a new particle to the system at the given position.
     *
     * @param x
     * @param y
     */
    public void addParticle(double x, double y, double charge) {
        this.addParticle(new Particle(x, y, charge));
    }

    /**
     * Adds a new particle to the system at the given position.
     *
     * @param position
     */
    public void addParticle(Particle position) {
        this.particles.add(position);
    }

    /**
     * Returns all particles of the system.
     *
     * @return
     */
    public List<Particle> getParticles() {
        return this.particles;
    }

    /**
     * Calculates the potential at the given position.
     *
     * @param position
     * @return
     */
    public double getPotentialAt(Vector2D position) {
        double potenial = 0;

        for (Particle particle: this.particles) {
            potenial += particle.getCharge()*Math.log(
                    Vector2D.sub(particle.getPosition(), position).getLength()
            );
        }

        return -potenial;
    }

    public double getPotentialFor(Particle p) {
        double potenial = 0;

        for (Particle particle: this.particles) {
            if(particle == p) continue;

            potenial += particle.getCharge()*Math.log(
                    Vector2D.distance(particle.getPosition(), p.getPosition())
            );
        }

        if(p.getCharge() > 0) {
            return -potenial;
        } else {
            return potenial;
        }
    }

    public boolean intersectsWithParticle(Particle p) {
        for (Particle particle: this.particles) {
            if(particle == p) continue;

            if( Vector2D.distance(p.getPosition(), particle.getPosition()) < 0.1 ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        for (Particle particle: this.particles ) {
            particle.draw(panel, g);
        }
    }
}
