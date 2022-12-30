package net.ivanzykov.craftworkstasktracker;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    List<Task> fetchAll() {
        return taskRepository.findAll();
    }

    @GetMapping("{id}")
    Task fetchSingle (@PathVariable Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    // FIXME: use DTO for task arg
    @PostMapping
    Task createSingle(@RequestBody final Task task) {
        return taskRepository.saveAndFlush(task);
    }

    // FIXME: use DTO for task arg
    @PutMapping("{id}")
    Task updateSingle(@PathVariable Long id, @RequestBody Task updatedTask) {
        // TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Task oldTask = taskRepository.getReferenceById(id);
        BeanUtils.copyProperties(updatedTask, oldTask, "id");
        return taskRepository.saveAndFlush(oldTask);
    }

    @DeleteMapping("{id}")
    void deleteSingle(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}
