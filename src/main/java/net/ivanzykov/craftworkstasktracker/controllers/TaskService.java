package net.ivanzykov.craftworkstasktracker.controllers;

import net.ivanzykov.craftworkstasktracker.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> fetchAll();

    Optional<Task> fetchSingle(Long id);

    Task createSingle(Task task);

    Task updateSingle(Task oldTask, Task updatedTask);

    void delete(Task task);

    void deleteAll();
}
