package net.ivanzykov.craftworkstasktracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        Sort sorting = Sort.by("id").ascending();
        return taskRepository.findAll(sorting);
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
    public Task updateSingle(Task oldTask, Task updatedTask) {
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
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    private Task saveAndFlush(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
