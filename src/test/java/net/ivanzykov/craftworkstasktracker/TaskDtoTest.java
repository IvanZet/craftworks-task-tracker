package net.ivanzykov.craftworkstasktracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    @Test
    void setAllDatesToClientsTimeZone() {

        // Prepare args
        ZoneId timezoneOfDb = ZoneId.of("UTC");
        ZoneOffset zoneOffset = timezoneOfDb.getRules().getOffset(LocalDateTime.now());

        OffsetDateTime createdAt = OffsetDateTime.of(2023, 1, 4, 15, 0, 0,
                0, zoneOffset);

        OffsetDateTime updatedAt = OffsetDateTime.of(2023, 1, 4, 20, 0, 0,
                0, zoneOffset);

        OffsetDateTime dueDate = OffsetDateTime.of(2023, 1, 31, 23, 0, 0,
                0, zoneOffset);

        OffsetDateTime resolvedAt = OffsetDateTime.of(2023, 1, 8, 17, 0, 0,
                0, zoneOffset);

        // Instantiate SUT
        TaskDto taskDto = new TaskDto();

        // Run this test
        ZoneId timezoneOfClient = ZoneId.of("CET");
        taskDto.setAllDatesToClientsTimeZone(createdAt, updatedAt, dueDate, resolvedAt, timezoneOfClient);

        assertAll(
                () -> assertEquals("2023-01-04 16:00 CET", taskDto.getCreatedAt()),
                () -> assertEquals("2023-01-04 21:00 CET", taskDto.getUpdatedAt()),
                () -> assertEquals("2023-02-01 CET", taskDto.getDueDate()),
                () -> assertEquals("2023-01-08 CET", taskDto.getResolvedAt())
        );
    }
}
