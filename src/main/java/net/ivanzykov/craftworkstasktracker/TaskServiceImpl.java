package net.ivanzykov.craftworkstasktracker;

import net.ivanzykov.craftworkstasktracker.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> fetchAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> fetchSingle(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createSingle(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public Task updateSingle(Long id, Task updatedTask) {
        // TODO: rework updating fields via DTO
        // TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Task oldTask = fetchSingle(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        oldTask.setUpdatedAt(updatedTask.getUpdatedAt());
        if (updatedTask.getDueDate() != null) {
            oldTask.setDueDate(updatedTask.getDueDate());
        }
        oldTask.setResolvedAt(updatedTask.getResolvedAt());
        if (updatedTask.getTitle() != null) {
            oldTask.setTitle(updatedTask.getTitle());
        }
        oldTask.setDescription(updatedTask.getDescription());
        if (updatedTask.getPriority() != null) {
            oldTask.setPriority(updatedTask.getPriority());
        }
        if (updatedTask.getStatus() != null) {
            oldTask.setPriority(updatedTask.getPriority());
        }

        return saveAndFlush(oldTask);
    }

    @Override
    public void deleteSingle(Long id) {
        taskRepository.deleteById(id);
    }

    private Task saveAndFlush(Task task) {
        return taskRepository.saveAndFlush(task);
    }
}
