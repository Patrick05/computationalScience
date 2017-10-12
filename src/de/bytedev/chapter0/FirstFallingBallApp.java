/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

package de.bytedev.chapter0;

import org.opensourcephysics.controls.AbstractCalculation;
import org.opensourcephysics.controls.CalculationControl;

/**
 * FirstFallingBallApp computes the time for a ball to fall 10 meters and displays the variables.
 *
 * @author Wolfgang Christian, Jan Tobochnik, Harvey Gould
 * @version 1.0
 */
public class FirstFallingBallApp extends AbstractCalculation {

    enum Types {
        INITIALTIME("Initial Time"),
        TIMESTEP("Time Step");

        private String v;

        Types(String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return v;
        }
    }

    /**
     * Starts the Java application.
     *
     * @param args command line parameters
     */
    public static void main(String[] args) {
        CalculationControl.createApp(new FirstFallingBallApp());
    }

    @Override
    public void calculate() {
        double t = this.control.getDouble(Types.INITIALTIME.toString());     // time
        double dt = this.control.getDouble(Types.TIMESTEP.toString()); // time step

        Particle p = new Particle(10, 0, t);

        for (int n = 0; n < 100; n++) {
            p.update(dt);
            t = t + dt;
        }

        this.control.println("Results");
        this.control.println("final time = " + t);
        this.control.println("numerical:\n" + p.toString() );
        this.control.println("analytic: y = " + p.analyticalY(t) + " v = " + p.analyticV(t));
    }

    @Override
    public void reset() {
        this.control.setValue(Types.INITIALTIME.toString(), 0);
        this.control.setValue(Types.TIMESTEP.toString(), 0.01);
    }
}

/* 
 * Open Source Physics software is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation; either version 2 of the License,
 * or(at your option) any later version.

 * Code that uses any portion of the code in the org.opensourcephysics package
 * or any subpackage (subdirectory) of this package must must also be be released
 * under the GNU GPL license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston MA 02111-1307 USA
 * or view the license online at http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2007  The Open Source Physics project
 *                     http://www.opensourcephysics.org
 */
