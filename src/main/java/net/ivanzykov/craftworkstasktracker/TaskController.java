package net.ivanzykov.craftworkstasktracker;

import net.ivanzykov.craftworkstasktracker.exceptions.TaskDateTimeParseException;
import net.ivanzykov.craftworkstasktracker.exceptions.TaskFieldsMissingException;
import net.ivanzykov.craftworkstasktracker.exceptions.TaskNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskService taskService;

    @Value("${spring.jpa.properties.hibernate.jdbc.time_zone}")
    private String timezoneOfDb;

    @GetMapping
    List<TaskDto> fetchAll() {
        List<Task> tasks = taskService.fetchAll();
        return tasks.stream()
                .map(this::mapToDto)
                .toList();
    }

    @GetMapping("{id}")
    TaskDto fetchSingle (@PathVariable Long id) {
        Task task = taskService.fetchSingle(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return mapToDto(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TaskDto createSingle(@RequestBody final TaskDto taskDto) {
        Task task;
        try {
            task = mapToEntity(taskDto);
        } catch (DateTimeParseException ex) {
            throw new TaskDateTimeParseException(ex.getMessage(), TaskDto.getDateTimeFormatter());
        }
        Task taskCreated;
        try {
            taskCreated = taskService.createSingle(task);
        } catch (ConstraintViolationException ex) {
            throw new TaskFieldsMissingException(ex.getMessage());
        }
        return mapToDto(taskCreated);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskDto updateSingle(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task oldTask = taskService.fetchSingle(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        Task newTask;
        try {
            newTask = mapToEntity(taskDto);
        } catch (DateTimeParseException ex) {
            throw new TaskDateTimeParseException(ex.getMessage(), TaskDto.getDateTimeFormatter());
        }
        Task taskUpdated = taskService.updateSingle(oldTask, newTask);
        return mapToDto(taskUpdated);    }

    @DeleteMapping("{id}")
    void deleteSingle(@PathVariable Long id) {
        Task task = taskService.fetchSingle(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskService.delete(task);
    }

    @DeleteMapping
    void deleteAll() {
        taskService.deleteAll();
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        // TODO: try getting time zone from the request
        taskDto.setAllDatesConverted(task, ZoneId.of("CET"));
        return taskDto;
    }

    private Task mapToEntity(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        ZoneId zoneIdOfDb = ZoneId.of(timezoneOfDb);
        // TODO: move setting individual fields to entity or even DTO?
        task.setCreatedAt(taskDto.getCreatedAtConverted(zoneIdOfDb));
        task.setUpdatedAt(taskDto.getUpdatedAtConverted(zoneIdOfDb));
        task.setDueDate(taskDto.getDueDateConverted(zoneIdOfDb));
        task.setResolvedAt(taskDto.getResolvedAtConverted(zoneIdOfDb));
        return task;
    }
}
