package org.maksymov.clock;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class ClockTest {

    @ParameterizedTest
    @MethodSource("inputProvider")
    void testToString(final String input, final String expectedValue) {
        final Clock clock = Clock.from(input);
        final String actualValue = clock.toString();

        assertThat(actualValue, is(expectedValue));
    }

    private static Stream<Arguments> inputProvider() {
        return Stream.of(
                Arguments.of("00:00:00", "Y\n" +
                                         "OOOO\n" +
                                         "OOOO\n" +
                                         "OOOOOOOOOOO\n" +
                                         "OOOO"),

                Arguments.of("13:17:01", "O\n" +
                                         "RROO\n" +
                                         "RRRO\n" +
                                         "YYROOOOOOOO\n" +
                                         "YYOO"),
                Arguments.of("23:59:59", "O\n" +
                                         "RRRR\n" +
                                         "RRRO\n" +
                                         "YYRYYRYYRYY\n" +
                                         "YYYY")
        );
    }
}