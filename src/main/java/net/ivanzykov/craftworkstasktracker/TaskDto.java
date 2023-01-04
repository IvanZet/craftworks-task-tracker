package net.ivanzykov.craftworkstasktracker;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents Task entity for external clients and converts time to client's time zone.
 */
public class TaskDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String dueDate;
    private String resolvedAt;
    private String title;
    private String description;
    private String priority;
    private String status;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zz");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd zz");
    private ZoneId timezone;

    public TaskDto() {
        // Required by Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts all time-related fields to the time zone of the client.
     *
     * @param createdAt     offsetDateTime object when the task was created
     * @param updatedAt     offsetDateTime object when the task was last updated
     * @param dueDate       offsetDateTime object when the task is expected to be done
     * @param resolvedAt    offsetDateTime object when the task was done
     * @param timezone      zoneId object of the client's timezone
     */
    public void setAllDatesToClientsTimeZone(OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime dueDate,
                                             OffsetDateTime resolvedAt, ZoneId timezone) {
        this.timezone = timezone;
        setCreatedAtToClientsTimeZone(createdAt);
        setUpdatedAtToClientsTimeZone(updatedAt);
        setDueDateToClientsTimeZone(dueDate);
        setResolvedAtToClientsTimeZone(resolvedAt);
    }

    private void setCreatedAtToClientsTimeZone(OffsetDateTime createdAt) {
        this.createdAt = createdAt.atZoneSameInstant(timezone)
                .format(dateTimeFormatter);
    }

    private void setUpdatedAtToClientsTimeZone(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt.atZoneSameInstant(timezone)
                .format(dateTimeFormatter);
    }

    private void setDueDateToClientsTimeZone(OffsetDateTime dueDate) {
        this.dueDate = dueDate.atZoneSameInstant(timezone)
                .format(dateFormatter);
    }

    private void setResolvedAtToClientsTimeZone(OffsetDateTime resolvedAt) {
        // resolvedAt allowed to be null
        if (resolvedAt != null) {
            this.resolvedAt = resolvedAt.atZoneSameInstant(timezone)
                    .format(dateFormatter);
        }
    }

    // TODO: add methods returning dates converted to DB's time zone
}
