package de.bytedev.exercise3;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;

import java.awt.*;


public class Planet2App extends AbstractSimulation {
    enum Param {
        TIME_STEP("Time Step");

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
    private ExtendedPlotFrame energyFrame = new ExtendedPlotFrame("t", "E", "Energy");
    private ExtendedPlotFrame momentumFrame = new ExtendedPlotFrame("t", "L", "momentum");

    private PlanetGroup planetGroup;
    private double dt;
    private double t = 0;

    private int nrPlanets = 1;

    private static Color[] planetColors = new Color[]{
            Color.LIGHT_GRAY,
            Color.YELLOW,
            Color.BLUE,
            Color.RED,
            Color.PINK,
            Color.MAGENTA,
            Color.CYAN,
            Color.LIGHT_GRAY,
            Color.DARK_GRAY
    };

    /**
     * Constructs the PlanetApp.
     */
    public Planet2App() {
        this.frame.setPreferredMinMax(-10, 10, -10, 10);
        this.frame.setSquareAspect(true);

        this.planetGroup = new PlanetGroup();

        this.planetGroup.addSun(1);
        //this.planetGroup.addSun(1, new Vector2D(-6, 0));


        for(int i = 0; i<this.nrPlanets; i++) {
            switch(i) {
                case 0:
                    this.planetGroup.addPlanet(PlanetGroup.mRatio1, planetColors[0]);
                    break;
                case 1:
                    this.planetGroup.addPlanet(PlanetGroup.mRatio2, planetColors[1]);
                    break;
                default:
                    this.planetGroup.addPlanet(PlanetGroup.mRatio2, planetColors[i%9]);
                    break;
            }
        }
        this.planetGroup.initialize();
    }

    /**
     * Initializes the animation using the values in the control.
     */
    public void initialize() {
        this.dt = this.control.getDouble(Param.TIME_STEP.toString());

        double[] state = new double[4 * this.nrPlanets + 1];
        for(int i =0; i<this.nrPlanets; i++) {
            int j = 4*i;

            state[j] = this.control.getDouble("Planet "+i+": x");
            state[j+1] = 0;
            state[j+2] = 0;
            state[j+3] = this.control.getDouble("Planet "+i+": vy");
        }

        state[4*this.nrPlanets] = 0;

        this.planetGroup.setState(state);
        this.planetGroup.clearDrawables();
        this.frame.clearDrawables();
        this.frame.addDrawable(this.planetGroup);

        this.energyFrame.setConnected(false);
    }

    /**
     * Steps the time.
     */
    public void doStep() {
        this.planetGroup.update(this.dt);
        this.t += this.dt;

        for(int i = 0; i<this.nrPlanets; i++) {
            Planet p = this.planetGroup.getPlanet(i);
            int j = i*3;

            double potE = p.getPotentialEnergy();
            double kinE = p.getKineticEnergy();
            double totE = potE + kinE;
            double momentum = p.calcAngularMomentum().getZ();

            this.energyFrame.setMarkerColor(j, Color.red);
            this.energyFrame.append(j, this.t, potE);
            this.energyFrame.setMarkerColor(j+1, Color.blue);
            this.energyFrame.append(j+1, this.t, kinE);
            this.energyFrame.setMarkerColor(j+2, Color.green);
            this.energyFrame.append(j+2, this.t, totE);

            this.momentumFrame.setMarkerColor(i, planetColors[i]);
            this.momentumFrame.append(i, this.t, momentum);
        }
    }

    /**
     * Resets animation to a predefined state.
     */
    public void reset() {

        for(int i =0; i<this.nrPlanets; i++) {

            switch (i) {
                case 0:
                    this.control.setValue("Planet "+i+": x", 2.52);
                    this.control.setValue("Planet "+i+": vy", Math.sqrt(PlanetGroup.GMsun / 2.52));
                    break;
                case 1:
                    this.control.setValue("Planet "+i+": x", 5.24);
                    this.control.setValue("Planet "+i+": vy", Math.sqrt(PlanetGroup.GMsun / 5.24));
                    break;
                default:
                    this.control.setValue("Planet "+i+": x", i*4 + 3);
                    this.control.setValue("Planet "+i+": vy", Math.sqrt(PlanetGroup.GMsun / (i*4 + 3)));
            }


        }

        this.control.setValue(Param.TIME_STEP.toString(), 0.01);

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