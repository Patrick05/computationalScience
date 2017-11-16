package de.bytedev.physics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GravityReceiverTest {

    private GravityReceiver gravityReceiver;

    @BeforeEach
    void beforeEach() {
        this.gravityReceiver = new GravityReceiver();
    }

    @Test
    void setPositionTest() {
        this.gravityReceiver.setPosition(5, 2);

        assertEquals(5, this.gravityReceiver.getPosition().getX());
        assertEquals(2, this.gravityReceiver.getPosition().getY());
    }
}