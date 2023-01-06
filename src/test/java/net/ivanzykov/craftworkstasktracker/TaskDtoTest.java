package net.ivanzykov.craftworkstasktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    private TaskDto taskDto;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zz");

    private static final String dateTimeOfClientString = "2023-01-04 16:00 CET";
    private static final Instant dateTimeOfClientInstant = ZonedDateTime.parse(dateTimeOfClientString, dateTimeFormatter)
            .toInstant();
    private static final ZoneId timezoneOfDb = ZoneId.of("UTC");
    private static final ZoneOffset offsetInDbTimezone = timezoneOfDb.getRules().getOffset(dateTimeOfClientInstant);

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
    }

    @Test
    void convertAndSetAllDatesInDto_datesConvertedCorrectly() {
        // Prepare args
        OffsetDateTime createdAt = OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                0, offsetInDbTimezone);
        OffsetDateTime updatedAt = OffsetDateTime.of(2023, 1, 4, 20, 0, 0,
                0, offsetInDbTimezone);
        OffsetDateTime dueDate = OffsetDateTime.of(2023, 1, 31, 23, 0, 0,
                0, offsetInDbTimezone);
        OffsetDateTime resolvedAt = OffsetDateTime.of(2023, 1, 8, 17, 0, 0,
                0, offsetInDbTimezone);

        // Run this test
        ZoneId timezoneOfClient = ZoneId.of("CET");
        taskDto.setAllDatesConverted(createdAt, updatedAt, dueDate, resolvedAt, timezoneOfClient);

        assertAll(
                () -> assertEquals("2023-01-04 16:00 CET", taskDto.getCreatedAt()),
                () -> assertEquals("2023-01-04 21:00 CET", taskDto.getUpdatedAt()),
                () -> assertEquals("2023-02-01 00:00 CET", taskDto.getDueDate()),
                () -> assertEquals("2023-01-08 18:00 CET", taskDto.getResolvedAt())
        );
    }

    @Test
    void getCreatedAtConvertedFromDto_conversionOk() {
        // Prepare SUT
        taskDto.setCreatedAt(dateTimeOfClientString);

        // Run this test
        OffsetDateTime expected = OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                0, offsetInDbTimezone);

        assertEquals(expected, taskDto.getCreatedAtConverted(timezoneOfDb));
    }

    @Test
    void getUpdatedAtConvertedFromDto_conversionOk() {
        // Prepare SUT
        taskDto.setUpdatedAt(dateTimeOfClientString);

        // Run this test
        OffsetDateTime expected = OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                0, offsetInDbTimezone);

        assertEquals(expected, taskDto.getUpdatedAtConverted(timezoneOfDb));
    }

    @Test
    void getDueDateConvertedFromDto_conversionOk() {
        // Prepare SUT
        taskDto.setDueDate(dateTimeOfClientString);

        // Run this test
        OffsetDateTime expected = OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                0, offsetInDbTimezone);

        assertEquals(expected, taskDto.getDueDateConverted(timezoneOfDb));
    }

    @ParameterizedTest
    @MethodSource("provideGetResolvedAtConverted")
    void getResolvedAtConvertedFromDto_conversionOk(String dateTime, OffsetDateTime expected) {
        // Prepare SUT
        taskDto.setResolvedAt(dateTime);

        assertEquals(expected, taskDto.getResolvedAtConverted(timezoneOfDb));
    }

    public static Stream<Arguments> provideGetResolvedAtConverted() {
        return Stream.of(
                Arguments.of(dateTimeOfClientString, OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                        0, offsetInDbTimezone)),
                Arguments.of(null, null)
        );
    }
}
