package net.ivanzykov.craftworkstasktracker;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void fetchAll_returnsOneTaskWithCorrectFields() throws Exception{
        // Prepare args
        var task = new Task();

        ZoneId timezoneOfDb = ZoneId.of("UTC");

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
        final String subPath = "/api/v1/tasks";
        mockMvc.perform(get(subPath))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                // Expect time in client's CET timezone
                .andExpect(jsonPath("$[0].createdAt", is("2023-01-04 16:00 CET")))
                .andExpect(jsonPath("$[0].updatedAt", is("2023-01-04 21:00 CET")))
                .andExpect(jsonPath("$[0].dueDate", is("2023-02-01 00:00 CET")))
                .andExpect(jsonPath("$[0].resolvedAt", is("2023-01-08 18:00 CET")))
                .andExpect(jsonPath("$[0].title", is(task.getTitle())))
                .andExpect(jsonPath("$[0].description", is(task.getDescription())))
                .andExpect(jsonPath("$[0].priority", is(task.getPriority())))
                .andExpect(jsonPath("$[0].status", is(task.getStatus())));
    }
}
