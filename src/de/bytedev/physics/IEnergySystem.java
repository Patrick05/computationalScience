package de.bytedev.physics;

/**
 * This interface should be implemented by an energy system.
 */
public interface IEnergySystem {

    /**
     * Returns the potential energy of the energy system.
     *
     * @return
     */
    double getPotentialEnergy();

    /**
     * Returns the kinetic energy of the energy system.
     *
     * @return
     */
    double getKineticEnergy();
}
