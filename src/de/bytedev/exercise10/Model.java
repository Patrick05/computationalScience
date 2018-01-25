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
    public void addParticle(double x, double y) {
        this.addParticle(new Particle(x, y));
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
            potenial += Math.log(
                    Vector2D.sub(particle.getPosition(), position).getLength()
            );
        }

        return -potenial;
    }

    public double getPotentialFor(Particle p) {
        double potenial = 0;

        for (Particle particle: this.particles) {
            if(particle == p) continue;

            potenial += Math.log(
                    Vector2D.sub(particle.getPosition(), p.getPosition()).getLength()
            );
        }

        return -potenial;
    }

    @Override
    public void draw(DrawingPanel panel, Graphics g) {
        for (Particle particle: this.particles ) {
            particle.draw(panel, g);
        }
    }
}
