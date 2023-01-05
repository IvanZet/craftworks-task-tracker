package net.ivanzykov.craftworkstasktracker;

import org.modelmapper.ModelMapper;
import net.ivanzykov.craftworkstasktracker.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
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
        // TODO: check that required fields passed, otherwise return a 400 bad payload
        Task task = mapToEntity(taskDto);
        Task taskCreated = taskService.createSingle(task);
        return mapToDto(taskCreated);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskDto updateSingle(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task task = mapToEntity(taskDto);
        Task taskUpdated = taskService.updateSingle(id, task);
        return mapToDto(taskUpdated);
    }

    @DeleteMapping("{id}")
    void deleteSingle(@PathVariable Long id) {
        // TODO: handle when task not found
        taskService.deleteSingle(id);
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setAllDatesConverted(
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                task.getResolvedAt(),
                // TODO: try getting time zone from the request
                ZoneId.of("CET"));
        return taskDto;
    }

    private Task mapToEntity(TaskDto taskDto) {
        Task task = modelMapper.map(taskDto, Task.class);
        ZoneId zoneIdOfDb = ZoneId.of(timezoneOfDb);
        task.setCreatedAt(taskDto.getCreatedAtConverted(zoneIdOfDb));
        task.setUpdatedAt(taskDto.getUpdatedAtConverted(zoneIdOfDb));
        task.setDueDate(taskDto.getDueDateConverted(zoneIdOfDb));
        task.setResolvedAt(taskDto.getResolvedAtConverted(zoneIdOfDb));
        return task;
    }
}
