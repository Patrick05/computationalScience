package de.bytedev.exercise8;

import de.bytedev.ui.ExtendedPlotFrame;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.controls.XML;
import org.opensourcephysics.display.GUIUtils;
import org.opensourcephysics.frames.DisplayFrame;
import org.opensourcephysics.frames.HistogramFrame;

/**
 * Info: Copied and edited from OSP
 */
public class LJParticlesApp extends AbstractSimulation {
    enum Param {
        NX("nx"),
        NY("ny"),
        LX("Lx"),
        LY("Ly"),
        DT("dt"),
        INITIAL_KINETIC_ENERGY_PP("initial kinetic energy per particle"),
        INITIAL_CONFIGURATION("initial configuration");

        private final String v;

        Param(final String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    private LJParticles model;
    private double dt;

    private ExtendedPlotFrame pressureData = new ExtendedPlotFrame("time", "PA/NkT", "Mean pressure");
    private ExtendedPlotFrame temperatureData = new ExtendedPlotFrame("time", "temperature", "Mean temperature");
    private ExtendedPlotFrame energyData = new ExtendedPlotFrame("time", "energy", "Mean energy");

    private HistogramFrame xVelocityHistogram = new HistogramFrame("vx", "H(vx)", "Velocity histogram");
    private DisplayFrame display = new DisplayFrame("x", "y", "Lennard-Jones system");

    @Override
    public void initialize() {
        this.model = new LJParticles(
                this.control.getInt(Param.NX.toString()),
                this.control.getInt(Param.NY.toString()),
                this.control.getDouble(Param.LX.toString()),
                this.control.getDouble(Param.LY.toString())
        );
        this.model.setInitialConfiguration(
                this.control.getString(Param.INITIAL_CONFIGURATION.toString())
        );
        this.model.setInitialKineticEnergy(
                this.control.getDouble(Param.INITIAL_KINETIC_ENERGY_PP.toString())
        );
        this.model.initialize();

        this.dt = control.getDouble(Param.DT.toString());

        this.display.addDrawable(this.model);
        this.display.setPreferredMinMax(
                0,
                this.model.getLx(),
                0,
                this.model.getLy()
        ); // assumes vmax = 2*initalTemp and bin width = Vmax/N

        this.xVelocityHistogram.setBinWidth(2 * this.model.getInitialKineticEnergy() / this.model.getN());
    }

    @Override
    public void doStep() {
        this.model.update(this.dt, this.xVelocityHistogram);

        if(this.model.isEquilibrium()) {
            this.pressureData.append(this.model.getTime(), this.model.getMeanPressure());
            this.temperatureData.append(this.model.getTime(), this.model.getMeanTemperature());
            this.energyData.append(this.model.getTime(), this.model.getMeanEnergy());

            this.display.setMessage("step = "+ this.model.getSteps() +"; isEquilibrium = true");
        } else {
            this.display.setMessage("step = "+ this.model.getSteps() +"; isEquilibrium = false");
        }

    }

    @Override
    public void stop() {
        this.control.println("Density = " + decimalFormat.format(this.model.getRho()));
        this.control.println("Number of time steps = " + this.model.getSteps());
        this.control.println("Time step dt = " + decimalFormat.format(this.dt));
        this.control.println("<T>= " + decimalFormat.format(model.getMeanTemperature()));
        this.control.println("<E> = " + decimalFormat.format(model.getMeanEnergy()));
        this.control.println("Heat capacity = " + decimalFormat.format(model.getHeatCapacity()));
        this.control.println("<PA/NkT> = " + decimalFormat.format(model.getMeanPressure()));
    }

    @Override
    public void startRunning() {
        this.dt = this.control.getDouble(Param.DT.toString());
        double Lx = this.control.getDouble(Param.LX.toString());
        double Ly = this.control.getDouble(Param.LY.toString());
        if ((Lx != this.model.getLx()) || (Ly != this.model.getLy())) {
            this.model.setLx(Lx);
            this.model.setLy(Ly);
            this.model.computeAcceleration();
            this.display.setPreferredMinMax(0, Lx, 0, Ly);
            this.resetData();
        }
    }

    @Override
    public void reset() {
        this.control.setValue(Param.NX.toString(), 8);
        this.control.setValue(Param.NY.toString(), 8);

        this.control.setAdjustableValue(Param.LX.toString(), 12.0);
        this.control.setAdjustableValue(Param.LY.toString(), 12.0);

        this.control.setAdjustableValue(Param.DT.toString(), 0.001);

        this.control.setValue(Param.INITIAL_KINETIC_ENERGY_PP.toString(), 1.0);
        this.control.setValue(Param.INITIAL_CONFIGURATION.toString(), "rectangular");

        this.enableStepsPerDisplay(true);
        super.setStepsPerDisplay(100);  // draw configurations every 10 steps

        this.display.clearDrawables();
        this.display.addDrawable(this.model);
        this.display.setSquareAspect(true); // so particles will appear as circular disks
    }

    /**
     * Resets the LJ model and the data graphs.
     * <p>
     * This method is invoked using a custom button.
     */
    public void resetData() {
        this.model.resetAverages();
        GUIUtils.clearDrawingFrameData(false); // clears old data from the plot frames
    }

    /**
     * Returns an XML.ObjectLoader to save and load data for this program.
     * <p>
     * LJParticle data can now be saved using the Save menu item in the control.
     *
     * @return the object loader
     */
    public static XML.ObjectLoader getLoader() {
        return new LJParticlesLoader();
    }

    /**
     * Returns the ParticleModel
     *
     * @return
     */
    public IParticleModel getModel() {
        return this.model;
    }

    /**
     * Starts the Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        SimulationControl control = SimulationControl.createApp(new LJParticlesApp());
        control.addButton("resetData", "Reset Data");
    }
}