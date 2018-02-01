package de.bytedev.exercise10;

import de.bytedev.utility.Vector2D;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.InteractiveMouseHandler;
import org.opensourcephysics.display.InteractivePanel;
import org.opensourcephysics.frames.DisplayFrame;

import java.awt.event.MouseEvent;


public class ElectrostaticField extends AbstractSimulation implements InteractiveMouseHandler {
    enum Param {
        CHARGE("charge");

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }


    private Model model;

    private DisplayFrame displayFrame;
    private Ellipse ellipse;

    public ElectrostaticField() {
        this.model = new Model();
        this.displayFrame = new DisplayFrame("Display");

        this.ellipse = new Ellipse(10, 6);

        this.displayFrame.addDrawable(this.ellipse);
        this.displayFrame.addDrawable(this.model);
        this.displayFrame.setInteractiveMouseHandler(this);
    }

    @Override
    public void reset() {
        this.control.setAdjustableValue(Param.CHARGE.toString(), 1);
    }

    @Override
    public void handleMouseAction(InteractivePanel panel, MouseEvent evt) {
        panel.handleMouseAction(panel, evt); // panel moves the charge

        if(panel.getMouseAction()==InteractivePanel.MOUSE_CLICKED) {

            if(panel.getMouseButton() == 1) { // Left mouse button
                this.model.addParticle( panel.getMouseX(), panel.getMouseY(), this.control.getDouble(Param.CHARGE.toString()));
            }

            panel.repaint();
        }
    }

    @Override
    protected void doStep() {
        // TODO

        for( Particle p : this.model.getParticles() ) {

            double oldPot = this.model.getPotentialFor(p);
            double newPot = Double.POSITIVE_INFINITY;
            int i = 0;

            while( oldPot < newPot && i < 100 ) {
                Vector2D direction = p.moveRandomly();

                newPot = this.model.getPotentialFor(p);

                if(oldPot < newPot || !this.ellipse.intersects(p.getPosition()) || this.model.intersectsWithParticle(p) ) {
                    p.move( direction.invert() );
                }

                i++;
            }
        }
    }



    public static void main(String[] args) {
        SimulationControl.createApp(new ElectrostaticField());
    }
}
