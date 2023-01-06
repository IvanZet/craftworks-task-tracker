package net.ivanzykov.craftworkstasktracker;

import java.util.List;
import java.util.Optional;

interface TaskService {

    List<Task> fetchAll();

    Optional<Task> fetchSingle(Long id);

    Task createSingle(Task task);

    Task updateSingle(Task oldTask, Task updatedTask);

    void delete(Task task);
}
