package de.bytedev.exercise1;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

public class ProjectileApp extends AbstractSimulation {
    enum Param {
        INITIAL_ANGLE("Initial angle"),
        INITIAL_X("Initial X"),
        INITIAL_Y("Initial Y"),
        INITIAL_VELOCITY("Initial Velocity"),
        PROJECTILE_MASS("Projectile mass"),
        PROJECTILE_C2("Projectile C2"),
        TIME_STEP("Time Step"),
        USE_DYNAMIC_GRAVITY("Use dynamic gravity"),
        USE_AIR_EFFECTS("Use air effects");

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private ExtendedPlotFrame plotFrame = new ExtendedPlotFrame("x", "y", "Trajectory");
    private Projectile projectile;

    private double t;
    private double dt;

    public static boolean USE_DYNAMIC_GRAVITY;
    public static boolean USE_AIR_EFFECTS;

    @Override
    public void initialize() {
        this.t = 0;
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());
        USE_DYNAMIC_GRAVITY = this.control.getBoolean(Param.USE_DYNAMIC_GRAVITY.toString());
        USE_AIR_EFFECTS = this.control.getBoolean(Param.USE_AIR_EFFECTS.toString());

        this.projectile =  new Projectile(
                this.control.getDouble(Param.INITIAL_X.toString()),
                this.control.getDouble(Param.INITIAL_Y.toString()),
                this.control.getDouble(Param.INITIAL_ANGLE.toString()),
                this.control.getDouble(Param.INITIAL_VELOCITY.toString()),
                this.control.getDouble(Param.PROJECTILE_MASS.toString()),
                this.control.getDouble(Param.PROJECTILE_C2.toString())
        );

        this.plotFrame.addDrawable(this.projectile);
        this.plotFrame.newPlot();
    }

    @Override
    public void doStep() {
        this.plotFrame.append(
                this.projectile.getX(),
                this.projectile.getY()
        );

        this.projectile.update(this.dt);

        if( this.projectile.hitsGround() ) {
            this.control.calculationDone(
                    "Projectile hits the ground at ("+this.projectile.getX()+"|"+this.projectile.getY()+") after "+this.t+"s!\n" +
                            "Analytical Rmax = "+this.projectile.analyticRmax()
            );
        }

        this.t += this.dt;
    }

    @Override
    public void reset() {
        this.control.setValue(Param.TIME_STEP.toString(), 0.01);
        this.control.setValue(Param.INITIAL_X.toString(), 0);
        this.control.setValue(Param.INITIAL_Y.toString(), 0);
        this.control.setValue(Param.INITIAL_ANGLE.toString(), 45);
        this.control.setValue(Param.INITIAL_VELOCITY.toString(), 15);
        this.control.setValue(Param.PROJECTILE_MASS.toString(), 7);
        this.control.setValue(Param.PROJECTILE_C2.toString(), 0.1);
        this.control.setValue(Param.USE_DYNAMIC_GRAVITY.toString(), false);
        this.control.setValue(Param.USE_AIR_EFFECTS.toString(), false);

        this.enableStepsPerDisplay(true);
    }

    public static void main(String[] args) {
        SimulationControl.createApp(new ProjectileApp());
    }
}