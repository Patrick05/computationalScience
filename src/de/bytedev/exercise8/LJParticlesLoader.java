package de.bytedev.exercise8;

import org.opensourcephysics.controls.XML;
import org.opensourcephysics.controls.XMLControl;
import org.opensourcephysics.display.GUIUtils;

/**
 * Info: Copied and edited from OSP
 */
public class LJParticlesLoader implements XML.ObjectLoader {

    /**
     * Creates a LJParticlesApp object.
     *
     * @param element the xml control
     * @return a new object
     */
    public Object createObject(XMLControl element) {
        return new LJParticlesApp();
    }

    /**
     * Saves data from the LJParticlesApp model into the control.
     *
     * @param control XMLControl
     * @param obj     Object
     */
    public void saveObject(XMLControl control, Object obj) {
        IParticleModel model = ((LJParticlesApp) obj).getModel();
        control.setValue("initial_configuration", model.getInitialConfiguration());
        control.setValue("state", model.getState());
        control.setValue("ax", model.getAx());
        control.setValue("ay", model.getAy());
    }

    /**
     * Loads data from the control into the LJParticlesApp model.
     *
     * @param control XMLControl
     * @param obj     Object
     * @return Object
     */
    public Object loadObject(XMLControl control, Object obj) {
        // GUI has been loaded with the saved values; now restore the LJ state
        LJParticlesApp app = (LJParticlesApp) obj;
        IParticleModel model = app.getModel();

        app.initialize(); // reads values from the GUI into the LJ model

        model.setInitialConfiguration(control.getString("initial_configuration"));
        model.setState((double[]) control.getObject("state"));
        model.setAx((double[]) control.getObject("ax"));
        model.setAy((double[]) control.getObject("ay"));

        model.resetAverages();

        GUIUtils.clearDrawingFrameData(false); // clears old data from the plot frames
        return obj;
    }
}