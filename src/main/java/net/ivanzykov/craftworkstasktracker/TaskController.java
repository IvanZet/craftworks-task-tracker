package net.ivanzykov.craftworkstasktracker;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    List<TaskDto> fetchAll() {
        List<Task> tasks = taskService.fetchAll();
        return tasks.stream()
                .map(this::mapToDto)
                .toList();
    }

    @GetMapping("{id}")
    // FIXME: use DTO
    Task fetchSingle (@PathVariable Long id) {
        return taskService.fetchSingle(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // FIXME: use DTO for task arg
    @PostMapping
    Task createSingle(@RequestBody final Task task) {
        // TODO: check that required fields passed, otherwise return a 400 bad payload
        return taskService.createSingle(task);
    }

    // FIXME: use DTO for task arg
    @PutMapping("{id}")
    Task updateSingle(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateSingle(id, updatedTask);
    }

    @DeleteMapping("{id}")
    void deleteSingle(@PathVariable Long id) {
        // TODO: handle when task not found
        taskService.deleteSingle(id);
    }

    private TaskDto mapToDto(Task task) {
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        taskDto.setAllDatesToClientsTimeZone(
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                task.getResolvedAt(),
                // TODO: try getting time zone from the request
                ZoneId.of("CET"));
        return taskDto;
    }
}
