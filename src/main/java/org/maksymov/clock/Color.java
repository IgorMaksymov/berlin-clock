package org.maksymov.clock;

public enum Color {

    YELLOW("Y"),
    RED("R");

    private final String stringValue;

    Color(final String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
