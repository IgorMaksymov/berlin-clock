package org.maksymov.clock;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.maksymov.clock.Color.RED;
import static org.maksymov.clock.Color.YELLOW;

public class Clock {

    private final Led evenSecondMarker;

    private final List<Led> fiveHoursMarkerRow;

    private final List<Led> hoursMarkerRow;

    private final List<Led> fiveMinutesMarkerRow;

    private final List<Led> minutesMarkingRow;

    private Clock(final Led evenSecondMarker,
                 final List<Led> fiveHoursMarkerRow,
                 final List<Led> hoursMarkerRow,
                 final List<Led> fiveMinutesMarkerRow,
                 final List<Led> minutesMarkingRow) {
        this.evenSecondMarker     = requireNonNull(evenSecondMarker);
        this.fiveHoursMarkerRow   = requireNonNull(fiveHoursMarkerRow);
        this.hoursMarkerRow       = requireNonNull(hoursMarkerRow);
        this.fiveMinutesMarkerRow = requireNonNull(fiveMinutesMarkerRow);
        this.minutesMarkingRow    = requireNonNull(minutesMarkingRow);
    }

    public static Clock from(final String time) {
        final LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
        final Led evenMarker = createEvenSecondMarker(localTime.getSecond());
        final List<Led> fiveHoursMarkerRow = createFiveHoursMarkerRow(localTime.getHour());
        final List<Led> hoursMarkerRow = createHoursMarkerRow(localTime.getHour());
        final List<Led> fiveMinutesMarkerRow = createFiveMinutesMarkerRow(localTime.getMinute());
        final List<Led> minutesMarkerRow = createMinutesMarkingRowMarkerRow(localTime.getMinute());

        return new Clock(evenMarker, fiveHoursMarkerRow, hoursMarkerRow, fiveMinutesMarkerRow, minutesMarkerRow);
    }

    private static Led createEvenSecondMarker(final int seconds) {
        final Led evenMarker = new Led(YELLOW);
        if (seconds % 2 != 0) {
            evenMarker.turnOff();
        } else {
            evenMarker.turnOn();
        }
        return evenMarker;
    }

    private static List<Led> createFiveHoursMarkerRow(final int timeValue) {
        final List<Led> fiveHoursMarkerRow = createLedRowAndSwitchOnByTimeValue(RED, () -> timeValue / 5);
        return fiveHoursMarkerRow;
    }

    private static List<Led> createHoursMarkerRow(final int timeValue) {
        final List<Led> hoursMarkerRow = createLedRowAndSwitchOnByTimeValue(RED, () -> timeValue % 5);
        return hoursMarkerRow;
    }

    private static List<Led> createFiveMinutesMarkerRow(final int timeValue) {
        final int numberOfActiveLed = timeValue / 5;
        final byte sizeOfRow = 11;
        final Set<Integer> redPositions = IntStream.rangeClosed(1,59).filter(minute -> minute % 15 == 0).map(minute -> minute / 5).boxed().collect(Collectors.toSet());
        List<Led> fiveMinutesMarkerRow = new ArrayList<>(sizeOfRow);
        for (int i = 0; i < sizeOfRow; i++) {
            if (redPositions.contains(i+1)) {
                fiveMinutesMarkerRow.add(new Led(RED));
            } else {
                fiveMinutesMarkerRow.add(new Led(YELLOW));
            }
        }
        IntStream.range(0, numberOfActiveLed).forEach(i -> fiveMinutesMarkerRow.get(i).turnOn());
        return Collections.unmodifiableList(fiveMinutesMarkerRow);
    }

    private static List<Led> createMinutesMarkingRowMarkerRow(final int timeValue) {
        final List<Led> minutesMarkingRow = createLedRowAndSwitchOnByTimeValue(YELLOW, () -> timeValue % 5);
        return minutesMarkingRow;
    }

    private static List<Led> createLedRowAndSwitchOnByTimeValue(final Color color, IntSupplier amountToSwitch) {
        final int numberOfActiveLed = amountToSwitch.getAsInt();
        final long sizeOfRow = 4;
        final List<Led> ledRow = Collections.unmodifiableList(Stream.generate(() -> new Led(color)).limit(sizeOfRow).collect(Collectors.toList()));
        IntStream.range(0, numberOfActiveLed).forEach(i -> ledRow.get(i).turnOn());
        return ledRow;
    }


    @Override
    public String toString() {
       return evenSecondMarker.toString()                                                    + "\n" +
              fiveHoursMarkerRow.stream().map(Led::toString).collect(Collectors.joining())   + "\n" +
              hoursMarkerRow.stream().map(Led::toString).collect(Collectors.joining())       + "\n" +
              fiveMinutesMarkerRow.stream().map(Led::toString).collect(Collectors.joining()) + "\n" +
              minutesMarkingRow.stream().map(Led::toString).collect(Collectors.joining());
    }
}
