package org.maksymov.clock;

import static java.util.Objects.requireNonNull;

public class Led {

    private final Color color;

    private boolean turnedOn;

    Led(final Color color) {
        this.color = requireNonNull(color);
    }

    public void turnOn() {
        turnedOn = true;
    }

    public void turnOff() {
        turnedOn = false;
    }

    @Override
    public String toString() {
        return  turnedOn ? color.toString() : "O";
    }
}
