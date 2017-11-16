package de.bytedev.exercise3;

import de.bytedev.ui.ExtendedPlotFrame;
import de.bytedev.utility.World;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;


public class Planet2App extends AbstractSimulation {
    enum Param {
        TIME_STEP("Time Step"),
        X1("x1"),
        X2("x2"),
        VY1("vy1"),
        VY2("vy2"),
        ENABLE_SUN_GRAV("Enable Sun Gravity"),
        ENABLE_PLANET_GRAV("Enable Planet Gravity"),
        ;

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private ExtendedPlotFrame frame = new ExtendedPlotFrame("x (AU)", "y (AU)", "Planet Program");

    private PlanetGroup planetGroup;
    private double dt;

    public static boolean SUN_GRAV_ENABLED;
    public static boolean PLANET_GRAV_ENABLED;

    /**
     * Constructs the PlanetApp.
     */
    public Planet2App() {
        this.frame.setPreferredMinMax(-10, 10, -10, 10);
        this.frame.setSquareAspect(true);

        this.planetGroup = new PlanetGroup();
    }

    /**
     * Initializes the animation using the values in the control.
     */
    public void initialize() {
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());

        // state[]: x1, vx1, y1, vy1, x2, vx2, y2, vy2, t
        this.planetGroup.setState(
                new double[] {
                        this.control.getDouble(Param.X1.toString()),
                        0,
                        0,
                        this.control.getDouble(Param.VY1.toString()),
                        this.control.getDouble(Param.X2.toString()),
                        0,
                        0,
                        this.control.getDouble(Param.VY2.toString()),
                        0
                }
        );

        Planet2App.SUN_GRAV_ENABLED = this.control.getInt(Param.ENABLE_SUN_GRAV.toString()) != 0;
        Planet2App.PLANET_GRAV_ENABLED = this.control.getInt(Param.ENABLE_PLANET_GRAV.toString()) != 0;

        this.planetGroup.clearDrawables();
        this.frame.clearDrawables();
        this.frame.addDrawable(this.planetGroup);
    }

    /**
     * Steps the time.
     */
    public void doStep() {
        this.planetGroup.update(this.dt);
    }


    /**
     * Resets animation to a predefined state.
     */
    public void reset() {
        this.control.setValue(Param.X1.toString(), 2.52);
        this.control.setValue(Param.VY1.toString(), Math.sqrt(World.GMsun / 2.52));
        this.control.setValue(Param.X2.toString(), 5.24);
        this.control.setValue(Param.VY2.toString(), Math.sqrt(World.GMsun / 5.24));
        this.control.setValue(Param.TIME_STEP.toString(), 0.01);

        this.control.setValue(Param.ENABLE_SUN_GRAV.toString(), 1);
        this.control.setValue(Param.ENABLE_PLANET_GRAV.toString(), 1);

        this.enableStepsPerDisplay(true);
        // initialize();
    }

    /**
     * Start Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        SimulationControl.createApp(new Planet2App());
    }
}