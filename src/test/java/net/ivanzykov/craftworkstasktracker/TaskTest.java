package net.ivanzykov.craftworkstasktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();

        task.setId(1L);

        OffsetDateTime now = ZonedDateTime.now().toOffsetDateTime();
        task.setDueDate(now);
        task.setResolvedAt(now);

        task.setTitle("testTitle");
        task.setDescription("testDescr");
        task.setPriority("lo");
        task.setStatus("done");
    }

    @Test
    void saveOneTask_getTheSameOne() {
        taskRepository.saveAndFlush(task);

        Optional<Task> savedOptional = taskRepository.findById(task.getId());
        assertTrue(savedOptional.isPresent());
        Task savedTask = savedOptional.get();

        assertAll(
                () -> assertEquals(task.getId(), savedTask.getId()),
                () -> assertNotNull(savedTask.getCreatedAt()),
                () -> assertNotNull(savedTask.getUpdatedAt()),
                () -> assertEquals(task.getDueDate(), savedTask.getDueDate()),
                () -> assertEquals(task.getResolvedAt(), savedTask.getResolvedAt()),
                () -> assertEquals(task.getTitle(), savedTask.getTitle()),
                () -> assertEquals(task.getDescription(), savedTask.getDescription()),
                () -> assertEquals(task.getPriority(), savedTask.getPriority()),
                () -> assertEquals(task.getStatus(), savedTask.getStatus())
        );
    }
}
