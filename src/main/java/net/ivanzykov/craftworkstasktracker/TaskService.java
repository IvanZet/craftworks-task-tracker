package net.ivanzykov.craftworkstasktracker;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

interface TaskService {

    List<Task> fetchAll();

    Optional<Task> fetchSingle(Long id);

    Task createSingle(Task task);

    Task updateSingle(Long id, Task updatedTask);

    void deleteSingle(Long id);
}
