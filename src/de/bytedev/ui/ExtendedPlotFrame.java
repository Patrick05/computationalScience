package de.bytedev.ui;

import org.opensourcephysics.frames.PlotFrame;

public class ExtendedPlotFrame extends PlotFrame {

    private int plotIndex = 0;

    public ExtendedPlotFrame(String xlabel, String ylabel, String frameTitle) {
        super(xlabel, ylabel, frameTitle);

        this.setConnected(true);
        this.setAutoclear(false);
    }

    public void newPlot() {
        this.plotIndex++;
    }

    public void append(double x, double y) {
        super.append(this.plotIndex, x, y);
    }
}
