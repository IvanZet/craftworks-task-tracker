package net.ivanzykov.craftworkstasktracker;

import net.ivanzykov.craftworkstasktracker.controllers.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SchedulerOfTasks {

    private static final Logger log = LoggerFactory.getLogger(SchedulerOfTasks.class);

    @Autowired
    TaskService taskService;

    @Value("${spring.jpa.properties.hibernate.jdbc.time_zone}")
    private String timezoneOfDb;

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    @Transactional
    public void scheduleCreatingTask() {
        Task task = new Task();

        ZonedDateTime nowInDbTimeZone = ZonedDateTime.now()
                .withZoneSameInstant(ZoneId.of(timezoneOfDb));
        OffsetDateTime dueDate = nowInDbTimeZone.plusMonths(1L).toOffsetDateTime();
        task.setDueDate(dueDate);

        task.setTitle("Another auto-generated task");
        task.setPriority("md");
        task.setStatus("wait");

        Task taskCreated = taskService.createSingle(task);

        log.info("Task auto-generated with ID: {}", taskCreated.getId());
    }
}
