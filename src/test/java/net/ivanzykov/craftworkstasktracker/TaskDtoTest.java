package net.ivanzykov.craftworkstasktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    private final ModelMapper modelMapper = new ModelMapper();

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

    @Test
    void mapTaskEntityToTaskDto_allFieldsOfSameContent() {
        // Prepare args
        Task task = new Task();
        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());
        task.setDueDate(OffsetDateTime.now());
        task.setResolvedAt(OffsetDateTime.now());
        task.setTitle("Test title");
        task.setDescription("Test description");
        task.setPriority("md");
        task.setStatus("work");

        // Run this test
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);

        assertAll(
                () -> assertEquals(task.getCreatedAt().toString(), taskDto.getCreatedAt()),
                () -> assertEquals(task.getUpdatedAt().toString(), taskDto.getUpdatedAt()),
                () -> assertEquals(task.getDueDate().toString(), taskDto.getDueDate()),
                () -> assertEquals(task.getResolvedAt().toString(), taskDto.getResolvedAt()),
                () -> assertEquals(task.getTitle(), taskDto.getTitle()),
                () -> assertEquals(task.getDescription(), taskDto.getDescription()),
                () -> assertEquals(task.getPriority(), taskDto.getPriority()),
                () -> assertEquals(task.getStatus(), taskDto.getStatus())
        );
    }

    @Test
    void mapTaskDtoToTaskEntity_nonDateFieldsOfSameContent() {
        // Prepare args
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Test title");
        taskDto.setDescription("Test description");
        taskDto.setPriority("lo");
        taskDto.setPriority("wait");

        // Run this test
        Task task = modelMapper.map(taskDto, Task.class);

        assertAll(
                () -> assertEquals(taskDto.getTitle(), task.getTitle()),
                () -> assertEquals(taskDto.getDescription(), task.getDescription()),
                () -> assertEquals(taskDto.getPriority(), task.getPriority()),
                () -> assertEquals(taskDto.getStatus(), task.getStatus())
        );
    }
}
