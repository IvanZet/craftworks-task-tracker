package net.ivanzykov.craftworkstasktracker;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @TestConfiguration
    static class TaskControllerTestWebLayerTestConfiguration {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    private final ZoneId timezoneOfDb = ZoneId.of("UTC");
    private final String createdAtCet = "2023-01-04 16:00 CET";
    private final String updatedAtCet = "2023-01-04 21:00 CET";
    private final String dueDateCet = "2023-02-01 00:00 CET";
    private final String resolvedAtCet = "2023-01-08 18:00 CET";
    private final String subPath = "/api/v1/tasks";

    @Test
    void fetchAll_returnsOneTaskWithCorrectFields() throws Exception {
        // Prepare args
        var task = new Task();

        OffsetDateTime createdAt = ZonedDateTime.of(2023, 1, 4, 15, 0, 0,
                0, timezoneOfDb).toOffsetDateTime();
        task.setCreatedAt(createdAt);

        OffsetDateTime updatedAt = ZonedDateTime.of(2023, 1, 4, 20, 0, 0,
                0, timezoneOfDb).toOffsetDateTime();
        task.setUpdatedAt(updatedAt);

        OffsetDateTime dueDate = ZonedDateTime.of(2023, 1, 31, 23, 0, 0,
                0, timezoneOfDb).toOffsetDateTime();
        task.setDueDate(dueDate);

        OffsetDateTime resolvedAt = ZonedDateTime.of(2023, 1, 8, 17, 0, 0,
                0, timezoneOfDb).toOffsetDateTime();
        task.setResolvedAt(resolvedAt);

        task.setTitle("t");
        task.setDescription("d");
        task.setPriority("hi");
        task.setStatus("done");

        // Stub task service
        when(taskService.fetchAll())
                .thenReturn(List.of(task));

        // Run this test
        mockMvc.perform(MockMvcRequestBuilders.get(subPath))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                // Expect time in client's CET timezone
                .andExpect(jsonPath("$[0].createdAt", is(createdAtCet)))
                .andExpect(jsonPath("$[0].updatedAt", is(updatedAtCet)))
                .andExpect(jsonPath("$[0].dueDate", is(dueDateCet)))
                .andExpect(jsonPath("$[0].resolvedAt", is(resolvedAtCet)))
                .andExpect(jsonPath("$[0].title", is(task.getTitle())))
                .andExpect(jsonPath("$[0].description", is(task.getDescription())))
                .andExpect(jsonPath("$[0].priority", is(task.getPriority())))
                .andExpect(jsonPath("$[0].status", is(task.getStatus())));
    }

    @Test
    void createSingle_returnCreatedTasksWithCorrectFields() throws Exception {
        // Prepare args
        var taskDto = new TaskDto();
        taskDto.setCreatedAt(createdAtCet);
        taskDto.setUpdatedAt(updatedAtCet);
        taskDto.setDueDate(dueDateCet);
        taskDto.setResolvedAt(resolvedAtCet);
        taskDto.setTitle("t");
        taskDto.setDescription("d");
        taskDto.setPriority("md");
        taskDto.setStatus("work");

        var task = new Task();
        task.setCreatedAt(taskDto.getCreatedAtConverted(timezoneOfDb));
        task.setUpdatedAt(taskDto.getUpdatedAtConverted(timezoneOfDb));
        task.setDueDate(taskDto.getDueDateConverted(timezoneOfDb));
        task.setResolvedAt(taskDto.getResolvedAtConverted(timezoneOfDb));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());

        // Stub task service
        when(taskService.createSingle(any())).thenReturn(task);

        // Run this test
        mockMvc.perform(MockMvcRequestBuilders
                        .post(subPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(taskDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdAt", is(taskDto.getCreatedAt())))
                .andExpect(jsonPath("$.updatedAt", is(taskDto.getUpdatedAt())))
                .andExpect(jsonPath("$.dueDate", is(taskDto.getDueDate())))
                .andExpect(jsonPath("$.resolvedAt", is(taskDto.getResolvedAt())))
                .andExpect(jsonPath("$.title", is(taskDto.getTitle())))
                .andExpect(jsonPath("$.description", is(taskDto.getDescription())))
                .andExpect(jsonPath("$.priority", is(taskDto.getPriority())))
                .andExpect(jsonPath("$.status", is(taskDto.getStatus())));
    }

    // Marshal request body
    public static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            fail();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
